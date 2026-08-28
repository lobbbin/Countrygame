const express = require('express');
const { getDb } = require('../db');
const { requireAuth, requireAdmin } = require('../middleware/auth');

const router = express.Router();

router.use(requireAuth);

router.post('/', (req, res, next) => {
  try {
    const { shippingAddress, paymentMethod = 'card' } = req.body;
    if (!shippingAddress) return res.status(400).json({ error: 'shippingAddress is required' });
    const db = getDb();
    const runCheckout = () => {
      db.exec('BEGIN');
      try {
        const cart = db.prepare('SELECT id FROM carts WHERE user_id = ?').get(req.user.id);
        if (!cart) throw Object.assign(new Error('Cart is empty'), { status: 400 });
        const items = db.prepare(
          `SELECT ci.id as item_id, ci.product_id, ci.quantity, ci.price, p.name, p.stock
           FROM cart_items ci
           JOIN products p ON p.id = ci.product_id
           WHERE ci.cart_id = ?`
        ).all(cart.id);
        if (items.length === 0) throw Object.assign(new Error('Cart is empty'), { status: 400 });

        let total = 0;
        for (const it of items) {
          if (it.quantity > it.stock) {
            throw Object.assign(new Error(`${it.name} does not have enough stock`), { status: 400 });
          }
          total += it.price * it.quantity;
        }

        const info = db.prepare(
          'INSERT INTO orders (user_id, total, shipping_address, payment_method) VALUES (?, ?, ?, ?)'
        ).run(req.user.id, total.toFixed(2), shippingAddress, paymentMethod);
        const orderId = info.lastInsertRowid;

        const insertItem = db.prepare(
          'INSERT INTO order_items (order_id, product_id, name, quantity, price) VALUES (?, ?, ?, ?, ?)'
        );
        for (const it of items) {
          insertItem.run(orderId, it.product_id, it.name, it.quantity, it.price);
          db.prepare('UPDATE products SET stock = stock - ? WHERE id = ?').run(it.quantity, it.product_id);
        }
        db.prepare('DELETE FROM cart_items WHERE cart_id = ?').run(cart.id);
        db.exec('COMMIT');
        return orderId;
      } catch (e) {
        db.exec('ROLLBACK');
        throw e;
      }
    };

    const orderId = runCheckout();
    const order = db.prepare(
      'SELECT id, user_id, total, status, shipping_address, payment_method, created_at, updated_at FROM orders WHERE id = ?'
    ).get(orderId);
    res.status(201).json(order);
  } catch (err) {
    next(err);
  }
});

router.get('/', (req, res, next) => {
  try {
    const db = getDb();
    let orders;
    if (req.user.role === 'admin') {
      orders = db.prepare(
        'SELECT id, user_id, total, status, shipping_address, payment_method, created_at, updated_at FROM orders ORDER BY id DESC'
      ).all();
    } else {
      orders = db.prepare(
        'SELECT id, user_id, total, status, shipping_address, payment_method, created_at, updated_at FROM orders WHERE user_id = ? ORDER BY id DESC'
      ).all(req.user.id);
    }
    res.json(orders);
  } catch (err) {
    next(err);
  }
});

router.get('/:id', (req, res, next) => {
  try {
    const db = getDb();
    const order = db.prepare(
      'SELECT id, user_id, total, status, shipping_address, payment_method, created_at, updated_at FROM orders WHERE id = ?'
    ).get(req.params.id);
    if (!order) return res.status(404).json({ error: 'Order not found' });
    if (req.user.role !== 'admin' && order.user_id !== req.user.id) {
      return res.status(403).json({ error: 'Not authorized to view this order' });
    }
    order.items = db.prepare(
      'SELECT id, order_id, product_id, name, quantity, price FROM order_items WHERE order_id = ?'
    ).all(req.params.id);
    res.json(order);
  } catch (err) {
    next(err);
  }
});

router.put('/:id/status', requireAdmin, (req, res, next) => {
  try {
    const { status } = req.body;
    const valid = ['pending', 'processing', 'shipped', 'delivered', 'cancelled'];
    if (!valid.includes(status)) return res.status(400).json({ error: 'Invalid status' });
    const db = getDb();
    const info = db.prepare('UPDATE orders SET status = ?, updated_at = datetime(\'now\') WHERE id = ?').run(status, req.params.id);
    if (info.changes === 0) return res.status(404).json({ error: 'Order not found' });
    const order = db.prepare(
      'SELECT id, user_id, total, status, shipping_address, payment_method, created_at, updated_at FROM orders WHERE id = ?'
    ).get(req.params.id);
    res.json(order);
  } catch (err) {
    next(err);
  }
});

module.exports = router;
