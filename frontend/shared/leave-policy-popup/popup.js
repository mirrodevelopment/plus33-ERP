/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Shared — Leave Policy Booklet Popup
 * File              : popup.js
 * Path              : frontend/shared/leave-policy-popup/popup.js
 * Purpose           : Reusable modal popup to display dynamic read-only policy rules for employee, store admin, and regional admin roles.
 * Version           : 1.0.0
 ******************************************************************************/

import { apiClient } from '../../api/client.js';
import { logger } from '../../core/logger.js';

export class LeavePolicyPopup {
  constructor() {
    this.modalEl = null;
    this.leaveTypes = [];
    this.profileCountry = 'France';
  }

  async init(triggerElement) {
    if (!triggerElement) return;
    triggerElement.addEventListener('click', () => this.show());
    
    // Pre-fetch data for responsiveness
    this.loadData();
  }

  async loadData() {
    try {
      const [profileRes, typesRes] = await Promise.all([
        apiClient.get('/api/v1/auth/me').catch(() => null),
        apiClient.get('/leaves/types').catch(() => null)
      ]);

      if (profileRes?.success && profileRes.data) {
        this.profileCountry = profileRes.data.country || 'France';
      }
      if (typesRes?.success && Array.isArray(typesRes.data)) {
        this.leaveTypes = typesRes.data;
      }
    } catch (err) {
      logger.error('LeavePolicyPopup', 'Failed to load policy data', err);
    }
  }

