/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Routing Module
 * File              : router.js
 * Path              : frontend/routing/router.js
 * Purpose           : Client-side hash-based router for the SPA frontend; manages route matching, authentication guards, permission validation, dynamic page module imports, layout shell lifecycle, and error page rendering (404, 403, 500).
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Core client-side router component for PLUS33 Coffee ERP.
 * Responsibilities:
 *   - Listens to window hashchange and load events to evaluate current location.
 *   - Automatically redirects generic #dashboard and #profile hashes to role-specific dashboard and profile page routes based on user role from authStore.
 *   - Enforces authentication requirement (requiresAuth) and role/permission checks (permissionStore) before loading target pages.
 *   - Destroys previous page lifecycle hooks and mounts new page component dynamically via dynamic import().
 *   - Renders 404 Not Found, 403 Access Denied, and 500 Interface Exception error overlays.
 ******************************************************************************/

import { routes, defaultRoute, loginRoute } from '../navigation/routes.js';
import { authStore } from '../store/authStore.js';
import { permissionStore } from '../store/permissionStore.js';
import { logger } from '../core/logger.js';
import { Lifecycle } from '../core/lifecycle.js';
import { eventBus } from '../core/eventBus.js';

class Router {
  /**
   * Performs the fn operation in this module.
   * @memberof Routing Module
   */
  constructor() {
    this.currentRoute = null;
    this.currentLayout = null;
    this.currentPageInstance = null;
    this.lifecycle = new Lifecycle();
    
    // Bind hashchange events
    window.addEventListener('hashchange', () => this.handleRouting());
    window.addEventListener('load', () => this.handleRouting());

    // Listen for session expiration events and redirect to login
    eventBus.on('auth:session-expired', () => {
      logger.info('Router', 'Session expired. Logging out and redirecting.');
      authStore.logout();
      window.location.hash = loginRoute;
    });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Routing Module
   */
  async handleRouting() {
    let hash = window.location.hash || defaultRoute;
    if (hash === '#dashboard') {
      if (authStore.isLoggedIn()) {
        const role = authStore.getRole();
        if (role === 'ultimateAdmin') hash = '#ultimate-dashboard';
        else if (role === 'nationalAdmin') hash = '#national-dashboard';
        else if (role === 'regionalAdmin') hash = '#regional-dashboard';
        else if (role === 'nationalWarehouseAdmin') hash = '#national-warehouse-dashboard';
        else if (role === 'regionalWarehouseAdmin') hash = '#regional-warehouse-dashboard';
        else if (role === 'store') hash = '#store-dashboard';
        else if (role === 'storeEmployee') hash = '#employee-dashboard';
        else if (role === 'shiftSupervisor') hash = '#supervisor-dashboard';
        window.location.hash = hash;
        return;
      } else {
        window.location.hash = loginRoute;
        return;
      }
    }
    if (hash === '#profile') {
      if (authStore.isLoggedIn()) {
        const role = authStore.getRole();
        let targetProfile = null;
        if (role === 'store' || role === 'storeAdmin') targetProfile = '#store-profile';
        else if (role === 'shiftSupervisor' || role === 'supervisor') targetProfile = '#supervisor-profile';
        else if (role === 'storeEmployee') targetProfile = '#employee-profile';
        else if (role === 'regionalAdmin') targetProfile = '#regional-profile';
        
        if (targetProfile && targetProfile !== hash) {
          window.location.hash = targetProfile;
          return;
        }
      }
    }
    logger.debug('Router', `Routing to hash: ${hash}`);
    
    const matchedRoute = routes.find(r => r.path === hash);
    /**
     * Performs the fn operation in this module.
     * @memberof Routing Module
     */
    if (!matchedRoute) {
      logger.warn('Router', `Route not found: ${hash}`);
      this.render404();
      return;
    }

    // 1. Auth Guard
    if (matchedRoute.requiresAuth && !authStore.isLoggedIn()) {
      logger.info('Router', 'Authentication required. Redirecting to login.');
      window.location.hash = loginRoute;
      return;
    }

    // 2. Permission Guard
    if (matchedRoute.permission && !permissionStore.hasPermission(matchedRoute.permission)) {
      logger.warn('Router', `Permission denied for route ${hash}`);
      this.render403();
      return;
    }

    // Clear old page lifecycle
    this.lifecycle.destroy();
    this.lifecycle = new Lifecycle();

    // 3. Load & Render Layout
    const layoutChanged = this.currentLayout !== matchedRoute.layout;
    /**
     * Performs the fn operation in this module.
     * @memberof Routing Module
     */
    if (layoutChanged) {
      await this.loadLayout(matchedRoute.layout);
    }

    // 4. Load & Render Page Dynamically
    try {
      const pageModule = await import(`../${matchedRoute.page}?v=${Date.now()}`);
      const pageContainer = document.getElementById('main-content') || document.getElementById('app');
      
      /**
       * Performs the fn operation in this module.
       * @memberof Routing Module
       */
      if (!pageContainer) {
        throw new Error('Content mount container not found in layout template.');
      }
      
      pageContainer.innerHTML = '';
      
      // Instantiate component
      const PageClass = pageModule.default || pageModule[Object.keys(pageModule)[0]];
      this.currentPageInstance = new PageClass();
      
      // Mount component and register its lifecycle hooks
      await this.currentPageInstance.mount(pageContainer, this.lifecycle);
      this.currentRoute = matchedRoute;
      
      logger.info('Router', `Successfully mounted page: ${matchedRoute.title}`);
      eventBus.emit('router:navigated', matchedRoute);
    } catch (err) {
      logger.error('Router', `Failed to load page module for "${hash}":`, err);
      this.render500(err);
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Routing Module
   */
  async loadLayout(layoutName) {
    logger.debug('Router', `Loading layout framework: ${layoutName}`);
    try {
      const layoutModule = await import(`../layouts/${layoutName}.js?v=${Date.now()}`);
      const layout = layoutModule.default || layoutModule[Object.keys(layoutModule)[0]];
      
      const appContainer = document.getElementById('app');
      layout.render(appContainer);
      this.currentLayout = layoutName;
    } catch (err) {
      logger.error('Router', `Failed to load layout framework "${layoutName}":`, err);
      throw err;
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Routing Module
   */
  render404() {
    const app = document.getElementById('app');
    app.innerHTML = `
      <div style="height: 100vh; display: flex; flex-direction: column; align-items: center; justify-content: center; background: var(--bg-app); color: var(--text-primary);">
        <h1 style="color: var(--accent-primary);">404</h1>
        <p>The requested enterprise resource could not be located.</p>
        <a href="${defaultRoute}" style="color: var(--accent-primary); text-decoration: none; border: 1px solid var(--border-color); padding: 0.5rem 1rem; border-radius: 4px;">Return Dashboard</a>
      </div>
    `;
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Routing Module
   */
  render403() {
    const app = document.getElementById('app');
    app.innerHTML = `
      <div style="height: 100vh; display: flex; flex-direction: column; align-items: center; justify-content: center; background: var(--bg-app); color: var(--text-primary);">
        <h1 style="color: var(--status-danger);">403</h1>
        <p>Access Denied. You do not possess clearance for this operation.</p>
        <a href="${defaultRoute}" style="color: var(--accent-primary); text-decoration: none; border: 1px solid var(--border-color); padding: 0.5rem 1rem; border-radius: 4px;">Return Dashboard</a>
      </div>
    `;
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Routing Module
   */
  render500(err) {
    const app = document.getElementById('app');
    app.innerHTML = `
      <div style="height: 100vh; display: flex; flex-direction: column; align-items: center; justify-content: center; background: var(--bg-app); color: var(--text-primary); padding: 2rem;">
        <h1 style="color: var(--status-danger);">500</h1>
        <p>A fatal interface compilation exception occurred.</p>
        <pre style="background: rgba(0,0,0,0.2); padding: 1rem; border-radius: 8px; border: 1px solid var(--border-color); max-width: 600px; overflow-x: auto; font-family: var(--font-mono); font-size: 0.8rem;">${err.message}\n${err.stack}</pre>
        <a href="${defaultRoute}" style="color: var(--accent-primary); text-decoration: none; border: 1px solid var(--border-color); padding: 0.5rem 1rem; border-radius: 4px; margin-top: 1rem;">Attempt Reload</a>
      </div>
    `;
  }
}

export const router = new Router();
export default router;
