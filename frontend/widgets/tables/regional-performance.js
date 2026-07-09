/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : regional-performance.js
 * Path              : frontend/widgets/tables/regional-performance.js
 * Purpose           : Frontend utility: regional-performance for PLUS33 Coffee ERP
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
 * Frontend utility: regional-performance for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../core/logger.js';
import { notificationStore } from '../../store/notificationStore.js';

export class RegionalPerformance {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config, regionalPerformance) {
    this.config = config;
    this.regionalPerformance = regionalPerformance || [];
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    logger.debug('RegionalPerformance', 'Rendering Regional Performance circle graph & table...');

    // 1. Process and align regional performance data (scale fallback if data is sparse)
    let regions = [];
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (this.regionalPerformance && this.regionalPerformance.length > 0) {
      regions = [...this.regionalPerformance];
    } else {
      regions = [];
    }

    // 2. Sort regions by sales to show highest performers first
    regions.sort((a, b) => Number(b.sales || 0) - Number(a.sales || 0));

    // 3. Compute totals and percentages for the donut chart segment rendering
    const totalSales = regions.reduce((acc, r) => acc + Number(r.sales || 0), 0) || 1;
    
    const colors = [
      'var(--accent-primary)',
      'var(--accent-secondary)',
      'var(--status-info)',
      'var(--status-success)',
      'var(--status-warning)'
    ];

    let currentOffset = 25;
    const segmentsSvg = regions.slice(0, 5).map((r, i) => {
      /**
       * Performs the pct operation in this module.
       * @memberof Widgets Module
       */
      const pct = (Number(r.sales || 0) / totalSales) * 100;
      const stroke = colors[i % colors.length];
      const dash = `${pct.toFixed(1)} ${(100 - pct).toFixed(1)}`;
      const offset = currentOffset;
      currentOffset = (currentOffset - pct + 100) % 100;
      return `<circle class="donut-segment" cx="21" cy="21" r="15.915" fill="transparent" stroke="${stroke}" stroke-width="4.2" stroke-dasharray="${dash}" stroke-dashoffset="${offset}" style="transition: stroke-dasharray 0.3s ease;"></circle>`;
    }).join('');

    // Format total sales for chart center text
    /**
     * Performs the formatTotalCompact operation in this module.
     * @memberof Widgets Module
     */
    const formatTotalCompact = (val) => {
      if (val >= 1_000_000) return `€${(val / 1_000_000).toFixed(1)}M`;
      if (val >= 1_000) return `€${(val / 1_000).toFixed(1)}K`;
      return `€${val.toFixed(0)}`;
    };

