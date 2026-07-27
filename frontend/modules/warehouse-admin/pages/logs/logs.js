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
    this.loginLogs = [];
    this.dashboardStats = {
      totalCacheNodes: 4,
      totalPods: 12,
      totalRegions: 6,
      totalBreakers: 2,
      status: "HEALTHY"
    };
    this.activeTab = 'logins';
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


      <!-- Main Layout Panels -->
      <div class="mb-lg">
        
        <!-- Interactive Tabbed Log Viewer -->
        <div class="card glass flex flex-col" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05); min-height: 480px;">
          <div class="flex justify-between align-center mb-md flex-wrap gap-sm">
            <!-- Tabs Controls -->
            <div class="flex gap-xs" style="background:rgba(0,0,0,0.2); padding:2px; border-radius:var(--radius-md); border:1px solid rgba(255,255,255,0.05);">
              <button id="tab-login-logs" class="btn" style="padding:6px 16px; border-radius:var(--radius-sm); font-size:0.75rem; font-weight:700; cursor:pointer; border:none; transition:var(--transition-fast); background:${this.activeTab === 'logins' ? 'rgba(255,255,255,0.08)' : 'transparent'}; color:${this.activeTab === 'logins' ? '#fff' : 'var(--text-muted)'};">
                User Login Activity
              </button>
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
      const [dashRes, auditRes, sysRes, loginRes] = await Promise.all([
        apiClient.get('/api/platform/dashboard').catch(() => null),
        apiClient.get('/api/platform/logs/audit').catch(() => []),
        apiClient.get('/api/platform/logs/system').catch(() => []),
        apiClient.get('/api/platform/logs/logins').catch(() => [])
      ]);

      if (dashRes) this.dashboardStats = dashRes;
      if (auditRes) this.auditLogs = auditRes;
      if (sysRes) this.systemLogs = sysRes;
      this.loginLogs = loginRes || [];
    } catch (err) {
      logger.error('LogsPage', 'Failed to fetch database logs:', err);
      this.auditLogs = [];
      this.systemLogs = [];
      this.loginLogs = [];
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  renderLogHeader() {
    if (this.activeTab === 'logins') {
      return `
        <tr style="border-bottom: 1px solid rgba(255,255,255,0.08); color: var(--text-muted); font-size: 0.72rem; text-transform: uppercase; font-weight:700;">
          <th style="padding: var(--spacing-sm) var(--spacing-md);">User Email</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">IP Address</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Resolved Location</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Status</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Session Active Duration</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Client Context</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Login Time</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md); text-align:right;">Logout Time</th>
        </tr>
      `;
    } else if (this.activeTab === 'audit') {
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
    if (this.activeTab === 'logins') {
      if (!this.loginLogs || this.loginLogs.length === 0) {
        return `<tr><td colspan="8" style="padding: var(--spacing-xl); text-align:center; color: var(--text-muted);">No login activity logs available.</td></tr>`;
      }
      return this.loginLogs.map(l => {
        let statusBadge = '';
        if (l.status === 'SUCCESS') {
          statusBadge = `<span style="font-size:0.68rem; font-weight:700; padding:2px 8px; border-radius:3px; background:rgba(46,204,113,0.15); color:#2ecc71;">SUCCESS</span>`;
        } else {
          statusBadge = `<span style="font-size:0.68rem; font-weight:700; padding:2px 8px; border-radius:3px; background:rgba(231,76,60,0.15); color:var(--status-danger);" title="${l.failureReason || 'Invalid credentials'}">FAILED</span>`;
        }

        let durationText = '';
        let isActiveNow = false;
        if (l.status === 'FAILED') {
          durationText = '<span style="color:var(--text-muted);">—</span>';
        } else {
          const loginTime = new Date(l.loginTime);
          let end = l.logoutTime ? new Date(l.logoutTime) : null;

          if (!end && l.lastActiveTime) {
            const lastActive = new Date(l.lastActiveTime);
            const now = new Date();
            if ((now - lastActive) < 60000) {
              isActiveNow = true;
              end = now;
            } else {
              end = lastActive;
            }
          }

          if (!end) end = new Date(l.loginTime);

          const diffMs = end - loginTime;
          const diffMins = isNaN(diffMs) ? 0 : Math.max(0, Math.round(diffMs / 60000));
          
          if (isActiveNow) {
            durationText = `<span style="display:inline-flex; align-items:center; gap:5px; font-weight:700; color:#2ecc71;">Active Now <span style="width:6px; height:6px; border-radius:50%; background:#2ecc71; display:inline-block; animation: pulse-dot 1.5s infinite;"></span> (${diffMins}m elapsed)</span>`;
          } else {
            durationText = `<span style="color:var(--text-primary); font-family:monospace;">${diffMins} min</span>`;
          }
        }

        let logoutText = '—';
        if (l.status === 'SUCCESS') {
          if (l.logoutTime) {
            logoutText = String(l.logoutTime).replace('T', ' ').substring(0, 19);
          } else if (isActiveNow) {
            logoutText = '<span style="color:#2ecc71; font-weight:700;">Active Now</span>';
          } else if (l.lastActiveTime) {
            logoutText = `<span style="color:var(--text-muted);">${String(l.lastActiveTime).replace('T', ' ').substring(0, 19)} <span style="font-size:0.68rem; opacity:0.75; font-style:italic;">(Timed Out)</span></span>`;
          } else {
            logoutText = '<span style="color:var(--text-muted);">Timed Out</span>';
          }
        }

        let shortUA = 'Unknown';
        if (l.userAgent) {
          if (l.userAgent.includes('Chrome')) shortUA = 'Chrome / Browser';
          else if (l.userAgent.includes('Firefox')) shortUA = 'Firefox / Browser';
          else if (l.userAgent.includes('Safari') && !l.userAgent.includes('Chrome')) shortUA = 'Safari / Browser';
          else if (l.userAgent.includes('Postman')) shortUA = 'Postman Client';
          else shortUA = l.userAgent.length > 25 ? l.userAgent.substring(0, 22) + '...' : l.userAgent;
        }

        return `
          <tr style="border-bottom: 1px solid rgba(255,255,255,0.04); transition: var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.01)'" onmouseout="this.style.background='transparent'">
            <td style="padding: var(--spacing-md); font-weight:700; color:#fff;">${l.username}</td>
            <td style="padding: var(--spacing-md); color:var(--text-muted); font-family:monospace; font-size:0.75rem;">${l.ipAddress}</td>
            <td style="padding: var(--spacing-md); color:#fff; font-size:0.78rem;">${l.location || 'Local Loopback'}</td>
            <td style="padding: var(--spacing-md);">${statusBadge}</td>
            <td style="padding: var(--spacing-md); font-size:0.75rem;">${durationText}</td>
            <td style="padding: var(--spacing-md); color:var(--text-muted); font-size:0.75rem;" title="${l.userAgent || ''}">${shortUA}</td>
            <td style="padding: var(--spacing-md); color:var(--text-muted); font-size:0.75rem;">${l.loginTime ? String(l.loginTime).replace('T', ' ').substring(0, 19) : '—'}</td>
            <td style="padding: var(--spacing-md); text-align:right; color:var(--text-muted); font-size:0.75rem;">${logoutText}</td>
          </tr>
        `;
      }).join('');
    } else if (this.activeTab === 'audit') {
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
            <td style="padding: var(--spacing-md); text-align:right; color:var(--text-muted); font-size:0.75rem;">${l.createdAt ? String(l.createdAt).replace('T', ' ').substring(0, 19) : '—'}</td>
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
            <td style="padding: var(--spacing-md); text-align:right; color:var(--text-muted); font-size:0.75rem;">${l.timestamp ? String(l.timestamp).replace('T', ' ').substring(0, 19) : '—'}</td>
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
    const loginBtn = container.querySelector('#tab-login-logs');
    const auditBtn = container.querySelector('#tab-audit-logs');
    const sysBtn = container.querySelector('#tab-system-logs');
    const tbody = container.querySelector('#log-table-body');
    const thead = container.querySelector('#log-table-head');
    
    if (loginBtn && auditBtn && sysBtn) {
      loginBtn.addEventListener('click', () => {
        this.activeTab = 'logins';
        loginBtn.style.background = 'rgba(255,255,255,0.08)';
        loginBtn.style.color = '#fff';
        auditBtn.style.background = 'transparent';
        auditBtn.style.color = 'var(--text-muted)';
        sysBtn.style.background = 'transparent';
        sysBtn.style.color = 'var(--text-muted)';
        if (thead) thead.innerHTML = this.renderLogHeader();
        if (tbody) tbody.innerHTML = this.renderLogRows();
      });

      auditBtn.addEventListener('click', () => {
        this.activeTab = 'audit';
        auditBtn.style.background = 'rgba(255,255,255,0.08)';
        auditBtn.style.color = '#fff';
        loginBtn.style.background = 'transparent';
        loginBtn.style.color = 'var(--text-muted)';
        sysBtn.style.background = 'transparent';
        sysBtn.style.color = 'var(--text-muted)';
        if (thead) thead.innerHTML = this.renderLogHeader();
        if (tbody) tbody.innerHTML = this.renderLogRows();
      });

      sysBtn.addEventListener('click', () => {
        this.activeTab = 'system';
        sysBtn.style.background = 'rgba(255,255,255,0.08)';
        sysBtn.style.color = '#fff';
        loginBtn.style.background = 'transparent';
        loginBtn.style.color = 'var(--text-muted)';
        auditBtn.style.background = 'transparent';
        auditBtn.style.color = 'var(--text-muted)';
        if (thead) thead.innerHTML = this.renderLogHeader();
        if (tbody) tbody.innerHTML = this.renderLogRows();
      });
    }


  }
}



