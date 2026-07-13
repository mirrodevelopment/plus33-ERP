/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Navigation Module
 * File              : routes.js
 * Path              : frontend/navigation/routes.js
 * Purpose           : Client-side hash-based router defining application navigation
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : 
 * Depends On        : 
 *
 * Description
 * ---------------------------------------------------------------------------
 * Client-side hash-based router defining application navigation. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

/**
 * Static route definitions mapping hash paths to dynamic page renderers.
 */
export const routes = [
  {
    path: '#login',
    page: 'shared/login/login.js',
    layout: 'auth',
    title: 'Login',
    requiresAuth: false
  },
  {
    path: '#ultimate-dashboard',
    page: 'modules/ultimate-admin/pages/dashboard/dashboard.js',
    layout: 'dashboard',
    title: 'Enterprise Dashboard',
    requiresAuth: true
  },
  {
    path: '#national-dashboard',
    page: 'modules/national-admin/pages/dashboard/dashboard.js',
    layout: 'dashboard',
    title: 'National Dashboard',
    requiresAuth: true
  },
  {
    path: '#regional-dashboard',
    page: 'modules/regional-admin/pages/dashboard/dashboard.js',
    layout: 'dashboard',
    title: 'Regional Dashboard',
    requiresAuth: true
  },
  {
    path: '#national-warehouse-dashboard',
    page: 'modules/national-warehouse-admin/pages/dashboard/dashboard.js',
    layout: 'dashboard',
    title: 'National Warehouse Admin Dashboard',
    requiresAuth: true
  },
  {
    path: '#regional-warehouse-dashboard',
    page: 'modules/regional-warehouse-admin/pages/dashboard/dashboard.js',
    layout: 'dashboard',
    title: 'Regional Warehouse Admin Dashboard',
    requiresAuth: true
  },
  {
    path: '#store-dashboard',
    page: 'modules/store-admin/pages/dashboard/dashboard.js',
    layout: 'dashboard',
    title: 'Store Manager Dashboard',
    requiresAuth: true
  },
  {
    path: '#employee-dashboard',
    page: 'modules/store-employee/pages/dashboard/dashboard.js',
    layout: 'dashboard',
    title: 'Store Employee Dashboard',
    requiresAuth: true
  },
  {
    path: '#supervisor-dashboard',
    page: 'modules/store-employee/pages/supervisor-dashboard/dashboard.js',
    layout: 'dashboard',
    title: 'Shift Supervisor Dashboard',
    requiresAuth: true
  },
  {
    path: '#inventory',
    page: 'modules/inventory/pages/stock-list/stock-list.js',
    layout: 'dashboard',
    title: 'Inventory & Stock Logs',
    requiresAuth: true
  },
  {
    path: '#warehouse',
    page: 'modules/warehouse-admin/pages/topology/topology.js',
    layout: 'dashboard',
    title: 'WMS Physical Topology',
    requiresAuth: true
  },
  {
    path: '#docs',
    page: 'shared/docs/docs.js',
    layout: 'dashboard',
    title: 'Architecture Docs',
    requiresAuth: false
  },
  
  // Newly Registered Placeholder Navigation Routes
  { path: '#national-management', page: 'modules/ultimate-admin/pages/national/national.js', layout: 'dashboard', title: 'National Management', requiresAuth: true },
  { path: '#regions', page: 'modules/ultimate-admin/pages/regions/regions.js', layout: 'dashboard', title: 'Regions Management', requiresAuth: true },
  { path: '#stores', page: 'modules/ultimate-admin/pages/stores/stores.js', layout: 'dashboard', title: 'Stores Management', requiresAuth: true },
  { path: '#warehouses', page: 'modules/ultimate-admin/pages/warehouses/warehouses.js', layout: 'dashboard', title: 'Warehouses Management', requiresAuth: true },
  { path: '#users', page: 'modules/ultimate-admin/pages/users/users.js', layout: 'dashboard', title: 'Users & Roles', requiresAuth: true },
  { path: '#permissions', page: 'modules/ultimate-admin/pages/permissions/permissions.js', layout: 'dashboard', title: 'Roles & Permissions', requiresAuth: true },
  { path: '#settings', page: 'modules/ultimate-admin/pages/settings/settings.js', layout: 'dashboard', title: 'System Settings', requiresAuth: true },
  { path: '#sales', page: 'modules/store-admin/pages/sales/sales.js', layout: 'dashboard', title: 'Sales Overview', requiresAuth: true },
  { path: '#supply-chain', page: 'modules/warehouse-admin/pages/supply-chain/supply-chain.js', layout: 'dashboard', title: 'Supply Chain Overview', requiresAuth: true },
  { path: '#workforce', page: 'modules/ultimate-admin/pages/workforce/workforce.js', layout: 'dashboard', title: 'Workforce Overview', requiresAuth: true },
  { path: '#customers', page: 'modules/ultimate-admin/pages/customers/customers.js', layout: 'dashboard', title: 'Customer Overview', requiresAuth: true },
  { path: '#finance', page: 'modules/ultimate-admin/pages/finance/finance.js', layout: 'dashboard', title: 'Financial Overview', requiresAuth: true },
  { path: '#budgets', page: 'shared/not-implemented/not-implemented.js', layout: 'dashboard', title: 'Budget & Forecasting', requiresAuth: true },
  { path: '#profitability', page: 'shared/not-implemented/not-implemented.js', layout: 'dashboard', title: 'Profitability Analysis', requiresAuth: true },
  { path: '#audit', page: 'shared/not-implemented/not-implemented.js', layout: 'dashboard', title: 'Audit & Compliance', requiresAuth: true },
  { path: '#legal', page: 'shared/not-implemented/not-implemented.js', layout: 'dashboard', title: 'Legal & Cases', requiresAuth: true },
  { path: '#complaints', page: 'shared/not-implemented/not-implemented.js', layout: 'dashboard', title: 'Complaints Overview', requiresAuth: true },
  { path: '#policies', page: 'shared/not-implemented/not-implemented.js', layout: 'dashboard', title: 'Policies & Documents', requiresAuth: true },
  { path: '#reports', page: 'shared/not-implemented/not-implemented.js', layout: 'dashboard', title: 'Reports & Analytics', requiresAuth: true },
  { path: '#insights', page: 'shared/not-implemented/not-implemented.js', layout: 'dashboard', title: 'Data Insights', requiresAuth: true },
  { path: '#logs', page: 'modules/warehouse-admin/pages/logs/logs.js', layout: 'dashboard', title: 'Activity Logs', requiresAuth: true },
  { path: '#schedule', page: 'modules/store-employee/pages/schedule/schedule.js', layout: 'dashboard', title: 'My Schedule', requiresAuth: true },
  { path: '#tasks', page: 'modules/store-employee/pages/tasks/tasks.js', layout: 'dashboard', title: 'My Tasks', requiresAuth: true },
  { path: '#attendance', page: 'modules/store-employee/pages/attendance/attendance.js', layout: 'dashboard', title: 'Attendance Logs', requiresAuth: true },
  { path: '#training', page: 'modules/store-employee/pages/training/training.js', layout: 'dashboard', title: 'Training & Certifications', requiresAuth: true },
  { path: '#performance', page: 'modules/store-employee/pages/performance/performance.js', layout: 'dashboard', title: 'Performance Metrics', requiresAuth: true },
  { path: '#leave', page: 'modules/store-employee/pages/leave/leave.js', layout: 'dashboard', title: 'Time Off Requests', requiresAuth: true },
  { path: '#supervisor-leaves', page: 'modules/store-employee/pages/supervisor-leaves/leaves.js', layout: 'dashboard', title: 'Barista Time Off Requests', requiresAuth: true },
  { path: '#announcements', page: 'modules/store-employee/pages/announcements/announcements.js', layout: 'dashboard', title: 'Announcements Board', requiresAuth: true },
  { path: '#supervisor-announcements', page: 'modules/store-employee/pages/supervisor-announcements/announcements.js', layout: 'dashboard', title: 'Broadcast Announcements', requiresAuth: true },
  { path: '#store-announcements', page: 'modules/store-admin/pages/announcements/announcements.js', layout: 'dashboard', title: 'Broadcast Announcements', requiresAuth: true },
  { path: '#store-leave', page: 'modules/store-admin/pages/leave/leave.js', layout: 'dashboard', title: 'Leave Management', requiresAuth: true },
  { path: '#documents', page: 'modules/store-employee/pages/documents/documents.js', layout: 'dashboard', title: 'Documents Center', requiresAuth: true },
  { path: '#payslips', page: 'modules/store-employee/pages/payslips/payslips.js', layout: 'dashboard', title: 'Earnings & Payslips', requiresAuth: true },
  { path: '#profile', page: 'shared/profile/profile.js', layout: 'dashboard', title: 'User Profile', requiresAuth: true }
];

export const defaultRoute = '#dashboard';
export const loginRoute = '#login';
