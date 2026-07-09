/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Navigation Module
 * File              : menus.js
 * Path              : frontend/navigation/menus.js
 * Purpose           : Frontend utility: menus for PLUS33 Coffee ERP
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : store/permissionStore
 * Depends On        : store/permissionStore
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend utility: menus for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { permissionStore } from '../store/permissionStore.js';
import { authStore } from '../store/authStore.js';

/**
 * Dynamic menu configurations filtered on permission checks.
 */
export function getMenuItems() {
  const role = authStore.getRole();

  if (role === 'nationalAdmin' || role === 'regionalAdmin') {
    return [
      {
        title: 'OPERATIONS',
        items: [
          { name: 'Regions Management', icon: 'map', route: '#regions' },
          { name: 'Stores Management', icon: 'coffee', route: '#stores' },
          { name: 'Warehouse Overview', icon: 'warehouse', route: '#warehouse' },
          { name: 'Sales Overview', icon: 'chart', route: '#sales' },
          { name: 'Inventory Overview', icon: 'package', route: '#inventory' },
          { name: 'Supply Requests', icon: 'truck', route: '#supply-chain' }
        ]
      },
      {
        title: 'WORKFORCE',
        items: [
          { name: 'Employee Overview', icon: 'users', route: '#workforce' },
          { name: 'Recruitment', icon: 'user-plus', route: '#recruitment' },
          { name: 'Training & Development', icon: 'graduation-cap', route: '#training' },
          { name: 'Performance', icon: 'star', route: '#performance' }
        ]
      },
      {
        title: 'VENDORS & PROCUREMENT',
        items: [
          { name: 'Vendors', icon: 'truck', route: '#vendors' },
          { name: 'Purchase Management', icon: 'shopping-bag', route: '#purchases' },
          { name: 'Contracts', icon: 'file-text', route: '#contracts' }
        ]
      },
      {
        title: 'COMPLIANCE & SUPPORT',
        items: [
          { name: 'Complaints Management', icon: 'message-square', route: '#complaints' },
          { name: 'Legal Cases', icon: 'scale', route: '#legal' },
          { name: 'Audit & Compliance', icon: 'shield', route: '#audit' }
        ]
      },
      {
        title: 'REPORTS & ANALYTICS',
        items: [
          { name: 'Reports', icon: 'bar-chart', route: '#reports' },
          { name: 'Analytics', icon: 'activity', route: '#insights' },
          { name: 'Settings', icon: 'settings', route: '#settings' }
        ]
      }
    ];
  }

  if (role === 'warehouse') {
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
          { name: 'My Profile', icon: 'user', route: '#profile' },
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
          { name: 'My Profile', icon: 'user', route: '#profile' },
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
          { name: 'Inventory Overview', icon: 'package', route: '#inventory' },
          { name: 'Sales Overview', icon: 'chart', route: '#sales' }
        ]
      },
      {
        title: 'ADMINISTRATION',
        items: [
          { name: 'Broadcast Announcement', icon: 'send', route: '#store-announcements' },
          { name: 'Leave Management', icon: 'calendar', route: '#store-leave' },
          { name: 'My Profile', icon: 'user', route: '#profile' }
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
        { name: 'My Profile', icon: 'user', route: '#profile' }
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
