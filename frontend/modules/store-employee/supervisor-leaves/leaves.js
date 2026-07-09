/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module - Shift Supervisor Sub-Role
 * File              : leaves.js
 * Purpose           : Standalone Barista Leave Approvals Hub for Supervisor
 * Version           : 1.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';
import { apiClient } from '../../../api/client.js';

export default class StoreSupervisorLeaves {
  constructor() {
    this.user = authStore.getUser();
    this.pendingLeaves = [];
    this.cancellationRequests = [];
  }

  async mount(container, lifecycle) {
    logger.info('StoreSupervisorLeaves', 'Mounting supervisor leave approvals...');
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
      const pendingRes = await safeGet('/leaves/pending');
      if (pendingRes && pendingRes.success) {
        this.pendingLeaves = pendingRes.data?.pending || [];
        this.cancellationRequests = pendingRes.data?.cancellationRequests || [];
      } else {
        this.pendingLeaves = [];
        this.cancellationRequests = [];
      }
    } catch (err) {
      logger.error('StoreSupervisorLeaves', 'Failed to load pending leaves', err);
    }
  }

  render(container, loading = false) {
    if (loading) {
      container.innerHTML = `
        <div style="display:flex;align-items:center;justify-content:center;height:400px;flex-direction:column;gap:12px;">
          <i data-lucide="loader-2" style="width:32px;height:32px;color:var(--accent-primary);animation:spin 1s linear infinite;"></i>
          <span style="color:var(--text-muted);font-size:0.8rem;font-weight:600;">Loading time off requests...</span>
        </div>`;
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    const pendingListHtml = this.pendingLeaves.map(req => `
      <div class="leave-approval-card card glass" style="padding:var(--spacing-md);border-radius:var(--radius-md);border:1px solid var(--border-color);background:rgba(201,164,106,0.04);display:flex;flex-direction:column;gap:var(--spacing-sm);text-align:left;">
        <div style="display:flex;justify-content:space-between;align-items:center;gap:8px;">
          <strong style="color:var(--text-primary);font-size:0.85rem;">${req.employeeName}</strong>
          <span style="font-size:0.6rem;font-weight:700;background:rgba(255,255,255,0.05);padding:2px 8px;border-radius:4px;color:var(--accent-primary);text-transform:uppercase;">
            ${req.leaveTypeCode}
          </span>
        </div>

        <div style="font-size:0.72rem;color:var(--text-muted);line-height:1.4;margin:4px 0;">
          <div><strong>Duration:</strong> ${req.totalDays} day(s) (${req.startDate} to ${req.endDate})</div>
          <div style="margin-top:4px;font-style:italic;color:var(--text-primary);">"${req.reason || 'No reason specified'}"</div>
          ${!req.hasDocument && req.requiresDocument ? `<div style="margin-top:6px;color:var(--status-danger);font-weight:700;display:flex;align-items:center;gap:4px;"><i data-lucide="alert-circle" style="width:12px;height:12px;"></i> Medical Certificate Required</div>` : ''}
        </div>

        <div style="display:flex;flex-direction:column;gap:4px;margin-top:2px;">
          <input type="text" class="supervisor-comment-input" data-id="${req.id}" placeholder="Optional approval comment..." style="background:rgba(0,0,0,0.3);border:1px solid var(--border-color);border-radius:var(--radius-sm);color:var(--text-primary);padding:6px 8px;font-size:0.7rem;outline:none;font-family:inherit;">
        </div>

        <div style="display:flex;gap:8px;margin-top:6px;">
          <button class="btn btn-approve-leave" data-id="${req.id}" style="flex:1;background:var(--status-success);color:#000;font-weight:800;border:none;padding:8px;border-radius:4px;font-size:0.7rem;cursor:pointer;${(!req.hasDocument && req.requiresDocument) ? 'opacity:0.4;pointer-events:none;' : ''}">
            Approve
          </button>
          <button class="btn btn-reject-leave" data-id="${req.id}" style="flex:1;background:var(--status-danger);color:#fff;font-weight:800;border:none;padding:8px;border-radius:4px;font-size:0.7rem;cursor:pointer;${req.isProtected ? 'opacity:0.4;pointer-events:none;' : ''}" ${req.isProtected ? 'disabled' : ''}>
            Reject
          </button>
        </div>
      </div>
    `).join('');

    const cancellationListHtml = this.cancellationRequests.map(req => `
      <div class="leave-approval-card card glass" style="padding:var(--spacing-md);border-radius:var(--radius-md);border:1px solid var(--border-color);background:rgba(239,68,68,0.04);display:flex;flex-direction:column;gap:var(--spacing-sm);text-align:left;">
        <div style="display:flex;justify-content:space-between;align-items:center;gap:8px;">
          <strong style="color:var(--text-primary);font-size:0.85rem;">${req.employeeName}</strong>
          <span style="font-size:0.58rem;font-weight:700;background:rgba(239,68,68,0.1);padding:2px 8px;border-radius:4px;color:var(--status-danger);text-transform:uppercase;">
            Cancel: ${req.leaveTypeCode}
          </span>
        </div>

        <div style="font-size:0.72rem;color:var(--text-muted);line-height:1.4;margin:4px 0;">
          <div><strong>Duration:</strong> ${req.totalDays} day(s) (${req.startDate} to ${req.endDate})</div>
          <div style="margin-top:4px;font-style:italic;color:var(--text-primary);">Reason: "${req.cancellationReason || 'No reason specified'}"</div>
        </div>

        <div style="display:flex;gap:8px;margin-top:6px;">
          <button class="btn btn-approve-cancel" data-id="${req.id}" style="width:100%;background:var(--status-danger);color:#fff;font-weight:800;border:none;padding:8px;border-radius:4px;font-size:0.7rem;cursor:pointer;">
            Approve Cancellation
          </button>
        </div>
      </div>
    `).join('');

    container.innerHTML = `
      <div class="workspace-container animate-fade-in" style="padding:var(--spacing-lg);display:flex;flex-direction:column;gap:var(--spacing-lg);max-width:1400px;margin:0 auto;">

        <!-- Header -->
        <div class="card glass" style="padding:var(--spacing-md) var(--spacing-lg);border-radius:var(--radius-lg);background:var(--bg-card);border:1px solid var(--border-color);display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:var(--spacing-md);">
          <div>
            <h2 style="font-family:var(--font-display);font-weight:800;font-size:1.5rem;color:var(--text-primary);margin:0;">Time Off Requests</h2>
            <p style="font-size:0.7rem;color:var(--text-muted);margin:2px 0 0 0;text-transform:uppercase;font-weight:600;letter-spacing:.5px;">
              Barista Approvals Hub &nbsp;·&nbsp; <span style="color:var(--accent-primary);">Supervisor Dashboard</span>
            </p>
          </div>
          <div style="display:flex;align-items:center;gap:8px;">
            <span style="background:rgba(130,163,125,0.12);border:1px solid rgba(130,163,125,0.3);border-radius:var(--radius-full);padding:4px 12px;font-size:0.7rem;font-weight:700;color:var(--status-success);">
              ● Active
            </span>
          </div>
        </div>

        <!-- Approvals Section -->
        <div class="card glass" style="padding:var(--spacing-lg);border-radius:var(--radius-lg);border:1px solid var(--border-color);background:var(--bg-card);display:flex;flex-direction:column;gap:var(--spacing-md);text-align:left;width:100%;">
          <div style="border-bottom:1px solid rgba(255,255,255,0.05);padding-bottom:var(--spacing-xs);display:flex;justify-content:space-between;align-items:center;">
            <h3 style="font-family:var(--font-display);font-weight:800;font-size:1.1rem;margin:0;color:var(--accent-primary);">Pending Barista Time Off Requests Awaiting Your Approval</h3>
            <span style="background:rgba(239,68,68,0.1);border:1px solid rgba(239,68,68,0.3);border-radius:var(--radius-full);padding:2px 10px;font-size:0.65rem;font-weight:700;color:var(--status-danger);">
              ${this.pendingLeaves.length + this.cancellationRequests.length} Pending
            </span>
          </div>

          ${(this.pendingLeaves.length === 0 && this.cancellationRequests.length === 0) ? `
            <div style="display:flex;flex-direction:column;align-items:center;justify-content:center;padding:50px 20px;color:var(--text-muted);border:1.5px dashed rgba(255,255,255,0.05);border-radius:var(--radius-md);text-align:center;">
              <i data-lucide="check-circle" style="width:36px;height:36px;margin-bottom:8px;opacity:0.3;color:var(--status-success);"></i>
              <span style="font-weight:600;font-size:0.85rem;color:var(--text-primary);">All Barista Requests Signed Off</span>
              <span style="font-size:0.68rem;margin-top:2px;">No pending leave applications or cancellation requests to process.</span>
            </div>
          ` : `
            <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(300px,1fr));gap:var(--spacing-md);width:100%;">
              ${pendingListHtml}
              ${cancellationListHtml}
            </div>
          `}
        </div>

      </div>

      <!-- Reject Modal -->
      <div id="reject-modal" style="display:none;position:fixed;inset:0;background:rgba(0,0,0,0.7);z-index:9999;align-items:center;justify-content:center;backdrop-filter:blur(4px);">
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
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    // 1. Approve Leave
    container.querySelectorAll('.btn-approve-leave').forEach(btn => {
      btn.addEventListener('click', async () => {
        const leaveId = btn.getAttribute('data-id');
        const commentInput = container.querySelector(`.supervisor-comment-input[data-id="${leaveId}"]`);
        const comment = commentInput?.value?.trim() || '';
        try {
          const response = await apiClient.put(`/leaves/${leaveId}/approve`, { comment });
          if (response && response.success) {
            notificationStore.success('Time-off application approved successfully.');
            await this.loadData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(response?.message || 'Failed to approve leave request.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to approve leave.');
        }
      });
    });

    // 2. Reject Leave Open Modal
    container.querySelectorAll('.btn-reject-leave').forEach(btn => {
      btn.addEventListener('click', () => {
        const leaveId = btn.getAttribute('data-id');
        const modal = container.querySelector('#reject-modal');
        container.querySelector('#reject-modal-submit').dataset.leaveId = leaveId;
        container.querySelector('#reject-reason-input').value = '';
        modal.style.display = 'flex';
      });
    });

    // 3. Reject Modal submit
    container.querySelector('#reject-modal-submit')?.addEventListener('click', async () => {
      const leaveId = container.querySelector('#reject-modal-submit').dataset.leaveId;
      const reason = container.querySelector('#reject-reason-input')?.value?.trim();
      if (!reason || reason.length < 10) {
        notificationStore.danger('Rejection reason must be at least 10 characters.');
        return;
      }
      try {
        const response = await apiClient.put(`/leaves/${leaveId}/reject`, { rejectionReason: reason });
        if (response && response.success) {
          notificationStore.success('Time-off application rejected.');
          container.querySelector('#reject-modal').style.display = 'none';
          await this.loadData();
          this.render(container);
          this.bindEvents(container, lifecycle);
        } else {
          notificationStore.danger(response?.message || 'Failed to reject leave request.');
        }
      } catch (err) {
        notificationStore.danger('Network error rejecting leave.');
      }
    });

    container.querySelector('#reject-modal-close')?.addEventListener('click', () => {
      container.querySelector('#reject-modal').style.display = 'none';
    });

    // 4. Approve Cancellation
    container.querySelectorAll('.btn-approve-cancel').forEach(btn => {
      btn.addEventListener('click', async () => {
        const leaveId = btn.getAttribute('data-id');
        try {
          const response = await apiClient.put(`/leaves/${leaveId}/approve-cancellation`, {});
          if (response && response.success) {
            notificationStore.success('Cancellation request approved successfully.');
            await this.loadData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(response?.message || 'Failed to approve cancellation request.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to approve cancellation.');
        }
      });
    });
  }
}
