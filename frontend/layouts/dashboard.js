/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Layouts Module
 * File              : dashboard.js
 * Path              : frontend/layouts/dashboard.js
 * Purpose           : Frontend utility: dashboard for PLUS33 Coffee ERP
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : navigation/menus, navigation/breadcrumbs, store/authStore, store/themeStore, store/userStore
 * Depends On        : navigation/menus, navigation/breadcrumbs, store/authStore, store/themeStore, store/userStore
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend utility: dashboard for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { getMenuItems } from '../navigation/menus.js';
import { getBreadcrumbs } from '../navigation/breadcrumbs.js';
import { authStore } from '../store/authStore.js';
import { themeStore } from '../store/themeStore.js';
import { userStore } from '../store/userStore.js';
import { eventBus } from '../core/eventBus.js';
import { logger } from '../core/logger.js';

export const dashboardLayout = {
  /**
   * Performs the fn operation in this module.
   * @memberof Layouts Module
   */
  render(container) {
    const user = authStore.getUser();
    const profile = userStore.getProfile(user?.role);
    const activeTheme = themeStore.getTheme();
    
    container.innerHTML = `
      <div class="dashboard-shell">
        <!-- Sidebar Navigation -->
        <aside class="sidebar animate-slide-left" id="app-sidebar">
          <div class="sidebar-brand" style="gap: 10px; padding: var(--spacing-lg); border-bottom: 1px solid var(--border-color); display: flex; align-items: center;">
            <img src="imgs/logo-gold.png" alt="PLUS33 Logo" style="width: 22px; height: 22px; object-fit: contain; filter: drop-shadow(0 2px 4px rgba(0,0,0,0.2));">
            <span class="brand-text" style="font-family: var(--font-display); font-weight: 800; font-size: 1.15rem; color: var(--accent-primary); letter-spacing: 0.5px; white-space: nowrap;">PLUS33 Coffee</span>
          </div>
          
          <nav class="sidebar-nav" id="sidebar-nav-container">
            <!-- Sidebar menus injected here -->
          </nav>

          ${user?.role === 'store' ? `
          <div class="sidebar-shift-card" style="margin: var(--spacing-md) var(--spacing-sm); padding: var(--spacing-sm); background: rgba(0,0,0,0.25); border: 1px solid var(--border-color); border-radius: var(--radius-md); text-align: left; font-size: 0.72rem; display: flex; flex-direction: column; gap: 4px;">
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span style="color: var(--text-muted); font-weight: 700; text-transform: uppercase; font-size: 0.6rem; letter-spacing: 0.5px;">Current Shift</span>
              <span style="background: rgba(74,222,128,0.15); color: var(--status-success); padding: 1px 4px; border-radius: var(--radius-sm); font-size: 0.55rem; font-weight: 900;">Open</span>
            </div>
            <strong style="font-size: 0.8rem; color: var(--accent-primary); margin-top: 2px;">Morning Shift</strong>
            <span style="color: var(--text-muted); font-size: 0.65rem;">06:00 AM - 02:00 PM</span>
            <div style="font-size: 0.62rem; color: var(--text-muted); margin-top: 4px; display: flex; justify-content: space-between; align-items: center;">
              <span>Time Elapsed:<br><strong style="font-variant-numeric: tabular-nums; color: var(--text-primary);">04:35:20</strong></span>
              <button style="background: rgba(220,38,38,0.2); border: 1px solid var(--status-danger); color: var(--status-danger); padding: 2px 6px; border-radius: var(--radius-sm); font-size: 0.62rem; font-weight: 700; cursor: pointer; transition: var(--transition-fast);" onmouseover="this.style.background='var(--status-danger)'; this.style.color='#fff';" onmouseout="this.style.background='rgba(220,38,38,0.2)'; this.style.color='var(--status-danger)';" onclick="document.getElementById('btn-logout').click();">End Shift</button>
            </div>
          </div>
          ` : ''}
          
          <div class="sidebar-footer" style="padding: var(--spacing-sm) var(--spacing-md);">
            <a href="#profile" style="display: flex; align-items: center; gap: var(--spacing-sm); text-decoration: none; color: inherit; min-width: 0; flex: 1;">
              <img class="user-avatar" src="${profile.avatarUrl || 'imgs/male-avatar.png'}" alt="User Avatar" id="user-avatar-sidebar">
              <div class="user-info">
                <div class="user-name" id="user-name-sidebar">${profile.name}</div>
                <div class="user-role" id="user-role-sidebar">${user?.role || 'Guest'}</div>
              </div>
            </a>
            <button class="btn-logout" id="btn-logout" title="Log Out" style="background: none; border: none; color: var(--text-muted); cursor: pointer; display: flex; align-items: center; justify-content: center; width: 22px; height: 22px; transition: var(--transition-fast); margin-left: auto;" onmouseover="this.style.color='var(--accent-primary)'" onmouseout="this.style.color='var(--text-muted)'">
              <i data-lucide="log-out" style="width: 18px; height: 18px;"></i>
            </button>
          </div>
        </aside>
        
        <!-- Main Content Area -->
        <div class="main-wrapper">
          <header class="header" style="height: 70px; background-color: var(--bg-header); border-bottom: 1px solid var(--border-color); display: flex; align-items: center; justify-content: space-between; padding: 0 var(--spacing-lg);">
            <div class="header-left" style="display: flex; align-items: center; gap: var(--spacing-md); flex: 1;">
              <button class="btn-icon" id="sidebar-toggle" title="Toggle Navigation" style="margin-right: 4px;">
                <i data-lucide="menu" style="width: 20px; height: 20px;"></i>
              </button>
              <div class="header-search" style="display: flex; align-items: center; gap: var(--spacing-sm); background: rgba(0,0,0,0.15); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 6px 12px; width: 100%; max-width: 280px;">
                <i data-lucide="search" style="width: 14px; height: 14px; color: var(--text-muted);"></i>
                <input type="text" placeholder="Search enterprise resources..." style="background: none; border: none; outline: none; color: var(--text-primary); font-size: 0.8rem; width: 100%;">
              </div>
              <div class="breadcrumbs" id="breadcrumbs-container" style="display: flex; align-items: center; gap: var(--spacing-xs); font-size: 0.8rem; color: var(--text-muted); margin-left: 10px;">
                <!-- Breadcrumbs injected dynamically -->
              </div>
            </div>
            
            <div class="header-right" style="display: flex; align-items: center; gap: var(--spacing-md);">
              <div class="btn-icon" title="Notifications" style="position: relative; cursor: pointer;">
                <i data-lucide="bell" style="width: 18px; height: 18px;"></i>
                <span id="notification-badge" style="position: absolute; top: -5px; right: -5px; background: var(--accent-primary); color: #000; font-size: 0.65rem; padding: 1px 4px; border-radius: 50%; display: none; font-weight: bold;">0</span>
              </div>
            </div>
          </header>
          
          <!-- Page Mount Container -->
          <main class="content-area animate-fade-in" id="main-content">
            <!-- Dynamic pages injected here -->
          </main>
          
          <footer class="footer" style="padding: var(--spacing-sm); border-top: 1px solid var(--border-color); font-size: 0.8rem; text-align: center; justify-content: center; color: var(--text-muted);">
            <div>© 2026 PLUS33 Coffee Co. Enterprise</div>
          </footer>
        </div>
      </div>
      
      <!-- Toast Notification overlay container -->
      <div id="toast-container"></div>
    `;
    
    this.bindEvents();
    this.renderSidebarMenu();
    this.renderBreadcrumbs();
    
    /**
     * Performs the fn operation in this module.
     * @memberof Layouts Module
     */
    if (window.lucide) {
      window.lucide.createIcons();
    }
  },

  /**
   * Performs the fn operation in this module.
   * @memberof Layouts Module
   */
  bindEvents() {
    // 1. Sidebar Toggle
    const toggleBtn = document.getElementById('sidebar-toggle');
    const sidebar = document.getElementById('app-sidebar');
    /**
     * Performs the fn operation in this module.
     * @memberof Layouts Module
     */
    if (toggleBtn && sidebar) {
      toggleBtn.addEventListener('click', () => {
        const isCollapsed = sidebar.style.width === '0px';
        sidebar.style.width = isCollapsed ? '260px' : '0px';
        sidebar.style.minWidth = isCollapsed ? '260px' : '0px';
        sidebar.style.overflow = isCollapsed ? 'visible' : 'hidden';
      });
    }

    // 5. Logout
    const logoutBtn = document.getElementById('btn-logout');
    /**
     * Performs the fn operation in this module.
     * @memberof Layouts Module
     */
    if (logoutBtn) {
      logoutBtn.addEventListener('click', () => {
        authStore.logout();
        window.location.hash = '#login';
      });
    }

    const sidebarLogoutLink = document.getElementById('sidebar-logout-link');
    /**
     * Performs the fn operation in this module.
     * @memberof Layouts Module
     */
    if (sidebarLogoutLink) {
      sidebarLogoutLink.addEventListener('click', (e) => {
        e.preventDefault();
        authStore.logout();
        window.location.hash = '#login';
      });
    }

    // 6. Toast alerts listener
    eventBus.on('notification:toast', (notification) => {
      this.showToast(notification);
    });

    // 7. Router navigation tracker
    eventBus.on('router:navigated', () => {
      this.renderSidebarMenu();
      this.renderBreadcrumbs();
    });

    // 8. Dynamic user profile update listener
    eventBus.on('user:profile-updated', ({ role, profile }) => {
      const user = authStore.getUser();
      if (user && user.role === role) {
        const avatarEl = document.getElementById('user-avatar-sidebar');
        const nameEl = document.getElementById('user-name-sidebar');
        if (avatarEl) avatarEl.src = profile.avatarUrl || 'imgs/male-avatar.png';
        if (nameEl) nameEl.textContent = profile.name;
      }
    });
  },

  /**
   * Performs the fn operation in this module.
   * @memberof Layouts Module
   */
  renderSidebarMenu() {
    const navContainer = document.getElementById('sidebar-nav-container');
    if (!navContainer) return;
    
    const role = authStore.getRole();
    let dashboardRoute = '#dashboard';
    if (role === 'ultimateAdmin') dashboardRoute = '#ultimate-dashboard';
    else if (role === 'nationalAdmin') dashboardRoute = '#national-dashboard';
    else if (role === 'regionalAdmin') dashboardRoute = '#regional-dashboard';
    else if (role === 'warehouse') dashboardRoute = '#national-dashboard';
    else if (role === 'store') dashboardRoute = '#store-dashboard';
    else if (role === 'storeEmployee') dashboardRoute = '#employee-dashboard';

    const menuGroups = getMenuItems();
    const currentHash = window.location.hash || dashboardRoute;
    
    navContainer.innerHTML = `
      <div class="nav-group" style="margin-top: var(--spacing-sm);">
        <a class="nav-item ${currentHash === dashboardRoute ? 'active' : ''}" href="${dashboardRoute}">
          <span class="nav-item-icon" style="display: flex; align-items: center;">
            <i data-lucide="layout-dashboard" style="width: 16px; height: 16px; stroke-width: 2;"></i>
          </span>
          <span class="nav-item-text" style="font-weight: 700;">Dashboard</span>
        </a>
      </div>
      
      ${menuGroups.map(group => `
        <div class="nav-group">
          <div class="nav-group-title">${group.title}</div>
          ${group.items.map(item => `
            <a class="nav-item ${currentHash === item.route ? 'active' : ''}" href="${item.route}">
              <span class="nav-item-icon" style="display: flex; align-items: center;">
                <i data-lucide="${this.getLucideIconName(item.icon)}" style="width: 16px; height: 16px; stroke-width: 2;"></i>
              </span>
              <span class="nav-item-text">${item.name}</span>
            </a>
          `).join('')}
        </div>
      `).join('')}

      <!-- Footer Navigation Items -->
      <div class="nav-group" style="border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-sm); margin-top: var(--spacing-md);">
        <a class="nav-item ${currentHash === '#help' ? 'active' : ''}" href="#help">
          <span class="nav-item-icon" style="display: flex; align-items: center;">
            <i data-lucide="help-circle" style="width: 16px; height: 16px; stroke-width: 2;"></i>
          </span>
          <span class="nav-item-text">Help & Support</span>
        </a>
        <a class="nav-item" href="#logout" id="sidebar-logout-link-inner">
          <span class="nav-item-icon" style="display: flex; align-items: center;">
            <i data-lucide="log-out" style="width: 16px; height: 16px; stroke-width: 2;"></i>
          </span>
          <span class="nav-item-text">Logout</span>
        </a>
      </div>
    `;
    
    // Bind dynamic sidebar logout click listener
    const sidebarLogoutLink = document.getElementById('sidebar-logout-link-inner');
    /**
     * Performs the fn operation in this module.
     * @memberof Layouts Module
     */
    if (sidebarLogoutLink) {
      sidebarLogoutLink.addEventListener('click', (e) => {
        e.preventDefault();
        authStore.logout();
        window.location.hash = '#login';
      });
    }

    /**
     * Performs the fn operation in this module.
     * @memberof Layouts Module
     */
    if (window.lucide) {
      window.lucide.createIcons();
    }
  },

  /**
   * Performs the fn operation in this module.
   * @memberof Layouts Module
   */
  renderBreadcrumbs() {
    const crumbsContainer = document.getElementById('breadcrumbs-container');
    if (!crumbsContainer) return;
    
    const hash = window.location.hash || '#dashboard';
    const crumbs = getBreadcrumbs(hash);
    
    crumbsContainer.innerHTML = crumbs.map((c, index) => {
      const isLast = index === crumbs.length - 1;
      /**
       * Performs the fn operation in this module.
       * @memberof Layouts Module
       */
      if (isLast) {
        return `<span class="breadcrumb-item active">${c.name}</span>`;
      }
      return `
        <a class="breadcrumb-item" href="${c.route}" style="color: var(--text-muted); text-decoration: none;">${c.name}</a>
        <span class="breadcrumb-sep">/</span>
      `;
    }).join('');
  },

  /**
   * Performs the fn operation in this module.
   * @memberof Layouts Module
   */
  showToast(toast) {
    const container = document.getElementById('toast-container');
    if (!container) return;
    
    const toastEl = document.createElement('div');
    toastEl.id = toast.id;
    toastEl.className = `toast toast-${toast.type}`;
    toastEl.innerHTML = `
      <span class="toast-message">${toast.message}</span>
      <button class="toast-close">&times;</button>
    `;
    
    // Add close listener
    const closeBtn = toastEl.querySelector('.toast-close');
    closeBtn.addEventListener('click', () => {
      toastEl.remove();
    });
    
    container.appendChild(toastEl);
    
    // Auto dismiss sync removal event
    eventBus.on('notification:removed', (removedId) => {
      /**
       * Performs the fn operation in this module.
       * @memberof Layouts Module
       */
      if (removedId === toast.id && toastEl) {
        toastEl.remove();
      }
    });
  },

  /**
   * Performs the fn operation in this module.
   * @memberof Layouts Module
   */
  getThemeLabel(theme) {
    const labels = {
      'coffee-dark': 'Coffee Dark',
      'light': 'Light Minimal',
      'dark': 'Cyber Dark',
      'corporate': 'Corporate Blue',
      'france': 'France Edition',
      'charcoal': 'Charcoal Noir'
    };
    return labels[theme] || 'Theme';
  },

  /**
   * Performs the fn operation in this module.
   * @memberof Layouts Module
   */
  getThemeOptions(activeTheme) {
    const themes = [
      { id: 'coffee-dark', name: 'Coffee Dark', bg: '#141414', accent: '#c9a46a' },
      { id: 'light', name: 'Light Minimal', bg: '#ffffff', accent: '#2563eb' },
      { id: 'dark', name: 'Cyber Dark', bg: '#030712', accent: '#3b82f6' },
      { id: 'corporate', name: 'Corporate Blue', bg: '#0f172a', accent: '#3b82f6' },
      { id: 'france', name: 'France Edition', bg: '#081125', accent: '#0055a5' },
      { id: 'charcoal', name: 'Charcoal Noir', bg: '#121212', accent: '#e5e5e5' }
    ];

    return themes.map(t => {
      const isActive = t.id === activeTheme;
      return `
        <button class="theme-option-btn" data-theme-val="${t.id}" style="display:flex; align-items:center; justify-content:space-between; width:100%; padding:8px 10px; background:${isActive ? 'rgba(255,255,255,0.06)' : 'transparent'}; border:none; border-radius:var(--radius-md); color:${isActive ? 'var(--text-primary)' : 'var(--text-secondary)'}; font-size:0.75rem; font-weight:${isActive ? '700' : '500'}; text-align:left; cursor:pointer; margin-bottom:2px; transition:var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.04)'" onmouseout="this.style.background='${isActive ? 'rgba(255,255,255,0.06)' : 'transparent'}'">
          <div style="display:flex; align-items:center; gap:8px;">
            <span style="display:inline-block; width:12px; height:12px; border-radius:50%; background:${t.bg}; border:1px solid ${t.accent};"></span>
            <span>${t.name}</span>
          </div>
          ${isActive ? `<i data-lucide="check" style="width:12px; height:12px; color:var(--accent-primary);"></i>` : ''}
        </button>
      `;
    }).join('');
  },

  getLucideIconName(iconKey) {
    const icons = {
      'home': 'layout-dashboard',
      'layout': 'layout',
      'book-open': 'book-open',
      'package': 'package',
      'map': 'map',
      'coffee': 'coffee',
      'warehouse': 'building-2',
      'users': 'users',
      'shield': 'shield',
      'settings': 'settings',
      'chart': 'bar-chart-3',
      'truck': 'truck',
      'user': 'user',
      'dollar': 'dollar-sign',
      'scale': 'scale',
      'message': 'message-square',
      'file': 'file-text',
      'clock': 'clock',
      'help': 'help-circle',
      'logout': 'log-out',
      'plane-takeoff': 'plane'
    };
    return icons[iconKey] || iconKey || 'square';
  },

  /**
   * Performs the fn operation in this module.
   * @memberof Layouts Module
   */
  getIconSpan(iconName) {
    return '🔹';
  }
};

export default dashboardLayout;
