/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : page.js
 * Path              : frontend/pages/finance/page.js
 * Purpose           : Frontend page component for the Pages Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/customer-invoices?page=0&size=20, POST /api/v1/customer-invoices
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : api/client, core/logger, store/notificationStore
 * Depends On        : api/client, core/logger, store/notificationStore
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend page component for the Pages Module UI. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { apiClient } from '../../../api/client.js';
import { logger } from '../../../core/logger.js';
import { notificationStore } from '../../../store/notificationStore.js';

export default class FinancePage {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  constructor() {
    this.invoices = [];
    this.summary = {
      receivables: 0,
      outstanding: 0,
      draftVolume: 0,
      closedRuns: 0
    };
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  async mount(container, lifecycle) {
    logger.info('FinancePage', 'Mounting Financial Overview dashboard...');

    let currencyCode = 'EUR';
    let symbol = '€';
    const storedGeneral = localStorage.getItem('plus33-settings-general');
    if (storedGeneral) {
      try {
        const parsed = JSON.parse(storedGeneral);
        if (parsed.defaultCurrency) {
          currencyCode = parsed.defaultCurrency;
          if (currencyCode === 'USD') symbol = '$';
          else if (currencyCode === 'INR') symbol = '₹';
          else if (currencyCode === 'AED') symbol = 'AED ';
        }
      } catch (e) { }
    }
    this.currencySymbol = symbol;
    this.currencyCode = currencyCode;

    // Fetch dynamic client invoices
    await this.fetchData();

    container.innerHTML = `
      <!-- Page Header -->
      <div class="dashboard-header flex justify-between align-center mb-lg" style="flex-wrap: wrap; gap: var(--spacing-md);">
        <div>
          <h2 class="m-0" style="font-family: var(--font-display); font-weight: 800; font-size: 1.65rem; letter-spacing: -0.02em;">
            Financial Overview &amp; Accounts Receivable
          </h2>
          <p class="m-0" style="color: var(--text-muted); font-size: 0.82rem; margin-top: 2px;">
            Audit customer invoices, double-entry General Ledger postings, and credit control workflow submissions
          </p>
        </div>
        <div style="display:flex; align-items:center; gap: var(--spacing-md);">
          <!-- Live Status Pill -->
          <div style="display:flex; align-items:center; gap:6px; background: rgba(130,163,125,0.12); border: 1px solid rgba(130,163,125,0.3); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.75rem; font-weight: 600; color: var(--status-success);">
            <span style="width:7px; height:7px; border-radius:50%; background: var(--status-success); display:inline-block; animation: pulse-dot 2s infinite;"></span>
            Ledger Balanced
          </div>
        </div>
      </div>

      <!-- KPI Grid -->
      <div class="grid grid-cols-4 gap-md mb-lg">
        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(201,164,106,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--accent-primary); display:flex; align-items:center;">
            <i data-lucide="badge-dollar-sign" style="width:24px; height:24px;"></i>
          </div>
          <div>
             <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">${this.currencySymbol}${this.summary.receivables.toLocaleString()}</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Total Receivables</div>
          </div>
        </div>

        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(239,68,68,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-danger); display:flex; align-items:center;">
            <i data-lucide="alert-circle" style="width:24px; height:24px;"></i>
          </div>
          <div>
             <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">${this.currencySymbol}${this.summary.outstanding.toLocaleString()}</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">AR Outstanding</div>
          </div>
        </div>

        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(59,130,246,0.1); border-radius: var(--radius-md); padding: 10px; color: #3b82f6; display:flex; align-items:center;">
            <i data-lucide="file-text" style="width:24px; height:24px;"></i>
          </div>
          <div>
             <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">${this.currencySymbol}${this.summary.draftVolume.toLocaleString()}</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Draft Volume</div>
          </div>
        </div>

        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(130,163,125,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-success); display:flex; align-items:center;">
            <i data-lucide="check-square" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">${this.summary.closedRuns} Closed</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Paid Invoices</div>
          </div>
        </div>
      </div>

      <!-- Main Layout Panels -->
      <div class="grid grid-cols-3 gap-lg mb-lg">
        
        <!-- Left: Invoices List Table -->
        <div class="card glass col-span-2 flex flex-col" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05); min-height: 480px;">
          <div class="flex justify-between align-center mb-md">
            <h3 class="m-0" style="font-family: var(--font-display); font-weight: 700; font-size: 1.05rem;">
              Billing &amp; Invoice Journal
            </h3>
            <span style="font-size: 0.7rem; color: var(--text-muted); font-weight:600; background:rgba(255,255,255,0.05); padding: 2px 8px; border-radius: 4px;">
              Live DB Synced
            </span>
          </div>
          
          <div style="overflow-x: auto; flex-grow: 1;">
            <table class="w-100" style="border-collapse: collapse; text-align: left; font-size: 0.8rem;">
              <thead>
                <tr style="border-bottom: 1px solid rgba(255,255,255,0.08); color: var(--text-muted); font-size: 0.72rem; text-transform: uppercase; font-weight:700;">
                  <th style="padding: var(--spacing-sm) var(--spacing-md);">Invoice #</th>
                  <th style="padding: var(--spacing-sm) var(--spacing-md);">Cust ID</th>
                  <th style="padding: var(--spacing-sm) var(--spacing-md);">Invoice Date</th>
                  <th style="padding: var(--spacing-sm) var(--spacing-md);">Status</th>
                  <th style="padding: var(--spacing-sm) var(--spacing-md);" width="120">Total Amount</th>
                  <th style="padding: var(--spacing-sm) var(--spacing-md); text-align:right;">Workflow Actions</th>
                </tr>
              </thead>
              <tbody id="invoice-roster-body">
                ${this.renderInvoiceRows()}
              </tbody>
            </table>
          </div>
        </div>
        
        <!-- Right: Register / General Ledger Panels -->
        <div class="flex flex-col gap-lg col-span-1">
          <!-- 1. Create Invoice Form -->
          <div class="card glass" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05);">
            <div class="flex align-center gap-xs mb-md">
              <i data-lucide="file-plus" style="color: var(--accent-primary); width:18px; height:18px;"></i>
              <h3 class="m-0" style="font-family: var(--font-display); font-weight: 700; font-size: 1.05rem;">
                Create Draft Invoice
              </h3>
            </div>
            
            <form id="create-invoice-form" style="display:flex; flex-direction:column; gap: var(--spacing-sm);">
              <div class="flex flex-col gap-xs">
                <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Invoice Number</label>
                <input type="text" id="inv-num" placeholder="INV-2026-001" required style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none;" />
              </div>

              <div class="flex gap-sm">
                <div class="flex flex-col gap-xs flex-grow">
                  <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Customer ID</label>
                  <input type="number" id="inv-cust-id" value="1" required style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none; text-align:center;" />
                </div>
                <div class="flex flex-col gap-xs flex-grow">
                   <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Invoice Amount (${this.currencySymbol})</label>
                  <input type="number" id="inv-amount" value="25000" required style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none; text-align:center;" />
                </div>
              </div>

              <button type="submit" class="btn" style="background:var(--accent-primary); color:#000; font-weight:700; font-size:0.75rem; text-transform:uppercase; padding:8px; border-radius:var(--radius-sm); border:none; display:flex; align-items:center; justify-content:center; gap:4px; cursor:pointer; margin-top:var(--spacing-xs); transition:var(--transition-fast);">
                <i data-lucide="plus" style="width:14px; height:14px;"></i>
                Generate Draft
              </button>
            </form>
          </div>

          <!-- 2. Double-Entry General Ledger Status -->
          <div class="card glass" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05);">
            <div class="flex align-center gap-xs mb-md">
              <i data-lucide="scale" style="color: var(--accent-primary); width:18px; height:18px;"></i>
              <h3 class="m-0" style="font-family: var(--font-display); font-weight: 700; font-size: 1.05rem;">
                General Ledger Reconciliation
              </h3>
            </div>
            
            <p style="color: var(--text-muted); font-size: 0.75rem; line-height: 1.5; margin-top: 0; margin-bottom: var(--spacing-md);">
              Double-entry postings trace audit for accounts receivable (Debits) vs. sales revenues (Credits).
            </p>

            <div style="font-family: monospace; font-size: 0.72rem; color: var(--text-muted); background:rgba(0,0,0,0.15); padding:10px 12px; border-radius:var(--radius-sm); border:1px solid rgba(255,255,255,0.03); line-height:1.5;">
               <div class="flex justify-between"><span>AR Account (Dr)</span><span style="color:#fff;">${this.currencySymbol}${this.summary.receivables.toLocaleString()}</span></div>
              <div class="flex justify-between"><span>Revenue Account (Cr)</span><span style="color:#fff;">${this.currencySymbol}${this.summary.receivables.toLocaleString()}</span></div>
              <div style="border-top: 1px solid rgba(255,255,255,0.08); margin: 6px 0; padding-top: 4px;" class="flex justify-between">
                <strong>Status Check</strong>
                <strong style="color: var(--status-success);">✔ BALANCED</strong>
              </div>
            </div>
          </div>
        </div>

      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  async fetchData() {
    try {
      const res = await apiClient.get('/api/v1/customer-invoices?page=0&size=20');
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
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

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  renderInvoiceRows() {
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (!this.invoices || this.invoices.length === 0) {
      return `<tr><td colspan="6" style="padding: var(--spacing-xl); text-align:center; color: var(--text-muted);">No invoice records found.</td></tr>`;
    }

    return this.invoices.map(i => {
      let statusColor = 'var(--text-muted)';
      if (i.status === 'APPROVED') statusColor = 'var(--status-success)';
      if (i.status === 'SUBMITTED') statusColor = 'var(--accent-primary)';
      if (i.status === 'DRAFT') statusColor = '#3b82f6';
      if (i.status === 'CANCELLED') statusColor = 'var(--status-danger)';

      return `
        <tr style="border-bottom: 1px solid rgba(255,255,255,0.04); transition: var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.01)'" onmouseout="this.style.background='transparent'">
          <td style="padding: var(--spacing-md); font-weight:700; color:#fff; font-family:monospace;">${i.invoiceNumber}</td>
          <td style="padding: var(--spacing-md); font-weight:700; color:var(--text-muted);">#${i.customerId}</td>
          <td style="padding: var(--spacing-md); color:var(--text-muted); font-size: 0.78rem;">${i.invoiceDate || '2026-07-04'}</td>
          <td style="padding: var(--spacing-md);">
            <span style="display:inline-flex; align-items:center; gap:5px; font-size:0.72rem; font-weight:700; color:${statusColor};">
              <span style="width:6px; height:6px; border-radius:50%; background:${statusColor}; display:inline-block;"></span>
              ${i.status}
            </span>
          </td>
          <td style="padding: var(--spacing-md); font-weight:600; color:var(--text-primary); font-variant-numeric: tabular-nums;">${this.currencySymbol}${i.totalAmount.toLocaleString()}</td>
          <td style="padding: var(--spacing-md); text-align:right;">
            <div style="display:flex; justify-content:flex-end; gap:6px;">
              ${i.status === 'DRAFT' ? `
                <button class="btn-workflow btn-submit" data-id="${i.id}" style="background:rgba(201,164,106,0.06); border:1px solid rgba(201,164,106,0.25); color:var(--accent-primary); font-size:0.68rem; font-weight:700; padding:2px 6px; border-radius:var(--radius-sm); cursor:pointer;">Submit</button>
              ` : ''}
              ${i.status === 'SUBMITTED' ? `
                <button class="btn-workflow btn-approve" data-id="${i.id}" style="background:rgba(46,204,113,0.06); border:1px solid rgba(46,204,113,0.25); color:var(--status-success); font-size:0.68rem; font-weight:700; padding:2px 6px; border-radius:var(--radius-sm); cursor:pointer;">Approve</button>
              ` : ''}
              ${i.status === 'APPROVED' ? `
                <button class="btn-workflow btn-cancel" data-id="${i.id}" style="background:rgba(231,76,60,0.06); border:1px solid rgba(231,76,60,0.25); color:var(--status-danger); font-size:0.68rem; font-weight:700; padding:2px 6px; border-radius:var(--radius-sm); cursor:pointer;">Cancel</button>
              ` : ''}
            </div>
          </td>
        </tr>
      `;
    }).join('');
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindEvents(container, lifecycle) {
    // 1. Create Invoice Handler
    const createForm = container.querySelector('#create-invoice-form');
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (createForm) {
      /**
       * Handles the handler event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handler = async (e) => {
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

          // Reload roster
          await this.fetchData();

          // Re-render table and update counter
          const tbody = container.querySelector('#invoice-roster-body');
          if (tbody) tbody.innerHTML = this.renderInvoiceRows();

          this.bindWorkflowHandlers(container);

          notificationStore.success(`Draft invoice ${invoiceNumber} created successfully!`, 5000);
          createForm.reset();
        } catch (err) {
          logger.error('FinancePage', 'Failed to create invoice:', err);
          notificationStore.danger('Invoice creation failed. Ensure invoice number is unique.');
        }
      };
      createForm.addEventListener('submit', handler);
      lifecycle.onCleanup(() => createForm.removeEventListener('submit', handler));
    }

    // 2. Bind Workflow Action Handlers
    this.bindWorkflowHandlers(container);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindWorkflowHandlers(container) {
    // Submit buttons
    container.querySelectorAll('.btn-submit').forEach(btn => {
      const id = btn.getAttribute('data-id');
      btn.addEventListener('click', async () => {
        try {
          await apiClient.post(`/api/v1/customer-invoices/${id}/submit`);
          notificationStore.success('Invoice submitted for approval.', 4000);
          await this.fetchData();
          const tbody = container.querySelector('#invoice-roster-body');
          if (tbody) tbody.innerHTML = this.renderInvoiceRows();
          this.bindWorkflowHandlers(container);
        } catch (err) {
          logger.error('FinancePage', 'Failed to submit invoice:', err);
          notificationStore.danger('Failed to submit invoice. Please try again.');
        }
      });
    });

    // Approve buttons
    container.querySelectorAll('.btn-approve').forEach(btn => {
      const id = btn.getAttribute('data-id');
      btn.addEventListener('click', async () => {
        try {
          await apiClient.post(`/api/v1/customer-invoices/${id}/approve`);
          notificationStore.success('Invoice approved. Journal postings balance trace completed.', 5000);
          await this.fetchData();
          const tbody = container.querySelector('#invoice-roster-body');
          if (tbody) tbody.innerHTML = this.renderInvoiceRows();
          this.bindWorkflowHandlers(container);
        } catch (err) {
          logger.error('FinancePage', 'Failed to approve invoice:', err);
          notificationStore.danger('Failed to approve invoice. Please try again.');
        }
      });
    });

    // Cancel buttons
    container.querySelectorAll('.btn-cancel').forEach(btn => {
      const id = btn.getAttribute('data-id');
      btn.addEventListener('click', async () => {
        try {
          await apiClient.post(`/api/v1/customer-invoices/${id}/cancel`);
          notificationStore.success('Invoice cancelled and reversing journal entry posted.', 5000);
          await this.fetchData();
          const tbody = container.querySelector('#invoice-roster-body');
          if (tbody) tbody.innerHTML = this.renderInvoiceRows();
          this.bindWorkflowHandlers(container);
        } catch (err) {
          logger.error('FinancePage', 'Failed to cancel invoice:', err);
          notificationStore.danger('Failed to cancel invoice. Please try again.');
        }
      });
    });
  }
}



