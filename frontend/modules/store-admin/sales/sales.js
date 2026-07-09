/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : page.js
 * Path              : frontend/pages/sales/page.js
 * Purpose           : Frontend page component for the Pages Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/sales-orders, POST /api/v1/sales-orders
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : api/client, core/logger, store/notificationStore, services/dashboard/DashboardService
 * Depends On        : api/client, core/logger, store/notificationStore, services/dashboard/DashboardService
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend page component for the Pages Module UI. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { apiClient } from '../../../api/client.js';
import { logger } from '../../../core/logger.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { dashboardService } from '../../../services/dashboard/DashboardService.js';

export default class SalesPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  constructor() {
    this.orders = [];
    this.searchQuery = '';
    this.selectedStatusFilter = 'all';
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  async mount(container, lifecycle) {
    logger.info('SalesPage', 'Syncing sales ledger with backend database...');

    try {
      // Fetch sales orders from GET /api/v1/sales-orders
      const response = await apiClient.get('/api/v1/sales-orders', { size: 100 });
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (response && response.success && response.data && response.data.content) {
        this.orders = response.data.content;
      }
    } catch (err) {
      logger.error('SalesPage', 'Failed to synchronize data with database:', err);
      notificationStore.danger('Database connection failed. Displaying local cache.');
    }

    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  sanitizeText(text) {
    if (!text) return '';
    return text
      .replace(/\??/g, ' ')
      .replace(/ǩ/g, 'î')
      .replace(/Ǹ/g, 'é')
      .replace(/Ǫ/g, 'è');
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  render(container) {
    const totalOrders = this.orders.length;
    const grossRevenue = this.orders.reduce((acc, o) => acc + (o.totalAmount || 0), 0);
    const pendingOrders = this.orders.filter(o => o.status === 'PENDING' || o.status === 'APPROVED').length;
    const avgOrderValue = totalOrders > 0 ? (grossRevenue / totalOrders) : 0;

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
      } catch (e) {
        // ignore
      }
    }

    container.innerHTML = `
      <style>
        @media print {
          body, html {
            background: #ffffff !important;
            color: #000000 !important;
          }
          .sidebar, #app-sidebar, .dashboard-header, header, nav, #sidebar-toggle, .btn-icon,
          .btn, button, select, input, .form-group, #add-order-modal,
          thead th:last-child, tbody td:last-child,
          #select-filter-status, #input-search-orders {
            display: none !important;
          }
          .card.glass {
            background: none !important;
            border: 1px solid #ddd !important;
            color: #000000 !important;
            box-shadow: none !important;
          }
          tr {
            background: none !important;
            border-bottom: 1px solid #ccc !important;
          }
          td, th {
            color: #000000 !important;
          }
        }
      </style>
      <div style="display:flex; flex-direction:column; gap: var(--spacing-lg); max-width: 1200px; margin: 0 auto; padding-bottom: var(--spacing-xxl);">
        
        <!-- Header row -->
        <div class="flex justify-between align-center" style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
          <div>
            <div class="flex align-center gap-xs" style="color: var(--accent-primary); font-weight: 700; font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 4px;">
              <i data-lucide="bar-chart-3" style="width: 14px; height: 14px;"></i> Operations
            </div>
            <h1 style="font-family: var(--font-display); font-weight: 800; font-size: 1.8rem; margin: 0; color: var(--text-primary); letter-spacing: -0.01em;">Sales Overview</h1>
            <p style="color: var(--text-muted); font-size: 0.85rem; margin: 0; margin-top: 4px;">Trace customer wholesale invoices, check retail order status, and review gross revenue metrics.</p>
          </div>
          <div class="flex align-center gap-sm">
            <button id="btn-download-sales" class="btn" style="background: rgba(255,255,255,0.05); border: 1px solid var(--border-color); color: var(--text-secondary); font-weight: 700; font-size: 0.8rem; padding: 10px 16px; border-radius: var(--radius-md); display:flex; align-items:center; gap:6px; cursor:pointer;" onmouseover="this.style.borderColor='var(--border-color-hover)'" onmouseout="this.style.borderColor='var(--border-color)'">
              <i data-lucide="download" style="width:15px; height:15px; color: var(--accent-secondary);"></i> Export CSV
            </button>
            <button id="btn-print-sales" class="btn" style="background: rgba(255,255,255,0.05); border: 1px solid var(--border-color); color: var(--text-secondary); font-weight: 700; font-size: 0.8rem; padding: 10px 16px; border-radius: var(--radius-md); display:flex; align-items:center; gap:6px; cursor:pointer;" onmouseover="this.style.borderColor='var(--border-color-hover)'" onmouseout="this.style.borderColor='var(--border-color)'">
              <i data-lucide="printer" style="width:15px; height:15px; color: var(--accent-primary);"></i> Print Report
            </button>
            <button id="btn-add-order" class="btn" style="background: var(--accent-primary); color: #000; font-weight: 700; font-size: 0.8rem; padding: 10px 18px; border-radius: var(--radius-md); border:none; display:flex; align-items:center; gap:6px; cursor:pointer;">
              <i data-lucide="plus" style="width:16px; height:16px;"></i> Place Sales Order
            </button>
          </div>
        </div>

        <!-- KPI summary grid -->
        <div style="display:grid; grid-template-columns: repeat(4, 1fr); gap: var(--spacing-md);">
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-primary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Gross Revenue</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${symbol}${grossRevenue.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-success);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Invoiced Orders</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--status-success); margin-top: 6px;">${totalOrders - pendingOrders}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--status-warning);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Pending Approvals</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--status-warning); margin-top: 6px;">${pendingOrders}</span>
          </div>
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); min-height: 80px; position:relative; overflow:hidden;">
            <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: var(--accent-secondary);"></div>
            <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em;">Average Order Value</span>
            <span style="font-family: var(--font-display); font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-top: 6px;">${symbol}${avgOrderValue.toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
          </div>
        </div>

        <!-- Filter bar -->
        <div class="card glass flex align-center justify-between" style="padding: var(--spacing-md); border-color: rgba(255,255,255,0.03); gap:var(--spacing-md); flex-wrap:wrap;">
          
          <!-- Search box -->
          <div style="display:flex; align-items:center; gap: var(--spacing-md); width: 100%; max-width: 320px; background: rgba(0,0,0,0.15); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 8px 12px;">
            <i data-lucide="search" style="width:16px; height:16px; color: var(--text-muted);"></i>
            <input id="input-search-orders" type="text" placeholder="Search order number or client..." style="background:none; border:none; outline:none; color:var(--text-primary); font-size:0.8rem; width:100%;" value="${this.searchQuery}" />
          </div>

          <!-- Status Dropdown filter -->
          <div class="flex align-center gap-xs" style="font-size: 0.8rem;">
            <span style="color:var(--text-muted); font-weight:600;">Filter Status:</span>
            <select id="select-filter-status" style="background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-secondary); padding: 6px 12px; border-radius:var(--radius-md); outline:none; cursor:pointer;">
              <option value="all" ${this.selectedStatusFilter === 'all' ? 'selected' : ''}>All Orders</option>
              <option value="PENDING" ${this.selectedStatusFilter === 'PENDING' ? 'selected' : ''}>PENDING</option>
              <option value="INVOICED" ${this.selectedStatusFilter === 'INVOICED' ? 'selected' : ''}>INVOICED</option>
              <option value="CANCELLED" ${this.selectedStatusFilter === 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
            </select>
          </div>
        </div>

        <!-- Data table layout -->
        <div class="card glass" style="padding:0; overflow:hidden; border-color:rgba(255,255,255,0.05);">
          <div style="overflow-x:auto;">
            <table style="width:100%; border-collapse:collapse; text-align:left; font-size:0.75rem;">
              <thead>
                <tr style="background: rgba(255,255,255,0.02); border-bottom: 1px solid var(--border-color);">
                  <th style="padding:12px 16px; color:var(--text-muted); font-weight:700;">Order Code</th>
                  <th style="padding:12px 16px; color:var(--text-muted); font-weight:700;">Order Date</th>
                  <th style="padding:12px 16px; color:var(--text-muted); font-weight:700;">Customer</th>
                  <th style="padding:12px 16px; color:var(--text-muted); font-weight:700; text-align:right;">Amount</th>
                  <th style="padding:12px 16px; color:var(--text-muted); font-weight:700; text-align:center;">Status</th>
                  <th style="padding:12px 16px; color:var(--text-muted); font-weight:700; text-align:right;">Actions</th>
                </tr>
              </thead>
              <tbody id="sales-orders-tbody">
                ${this.renderTableRows()}
              </tbody>
            </table>
          </div>
        </div>

        <!-- Add Sales Order Modal -->
        <div id="add-order-modal" style="display:none; position:fixed; inset:0; background: rgba(0,0,0,0.7); z-index:99999; display:none; align-items:center; justify-content:center; padding: var(--spacing-lg); backdrop-filter: blur(4px);">
          <div class="card glass" style="width:100%; max-width: 450px; background: var(--bg-card); border-color: var(--border-color); padding: var(--spacing-xl); box-shadow: 0 25px 50px rgba(0,0,0,0.5);">
            <div class="flex justify-between align-center mb-md" style="border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
              <h3 style="font-family:var(--font-display); font-weight:800; font-size:1.15rem; margin:0; color:var(--text-primary);">Record New Sales Order</h3>
              <button id="btn-close-order-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer; font-size:1.25rem; line-height:1;">&times;</button>
            </div>
            
            <form id="form-add-order" style="display:flex; flex-direction:column; gap: var(--spacing-sm); text-align:left;">
              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Customer Name</label>
                <input id="order-modal-customer" type="text" placeholder="e.g. Café de Flore" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
              </div>
              
              <div style="display:grid; grid-template-columns: 1.1fr 0.9fr; gap:var(--spacing-sm);">
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Order Total Amount (${symbol})</label>
                  <input id="order-modal-amount" type="number" step="0.01" min="0.01" placeholder="e.g. 125.50" required style="padding:8px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
                
                <div class="form-group flex flex-col gap-xs">
                  <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Order Date</label>
                  <input id="order-modal-date" type="date" required style="padding:7px 10px; border-radius:var(--radius-md); background:rgba(0,0,0,0.2); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none;" />
                </div>
              </div>

              <div class="form-group flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; color:var(--text-muted); text-transform:uppercase;">Initial Status</label>
                <select id="order-modal-status" required style="padding:8px 10px; border-radius:var(--radius-md); background:var(--bg-card); border:1px solid var(--border-color); color:var(--text-primary); font-size:0.8rem; outline:none; cursor:pointer;">
                  <option value="PENDING">PENDING</option>
                  <option value="INVOICED" selected>INVOICED (Completed)</option>
                </select>
              </div>

              <button id="btn-order-submit" type="submit" class="btn" style="background:var(--accent-primary); color:#000; font-weight:700; font-size:0.8rem; padding:12px; margin-top:var(--spacing-sm); border:none; border-radius:var(--radius-md); cursor:pointer;">
                Register Order Record
              </button>
            </form>
          </div>
        </div>

        <!-- View Order Details Modal -->
        <div id="view-order-modal" style="display:none; position:fixed; inset:0; background: rgba(0,0,0,0.7); z-index:99999; align-items:center; justify-content:center; padding: var(--spacing-lg); backdrop-filter: blur(4px);">
          <div class="card glass animate-fade-in" style="width:100%; max-width: 480px; background: var(--bg-card); border-color: var(--border-color); padding: var(--spacing-xl); box-shadow: 0 25px 50px rgba(0,0,0,0.5); position:relative;">
            <div class="flex justify-between align-center mb-md" style="border-bottom:1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-sm);">
              <h3 style="font-family:var(--font-display); font-weight:800; font-size:1.15rem; margin:0; color:var(--text-primary); display:flex; align-items:center; gap:6px;">
                <i data-lucide="file-text" style="color:var(--accent-primary); width:18px; height:18px;"></i> Wholesale Sales Invoice
              </h3>
              <button id="btn-close-view-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer; font-size:1.25rem; line-height:1;" onmouseover="this.style.color='var(--text-primary)'" onmouseout="this.style.color='var(--text-muted)'">&times;</button>
            </div>
            
            <div id="view-order-details-content" style="display:flex; flex-direction:column; gap: var(--spacing-md); text-align:left;">
              <!-- Loaded dynamically -->
            </div>
            
            <div class="flex justify-end gap-sm" style="margin-top: var(--spacing-lg); border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-md);">
              <button id="modal-btn-download" class="btn" style="background: rgba(255,255,255,0.05); border: 1px solid var(--border-color); color: var(--text-secondary); font-size: 0.75rem; padding: 8px 12px; border-radius: var(--radius-md); display:flex; align-items:center; gap:4px; cursor:pointer;" onmouseover="this.style.borderColor='var(--border-color-hover)'" onmouseout="this.style.borderColor='var(--border-color)'">
                <i data-lucide="download" style="width:14px; height:14px; color:var(--accent-secondary);"></i> Download CSV
              </button>
              <button id="modal-btn-print" class="btn" style="background: var(--accent-primary); color: #000; font-weight:700; font-size: 0.75rem; padding: 8px 14px; border-radius: var(--radius-md); border:none; display:flex; align-items:center; gap:4px; cursor:pointer;">
                <i data-lucide="printer" style="width:14px; height:14px;"></i> Print Invoice
              </button>
            </div>
          </div>
        </div>

      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  renderTableRows() {
    let filtered = [...this.orders];

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
      } catch (e) {
        // ignore
      }
    }

    // 1. Text Search Filter
    const query = this.searchQuery.toLowerCase().trim();
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (query) {
      filtered = filtered.filter(o => 
        o.orderNumber.toLowerCase().includes(query) || 
        (o.customerName && o.customerName.toLowerCase().includes(query))
      );
    }

    // 2. Status selection filter
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (this.selectedStatusFilter !== 'all') {
      filtered = filtered.filter(o => o.status === this.selectedStatusFilter);
    }

    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (filtered.length === 0) {
      return `
        <tr>
          <td colspan="6" style="padding: var(--spacing-xl); text-align:center; color:var(--text-muted);">
            <i data-lucide="info" style="width:20px; height:20px; margin:0 auto 8px auto; color:var(--accent-primary);"></i>
            No wholesale sales orders found.
          </td>
        </tr>
      `;
    }

    // Sorting by order number descending
    filtered.sort((a, b) => b.orderNumber.localeCompare(a.orderNumber));

    return filtered.map(o => {
      const cleanCustomer = this.sanitizeText(o.customerName);
      
      // Status styling
      const statusText = o.status;
      let statusColor = 'var(--text-muted)';
      let statusBg = 'rgba(255,255,255,0.02)';
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (statusText === 'INVOICED' || statusText === 'APPROVED') {
        statusColor = 'var(--status-success)';
        statusBg = 'rgba(16,185,129,0.06)';
      } else if (statusText === 'PENDING') {
        statusColor = 'var(--status-warning)';
        statusBg = 'rgba(245,158,11,0.06)';
      } else if (statusText === 'CANCELLED') {
        statusColor = 'var(--status-danger)';
        statusBg = 'rgba(239,68,68,0.06)';
      }

      return `
        <tr style="border-bottom:1px solid rgba(255,255,255,0.03); transition: background var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.01)'" onmouseout="this.style.background='none'">
          <td style="padding:12px 16px; font-weight:700; color:var(--text-primary); font-family:monospace;">${o.orderNumber}</td>
          <td style="padding:12px 16px; color:var(--text-secondary);">${o.orderDate || 'Unknown'}</td>
          <td style="padding:12px 16px; color:var(--text-primary); font-weight:600;" title="${cleanCustomer}">${cleanCustomer}</td>
          <td style="padding:12px 16px; text-align:right; font-weight:700; color:var(--text-primary);">${symbol}${(o.totalAmount || 0).toFixed(2)}</td>
          <td style="padding:12px 16px; text-align:center;">
            <span style="display:inline-block; font-size:0.62rem; font-weight:700; color:${statusColor}; background:${statusBg}; border:1px solid ${statusColor}40; border-radius:3px; padding:2px 6px; letter-spacing:0.04em;">
              ${statusText}
            </span>
          </td>
          <td style="padding:12px 16px; text-align:right;">
            <div class="flex justify-end gap-xs">
              <!-- View details -->
              <button class="btn-view-order" data-id="${o.id}" title="See Details" style="background:none; border:none; color:var(--accent-secondary); cursor:pointer; padding:4px; border-radius:3px; display:inline-flex; align-items:center;">
                <i data-lucide="eye" style="width:14px; height:14px;"></i>
              </button>

              <!-- Download individual receipt -->
              <button class="btn-download-invoice" data-id="${o.id}" title="Download Receipt" style="background:none; border:none; color:var(--text-muted); opacity:0.6; cursor:pointer; padding:4px; border-radius:3px; display:inline-flex; align-items:center;" onmouseover="this.style.opacity='1'" onmouseout="this.style.opacity='0.6'">
                <i data-lucide="download" style="width:14px; height:14px;"></i>
              </button>

              <!-- Print individual invoice -->
              <button class="btn-print-invoice" data-id="${o.id}" title="Print Invoice" style="background:none; border:none; color:var(--accent-primary); cursor:pointer; padding:4px; border-radius:3px; display:inline-flex; align-items:center;">
                <i data-lucide="printer" style="width:14px; height:14px;"></i>
              </button>

              <!-- Complete Fulfill Trigger -->
              ${statusText === 'PENDING' ? `
                <button class="btn-fulfill-order" data-id="${o.id}" data-number="${o.orderNumber}" title="Fulfill / Invoice Order" style="background:none; border:none; color:var(--status-success); cursor:pointer; padding:4px; border-radius:3px; display:inline-flex; align-items:center;">
                  <i data-lucide="check-circle" style="width:14px; height:14px;"></i>
                </button>
              ` : ''}
              
              <!-- Cancel Trigger -->
              ${statusText === 'PENDING' || statusText === 'APPROVED' ? `
                <button class="btn-cancel-order" data-id="${o.id}" data-number="${o.orderNumber}" title="Cancel Order" style="background:none; border:none; color:var(--status-danger); cursor:pointer; padding:4px; border-radius:3px; display:inline-flex; align-items:center;">
                  <i data-lucide="x-circle" style="width:14px; height:14px;"></i>
                </button>
              ` : ''}

              <!-- Delete Soft-Delete -->
              <button class="btn-delete-order" data-id="${o.id}" data-number="${o.orderNumber}" title="Delete Record" style="background:none; border:none; color:var(--text-muted); opacity:0.6; cursor:pointer; padding:4px; border-radius:3px; display:inline-flex; align-items:center;" onmouseover="this.style.opacity='1'; this.style.color='var(--status-danger)'" onmouseout="this.style.opacity='0.6'; this.style.color='var(--text-muted)'">
                <i data-lucide="trash-2" style="width:14px; height:14px;"></i>
              </button>
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
    const searchInput = container.querySelector('#input-search-orders');
    const statusFilter = container.querySelector('#select-filter-status');
    const tbody = container.querySelector('#sales-orders-tbody');
    const btnAddOrder = container.querySelector('#btn-add-order');
    const btnDownloadSales = container.querySelector('#btn-download-sales');
    const btnPrintSales = container.querySelector('#btn-print-sales');
    const modal = container.querySelector('#add-order-modal');
    const btnCloseModal = container.querySelector('#btn-close-order-modal');
    const formAddOrder = container.querySelector('#form-add-order');
    const submitBtn = container.querySelector('#btn-order-submit');

    // Export CSV Binding
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (btnDownloadSales) {
      /**
       * Handles the handle download event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleDownload = () => this.downloadCSV();
      btnDownloadSales.addEventListener('click', handleDownload);
      lifecycle.onCleanup(() => btnDownloadSales.removeEventListener('click', handleDownload));
    }

    // Print Report Binding
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (btnPrintSales) {
      /**
       * Handles the handle print event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handlePrint = () => this.printReport();
      btnPrintSales.addEventListener('click', handlePrint);
      lifecycle.onCleanup(() => btnPrintSales.removeEventListener('click', handlePrint));
    }

    // 1. Text Search Input
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (searchInput) {
      /**
       * Handles the handle search event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleSearch = (e) => {
        this.searchQuery = e.target.value;
        this.refreshTable(container, lifecycle);
      };
      searchInput.addEventListener('input', handleSearch);
      lifecycle.onCleanup(() => searchInput.removeEventListener('input', handleSearch));
    }

    // 2. Status Dropdown Filter
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (statusFilter) {
      /**
       * Handles the handle status filter event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleStatusFilter = (e) => {
        this.selectedStatusFilter = e.target.value;
        this.refreshTable(container, lifecycle);
      };
      statusFilter.addEventListener('change', handleStatusFilter);
      lifecycle.onCleanup(() => statusFilter.removeEventListener('change', handleStatusFilter));
    }

    // 3. Open Modal
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (btnAddOrder && modal) {
      /**
       * Performs the openModal operation in this module.
       * @memberof Pages Module
       */
      const openModal = () => {
        modal.style.display = 'flex';
        const dateInput = container.querySelector('#order-modal-date');
        if (dateInput) dateInput.value = new Date().toISOString().substring(0, 10);
      };
      btnAddOrder.addEventListener('click', openModal);
      lifecycle.onCleanup(() => btnAddOrder.removeEventListener('click', openModal));
    }

    // 4. Close Modal
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (btnCloseModal && modal) {
      /**
       * Completes the close modal workflow and finalizes the record status.
       * @memberof Pages Module
       */
      const closeModal = () => {
        modal.style.display = 'none';
      };
      btnCloseModal.addEventListener('click', closeModal);
      lifecycle.onCleanup(() => btnCloseModal.removeEventListener('click', closeModal));

      /**
       * Performs the backdropClose operation in this module.
       * @memberof Pages Module
       */
      const backdropClose = (e) => {
        /**
         * Performs the fn operation in this module.
         * @memberof Pages Module
         */
        if (e.target === modal) {
          modal.style.display = 'none';
        }
      };
      modal.addEventListener('click', backdropClose);
      lifecycle.onCleanup(() => modal.removeEventListener('click', backdropClose));
    }

    // 5. Form Submit connection to POST /api/v1/sales-orders
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (formAddOrder && modal) {
      /**
       * Handles the handle submit event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleSubmit = async (e) => {
        e.preventDefault();

        const customerName = container.querySelector('#order-modal-customer').value.trim();
        const totalAmount = Number(container.querySelector('#order-modal-amount').value);
        const orderDate = container.querySelector('#order-modal-date').value;
        const status = container.querySelector('#order-modal-status').value;

        /**
         * Performs the fn operation in this module.
         * @memberof Pages Module
         */
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
            } catch (e) {}
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

          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (response && response.success) {
            // Force dynamic metrics update
            await dashboardService.getDashboardOverview();
            
            modal.style.display = 'none';
            formAddOrder.reset();

            // Refresh UI
            await this.mount(container, lifecycle);

            notificationStore.success(`Sales Order '${orderNumber}' registered successfully!`);
          } else {
            throw new Error(response.message || 'Operation rejected by database engine.');
          }

        } catch (err) {
          logger.error('SalesPage', 'Failed to register sales order:', err);
          notificationStore.danger(`Registration failed: ${err.message}`);
        } finally {
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Register Order Record';
          }
        }
      };

      formAddOrder.addEventListener('submit', handleSubmit);
      lifecycle.onCleanup(() => formAddOrder.removeEventListener('submit', handleSubmit));
    }

    // 6. Bind Actions (Fulfill, Cancel, Delete)
    this.bindTableActions(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  refreshTable(container, lifecycle) {
    const tbody = container.querySelector('#sales-orders-tbody');
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (tbody) {
      tbody.innerHTML = this.renderTableRows();
      if (window.lucide) window.lucide.createIcons();
      this.bindTableActions(container, lifecycle);
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindTableActions(container, lifecycle) {
    // 1. Soft-Delete Record
    const deleteButtons = container.querySelectorAll('.btn-delete-order');
    deleteButtons.forEach(btn => {
      /**
       * Handles the handle delete event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleDelete = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const number = btn.getAttribute('data-number');
        
        if (confirm(`Are you sure you want to delete sales order record "${number}"?`)) {
          try {
            const res = await apiClient.delete(`/api/v1/sales-orders/${id}`);
            /**
             * Performs the fn operation in this module.
             * @memberof Pages Module
             */
            if (res && res.success) {
              notificationStore.success(`Order "${number}" deleted successfully.`);
              
              await dashboardService.getDashboardOverview();
              await this.mount(container, lifecycle);
            } else {
              throw new Error(res.message || 'Operation rejected by backend API.');
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

    // 2. Fulfill Order (Mark as INVOICED)
    const fulfillButtons = container.querySelectorAll('.btn-fulfill-order');
    fulfillButtons.forEach(btn => {
      /**
       * Handles the handle fulfill event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleFulfill = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const number = btn.getAttribute('data-number');
        
        try {
          // Use PUT endpoint to transition status to INVOICED
          const res = await apiClient.put(`/api/v1/sales-orders/${id}/status`, { status: 'INVOICED' });
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (res && res.success) {
            notificationStore.success(`Order "${number}" marked as INVOICED successfully.`);
            
            await dashboardService.getDashboardOverview();
            await this.mount(container, lifecycle);
          } else {
            throw new Error(res.message || 'State modification rejected.');
          }
        } catch (err) {
          logger.error('SalesPage', `Fulfillment failed:`, err);
          notificationStore.danger(`Fulfillment failed: ${err.message}`);
        }
      };
      btn.addEventListener('click', handleFulfill);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleFulfill));
    });

    // 3. Cancel Order (Mark as CANCELLED)
    const cancelButtons = container.querySelectorAll('.btn-cancel-order');
    cancelButtons.forEach(btn => {
      /**
       * Handles the handle cancel event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handleCancel = async (e) => {
        e.stopPropagation();
        const id = btn.getAttribute('data-id');
        const number = btn.getAttribute('data-number');
        
        try {
          const res = await apiClient.put(`/api/v1/sales-orders/${id}/status`, { status: 'CANCELLED' });
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (res && res.success) {
            notificationStore.success(`Order "${number}" cancelled successfully.`);
            
            await dashboardService.getDashboardOverview();
            await this.mount(container, lifecycle);
          } else {
            throw new Error(res.message || 'Cancellation rejected.');
          }
        } catch (err) {
          logger.error('SalesPage', `Cancellation failed:`, err);
          notificationStore.danger(`Cancellation failed: ${err.message}`);
        }
      };
      btn.addEventListener('click', handleCancel);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleCancel));
    });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  downloadCSV() {
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
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

    // Construct CSV content line-by-line with safe escaping
    const csvRows = [headers.join(',')];
    /**
     * Performs the r operation in this module.
     * @memberof Pages Module
     */
    for (const r of rows) {
      csvRows.push(r.map(val => `"${String(val).replace(/"/g, '""')}"`).join(','));
    }
    const csvContent = "\uFEFF" + csvRows.join('\n'); // Prefix with BOM for proper Excel encoding

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

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  printReport() {
    window.print();
  }
}