    container.innerHTML = `
      <div class="flex justify-between align-center mb-md" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs);">
        <h3 class="m-0" style="font-size: 0.95rem; font-weight: 700; color: var(--text-primary);">${this.config.title} by Income</h3>
        <a href="#regions" style="font-size: 0.75rem; color: var(--accent-primary); text-decoration: none;">View All</a>
      </div>

      <div style="display: flex; gap: var(--spacing-lg); align-items: center; justify-content: space-between; margin-bottom: var(--spacing-md); min-height: 160px;">
        <!-- Left: Donut Chart -->
        <div style="position: relative; width: 120px; height: 120px; flex-shrink: 0; display: flex; align-items: center; justify-content: center;">
          <svg width="120" height="120" viewBox="0 0 42 42" class="donut" style="transform: rotate(0deg);">
            <circle class="donut-hole" cx="21" cy="21" r="15.915" fill="transparent"></circle>
            <circle class="donut-ring" cx="21" cy="21" r="15.915" fill="transparent" stroke="rgba(255,255,255,0.04)" stroke-width="4.2"></circle>
            ${segmentsSvg}
          </svg>
          <div style="position: absolute; display: flex; flex-direction: column; align-items: center; justify-content: center; text-align: center; pointer-events: none; line-height: 1.1;">
            <span style="font-family: var(--font-display); font-size: 0.95rem; font-weight: 800; color: var(--text-primary);">${formatTotalCompact(totalSales)}</span>
            <span style="font-size: 0.52rem; color: var(--text-muted); text-transform: uppercase; font-weight: 700; letter-spacing: 0.05em; margin-top: 2px;">Total Income</span>
          </div>
        </div>

        <!-- Right: Regions Detail Table -->
        <div style="flex-grow: 1; overflow-x: auto;">
          <table style="width: 100%; border-collapse: collapse; text-align: left; font-size: 0.73rem;">
            <thead>
              <tr style="border-bottom: 1px solid var(--border-color); color: var(--text-muted); font-weight: 600; font-size: 0.65rem; text-transform: uppercase; letter-spacing: 0.04em;">
                <th style="padding: 4px 0; padding-bottom: 6px;">Country</th>
                <th style="padding: 4px; text-align: right; padding-bottom: 6px;">Income</th>
                <th style="padding: 4px; text-align: right; padding-bottom: 6px;">Share</th>
                <th style="padding: 4px; text-align: right; padding-bottom: 6px;">Ach.</th>
              </tr>
            </thead>
            <tbody>
              ${regions.length === 0 ? `
                <tr>
                  <td colspan="4" style="padding: var(--spacing-lg); text-align: center; color: var(--text-muted); font-size: 0.7rem;">
                    No regional sales recorded.
                  </td>
                </tr>
              ` : regions.slice(0, 5).map((r, i) => {
                const salesVal = Number(r.sales || 0);
                /**
                 * Performs the sharePct operation in this module.
                 * @memberof Widgets Module
                 */
                const sharePct = ((salesVal / totalSales) * 100).toFixed(1);
                const achievement = Number(r.achievement || ((salesVal / (r.target || salesVal * 1.1)) * 100)).toFixed(1);
                
                let currencyCode = 'EUR';
                let locale = 'fr-FR';
                const storedGeneral = localStorage.getItem('plus33-settings-general');
                if (storedGeneral) {
                  try {
                    const parsed = JSON.parse(storedGeneral);
                    if (parsed.defaultCurrency) {
                      currencyCode = parsed.defaultCurrency;
                      if (currencyCode === 'USD') locale = 'en-US';
                      else if (currencyCode === 'INR') locale = 'en-IN';
                      else if (currencyCode === 'AED') locale = 'en-US';
                    }
                  } catch (e) {
                    // ignore
                  }
                }

                const salesFormatted = new Intl.NumberFormat(locale, {
                  style: 'currency',
                  currency: currencyCode,
                  maximumFractionDigits: 0
                }).format(salesVal);

                const bulletColor = colors[i % colors.length];
                const statusColor = Number(achievement) >= 90 
                  ? 'var(--status-success)' 
                  : (Number(achievement) >= 80 ? 'var(--status-warning)' : 'var(--status-danger)');

                return `
                  <tr style="border-bottom: 1px solid rgba(255,255,255,0.03); transition: background 0.15s;" onmouseover="this.style.background='rgba(255,255,255,0.02)'" onmouseout="this.style.background='none'">
                    <td style="padding: 6px 0; color: var(--text-primary); font-weight: 600; display: flex; align-items: center; gap: 6px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 130px;">
                      <span style="display: inline-block; width: 6px; height: 6px; border-radius: 50%; background-color: ${bulletColor}; flex-shrink: 0;"></span>
                      ${r.region}
                    </td>
                    <td style="padding: 6px 4px; text-align: right; color: var(--text-primary); font-weight: 500;">${salesFormatted}</td>
                    <td style="padding: 6px 4px; text-align: right; color: var(--text-muted); font-size: 0.68rem;">${sharePct}%</td>
                    <td style="padding: 6px 0; text-align: right; font-weight: 700; color: ${statusColor};">${achievement}%</td>
                  </tr>
                `;
              }).join('')}
            </tbody>
          </table>
        </div>
      </div>

      <!-- Quick Actions for Region operations -->
      <div style="border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-sm); display: flex; gap: var(--spacing-xs); flex-wrap: wrap;">
        <button class="btn btn-secondary btn-action-view" style="font-size: 0.65rem; padding: 4px 10px; display: flex; align-items: center; gap: 4px;">
          <i data-lucide="eye" style="width:12px; height:12px;"></i> View Dashboard
        </button>
        <button class="btn btn-secondary btn-action-plan" style="font-size: 0.65rem; padding: 4px 10px; display: flex; align-items: center; gap: 4px;">
          <i data-lucide="clipboard-list" style="width:12px; height:12px;"></i> Improvement Plan
        </button>
        <button class="btn btn-secondary btn-action-review" style="font-size: 0.65rem; padding: 4px 10px; display: flex; align-items: center; gap: 4px;">
          <i data-lucide="user-check" style="width:12px; height:12px;"></i> Assign Review
        </button>
      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  bindEvents(container, lifecycle) {
    const viewBtn = container.querySelector('.btn-action-view');
    const planBtn = container.querySelector('.btn-action-plan');
    const reviewBtn = container.querySelector('.btn-action-review');

    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (viewBtn) {
      /**
       * Performs the h operation in this module.
       * @memberof Widgets Module
       */
      const h = () => window.location.hash = '#regions';
      viewBtn.addEventListener('click', h);
      lifecycle.onCleanup(() => viewBtn.removeEventListener('click', h));
    }
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (planBtn) {
      /**
       * Performs the h operation in this module.
       * @memberof Widgets Module
       */
      const h = () => notificationStore.success('New regional improvement plan initialized.');
      planBtn.addEventListener('click', h);
      lifecycle.onCleanup(() => planBtn.removeEventListener('click', h));
    }
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (reviewBtn) {
      /**
       * Performs the h operation in this module.
       * @memberof Widgets Module
       */
      const h = () => notificationStore.success('Regional performance review checklist assigned to regional administrator.');
      reviewBtn.addEventListener('click', h);
      lifecycle.onCleanup(() => reviewBtn.removeEventListener('click', h));
    }
  }
}
