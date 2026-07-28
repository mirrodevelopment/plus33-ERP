/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Navigation Module
 * File              : menus.js
 * Path              : frontend/navigation/menus.js
 * Purpose           : Navigation menu generator providing role-specific sidebar navigation groups and items filtered against user permissions for nationalAdmin, regionalAdmin, storeAdmin, shiftSupervisor, storeEmployee, warehouse roles, and ultimateAdmin.
 * Version           : 1.0.0
 ******************************************************************************/

import { permissionStore } from '../store/permissionStore.js';
import { authStore } from '../store/authStore.js';

/**
 * Dynamic menu configurations filtered on permission checks.
 */
export function getMenuItems() {
  const role = authStore.getRole();

  const groups = [
    {
      title: 'ADMINISTATION',
      items: [
        { name: 'National Management', icon: 'globe', route: '#national-management', roles: ['ultimateAdmin'] },
        { name: 'Regions Management', icon: 'map', route: role === 'ultimateAdmin' ? '#regions' : '#regional-regions', roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin'] },
        { name: 'Stores Management', icon: 'coffee', route: role === 'ultimateAdmin' ? '#stores' : '#regional-stores', roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin'] },
        { name: 'Warehouses Management', icon: 'warehouse', route: role === 'ultimateAdmin' ? '#warehouses' : '#regional-warehouse', roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin', 'nationalWarehouseAdmin', 'regionalWarehouseAdmin'] },
        { name: 'Users & Roles', icon: 'users', route: '#users', roles: ['ultimateAdmin', 'nationalWarehouseAdmin', 'regionalWarehouseAdmin'] },
        { name: 'Roles & Permissions', icon: 'shield', route: '#permissions', roles: ['ultimateAdmin'] },
        { name: 'System Settings', icon: 'settings', route: role === 'regionalAdmin' ? '#regional-settings' : (role === 'store' || role === 'storeAdmin') ? '#store-settings' : '#settings', roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin', 'store', 'storeAdmin', 'shiftSupervisor', 'supervisor', 'storeEmployee', 'employee'] },
        { name: 'Leave Policy Management', icon: 'sliders', route: '#leave-policy-admin', roles: ['ultimateAdmin', 'nationalAdmin'] },
        { name: 'Leave Management', icon: 'calendar', route: (role === 'store' || role === 'storeAdmin') ? '#store-leave' : '#supervisor-leaves', roles: ['store', 'storeAdmin', 'shiftSupervisor', 'supervisor'] },
        { name: 'Leave', icon: 'plane-takeoff', route: '#leave', roles: ['storeEmployee', 'employee'] },
        { name: 'Apply Leave', icon: 'plane-takeoff', route: '#leave', roles: ['shiftSupervisor', 'supervisor'] },
        { name: 'Payslips', icon: 'banknote', route: '#payslips', roles: ['storeEmployee', 'employee'] },
        { name: 'My Profile', icon: 'user', route: () => {
            if (role === 'ultimateAdmin') return '#ultimate-profile';
            if (role === 'nationalAdmin') return '#national-profile';
            if (role === 'regionalAdmin') return '#regional-profile';
            if (role === 'shiftSupervisor' || role === 'supervisor') return '#supervisor-profile';
            if (role === 'storeEmployee' || role === 'employee') return '#employee-profile';
            if (role === 'store' || role === 'storeAdmin') return '#store-profile';
            return '#profile';
          }, roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin', 'nationalWarehouseAdmin', 'regionalWarehouseAdmin', 'shiftSupervisor', 'supervisor', 'storeEmployee', 'employee', 'store', 'storeAdmin'] }
      ]
    },
    {
      title: 'COMMUNICATION',
      items: [
        { name: 'Broadcast Announcement', icon: 'send', route: () => {
            if (role === 'ultimateAdmin') return '#ultimate-announcements';
            if (role === 'nationalAdmin' || role === 'regionalAdmin') return '#regional-announcements';
            if (role === 'store' || role === 'storeAdmin') return '#store-announcements';
            if (role === 'shiftSupervisor' || role === 'supervisor') return '#supervisor-announcements';
            return '#announcements';
          }, roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin', 'store', 'storeAdmin', 'shiftSupervisor', 'supervisor', 'storeEmployee', 'employee'] }
      ]
    },
    {
      title: 'OPERATIONS',
      items: [
        { name: 'Shift Dashboard', icon: 'bar-chart', route: '#supervisor-dashboard', roles: ['shiftSupervisor', 'supervisor'] },
        { name: 'Shift Planner', icon: 'calendar', route: '#shift-planner', roles: ['shiftSupervisor', 'supervisor'] },
        { name: 'My Roster', icon: 'calendar', route: '#schedule', roles: ['shiftSupervisor', 'supervisor'] },
        { name: 'My Schedule', icon: 'calendar', route: '#schedule', roles: ['storeEmployee', 'employee'] },
        { name: 'Store Tasks Governance', icon: 'check-square', route: '#store-tasks', roles: ['store', 'storeAdmin'] },
        { name: 'My Shift Tasks', icon: 'check-square', route: '#supervisor-my-tasks', roles: ['shiftSupervisor', 'supervisor'] },
        { name: 'Shift Task Management', icon: 'users', route: '#supervisor-tasks', roles: ['shiftSupervisor', 'supervisor'] },
        { name: 'My Tasks', icon: 'check-square', route: '#tasks', roles: ['storeEmployee', 'employee'] },
        { name: 'Shift Attendance', icon: 'clock', route: '#attendance', roles: ['shiftSupervisor', 'supervisor'] },
        { name: 'Attendance', icon: 'clock', route: '#attendance', roles: ['storeEmployee', 'employee'] },
        { name: 'Training', icon: 'graduation-cap', route: '#training', roles: ['storeEmployee', 'employee'] },
        { name: 'Performance', icon: 'star', route: '#performance', roles: ['storeEmployee', 'employee'] },
        { name: 'Sales Overview', icon: 'chart', route: (role === 'store' || role === 'storeAdmin') ? '#sales' : '#regional-sales', roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin', 'store', 'storeAdmin'] },
        { name: 'Inventory Overview', icon: 'package', route: (role === 'store' || role === 'storeAdmin') ? '#store-inventory' : (role === 'nationalWarehouseAdmin' || role === 'regionalWarehouseAdmin') ? '#inventory' : '#regional-inventory', roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin', 'nationalWarehouseAdmin', 'regionalWarehouseAdmin', 'store', 'storeAdmin'] },
        { name: 'Daily Usage Tracker', icon: 'clipboard', route: '#store-usage', roles: ['store', 'storeAdmin'] },
        { name: 'WMS Physical Topology', icon: 'map', route: '#warehouse', roles: ['nationalWarehouseAdmin', 'regionalWarehouseAdmin'] },
        { name: 'Supply Chain Overview', icon: 'truck', route: (role === 'nationalWarehouseAdmin' || role === 'regionalWarehouseAdmin') ? '#supply-chain' : '#regional-supply-requests', roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin', 'nationalWarehouseAdmin', 'regionalWarehouseAdmin'] },
        { name: 'Workforce Overview', icon: 'users', route: (role === 'store' || role === 'storeAdmin') ? '#store-workforce' : '#regional-employees', roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin', 'store', 'storeAdmin'] },
        { name: 'Customer Overview', icon: 'user', route: '#customers', roles: ['ultimateAdmin'] }
      ]
    },
    {
      title: 'FINANCE',
      items: [
        { name: 'Financial Overview', icon: 'dollar', route: '#finance', roles: ['ultimateAdmin'] },
        { name: 'Budget & Forecasting', icon: 'chart', route: '#budgets', roles: ['ultimateAdmin'] },
        { name: 'Profitability Analysis', icon: 'chart', route: '#profitability', roles: ['ultimateAdmin'] }
      ]
    },
    {
      title: 'COMPLIANCE & CONTROL',
      items: [
        { name: 'Audit & Compliance', icon: 'shield', route: role === 'regionalAdmin' ? '#regional-audit' : '#audit', roles: ['ultimateAdmin', 'regionalAdmin'] },
        { name: 'Legal & Cases', icon: 'scale', route: role === 'regionalAdmin' ? '#regional-legal' : '#legal', roles: ['ultimateAdmin', 'regionalAdmin'] },
        { name: 'Complaints Overview', icon: 'message-square', route: (role === 'store' || role === 'storeAdmin') ? '#store-complaints' : role === 'regionalAdmin' ? '#regional-complaints' : role === 'nationalAdmin' ? '#national-complaints' : '#complaints', roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin', 'store', 'storeAdmin'] },
        { name: 'Policies & Documents', icon: 'file-text', route: role === 'regionalAdmin' ? '#leave-policy-booklet' : (role === 'shiftSupervisor' || role === 'supervisor' || role === 'storeEmployee' || role === 'employee') ? '#documents' : '#policies', roles: ['ultimateAdmin', 'regionalAdmin', 'shiftSupervisor', 'supervisor', 'storeEmployee', 'employee'] }
      ]
    },
    {
      title: 'SYSTEM & REPORTS',
      items: [
        { name: 'Reports & Analytics', icon: 'chart', route: (role === 'nationalWarehouseAdmin' || role === 'regionalWarehouseAdmin') ? '#reports' : '#regional-reports', roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin', 'nationalWarehouseAdmin', 'regionalWarehouseAdmin'] },
        { name: 'Data Insights', icon: 'chart', route: '#insights', roles: ['ultimateAdmin'] },
        { name: 'Activity Logs', icon: 'clock', route: '#logs', roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin', 'nationalWarehouseAdmin', 'regionalWarehouseAdmin', 'shiftSupervisor', 'supervisor', 'store', 'storeAdmin'] },
        { name: 'Help & Support', icon: 'help-circle', route: '#support', roles: ['ultimateAdmin', 'nationalAdmin', 'regionalAdmin', 'nationalWarehouseAdmin', 'regionalWarehouseAdmin', 'shiftSupervisor', 'supervisor', 'storeEmployee', 'employee', 'store', 'storeAdmin'] }
      ]
    }
  ];

  return groups.map(group => {
    const visibleItems = group.items
      .filter(item => {
        // Enforce role-based access
        if (item.roles && !item.roles.includes(role)) return false;
        // Enforce permission-based access (if defined)
        if (item.permission && !permissionStore.hasPermission(item.permission)) return false;
        return true;
      })
      .map(item => {
        const route = typeof item.route === 'function' ? item.route() : item.route;
        return {
          name: item.name,
          icon: item.icon,
          route
        };
      });

    return {
      title: group.title,
      items: visibleItems
    };
  }).filter(group => group.items.length > 0);
}
