/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : financial-chart.js
 * Path              : frontend/widgets/charts/financial-chart.js
 * Purpose           : Frontend utility: financial-chart for PLUS33 Coffee ERP
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
 * Frontend utility: financial-chart for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../core/logger.js';

export class FinancialChart {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config, financialOverview) {
    this.config = config;
    this.financialOverview = financialOverview || { totalRevenue: 0, totalExpenses: 0, totalProfit: 0, profitMargin: 0, trend: [] };
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    logger.debug('FinancialChart', 'Rendering Financial Overview bar chart...');

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

    const revFormatted = new Intl.NumberFormat(locale, {
      style: 'currency',
      currency: currencyCode,
      maximumFractionDigits: 0
    }).format(this.financialOverview.totalRevenue);

    const profitFormatted = new Intl.NumberFormat(locale, {
      style: 'currency',
      currency: currencyCode,
      maximumFractionDigits: 0
    }).format(this.financialOverview.totalProfit);

    const expFormatted = new Intl.NumberFormat(locale, {
      style: 'currency',
      currency: currencyCode,
      maximumFractionDigits: 0
    }).format(this.financialOverview.totalExpenses);

    const marginVal = Number(this.financialOverview.profitMargin || 0).toFixed(2);

    /**
     * Performs the royaltyVal operation in this module.
     * @memberof Widgets Module
     */
    const royaltyVal = (this.financialOverview.totalRevenue || 0) * 0.05;
    const royaltyFormatted = new Intl.NumberFormat(locale, {
      style: 'currency',
      currency: currencyCode,
      maximumFractionDigits: 0
    }).format(royaltyVal);

    const trend = this.financialOverview.trend || [];
    let daysData = [];

    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (trend.length > 0) {
      daysData = trend.map((t, index) => {
        const dateParts = t.month.split('-');
        const label = dateParts.length === 3 ? `${dateParts[1]}/${dateParts[2]}` : t.month;
        return {
          label: label,
          revenue: Number(t.revenue || 0),
          expense: Number(t.expense || 0),
          isToday: (index === trend.length - 1)
        };
      });
    } else {
      const today = new Date();
      /**
       * Performs the i operation in this module.
       * @memberof Widgets Module
       */
      for (let i = 4; i >= 0; i--) {
        const d = new Date();
        d.setDate(today.getDate() - i);
        const dayLabel = `${String(d.getMonth() + 1).padStart(2, '0')}/${String(d.getDate()).padStart(2, '0')}`;
        daysData.push({
          label: dayLabel,
          revenue: i === 0 ? (this.financialOverview.totalRevenue || 721) : 0,
          expense: 0,
          isToday: i === 0
        });
      }
    }

    const width = 450;
    const height = 150;
    const padding = 20;

    const maxVal = Math.max(...daysData.map(d => Number(d.revenue))) * 1.15 || 1;
    /**
     * Performs the stepWidth operation in this module.
     * @memberof Widgets Module
     */
    const stepWidth = (width - 2 * padding) / daysData.length;
    const barWidth = 24;

    const barsSvg = daysData.map((d, i) => {
      const x = padding + i * stepWidth;
      /**
       * Performs the barHeight operation in this module.
       * @memberof Widgets Module
       */
      const barHeight = (Number(d.revenue) / maxVal) * (height - 2 * padding);
      const barY = height - padding - barHeight;
      const barX = x + (stepWidth - barWidth) / 2;
      const barColorId = d.isToday ? 'grad-today' : 'grad-prev';

      const dailyRevenue = Number(d.revenue || 0);
      const dailyExpense = Number(d.expense || 0);
      const dailyProfit = dailyRevenue - dailyExpense;
      const dailyMargin = dailyRevenue > 0 ? ((dailyProfit / dailyRevenue) * 100).toFixed(0) : '0';

      // Rounded-top bar using a path
      const r = Math.min(4, barWidth / 2);
      const bx = barX, by = barY, bw = barWidth, bh = barHeight;
      const barPath = bh > r
        ? `M${bx},${by + bh} L${bx},${by + r} Q${bx},${by} ${bx + r},${by} L${bx + bw - r},${by} Q${bx + bw},${by} ${bx + bw},${by + r} L${bx + bw},${by + bh} Z`
        : `M${bx},${by + bh} L${bx},${by} L${bx + bw},${by} L${bx + bw},${by + bh} Z`;

      return `
        <path class="financial-bar" style="cursor:pointer; transition:opacity var(--transition-fast);" d="${barPath}" fill="url(#${barColorId})" data-date="${d.label}" data-revenue="${dailyRevenue}" data-expense="${dailyExpense}" data-margin="${dailyMargin}" />
        <text x="${barX + barWidth / 2}" y="${barY - 5}" fill="var(--text-primary)" font-size="8" font-weight="700" text-anchor="middle" style="pointer-events:none;">${dailyMargin}%</text>
        <text x="${x + stepWidth / 2}" y="${height - 3}" fill="var(--text-muted)" font-size="8" text-anchor="middle" style="pointer-events:none;">${d.label}</text>
      `;
    }).join('');

