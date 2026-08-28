import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { ui } from '../lib/events';

export default function ProductCard({ product }) {
  const { addItem } = useCart();
  const { isAuthenticated } = useAuth();

  const handleAdd = async (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (!isAuthenticated) { ui.openCart(); return; }
    try {
      await addItem(product.id, 1);
      ui.openCart();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <Link to={`/products/${product.id}`} className="card" style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      <div style={{ width: '100%', height: 180, borderRadius: 10, overflow: 'hidden', background: 'var(--surface-2)' }}>
        {product.image ? (
          <img src={product.image} alt={product.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
        ) : <div style={{ width: '100%', height: '100%' }} />}
      </div>
      <div style={{ padding: '1rem', flex: 1, display: 'flex', flexDirection: 'column' }}>
        <span className="muted" style={{ fontSize: '.75rem' }}>{product.category}</span>
        <h3 style={{ fontSize: '1.05rem', fontWeight: 600, margin: '.3rem 0' }}>{product.name}</h3>
        <p style={{ margin: '.5rem 0', flex: 1, color: 'var(--muted)', fontSize: '.85rem' }}>
          {product.description ? product.description.slice(0, 80) + '...' : 'No description'}
        </p>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <span style={{ fontWeight: 700, fontSize: '1.1rem' }}>${Number(product.price).toFixed(2)}</span>
          <span className={`badge ${product.stock > 0 ? '' : ''}`} style={{ background: product.stock > 0 ? '#16a34a' : '#dc2626' }}>
            {product.stock > 0 ? `${product.stock} in stock` : 'Out of stock'}
          </span>
        </div>
        <button className={`btn ${product.stock > 0 ? 'btn-primary' : 'btn-outline'}`} style={{ marginTop: '1rem' }} disabled={product.stock === 0} onClick={handleAdd}>
          {product.stock > 0 ? 'Add to cart' : 'Out of stock'}
        </button>
      </div>
    </Link>
  );
}