  async show() {
    logger.info('LeavePolicyPopup', 'Opening leave policy booklet popup...');
    
    if (this.leaveTypes.length === 0) {
      await this.loadData();
    }

    this._injectStyles();

    // Create modal element if not present
    if (!this.modalEl) {
      this.modalEl = document.createElement('div');
      this.modalEl.className = 'policy-popup-overlay';
      document.body.appendChild(this.modalEl);
    }

    const country = this.profileCountry.toLowerCase();
    let flag = '🇫🇷';
    let complianceText = 'French Labour Law Compliant';
    let generalRulesHtml = '';

    if (country.includes('india')) {
      flag = '🇮🇳';
      complianceText = 'Indian Labour Laws Compliant';
      generalRulesHtml = `
        <li>Leave requests must be submitted at least the required notice period in advance (e.g. 7 days for Annual Leave).</li>
        <li>Sick leave exceeding 2 consecutive days requires a medical certificate.</li>
        <li>Protected leave types (🛡️) like Maternity, Paternity, and Bereavement are auto-approved per Labour Code guidelines.</li>
        <li>Casual and Personal leaves allow short term emergency time-off.</li>
        <li>Leave cannot be applied during active <strong>blackout periods</strong>.</li>
        <li>Unused annual leave is subject to carry-forward policy (max 10 days) and encashment.</li>
      `;
    } else if (country.includes('emirates') || country.includes('uae')) {
      flag = '🇦🇪';
      complianceText = 'UAE Labour Law Compliant';
      generalRulesHtml = `
        <li>Annual leave requests require at least 30 days notice to ensure store coverage.</li>
        <li>Sick leave is capped at 90 calendar days per year as per UAE Labour Law (first 15 days full pay, next 30 days half pay, next 45 days unpaid).</li>
        <li>Medical certificate is required after 2 consecutive days of sick leave.</li>
        <li>Protected leave types (🛡️) like Maternity and Paternity are auto-approved.</li>
        <li>Leave cannot be applied during active <strong>blackout periods</strong>.</li>
        <li>Unused annual leave is subject to carry-forward policy (max 15 days) and encashment.</li>
      `;
    } else {
      flag = '🇫🇷';
      complianceText = 'French Labour Law Compliant';
      generalRulesHtml = `
        <li>Leave requests must be submitted at least the required notice period in advance (e.g. 14 days for Annual Leave).</li>
        <li>Sick leave exceeding 2 consecutive days requires a medical certificate.</li>
        <li>Protected leave types (🛡️) are auto-approved per Labour Code guidelines.</li>
        <li>Unpaid leave does not accrue and is deducted from salary.</li>
        <li>Leave cannot be applied during active <strong>blackout periods</strong>.</li>
        <li>Approval workflow: Employee → Supervisor → Store Admin (for extended leaves).</li>
        <li>Unused annual leave is subject to carry-forward policy (max 5 days).</li>
      `;
    }

    const rowsHtml = this.leaveTypes.map(lt => `
      <tr class="popup-table-row">
        <td class="type-cell">${lt.name}${lt.protected ? ' 🛡️' : ''}</td>
        <td class="center-cell">${lt.annualLimit !== null && lt.annualLimit !== undefined ? `${lt.annualLimit} days` : 'Unlimited'}</td>
        <td class="center-cell">${lt.monthlyAccrual !== null && lt.monthlyAccrual !== undefined ? `${lt.monthlyAccrual} days` : '0'}</td>
        <td class="center-cell">${lt.maxConsecutiveDays || '—'}</td>
        <td class="center-cell">${lt.minNoticeDays || '0'} days</td>
        <td class="center-cell">
          <span class="badge ${lt.paid ? 'paid-badge' : 'unpaid-badge'}">${lt.paid ? 'Paid' : 'Unpaid'}</span>
        </td>
        <td class="center-cell">
          <span class="badge ${lt.protected ? 'protected-badge' : 'standard-badge'}">${lt.protected ? 'Protected' : 'Standard'}</span>
        </td>
        <td class="center-cell">
          <span class="badge ${lt.requiresDocument ? 'doc-badge' : 'nodoc-badge'}">${lt.requiresDocument ? 'Required' : 'No'}</span>
        </td>
      </tr>
    `).join('');

    this.modalEl.innerHTML = `
      <div class="policy-popup-card animate-slide-up">
        <div class="policy-popup-header">
          <div class="policy-popup-title-group">
            <h3 class="policy-popup-title">📋 Leave Policy Booklet (${this.profileCountry})</h3>
            <p class="policy-popup-subtitle">${flag} ${complianceText}</p>
          </div>
          <button class="policy-popup-close-btn" aria-label="Close modal">&times;</button>
        </div>
        <div class="policy-popup-body">
          <div class="popup-table-wrapper">
            <table class="popup-policy-table">
              <thead>
                <tr>
                  <th>Leave Type</th>
                  <th class="center-cell">Annual Limit</th>
                  <th class="center-cell">Accrual/Month</th>
                  <th class="center-cell">Max Consecutive</th>
                  <th class="center-cell">Notice Needed</th>
                  <th class="center-cell">Paid Status</th>
                  <th class="center-cell">Protection</th>
                  <th class="center-cell">Document</th>
                </tr>
              </thead>
              <tbody>
                ${rowsHtml || '<tr><td colspan="8" class="center-cell text-muted">No rules retrieved.</td></tr>'}
              </tbody>
            </table>
          </div>
          <div class="popup-general-rules">
            <h4 class="popup-rules-heading">General Rules &amp; Compliance</h4>
            <ul class="popup-rules-list">
              ${generalRulesHtml}
            </ul>
          </div>
        </div>
      </div>
    `;

    this.modalEl.style.display = 'flex';
    
    // Bind close handlers
    const closeBtn = this.modalEl.querySelector('.policy-popup-close-btn');
    if (closeBtn) {
      closeBtn.onclick = () => this.hide();
    }
    this.modalEl.onclick = (e) => {
      if (e.target === this.modalEl) {
        this.hide();
      }
    };
  }

  hide() {
    if (this.modalEl) {
      this.modalEl.style.display = 'none';
    }
  }

