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
    this.storeEmployees = [];   // for the replacement employee picker
    this.baseDate       = new Date();
    this.baseDate.setHours(0, 0, 0, 0);

    // Active swap ID tracked across modal open / confirm
    this._activeSwapId    = null;
    this._activeRejectId  = null;
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
      const [empRes, shiftRes, assignRes, swapRes] = await Promise.all([
        apiClient.get('/api/v1/shift-assignments/store-employees'),
        apiClient.get('/api/v1/shifts'),
        apiClient.get('/api/v1/shift-assignments'),
        apiClient.get('/api/v1/shift-swaps/store-requests'),
      ]);

      this.employees   = empRes?.success   ? empRes.data   : [];
      this.shifts      = shiftRes?.success ? shiftRes.data : [];
      this.assignments = assignRes?.success ? assignRes.data : [];

      const swapData      = swapRes?.success ? swapRes.data : {};
      this.pendingSwaps   = Array.isArray(swapData) ? swapData : (swapData.swaps || []);
      this.storeEmployees = swapData.storeEmployees || [];
    } catch (e) {
      logger.error('ShiftDashboard', 'Error fetching data:', e);
      this.employees = this.shifts = this.assignments = this.pendingSwaps = this.storeEmployees = [];
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
        this.shifts.filter(s => s.active).map(s =>
          `<option value="${s.id}">${s.name} (${s.startTime} – ${s.endTime})</option>`
        ).join('');
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
  }
}
