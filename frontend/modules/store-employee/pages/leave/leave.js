/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : leave.js
 * Path              : frontend/modules/store-employee/leave/leave.js
 * Purpose           : Controller component for Store Employee Leave page UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/leave/leave.html
 * Related CSS       : frontend/modules/store-employee/leave/leave.css
 * Related APIs      : GET /leaves/types
 *                     GET /leaves/my/balance
 *                     GET /leaves/my
 *                     GET /leaves/holidays
 *                     GET /leaves/blackout
 *                     POST /leaves
 *                     POST /leaves/:id/upload-document
 *                     PUT /leaves/:id/cancel
 *                     PUT /leaves/:id/request-cancellation
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in leave.html — this file is a controller only.
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the leave HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/leave/leave.html';

export default class StoreEmployeeLeave {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.leaveTypes = [];
    this.balances = [];
    this.myLeaves = [];
    this.holidays = [];
    this.blackouts = [];
    this.calendarDate = new Date();
    this.countryName = 'France';
    this.countryLawLabel = 'French Labour Law Compliant';
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the StoreEmployeeLeave component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreEmployeeLeave', 'Mounting enterprise leave page...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML layout with loading state
    this._renderLoading(container);

    // 2. Fetch all telemetry and policies data from database
    await this.loadData();

    // 3. Inject full layout
    await this._loadTemplate(container);

    // 4. Render details
    this.render(container);

    // 5. Bind event listeners
    this.bindEvents(container, lifecycle);
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // DATA TELEMETRY SUB-ROUTINES
  // ---------------------------------------------------------------------------

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

  // ---------------------------------------------------------------------------
  // PUBLIC HELPER (Legacy Bridge): loadAndRender
  // ---------------------------------------------------------------------------

