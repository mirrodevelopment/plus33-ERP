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
 *
 * Description
 * ---------------------------------------------------------------------------
 * Menu structure provider for sidebar navigation in frontend/layouts/dashboard.js.
 * Features:
 *   - Reads active user role via authStore.getRole().
 *   - Dynamically constructs sidebar navigation sections for each role.
 *   - Points 'My Profile' links to correct role-specific routes (#national-profile, #regional-profile, #supervisor-profile, #employee-profile, #store-profile, #ultimate-profile).
 *   - Filters items against permissionStore.hasPermission() to enforce RBAC.
 ******************************************************************************/

import { permissionStore } from '../store/permissionStore.js';
import { authStore } from '../store/authStore.js';

/**
 * Dynamic menu configurations filtered on permission checks.
 */
export function getMenuItems() {
  const role = authStore.getRole();

  if (role === 'nationalAdmin') {
    return [
      {
        title: 'OPERATIONS',
        items: [
          { name: 'Regions Management', icon: 'map', route: '#regional-regions' },
          { name: 'Stores Management', icon: 'coffee', route: '#regional-stores' },
          { name: 'Warehouse Overview', icon: 'warehouse', route: '#regional-warehouse' },
          { name: 'Sales Overview', icon: 'chart', route: '#regional-sales' },
          { name: 'Inventory Overview', icon: 'package', route: '#regional-inventory' },
          { name: 'Supply Requests', icon: 'truck', route: '#regional-supply-requests' }
        ]
      },
      {
        title: 'COMMUNICATION',
        items: [
          { name: 'Broadcast Announcement', icon: 'send', route: '#regional-announcements' }
        ]
      },
      {
        title: 'WORKFORCE',
        items: [
          { name: 'Employee Overview', icon: 'users', route: '#regional-employees' },
          { name: 'Recruitment', icon: 'user-plus', route: '#regional-recruitment' },
          { name: 'Training & Development', icon: 'graduation-cap', route: '#regional-training' },
          { name: 'Performance', icon: 'star', route: '#regional-performance' }
        ]
      },
      {
        title: 'REPORTS & ANALYTICS',
        items: [
          { name: 'Reports', icon: 'bar-chart', route: '#regional-reports' },
          { name: 'Analytics', icon: 'activity', route: '#regional-analytics' },
          { name: 'Settings', icon: 'settings', route: '#regional-settings' },
          { name: 'My Profile', icon: 'user', route: '#national-profile' },
          { name: 'Help & Support', icon: 'help-circle', route: '#regional-help' }
        ]
      }
    ];
  }

  if (role === 'regionalAdmin') {
    return [
      {
        title: 'OPERATIONS',
        items: [
          { name: 'Regions Management', icon: 'map', route: '#regional-regions' },
          { name: 'Stores Management', icon: 'coffee', route: '#regional-stores' },
          { name: 'Warehouse Overview', icon: 'warehouse', route: '#regional-warehouse' },
          { name: 'Sales Overview', icon: 'chart', route: '#regional-sales' },
          { name: 'Inventory Overview', icon: 'package', route: '#regional-inventory' },
          { name: 'Supply Requests', icon: 'truck', route: '#regional-supply-requests' }
        ]
      },
      {
        title: 'COMMUNICATION',
        items: [
          { name: 'Broadcast Announcement', icon: 'send', route: '#regional-announcements' }
        ]
      },
      {
        title: 'WORKFORCE',
        items: [
          { name: 'Employee Overview', icon: 'users', route: '#regional-employees' },
          { name: 'Recruitment', icon: 'user-plus', route: '#regional-recruitment' },
          { name: 'Training & Development', icon: 'graduation-cap', route: '#regional-training' },
          { name: 'Performance', icon: 'star', route: '#regional-performance' }
        ]
      },
      {
        title: 'VENDORS & PROCUREMENT',
        items: [
          { name: 'Vendors', icon: 'truck', route: '#regional-vendors' },
          { name: 'Purchase Management', icon: 'shopping-bag', route: '#regional-purchase' },
          { name: 'Contracts', icon: 'file-text', route: '#regional-contracts' }
        ]
      },
      {
        title: 'COMPLIANCE & SUPPORT',
        items: [
          { name: 'Complaints Management', icon: 'message-square', route: '#regional-complaints' },
          { name: 'Legal Cases', icon: 'scale', route: '#regional-legal' },
          { name: 'Audit & Compliance', icon: 'shield', route: '#regional-audit' }
        ]
      },
      {
        title: 'REPORTS & ANALYTICS',
        items: [
          { name: 'Reports', icon: 'bar-chart', route: '#regional-reports' },
          { name: 'Analytics', icon: 'activity', route: '#regional-analytics' },
          { name: 'Settings', icon: 'settings', route: '#regional-settings' },
          { name: 'My Profile', icon: 'user', route: '#regional-profile' },
          { name: 'Help & Support', icon: 'help-circle', route: '#regional-help' }
        ]
      }
    ];
  }

  if (role === 'nationalWarehouseAdmin' || role === 'regionalWarehouseAdmin') {
    return [
      {
        title: 'OPERATIONS',
        items: [
          { name: 'Inventory Overview', icon: 'package', route: '#inventory' },
          { name: 'WMS Physical Topology', icon: 'map', route: '#warehouse' },
          { name: 'Supply Chain Overview', icon: 'truck', route: '#supply-chain' }
        ]
      },
      {
        title: 'REPORTS & ANALYTICS',
        items: [
          { name: 'Reports & Analytics', icon: 'chart', route: '#reports' },
          { name: 'Activity Logs', icon: 'clock', route: '#logs' }
        ]
      },
      {
        title: 'ADMINISTRATION',
        items: [
          { name: 'Users & Roles', icon: 'users', route: '#users' },
          { name: 'My Profile', icon: 'user', route: '#profile' }
        ]
      }
    ];
  }

  if (role === 'shiftSupervisor') {
    return [
      {
        title: 'SHIFT SUPERVISOR',
        items: [
          { name: 'Shift Dashboard', icon: 'bar-chart', route: '#supervisor-dashboard' },
          { name: 'Shift Planner', icon: 'calendar', route: '#shift-planner' },
          { name: 'Broadcast Announcement', icon: 'send', route: '#supervisor-announcements' },
          { name: 'My Roster', icon: 'calendar', route: '#schedule' },
          { name: 'Team Checklist', icon: 'check-square', route: '#tasks' },
          { name: 'Shift Attendance', icon: 'clock', route: '#attendance' },
          { name: 'Announcements', icon: 'megaphone', route: '#announcements' },
          { name: 'Documents Hub', icon: 'file-text', route: '#documents' },
          { name: 'Time Off Requests', icon: 'plane-takeoff', route: '#supervisor-leaves' }
        ]
      },
      {
        title: 'PERSONAL',
        items: [
          { name: 'Leave Management', icon: 'plane-takeoff', route: '#leave' },
          { name: 'My Profile', icon: 'user', route: '#supervisor-profile' },
          { name: 'Settings', icon: 'settings', route: '#settings' }
        ]
      }
    ];
  }

  if (role === 'storeEmployee') {
    return [
      {
        title: 'WORKPLACE',
        items: [
          { name: 'My Schedule', icon: 'calendar', route: '#schedule' },
          { name: 'My Tasks', icon: 'check-square', route: '#tasks' },
          { name: 'Attendance', icon: 'clock', route: '#attendance' },
          { name: 'Training', icon: 'graduation-cap', route: '#training' },
          { name: 'Performance', icon: 'star', route: '#performance' },
          { name: 'Leave', icon: 'plane-takeoff', route: '#leave' },
          { name: 'Announcements', icon: 'megaphone', route: '#announcements' },
          { name: 'Documents', icon: 'file-text', route: '#documents' },
          { name: 'My Profile', icon: 'user', route: '#employee-profile' },
          { name: 'Payslips', icon: 'banknote', route: '#payslips' },
          { name: 'Settings', icon: 'settings', route: '#settings' },
          { name: 'Help & Support', icon: 'help-circle', route: '#support' }
        ]
      }
    ];
  }

  if (role === 'store') {
    return [
      {
        title: 'OPERATIONS',
        items: [
          { name: 'Sales Overview', icon: 'chart', route: '#sales' }
        ]
      },
      {
        title: 'INVENTORY',
        items: [
          { name: 'Inventory Overview', icon: 'package', route: '#store-inventory' },
          { name: 'Daily Usage Tracker', icon: 'clipboard', route: '#store-usage' }
        ]
      },
      {
        title: 'ADMINISTRATION',
        items: [
          { name: 'Broadcast Announcement', icon: 'send', route: '#store-announcements' },
          { name: 'Leave Management', icon: 'calendar', route: '#store-leave' },
          { name: 'Workforce Overview', icon: 'users', route: '#store-workforce' },
          { name: 'Store Settings', icon: 'settings', route: '#store-settings' },
          { name: 'My Profile', icon: 'user', route: '#store-profile' }
        ]
      }
    ];
  }

  // Default / ultimateAdmin menus
  const groups = [
    {
      title: 'ADMINISTATION',
      items: [
        { name: 'National Management', icon: 'globe', route: '#national-management' },
        { name: 'Regions Management', icon: 'map', route: '#regions' },
        { name: 'Stores Management', icon: 'coffee', route: '#stores' },
        { name: 'Warehouses Management', icon: 'warehouse', route: '#warehouses' },
        { name: 'Users & Roles', icon: 'users', route: '#users' },
        { name: 'Roles & Permissions', icon: 'shield', route: '#permissions' },
        { name: 'System Settings', icon: 'settings', route: '#settings' },
        { name: 'My Profile', icon: 'user', route: '#ultimate-profile' }
      ]
    },
    {
      title: 'COMMUNICATION',
      items: [
        { name: 'Broadcast Announcement', icon: 'send', route: '#ultimate-announcements' }
      ]
    },
    {
      title: 'OPERATIONS',
      items: [
        { name: 'Sales Overview', icon: 'chart', route: '#sales' },
        { name: 'Inventory Overview', icon: 'package', route: '#inventory' },
        { name: 'Supply Chain Overview', icon: 'truck', route: '#supply-chain' },
        { name: 'Workforce Overview', icon: 'users', route: '#workforce' },
        { name: 'Customer Overview', icon: 'user', route: '#customers' }
      ]
    },
    {
      title: 'FINANCE',
      items: [
        { name: 'Financial Overview', icon: 'dollar', route: '#finance' },
        { name: 'Budget & Forecasting', icon: 'chart', route: '#budgets' },
        { name: 'Profitability Analysis', icon: 'chart', route: '#profitability' }
      ]
    },
    {
      title: 'COMPLIANCE & CONTROL',
      items: [
        { name: 'Audit & Compliance', icon: 'shield', route: '#audit' },
        { name: 'Legal & Cases', icon: 'scale', route: '#legal' },
        { name: 'Complaints Overview', icon: 'message', route: '#complaints' },
        { name: 'Policies & Documents', icon: 'file', route: '#policies' }
      ]
    },
    {
      title: 'SYSTEM & REPORTS',
      items: [
        { name: 'Reports & Analytics', icon: 'chart', route: '#reports' },
        { name: 'Data Insights', icon: 'chart', route: '#insights' },
        { name: 'Activity Logs', icon: 'clock', route: '#logs' }
      ]
    }
  ];

  // Filter menu items by checking current permissions
  return groups.map(group => {
    const visibleItems = group.items.filter(item => {
      if (!item.permission) return true;
      return permissionStore.hasPermission(item.permission);
    });
    return {
      title: group.title,
      items: visibleItems
    };
  }).filter(group => group.items.length > 0);
}
