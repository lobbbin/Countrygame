import React, { useEffect, useState } from 'react';
import { api } from '../api';

export default function ProductForm({ product, onSuccess, onCancel }) {
  const isEdit = !!product;
  const [form, setForm] = useState({
    name: '', description: '', price: '', image: '', category: '', stock: ''
  });
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (product) setForm({
      name: product.name || '',
      description: product.description || '',
      price: product.price ?? '',
      image: product.image || '',
      category: product.category || '',
      stock: product.stock ?? ''
    });
  }, [product]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(f => ({ ...f, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true); setError('');
    try {
      if (isEdit) {
        await api(`/products/${product.id}`, { method: 'PUT', body: form });
      } else {
        await api('/products', { method: 'POST', body: form });
      }
      onSuccess && onSuccess();
    } catch (err) {
      setError(err.message || 'Failed to save product');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="card" style={{ marginBottom: '1.5rem' }}>
      {error && <div className="error-box">{error}</div>}
      <h3 style={{ marginBottom: '1rem' }}>{isEdit ? 'Edit Product' : 'Add Product'}</h3>
      <div className="form-group">
        <label>Name</label>
        <input name="name" value={form.name} onChange={handleChange} required disabled={submitting} />
      </div>
      <div className="form-group">
        <label>Description</label>
        <textarea name="description" value={form.description} onChange={handleChange} rows={3} disabled={submitting} />
      </div>
      <div className="grid-2" style={{ gap: '1rem' }}>
        <div className="form-group">
          <label>Price ($)</label>
          <input type="number" step="0.01" name="price" value={form.price} onChange={handleChange} required disabled={submitting} />
        </div>
        <div className="form-group">
          <label>Stock</label>
          <input type="number" name="stock" value={form.stock} onChange={handleChange} required disabled={submitting} />
        </div>
        <div className="form-group">
          <label>Category</label>
          <input name="category" value={form.category} onChange={handleChange} disabled={submitting} />
        </div>
        <div className="form-group">
          <label>Image URL</label>
          <input name="image" value={form.image} onChange={handleChange} disabled={submitting} />
        </div>
      </div>
      <div style={{ display: 'flex', gap: '0.75rem', marginTop: '0.5rem' }}>
        <button type="submit" className="btn btn-primary" disabled={submitting}>{submitting ? 'Saving...' : (isEdit ? 'Update' : 'Create')}</button>
        {onCancel && <button type="button" className="btn btn-outline" onClick={onCancel} disabled={submitting}>Cancel</button>}
      </div>
    </form>
  );
}
