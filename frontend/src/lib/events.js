const listeners = {};

export const on = (event, cb) => {
  (listeners[event] = listeners[event] || []).push(cb);
  return () => off(event, cb);
};
export const off = (event, cb) => {
  if (listeners[event]) listeners[event] = listeners[event].filter(c => c !== cb);
};
export const emit = (event, data) => (listeners[event] || []).forEach(cb => cb(data));

export const ui = { openCart: () => emit('cart:open'), closeCart: () => emit('cart:close') };
