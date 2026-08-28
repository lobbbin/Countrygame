import React, { useState } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import { ui } from '../lib/events';

const navLinks = [
  { to: '/', label: 'Home' },
  { to: '/products', label: 'Products' }
];

export default function Navbar() {
  const { user, isAuthenticated, isAdmin, logout } = useAuth();
  const { count } = useCart();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    setMenuOpen(false);
    navigate('/login');
  };

  return (
    <nav className="navbar-height" style={{
      position: 'sticky', top: 0, zIndex: 100,
      background: 'rgba(15,23,42,.85)', backdropFilter: 'blur(10px)',
      borderBottom: '1px solid var(--border)'
    }}>
      <div className="app-container" style={{ display: 'flex', height: '100%', alignItems: 'center', justifyContent: 'space-between' }}>
        <Link to="/" className="text-accent" style={{ fontSize: '1.25rem', fontWeight: 700 }}>KiloStore</Link>

        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          {navLinks.map(l => (
            <NavLink key={l.to} to={l.to} className="btn-ghost">{l.label}</NavLink>
          ))}
          <NavLink to="/cart" className="btn-ghost" style={{ position: 'relative' }}>
            <span>Cart</span>
            {count > 0 && <span className="badge" style={{ position: 'absolute', top: -6, right: -6, background: 'var(--accent)', color: '#0f172a' }}>{count}</span>}
          </NavLink>

          {isAuthenticated ? (
            <div style={{ position: 'relative', display: 'inline-block' }}>
              <button className="btn btn-ghost" onClick={() => setMenuOpen(!menuOpen)}>
                {user?.name || 'Account'} ▼
              </button>
              {menuOpen && (
                <div className="card" style={{ position: 'absolute', right: 0, top: '110%', minWidth: 180, padding: '0.75rem' }}>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                    <Link to="/orders" onClick={() => setMenuOpen(false)} className="btn btn-ghost">My Orders</Link>
                    {isAdmin && <Link to="/admin" onClick={() => setMenuOpen(false)} className="btn btn-ghost">Admin Panel</Link>}
                    <button className="btn btn-outline" onClick={handleLogout}>Logout</button>
                  </div>
                </div>
              )}
            </div>
          ) : (
            <Link to="/login" className="btn btn-primary">Login</Link>
          )}
        </div>
      </div>
    </nav>
  );
}
