const express = require('express');
const { getDb } = require('../db');
const { requireAuth } = require('../middleware/auth');

const router = express.Router();

router.use(requireAuth);

function getCart(db, userId) {
  let cart = db.prepare('SELECT id FROM carts WHERE user_id = ?').get(userId);
  if (!cart) {
    const info = db.prepare('INSERT INTO carts (user_id) VALUES (?)').run(userId);
    cart = { id: info.lastInsertRowid };
  }
  return cart;
}

router.get('/', (req, res, next) => {
  try {
    const db = getDb();
    const cart = getCart(db, req.user.id);
    const items = db.prepare(
      `SELECT ci.id, ci.quantity, ci.price,
              p.id as product_id, p.name, p.image, p.price as product_price, p.stock
       FROM cart_items ci
       JOIN products p ON p.id = ci.product_id
       WHERE ci.cart_id = ?`
    ).all(cart.id);
    res.json({ cartId: cart.id, items });
  } catch (err) {
    next(err);
  }
});

router.post('/items', (req, res, next) => {
  try {
    const { productId, quantity = 1 } = req.body;
    if (!productId) return res.status(400).json({ error: 'productId is required' });
    const db = getDb();
    const product = db.prepare('SELECT id, price, stock FROM products WHERE id = ?').get(productId);
    if (!product) return res.status(404).json({ error: 'Product not found' });
    const cart = getCart(db, req.user.id);
    const existing = db.prepare('SELECT id, quantity FROM cart_items WHERE cart_id = ? AND product_id = ?').get(cart.id, productId);
    if (existing) {
      const newQty = existing.quantity + Number(quantity);
      db.prepare('UPDATE cart_items SET quantity = ? WHERE id = ?').run(Math.min(newQty, product.stock), existing.id);
    } else {
      db.prepare('INSERT INTO cart_items (cart_id, product_id, quantity, price) VALUES (?, ?, ?, ?)')
        .run(cart.id, productId, Math.min(Number(quantity), product.stock), product.price);
    }
    res.status(201).json({ success: true });
  } catch (err) {
    next(err);
  }
});

router.put('/items/:id', (req, res, next) => {
  try {
    const { quantity } = req.body;
    if (!quantity || quantity < 1) return res.status(400).json({ error: 'quantity must be >= 1' });
    const db = getDb();
    const cart = getCart(db, req.user.id);
    const item = db.prepare('SELECT id FROM cart_items WHERE id = ? AND cart_id = ?').get(req.params.id, cart.id);
    if (!item) return res.status(404).json({ error: 'Cart item not found' });
    db.prepare('UPDATE cart_items SET quantity = ? WHERE id = ?').run(Number(quantity), req.params.id);
    res.json({ success: true });
  } catch (err) {
    next(err);
  }
});

router.delete('/items/:id', (req, res, next) => {
  try {
    const db = getDb();
    const cart = getCart(db, req.user.id);
    const info = db.prepare('DELETE FROM cart_items WHERE id = ? AND cart_id = ?').run(req.params.id, cart.id);
    if (info.changes === 0) return res.status(404).json({ error: 'Cart item not found' });
    res.status(204).end();
  } catch (err) {
    next(err);
  }
});

router.delete('/', (req, res, next) => {
  try {
    const db = getDb();
    const cart = getCart(db, req.user.id);
    db.prepare('DELETE FROM cart_items WHERE cart_id = ?').run(cart.id);
    res.json({ success: true });
  } catch (err) {
    next(err);
  }
});

module.exports = router;
