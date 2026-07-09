/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : stock-chart.js
 * Path              : frontend/widgets/charts/stock-chart.js
 * Purpose           : Frontend utility: stock-chart for PLUS33 Coffee ERP
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
 * Frontend utility: stock-chart for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../core/logger.js';

export class StockChart {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config, storeStatusOverview) {
    this.config = config;
    this.storeStatusOverview = storeStatusOverview || { highProfit: 0, midProfit: 0, lowProfit: 0, loss: 0 };
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    logger.debug('StockChart', 'Rendering store profit breakdown donut graph...');

    const highProfit = this.storeStatusOverview.highProfit || 0;
    const midProfit = this.storeStatusOverview.midProfit || 0;
    const lowProfit = this.storeStatusOverview.lowProfit || 0;
    const loss = this.storeStatusOverview.loss || 0;
    const total = highProfit + midProfit + lowProfit + loss || 1;

    /**
     * Performs the hpPct operation in this module.
     * @memberof Widgets Module
     */
    const hpPct = ((highProfit / total) * 100).toFixed(1);
    /**
     * Performs the mpPct operation in this module.
     * @memberof Widgets Module
     */
    const mpPct = ((midProfit / total) * 100).toFixed(1);
    /**
     * Performs the lpPct operation in this module.
     * @memberof Widgets Module
     */
    const lpPct = ((lowProfit / total) * 100).toFixed(1);
    /**
     * Performs the lsPct operation in this module.
     * @memberof Widgets Module
     */
    const lsPct = ((loss / total) * 100).toFixed(1);

    // Draw donut segment offsets dynamically
    let currentOffset = 25;
    const dataSegments = [
      { name: 'High Profit', count: highProfit, percent: Number(hpPct), color: 'var(--status-success)' },
      { name: 'Mid Profit', count: midProfit, percent: Number(mpPct), color: 'var(--status-warning)' },
      { name: 'Low Profit', count: lowProfit, percent: Number(lpPct), color: '#eab308' },
      { name: 'Loss', count: loss, percent: Number(lsPct), color: 'var(--status-danger)' }
    ];

    const segmentsSvg = dataSegments.map(seg => {
      const dash = `${seg.percent} ${100 - seg.percent}`;
      const offset = currentOffset;
      currentOffset = (currentOffset - seg.percent + 100) % 100;
      return `<circle class="donut-segment" cx="21" cy="21" r="15.915" fill="transparent" stroke="${seg.color}" stroke-width="4" stroke-dasharray="${dash}" stroke-dashoffset="${offset}"></circle>`;
    }).join('');

