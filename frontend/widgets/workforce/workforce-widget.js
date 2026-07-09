/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : workforce-widget.js
 * Path              : frontend/widgets/workforce/workforce-widget.js
 * Purpose           : Frontend utility: workforce-widget for PLUS33 Coffee ERP
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
 * Frontend utility: workforce-widget for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../core/logger.js';

export class WorkforceWidget {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config, workforceOverview) {
    this.config = config;
    this.workforceOverview = workforceOverview || { totalEmployees: 0, presentCount: 0, onLeaveCount: 0, openPositions: 0, categoriesDistribution: [] };
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    logger.debug('WorkforceWidget', 'Rendering Workforce Overview...');

    const headcount = this.workforceOverview.totalEmployees || 0;
    const present = this.workforceOverview.presentCount || 0;
    const onLeave = this.workforceOverview.onLeaveCount || 0;
    const openPos = this.workforceOverview.openPositions || 0;

    const categories = this.workforceOverview.categoriesDistribution || [];
    const totalCategories = categories.reduce((acc, c) => acc + Number(c.count), 0) || 1;

    let currentOffset = 25;
    const colors = ['var(--accent-primary)', 'var(--status-success)', 'var(--status-warning)', 'var(--status-danger)', 'var(--text-muted)'];
    
    // Group categories/departments (show top 3 and combine the rest under 'Other' to total 100%)
    let displayCategories = [];
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (categories.length <= 4) {
      displayCategories = [...categories];
    } else {
      displayCategories = categories.slice(0, 3);
      const otherCount = categories.slice(3).reduce((acc, c) => acc + Number(c.count), 0);
      displayCategories.push({ category: 'Other', count: otherCount });
    }

    const segmentsSvg = displayCategories.map((c, i) => {
      /**
       * Performs the pct operation in this module.
       * @memberof Widgets Module
       */
      const pct = (Number(c.count) / totalCategories) * 100;
      const stroke = colors[i % colors.length];
      const dash = `${pct.toFixed(1)} ${(100 - pct).toFixed(1)}`;
      const offset = currentOffset;
      currentOffset = (currentOffset - pct + 100) % 100;
      return `<circle class="donut-segment" cx="21" cy="21" r="15.915" fill="transparent" stroke="${stroke}" stroke-width="4" stroke-dasharray="${dash}" stroke-dashoffset="${offset}"></circle>`;
    }).join('');

    container.innerHTML = `
      <div class="flex justify-between align-center mb-md" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs);">
        <h3 class="m-0" style="font-size: 0.95rem; font-weight: 700; color: var(--text-primary);">${this.config.title}</h3>
        <a href="#workforce" style="font-size: 0.75rem; color: var(--accent-primary); text-decoration: none;">View workforce dashboard &rarr;</a>
      </div>

      <div style="display: grid; grid-template-columns: 7fr 5fr; gap: var(--spacing-md);">
        <!-- Left: Key stats + Roles Donut -->
        <div style="min-width: 0;">
          <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: var(--spacing-xs); font-size: 0.75rem; margin-bottom: var(--spacing-md); background: rgba(255,255,255,0.02); padding: var(--spacing-sm); border-radius: 6px; border: 1px solid rgba(255,255,255,0.05);">
            <div>
              <div style="color: var(--text-muted); font-size: 0.65rem;">Total</div>
              <div style="font-weight: 700; color: var(--text-primary); font-size: 0.85rem;">${headcount}</div>
            </div>
            <div>
              <div style="color: var(--text-muted); font-size: 0.65rem;">Leave</div>
              <div style="font-weight: 700; color: var(--text-primary); font-size: 0.85rem;">${onLeave}</div>
            </div>
            <div>
              <div style="color: var(--text-muted); font-size: 0.65rem;">Present</div>
              <div style="font-weight: 700; color: var(--status-success); font-size: 0.85rem;">${present}</div>
            </div>
            <div>
              <div style="color: var(--text-muted); font-size: 0.65rem;">Open</div>
              <div style="font-weight: 700; color: var(--status-warning); font-size: 0.85rem;">${openPos}</div>
            </div>
          </div>

          <div style="display: flex; gap: var(--spacing-sm); align-items: center; min-width: 0;">
            <div style="position: relative; width: 70px; height: 70px; flex-shrink: 0;">
              <svg width="70" height="70" viewBox="0 0 42 42" class="donut">
                <circle class="donut-hole" cx="21" cy="21" r="15.915" fill="transparent"></circle>
                <circle class="donut-ring" cx="21" cy="21" r="15.915" fill="transparent" stroke="rgba(255,255,255,0.05)" stroke-width="4"></circle>
                ${segmentsSvg}
              </svg>
            </div>
            <!-- Sub Roles Legend -->
            <div style="display: flex; flex-direction: column; gap: 4px; font-size: 0.65rem; flex-grow: 1; min-width: 0;">
              <div style="font-weight: 700; color: var(--text-primary); margin-bottom: 2px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">Worker Category</div>
              ${displayCategories.map((c, idx) => `
                <div class="flex justify-between align-center" style="gap: 4px;">
                  <div class="flex align-center gap-xs" style="min-width: 0;">
                    <span style="display: inline-block; width: 6px; height: 6px; border-radius: 50%; background-color: ${colors[idx % colors.length]}; flex-shrink: 0;"></span>
                    <span style="color: var(--text-muted); text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">${c.category}</span>
                  </div>
                  <span style="font-weight: 700; color: var(--text-primary); flex-shrink: 0;">${c.count}</span>
                </div>
              `).join('')}
            </div>
          </div>
        </div>

        <!-- Right Side: Key Metrics -->
        <div style="border-left: 1px solid rgba(255,255,255,0.05); padding-left: var(--spacing-lg); font-size: 0.75rem; display: flex; flex-direction: column; gap: var(--spacing-sm); justify-content: center;">
          <div style="font-weight: 700; color: var(--text-primary);">Key Metrics</div>
          <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.02); padding-bottom: 3px;">
            <span style="color: var(--text-muted);">Attrition Rate (YTD)</span>
            <span style="font-weight: 700; color: var(--text-primary);">12.4%</span>
          </div>
          <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.02); padding-bottom: 3px;">
            <span style="color: var(--text-muted);">Training Completion</span>
            <span style="font-weight: 700; color: var(--text-primary);">76.8%</span>
          </div>
          <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.02); padding-bottom: 3px;">
            <span style="color: var(--text-muted);">Avg Performance</span>
            <span style="font-weight: 700; color: var(--status-success);">4.2 / 5</span>
          </div>
          <div class="flex justify-between align-center">
            <span style="color: var(--text-muted);">Overtime Hours (MTD)</span>
            <span style="font-weight: 700; color: var(--text-primary);">1,248</span>
          </div>
        </div>
      </div>
    `;
  }
}
