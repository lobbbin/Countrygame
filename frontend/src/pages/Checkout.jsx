import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../api';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';

export default function Checkout() {
  const { isAuthenticated, loading: authLoading } = useAuth();
  const { items, total, refreshCart } = useCart();
  const navigate = useNavigate();
  const [address, setAddress] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('card');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [orderId, setOrderId] = useState(null);

  useEffect(() => {
    if (!authLoading && !isAuthenticated) navigate('/login');
  }, [isAuthenticated, authLoading, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!address.trim()) return setError('Shipping address is required');
    setSubmitting(true); setError('');
    try {
      const order = await api('/orders', { method: 'POST', body: { shippingAddress: address, paymentMethod } });
      await refreshCart();
      setOrderId(order.id);
    } catch (err) {
      setError(err.message || 'Checkout failed');
    } finally {
      setSubmitting(false);
    }
  };

  if (authLoading) return <p className="muted">Loading...</p>;
  if (!isAuthenticated) return null;
  if (orderId) {
    return (
      <div className="card" style={{ textAlign: 'center', padding: '2.5rem' }}>
        <h2 style={{ color: '#4ade80' }}>Order placed!</h2>
        <p style={{ margin: '1rem 0' }}>Your order #${orderId} is confirmed.</p>
        <button className="btn btn-primary" onClick={() => navigate('/orders')}>View my orders</button>
      </div>
    );
  }
  if (items.length === 0) {
    return (
      <div className="card" style={{ textAlign: 'center', padding: '2rem' }}>
        <p className="muted">Your cart is empty.</p>
        <Link to="/products" className="btn btn-primary" style={{ marginTop: '1rem' }}>Shopping</Link>
      </div>
    );
  }

  return (
    <div className="grid grid-2">
      <form onSubmit={handleSubmit} className="card">
        <h2 style={{ marginBottom: '1rem' }}>Shipping details</h2>
        {error && <div className="error-box">{error}</div>}
        <div className="form-group">
          <label>Shipping address</label>
          <textarea
            value={address} onChange={e => setAddress(e.target.value)}
            rows={3} placeholder="Street, city, zip code" required disabled={submitting}
          />
        </div>
        <div className="form-group">
          <label>Payment method</label>
          <select value={paymentMethod} onChange={e => setPaymentMethod(e.target.value)} disabled={submitting}>
            <option value="card">Credit / Debit Card</option>
            <option value="paypal">PayPal</option>
            <option value="cod">Cash on delivery</option>
          </select>
        </div>
        <div style={{ marginTop: '1rem' }}>
          <button type="submit" className="btn btn-primary" disabled={submitting}>{submitting ? 'Placing order...' : 'Place order'}</button>
        </div>
      </form>

      <div className="card">
        <h2 style={{ marginBottom: '1rem' }}>Order summary</h2>
        <div style={{ marginBottom: '1rem' }}>
          {items.map(it => (
            <div key={it.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '.3rem 0', borderBottom: '1px solid var(--border)' }}>
              <span>{it.name} x {it.quantity}</span>
              <span>${(Number(it.price) * it.quantity).toFixed(2)}</span>
            </div>
          ))}
        </div>
        <div style={{ display: 'flex', justifyContent: 'space-between', fontWeight: 700, fontSize: '1.2rem' }}>
          <span>Total</span><span>${total.toFixed(2)}</span>
        </div>
      </div>
    </div>
  );
}
