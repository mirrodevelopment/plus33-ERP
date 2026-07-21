/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Admin Module
 * File              : workforce.js
 * Path              : frontend/modules/store-admin/pages/workforce/workforce.js
 * Purpose           : Frontend component file supplying interface presentation, layout structures, or UI logic for Store Admin Module
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Implementation component part of the PLUS33 Coffee ERP web application SPA architecture.
 ******************************************************************************/
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { eventBus } from '../../../../core/eventBus.js';

const TEMPLATE_URL = 'modules/store-admin/pages/workforce/workforce.html';

export default class StoreWorkforceModule {
  constructor() {
    this.storeId = null;
    this.storeName = '';
    this.employees = [];
    this.filteredEmployees = [];
    
    // Pagination state
    this.currentPage = 0;
    this.pageSize = 10;
    
    // Filters state
    this.searchQuery = '';
    this.statusFilter = 'ACTIVE';
    this.designationFilter = 'ALL';
    this.employmentFilter = 'ALL';
    
    this.escalatedSwaps = [];
    this.storeEscalatedEmployees = []; // for replacement picker in escalation modal
  }

  async mount(container, lifecycle) {
    logger.info('StoreWorkforce', 'Mounting Store Workforce Module...');
    this._loadCss();

    // 1. Inject HTML layout
    await this._loadTemplate(container);

    // 2. Load live employee data
    await this._loadData();

    // 3. Render and bind
    this._render(container);
    this._bindEvents(container, lifecycle);

    // 4. Fetch pending document count and update sidebar/bell indicators
    await this._fetchAndEmitPendingDocsCount();
  }

  /**
   * Fetches pending document verification count for this store and emits
   * the workforce:pending-docs event so the sidebar dot and bell badge update.
   */
  async _fetchAndEmitPendingDocsCount() {
    try {
      const res = await apiClient.get('/api/v1/dashboard/overview');
      const pendingDocs = Number(
        res?.data?.workforceOverview?.pendingDocuments ||
        res?.workforceOverview?.pendingDocuments || 0
      );
      // Emit the count so the layout can update the nav dot and bell badge
      eventBus.emit('workforce:pending-docs', { count: pendingDocs });
      logger.debug('StoreWorkforce', `Pending docs count emitted: ${pendingDocs}`);
    } catch (err) {
      logger.warn('StoreWorkforce', 'Could not fetch pending docs count for badge.', err);
    }
  }

  _loadCss() {
    const cssId = 'store-workforce-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-admin/pages/workforce/workforce.css';
      document.head.appendChild(link);
    }
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  async _loadData() {
    try {
      // 1. Fetch current logged-in user to find store assigned
      const meRes = await apiClient.get('/api/v1/auth/me');
      if (meRes && meRes.success && meRes.data) {
        this.storeId = meRes.data.storeId;
        this.storeName = meRes.data.store || 'This Store';
      }

      if (!this.storeId) {
        logger.warn('StoreWorkforce', 'No storeId linked to user session. Defaulting to Store 1.');
        this.storeId = 1;
        this.storeName = 'Paris Versailles';
      }

      // 2. Fetch all employees associated with this store (using page size 100 to load all of them)
      const empRes = await apiClient.get(`/api/v1/employees?storeId=${this.storeId}&size=100`);
      if (empRes && empRes.success && empRes.data) {
        this.employees = empRes.data.content || [];
      } else {
        this.employees = [];
      }

      // Fetch escalated requests
      const swapsRes = await apiClient.get('/api/v1/shift-swaps/escalated-requests');
      const swapData = (swapsRes && swapsRes.success) ? swapsRes.data : {};
      this.escalatedSwaps = Array.isArray(swapData) ? swapData : (swapData.swaps || []);
      this.storeEscalatedEmployees = swapData.storeEmployees || [];

      this._applyFilters();

    } catch (err) {
      logger.error('StoreWorkforce', 'Failed to fetch workforce data.', err);
      notificationStore.error('Unable to fetch store workforce roster.');
    }
  }

  _applyFilters() {
    this.filteredEmployees = this.employees.filter(emp => {
      // Search term matching
      if (this.searchQuery) {
        const query = this.searchQuery.toLowerCase();
        const codeMatch = emp.employeeCode && emp.employeeCode.toLowerCase().includes(query);
        const firstNameMatch = emp.firstName && emp.firstName.toLowerCase().includes(query);
        const lastNameMatch = emp.lastName && emp.lastName.toLowerCase().includes(query);
        const emailMatch = emp.email && emp.email.toLowerCase().includes(query);
        
        if (!codeMatch && !firstNameMatch && !lastNameMatch && !emailMatch) {
          return false;
        }
      }

      // Status filter matching
      if (this.statusFilter !== 'ALL') {
        const isActive = this.statusFilter === 'ACTIVE';
        if (emp.active !== isActive) {
          return false;
        }
      }

      // Designation filter matching
      if (this.designationFilter !== 'ALL') {
        if (emp.designation !== this.designationFilter) {
          return false;
        }
      }

      // Employment type filter matching
      if (this.employmentFilter !== 'ALL') {
        if (emp.employmentType !== this.employmentFilter) {
          return false;
        }
      }

      return true;
    });

    this.currentPage = 0; // reset page on filter change
  }

