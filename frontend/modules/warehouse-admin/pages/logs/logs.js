/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : logs.js
 * Path              : frontend/modules/warehouse-admin/pages/logs/logs.js
 * Purpose           : Frontend page component for Platform Operations & Logs Auditor.
 *                     Displays logged-in user data by default, allows admins to pull
 *                     and inspect specific user data, and supports exporting selected user PDF reports.
 * Version           : 1.0.0
 ******************************************************************************/

import { apiClient } from '../../../../api/client.js';
import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { authStore } from '../../../../store/authStore.js';

export default class LogsPage {
  constructor() {
    this.auditLogs = [];
    this.systemLogs = [];
    this.loginLogs = [];
    this.targetUsers = [];
    this.currentUser = null;
    this.selectedUserData = null;
    this.targetEmail = '';
    this.startDate = '';
    this.endDate = '';
    this.dashboardStats = {
      totalCacheNodes: 4,
      totalPods: 12,
      totalRegions: 6,
      totalBreakers: 2,
      status: "HEALTHY"
    };
    this.activeTab = 'logins';
    this.isAdmin = false;
  }

  async mount(container, lifecycle) {
    logger.info('LogsPage', 'Mounting Platform Operations & Logs Auditor...');

    // 1. Fetch current logged-in user profile
    await this.fetchCurrentUserProfile();

    // 2. Fetch admin target users if applicable
    await this.checkAdminAndLoadTargetUsers();

    // 3. Render HTML Layout
    container.innerHTML = this.getHtmlTemplate();

    // 4. Populate Admin Dropdown if available
    this.renderUserSelectorOptions(container);

    // 5. Fetch initial data (logged-in user data first)
    await this.fetchData();

    // 6. Bind events & Lucide icons
    if (window.lucide) window.lucide.createIcons();
    this.bindEvents(container, lifecycle);
  }

  async fetchCurrentUserProfile() {
    try {
      const res = await apiClient.get('/api/v1/auth/me');
      if (res && res.success && res.data) {
        this.currentUser = res.data;
      }
    } catch (e) {
      logger.warn('LogsPage', 'Could not fetch /api/v1/auth/me profile:', e);
    }

    const authUser = authStore.getUser() || {};
    const defaultEmail = this.currentUser?.email || this.currentUser?.username || authUser.username || '';
    if (!this.targetEmail) {
      this.targetEmail = defaultEmail;
    }
  }

  async checkAdminAndLoadTargetUsers() {
    this.isAdmin = true;

    try {
      const res = await apiClient.get('/api/v1/activity-logs/users');
      if (res && res.success && Array.isArray(res.data) && res.data.length > 0) {
        this.targetUsers = res.data;
      }
    } catch (e) {
      logger.warn('LogsPage', 'Could not fetch target users list from /api/v1/activity-logs/users:', e);
    }
  }

