const express = require('express');
const { getDb } = require('../db');
const { requireAdmin } = require('../middleware/auth');

const router = express.Router();

router.get('/stats', requireAdmin, (req, res, next) => {
  try {
    const db = getDb();
    const productCount = db.prepare('SELECT COUNT(*) as c FROM products').get().c;
    const userCount = db.prepare("SELECT COUNT(*) as c FROM users WHERE role = 'customer'").get().c;
    const orderCount = db.prepare('SELECT COUNT(*) as c FROM orders').get().c;
    const revenue = db.prepare("SELECT COALESCE(SUM(total),0) as total FROM orders WHERE status != 'cancelled'").get().total;
    const lowStock = db.prepare('SELECT COUNT(*) as c FROM products WHERE stock < 5').get().c;
    res.json({
      products: productCount,
      customers: userCount,
      orders: orderCount,
      revenue: Number(revenue),
      lowStock
    });
  } catch (err) {
    next(err);
  }
});

module.exports = router;
