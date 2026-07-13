/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : widget-engine.js
 * Path              : frontend/widgets/widget-engine.js
 * Purpose           : Frontend utility: widget-engine for PLUS33 Coffee ERP
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
 * Frontend utility: widget-engine for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../core/logger.js';

export class WidgetEngine {
  /**
   * Evaluates layout grid configuration and instantiates the target widget
   * @param {Object} config widget details
   * @param {Object} layout positioning parameters
   * @param {HTMLElement} container target element
   * @param {Object} overview unified dashboard overview payload
   * @param {Object} lifecycle component lifecycle hooks
   */
  static async loadWidget(config, layout, container, overview, lifecycle) {
    const colSpan = layout?.colSpan || 12;

    // Create card wrapper container with grid attributes
    const cardEl = document.createElement('div');
    cardEl.className = `card col-${colSpan} glass animate-slide-up`;
    cardEl.id = `widget-card-${config.id}`;
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (config.type === 'kpi') {
      cardEl.style.padding = 'var(--spacing-sm) var(--spacing-md)';
      cardEl.style.minWidth = '0';
      cardEl.style.overflow = 'hidden';
    }
    container.appendChild(cardEl);

    logger.debug('WidgetEngine', `Dynamic instantiation: ${config.id} (${config.type}) spanning ${colSpan} cols`);

    try {
      let instance;
      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      switch (config.type) {
        case 'kpi': {
          const { KpiCard } = await import('./kpi/kpi-card.js');
          // Merge kpis with financialOverview so totalProfit is available
          const kpiData = {
            ...(overview.kpis || {}),
            totalProfit: (overview.financialOverview || {}).totalProfit ?? (overview.kpis || {}).totalProfit ?? 0
          };
          instance = new KpiCard(config, kpiData);
          break;
        }
        case 'chart-sales':
          const { SalesChart } = await import('./charts/sales-chart/sales-chart.js?v=' + Date.now());
          instance = new SalesChart(config, overview.salesOverview, overview.regionalPerformance);
          break;
        case 'regional-performance':
          const { RegionalPerformance } = await import('./tables/regional-performance.js');
          instance = new RegionalPerformance(config, overview.regionalPerformance);
          break;
        case 'chart-stock':
          const { StockChart } = await import('./charts/stock-chart/stock-chart.js?v=' + Date.now());
          instance = new StockChart(config, overview.storeStatusOverview);
          break;
        case 'chart-financial':
          const { FinancialChart } = await import('./charts/financial-chart/financial-chart.js?v=' + Date.now());
          instance = new FinancialChart(config, overview.financialOverview);
          break;
        case 'inventory-overview':
          const { InventoryWidget } = await import('./inventory/inventory-widget.js?v=' + Date.now());
          instance = new InventoryWidget(config, overview.inventoryOverview);
          break;
        case 'workforce-overview':
          const { WorkforceWidget } = await import('./workforce/workforce-widget.js?v=' + Date.now());
          instance = new WorkforceWidget(config, overview.workforceOverview);
          break;
        case 'compliance-gauge':
          const { ComplianceGauge } = await import('./charts/compliance-gauge/compliance-gauge.js');
          instance = new ComplianceGauge(config, overview.complianceOverview);
          break;
        case 'complaints-legal': {
          const { ComplaintsWidget } = await import('./complaints/complaints-widget.js');
          // Pass the full complianceOverview which contains audit/complaint data
          instance = new ComplaintsWidget(config, overview.complianceOverview);
          break;
        }
        case 'activities':
          const { RecentActivity } = await import('./tables/recent-activity.js');
          instance = new RecentActivity(config, overview.recentActivities);
          break;
        case 'approvals':
          const { ApprovalsList } = await import('./approvals/approvals-list.js');
          instance = new ApprovalsList(config, overview.pendingApprovals);
          break;
        case 'notifications':
          const { AlertsList } = await import('./notifications/alerts-list.js');
          instance = new AlertsList(config, overview.alerts);
          break;
        case 'franchise-development':
          const { FranchiseDevelopment } = await import('./franchise/franchise-development.js?v=' + Date.now());
          instance = new FranchiseDevelopment(config, overview.franchiseDevelopment);
          break;
        case 'quick-actions':
          const { QuickActions } = await import('./actions/quick-actions.js?v=' + Date.now());
          instance = new QuickActions(config);
          break;
        default:
          cardEl.innerHTML = `<p style="color: var(--status-danger);">Unresolved widget engine type: ${config.type}</p>`;
          return;
      }

      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      if (instance) {
        // Individual Widget try-catch boundary
        try {
          await instance.mount(cardEl, lifecycle);
          // Activate Lucide icons injected by this widget
          if (window.lucide) window.lucide.createIcons();
        } catch (widgetError) {
          logger.error('WidgetEngine', `Error rendering widget "${config.id}":`, widgetError);
          cardEl.innerHTML = `
            <div style="display:flex; flex-direction:column; align-items:flex-start; gap:var(--spacing-sm); padding: var(--spacing-lg);">
              <div style="display:flex; align-items:center; gap:8px; color: var(--status-danger);">
                <i data-lucide="alert-triangle" style="width:18px;height:18px;"></i>
                <strong style="font-size:0.9rem;">Widget render error</strong>
              </div>
              <p style="font-size:0.78rem; color:var(--text-muted); margin:0;">${widgetError.message}</p>
              <button onclick="window.location.reload()" style="margin-top:4px; padding: 4px 12px; font-size:0.75rem; background: rgba(201,164,106,0.1); border:1px solid var(--accent-primary); border-radius:6px; color:var(--accent-primary); cursor:pointer;">↺ Retry</button>
            </div>
          `;
          if (window.lucide) window.lucide.createIcons();
        }
      }
    } catch (err) {
      logger.error('WidgetEngine', `Critical loading fault on widget "${config.id}":`, err);
      cardEl.innerHTML = `
        <div style="display:flex; flex-direction:column; align-items:flex-start; gap:var(--spacing-sm); padding: var(--spacing-lg);">
          <div style="display:flex; align-items:center; gap:8px; color: var(--status-danger);">
            <i data-lucide="cloud-off" style="width:18px;height:18px;"></i>
            <strong style="font-size:0.9rem;">Failed to load widget</strong>
          </div>
          <p style="font-size:0.78rem; color:var(--text-muted); margin:0;">${config.title || config.id} — ${err.message}</p>
          <button onclick="window.location.reload()" style="margin-top:4px; padding: 4px 12px; font-size:0.75rem; background: rgba(201,164,106,0.1); border:1px solid var(--accent-primary); border-radius:6px; color:var(--accent-primary); cursor:pointer;">↺ Reload</button>
        </div>
      `;
      if (window.lucide) window.lucide.createIcons();
    }
  }
}
