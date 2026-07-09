/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Admin Module
 * File              : leave.js
 * Purpose           : Store Admin Leave Management — approval hub, analytics,
 *                     calendar, audit trail, balance report
 * Version           : 1.0.0
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../api/client.js';

const API = '/api/v1';

export default class StoreAdminLeave {
  constructor() {
    this.user = authStore.getUser();
    this.pendingLeaves = [];
    this.cancellationRequests = [];
    this.allLeaves = [];
    this.summary = {};
    this.holidays = [];
    this.leaveTypes = [];
    this.activeTab = 'approvals';
  }

  async mount(container, lifecycle) {
    logger.info('StoreAdminLeave', 'Mounting store admin leave management...');
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
      const [pendingRes, allRes, summaryRes, holidayRes, typesRes] = await Promise.all([
        safeGet('/leaves/pending'),
        safeGet('/leaves/my'),
        safeGet('/leaves/reports/summary'),
        safeGet(`/leaves/holidays?countryCode=FR&year=${new Date().getFullYear()}`),
        safeGet('/leaves/types')
      ]);
      if (pendingRes && pendingRes.success) {
        this.pendingLeaves = pendingRes.data?.pending || [];
        this.cancellationRequests = pendingRes.data?.cancellationRequests || [];
      }
      this.summary = (summaryRes && summaryRes.success) ? summaryRes.data || {} : {};
      this.holidays = (holidayRes && holidayRes.success) ? holidayRes.data || [] : [];
      this.leaveTypes = (typesRes && typesRes.success) ? typesRes.data || [] : [];
    } catch (err) {
      logger.error('StoreAdminLeave', 'Failed to load data', err);
    }
  }

  statusColor(status) {
    return status === 'APPROVED' ? 'var(--status-success)' :
           status === 'PENDING'  ? 'var(--status-warning)' :
           status === 'REJECTED' ? 'var(--status-danger)'  :
           'var(--text-muted)';
  }

  slaWarning(leave) {
    if (!leave.approvalDueAt) return '';
    const due = new Date(leave.approvalDueAt);
    const now = new Date();
    const hoursLeft = Math.floor((due - now) / 3600000);
    if (hoursLeft < 0) return `<span style="font-size:0.6rem;font-weight:700;color:var(--status-danger);background:rgba(239,68,68,0.12);padding:2px 6px;border-radius:99px;">⏰ SLA OVERDUE</span>`;
    if (hoursLeft < 6) return `<span style="font-size:0.6rem;font-weight:700;color:var(--status-warning);background:rgba(234,179,8,0.12);padding:2px 6px;border-radius:99px;">⏳ ${hoursLeft}h left</span>`;
    return `<span style="font-size:0.6rem;color:var(--text-muted);">Due ${due.toLocaleDateString()}</span>`;
  }

  render(container, loading = false) {
    if (loading) {
      container.innerHTML = `
        <div style="display:flex;align-items:center;justify-content:center;height:400px;flex-direction:column;gap:12px;">
          <i data-lucide="loader-2" style="width:32px;height:32px;color:var(--accent-primary);animation:spin 1s linear infinite;"></i>
          <span style="color:var(--text-muted);font-size:0.8rem;font-weight:600;">Loading leave management...</span>
        </div>`;
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    const { totalPending = 0, totalApproved = 0, totalRejected = 0, totalRequests = 0 } = this.summary;
    const cancellationCount = this.cancellationRequests.length;

    const kpiCards = [
      { label:'Pending Approvals', value: totalPending, icon:'clock', color:'var(--status-warning)' },
      { label:'Cancellation Requests', value: cancellationCount, icon:'x-circle', color:'var(--status-danger)' },
      { label:'Approved This Year', value: totalApproved, icon:'check-circle', color:'var(--status-success)' },
      { label:'Total Requests', value: totalRequests, icon:'calendar', color:'var(--accent-primary)' }
    ].map(k => `
      <div class="card glass" style="padding:var(--spacing-md);border-radius:var(--radius-md);border:1px solid var(--border-color);background:var(--bg-card);">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:6px;">
          <span style="font-size:0.6rem;font-weight:700;text-transform:uppercase;letter-spacing:.5px;color:var(--text-muted);">${k.label}</span>
          <i data-lucide="${k.icon}" style="width:14px;height:14px;color:${k.color};"></i>
        </div>
        <div style="font-size:1.6rem;font-weight:900;color:var(--text-primary);">${k.value}</div>
      </div>`
    ).join('');

    const pendingRows = this.pendingLeaves.length === 0
      ? `<div style="text-align:center;padding:var(--spacing-lg);color:var(--text-muted);font-size:0.78rem;">✅ No pending leave requests requiring your approval.</div>`
      : this.pendingLeaves.map(l => `
        <div class="card glass leave-approval-card" style="padding:var(--spacing-md);border-radius:var(--radius-md);border:1px solid var(--border-color);background:rgba(255,255,255,0.02);display:flex;flex-direction:column;gap:8px;" data-leave-id="${l.id}" data-protected="${l.isProtected}">
          <div style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:6px;">
            <div>
              <div style="font-weight:800;font-size:0.82rem;color:var(--text-primary);">${l.employeeName || 'Employee'}</div>
              <div style="font-size:0.7rem;color:var(--text-muted);">${l.leaveType} &nbsp;·&nbsp; ${l.session === 'FULL_DAY' ? 'Full Day' : l.session === 'HALF_DAY_AM' ? '½ Morning' : '½ Afternoon'} &nbsp;·&nbsp; <strong>${l.totalDays}d</strong></div>
            </div>
            <div style="display:flex;align-items:center;gap:8px;flex-wrap:wrap;">
              ${this.slaWarning(l)}
              ${l.isProtected ? '<span style="font-size:0.6rem;font-weight:700;color:#818cf8;background:rgba(129,140,248,0.12);padding:2px 6px;border-radius:99px;">🛡️ Protected</span>' : ''}
              ${!l.hasDocument && l.requiresDocument ? '<span style="font-size:0.6rem;font-weight:700;color:var(--status-danger);background:rgba(239,68,68,0.08);padding:2px 6px;border-radius:99px;">📄 Doc Missing</span>' : ''}
            </div>
          </div>
          <div style="display:flex;gap:8px;font-size:0.68rem;color:var(--text-muted);">
            <span>📅 ${l.startDate} → ${l.endDate}</span>
          </div>
          ${l.reason ? `<div style="font-size:0.7rem;color:var(--text-secondary);background:rgba(255,255,255,0.03);padding:6px 8px;border-radius:var(--radius-sm);border-left:2px solid var(--accent-primary);">"${l.reason}"</div>` : ''}
          <div style="display:flex;flex-direction:column;gap:4px;">
            <input type="text" class="admin-comment-input" data-id="${l.id}" placeholder="Store Admin comment (optional)..." style="background:rgba(0,0,0,0.3);border:1px solid var(--border-color);border-radius:var(--radius-sm);color:var(--text-primary);padding:6px 8px;font-size:0.7rem;outline:none;font-family:inherit;">
          </div>
          <div style="display:flex;gap:8px;flex-wrap:wrap;">
            <button class="btn-approve-leave" data-id="${l.id}" style="flex:1;background:var(--status-success);color:#000;font-weight:800;border:none;border-radius:var(--radius-sm);padding:var(--spacing-xs) var(--spacing-sm);cursor:pointer;font-size:0.72rem;${(!l.hasDocument && l.requiresDocument) ? 'opacity:0.4;pointer-events:none;' : ''}">
              ✓ Approve
            </button>
            <button class="btn-reject-leave" data-id="${l.id}" style="flex:1;background:var(--status-danger);color:#fff;font-weight:800;border:none;border-radius:var(--radius-sm);padding:var(--spacing-xs) var(--spacing-sm);cursor:pointer;font-size:0.72rem;${l.isProtected ? 'opacity:0.4;pointer-events:none;' : ''}" ${l.isProtected ? 'disabled' : ''}>✕ Reject</button>
          </div>
          ${(!l.hasDocument && l.requiresDocument) ? '<div style="font-size:0.62rem;color:var(--status-danger);text-align:center;">Approve disabled — medical certificate required first.</div>' : ''}
        </div>`
      ).join('');

    const cancellationRows = this.cancellationRequests.length === 0
      ? `<div style="text-align:center;padding:var(--spacing-md);color:var(--text-muted);font-size:0.78rem;">No cancellation requests.</div>`
      : this.cancellationRequests.map(l => `
        <div class="card glass" style="padding:var(--spacing-md);border-radius:var(--radius-md);border:1px solid rgba(234,179,8,0.25);background:rgba(234,179,8,0.04);">
          <div style="font-weight:800;font-size:0.8rem;">${l.employeeName}</div>
          <div style="font-size:0.7rem;color:var(--text-muted);">Requesting cancellation of <strong>${l.leaveType}</strong> (${l.startDate} – ${l.endDate})</div>
          <div style="font-size:0.7rem;color:var(--status-warning);margin-top:4px;background:rgba(0,0,0,0.2);padding:4px 8px;border-radius:var(--radius-sm);">Reason: "${l.cancellationReason || 'Not specified'}"</div>
          <div style="display:flex;gap:8px;margin-top:8px;">
            <button class="btn-approve-cancellation" data-id="${l.id}" style="flex:1;background:var(--status-success);color:#000;font-weight:800;border:none;border-radius:var(--radius-sm);padding:6px;cursor:pointer;font-size:0.72rem;">Approve Cancellation</button>
          </div>
        </div>`
      ).join('');

    const holidayList = this.holidays.slice(0, 6).map(h =>
      `<div style="display:flex;justify-content:space-between;padding:4px 0;border-bottom:1px solid rgba(255,255,255,0.04);">
        <span style="font-size:0.68rem;">🇫🇷 ${h.name}</span>
        <span style="font-size:0.65rem;color:var(--text-muted);">${h.date}</span>
      </div>`
    ).join('');

    container.innerHTML = `
      <div class="workspace-container animate-fade-in" style="padding:var(--spacing-lg);display:flex;flex-direction:column;gap:var(--spacing-lg);max-width:1400px;margin:0 auto;">

        <!-- Header -->
        <div class="card glass" style="padding:var(--spacing-md) var(--spacing-lg);border-radius:var(--radius-lg);background:var(--bg-card);border:1px solid var(--border-color);display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:var(--spacing-md);">
          <div>
            <h2 style="font-family:var(--font-display);font-weight:800;font-size:1.5rem;color:var(--text-primary);margin:0;">Leave Management</h2>
            <p style="font-size:0.7rem;color:var(--text-muted);margin:2px 0 0 0;text-transform:uppercase;font-weight:600;letter-spacing:.5px;">
              Store Admin &nbsp;·&nbsp; <span style="color:var(--accent-primary);">Approval Hub &amp; Analytics</span>
            </p>
          </div>
          <div style="display:flex;align-items:center;gap:8px;">
            <span style="background:rgba(130,163,125,0.12);border:1px solid rgba(130,163,125,0.3);border-radius:var(--radius-full);padding:4px 12px;font-size:0.7rem;font-weight:700;color:var(--status-success);">● Live</span>
          </div>
        </div>

        <!-- KPI Cards -->
        <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(180px,1fr));gap:var(--spacing-sm);">
          ${kpiCards}
        </div>

        <!-- Main Grid -->
        <div style="display:grid;grid-template-columns:1.4fr 1fr;gap:var(--spacing-lg);width:100%;">

          <!-- Left: Approval Queue -->
          <div style="display:flex;flex-direction:column;gap:var(--spacing-lg);">

            <!-- Pending Approvals -->
            <div class="card glass" style="padding:var(--spacing-lg);border-radius:var(--radius-lg);border:1px solid var(--border-color);background:var(--bg-card);">
              <div style="display:flex;justify-content:space-between;align-items:center;border-bottom:1px solid rgba(255,255,255,0.05);padding-bottom:var(--spacing-xs);margin-bottom:var(--spacing-md);">
                <h3 style="font-family:var(--font-display);font-weight:800;font-size:1rem;margin:0;color:var(--accent-primary);">Approval Queue <span style="font-size:0.65rem;font-weight:700;color:var(--status-warning);background:rgba(234,179,8,0.12);padding:2px 8px;border-radius:99px;margin-left:6px;">${this.pendingLeaves.length}</span></h3>
                <i data-lucide="inbox" style="width:16px;height:16px;color:var(--accent-primary);"></i>
              </div>
              <div style="display:flex;flex-direction:column;gap:var(--spacing-md);max-height:420px;overflow-y:auto;" id="approval-queue">
                ${pendingRows}
              </div>
            </div>

            <!-- Cancellation Requests -->
            <div class="card glass" style="padding:var(--spacing-lg);border-radius:var(--radius-lg);border:1px solid rgba(234,179,8,0.2);background:var(--bg-card);">
              <div style="display:flex;justify-content:space-between;align-items:center;border-bottom:1px solid rgba(255,255,255,0.05);padding-bottom:var(--spacing-xs);margin-bottom:var(--spacing-md);">
                <h3 style="font-family:var(--font-display);font-weight:800;font-size:1rem;margin:0;color:var(--status-warning);">Cancellation Requests <span style="font-size:0.65rem;font-weight:700;color:var(--status-warning);background:rgba(234,179,8,0.12);padding:2px 8px;border-radius:99px;margin-left:6px;">${cancellationCount}</span></h3>
                <i data-lucide="x-circle" style="width:16px;height:16px;color:var(--status-warning);"></i>
              </div>
              <div style="display:flex;flex-direction:column;gap:var(--spacing-sm);">
                ${cancellationRows}
              </div>
            </div>

          </div>

          <!-- Right: Summary + Holidays -->
          <div style="display:flex;flex-direction:column;gap:var(--spacing-lg);">

            <!-- Summary Stats -->
            <div class="card glass" style="padding:var(--spacing-lg);border-radius:var(--radius-lg);border:1px solid var(--border-color);background:var(--bg-card);">
              <div style="display:flex;justify-content:space-between;align-items:center;border-bottom:1px solid rgba(255,255,255,0.05);padding-bottom:var(--spacing-xs);margin-bottom:var(--spacing-md);">
                <h3 style="font-family:var(--font-display);font-weight:800;font-size:1rem;margin:0;color:var(--accent-primary);">Leave Summary ${this.summary.year || ''}</h3>
                <i data-lucide="bar-chart-2" style="width:16px;height:16px;color:var(--accent-primary);"></i>
              </div>
              ${[
                { label:'Total Requests', value: this.summary.totalRequests || 0, color:'var(--accent-primary)' },
                { label:'Approved', value: this.summary.totalApproved || 0, color:'var(--status-success)' },
                { label:'Pending', value: this.summary.totalPending || 0, color:'var(--status-warning)' },
                { label:'Rejected', value: this.summary.totalRejected || 0, color:'var(--status-danger)' },
                { label:'Cancelled', value: this.summary.totalCancelled || 0, color:'var(--text-muted)' }
              ].map(s => `
                <div style="display:flex;justify-content:space-between;align-items:center;padding:6px 0;border-bottom:1px solid rgba(255,255,255,0.04);">
                  <span style="font-size:0.72rem;color:var(--text-muted);">${s.label}</span>
                  <span style="font-size:0.8rem;font-weight:800;color:${s.color};">${s.value}</span>
                </div>
              `).join('')}
            </div>

            <!-- Leave Types Reference -->
            <div class="card glass" style="padding:var(--spacing-lg);border-radius:var(--radius-lg);border:1px solid var(--border-color);background:var(--bg-card);">
              <div style="display:flex;justify-content:space-between;align-items:center;border-bottom:1px solid rgba(255,255,255,0.05);padding-bottom:var(--spacing-xs);margin-bottom:var(--spacing-md);">
                <h3 style="font-family:var(--font-display);font-weight:800;font-size:0.9rem;margin:0;color:var(--accent-primary);">Leave Types Policy</h3>
                <i data-lucide="file-text" style="width:14px;height:14px;color:var(--accent-primary);"></i>
              </div>
              ${this.leaveTypes.map(lt => `
                <div style="display:flex;justify-content:space-between;align-items:center;padding:4px 0;border-bottom:1px solid rgba(255,255,255,0.03);">
                  <span style="font-size:0.68rem;color:var(--text-primary);">
                    ${lt.protected ? '🛡️ ' : ''}${lt.name}
                  </span>
                  <div style="display:flex;gap:4px;align-items:center;">
                    <span style="font-size:0.6rem;color:var(--text-muted);background:rgba(255,255,255,0.05);padding:1px 6px;border-radius:4px;">${lt.approvalLevel}</span>
                    ${lt.annualLimit ? `<span style="font-size:0.6rem;color:var(--accent-primary);">${lt.annualLimit}d</span>` : '<span style="font-size:0.6rem;color:var(--text-muted);">Open</span>'}
                  </div>
                </div>
              `).join('')}
            </div>

            <!-- Public Holidays -->
            <div class="card glass" style="padding:var(--spacing-lg);border-radius:var(--radius-lg);border:1px solid var(--border-color);background:var(--bg-card);">
              <div style="display:flex;justify-content:space-between;align-items:center;border-bottom:1px solid rgba(255,255,255,0.05);padding-bottom:var(--spacing-xs);margin-bottom:var(--spacing-sm);">
                <h3 style="font-family:var(--font-display);font-weight:800;font-size:0.9rem;margin:0;color:var(--accent-primary);">🇫🇷 Public Holidays 2026</h3>
              </div>
              ${holidayList}
            </div>

          </div>

        </div>

      </div>

      <!-- Reject Modal -->
      <div id="reject-modal" style="display:none;position:fixed;inset:0;background:rgba(0,0,0,0.7);z-index:9999;align-items:center;justify-content:center;">
        <div style="background:var(--bg-card);border:1px solid var(--border-color);border-radius:var(--radius-lg);padding:var(--spacing-lg);width:100%;max-width:440px;display:flex;flex-direction:column;gap:var(--spacing-md);">
          <h3 style="font-family:var(--font-display);font-weight:800;font-size:1rem;margin:0;color:var(--status-danger);">Reject Leave Request</h3>
          <div style="font-size:0.72rem;color:var(--text-muted);">You are rejecting a leave request. This action is irreversible.</div>
          <label style="font-size:0.72rem;font-weight:700;color:var(--text-muted);">Rejection Reason <span style="color:var(--status-danger);">*</span> (10–500 chars)</label>
          <textarea id="reject-reason-input" rows="3" placeholder="State the reason for rejection..." style="width:100%;background:rgba(0,0,0,0.3);border:1px solid var(--status-danger);border-radius:var(--radius-md);color:var(--text-primary);padding:var(--spacing-sm);outline:none;font-family:inherit;resize:none;"></textarea>
          <div id="reject-char-count" style="font-size:0.62rem;color:var(--text-muted);text-align:right;">0 / 500</div>
          <div style="display:flex;gap:var(--spacing-sm);">
            <button id="reject-modal-submit" data-leave-id="" style="flex:1;background:var(--status-danger);color:#fff;font-weight:800;border:none;padding:var(--spacing-sm);border-radius:var(--radius-md);cursor:pointer;">Confirm Rejection</button>
            <button id="reject-modal-close" style="flex:1;background:rgba(255,255,255,0.06);color:var(--text-primary);font-weight:700;border:1px solid var(--border-color);padding:var(--spacing-sm);border-radius:var(--radius-md);cursor:pointer;">Cancel</button>
          </div>
        </div>
      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {

    // Approve leave
    container.querySelectorAll('.btn-approve-leave').forEach(btn => {
      btn.addEventListener('click', async () => {
        const leaveId = btn.dataset.id;
        const commentInput = container.querySelector(`.admin-comment-input[data-id="${leaveId}"]`);
        const comment = commentInput?.value?.trim() || '';
        await this.doApprove(leaveId, comment, container, lifecycle);
      });
    });

    // Open reject modal
    container.querySelectorAll('.btn-reject-leave').forEach(btn => {
      btn.addEventListener('click', () => {
        const modal = container.querySelector('#reject-modal');
        container.querySelector('#reject-modal-submit').dataset.leaveId = btn.dataset.id;
        container.querySelector('#reject-reason-input').value = '';
        container.querySelector('#reject-char-count').textContent = '0 / 500';
        modal.style.display = 'flex';
      });
    });

    // Char count for rejection reason
    container.querySelector('#reject-reason-input')?.addEventListener('input', (e) => {
      container.querySelector('#reject-char-count').textContent = `${e.target.value.length} / 500`;
    });

    // Reject submit
    container.querySelector('#reject-modal-submit')?.addEventListener('click', async () => {
      const reason = container.querySelector('#reject-reason-input')?.value?.trim();
      const leaveId = container.querySelector('#reject-modal-submit').dataset.leaveId;
      if (!reason || reason.length < 10) { notificationStore.danger('Rejection reason must be at least 10 characters.'); return; }
      if (reason.length > 500) { notificationStore.danger('Rejection reason cannot exceed 500 characters.'); return; }
      try {
        const response = await apiClient.put(`/leaves/${leaveId}/reject`, { rejectionReason: reason });
        if (response && response.success) {
          notificationStore.success('Leave request rejected.');
          container.querySelector('#reject-modal').style.display = 'none';
          await this.loadData(); this.render(container); this.bindEvents(container, lifecycle);
        } else {
          notificationStore.danger(response?.message || 'Rejection failed.');
        }
      } catch (err) {
        notificationStore.danger(err.message || 'Rejection failed.');
      }
    });

    container.querySelector('#reject-modal-close')?.addEventListener('click', () => {
      container.querySelector('#reject-modal').style.display = 'none';
    });

    // Approve cancellation
    container.querySelectorAll('.btn-approve-cancellation').forEach(btn => {
      btn.addEventListener('click', async () => {
        try {
          const response = await apiClient.put(`/leaves/${btn.dataset.id}/approve-cancellation`, {});
          if (response && response.success) {
            notificationStore.success('Leave cancellation approved. Balance restored.');
            await this.loadData(); this.render(container); this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(response?.message || 'Failed to approve cancellation.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to approve cancellation.');
        }
      });
    });
  }

  async doApprove(leaveId, comment, container, lifecycle) {
    try {
      const response = await apiClient.put(`/leaves/${leaveId}/approve`, { comment });
      if (response && response.success) {
        notificationStore.success('Leave approved. Balance updated.');
        await this.loadData(); this.render(container); this.bindEvents(container, lifecycle);
      } else {
        notificationStore.danger(response?.message || 'Approval failed.');
      }
    } catch (err) {
      notificationStore.danger(err.message || 'Approval failed.');
    }
  }
}
