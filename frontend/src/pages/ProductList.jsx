import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { api } from '../api';
import ProductCard from '../components/ProductCard';

export default function ProductList() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  const search = searchParams.get('q') || '';
  const category = searchParams.get('category') || '';

  const fetchProducts = () => {
    setLoading(true);
    let url = '/products?limit=20';
    if (search) url += `&search=${encodeURIComponent(search)}`;
    if (category) url += `&category=${encodeURIComponent(category)}`;
    api(url).then(d => { setProducts(d); setLoading(false); }).catch(() => setLoading(false));
  };

  useEffect(() => {
    api('/products/categories').then(d => setCategories(d)).catch(() => {});
    fetchProducts();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [search, category]);

  const handleSearch = (q) => setSearchParams(q ? { q, category } : { category });
  const handleCategory = (c) => setSearchParams(search ? { q: search, category: c } : { category: c });

  return (
    <div style={{ display: 'flex', gap: '2rem', flexDirection: window !== undefined ? 'row' : 'row' }}>
      <aside className="card" style={{ minWidth: 220, alignSelf: 'flex-start' }}>
        <h3 style={{ marginBottom: '1rem' }}>Filters</h3>
        <div className="form-group">
          <label>Search</label>
          <input
            type="text" placeholder="Search products..."
            value={search}
            onChange={e => handleSearch(e.target.value)}
          />
        </div>
        <div className="form-group">
          <label>Category</label>
          <select value={category} onChange={e => handleCategory(e.target.value)}>
            <option value="">All</option>
            {categories.map(c => <option key={c} value={c}>{c}</option>)}
          </select>
        </div>
        {search && <button className="btn btn-outline" onClick={() => handleSearch('')}>Clear</button>}
      </aside>

      <main>
        {loading ? <p className="muted">Loading products...</p> : (
          <div className="grid grid-4">
            {products.length ? products.map(p => <ProductCard key={p.id} product={p} />) : <p className="muted">No products found.</p>}
          </div>
        )}
      </main>
    </div>
  );
}
