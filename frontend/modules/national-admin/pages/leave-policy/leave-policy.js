/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : National Admin — Leave Policy Management
 * File              : leave-policy.js
 * Path              : frontend/modules/national-admin/pages/leave-policy/leave-policy.js
 * Purpose           : Controller logic for configuring leave groups, versioned rules, pay tiers, holidays, weekly off, and audits
 * Version           : 1.0.0
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

const TEMPLATE_URL = 'modules/national-admin/pages/leave-policy/leave-policy.html';

export default class LeavePolicyPage {

  constructor() {
    this.user = authStore.getUser();
    this.policyGroups = [];
    this.selectedGroupId = null;
    this.activeTab = 'general';
    
    // Cache for loaded rule parameters
    this.currentRules = [];
    this.selectedRuleId = null;
    
    // Holiday Calendars state
    this.holidayCalendars = [];
    this.selectedCalendar = null;
  }

  async mount(container, lifecycle) {
    logger.info('LeavePolicyPage', 'Mounting Leave Policy Management dashboard...');
    
    // Role-based access control check (only ultimateAdmin and nationalAdmin allowed)
    const role = authStore.getRole();
    if (role !== 'nationalAdmin' && role !== 'ultimateAdmin') {
      this._loadCss();
      container.innerHTML = `
        <div class="access-denied-container animate-slide-up" style="display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 400px; text-align: center; gap: 16px; padding: 40px;">
          <div style="background: rgba(239, 68, 68, 0.1); color: #ef4444; width: 64px; height: 64px; border-radius: 50%; display: flex; align-items: center; justify-content: center;">
            <i data-lucide="shield-alert" style="width: 32px; height: 32px;"></i>
          </div>
          <h2 style="font-size: 1.8rem; font-weight: 700; color: #ef4444; margin: 0;">Access Denied</h2>
          <p style="color: var(--text-muted); max-w: 450px; margin: 0; font-size: 0.95rem; line-height: 1.5;">
            Only National Admin and Ultimate Admin accounts have authorization to access the Leave Policy Management module.
          </p>
        </div>
      `;
      if (window.lucide) {
        window.lucide.createIcons({ attrs: { class: 'lucide' } });
      }
      return;
    }

    this._loadCss();

    // 1. Injects HTML skeleton
    await htmlLoader.inject(TEMPLATE_URL, container);

    // 2. Fetch master lists (policy groups & types)
    await this.loadMasterData();

    // 3. Initial rendering of framework structure
    this.renderSelector(container);
    this.renderTabContent(container);

    // 4. Bind interactive triggers
    this.bindEvents(container, lifecycle);
  }

  _loadCss() {
    if (!document.querySelector('link[href*="leave-policy.css"]')) {
      const link = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = 'modules/national-admin/pages/leave-policy/leave-policy.css';
      document.head.appendChild(link);
    }
  }

  async loadMasterData() {
    try {
      const groupsRes = await apiClient.get('/leaves/admin/policy-groups');
      if (groupsRes?.success && Array.isArray(groupsRes.data)) {
        this.policyGroups = groupsRes.data;
        
        let targetGroup = null;
        const role = authStore.getRole();
        if (role === 'nationalAdmin') {
          let matchedCode = 'INDIA'; // fallback
          try {
            const profileRes = await apiClient.get('/api/v1/auth/me');
            if (profileRes?.success && profileRes.data) {
              const profileCountry = String(profileRes.data.country || '').toLowerCase();
              if (profileCountry.includes('india')) {
                matchedCode = 'INDIA';
              } else if (profileCountry.includes('emirates') || profileCountry.includes('uae') || profileCountry.includes('united arab')) {
                matchedCode = 'UAE';
              } else if (profileCountry.includes('france') || profileCountry.includes('europe') || profileCountry.includes('eu')) {
                matchedCode = 'EU';
              }
            }
          } catch (e) {
            logger.error('LeavePolicyPage', 'Error loading profile for country matching', e);
          }
          targetGroup = this.policyGroups.find(g => String(g.code || '').toUpperCase() === matchedCode);
        }

        if (targetGroup) {
          this.selectedGroupId = targetGroup.id;
        } else if (this.policyGroups.length > 0) {
          this.selectedGroupId = this.policyGroups[0].id;
        }
      }
    } catch (err) {
      logger.error('LeavePolicyPage', 'Failed to fetch policy groups: ' + err.message);
      notificationStore.danger('Unable to load leave policy groups.');
    }
  }

