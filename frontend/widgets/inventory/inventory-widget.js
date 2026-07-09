/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : inventory-widget.js
 * Path              : frontend/widgets/inventory/inventory-widget.js
 * Purpose           : Frontend utility: inventory-widget for PLUS33 Coffee ERP
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
 * Frontend utility: inventory-widget for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../core/logger.js';

export class InventoryWidget {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config, inventoryOverview) {
    this.config = config;
    this.inventoryOverview = inventoryOverview || { totalValue: 0, stockInHand: 0, lowStockCount: 0, outOfStockCount: 0, distribution: [], expiryAlerts: {} };
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    logger.debug('InventoryWidget', 'Rendering Inventory Overview...');

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

    const valFormatted = new Intl.NumberFormat(locale, {
      style: 'currency',
      currency: currencyCode,
      maximumFractionDigits: 0
    }).format(this.inventoryOverview.totalValue);

    const stockCountFormatted = new Intl.NumberFormat('en-US').format(this.inventoryOverview.stockInHand);

    const dist = this.inventoryOverview.distribution || [];
    const totalStock = dist.reduce((acc, d) => acc + Number(d.stockCount), 0) || 1;

    // Draw donut segment offsets dynamically
    let currentOffset = 25;
    const colors = ['var(--accent-primary)', 'var(--status-success)', 'var(--status-warning)', 'var(--status-danger)', 'var(--text-muted)'];
    const segmentsSvg = dist.slice(0, 5).map((d, i) => {
      /**
       * Performs the pct operation in this module.
       * @memberof Widgets Module
       */
      const pct = (Number(d.stockCount) / totalStock) * 100;
      const stroke = colors[i % colors.length];
      const dash = `${pct.toFixed(1)} ${(100 - pct).toFixed(1)}`;
      const offset = currentOffset;
      currentOffset = (currentOffset - pct + 100) % 100;
      return `<circle class="donut-segment" cx="21" cy="21" r="15.915" fill="transparent" stroke="${stroke}" stroke-width="4" stroke-dasharray="${dash}" stroke-dashoffset="${offset}"></circle>`;
    }).join('');

    const expiry = this.inventoryOverview.expiryAlerts || { within30: 0, within60: 0, within90: 0 };

    container.innerHTML = `
      <div class="flex justify-between align-center mb-md" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs);">
        <h3 class="m-0" style="font-size: 0.95rem; font-weight: 700; color: var(--text-primary);">${this.config.title}</h3>
        <a href="#inventory" style="font-size: 0.75rem; color: var(--accent-primary); text-decoration: none;">View All</a>
      </div>

      <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: var(--spacing-xs); font-size: 0.75rem; margin-bottom: var(--spacing-md); background: rgba(255,255,255,0.02); padding: var(--spacing-sm); border-radius: 6px; border: 1px solid rgba(255,255,255,0.05);">
        <div>
          <div style="color: var(--text-muted); font-size: 0.65rem;">Stock Items</div>
          <div style="font-weight: 700; color: var(--text-primary);">${stockCountFormatted}</div>
        </div>
        <div>
          <div style="color: var(--text-muted); font-size: 0.65rem;">Low Stock</div>
          <div style="font-weight: 700; color: var(--status-warning);">${this.inventoryOverview.lowStockCount}</div>
        </div>
        <div>
          <div style="color: var(--text-muted); font-size: 0.65rem;">Out of Stock</div>
          <div style="font-weight: 700; color: var(--status-danger);">${this.inventoryOverview.outOfStockCount}</div>
        </div>
      </div>

      <div style="display: flex; gap: var(--spacing-md); align-items: center; justify-content: space-between;">
        <!-- Left: Donut Chart -->
        <div style="position: relative; width: 100px; height: 100px; flex-shrink: 0;">
          <svg width="100" height="100" viewBox="0 0 42 42" class="donut">
            <circle class="donut-hole" cx="21" cy="21" r="15.915" fill="transparent"></circle>
            <circle class="donut-ring" cx="21" cy="21" r="15.915" fill="transparent" stroke="rgba(255,255,255,0.05)" stroke-width="4"></circle>
            ${segmentsSvg}
          </svg>
          <div style="position: absolute; inset: 0; display: flex; flex-direction: column; align-items: center; justify-content: center; line-height: 1.1;">
            <span style="font-size: 0.75rem; font-weight: 800; color: var(--text-primary);">${stockCountFormatted}</span>
            <span style="font-size: 0.5rem; color: var(--text-muted); text-transform: uppercase; font-weight: 600;">Total Items</span>
          </div>
        </div>

        <!-- Right: Expiry items -->
        <div style="flex-grow: 1; display: flex; flex-direction: column; gap: var(--spacing-xs); font-size: 0.75rem;">
          <div style="font-weight: 700; color: var(--text-primary); margin-bottom: 2px;">Expiring Items</div>
          <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.02); padding-bottom: 3px;">
            <span style="color: var(--text-muted);">Within 30 Days</span>
            <span style="font-weight: 700; color: var(--status-danger);">${expiry.within30}</span>
          </div>
          <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.02); padding-bottom: 3px;">
            <span style="color: var(--text-muted);">Within 60 Days</span>
            <span style="font-weight: 700; color: var(--status-warning);">${expiry.within60}</span>
          </div>
          <div class="flex justify-between align-center">
            <span style="color: var(--text-muted);">Within 90 Days</span>
            <span style="font-weight: 700; color: var(--status-success);">${expiry.within90}</span>
          </div>
        </div>
      </div>
    `;
  }
}
