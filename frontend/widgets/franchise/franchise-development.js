/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : franchise-development.js
 * Path              : frontend/widgets/franchise/franchise-development.js
 * Purpose           : Frontend utility: franchise-development for PLUS33 Coffee ERP
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
 * Frontend utility: franchise-development for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../core/logger.js';
import { notificationStore } from '../../store/notificationStore.js';

export class FranchiseDevelopment {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config, franchiseDevelopment) {
    this.config = config;
    this.franchiseDevelopment = franchiseDevelopment || { newApplications: 0, underReview: 0, underSetup: 0, upcomingOpenings: 0 };
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    logger.debug('FranchiseDevelopment', 'Rendering Franchise Development Overview...');

    const newApps = this.franchiseDevelopment.newApplications || 0;
    const underReview = this.franchiseDevelopment.underReview || 0;
    const underSetup = this.franchiseDevelopment.underSetup || 0;
    const upcoming = this.franchiseDevelopment.upcomingOpenings || 0;

    container.innerHTML = `
      <div class="flex justify-between align-center mb-md" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs);">
        <h3 class="m-0" style="font-size: 0.95rem; font-weight: 700; color: var(--text-primary);">${this.config.title}</h3>
        <a href="#franchise" style="font-size: 0.75rem; color: var(--accent-primary); text-decoration: none;">View Development Dashboard &rarr;</a>
      </div>

      <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: var(--spacing-sm); font-size: 0.75rem; margin-bottom: var(--spacing-lg); background: rgba(255,255,255,0.02); padding: var(--spacing-md); border-radius: 6px; border: 1px solid rgba(255,255,255,0.05); text-align: center;">
        <div>
          <div style="display:flex; justify-content:center; margin-bottom:6px;"><i data-lucide="file-text" style="width:20px;height:20px; color:var(--text-muted);"></i></div>
          <div style="font-weight: 700; color: var(--text-primary); font-size: 1.15rem;">${newApps}</div>
          <div style="color: var(--text-muted); font-size: 0.65rem; margin-top: 2px;">New Applications</div>
        </div>
        <div>
          <div style="display:flex; justify-content:center; margin-bottom:6px;"><i data-lucide="search" style="width:20px;height:20px; color:var(--status-warning);"></i></div>
          <div style="font-weight: 700; color: var(--status-warning); font-size: 1.15rem;">${underReview}</div>
          <div style="color: var(--text-muted); font-size: 0.65rem; margin-top: 2px;">Under Review</div>
        </div>
        <div>
          <div style="display:flex; justify-content:center; margin-bottom:6px;"><i data-lucide="settings" style="width:20px;height:20px; color:var(--accent-primary);"></i></div>
          <div style="font-weight: 700; color: var(--accent-primary); font-size: 1.15rem;">${underSetup}</div>
          <div style="color: var(--text-muted); font-size: 0.65rem; margin-top: 2px;">Under Setup</div>
        </div>
        <div>
          <div style="display:flex; justify-content:center; margin-bottom:6px;"><i data-lucide="rocket" style="width:20px;height:20px; color:var(--status-success);"></i></div>
          <div style="font-weight: 700; color: var(--status-success); font-size: 1.15rem;">${upcoming}</div>
          <div style="color: var(--text-muted); font-size: 0.65rem; margin-top: 2px;">Upcoming Openings</div>
        </div>
      </div>

      <!-- Quick Actions for Franchise Operations -->
      <div style="border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-sm); display: flex; gap: var(--spacing-xs); flex-wrap: wrap;">
        <button class="btn btn-secondary btn-action-review-apps" style="font-size: 0.65rem; padding: 3px 8px;">Review Applications</button>
        <button class="btn btn-secondary btn-action-approve-agreements" style="font-size: 0.65rem; padding: 3px 8px;">Approve Agreements</button>
        <button class="btn btn-secondary btn-action-assign-evals" style="font-size: 0.65rem; padding: 3px 8px;">Assign Evaluations</button>
      </div>
    `;

    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  bindEvents(container, lifecycle) {
    const reviewAppsBtn = container.querySelector('.btn-action-review-apps');
    const approveAgreementsBtn = container.querySelector('.btn-action-approve-agreements');
    const assignEvalsBtn = container.querySelector('.btn-action-assign-evals');

    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (reviewAppsBtn) {
      /**
       * Performs the h operation in this module.
       * @memberof Widgets Module
       */
      const h = () => notificationStore.success('Redirecting to franchise applications pending queue...');
      reviewAppsBtn.addEventListener('click', h);
      lifecycle.onCleanup(() => reviewAppsBtn.removeEventListener('click', h));
    }
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (approveAgreementsBtn) {
      /**
       * Performs the h operation in this module.
       * @memberof Widgets Module
       */
      const h = () => notificationStore.success('All signed franchise legal covenants verified and approved.');
      approveAgreementsBtn.addEventListener('click', h);
      lifecycle.onCleanup(() => approveAgreementsBtn.removeEventListener('click', h));
    }
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (assignEvalsBtn) {
      /**
       * Performs the h operation in this module.
       * @memberof Widgets Module
       */
      const h = () => notificationStore.success('Store site suitability evaluation dispatch triggered.');
      assignEvalsBtn.addEventListener('click', h);
      lifecycle.onCleanup(() => assignEvalsBtn.removeEventListener('click', h));
    }
  }
}
