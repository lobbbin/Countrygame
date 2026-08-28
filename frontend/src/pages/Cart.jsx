import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import CartItem from '../components/CartItem';

export default function Cart() {
  const { items, count, total, clearCart, loading, refreshCart } = useCart();
  const { isAuthenticated, loading: authLoading } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!authLoading && !isAuthenticated) navigate('/login');
  }, [isAuthenticated, authLoading, navigate]);

  const handleCheckout = () => navigate('/checkout');
  const handleClear = async () => {
    if (window.confirm('Clear your cart?')) await clearCart();
  };

  if (authLoading) return <p className="muted">Loading...</p>;
  if (!isAuthenticated) return null;

  return (
    <div>
      <h1 style={{ marginBottom: '1.5rem' }}>Your Cart</h1>
      {loading && <p className="muted">Refreshing...</p>}
      {items.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: '2.5rem' }}>
          <p className="muted">Your cart is empty.</p>
          <Link to="/products" className="btn btn-primary" style={{ marginTop: '1rem' }}>Keep shopping</Link>
        </div>
      ) : (
        <>
          <div>
            {items.map(it => <CartItem key={it.id} item={it} />)}
          </div>
          <div className="card" style={{ marginTop: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <button className="btn btn-outline" onClick={handleClear}>Clear cart</button>
            <div style={{ textAlign: 'right' }}>
              <div className="muted">Subtotal ({count} items)</div>
              <div style={{ fontSize: '1.4rem', fontWeight: 700 }}>${total.toFixed(2)}</div>
            </div>
          </div>
          <button className="btn btn-primary" style={{ marginTop: '1rem', float: 'right' }} onClick={handleCheckout}>Proceed to checkout</button>
        </>
      )}
    </div>
  );
}
