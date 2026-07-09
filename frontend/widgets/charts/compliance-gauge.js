/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : compliance-gauge.js
 * Path              : frontend/widgets/charts/compliance-gauge.js
 * Purpose           : Frontend utility: compliance-gauge for PLUS33 Coffee ERP
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/logger
 * Depends On        : core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend utility: compliance-gauge for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../core/logger.js';

export class ComplianceGauge {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config, complianceOverview) {
    this.config = config;
    this.complianceOverview = complianceOverview || { complianceScore: 0, auditsCompleted: 0, correctiveActionsOpen: 0, overdueActions: 0 };
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    logger.debug('ComplianceGauge', 'Rendering compliance radial gauge...');

    const score = Number(this.complianceOverview.complianceScore || 0).toFixed(1);

    container.innerHTML = `
      <div class="flex justify-between align-center mb-md" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs);">
        <h3 class="m-0" style="font-size: 0.95rem; font-weight: 700; color: var(--text-primary);">${this.config.title}</h3>
        <a href="#compliance" style="font-size: 0.75rem; color: var(--accent-primary); text-decoration: none; display:flex; align-items:center; gap:4px;">
          <i data-lucide="external-link" style="width:12px; height:12px;"></i> View All
        </a>
      </div>

      <div style="display: flex; gap: var(--spacing-lg); align-items: center; justify-content: space-between;">
        <!-- Left: Gauge/Progress ring -->
        <div style="position: relative; width: 100px; height: 100px; flex-shrink: 0; display: flex; align-items: center; justify-content: center;">
          <svg width="100" height="100" viewBox="0 0 36 36">
            <path stroke="rgba(255,255,255,0.04)" stroke-width="4" fill="none" d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" />
            <path stroke="var(--status-success)" stroke-width="4" stroke-dasharray="${score}, 100" stroke-linecap="round" fill="none" d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" style="transition: stroke-dasharray 0.6s ease;" />
          </svg>
          <div style="position: absolute; display: flex; flex-direction: column; align-items: center; justify-content: center; line-height: 1.1;">
            <span style="font-family: var(--font-display); font-size: 1.25rem; font-weight: 800; color: var(--text-primary);">${score}%</span>
            <span style="font-size: 0.5rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700; letter-spacing: 0.05em; margin-top: 2px;">Score</span>
          </div>
        </div>

        <!-- Right: Differentiated Compliance list -->
        <div style="flex-grow: 1; display: flex; flex-direction: column; gap: var(--spacing-sm); font-size: 0.73rem;">
          
          <!-- Audits Completed (Success Green) -->
          <div class="flex justify-between align-center" style="background: rgba(16,185,129,0.04); border: 1px solid rgba(16,185,129,0.12); border-radius: var(--radius-md); padding: 6px 10px; transition: background 0.15s;" onmouseover="this.style.background='rgba(16,185,129,0.08)'" onmouseout="this.style.background='rgba(16,185,129,0.04)'">
            <div class="flex align-center gap-xs" style="color: var(--status-success); font-weight: 600;">
              <i data-lucide="check-circle" style="width: 13px; height: 13px; stroke-width: 2.5;"></i>
              <span>Audits Completed</span>
            </div>
            <span style="font-weight: 700; color: var(--status-success); font-size: 0.8rem;">${this.complianceOverview.auditsCompleted}</span>
          </div>
          
          <!-- Corrective Actions (Warning Yellow) -->
          <div class="flex justify-between align-center" style="background: rgba(245,158,11,0.04); border: 1px solid rgba(245,158,11,0.12); border-radius: var(--radius-md); padding: 6px 10px; transition: background 0.15s;" onmouseover="this.style.background='rgba(245,158,11,0.08)'" onmouseout="this.style.background='rgba(245,158,11,0.04)'">
            <div class="flex align-center gap-xs" style="color: var(--status-warning); font-weight: 600;">
              <i data-lucide="alert-triangle" style="width: 13px; height: 13px; stroke-width: 2.5;"></i>
              <span>Corrective Actions</span>
            </div>
            <span style="font-weight: 700; color: var(--status-warning); font-size: 0.8rem;">${this.complianceOverview.correctiveActionsOpen}</span>
          </div>
          
          <!-- Overdue Actions (Danger Red) -->
          <div class="flex justify-between align-center" style="background: rgba(239,68,68,0.04); border: 1px solid rgba(239,68,68,0.12); border-radius: var(--radius-md); padding: 6px 10px; transition: background 0.15s;" onmouseover="this.style.background='rgba(239,68,68,0.08)'" onmouseout="this.style.background='rgba(239,68,68,0.04)'">
            <div class="flex align-center gap-xs" style="color: var(--status-danger); font-weight: 600;">
              <i data-lucide="alert-circle" style="width: 13px; height: 13px; stroke-width: 2.5;"></i>
              <span>Overdue Actions</span>
            </div>
            <span style="font-weight: 700; color: var(--status-danger); font-size: 0.8rem;">${this.complianceOverview.overdueActions}</span>
          </div>

        </div>
      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }
}
