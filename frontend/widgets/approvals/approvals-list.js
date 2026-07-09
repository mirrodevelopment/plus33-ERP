/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : approvals-list.js
 * Path              : frontend/widgets/approvals/approvals-list.js
 * Purpose           : Frontend utility: approvals-list for PLUS33 Coffee ERP
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : store/notificationStore
 * Depends On        : store/notificationStore
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend utility: approvals-list for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { notificationStore } from '../../store/notificationStore.js';

export class ApprovalsList {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config, alerts) {
    this.config = config;
    // alerts from backend contains system alerts which we enrich as approvals
    this.alerts = alerts || [];
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    // Combine backend alert data + standard ERP pending approvals
    const pendingApprovals = [
      { id: 1, type: 'Budget Approvals', detail: 'Marketing Spend Q3 2026', count: 12, icon: 'banknote', color: 'var(--status-warning)' },
      { id: 2, type: 'Store Openings', detail: 'Green Park Café, London', count: 8, icon: 'coffee', color: 'var(--accent-primary)' },
      { id: 3, type: 'Vendor Approvals', detail: 'Bean supplies — Arabica Premium deal', count: 15, icon: 'package', color: 'var(--status-info)' },
      { id: 4, type: 'Policy Approvals', detail: 'Zero-trust attestation policy v3', count: 6, icon: 'shield', color: 'var(--status-success)' },
      { id: 5, type: 'Discrepancy Requests', detail: 'WMS inventory zone C override', count: 4, icon: 'alert-triangle', color: 'var(--status-danger)' },
      { id: 6, type: 'Emergency Requests', detail: 'Regional stock redistribution', count: 3, icon: 'zap', color: 'var(--status-danger)' }
    ];

    // Enrich count from backend alert data if available
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (this.alerts.length > 0) {
      this.alerts.forEach((alert, i) => {
        /**
         * Performs the fn operation in this module.
         * @memberof Widgets Module
         */
        if (pendingApprovals[i]) {
          pendingApprovals[i].detail = alert.message || pendingApprovals[i].detail;
        }
      });
    }

    container.innerHTML = `
      <div class="flex justify-between align-center mb-md" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs);">
        <h3 class="m-0" style="font-size: 0.95rem; font-weight: 700; color: var(--text-primary);">${this.config.title}</h3>
        <a href="#approvals" style="font-size: 0.75rem; color: var(--accent-primary); text-decoration: none;">View All</a>
      </div>
      
      <div class="flex flex-col gap-sm">
        ${pendingApprovals.map(a => `
          <div class="approval-item flex justify-between align-center" style="border: 1px solid rgba(255,255,255,0.05); border-radius: var(--radius-md); background: rgba(255,255,255,0.01); padding: 7px 10px; cursor: pointer; transition: var(--transition-fast); min-width: 0; gap: var(--spacing-sm);" onmouseover="this.style.background='rgba(255,255,255,0.03)'" onmouseout="this.style.background='rgba(255,255,255,0.01)'">
            <div class="flex align-center gap-sm" style="min-width: 0; flex-grow: 1;">
              <i data-lucide="${a.icon}" style="width:13px; height:13px; color:${a.color}; flex-shrink:0;"></i>
              <div class="flex flex-col" style="min-width: 0;">
                <span style="font-size: 0.75rem; color: var(--text-primary); font-weight: 600; text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">${a.type}</span>
                <span style="font-size: 0.65rem; color: var(--text-muted); text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">${a.detail}</span>
              </div>
            </div>
            <div class="flex align-center gap-sm" style="flex-shrink: 0;">
              <span style="font-size: 0.68rem; background: rgba(201,164,106,0.1); color: var(--accent-primary); border: 1px solid rgba(201,164,106,0.25); padding: 2px 8px; border-radius: var(--radius-full); font-weight: 700; white-space:nowrap;">
                ${a.count} pending
              </span>
              <button class="btn-approve-action" data-id="${a.id}" style="background: none; border: none; color: var(--status-success); cursor: pointer; font-size: 0.85rem; padding: 0; display:flex; align-items:center;">
                <i data-lucide="check-circle" style="width:16px; height:16px;"></i>
              </button>
            </div>
          </div>
        `).join('')}
      </div>
    `;

    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  bindEvents(container, lifecycle) {
    const approveButtons = container.querySelectorAll('.btn-approve-action');
    approveButtons.forEach(btn => {
      /**
       * Handles the handler event or exception in the business workflow.
       * @memberof Widgets Module
       */
      const handler = (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        notificationStore.success(`Workflow request #${id} approved successfully!`);
        btn.closest('.approval-item').style.opacity = '0.4';
        btn.closest('.approval-item').style.pointerEvents = 'none';
      };
      btn.addEventListener('click', handler);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
    });
  }
}
