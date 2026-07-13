/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Layouts Module
 * File              : dashboard.js
 * Path              : frontend/layouts/dashboard.js
 * Purpose           : Dashboard layout controller — loads HTML, binds events
 * Version           : 2.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in layouts/html/dashboard-layout.html
 * This controller is responsible only for:
 *   - Loading the HTML template via htmlLoader
 *   - Populating dynamic data (user profile, menu, breadcrumbs)
 *   - Binding event listeners
 *   - Managing toast notifications
 ******************************************************************************/

import { htmlLoader } from '../core/htmlLoader.js';
import { getMenuItems } from '../navigation/menus.js';
import { getBreadcrumbs } from '../navigation/breadcrumbs.js';
import { authStore } from '../store/authStore.js';
import { themeStore } from '../store/themeStore.js';
import { userStore } from '../store/userStore.js';
import { eventBus } from '../core/eventBus.js';
import { logger } from '../core/logger.js';

/** Path to the dashboard layout HTML template */
const LAYOUT_TEMPLATE_URL = 'layouts/html/dashboard-layout.html';

export const dashboardLayout = {

  /**
   * Render the dashboard layout shell.
   * Loads the HTML template, populates dynamic values, and binds events.
   * @param {HTMLElement} container - The #app root element
   */
  async render(container) {
    // 1. Load and inject the HTML template
    await htmlLoader.inject(LAYOUT_TEMPLATE_URL, container);

    // 2. Populate dynamic values into the DOM
    this._populateUserProfile();
    this._renderSidebarMenu();
    this._renderBreadcrumbs();
    this._applyRoleVisibility();

    // 3. Bind all layout-level event listeners
    this._bindEvents();

    logger.debug('DashboardLayout', 'Layout rendered and events bound.');
  },

  // ---------------------------------------------------------------------------
  // PRIVATE: DOM Population
  // ---------------------------------------------------------------------------

  /**
   * Populate sidebar user profile section with current user data.
   */
  _populateUserProfile() {
    const user = authStore.getUser();
    const profile = userStore.getProfile(user?.role);

    const avatarEl = document.getElementById('user-avatar-sidebar');
    const nameEl = document.getElementById('user-name-sidebar');
    const roleEl = document.getElementById('user-role-sidebar');

    if (avatarEl) avatarEl.src = profile?.avatarUrl || 'imgs/male-avatar.png';
    if (nameEl) nameEl.textContent = profile?.name || 'User';
    if (roleEl) roleEl.textContent = user?.role || '—';

    // Populate header profile card
    const avatarHeader = document.getElementById('user-avatar-header');
    const nameHeader = document.getElementById('user-name-header');
    const roleHeader = document.getElementById('user-role-header');

    if (avatarHeader) avatarHeader.src = profile?.avatarUrl || 'imgs/male-avatar.png';
    if (nameHeader) nameHeader.textContent = profile?.name || 'User';
    if (roleHeader) roleHeader.textContent = user?.role || '—';
  },

  /**
   * Show/hide role-specific elements (e.g. shift card for store role).
   */
  _applyRoleVisibility() {
    const user = authStore.getUser();
    const shiftCard = document.getElementById('sidebar-shift-card');
    if (shiftCard) {
      shiftCard.hidden = user?.role !== 'store';
    }
  },

  /**
   * Render sidebar navigation menu items using DOM manipulation.
   * Reads from getMenuItems() based on current user role.
   */
  _renderSidebarMenu() {
    const navContainer = document.getElementById('sidebar-nav-container');
    if (!navContainer) return;

    const role = authStore.getRole();
    const dashboardRoute = this._getDashboardRoute(role);
    const currentHash = window.location.hash || dashboardRoute;
    const menuGroups = getMenuItems();

    // Clear previous content
    navContainer.replaceChildren();

    // Dashboard link (always first)
    navContainer.appendChild(this._createNavItem('layout-dashboard', 'Dashboard', dashboardRoute, currentHash));

    // Menu groups from navigation/menus.js
    menuGroups.forEach(group => {
      const groupEl = document.createElement('div');
      groupEl.className = 'nav-group';

      const titleEl = document.createElement('div');
      titleEl.className = 'nav-group-title';
      titleEl.textContent = group.title;
      groupEl.appendChild(titleEl);

      group.items.forEach(item => {
        const iconName = this._getLucideIconName(item.icon);
        groupEl.appendChild(this._createNavItem(iconName, item.name, item.route, currentHash));
      });

      navContainer.appendChild(groupEl);
    });

    // Footer nav items
    const footerGroup = document.createElement('div');
    footerGroup.className = 'nav-group nav-group--footer';
    footerGroup.appendChild(this._createNavItem('help-circle', 'Help & Support', '#help', currentHash));

    const logoutItem = this._createNavItem('log-out', 'Logout', '#logout', currentHash);
    logoutItem.id = 'sidebar-logout-link-inner';
    logoutItem.addEventListener('click', (e) => {
      e.preventDefault();
      authStore.logout();
      window.location.hash = '#login';
    });
    footerGroup.appendChild(logoutItem);
    navContainer.appendChild(footerGroup);

    // Re-init icons after DOM insertion
    if (window.lucide) window.lucide.createIcons();
  },

  /**
   * Create a single <a> nav item element.
   * @param {string} icon - Lucide icon name
   * @param {string} label - Link text
   * @param {string} route - Hash route
   * @param {string} currentHash - Active hash for highlighting
   * @returns {HTMLAnchorElement}
   */
  _createNavItem(icon, label, route, currentHash) {
    const a = document.createElement('a');
    a.className = `nav-item${currentHash === route ? ' active' : ''}`;
    a.href = route;

    const iconSpan = document.createElement('span');
    iconSpan.className = 'nav-item-icon';
    iconSpan.setAttribute('aria-hidden', 'true');
    const iconEl = document.createElement('i');
    iconEl.dataset.lucide = icon;
    iconSpan.appendChild(iconEl);

    const textSpan = document.createElement('span');
    textSpan.className = 'nav-item-text';
    textSpan.textContent = label;

    a.appendChild(iconSpan);
    a.appendChild(textSpan);
    return a;
  },

  /**
   * Render breadcrumb trail in the header.
   */
  _renderBreadcrumbs() {
    const crumbsContainer = document.getElementById('breadcrumbs-container');
    if (!crumbsContainer) return;

    const hash = window.location.hash || '#dashboard';
    const crumbs = getBreadcrumbs(hash);

    crumbsContainer.replaceChildren();

    crumbs.forEach((crumb, index) => {
      const isLast = index === crumbs.length - 1;

      if (isLast) {
        const span = document.createElement('span');
        span.className = 'breadcrumb-item active';
        span.textContent = crumb.name;
        crumbsContainer.appendChild(span);
      } else {
        const a = document.createElement('a');
        a.className = 'breadcrumb-item';
        a.href = crumb.route;
        a.textContent = crumb.name;
        crumbsContainer.appendChild(a);

        const sep = document.createElement('span');
        sep.className = 'breadcrumb-sep';
        sep.textContent = '/';
        crumbsContainer.appendChild(sep);
      }
    });
  },

  // ---------------------------------------------------------------------------
  // PRIVATE: Event Binding
  // ---------------------------------------------------------------------------

  /**
   * Bind all layout-level event listeners.
   * Called once after template is injected.
   */
  _bindEvents() {
    // Sidebar toggle
    const toggleBtn = document.getElementById('sidebar-toggle');
    const sidebar = document.getElementById('app-sidebar');
    if (toggleBtn && sidebar) {
      toggleBtn.addEventListener('click', () => {
        const isCollapsed = sidebar.classList.contains('sidebar--collapsed');
        sidebar.classList.toggle('sidebar--collapsed', !isCollapsed);
      });
    }

    // Logout button
    const logoutBtn = document.getElementById('btn-logout');
    if (logoutBtn) {
      logoutBtn.addEventListener('click', () => {
        authStore.logout();
        window.location.hash = '#login';
      });
    }

    // End shift button (store role)
    const endShiftBtn = document.getElementById('btn-end-shift');
    if (endShiftBtn) {
      endShiftBtn.addEventListener('click', () => {
        document.getElementById('btn-logout')?.click();
      });
    }

    // Toast notification listener
    eventBus.on('notification:toast', (notification) => {
      this._showToast(notification);
    });

    // Router navigation — re-render menu + breadcrumbs on each route change
    eventBus.on('router:navigated', () => {
      this._renderSidebarMenu();
      this._renderBreadcrumbs();
    });

    // User profile update listener
    eventBus.on('user:profile-updated', ({ role, profile }) => {
      const user = authStore.getUser();
      if (user?.role === role) {
        const avatarEl = document.getElementById('user-avatar-sidebar');
        const nameEl = document.getElementById('user-name-sidebar');
        if (avatarEl) avatarEl.src = profile.avatarUrl || 'imgs/male-avatar.png';
        if (nameEl) nameEl.textContent = profile.name;
      }
    });
  },

  // ---------------------------------------------------------------------------
  // PRIVATE: Toast
  // ---------------------------------------------------------------------------

  /**
   * Display a toast notification.
   * @param {{ id: string, type: string, message: string }} toast
   */
  _showToast(toast) {
    const container = document.getElementById('toast-container');
    if (!container) return;

    const toastEl = document.createElement('div');
    toastEl.id = toast.id;
    toastEl.className = `toast toast-${toast.type}`;

    const msg = document.createElement('span');
    msg.className = 'toast-message';
    msg.textContent = toast.message;

    const closeBtn = document.createElement('button');
    closeBtn.className = 'toast-close';
    closeBtn.type = 'button';
    closeBtn.setAttribute('aria-label', 'Dismiss notification');
    closeBtn.textContent = '×';
    closeBtn.addEventListener('click', () => toastEl.remove());

    toastEl.appendChild(msg);
    toastEl.appendChild(closeBtn);
    container.appendChild(toastEl);

    // Auto-remove on notification:removed event
    eventBus.on('notification:removed', (removedId) => {
      if (removedId === toast.id && toastEl.parentNode) {
        toastEl.remove();
      }
    });
  },

  // ---------------------------------------------------------------------------
  // PRIVATE: Utilities
  // ---------------------------------------------------------------------------

  /**
   * Resolve dashboard route for a given role.
   * @param {string} role
   * @returns {string} Hash route
   */
  _getDashboardRoute(role) {
    const routes = {
      ultimateAdmin: '#ultimate-dashboard',
      nationalAdmin: '#national-dashboard',
      regionalAdmin: '#regional-dashboard',
      warehouse: '#national-dashboard',
      store: '#store-dashboard',
      storeEmployee: '#employee-dashboard',
      shiftSupervisor: '#supervisor-dashboard'
    };
    return routes[role] || '#dashboard';
  },

  /**
   * Map legacy icon keys to Lucide icon names.
   * @param {string} iconKey
   * @returns {string}
   */
  _getLucideIconName(iconKey) {
    const icons = {
      home: 'layout-dashboard', layout: 'layout', 'book-open': 'book-open',
      package: 'package', map: 'map', coffee: 'coffee', warehouse: 'building-2',
      users: 'users', shield: 'shield', settings: 'settings', chart: 'bar-chart-3',
      truck: 'truck', user: 'user', dollar: 'dollar-sign', scale: 'scale',
      message: 'message-square', file: 'file-text', clock: 'clock',
      help: 'help-circle', logout: 'log-out', 'plane-takeoff': 'plane'
    };
    return icons[iconKey] || iconKey || 'square';
  },

  // Keep legacy non-prefixed names for router compatibility
  renderSidebarMenu() { return this._renderSidebarMenu(); },
  renderBreadcrumbs() { return this._renderBreadcrumbs(); },
  showToast(toast) { return this._showToast(toast); },
  getThemeLabel(theme) {
    const labels = {
      'coffee-dark': 'Coffee Dark', light: 'Light Minimal', dark: 'Cyber Dark',
      corporate: 'Corporate Blue', france: 'France Edition', charcoal: 'Charcoal Noir'
    };
    return labels[theme] || 'Theme';
  },
  getLucideIconName(key) { return this._getLucideIconName(key); }
};

export default dashboardLayout;
