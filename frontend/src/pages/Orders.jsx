import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api } from '../api';
import { useAuth } from '../context/AuthContext';

const statusClass = {
  pending: 'badge-pending', processing: 'badge-processing',
  shipped: 'badge-shipped', delivered: 'badge-delivered', cancelled: 'badge-cancelled'
};

export default function Orders() {
  const { isAuthenticated, loading: authLoading } = useAuth();
  const navigate = useNavigate();
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    if (!authLoading && !isAuthenticated) navigate('/login');
  }, [isAuthenticated, authLoading, navigate]);

  useEffect(() => {
    if (isAuthenticated) {
      api('/orders').then(d => setOrders(d)).catch(() => {});
    }
  }, [isAuthenticated]);

  if (authLoading) return <p className="muted">Loading...</p>;
  if (!isAuthenticated) return null;

  return (
    <div>
      <h1 style={{ marginBottom: '1.5rem' }}>My Orders</h1>
      {orders.length === 0 ? (
        <p className="muted">You haven't placed any orders yet. <Link to="/products">Start shopping</Link>.</p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
          {orders.map(o => (
            <div key={o.id} className="card">
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '.75rem' }}>
                <h3 style={{ fontSize: '1.1rem' }}>Order #{o.id}</h3>
                <span className={`badge ${statusClass[o.status] || ''}`}>{o.status}</span>
              </div>
              <p className="muted" style={{ fontSize: '.8rem' }}>{new Date(o.created_at).toLocaleString()}</p>
              <p style={{ margin: '.5rem 0' }}><strong>Total:</strong> ${Number(o.total).toFixed(2)}</p>
              <p><strong>Address:</strong> {o.shipping_address}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
