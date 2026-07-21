/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Layouts Module
 * File              : dashboard.js
 * Path              : frontend/layouts/dashboard.js
 * Purpose           : Main layout shell controller for all authenticated ERP dashboards; handles dynamic HTML template loading, role-based profile routing, sidebar navigation, header breadcrumbs, user status updates, notification bells/badges, and toast popups.
 * Version           : 2.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend layout manager for the SPA shell.
 * Key capabilities:
 *   - Renders the dashboard shell layout from layouts/html/dashboard-layout.html.
 *   - Dynamically resolves user role (ultimateAdmin, nationalAdmin, regionalAdmin, storeAdmin, shiftSupervisor, storeEmployee) to map topbar and sidebar profile card clicks directly to role-specific profile page hash routes (#ultimate-profile, #national-profile, #regional-profile, #store-profile, #supervisor-profile, #employee-profile).
 *   - Builds dynamic sidebar navigation menus from navigation/menus.js with role filtering and Lucide icons.
 *   - Manages topbar breadcrumbs trail based on window.location.hash.
 *   - Handles real-time eventBus listeners for user profile changes, authentication state changes, document pending approval count badges, and toast messages.
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

    // 3. Bind all layout-level event listeners
    this._bindEvents();

    logger.debug('DashboardLayout', 'Layout rendered and events bound.');
  },

  // ---------------------------------------------------------------------------
  // PRIVATE: DOM Population
  // ---------------------------------------------------------------------------

  /**
   * Resolve appropriate profile route hash based on user role.
   */
  _getProfileHashForUser() {
    const role = authStore.getRole();
    switch (role) {
      case 'ultimateAdmin':
        return '#ultimate-profile';
      case 'nationalAdmin':
        return '#national-profile';
      case 'regionalAdmin':
        return '#regional-profile';
      case 'storeAdmin':
      case 'store':
        return '#store-profile';
      case 'shiftSupervisor':
      case 'supervisor':
        return '#supervisor-profile';
      case 'storeEmployee':
        return '#employee-profile';
      case 'nationalWarehouseAdmin':
      case 'regionalWarehouseAdmin':
      default:
        return '#profile';
    }
  },


  /**
   * Populate sidebar user profile section with current user data.
   */
  _populateUserProfile() {
    const user = authStore.getUser();
    const profile = userStore.getProfile(user?.role);

    const avatarUrl = profile?.avatarUrl
      ? (profile.avatarUrl.includes('unsplash.com') ? profile.avatarUrl : `${profile.avatarUrl}?t=${Date.now()}`)
      : 'imgs/male-avatar.png';

    const targetHash = this._getProfileHashForUser();

    const avatarEl = document.getElementById('user-avatar-sidebar');
    const nameEl = document.getElementById('user-name-sidebar');
    const roleEl = document.getElementById('user-role-sidebar');
    const sidebarLink = document.getElementById('sidebar-profile-link');

    const formatRoleName = (r) => {
      if (!r) return '—';
      if (r === 'nationalAdmin') return 'National Admin';
      if (r === 'regionalAdmin') return 'Regional Admin';
      if (r === 'storeAdmin') return 'Store Manager';
      if (r === 'supervisor') return 'Shift Lead';
      if (r === 'storeEmployee') return 'Barista / Employee';
      if (r === 'ultimateAdmin') return 'Ultimate Admin';
      return r.replace(/([a-z])([A-Z])/g, '$1 $2').replace('_', ' ');
    };

    const navigateToProfile = (e) => {
      if (e) {
        e.preventDefault();
        e.stopPropagation();
      }
      const hash = this._getProfileHashForUser();
      logger.info('DashboardLayout', `Navigating to profile page: ${hash}`);
      window.location.hash = hash;
    };

    if (avatarEl) {
      avatarEl.src = avatarUrl;
      avatarEl.style.cursor = 'pointer';
      avatarEl.onclick = navigateToProfile;
    }
    if (nameEl) {
      nameEl.textContent = profile?.name || 'User';
      nameEl.style.cursor = 'pointer';
      nameEl.onclick = navigateToProfile;
    }
    if (roleEl) {
      roleEl.textContent = formatRoleName(user?.role);
      roleEl.style.cursor = 'pointer';
      roleEl.onclick = navigateToProfile;
    }
    if (sidebarLink) {
      sidebarLink.href = targetHash;
      sidebarLink.onclick = navigateToProfile;
    }

    // Populate header profile card
    const avatarHeader = document.getElementById('user-avatar-header');
    const nameHeader = document.getElementById('user-name-header');
    const roleHeader = document.getElementById('user-role-header');
    const headerProfileCard = document.getElementById('header-profile-card');

    if (avatarHeader) {
      avatarHeader.src = avatarUrl;
      avatarHeader.style.cursor = 'pointer';
      avatarHeader.title = 'Click to view profile page';
      avatarHeader.onclick = navigateToProfile;
    }
    if (nameHeader) {
      nameHeader.textContent = profile?.name || 'User';
      nameHeader.style.cursor = 'pointer';
      nameHeader.onclick = navigateToProfile;
    }
    if (roleHeader) {
      roleHeader.textContent = formatRoleName(user?.role);
      roleHeader.style.cursor = 'pointer';
      roleHeader.onclick = navigateToProfile;
    }
    if (headerProfileCard) {
      headerProfileCard.style.cursor = 'pointer';
      headerProfileCard.title = 'Click to view profile page';
      headerProfileCard.onclick = navigateToProfile;
    }
  },

  /**
   * Show/hide role-specific elements (e.g. shift card for store role).
   */


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

    // Profile Top Bar & Avatar Click -> Open User Profile Page for ALL users
    const navigateToProfile = (e) => {
      if (e) {
        e.preventDefault();
        e.stopPropagation();
      }
      const targetHash = this._getProfileHashForUser();
      logger.info('DashboardLayout', `Navigating user to profile page: ${targetHash}`);
      window.location.hash = targetHash;
    };

    // Attach explicit click handlers to topbar and sidebar profile elements
    const profileSelectors = [
      '#user-avatar-header',
      '#user-avatar-sidebar',
      '#header-profile-card',
      '#user-name-header',
      '#user-role-header',
      '#user-name-sidebar',
      '#user-role-sidebar',
      '#sidebar-profile-link',
      '.user-avatar',
      '.header-profile',
      '#header-user-profile',
      '.user-profile-widget'
    ];

    profileSelectors.forEach(selector => {
      document.querySelectorAll(selector).forEach(el => {
        el.style.cursor = 'pointer';
        el.title = 'Click to open profile page';
        el.addEventListener('click', navigateToProfile);
      });
    });

    // Global capture-phase click delegation listener for any profile image/card elements across all pages
    document.addEventListener('click', (e) => {
      const target = e.target.closest('#user-avatar-header, #user-avatar-sidebar, #header-profile-card, #user-name-header, #user-role-header, .user-avatar, .header-profile, #header-user-profile, .user-profile-widget, #sidebar-profile-link, .sidebar-footer');
      if (target && !e.target.closest('#btn-logout')) {
        navigateToProfile(e);
      }
    }, true);



    // Toast notification listener
    eventBus.on('notification:toast', (notification) => {
      this._showToast(notification);
    });

    // Bell notification panel toggle
    const bellBtn = document.getElementById('btn-notifications');
    const notifPanel = document.getElementById('notification-panel');
    if (bellBtn && notifPanel) {
      bellBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        const isHidden = notifPanel.hidden;
        notifPanel.hidden = !isHidden;
      });

      // Close panel when clicking outside
      document.addEventListener('click', (e) => {
        if (!document.getElementById('notification-wrapper')?.contains(e.target)) {
          if (notifPanel) notifPanel.hidden = true;
        }
      });

      // Clear all button
      const clearBtn = document.getElementById('btn-clear-notifications');
      if (clearBtn) {
        clearBtn.addEventListener('click', () => {
          eventBus.emit('workforce:pending-docs', { count: 0 });
          notifPanel.hidden = true;
        });
      }
    }

    // Router navigation — re-render menu + breadcrumbs on each route change
    eventBus.on('router:navigated', () => {
      this._renderSidebarMenu();
      this._renderBreadcrumbs();
    });

    // User profile update listener
    eventBus.on('user:profile-updated', (data) => {
      const profile = data?.profile || data || {};
      const displayName = profile.name || `${profile.firstName || ''} ${profile.lastName || ''}`.trim() || 'User Profile';

      const avatarEl = document.getElementById('user-avatar-sidebar');
      const avatarHeader = document.getElementById('user-avatar-header');
      const nameEl = document.getElementById('user-name-sidebar');
      const nameHeader = document.getElementById('user-name-header');
      const welcomeName = document.getElementById('header-user-name');

      let avatarUrl = profile.avatarUrl;
      const gender = (profile.gender || 'Male').toLowerCase();

      if (!avatarUrl || avatarUrl === 'imgs/male-avatar.png' || avatarUrl === 'imgs/female-avatar.jpg') {
        avatarUrl = (gender === 'female') ? 'imgs/female-avatar.jpg' : 'imgs/male-avatar.png';
      } else if (!avatarUrl.includes('unsplash.com') && !avatarUrl.startsWith('http') && !avatarUrl.includes('?')) {
        avatarUrl = `${avatarUrl}?t=${Date.now()}`;
      }

      if (avatarEl) avatarEl.src = avatarUrl;
      if (avatarHeader) avatarHeader.src = avatarUrl;
      if (nameEl) nameEl.textContent = displayName;
      if (nameHeader) nameHeader.textContent = displayName;
      if (welcomeName) welcomeName.textContent = displayName;
    });

    eventBus.on('auth:state-changed', ({ user }) => {
      if (user && user.name) {
        const nameEl = document.getElementById('user-name-sidebar');
        const nameHeader = document.getElementById('user-name-header');
        const welcomeName = document.getElementById('header-user-name');
        if (nameEl) nameEl.textContent = user.name;
        if (nameHeader) nameHeader.textContent = user.name;
        if (welcomeName) welcomeName.textContent = user.name;
      }
    });

    // Workforce pending documents badge — updates sidebar nav dot and bell count
    eventBus.on('workforce:pending-docs', ({ count }) => {
      this._updateWorkforcePendingBadge(count);
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
  // PRIVATE: Workforce Pending Badge
  // ---------------------------------------------------------------------------

  /**
   * Shows or hides the pending-docs dot on the Workforce nav item
   * and updates the global notification bell badge in the top header.
   * @param {number} count - Number of pending document approvals
   */
  _updateWorkforcePendingBadge(count) {
    // --- Inject dot styles once ---
    if (!document.getElementById('workforce-dot-styles')) {
      const style = document.createElement('style');
      style.id = 'workforce-dot-styles';
      style.textContent = `
        .nav-item--has-badge { position: relative; }
        .nav-item--has-badge .nav-pending-dot {
          position: absolute;
          top: 6px;
          right: 6px;
          width: 8px;
          height: 8px;
          border-radius: 50%;
          background: #f59e0b;
          box-shadow: 0 0 0 2px rgba(245, 158, 11, 0.3);
          animation: navDotPulse 1.8s ease-in-out infinite;
        }
        @keyframes navDotPulse {
          0%, 100% { box-shadow: 0 0 0 2px rgba(245, 158, 11, 0.35); }
          50% { box-shadow: 0 0 0 5px rgba(245, 158, 11, 0.0); }
        }
        .notif-item {
          display: flex;
          align-items: flex-start;
          gap: 10px;
          padding: 0.65rem 1rem;
          border-bottom: 1px solid rgba(255,255,255,0.05);
          cursor: pointer;
          transition: background 0.15s;
        }
        .notif-item:hover { background: rgba(255,255,255,0.05); }
        .notif-item:last-child { border-bottom: none; }
        .notif-item-icon {
          width: 28px;
          height: 28px;
          border-radius: 8px;
          background: rgba(245, 158, 11, 0.15);
          display: flex;
          align-items: center;
          justify-content: center;
          flex-shrink: 0;
          color: #f59e0b;
        }
        .notif-item-body { flex: 1; }
        .notif-item-title { font-size: 0.82rem; font-weight: 600; color: #fff; margin-bottom: 2px; }
        .notif-item-sub { font-size: 0.72rem; color: rgba(255,255,255,0.5); }
        .notif-empty { padding: 1.5rem 1rem; text-align: center; color: rgba(255,255,255,0.4); font-size: 0.82rem; }
      `;
      document.head.appendChild(style);
    }

    // --- Find the Workforce nav item (route = #store-workforce) ---
    const nav = document.getElementById('sidebar-nav-container');
    if (nav) {
      const workforceLink = nav.querySelector('a[href="#store-workforce"]');
      if (workforceLink) {
        // Remove any existing dot
        workforceLink.querySelector('.nav-pending-dot')?.remove();
        workforceLink.classList.remove('nav-item--has-badge');

        if (count > 0) {
          workforceLink.classList.add('nav-item--has-badge');
          const dot = document.createElement('span');
          dot.className = 'nav-pending-dot';
          dot.setAttribute('title', `${count} pending document approval${count > 1 ? 's' : ''}`);
          workforceLink.appendChild(dot);
        }
      }
    }

    // --- Update bell notification badge ---
    const bellBadge = document.getElementById('notification-badge');
    if (bellBadge) {
      if (count > 0) {
        bellBadge.hidden = false;
        bellBadge.textContent = count;
      } else {
        bellBadge.hidden = true;
        bellBadge.textContent = '0';
      }
    }

    // --- Populate notification panel items ---
    const itemsList = document.getElementById('notification-items-list');
    if (itemsList) {
      itemsList.replaceChildren();
      if (count > 0) {
        const item = document.createElement('div');
        item.className = 'notif-item';
        item.innerHTML = `
          <div class="notif-item-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24"
                 fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
              <polyline points="14 2 14 8 20 8"/>
              <line x1="16" y1="13" x2="8" y2="13"/>
              <line x1="16" y1="17" x2="8" y2="17"/>
            </svg>
          </div>
          <div class="notif-item-body">
            <div class="notif-item-title">
              ${count} Document${count > 1 ? 's' : ''} Pending Approval
            </div>
            <div class="notif-item-sub">Workforce → Verification Documents</div>
          </div>
        `;
        item.addEventListener('click', () => {
          window.location.hash = '#store-workforce';
          const panel = document.getElementById('notification-panel');
          if (panel) panel.hidden = true;
        });
        itemsList.appendChild(item);
      } else {
        const empty = document.createElement('div');
        empty.className = 'notif-empty';
        empty.textContent = 'No pending notifications';
        itemsList.appendChild(empty);
      }
    }

    logger.debug('DashboardLayout', `Workforce pending badge updated: ${count}`);
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
