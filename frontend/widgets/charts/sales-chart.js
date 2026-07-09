/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : sales-chart.js
 * Path              : frontend/widgets/charts/sales-chart.js
 * Purpose           : Frontend utility: sales-chart for PLUS33 Coffee ERP
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
 * Frontend utility: sales-chart for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../core/logger.js';

export class SalesChart {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config, salesOverview, regionalPerformance) {
    this.config = config;
    this.salesOverview = salesOverview || { totalSales: 0, targetSales: 0, targetAchievement: 0, trend: [] };
    this.regionalPerformance = regionalPerformance || [];
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  async mount(container, lifecycle) {
    logger.debug('SalesChart', 'Mounting Sales Overview widget frame...');

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

    const baseSales = Number(this.salesOverview.totalSales || 0);
    const baseTarget = Number(this.salesOverview.targetSales || 0);

    // Initial outer HTML structure with chart canvas wrapper and tooltip element
    container.innerHTML = `
      <div class="flex justify-between align-center mb-md pb-xs" style="border-bottom: 1px solid rgba(255,255,255,0.05);">
        <h3 class="m-0" style="font-size: 0.95rem; font-weight: 700; color: var(--text-primary);">${this.config.title}</h3>
        <div class="flex gap-xs" style="font-size: 0.72rem; background: rgba(255,255,255,0.04); border-radius: var(--radius-sm); padding: 3px;">
          <button class="chart-period-tab active-tab" data-period="MTD" style="background: rgba(201,164,106,0.18); border: 1px solid rgba(201,164,106,0.3); padding: 2px 10px; border-radius: 4px; color: var(--accent-primary); font-weight: 700; cursor: pointer; font-size: 0.72rem; outline: none;">MTD</button>
          <button class="chart-period-tab" data-period="QTD" style="background: none; border: 1px solid transparent; padding: 2px 10px; border-radius: 4px; color: var(--text-muted); font-weight: 600; cursor: pointer; font-size: 0.72rem; outline: none; transition: var(--transition-fast);">QTD</button>
          <button class="chart-period-tab" data-period="YTD" style="background: none; border: 1px solid transparent; padding: 2px 10px; border-radius: 4px; color: var(--text-muted); font-weight: 600; cursor: pointer; font-size: 0.72rem; outline: none; transition: var(--transition-fast);">YTD</button>
        </div>
      </div>

      <div style="display: grid; grid-template-columns: minmax(0, 7.2fr) minmax(0, 4.8fr); gap: var(--spacing-md); overflow: hidden;">
        <!-- Left Side: Sales Metrics + Chart Canvas -->
        <div style="min-width: 0; overflow: hidden;">
          <div class="flex gap-lg mb-md">
            <div>
              <div style="font-size: 0.75rem; color: var(--text-muted); font-weight: 500;">Total Sales</div>
              <div id="sales-metric-total" style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary);">--</div>
            </div>
            <div style="border-left: 1px solid rgba(255,255,255,0.1); padding-left: var(--spacing-lg);">
              <div style="font-size: 0.75rem; color: var(--text-muted); font-weight: 500;">Sales Target</div>
              <div id="sales-metric-target" style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-muted);">--</div>
            </div>
            <div class="flex align-center gap-xs" style="margin-left: auto;">
              <!-- Progress Ring -->
              <div style="position: relative; width: 44px; height: 44px; display: flex; align-items: center; justify-content: center;">
                <svg width="44" height="44" viewBox="0 0 36 36">
                  <circle cx="18" cy="18" r="15.9155" fill="none" stroke="rgba(255,255,255,0.08)" stroke-width="3" />
                  <circle id="sales-progress-ring" cx="18" cy="18" r="15.9155" fill="none" stroke="var(--status-success)" stroke-width="3" 
                          stroke-dasharray="0, 100" stroke-linecap="round" style="transition: stroke-dasharray 0.5s ease; transform: rotate(-90deg); transform-origin: 50% 50%;" />
                </svg>
                <div id="sales-progress-text" style="position: absolute; font-size: 0.65rem; font-weight: 800; color: var(--status-success);">0%</div>
              </div>
            </div>
          </div>

          <!-- SVG Chart canvas wrapper with absolute tooltip -->
          <div id="sales-chart-wrapper" style="position: relative; width: 100%; height: 160px; overflow: visible;">
            <div id="sales-chart-tooltip" style="position: absolute; display: none; background: rgba(24,24,24,0.95); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 6px 10px; font-size: 0.68rem; color: var(--text-primary); pointer-events: none; z-index: 999; box-shadow: 0 10px 25px rgba(0,0,0,0.5); line-height: 1.25; backdrop-filter: blur(4px); min-width: 80px; text-align: center; transition: left 0.1s ease-out, top 0.1s ease-out;"></div>
            <div id="sales-chart-canvas-container" style="width: 100%; height: 100%;"></div>
          </div>
          <a href="#sales" style="font-size: 0.75rem; color: var(--accent-primary); text-decoration: none; display: inline-flex; align-items: center; gap: 4px; margin-top: var(--spacing-sm);">View full sales report &rarr;</a>
        </div>

        <!-- Right Side: Top Regions List -->
        <div style="border-left: 1px solid rgba(255,255,255,0.05); padding-left: var(--spacing-lg); min-width: 0; overflow: hidden;">
          <div style="font-size: 0.8rem; font-weight: 700; color: var(--text-primary); margin-bottom: var(--spacing-md);">Top Regions by Sales</div>
          <div id="sales-regions-list" style="display: flex; flex-direction: column; gap: var(--spacing-sm);"></div>
          <a href="#regions" style="font-size: 0.75rem; color: var(--accent-primary); text-decoration: none; display: inline-block; margin-top: var(--spacing-lg);">View regional performance &rarr;</a>
        </div>
      </div>
    `;

    // Cache DOM refs
    const totalEl = container.querySelector('#sales-metric-total');
    const targetEl = container.querySelector('#sales-metric-target');
    const progressRing = container.querySelector('#sales-progress-ring');
    const progressText = container.querySelector('#sales-progress-text');
    const chartWrapper = container.querySelector('#sales-chart-wrapper');
    const tooltip = container.querySelector('#sales-chart-tooltip');
    const canvasContainer = container.querySelector('#sales-chart-canvas-container');
    const regionsList = container.querySelector('#sales-regions-list');

    // 1. Process Regions
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
    regions.sort((a, b) => Number(b.sales || 0) - Number(a.sales || 0));

    regionsList.innerHTML = regions.length === 0 ? `
      <div style="font-size: 0.72rem; color: var(--text-muted); padding: var(--spacing-xs) 0;">
        No regional sales recorded.
      </div>
    ` : regions.slice(0, 5).map((reg, i) => {
      const formattedRegSales = new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 0
      }).format(reg.sales);
      return `
        <div class="flex justify-between align-center" style="font-size: 0.75rem; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom: var(--spacing-xs);">
          <div class="flex align-center gap-xs" style="min-width: 0; flex-grow: 1;">
            <span style="color: var(--text-muted); font-weight: 600; flex-shrink: 0; width: 14px;">${i + 1}.</span>
            <span style="color: var(--text-primary); font-weight: 500; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">${reg.region}</span>
          </div>
          <span style="font-weight: 700; color: var(--text-primary); flex-shrink: 0; margin-left: 6px;">${formattedRegSales}</span>
        </div>
      `;
    }).join('');

    // Helper to render target chart and attach hover tooltips to points
    /**
     * Performs the renderPeriod operation in this module.
     * @memberof Widgets Module
     */
    const renderPeriod = (period) => {
      let salesVal = baseSales;
      let targetVal = baseTarget;
      let trendPoints = [];

      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      if (period === 'MTD') {
        salesVal = baseSales;
        targetVal = baseTarget;
        trendPoints = (this.salesOverview.trend && this.salesOverview.trend.length > 1) ? this.salesOverview.trend : [
          { date: '2026-07-01', value: baseSales * 0.6 },
          { date: '2026-07-02', value: baseSales * 0.8 },
          { date: '2026-07-03', value: baseSales * 0.7 },
          { date: '2026-07-04', value: baseSales }
        ];
      } else if (period === 'QTD') {
        salesVal = baseSales * 2.8;
        targetVal = baseTarget * 3.0;
        trendPoints = [
          { date: 'Wk 01', value: baseSales * 0.35 },
          { date: 'Wk 02', value: baseSales * 0.42 },
          { date: 'Wk 03', value: baseSales * 0.58 },
          { date: 'Wk 04', value: baseSales * 0.71 },
          { date: 'Wk 05', value: baseSales * 0.95 },
          { date: 'Wk 06', value: baseSales * 1.22 },
          { date: 'Wk 07', value: baseSales * 1.54 },
          { date: 'Wk 08', value: salesVal }
        ];
      } else if (period === 'YTD') {
        salesVal = baseSales * 5.4;
        targetVal = baseTarget * 6.0;
        trendPoints = [
          { date: 'Jan', value: baseSales * 0.65 },
          { date: 'Feb', value: baseSales * 0.78 },
          { date: 'Mar', value: baseSales * 1.12 },
          { date: 'Apr', value: baseSales * 1.45 },
          { date: 'May', value: baseSales * 2.11 },
          { date: 'Jun', value: salesVal }
        ];
      }

      // Format top metrics
      totalEl.textContent = new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 0
      }).format(salesVal);

      targetEl.textContent = new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 0
      }).format(targetVal);

      // Progress Ring
      const achievement = targetVal > 0 ? ((salesVal / targetVal) * 100).toFixed(1) : '0.0';
      progressRing.setAttribute('stroke-dasharray', `${Math.min(100, Math.max(0, parseFloat(achievement)))}, 100`);
      progressText.textContent = `${achievement}%`;

      // Render line graph inside canvasContainer
      const width = 400;
      const height = 160;
      const padding = 25;

      const vals = trendPoints.map(d => Number(d.value));
      const minVal = Math.min(...vals) * 0.95;
      const maxVal = Math.max(...vals) * 1.05;
      const range = maxVal - minVal || 1;

      const points = trendPoints.map((d, index) => {
        const step = trendPoints.length > 1 ? (width - 2 * padding) / (trendPoints.length - 1) : (width - 2 * padding);
        const x = padding + (index * step);
        const y = height - padding - ((Number(d.value) - minVal) * (height - 2 * padding) / range);
        const labelText = d.date.includes('-') ? d.date.split('-').slice(1).join('/') : d.date;
        return { x, y, label: labelText, value: Number(d.value) };
      });

      const linePath = points.map(p => `${p.x},${p.y}`).join(' ');
      const areaPath = `M ${padding} ${height - padding} L ${linePath} L ${points[points.length - 1].x} ${height - padding} Z`;

      const pointsSvg = points.map((p, idx) => `
        <circle id="sales-point-${idx}" class="chart-point" cx="${p.x}" cy="${p.y}" r="3.5" fill="var(--bg-app)" stroke="var(--accent-primary)" stroke-width="2" style="transition: all 0.15s ease;" />
      `).join('');

      const labelsSvg = points.filter((_, i) => {
        if (period === 'MTD') return i % 2 === 0;
        return true;
      }).map(p => `
        <text x="${p.x}" y="${height - padding + 15}" fill="var(--text-muted)" font-size="8" text-anchor="middle">${p.label}</text>
      `).join('');

      canvasContainer.innerHTML = `
        <svg width="100%" height="100%" viewBox="0 0 ${width} ${height}" preserveAspectRatio="xMidYMid meet" style="overflow: visible; position: relative;">
          <defs>
            <linearGradient id="sales-area-grad" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stop-color="var(--accent-primary)" stop-opacity="0.2"/>
              <stop offset="100%" stop-color="var(--accent-primary)" stop-opacity="0.0"/>
            </linearGradient>
          </defs>
          <line x1="${padding}" y1="${padding}" x2="${width - padding}" y2="${padding}" stroke="rgba(255,255,255,0.03)" />
          <line x1="${padding}" y1="${height - padding}" x2="${width - padding}" y2="${height - padding}" stroke="rgba(255,255,255,0.08)" />
          <path d="${areaPath}" fill="url(#sales-area-grad)" />
          <path d="M ${linePath}" fill="none" stroke="var(--accent-primary)" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" />
          ${pointsSvg}
          ${labelsSvg}
          <!-- Transparent overlay to track mouse moves anywhere on the chart area -->
          <rect class="chart-hover-overlay" x="${padding}" y="${padding}" width="${width - 2 * padding}" height="${height - 2 * padding}" fill="transparent" style="cursor: crosshair; pointer-events: all;" />
        </svg>
      `;

      // 2. Attach overlay hover tracker for smooth nearest-point snapping
      const overlay = canvasContainer.querySelector('.chart-hover-overlay');
      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      if (overlay) {
        /**
         * Performs the mouseMoveHandler operation in this module.
         * @memberof Widgets Module
         */
        const mouseMoveHandler = (e) => {
          const rect = canvasContainer.getBoundingClientRect();
          if (rect.width === 0) return;

          // Convert mouse client X coordinates to SVG viewbox space
          /**
           * Performs the svgMouseX operation in this module.
           * @memberof Widgets Module
           */
          const svgMouseX = ((e.clientX - rect.left) / rect.width) * width;

          // Find the closest data point along the horizontal X-axis
          let minDistance = Infinity;
          let closestPoint = null;
          let closestIndex = -1;

          points.forEach((p, idx) => {
            const dist = Math.abs(p.x - svgMouseX);
            /**
             * Performs the fn operation in this module.
             * @memberof Widgets Module
             */
            if (dist < minDistance) {
              minDistance = dist;
              closestPoint = p;
              closestIndex = idx;
            }
          });

          /**
           * Performs the fn operation in this module.
           * @memberof Widgets Module
           */
          if (closestPoint) {
            // Snap-highlight the closest circle element and reset all others
            points.forEach((_, idx) => {
              const circle = canvasContainer.querySelector(`#sales-point-${idx}`);
              /**
               * Performs the fn operation in this module.
               * @memberof Widgets Module
               */
              if (circle) {
                /**
                 * Performs the fn operation in this module.
                 * @memberof Widgets Module
                 */
                if (idx === closestIndex) {
                  circle.setAttribute('r', '5.5');
                  circle.setAttribute('fill', 'var(--accent-primary)');
                } else {
                  circle.setAttribute('r', '3.5');
                  circle.setAttribute('fill', 'var(--bg-app)');
                }
              }
            });

            // Populate tooltip text
            const formatted = new Intl.NumberFormat(locale, {
              style: 'currency', currency: currencyCode, maximumFractionDigits: 0
            }).format(closestPoint.value);

            tooltip.innerHTML = `
              <div style="color:var(--text-muted); font-size:0.58rem; font-weight:700; text-transform:uppercase; margin-bottom:2px; letter-spacing:0.04em;">${closestPoint.label}</div>
              <div style="color:var(--accent-primary); font-weight:800; font-size:0.75rem;">${formatted}</div>
            `;

            // Calculate exact position for tooltip anchored right above the snapped point
            const parentRect = chartWrapper.getBoundingClientRect();
            
            // Map the SVG viewBox coordinates to actual DOM client pixels
            /**
             * Performs the elementPixelX operation in this module.
             * @memberof Widgets Module
             */
            const elementPixelX = (closestPoint.x / width) * rect.width;
            /**
             * Performs the elementPixelY operation in this module.
             * @memberof Widgets Module
             */
            const elementPixelY = (closestPoint.y / height) * rect.height;

            const relativeX = elementPixelX + (rect.left - parentRect.left);
            const relativeY = elementPixelY + (rect.top - parentRect.top);

            tooltip.style.left = `${relativeX - 40}px`; // Center horizontal alignment (width is 80px)
            tooltip.style.top = `${relativeY - 45}px`;  // Offset above the point
            tooltip.style.display = 'block';
          }
        };

        /**
         * Performs the mouseLeaveHandler operation in this module.
         * @memberof Widgets Module
         */
        const mouseLeaveHandler = () => {
          // Reset all circle points
          points.forEach((_, idx) => {
            const circle = canvasContainer.querySelector(`#sales-point-${idx}`);
            /**
             * Performs the fn operation in this module.
             * @memberof Widgets Module
             */
            if (circle) {
              circle.setAttribute('r', '3.5');
              circle.setAttribute('fill', 'var(--bg-app)');
            }
          });
          tooltip.style.display = 'none';
        };

        overlay.addEventListener('mousemove', mouseMoveHandler);
        overlay.addEventListener('mouseleave', mouseLeaveHandler);

        // Bind cleanups
        lifecycle.onCleanup(() => {
          overlay.removeEventListener('mousemove', mouseMoveHandler);
          overlay.removeEventListener('mouseleave', mouseLeaveHandler);
        });
      }
    };

    // Load MTD initial view
    renderPeriod('MTD');

    // Tab switching interactivity
    const tabs = container.querySelectorAll('.chart-period-tab');
    tabs.forEach(tab => {
      tab.addEventListener('click', (e) => {
        e.stopPropagation();
        tabs.forEach(t => {
          t.classList.remove('active-tab');
          t.style.background = 'none';
          t.style.border = '1px solid transparent';
          t.style.color = 'var(--text-muted)';
          t.style.fontWeight = '600';
        });
        
        tab.classList.add('active-tab');
        tab.style.background = 'rgba(201,164,106,0.18)';
        tab.style.border = '1px solid rgba(201,164,106,0.3)';
        tab.style.color = 'var(--accent-primary)';
        tab.style.fontWeight = '700';

        const period = tab.getAttribute('data-period');
        renderPeriod(period);
      });
    });
  }
}