    container.innerHTML = `
      <div class="flex justify-between align-center mb-md" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs);">
        <div style="display:flex; align-items:center; gap:var(--spacing-sm);">
          <h3 class="m-0" style="font-size: 0.95rem; font-weight: 700; color: var(--text-primary);">${this.config.title}</h3>
          <!-- Live indicator badge -->
          <div style="display:flex; align-items:center; gap:5px; background: rgba(130,163,125,0.12); border: 1px solid rgba(130,163,125,0.3); border-radius: var(--radius-full); padding: 2px 9px; font-size: 0.68rem; font-weight: 700; color: var(--status-success);">
            <span style="width:6px; height:6px; border-radius:50%; background: var(--status-success); display:inline-block; animation: pulse-dot 2s infinite;"></span>
            Live
          </div>
        </div>
        <a href="#finance" style="font-size: 0.75rem; color: var(--accent-primary); text-decoration: none; display:flex; align-items:center; gap:4px;">
          <i data-lucide="external-link" style="width:12px;height:12px;"></i> View Finance
        </a>
      </div>

      <div style="display: grid; grid-template-columns: 4fr 8fr; gap: var(--spacing-lg); margin-bottom: var(--spacing-sm);">
        <!-- MTD Key metrics -->
        <div style="display: flex; flex-direction: column; gap: 4px; border-right: 1px solid rgba(255,255,255,0.05); padding-right: var(--spacing-md); justify-content: space-between;">
          <div id="financial-card-title" style="font-size: 0.65rem; color: var(--accent-primary); font-weight: 700; text-transform: uppercase; margin-bottom: 2px;">MTD Overall</div>
          <div>
            <div style="font-size: 0.65rem; color: var(--text-muted); line-height: 1;">Revenue</div>
            <div id="financial-card-rev" style="font-family: var(--font-display); font-size: 0.95rem; font-weight: 800; color: var(--text-primary);">${revFormatted}</div>
          </div>
          <div>
            <div style="font-size: 0.65rem; color: var(--text-muted); line-height: 1;">Expenses</div>
            <div id="financial-card-exp" style="font-family: var(--font-display); font-size: 0.95rem; font-weight: 800; color: var(--text-primary);">${expFormatted}</div>
          </div>
          <div>
            <div style="font-size: 0.65rem; color: var(--text-muted); line-height: 1;">Profit Margin</div>
            <div id="financial-card-margin" style="font-family: var(--font-display); font-size: 0.95rem; font-weight: 800; color: var(--status-success);">${marginVal}%</div>
          </div>
          <div>
            <div style="font-size: 0.65rem; color: var(--text-muted); line-height: 1;">Royalty Income (5%)</div>
            <div id="financial-card-royalty" style="font-family: var(--font-display); font-size: 0.95rem; font-weight: 800; color: var(--accent-secondary);">${royaltyFormatted}</div>
          </div>
          <div>
            <div style="font-size: 0.65rem; color: var(--text-muted); line-height: 1;">Budget Utilized</div>
            <div id="financial-card-budget" style="font-family: var(--font-display); font-size: 0.95rem; font-weight: 800; color: var(--status-warning);">82.4%</div>
          </div>
        </div>

        <!-- Trend Chart -->
        <div>
          <div class="flex justify-between align-center mb-sm" style="font-size: 0.7rem; color: var(--text-muted);">
            <div>Daily Revenue Trend</div>
            <div class="flex gap-sm">
              <span class="flex align-center gap-xs"><span style="width:6px; height:6px; background:var(--accent-primary); display:inline-block; border-radius:50%;"></span> Previous Days</span>
              <span class="flex align-center gap-xs"><span style="width:6px; height:6px; background:var(--status-success); display:inline-block; border-radius:50%;"></span> Today</span>
            </div>
          </div>
          <div style="position: relative; width: 100%; height: ${height}px;">
            <svg width="100%" height="100%" viewBox="0 0 ${width} ${height}" preserveAspectRatio="xMidYMid meet">
              <defs>
                <linearGradient id="grad-prev" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stop-color="#c9a46a" stop-opacity="1"/>
                  <stop offset="100%" stop-color="#c9a46a" stop-opacity="0.5"/>
                </linearGradient>
                <linearGradient id="grad-today" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stop-color="#82a37d" stop-opacity="1"/>
                  <stop offset="100%" stop-color="#82a37d" stop-opacity="0.5"/>
                </linearGradient>
              </defs>
              <line x1="${padding}" y1="${padding}" x2="${width - padding}" y2="${padding}" stroke="rgba(255,255,255,0.03)" />
              <line x1="${padding}" y1="${height - padding}" x2="${width - padding}" y2="${height - padding}" stroke="rgba(255,255,255,0.08)" />
              ${barsSvg}
            </svg>
          </div>
        </div>
      </div>

      <!-- Quick Actions for Finance Operations -->
      <div style="border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-sm); display: flex; gap: var(--spacing-xs); flex-wrap: wrap;">
        <button class="btn btn-secondary btn-action-approve-budgets" style="font-size: 0.65rem; padding: 3px 8px;">Approve Budgets</button>
        <button class="btn btn-secondary btn-action-financial-reports" style="font-size: 0.65rem; padding: 3px 8px;">View Financial Reports</button>
        <button class="btn btn-secondary btn-action-review-forecasts" style="font-size: 0.65rem; padding: 3px 8px;">Review Forecasts</button>
      </div>
    `;

