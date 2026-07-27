/******************************************************************************
 * Project    : PLUS33 Coffee ERP
 * Component  : Ultimate System Admin Executive Complaints — Separate Controller
 * File       : ultimate-complaints.js
 ******************************************************************************/

import { authStore }         from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger }            from '../../../../core/logger.js';
import { htmlLoader }        from '../../../../core/htmlLoader.js';
import { apiClient }         from '../../../../api/client.js';

const TEMPLATE_URL = 'modules/ultimate-admin/pages/complaints/ultimate-complaints.html';
const CSS_ID       = 'ultimate-complaints-sep-css';
const CSS_URL      = 'modules/ultimate-admin/pages/complaints/ultimate-complaints.css';

const STATUS_MAP = {
  OPEN:               { color: '#c9a46a', bg: 'rgba(201,164,106,0.15)', border: 'rgba(201,164,106,0.32)', label: 'Open' },
  UNDER_INVESTIGATION:{ color: '#60a5fa', bg: 'rgba(59,130,246,0.15)',  border: 'rgba(59,130,246,0.35)',  label: 'Investigating' },
  ACTION_TAKEN:       { color: '#4ade80', bg: 'rgba(74,222,128,0.15)',  border: 'rgba(74,222,128,0.3)',   label: 'Action Taken' },
  RESOLVED:           { color: '#4ade80', bg: 'rgba(74,222,128,0.15)',  border: 'rgba(74,222,128,0.3)',   label: 'Resolved' },
  CLOSED:             { color: '#9ca3af', bg: 'rgba(255,255,255,0.05)', border: 'rgba(255,255,255,0.12)', label: 'Closed' },
};

function animateCount(el, target, duration = 600) {
  if (!el) return;
  const start = parseInt(el.textContent, 10) || 0;
  const range = target - start;
  const startTime = performance.now();
  const step = (now) => {
    const elapsed = now - startTime;
    const progress = Math.min(elapsed / duration, 1);
    const ease = 1 - Math.pow(1 - progress, 3);
    el.textContent = Math.round(start + range * ease);
    if (progress < 1) requestAnimationFrame(step);
  };
  requestAnimationFrame(step);
}

export default class UltimateAdminComplaints {

  constructor() {
    this.user = authStore.getUser();
    this.container = null;
    this.complaints = [];
    this.activeFilter = '';
  }

  async mount(container) {
    logger.info('UltimateAdminComplaints', 'Mounting Ultimate Admin Executive Registry…');
    this.container = container;
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);
    this._bindEvents();
    await this._loadData();
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

