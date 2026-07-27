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
import { authStore } from '../../../../store/authStore.js';

export default class LogsPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  constructor() {
    this.auditLogs = [];
    this.systemLogs = [];
    this.loginLogs = [];
    this.startDate = '';
    this.endDate = '';
    this.targetEmail = '';
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

            <!-- Date Range Filters & Export Actions Bar -->
            <div class="flex align-center gap-xs flex-wrap">
              <div style="display:flex; align-items:center; gap:4px; font-size:0.75rem; color:var(--text-muted);">
                <span style="font-weight:600;">User:</span>
                <input type="text" id="log-target-email" placeholder="User email..." value="${this.targetEmail || ''}" style="background:rgba(0,0,0,0.3); border:1px solid rgba(255,255,255,0.1); color:#fff; padding:4px 8px; border-radius:4px; font-size:0.75rem; width:135px;" />
              </div>
              <div style="display:flex; align-items:center; gap:4px; font-size:0.75rem; color:var(--text-muted);">
                <span style="font-weight:600;">From:</span>
                <input type="date" id="log-start-date" value="${this.startDate || ''}" style="background:rgba(0,0,0,0.3); border:1px solid rgba(255,255,255,0.1); color:#fff; padding:4px 8px; border-radius:4px; font-size:0.75rem;" />
              </div>
              <div style="display:flex; align-items:center; gap:4px; font-size:0.75rem; color:var(--text-muted);">
                <span style="font-weight:600;">To:</span>
                <input type="date" id="log-end-date" value="${this.endDate || ''}" style="background:rgba(0,0,0,0.3); border:1px solid rgba(255,255,255,0.1); color:#fff; padding:4px 8px; border-radius:4px; font-size:0.75rem;" />
              </div>
              <button id="btn-filter-logs" class="btn" type="button" style="padding:4px 12px; font-size:0.72rem; font-weight:700; background:var(--accent-primary); color:#000; border:none; border-radius:4px; cursor:pointer;">
                Pull Logs
              </button>
              <button id="btn-reset-logs" class="btn" type="button" style="padding:4px 10px; font-size:0.72rem; font-weight:600; background:rgba(255,255,255,0.08); color:var(--text-muted); border:none; border-radius:4px; cursor:pointer;">
                Reset
              </button>
              <button id="btn-export-pdf" class="btn" type="button" style="padding:4px 12px; font-size:0.72rem; font-weight:700; background:rgba(230,126,34,0.18); color:#e67e22; border:1px solid rgba(230,126,34,0.3); border-radius:4px; cursor:pointer; display:inline-flex; align-items:center; gap:4px;">
                <i data-lucide="file-text" style="width:14px; height:14px;"></i> Export PDF
              </button>
            </div>
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
      let queryStr = '';
      const params = new URLSearchParams();
      if (this.startDate) params.append('startDate', this.startDate);
      if (this.endDate) params.append('endDate', this.endDate);
      if (this.targetEmail) params.append('targetEmail', this.targetEmail);
      if (params.toString()) queryStr = '?' + params.toString();

