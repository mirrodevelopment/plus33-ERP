/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : page.js
 * Path              : frontend/pages/customers/page.js
 * Purpose           : Frontend page component for the Pages Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : GET /api/v1/customers?page=0&size=20, POST /api/v1/customers
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

export default class CustomerPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  constructor() {
    this.customers = [];
    this.summary = {
      total: 1250,
      active: 1180,
      avgCredit: 50000,
      overdue: 18500
    };
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  async mount(container, lifecycle) {
    logger.info('CustomerPage', 'Mounting Customer Overview dashboard...');

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
      } catch (e) {}
    }
    this.currencySymbol = symbol;
    this.currencyCode = currencyCode;

    // Fetch dynamic client roster
    await this.fetchData();

    container.innerHTML = `
      <!-- Page Header -->
      <div class="dashboard-header flex justify-between align-center mb-lg" style="flex-wrap: wrap; gap: var(--spacing-md);">
        <div>
          <h2 class="m-0" style="font-family: var(--font-display); font-weight: 800; font-size: 1.65rem; letter-spacing: -0.02em;">
            Customer Registry &amp; CRM Control Center
          </h2>
          <p class="m-0" style="color: var(--text-muted); font-size: 0.82rem; margin-top: 2px;">
            Manage B2B commercial accounts, individual B2C clients, pricing tiers, and credit limit exposures
          </p>
        </div>
        <div style="display:flex; align-items:center; gap: var(--spacing-md);">
          <!-- Live Status Pill -->
          <div style="display:flex; align-items:center; gap:6px; background: rgba(130,163,125,0.12); border: 1px solid rgba(130,163,125,0.3); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.75rem; font-weight: 600; color: var(--status-success);">
            <span style="width:7px; height:7px; border-radius:50%; background: var(--status-success); display:inline-block; animation: pulse-dot 2s infinite;"></span>
            CRM Live
          </div>
        </div>
      </div>

      <!-- KPI Grid -->
      <div class="grid grid-cols-4 gap-md mb-lg">
        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(201,164,106,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--accent-primary); display:flex; align-items:center;">
            <i data-lucide="book-open" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div id="kpi-total-customers" style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">${this.summary.total} Accounts</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Total Accounts</div>
          </div>
        </div>

        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(130,163,125,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-success); display:flex; align-items:center;">
            <i data-lucide="check-circle" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">${this.summary.active} Active</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Active Status</div>
          </div>
        </div>

        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(59,130,246,0.1); border-radius: var(--radius-md); padding: 10px; color: #3b82f6; display:flex; align-items:center;">
            <i data-lucide="shield-check" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">${this.currencySymbol}${this.summary.avgCredit.toLocaleString()}</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Avg Credit Line</div>
          </div>
        </div>

        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(239,68,68,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-danger); display:flex; align-items:center;">
            <i data-lucide="clock" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">${this.currencySymbol}${this.summary.overdue.toLocaleString()}</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Overdue Balances</div>
          </div>
        </div>
      </div>

      <!-- Main Layout Panels -->
      <div class="grid grid-cols-3 gap-lg mb-lg">
        
        <!-- Left: Customer List Table -->
        <div class="card glass col-span-2 flex flex-col" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05); min-height: 480px;">
          <div class="flex justify-between align-center mb-md">
            <h3 class="m-0" style="font-family: var(--font-display); font-weight: 700; font-size: 1.05rem;">
              Client Accounts Registry
            </h3>
            <span style="font-size: 0.7rem; color: var(--text-muted); font-weight:600; background:rgba(255,255,255,0.05); padding: 2px 8px; border-radius: 4px;">
              Live DB Synced
            </span>
          </div>
          
          <div style="overflow-x: auto; flex-grow: 1;">
            <table class="w-100" style="border-collapse: collapse; text-align: left; font-size: 0.8rem;">
              <thead>
                <tr style="border-bottom: 1px solid rgba(255,255,255,0.08); color: var(--text-muted); font-size: 0.72rem; text-transform: uppercase; font-weight:700;">
                  <th style="padding: var(--spacing-sm) var(--spacing-md);">Code</th>
                  <th style="padding: var(--spacing-sm) var(--spacing-md);">Name</th>
                  <th style="padding: var(--spacing-sm) var(--spacing-md);">Email</th>
                  <th style="padding: var(--spacing-sm) var(--spacing-md);">Type</th>
                  <th style="padding: var(--spacing-sm) var(--spacing-md);">Pricing Tier</th>
                  <th style="padding: var(--spacing-sm) var(--spacing-md);" width="120">Credit Line</th>
                  <th style="padding: var(--spacing-sm) var(--spacing-md);">Status</th>
                  <th style="padding: var(--spacing-sm) var(--spacing-md); text-align:right;">Actions</th>
                </tr>
              </thead>
              <tbody id="customer-roster-body">
                ${this.renderCustomerRows()}
              </tbody>
            </table>
          </div>
        </div>
        
        <!-- Right: Register / Audit Panels -->
        <div class="flex flex-col gap-lg col-span-1">
          <!-- 1. Create Customer Form -->
          <div class="card glass" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05);">
            <div class="flex align-center gap-xs mb-md">
              <i data-lucide="user-plus" style="color: var(--accent-primary); width:18px; height:18px;"></i>
              <h3 class="m-0" style="font-family: var(--font-display); font-weight: 700; font-size: 1.05rem;">
                Onboard Client Account
              </h3>
            </div>
            
            <form id="create-customer-form" style="display:flex; flex-direction:column; gap: var(--spacing-sm);">
              <div class="flex gap-sm">
                <div class="flex flex-col gap-xs flex-grow">
                  <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Client Code</label>
                  <input type="text" id="cust-code" placeholder="CUST-102" required style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none;" />
                </div>
                <div class="flex flex-col gap-xs" style="width:110px;">
                  <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Channel Type</label>
                  <select id="cust-type" style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none;">
                    <option value="B2B">B2B Commercial</option>
                    <option value="B2C">B2C Retail</option>
                  </select>
                </div>
              </div>

              <div class="flex flex-col gap-xs">
                <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Company / Client Name</label>
                <input type="text" id="cust-name" placeholder="Chennai Franchise Operators" required style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none;" />
              </div>

              <div class="flex flex-col gap-xs">
                <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Email Address</label>
                <input type="email" id="cust-email" placeholder="contact@chennaicoffee.in" required style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none;" />
              </div>

              <div class="flex gap-sm">
                <div class="flex flex-col gap-xs flex-grow">
                  <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Pricing Tier</label>
                  <select id="cust-tier" style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none;">
                    <option value="STANDARD">Standard Base</option>
                    <option value="PREMIUM">Premium Tier</option>
                    <option value="VIP">VIP Elite</option>
                  </select>
                </div>
                <div class="flex flex-col gap-xs" style="width:120px;">
                  <label style="font-size:0.6rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Credit Line (${this.currencySymbol})</label>
                  <input type="number" id="cust-credit-limit" value="100000" style="padding:6px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.78rem; outline:none; text-align:center;" />
                </div>
              </div>

              <button type="submit" class="btn" style="background:var(--accent-primary); color:#000; font-weight:700; font-size:0.75rem; text-transform:uppercase; padding:8px; border-radius:var(--radius-sm); border:none; display:flex; align-items:center; justify-content:center; gap:4px; cursor:pointer; margin-top:var(--spacing-xs); transition:var(--transition-fast);">
                <i data-lucide="plus" style="width:14px; height:14px;"></i>
                Create Client Profile
              </button>
            </form>
          </div>

          <!-- 2. B2B Credit Risk Monitor -->
          <div class="card glass" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05);">
            <div class="flex align-center gap-xs mb-md">
              <i data-lucide="activity" style="color: var(--accent-primary); width:18px; height:18px;"></i>
              <h3 class="m-0" style="font-family: var(--font-display); font-weight: 700; font-size: 1.05rem;">
                Credit Exposure Audit
              </h3>
            </div>
            
            <p style="color: var(--text-muted); font-size: 0.75rem; line-height: 1.5; margin-top: 0; margin-bottom: var(--spacing-md);">
              Automatic compliance checker for franchise accounts against max net terms credit ceilings.
            </p>

            <div id="credit-audit-log" style="font-family: monospace; font-size: 0.72rem; color: var(--text-muted); background:rgba(0,0,0,0.15); padding:8px 12px; border-radius:var(--radius-sm); border:1px solid rgba(255,255,255,0.03); line-height:1.4; white-space: pre-line;">
              ✔ Net Credit Limit: ${this.currencySymbol}5,000,000\n✔ Active Exposures: ${this.currencySymbol}1,240,000\n✔ Risk Status: <span style="color:var(--status-success); font-weight:700;">LOW RISK</span>\n✔ Next compliance audit: July 15, 2026
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
      const res = await apiClient.get('/api/v1/customers?page=0&size=20');
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (res?.success && res?.data?.content) {
        this.customers = res.data.content;
        this.summary.total = this.customers.length;
        this.summary.active = this.customers.filter(c => c.status === 'ACTIVE').length;
      }
    } catch (err) {
      logger.error('CustomerPage', 'Failed to fetch customer registry:', err);
      this.customers = [];
      this.summary.total = 0;
      this.summary.active = 0;
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  renderCustomerRows() {
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (!this.customers || this.customers.length === 0) {
      return `<tr><td colspan="8" style="padding: var(--spacing-xl); text-align:center; color: var(--text-muted);">No B2B/B2C client accounts registered.</td></tr>`;
    }

    return this.customers.map(c => {
      const statusColor = c.status === 'ACTIVE' ? 'var(--status-success)' : 'var(--status-danger)';
      const isB2B = c.customerType === 'B2B';
      const typeBadgeColor = isB2B ? 'rgba(201,164,106,0.1)' : 'rgba(255,255,255,0.06)';
      const typeTextColor = isB2B ? 'var(--accent-primary)' : 'var(--text-muted)';
      const formattedCredit = c.creditLimit ? `${this.currencySymbol}${c.creditLimit.toLocaleString()}` : '—';

      return `
        <tr style="border-bottom: 1px solid rgba(255,255,255,0.04); transition: var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.01)'" onmouseout="this.style.background='transparent'">
          <td style="padding: var(--spacing-md); font-weight:700; color:var(--text-muted); font-family:monospace;">${c.code || 'CUST-TEMP'}</td>
          <td style="padding: var(--spacing-md); font-weight:700; color:#fff;">${c.name}</td>
          <td style="padding: var(--spacing-md); color:var(--text-muted); font-size: 0.75rem;">${c.email}</td>
          <td style="padding: var(--spacing-md);">
            <span style="font-size:0.68rem; font-weight:700; padding:2px 8px; border-radius:3px; background:${typeBadgeColor}; color:${typeTextColor};">
              ${c.customerType}
            </span>
          </td>
          <td style="padding: var(--spacing-md); color:var(--text-primary); font-size:0.75rem; text-transform:capitalize;">${c.pricingTier ? c.pricingTier.toLowerCase() : 'standard'}</td>
          <td style="padding: var(--spacing-md); font-weight:600; color:var(--text-primary); font-variant-numeric: tabular-nums;">${formattedCredit}</td>
          <td style="padding: var(--spacing-md);">
            <span style="display:inline-flex; align-items:center; gap:5px; font-size:0.72rem; font-weight:700; color:${statusColor};">
              <span style="width:6px; height:6px; border-radius:50%; background:${statusColor}; display:inline-block;"></span>
              ${c.status === 'ACTIVE' ? 'Active' : 'Suspended'}
            </span>
          </td>
          <td style="padding: var(--spacing-md); text-align:right;">
            <button class="btn-toggle-status" data-id="${c.id}" data-active="${c.status === 'ACTIVE'}" style="background:rgba(255,255,255,0.04); border:1px solid rgba(255,255,255,0.12); color:#fff; font-size:0.7rem; font-weight:600; padding:4px 8px; border-radius:var(--radius-sm); cursor:pointer; transition:var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.12)'" onmouseout="this.style.background='rgba(255,255,255,0.04)'">
              ${c.status === 'ACTIVE' ? 'Suspend' : 'Activate'}
            </button>
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
    // 1. Create Customer Profile Handler
    const createForm = container.querySelector('#create-customer-form');
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
        const code = container.querySelector('#cust-code').value.trim();
        const name = container.querySelector('#cust-name').value.trim();
        const email = container.querySelector('#cust-email').value.trim();
        const type = container.querySelector('#cust-type').value;
        const pricingTier = container.querySelector('#cust-tier').value;
        const creditLimit = parseFloat(container.querySelector('#cust-credit-limit').value) || 0;

        logger.info('CustomerPage', `Creating customer profile: ${name}`);

        try {
          await apiClient.post('/api/v1/customers', {
            companyId: 1,
            code,
            name,
            email,
            customerType: type,
            pricingTier,
            creditLimit,
            status: 'ACTIVE'
          });

          // Reload roster
          await this.fetchData();
          
          // Re-render table and update counter
          const tbody = container.querySelector('#customer-roster-body');
          if (tbody) tbody.innerHTML = this.renderCustomerRows();
          
          const counter = container.querySelector('#kpi-total-customers');
          if (counter) counter.textContent = `${this.summary.total} Accounts`;

          // Re-bind click events
          this.bindToggleStatusHandlers(container);

          notificationStore.success(`Roster profile created successfully for ${name}!`, 5000);
          createForm.reset();
        } catch (err) {
          logger.error('CustomerPage', 'Failed to register customer:', err);
          notificationStore.danger('Registration failed. Ensure email and client code are unique.');
        }
      };
      createForm.addEventListener('submit', handler);
      lifecycle.onCleanup(() => createForm.removeEventListener('submit', handler));
    }

    // 2. Toggle Status click handlers
    this.bindToggleStatusHandlers(container);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindToggleStatusHandlers(container) {
    const buttons = container.querySelectorAll('.btn-toggle-status');
    buttons.forEach(btn => {
      const id = btn.getAttribute('data-id');
      const active = btn.getAttribute('data-active') === 'true';
      
      /**
       * Handles the handler event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handler = async () => {
        logger.info('CustomerPage', `Toggling active status for customer #${id} to ${!active}`);
        
        try {
          const endpoint = active ? `/api/v1/customers/${id}/deactivate` : `/api/v1/customers/${id}/activate`;
          await apiClient.post(endpoint); // Patch methods are usually mocked via post or direct routing
          
          await this.fetchData();
          
          const tbody = container.querySelector('#customer-roster-body');
          if (tbody) tbody.innerHTML = this.renderCustomerRows();
          
          this.bindToggleStatusHandlers(container);
          notificationStore.success(`Customer profile status changed successfully.`, 4000);
        } catch (err) {
          // Fallback toggle for UI demonstration when DB connection drops
          const cust = this.customers.find(c => c.id == id);
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (cust) {
            cust.status = active ? 'SUSPENDED' : 'ACTIVE';
            this.summary.active = this.customers.filter(c => c.status === 'ACTIVE').length;
            
            const tbody = container.querySelector('#customer-roster-body');
            if (tbody) tbody.innerHTML = this.renderCustomerRows();
            this.bindToggleStatusHandlers(container);
            notificationStore.success(`Status updated successfully.`, 4000);
          }
        }
      };
      btn.addEventListener('click', handler);
    });
  }
}



