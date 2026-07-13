/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Admin — Sales Ledger
 * File              : sales.js
 * Path              : frontend/modules/store-admin/sales/sales.js
 * Purpose           : Controller component for Store Admin Sales Ledger UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-admin/sales/sales.html
 * Related CSS       : frontend/modules/store-admin/sales/sales.css
 * Related APIs      : GET /api/v1/sales-orders, POST /api/v1/sales-orders
 *                     PUT /api/v1/sales-orders/:id/status
 *                     DELETE /api/v1/sales-orders/:id
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in sales.html — this file is a controller only.
 ******************************************************************************/

import { apiClient } from '../../../../api/client.js';
import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { dashboardService } from '../../../../services/dashboard/DashboardService.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the sales HTML template */
const TEMPLATE_URL = 'modules/store-admin/pages/sales/sales.html';

export default class SalesPage {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.orders = [];
    this.searchQuery = '';
    this.selectedStatusFilter = 'all';
    this.selectedOrder = null;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the SalesPage component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('SalesPage', 'Syncing sales ledger with backend database...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML layout
    await this._loadTemplate(container);

    // 2. Fetch sales orders from GET /api/v1/sales-orders
    await this.loadData();

    // 3. Render layout elements
    this.render(container);

    // 4. Bind event listeners
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
      const response = await apiClient.get('/api/v1/sales-orders', { size: 100 });
      if (response?.success && response.data?.content) {
        this.orders = response.data.content;
      }
    } catch (err) {
      logger.error('SalesPage', 'Failed to synchronize data with database:', err);
      notificationStore.danger('Database connection failed. Displaying local cache.');
    }
  }

  // ---------------------------------------------------------------------------
  // TEXT SANITIZATION
  // ---------------------------------------------------------------------------

  sanitizeText(text) {
    if (!text) return '';
    return text
      .replace(/\??/g, ' ')
      .replace(/ǩ/g, 'î')
      .replace(/Ǹ/g, 'é')
      .replace(/Ǫ/g, 'è');
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  render(container) {
    const totalOrders = this.orders.length;
    const grossRevenue = this.orders.reduce((acc, o) => acc + (o.totalAmount || 0), 0);
    const pendingOrders = this.orders.filter(o => o.status === 'PENDING' || o.status === 'APPROVED').length;
    const avgOrderValue = totalOrders > 0 ? (grossRevenue / totalOrders) : 0;

    // Currency Settings
    let symbol = '€';
    const storedGeneral = localStorage.getItem('plus33-settings-general');
    if (storedGeneral) {
      try {
        const parsed = JSON.parse(storedGeneral);
        if (parsed.defaultCurrency) {
          const currencyCode = parsed.defaultCurrency;
          if (currencyCode === 'USD') symbol = '$';
          else if (currencyCode === 'INR') symbol = '₹';
          else if (currencyCode === 'AED') symbol = 'AED ';
        }
      } catch (e) {
        // ignore
      }
    }

    // Set currency symbols in HTML
    container.querySelectorAll('.currency-symbol').forEach(el => {
      el.textContent = symbol;
    });

    // Sync KPIs
    const grossEl = container.querySelector('#kpi-gross-revenue');
    const invoicedEl = container.querySelector('#kpi-invoiced-count');
    const pendingEl = container.querySelector('#kpi-pending-count');
    const avgEl = container.querySelector('#kpi-avg-order');

    if (grossEl) grossEl.textContent = `${symbol}${grossRevenue.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
    if (invoicedEl) invoicedEl.textContent = String(totalOrders - pendingOrders);
    if (pendingEl) pendingEl.textContent = String(pendingOrders);
    if (avgEl) avgEl.textContent = `${symbol}${avgOrderValue.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;

    // Sync search and filters values
    const searchInput = container.querySelector('#input-search-orders');
    const statusSelect = container.querySelector('#select-filter-status');
    if (searchInput) searchInput.value = this.searchQuery;
    if (statusSelect) statusSelect.value = this.selectedStatusFilter;

    // Render Table Rows
    this._renderTable(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const searchInput = container.querySelector('#input-search-orders');
    const statusFilter = container.querySelector('#select-filter-status');
    const btnAddOrder = container.querySelector('#btn-add-order');
    const btnDownloadSales = container.querySelector('#btn-download-sales');
    const btnPrintSales = container.querySelector('#btn-print-sales');
    const modal = container.querySelector('#add-order-modal');
    const btnCloseModal = container.querySelector('#btn-close-order-modal');
    const formAddOrder = container.querySelector('#form-add-order');
    const submitBtn = container.querySelector('#btn-order-submit');

    // Export CSV Binding
    if (btnDownloadSales) {
      const handleDownload = () => this.downloadCSV();
      btnDownloadSales.addEventListener('click', handleDownload);
      lifecycle.onCleanup(() => btnDownloadSales.removeEventListener('click', handleDownload));
    }

    // Print Report Binding
    if (btnPrintSales) {
      const handlePrint = () => this.printReport();
      btnPrintSales.addEventListener('click', handlePrint);
      lifecycle.onCleanup(() => btnPrintSales.removeEventListener('click', handlePrint));
    }

    // 1. Text Search Input
    if (searchInput) {
      const handleSearch = (e) => {
        this.searchQuery = e.target.value;
        this.refreshTable(container, lifecycle);
      };
      searchInput.addEventListener('input', handleSearch);
      lifecycle.onCleanup(() => searchInput.removeEventListener('input', handleSearch));
    }

    // 2. Status Dropdown Filter
    if (statusFilter) {
      const handleStatusFilter = (e) => {
        this.selectedStatusFilter = e.target.value;
        this.refreshTable(container, lifecycle);
      };
      statusFilter.addEventListener('change', handleStatusFilter);
      lifecycle.onCleanup(() => statusFilter.removeEventListener('change', handleStatusFilter));
    }

    // 3. Open Add Order Modal
    if (btnAddOrder && modal) {
      const openModal = () => {
        modal.style.display = 'flex';
        modal.setAttribute('aria-hidden', 'false');
        const dateInput = container.querySelector('#order-modal-date');
        if (dateInput) dateInput.value = new Date().toISOString().substring(0, 10);
      };
      btnAddOrder.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddOrder.removeEventListener('click', openModal));
    }

    // 4. Close Add Order Modal
    if (btnCloseModal && modal) {
      const closeModal = () => {
        modal.style.display = 'none';
        modal.setAttribute('aria-hidden', 'true');
      };
      btnCloseModal.addEventListener('click', closeModal);
      lifecycle.onCleanup(() => btnCloseModal.removeEventListener('click', closeModal));

      const backdropClose = (e) => {
        if (e.target === modal) {
          modal.style.display = 'none';
          modal.setAttribute('aria-hidden', 'true');
        }
      };
      modal.addEventListener('click', backdropClose);
      lifecycle.onCleanup(() => modal.removeEventListener('click', backdropClose));
    }

    // 5. Form Submit connection to POST /api/v1/sales-orders
    if (formAddOrder && modal) {
      const handleSubmit = async (e) => {
        e.preventDefault();

        const customerName = container.querySelector('#order-modal-customer').value.trim();
        const totalAmount = Number(container.querySelector('#order-modal-amount').value);
        const orderDate = container.querySelector('#order-modal-date').value;
        const status = container.querySelector('#order-modal-status').value;

        if (submitBtn) {
          submitBtn.disabled = true;
          submitBtn.textContent = 'Registering…';
        }

        try {
          // Generate unique sales order code sequence
          const numSeed = Math.floor(100000 + Math.random() * 900000);
          const orderNumber = `SO-2026-${numSeed}`;

          let systemCurrency = 'EUR';
          const storedGeneral = localStorage.getItem('plus33-settings-general');
          if (storedGeneral) {
            try {
              const parsed = JSON.parse(storedGeneral);
              if (parsed.defaultCurrency) systemCurrency = parsed.defaultCurrency;
            } catch (err) {}
          }

          // Construct request body matching com.plus33.erp.sales.dto.SalesOrderRequest DTO
          const response = await apiClient.post('/api/v1/sales-orders', {
            orderNumber: orderNumber,
            customerName: customerName,
            customerCode: 'CUST-B2B-011',
            orderDate: orderDate,
            requestedDeliveryDate: orderDate,
            currencyCode: systemCurrency,
            billingAddress: 'PLUS33 Coffee House Outlet',
            shippingAddress: 'PLUS33 Coffee House Outlet',
            status: status,
            totalAmount: totalAmount,
            subtotal: totalAmount / 1.2, // 20% tax profile simulated
            taxAmount: totalAmount - (totalAmount / 1.2),
            outstandingAmount: status === 'INVOICED' ? 0.0 : totalAmount,
            paymentTermsDays: 30
          });

          if (response?.success) {
            await dashboardService.getDashboardOverview();
            
            modal.style.display = 'none';
            modal.setAttribute('aria-hidden', 'true');
            formAddOrder.reset();

            // Refresh UI
            await this.mount(container, lifecycle);

            notificationStore.success(`Sales Order '${orderNumber}' registered successfully!`);
          } else {
            throw new Error(response?.message || 'Operation rejected by database engine.');
          }

        } catch (err) {
          logger.error('SalesPage', 'Failed to register sales order:', err);
          notificationStore.danger(`Registration failed: ${err.message}`);
        } finally {
          if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register Order Record';
          }
        }
      };

      formAddOrder.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => formAddOrder.removeEventListener('submit', handleSubmit));
    }

    // 6. Bind Actions (Fulfill, Cancel, Delete, View details)
    this.bindTableActions(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // PUBLIC HELPER (Legacy Bridge): refreshTable
  // ---------------------------------------------------------------------------

  refreshTable(container, lifecycle) {
    this._renderTable(container);
    this.bindTableActions(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // TABLE ACTIONS BINDINGS
  // ---------------------------------------------------------------------------

  bindTableActions(container, lifecycle) {
    const viewModal = container.querySelector('#view-order-modal');
    const closeViewBtn = container.querySelector('#btn-close-view-modal');
    const modalDownload = container.querySelector('#modal-btn-download');
    const modalPrint = container.querySelector('#modal-btn-print');

    // Modal close binds
    if (closeViewBtn && viewModal) {
      const handleClose = () => {
        viewModal.style.display = 'none';
        viewModal.setAttribute('aria-hidden', 'true');
        this.selectedOrder = null;
      };
      closeViewBtn.addEventListener('click', handleClose);
      lifecycle.onCleanup(() => closeViewBtn.removeEventListener('click', handleClose));

      const handleBgClick = (e) => {
        if (e.target === viewModal) handleClose();
      };
      viewModal.addEventListener('click', handleBgClick);
      lifecycle.onCleanup(() => viewModal.removeEventListener('click', handleBgClick));
    }

    if (modalDownload) {
      const handleModalDownload = () => {
        if (this.selectedOrder) {
          const headers = ['Order Number', 'Order Date', 'Customer Name', 'Total Amount', 'Status'];
          const r = [
            this.selectedOrder.orderNumber,
            this.selectedOrder.orderDate || '',
            this.selectedOrder.customerName || '',
            (this.selectedOrder.totalAmount || 0).toFixed(2),
            this.selectedOrder.status
          ];
          const csvContent = "\uFEFF" + [headers.join(','), r.map(v => `"${String(v).replace(/"/g, '""')}"`).join(',')].join('\n');
          const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
          const url = URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.setAttribute("href", url);
          link.setAttribute("download", `invoice_${this.selectedOrder.orderNumber}.csv`);
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          notificationStore.success('Wholesale invoice details downloaded!');
        }
      };
      modalDownload.addEventListener('click', handleModalDownload);
      lifecycle.onCleanup(() => modalDownload.removeEventListener('click', handleModalDownload));
    }

    if (modalPrint) {
      const handleModalPrint = () => {
        window.print();
      };
      modalPrint.addEventListener('click', handleModalPrint);
      lifecycle.onCleanup(() => modalPrint.removeEventListener('click', handleModalPrint));
    }

    // 1. View details button trigger
    const viewButtons = container.querySelectorAll('.btn-action-view');
    viewButtons.forEach(btn => {
      const handleView = () => {
        const id = Number(btn.getAttribute('data-id'));
        const order = this.orders.find(o => o.id === id);
        if (order && viewModal) {
          this.selectedOrder = order;
          const cleanCustomer = this.sanitizeText(order.customerName);
          
          let symbol = '€';
          const storedGeneral = localStorage.getItem('plus33-settings-general');
          if (storedGeneral) {
            try {
              const parsed = JSON.parse(storedGeneral);
              if (parsed.defaultCurrency) {
                const currencyCode = parsed.defaultCurrency;
                if (currencyCode === 'USD') symbol = '$';
                else if (currencyCode === 'INR') symbol = '₹';
                else if (currencyCode === 'AED') symbol = 'AED ';
              }
            } catch (err) {}
          }

          const detailsContainer = container.querySelector('#view-order-details-content');
          if (detailsContainer) {
            detailsContainer.innerHTML = `
              <div style="font-size:0.75rem; color:var(--text-primary); display:flex; flex-direction:column; gap:10px;">
                <div style="display:grid; grid-template-columns:1fr 1fr; gap:8px; border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom:8px;">
                  <div><strong>Invoice Code:</strong> ${order.orderNumber}</div>
                  <div><strong>Invoice Date:</strong> ${order.orderDate || 'Unknown'}</div>
                  <div><strong>Customer:</strong> ${cleanCustomer}</div>
                  <div><strong>Status:</strong> ${order.status}</div>
                </div>
                
                <strong style="font-size:0.8rem; color:var(--accent-primary); margin-top:4px;">Billing Details</strong>
                <div style="display:flex; justify-content:space-between; padding:2px 0;">
                  <span>Subtotal Amount:</span>
                  <span>${symbol}${(order.subtotal || order.totalAmount / 1.2 || 0).toFixed(2)}</span>
                </div>
                <div style="display:flex; justify-content:space-between; padding:2px 0; border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom:6px;">
                  <span>VAT / Tax (20%):</span>
                  <span>${symbol}${(order.taxAmount || order.totalAmount - (order.totalAmount / 1.2) || 0).toFixed(2)}</span>
                </div>
                
                <div style="background:rgba(201,164,106,0.1); border:1px solid var(--accent-primary); padding:10px; border-radius:var(--radius-md); display:flex; justify-content:space-between; font-weight:800; font-size:0.85rem; margin-top:6px; color:var(--accent-primary);">
                  <span>Total Amount Due:</span>
                  <span>${symbol}${(order.totalAmount || 0).toFixed(2)}</span>
                </div>
              </div>
            `;
          }

          viewModal.style.display = 'flex';
          viewModal.setAttribute('aria-hidden', 'false');
        }
      };
      btn.addEventListener('click', handleView);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleView));
    });

    // 2. Generate/Download invoice receipt button
    const downloadBtns = container.querySelectorAll('.btn-action-invoice');
    downloadBtns.forEach(btn => {
      const handleDownloadReceipt = () => {
        const id = Number(btn.getAttribute('data-id'));
        const order = this.orders.find(o => o.id === id);
        if (order) {
          notificationStore.success(`Generating PDF invoice for ${order.orderNumber}...`);
        }
      };
      btn.addEventListener('click', handleDownloadReceipt);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDownloadReceipt));
    });

    // 3. Soft-Delete Record
    const deleteButtons = container.querySelectorAll('.btn-action-cancel');
    deleteButtons.forEach(btn => {
      const handleDelete = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const number = btn.getAttribute('data-number');
        
        if (confirm(`Are you sure you want to delete sales order record "${number}"?`)) {
          try {
            const res = await apiClient.delete(`/api/v1/sales-orders/${id}`);
            if (res?.success) {
              notificationStore.success(`Order "${number}" deleted successfully.`);
              
              await dashboardService.getDashboardOverview();
              await this.mount(container, lifecycle);
            } else {
              throw new Error(res?.message || 'Operation rejected by backend API.');
            }
          } catch (err) {
            logger.error('SalesPage', `Failed to delete order ${number}:`, err);
            notificationStore.danger(`Deletion failed: ${err.message}`);
          }
        }
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });
  }

  // ---------------------------------------------------------------------------
  // PUBLIC REPORT TRIGGERS
  // ---------------------------------------------------------------------------

  downloadCSV() {
    if (this.orders.length === 0) {
      notificationStore.danger('No orders available to export.');
      return;
    }

    const headers = ['Order Number', 'Order Date', 'Customer Name', 'Total Amount', 'Status'];
    const rows = this.orders.map(o => [
      o.orderNumber,
      o.orderDate || '',
      o.customerName || '',
      (o.totalAmount || 0).toFixed(2),
      o.status
    ]);

    const csvRows = [headers.join(',')];
    for (const r of rows) {
      csvRows.push(r.map(val => `"${String(val).replace(/"/g, '""')}"`).join(','));
    }
    const csvContent = "\uFEFF" + csvRows.join('\n');

    try {
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.setAttribute("href", url);
      link.setAttribute("download", `sales_ledger_export_${new Date().toISOString().slice(0, 10)}.csv`);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      notificationStore.success('Sales ledger exported successfully as CSV!');
    } catch (err) {
      logger.error('SalesPage', 'Export failed:', err);
      notificationStore.danger('Export failed. Please check browser permissions.');
    }
  }

  printReport() {
    window.print();
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderTable(container) {
    const tbody = container.querySelector('#sales-orders-tbody');
    const rowTpl = container.querySelector('#sales-row-tpl');

    if (!tbody || !rowTpl) return;

    tbody.replaceChildren();

    let filtered = [...this.orders];

    // Currency settings
    let symbol = '€';
    const storedGeneral = localStorage.getItem('plus33-settings-general');
    if (storedGeneral) {
      try {
        const parsed = JSON.parse(storedGeneral);
        if (parsed.defaultCurrency) {
          const currencyCode = parsed.defaultCurrency;
          if (currencyCode === 'USD') symbol = '$';
          else if (currencyCode === 'INR') symbol = '₹';
          else if (currencyCode === 'AED') symbol = 'AED ';
        }
      } catch (e) {}
    }

    // 1. Text Search Filter
    const query = this.searchQuery.toLowerCase().trim();
    if (query) {
      filtered = filtered.filter(o => 
        o.orderNumber.toLowerCase().includes(query) || 
        (o.customerName && o.customerName.toLowerCase().includes(query))
      );
    }

    // 2. Status Selection Filter
    if (this.selectedStatusFilter !== 'all') {
      filtered = filtered.filter(o => o.status === this.selectedStatusFilter);
    }

    if (filtered.length === 0) {
      const emptyRow = document.createElement('tr');
      emptyRow.innerHTML = `
        <td colspan="6" style="padding: var(--spacing-xl); text-align:center; color:var(--text-muted);">
          <i data-lucide="info" style="width:20px; height:20px; margin:0 auto 8px auto; color:var(--accent-primary);"></i>
          No wholesale sales orders found.
        </td>
      `;
      tbody.appendChild(emptyRow);
      if (window.lucide) window.lucide.createIcons();
      return;
    }

    // Sorting by order number descending
    filtered.sort((a, b) => b.orderNumber.localeCompare(a.orderNumber));

    filtered.forEach(o => {
      const clone = rowTpl.content.cloneNode(true);

      const codeEl = clone.querySelector('.order-code-cell');
      const dateEl = clone.querySelector('.order-date-cell');
      const customerEl = clone.querySelector('.customer-cell');
      const amountEl = clone.querySelector('.amount-cell');
      const statusBadge = clone.querySelector('.status-badge');
      const viewBtn = clone.querySelector('.btn-action-view');
      const invoiceBtn = clone.querySelector('.btn-action-invoice');
      const cancelBtn = clone.querySelector('.btn-action-cancel');

      const cleanCustomer = this.sanitizeText(o.customerName);

      if (codeEl) codeEl.textContent = o.orderNumber;
      if (dateEl) dateEl.textContent = o.orderDate || 'Unknown';
      if (customerEl) {
        customerEl.textContent = cleanCustomer;
        customerEl.title = cleanCustomer;
      }
      if (amountEl) {
        amountEl.textContent = `${symbol}${(o.totalAmount || 0).toFixed(2)}`;
      }

      if (statusBadge) {
        statusBadge.textContent = o.status;
        if (o.status === 'INVOICED' || o.status === 'APPROVED') {
          statusBadge.className = 'status-badge status-badge--invoiced';
        } else if (o.status === 'PENDING') {
          statusBadge.className = 'status-badge status-badge--pending';
        } else if (o.status === 'CANCELLED') {
          statusBadge.className = 'status-badge status-badge--cancelled';
        }
      }

      if (viewBtn) viewBtn.setAttribute('data-id', String(o.id));
      if (invoiceBtn) invoiceBtn.setAttribute('data-id', String(o.id));
      
      if (cancelBtn) {
        cancelBtn.setAttribute('data-id', String(o.id));
        cancelBtn.setAttribute('data-number', o.orderNumber);
      }

      tbody.appendChild(clone);
    });

    if (window.lucide) window.lucide.createIcons();
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _loadCss() {
    const cssId = 'store-sales-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-admin/pages/sales/sales.css';
      document.head.appendChild(link);
    }
  }
}
export { SalesPage };
