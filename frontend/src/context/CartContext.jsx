import React, { createContext, useContext, useEffect, useState, useCallback } from 'react';
import { api } from '../api';
import { useAuth } from './AuthContext';

const CartContext = createContext(null);

export function CartProvider({ children }) {
  const [items, setItems] = useState([]);
  const [cartId, setCartId] = useState(null);
  const [loading, setLoading] = useState(false);
  const { isAuthenticated, loading: authLoading } = useAuth();

  const refreshCart = useCallback(async () => {
    if (!isAuthenticated) {
      setItems([]);
      setCartId(null);
      return;
    }
    setLoading(true);
    try {
      const data = await api('/cart');
      setItems(data.items || []);
      setCartId(data.cartId);
    } catch (e) {
      setItems([]);
    } finally {
      setLoading(false);
    }
  }, [isAuthenticated]);

  const addItem = useCallback(async (productId, quantity = 1) => {
    await api('/cart/items', { method: 'POST', body: { productId, quantity } });
    await refreshCart();
  }, [refreshCart]);

  const updateItem = useCallback(async (itemId, quantity) => {
    await api(`/cart/items/${itemId}`, { method: 'PUT', body: { quantity } });
    await refreshCart();
  }, [refreshCart]);

  const removeItem = useCallback(async (itemId) => {
    await api(`/cart/items/${itemId}`, { method: 'DELETE' });
    await refreshCart();
  }, [refreshCart]);

  const clearCart = useCallback(async () => {
    await api('/cart', { method: 'DELETE' });
    await refreshCart();
  }, [refreshCart]);

  const total = items.reduce((sum, it) => sum + Number(it.price) * it.quantity, 0);
  const count = items.reduce((sum, it) => sum + it.quantity, 0);

  useEffect(() => {
    if (!authLoading) refreshCart();
  }, [isAuthenticated, authLoading, refreshCart]);

  const value = {
    items,
    cartId,
    loading,
    count,
    total,
    addItem,
    updateItem,
    removeItem,
    clearCart,
    refreshCart
  };

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export function useCart() {
  return useContext(CartContext);
}
