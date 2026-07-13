/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ultimate Admin â€” Finance & General Ledger
 * File              : finance.js
 * Purpose           : Controller component for Finance Management UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/ultimate-admin/finance/finance.html
 * Related CSS       : frontend/modules/ultimate-admin/finance/finance.css
 * Related APIs      : GET /api/v1/customer-invoices
 *                     POST /api/v1/customer-invoices
 *                     POST /api/v1/customer-invoices/:id/submit
 *                     POST /api/v1/customer-invoices/:id/approve
 *                     POST /api/v1/customer-invoices/:id/cancel
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in finance.html â€” this file is a controller only.
 *
 * Controller Lifecycle:
 *   constructor â†’ mount â†’ loadTemplate â†’ loadData â†’ render â†’ bindEvents â†’ destroy
 ******************************************************************************/

import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';
import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';

/** Path to the finance HTML template */
const TEMPLATE_URL = 'modules/ultimate-admin/pages/finance/finance.html';

export default class FinancePage {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.invoices = [];
    this.summary = {
      receivables: 0,
      outstanding: 0,
      draftVolume: 0,
      closedRuns: 0
    };
    this.currencyCode = 'EUR';
    this.currencySymbol = 'â‚¬';
    this.container = null;
    this.lifecycle = null;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the finance registry page component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function, onDestroy?: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('FinancePage', 'Mounting Financial Overview dashboard...');
    this.container = container;
    this.lifecycle = lifecycle;

    // Dynamically load finance CSS
    this._loadCss();

    // 1. Load template HTML
    await this._loadTemplate(container);

    // 2. Resolve settings and fetch ledger data
    this._resolveCurrencySettings();
    await this._loadData();

    // 3. Render loaded data into the DOM
    this._render(container);

    // 4. Bind event listeners
    this._bindEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: loadTemplate
  // ---------------------------------------------------------------------------

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: loadData
  // ---------------------------------------------------------------------------

