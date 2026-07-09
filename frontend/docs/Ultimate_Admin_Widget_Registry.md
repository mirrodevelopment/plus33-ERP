# Ultimate Admin Dashboard - Widget Registry

The dashboard framework leverages modular, reusable, and parameter-driven widgets. This document acts as the official components registry.

## Registered Widget Modules

### 1. KPI Card Component
*   **Module Path:** `widgets/kpi/kpi-card.js`
*   **Visual Properties:** Currency (€), percentage (%), and numeric formatting. Displays green (+12.3% success) or red (-4.5% danger) indicators.
*   **Data Bindings:** Subscribed to `dashboardService.getKPIs()`.

### 2. Line Chart Component
*   **Module Path:** `widgets/charts/sales-chart.js`
*   **Visual Properties:** SVG vector line drawing with linear gradient filling, target line overlays, and grid outlines.
*   **Data Bindings:** Subscribed to `dashboardService.getSalesTrend()`.

### 3. Donut Chart Component
*   **Module Path:** `widgets/charts/stock-chart.js`
*   **Visual Properties:** Multi-colored concentric SVG donut circles representing relative percentages of stores status.
*   **Data Bindings:** Seed breakdown matrix.

### 4. Operations Log Table Component
*   **Module Path:** `widgets/tables/recent-activity.js`
*   **Visual Properties:** Data grid table featuring action details, completion times, and operations usernames.
*   **Data Bindings:** Subscribed to `dashboardService.getRecentActivities()`.

### 5. Workflows Approvals Component
*   **Module Path:** `widgets/approvals/approvals-list.js`
*   **Visual Properties:** Interactive cards showcasing pending counts, and containing approval buttons.
*   **Data Bindings:** Mock workflows queue.

### 6. Notifications Alerts Component
*   **Module Path:** `widgets/notifications/alerts-list.js`
*   **Visual Properties:** Lists color-coded by alert severity (Red danger/Orange warning/Blue info).
*   **Data Bindings:** Mock resource alerts.

### 7. Console Console Component
*   **Module Path:** `widgets/system/quick-actions.js`
*   **Visual Properties:** Buttons to trigger operations like ESG carbon footprint metrics updates.
*   **Data Bindings:** Binds click listeners to push toast alerts.