  async loadAndRender(container, lifecycle) {
    await this.loadData();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  render(container) {
    // 1. Sync header text
    const labelLaw = container.querySelector('#lbl-country-law');
    if (labelLaw) labelLaw.textContent = this.countryLawLabel;

    // 2. Render active blackout period banners
    const blackoutAlerts = container.querySelector('#blackout-alerts-container');
    if (blackoutAlerts) {
      blackoutAlerts.replaceChildren();
      if (this.blackouts.length > 0) {
        const div = document.createElement('div');
        div.className = 'blackout-alert-banner';
        div.innerHTML = `
          <strong>⛔ Active Blackout Period:</strong>
          ${this.blackouts.map(b => `${b.name} (${b.startDate} – ${b.endDate})`).join(', ')}
        `;
        blackoutAlerts.appendChild(div);
      }
    }

    // 3. Render 10 balances cards
    this._renderBalances(container);

    // 4. Render history table rows
    this._renderHistory(container);

    // 5. Render leave types select options
    this._renderLeaveTypes(container);

    // 6. Render public holidays
    this._renderHolidays(container);

    // 7. Populate policy rules table
    this._renderPolicyTable(container);

    if (window.lucide) window.lucide.createIcons();
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const typeSelect = container.querySelector('#leave-select-type');
    const docSection = container.querySelector('#doc-upload-section');
    const protectedNotice = container.querySelector('#protected-notice');
    const startInput = container.querySelector('#leave-input-start');
    const endInput = container.querySelector('#leave-input-end');
    const indicator = container.querySelector('#working-days-indicator');
    const counter = container.querySelector('#working-days-count');
    const submitBtn = container.querySelector('#btn-submit-leave-request');
    const globalDocInput = container.querySelector('#global-doc-upload-input');

    // Type change listener
    if (typeSelect) {
      const handleTypeChange = () => {
        const opt = typeSelect.selectedOptions[0];
        const isProtected = opt?.dataset.protected === 'true';
        const requiresDoc = opt?.dataset.doc === 'true';
        const docLabel = container.querySelector('#doc-upload-label');
        if (docLabel) {
          docLabel.innerHTML = requiresDoc 
            ? '📎 Supporting Document (Max 10MB) <span style="color:var(--status-danger);font-weight:bold;">* Required</span>'
            : '📎 Supporting Document (Max 10MB) <span class="doc-optional-lbl">(Optional)</span>';
        }
        if (protectedNotice) protectedNotice.style.display = isProtected ? 'block' : 'none';
      };
      typeSelect.addEventListener('change', handleTypeChange);
      lifecycle.onCleanup(() => typeSelect.removeEventListener('change', handleTypeChange));
    }

    // Date range indicator listener
    const updateDays = () => {
      const startVal = startInput?.value;
      const endVal = endInput?.value;
      if (startVal && endVal) {
        const start = new Date(startVal);
        const end = new Date(endVal);
        if (end >= start) {
          const days = this.calcWorkingDays(start, end, 'FULL_DAY');
          if (counter) counter.textContent = days % 1 === 0 ? days : days.toFixed(1);
          if (indicator) indicator.style.display = 'block';
        } else {
          if (indicator) indicator.style.display = 'none';
        }
      } else {
        if (indicator) indicator.style.display = 'none';
      }
    };

    if (startInput) {
      startInput.addEventListener('change', updateDays);
      startInput.addEventListener('input', updateDays);
      lifecycle.onCleanup(() => {
        startInput.removeEventListener('change', updateDays);
        startInput.removeEventListener('input', updateDays);
      });
    }

    if (endInput) {
      endInput.addEventListener('change', updateDays);
      endInput.addEventListener('input', updateDays);
      lifecycle.onCleanup(() => {
        endInput.removeEventListener('change', updateDays);
        endInput.removeEventListener('input', updateDays);
      });
    }

    // Form Submit listener
    if (submitBtn) {
      const handleSubmit = async () => {
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
          if (response?.success) {
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
                if (!uploadRes?.success) {
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
              await this.loadAndRender(container, lifecycle);
            }
          } else {
            notificationStore.danger(response?.message || 'Failed to submit leave. Please check your details.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to submit leave.');
        }
      };
      submitBtn.addEventListener('click', handleSubmit);
      lifecycle.onCleanup(() => submitBtn.removeEventListener('click', handleSubmit));
    }

    // Bind cancel buttons in table rows
    const cancelRowBtns = container.querySelectorAll('.btn-cancel-leave');
    const modal = container.querySelector('#cancel-modal');
    const modalSubmit = container.querySelector('#cancel-modal-submit');
    const modalClose = container.querySelector('#cancel-modal-close');
    const cancelReasonInput = container.querySelector('#cancel-reason-input');

    cancelRowBtns.forEach(btn => {
      const handleOpenCancel = () => {
        container.querySelector('#cancel-modal-title').textContent = 'Cancel Leave Request';
        if (modalSubmit) {
          modalSubmit.dataset.leaveId = btn.dataset.id;
          modalSubmit.dataset.mode = 'cancel';
        }
        if (cancelReasonInput) cancelReasonInput.value = '';
        if (modal) {
          modal.style.display = 'flex';
          modal.setAttribute('aria-hidden', 'false');
        }
      };
      btn.addEventListener('click', handleOpenCancel);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleOpenCancel));
    });