  _bindEvents() {
    this.container.querySelector('#btn-uc-refresh')?.addEventListener('click', () => this._loadData());
    this.container.querySelector('#uc-filter-store')?.addEventListener('change', () => this._render());

    const tabs = this.container.querySelectorAll('.uc-filter-tab');
    tabs.forEach(t => {
      t.addEventListener('click', () => {
        tabs.forEach(b => b.classList.remove('active'));
        t.classList.add('active');
        this.activeFilter = t.dataset.status || '';
        this._render();
      });
    });

    const modal = this.container.querySelector('#uc-action-modal');
    const closeModal = () => { if (modal) modal.style.display = 'none'; };
    this.container.querySelector('#btn-uc-close-modal')?.addEventListener('click', closeModal);
    this.container.querySelector('#btn-uc-cancel-modal')?.addEventListener('click', closeModal);
    modal?.addEventListener('click', e => { if (e.target === modal) closeModal(); });

    this.container.querySelector('#uc-action-form')?.addEventListener('submit', e => this._handleSubmitAction(e));

    // Process Guide Modal Events
    const processBtn = this.container.querySelector('#uc-btn-process-guide');
    const processModal = this.container.querySelector('#uc-process-modal');
    const closeProcessBtn = this.container.querySelector('#btn-uc-close-process-modal');

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

  async _loadData() {
    const listEl = this.container.querySelector('#uc-complaints-list');
    if (listEl) listEl.innerHTML = `<div style="padding:20px;text-align:center;color:rgba(255,255,255,0.4);">Loading complaints registry table…</div>`;

    try {
      const res = await apiClient.get('/api/v1/complaints/overview?targetRole=ULTIMATE_ADMIN');
      if (res?.success && Array.isArray(res.data)) {
        this.complaints = res.data;
      } else {
        this.complaints = [];
      }
    } catch (err) {
      logger.error('UltimateAdminComplaints', 'Failed loading live complaints', err);
      this.complaints = [];
    }
    this._updateKpis();
    this._render();
    if (window.lucide) window.lucide.createIcons();
  }

  _updateKpis() {
    const open = this.complaints.filter(c => c.status === 'OPEN').length;
    const inv  = this.complaints.filter(c => c.status === 'UNDER_INVESTIGATION').length;
    const act  = this.complaints.filter(c => ['ACTION_TAKEN','RESOLVED','CLOSED'].includes(c.status)).length;
    const crit = this.complaints.filter(c => ['CRITICAL','HIGH'].includes(c.severity)).length;

    animateCount(this.container.querySelector('#uc-kpi-open'), open);
    animateCount(this.container.querySelector('#uc-kpi-inv'),  inv);
    animateCount(this.container.querySelector('#uc-kpi-act'),  act);
    animateCount(this.container.querySelector('#uc-kpi-crit'), crit);
  }

  _render() {
    const listEl = this.container.querySelector('#uc-complaints-list');
    if (!listEl) return;

    const storeVal = this.container.querySelector('#uc-filter-store')?.value;
    let filtered = this.complaints;
    if (storeVal) filtered = filtered.filter(c => String(c.storeId) === String(storeVal));
    if (this.activeFilter) filtered = filtered.filter(c => c.status === this.activeFilter);

    if (!filtered.length) {
      listEl.innerHTML = `<div style="padding:24px;text-align:center;color:rgba(255,255,255,0.4);font-size:0.83rem;">No complaints recorded in database yet.</div>`;
      return;
    }

    const rows = filtered.map(c => {
      const s        = STATUS_MAP[c.status] || STATUS_MAP.OPEN;
      const sub      = c.customCategory || c.subcategory || c.category;
      const date     = c.createdAt ? new Date(c.createdAt).toLocaleDateString([], { month:'short', day:'numeric', year:'numeric' }) : '—';
      const escCount = c.escalationLevel || (c.isEscalated ? 1 : 0);

      const escBadge = escCount > 0
        ? `<span style="font-size:0.68rem;padding:2px 8px;border-radius:4px;background:rgba(239,68,68,0.25);border:1px solid rgba(239,68,68,0.5);color:#ef4444;font-weight:800;">🚨 Level ${escCount}</span>`
        : `<span style="font-size:0.68rem;color:rgba(255,255,255,0.3);">Level 0</span>`;

      const responseSnippet = c.complianceResponse
        ? `<div style="margin-top:4px;font-size:0.75rem;color:#4ade80;">📬 ${c.complianceResponse.substring(0, 60)}…</div>`
        : '';

      const isAnonymous = c.trackingKey.startsWith('TK-ANO-');
      const reporterHtml = isAnonymous
        ? `<span style="padding:3px 8px;border-radius:4px;background:rgba(244,63,94,0.15);color:#f43f5e;border:1px solid rgba(244,63,94,0.3);font-size:0.68rem;font-weight:700;letter-spacing:0.03em;">Anonymous</span>`
        : `<div style="font-weight:700;color:#fff;font-size:0.8rem;">${c.reporterName || 'Unknown'}</div>
           <div style="font-size:0.68rem;color:var(--text-muted,#8c857b);">ID: ${c.reporterId || '—'}</div>`;

      return `
        <tr style="border-bottom:1px solid rgba(255,255,255,0.06);">
          <td style="padding:12px;font-family:monospace;font-weight:800;color:var(--accent-primary,#c9a46a);">${c.trackingKey}</td>
          <td style="padding:12px;font-size:0.78rem;font-weight:700;color:#fff;">${c.category}</td>
          <td style="padding:12px;font-size:0.78rem;color:var(--text-muted,#8c857b);">${sub}</td>
          <td style="padding:12px;">${reporterHtml}</td>
          <td style="padding:12px;">
            <div style="font-size:0.83rem;font-weight:700;color:#fff;">${c.subject}</div>
            <div style="font-size:0.76rem;color:var(--text-muted,#8c857b);max-width:280px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">${c.description}</div>
            ${responseSnippet}
          </td>
          <td style="padding:12px;font-size:0.75rem;color:rgba(255,255,255,0.5);">Store #${c.storeId || 1}<br>${date}</td>
          <td style="padding:12px;font-size:0.75rem;font-weight:700;color:#c9a46a;">${c.targetRole || 'ULTIMATE_ADMIN'}</td>
          <td style="padding:12px;">${escBadge}</td>
          <td style="padding:12px;">
            <span style="padding:3px 10px;border-radius:12px;font-size:0.7rem;font-weight:700;background:${s.bg};border:1px solid ${s.border};color:${s.color};">● ${s.label}</span>
          </td>
          <td style="padding:12px;text-align:right;">
            <button type="button" class="uc-btn uc-btn-secondary btn-uc-respond" data-id="${c.id}" style="padding:5px 10px;font-size:0.75rem;">
              ✏️ Take Action
            </button>
          </td>
        </tr>`;
    }).join('');

    listEl.innerHTML = `
      <table style="width:100%;border-collapse:collapse;text-align:left;">
        <thead>
          <tr style="border-bottom:1px solid rgba(201,164,106,0.3);background:rgba(201,164,106,0.06);font-size:0.72rem;font-weight:800;color:var(--accent-primary,#c9a46a);text-transform:uppercase;">
            <th style="padding:12px;">ID / Code</th>
            <th style="padding:12px;">Complaint Type</th>
            <th style="padding:12px;">Subcategory</th>
            <th style="padding:12px;">Reporter</th>
            <th style="padding:12px;">Complaint Details</th>
            <th style="padding:12px;">Created Info</th>
            <th style="padding:12px;">To Who</th>
            <th style="padding:12px;">Escalate Count</th>
            <th style="padding:12px;">Status (Now Under)</th>
            <th style="padding:12px;text-align:right;">Actions</th>
          </tr>
        </thead>
        <tbody>
          ${rows}
        </tbody>
      </table>`;

    listEl.querySelectorAll('.btn-uc-respond').forEach(btn => {
      btn.addEventListener('click', () => {
        const item = this.complaints.find(c => c.id === parseInt(btn.dataset.id, 10));
        if (item) this._openModal(item);
      });
    });

    if (window.lucide) window.lucide.createIcons();
  }

  _openModal(item) {
    const modal = this.container.querySelector('#uc-action-modal');
    if (!modal) return;
    this.container.querySelector('#uc-modal-id').value       = item.id;
    this.container.querySelector('#uc-modal-key').value      = item.trackingKey;
    this.container.querySelector('#uc-modal-subject').value  = item.subject;
    this.container.querySelector('#uc-modal-status').value   = item.status || 'UNDER_INVESTIGATION';
    this.container.querySelector('#uc-modal-report').value   = item.complianceResponse || '';
    modal.style.display = 'flex';
  }

  async _handleSubmitAction(e) {
    e.preventDefault();
    const id     = parseInt(this.container.querySelector('#uc-modal-id').value, 10);
    const status = this.container.querySelector('#uc-modal-status').value;
    const report = this.container.querySelector('#uc-modal-report').value?.trim();

    const isClosing = ['RESOLVED', 'CLOSED'].includes(status);
    if (isClosing && !report) {
      this.container.querySelector('#uc-modal-report')?.focus();
      notificationStore.warning('Official investigation closing report is mandatory to mark as Resolved or Closed.');
      return;
    }

    try {
      await apiClient.put(`/api/v1/complaints/${id}/response`, { status, complianceResponse: report });
    } catch (_) {}

    const item = this.complaints.find(c => c.id === id);
    if (item) {
      item.status = status;
      if (report) item.complianceResponse = report;
    }

    notificationStore.success(`Action saved! Status updated to ${STATUS_MAP[status]?.label || status}.`);
    const modal = this.container.querySelector('#uc-action-modal');
    if (modal) modal.style.display = 'none';

    this._updateKpis();
    this._render();
  }

  destroy() {
    logger.info('UltimateAdminComplaints', 'Ultimate Admin Registry unmounted.');
  }
}
