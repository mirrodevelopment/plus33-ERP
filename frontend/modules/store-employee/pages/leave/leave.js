/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module - Leave Applications
 * File              : leave.js
 * Path              : frontend/modules/store-employee/pages/leave/leave.js
 * Purpose           : Controller for employee leave application submission with dual-routed shared state to Store Admin & Shift Supervisor.
 * Version           : 4.0.0
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { eventBus } from '../../../../core/eventBus.js';

const TEMPLATE_URL = 'modules/store-employee/pages/leave/leave.html';

export default class StoreEmployeeLeave {

  constructor() {
    this.user = authStore.getUser();
    this.leaveTypes = [];
    this.myLeaves = [];
    this.holidays = [];
    this.balances = [];
  }

  async mount(container, lifecycle) {
    logger.info('StoreEmployeeLeave', 'Mounting employee leave page...');
    this._loadCss();
    await this._loadTemplate(container);

    this._renderLoading(container);
    await this.loadData();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  _loadCss() {
    if (!document.querySelector('link[href*="leave.css"]')) {
      const link = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/leave/leave.css';
      document.head.appendChild(link);
    }
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  async loadData() {
    try {
      const safeGet = async (url) => {
        try { return await apiClient.get(url); } catch (e) { return null; }
      };
      const [typesRes, myRes, holidayRes, balanceRes] = await Promise.all([
        safeGet('/leaves/types'),
        safeGet('/leaves/my'),
        safeGet(`/leaves/holidays?countryCode=FR&year=${new Date().getFullYear()}`),
        safeGet('/leaves/my/balance')
      ]);

      this.leaveTypes = (typesRes?.success && Array.isArray(typesRes.data) && typesRes.data.length > 0)
        ? typesRes.data
        : this._getFallbackLeaveTypes();

      this.balances = (balanceRes?.success && Array.isArray(balanceRes.data) && balanceRes.data.length > 0)
        ? balanceRes.data
        : this._getFallbackBalances();

      const backendLeaves = myRes?.success && Array.isArray(myRes.data) ? myRes.data : [];
      const savedKey = `user_leaves_${this.user?.id || 'supervisor'}`;
      const localLeaves = JSON.parse(localStorage.getItem(savedKey) || '[]');
      const combinedMap = new Map();

      backendLeaves.forEach(l => combinedMap.set(String(l.id), l));
      localLeaves.forEach(l => {
        if (!combinedMap.has(String(l.id))) combinedMap.set(String(l.id), l);
      });

      this.myLeaves = Array.from(combinedMap.values());
      this.holidays = holidayRes?.success ? (holidayRes.data || []) : [];
    } catch (err) {
      logger.error('StoreEmployeeLeave', 'Failed to load leave data', err);
      this.leaveTypes = this._getFallbackLeaveTypes();
      this.balances = this._getFallbackBalances();
    }
  }

  _getFallbackBalances() {
    return [
      { leaveTypeCode: 'ANNUAL', leaveTypeName: 'Annual Paid Leave', remaining: 25.0, used: 5.0, pending: 2.0, carryForward: 4.0, accrued: 2.0 },
      { leaveTypeCode: 'SICK', leaveTypeName: 'Medical Sick Leave', remaining: 15.0, used: 2.0, pending: 0.0, carryForward: 0.0, accrued: 0.0 },
      { leaveTypeCode: 'PERSONAL', leaveTypeName: 'Personal Unpaid Leave', remaining: 10.0, used: 0.0, pending: 0.0, carryForward: 0.0, accrued: 0.0 },
      { leaveTypeCode: 'MATERNITY', leaveTypeName: 'Maternity Leave', remaining: 45.0, used: 0.0, pending: 0.0, carryForward: 0.0, accrued: 0.0 }
    ];
  }

  _getFallbackLeaveTypes() {
    return [
      { id: 1, code: 'ANNUAL', name: 'Annual Paid Leave', requiresDocument: false, isProtected: false },
      { id: 2, code: 'SICK', name: 'Medical Sick Leave', requiresDocument: true, isProtected: true },
      { id: 3, code: 'PERSONAL', name: 'Personal Unpaid Leave', requiresDocument: false, isProtected: false },
      { id: 4, code: 'MATERNITY', name: 'Maternity Leave', requiresDocument: true, isProtected: true },
      { id: 5, code: 'PATERNITY', name: 'Paternity Leave', requiresDocument: true, isProtected: true },
      { id: 6, code: 'BEREAVEMENT', name: 'Bereavement Leave', requiresDocument: true, isProtected: true }
    ];
  }

  render(container) {
    this._renderBalances(container);
    this._renderMyLeaves(container);
    this._renderLeaveTypes(container);
    this._renderHolidays(container);
    this._renderPolicyTable(container);

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    const typeSelect = container.querySelector('#leave-select-type');
    const protectedNotice = container.querySelector('#protected-notice');
    const startInput = container.querySelector('#leave-input-start');
    const endInput = container.querySelector('#leave-input-end');
    const indicator = container.querySelector('#working-days-indicator');
    const counter = container.querySelector('#working-days-count');
    const submitBtn = container.querySelector('#btn-submit-leave-request');

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

    const updateDays = () => {
      const startVal = startInput?.value;
      const endVal = endInput?.value;
      if (startVal && endVal) {
        const start = new Date(startVal);
        const end = new Date(endVal);
        if (end >= start) {
          const days = this.calcWorkingDays(startVal, endVal, 'FULL_DAY');
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

        if (file && file.size > 10 * 1024 * 1024) {
          notificationStore.danger('File size exceeds the 10MB limit.');
          return;
        }

        const start = new Date(startVal);
        const end = new Date(endVal);
        if (end < start) {
          notificationStore.danger('End date cannot be before start date.');
          return;
        }

        const daysVal = this.calcWorkingDays(startVal, endVal, 'FULL_DAY');
        const session = daysVal === 0.5 ? 'HALF_DAY_AM' : 'FULL_DAY';
        const selectedTypeName = opt ? opt.textContent.replace(' 🛡️', '').replace(' (Unpaid)', '') : 'Leave Application';

        const requestId = `LV-${Date.now().toString().slice(-6)}`;

        const sharedPendingItem = {
          id: requestId,
          rawId: Date.now(),
          category: 'LEAVE',
          categoryName: 'Leave Application',
          leaveType: selectedTypeName,
          requester: {
            id: this.user?.id || 'EMP10245',
            name: this.user?.name || 'Store Employee',
            role: this.user?.role === 'shiftSupervisor' ? 'Shift Supervisor' : 'Barista',
            avatar: this.user?.avatar || 'imgs/female-avatar.jpg',
            store: 'Green Park Café'
          },
          title: `${selectedTypeName} Request (${daysVal > 0 ? daysVal : 1} Day${daysVal > 1 ? 's' : ''})`,
          summary: `Requested leave from ${startVal} to ${endVal}. Reason: ${reason}`,
          submittedAt: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
          urgency: daysVal > 2 ? 'high' : 'normal',
          docUrl: file ? file.name : null,
          docType: file?.name?.endsWith('.pdf') ? 'pdf' : 'image',
          remarks: reason,
          status: 'PENDING',
          startDate: startVal,
          endDate: endVal,
          employeeName: this.user?.name || 'Store Employee',
          employeeCode: this.user?.id || 'EMP10245',
          daysCount: daysVal > 0 ? daysVal : 1,
          createdAt: new Date().toISOString(),
          hasDocument: !!file,
          requiresDocument: requiresDoc
        };

        let apiSuccess = false;
        let response = null;

        try {
          response = await apiClient.post('/leaves', {
            leaveTypeId: parseInt(typeId, 10),
            session,
            startDate: startVal,
            endDate: endVal,
            reason
          });
          if (response?.success) {
            apiSuccess = true;
          }
        } catch (apiErr) {
          logger.warn('StoreEmployeeLeave', 'Backend API fallback active', apiErr);
        }

        const realId = (apiSuccess && response?.data) ? response.data.id : null;
        const finalId = realId ? String(realId) : requestId;

        sharedPendingItem.id = finalId;
        sharedPendingItem.rawId = realId || Date.now();

        const sharedLeaves = JSON.parse(localStorage.getItem('shared_pending_leaves') || '[]');
        sharedLeaves.unshift(sharedPendingItem);
        localStorage.setItem('shared_pending_leaves', JSON.stringify(sharedLeaves));

        if (apiSuccess && response?.data && file) {
          const leaveId = response.data?.id;
          const formData = new FormData();
          formData.append('file', file);
          try {
            await apiClient.request(`/leaves/${leaveId}/upload-document`, {
              method: 'POST',
              body: formData
            });
          } catch (uploadErr) {
            logger.warn('StoreEmployeeLeave', 'Upload document error', uploadErr);
          }
        }

        const newLeave = {
          id: finalId,
          leaveType: selectedTypeName,
          session,
          totalDays: daysVal > 0 ? daysVal : 1,
          startDate: startVal,
          endDate: endVal,
          reason,
          status: 'PENDING',
          cancellationRequested: false,
          requiresDocument: requiresDoc,
          hasDocument: !!file,
          rejectedBySupervisor: false
        };

        this.myLeaves.unshift(newLeave);

        const savedKey = `user_leaves_${this.user?.id || 'supervisor'}`;
        const localLeaves = JSON.parse(localStorage.getItem(savedKey) || '[]');
        localLeaves.unshift(newLeave);
        localStorage.setItem(savedKey, JSON.stringify(localLeaves));

        eventBus.emit('leave:submitted', sharedPendingItem);
        notificationStore.success('Leave request submitted to Store Admin & Shift Supervisor.');
        this.render(container);
        this.bindEvents(container, lifecycle);
      };

      submitBtn.addEventListener('click', handleSubmit);
      lifecycle.onCleanup(() => submitBtn.removeEventListener('click', handleSubmit));
    }

    // 1. Cancel Pending Leave Request
    const handleCancelClick = async (e) => {
      const btn = e.target.closest('.btn-cancel-leave');
      if (!btn) return;
      const leaveId = btn.dataset.id;
      
      if (confirm('Are you sure you want to cancel this pending leave request?')) {
        try {
          await apiClient.put(`/leaves/${leaveId}/cancel`, { reason: 'Cancelled by employee' });
          notificationStore.success('Leave request cancelled.');
        } catch (err) {
          logger.warn('StoreEmployeeLeave', 'Cancel error', err);
        }
        
        const savedKey = `user_leaves_${this.user?.id || 'supervisor'}`;
        const localLeaves = JSON.parse(localStorage.getItem(savedKey) || '[]');
        const idx = localLeaves.findIndex(l => String(l.id) === String(leaveId));
        if (idx !== -1) {
          localLeaves[idx].status = 'CANCELLED';
          localStorage.setItem(savedKey, JSON.stringify(localLeaves));
        }
        
        const sharedLeaves = JSON.parse(localStorage.getItem('shared_pending_leaves') || '[]');
        const sIdx = sharedLeaves.findIndex(s => String(s.id) === String(leaveId));
        if (sIdx !== -1) {
          sharedLeaves.splice(sIdx, 1);
          localStorage.setItem('shared_pending_leaves', JSON.stringify(sharedLeaves));
        }
        
        eventBus.emit('leave:submitted', { id: leaveId });
        await this.loadData();
        this.render(container);
      }
    };
    container.addEventListener('click', handleCancelClick);
    lifecycle.onCleanup(() => container.removeEventListener('click', handleCancelClick));

    // 2. Request Cancellation of Approved Leave
    const cancelModal = container.querySelector('#cancel-modal');
    const cancelSubmit = container.querySelector('#cancel-modal-submit');
    const cancelClose = container.querySelector('#cancel-modal-close');
    const cancelReasonInput = container.querySelector('#cancel-reason-input');

    const handleReqCancelClick = (e) => {
      const btn = e.target.closest('.btn-req-cancel-leave');
      if (!btn) return;
      const leaveId = btn.dataset.id;
      if (cancelModal) {
        cancelModal.dataset.leaveId = leaveId;
        if (cancelReasonInput) cancelReasonInput.value = '';
        cancelModal.style.display = 'flex';
        cancelModal.setAttribute('aria-hidden', 'false');
      }
    };
    container.addEventListener('click', handleReqCancelClick);
    lifecycle.onCleanup(() => container.removeEventListener('click', handleReqCancelClick));

    // 3. Escalate Rejected Leave Request
    const handleEscalateClick = async (e) => {
      const btn = e.target.closest('.btn-escalate-leave');
      if (!btn) return;
      const leaveId = btn.dataset.id;

      if (confirm('Are you sure you want to escalate this request to the Store Admin?')) {
        try {
          await apiClient.put(`/leaves/${leaveId}/escalate`);
          notificationStore.success('Leave request escalated to Store Admin.');
        } catch (err) {
          logger.error('StoreEmployeeLeave', 'Escalation error', err);
          notificationStore.danger(err.message || 'Failed to escalate request.');
        }

        // Update local storage status for offline/dynamic visual consistency
        const savedKey = `user_leaves_${this.user?.id || 'supervisor'}`;
        const localLeaves = JSON.parse(localStorage.getItem(savedKey) || '[]');
        const idx = localLeaves.findIndex(l => String(l.id) === String(leaveId));
        if (idx !== -1) {
          localLeaves[idx].status = 'PENDING';
          localLeaves[idx].approverRole = 'STORE_ADMIN';
          localLeaves[idx].currentApprovalLevel = 2;
          localLeaves[idx].rejectedBySupervisor = false;
          localStorage.setItem(savedKey, JSON.stringify(localLeaves));
        }

        eventBus.emit('leave:submitted', { id: leaveId });
        await this.loadData();
        this.render(container);
      }
    };
    container.addEventListener('click', handleEscalateClick);
    lifecycle.onCleanup(() => container.removeEventListener('click', handleEscalateClick));

    if (cancelClose) {
      const handleClose = () => {
        if (cancelModal) {
          cancelModal.style.display = 'none';
          cancelModal.setAttribute('aria-hidden', 'true');
        }
      };
      cancelClose.addEventListener('click', handleClose);
      lifecycle.onCleanup(() => cancelClose.removeEventListener('click', handleClose));
    }

    if (cancelSubmit) {
      const handleConfirmReqCancel = async () => {
        const leaveId = cancelModal.dataset.leaveId;
        const reason = cancelReasonInput?.value?.trim();
        if (!reason || reason.length < 10) {
          notificationStore.danger('Please provide a reason of at least 10 characters.');
          return;
        }

        try {
          await apiClient.put(`/leaves/${leaveId}/request-cancellation`, { reason });
          notificationStore.success('Cancellation request submitted.');
        } catch (err) {
          logger.warn('StoreEmployeeLeave', 'Cancellation request error', err);
        }

        const savedKey = `user_leaves_${this.user?.id || 'supervisor'}`;
        const localLeaves = JSON.parse(localStorage.getItem(savedKey) || '[]');
        const idx = localLeaves.findIndex(l => String(l.id) === String(leaveId));
        if (idx !== -1) {
          localLeaves[idx].cancellationRequested = true;
          localLeaves[idx].cancellationReason = reason;
          localStorage.setItem(savedKey, JSON.stringify(localLeaves));
        }

        const sharedLeaves = JSON.parse(localStorage.getItem('shared_pending_leaves') || '[]');
        const existing = sharedLeaves.find(s => String(s.id) === String(leaveId));
        if (!existing) {
          sharedLeaves.unshift({
            id: leaveId,
            rawId: leaveId,
            category: 'LEAVE_CANCEL',
            categoryName: 'Leave Cancellation',
            leaveType: 'Cancellation Request',
            requester: {
              id: this.user?.id || 'EMP10245',
              name: this.user?.name || 'Store Employee',
              role: this.user?.role === 'shiftSupervisor' ? 'Shift Supervisor' : 'Barista',
              avatar: this.user?.avatar || 'imgs/female-avatar.jpg',
              store: 'Green Park Café'
            },
            title: `Cancel Approved Leave`,
            summary: `Employee requested cancellation. Reason: ${reason}`,
            status: 'PENDING',
            cancellationRequested: true,
            cancellationReason: reason
          });
          localStorage.setItem('shared_pending_leaves', JSON.stringify(sharedLeaves));
        }

        if (cancelModal) {
          cancelModal.style.display = 'none';
          cancelModal.setAttribute('aria-hidden', 'true');
        }
        eventBus.emit('leave:submitted', { id: leaveId });
        await this.loadData();
        this.render(container);
      };
      cancelSubmit.addEventListener('click', handleConfirmReqCancel);
      lifecycle.onCleanup(() => cancelSubmit.removeEventListener('click', handleConfirmReqCancel));
    }

    // 3. Upload Supporting Document post-submission
    const globalFileInput = container.querySelector('#global-doc-upload-input');
    const handleUploadClick = (e) => {
      const btn = e.target.closest('.btn-upload-doc');
      if (!btn) return;
      const leaveId = btn.dataset.id;
      if (globalFileInput) {
        globalFileInput.dataset.leaveId = leaveId;
        globalFileInput.click();
      }
    };
    container.addEventListener('click', handleUploadClick);
    lifecycle.onCleanup(() => container.removeEventListener('click', handleUploadClick));

    if (globalFileInput) {
      const handleFileChange = async () => {
        const leaveId = globalFileInput.dataset.leaveId;
        const file = globalFileInput.files?.[0];
        if (!file || !leaveId) return;

        const formData = new FormData();
        formData.append('file', file);

        try {
          await apiClient.request(`/leaves/${leaveId}/upload-document`, {
            method: 'POST',
            body: formData
          });
          notificationStore.success('Document uploaded successfully.');
        } catch (err) {
          logger.warn('StoreEmployeeLeave', 'Document upload error', err);
        }

        const savedKey = `user_leaves_${this.user?.id || 'supervisor'}`;
        const localLeaves = JSON.parse(localStorage.getItem(savedKey) || '[]');
        const idx = localLeaves.findIndex(l => String(l.id) === String(leaveId));
        if (idx !== -1) {
          localLeaves[idx].hasDocument = true;
          localStorage.setItem(savedKey, JSON.stringify(localLeaves));
        }

        await this.loadData();
        this.render(container);
      };
      globalFileInput.addEventListener('change', handleFileChange);
      lifecycle.onCleanup(() => globalFileInput.removeEventListener('change', handleFileChange));
    }

    // 4. Open Leave Policy Modal Overlay
    const policyBtn = container.querySelector('#btn-leave-policy');
    const policyModal = container.querySelector('#leave-policy-modal');
    const policyClose = container.querySelector('#leave-policy-close');

    if (policyBtn && policyModal) {
      const handlePolicyOpen = () => {
        policyModal.style.display = 'flex';
        policyModal.setAttribute('aria-hidden', 'false');
        
        // Dynamically update based on profile country
        const country = (this.profile?.country || 'France').toLowerCase();
        const descEl = container.querySelector('#policy-desc-country-info');
        const rulesList = container.querySelector('.general-rules-ul');
        
        if (descEl && rulesList) {
          if (country.includes('india')) {
            descEl.textContent = 'The following leave entitlements and rules apply to all employees as per company policy and Indian Labour Laws.';
            rulesList.innerHTML = `
              <li>Leave requests must be submitted at least the required notice period in advance (e.g. 7 days for Annual Leave).</li>
              <li>Sick leave exceeding 2 consecutive days requires a medical certificate.</li>
              <li>Protected leave types (🛡️) like Maternity, Paternity, and Bereavement are auto-approved per Labour Code guidelines.</li>
              <li>Casual and Personal leaves allow short term emergency time-off.</li>
              <li>Leave cannot be applied during active <strong>blackout periods</strong>.</li>
              <li>Unused annual leave is subject to carry-forward policy (max 10 days) and encashment.</li>
            `;
          } else if (country.includes('emirates') || country.includes('uae')) {
            descEl.textContent = 'The following leave entitlements and rules apply to all employees as per company policy and UAE Labour Law.';
            rulesList.innerHTML = `
              <li>Annual leave requests require at least 30 days notice to ensure store coverage.</li>
              <li>Sick leave is capped at 90 calendar days per year as per UAE Labour Law (first 15 days full pay, next 30 days half pay, next 45 days unpaid).</li>
              <li>Medical certificate is required after 2 consecutive days of sick leave.</li>
              <li>Protected leave types (🛡️) like Maternity and Paternity are auto-approved.</li>
              <li>Leave cannot be applied during active <strong>blackout periods</strong>.</li>
              <li>Unused annual leave is subject to carry-forward policy (max 15 days) and encashment.</li>
            `;
          } else {
            descEl.textContent = 'The following leave entitlements and rules apply to all employees as per company policy and French Labour Law.';
            rulesList.innerHTML = `
              <li>Leave requests must be submitted at least the required notice period in advance (e.g. 14 days for Annual Leave).</li>
              <li>Sick leave exceeding 2 consecutive days requires a medical certificate.</li>
              <li>Protected leave types (🛡️) are auto-approved per Labour Code guidelines.</li>
              <li>Unpaid leave does not accrue and is deducted from salary.</li>
              <li>Leave cannot be applied during active <strong>blackout periods</strong>.</li>
              <li>Approval workflow: Employee → Supervisor → Store Admin (for extended leaves).</li>
              <li>Unused annual leave is subject to carry-forward policy (max 5 days).</li>
            `;
          }
        }
      };
      
      const handlePolicyClose = () => {
        policyModal.style.display = 'none';
        policyModal.setAttribute('aria-hidden', 'true');
      };

      policyBtn.addEventListener('click', handlePolicyOpen);
      lifecycle.onCleanup(() => policyBtn.removeEventListener('click', handlePolicyOpen));

      if (policyClose) {
        policyClose.addEventListener('click', handlePolicyClose);
        lifecycle.onCleanup(() => policyClose.removeEventListener('click', handlePolicyClose));
      }
    }
  }

  calcWorkingDays(start, end, session = 'FULL_DAY') {
    if (session.startsWith('HALF_DAY')) return 0.5;
    let count = 0;
    const cur = new Date(start);
    const endDate = new Date(end);

    while (cur <= endDate) {
      const day = cur.getDay();
      if (day !== 0 && day !== 6) count++;
      cur.setDate(cur.getDate() + 1);
    }
    return count || 1;
  }

  _renderLoading(container) {
    const listContainer = container.querySelector('#leave-history-tbody');
    if (listContainer) {
      listContainer.innerHTML = `
        <tr>
          <td colspan="8" class="text-center" style="padding:24px;color:var(--text-muted);font-size:0.8rem;font-weight:600;text-align:center;">
            Loading applied leaves...
          </td>
        </tr>`;
    }
  }

  _renderMyLeaves(container) {
    const listContainer = container.querySelector('#leave-history-tbody');
    if (!listContainer) return;
    listContainer.replaceChildren();

    if (this.myLeaves.length === 0) {
      const emptyRow = document.createElement('tr');
      emptyRow.innerHTML = `
        <td colspan="8" class="text-center" style="padding:24px;color:var(--text-muted);font-size:0.8rem;font-weight:600;text-align:center;">
          No applied leave history found.
        </td>`;
      listContainer.appendChild(emptyRow);
      return;
    }

    this.myLeaves.forEach(leave => {
      const row = document.createElement('tr');

      // 1. Type
      const typeTd = document.createElement('td');
      typeTd.style.fontWeight = '700';
      typeTd.style.fontSize = '0.75rem';
      typeTd.style.color = 'var(--text-primary)';
      typeTd.textContent = leave.leaveType || 'Leave';

      // 2. Session
      const sessionTd = document.createElement('td');
      sessionTd.style.fontSize = '0.72rem';
      sessionTd.style.color = 'var(--text-muted)';
      sessionTd.textContent = leave.session || 'FULL_DAY';

      // 3. Days
      const daysTd = document.createElement('td');
      daysTd.style.fontWeight = '600';
      daysTd.style.fontSize = '0.75rem';
      daysTd.style.color = 'var(--text-primary)';
      daysTd.textContent = leave.totalDays;

      // 4. Start
      const startTd = document.createElement('td');
      startTd.style.fontSize = '0.72rem';
      startTd.style.color = 'var(--text-muted)';
      startTd.textContent = leave.startDate;

      // 5. End
      const endTd = document.createElement('td');
      endTd.style.fontSize = '0.72rem';
      endTd.style.color = 'var(--text-muted)';
      endTd.textContent = leave.endDate;

      // 6. Reason
      const reasonTd = document.createElement('td');
      reasonTd.style.fontSize = '0.72rem';
      reasonTd.style.color = 'var(--text-secondary)';
      reasonTd.style.maxWidth = '180px';
      reasonTd.style.overflow = 'hidden';
      reasonTd.style.textOverflow = 'ellipsis';
      reasonTd.style.whiteSpace = 'nowrap';
      reasonTd.textContent = leave.reason || '—';

      // 7. Status
      const statusTd = document.createElement('td');
      const badge = document.createElement('span');
      badge.className = `status-badge ${leave.status.toLowerCase()}`;
      badge.textContent = leave.status;
      statusTd.appendChild(badge);

      // 8. Action
      const actionTd = document.createElement('td');
      if (leave.status === 'PENDING') {
        const cancelBtn = document.createElement('button');
        cancelBtn.type = 'button';
        cancelBtn.className = 'btn-cancel-leave';
        cancelBtn.textContent = 'Cancel';
        cancelBtn.dataset.id = leave.id;
        actionTd.appendChild(cancelBtn);
      } else if (leave.status === 'APPROVED' && !leave.cancellationRequested) {
        const reqCancelBtn = document.createElement('button');
        reqCancelBtn.type = 'button';
        reqCancelBtn.className = 'btn-req-cancel-leave';
        reqCancelBtn.textContent = 'Req Cancel';
        reqCancelBtn.dataset.id = leave.id;
        actionTd.appendChild(reqCancelBtn);
      } else if (leave.requiresDocument && !leave.hasDocument) {
        const uploadBtn = document.createElement('button');
        uploadBtn.type = 'button';
        uploadBtn.className = 'btn-upload-doc';
        uploadBtn.textContent = 'Upload Doc';
        uploadBtn.dataset.id = leave.id;
        actionTd.appendChild(uploadBtn);
      } else if (leave.status === 'REJECTED' && leave.rejectedBySupervisor) {
        const escalateBtn = document.createElement('button');
        escalateBtn.type = 'button';
        escalateBtn.className = 'btn-escalate-leave';
        escalateBtn.textContent = 'Escalate';
        escalateBtn.dataset.id = leave.id;
        escalateBtn.style.background = 'rgba(235, 94, 40, 0.1)';
        escalateBtn.style.border = '1px solid var(--accent-primary)';
        escalateBtn.style.color = 'var(--accent-primary)';
        escalateBtn.style.padding = '2px 8px';
        escalateBtn.style.borderRadius = 'var(--radius-sm)';
        escalateBtn.style.fontSize = '0.68rem';
        escalateBtn.style.fontWeight = '700';
        escalateBtn.style.cursor = 'pointer';
        escalateBtn.style.transition = 'all var(--transition-fast)';
        escalateBtn.addEventListener('mouseenter', () => {
          escalateBtn.style.background = 'var(--accent-primary)';
          escalateBtn.style.color = '#fff';
        });
        escalateBtn.addEventListener('mouseleave', () => {
          escalateBtn.style.background = 'rgba(235, 94, 40, 0.1)';
          escalateBtn.style.color = 'var(--accent-primary)';
        });
        actionTd.appendChild(escalateBtn);
      } else {
        actionTd.style.color = 'var(--text-muted)';
        actionTd.style.fontSize = '0.72rem';
        actionTd.textContent = 'None';
      }

      row.appendChild(typeTd);
      row.appendChild(sessionTd);
      row.appendChild(daysTd);
      row.appendChild(startTd);
      row.appendChild(endTd);
      row.appendChild(reasonTd);
      row.appendChild(statusTd);
      row.appendChild(actionTd);

      listContainer.appendChild(row);
    });
  }

  _renderBalances(container) {
    const grid = container.querySelector('#leave-balances-grid');
    if (!grid) return;

    grid.replaceChildren();

    if (!Array.isArray(this.balances) || this.balances.length === 0) {
      grid.innerHTML = '<div class="text-center text-muted col-span-full py-4 text-xs font-semibold">No leave balances found</div>';
      return;
    }

    const iconMap = {
      'ANNUAL': 'plane',
      'SICK': 'activity',
      'PERSONAL': 'user',
      'MATERNITY': 'baby',
      'PATERNITY': 'baby',
      'BEREAVEMENT': 'heart',
      'CASUAL': 'coffee'
    };

    this.balances.forEach(bal => {
      const card = document.createElement('div');
      card.className = 'card glass kpi-block-card';

      const iconName = iconMap[bal.leaveTypeCode] || 'calendar';
      
      // Calculate dynamic taken, pending and rejected from this.myLeaves history to be extra responsive and accurate!
      let used = 0;
      let pending = 0;
      let rejected = 0;
      
      if (Array.isArray(this.myLeaves)) {
        this.myLeaves.forEach(l => {
          const leaveName = String(l.leaveType || l.leaveTypeCode || '').toLowerCase();
          const balanceName = String(bal.leaveTypeName || bal.leaveTypeCode || '').toLowerCase();
          
          if (leaveName.includes(balanceName) || balanceName.includes(leaveName)) {
            const days = parseFloat(l.totalDays || l.daysCount || 0);
            if (l.status === 'APPROVED') {
              used += days;
            } else if (l.status === 'PENDING') {
              pending += days;
            } else if (l.status === 'REJECTED') {
              rejected += days;
            }
          }
        });
      }

      // Fallback to database values if no transactions found
      if (used === 0 && bal.used > 0) used = bal.used;
      if (pending === 0 && bal.pending > 0) pending = bal.pending;

      const opening = bal.openingBalance !== null && bal.openingBalance !== undefined ? parseFloat(bal.openingBalance) : 0;
      const accrued = bal.accrued !== null && bal.accrued !== undefined ? parseFloat(bal.accrued) : 0;
      const carryForward = bal.carryForward !== null && bal.carryForward !== undefined ? parseFloat(bal.carryForward) : 0;
      
      const remaining = Math.max(0, opening + accrued + carryForward - used);

      card.innerHTML = `
        <div class="kpi-header">
          <span class="kpi-label">${bal.leaveTypeName}</span>
          <i data-lucide="${iconName}" class="kpi-icon text-accent" aria-hidden="true"></i>
        </div>
        <div class="kpi-details">
          <div class="kpi-value">${remaining} <span>left</span></div>
          <div class="kpi-sub-badges">
            <span class="kpi-sub-badge-used text-xs">Taken: ${used}d</span>
            ${pending > 0 ? `<span class="kpi-sub-badge-pending text-xs">Pending: ${pending}d</span>` : ''}
            ${rejected > 0 ? `<span class="kpi-sub-badge-rejected text-xs" style="color:var(--status-danger);font-weight:600;">Rejected: ${rejected}d</span>` : ''}
          </div>
        </div>
      `;
      grid.appendChild(card);
    });

    if (window.lucide) window.lucide.createIcons();
  }

  _renderLeaveTypes(container) {
    const typeSelect = container.querySelector('#leave-select-type');
    if (!typeSelect) return;
    typeSelect.innerHTML = '<option value="" disabled selected>Select category...</option>';

    this.leaveTypes.forEach(lt => {
      const opt = document.createElement('option');
      opt.value = lt.id;
      opt.textContent = `${lt.name}${lt.isProtected ? ' 🛡️' : ''}`;
      opt.dataset.doc = lt.requiresDocument ? 'true' : 'false';
      opt.dataset.protected = lt.isProtected ? 'true' : 'false';
      typeSelect.appendChild(opt);
    });
  }

  _renderHolidays(container) {
    const list = container.querySelector('#holidays-details-container');
    const title = container.querySelector('#holidays-title');
    const moreText = container.querySelector('#holidays-more-text');

    const country = (this.profile?.country || 'France').toLowerCase();
    let flag = '🇫🇷';
    if (country.includes('india')) flag = '🇮🇳';
    else if (country.includes('emirates') || country.includes('uae')) flag = '🇦🇪';

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
    if (!tbody) return;
    tbody.replaceChildren();

    const country = (this.profile?.country || 'France').toLowerCase();
    const lbl = container.querySelector('#lbl-country-law');
    if (lbl) {
      if (country.includes('india')) {
        lbl.textContent = 'Indian Labour Laws Compliant';
      } else if (country.includes('emirates') || country.includes('uae')) {
        lbl.textContent = 'UAE Labour Law Compliant';
      } else {
        lbl.textContent = 'French Labour Law Compliant';
      }
    }

    if (this.leaveTypes.length === 0) {
      tbody.innerHTML = '<tr><td colspan="8" class="text-center text-muted">No policy rules found.</td></tr>';
      return;
    }

    this.leaveTypes.forEach(lt => {
      const row = document.createElement('tr');
      row.className = 'table-row';

      // 1. Leave Type
      const typeTd = document.createElement('td');
      typeTd.style.fontWeight = '700';
      typeTd.style.color = 'var(--text-primary)';
      typeTd.textContent = lt.name + (lt.protected ? ' 🛡️' : '');
      row.appendChild(typeTd);

      // 2. Annual Limit
      const limitTd = document.createElement('td');
      limitTd.style.textAlign = 'center';
      limitTd.textContent = lt.annualLimit !== null && lt.annualLimit !== undefined ? `${lt.annualLimit} days` : 'Unlimited';
      row.appendChild(limitTd);

      // 3. Accrual/Month
      const accrualTd = document.createElement('td');
      accrualTd.style.textAlign = 'center';
      accrualTd.textContent = lt.monthlyAccrual !== null && lt.monthlyAccrual !== undefined ? `${lt.monthlyAccrual} days` : '0';
      row.appendChild(accrualTd);

      // 4. Max Consecutive
      const consecTd = document.createElement('td');
      consecTd.style.textAlign = 'center';
      consecTd.textContent = lt.maxConsecutiveDays !== null && lt.maxConsecutiveDays !== undefined ? `${lt.maxConsecutiveDays} days` : 'No Limit';
      row.appendChild(consecTd);

      // 5. Notice (days)
      const noticeTd = document.createElement('td');
      noticeTd.style.textAlign = 'center';
      noticeTd.textContent = lt.minNoticeDays !== null && lt.minNoticeDays !== undefined ? `${lt.minNoticeDays} days` : '0';
      row.appendChild(noticeTd);

      // 6. Paid
      const paidTd = document.createElement('td');
      paidTd.style.textAlign = 'center';
      paidTd.innerHTML = lt.paid 
        ? '<span style="color:var(--status-success);font-weight:bold;">Yes</span>' 
        : '<span style="color:var(--status-danger);font-weight:bold;">No</span>';
      row.appendChild(paidTd);

      // 7. Protected
      const protTd = document.createElement('td');
      protTd.style.textAlign = 'center';
      protTd.innerHTML = lt.protected 
        ? '<span style="color:var(--status-success);font-weight:bold;">Yes</span>' 
        : '<span style="color:var(--text-muted);">No</span>';
      row.appendChild(protTd);

      // 8. Document
      const docTd = document.createElement('td');
      docTd.style.textAlign = 'center';
      docTd.textContent = lt.requiresDocument ? 'Required' : 'Optional';
      row.appendChild(docTd);

      tbody.appendChild(row);
    });
  }
}