  _injectStyles() {
    if (document.getElementById('leave-policy-popup-styles')) return;

    const style = document.createElement('style');
    style.id = 'leave-policy-popup-styles';
    style.textContent = `
      .policy-popup-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100vw;
        height: 100vh;
        background: rgba(15, 12, 8, 0.7);
        backdrop-filter: blur(10px);
        display: none;
        align-items: center;
        justify-content: center;
        z-index: 99999;
      }
      .policy-popup-card {
        background: rgba(30, 25, 20, 0.95);
        border: 1px solid rgba(255, 255, 255, 0.1);
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.5);
        border-radius: 16px;
        width: 90%;
        max-width: 900px;
        max-height: 85vh;
        display: flex;
        flex-direction: column;
        overflow: hidden;
      }
      .policy-popup-header {
        padding: 20px 24px;
        border-bottom: 1px solid rgba(255, 255, 255, 0.08);
        display: flex;
        justify-content: space-between;
        align-items: center;
        background: linear-gradient(135deg, rgba(201, 164, 106, 0.15) 0%, transparent 100%);
      }
      .policy-popup-title {
        font-size: 1.25rem;
        font-weight: 700;
        color: #f7e7d4;
        margin: 0;
      }
      .policy-popup-subtitle {
        font-size: 0.8rem;
        color: var(--accent-primary, #c9a46a);
        margin: 4px 0 0 0;
        font-weight: 600;
      }
      .policy-popup-close-btn {
        background: none;
        border: none;
        color: rgba(255, 255, 255, 0.6);
        font-size: 2rem;
        cursor: pointer;
        padding: 0;
        line-height: 1;
        transition: color 0.2s;
      }
      .policy-popup-close-btn:hover {
        color: #fff;
      }
      .policy-popup-body {
        padding: 24px;
        overflow-y: auto;
        display: flex;
        flex-direction: column;
        gap: 20px;
      }
      .popup-table-wrapper {
        width: 100%;
        overflow-x: auto;
        border-radius: 8px;
        border: 1px solid rgba(255, 255, 255, 0.05);
      }
      .popup-policy-table {
        width: 100%;
        border-collapse: collapse;
        text-align: left;
        font-size: 0.85rem;
      }
      .popup-policy-table th {
        background: rgba(255, 255, 255, 0.03);
        color: rgba(255, 255, 255, 0.7);
        padding: 12px 16px;
        font-weight: 600;
        font-size: 0.75rem;
        text-transform: uppercase;
        letter-spacing: 0.5px;
      }
      .popup-policy-table td {
        padding: 12px 16px;
        border-bottom: 1px solid rgba(255, 255, 255, 0.03);
        color: rgba(255, 255, 255, 0.85);
      }
      .popup-table-row:hover {
        background: rgba(255, 255, 255, 0.01);
      }
      .type-cell {
        font-weight: 700;
        color: #f7e7d4 !important;
      }
      .center-cell {
        text-align: center !important;
      }
      .badge {
        display: inline-block;
        padding: 3px 8px;
        border-radius: 4px;
        font-size: 0.7rem;
        font-weight: 700;
        text-transform: uppercase;
      }
      .paid-badge {
        background: rgba(34, 197, 94, 0.15);
        color: #4ade80;
        border: 1px solid rgba(34, 197, 94, 0.2);
      }
      .unpaid-badge {
        background: rgba(239, 68, 68, 0.15);
        color: #f87171;
        border: 1px solid rgba(239, 68, 68, 0.2);
      }
      .protected-badge {
        background: rgba(59, 130, 246, 0.15);
        color: #60a5fa;
        border: 1px solid rgba(59, 130, 246, 0.2);
      }
      .standard-badge {
        background: rgba(255, 255, 255, 0.05);
        color: rgba(255, 255, 255, 0.6);
        border: 1px solid rgba(255, 255, 255, 0.08);
      }
      .doc-badge {
        background: rgba(245, 158, 11, 0.15);
        color: #fbbf24;
        border: 1px solid rgba(245, 158, 11, 0.2);
      }
      .nodoc-badge {
        background: rgba(255, 255, 255, 0.05);
        color: rgba(255, 255, 255, 0.5);
      }
      .popup-general-rules {
        background: rgba(255, 255, 255, 0.02);
        border: 1px solid rgba(255, 255, 255, 0.05);
        border-radius: 8px;
        padding: 16px 20px;
      }
      .popup-rules-heading {
        margin: 0 0 10px 0;
        font-size: 0.9rem;
        font-weight: 700;
        color: var(--accent-primary, #c9a46a);
      }
      .popup-rules-list {
        margin: 0;
        padding-left: 20px;
        font-size: 0.8rem;
        color: rgba(255, 255, 255, 0.7);
        display: flex;
        flex-direction: column;
        gap: 6px;
      }
      .popup-rules-list li {
        line-height: 1.4;
      }
    `;
    document.head.appendChild(style);
  }
}
