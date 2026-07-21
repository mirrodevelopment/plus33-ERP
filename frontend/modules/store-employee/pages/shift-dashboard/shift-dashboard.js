/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module – Shift Supervisor Sub-Role
 * File              : shift-dashboard.js
 * Path              : frontend/modules/store-employee/pages/shift-dashboard/shift-dashboard.js
 * Purpose           : Controller — loads shift-dashboard.html + .css, populates
 *                     dynamic content, and wires all interaction events.
 * Version           : 2.0.0
 ******************************************************************************/

import { authStore }          from '../../../../store/authStore.js';
import { userStore }          from '../../../../store/userStore.js';
import { notificationStore }  from '../../../../store/notificationStore.js';
import { logger }             from '../../../../core/logger.js';
import { apiClient }          from '../../../../api/client.js';
import { htmlLoader }         from '../../../../core/htmlLoader.js';

const TEMPLATE_URL = 'modules/store-employee/pages/shift-dashboard/shift-dashboard.html';
const CSS_ID       = 'shift-dashboard-page-css';
const CSS_HREF     = 'modules/store-employee/pages/shift-dashboard/shift-dashboard.css';

// ─── Shift colour palette ────────────────────────────────────────────────────
const SHIFT_COLORS = {
  SHIFT_MORN: { bg: 'rgba(255,193,7,0.12)',   border: '#ffc107', icon: '☀️'  },
  SHIFT_AFT:  { bg: 'rgba(255,152,0,0.12)',   border: '#ff9800', icon: '🌤️' },
  SHIFT_NGHT: { bg: 'rgba(103,58,183,0.12)',  border: '#673ab7', icon: '🌙'  },
  DEFAULT:    { bg: 'rgba(201,164,106,0.12)', border: 'var(--accent-primary)', icon: '⏰' },
};

export default class ShiftDashboard {

  constructor() {
    this.user           = authStore.getUser();
    this.profile        = userStore.getProfile(this.user?.role) || {};
    this.employees      = [];
    this.shifts         = [];
    this.assignments    = [];
    this.pendingSwaps   = [];
    this.awayPermissions = [];  // pending away pass requests from employees
    this.ongoingAwayPermissions = []; // ongoing away passes from employees
    this.overtimeRequests = []; // pending overtime requests from employees
    this.storeEmployees = [];   // for the replacement employee picker
    this.baseDate       = new Date();
    this.baseDate.setHours(0, 0, 0, 0);

    // Active swap ID tracked across modal open / confirm
    this._activeSwapId    = null;
    this._activeRejectId  = null;
    this._activeAwayId    = null;  // away permission being approved/denied
  }

  // ─── Lifecycle ─────────────────────────────────────────────────────────────

