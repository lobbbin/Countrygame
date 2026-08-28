# KiloStore — Fullstack Online Store

A complete online store with a React frontend and a Node.js/Express backend backed by
SQLite. Features a product catalog, JWT authentication (customer + admin roles), a
shopping cart, checkout with stock reservations, order management, and an admin
dashboard.

## Tech stack
- **Backend:** Node.js 22 (built-in `node:sqlite`), Express, JWT, bcryptjs
- **Frontend:** React 18, React Router v6, Vite
- **Database:** SQLite (file-based, embedded — no external server required)

> The backend uses Node's built-in `node:sqlite` module, so **no native
> compilation / C++ toolchain is required** (works in restricted sandboxes).

## Project structure
```
package.json        # root convenience scripts (dev/build/start/seed)
README.md
.gitignore
backend/
├── server.js        # Express app + API; serves built frontend in production
├── db.js            # SQLite schema (node:sqlite)
├── .env             # PORT, JWT_SECRET, CORS_ORIGIN
├── src/
│   ├── middleware/  # auth.js (JWT), errorHandler.js
│   ├── routes/      # auth, products, cart, orders, admin
│   └── seed.js      # seeds products + admin account
└── data/store.db    # (generated at runtime, gitignored)
frontend/
├── vite.config.js   # dev proxy -> http://localhost:4000
└── src/
    ├── context/     # AuthContext, CartContext
    ├── components/  # Navbar, ProductCard, CartItem, CartDrawer, ProductForm, route guards
    ├── pages/       # Home, ProductList, ProductDetail, Cart, Checkout, Orders, Login, Register, AdminDashboard
    ├── lib/events.js# tiny UI event bus
    ├── api.js       # fetch wrapper (auth header, errors)
    └── App.jsx
```

## Prerequisites
- Node.js >= 22

## Run

### Development (two processes)
From the repository root:
```bash
npm install          # installs root dev dep (concurrently)
npm run dev          # -> backend on :4000, frontend (Vite) on :5173
```
The Vite dev server proxies `/api` to the backend, so the frontend can be opened at
http://localhost:5173.

### Production (backend serves the built frontend)
```bash
npm run build        # builds the frontend into frontend/dist
npm start            # seeds the DB, then starts the server on :4000
# open http://localhost:4000
```

### Individual commands
```bash
npm run seed         # (re)seed products + admin account
npm run build        # build frontend only
npm run dev:backend  # backend only
npm run dev:frontend # Vite dev server only
```

## Seeded accounts
| Role     | Email           | Password  |
|----------|-----------------|-----------|
| Admin    | admin@store.dev | admin123  |
| Customer | _(register via /register)_ | — |

## API reference (`/api`)
| Method   | Route                  | Auth      | Description                          |
|----------|------------------------|-----------|--------------------------------------|
| POST     | /auth/register         | public    | Register (default role: customer)    |
| POST     | /auth/login            | public    | Login -> `{ user, token }`           |
| GET      | /auth/me               | auth      | Current user                         |
| GET      | /products              | public    | List (search, category, limit, offset)|
| GET      | /products/categories   | public    | Distinct categories                  |
| GET      | /products/:id          | public    | Single product                       |
| POST     | /products              | admin     | Create product                       |
| PUT      | /products/:id          | admin     | Update product                       |
| DELETE   | /products/:id          | admin     | Delete product                       |
| GET      | /cart                  | auth      | Get cart + items                     |
| POST     | /cart/items            | auth      | Add item                             |
| PUT      | /cart/items/:id        | auth      | Update quantity                      |
| DELETE   | /cart/items/:id        | auth      | Remove item                          |
| DELETE   | /cart                  | auth      | Clear cart                           |
| POST     | /orders                | auth      | Checkout (cart -> order)             |
| GET      | /orders                | auth      | List orders (own / all if admin)     |
| GET      | /orders/:id            | auth      | Order detail + items                 |
| PUT      | /orders/:id/status     | admin     | Update order status                  |
| GET      | /admin/stats           | admin     | Dashboard stats                      |
| GET      | /health                | public    | Health check                         |

## Configuration (`backend/.env`)
```
PORT=4000
JWT_SECRET=dev-secret-change-me   # change in production
CORS_ORIGIN=http://localhost:5173
NODE_ENV=development
```

## Deployed
The code is published on the `online-store` branch of
https://github.com/lobbbin/Countrygame/tree/online-store