      const [dashRes, auditRes, sysRes, loginRes] = await Promise.all([
        apiClient.get('/api/platform/dashboard').catch(() => null),
        apiClient.get('/api/platform/logs/audit').catch(() => []),
        apiClient.get('/api/platform/logs/system').catch(() => []),
        apiClient.get(`/api/platform/logs/logins${queryStr}`).catch(() => [])
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
            logoutText = `<span style="color:var(--text-muted);">${String(l.logoutTime).replace('T', ' ').substring(0, 19)}</span> <span style="font-size:0.68rem; font-weight:700; color:#e74c3c; background:rgba(231,76,60,0.12); padding:2px 6px; border-radius:3px; margin-left:4px;">Logged Out</span>`;
          } else if (isActiveNow) {
            logoutText = '<span style="color:#2ecc71; font-weight:700; background:rgba(46,204,113,0.12); padding:2px 8px; border-radius:3px; font-size:0.7rem;">Active Now</span>';
          } else if (l.lastActiveTime) {
            logoutText = `<span style="color:var(--text-muted);">${String(l.lastActiveTime).replace('T', ' ').substring(0, 19)}</span> <span style="font-size:0.68rem; font-weight:700; color:#e67e22; background:rgba(230,126,34,0.12); padding:2px 6px; border-radius:3px; margin-left:4px;">Timed Out</span>`;
          } else {
            logoutText = '<span style="font-size:0.68rem; font-weight:700; color:#e67e22; background:rgba(230,126,34,0.12); padding:2px 6px; border-radius:3px;">Timed Out</span>';
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

    // 2. Date & user filters & PDF export buttons
    const filterBtn = container.querySelector('#btn-filter-logs');
    const resetBtn = container.querySelector('#btn-reset-logs');
    const pdfBtn = container.querySelector('#btn-export-pdf');
    const startDateInput = container.querySelector('#log-start-date');
    const endDateInput = container.querySelector('#log-end-date');
    const targetEmailInput = container.querySelector('#log-target-email');

    if (filterBtn) {
      filterBtn.addEventListener('click', async () => {
        if (startDateInput) this.startDate = startDateInput.value;
        if (endDateInput) this.endDate = endDateInput.value;
        if (targetEmailInput) this.targetEmail = targetEmailInput.value;
        await this.fetchData();
        if (tbody) tbody.innerHTML = this.renderLogRows();
      });
    }

    if (resetBtn) {
      resetBtn.addEventListener('click', async () => {
        this.startDate = '';
        this.endDate = '';
        this.targetEmail = '';
        if (startDateInput) startDateInput.value = '';
        if (endDateInput) endDateInput.value = '';
        if (targetEmailInput) targetEmailInput.value = '';
        await this.fetchData();
        if (tbody) tbody.innerHTML = this.renderLogRows();
      });
    }

    if (pdfBtn) {
      pdfBtn.addEventListener('click', async () => {
        await this.exportPdf();
      });
    }
  }

  async exportPdf() {
    let me = null;
    try {
      const meRes = await apiClient.get('/api/v1/auth/me');
      if (meRes && meRes.success && meRes.data) {
        me = meRes.data;
      }
    } catch (e) {
      logger.warn('LogsPage', 'Could not fetch current user profile for PDF export:', e);
    }

    const authUser = authStore.getUser() || {};
    const exportingUserName = me?.name || (me?.firstName ? `${me.firstName} ${me.lastName || ''}`.trim() : '') || authUser.name || authUser.username || 'System User';
    const exportingEmpId = me?.employeeCode || (me?.id ? `EMP-${me.id}` : 'ADMIN-001');
    const exportingUserEmail = me?.email || me?.username || authUser.username || 'N/A';
    const exportingUserRole = me?.designation || authUser.role || 'Platform Administrator';
    const exportingDept = me?.department || 'Operations';

    const printWin = window.open('', '_blank', 'width=950,height=750');
    if (!printWin) {
      alert('Pop-up window blocked. Please allow pop-ups for this site to export PDF reports.');
      return;
    }

    const dateFilterStr = (this.startDate || this.endDate) 
      ? `${this.startDate || 'Beginning'} to ${this.endDate || 'Present'}`
      : 'All Recorded Activity';

    const rowsHtml = (this.loginLogs || []).map(l => `
      <tr style="border-bottom: 1px solid #e0e0e0;">
        <td style="padding: 8px; font-weight: 700; color: #111;">${l.username}</td>
        <td style="padding: 8px; font-family: monospace; color: #555;">${l.ipAddress}</td>
        <td style="padding: 8px;">${l.location || 'Local Loopback'}</td>
        <td style="padding: 8px;">
          <span style="padding: 2px 6px; border-radius: 3px; font-size: 10px; font-weight: bold; background: ${l.status === 'SUCCESS' ? '#e8f5e9' : '#ffebee'}; color: ${l.status === 'SUCCESS' ? '#2e7d32' : '#c62828'};">
            ${l.status}
          </span>
        </td>
        <td style="padding: 8px; color: #555;">${l.userAgent ? (l.userAgent.includes('Chrome') ? 'Chrome / Browser' : (l.userAgent.length > 25 ? l.userAgent.substring(0, 22) + '...' : l.userAgent)) : 'Unknown'}</td>
        <td style="padding: 8px; color: #444;">${l.loginTime ? String(l.loginTime).replace('T', ' ').substring(0, 19) : '—'}</td>
        <td style="padding: 8px; text-align: right; color: #444;">${l.logoutTime ? String(l.logoutTime).replace('T', ' ').substring(0, 19) + ' (Logged Out)' : (l.status === 'SUCCESS' ? 'Active / Logged In' : '—')}</td>
      </tr>
    `).join('');

    const htmlContent = `
      <!DOCTYPE html>
      <html>
      <head>
        <title>PLUS33 ERP - User Activity Logs Report</title>
        <style>
          body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; padding: 25px; color: #222; background: #fff; }
          .header { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #c9a96e; padding-bottom: 15px; margin-bottom: 20px; }
          .brand { font-size: 20px; font-weight: 800; color: #1a1a1a; letter-spacing: -0.5px; }
          .sub-brand { font-size: 11px; font-weight: 600; color: #888; letter-spacing: 1px; text-transform: uppercase; margin-top: 2px; }
          .title { font-size: 15px; font-weight: 700; color: #c9a96e; text-transform: uppercase; margin-top: 6px; }
          .meta { font-size: 12px; color: #444; margin-bottom: 20px; background: #f8f9fa; padding: 14px 18px; border-radius: 6px; border: 1px solid #e9ecef; }
          .meta-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
          .meta-item { line-height: 1.6; }
          .emp-badge { font-family: monospace; font-weight: 700; background: rgba(0,0,0,0.06); padding: 2px 6px; border-radius: 4px; color: #111; }
          table { width: 100%; border-collapse: collapse; font-size: 11px; margin-top: 10px; }
          th { background: #1a1a1a; color: #fff; text-align: left; padding: 10px 8px; font-weight: 600; text-transform: uppercase; font-size: 10px; letter-spacing: 0.5px; }
          td { padding: 8px; vertical-align: middle; }
          .footer { margin-top: 35px; border-top: 1px solid #e0e0e0; padding-top: 12px; font-size: 10px; color: #888; display: flex; justify-content: space-between; }
          @media print {
            body { padding: 0; }
          }
        </style>
      </head>
      <body>
        <div class="header">
          <div>
            <div class="brand">PLUS33 COFFEE</div>
            <div class="sub-brand">Enterprise Resource Planning</div>
            <div class="title">User Access &amp; Activity Audit Log Report</div>
          </div>
          <div style="text-align: right; font-size: 11px; color: #666;">
            <div><strong>Generated:</strong> ${new Date().toLocaleString()}</div>
            <div><strong>Exported By:</strong> ${exportingUserName}</div>
            <div style="margin-top: 2px; color: #888;">Emp ID: <span class="emp-badge">${exportingEmpId}</span></div>
          </div>
        </div>

        <div class="meta">
          <div class="meta-grid">
            <div class="meta-item">
              <div><strong>Exporting User:</strong> ${exportingUserName}</div>
              <div><strong>Employee ID (EMP ID):</strong> <span class="emp-badge">${exportingEmpId}</span></div>
              <div><strong>Role / Designation:</strong> ${exportingUserRole} (${exportingDept})</div>
            </div>
            <div class="meta-item">
              <div><strong>User Email:</strong> ${exportingUserEmail}</div>
              <div><strong>Date Range Filter:</strong> ${dateFilterStr}</div>
              <div><strong>Total Log Entries:</strong> ${(this.loginLogs || []).length}</div>
            </div>
          </div>
        </div>

        <table>
          <thead>
            <tr>
              <th>USER EMAIL</th>
              <th>IP ADDRESS</th>
              <th>LOCATION</th>
              <th>STATUS</th>
              <th>CLIENT CONTEXT</th>
              <th>LOGIN TIME</th>
              <th style="text-align: right;">LOGOUT / STATUS</th>
            </tr>
          </thead>
          <tbody>
            ${rowsHtml || '<tr><td colspan="7" style="text-align:center; padding:30px; color:#888;">No activity log entries found matching selected criteria.</td></tr>'}
          </tbody>
        </table>

        <div class="footer">
          <div>PLUS33 Coffee ERP System — Confidential Record Exported by ${exportingUserName} (${exportingEmpId})</div>
          <div>Page 1 of 1</div>
        </div>

        <script>
          window.onload = function() {
            setTimeout(function() {
              window.print();
            }, 300);
          };
        </script>
      </body>
      </html>
    `;

    printWin.document.open();
    printWin.document.write(htmlContent);
    printWin.document.close();
  }
}



