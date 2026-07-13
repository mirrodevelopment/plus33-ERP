/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : alerts-list.js
 * Path              : frontend/widgets/notifications/alerts-list.js
 * Purpose           : Frontend utility: alerts-list for PLUS33 Coffee ERP
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : 
 * Depends On        : 
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend utility: alerts-list for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

export class AlertsList {
  constructor(config, alerts) {
    this.config = config;
    this.alerts = alerts || [];
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    // Merge backend alerts with sensible defaults
    const backendAlerts = this.alerts.map((a, i) => ({
      id: a.id || i + 1,
      message: a.message || 'System notification',
      severity: a.type === 'WARNING' ? 'warning' : (a.type === 'DANGER' ? 'danger' : 'info'),
      count: a.count || 1
    }));

    const rawAlerts = backendAlerts;

    const severityIcon = { danger: 'alert-circle', warning: 'alert-triangle', info: 'info' };
    const severityColor = {
      danger: 'var(--status-danger)',
      warning: 'var(--status-warning)',
      info: 'var(--status-info)'
    };

    container.innerHTML = `
      <div class="flex justify-between align-center mb-md" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs);">
        <h3 class="m-0" style="font-size: 0.95rem; font-weight: 700; color: var(--text-primary);">${this.config.title}</h3>
        <a href="#alerts" style="font-size: 0.75rem; color: var(--accent-primary); text-decoration: none;">View All</a>
      </div>
      
      <div class="flex flex-col gap-sm">
        ${rawAlerts.length === 0 ? `
          <div style="text-align: center; color: var(--text-muted); padding: var(--spacing-lg) 0; font-size: 0.72rem; border: 1px dashed rgba(255,255,255,0.05); border-radius: var(--radius-md);">
            No pending alerts or notifications.
          </div>
        ` : rawAlerts.map(a => {
          const color = severityColor[a.severity] || severityColor.info;
          const icon = severityIcon[a.severity] || 'info';
          return `
            <div class="alert-item flex justify-between align-center" style="border: 1px solid rgba(255,255,255,0.05); border-radius: var(--radius-md); background: rgba(255,255,255,0.01); padding: 7px 10px; min-width: 0; gap: var(--spacing-sm);">
              <div class="flex align-center gap-sm" style="min-width: 0; flex-grow: 1;">
                <i data-lucide="${icon}" style="width:13px; height:13px; color:${color}; flex-shrink:0;"></i>
                <span style="font-size: 0.73rem; color: var(--text-secondary); text-overflow: ellipsis; overflow: hidden; white-space: nowrap;">${a.message}</span>
              </div>
              <span style="font-size: 0.68rem; background: rgba(255,255,255,0.05); padding: 2px 8px; border-radius: 20px; font-weight: 700; border: 1px solid rgba(255,255,255,0.08); flex-shrink: 0; color: ${color};">${a.count}</span>
            </div>
          `;
        }).join('')}
      </div>
    `;
  }
}
