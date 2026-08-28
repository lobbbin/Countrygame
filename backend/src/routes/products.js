const express = require('express');
const { getDb } = require('../db');
const { requireAdmin } = require('../middleware/auth');

const router = express.Router();

router.get('/', (req, res, next) => {
  try {
    const db = getDb();
    const { search, category, limit = 50, offset = 0 } = req.query;
    let sql = 'SELECT * FROM products WHERE 1=1';
    const params = [];
    if (search) {
      sql += ' AND (name LIKE ? OR description LIKE ? OR category LIKE ?)';
      const like = `%${search}%`;
      params.push(like, like, like);
    }
    if (category) {
      sql += ' AND category = ?';
      params.push(category);
    }
    sql += ' ORDER BY id DESC LIMIT ? OFFSET ?';
    params.push(Number(limit), Number(offset));
    const products = db.prepare(sql).all(...params);
    res.json(products);
  } catch (err) {
    next(err);
  }
});

router.get('/categories', (req, res, next) => {
  try {
    const db = getDb();
    const rows = db.prepare('SELECT DISTINCT category FROM products WHERE category IS NOT NULL ORDER BY category').all();
    res.json(rows.map(r => r.category));
  } catch (err) {
    next(err);
  }
});

router.get('/:id', (req, res, next) => {
  try {
    const db = getDb();
    const product = db.prepare('SELECT * FROM products WHERE id = ?').get(req.params.id);
    if (!product) return res.status(404).json({ error: 'Product not found' });
    res.json(product);
  } catch (err) {
    next(err);
  }
});

router.post('/', requireAdmin, (req, res, next) => {
  try {
    const { name, description, price, image, category, stock } = req.body;
    if (!name || price == null || isNaN(price)) {
      return res.status(400).json({ error: 'name and a valid price are required' });
    }
    const db = getDb();
    const info = db.prepare(
      'INSERT INTO products (name, description, price, image, category, stock) VALUES (?, ?, ?, ?, ?, ?)'
    ).run(name, description || null, Number(price), image || null, category || null, Number(stock) || 0);
    const product = db.prepare('SELECT * FROM products WHERE id = ?').get(info.lastInsertRowid);
    res.status(201).json(product);
  } catch (err) {
    next(err);
  }
});

router.put('/:id', requireAdmin, (req, res, next) => {
  try {
    const db = getDb();
    const existing = db.prepare('SELECT * FROM products WHERE id = ?').get(req.params.id);
    if (!existing) return res.status(404).json({ error: 'Product not found' });
    const { name, description, price, image, category, stock } = req.body;
    db.prepare(
      'UPDATE products SET name = ?, description = ?, price = ?, image = ?, category = ?, stock = ? WHERE id = ?'
    ).run(
      name != null ? name : existing.name,
      description != null ? description : existing.description,
      price != null ? Number(price) : existing.price,
      image != null ? image : existing.image,
      category != null ? category : existing.category,
      stock != null ? Number(stock) : existing.stock,
      req.params.id
    );
    const product = db.prepare('SELECT * FROM products WHERE id = ?').get(req.params.id);
    res.json(product);
  } catch (err) {
    next(err);
  }
});

router.delete('/:id', requireAdmin, (req, res, next) => {
  try {
    const db = getDb();
    const info = db.prepare('DELETE FROM products WHERE id = ?').run(req.params.id);
    if (info.changes === 0) return res.status(404).json({ error: 'Product not found' });
    res.status(204).end();
  } catch (err) {
    next(err);
  }
});

module.exports = router;
