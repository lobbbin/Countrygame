import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../api';
import ProductCard from '../components/ProductCard';

export default function Home() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api('/products?limit=8')
      .then(d => { setProducts(d); setLoading(false); })
      .catch(() => setLoading(false));
  }, []);

  return (
    <>
      <section className="card" style={{ marginBottom: '2rem', textAlign: 'center', padding: '3rem 1.5rem' }}>
        <h1 style={{ fontSize: '2.2rem', marginBottom: '.5rem' }}>Welcome to KiloStore</h1>
        <p className="muted">Quality gadgets and gear delivered fast.</p>
        <Link to="/products" className="btn btn-primary" style={{ marginTop: '1rem' }}>Shop now</Link>
      </section>

      <h2 style={{ marginBottom: '1rem' }}>Featured products</h2>
      {loading ? <p className="muted">Loading...</p> : (
        <div className="grid grid-4">
          {products.map(p => <ProductCard key={p.id} product={p} />)}
        </div>
      )}
    </>
  );
}
