import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { api } from '../api';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { ui } from '../lib/events';

export default function ProductDetail() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [qty, setQty] = useState(1);
  const { addItem } = useCart();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    api(`/products/${id}`).then(d => { setProduct(d); setLoading(false); }).catch(() => setLoading(false));
  }, [id]);

  const handleAdd = async () => {
    if (!isAuthenticated) { ui.openCart(); return; }
    try {
      await addItem(product.id, qty);
      ui.openCart();
    } catch (err) {
      console.error(err);
    }
  };

  if (loading) return <p className="muted">Loading...</p>;
  if (!product) return <p className="muted">Product not found.</p>;

  const maxQty = Math.min(qty, product.stock);

  return (
    <div className="grid grid-2">
      <div style={{ borderRadius: 12, overflow: 'hidden', background: 'var(--surface-2)', height: 320 }}>
        {product.image ? <img src={product.image} alt={product.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} /> : null}
      </div>
      <div>
        <span className="muted" style={{ fontSize: '.8rem', textTransform: 'uppercase' }}>{product.category}</span>
        <h1 style={{ fontSize: '1.7rem', margin: '.4rem 0' }}>{product.name}</h1>
        <p style={{ fontSize: '1.4rem', fontWeight: 700, margin: '.8rem 0' }}>${Number(product.price).toFixed(2)}</p>
        <span className="badge" style={{ background: product.stock > 0 ? '#16a34a' : '#dc2626' }}>
          {product.stock > 0 ? `${product.stock} in stock` : 'Out of stock'}
        </span>
        <p style={{ margin: '1rem 0', color: 'var(--muted)' }}>{product.description || 'No description available.'}</p>
        {product.stock > 0 && (
          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginTop: '1rem' }}>
            <input
              type="number" min={1} max={product.stock} value={qty}
              onChange={e => setQty(Math.max(1, Math.min(product.stock, parseInt(e.target.value) || 1)))}
              style={{ width: 72, padding: '.5rem', background: 'var(--surface-2)', border: '1px solid var(--border)', borderRadius: 8, color: 'var(--text)', textAlign: 'center' }}
            />
            <button className="btn btn-primary" onClick={handleAdd}>Add to cart</button>
          </div>
        )}
      </div>
    </div>
  );
}
