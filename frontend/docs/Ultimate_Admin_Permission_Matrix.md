# Ultimate Admin Dashboard - Permissions & Clearance Matrix

Security and views visibility inside **PLUS33 ERP** are managed via Role-Based Access Control (RBAC).

## Roles & Permissions Mapping

| Role | Allowed Sections | Active Clearance Tag | Interface Navigation Options |
| :--- | :--- | :--- | :--- |
| **Ultimate Admin** | All Platform Sections | `*` (Wildcard clearance) | Core Dashboard, Stock Ledger, Physical WMS Topology, Platform Docs |
| **Warehouse Admin** | Supply Chain & WMS | `warehouse:view`, `warehouse:write`, `wms:count` | Stock Ledger, Physical WMS Topology |
| **Store Admin** | Inventory Stock Ledger | `inventory:view`, `inventory:write` | Stock Ledger |

## Client Enforcement Engine

1.  **Route Protection:** The SPA Router (`routing/router.js`) parses `requiresAuth` and `permission` declarations on navigation requests. If a user profile lacks permissions, a `403 Access Denied` wrapper is loaded.
2.  **Navigation View Filtering:** The Menu Service (`navigation/menus.js`) queries the `permissionStore` to dynamically generate sidebar links.
