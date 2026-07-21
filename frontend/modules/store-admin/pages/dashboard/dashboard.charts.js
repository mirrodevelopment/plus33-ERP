/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Admin — Dashboard Charts
 * File              : dashboard.charts.js
 * Purpose           : Encapsulates math and rendering for gauges and donut charts
 * Version           : 1.0.0
 ******************************************************************************/

export class StoreAdminDashboardCharts {
  /**
   * Render the sales progress gauge (conic gradient).
   * @param {HTMLElement} container
   * @param {number} todayAchievedPercent
   */
  static renderSalesProgress(container, todayAchievedPercent) {
    const gauge = container.querySelector('#sales-progress-gauge');
    const percentageText = container.querySelector('#sales-progress-percentage');

    if (gauge) {
      gauge.style.background = todayAchievedPercent > 0 
        ? `conic-gradient(var(--status-success) 0% ${todayAchievedPercent}%, rgba(255,255,255,0.05) ${todayAchievedPercent}% 100%)`
        : 'rgba(255,255,255,0.05)';
    }
    if (percentageText) {
      percentageText.textContent = todayAchievedPercent > 0 ? `${todayAchievedPercent}%` : 'NA/DB';
    }
  }

  /**
   * Render the workforce attendance gauge (conic gradient).
   * @param {HTMLElement} container
   * @param {number} presentPercent
   */
  static renderAttendanceGauge(container, presentPercent) {
    const gauge = container.querySelector('#attendance-gauge');
    const percentageText = container.querySelector('#attendance-gauge-percent');

    if (gauge) {
      gauge.style.background = presentPercent > 0
        ? `conic-gradient(var(--status-success) 0% ${presentPercent}%, var(--status-danger) ${presentPercent}% 100%)`
        : 'rgba(255,255,255,0.05)';
    }
    if (percentageText) {
      percentageText.textContent = presentPercent > 0 ? `${presentPercent}%` : 'NA/DB';
    }
  }

  /**
   * Render the SVG Donut chart for the Supply Chain & Inventory panel.
   * @param {HTMLElement} container
   * @param {Object} inventoryOverview
   */
  static renderInventoryDonut(container, inventoryOverview) {
    const total = Number(inventoryOverview.stockInHand || 0);
    const low = Number(inventoryOverview.lowStockCount || 0);
    const out = Number(inventoryOverview.outOfStockCount || 0);
    const normal = Math.max(0, total - low - out);

    const normalEl = container.querySelector('#donut-segment-normal');
    const lowEl = container.querySelector('#donut-segment-low');
    const outEl = container.querySelector('#donut-segment-out');
    const totalText = container.querySelector('#donut-total-count');

    if (totalText) {
      totalText.textContent = total.toLocaleString();
    }

    if (total === 0) {
      if (normalEl) normalEl.style.strokeDasharray = '0 238.76';
      if (lowEl) lowEl.style.strokeDasharray = '0 238.76';
      if (outEl) outEl.style.strokeDasharray = '0 238.76';
      return;
    }

    // Proportions
    let pNormal = normal / total;
    let pLow = low > 0 ? Math.max(0.08, low / total) : 0;
    let pOut = out > 0 ? Math.max(0.08, out / total) : 0;

    // Normalize so they sum to 1.0
    const sum = pNormal + pLow + pOut;
    if (sum > 0) {
      pNormal /= sum;
      pLow /= sum;
      pOut /= sum;
    }

    const circ = 2 * Math.PI * 38; // ~238.76
    const dashNormal = circ * pNormal;
    const dashLow = circ * pLow;
    const dashOut = circ * pOut;

    if (normalEl) {
      normalEl.style.strokeDasharray = `${dashNormal} ${circ}`;
      normalEl.style.strokeDashoffset = '0';
    }
    if (lowEl) {
      lowEl.style.strokeDasharray = `${dashLow} ${circ}`;
      lowEl.style.strokeDashoffset = `-${dashNormal}`;
    }
    if (outEl) {
      outEl.style.strokeDasharray = `${dashOut} ${circ}`;
      outEl.style.strokeDashoffset = `-${dashNormal + dashLow}`;
    }
  }
}
