const path = require('path');
const bcrypt = require('bcryptjs');
const { getDb, initDb } = require('./db');

const products = [
  { name: 'Wireless Headphones', description: 'Noise-cancelling over-ear Bluetooth headphones with 30h battery life.', price: 199.99, image: 'https://picsum.photos/seed/headphones/400/400', category: 'Audio', stock: 42 },
  { name: 'Mechanical Keyboard', description: 'RGB mechanical gaming keyboard with cherry MX brown switches.', price: 139.5, image: 'https://picsum.photos/seed/keyboard/400/400', category: 'Computer', stock: 28 },
  { name: '4K Monitor', description: '27-inch Ultra HD (3840x2160) IPS monitor with HDR10.', price: 399.0, image: 'https://picsum.photos/seed/monitor/400/400', category: 'Computer', stock: 9 },
  { name: 'Smart Watch', description: 'Fitness tracker with heart rate, GPS and AMOLED display.', price: 249.99, image: 'https://picsum.photos/seed/watch/400/400', category: 'Wearables', stock: 55 },
  { name: 'USB-C Docking Station', description: 'Dual 4K HDMI, USB-A, Ethernet and PD charging.', price: 119.0, image: 'https://picsum.photos/seed/dock/400/400', category: 'Computer', stock: 3 },
  { name: 'Bluetooth Speaker', description: 'Waterproof portable speaker with 360° sound and 12h playtime.', price: 89.95, image: 'https://picsum.photos/seed/speaker/400/400', category: 'Audio', stock: 17 },
  { name: 'Gaming Mouse', description: '16000 DPI optical sensor with programmable buttons.', price: 64.99, image: 'https://picsum.photos/seed/mouse/400/400', category: 'Computer', stock: 61 },
  { name: 'Webcam 1080p', description: 'Full HD webcam with built-in microphone and privacy shutter.', price: 74.5, image: 'https://picsum.photos/seed/webcam/400/400', category: 'Computer', stock: 34 }
];

function seed() {
  initDb();
  const db = getDb();
  db.exec('DELETE FROM cart_items');
  db.exec('DELETE FROM carts');
  db.exec('DELETE FROM order_items');
  db.exec('DELETE FROM orders');

  const adminEmail = 'admin@store.dev';
  const adminPassword = 'admin123';
  const admin = db.prepare('SELECT id FROM users WHERE email = ? AND role = ?').get(adminEmail, 'admin');
  if (admin) {
    db.prepare('UPDATE users SET name = ?, password = ? WHERE id = ?')
      .run('Store Admin', bcrypt.hashSync(adminPassword, 10), admin.id);
  } else {
    db.prepare('INSERT INTO users (email, password, name, role) VALUES (?, ?, ?, ?)')
      .run(adminEmail, bcrypt.hashSync(adminPassword, 10), 'Store Admin', 'admin');
  }

  const insert = db.prepare(
    'INSERT INTO products (name, description, price, image, category, stock) VALUES (?, ?, ?, ?, ?, ?)'
  );
  db.exec('BEGIN');
  try {
    for (const p of products) {
      insert.run(p.name, p.description, p.price, p.image, p.category, p.stock);
    }
    db.exec('COMMIT');
  } catch (e) {
    db.exec('ROLLBACK');
    throw e;
  }

  console.log(`Seeded ${products.length} products and admin account (${adminEmail} / ${adminPassword}).`);
}

seed();