  getHtmlTemplate() {
    return `
      <div class="logs-page-container">
        <!-- Page Header -->
        <div class="dashboard-header flex justify-between align-center mb-lg" style="flex-wrap: wrap; gap: var(--spacing-md);">
          <div>
            <h2 class="m-0 page-title" style="font-family: var(--font-display); font-weight: 800; font-size: 1.65rem; letter-spacing: -0.02em;">
              Platform Operations &amp; Logs Auditor
            </h2>
            <p class="m-0 page-subtitle" style="color: var(--text-muted); font-size: 0.82rem; margin-top: 2px;">
              Monitor Kubernetes pod pools, distributed cache status, and double-entry platform audit transactions
            </p>
          </div>
          <div style="display:flex; align-items:center; gap: var(--spacing-md);">
            <div style="display:flex; align-items:center; gap:6px; background: rgba(130,163,125,0.12); border: 1px solid rgba(130,163,125,0.3); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.75rem; font-weight: 600; color: var(--status-success);">
              <span style="width:7px; height:7px; border-radius:50%; background: var(--status-success); display:inline-block; animation: pulse-dot 2s infinite;"></span>
              AIOps Stable
            </div>
            <button id="btn-header-pull-data" class="btn" type="button" style="padding:6px 14px; font-size:0.78rem; font-weight:700; background:linear-gradient(135deg, #c9a96e 0%, #a88344 100%); color:#000; border:none; border-radius:var(--radius-md); cursor:pointer; display:inline-flex; align-items:center; gap:6px; box-shadow:0 2px 8px rgba(201,169,110,0.25);">
              <i data-lucide="refresh-cw" style="width:14px; height:14px;"></i> Pull Data
            </button>
          </div>
        </div>

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

            <!-- Date Range & User Selector Filters Bar -->
            <div class="flex align-center gap-xs flex-wrap">
              <div id="admin-user-selector-container" style="display:${this.isAdmin ? 'flex' : 'none'}; align-items:center; gap:4px; font-size:0.75rem; color:var(--text-muted);">
                <span style="font-weight:600;">Select User:</span>
                <select id="select-log-user" style="background:rgba(0,0,0,0.4); border:1px solid rgba(255,255,255,0.15); color:#fff; padding:4px 8px; border-radius:4px; font-size:0.75rem; max-width:210px; cursor:pointer;">
                  <!-- Dynamically populated -->
                </select>
              </div>

              <div style="display:flex; align-items:center; gap:4px; font-size:0.75rem; color:var(--text-muted);">
                <span style="font-weight:600;">From:</span>
                <input type="date" id="log-start-date" value="${this.startDate || ''}" style="background:rgba(0,0,0,0.3); border:1px solid rgba(255,255,255,0.1); color:#fff; padding:4px 8px; border-radius:4px; font-size:0.75rem;" />
              </div>
              <div style="display:flex; align-items:center; gap:4px; font-size:0.75rem; color:var(--text-muted);">
                <span style="font-weight:600;">To:</span>
                <input type="date" id="log-end-date" value="${this.endDate || ''}" style="background:rgba(0,0,0,0.3); border:1px solid rgba(255,255,255,0.1); color:#fff; padding:4px 8px; border-radius:4px; font-size:0.75rem;" />
              </div>
              <button id="btn-pull-user-data" class="btn" type="button" style="padding:4px 12px; font-size:0.72rem; font-weight:700; background:var(--accent-primary); color:#000; border:none; border-radius:4px; cursor:pointer; display:inline-flex; align-items:center; gap:4px;">
                <i data-lucide="download-cloud" style="width:12px; height:12px;"></i> Pull User Data
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
  }

  renderUserSelectorOptions(container) {
    const selectEl = container.querySelector('#select-log-user');
    if (!selectEl) return;

    let html = '';
    const myEmail = this.currentUser?.email || authStore.getUser()?.username || '';

    // Option 1: Bulk Pull All Managed Personnel
    html += `<option value="ALL_UNDER_ME">⚡ All Users & Employees Under Me (Bulk Pull)</option>`;

    // Option 2: Logged-in User's own activity data
    html += `<option value="${myEmail}">My Own Activity Logs (${myEmail})</option>`;

    // Option 3..N: Specific Individual Users & Employees Under Admin
    if (this.targetUsers && this.targetUsers.length > 0) {
      for (const u of this.targetUsers) {
        if (u.email !== myEmail) {
          html += `<option value="${u.email}">${u.name} — ${u.employeeCode} (${u.email})</option>`;
        }
      }
    }
    selectEl.innerHTML = html;
    selectEl.value = this.targetEmail || 'ALL_UNDER_ME';
  }

  async fetchData() {
    try {
      const params = new URLSearchParams();
      if (this.startDate) params.append('startDate', this.startDate);
      if (this.endDate) params.append('endDate', this.endDate);
      if (this.targetEmail) params.append('targetEmail', this.targetEmail);
      const queryStr = params.toString() ? '?' + params.toString() : '';

      // Primary API: /api/v1/activity-logs/search
      const [dashRes, auditRes, sysRes, searchRes] = await Promise.all([
        apiClient.get('/api/platform/dashboard').catch(() => null),
        apiClient.get('/api/platform/logs/audit').catch(() => []),
        apiClient.get('/api/platform/logs/system').catch(() => []),
        apiClient.get(`/api/v1/activity-logs/search${queryStr}`).catch(() => null)
      ]);

      if (dashRes) this.dashboardStats = dashRes;
      if (auditRes) this.auditLogs = auditRes;
      if (sysRes) this.systemLogs = sysRes;

      if (searchRes && searchRes.success && Array.isArray(searchRes.data)) {
        this.loginLogs = searchRes.data;
      } else {
        // Fallback to /api/platform/logs/logins
        const fallbackRes = await apiClient.get(`/api/platform/logs/logins${queryStr}`).catch(() => []);
        this.loginLogs = fallbackRes || [];
      }
    } catch (err) {
      logger.error('LogsPage', 'Failed to fetch user activity logs:', err);
      this.loginLogs = [];
    }
  }

  renderLogHeader() {
    if (this.activeTab === 'logins') {
      return `
        <tr style="border-bottom: 1px solid rgba(255,255,255,0.08); color: var(--text-muted); font-size: 0.72rem; text-transform: uppercase; font-weight:700;">
          <th style="padding: var(--spacing-sm) var(--spacing-md);">User Email</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">IP Address</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Resolved Location</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Status</th>
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
          <th style="padding: var(--spacing-sm) var(--spacing-md); color: var(--accent-primary);">Operator</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Trace Context</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md); color: var(--accent-primary);">Change Details</th>
        </tr>
      `;
    } else {
      return `
        <tr style="border-bottom: 1px solid rgba(255,255,255,0.08); color: var(--text-muted); font-size: 0.72rem; text-transform: uppercase; font-weight:700;">
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Pod / Node ID</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Subsystem Component</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Severity Level</th>
          <th style="padding: var(--spacing-sm) var(--spacing-md);">Diagnostic Log Payload</th>
        </tr>
      `;
    }
  }

  renderLogRows() {
    if (this.activeTab === 'logins') {
      if (!this.loginLogs || this.loginLogs.length === 0) {
        return `<tr><td colspan="7" style="text-align:center; padding: 40px; color: var(--text-muted);">No login activity logs found for selected user criteria.</td></tr>`;
      }
      return this.loginLogs.map(l => `
        <tr style="border-bottom: 1px solid rgba(255,255,255,0.04);">
          <td style="padding: var(--spacing-sm) var(--spacing-md); font-weight:700;">${l.username}</td>
          <td style="padding: var(--spacing-sm) var(--spacing-md); font-family: monospace; color: var(--text-muted);">${l.ipAddress}</td>
          <td style="padding: var(--spacing-sm) var(--spacing-md);">${l.location || 'Local Loopback'}</td>
          <td style="padding: var(--spacing-sm) var(--spacing-md);">
            <span style="padding: 2px 8px; border-radius: 4px; font-size: 0.7rem; font-weight: 700; background: ${l.status === 'SUCCESS' ? 'rgba(46,125,50,0.2)' : 'rgba(198,40,40,0.2)'}; color: ${l.status === 'SUCCESS' ? '#81c784' : '#e57373'}; border: 1px solid ${l.status === 'SUCCESS' ? 'rgba(46,125,50,0.4)' : 'rgba(198,40,40,0.4)'};">
              ${l.status}
            </span>
          </td>
          <td style="padding: var(--spacing-sm) var(--spacing-md); color: var(--text-muted);">${l.userAgent ? (l.userAgent.includes('Chrome') ? 'Chrome Browser' : (l.userAgent.length > 25 ? l.userAgent.substring(0, 22) + '...' : l.userAgent)) : 'Unknown'}</td>
          <td style="padding: var(--spacing-sm) var(--spacing-md);">${l.loginTime ? String(l.loginTime).replace('T', ' ').substring(0, 19) : '—'}</td>
          <td style="padding: var(--spacing-sm) var(--spacing-md); text-align:right;">${l.logoutTime ? String(l.logoutTime).replace('T', ' ').substring(0, 19) : (l.status === 'SUCCESS' ? 'Active / Logged In' : '—')}</td>
        </tr>
      `).join('');
    } else if (this.activeTab === 'audit') {
      if (!this.auditLogs || this.auditLogs.length === 0) {
        return `<tr><td colspan="5" style="text-align:center; padding: 40px; color: var(--text-muted);">No audit log transactions found.</td></tr>`;
      }
      return this.auditLogs.map(a => `
        <tr style="border-bottom: 1px solid rgba(255,255,255,0.04);">
          <td style="padding: var(--spacing-sm) var(--spacing-md); font-family: monospace; color: var(--text-muted);">#AUD-${a.id}</td>
          <td style="padding: var(--spacing-sm) var(--spacing-md); font-weight:700;">${a.action}</td>
          <td style="padding: var(--spacing-sm) var(--spacing-md); color: var(--accent-primary); font-weight:600;">${a.operator}</td>
          <td style="padding: var(--spacing-sm) var(--spacing-md); font-family: monospace; font-size:0.75rem;">${a.traceId || 'REST-API'}</td>
          <td style="padding: var(--spacing-sm) var(--spacing-md); color: var(--text-muted);">${a.details}</td>
        </tr>
      `).join('');
    } else {
      if (!this.systemLogs || this.systemLogs.length === 0) {
        return `<tr><td colspan="4" style="text-align:center; padding: 40px; color: var(--text-muted);">No system diagnostic logs found.</td></tr>`;
      }
      return this.systemLogs.map(s => `
        <tr style="border-bottom: 1px solid rgba(255,255,255,0.04);">
          <td style="padding: var(--spacing-sm) var(--spacing-md); font-family: monospace;">${s.podId}</td>
          <td style="padding: var(--spacing-sm) var(--spacing-md); font-weight:700;">${s.component}</td>
          <td style="padding: var(--spacing-sm) var(--spacing-md);">
            <span style="padding:2px 8px; border-radius:4px; font-size:0.7rem; font-weight:700; background:rgba(201,169,110,0.15); color:var(--accent-primary);">${s.level}</span>
          </td>
          <td style="padding: var(--spacing-sm) var(--spacing-md); font-family: monospace; font-size:0.75rem; color:var(--text-muted);">${s.message}</td>
        </tr>
      `).join('');
    }
  }

  bindEvents(container, lifecycle) {
    const loginBtn = container.querySelector('#tab-login-logs');
    const auditBtn = container.querySelector('#tab-audit-logs');
    const sysBtn = container.querySelector('#tab-system-logs');
    const thead = container.querySelector('#log-table-head');
    const tbody = container.querySelector('#log-table-body');

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

    const pullUserDataBtn = container.querySelector('#btn-pull-user-data');
    const resetBtn = container.querySelector('#btn-reset-logs');
    const pdfBtn = container.querySelector('#btn-export-pdf');
    const headerPullBtn = container.querySelector('#btn-header-pull-data');
    const startDateInput = container.querySelector('#log-start-date');
    const endDateInput = container.querySelector('#log-end-date');
    const selectLogUser = container.querySelector('#select-log-user');

    if (selectLogUser) {
      selectLogUser.addEventListener('change', () => {
        this.targetEmail = selectLogUser.value;
      });
    }

    if (pullUserDataBtn) {
      pullUserDataBtn.addEventListener('click', async () => {
        if (selectLogUser) this.targetEmail = selectLogUser.value;
        if (startDateInput) this.startDate = startDateInput.value;
        if (endDateInput) this.endDate = endDateInput.value;
        notificationStore.info(`Pulling activity logs for ${this.targetEmail || 'user'}...`);
        await this.fetchData();
        if (tbody) tbody.innerHTML = this.renderLogRows();
        notificationStore.success('User data pulled successfully!');
      });
    }

    if (headerPullBtn) {
      headerPullBtn.addEventListener('click', async () => {
        if (selectLogUser) this.targetEmail = selectLogUser.value;
        if (startDateInput) this.startDate = startDateInput.value;
        if (endDateInput) this.endDate = endDateInput.value;
        notificationStore.info('Refreshing platform activity data...');
        await this.fetchData();
        if (tbody) tbody.innerHTML = this.renderLogRows();
        notificationStore.success('Data refreshed successfully!');
      });
    }

    if (resetBtn) {
      resetBtn.addEventListener('click', async () => {
        const myEmail = this.currentUser?.email || authStore.getUser()?.username || '';
        this.startDate = '';
        this.endDate = '';
        this.targetEmail = myEmail;
        if (startDateInput) startDateInput.value = '';
        if (endDateInput) endDateInput.value = '';
        if (selectLogUser) selectLogUser.value = myEmail;
        await this.fetchData();
        if (tbody) tbody.innerHTML = this.renderLogRows();
        notificationStore.info('Reset logs view to logged-in user data.');
      });
    }

    if (pdfBtn) {
      pdfBtn.addEventListener('click', async () => {
        await this.exportPdf();
      });
    }
  }

  async exportPdf() {
    const isBulkMode = this.targetEmail === 'ALL_UNDER_ME';
    let targetUserInfo = null;
    const authUser = authStore.getUser() || {};
    const adminName = this.currentUser?.name || (this.currentUser?.firstName ? `${this.currentUser.firstName} ${this.currentUser.lastName || ''}`.trim() : '') || authUser.name || authUser.username || 'System Administrator';
    const adminEmpId = this.currentUser?.employeeCode || (this.currentUser?.id ? `EMP-${this.currentUser.id}` : 'ADMIN-001');

    if (!isBulkMode) {
      const myEmail = this.currentUser?.email || authUser.username || '';
      if (this.targetEmail && this.targetEmail !== myEmail && this.targetUsers) {
        const match = this.targetUsers.find(u => u.email === this.targetEmail);
        if (match) {
          targetUserInfo = {
            name: match.name,
            employeeCode: match.employeeCode,
            email: match.email,
            designation: match.designation || 'Employee',
            department: 'Operations'
          };
        }
      }

      if (!targetUserInfo) {
        targetUserInfo = {
          name: adminName,
          employeeCode: adminEmpId,
          email: this.currentUser?.email || this.currentUser?.username || authUser.username || 'N/A',
          designation: this.currentUser?.designation || authUser.role || 'Platform Administrator',
          department: this.currentUser?.department || 'Operations'
        };
      }
    }

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
        <td style="padding: 8px; color: #555;">${l.userAgent ? (l.userAgent.includes('Chrome') ? 'Chrome Browser' : (l.userAgent.length > 25 ? l.userAgent.substring(0, 22) + '...' : l.userAgent)) : 'Unknown'}</td>
        <td style="padding: 8px; color: #444;">${l.loginTime ? String(l.loginTime).replace('T', ' ').substring(0, 19) : '—'}</td>
        <td style="padding: 8px; text-align: right; color: #444;">${l.logoutTime ? String(l.logoutTime).replace('T', ' ').substring(0, 19) : (l.status === 'SUCCESS' ? 'Active / Logged In' : '—')}</td>
      </tr>
    `).join('');

    const titleText = isBulkMode 
      ? 'All Managed Personnel Activity Audit Log Report' 
      : `User Access & Activity Audit Log Report — ${targetUserInfo.name}`;

    const metaBlockHtml = isBulkMode ? `
      <div class="meta-grid">
        <div class="meta-item">
          <div><strong>Managing Administrator:</strong> ${adminName}</div>
          <div><strong>Admin Employee ID:</strong> <span class="emp-badge">${adminEmpId}</span></div>
          <div><strong>Audit Scope:</strong> All Managed Users &amp; Employees Under Hierarchy</div>
        </div>
        <div class="meta-item">
          <div><strong>Date Range Filter:</strong> ${dateFilterStr}</div>
          <div><strong>Total Log Entries Pulled:</strong> ${(this.loginLogs || []).length}</div>
          <div><strong>Total Managed Users:</strong> ${this.targetUsers ? this.targetUsers.length : 1}</div>
        </div>
      </div>
    ` : `
      <div class="meta-grid">
        <div class="meta-item">
          <div><strong>Selected User Name:</strong> ${targetUserInfo.name}</div>
          <div><strong>Employee ID (EMP ID):</strong> <span class="emp-badge">${targetUserInfo.employeeCode}</span></div>
          <div><strong>Role / Designation:</strong> ${targetUserInfo.designation} (${targetUserInfo.department})</div>
        </div>
        <div class="meta-item">
          <div><strong>User Email:</strong> ${targetUserInfo.email}</div>
          <div><strong>Date Range Filter:</strong> ${dateFilterStr}</div>
          <div><strong>Total Log Entries:</strong> ${(this.loginLogs || []).length}</div>
        </div>
      </div>
    `;

    const htmlContent = `
      <!DOCTYPE html>
      <html>
      <head>
        <title>PLUS33 ERP - User Activity Audit Report</title>
        <style>
          body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; padding: 25px; color: #222; background: #fff; }
          .header { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #c9a96e; padding-bottom: 15px; margin-bottom: 20px; }
          .brand { font-size: 20px; font-weight: 800; color: #1a1a1a; letter-spacing: -0.5px; }
          .sub-brand { font-size: 11px; font-weight: 600; color: #888; letter-spacing: 1px; text-transform: uppercase; margin-top: 2px; }
          .title { font-size: 14px; font-weight: 700; color: #c9a96e; text-transform: uppercase; margin-top: 6px; }
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
            <div class="title">${titleText}</div>
          </div>
          <div style="text-align: right; font-size: 11px; color: #666;">
            <div><strong>Generated:</strong> ${new Date().toLocaleString()}</div>
            <div><strong>Auditor/Admin:</strong> ${adminName}</div>
            <div style="margin-top: 2px; color: #888;">Emp ID: <span class="emp-badge">${adminEmpId}</span></div>
          </div>
        </div>

        <div class="meta">
          ${metaBlockHtml}
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
          <div>PLUS33 Coffee ERP System — Confidential Administrative Record (${adminName})</div>
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
