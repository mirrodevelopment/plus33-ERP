/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Navigation Module
 * File              : routes.js
 * Path              : frontend/navigation/routes.js
 * Purpose           : Static router table mapping client-side hash paths to dynamic page module JS file paths, layout shell names, page titles, authentication flags, and role profile routes across all ERP roles.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Static route registry for the client-side router (frontend/routing/router.js).
 * Details:
 *   - Defines hash paths for logins, dashboards, modules, shared pages, and role-specific user profiles (#national-profile, #ultimate-profile, #regional-profile, #store-profile, #supervisor-profile, #employee-profile).
 *   - Maps each hash route to its target page JS module path relative to frontend/.
 *   - Specifies layout shell required (e.g. 'dashboard' or 'auth').
 *   - Configures authentication requirement flags (requiresAuth) and default entry point constants (defaultRoute, loginRoute).
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
    path: '#ultimate-announcements',
    page: 'modules/ultimate-admin/pages/announcements/announcements.js',
    layout: 'dashboard',
    title: 'Enterprise Broadcast Announcements',
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
    path: '#shift-planner',
    page: 'modules/store-employee/pages/shift-dashboard/shift-dashboard.js',
    layout: 'dashboard',
    title: 'Shift Planner',
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
  { path: '#store-inventory', page: 'modules/store-admin/pages/inventory/inventory.js', layout: 'dashboard', title: 'Inventory Overview', requiresAuth: true },
  { path: '#store-usage', page: 'modules/store-admin/pages/usage/usage.js', layout: 'dashboard', title: 'Daily Usage Tracker', requiresAuth: true },
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
  { path: '#regional-announcements', page: 'modules/regional-admin/pages/announcements/announcements.js', layout: 'dashboard', title: 'Broadcast Announcements', requiresAuth: true },
  { path: '#regional-regions', page: 'modules/regional-admin/pages/regions/regions.js', layout: 'dashboard', title: 'Regions Management', requiresAuth: true },
  { path: '#regional-stores', page: 'modules/regional-admin/pages/stores/stores.js', layout: 'dashboard', title: 'Stores Management', requiresAuth: true },
  { path: '#regional-warehouse', page: 'modules/regional-admin/pages/warehouse/warehouse.js', layout: 'dashboard', title: 'Warehouse Overview', requiresAuth: true },
  { path: '#regional-sales', page: 'modules/regional-admin/pages/sales/sales.js', layout: 'dashboard', title: 'Sales Overview', requiresAuth: true },
  { path: '#regional-inventory', page: 'modules/regional-admin/pages/inventory/inventory.js', layout: 'dashboard', title: 'Inventory Overview', requiresAuth: true },
  { path: '#regional-supply-requests', page: 'modules/regional-admin/pages/supply-requests/supply-requests.js', layout: 'dashboard', title: 'Supply Requests', requiresAuth: true },
  { path: '#regional-employees', page: 'modules/regional-admin/pages/employees/employees.js', layout: 'dashboard', title: 'Employee Overview', requiresAuth: true },
  { path: '#regional-recruitment', page: 'modules/regional-admin/pages/recruitment/recruitment.js', layout: 'dashboard', title: 'Recruitment', requiresAuth: true },
  { path: '#regional-training', page: 'modules/regional-admin/pages/training/training.js', layout: 'dashboard', title: 'Training & Development', requiresAuth: true },
  { path: '#regional-performance', page: 'modules/regional-admin/pages/performance/performance.js', layout: 'dashboard', title: 'Performance', requiresAuth: true },
  { path: '#regional-vendors', page: 'modules/regional-admin/pages/vendors/vendors.js', layout: 'dashboard', title: 'Vendors', requiresAuth: true },
  { path: '#regional-purchase', page: 'modules/regional-admin/pages/purchase/purchase.js', layout: 'dashboard', title: 'Purchase Management', requiresAuth: true },
  { path: '#regional-contracts', page: 'modules/regional-admin/pages/contracts/contracts.js', layout: 'dashboard', title: 'Contracts', requiresAuth: true },
  { path: '#regional-complaints', page: 'modules/regional-admin/pages/complaints/complaints.js', layout: 'dashboard', title: 'Complaints Management', requiresAuth: true },
  { path: '#regional-legal', page: 'modules/regional-admin/pages/legal/legal.js', layout: 'dashboard', title: 'Legal Cases', requiresAuth: true },
  { path: '#regional-audit', page: 'modules/regional-admin/pages/audit/audit.js', layout: 'dashboard', title: 'Audit & Compliance', requiresAuth: true },
  { path: '#regional-reports', page: 'modules/regional-admin/pages/reports/reports.js', layout: 'dashboard', title: 'Reports', requiresAuth: true },
  { path: '#regional-analytics', page: 'modules/regional-admin/pages/analytics/analytics.js', layout: 'dashboard', title: 'Analytics', requiresAuth: true },
  { path: '#regional-settings', page: 'modules/regional-admin/pages/settings/settings.js', layout: 'dashboard', title: 'Regional Settings', requiresAuth: true },
  { path: '#regional-help', page: 'modules/regional-admin/pages/help/help.js', layout: 'dashboard', title: 'Help & Support', requiresAuth: true },
  { path: '#store-leave', page: 'modules/store-admin/pages/leave/leave.js', layout: 'dashboard', title: 'Leave Management', requiresAuth: true },
  { path: '#store-workforce', page: 'modules/store-admin/pages/workforce/workforce.js', layout: 'dashboard', title: 'Store Workforce Overview', requiresAuth: true },
  { path: '#store-settings', page: 'modules/store-admin/pages/settings/settings.js', layout: 'dashboard', title: 'Store Settings', requiresAuth: true },
  { path: '#documents', page: 'modules/store-employee/pages/documents/documents.js', layout: 'dashboard', title: 'Documents Center', requiresAuth: true },
  { path: '#payslips', page: 'modules/store-employee/pages/payslips/payslips.js', layout: 'dashboard', title: 'Earnings & Payslips', requiresAuth: true },
  { path: '#profile', page: 'shared/profile/profile.js', layout: 'dashboard', title: 'User Profile', requiresAuth: true },
  { path: '#national-profile', page: 'modules/national-admin/pages/profile/profile.js', layout: 'dashboard', title: 'National Admin Profile', requiresAuth: true },
  { path: '#ultimate-profile', page: 'modules/ultimate-admin/pages/profile/profile.js', layout: 'dashboard', title: 'Ultimate Admin Profile', requiresAuth: true },
  { path: '#regional-profile', page: 'modules/regional-admin/pages/profile/profile.js', layout: 'dashboard', title: 'Regional Admin Profile', requiresAuth: true },
  { path: '#store-profile', page: 'modules/store-admin/pages/profile/profile.js', layout: 'dashboard', title: 'Store Admin Profile', requiresAuth: true },
  { path: '#supervisor-profile', page: 'modules/store-employee/pages/profile-supervisor/profile.js', layout: 'dashboard', title: 'Supervisor Profile', requiresAuth: true },
  { path: '#employee-profile', page: 'modules/store-employee/pages/profile-employee/profile.js', layout: 'dashboard', title: 'Employee Profile', requiresAuth: true }
];

export const defaultRoute = '#dashboard';
export const loginRoute = '#login';