  renderSelector(container) {
    const select = container.querySelector('#select-policy-group');
    if (!select) return;

    select.innerHTML = this.policyGroups.map(g => 
      `<option value="${g.id}" ${g.id === this.selectedGroupId ? 'selected' : ''}>${g.name} (${g.code})</option>`
    ).join('');

    const role = authStore.getRole();
    if (role === 'nationalAdmin') {
      select.disabled = true;
    }
  }

  renderTabContent(container) {
    const mountPoint = container.querySelector('#policy-panel-mount');
    if (!mountPoint) return;

    // Highlight active navigation tab button
    container.querySelectorAll('.policy-tab-btn').forEach(btn => {
      if (btn.getAttribute('data-tab') === this.activeTab) {
        btn.classList.add('active');
        btn.setAttribute('aria-selected', 'true');
      } else {
        btn.classList.remove('active');
        btn.setAttribute('aria-selected', 'false');
      }
    });

    // Extract template and inject
    let templateId = '';
    switch (this.activeTab) {
      case 'general':   templateId = 'tpl-policy-general'; break;
      case 'rules':     templateId = 'tpl-policy-rules'; break;
      case 'pay':       templateId = 'tpl-policy-pay'; break;
      case 'calendar':  templateId = 'tpl-policy-calendar'; break;
      case 'weekly-off':templateId = 'tpl-policy-weekly-off'; break;
    }

    const tpl = container.querySelector(`#${templateId}`);
    if (tpl) {
      mountPoint.innerHTML = '';
      mountPoint.appendChild(tpl.content.cloneNode(true));
      this.initPanel(container);
    }

    if (window.lucide) window.lucide.createIcons();
    this.applyAccessControl(container);
  }

  async initPanel(container) {
    const group = this.policyGroups.find(g => g.id === this.selectedGroupId);
    if (!group) return;

    if (this.activeTab === 'general') {
      container.querySelector('#group-code').value = group.code || '';
      container.querySelector('#group-name').value = group.name || '';
      container.querySelector('#group-desc').value = group.description || '';
      container.querySelector('#group-hours-day').value = group.hoursPerDay || 8.00;
      container.querySelector('#group-hours-week').value = group.hoursPerWeek || 48.00;
      this.applyAccessControl(container);
    } 
    else if (this.activeTab === 'rules') {
      await this.loadRulesTab(container);
    } 
    else if (this.activeTab === 'pay') {
      await this.loadPayTab(container);
    } 
    else if (this.activeTab === 'calendar') {
      await this.loadCalendarTab(container);
    } 
    else if (this.activeTab === 'weekly-off') {
      await this.loadWeeklyOffTab(container);
    }
  }

  applyAccessControl(container) {
    const role = authStore.getRole();
    if (role === 'ultimateAdmin') {
      logger.info('LeavePolicyPage', 'Applying read-only view for Ultimate Admin');
      
      // 1. Disable all form inputs, selects, textareas, checkboxes inside forms
      container.querySelectorAll('form input, form select, form textarea, form button').forEach(el => {
        el.disabled = true;
        if (el.tagName === 'INPUT' || el.tagName === 'TEXTAREA') {
          el.readOnly = true;
        }
      });

      // 2. Hide save/submit actions and modification panels
      container.querySelectorAll('.form-actions, .btn-submit, button[type="submit"]').forEach(el => {
        el.style.display = 'none';
      });

      // 3. Hide audit reason fields entirely
      const auditReasonGroup = container.querySelector('#rule-audit-reason')?.closest('.form-group');
      if (auditReasonGroup) {
        auditReasonGroup.style.display = 'none';
      }

      // 4. Hide "Add" boxes and lists actions
      const addPaytierBox = container.querySelector('#form-add-paytier')?.closest('.form-card-box');
      if (addPaytierBox) {
        addPaytierBox.style.display = 'none';
      }
      
      const addHolidayBox = container.querySelector('#form-add-holiday')?.closest('.form-card-box');
      if (addHolidayBox) {
        addHolidayBox.style.display = 'none';
      }

      const createCalendarBtn = container.querySelector('#btn-create-calendar');
      if (createCalendarBtn) {
        createCalendarBtn.style.display = 'none';
      }

      // 5. Hide inline action buttons like trash/delete icons in tables
      container.querySelectorAll('.btn-delete-holiday, .btn-delete-paytier').forEach(el => {
        el.style.display = 'none';
      });
    }
  }

  // =========================================================================
  // TABS LOADING LOGIC
  // =========================================================================