  async mount(container, lifecycle) {
    logger.info('ShiftDashboard', 'Mounting Shift Planner...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);
    await this.fetchData();
    this._populate(container);
    this._bindEvents(container, lifecycle);
  }

  // ─── CSS Loader ────────────────────────────────────────────────────────────

  _loadCss() {
    if (!document.getElementById(CSS_ID)) {
      const link = document.createElement('link');
      link.id   = CSS_ID;
      link.rel  = 'stylesheet';
      link.href = CSS_HREF;
      document.head.appendChild(link);
    }
  }

  // ─── Data Fetching ─────────────────────────────────────────────────────────

  async fetchData() {
    try {
      const [empRes, shiftRes, assignRes, swapRes, awayRes, ongoingAwayRes, otRes] = await Promise.all([
        apiClient.get('/api/v1/shift-assignments/store-employees'),
        apiClient.get('/api/v1/shifts'),
        apiClient.get('/api/v1/shift-assignments'),
        apiClient.get('/api/v1/shift-swaps/store-requests'),
        apiClient.get('/api/v1/away-permission/pending').catch(() => null),
        apiClient.get('/api/v1/away-permission/ongoing').catch(() => null),
        apiClient.get('/api/v1/overtime-requests/pending').catch(() => null)
      ]);

      this.employees   = empRes?.success   ? empRes.data   : [];
      this.shifts      = shiftRes?.success ? shiftRes.data : [];
      this.assignments = assignRes?.success ? assignRes.data : [];

      const swapData      = swapRes?.success ? swapRes.data : {};
      this.pendingSwaps   = Array.isArray(swapData) ? swapData : (swapData.swaps || []);
      this.storeEmployees = swapData.storeEmployees || [];

      this.awayPermissions = awayRes?.success ? (awayRes.data || []) : [];
      this.ongoingAwayPermissions = ongoingAwayRes?.success ? (ongoingAwayRes.data || []) : [];
      this.overtimeRequests = otRes?.success ? (otRes.data || []) : [];
    } catch (e) {
      logger.error('ShiftDashboard', 'Error fetching data:', e);
      this.employees = this.shifts = this.assignments = this.pendingSwaps = this.storeEmployees = [];
      this.awayPermissions = [];
      this.ongoingAwayPermissions = [];
      this.overtimeRequests = [];
    }
  }

  // ─── Date helpers ──────────────────────────────────────────────────────────

  _getDayDates() {
    return [0, 1, 2].map(i => {
      const d = new Date(this.baseDate);
      d.setDate(d.getDate() + i);
      return d;
    });
  }

  _fmt(d)        { return d.toISOString().split('T')[0]; }
  _fmtDisplay(d) { return d.toLocaleDateString('en-GB', { weekday: 'short', day: '2-digit', month: 'short' }); }
  _isToday(d)    { const t = new Date(); return d.getDate()===t.getDate() && d.getMonth()===t.getMonth() && d.getFullYear()===t.getFullYear(); }
  _isPast(d)     { const t = new Date(); t.setHours(0,0,0,0); const c = new Date(d); c.setHours(0,0,0,0); return c < t; }

  _getColor(code) {
    return SHIFT_COLORS[code] || SHIFT_COLORS.DEFAULT;
  }

  _getAssigned(shiftId, dateStr) {
    return this.assignments.filter(a => {
      if (a.shiftId !== shiftId) return false;
      const from = a.effectiveFrom;
      const to   = a.effectiveTo || from;
      return dateStr >= from && dateStr <= to;
    });
  }

  // ─── DOM population ────────────────────────────────────────────────────────

  _populate(container) {
    this._populateHeader(container);
    this._populateAssignForm(container);
    this._populateCalendar(container);
    this._populateSwapsTable(container);
    this._populateApprovalModalSelects(container);
    this._populateAwayPermissions(container);
    this._populateOvertimeRequests(container);
  }

  _populateHeader(container) {
    const badge = container.querySelector('#sd-team-count');
    if (badge) badge.textContent = this.employees.length;
  }

  _populateAssignForm(container) {
    const today = this._fmt(this.baseDate);

    // Employee select
    const empSel = container.querySelector('#select-employee');
    if (empSel) {
      empSel.innerHTML = '<option value="">-- Select Employee --</option>' +
        this.employees.map(e =>
          `<option value="${e.id}">${e.firstName} ${e.lastName} (${e.employeeCode})</option>`
        ).join('');
    }

    // Shift select
    const shiftSel = container.querySelector('#select-shift');
    if (shiftSel) {
      shiftSel.innerHTML = '<option value="">-- Select Shift --</option>' +
        this.shifts.filter(s => s.active).map(s => {
          const cap = s.maxEmployees ? ` [Max Staff: ${s.maxEmployees}]` : '';
          const type = s.shiftType ? ` [${s.shiftType}]` : '';
          return `<option value="${s.id}">${s.name}${type} (${s.startTime} – ${s.endTime})${cap}</option>`;
        }).join('');
    }


    // Date input — default to today, minimum = today
    const dateInput = container.querySelector('#input-start-date');
    if (dateInput) {
      dateInput.value = today;
      dateInput.min   = today;
    }
  }

  _populateCalendar(container) {
    const days  = this._getDayDates();
    const thead = container.querySelector('#sd-calendar-thead');
    const tbody = container.querySelector('#sd-calendar-tbody');
    if (!thead || !tbody) return;

    // Header row
    const headerCells = days.map(d => {
      const today  = this._isToday(d);
      const dotHtml = today ? '<span style="display:inline-block;width:6px;height:6px;border-radius:50%;background:var(--accent-primary);margin-right:4px;"></span>' : '';
      return `<th class="sd-th" style="text-align:center;${today ? 'color:var(--accent-primary);background:rgba(201,164,106,0.05);' : 'color:var(--text-secondary);'}">${dotHtml}${this._fmtDisplay(d)}</th>`;
    }).join('');
    thead.innerHTML = `<tr><th class="sd-th sd-th--left" style="width:140px;">Shift</th>${headerCells}</tr>`;

    // Body rows
    const activeShifts = this.shifts.filter(s => s.active);
    if (activeShifts.length === 0) {
      tbody.innerHTML = `<tr><td class="sd-td" colspan="${days.length + 1}" style="text-align:center;color:var(--text-muted);font-style:italic;padding:24px;">No active shifts defined.</td></tr>`;
      return;
    }

    tbody.innerHTML = activeShifts.map(shift => {
      const sc = this._getColor(shift.code);
      const dateCells = days.map(d => {
        const dateStr  = this._fmt(d);
        const assigned = this._getAssigned(shift.id, dateStr);
        const bgStyle  = this._isToday(d) ? 'background:rgba(201,164,106,0.03);' : '';
        const pills    = assigned.length === 0
          ? `<span class="sd-no-assignment">No assignment</span>`
          : assigned.map(a => {
              const removeBtn = this._isPast(d) ? '' :
                `<button type="button" class="sd-btn-remove btn-remove-assignment"
                   data-employee-id="${a.employeeId}"
                   data-shift-id="${a.shiftId}"
                   data-effective-from="${a.effectiveFrom}"
                   title="Remove">✕</button>`;
              return `<div class="sd-assignment-pill" style="background:${sc.bg};border:1px solid ${sc.border}20;">
                        <span class="sd-assignment-name" title="${a.employeeName.trim()}">${a.employeeName.trim()}</span>
                        ${removeBtn}
                      </div>`;
            }).join('');
        return `<td class="sd-td" style="${bgStyle}"><div style="display:flex;flex-direction:column;gap:4px;min-height:40px;">${pills}</div></td>`;
      }).join('');

      return `<tr>
        <td class="sd-td">
          <div style="display:flex;align-items:center;gap:8px;">
            <span style="font-size:1.1rem;">${sc.icon}</span>
            <div>
              <div style="font-weight:700;color:var(--text-primary);font-size:0.78rem;">${shift.name}</div>
              <div style="font-size:0.65rem;color:var(--text-muted);">${shift.startTime} – ${shift.endTime}</div>
            </div>
          </div>
        </td>
        ${dateCells}
      </tr>`;
    }).join('');

    // Show/hide empty state
    const emptyState = container.querySelector('#sd-no-assignments');
    if (emptyState) emptyState.style.display = this.assignments.length === 0 ? 'block' : 'none';
  }

  _populateSwapsTable(container) {
    const tbody = container.querySelector('#sd-swaps-tbody');
    if (!tbody) return;

    if ((this.pendingSwaps || []).length === 0) {
      tbody.innerHTML = `<tr><td class="sd-td" colspan="6" style="text-align:center;color:var(--text-muted);font-style:italic;padding:24px;">No pending swap requests</td></tr>`;
      return;
    }

    tbody.innerHTML = this.pendingSwaps.map(sw => `
      <tr>
        <td class="sd-td sd-td--name">${sw.employeeName}</td>
        <td class="sd-td sd-td--muted">${sw.shiftDateDisplay || sw.shiftDate}</td>
        <td class="sd-td sd-td--muted">${sw.currentShiftName}</td>
        <td class="sd-td sd-td--accent">${sw.preferredShiftName}</td>
        <td class="sd-td sd-td--success">${sw.preferredDateDisplay || sw.preferredDate || '<span style="color:var(--text-muted);font-style:italic;">Not specified</span>'}</td>
        <td class="sd-td sd-td--right">
          <button type="button" class="sd-btn-approve btn-approve-swap"
            data-id="${sw.id}"
            data-employee-name="${sw.employeeName}"
            data-shift-date="${sw.shiftDate}"
            data-shift-date-display="${sw.shiftDateDisplay || sw.shiftDate}"
            data-current-shift="${sw.currentShiftName}"
            data-preferred-shift-id="${sw.preferredShiftId}"
            data-preferred-shift-name="${sw.preferredShiftName}"
            data-preferred-date="${sw.preferredDate || ''}">Approve</button>
          <button type="button" class="sd-btn-reject btn-reject-swap"
            data-id="${sw.id}">Reject</button>
        </td>
      </tr>
    `).join('');
  }

  _populateApprovalModalSelects(container) {
    // Shift override select
    const shiftSel = container.querySelector('#modal-approved-shift');
    if (shiftSel) {
      shiftSel.innerHTML = '<option value="">-- Keep employee\'s preferred shift --</option>' +
        this.shifts.filter(s => s.active).map(s =>
          `<option value="${s.id}">${s.name} (${s.startTime} – ${s.endTime})</option>`
        ).join('');
    }

    // Replacement employee select
    const replSel = container.querySelector('#modal-replacement-employee');
    if (replSel) {
      replSel.innerHTML = '<option value="">-- No replacement (leave slot empty) --</option>' +
        this.storeEmployees.map(e =>
          `<option value="${e.id}">${e.name} (${e.employeeCode})</option>`
        ).join('');
    }
  }

  // ─── Event binding ─────────────────────────────────────────────────────────

  _bindEvents(container, lifecycle) {
    this._bindAssignForm(container, lifecycle);
    this._bindRemoveBtns(container, lifecycle);
    this._bindApprovalModal(container, lifecycle);
    this._bindRejectModal(container, lifecycle);
    this._bindAwayPermissionButtons(container, lifecycle);
    this._bindOvertimeRequestButtons(container, lifecycle);
  }

  /** Quick-assign form submit */
  _bindAssignForm(container, lifecycle) {
    const form = container.querySelector('#form-assign-shift');
    if (!form) return;

    const handler = async (e) => {
      e.preventDefault();
      const employeeId = container.querySelector('#select-employee').value;
      const shiftId    = container.querySelector('#select-shift').value;
      const startDate  = container.querySelector('#input-start-date').value;

      if (!employeeId || !shiftId || !startDate) {
        notificationStore.danger('Please select an employee, shift, and start date.');
        return;
      }

      try {
        const res = await apiClient.post('/api/v1/shift-assignments', {
          employeeId: parseInt(employeeId),
          shiftId:    parseInt(shiftId),
          startDate,
        });
        if (res?.success) {
          notificationStore.success('Shift assigned successfully!');
          await this._reload(container, lifecycle);
        } else {
          notificationStore.danger(res?.message || 'Failed to assign shift.');
        }
      } catch (err) {
        logger.error('ShiftDashboard', 'Error assigning shift:', err);
        notificationStore.danger('Error assigning shift: ' + (err.message || 'Unknown error'));
      }
    };

    form.addEventListener('submit', handler);
    if (lifecycle) lifecycle.onCleanup(() => form.removeEventListener('submit', handler));
  }

  /** Remove assignment ✕ buttons in the calendar */
  _bindRemoveBtns(container, lifecycle) {
    container.querySelectorAll('.btn-remove-assignment').forEach(btn => {
      const handler = async () => {
        const employeeId    = btn.getAttribute('data-employee-id');
        const shiftId       = btn.getAttribute('data-shift-id');
        const effectiveFrom = btn.getAttribute('data-effective-from');

        if (!confirm('Remove this shift assignment?')) return;

        try {
          const res = await apiClient.delete(`/api/v1/shift-assignments/${employeeId}/${shiftId}/${effectiveFrom}`);
          if (res?.success) {
            notificationStore.success('Shift assignment removed.');
            await this._reload(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to remove assignment.');
          }
        } catch (err) {
          logger.error('ShiftDashboard', 'Error removing assignment:', err);
          notificationStore.danger('Error removing assignment.');
        }
      };

      btn.addEventListener('click', handler);
      if (lifecycle) lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
    });
  }

  /** Swap Approval Modal */
  _bindApprovalModal(container, lifecycle) {
    const overlay    = container.querySelector('#swap-approval-overlay');
    const closeModal = () => { if (overlay) overlay.classList.remove('is-open'); };

    // Close triggers
    [
      container.querySelector('#btn-close-swap-modal'),
      container.querySelector('#btn-modal-cancel'),
    ].forEach(el => {
      if (!el) return;
      const h = () => closeModal();
      el.addEventListener('click', h);
      if (lifecycle) lifecycle.onCleanup(() => el.removeEventListener('click', h));
    });

    if (overlay) {
      const bgClose = (e) => { if (e.target === overlay) closeModal(); };
      overlay.addEventListener('click', bgClose);
      if (lifecycle) lifecycle.onCleanup(() => overlay.removeEventListener('click', bgClose));
    }

    // Open modal on Approve click
    container.querySelectorAll('.btn-approve-swap').forEach(btn => {
      const handler = () => {
        this._activeSwapId = btn.getAttribute('data-id');

        const employeeName     = btn.getAttribute('data-employee-name');
        const shiftDateDisplay = btn.getAttribute('data-shift-date-display');
        const currentShift     = btn.getAttribute('data-current-shift');
        const preferredShiftName = btn.getAttribute('data-preferred-shift-name');
        const preferredDate    = btn.getAttribute('data-preferred-date');

        // Populate summary card
        const summary = container.querySelector('#swap-modal-summary');
        if (summary) {
          summary.innerHTML = `
            <div style="font-weight:700;color:var(--text-primary);margin-bottom:8px;font-size:0.85rem;">${employeeName}</div>
            <div style="display:grid;grid-template-columns:1fr 1fr;gap:8px;font-size:0.78rem;">
              <div><span style="color:var(--text-muted);">Vacating shift:</span><br>
                <strong>${currentShift}</strong> on <strong>${shiftDateDisplay}</strong></div>
              <div><span style="color:var(--text-muted);">Requested new shift:</span><br>
                <strong>${preferredShiftName}</strong> on <strong>${preferredDate || 'Not specified'}</strong></div>
            </div>`;
        }

        // Vacancy info
        const vacInfo = container.querySelector('#swap-modal-vacancy-info');
        if (vacInfo) {
          vacInfo.textContent = `After approval, ${currentShift} on ${shiftDateDisplay} will be empty. Assign a replacement worker below.`;
        }

        // Pre-fill date
        const dateInput = container.querySelector('#modal-approved-date');
        if (dateInput) {
          dateInput.value = preferredDate || '';
          const today = new Date(); today.setHours(0,0,0,0);
          const min   = new Date(today); min.setDate(min.getDate() + 1);
          dateInput.min = min.toISOString().split('T')[0];
        }

        // Reset overrides
        const shiftSel = container.querySelector('#modal-approved-shift');
        if (shiftSel) shiftSel.value = '';
        const replSel  = container.querySelector('#modal-replacement-employee');
        if (replSel)  replSel.value  = '';

        if (overlay) overlay.classList.add('is-open');
      };

      btn.addEventListener('click', handler);
      if (lifecycle) lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
    });

    // Confirm Approval
    const confirmBtn = container.querySelector('#btn-modal-confirm-approve');
    if (confirmBtn) {
      const handler = async () => {
        if (!this._activeSwapId) return;

        const approvedDate    = container.querySelector('#modal-approved-date')?.value       || '';
        const approvedShiftId = container.querySelector('#modal-approved-shift')?.value      || '';
        const replacementId   = container.querySelector('#modal-replacement-employee')?.value || '';

        const payload = {};
        if (approvedDate)    payload.approvedDate           = approvedDate;
        if (approvedShiftId) payload.approvedShiftId        = parseInt(approvedShiftId);
        if (replacementId)   payload.replacementEmployeeId  = parseInt(replacementId);

        confirmBtn.disabled    = true;
        confirmBtn.textContent = 'Approving…';
        try {
          const res = await apiClient.post(`/api/v1/shift-swaps/${this._activeSwapId}/approve`, payload);
          if (res?.success) {
            closeModal();
            notificationStore.success('Swap approved — roster updated successfully.');
            await this._reload(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to approve swap request.');
            confirmBtn.disabled = false;
            confirmBtn.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg> Confirm Approval';
          }
        } catch (err) {
          logger.error('ShiftDashboard', 'Error approving swap:', err);
          notificationStore.danger('Error approving swap: ' + (err.message || 'Unknown error'));
          confirmBtn.disabled = false;
          confirmBtn.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg> Confirm Approval';
        }
      };

      confirmBtn.addEventListener('click', handler);
      if (lifecycle) lifecycle.onCleanup(() => confirmBtn.removeEventListener('click', handler));
    }
  }

  /** Swap Reject Modal */
  _bindRejectModal(container, lifecycle) {
    const overlay    = container.querySelector('#reject-modal-overlay');
    const closeModal = () => {
      if (overlay) overlay.classList.remove('is-open');
      const ta = container.querySelector('#reject-reason-input');
      if (ta) ta.value = '';
      this._activeRejectId = null;
    };

    // Close triggers
    [
      container.querySelector('#btn-close-reject-modal'),
      container.querySelector('#btn-reject-cancel'),
    ].forEach(el => {
      if (!el) return;
      const h = () => closeModal();
      el.addEventListener('click', h);
      if (lifecycle) lifecycle.onCleanup(() => el.removeEventListener('click', h));
    });

    if (overlay) {
      const bgClose = (e) => { if (e.target === overlay) closeModal(); };
      overlay.addEventListener('click', bgClose);
      if (lifecycle) lifecycle.onCleanup(() => overlay.removeEventListener('click', bgClose));
    }

    // Open modal on Reject click
    container.querySelectorAll('.btn-reject-swap').forEach(btn => {
      const handler = () => {
        this._activeRejectId = btn.getAttribute('data-id');
        const ta = container.querySelector('#reject-reason-input');
        if (ta) ta.value = '';
        if (overlay) overlay.classList.add('is-open');
        setTimeout(() => ta?.focus(), 80);
      };

      btn.addEventListener('click', handler);
      if (lifecycle) lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
    });

    // Confirm Rejection
    const confirmBtn = container.querySelector('#btn-reject-confirm');
    if (confirmBtn) {
      const handler = async () => {
        if (!this._activeRejectId) return;

        const reason = (container.querySelector('#reject-reason-input')?.value || '').trim();
        if (!reason) {
          notificationStore.danger('Rejection reason is mandatory.');
          return;
        }

        confirmBtn.disabled    = true;
        confirmBtn.textContent = 'Rejecting…';
        try {
          const res = await apiClient.post(`/api/v1/shift-swaps/${this._activeRejectId}/reject`, { reason });
          if (res?.success) {
            closeModal();
            notificationStore.success('Swap request rejected.');
            await this._reload(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to reject swap request.');
            confirmBtn.disabled = false;
            confirmBtn.textContent = 'Confirm Rejection';
          }
        } catch (err) {
          logger.error('ShiftDashboard', 'Error rejecting swap:', err);
          notificationStore.danger('Error rejecting swap request.');
          confirmBtn.disabled = false;
          confirmBtn.textContent = 'Confirm Rejection';
        }
      };

      confirmBtn.addEventListener('click', handler);
      if (lifecycle) lifecycle.onCleanup(() => confirmBtn.removeEventListener('click', handler));
    }
  }

  // ─── Reload helper ─────────────────────────────────────────────────────────

  /** Re-fetch data and refresh just the dynamic parts (no full re-inject of HTML). */
  async _reload(container, lifecycle) {
    await this.fetchData();
    this._populate(container);

    // Re-bind event listeners on the newly-rendered rows/buttons
    this._bindRemoveBtns(container, lifecycle);

    // Re-bind approve/reject swap buttons (the tbody was replaced)
    this._bindApprovalModal(container, lifecycle);
    this._bindRejectModal(container, lifecycle);
    this._bindAwayPermissionButtons(container, lifecycle);
    this._bindOvertimeRequestButtons(container, lifecycle);
  }

  // ─── Away Permission Section ───────────────────────────────────────────────

  /**
   * Renders the away permission request cards inside #sd-away-permissions-list.
   * If the container element does not exist in the HTML template, this is a no-op.
   */
  _populateAwayPermissions(container) {
    const list = container.querySelector('#sd-away-permissions-list');
    const badge = container.querySelector('#sd-away-badge');
    if (!list) return;

    const count = this.awayPermissions.length;
    if (badge) {
      badge.textContent = count;
      badge.style.display = count > 0 ? 'inline-flex' : 'none';
    }

    if (count === 0) {
      list.innerHTML = `
        <div style="text-align:center; color:var(--text-muted); font-size:0.78rem; font-style:italic; padding:16px;">
          No pending away pass requests right now.
        </div>`;
    } else {
      list.innerHTML = this.awayPermissions.map(req => {
        const statusColor = req.status === 'EXTENSION_REQUESTED' ? '#f59e0b' : 'var(--accent-primary)';
        const tag = req.status === 'EXTENSION_REQUESTED' ? '🔁 Extension Requested' : '⏸ Away Pass Requested';
        const since = req.requestedAt ? new Date(req.requestedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '--';
        return `
          <div class="sd-away-card" data-away-id="${req.id}" style="
            background:rgba(255,255,255,0.03);
            border:1px solid var(--border-color);
            border-left: 3px solid ${statusColor};
            border-radius:var(--radius-md,8px);
            padding:12px 14px;
            display:flex;
            flex-direction:column;
            gap:8px;
            margin-bottom:8px;
          ">
            <div style="display:flex; justify-content:space-between; align-items:center;">
              <span style="font-size:0.8rem; font-weight:700; color:var(--text-primary);">${req.employeeName || 'Employee'}</span>
              <span style="font-size:0.68rem; color:${statusColor}; font-weight:600;">${tag}</span>
            </div>
            <div style="font-size:0.74rem; color:var(--text-secondary); display:flex; flex-direction:column; gap:2px;">
              ${req.reason ? `<div><strong>Original Reason:</strong> <em>${req.reason}</em></div>` : ''}
              ${req.status === 'EXTENSION_REQUESTED' && req.extensionReason ? `<div><strong>Extension Request:</strong> <em style="color:var(--accent-warning, #ff9800);">${req.extensionReason}</em></div>` : ''}
            </div>
            <div style="font-size:0.68rem; color:var(--text-muted);">Requested at ${since} · Store: ${req.storeName || '--'}</div>
            <div style="display:flex; gap:8px; margin-top:4px;">
              <button type="button" class="btn btn-primary btn-away-approve" data-away-id="${req.id}"
                style="flex:1; padding:6px 10px; font-size:0.75rem;">
                ✓ Approve Pass
              </button>
              <button type="button" class="btn btn-secondary btn-away-deny" data-away-id="${req.id}"
                style="flex:1; padding:6px 10px; font-size:0.75rem;">
                ✕ Deny
              </button>
            </div>
          </div>
        `;
      }).join('');
    }

    const ongoingSection = container.querySelector('#sd-ongoing-away-section');
    const ongoingList = container.querySelector('#sd-ongoing-away-list');
    if (ongoingSection && ongoingList) {
      const ongoingCount = this.ongoingAwayPermissions.length;
      if (ongoingCount > 0) {
        ongoingSection.style.display = 'block';
        ongoingList.innerHTML = this.ongoingAwayPermissions.map(req => {
          const until = req.approvedUntil ? new Date(req.approvedUntil).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '--:--';
          const duration = req.approvedDurationMins || 0;
          return `
            <div style="
              background:rgba(255,255,255,0.03);
              border:1px solid var(--border-color);
              border-left:3px solid var(--status-success, #4caf50);
              border-radius:var(--radius-md,8px);
              padding:12px 14px;
              display:flex; flex-direction:column; gap:6px;
              margin-bottom:8px;
            ">
              <div style="display:flex; justify-content:space-between; align-items:center;">
                <span style="font-size:0.8rem; font-weight:700; color:var(--text-primary);">${req.employeeName || 'Employee'}</span>
                <span style="font-size:0.65rem; background:rgba(76,175,80,0.12); color:#4caf50; border:1px solid rgba(76,175,80,0.2); padding:2px 6px; border-radius:4px; font-weight:600;">ACTIVE</span>
              </div>
              <div style="font-size:0.74rem; color:var(--text-secondary); display:flex; flex-direction:column; gap:2px;">
                <div><strong>Approved:</strong> ${duration} mins (until ${until})</div>
                ${req.reason ? `<div><strong>Reason:</strong> <em>${req.reason}</em></div>` : ''}
              </div>
            </div>
          `;
        }).join('');
      } else {
        ongoingSection.style.display = 'none';
        ongoingList.innerHTML = '';
      }
    }
  }

  /**
   * Binds approve/deny buttons on away permission cards.
   * Approve opens an inline duration-picker popup card.
   */
  _bindAwayPermissionButtons(container, lifecycle) {
    // APPROVE
    container.querySelectorAll('.btn-away-approve').forEach(btn => {
      const handler = () => {
        const awayId = btn.getAttribute('data-away-id');
        this._showAwayApproveCard(container, lifecycle, awayId);
      };
      btn.addEventListener('click', handler);
      if (lifecycle) lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
    });

    // DENY
    container.querySelectorAll('.btn-away-deny').forEach(btn => {
      const handler = async () => {
        const awayId = btn.getAttribute('data-away-id');
        btn.disabled = true;
        btn.textContent = 'Denying…';
        try {
          const res = await apiClient.put(`/api/v1/away-permission/${awayId}/deny`, {});
          if (res?.success) {
            notificationStore.success('Away pass denied.');
            await this._reload(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to deny away pass.');
            btn.disabled = false;
            btn.textContent = '✕ Deny';
          }
        } catch (e) {
          notificationStore.danger('Error: ' + e.message);
          btn.disabled = false;
          btn.textContent = '✕ Deny';
        }
      };
      btn.addEventListener('click', handler);
      if (lifecycle) lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
    });
  }

  /**
   * Shows a duration-picker popup card for approving an away pass.
   * Rendered into a shared overlay element #sd-away-approve-overlay.
   */
  _showAwayApproveCard(container, lifecycle, awayId) {
    // Lazy-create overlay if not present
    let overlay = container.querySelector('#sd-away-approve-overlay');
    if (!overlay) {
      overlay = document.createElement('div');
      overlay.id = 'sd-away-approve-overlay';
      overlay.style.cssText = `
        position:fixed; inset:0; background:rgba(0,0,0,0.6); backdrop-filter:blur(4px);
        display:flex; align-items:center; justify-content:center; z-index:9999;
      `;
      container.appendChild(overlay);
    }

    overlay.innerHTML = `
      <div style="
        background:var(--bg-card,#1e1e2e); border:1px solid var(--border-color);
        border-radius:14px; padding:28px 24px; width:340px; max-width:95vw;
        display:flex; flex-direction:column; gap:16px; box-shadow: 0 8px 40px rgba(0,0,0,0.5);
      ">
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <h3 style="margin:0; font-size:0.95rem; font-weight:700; color:var(--text-primary);">⏱ Approve Away Pass</h3>
          <button id="btn-away-overlay-close" type="button" style="background:none;border:none;color:var(--text-muted);cursor:pointer;font-size:1.2rem;line-height:1;">✕</button>
        </div>
        <p style="margin:0; font-size:0.8rem; color:var(--text-secondary); line-height:1.5;">
          Set how many minutes the employee is allowed away from the store.
          A <strong>10-minute grace buffer</strong> will be added automatically before auto clock-out triggers.
        </p>
        <div style="display:flex; flex-direction:column; gap:6px;">
          <label style="font-size:0.75rem; color:var(--text-muted); font-weight:600;">Duration (minutes)</label>
          <input id="away-duration-input" type="number" min="5" max="240" value="30" style="
            background:var(--bg-input,rgba(255,255,255,0.05));
            border:1px solid var(--border-color); border-radius:8px;
            color:var(--text-primary); padding:8px 12px; font-size:0.85rem; width:100%; box-sizing:border-box;
          ">
          <span style="font-size:0.68rem; color:var(--text-muted);">Employee will have 30 min + 10 min grace = 40 min total before auto clock-out.</span>
        </div>
        <div style="display:flex; gap:10px;">
          <button id="btn-away-overlay-confirm" type="button" class="btn btn-primary" style="flex:1; padding:9px;">
            ✓ Confirm Approval
          </button>
          <button id="btn-away-overlay-cancel" type="button" class="btn btn-secondary" style="flex:1; padding:9px;">
            Cancel
          </button>
        </div>
      </div>
    `;
    overlay.style.display = 'flex';
    if (window.lucide) window.lucide.createIcons();

    const closeOverlay = () => { overlay.style.display = 'none'; };

    // Update grace hint dynamically
    const durationInput = overlay.querySelector('#away-duration-input');
    const graceHint = overlay.querySelector('span');
    const updateHint = () => {
      const d = parseInt(durationInput?.value || '30');
      if (graceHint) graceHint.textContent = `Employee will have ${d} min + 10 min grace = ${d + 10} min total before auto clock-out.`;
    };
    durationInput?.addEventListener('input', updateHint);

    overlay.querySelector('#btn-away-overlay-close')?.addEventListener('click', closeOverlay);
    overlay.querySelector('#btn-away-overlay-cancel')?.addEventListener('click', closeOverlay);
    overlay.addEventListener('click', e => { if (e.target === overlay) closeOverlay(); });

    const confirmBtn = overlay.querySelector('#btn-away-overlay-confirm');
    confirmBtn?.addEventListener('click', async () => {
      const durationMins = parseInt(durationInput?.value || '30');
      if (!durationMins || durationMins < 1) {
        notificationStore.danger('Please enter a valid duration.');
        return;
      }
      confirmBtn.disabled = true;
      confirmBtn.textContent = 'Approving…';
      try {
        const res = await apiClient.put(`/api/v1/away-permission/${awayId}/approve`, { durationMins });
        if (res?.success) {
          closeOverlay();
          notificationStore.success(`Away pass approved for ${durationMins} minutes (+10 min grace).`);
          await this._reload(container, lifecycle);
        } else {
          notificationStore.danger(res?.message || 'Failed to approve away pass.');
          confirmBtn.disabled = false;
          confirmBtn.textContent = '✓ Confirm Approval';
        }
      } catch (e) {
        notificationStore.danger('Error: ' + e.message);
        confirmBtn.disabled = false;
        confirmBtn.textContent = '✓ Confirm Approval';
      }
    });
  }

  // ─── Overtime Request Section ──────────────────────────────────────────────

  _populateOvertimeRequests(container) {
    const list = container.querySelector('#sd-ot-requests-list');
    const badge = container.querySelector('#sd-ot-badge');
    if (!list) return;

    const count = this.overtimeRequests.length;
    if (badge) {
      badge.textContent = count;
      badge.style.display = count > 0 ? 'inline-flex' : 'none';
    }

    if (count === 0) {
      list.innerHTML = `
        <div style="text-align:center; color:var(--text-muted); font-size:0.78rem; font-style:italic; padding:16px;">
          No pending overtime requests right now.
        </div>`;
      return;
    }

    list.innerHTML = this.overtimeRequests.map(req => {
      const dateStr = req.requestedDateDisplay || req.requestedDate || '';
      return `
        <div class="sd-ot-card" data-ot-id="${req.id}" style="
          background:rgba(255,255,255,0.03);
          border:1px solid var(--border-color);
          border-left: 3px solid var(--accent-primary);
          border-radius:var(--radius-md,8px);
          padding:12px 14px;
          display:flex;
          flex-direction:column;
          gap:8px;
          margin-bottom:8px;
        ">
          <div style="display:flex; justify-content:space-between; align-items:center;">
            <span style="font-size:0.8rem; font-weight:700; color:var(--text-primary);">${req.employeeName || 'Employee'}</span>
            <span style="font-size:0.68rem; color:var(--accent-primary); font-weight:600;">⏳ Overtime Requested</span>
          </div>
          <div style="font-size:0.74rem; color:var(--text-secondary);">
            Date: <strong>${dateStr}</strong> &nbsp;·&nbsp; Shift: <strong>${req.shiftName || ''}</strong>
          </div>
          <div style="font-size:0.74rem; color:var(--text-secondary);">
            ${req.reason ? `Reason: <em>${req.reason}</em>` : '<span style="color:var(--text-muted); font-style:italic;">No reason provided</span>'}
          </div>
          <div style="display:flex; gap:8px; margin-top:4px;">
            <button type="button" class="btn btn-primary btn-ot-approve" data-ot-id="${req.id}"
              style="flex:1; padding:6px 10px; font-size:0.75rem;">
              ✓ Approve Request
            </button>
            <button type="button" class="btn btn-secondary btn-ot-deny" data-ot-id="${req.id}"
              style="flex:1; padding:6px 10px; font-size:0.75rem;">
              ✕ Deny
            </button>
          </div>
        </div>
      `;
    }).join('');
  }

  _bindOvertimeRequestButtons(container, lifecycle) {
    container.querySelectorAll('.btn-ot-approve').forEach(btn => {
      const handler = async () => {
        btn.disabled = true;
        btn.textContent = 'Approving…';
        try {
          const res = await apiClient.put(`/api/v1/overtime-requests/${btn.getAttribute('data-ot-id')}/approve`, {});
          if (res?.success) {
            notificationStore.success('Overtime shift approved.');
            await this._reload(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to approve overtime request.');
            btn.disabled = false;
            btn.textContent = '✓ Approve Request';
          }
        } catch (e) {
          notificationStore.danger('Error: ' + e.message);
          btn.disabled = false;
          btn.textContent = '✓ Approve Request';
        }
      };
      btn.addEventListener('click', handler);
      if (lifecycle) lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
    });

    container.querySelectorAll('.btn-ot-deny').forEach(btn => {
      const handler = async () => {
        btn.disabled = true;
        btn.textContent = 'Denying…';
        try {
          const res = await apiClient.put(`/api/v1/overtime-requests/${btn.getAttribute('data-ot-id')}/deny`, {});
          if (res?.success) {
            notificationStore.success('Overtime shift denied.');
            await this._reload(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to deny request.');
            btn.disabled = false;
            btn.textContent = '✕ Deny';
          }
        } catch (e) {
          notificationStore.danger('Error: ' + e.message);
          btn.disabled = false;
          btn.textContent = '✕ Deny';
        }
      };
      btn.addEventListener('click', handler);
      if (lifecycle) lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
    });
  }
}

