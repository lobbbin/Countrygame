import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { on } from '../lib/events';
import CartItem from './CartItem';

export default function CartDrawer() {
  const { items, total, count, refreshCart } = useCart();
  const [open, setOpen] = useState(false);

  useEffect(() => {
    const unsub = on('cart:open', () => setOpen(true));
    return unsub;
  }, []);

  if (!open) return null;

  return (
    <div className="cart-drawer-backdrop" style={{
      position: 'fixed', inset: 0, background: 'rgba(0,0,0,.6)', zIndex: 1000, display: 'flex', justifyContent: 'flex-end'
    }} onClick={() => setOpen(false)}>
      <div className="card" style={{ width: 380, maxWidth: '90vw', maxHeight: '90vh', display: 'flex', flexDirection: 'column', margin: '1rem', overflowY: 'auto' }} onClick={e => e.stopPropagation()}>
        <h2 style={{ fontSize: '1.1rem', marginBottom: '1rem' }}>Your Cart ({count})</h2>
        {items.length === 0 ? (
          <p className="muted">Your cart is empty.</p>
        ) : (
          <div style={{ flex: 1, overflowY: 'auto' }}>
            {items.map(it => <CartItem key={it.id} item={it} />)}
          </div>
        )}
        <div style={{ borderTop: '1px solid var(--border)', paddingTop: '1rem', marginTop: '1rem' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', fontWeight: 700, fontSize: '1.1rem' }}>
            <span>Total</span><span>${total.toFixed(2)}</span>
          </div>
          <div style={{ display: 'flex', gap: '0.75rem', marginTop: '1rem' }}>
            <button className="btn btn-ghost" onClick={() => setOpen(false)}>Continue shopping</button>
            <Link to="/cart" className="btn btn-primary" style={{ flex: 1, textAlign: 'center' }} onClick={() => setOpen(false)}>Go to cart</Link>
          </div>
        </div>
      </div>
    </div>
  );
}
