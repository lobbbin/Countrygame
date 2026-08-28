import React, { useEffect, useState } from 'react';
import { api } from '../api';
import ProductForm from '../components/ProductForm';

const statusOptions = ['pending', 'processing', 'shipped', 'delivered', 'cancelled'];
const statusClass = {
  pending: 'badge-pending', processing: 'badge-processing',
  shipped: 'badge-shipped', delivered: 'badge-delivered', cancelled: 'badge-cancelled'
};

export default function AdminDashboard() {
  const [active, setActive] = useState('stats');
  const [stats, setStats] = useState(null);
  const [products, setProducts] = useState([]);
  const [orders, setOrders] = useState([]);
  const [editingProduct, setEditingProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchAll = async () => {
    setLoading(true);
    try {
      const [s, p, o] = await Promise.all([
        api('/admin/stats'),
        api('/products?limit=100'),
        api('/orders')
      ]);
      setStats(s); setProducts(p); setOrders(o);
    } catch (err) {
      setError(err.message || 'Failed to load data');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchAll(); }, []);

  const deleteProduct = async (id) => {
    if (!window.confirm('Delete this product?')) return;
    try {
      await api(`/products/${id}`, { method: 'DELETE', raw: true });
      setProducts(products.filter(p => p.id !== id));
      if (editingProduct?.id === id) setEditingProduct(null);
    } catch (err) {
      setError(err.message);
    }
  };

  const updateOrderStatus = async (id, status) => {
    try {
      await api(`/orders/${id}/status`, { method: 'PUT', body: { status } });
      fetchAll();
    } catch (err) {
      setError(err.message);
    }
  };

  const onProductSaved = () => {
    setEditingProduct(null);
    fetchAll();
  };

  const statCards = stats && [
    { label: 'Products', value: stats.products },
    { label: 'Customers', value: stats.customers },
    { label: 'Orders', value: stats.orders },
    { label: 'Revenue', value: `$${stats.revenue.toFixed(2)}` },
    { label: 'Low stock', value: stats.lowStock }
  ];

  const renderStats = () => (
    <div>
      {error && <div className="error-box">{error}</div>}
      <div className="grid grid-4">
        {statCards.map(c => (
          <div key={c.label} className="card" style={{ textAlign: 'center', padding: '1.5rem' }}>
            <div style={{ fontSize: '1.6rem', fontWeight: 700 }}>{c.value}</div>
            <div className="muted">{c.label}</div>
          </div>
        ))}
      </div>
    </div>
  );

  const renderProducts = () => (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h2 style={{ margin: 0 }}>Products</h2>
        <button className="btn btn-outline" onClick={() => setEditingProduct(null)}>Add product</button>
      </div>
      {editingProduct ? (
        <ProductForm product={editingProduct} onSuccess={onProductSaved} onCancel={() => setEditingProduct(null)} />
      ) : (
        <ProductForm onSuccess={fetchAll} onCancel={() => setEditingProduct(null)} />
      )}
      <div className="grid grid-4">
        {products.map(p => (
          <div key={p.id} className="card">
            <h3 style={{ fontSize: '1rem' }}>{p.name}</h3>
            <p className="muted" style={{ fontSize: '.8rem', margin: '.3rem 0' }}>${p.price} · {p.stock} in stock</p>
            <div style={{ display: 'flex', gap: '.5rem', marginTop: '.5rem' }}>
              <button className="btn btn-ghost" onClick={() => setEditingProduct(p)}>Edit</button>
              <button className="btn btn-ghost btn-danger" onClick={() => deleteProduct(p.id)}>Delete</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );

  const renderOrders = () => (
    <div>
      <h2 style={{ marginBottom: '1rem' }}>Orders ({orders.length})</h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
        {orders.map(o => (
          <div key={o.id} className="card">
            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
              <h3 style={{ margin: 0 }}>Order #{o.id}</h3>
              <span className={`badge ${statusClass[o.status] || ''}`}>{o.status}</span>
            </div>
            <p className="muted" style={{ fontSize: '.8rem' }}>{new Date(o.created_at).toLocaleString()}</p>
            <p>Total: ${Number(o.total).toFixed(2)} · {o.shipping_address}</p>
            <select
              className="muted"
              style={{ marginTop: '.5rem', background: 'var(--surface-2)', border: '1px solid var(--border)', borderRadius: 8, color: 'var(--text)', padding: '.3rem' }}
              value={o.status}
              onChange={e => updateOrderStatus(o.id, e.target.value)}
            >
              {statusOptions.map(s => <option key={s} value={s}>{s}</option>)}
            </select>
          </div>
        ))}
      </div>
    </div>
  );

  if (loading) return <p className="muted">Loading admin...</p>;

  return (
    <div>
      <h1 style={{ marginBottom: '1.5rem' }}>Admin Dashboard</h1>
      <div style={{ display: 'flex', gap: '0.5rem', marginBottom: '1.5rem', flexWrap: 'wrap' }}>
        {['stats', 'products', 'orders'].map(tab => (
          <button key={tab} className={`btn ${active === tab ? 'btn-primary' : 'btn-ghost'}`} onClick={() => setActive(tab)}>
            {tab.charAt(0).toUpperCase() + tab.slice(1)}
          </button>
        ))}
      </div>
      {active === 'stats' && renderStats()}
      {active === 'products' && renderProducts()}
      {active === 'orders' && renderOrders()}
    </div>
  );
}