    container.innerHTML = `
      <div class="flex justify-between align-center mb-md" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs);">
        <h3 class="m-0" style="font-size: 0.95rem; font-weight: 700; color: var(--text-primary);">${this.config.title}</h3>
        <a href="#stores" style="font-size: 0.75rem; color: var(--accent-primary); text-decoration: none;">View All</a>
      </div>

      <div style="display: flex; gap: var(--spacing-lg); align-items: center; justify-content: space-between;">
        <!-- SVG Donut Chart -->
        <div style="position: relative; width: 110px; height: 110px; flex-shrink: 0;">
          <svg width="110" height="110" viewBox="0 0 42 42" class="donut">
            <circle class="donut-hole" cx="21" cy="21" r="15.915" fill="transparent"></circle>
            <circle class="donut-ring" cx="21" cy="21" r="15.915" fill="transparent" stroke="rgba(255,255,255,0.05)" stroke-width="4"></circle>
            ${segmentsSvg}
          </svg>
          <div style="position: absolute; inset: 0; display: flex; flex-direction: column; align-items: center; justify-content: center; line-height: 1.1;">
            <span style="font-family: var(--font-display); font-size: 1.25rem; font-weight: 800; color: var(--text-primary);">${total}</span>
            <span style="font-size: 0.55rem; color: var(--text-muted); text-transform: uppercase; font-weight: 600;">Total Stores</span>
          </div>
        </div>

        <!-- Legend breakdown -->
        <div style="flex-grow: 1; display: flex; flex-direction: column; gap: 6px; font-size: 0.75rem;">
          <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.02); padding-bottom: 3px;">
            <div class="flex align-center gap-xs">
              <span style="display: inline-block; width: 6px; height: 6px; border-radius: 50%; background-color: var(--status-success);"></span>
              <span style="color: var(--text-muted);">High Profit</span>
            </div>
            <span style="font-weight: 700; color: var(--text-primary);">${highProfit} (${hpPct}%)</span>
          </div>
          <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.02); padding-bottom: 3px;">
            <div class="flex align-center gap-xs">
              <span style="display: inline-block; width: 6px; height: 6px; border-radius: 50%; background-color: var(--status-warning);"></span>
              <span style="color: var(--text-muted);">Mid Profit</span>
            </div>
            <span style="font-weight: 700; color: var(--text-primary);">${midProfit} (${mpPct}%)</span>
          </div>
          <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.02); padding-bottom: 3px;">
            <div class="flex align-center gap-xs">
              <span style="display: inline-block; width: 6px; height: 6px; border-radius: 50%; background-color: #eab308;"></span>
              <span style="color: var(--text-muted);">Low Profit</span>
            </div>
            <span style="font-weight: 700; color: var(--text-primary);">${lowProfit} (${lpPct}%)</span>
          </div>
          <div class="flex justify-between align-center">
            <div class="flex align-center gap-xs">
              <span style="display: inline-block; width: 6px; height: 6px; border-radius: 50%; background-color: var(--status-danger);"></span>
              <span style="color: var(--text-muted);">Loss</span>
            </div>
            <span style="font-weight: 700; color: var(--text-primary);">${loss} (${lsPct}%)</span>
          </div>
        </div>
      </div>

      <div style="border-top: 1px solid rgba(255,255,255,0.05); margin-top: var(--spacing-md); padding-top: var(--spacing-sm); display: grid; grid-template-columns: repeat(2, 1fr); gap: var(--spacing-sm); font-size: 0.75rem;">
        <div>
          <div style="color: var(--text-muted); font-size: 0.65rem;">New Stores (This Month)</div>
          <div style="font-weight: 700; color: var(--text-primary); font-size: 0.95rem;">12 <span style="color: var(--status-success); font-size: 0.65rem;">&uarr; 5</span></div>
        </div>
        <div>
          <div style="color: var(--text-muted); font-size: 0.65rem;">Avg Store Rating</div>
          <div style="font-weight: 700; color: var(--text-primary); font-size: 0.95rem;">4.3 / 5 <span style="color: var(--status-success); font-size: 0.65rem;">&uarr; 0.2</span></div>
        </div>
      </div>

      <!-- Quick Actions for Store operations -->
      <div style="border-top: 1px solid rgba(255,255,255,0.05); margin-top: var(--spacing-sm); padding-top: var(--spacing-sm); display: flex; gap: var(--spacing-xs); flex-wrap: wrap;">
        <button class="btn btn-secondary btn-action-approve-stores" style="font-size: 0.65rem; padding: 3px 8px;">Approve New Stores</button>
        <button class="btn btn-secondary btn-action-view-perf" style="font-size: 0.65rem; padding: 3px 8px;">View Performance</button>
        <button class="btn btn-secondary btn-action-visits" style="font-size: 0.65rem; padding: 3px 8px;">Review Visits</button>
      </div>
    `;

    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  bindEvents(container, lifecycle) {
    // Import notification store dynamically
    import('../../store/notificationStore.js').then(({ notificationStore }) => {
      const approveBtn = container.querySelector('.btn-action-approve-stores');
      const perfBtn = container.querySelector('.btn-action-view-perf');
      const visitsBtn = container.querySelector('.btn-action-visits');

      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      if (approveBtn) {
        /**
         * Performs the h operation in this module.
         * @memberof Widgets Module
         */
        const h = () => notificationStore.success('Opening pending new store applications queue...');
        approveBtn.addEventListener('click', h);
        lifecycle.onCleanup(() => approveBtn.removeEventListener('click', h));
      }
      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      if (perfBtn) {
        /**
         * Performs the h operation in this module.
         * @memberof Widgets Module
         */
        const h = () => window.location.hash = '#stores';
        perfBtn.addEventListener('click', h);
        lifecycle.onCleanup(() => perfBtn.removeEventListener('click', h));
      }
      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      if (visitsBtn) {
        /**
         * Performs the h operation in this module.
         * @memberof Widgets Module
         */
        const h = () => notificationStore.success('Loading regional audit checklist logs...');
        visitsBtn.addEventListener('click', h);
        lifecycle.onCleanup(() => visitsBtn.removeEventListener('click', h));
      }
    });
  }
}