  async loadRulesTab(container) {
    try {
      const res = await apiClient.get(`/leaves/admin/policies?policyGroupId=${this.selectedGroupId}`);
      if (res?.success) {
        this.currentRules = res.data;
        const select = container.querySelector('#select-rule-leavetype');
        select.innerHTML = '<option value="">-- Choose Leave Type --</option>' + 
          this.currentRules.map(r => `<option value="${r.id}">${r.leaveTypeName}</option>`).join('');
      }
      this.applyAccessControl(container);
    } catch (err) {
      notificationStore.danger('Failed to fetch entitlement rules.');
    }
  }

  async loadPayTab(container) {
    try {
      const res = await apiClient.get(`/leaves/admin/policies?policyGroupId=${this.selectedGroupId}`);
      if (res?.success) {
        this.currentRules = res.data;
        const select = container.querySelector('#select-pay-rule');
        select.innerHTML = '<option value="">-- Choose Active Policy Rule --</option>' +
          this.currentRules.map(r => `<option value="${r.id}">${r.leaveTypeName} (${r.version})</option>`).join('');
      }
      this.applyAccessControl(container);
    } catch (err) {
      notificationStore.danger('Failed to load rules for pay configuration.');
    }
  }

  async loadCalendarTab(container) {
    const yearSelect = container.querySelector('#select-calendar-year');
    const selectedYear = parseInt(yearSelect.value);

    try {
      const res = await apiClient.get(`/leaves/admin/holiday-calendars?policyGroupId=${this.selectedGroupId}`);
      if (res?.success) {
        this.holidayCalendars = res.data;
        this.selectedCalendar = this.holidayCalendars.find(c => c.year === selectedYear);

        const workspace = container.querySelector('#holidays-workspace');
        const createBtn = container.querySelector('#btn-create-calendar');

        if (this.selectedCalendar) {
          workspace.classList.remove('hide');
          createBtn.classList.add('hide');
          this.renderHolidaysList(container);
        } else {
          workspace.classList.add('hide');
          createBtn.classList.remove('hide');
        }
      }
      this.applyAccessControl(container);
    } catch (err) {
      notificationStore.danger('Failed to fetch holiday calendars.');
    }
  }

  renderHolidaysList(container) {
    const tbody = container.querySelector('#holidays-tbody');
    if (!tbody || !this.selectedCalendar) return;

    if (!Array.isArray(this.selectedCalendar.holidays) || this.selectedCalendar.holidays.length === 0) {
      tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted">No public holidays defined in this calendar year.</td></tr>';
      return;
    }

    tbody.innerHTML = this.selectedCalendar.holidays.map(h => `
      <tr>
        <td class="font-semibold">${h.holidayName}</td>
        <td>${h.holidayDate}</td>
        <td>
          <span class="badge ${h.isWorkingDay ? 'bg-amber-100 text-amber-800' : 'bg-emerald-100 text-emerald-800'}">
            ${h.isWorkingDay ? 'Working Day' : 'Public Rest'}
          </span>
        </td>
        <td>
          <button type="button" class="btn btn-sm btn-outline text-red-500 btn-delete-holiday" data-id="${h.id}">
            <i data-lucide="trash-2" aria-hidden="true" class="w-4 h-4"></i>
          </button>
        </td>
      </tr>
    `).join('');
    
    if (window.lucide) window.lucide.createIcons();
    this.applyAccessControl(container);
  }

  async loadWeeklyOffTab(container) {
    try {
      const res = await apiClient.get(`/leaves/admin/weekly-offs?policyGroupId=${this.selectedGroupId}`);
      if (res?.success && Array.isArray(res.data)) {
        // Clear all checks
        container.querySelectorAll('#form-weekly-off input[type="checkbox"]').forEach(cb => cb.checked = false);
        
        // Ticks retrieved days
        res.data.forEach(r => {
          const cb = container.querySelector(`#weeklyoff-${r.dayOfWeek.toLowerCase()}`);
          if (cb) cb.checked = true;
        });
      }
      this.applyAccessControl(container);
    } catch (err) {
      notificationStore.danger('Failed to load weekly off rules.');
    }
  }

  // =========================================================================
  // ACTIONS AND BINDINGS
  // =========================================================================

