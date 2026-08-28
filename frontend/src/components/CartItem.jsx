import React, { useState } from 'react';
import { useCart } from '../context/CartContext';

export default function CartItem({ item }) {
  const { updateItem, removeItem } = useCart();
  const [qty, setQty] = useState(item.quantity);
  const [saving, setSaving] = useState(false);

  const changeQty = async (e) => {
    const next = Math.max(1, parseInt(e.target.value) || 1);
    setQty(next);
    setSaving(true);
    try {
      await updateItem(item.id, next);
    } finally {
      setSaving(false);
    }
  };

  const canRemove = item.quantity <= 1;

  return (
    <div className="cart-item" style={{ display: 'flex', alignItems: 'center', gap: '1rem', padding: '1rem 0', borderBottom: '1px solid var(--border)' }}>
      <div style={{ width: 72, height: 72, flexShrink: 0, borderRadius: 8, overflow: 'hidden', background: 'var(--surface-2)' }}>
        {item.image ? <img src={item.image} alt={item.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} /> : null}
      </div>
      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{ fontWeight: 600 }}>{item.name}</div>
        <div className="muted" style={{ fontSize: '.85rem' }}>${Number(item.price).toFixed(2)} each</div>
      </div>
      <div style={{ width: 96 }}>
        <input
          type="number" min={1} value={qty} onChange={changeQty} disabled={saving}
          style={{ width: '100%', padding: '.5rem', background: 'var(--surface-2)', border: '1px solid var(--border)', borderRadius: 8, color: 'var(--text)', textAlign: 'center' }}
        />
      </div>
      <div style={{ width: 110, textAlign: 'right', fontWeight: 600 }}>${(Number(item.price) * item.quantity).toFixed(2)}</div>
      <button className={`btn btn-ghost ${canRemove ? 'btn-danger' : ''}`} onClick={() => removeItem(item.id)} style={{ padding: '.3rem .7rem' }}>
        {canRemove ? 'Remove' : '-'}
      </button>
    </div>
  );
}
