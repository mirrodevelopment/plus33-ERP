/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : leave.js
 * Purpose           : Enterprise leave management — DB-backed balances, 10 leave
 *                     types, working day calculator, calendar, document upload
 * Version           : 2.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';
import { apiClient } from '../../../api/client.js';

const API = '/api/v1';

export default class StoreEmployeeLeave {
  constructor() {
    this.user = authStore.getUser();
    this.leaveTypes = [];
    this.balances = [];
    this.myLeaves = [];
    this.holidays = [];
    this.blackouts = [];
    this.calendarDate = new Date();
  }

  async mount(container, lifecycle) {
    logger.info('StoreEmployeeLeave', 'Mounting enterprise leave page...');
    this.render(container, true);
    await this.loadData();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  async loadData() {
    try {
      const safeGet = async (url) => {
        try { return await apiClient.get(url); } catch (e) { return null; }
      };
      
      const profileRes = await safeGet('/auth/me');
      const country = (profileRes && profileRes.success && profileRes.data) ? profileRes.data.country || 'France' : 'France';
      this.countryName = country;
      
      let countryCode = 'FR';
      if (country === 'India') countryCode = 'IN';
      else if (country === 'UAE') countryCode = 'AE';
      
      this.countryLawLabel = country === 'India' ? 'Indian Labour Law Compliant' :
                             country === 'UAE' ? 'UAE Labour Law Compliant' :
                             'French Labour Law Compliant';

      const [typesRes, balancesRes, leavesRes, holidaysRes, blackoutsRes] = await Promise.all([
        safeGet('/leaves/types'),
        safeGet('/leaves/my/balance'),
        safeGet('/leaves/my'),
        safeGet(`/leaves/holidays?countryCode=${countryCode}&year=${new Date().getFullYear()}`),
        safeGet('/leaves/blackout')
      ]);
      this.leaveTypes = (typesRes && typesRes.success) ? typesRes.data || [] : [];
      this.balances = (balancesRes && balancesRes.success) ? balancesRes.data || [] : [];
      this.myLeaves = (leavesRes && leavesRes.success) ? leavesRes.data || [] : [];
      this.holidays = (holidaysRes && holidaysRes.success) ? holidaysRes.data || [] : [];
      this.blackouts = (blackoutsRes && blackoutsRes.success) ? blackoutsRes.data || [] : [];
    } catch (err) {
      logger.error('StoreEmployeeLeave', 'Failed to load leave data', err);
    }
  }

  getBalance(code) {
    const b = this.balances.find(b => b.leaveTypeCode === code);
    return b ? b : { remaining: 0, used: 0, pending: 0, accrued: 0 };
  }

  statusColor(status) {
    return status === 'APPROVED' ? 'var(--status-success)' :
      status === 'PENDING' ? 'var(--status-warning)' :
        status === 'REJECTED' ? 'var(--status-danger)' :
          'var(--text-muted)';
  }

  sessionLabel(s) {
    return s === 'HALF_DAY_AM' ? '½ Morning' : s === 'HALF_DAY_PM' ? '½ Afternoon' : 'Full Day';
  }

  render(container, loading = false) {
    if (loading) {
      container.innerHTML = `
        <div style="display:flex;align-items:center;justify-content:center;height:400px;flex-direction:column;gap:12px;">
          <i data-lucide="loader-2" style="width:32px;height:32px;color:var(--accent-primary);animation:spin 1s linear infinite;"></i>
          <span style="color:var(--text-muted);font-size:0.8rem;font-weight:600;">Loading leave data...</span>
        </div>`;
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    const typeOptions = this.leaveTypes.map(lt =>
      `<option value="${lt.id}" data-code="${lt.code}" data-protected="${lt.protected}" data-doc="${lt.requiresDocument}" data-notice="${lt.minNoticeDays || 0}">
        ${lt.name}${lt.protected ? ' 🛡️' : ''}${!lt.paid ? ' (Unpaid)' : ''}
      </option>`
    ).join('');

    const balanceCards = [
      { code: 'ANNUAL', label: 'Annual Leave', icon: 'plane', color: 'var(--accent-primary)' },
      { code: 'SICK', label: 'Sick Leave', icon: 'shield-alert', color: 'var(--status-danger)' },
      { code: 'PERSONAL', label: 'Personal', icon: 'user', color: 'var(--status-info)' },
      { code: 'CASUAL', label: 'Casual', icon: 'umbrella', color: '#a78bfa' },
      { code: 'EMERGENCY', label: 'Emergency', icon: 'alert-triangle', color: 'var(--status-warning)' },
      { code: 'MARRIAGE', label: 'Marriage', icon: 'heart', color: '#f472b6' },
      { code: 'BEREAVEMENT', label: 'Bereavement', icon: 'flower', color: '#94a3b8' },
      { code: 'MATERNITY', label: 'Maternity', icon: 'baby', color: '#34d399' },
      { code: 'PATERNITY', label: 'Paternity', icon: 'users', color: '#60a5fa' },
      { code: 'UNPAID', label: 'Unpaid', icon: 'banknote', color: 'var(--text-muted)' },
    ].map(({ code, label, icon, color }) => {
      const b = this.getBalance(code);
      const remaining = typeof b.remaining === 'number' ? b.remaining : '—';
      return `
        <div class="card glass" style="padding:var(--spacing-md);border-radius:var(--radius-md);border:1px solid var(--border-color);background:var(--bg-card);min-height:90px;display:flex;flex-direction:column;justify-content:space-between;">
          <div style="display:flex;justify-content:space-between;align-items:center;">
            <span style="font-size:0.6rem;font-weight:700;text-transform:uppercase;letter-spacing:.5px;color:var(--text-muted);">${label}</span>
            <i data-lucide="${icon}" style="width:14px;height:14px;color:${color};"></i>
          </div>
          <div>
            <div style="font-size:1.1rem;font-weight:800;color:var(--text-primary);">${remaining} <span style="font-size:0.65rem;font-weight:600;color:var(--text-muted);">days</span></div>
            <div style="display:flex;gap:8px;margin-top:2px;">
              <span style="font-size:0.55rem;color:var(--status-warning);">⏳ ${b.pending || 0} pending</span>
              <span style="font-size:0.55rem;color:${color};">✓ ${b.used || 0} used</span>
            </div>
          </div>
        </div>`;
    }).join('');

    const leaveRows = this.myLeaves.length === 0
      ? `<tr><td colspan="8" style="text-align:center;padding:var(--spacing-lg);color:var(--text-muted);font-size:0.75rem;">No leave requests yet.</td></tr>`
      : this.myLeaves.map(l => `
          <tr style="border-bottom:1px solid rgba(255,255,255,0.04);">
            <td style="padding:8px 6px;font-weight:600;font-size:0.74rem;">${l.leaveType}</td>
            <td style="padding:8px 6px;font-size:0.7rem;color:var(--text-muted);">${this.sessionLabel(l.session)}</td>
            <td style="padding:8px 6px;font-size:0.7rem;font-weight:700;">${l.totalDays}d</td>
            <td style="padding:8px 6px;font-size:0.68rem;color:var(--text-muted);">${l.startDate}</td>
            <td style="padding:8px 6px;font-size:0.68rem;color:var(--text-muted);">${l.endDate}</td>
            <td style="padding:8px 6px;font-size:0.68rem;max-width:120px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${l.reason || ''}">${l.reason || '—'}</td>
            <td style="padding:8px 6px;">
              <span style="font-size:0.62rem;font-weight:800;color:${this.statusColor(l.status)};background:${this.statusColor(l.status)}20;padding:2px 8px;border-radius:99px;">${l.status}</span>
            </td>
            <td style="padding:8px 6px;">
              ${l.status === 'PENDING' ? `<button class="btn-cancel-leave" data-id="${l.id}" style="font-size:0.6rem;font-weight:700;color:var(--status-danger);background:none;border:1px solid var(--status-danger);padding:2px 8px;border-radius:4px;cursor:pointer;">Cancel</button>` : ''}
              ${l.status === 'APPROVED' && !l.cancellationRequested ? `<button class="btn-req-cancel-leave" data-id="${l.id}" style="font-size:0.6rem;font-weight:700;color:var(--status-warning);background:none;border:1px solid var(--status-warning);padding:2px 8px;border-radius:4px;cursor:pointer;">Req. Cancel</button>` : ''}
              ${l.status === 'APPROVED' && l.cancellationRequested ? `<span style="font-size:0.6rem;color:var(--text-muted);font-style:italic;">Cancel Pending</span>` : ''}
              ${l.requiresDocument && l.status === 'PENDING' && !l.hasDocument ? `<button class="btn-upload-doc" data-id="${l.id}" style="margin-top:2px;font-size:0.6rem;font-weight:700;color:var(--accent-primary);background:none;border:1px solid var(--accent-primary);padding:2px 8px;border-radius:4px;cursor:pointer;">Upload Doc</button>` : ''}
            </td>
          </tr>`
      ).join('');

    const holidayList = this.holidays.slice(0, 5).map(h =>
      `<div style="display:flex;justify-content:space-between;padding:4px 0;border-bottom:1px solid rgba(255,255,255,0.04);">
        <span style="font-size:0.68rem;color:var(--text-primary);">🇫🇷 ${h.name}</span>
        <span style="font-size:0.65rem;color:var(--text-muted);">${h.date}</span>
      </div>`
    ).join('');

    const blackoutAlert = this.blackouts.length > 0 ? `
      <div style="background:rgba(239,68,68,0.08);border:1px solid rgba(239,68,68,0.3);border-radius:var(--radius-md);padding:var(--spacing-sm) var(--spacing-md);margin-bottom:var(--spacing-md);font-size:0.72rem;color:var(--status-danger);">
        <strong>⛔ Active Blackout Period:</strong>
        ${this.blackouts.map(b => `${b.name} (${b.startDate} – ${b.endDate})`).join(', ')}
      </div>` : '';

    container.innerHTML = `
      <div class="workspace-container animate-fade-in" style="padding:var(--spacing-lg);display:flex;flex-direction:column;gap:var(--spacing-lg);max-width:1400px;margin:0 auto;">

        <!--Header-->
      <div class="card glass" style="padding:var(--spacing-md) var(--spacing-lg);border-radius:var(--radius-lg);background:var(--bg-card);border:1px solid var(--border-color);display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:var(--spacing-md);">
        <div>
          <h2 style="font-family:var(--font-display);font-weight:800;font-size:1.5rem;color:var(--text-primary);margin:0;">Leave &amp; Time-Off</h2>
          <p style="font-size:0.7rem;color:var(--text-muted);margin:2px 0 0 0;text-transform:uppercase;font-weight:600;letter-spacing:.5px;">
            Enterprise Leave Management &nbsp;·&nbsp; <span style="color:var(--accent-primary);">${this.countryLawLabel || 'French Labour Law Compliant'}</span>
          </p>
        </div>
        <div style="display:flex;align-items:center;gap:8px;">
          <span style="background:rgba(130,163,125,0.12);border:1px solid rgba(130,163,125,0.3);border-radius:var(--radius-full);padding:4px 12px;font-size:0.7rem;font-weight:700;color:var(--status-success);">
            ● API Connected
          </span>
        </div>
      </div>

        ${ blackoutAlert }

        <!--10 Balance Cards-->
        <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(140px,1fr));gap:var(--spacing-sm);">
          ${balanceCards}
        </div>

        <!--Main Grid-->
      <div style="display:grid;grid-template-columns:1.6fr 1fr;gap:var(--spacing-lg);width:100%;">

        <!-- Left: History Table -->
        <div class="card glass" style="padding:var(--spacing-lg);border-radius:var(--radius-lg);border:1px solid var(--border-color);background:var(--bg-card);display:flex;flex-direction:column;gap:var(--spacing-md);">
          <div style="display:flex;justify-content:space-between;align-items:center;border-bottom:1px solid rgba(255,255,255,0.05);padding-bottom:var(--spacing-xs);">
            <h3 style="font-family:var(--font-display);font-weight:800;font-size:1rem;margin:0;color:var(--accent-primary);">Leave Request History</h3>
            <i data-lucide="history" style="width:16px;height:16px;color:var(--accent-primary);"></i>
          </div>
          <div style="overflow-x:auto;">
            <table style="width:100%;border-collapse:collapse;font-size:0.73rem;text-align:left;">
              <thead>
                <tr style="border-bottom:1.5px solid var(--border-color);color:var(--text-muted);text-transform:uppercase;font-weight:700;font-size:0.6rem;">
                  <th style="padding:8px 6px;">Type</th>
                  <th style="padding:8px 6px;">Session</th>
                  <th style="padding:8px 6px;">Days</th>
                  <th style="padding:8px 6px;">Start</th>
                  <th style="padding:8px 6px;">End</th>
                  <th style="padding:8px 6px;">Reason</th>
                  <th style="padding:8px 6px;">Status</th>
                  <th style="padding:8px 6px;">Action</th>
                </tr>
              </thead>
              <tbody>${leaveRows}</tbody>
            </table>
          </div>
        </div>

        <!-- Right: Apply + Holidays -->
        <div style="display:flex;flex-direction:column;gap:var(--spacing-lg);">

          <!-- Apply Form -->
          <div class="card glass" style="padding:var(--spacing-lg);border-radius:var(--radius-lg);border:1px solid var(--border-color);background:var(--bg-card);display:flex;flex-direction:column;gap:var(--spacing-md);text-align:left;">
            <div style="display:flex;justify-content:space-between;align-items:center;border-bottom:1px solid rgba(255,255,255,0.05);padding-bottom:var(--spacing-xs);">
              <h3 style="font-family:var(--font-display);font-weight:800;font-size:1.1rem;margin:0;color:var(--text-primary);">Apply for Time Off</h3>
              <i data-lucide="plane" style="width:18px;height:18px;color:var(--text-primary);"></i>
            </div>

            <div style="display:flex;flex-direction:column;gap:var(--spacing-sm);font-size:0.76rem;">
              <div style="display:flex;flex-direction:column;gap:4px;">
                <label style="font-weight:700;color:var(--text-muted);text-transform:uppercase;font-size:0.6rem;letter-spacing:0.5px;">Leave Category</label>
                <select id="leave-select-type" style="width:100%;background:rgba(0,0,0,0.3);border:1px solid var(--border-color);border-radius:var(--radius-md);color:var(--text-primary);padding:var(--spacing-sm);outline:none;cursor:pointer;">
                  <option value="">— Select Category —</option>
                  ${typeOptions}
                </select>
              </div>

              <div style="display:grid;grid-template-columns:1fr 1fr;gap:var(--spacing-sm);width:100%;">
                <div style="display:flex;flex-direction:column;gap:4px;">
                  <label style="font-weight:700;color:var(--text-muted);text-transform:uppercase;font-size:0.6rem;letter-spacing:0.5px;">Start Date</label>
                  <input type="date" id="leave-input-start" style="width:100%;background:rgba(0,0,0,0.3);border:1px solid var(--border-color);border-radius:var(--radius-md);color:var(--text-primary);padding:var(--spacing-sm);outline:none;color-scheme:dark;">
                </div>
                <div style="display:flex;flex-direction:column;gap:4px;">
                  <label style="font-weight:700;color:var(--text-muted);text-transform:uppercase;font-size:0.6rem;letter-spacing:0.5px;">End Date</label>
                  <input type="date" id="leave-input-end" style="width:100%;background:rgba(0,0,0,0.3);border:1px solid var(--border-color);border-radius:var(--radius-md);color:var(--text-primary);padding:var(--spacing-sm);outline:none;color-scheme:dark;">
                </div>
              </div>

              <div style="display:flex;flex-direction:column;gap:4px;">
                <label style="font-weight:700;color:var(--text-muted);text-transform:uppercase;font-size:0.6rem;letter-spacing:0.5px;">Reason Justification</label>
                <textarea id="leave-input-reason" placeholder="Explain your time-off request..." rows="3" style="width:100%;background:rgba(0,0,0,0.3);border:1px solid var(--border-color);border-radius:var(--radius-md);color:var(--text-primary);padding:var(--spacing-sm);outline:none;font-family:inherit;resize:none;"></textarea>
              </div>

              <div id="working-days-indicator" style="display:none;background:rgba(130,163,125,0.08);border:1px solid rgba(130,163,125,0.2);border-radius:var(--radius-sm);padding:6px 10px;font-size:0.7rem;color:var(--accent-primary);font-weight:700;">
                📅 <span id="working-days-count">0</span> working day(s) — excl. weekends &amp; public holidays
              </div>

              <div id="doc-upload-section" style="display:flex;background:rgba(201,164,106,0.06);border:1px solid var(--border-color);border-radius:var(--radius-md);padding:var(--spacing-sm);font-size:0.7rem;display:flex;flex-direction:column;gap:6px;text-align:left;">
                <label id="doc-upload-label" style="font-weight:700;color:var(--text-muted);text-transform:uppercase;font-size:0.6rem;letter-spacing:0.5px;">📎 Supporting Document (Max 10MB)</label>
                <input type="file" id="leave-input-doc" style="width:100%;background:rgba(0,0,0,0.3);border:1px solid var(--border-color);border-radius:var(--radius-md);color:var(--text-primary);padding:4px;outline:none;" accept="application/pdf,image/*">
              </div>

              <div id="protected-notice" style="display:none;background:rgba(99,102,241,0.08);border:1px solid rgba(99,102,241,0.3);border-radius:var(--radius-sm);padding:8px;font-size:0.7rem;color:#818cf8;text-align:left;">
                🛡️ This is a <strong>legally protected</strong> leave type. It requires approval from a Supervisor or Store Admin, and can only be rejected with a written reason.
              </div>

              <button class="btn" id="btn-submit-leave-request" style="background:var(--accent-primary);color:#000;font-weight:800;border:none;border-radius:var(--radius-md);padding:var(--spacing-sm);cursor:pointer;transition:var(--transition-fast);margin-top:4px;">
                Submit Leave Application
              </button>
            </div>
          </div>

          <!-- Public Holidays -->
          <div class="card glass" style="padding:var(--spacing-lg);border-radius:var(--radius-lg);border:1px solid var(--border-color);background:var(--bg-card);">
            <div style="display:flex;justify-content:space-between;align-items:center;border-bottom:1px solid rgba(255,255,255,0.05);padding-bottom:var(--spacing-xs);margin-bottom:var(--spacing-sm);">
              <h3 style="font-family:var(--font-display);font-weight:800;font-size:0.9rem;margin:0;color:var(--accent-primary);">🇫🇷 Public Holidays 2026</h3>
            </div>
            ${holidayList || '<div style="color:var(--text-muted);font-size:0.72rem;">No holidays loaded.</div>'}
            ${this.holidays.length > 5 ? `<div style="font-size:0.62rem;color:var(--text-muted);margin-top:6px;">+${this.holidays.length - 5} more holidays</div>` : ''}
          </div>

        </div>

      </div>

      </div>

      <!--Leave Policy Card (clickable)-->
      <div id="btn-leave-policy" class="card glass" style="padding:var(--spacing-md);border-radius:var(--radius-lg);border:1px solid var(--border-color);background:var(--bg-card);cursor:pointer;transition:var(--transition-fast);display:flex;align-items:center;gap:var(--spacing-md);" onmouseover="this.style.transform='translateY(-2px)';this.style.borderColor='var(--accent-primary)';" onmouseout="this.style.transform='none';this.style.borderColor='var(--border-color)';">
        <div style="width:42px;height:42px;border-radius:var(--radius-md);background:rgba(201,164,106,0.12);display:flex;align-items:center;justify-content:center;flex-shrink:0;">
          <i data-lucide="book-open" style="width:20px;height:20px;color:var(--accent-primary);"></i>
        </div>
        <div style="flex:1;">
          <h4 style="margin:0;font-family:var(--font-display);font-weight:800;font-size:0.95rem;color:var(--text-primary);">Leave Policy</h4>
          <p style="margin:2px 0 0 0;font-size:0.65rem;color:var(--text-muted);font-weight:600;">View leave rules, entitlements & approval workflows</p>
        </div>
        <i data-lucide="chevron-right" style="width:16px;height:16px;color:var(--text-muted);flex-shrink:0;"></i>
      </div>

      <!--Leave Policy Modal-->
      <div id="leave-policy-modal" style="display:none;position:fixed;inset:0;background:rgba(0,0,0,0.7);backdrop-filter:blur(6px);z-index:9999;align-items:center;justify-content:center;padding:var(--spacing-md);">
        <div style="background:var(--bg-card);border:1px solid var(--border-color);border-radius:var(--radius-lg);padding:var(--spacing-lg);width:100%;max-width:700px;max-height:85vh;overflow-y:auto;display:flex;flex-direction:column;gap:var(--spacing-md);">
          <div style="display:flex;justify-content:space-between;align-items:center;border-bottom:1.5px solid var(--accent-primary);padding-bottom:var(--spacing-xs);flex-shrink:0;">
            <h3 style="margin:0;font-family:var(--font-display);font-weight:800;font-size:1.1rem;color:var(--accent-primary);">📋 Leave Policy & Rules</h3>
            <button id="leave-policy-close" style="background:none;border:none;color:var(--text-muted);cursor:pointer;padding:4px;">
              <i data-lucide="x" style="width:18px;height:18px;"></i>
            </button>
          </div>

          <div style="font-size:0.72rem;color:var(--text-muted);line-height:1.5;flex-shrink:0;">
            The following leave entitlements and rules apply to all employees as per company policy and ${this.countryName || 'French'} Labour Law.
          </div>

          <!-- Policy Table -->
          <div style="overflow-x:auto;flex-shrink:0;">
            <table style="width:100%;border-collapse:collapse;font-size:0.7rem;text-align:left;">
              <thead>
                <tr style="border-bottom:1.5px solid var(--border-color);color:var(--text-muted);text-transform:uppercase;font-weight:700;font-size:0.58rem;">
                  <th style="padding:8px 6px;">Leave Type</th>
                  <th style="padding:8px 6px;text-align:center;">Annual Limit</th>
                  <th style="padding:8px 6px;text-align:center;">Accrual/Month</th>
                  <th style="padding:8px 6px;text-align:center;">Max Consecutive</th>
                  <th style="padding:8px 6px;text-align:center;">Notice (days)</th>
                  <th style="padding:8px 6px;text-align:center;">Paid</th>
                  <th style="padding:8px 6px;text-align:center;">Protected</th>
                  <th style="padding:8px 6px;text-align:center;">Document</th>
                </tr>
              </thead>
              <tbody>
                ${this.leaveTypes.map(t => `
                  <tr style="border-bottom:1px solid rgba(255,255,255,0.04);">
                    <td style="padding:8px 6px;font-weight:700;color:var(--text-primary);">${t.name}${t.protected ? ' 🛡️' : ''}</td>
                    <td style="padding:8px 6px;text-align:center;font-weight:600;">${t.annualLimit != null ? t.annualLimit + 'd' : '—'}</td>
                    <td style="padding:8px 6px;text-align:center;">${t.monthlyAccrual != null ? t.monthlyAccrual + 'd' : '—'}</td>
                    <td style="padding:8px 6px;text-align:center;">${t.maxConsecutiveDays != null ? t.maxConsecutiveDays + 'd' : '—'}</td>
                    <td style="padding:8px 6px;text-align:center;">${t.minNoticeDays != null ? t.minNoticeDays + 'd' : '0d'}</td>
                    <td style="padding:8px 6px;text-align:center;">${t.paid ? '<span style="color:var(--status-success);font-weight:700;">Yes</span>' : '<span style="color:var(--status-danger);font-weight:700;">No</span>'}</td>
                    <td style="padding:8px 6px;text-align:center;">${t.protected ? '<span style="color:#818cf8;font-weight:700;">Yes</span>' : '<span style="color:var(--text-muted);">No</span>'}</td>
                    <td style="padding:8px 6px;text-align:center;">${t.requiresDocument ? '<span style="color:var(--status-warning);font-weight:700;">Required</span>' : '<span style="color:var(--text-muted);">—</span>'}</td>
                  </tr>
                `).join('')}
              </tbody>
            </table>
          </div>

          <!-- General Rules -->
          <div style="background:rgba(201,164,106,0.06);border:1px solid rgba(201,164,106,0.15);border-radius:var(--radius-md);padding:var(--spacing-md);display:flex;flex-direction:column;gap:8px;font-size:0.7rem;flex-shrink:0;">
            <h4 style="margin:0;font-weight:800;color:var(--accent-primary);font-size:0.8rem;">General Rules</h4>
            <ul style="margin:0;padding-left:16px;color:var(--text-primary);line-height:1.7;">
              <li>Leave requests must be submitted at least the required notice period in advance.</li>
              <li>Sick leave exceeding 3 consecutive days requires a medical certificate.</li>
              <li>Protected leave types (🛡️) are auto-approved per French Labour Code.</li>
              <li>Unpaid leave does not accrue and is deducted from salary.</li>
              <li>Leave cannot be applied during active <strong>blackout periods</strong>.</li>
              <li>Approval workflow: Employee → Supervisor → Store Admin (for extended leaves).</li>
              <li>Unused annual leave is subject to carry-forward policy (max 5 days).</li>
            </ul>
          </div>
        </div>
      </div>

      <!--Cancel Modal-- >
      <div id="cancel-modal" style="display:none;position:fixed;inset:0;background:rgba(0,0,0,0.7);z-index:9999;align-items:center;justify-content:center;">
        <div style="background:var(--bg-card);border:1px solid var(--border-color);border-radius:var(--radius-lg);padding:var(--spacing-lg);width:100%;max-width:440px;display:flex;flex-direction:column;gap:var(--spacing-md);">
          <h3 id="cancel-modal-title" style="font-family:var(--font-display);font-weight:800;font-size:1rem;margin:0;color:var(--accent-primary);">Cancel Leave Request</h3>
          <label style="font-size:0.72rem;font-weight:700;color:var(--text-muted);">Cancellation Reason <span style="color:var(--status-danger);">*</span> (min 10 chars)</label>
          <textarea id="cancel-reason-input" rows="3" placeholder="Please provide a reason for cancellation..." style="width:100%;background:rgba(0,0,0,0.3);border:1px solid var(--border-color);border-radius:var(--radius-md);color:var(--text-primary);padding:var(--spacing-sm);outline:none;font-family:inherit;resize:none;"></textarea>
          <div style="display:flex;gap:var(--spacing-sm);">
            <button id="cancel-modal-submit" data-leave-id="" data-mode="cancel" style="flex:1;background:var(--status-danger);color:#fff;font-weight:800;border:none;padding:var(--spacing-sm);border-radius:var(--radius-md);cursor:pointer;">Confirm</button>
            <button id="cancel-modal-close" style="flex:1;background:rgba(255,255,255,0.06);color:var(--text-primary);font-weight:700;border:1px solid var(--border-color);padding:var(--spacing-sm);border-radius:var(--radius-md);cursor:pointer;">Back</button>
          </div>
        </div>
      </div>

      <!--Reject Modal-->
        <div id="reject-modal" style="display:none;position:fixed;inset:0;background:rgba(0,0,0,0.7);z-index:9999;align-items:center;justify-content:center;">
          <div style="background:var(--bg-card);border:1px solid var(--border-color);border-radius:var(--radius-lg);padding:var(--spacing-lg);width:100%;max-width:400px;display:flex;flex-direction:column;gap:var(--spacing-md);text-align:left;">
            <h3 style="font-family:var(--font-display);font-weight:800;font-size:1.0rem;margin:0;color:var(--status-danger);">Reject Barista Leave</h3>
            <label style="font-size:0.72rem;font-weight:700;color:var(--text-muted);">Rejection Reason <span style="color:var(--status-danger);">*</span> (min 10 chars)</label>
            <textarea id="reject-reason-input" rows="3" placeholder="State reason for rejection..." style="width:100%;background:rgba(0,0,0,0.3);border:1px solid var(--border-color);border-radius:var(--radius-md);color:var(--text-primary);padding:var(--spacing-sm);outline:none;font-family:inherit;resize:none;"></textarea>
            <div style="display:flex;gap:var(--spacing-sm);">
              <button id="reject-modal-submit" data-leave-id="" style="flex:1;background:var(--status-danger);color:#fff;font-weight:800;border:none;padding:var(--spacing-sm);border-radius:var(--radius-md);cursor:pointer;">Confirm Rejection</button>
              <button id="reject-modal-close" style="flex:1;background:rgba(255,255,255,0.06);color:var(--text-primary);font-weight:700;border:1px solid var(--border-color);padding:var(--spacing-sm);border-radius:var(--radius-md);cursor:pointer;">Cancel</button>
            </div>
          </div>
        </div>

        <input type="file" id="global-doc-upload-input" style="display:none;" accept="application/pdf,image/*">
      `;

    if (window.lucide) window.lucide.createIcons();
  }

  /** Parse date range string like "18 Jul - 20 Jul 2026" or "18 Jul 2026" */
  parseDateRange(str) {
    if (!str) return null;
    const parts = str.split('-').map(p => p.trim());
    
    const parseSingle = (s) => {
      let d = new Date(s);
      if (!isNaN(d.getTime())) return d;
      
      const months = {
        jan: 0, feb: 1, mar: 2, apr: 3, may: 4, jun: 5,
        jul: 6, aug: 7, sep: 8, oct: 9, nov: 10, dec: 11
      };
      
      const tokens = s.toLowerCase().replace(/,/g, '').split(/\s+/);
      if (tokens.length >= 2) {
        const day = parseInt(tokens[0]);
        const monthStr = tokens[1].substring(0, 3);
        const month = months[monthStr];
        let year = new Date().getFullYear();
        if (tokens.length >= 3) {
          const y = parseInt(tokens[2]);
          if (!isNaN(y)) year = y;
        }
        if (!isNaN(day) && month !== undefined) {
          return new Date(year, month, day);
        }
      }
      return null;
    };

    if (parts.length === 1) {
      const d = parseSingle(parts[0]);
      if (d) {
        const iso = d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0');
        return { start: iso, end: iso };
      }
    } else if (parts.length === 2) {
      let dStart = parseSingle(parts[0]);
      let dEnd = parseSingle(parts[1]);
      
      if (dStart && dEnd) {
        const startTokens = parts[0].toLowerCase().split(/\s+/);
        const endTokens = parts[1].toLowerCase().split(/\s+/);
        if (endTokens.length === 2 && startTokens.length === 3) {
          const year = parseInt(startTokens[2]);
          if (!isNaN(year)) {
            dEnd.setFullYear(year);
          }
        }
        
        const startIso = dStart.getFullYear() + '-' + String(dStart.getMonth() + 1).padStart(2, '0') + '-' + String(dStart.getDate()).padStart(2, '0');
        const endIso = dEnd.getFullYear() + '-' + String(dEnd.getMonth() + 1).padStart(2, '0') + '-' + String(dEnd.getDate()).padStart(2, '0');
        return { start: startIso, end: endIso };
      }
    }
    return null;
  }

  bindEvents(container, lifecycle) {
    // Type change → show/hide doc/protected notices
    const typeSelect = container.querySelector('#leave-select-type');
    const docSection = container.querySelector('#doc-upload-section');
    const protectedNotice = container.querySelector('#protected-notice');
    typeSelect?.addEventListener('change', () => {
      const opt = typeSelect.selectedOptions[0];
      const isProtected = opt?.dataset.protected === 'true';
      const requiresDoc = opt?.dataset.doc === 'true';
      const docLabel = container.querySelector('#doc-upload-label');
      if (docLabel) {
        docLabel.innerHTML = requiresDoc 
          ? '📎 Supporting Document (Max 10MB) <span style="color:var(--status-danger);font-weight:bold;">* Required</span>'
          : '📎 Supporting Document (Max 10MB) <span style="color:var(--text-muted);">(Optional)</span>';
      }
      protectedNotice.style.display = isProtected ? 'block' : 'none';
    });

    // Date range auto-calculator as employee selects calendar dates
    // Date range auto-calculator as employee selects calendar dates
    const startInput = container.querySelector('#leave-input-start');
    const endInput = container.querySelector('#leave-input-end');
    const indicator = container.querySelector('#working-days-indicator');
    const counter = container.querySelector('#working-days-count');

    const updateDays = () => {
      const startVal = startInput?.value;
      const endVal = endInput?.value;
      if (startVal && endVal) {
        const start = new Date(startVal);
        const end = new Date(endVal);
        if (end >= start) {
          const days = this.calcWorkingDays(start, end, 'FULL_DAY');
          counter.textContent = days % 1 === 0 ? days : days.toFixed(1);
          indicator.style.display = 'block';
        } else {
          indicator.style.display = 'none';
        }
      } else {
        indicator.style.display = 'none';
      }
    };

    startInput?.addEventListener('change', updateDays);
    startInput?.addEventListener('input', updateDays);
    endInput?.addEventListener('change', updateDays);
    endInput?.addEventListener('input', updateDays);

    // Submit request
    container.querySelector('#btn-submit-leave-request')?.addEventListener('click', async () => {
      const typeId = typeSelect?.value;
      const startVal = startInput?.value;
      const endVal = endInput?.value;
      const reason = container.querySelector('#leave-input-reason')?.value?.trim();
      const fileInput = container.querySelector('#leave-input-doc');
      const file = fileInput?.files?.[0];

      if (!typeId) { notificationStore.danger('Please select a leave category.'); return; }
      if (!startVal || !endVal) { notificationStore.danger('Please specify both start and end dates.'); return; }
      if (!reason) { notificationStore.danger('Please provide a reason justification.'); return; }

      const opt = typeSelect.selectedOptions[0];
      const requiresDoc = opt?.dataset.doc === 'true';
      if (requiresDoc && !file) {
        notificationStore.danger('Supporting document is required for this leave type.');
        return;
      }

      if (file) {
        const maxLimit = 10 * 1024 * 1024; // 10MB
        if (file.size > maxLimit) {
          notificationStore.danger('File size exceeds the 10MB limit.');
          return;
        }
      }

      const start = new Date(startVal);
      const end = new Date(endVal);
      if (end < start) {
        notificationStore.danger('End date cannot be before start date.');
        return;
      }

      const daysVal = this.calcWorkingDays(start, end, 'FULL_DAY');
      const session = daysVal === 0.5 ? 'HALF_DAY_AM' : 'FULL_DAY';

      try {
        const response = await apiClient.post('/leaves', {
          leaveTypeId: typeId,
          session,
          startDate: startVal,
          endDate: endVal,
          reason
        });
        if (response && response.success) {
          const leaveId = response.data?.id;
          let uploadSuccess = true;

          if (file && leaveId) {
            const formData = new FormData();
            formData.append('file', file);
            try {
              const uploadRes = await apiClient.request(`/leaves/${leaveId}/upload-document`, {
                method: 'POST',
                body: formData
              });
              if (!uploadRes || !uploadRes.success) {
                uploadSuccess = false;
                notificationStore.danger(uploadRes?.message || 'Document upload failed.');
              }
            } catch (uploadErr) {
              uploadSuccess = false;
              notificationStore.danger('Failed to upload document: ' + uploadErr.message);
            }
          }

          if (uploadSuccess) {
            const status = response.data?.status;
            if (status === 'APPROVED') {
              notificationStore.success('✅ Leave auto-approved.');
            } else {
              notificationStore.success('Leave application submitted successfully. Pending approval.');
            }
            await this.loadData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          }
        } else {
          notificationStore.danger(response?.message || 'Failed to submit leave. Please check your details.');
        }
      } catch (err) {
        notificationStore.danger(err.message || 'Failed to submit leave.');
      }
    });

    // Cancel leave (PENDING)
    container.querySelectorAll('.btn-cancel-leave').forEach(btn => {
      btn.addEventListener('click', () => {
        const modal = container.querySelector('#cancel-modal');
        const submitBtn = container.querySelector('#cancel-modal-submit');
        container.querySelector('#cancel-modal-title').textContent = 'Cancel Leave Request';
        submitBtn.dataset.leaveId = btn.dataset.id;
        submitBtn.dataset.mode = 'cancel';
        container.querySelector('#cancel-reason-input').value = '';
        modal.style.display = 'flex';
      });
    });

    // Request cancellation (APPROVED)
    container.querySelectorAll('.btn-req-cancel-leave').forEach(btn => {
      btn.addEventListener('click', () => {
        const modal = container.querySelector('#cancel-modal');
        const submitBtn = container.querySelector('#cancel-modal-submit');
        container.querySelector('#cancel-modal-title').textContent = 'Request Leave Cancellation';
        submitBtn.dataset.leaveId = btn.dataset.id;
        submitBtn.dataset.mode = 'request';
        container.querySelector('#cancel-reason-input').value = '';
        modal.style.display = 'flex';
      });
    });

    // Modal submit
    container.querySelector('#cancel-modal-submit')?.addEventListener('click', async () => {
      const reason = container.querySelector('#cancel-reason-input')?.value?.trim();
      const btn = container.querySelector('#cancel-modal-submit');
      const leaveId = btn.dataset.leaveId;
      const mode = btn.dataset.mode;
      if (!reason || reason.length < 10) {
        notificationStore.danger('Cancellation reason must be at least 10 characters.'); return;
      }
      const endpoint = mode === 'cancel' ? `/ leaves / ${ leaveId } /cancel` : `/leaves / ${ leaveId }/request-cancellation`;
      try {
        const response = await apiClient.put(endpoint, { reason });
        if (response && response.success) {
          notificationStore.success(mode === 'cancel' ? 'Leave cancelled. Balance restored.' : 'Cancellation request submitted.');
          container.querySelector('#cancel-modal').style.display = 'none';
          await this.loadData(); this.render(container); this.bindEvents(container, lifecycle);
        } else {
          notificationStore.danger(response?.message || 'Failed. Please try again.');
        }
      } catch (err) {
        notificationStore.danger(err.message || 'Failed to submit cancellation request.');
      }
    });

    container.querySelector('#cancel-modal-close')?.addEventListener('click', () => {
      container.querySelector('#cancel-modal').style.display = 'none';
    });



    // Document upload handling
    const fileInput = container.querySelector('#global-doc-upload-input');
    container.querySelectorAll('.btn-upload-doc').forEach(btn => {
      btn.addEventListener('click', () => {
        if (fileInput) {
          fileInput.dataset.leaveId = btn.dataset.id;
          fileInput.value = ''; // clear previous selection
          fileInput.click();
        }
      });
    });

    fileInput?.addEventListener('change', async (e) => {
      const file = e.target.files?.[0];
      if (!file) return;

      const maxLimit = 10 * 1024 * 1024; // 10MB
      if (file.size > maxLimit) {
        notificationStore.danger('File size exceeds the 10MB limit.');
        return;
      }

      const leaveId = fileInput.dataset.leaveId;
      const formData = new FormData();
      formData.append('file', file);

      try {
        const response = await apiClient.request(`/leaves/${leaveId}/upload-document`, {
          method: 'POST',
          body: formData
        });
        if (response && response.success) {
          notificationStore.success('Document uploaded successfully.');
          await this.loadData();
          this.render(container);
          this.bindEvents(container, lifecycle);
        } else {
          notificationStore.danger(response?.message || 'Failed to upload document.');
        }
      } catch (err) {
        notificationStore.danger(err.message || 'Failed to upload document.');
      }
    });

    // Leave Policy modal open/close
    const policyModal = container.querySelector('#leave-policy-modal');
    container.querySelector('#btn-leave-policy')?.addEventListener('click', () => {
      if (policyModal) policyModal.style.display = 'flex';
    });
    container.querySelector('#leave-policy-close')?.addEventListener('click', () => {
      if (policyModal) policyModal.style.display = 'none';
    });
    policyModal?.addEventListener('click', (e) => {
      if (e.target === policyModal) policyModal.style.display = 'none';
    });
  }

  /** Client-side working day estimator (excludes weekends + seeded holidays) */
  calcWorkingDays(startStr, endStr, session) {
    const start = new Date(startStr);
    const end = new Date(endStr);
    const holidayDates = new Set(this.holidays.map(h => h.date));
    let count = 0;
    const cur = new Date(start);
    while (cur <= end) {
      const d = cur.getUTCDay();
      const iso = cur.getUTCFullYear() + '-' +
        String(cur.getUTCMonth() + 1).padStart(2, '0') + '-' +
        String(cur.getUTCDate()).padStart(2, '0');
      if (d !== 0 && d !== 6 && !holidayDates.has(iso)) count++;
      cur.setUTCDate(cur.getUTCDate() + 1);
    }
    if ((session === 'HALF_DAY_AM' || session === 'HALF_DAY_PM') && count > 0) {
      return (count - 1) + 0.5;
    }
    return count;
  }
}