  _render(container) {
    if (!container) return;

    // 1. Calculate KPI Metrics
    this._renderKPIs(container);

    // 2. Render Table Body
    this._renderTableBody(container);

    // 3. Render Pagination
    this._renderPagination(container);

    // 4. Render Escalated Shift Swap Requests
    this._renderEscalatedSwaps(container);
  }

  _renderKPIs(container) {
    const activeEmployees = this.employees.filter(e => e.active);
    const totalWorkers = activeEmployees.length;

    const presentCount = activeEmployees.filter(e => {
      const status = e.todayStatus || 'ABSENT';
      return status.startsWith('PRESENT') || status === 'LATE' || status === 'PRESENT_HALF';
    }).length;

    const leaveCount = activeEmployees.filter(e => e.todayStatus === 'ON_LEAVE').length;

    const absentCount = totalWorkers - presentCount - leaveCount;

    const totalPayroll = activeEmployees.reduce((sum, e) => sum + (e.salary || 2400.00), 0);

    const totalWorkersElem = container.querySelector('#kpi-total-workers');
    const presentElem = container.querySelector('#kpi-present-today');
    const absentElem = container.querySelector('#kpi-absent-today');
    const leaveElem = container.querySelector('#kpi-leave-today');
    const payrollElem = container.querySelector('#kpi-total-payroll');

    if (totalWorkersElem) totalWorkersElem.textContent = totalWorkers;
    if (presentElem) presentElem.textContent = presentCount;
    if (absentElem) absentElem.textContent = Math.max(0, absentCount);
    if (leaveElem) leaveElem.textContent = leaveCount;
    if (payrollElem) {
      payrollElem.textContent = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'EUR',
        notation: 'compact',
        maximumFractionDigits: 1
      }).format(totalPayroll);
    }
  }

  _renderTableBody(container) {
    const tbody = container.querySelector('#workforce-table-body');
    if (!tbody) return;

    if (this.filteredEmployees.length === 0) {
      tbody.innerHTML = `
        <tr>
          <td colspan="8" class="empty-state">
            <i data-lucide="info" style="width: 2rem; height: 2rem; margin-bottom: 0.5rem; opacity: 0.5;"></i>
            <p>No matching store employees found.</p>
          </td>
        </tr>
      `;
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    const startIdx = this.currentPage * this.pageSize;
    const endIdx = Math.min(startIdx + this.pageSize, this.filteredEmployees.length);
    const paginatedItems = this.filteredEmployees.slice(startIdx, endIdx);

    tbody.innerHTML = paginatedItems.map(emp => {
      const initials = `${emp.firstName ? emp.firstName[0] : ''}${emp.lastName ? emp.lastName[0] : ''}`;
      const fullName = `${emp.firstName || ''} ${emp.lastName || ''}`.trim();
      const hireDateFormatted = emp.hireDate || 'N/A';
      
      // Calculate tenure string
      let tenureStr = 'N/A';
      if (emp.hireDate) {
        const hire = new Date(emp.hireDate);
        const now = new Date();
        const diffMonths = (now.getFullYear() - hire.getFullYear()) * 12 + (now.getMonth() - hire.getMonth());
        const yrs = Math.floor(diffMonths / 12);
        const mos = diffMonths % 12;
        tenureStr = yrs > 0 ? `${yrs}y ${mos}m` : `${mos}mo`;
      }

      const activeShiftStr = emp.activeShift || 'No Shift Assigned';
      const salaryFormatted = new Intl.NumberFormat('fr-FR', {
        style: 'currency',
        currency: 'EUR'
      }).format(emp.salary || 2400.00);

      const statusBadge = emp.active
        ? `<span class="badge-status badge-active">Active</span>`
        : `<span class="badge-status badge-inactive">Inactive</span>`;

      const avatarHtml = emp.avatarUrl
        ? `<img src="${emp.avatarUrl}" alt="${fullName}" style="width: 100%; height: 100%; border-radius: 50%; object-fit: cover;">`
        : (initials || 'EE');

      return `
        <tr>
          <td>
            <div class="emp-meta-wrapper">
              <div class="emp-avatar-placeholder" style="display: flex; align-items: center; justify-content: center; overflow: hidden; background: rgba(255,255,255,0.05); border: 1px solid rgba(255,255,255,0.1); width: 36px; height: 36px; border-radius: 50%; font-weight: 700; font-size: 0.78rem; color: var(--accent-primary); flex-shrink: 0;">${avatarHtml}</div>
              <div class="emp-text-details">
                <span class="emp-full-name">${fullName}</span>
                <span class="emp-sub-code">Code: ${emp.employeeCode || 'N/A'} • Username: ${emp.userEmail || 'N/A'}</span>
                <span class="emp-contacts">${emp.email || ''} ${emp.phone ? '• ' + emp.phone : ''}</span>
              </div>
            </div>
          </td>
          <td>
            <div class="emp-role-title">${emp.designation || 'Barista'}</div>
            <div class="emp-dept-label">${emp.department || 'Retail Operations'}</div>
          </td>
          <td>${hireDateFormatted}</td>
          <td>${tenureStr}</td>
          <td>${activeShiftStr}</td>
          <td style="font-weight: 500;">${salaryFormatted}</td>
          <td>${statusBadge}</td>
          <td style="text-align: right;">
            <button type="button" class="btn-details-action" data-emp-id="${emp.id}" title="View Details">
              <i data-lucide="chevron-right"></i>
            </button>
          </td>
        </tr>
      `;
    }).join('');

    // Bind details buttons
    tbody.querySelectorAll('.btn-details-action').forEach(btn => {
      btn.addEventListener('click', () => {
        const empId = parseInt(btn.getAttribute('data-emp-id'));
        const emp = this.filteredEmployees.find(e => e.id === empId);
        if (emp) {
          this._openDetailsDrawer(container, emp);
        }
      });
    });

    if (window.lucide) {
      window.lucide.createIcons();
    }
  }

  _renderPagination(container) {
    const summary = container.querySelector('#pagination-summary');
    const pagesContainer = container.querySelector('#pagination-pages');
    const btnPrev = container.querySelector('#btn-page-prev');
    const btnNext = container.querySelector('#btn-page-next');

    if (!summary || !pagesContainer || !btnPrev || !btnNext) return;

    const totalElements = this.filteredEmployees.length;
    const totalPages = Math.ceil(totalElements / this.pageSize);

    const start = totalElements === 0 ? 0 : this.currentPage * this.pageSize + 1;
    const end = Math.min((this.currentPage + 1) * this.pageSize, totalElements);

    summary.textContent = `Showing ${start} to ${end} of ${totalElements} employees`;

    // Render page number buttons
    pagesContainer.innerHTML = '';
    for (let i = 0; i < totalPages; i++) {
      const btn = document.createElement('button');
      btn.type = 'button';
      btn.className = `btn-page-number ${i === this.currentPage ? 'active' : ''}`;
      btn.textContent = i + 1;
      btn.addEventListener('click', () => {
        this.currentPage = i;
        this._renderTableBody(container);
        this._renderPagination(container);
      });
      pagesContainer.appendChild(btn);
    }

    btnPrev.disabled = this.currentPage === 0;
    btnNext.disabled = this.currentPage >= totalPages - 1 || totalPages === 0;
  }

  _bindEvents(container, lifecycle) {
    const searchInput = container.querySelector('#workforce-search');
    const statusSelect = container.querySelector('#filter-status');
    const designationSelect = container.querySelector('#filter-designation');
    const employmentSelect = container.querySelector('#filter-employment');

    const btnPrev = container.querySelector('#btn-page-prev');
    const btnNext = container.querySelector('#btn-page-next');

    const handleFilterChange = () => {
      this.searchQuery = searchInput ? searchInput.value.trim() : '';
      this.statusFilter = statusSelect ? statusSelect.value : 'ACTIVE';
      this.designationFilter = designationSelect ? designationSelect.value : 'ALL';
      this.employmentFilter = employmentSelect ? employmentSelect.value : 'ALL';
      
      this._applyFilters();
      this._render(container);
    };

    if (searchInput) searchInput.addEventListener('input', handleFilterChange);
    if (statusSelect) statusSelect.addEventListener('change', handleFilterChange);
    if (designationSelect) designationSelect.addEventListener('change', handleFilterChange);
    if (employmentSelect) employmentSelect.addEventListener('change', handleFilterChange);

    if (btnPrev) {
      btnPrev.addEventListener('click', () => {
        if (this.currentPage > 0) {
          this.currentPage--;
          this._renderTableBody(container);
          this._renderPagination(container);
        }
      });
    }

    if (btnNext) {
      btnNext.addEventListener('click', () => {
        const totalPages = Math.ceil(this.filteredEmployees.length / this.pageSize);
        if (this.currentPage < totalPages - 1) {
          this.currentPage++;
          this._renderTableBody(container);
          this._renderPagination(container);
        }
      });
    }

    const btnClose = container.querySelector('#btn-close-drawer');
    if (btnClose) {
      btnClose.addEventListener('click', () => {
        this._closeDetailsDrawer(container);
      });
    }

    // Close details drawer if clicked outside the drawer container boundaries
    // WHAT IT DOES: 
    // Captures all click events on the document body. If the drawer is currently open and 
    // the clicked target is outside the drawer panel (and not one of the trigger action buttons), 
    // it automatically slides the drawer shut.
    // 
    // SENDS DATA / FLOW:
    // Triggers local UI state change (removing 'open' class from drawer element).
    this._outsideClickListener = (event) => {
      const drawer = container.querySelector('#employee-details-drawer');
      if (drawer && drawer.classList.contains('open')) {
        const clickInside = drawer.contains(event.target);
        const clickActionButton = event.target.closest('.btn-details-action');
        if (!clickInside && !clickActionButton) {
          this._closeDetailsDrawer(container);
        }
      }
    };
    document.addEventListener('click', this._outsideClickListener);

    // Register cleanup callback on component unmount to prevent memory leaks
    if (lifecycle && typeof lifecycle.onCleanup === 'function') {
      lifecycle.onCleanup(() => {
        document.removeEventListener('click', this._outsideClickListener);
      });
    }

    // Bind escalated swap request buttons
    let activeEscalationId = null;
    const approveSwapBtns = container.querySelectorAll('.btn-approve-escalation');
    approveSwapBtns.forEach(btn => {
      const handleApprove = () => {
        activeEscalationId = btn.getAttribute('data-id');
        const employeeName     = btn.getAttribute('data-employee-name');
        const shiftDateDisplay = btn.getAttribute('data-shift-date-display');
        const currentShift     = btn.getAttribute('data-current-shift');
        const preferredShiftName = btn.getAttribute('data-preferred-shift-name');
        const preferredDate    = btn.getAttribute('data-preferred-date');

        const overlay = container.querySelector('#escalation-approval-overlay');
        if (!overlay) return;

        const summary = overlay.querySelector('#escalation-modal-summary');
        if (summary) {
          summary.innerHTML = `
            <div style="font-weight:700; color:var(--text-primary); margin-bottom:8px; font-size:0.85rem;">${employeeName}</div>
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:8px; font-size:0.78rem;">
              <div><span style="color:var(--text-muted);">Vacating shift:</span><br><strong>${currentShift}</strong> on <strong>${shiftDateDisplay}</strong></div>
              <div><span style="color:var(--text-muted);">Requested new shift:</span><br><strong>${preferredShiftName}</strong> on <strong>${preferredDate || 'Not specified'}</strong></div>
            </div>
          `;
        }

        const vacancyInfo = overlay.querySelector('#escalation-vacancy-info');
        if (vacancyInfo) {
          vacancyInfo.textContent = `After approval, ${currentShift} on ${shiftDateDisplay} will be empty. Assign a replacement worker below.`;
        }

        const dateInput = overlay.querySelector('#escalation-approved-date');
        if (dateInput) {
          dateInput.value = preferredDate || '';
          const today = new Date(); today.setHours(0,0,0,0);
          const min = new Date(today); min.setDate(min.getDate() + 1);
          dateInput.min = min.toISOString().split('T')[0];
        }

        const shiftSel = overlay.querySelector('#escalation-approved-shift');
        if (shiftSel) shiftSel.value = '';
        const replSel = overlay.querySelector('#escalation-replacement-employee');
        if (replSel) replSel.value = '';

        overlay.style.display = 'flex';
      };
      btn.addEventListener('click', handleApprove);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleApprove));
    });

    // Wire escalation Confirm Approval button
    const escalationConfirmBtn = container.querySelector('#btn-escalation-confirm');
    if (escalationConfirmBtn) {
      const handleConfirm = async () => {
        if (!activeEscalationId) return;
        const overlay = container.querySelector('#escalation-approval-overlay');

        const approvedDate    = overlay?.querySelector('#escalation-approved-date')?.value || '';
        const approvedShiftId = overlay?.querySelector('#escalation-approved-shift')?.value || '';
        const replacementId   = overlay?.querySelector('#escalation-replacement-employee')?.value || '';

        const payload = {};
        if (approvedDate)    payload.approvedDate    = approvedDate;
        if (approvedShiftId) payload.approvedShiftId = parseInt(approvedShiftId);
        if (replacementId)   payload.replacementEmployeeId = parseInt(replacementId);

        escalationConfirmBtn.disabled = true;
        escalationConfirmBtn.textContent = 'Approving...';
        try {
          const res = await apiClient.post(`/api/v1/shift-swaps/${activeEscalationId}/admin-approve`, payload);
          if (res && res.success) {
            if (overlay) overlay.style.display = 'none';
            notificationStore.success('Escalated swap approved — roster updated successfully.');
            await this._loadData();
            this._render(container);
            this._bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to approve escalated request.');
            escalationConfirmBtn.disabled = false;
            escalationConfirmBtn.textContent = '✓ Confirm Approval';
          }
        } catch (err) {
          logger.error('StoreWorkforce', 'Error approving escalation:', err);
          notificationStore.danger('Error approving escalation.');
          escalationConfirmBtn.disabled = false;
          escalationConfirmBtn.textContent = '✓ Confirm Approval';
        }
      };
      escalationConfirmBtn.addEventListener('click', handleConfirm);
      lifecycle.onCleanup(() => escalationConfirmBtn.removeEventListener('click', handleConfirm));
    }

    const rejectSwapBtns = container.querySelectorAll('.btn-reject-escalation');
    rejectSwapBtns.forEach(btn => {
      const handleReject = async () => {
        const id = btn.getAttribute('data-id');
        const reason = prompt('Enter admin rejection reason (mandatory):');
        if (reason === null) return;
        if (!reason.trim()) {
          notificationStore.danger('Rejection reason is mandatory.');
          return;
        }
        try {
          const res = await apiClient.post(`/api/v1/shift-swaps/${id}/admin-reject`, { reason: reason.trim() });
          if (res && res.success) {
            notificationStore.success('Escalated request rejected.');
            await this._loadData();
            this._render(container);
            this._bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to reject escalated request.');
          }
        } catch (err) {
          logger.error('StoreWorkforce', 'Error rejecting escalation:', err);
          notificationStore.danger('Error rejecting escalation.');
        }
      };
      btn.addEventListener('click', handleReject);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleReject));
    });
  }

  _openDetailsDrawer(container, emp) {
    const drawer = container.querySelector('#employee-details-drawer');
    if (!drawer) return;

    const initials = `${emp.firstName ? emp.firstName[0] : ''}${emp.lastName ? emp.lastName[0] : ''}`;
    const fullName = `${emp.firstName || ''} ${emp.lastName || ''}`.trim();
    
    let tenureStr = 'N/A';
    if (emp.hireDate) {
      const hire = new Date(emp.hireDate);
      const now = new Date();
      const diffMonths = (now.getFullYear() - hire.getFullYear()) * 12 + (now.getMonth() - hire.getMonth());
      const yrs = Math.floor(diffMonths / 12);
      const mos = diffMonths % 12;
      tenureStr = yrs > 0 ? `${yrs}y ${mos}m` : `${mos}mo`;
    }

    const salaryFormatted = new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR'
    }).format(emp.salary || 2400.00);

    const avatarElem = container.querySelector('#drawer-avatar');
    const nameElem = container.querySelector('#drawer-full-name');
    const codeElem = container.querySelector('#drawer-code');
    const designationElem = container.querySelector('#drawer-designation');
    const departmentElem = container.querySelector('#drawer-department');
    const employmentTypeElem = container.querySelector('#drawer-employment-type');
    const statusElem = container.querySelector('#drawer-status');
    const hireDateElem = container.querySelector('#drawer-hire-date');
    const tenureElem = container.querySelector('#drawer-tenure');
    const activeShiftElem = container.querySelector('#drawer-active-shift');
    const salaryElem = container.querySelector('#drawer-salary');
    const emailElem = container.querySelector('#drawer-email');
    const usernameElem = container.querySelector('#drawer-username');
    const phoneElem = container.querySelector('#drawer-phone');
    const storeElem = container.querySelector('#drawer-store');
    const regionElem = container.querySelector('#drawer-region');

    if (avatarElem) {
      if (emp.avatarUrl) {
        avatarElem.innerHTML = `<img src="${emp.avatarUrl}" alt="${fullName}" style="width: 100%; height: 100%; border-radius: 50%; object-fit: cover;">`;
      } else {
        avatarElem.innerHTML = initials || 'EE';
      }
    }
    if (nameElem) nameElem.textContent = fullName;
    if (codeElem) codeElem.textContent = emp.employeeCode || 'N/A';
    if (designationElem) designationElem.textContent = emp.designation || 'Barista';
    if (departmentElem) departmentElem.textContent = emp.department || 'Retail Operations';
    if (employmentTypeElem) employmentTypeElem.textContent = emp.employmentType || 'N/A';
    if (statusElem) statusElem.textContent = emp.status || 'ACTIVE';
    if (hireDateElem) hireDateElem.textContent = emp.hireDate || 'N/A';
    if (tenureElem) tenureElem.textContent = tenureStr;
    if (activeShiftElem) activeShiftElem.textContent = emp.activeShift || 'No Shift Assigned';
    if (salaryElem) salaryElem.textContent = salaryFormatted;
    if (emailElem) emailElem.textContent = emp.email || 'N/A';
    if (usernameElem) usernameElem.textContent = emp.userEmail || 'N/A';
    if (phoneElem) phoneElem.textContent = emp.phone || 'N/A';
    if (storeElem) storeElem.textContent = emp.storeName || this.storeName || 'N/A';
    if (regionElem) regionElem.textContent = emp.regionName || 'N/A';

    // WHAT IT DOES: 
    // Triggers retrieval of the onboarding and identity verification documents for the selected employee.
    // 
    // DATA SOURCE (ORIGIN):
    // GET request to REST endpoint '/api/v1/employees/{id}/documents' (EmployeeController.java).
    // 
    // STORAGE/RENDERING: 
    // Delegates rendering and event binding (e.g. Approve buttons) to _loadDrawerDocuments().
    this._loadDrawerDocuments(container, emp.id);

    drawer.classList.add('open');
  }

  /**
   * Loads and renders the list of documents inside the workforce details drawer.
   * Handles checkmarks for approved documents and allows admins to approve pending ones in-place.
   */
  _loadDrawerDocuments(container, employeeId) {
    const docsListContainer = container.querySelector('#drawer-documents-list');
    if (!docsListContainer) return;
    
    docsListContainer.innerHTML = '<span style="opacity: 0.5; font-size: 0.85rem;">Loading documents...</span>';
    apiClient.get(`/api/v1/employees/${employeeId}/documents`)
      .then(docsRes => {
        const docs = docsRes && docsRes.success ? docsRes.data : [];
        if (Array.isArray(docs)) {
          if (docs.length === 0) {
            docsListContainer.innerHTML = '<span style="opacity: 0.5; font-size: 0.85rem; padding: 0.25rem 0;">No verification documents uploaded.</span>';
          } else {
            docsListContainer.innerHTML = docs.map(doc => {
              let docLabel = doc.documentType;
              if (doc.documentType === 'panCard') docLabel = 'PAN Card (Permanent Account Number)';
              else if (doc.documentType === 'aadhaarCard') docLabel = 'Aadhaar Card (UIDAI)';
              else if (doc.documentType === 'workPermit') docLabel = 'Work Permit / Contract Agreement';
              else if (doc.documentType === 'contract') docLabel = 'Contract';
              else if (doc.documentType === 'payslip') docLabel = 'Payslip Receipt';
              
              const actionButtons = `
                <div class="doc-actions-container" style="display: flex; align-items: center; gap: 8px;">
                  <a href="${doc.filePath}" target="_blank" class="btn-view-doc" style="padding: 0.35rem 0.6rem; border-radius: 4px; display: inline-flex; align-items: center; gap: 4px; background: rgba(255,255,255,0.05); color: #fff; text-decoration: none; border: 1px solid rgba(255,255,255,0.1); font-size: 0.7rem;">
                    <i data-lucide="eye" style="width: 12px; height: 12px;"></i> View
                  </a>
                  ${doc.approved ? `
                    <span class="doc-status-approved" style="display: inline-flex; align-items: center; gap: 4px; font-size: 0.7rem; color: #22c55e; font-weight: 600; background: rgba(34, 197, 94, 0.1); padding: 0.35rem 0.6rem; border-radius: 4px; border: 1px solid rgba(34, 197, 94, 0.2);">
                      <i data-lucide="check-circle" style="width: 12px; height: 12px;"></i> Approved
                    </span>
                  ` : `
                    <button class="btn-approve-doc" data-doc-id="${doc.id}" style="padding: 0.35rem 0.6rem; border-radius: 4px; display: inline-flex; align-items: center; gap: 4px; background: rgba(212, 163, 115, 0.1); color: var(--accent-primary); border: 1px solid rgba(212, 163, 115, 0.3); font-size: 0.7rem; cursor: pointer; transition: all 0.2s;">
                      <i data-lucide="check" style="width: 12px; height: 12px;"></i> Approve
                    </button>
                  `}
                </div>
              `;
              
              return `
                <div class="doc-card-item animate-slide-up" style="display: flex; align-items: center; justify-content: space-between; background: rgba(255,255,255,0.03); padding: 0.6rem 0.8rem; border-radius: 6px; border: 1px solid rgba(255,255,255,0.05); gap: 10px; margin-bottom: 8px;">
                  <div class="doc-card-info" style="display: flex; flex-direction: column; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; flex-grow: 1;">
                    <span class="doc-card-title" style="font-weight: 600; font-size: 0.82rem; color: #fff;">${docLabel}</span>
                    <span class="doc-card-sub" style="font-size: 0.72rem; opacity: 0.5; overflow: hidden; text-overflow: ellipsis;">${doc.documentName}</span>
                  </div>
                  ${actionButtons}
                </div>
              `;
            }).join('');
            
            if (window.lucide) window.lucide.createIcons();
            
            // Add event listeners to approve buttons
            docsListContainer.querySelectorAll('.btn-approve-doc').forEach(btn => {
              btn.addEventListener('click', (e) => {
                const docId = e.currentTarget.getAttribute('data-doc-id');
                btn.disabled = true;
                btn.innerHTML = 'Approving...';
                
                apiClient.post(`/api/v1/employees/documents/${docId}/approve`)
                  .then(approveRes => {
                    if (approveRes && approveRes.success) {
                      notificationStore.success('Verification document approved successfully.');
                      this._loadDrawerDocuments(container, employeeId);
                    } else {
                      throw new Error(approveRes?.message || 'Failed to approve document.');
                    }
                  })
                  .catch(err => {
                    logger.error('StoreWorkforce', 'Failed to approve document:', err);
                    notificationStore.danger(err.message || 'Failed to approve document.');
                    btn.disabled = false;
                    btn.innerHTML = '<i data-lucide="check"></i> Approve';
                    if (window.lucide) window.lucide.createIcons();
                  });
              });
            });
          }
        } else {
          docsListContainer.innerHTML = '<span style="color: var(--status-danger); font-size: 0.85rem;">Invalid document response format.</span>';
        }
      })
      .catch(err => {
        logger.error('StoreWorkforce', 'Failed to retrieve employee documents:', err);
        docsListContainer.innerHTML = '<span style="color: var(--status-danger); font-size: 0.85rem;">Error loading verification documents.</span>';
      });
  }

  _closeDetailsDrawer(container) {
    const drawer = container.querySelector('#employee-details-drawer');
    if (drawer) {
      drawer.classList.remove('open');
    }
  }

  _renderEscalatedSwaps(container) {
    const tbody = container.querySelector('#escalated-swaps-table-body');
    if (!tbody) return;

    tbody.replaceChildren();

    if ((this.escalatedSwaps || []).length === 0) {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td colspan="7" style="text-align: center; padding: 24px; color: var(--text-muted); font-style: italic;">
          No escalated swap requests pending review.
        </td>
      `;
      tbody.appendChild(tr);
      return;
    }

    this.escalatedSwaps.forEach(sw => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td style="padding: 12px; border-bottom: 1px solid rgba(255,255,255,0.03); font-weight: 600; color: var(--text-primary); text-align: left;">${sw.employeeName}</td>
        <td style="padding: 12px; border-bottom: 1px solid rgba(255,255,255,0.03); color: var(--text-secondary); text-align: left;">${sw.shiftDateDisplay || sw.shiftDate}</td>
        <td style="padding: 12px; border-bottom: 1px solid rgba(255,255,255,0.03); color: var(--text-secondary); text-align: left;">${sw.currentShiftName}</td>
        <td style="padding: 12px; border-bottom: 1px solid rgba(255,255,255,0.03); color: var(--accent-primary); font-weight: 600; text-align: left;">${sw.preferredShiftName}</td>
        <td style="padding: 12px; border-bottom: 1px solid rgba(255,255,255,0.03); color: var(--status-success); font-weight: 600; text-align: left;">${sw.preferredDateDisplay || sw.preferredDate || '<span style="color:var(--text-muted);font-style:italic;">Not specified</span>'}</td>
        <td style="padding: 12px; border-bottom: 1px solid rgba(255,255,255,0.03); color: #ff6b6b; font-style: italic; text-align: left;">${sw.rejectionReason || '—'}</td>
        <td style="padding: 12px; border-bottom: 1px solid rgba(255,255,255,0.03); text-align: right;">
          <button type="button" class="btn btn-approve-escalation"
            data-id="${sw.id}"
            data-employee-name="${sw.employeeName}"
            data-shift-date="${sw.shiftDate}"
            data-shift-date-display="${sw.shiftDateDisplay || sw.shiftDate}"
            data-current-shift="${sw.currentShiftName}"
            data-preferred-shift-name="${sw.preferredShiftName}"
            data-preferred-date="${sw.preferredDate || ''}"
            style="background: var(--status-success); color: #000; font-size: 0.72rem; padding: 5px 10px; border-radius: 4px; border: none; cursor: pointer; font-weight: 700; margin-right: 6px;" onmouseover="this.style.opacity='0.9';" onmouseout="this.style.opacity='1';">Approve</button>
          <button type="button" class="btn btn-reject-escalation" data-id="${sw.id}" style="background: #ff6b6b; color: #fff; font-size: 0.72rem; padding: 5px 10px; border-radius: 4px; border: none; cursor: pointer; font-weight: 700;" onmouseover="this.style.opacity='0.9';" onmouseout="this.style.opacity='1';">Reject</button>
        </td>
      `;
      tbody.appendChild(tr);
    });

    // Update colspan on escalation header too (7 columns now)
    const emptyRow = tbody.querySelector('td[colspan]');
    if (emptyRow) emptyRow.setAttribute('colspan', '7');

    // Inject the escalation approval modal if not already present
    if (!container.querySelector('#escalation-approval-overlay')) {
      const shiftsOptions = (window.__escalationShifts__ || []).map(s =>
        `<option value="${s.id}">${s.name} (${s.startTime} – ${s.endTime})</option>`
      ).join('');

      const employeeOptions = (this.storeEscalatedEmployees || []).map(e =>
        `<option value="${e.id}">${e.name} (${e.employeeCode})</option>`
      ).join('');

      const modalEl = document.createElement('div');
      modalEl.id = 'escalation-approval-overlay';
      modalEl.style.cssText = 'display:none; position:fixed; inset:0; background:rgba(0,0,0,0.7); z-index:9100; align-items:center; justify-content:center; backdrop-filter:blur(4px);';
      modalEl.innerHTML = `
        <div style="background:var(--bg-card); border:1px solid var(--border-color); border-radius:var(--radius-lg); padding:var(--spacing-xl); max-width:520px; width:90%; box-shadow:0 24px 64px rgba(0,0,0,0.5);">
          <div style="display:flex; align-items:center; justify-content:space-between; margin-bottom:var(--spacing-lg); border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom:var(--spacing-md);">
            <h3 style="font-family:var(--font-display); font-weight:800; font-size:1.05rem; color:var(--text-primary); margin:0;">Approve Escalated Swap</h3>
            <button id="btn-close-escalation-modal" type="button" style="background:none; border:none; color:var(--text-muted); cursor:pointer; font-size:1.2rem; line-height:1;">✕</button>
          </div>
          <div id="escalation-modal-summary" style="background:rgba(255,255,255,0.03); border:1px solid rgba(255,255,255,0.06); border-radius:var(--radius-md); padding:var(--spacing-md); margin-bottom:var(--spacing-lg); font-size:0.8rem;"></div>
          <div style="margin-bottom:var(--spacing-lg);">
            <div style="font-size:0.68rem; font-weight:700; color:var(--accent-primary); text-transform:uppercase; letter-spacing:0.5px; margin-bottom:var(--spacing-sm);">Admin Override <span style="color:var(--text-muted); font-weight:400;">(optional)</span></div>
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:var(--spacing-sm);">
              <div style="display:flex; flex-direction:column; gap:4px;">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Approved Date</label>
                <input type="date" id="escalation-approved-date" style="padding:9px 12px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.82rem; outline:none;" onfocus="this.style.borderColor='var(--accent-primary)'" onblur="this.style.borderColor='var(--border-color)'">
              </div>
              <div style="display:flex; flex-direction:column; gap:4px;">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Approved Shift</label>
                <select id="escalation-approved-shift" style="padding:9px 12px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.82rem; outline:none; cursor:pointer;">
                  <option value="">-- Keep employee's preferred shift --</option>
                  ${shiftsOptions}
                </select>
              </div>
            </div>
          </div>
          <div style="margin-bottom:var(--spacing-lg); background:rgba(255,107,107,0.04); border:1px solid rgba(255,107,107,0.15); border-radius:var(--radius-md); padding:var(--spacing-md);">
            <div style="font-size:0.68rem; font-weight:700; color:#ff6b6b; text-transform:uppercase; letter-spacing:0.5px; margin-bottom:6px;">Fill Vacated Shift Slot</div>
            <div id="escalation-vacancy-info" style="font-size:0.75rem; color:var(--text-muted); margin-bottom:var(--spacing-sm);"></div>
            <div style="display:flex; flex-direction:column; gap:4px;">
              <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Replacement Employee <span style="font-weight:400;">(optional)</span></label>
              <select id="escalation-replacement-employee" style="padding:9px 12px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid rgba(255,107,107,0.3); color:var(--text-primary); font-size:0.82rem; outline:none; cursor:pointer;">
                <option value="">-- No replacement --</option>
                ${employeeOptions}
              </select>
            </div>
          </div>
          <div style="display:flex; gap:var(--spacing-sm); justify-content:flex-end;">
            <button id="btn-escalation-cancel" type="button" style="padding:10px 20px; border-radius:var(--radius-md); background:rgba(255,255,255,0.05); border:1px solid var(--border-color); color:var(--text-secondary); font-size:0.82rem; cursor:pointer; font-weight:600;">Cancel</button>
            <button id="btn-escalation-confirm" type="button" style="padding:10px 24px; border-radius:var(--radius-md); background:var(--status-success); border:none; color:#000; font-size:0.82rem; cursor:pointer; font-weight:700; display:flex; align-items:center; gap:6px;">
              ✓ Confirm Approval
            </button>
          </div>
        </div>
      `;
      container.appendChild(modalEl);

      // Wire close buttons
      const closeEscModal = () => { modalEl.style.display = 'none'; };
      modalEl.querySelector('#btn-close-escalation-modal').addEventListener('click', closeEscModal);
      modalEl.querySelector('#btn-escalation-cancel').addEventListener('click', closeEscModal);
      modalEl.addEventListener('click', e => { if (e.target === modalEl) closeEscModal(); });
    }
  }
}

