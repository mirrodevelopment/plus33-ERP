/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Shared — Leave Policy Booklet Page
 * File              : booklet.js
 * Path              : frontend/shared/leave-policy-booklet/booklet.js
 * Purpose           : Full reference page rendering read-only policy rules and legal compliance criteria.
 * Version           : 1.0.0
 ******************************************************************************/

import { apiClient } from '../../api/client.js';
import { authStore } from '../../store/authStore.js';
import { logger } from '../../core/logger.js';

export default class LeavePolicyBookletPage {
  constructor() {
    this.leaveTypes = [];
    this.profileCountry = 'France';
  }

  async mount(container, lifecycle) {
    logger.info('LeavePolicyBookletPage', 'Mounting Leave Policy Reference Booklet...');
    this._renderLoading(container);

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
      logger.error('LeavePolicyBookletPage', 'Failed to load policy data', err);
    }

    this.render(container);
  }

  _renderLoading(container) {
    container.innerHTML = `
      <div style="display:flex;align-items:center;justify-content:center;height:400px;flex-direction:column;gap:12px;">
        <i data-lucide="loader-2" class="animate-spin" style="width:32px;height:32px;color:var(--accent-primary);"></i>
        <span style="color:var(--text-muted);font-size:0.8rem;font-weight:600;">Loading policy booklet...</span>
      </div>`;
    if (window.lucide) window.lucide.createIcons();
  }

  render(container) {
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
        <li>Leave cannot be applied during active blackout periods.</li>
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
        <li>Leave cannot be applied during active blackout periods.</li>
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
        <li>Leave cannot be applied during active blackout periods.</li>
        <li>Approval workflow: Employee → Supervisor → Store Admin (for extended leaves).</li>
        <li>Unused annual leave is subject to carry-forward policy (max 5 days).</li>
      `;
    }

    const rowsHtml = this.leaveTypes.map(lt => `
      <tr>
        <td style="font-weight: 700; color: #f7e7d4; padding: 14px 16px; border-bottom: 1px solid rgba(255,255,255,0.03);">${lt.name}${lt.protected ? ' 🛡️' : ''}</td>
        <td style="text-align: center; padding: 14px 16px; border-bottom: 1px solid rgba(255,255,255,0.03);">${lt.annualLimit !== null && lt.annualLimit !== undefined ? `${lt.annualLimit} days` : 'Unlimited'}</td>
        <td style="text-align: center; padding: 14px 16px; border-bottom: 1px solid rgba(255,255,255,0.03);">${lt.monthlyAccrual !== null && lt.monthlyAccrual !== undefined ? `${lt.monthlyAccrual} days` : '0'}</td>
        <td style="text-align: center; padding: 14px 16px; border-bottom: 1px solid rgba(255,255,255,0.03);">${lt.maxConsecutiveDays || '—'}</td>
        <td style="text-align: center; padding: 14px 16px; border-bottom: 1px solid rgba(255,255,255,0.03);">${lt.minNoticeDays || '0'} days</td>
        <td style="text-align: center; padding: 14px 16px; border-bottom: 1px solid rgba(255,255,255,0.03);">
          <span style="display:inline-block; padding: 3px 8px; border-radius: 4px; font-size: 0.7rem; font-weight: 700; text-transform: uppercase; ${lt.paid ? 'background: rgba(34, 197, 94, 0.15); color: #4ade80; border: 1px solid rgba(34, 197, 94, 0.2);' : 'background: rgba(239, 68, 68, 0.15); color: #f87171; border: 1px solid rgba(239, 68, 68, 0.2);'}">${lt.paid ? 'Paid' : 'Unpaid'}</span>
        </td>
        <td style="text-align: center; padding: 14px 16px; border-bottom: 1px solid rgba(255,255,255,0.03);">
          <span style="display:inline-block; padding: 3px 8px; border-radius: 4px; font-size: 0.7rem; font-weight: 700; text-transform: uppercase; ${lt.protected ? 'background: rgba(59, 130, 246, 0.15); color: #60a5fa; border: 1px solid rgba(59, 130, 246, 0.2);' : 'background: rgba(255, 255, 255, 0.05); color: rgba(255, 255, 255, 0.6); border: 1px solid rgba(255, 255, 255, 0.08);'}">${lt.protected ? 'Protected' : 'Standard'}</span>
        </td>
        <td style="text-align: center; padding: 14px 16px; border-bottom: 1px solid rgba(255,255,255,0.03);">
          <span style="display:inline-block; padding: 3px 8px; border-radius: 4px; font-size: 0.7rem; font-weight: 700; text-transform: uppercase; ${lt.requiresDocument ? 'background: rgba(245, 158, 11, 0.15); color: #fbbf24; border: 1px solid rgba(245, 158, 11, 0.2);' : 'background: rgba(255, 255, 255, 0.05); color: rgba(255, 255, 255, 0.5);'}">${lt.requiresDocument ? 'Required' : 'No'}</span>
        </td>
      </tr>
    `).join('');

    container.innerHTML = `
      <div style="padding: 24px; display: flex; flex-direction: column; gap: 24px;" class="animate-slide-up">
        <div style="display: flex; flex-direction: column; gap: 4px;">
          <div style="display: flex; align-items: center; gap: 8px; font-size: 0.85rem; color: var(--text-muted);">
            <i data-lucide="book-open" style="width: 14px; height: 14px;"></i>
            <span>Policy Reference Booklet</span>
          </div>
          <h2 style="font-size: 1.8rem; font-weight: 800; color: var(--text-primary); margin: 0;">Company Leave Policy &amp; Rules</h2>
          <p style="color: var(--accent-primary); margin: 0; font-weight: 600; font-size: 0.95rem;">${flag} ${complianceText} (${this.profileCountry})</p>
        </div>

        <div class="card glass" style="overflow: hidden; border: 1px solid var(--border-color); background: rgba(30, 25, 20, 0.4);">
          <div style="overflow-x: auto; width: 100%;">
            <table style="width: 100%; border-collapse: collapse; text-align: left; font-size: 0.85rem;">
              <thead>
                <tr style="background: rgba(255, 255, 255, 0.02); border-bottom: 1px solid var(--border-color);">
                  <th style="padding: 14px 16px; font-weight: 700; color: var(--text-secondary); text-transform: uppercase; font-size: 0.75rem;">Leave Type</th>
                  <th style="padding: 14px 16px; font-weight: 700; color: var(--text-secondary); text-transform: uppercase; font-size: 0.75rem; text-align: center;">Annual Limit</th>
                  <th style="padding: 14px 16px; font-weight: 700; color: var(--text-secondary); text-transform: uppercase; font-size: 0.75rem; text-align: center;">Accrual/Month</th>
                  <th style="padding: 14px 16px; font-weight: 700; color: var(--text-secondary); text-transform: uppercase; font-size: 0.75rem; text-align: center;">Max Consecutive</th>
                  <th style="padding: 14px 16px; font-weight: 700; color: var(--text-secondary); text-transform: uppercase; font-size: 0.75rem; text-align: center;">Notice Needed</th>
                  <th style="padding: 14px 16px; font-weight: 700; color: var(--text-secondary); text-transform: uppercase; font-size: 0.75rem; text-align: center;">Paid Status</th>
                  <th style="padding: 14px 16px; font-weight: 700; color: var(--text-secondary); text-transform: uppercase; font-size: 0.75rem; text-align: center;">Protection</th>
                  <th style="padding: 14px 16px; font-weight: 700; color: var(--text-secondary); text-transform: uppercase; font-size: 0.75rem; text-align: center;">Document</th>
                </tr>
              </thead>
              <tbody>
                ${rowsHtml || '<tr><td colspan="8" style="text-align: center; padding: 24px; color: var(--text-muted);">No rules retrieved.</td></tr>'}
              </tbody>
            </table>
          </div>
        </div>

        <div class="card glass" style="padding: 20px 24px; border: 1px solid var(--border-color); display: flex; flex-direction: column; gap: 12px; background: rgba(30, 25, 20, 0.2);">
          <h4 style="margin: 0; font-size: 1rem; font-weight: 700; color: var(--accent-primary);">General Rules &amp; Compliance Instructions</h4>
          <ul style="margin: 0; padding-left: 20px; font-size: 0.85rem; color: var(--text-secondary); display: flex; flex-direction: column; gap: 8px; line-height: 1.5;">
            ${generalRulesHtml}
          </ul>
        </div>
      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  destroy() {}
}
