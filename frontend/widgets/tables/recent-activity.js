/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : recent-activity.js
 * Path              : frontend/widgets/tables/recent-activity.js
 * Purpose           : Frontend utility: recent-activity for PLUS33 Coffee ERP
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
 * Frontend utility: recent-activity for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

export class RecentActivity {
  constructor(config, recentActivities) {
    this.config = config;
    this.recentActivities = recentActivities || [];
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    // Normalize backend activity objects
    const backendActivities = this.recentActivities.map(a => ({
      action: typeof a === 'string' ? a : (a.description || a.action || ''),
      time: a.time || '--',
      operator: a.operator || a.user || 'System',
      module: a.module || 'ERP'
    }));

    const activities = backendActivities;

    const moduleColor = {
      'Stores': 'var(--accent-primary)',
      'HR': 'var(--status-info)',
      'Procurement': 'var(--status-warning)',
      'Inventory': '#e67e22',
      'Compliance': 'var(--status-success)',
      'Franchise': 'var(--accent-secondary)',
      'ERP': 'var(--text-muted)',
      'System': 'var(--text-muted)'
    };

    container.innerHTML = `
      <div class="flex justify-between align-center mb-md" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs);">
        <h3 class="m-0" style="font-size: 0.95rem; font-weight: 700; color: var(--text-primary);">${this.config.title}</h3>
        <a href="#activities" style="font-size: 0.75rem; color: var(--accent-primary); text-decoration: none;">View All</a>
      </div>
      
      <div style="overflow-x: auto; width: 100%;">
        <table style="width: 100%; border-collapse: collapse; text-align: left; font-size: 0.73rem;">
          <thead>
            <tr style="border-bottom: 1px solid var(--border-color); color: var(--text-muted); font-weight: 600; font-size: 0.65rem; text-transform: uppercase; letter-spacing: 0.04em;">
              <th style="padding: 4px 0; padding-bottom: 8px;">Activity / Operation</th>
              <th style="padding: 4px; text-align: center; padding-bottom: 8px;">Module</th>
              <th style="padding: 4px; text-align: right; padding-bottom: 8px;">Time</th>
              <th style="padding: 4px; text-align: right; padding-bottom: 8px;">Operator</th>
            </tr>
          </thead>
          <tbody>
            ${activities.length === 0 ? `
              <tr>
                <td colspan="4" style="text-align: center; color: var(--text-muted); padding: var(--spacing-lg) 0; font-size: 0.72rem;">
                  No recent activities recorded.
                </td>
              </tr>
            ` : activities.map(a => {
              const color = moduleColor[a.module] || moduleColor.ERP;
              return `
                <tr style="border-bottom: 1px solid rgba(255,255,255,0.03); transition: background 0.15s;" onmouseover="this.style.background='rgba(255,255,255,0.02)'" onmouseout="this.style.background='none'">
                  <td style="padding: 7px 0; color: var(--text-primary); font-weight: 500;">
                    <span style="display:inline-block; width:5px; height:5px; border-radius:50%; background:${color}; margin-right:7px; vertical-align:middle;"></span>${a.action}
                  </td>
                  <td style="padding: 7px 4px; text-align:center;">
                    <span style="font-size:0.62rem; background:rgba(255,255,255,0.04); border:1px solid rgba(255,255,255,0.08); border-radius:4px; padding:1px 6px; color:${color}; white-space:nowrap; font-weight:600;">${a.module}</span>
                  </td>
                  <td style="padding: 7px 4px; text-align: right; color: var(--text-muted); white-space: nowrap; font-size:0.68rem;">${a.time}</td>
                  <td style="padding: 7px 4px; text-align: right; color: var(--accent-primary); font-weight: 600; white-space: nowrap; font-size:0.68rem;">${a.operator}</td>
                </tr>
              `;
            }).join('')}
          </tbody>
        </table>
      </div>
    `;
  }
}
