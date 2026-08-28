const path = require('path');
const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');

dotenv.config({ path: path.join(__dirname, '.env') });
const { initDb } = require('./src/db');

const JWT_SECRET = process.env.JWT_SECRET || 'dev-secret-change-me';

const app = express();

initDb();

app.use(cors({
  origin: process.env.CORS_ORIGIN || '*',
  credentials: true
}));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

const authRoutes = require('./src/routes/auth');
const productRoutes = require('./src/routes/products');
const cartRoutes = require('./src/routes/cart');
const orderRoutes = require('./src/routes/orders');
const adminRoutes = require('./src/routes/admin');
const { errorHandler } = require('./src/middleware/errorHandler');

app.use('/api/auth', authRoutes);
app.use('/api/products', productRoutes);
app.use('/api/cart', cartRoutes);
app.use('/api/orders', orderRoutes);
app.use('/api/admin', adminRoutes);

app.get('/api/health', (req, res) => res.json({ status: 'ok' }));

const frontendDist = path.join(__dirname, '..', 'frontend', 'dist');
app.use(express.static(frontendDist));
app.get(/.*/, (req, res) => {
  res.sendFile(path.join(frontendDist, 'index.html'), err => {
    if (err) res.status(200).send('Frontend not built yet. Run the dev frontend separately.');
  });
});

app.use(errorHandler);

const PORT = process.env.PORT || 4000;
app.listen(PORT, () => {
  console.log(`Store API listening on http://localhost:${PORT}`);
  console.log(`JWT secret: ${JWT_SECRET === 'dev-secret-change-me' ? '(default dev key)' : '(configured)'}`);
});
