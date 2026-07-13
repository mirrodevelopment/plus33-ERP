/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : page.js
 * Path              : frontend/pages/logs/page.js
 * Purpose           : Frontend page component for the Pages Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/platform/dashboard, GET /api/platform/logs/audit, GET /api/platform/logs/system
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : api/client, core/logger, store/notificationStore
 * Depends On        : api/client, core/logger, store/notificationStore
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend page component for the Pages Module UI. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { apiClient } from '../../../../api/client.js';
import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';

export default class LogsPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  constructor() {
    this.auditLogs = [];
    this.systemLogs = [];
    this.dashboardStats = {
      totalCacheNodes: 4,
      totalPods: 12,
      totalRegions: 6,
      totalBreakers: 2,
      status: "HEALTHY"
    };
    this.activeTab = 'audit'; // 'audit' or 'system'
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  async mount(container, lifecycle) {
    logger.info('LogsPage', 'Mounting Platform Operations & Logs Auditor...');

    // Fetch dynamic operational dashboard stats & logs
    await this.fetchData();

    container.innerHTML = `
      <!-- Page Header -->
      <div class="dashboard-header flex justify-between align-center mb-lg" style="flex-wrap: wrap; gap: var(--spacing-md);">
        <div>
          <h2 class="m-0" style="font-family: var(--font-display); font-weight: 800; font-size: 1.65rem; letter-spacing: -0.02em;">
            Platform Operations &amp; Logs Auditor
          </h2>
          <p class="m-0" style="color: var(--text-muted); font-size: 0.82rem; margin-top: 2px;">
            Monitor Kubernetes pod pools, distributed cache status, and double-entry platform audit transactions
          </p>
        </div>
        <div style="display:flex; align-items:center; gap: var(--spacing-md);">
          <!-- Live Status Pill -->
          <div style="display:flex; align-items:center; gap:6px; background: rgba(130,163,125,0.12); border: 1px solid rgba(130,163,125,0.3); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.75rem; font-weight: 600; color: var(--status-success);">
            <span style="width:7px; height:7px; border-radius:50%; background: var(--status-success); display:inline-block; animation: pulse-dot 2s infinite;"></span>
            AIOps Stable
          </div>
        </div>
      </div>

      <!-- KPI Grid -->
      <div class="grid grid-cols-4 gap-md mb-lg">
        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(201,164,106,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--accent-primary); display:flex; align-items:center;">
            <i data-lucide="server" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">${this.dashboardStats.totalCacheNodes} Nodes</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Cache Nodes</div>
          </div>
        </div>

        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(59,130,246,0.1); border-radius: var(--radius-md); padding: 10px; color: #3b82f6; display:flex; align-items:center;">
            <i data-lucide="container" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">${this.dashboardStats.totalPods} Pods</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Kubernetes Pools</div>
          </div>
        </div>

        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(130,163,125,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-success); display:flex; align-items:center;">
            <i data-lucide="globe" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">${this.dashboardStats.totalRegions} Regions</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Active Clusters</div>
          </div>
        </div>

        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(46,204,113,0.1); border-radius: var(--radius-md); padding: 10px; color: #2ecc71; display:flex; align-items:center;">
            <i data-lucide="shield-check" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">${this.dashboardStats.status}</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Runtime Health</div>
          </div>
        </div>
      </div>

      <!-- Main Layout Panels -->
      <div class="grid grid-cols-3 gap-lg mb-lg">
        
        <!-- Left: Interactive Tabbed Log Viewer -->
        <div class="card glass col-span-2 flex flex-col" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05); min-height: 480px;">
          <div class="flex justify-between align-center mb-md flex-wrap gap-sm">
            <!-- Tabs Controls -->
            <div class="flex gap-xs" style="background:rgba(0,0,0,0.2); padding:2px; border-radius:var(--radius-md); border:1px solid rgba(255,255,255,0.05);">
              <button id="tab-audit-logs" class="btn" style="padding:6px 16px; border-radius:var(--radius-sm); font-size:0.75rem; font-weight:700; cursor:pointer; border:none; transition:var(--transition-fast); background:${this.activeTab === 'audit' ? 'rgba(255,255,255,0.08)' : 'transparent'}; color:${this.activeTab === 'audit' ? '#fff' : 'var(--text-muted)'};">
                Audit Logs
              </button>
              <button id="tab-system-logs" class="btn" style="padding:6px 16px; border-radius:var(--radius-sm); font-size:0.75rem; font-weight:700; cursor:pointer; border:none; transition:var(--transition-fast); background:${this.activeTab === 'system' ? 'rgba(255,255,255,0.08)' : 'transparent'}; color:${this.activeTab === 'system' ? '#fff' : 'var(--text-muted)'};">
                System Diagnostics
              </button>
            </div>
            
            <span style="font-size: 0.7rem; color: var(--text-muted); font-weight:600; background:rgba(255,255,255,0.05); padding: 2px 8px; border-radius: 4px;">
              Live Stream Enabled
            </span>
          </div>
          
          <div style="overflow-x: auto; flex-grow: 1;">
            <table class="w-100" style="border-collapse: collapse; text-align: left; font-size: 0.8rem;">
              <thead id="log-table-head">
                ${this.renderLogHeader()}
              </thead>
              <tbody id="log-table-body">
                ${this.renderLogRows()}
              </tbody>
            </table>
          </div>
        </div>
        
        <!-- Right: Actions Panels -->
        <div class="flex flex-col gap-lg col-span-1">
          <!-- 1. Evict Cache Cluster Form -->
          <div class="card glass" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05);">
            <div class="flex align-center gap-xs mb-md">
              <i data-lucide="trash-2" style="color: var(--accent-primary); width:18px; height:18px;"></i>
              <h3 class="m-0" style="font-family: var(--font-display); font-weight: 700; font-size: 1.05rem;">
                Evict Cache Key
              </h3>
            </div>
            
            <form id="cache-evict-form" style="display:flex; flex-direction:column; gap: var(--spacing-sm);">
              <div class="flex flex-col gap-xs">
                <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Namespace</label>
                <input type="text" id="cache-ns" placeholder="wms.locations" required style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none;" />
              </div>
              
              <div class="flex flex-col gap-xs">
                <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Key identifier</label>
                <input type="text" id="cache-key" placeholder="ledger" required style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none;" />
              </div>

              <button type="submit" class="btn" style="background:var(--accent-primary); color:#000; font-weight:700; font-size:0.75rem; text-transform:uppercase; padding:8px; border-radius:var(--radius-sm); border:none; display:flex; align-items:center; justify-content:center; gap:4px; cursor:pointer; margin-top:var(--spacing-xs); transition:var(--transition-fast);">
                <i data-lucide="zap" style="width:14px; height:14px;"></i>
                Evict Key
              </button>
            </form>
          </div>

          <!-- 2. Set Config Form -->
          <div class="card glass" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05);">
            <div class="flex align-center gap-xs mb-md">
              <i data-lucide="sliders" style="color: var(--accent-primary); width:18px; height:18px;"></i>
              <h3 class="m-0" style="font-family: var(--font-display); font-weight: 700; font-size: 1.05rem;">
                Set Global Variable
              </h3>
            </div>
            
            <form id="config-set-form" style="display:flex; flex-direction:column; gap: var(--spacing-sm);">
              <div class="flex gap-sm">
                <div class="flex flex-col gap-xs flex-grow">
                  <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Key</label>
                  <input type="text" id="cfg-key" placeholder="route.optimization" required style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none;" />
                </div>
                <div class="flex flex-col gap-xs" style="width:100px;">
                  <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Profile</label>
                  <input type="text" id="cfg-profile" value="production" required style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none; text-align:center;" />
                </div>
              </div>

              <div class="flex flex-col gap-xs">
                <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Value</label>
                <input type="text" id="cfg-val" placeholder="green" required style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none;" />
              </div>

              <button type="submit" class="btn" style="background:var(--accent-primary); color:#000; font-weight:700; font-size:0.75rem; text-transform:uppercase; padding:8px; border-radius:var(--radius-sm); border:none; display:flex; align-items:center; justify-content:center; gap:4px; cursor:pointer; margin-top:var(--spacing-xs); transition:var(--transition-fast);">
                <i data-lucide="save" style="width:14px; height:14px;"></i>
                Update Variable
              </button>
            </form>
          </div>
        </div>

      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  async fetchData() {
    try {
      const [dashRes, auditRes, sysRes] = await Promise.all([
        apiClient.get('/api/platform/dashboard'),
        apiClient.get('/api/platform/logs/audit'),
        apiClient.get('/api/platform/logs/system')
      ]);

      if (dashRes) this.dashboardStats = dashRes;
      if (auditRes) this.auditLogs = auditRes;
      if (sysRes) this.systemLogs = sysRes;
    } catch (err) {
      logger.error('LogsPage', 'Failed to fetch database audit/system logs:', err);
      this.auditLogs = [];
      this.systemLogs = [];
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  renderLogHeader() {
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (this.activeTab === 'audit') {
      return `
        <tr style="border-bottom: 1px solid rgba(255,255,255,0.08); color: var(--text-muted); font-size: 0.72rem; text-transform: uppercase; font-weight:700;">
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Log ID</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Action Event</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md); color: var(--accent-primary);">Operator (Who Made Change)</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Trace Context</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md); color: var(--accent-primary);">Change Details (What Was Changed)</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md); text-align:right;">Timestamp</th>
        </tr>
      `;
    } else {
      return `
        <tr style="border-bottom: 1px solid rgba(255,255,255,0.08); color: var(--text-muted); font-size: 0.72rem; text-transform: uppercase; font-weight:700;">
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Node ID</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Service</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Level</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Logger</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Message</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md); text-align:right;">Timestamp</th>
        </tr>
      `;
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  renderLogRows() {
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (this.activeTab === 'audit') {
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (!this.auditLogs || this.auditLogs.length === 0) {
        return `<tr><td colspan="6" style="padding: var(--spacing-xl); text-align:center; color: var(--text-muted);">No audit trails generated.</td></tr>`;
      }
      return this.auditLogs.map(l => {
        return `
          <tr style="border-bottom: 1px solid rgba(255,255,255,0.04); transition: var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.01)'" onmouseout="this.style.background='transparent'">
            <td style="padding: var(--spacing-md); font-weight:700; color:var(--text-muted); font-family:monospace;">#${l.id}</td>
            <td style="padding: var(--spacing-md); font-weight:700; color:var(--accent-primary); font-family:monospace;">${l.actionName}</td>
            <td style="padding: var(--spacing-md); color:#fff; font-size:0.78rem;">${l.userIdentity}</td>
            <td style="padding: var(--spacing-md); color:var(--text-muted); font-size:0.75rem;">${l.traceContext}</td>
            <td style="padding: var(--spacing-md); color:var(--text-primary); font-family:monospace; font-size:0.72rem;">${l.payloadDiff}</td>
            <td style="padding: var(--spacing-md); text-align:right; color:var(--text-muted); font-size:0.75rem;">${l.createdAt.replace('T', ' ')}</td>
          </tr>
        `;
      }).join('');
    } else {
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (!this.systemLogs || this.systemLogs.length === 0) {
        return `<tr><td colspan="6" style="padding: var(--spacing-xl); text-align:center; color: var(--text-muted);">No system diagnostic logs available.</td></tr>`;
      }
      return this.systemLogs.map(l => {
        let levelColor = '#3b82f6';
        if (l.logLevel === 'WARN') levelColor = '#e67e22';
        if (l.logLevel === 'ERROR' || l.logLevel === 'FATAL') levelColor = 'var(--status-danger)';

        return `
          <tr style="border-bottom: 1px solid rgba(255,255,255,0.04); transition: var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.01)'" onmouseout="this.style.background='transparent'">
            <td style="padding: var(--spacing-md); font-weight:700; color:var(--text-muted); font-family:monospace;">${l.nodeId || 'node-host-1'}</td>
            <td style="padding: var(--spacing-md); font-weight:700; color:#fff;">${l.serviceName}</td>
            <td style="padding: var(--spacing-md);">
              <span style="font-size:0.68rem; font-weight:700; padding:2px 8px; border-radius:3px; background:rgba(255,255,255,0.05); color:${levelColor};">
                ${l.logLevel}
              </span>
            </td>
            <td style="padding: var(--spacing-md); color:var(--text-muted); font-size:0.75rem;">${l.logger}</td>
            <td style="padding: var(--spacing-md); color:var(--text-primary); font-size:0.75rem;">${l.message}</td>
            <td style="padding: var(--spacing-md); text-align:right; color:var(--text-muted); font-size:0.75rem;">${l.timestamp.replace('T', ' ')}</td>
          </tr>
        `;
      }).join('');
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindEvents(container, lifecycle) {
    // 1. Tab switches
    const auditBtn = container.querySelector('#tab-audit-logs');
    const sysBtn = container.querySelector('#tab-system-logs');
    const tbody = container.querySelector('#log-table-body');
    const thead = container.querySelector('#log-table-head');
    
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (auditBtn && sysBtn) {
      auditBtn.addEventListener('click', () => {
        this.activeTab = 'audit';
        auditBtn.style.background = 'rgba(255,255,255,0.08)';
        auditBtn.style.color = '#fff';
        sysBtn.style.background = 'transparent';
        sysBtn.style.color = 'var(--text-muted)';
        if (thead) thead.innerHTML = this.renderLogHeader();
        if (tbody) tbody.innerHTML = this.renderLogRows();
      });

      sysBtn.addEventListener('click', () => {
        this.activeTab = 'system';
        sysBtn.style.background = 'rgba(255,255,255,0.08)';
        sysBtn.style.color = '#fff';
        auditBtn.style.background = 'transparent';
        auditBtn.style.color = 'var(--text-muted)';
        if (thead) thead.innerHTML = this.renderLogHeader();
        if (tbody) tbody.innerHTML = this.renderLogRows();
      });
    }

    // 2. Cache Eviction Submit Handler
    const evictForm = container.querySelector('#cache-evict-form');
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (evictForm) {
      /**
       * Handles the handler event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handler = async (e) => {
        e.preventDefault();
        const namespace = container.querySelector('#cache-ns').value.trim();
        const key = container.querySelector('#cache-key').value.trim();
        
        logger.info('LogsPage', `Requesting cache key eviction: ns=${namespace}, key=${key}`);

        try {
          await apiClient.post(`/api/platform/cache/evict?namespace=${encodeURIComponent(namespace)}&key=${encodeURIComponent(key)}&operator=admin@plus33.com`);
          notificationStore.success(`Cache key [${namespace}:${key}] evicted successfully.`, 5000);
          
          // Reload logs to show new audit entry
          await this.fetchData();
          if (tbody) tbody.innerHTML = this.renderLogRows();
          evictForm.reset();
        } catch (err) {
          logger.error('LogsPage', 'Eviction failed:', err);
          notificationStore.danger('Failed to evict cache key. Check system credentials.');
        }
      };
      evictForm.addEventListener('submit', handler);
      lifecycle.onCleanup(() => evictForm.removeEventListener('submit', handler));
    }

    // 3. Set Config Submit Handler
    const configForm = container.querySelector('#config-set-form');
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (configForm) {
      /**
       * Handles the handler event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handler = async (e) => {
        e.preventDefault();
        const key = container.querySelector('#cfg-key').value.trim();
        const value = container.querySelector('#cfg-val').value.trim();
        const profile = container.querySelector('#cfg-profile').value.trim();
        
        logger.info('LogsPage', `Setting config: key=${key}, val=${value}, profile=${profile}`);

        try {
          await apiClient.post(`/api/platform/config?key=${encodeURIComponent(key)}&value=${encodeURIComponent(value)}&profile=${encodeURIComponent(profile)}&operator=admin@plus33.com`);
          notificationStore.success(`Global configuration variable [${key}] updated successfully!`, 5000);
          
          // Reload logs to show new audit entry
          await this.fetchData();
          if (tbody) tbody.innerHTML = this.renderLogRows();
          configForm.reset();
        } catch (err) {
          logger.error('LogsPage', 'Configuration set failed:', err);
          notificationStore.danger('Failed to set config. Check system parameters.');
        }
      };
      configForm.addEventListener('submit', handler);
      lifecycle.onCleanup(() => configForm.removeEventListener('submit', handler));
    }
  }
}