    const cancelReqBtns = container.querySelectorAll('.btn-req-cancel-leave');
    cancelReqBtns.forEach(btn => {
      const handleOpenReqCancel = () => {
        container.querySelector('#cancel-modal-title').textContent = 'Request Leave Cancellation';
        if (modalSubmit) {
          modalSubmit.dataset.leaveId = btn.dataset.id;
          modalSubmit.dataset.mode = 'request';
        }
        if (cancelReasonInput) cancelReasonInput.value = '';
        if (modal) {
          modal.style.display = 'flex';
          modal.setAttribute('aria-hidden', 'false');
        }
      };
      btn.addEventListener('click', handleOpenReqCancel);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleOpenReqCancel));
    });

    if (modalSubmit) {
      const handleConfirmCancel = async () => {
        const reason = cancelReasonInput?.value?.trim();
        const leaveId = modalSubmit.dataset.leaveId;
        const mode = modalSubmit.dataset.mode;
        if (!reason || reason.length < 10) {
          notificationStore.danger('Cancellation reason must be at least 10 characters.');
          return;
        }
        const endpoint = mode === 'cancel' ? `/leaves/${leaveId}/cancel` : `/leaves/${leaveId}/request-cancellation`;
        try {
          const response = await apiClient.put(endpoint, { reason });
          if (response?.success) {
            notificationStore.success(mode === 'cancel' ? 'Leave cancelled. Balance restored.' : 'Cancellation request submitted.');
            if (modal) {
              modal.style.display = 'none';
              modal.setAttribute('aria-hidden', 'true');
            }
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(response?.message || 'Failed. Please try again.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to submit cancellation request.');
        }
      };
      modalSubmit.addEventListener('click', handleConfirmCancel);
      lifecycle.onCleanup(() => modalSubmit.removeEventListener('click', handleConfirmCancel));
    }

    if (modalClose) {
      const handleCloseCancel = () => {
        if (modal) {
          modal.style.display = 'none';
          modal.setAttribute('aria-hidden', 'true');
        }
      };
      modalClose.addEventListener('click', handleCloseCancel);
      lifecycle.onCleanup(() => modalClose.removeEventListener('click', handleCloseCancel));
    }

    if (modal) {
      const handleModalBgClick = (e) => {
        if (e.target === modal) {
          modal.style.display = 'none';
          modal.setAttribute('aria-hidden', 'true');
        }
      };
      modal.addEventListener('click', handleModalBgClick);
      lifecycle.onCleanup(() => modal.removeEventListener('click', handleModalBgClick));
    }

    // Document upload binds inside table rows
    const rowUploadBtns = container.querySelectorAll('.btn-upload-doc');
    rowUploadBtns.forEach(btn => {
      const handleOpenUpload = () => {
        if (globalDocInput) {
          globalDocInput.dataset.leaveId = btn.dataset.id;
          globalDocInput.value = '';
          globalDocInput.click();
        }
      };
      btn.addEventListener('click', handleOpenUpload);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleOpenUpload));
    });

    if (globalDocInput) {
      const handleGlobalUploadChange = async (e) => {
        const file = e.target.files?.[0];
        if (!file) return;

        const maxLimit = 10 * 1024 * 1024;
        if (file.size > maxLimit) {
          notificationStore.danger('File size exceeds the 10MB limit.');
          return;
        }

        const leaveId = globalDocInput.dataset.leaveId;
        const formData = new FormData();
        formData.append('file', file);

        try {
          const response = await apiClient.request(`/leaves/${leaveId}/upload-document`, {
            method: 'POST',
            body: formData
          });
          if (response?.success) {
            notificationStore.success('Document uploaded successfully.');
            await this.loadAndRender(container, lifecycle);
          } else {
            notificationStore.danger(response?.message || 'Failed to upload document.');
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to upload document.');
        }
      };
      globalDocInput.addEventListener('change', handleGlobalUploadChange);
      lifecycle.onCleanup(() => globalDocInput.removeEventListener('change', handleGlobalUploadChange));
    }

    // Policy Booklet Modal triggers
    const btnPolicy = container.querySelector('#btn-leave-policy');
    const policyModal = container.querySelector('#leave-policy-modal');
    const closePolicyBtn = container.querySelector('#leave-policy-close');

    if (btnPolicy) {
      const handleOpenPolicy = () => {
        if (policyModal) {
          policyModal.style.display = 'flex';
          policyModal.setAttribute('aria-hidden', 'false');
        }
      };
      btnPolicy.addEventListener('click', handleOpenPolicy);
      lifecycle.onCleanup(() => btnPolicy.removeEventListener('click', handleOpenPolicy));
    }

    if (closePolicyBtn) {
      const handleClosePolicy = () => {
        if (policyModal) {
          policyModal.style.display = 'none';
          policyModal.setAttribute('aria-hidden', 'true');
        }
      };
      closePolicyBtn.addEventListener('click', handleClosePolicy);
      lifecycle.onCleanup(() => closePolicyBtn.removeEventListener('click', handleClosePolicy));
    }

    if (policyModal) {
      const handlePolicyBgClick = (e) => {
        if (e.target === policyModal) {
          policyModal.style.display = 'none';
          policyModal.setAttribute('aria-hidden', 'true');
        }
      };
      policyModal.addEventListener('click', handlePolicyBgClick);
      lifecycle.onCleanup(() => policyModal.removeEventListener('click', handlePolicyBgClick));
    }
  }

  // ---------------------------------------------------------------------------
  // CLIENT-SIDE ESTIMATOR
  // ---------------------------------------------------------------------------

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

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderLoading(container) {
    container.innerHTML = `
      <div style="display:flex;align-items:center;justify-content:center;height:400px;flex-direction:column;gap:12px;">
        <i data-lucide="loader-2" class="animate-spin" style="width:32px;height:32px;color:var(--accent-primary);"></i>
        <span style="color:var(--text-muted);font-size:0.8rem;font-weight:600;">Loading leave data...</span>
      </div>`;
    if (window.lucide) window.lucide.createIcons();
  }

  _renderBalances(container) {
    const balancesContainer = container.querySelector('#leave-balances-grid');
    if (!balancesContainer) return;

    balancesContainer.replaceChildren();

    const config = [
      { code: 'ANNUAL', label: 'Annual Leave', icon: 'plane', color: 'var(--accent-primary)' },
      { code: 'SICK', label: 'Sick Leave', icon: 'shield-alert', color: 'var(--status-danger)' },
      { code: 'PERSONAL', label: 'Personal', icon: 'user', color: 'var(--status-info)' },
      { code: 'CASUAL', label: 'Casual', icon: 'umbrella', color: '#a78bfa' },
      { code: 'EMERGENCY', label: 'Emergency', icon: 'alert-triangle', color: 'var(--status-warning)' },
      { code: 'MARRIAGE', label: 'Marriage', icon: 'heart', color: '#f472b6' },
      { code: 'BEREAVEMENT', label: 'Bereavement', icon: 'flower', color: '#94a3b8' },
      { code: 'MATERNITY', label: 'Maternity', icon: 'baby', color: '#34d399' },
      { code: 'PATERNITY', label: 'Paternity', icon: 'users', color: '#60a5fa' },
      { code: 'UNPAID', label: 'Unpaid', icon: 'banknote', color: 'var(--text-muted)' }
    ];

    config.forEach(({ code, label, icon, color }) => {
      const b = this.getBalance(code);
      const remaining = typeof b.remaining === 'number' ? b.remaining : '—';
      const div = document.createElement('div');
      div.className = 'kpi-block-card';
      div.innerHTML = `
        <div class="kpi-header">
          <span class="kpi-label">${label}</span>
          <i data-lucide="${icon}" class="kpi-icon" style="color: ${color};" aria-hidden="true"></i>
        </div>
        <div class="kpi-details">
          <div class="kpi-value">${remaining} <span>days</span></div>
          <div class="kpi-sub-badges">
            <span class="kpi-sub-badge-pending">⏳ ${b.pending || 0} pending</span>
            <span class="kpi-sub-badge-used" style="color: ${color};">✓ ${b.used || 0} used</span>
          </div>
        </div>
      `;
      balancesContainer.appendChild(div);
    });
  }

  _renderHistory(container) {
    const tbody = container.querySelector('#leave-history-tbody');
    if (!tbody) return;

    tbody.replaceChildren();

    if (this.myLeaves.length === 0) {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td colspan="8" style="text-align:center;padding:var(--spacing-lg);color:var(--text-muted);font-size:0.75rem;">
          No leave requests yet.
        </td>
      `;
      tbody.appendChild(tr);
      return;
    }

    this.myLeaves.forEach(l => {
      const tr = document.createElement('tr');
      
      let actionHtml = '';
      if (l.status === 'PENDING') {
        actionHtml += `<button class="btn-cancel-leave" data-id="${l.id}">Cancel</button>`;
      }
      if (l.status === 'APPROVED' && !l.cancellationRequested) {
        actionHtml += `<button class="btn-req-cancel-leave" data-id="${l.id}">Req. Cancel</button>`;
      }
      if (l.status === 'APPROVED' && l.cancellationRequested) {
        actionHtml += `<span style="font-size:0.6rem;color:var(--text-muted);font-style:italic;">Cancel Pending</span>`;
      }
      if (l.requiresDocument && l.status === 'PENDING' && !l.hasDocument) {
        actionHtml += `<button class="btn-upload-doc" data-id="${l.id}">Upload Doc</button>`;
      }

      tr.innerHTML = `
        <td style="padding:8px 6px;font-weight:600;font-size:0.74rem;">${l.leaveType}</td>
        <td style="padding:8px 6px;font-size:0.7rem;color:var(--text-muted);">${this.sessionLabel(l.session)}</td>
        <td style="padding:8px 6px;font-size:0.7rem;font-weight:700;">${l.totalDays}d</td>
        <td style="padding:8px 6px;font-size:0.68rem;color:var(--text-muted);">${l.startDate}</td>
        <td style="padding:8px 6px;font-size:0.68rem;color:var(--text-muted);">${l.endDate}</td>
        <td style="padding:8px 6px;font-size:0.68rem;max-width:120px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${l.reason || ''}">${l.reason || '—'}</td>
        <td style="padding:8px 6px;">
          <span class="status-badge" style="color:${this.statusColor(l.status)};background:${this.statusColor(l.status)}20;">${l.status}</span>
        </td>
        <td style="padding:8px 6px;">${actionHtml}</td>
      `;
      tbody.appendChild(tr);
    });
  }

  _renderLeaveTypes(container) {
    const typeSelect = container.querySelector('#leave-select-type');
    if (!typeSelect) return;

    // Remove old dynamic options keeping placeholder
    while (typeSelect.options.length > 1) {
      typeSelect.remove(1);
    }

    this.leaveTypes.forEach(lt => {
      const opt = document.createElement('option');
      opt.value = String(lt.id);
      opt.setAttribute('data-code', String(lt.code));
      opt.setAttribute('data-protected', String(lt.protected));
      opt.setAttribute('data-doc', String(lt.requiresDocument));
      opt.setAttribute('data-notice', String(lt.minNoticeDays || 0));
      opt.textContent = `${lt.name}${lt.protected ? ' 🛡️' : ''}${!lt.paid ? ' (Unpaid)' : ''}`;
      typeSelect.appendChild(opt);
    });
  }

  _renderHolidays(container) {
    const list = container.querySelector('#holidays-details-container');
    const title = container.querySelector('#holidays-title');
    const moreText = container.querySelector('#holidays-more-text');

    let flag = '🇫🇷';
    if (this.countryName === 'India') flag = '🇮🇳';
    else if (this.countryName === 'UAE') flag = '🇦🇪';

    if (title) {
      title.textContent = `${flag} Public Holidays ${new Date().getFullYear()}`;
    }

    if (list) {
      list.replaceChildren();
      
      this.holidays.slice(0, 5).forEach(h => {
        const div = document.createElement('div');
        div.className = 'holiday-row';
        div.innerHTML = `
          <span class="label">${flag} ${h.name}</span>
          <span class="val">${h.date}</span>
        `;
        list.appendChild(div);
      });
    }

    if (moreText) {
      if (this.holidays.length > 5) {
        moreText.textContent = `+${this.holidays.length - 5} more holidays`;
        moreText.style.display = 'block';
      } else {
        moreText.style.display = 'none';
      }
    }
  }

  _renderPolicyTable(container) {
    const tbody = container.querySelector('#policy-details-tbody');
    const desc = container.querySelector('#policy-desc-country-info');

    if (desc) {
      desc.textContent = `The following leave entitlements and rules apply to all employees as per company policy and ${this.countryName || 'French'} Labour Law.`;
    }

    if (tbody) {
      tbody.replaceChildren();

      this.leaveTypes.forEach(t => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td style="padding:8px 6px;font-weight:700;color:var(--text-primary);">${t.name}${t.protected ? ' 🛡️' : ''}</td>
          <td style="padding:8px 6px;text-align:center;font-weight:600;">${t.annualLimit != null ? t.annualLimit + 'd' : '—'}</td>
          <td style="padding:8px 6px;text-align:center;">${t.monthlyAccrual != null ? t.monthlyAccrual + 'd' : '—'}</td>
          <td style="padding:8px 6px;text-align:center;">${t.maxConsecutiveDays != null ? t.maxConsecutiveDays + 'd' : '—'}</td>
          <td style="padding:8px 6px;text-align:center;">${t.minNoticeDays != null ? t.minNoticeDays + 'd' : '0d'}</td>
          <td style="padding:8px 6px;text-align:center;">${t.paid ? '<span style="color:var(--status-success);font-weight:700;">Yes</span>' : '<span style="color:var(--status-danger);font-weight:700;">No</span>'}</td>
          <td style="padding:8px 6px;text-align:center;">${t.protected ? '<span style="color:#818cf8;font-weight:700;">Yes</span>' : '<span style="color:var(--text-muted);">No</span>'}</td>
          <td style="padding:8px 6px;text-align:center;">${t.requiresDocument ? '<span style="color:var(--status-warning);font-weight:700;">Required</span>' : '<span style="color:var(--text-muted);">—</span>'}</td>
        `;
        tbody.appendChild(tr);
      });
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _loadCss() {
    const cssId = 'store-employee-leave-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/leave/leave.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreEmployeeLeave };
