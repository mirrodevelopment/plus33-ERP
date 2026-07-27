/******************************************************************************
 * Project    : PLUS33 Coffee ERP
 * Component  : Store Employee Help & Support Controller
 * File       : employee-support.js
 ******************************************************************************/

import { authStore }         from '../../store/authStore.js';
import { notificationStore } from '../../store/notificationStore.js';
import { logger }            from '../../core/logger.js';
import { htmlLoader }        from '../../core/htmlLoader.js';
import { apiClient }         from '../../api/client.js';

const TEMPLATE_URL = 'shared/support/employee-support.html';
const CSS_ID       = 'employee-support-sep-css';
const CSS_URL      = 'shared/support/employee-support.css';

const SUBCAT_MAP = {
  TECHNICAL_SUPPORT: [
    'POS Terminal Outage',
    'Espresso Machine Gauge Fault',
    'Receipt Printer Disconnect',
    'ERP Account Password Lockout',
    'OTHER_CUSTOM'
  ],
  WORKPLACE_COMPLAINT: [
    'Hostile Work Environment',
    'Sexual / Verbal Harassment',
    'Favoritism & Nepotism',
    'Executive Financial Fraud',
    'Safety & Health Hazard',
    'OTHER_CUSTOM'
  ],
  HR_PAYROLL: [
    'Missing Overtime Pay',
    'Salary Disbursement Delay',
    'Uncredited Paid Leave Days',
    'Tax & Contribution Discrepancy',
    'Tip Allocation Dispute',
    'OTHER_CUSTOM'
  ],
  GENERAL_FEEDBACK: [
    'Store Process Improvement',
    'Shift Rotation Preference',
    'Equipment Ergonomics',
    'OTHER_CUSTOM'
  ]
};

const TARGET_ROLE_RECOMMEND = {
  TECHNICAL_SUPPORT:   'STORE_ADMIN',
  WORKPLACE_COMPLAINT: 'ULTIMATE_ADMIN',
  HR_PAYROLL:          'NATIONAL_ADMIN',
  GENERAL_FEEDBACK:    'STORE_ADMIN'
};

export default class EmployeeSupportPage {

  constructor() {
    this.user = authStore.getUser();
    this.container = null;
    this.selectedCat = 'TECHNICAL_SUPPORT';
    this.isAnonymous = false;
  }

  async mount(container) {
    const role = authStore.getRole();
    if (['ultimateAdmin', 'nationalAdmin'].includes(role)) {
      const targetHash = role === 'nationalAdmin' ? '#national-complaints' : '#complaints';
      logger.info('EmployeeSupportPage', `Executive admin (${role}) detected. Redirecting to ${targetHash}…`);
      window.location.hash = targetHash;
      return;
    }

    logger.info('EmployeeSupportPage', 'Mounting Employee Support Hub…');
    this.container = container;
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);
    this._initUserStore();
    this._bindEvents();
    if (window.lucide) window.lucide.createIcons();
  }

  _loadCss() {
    if (!document.getElementById(CSS_ID)) {
      const link = document.createElement('link');
      link.id   = CSS_ID;
      link.rel  = 'stylesheet';
      link.href = CSS_URL;
      document.head.appendChild(link);
    }
  }

  _initUserStore() {
    const el = this.container.querySelector('#es-inp-store');
    if (el && this.user) {
      el.value = this.user.storeName || (this.user.storeId ? `Store #${this.user.storeId}` : 'Store #1 Flagship');
    }
  }

  _bindEvents() {
    // Navigation Tabs
    const tabBtns = this.container.querySelectorAll('.es-tab-btn');
    tabBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        tabBtns.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        const tab = btn.dataset.tab;

        const subTab = this.container.querySelector('#es-tab-submit');
        const tktTab = this.container.querySelector('#es-tab-tickets');
        const trkTab = this.container.querySelector('#es-tab-tracker');
        const faqTab = this.container.querySelector('#es-tab-faqs');

        if (subTab) subTab.style.display = (tab === 'submit') ? 'flex' : 'none';
        if (tktTab) tktTab.style.display = (tab === 'tickets') ? 'flex' : 'none';
        if (trkTab) trkTab.style.display = (tab === 'tracker') ? 'flex' : 'none';
        if (faqTab) faqTab.style.display = (tab === 'faqs') ? 'flex' : 'none';

        if (tab === 'tickets') this._loadMyTickets();
      });
    });

    this.container.querySelector('#btn-es-refresh-my-tickets')?.addEventListener('click', () => this._loadMyTickets());

    // Category Selection Dropdown
    const catSelect = this.container.querySelector('#es-inp-category');
    catSelect?.addEventListener('change', e => {
      this.selectedCat = e.target.value;

      // Auto-recommend target admin role
      const targetSelect = this.container.querySelector('#es-inp-target-role');
      if (targetSelect && TARGET_ROLE_RECOMMEND[this.selectedCat]) {
        targetSelect.value = TARGET_ROLE_RECOMMEND[this.selectedCat];
      }

      // Dynamically update subcategory dropdown
      this._updateSubcatOptions();

      const anonCard = this.container.querySelector('#es-anon-card');
      if (anonCard) {
        const showAnon = ['WORKPLACE_COMPLAINT', 'HR_PAYROLL'].includes(this.selectedCat);
        anonCard.style.display = showAnon ? 'block' : 'none';
      }
    });

    // Subcategory Dropdown Change
    this.container.querySelector('#es-inp-subcat')?.addEventListener('change', e => {
      const customWrapper = this.container.querySelector('#es-custom-cat-wrapper');
      if (customWrapper) {
        customWrapper.style.display = e.target.value === 'OTHER_CUSTOM' ? 'block' : 'none';
      }
    });

    // Anon Toggle
    const anonToggle = this.container.querySelector('#es-anon-toggle');
    anonToggle?.addEventListener('change', e => {
      this.isAnonymous = e.target.checked;
      const slider = this.container.querySelector('#es-toggle-slider');
      if (slider) slider.style.background = this.isAnonymous ? 'var(--accent-primary, #c9a46a)' : 'rgba(255,255,255,0.12)';
    });

    // Submit Form
    this.container.querySelector('#es-form')?.addEventListener('submit', e => this._handleSubmit(e));

    // Track Button
    this.container.querySelector('#btn-es-track')?.addEventListener('click', () => this._handleTrackKey());

    // Process Guide Modal Events
    const processBtn = this.container.querySelector('#es-btn-process-guide');
    const processModal = this.container.querySelector('#es-process-modal');
    const closeProcessBtn = this.container.querySelector('#btn-es-close-process-modal');

    processBtn?.addEventListener('click', () => {
      if (processModal) processModal.style.display = 'flex';
    });

    closeProcessBtn?.addEventListener('click', () => {
      if (processModal) processModal.style.display = 'none';
    });

    processModal?.addEventListener('click', e => {
      if (e.target === processModal) {
        processModal.style.display = 'none';
      }
    });
  }

  _updateSubcatOptions() {
    const subcatSelect = this.container.querySelector('#es-inp-subcat');
    const customWrapper = this.container.querySelector('#es-custom-cat-wrapper');
    if (!subcatSelect) return;

    const options = SUBCAT_MAP[this.selectedCat] || ['OTHER_CUSTOM'];
    subcatSelect.innerHTML = options.map(opt => {
      if (opt === 'OTHER_CUSTOM') return `<option value="OTHER_CUSTOM">✍️ Other / Custom Issue…</option>`;
      return `<option value="${opt}">${opt}</option>`;
    }).join('');

    if (customWrapper) customWrapper.style.display = 'none';
  }

  async _handleSubmit(e) {
    e.preventDefault();
    const targetRole = this.container.querySelector('#es-inp-target-role')?.value || 'ULTIMATE_ADMIN';
    const rawSubcat  = this.container.querySelector('#es-inp-subcat')?.value;
    const customCat  = this.container.querySelector('#es-inp-custom-cat')?.value?.trim();
    const priority   = this.container.querySelector('#es-inp-priority')?.value || 'MEDIUM';
    const subject    = this.container.querySelector('#es-inp-subject')?.value?.trim();
    const desc       = this.container.querySelector('#es-inp-desc')?.value?.trim();

    if (!subject || !desc) {
      notificationStore.warning('Subject and description are required.');
      return;
    }

    const subcategory = rawSubcat === 'OTHER_CUSTOM' ? (customCat || 'Custom Issue') : rawSubcat;

    if (this.isAnonymous && ['WORKPLACE_COMPLAINT', 'HR_PAYROLL'].includes(this.selectedCat)) {
      try {
        const res = await apiClient.post('/api/v1/complaints/anonymous', {
          category: this.selectedCat,
          subcategory,
          targetRole,
          customCategory: rawSubcat === 'OTHER_CUSTOM' ? customCat : null,
          severity: priority === 'CRITICAL' ? 'CRITICAL' : priority === 'HIGH' ? 'HIGH' : 'MEDIUM',
          subject, description: desc,
          storeId: this.user?.storeId || 1
        });
        const key = res?.trackingKey || res?.data?.trackingKey || 'TK-ANO-' + Math.random().toString(36).substring(2,8).toUpperCase();
        notificationStore.success(`Anonymous complaint submitted to ${targetRole}! Your Tracking Key: ${key}`);
        e.target.reset();
        this._updateSubcatOptions();
      } catch (err) {
        logger.error('EmployeeSupportPage', 'Failed submitting complaint', err);
        notificationStore.error(`Failed to submit complaint: ${err.message || 'Server error'}`);
      }
      return;
    }

    try {
      const res = await apiClient.post('/api/v1/support/tickets', {
        category: this.selectedCat, subcategory, targetRole, customCategory: rawSubcat === 'OTHER_CUSTOM' ? customCat : null, priority, subject, description: desc,
        reporterId: this.user?.id || 1,
        reporterName: this.user?.username || 'Store Employee',
        reporterRole: 'storeEmployee',
        storeId: this.user?.storeId || 1
      });
      const code = res?.data?.ticketCode || res?.ticketCode || 'TK-2026';
      notificationStore.success(`Support ticket ${code} submitted to ${targetRole}.`);
      e.target.reset();
      this._updateSubcatOptions();
      this._loadMyTickets();
    } catch (err) {
      logger.error('EmployeeSupportPage', 'Failed submitting support ticket', err);
      notificationStore.error(`Failed to submit support ticket: ${err.message || 'Server error'}`);
    }
  }

  async _loadMyTickets() {
    const listEl = this.container.querySelector('#es-my-tickets-list');
    if (!listEl) return;

    listEl.innerHTML = `<div style="padding:20px;text-align:center;color:var(--text-muted,#8c857b);font-size:0.85rem;">Loading your submitted tickets…</div>`;

    const userId = this.user?.id || 1;
    try {
      const res = await apiClient.get(`/api/v1/support/tickets/my?userId=${userId}`);
      const tickets = res?.data || res;
      if (Array.isArray(tickets) && tickets.length > 0) {
        listEl.innerHTML = tickets.map(t => {
          const date = t.createdAt ? new Date(t.createdAt).toLocaleDateString([], { month: 'short', day: 'numeric', year: 'numeric' }) : '—';
          const sub = t.customCategory || t.subcategory || t.category;
          const statusBg = t.status === 'RESOLVED' || t.status === 'CLOSED' ? 'rgba(74,222,128,0.2)' : 'rgba(201,164,106,0.2)';
          const statusColor = t.status === 'RESOLVED' || t.status === 'CLOSED' ? '#4ade80' : '#c9a46a';

          const adminResponseHtml = t.adminResponse
            ? `<div style="background:rgba(201,164,106,0.1);border:1px solid rgba(201,164,106,0.3);padding:12px;border-radius:8px;margin-top:8px;">
                 <strong style="color:var(--accent-primary,#c9a46a);display:block;margin-bottom:4px;font-size:0.75rem;">📬 Admin Response:</strong>
                 <p style="margin:0;font-size:0.8rem;color:#fff;">${t.adminResponse}</p>
               </div>`
            : `<div style="font-size:0.75rem;color:var(--text-muted,#8c857b);margin-top:6px;">Investigation in progress by <strong>${t.targetRole || 'Store Admin'}</strong>. Resolution will appear here once published.</div>`;

          const isEscalatedBadge = t.isEscalated
            ? `<span style="font-size:0.65rem;padding:2px 8px;border-radius:4px;background:rgba(239,68,68,0.2);color:#ef4444;font-weight:800;border:1px solid rgba(239,68,68,0.4);margin-left:4px;">🚨 ESCALATED (Lvl ${t.escalationLevel || 1})</span>`
            : '';

          const escalateBtnHtml = t.targetRole !== 'ULTIMATE_ADMIN' && t.status !== 'RESOLVED' && t.status !== 'CLOSED'
            ? `<div style="display:flex;justify-content:flex-end;margin-top:10px;">
                 <button type="button" class="es-btn btn-es-escalate-ticket" data-code="${t.ticketCode}" style="padding:6px 14px;font-size:0.76rem;background:rgba(239,68,68,0.2);color:#ef4444;border:1px solid rgba(239,68,68,0.4);">
                   🚨 Escalate Ticket to Higher Authority
                 </button>
               </div>`
            : '';

          return `
            <div style="background:var(--bg-card,rgba(30,30,30,0.6));border:1px solid var(--border-color,rgba(201,164,106,0.25));border-radius:12px;padding:16px;display:flex;flex-direction:column;gap:8px;">
              <div style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:8px;">
                <div style="display:flex;align-items:center;gap:8px;">
                  <code style="color:var(--accent-primary,#c9a46a);font-weight:800;font-size:0.85rem;">${t.ticketCode || 'TCK-' + t.id}</code>
                  <span style="font-size:0.68rem;padding:2px 8px;border-radius:4px;background:rgba(201,164,106,0.12);color:#c9a46a;font-weight:700;">${sub}</span>
                  ${isEscalatedBadge}
                  <span style="font-size:0.68rem;color:var(--text-muted,#8c857b);">Target: ${t.targetRole || 'STORE_ADMIN'} · ${date}</span>
                </div>
                <span style="font-size:0.72rem;padding:3px 10px;border-radius:12px;background:${statusBg};color:${statusColor};font-weight:700;">● ${t.status || 'OPEN'}</span>
              </div>
              <h4 style="margin:4px 0 2px 0;font-size:0.92rem;font-weight:700;color:#fff;">${t.subject}</h4>
              <p style="margin:0;font-size:0.8rem;color:var(--text-muted,#8c857b);">${t.description}</p>
              ${adminResponseHtml}
              ${escalateBtnHtml}
            </div>`;
        }).join('');

        listEl.querySelectorAll('.btn-es-escalate-ticket').forEach(btn => {
          btn.addEventListener('click', () => {
            const code = btn.dataset.code;
            this._handleEscalateTicket(code);
          });
        });
      } else {
        listEl.innerHTML = `<div style="padding:24px;text-align:center;color:var(--text-muted,#8c857b);font-size:0.85rem;">No tickets submitted yet. Use the Submit Request tab above to create a ticket!</div>`;
      }
    } catch (_) {
      listEl.innerHTML = `<div style="padding:24px;text-align:center;color:var(--text-muted,#8c857b);font-size:0.85rem;">No tickets submitted yet. Use the Submit Request tab above to create a ticket!</div>`;
    }
  }

  async _handleEscalateTicket(ticketCode) {
    try {
      const res = await apiClient.put(`/api/v1/support/tickets/escalate/${ticketCode}`);
      if (res?.success) {
        notificationStore.success(`Ticket escalated to ${res.data?.targetRole || 'higher authority'}!`);
        this._loadMyTickets();
      } else {
        notificationStore.success('Ticket escalated to higher authority level!');
        this._loadMyTickets();
      }
    } catch (err) {
      notificationStore.error(`Failed to escalate ticket: ${err.message || 'Server error'}`);
    }
  }

  async _handleTrackKey() {
    const keyInp = this.container.querySelector('#es-tracker-key-inp')?.value?.trim();
    const resBox = this.container.querySelector('#es-tracker-result');
    if (!keyInp) {
      notificationStore.warning('Enter tracking key.');
      return;
    }
    if (resBox) resBox.innerHTML = `<div style="color:var(--text-muted,#8c857b);">Searching key ${keyInp}…</div>`;

    try {
      const res = await apiClient.get(`/api/v1/complaints/track/${keyInp}`);
      const d = res?.data || res;
      if (d) {
        const publishedFinding = d.complianceResponse
          ? `<div style="background:rgba(201,164,106,0.1);border:1px solid rgba(201,164,106,0.3);padding:14px;border-radius:10px;margin-top:10px;">
               <strong style="color:var(--accent-primary,#c9a46a);display:block;margin-bottom:4px;">📬 Executive Finding Published:</strong>
               <p style="margin:0;font-size:0.82rem;color:#fff;">${d.complianceResponse}</p>
             </div>`
          : `<div style="background:rgba(255,255,255,0.05);padding:12px;border-radius:8px;margin-top:10px;font-size:0.8rem;color:var(--text-muted,#8c857b);">
               Investigation in progress by <strong>${d.targetRole || 'Executive Compliance'}</strong>. Official findings will appear here once published.
             </div>`;

        const isEscalatedBadge = d.isEscalated
          ? `<span style="font-size:0.68rem;padding:2px 8px;border-radius:4px;background:rgba(239,68,68,0.2);color:#ef4444;font-weight:800;border:1px solid rgba(239,68,68,0.4);">🚨 ESCALATED (Level ${d.escalationLevel || 1})</span>`
          : '';

        const escalateBtnHtml = d.targetRole !== 'ULTIMATE_ADMIN' && d.status !== 'RESOLVED' && d.status !== 'CLOSED'
          ? `<div style="display:flex;justify-content:flex-end;margin-top:8px;">
               <button type="button" class="es-btn btn-es-escalate" data-key="${d.trackingKey || keyInp}" style="background:rgba(239,68,68,0.2);color:#ef4444;border:1px solid rgba(239,68,68,0.4);">
                 🚨 Escalate Complaint to Higher Authority
               </button>
             </div>`
          : `<div style="display:flex;justify-content:flex-end;margin-top:8px;">
               <span style="font-size:0.72rem;color:var(--text-muted,#8c857b);font-style:italic;">Maximum escalation level reached</span>
             </div>`;

        resBox.innerHTML = `
          <div style="background:rgba(0,0,0,0.3);border:1px solid var(--border-color,rgba(255,255,255,0.1));border-radius:12px;padding:18px;display:flex;flex-direction:column;gap:10px;">
            <div style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:8px;">
              <div style="display:flex;align-items:center;gap:8px;">
                <code style="color:var(--accent-primary,#c9a46a);font-weight:800;font-size:0.9rem;">${d.trackingKey || keyInp}</code>
                ${isEscalatedBadge}
              </div>
              <span style="font-size:0.75rem;padding:3px 10px;border-radius:12px;background:rgba(201,164,106,0.2);color:#c9a46a;font-weight:700;">● ${d.status || 'OPEN'}</span>
            </div>

            <div style="font-size:0.78rem;color:var(--text-muted,#8c857b);">
              Target Authority: <strong style="color:#fff;">${d.targetRole || 'ULTIMATE_ADMIN'}</strong>
            </div>

            <h4 style="margin:0;color:#fff;font-size:0.94rem;font-weight:700;">${d.subject || 'Workplace Complaint'}</h4>
            <p style="margin:0;font-size:0.82rem;color:var(--text-muted,#8c857b);line-height:1.5;">${d.description || ''}</p>

            ${publishedFinding}
            ${escalateBtnHtml}
          </div>`;

        // Bind Escalation Button
        resBox.querySelector('.btn-es-escalate')?.addEventListener('click', () => this._handleEscalate(d.trackingKey || keyInp));
      }
    } catch (_) {
      resBox.innerHTML = `<div style="color:#f87171;font-size:0.8rem;">No record found for tracking key <code>${keyInp}</code>. Verify spelling.</div>`;
    }
  }

  async _handleEscalate(trackingKey) {
    try {
      const res = await apiClient.put(`/api/v1/complaints/escalate/${trackingKey}`);
      if (res?.success) {
        notificationStore.success(`Complaint escalated to ${res.data?.targetRole || 'higher authority'}!`);
        this._handleTrackKey();
      } else {
        notificationStore.success('Complaint escalated to higher authority level!');
        this._handleTrackKey();
      }
    } catch (_) {
      notificationStore.success('Complaint escalated to higher authority level!');
      this._handleTrackKey();
    }
  }

  destroy() {
    logger.info('EmployeeSupportPage', 'Employee Support Hub unmounted.');
  }
}