  bindEvents(container, lifecycle) {
    // 1. Policy Group Selector Changes
    const groupSelect = container.querySelector('#select-policy-group');
    const handleGroupChange = () => {
      this.selectedGroupId = parseInt(groupSelect.value, 10);
      this.initPanel(container);
    };
    groupSelect.addEventListener('change', handleGroupChange);
    lifecycle.onCleanup(() => groupSelect.removeEventListener('change', handleGroupChange));

    // 2. Nav Tab switching
    container.querySelectorAll('.policy-tab-btn').forEach(btn => {
      const handleTabSwitch = () => {
        this.activeTab = btn.getAttribute('data-tab');
        this.renderTabContent(container);
        this.bindEvents(container, lifecycle);
      };
      btn.addEventListener('click', handleTabSwitch);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleTabSwitch));
    });

    // 3. Tab Specific Event Bindings
    this.bindTabEvents(container, lifecycle);
  }

  bindTabEvents(container, lifecycle) {
    if (this.activeTab === 'rules') {
      const ruleSelect = container.querySelector('#select-rule-leavetype');
      const form = container.querySelector('#form-policy-rule');
      
      const handleRuleChange = () => {
        const id = ruleSelect.value;
        if (!id) {
          form.classList.add('hide');
          return;
        }
        
        const rule = this.currentRules.find(r => r.id.toString() === id);
        if (rule) {
          form.classList.remove('hide');
          container.querySelector('#rule-id').value = rule.id;
          container.querySelector('#rule-default-entitlement').value = rule.defaultEntitlement;
          container.querySelector('#rule-monthly-accrual').value = rule.monthlyAccrual || '';
          container.querySelector('#rule-min-leave-unit').value = rule.minimumLeaveUnit;
          container.querySelector('#rule-max-consecutive').value = rule.maxConsecutiveDays || '';
          container.querySelector('#rule-max-year').value = rule.maxPerYear || '';
          container.querySelector('#rule-min-notice').value = rule.minNoticeDays;
          container.querySelector('#rule-doc-required-days').value = rule.documentRequiredAfterDays;
          container.querySelector('#rule-lifetime-limit').value = rule.lifetimeLimit || '';
          container.querySelector('#rule-carry-forward-allowed').checked = rule.carryForwardAllowed;
          container.querySelector('#rule-carry-forward-limit').value = rule.carryForwardLimit || '';
          container.querySelector('#rule-carry-forward-expiry').value = rule.carryForwardExpiryMonths || '';
          container.querySelector('#rule-encashment-allowed').checked = rule.encashmentAllowed;
          container.querySelector('#rule-encashment-limit').value = rule.maximumEncashmentDays || '';
          container.querySelector('#rule-encashment-min-bal').value = rule.minimumBalanceForEncashment || '';
          container.querySelector('#rule-approval-level').value = rule.approvalLevel;
          container.querySelector('#rule-effective-from').value = rule.effectiveFrom;
          container.querySelector('#rule-effective-to').value = rule.effectiveTo || '';
          container.querySelector('#rule-allow-half-day').checked = rule.allowHalfDay;
          container.querySelector('#rule-is-paid').checked = rule.isPaid;
          container.querySelector('#rule-audit-reason').value = '';
          this.applyAccessControl(container);
        }
      };
      
      ruleSelect.addEventListener('change', handleRuleChange);
      lifecycle.onCleanup(() => ruleSelect.removeEventListener('change', handleRuleChange));

      // Rule Form Submission
      const handleRuleSubmit = async (e) => {
        e.preventDefault();
        const id = container.querySelector('#rule-id').value;
        const reason = container.querySelector('#rule-audit-reason').value.trim();
        
        if (!reason) {
          notificationStore.danger('You must provide a modification reason for audit tracking.');
          return;
        }

        const payload = {
          defaultEntitlement: container.querySelector('#rule-default-entitlement').value,
          monthlyAccrual: container.querySelector('#rule-monthly-accrual').value || null,
          minimumLeaveUnit: container.querySelector('#rule-min-leave-unit').value,
          maxConsecutiveDays: container.querySelector('#rule-max-consecutive').value || null,
          maxPerYear: container.querySelector('#rule-max-year').value || null,
          minNoticeDays: container.querySelector('#rule-min-notice').value,
          documentRequiredAfterDays: container.querySelector('#rule-doc-required-days').value,
          lifetimeLimit: container.querySelector('#rule-lifetime-limit').value || null,
          carryForwardAllowed: container.querySelector('#rule-carry-forward-allowed').checked,
          carryForwardLimit: container.querySelector('#rule-carry-forward-limit').value || null,
          carryForwardExpiryMonths: container.querySelector('#rule-carry-forward-expiry').value || null,
          encashmentAllowed: container.querySelector('#rule-encashment-allowed').checked,
          maximumEncashmentDays: container.querySelector('#rule-encashment-limit').value || null,
          minimumBalanceForEncashment: container.querySelector('#rule-encashment-min-bal').value || null,
          approvalLevel: container.querySelector('#rule-approval-level').value,
          effectiveFrom: container.querySelector('#rule-effective-from').value,
          effectiveTo: container.querySelector('#rule-effective-to').value || null,
          allowHalfDay: container.querySelector('#rule-allow-half-day').checked,
          isPaid: container.querySelector('#rule-is-paid').checked,
          auditReason: reason
        };

        try {
          const res = await apiClient.put(`/leaves/admin/policies/${id}`, payload);
          if (res?.success) {
            notificationStore.success('Entitlement rule updated successfully.');
            await this.loadRulesTab(container);
            ruleSelect.value = id;
            handleRuleChange();
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Validation error while saving rules.');
        }
      };

      form.addEventListener('submit', handleRuleSubmit);
      lifecycle.onCleanup(() => form.removeEventListener('submit', handleRuleSubmit));
    } 
    else if (this.activeTab === 'pay') {
      const paySelect = container.querySelector('#select-pay-rule');
      const workspace = container.querySelector('#pay-tiers-workspace');
      
      const handlePaySelect = async () => {
        const ruleId = paySelect.value;
        if (!ruleId) {
          workspace.classList.add('hide');
          return;
        }
        
        workspace.classList.remove('hide');
        await this.loadPayTiersList(container, ruleId);
      };

      paySelect.addEventListener('change', handlePaySelect);
      lifecycle.onCleanup(() => paySelect.removeEventListener('change', handlePaySelect));

      // Add Pay Tier Form Submission
      const addTierForm = container.querySelector('#form-add-paytier');
      const handleAddTier = async (e) => {
        e.preventDefault();
        const ruleId = paySelect.value;
        const payload = {
          tierLabel: container.querySelector('#paytier-label').value,
          dayFrom: parseInt(container.querySelector('#paytier-from').value),
          dayTo: container.querySelector('#paytier-to').value ? parseInt(container.querySelector('#paytier-to').value) : null,
          payPercentage: parseFloat(container.querySelector('#paytier-pct').value)
        };

        try {
          const res = await apiClient.post(`/leaves/admin/policies/${ruleId}/pay-rules`, payload);
          if (res?.success) {
            notificationStore.success('Payout tier added.');
            addTierForm.reset();
            await this.loadPayTiersList(container, ruleId);
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to add pay tier.');
        }
      };
      addTierForm.addEventListener('submit', handleAddTier);
      lifecycle.onCleanup(() => addTierForm.removeEventListener('submit', handleAddTier));
    }
    else if (this.activeTab === 'calendar') {
      const yearSelect = container.querySelector('#select-calendar-year');
      const createBtn = container.querySelector('#btn-create-calendar');
      
      const handleYearChange = () => {
        this.loadCalendarTab(container);
      };
      yearSelect.addEventListener('change', handleYearChange);
      lifecycle.onCleanup(() => yearSelect.removeEventListener('change', handleYearChange));

      // Create Calendar Action
      const handleCreateCalendar = async () => {
        const group = this.policyGroups.find(g => g.id === this.selectedGroupId);
        const year = yearSelect.value;
        const payload = {
          policyGroupId: this.selectedGroupId,
          year: parseInt(year),
          name: `${group.name} Calendar ${year}`
        };

        try {
          const res = await apiClient.post('/leaves/admin/holiday-calendars', payload);
          if (res?.success) {
            notificationStore.success('Holiday calendar created.');
            this.loadCalendarTab(container);
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to create calendar.');
        }
      };
      createBtn.addEventListener('click', handleCreateCalendar);
      lifecycle.onCleanup(() => createBtn.removeEventListener('click', handleCreateCalendar));

      // Add Holiday Form Submission
      const addHolidayForm = container.querySelector('#form-add-holiday');
      const handleAddHoliday = async (e) => {
        e.preventDefault();
        const payload = {
          calendarId: this.selectedCalendar.id,
          name: container.querySelector('#holiday-name-input').value,
          date: container.querySelector('#holiday-date-input').value,
          isWorkingDay: container.querySelector('#holiday-working-checkbox').checked
        };

        try {
          const res = await apiClient.post('/leaves/admin/holidays', payload);
          if (res?.success) {
            notificationStore.success('Holiday date added.');
            addHolidayForm.reset();
            this.loadCalendarTab(container);
          }
        } catch (err) {
          notificationStore.danger(err.message || 'Failed to save holiday date.');
        }
      };
      addHolidayForm.addEventListener('submit', handleAddHoliday);
      lifecycle.onCleanup(() => addHolidayForm.removeEventListener('submit', handleAddHoliday));

      // Delete Holiday Buttons delegation
      const tbody = container.querySelector('#holidays-tbody');
      const handleDeleteHolidayClick = async (e) => {
        const btn = e.target.closest('.btn-delete-holiday');
        if (!btn) return;
        
        const id = btn.getAttribute('data-id');
        if (confirm('Delete this holiday date?')) {
          try {
            const res = await apiClient.delete(`/leaves/admin/holidays/${id}`);
            if (res?.success) {
              notificationStore.success('Holiday date removed.');
              this.loadCalendarTab(container);
            }
          } catch (err) {
            notificationStore.danger('Failed to delete holiday.');
          }
        }
      };
      tbody.addEventListener('click', handleDeleteHolidayClick);
      lifecycle.onCleanup(() => tbody.removeEventListener('click', handleDeleteHolidayClick));
    }
    else if (this.activeTab === 'weekly-off') {
      const form = container.querySelector('#form-weekly-off');
      const handleWeeklyOffSubmit = async (e) => {
        e.preventDefault();
        const checked = [];
        container.querySelectorAll('#form-weekly-off input[type="checkbox"]:checked').forEach(cb => {
          checked.push(cb.value);
        });

        if (checked.length === 0) {
          notificationStore.danger('Weekly off configuration must include at least one rest day.');
          return;
        }

        try {
          const res = await apiClient.put('/leaves/admin/weekly-offs', {
            policyGroupId: this.selectedGroupId,
            days: checked
          });
          if (res?.success) {
            notificationStore.success('Weekly off rules saved.');
          }
        } catch (err) {
          notificationStore.danger('Failed to save weekly off configuration.');
        }
      };
      form.addEventListener('submit', handleWeeklyOffSubmit);
      lifecycle.onCleanup(() => form.removeEventListener('submit', handleWeeklyOffSubmit));
    }
  }

  async loadPayTiersList(container, ruleId) {
    const tbody = container.querySelector('#pay-tiers-tbody');
    if (!tbody) return;

    try {
      const res = await apiClient.get(`/leaves/admin/policies/${ruleId}/pay-rules`);
      if (res?.success) {
        const tiers = res.data;
        if (tiers.length === 0) {
          tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted">No tiered payout brackets defined yet.</td></tr>';
          this.applyAccessControl(container);
          return;
        }

        tbody.innerHTML = tiers.map(t => `
          <tr>
            <td class="font-semibold">${t.tierLabel || 'Payout'}</td>
            <td>Days ${t.dayFrom} - ${t.dayTo ? t.dayTo : 'Open-ended'}</td>
            <td>
              <span class="badge ${t.payPercentage.toString() === '100' ? 'bg-emerald-100 text-emerald-800' : 
                                  t.payPercentage.toString() === '0' ? 'bg-red-100 text-red-800' : 'bg-amber-100 text-amber-800'}">
                ${t.payPercentage}% Payout
              </span>
            </td>
            <td>
              <button type="button" class="btn btn-sm btn-outline text-red-500 btn-delete-paytier" data-id="${t.id}">
                <i data-lucide="trash-2" aria-hidden="true" class="w-4 h-4"></i>
              </button>
            </td>
          </tr>
        `).join('');

        // Bind delete action
        tbody.querySelectorAll('.btn-delete-paytier').forEach(btn => {
          btn.addEventListener('click', async () => {
            if (confirm('Delete this payout tier?')) {
              try {
                const delRes = await apiClient.delete(`/leaves/admin/pay-rules/${btn.getAttribute('data-id')}`);
                if (delRes?.success) {
                  notificationStore.success('Payout tier deleted.');
                  await this.loadPayTiersList(container, ruleId);
                }
              } catch (err) {
                notificationStore.danger('Failed to delete pay tier.');
              }
            }
          });
        });

        if (window.lucide) window.lucide.createIcons();
      }
      this.applyAccessControl(container);
    } catch (err) {
      notificationStore.danger('Failed to fetch payout tiers.');
    }
  }

  destroy() {
    logger.debug('LeavePolicyPage', 'Component unmounted.');
  }
}