  async _loadData() {
    try {
      const res = await apiClient.get('/api/v1/customer-invoices?page=0&size=20');
      if (res?.success && res?.data?.content) {
        this.invoices = res.data.content;
        
        // Compute summary KPIs from real invoice data
        this.summary.receivables = this.invoices.reduce((sum, i) => sum + (i.totalAmount || 0), 0);
        this.summary.outstanding = this.invoices.filter(i => i.status === 'SUBMITTED').reduce((sum, i) => sum + (i.totalAmount || 0), 0);
        this.summary.draftVolume = this.invoices.filter(i => i.status === 'DRAFT').reduce((sum, i) => sum + (i.totalAmount || 0), 0);
        this.summary.closedRuns  = this.invoices.filter(i => i.status === 'APPROVED' || i.status === 'CANCELLED').length;
      }
    } catch (err) {
      logger.error('FinancePage', 'Failed to fetch customer invoices:', err);
      this.invoices = [];
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  /**
   * Render dynamic fields, KPI cards, journal list table rows, double-entry balances.
   * @param {HTMLElement} container
   */
  _render(container) {
    this._populateKpiSummary(container);
    this._renderInvoiceRowsList(container);
    this._renderDoubleEntryBalances(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  /**
   * Bind event listeners for invoice submission forms and workflow triggers.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  _bindEvents(container, lifecycle) {
    const createForm = container.querySelector('#create-invoice-form');

    if (createForm) {
      const handleSubmit = async (e) => {
        e.preventDefault();
        const invoiceNumber = container.querySelector('#inv-num').value.trim();
        const customerId = parseInt(container.querySelector('#inv-cust-id').value) || 1;
        const totalAmount = parseFloat(container.querySelector('#inv-amount').value) || 0;

        logger.info('FinancePage', `Creating draft invoice: ${invoiceNumber}`);

        try {
          await apiClient.post('/api/v1/customer-invoices', {
            companyId: 1,
            customerId,
            invoiceNumber,
            totalAmount,
            status: 'DRAFT',
            invoiceDate: new Date().toISOString().split('T')[0]
          });

          await this.loadAndRender(container, lifecycle);
          notificationStore.success(`Draft invoice ${invoiceNumber} created successfully!`, 5000);
          createForm.reset();
        } catch (err) {
          logger.error('FinancePage', 'Failed to create invoice:', err);
          notificationStore.danger('Invoice creation failed. Ensure invoice number is unique.');
        }
      };

      createForm.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => createForm.removeEventListener('submit', handleSubmit));
    }

    // Bind action buttons inside table rows
    this._bindTableActionEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  destroy() {
    logger.debug('FinancePage', 'Workspace destroyed.');
  }

  // ---------------------------------------------------------------------------
  // PUBLIC HELPER (Legacy Bridge): loadAndRender
  // ---------------------------------------------------------------------------

  async loadAndRender(container = this.container, lifecycle = this.lifecycle) {
    await this._loadData();
    this._render(container);
    this._bindEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // PRIVATE: Rendering Sub-routines
  // ---------------------------------------------------------------------------

  /**
   * Populate top summary metric cards.
   * @param {HTMLElement} container
   */
  _populateKpiSummary(container) {
    const kpiReceivables = container.querySelector('#kpi-receivables');
    const kpiOutstanding = container.querySelector('#kpi-outstanding');
    const kpiDrafts = container.querySelector('#kpi-drafts');
    const kpiClosed = container.querySelector('#kpi-closed');

    const formattedReceivables = this.summary.receivables > 0
      ? new Intl.NumberFormat('fr-FR', {
          style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
        }).format(this.summary.receivables)
      : 'NA/DB';

    const formattedOutstanding = this.summary.outstanding > 0
      ? new Intl.NumberFormat('fr-FR', {
          style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
        }).format(this.summary.outstanding)
      : 'NA/DB';

    const formattedDrafts = this.summary.draftVolume > 0
      ? new Intl.NumberFormat('fr-FR', {
          style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
        }).format(this.summary.draftVolume)
      : 'NA/DB';

    if (kpiReceivables) kpiReceivables.textContent = formattedReceivables;
    if (kpiOutstanding) kpiOutstanding.textContent = formattedOutstanding;
    if (kpiDrafts) kpiDrafts.textContent = formattedDrafts;
    if (kpiClosed) kpiClosed.textContent = this.summary.closedRuns > 0 ? `${this.summary.closedRuns} Closed` : 'NA/DB';

    // Update form labels currencies symbols dynamically
    const lblInvAmount = container.querySelector('#lbl-inv-amount');
    if (lblInvAmount) lblInvAmount.textContent = `Invoice Amount (${this.currencySymbol})`;
  }

  /**
   * Map database invoices elements to registry journal rows list body.
   * @param {HTMLElement} container
   */
  _renderInvoiceRowsList(container) {
    const tbody = container.querySelector('#invoice-roster-body');
    if (!tbody) return;

    tbody.replaceChildren();

    if (!this.invoices || this.invoices.length === 0) {
      const tr = document.createElement('tr');
      const td = document.createElement('td');
      td.setAttribute('colspan', '6');
      td.style.padding = 'var(--spacing-xl)';
      td.style.textAlign = 'center';
      td.style.color = 'var(--text-muted)';
      td.textContent = 'No invoice records found.';
      tr.appendChild(td);
      tbody.appendChild(tr);
      return;
    }

    const rowTpl = container.querySelector('#invoice-row-tpl');
    if (!rowTpl) return;

    this.invoices.forEach(i => {
      const clone = rowTpl.content.cloneNode(true);
      
      const colNum = clone.querySelector('.col-num');
      const colCustId = clone.querySelector('.col-cust-id');
      const colDate = clone.querySelector('.col-date');
      const statusWrapper = clone.querySelector('.status-indicator-wrapper');
      const statusLabel = clone.querySelector('.status-label');
      const colAmount = clone.querySelector('.col-amount');
      const actionsWrapper = clone.querySelector('.actions-flex-wrapper');

      const isDraft = i.status === 'DRAFT';
      const isSubmitted = i.status === 'SUBMITTED';
      const isApproved = i.status === 'APPROVED';

      const formattedAmount = new Intl.NumberFormat('fr-FR', {
        style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
      }).format(i.totalAmount);

      if (colNum) colNum.textContent = i.invoiceNumber;
      if (colCustId) colCustId.textContent = `#${i.customerId}`;
      if (colDate) colDate.textContent = i.invoiceDate || '2026-07-04';
      if (colAmount) colAmount.textContent = formattedAmount;

      if (statusWrapper) {
        statusWrapper.className = `status-indicator-wrapper status-indicator-wrapper--${i.status.toLowerCase()}`;
      }
      if (statusLabel) statusLabel.textContent = i.status;

      // Populate workflow action triggers
      if (actionsWrapper) {
        actionsWrapper.replaceChildren();

        if (isDraft) {
          const btn = document.createElement('button');
          btn.className = 'btn-workflow btn-submit';
          btn.setAttribute('data-id', i.id);
          btn.textContent = 'Submit';
          actionsWrapper.appendChild(btn);
        } else if (isSubmitted) {
          const btn = document.createElement('button');
          btn.className = 'btn-workflow btn-approve';
          btn.setAttribute('data-id', i.id);
          btn.textContent = 'Approve';
          actionsWrapper.appendChild(btn);
        } else if (isApproved) {
          const btn = document.createElement('button');
          btn.className = 'btn-workflow btn-cancel';
          btn.setAttribute('data-id', i.id);
          btn.textContent = 'Cancel';
          actionsWrapper.appendChild(btn);
        }
      }

      tbody.appendChild(clone);
    });
  }

  /**
   * Populate double entry reconciliation log values.
   * @param {HTMLElement} container
   */
  _renderDoubleEntryBalances(container) {
    const debitEl = container.querySelector('#gl-val-debit');
    const creditEl = container.querySelector('#gl-val-credit');

    const formattedVal = this.summary.receivables > 0
      ? new Intl.NumberFormat('fr-FR', {
          style: 'currency', currency: this.currencyCode, maximumFractionDigits: 0
        }).format(this.summary.receivables)
      : 'NA/DB';

    if (debitEl) debitEl.textContent = formattedVal;
    if (creditEl) creditEl.textContent = formattedVal;
  }

  /**
   * Bind event handlers to row workflow actions.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  _bindTableActionEvents(container, lifecycle) {
    // 1. Submit Action
    container.querySelectorAll('.btn-submit').forEach(btn => {
      const id = btn.getAttribute('data-id');
      const handleSubmit = async () => {
        try {
          await apiClient.post(`/api/v1/customer-invoices/${id}/submit`);
          notificationStore.success('Invoice submitted for approval.', 4000);
          await this.loadAndRender(container, lifecycle);
        } catch (err) {
          logger.error('FinancePage', 'Failed to submit invoice:', err);
          notificationStore.danger('Failed to submit invoice. Please try again.');
        }
      };
      btn.addEventListener('click', handleSubmit);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleSubmit));
    });

    // 2. Approve Action
    container.querySelectorAll('.btn-approve').forEach(btn => {
      const id = btn.getAttribute('data-id');
      const handleApprove = async () => {
        try {
          await apiClient.post(`/api/v1/customer-invoices/${id}/approve`);
          notificationStore.success('Invoice approved. Journal postings balance trace completed.', 5000);
          await this.loadAndRender(container, lifecycle);
        } catch (err) {
          logger.error('FinancePage', 'Failed to approve invoice:', err);
          notificationStore.danger('Failed to approve invoice. Please try again.');
        }
      };
      btn.addEventListener('click', handleApprove);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleApprove));
    });

    // 3. Cancel Action
    container.querySelectorAll('.btn-cancel').forEach(btn => {
      const id = btn.getAttribute('data-id');
      const handleCancel = async () => {
        try {
          await apiClient.post(`/api/v1/customer-invoices/${id}/cancel`);
          notificationStore.success('Invoice cancelled and reversing journal entry posted.', 5000);
          await this.loadAndRender(container, lifecycle);
        } catch (err) {
          logger.error('FinancePage', 'Failed to cancel invoice:', err);
          notificationStore.danger('Failed to cancel invoice. Please try again.');
        }
      };
      btn.addEventListener('click', handleCancel);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleCancel));
    });
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _resolveCurrencySettings() {
    const storedGeneral = localStorage.getItem('plus33-settings-general');
    if (storedGeneral) {
      try {
        const parsed = JSON.parse(storedGeneral);
        if (parsed.defaultCurrency) {
          this.currencyCode = parsed.defaultCurrency;
          if (this.currencyCode === 'USD') { this.currencySymbol = '$'; }
          else if (this.currencyCode === 'INR') { this.currencySymbol = 'â‚¹'; }
          else if (this.currencyCode === 'AED') { this.currencySymbol = 'AED '; }
          else { this.currencySymbol = 'â‚¬'; }
        }
      } catch (e) {
        // ignore
      }
    }
  }

  _loadCss() {
    const cssId = 'finance-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/ultimate-admin/pages/finance/finance.css';
      document.head.appendChild(link);
    }
  }
}
export { FinancePage };