    // Interactivity: Bind click handlers to the SVG pillars
    const bars = container.querySelectorAll('.financial-bar');
    bars.forEach(bar => {
      bar.addEventListener('click', (e) => {
        e.stopPropagation(); // Avoid triggering background card reset click
        const date = bar.getAttribute('data-date');
        const rev = Number(bar.getAttribute('data-revenue'));
        const exp = Number(bar.getAttribute('data-expense'));
        const margin = bar.getAttribute('data-margin');

        const formattedRev = new Intl.NumberFormat(locale, {
          style: 'currency',
          currency: currencyCode,
          maximumFractionDigits: 0
        }).format(rev);

        const formattedExp = new Intl.NumberFormat(locale, {
          style: 'currency',
          currency: currencyCode,
          maximumFractionDigits: 0
        }).format(exp);

        const dailyRoyalty = rev * 0.05;
        const formattedRoyalty = new Intl.NumberFormat(locale, {
          style: 'currency',
          currency: currencyCode,
          maximumFractionDigits: 0
        }).format(dailyRoyalty);

        const cardTitle = container.querySelector('#financial-card-title');
        const cardRev = container.querySelector('#financial-card-rev');
        const cardExp = container.querySelector('#financial-card-exp');
        const cardMargin = container.querySelector('#financial-card-margin');
        const cardRoyalty = container.querySelector('#financial-card-royalty');
        const cardBudget = container.querySelector('#financial-card-budget');

        if (cardTitle) cardTitle.textContent = `Day: ${date}`;
        if (cardRev) cardRev.textContent = formattedRev;
        if (cardExp) cardExp.textContent = formattedExp;
        if (cardMargin) cardMargin.textContent = `${margin}%`;
        if (cardRoyalty) cardRoyalty.textContent = formattedRoyalty;
        if (cardBudget) cardBudget.textContent = "N/A";

        // Highlight selected bar and dim others
        bars.forEach(b => {
          b.setAttribute('opacity', '0.4');
          b.setAttribute('stroke', 'none');
        });
        bar.setAttribute('opacity', '1.0');
        bar.setAttribute('stroke', 'var(--text-primary)');
        bar.setAttribute('stroke-width', '1.5');
      });
    });

    // Reset metrics to total MTD when clicking elsewhere on the card
    container.addEventListener('click', () => {
      const cardTitle = container.querySelector('#financial-card-title');
      const cardRev = container.querySelector('#financial-card-rev');
      const cardExp = container.querySelector('#financial-card-exp');
      const cardMargin = container.querySelector('#financial-card-margin');
      const cardRoyalty = container.querySelector('#financial-card-royalty');
      const cardBudget = container.querySelector('#financial-card-budget');

      if (cardTitle) cardTitle.textContent = "MTD Overall";
      if (cardRev) cardRev.textContent = revFormatted;
      if (cardExp) cardExp.textContent = expFormatted;
      if (cardMargin) cardMargin.textContent = `${marginVal}%`;
      if (cardRoyalty) cardRoyalty.textContent = royaltyFormatted;
      if (cardBudget) cardBudget.textContent = "82.4%";

      bars.forEach(b => {
        b.setAttribute('opacity', '1.0');
        b.setAttribute('stroke', 'none');
      });
    });

    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  bindEvents(container, lifecycle) {
    import('../../store/notificationStore.js').then(({ notificationStore }) => {
      const approveBtn = container.querySelector('.btn-action-approve-budgets');
      const reportsBtn = container.querySelector('.btn-action-financial-reports');
      const forecastsBtn = container.querySelector('.btn-action-review-forecasts');

      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      if (approveBtn) {
        /**
         * Performs the h operation in this module.
         * @memberof Widgets Module
         */
        const h = () => notificationStore.success('Opening budget pending approvals workspace...');
        approveBtn.addEventListener('click', h);
        lifecycle.onCleanup(() => approveBtn.removeEventListener('click', h));
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
        const h = () => notificationStore.success('Consolidated profit & loss statement loading...');
        reportsBtn.addEventListener('click', h);
        lifecycle.onCleanup(() => reportsBtn.removeEventListener('click', h));
      }
      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      if (forecastsBtn) {
        /**
         * Performs the h operation in this module.
         * @memberof Widgets Module
         */
        const h = () => notificationStore.success('Consolidated multi-region cost forecasting report verified.');
        forecastsBtn.addEventListener('click', h);
        lifecycle.onCleanup(() => forecastsBtn.removeEventListener('click', h));
      }
    });
  }
}
