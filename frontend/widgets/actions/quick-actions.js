/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : quick-actions.js
 * Path              : frontend/widgets/actions/quick-actions.js
 * Purpose           : Frontend utility: quick-actions for PLUS33 Coffee ERP
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/logger, store/notificationStore
 * Depends On        : core/logger, store/notificationStore
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend utility: quick-actions for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../core/logger.js';
import { notificationStore } from '../../store/notificationStore.js';

export class QuickActions {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config) {
    this.config = config;
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    logger.debug('QuickActions', 'Rendering Quick Actions panel...');

    container.innerHTML = `
      <div class="flex justify-between align-center mb-md" style="border-bottom: 1px solid var(--border-color); padding-bottom: var(--spacing-xs);">
        <h3 class="m-0" style="font-size: 0.95rem; font-weight: 800; color: var(--text-primary); text-transform: uppercase; letter-spacing: 0.05em;">${this.config.title}</h3>
      </div>

      <div class="qa-grid">
        <button class="qa-btn btn-qa-apps" type="button" aria-label="Review Franchise Applications">
          <i data-lucide="file-text" aria-hidden="true"></i>
          <span>Review Franchise Applications</span>
        </button>
        <button class="qa-btn btn-qa-budgets" type="button" aria-label="Approve Budgets">
          <i data-lucide="wallet" aria-hidden="true"></i>
          <span>Approve Budgets</span>
        </button>
        <button class="qa-btn btn-qa-vendors" type="button" aria-label="Approve Vendors">
          <i data-lucide="users" aria-hidden="true"></i>
          <span>Approve Vendors</span>
        </button>
        <button class="qa-btn btn-qa-audit" type="button" aria-label="Create Audit">
          <i data-lucide="shield" aria-hidden="true"></i>
          <span>Create Audit</span>
        </button>
        <button class="qa-btn btn-qa-visit" type="button" aria-label="Schedule Surprise Visit">
          <i data-lucide="bell-ring" aria-hidden="true"></i>
          <span>Schedule Surprise Visit</span>
        </button>
        <button class="qa-btn btn-qa-reports" type="button" aria-label="View Executive Reports">
          <i data-lucide="bar-chart-2" aria-hidden="true"></i>
          <span>View Executive Reports</span>
        </button>
      </div>
    `;

    if (window.lucide) {
      window.lucide.createIcons();
    }

    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  bindEvents(container, lifecycle) {
    const appsBtn = container.querySelector('.btn-qa-apps');
    const budgetsBtn = container.querySelector('.btn-qa-budgets');
    const vendorsBtn = container.querySelector('.btn-qa-vendors');
    const auditBtn = container.querySelector('.btn-qa-audit');
    const visitBtn = container.querySelector('.btn-qa-visit');
    const reportsBtn = container.querySelector('.btn-qa-reports');

    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (appsBtn) {
      /**
       * Performs the h operation in this module.
       * @memberof Widgets Module
       */
      const h = () => notificationStore.success('Opening Franchise Application reviewer panel...');
      appsBtn.addEventListener('click', h);
      lifecycle.onCleanup(() => appsBtn.removeEventListener('click', h));
    }
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (budgetsBtn) {
      /**
       * Performs the h operation in this module.
       * @memberof Widgets Module
       */
      const h = () => notificationStore.success('Opening budget pending approvals workspace...');
      budgetsBtn.addEventListener('click', h);
      lifecycle.onCleanup(() => budgetsBtn.removeEventListener('click', h));
    }
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (vendorsBtn) {
      /**
       * Performs the h operation in this module.
       * @memberof Widgets Module
       */
      const h = () => notificationStore.success('Opening vendor validation queue...');
      vendorsBtn.addEventListener('click', h);
      lifecycle.onCleanup(() => vendorsBtn.removeEventListener('click', h));
    }
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (auditBtn) {
      /**
       * Performs the h operation in this module.
       * @memberof Widgets Module
       */
      const h = () => notificationStore.success('Initializing new compliance audit scheduler...');
      auditBtn.addEventListener('click', h);
      lifecycle.onCleanup(() => auditBtn.removeEventListener('click', h));
    }
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (visitBtn) {
      /**
       * Performs the h operation in this module.
       * @memberof Widgets Module
       */
      const h = () => notificationStore.success('Surprise compliance check visits generated for underperforming locations.');
      visitBtn.addEventListener('click', h);
      lifecycle.onCleanup(() => visitBtn.removeEventListener('click', h));
    }
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (reportsBtn) {
      /**
       * Performs the h operation in this module.
       * @memberof Widgets Module
       */
      const h = () => notificationStore.success('Loading consolidated enterprise growth reporting dashboard...');
      reportsBtn.addEventListener('click', h);
      lifecycle.onCleanup(() => reportsBtn.removeEventListener('click', h));
    }
  }
}
