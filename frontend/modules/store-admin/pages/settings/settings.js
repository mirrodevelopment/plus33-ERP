/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Admin — Settings Configuration
 * File              : settings.js
 * Path              : frontend/modules/store-admin/pages/settings/settings.js
 * Purpose           : Controller component managing Store Settings page, shifts, and holiday calendars
 * Version           : 2.2.0
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { themeStore } from '../../../../store/themeStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';

/** Path to the settings template */
const TEMPLATE_URL = 'modules/store-admin/pages/settings/settings.html';

export default class StoreSettings {

  constructor() {
    this.user = authStore.getUser();
    this.storeId = null;
    this.storeCode = null;
    this.storeName = null;
    this.settings = null;
    
    // UI state
    this.shifts = [];
    this.holidays = [];
    this.selectedYear = 2026;
    this.selectedTheme = themeStore.getTheme();
  }

  /**
   * Mount the store settings component page context.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreSettings', 'Mounting Store Settings Page...');

    // Load styles
    this._loadCss();

    // 1. Inject template
    await this._loadTemplate(container);

    // 2. Fetch live settings data, shifts and holidays
    await this._loadData();

    // 3. Render content
    this._render(container);

    // 4. Bind event listeners
    this._bindEvents(container, lifecycle);
  }

  _loadCss() {
    const cssId = 'store-settings-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-admin/pages/settings/settings.css';
      document.head.appendChild(link);
    }
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  async _loadData() {
    try {
      // 1. Get current logged in user details to find the assigned store ID
      const meRes = await apiClient.get('/api/v1/auth/me');
      if (meRes && meRes.success && meRes.data) {
        this.storeId = meRes.data.storeId;
        this.storeCode = meRes.data.storeCode;
        this.storeName = meRes.data.store;
      }

      // Fallback to Store ID 1 if not linked to a user session
      if (!this.storeId) {
        logger.warn('StoreSettings', 'No storeId linked to user session. Defaulting to Store ID 1.');
        this.storeId = 1;
        this.storeCode = 'STR-001';
        this.storeName = 'Paris Versailles';
      }

      // 2. Fetch the store settings using the store ID
      const settingsRes = await apiClient.get(`/api/v1/stores/${this.storeId}/settings`);
      if (settingsRes && settingsRes.success) {
        this.settings = settingsRes.data;
      }

      // 3. Fetch Shift schedules
      const shiftsRes = await apiClient.get('/api/v1/shifts');
      if (shiftsRes && shiftsRes.success) {
        this.shifts = shiftsRes.data;
      }

      // 4. Fetch Holidays
      await this._fetchHolidays();

    } catch (err) {
      logger.error('StoreSettings', 'Failed to retrieve settings data.', err);
      notificationStore.error('Could not load workspace configurations.');
    }
  }

  async _fetchHolidays() {
    try {
      const holidaysRes = await apiClient.get(`/api/v1/leaves/holidays?year=${this.selectedYear}`);
      if (holidaysRes && holidaysRes.success) {
        this.holidays = holidaysRes.data;
      }
    } catch (err) {
      logger.error('StoreSettings', 'Failed to query holidays.', err);
    }
  }

  _render(container) {
    if (!container) return;

    // Fill metadata fields
    const nameInput = container.querySelector('#settings-store-name');
    const codeInput = container.querySelector('#settings-store-code');
    if (nameInput) nameInput.value = this.storeName || '';
    if (codeInput) codeInput.value = this.storeCode || '';

    // Fill configurations if loaded
    if (this.settings) {
      const hoursInput = container.querySelector('#settings-operating-hours');
      const lowStockInput = container.querySelector('#settings-low-stock');
      const salesTargetInput = container.querySelector('#settings-sales-target');
      const wifiSsidInput = container.querySelector('#settings-wifi-ssid');
      const wifiPasswordInput = container.querySelector('#settings-wifi-password');
      const receiptFooterText = container.querySelector('#settings-receipt-footer');

      if (hoursInput) hoursInput.value = this.settings.operatingHours || '';
      if (lowStockInput) lowStockInput.value = this.settings.lowStockThreshold ?? 50;
      if (salesTargetInput) salesTargetInput.value = this.settings.salesTarget ?? 10000.00;
      if (wifiSsidInput) wifiSsidInput.value = this.settings.wifiSsid || '';
      if (wifiPasswordInput) wifiPasswordInput.value = this.settings.wifiPassword || '';
      if (receiptFooterText) receiptFooterText.value = this.settings.receiptFooter || '';
    }

    // Render shift cards
    this._renderShifts(container);

    // Render holidays list
    this._renderHolidays(container);

    // Load Local Storage personal preferences
    const activeCurrency = localStorage.getItem('system_currency') || 'EUR';
    const activeLanguage = localStorage.getItem('system_language') || 'en';

    const currencySelect = container.querySelector('#pref-currency');
    const languageSelect = container.querySelector('#pref-language');
    if (currencySelect) currencySelect.value = activeCurrency;
    if (languageSelect) languageSelect.value = activeLanguage;

    // Render active theme card selection states
    this.selectedTheme = themeStore.getTheme();
    const themeCards = container.querySelectorAll('.theme-card-option');
    themeCards.forEach(card => {
      const val = card.getAttribute('data-theme-val');
      if (val === this.selectedTheme) {
        card.classList.add('theme-card-option--selected');
        card.setAttribute('aria-checked', 'true');
      } else {
        card.classList.remove('theme-card-option--selected');
        card.setAttribute('aria-checked', 'false');
      }
    });

    // Refresh lucide icons
    if (window.lucide) {
      window.lucide.createIcons();
    }
  }

  _renderShifts(container) {
    const shiftsContainer = container.querySelector('#shifts-config-container');
    if (!shiftsContainer) return;

    if (this.shifts.length === 0) {
      shiftsContainer.innerHTML = '<p class="section-desc">No shift timings loaded.</p>';
      return;
    }

    shiftsContainer.innerHTML = this.shifts.map(s => {
      // Ensure time string is HH:MM formatted
      const start = s.startTime ? s.startTime.substring(0, 5) : '08:00';
      const end = s.endTime ? s.endTime.substring(0, 5) : '17:00';

      return `
        <div class="shift-timing-card" data-shift-id="${s.id}">
          <div class="shift-card-header">
            <span class="shift-card-title">${s.name} (${s.code})</span>
            <div class="shift-card-actions">
              <span class="shift-status-badge">${s.active ? 'Active' : 'Inactive'}</span>
              <button type="button" class="btn btn-primary btn-save-shift" data-shift-id="${s.id}" style="padding: 6px 12px; font-size: 0.72rem;">
                <i data-lucide="save"></i> Save Shift
              </button>
            </div>
          </div>
          <div class="form-grid-3col">
            <div class="form-group">
              <label class="form-label">Start Time</label>
              <input type="time" class="form-input shift-start" value="${start}" required>
            </div>
            <div class="form-group">
              <label class="form-label">End Time</label>
              <input type="time" class="form-input shift-end" value="${end}" required>
            </div>
            <div class="form-group">
              <label class="form-label">Break Duration (Min)</label>
              <input type="number" class="form-input shift-break" value="${s.breakMinutes ?? 30}" min="0" required>
            </div>
          </div>
        </div>
      `;
    }).join('');
  }

  _renderHolidays(container) {
    const listContainer = container.querySelector('#holidays-list-container');
    if (!listContainer) return;

    if (this.holidays.length === 0) {
      listContainer.innerHTML = '<p class="section-desc" style="padding: 20px; text-align: center;">No holidays registered for this year.</p>';
      return;
    }

    listContainer.innerHTML = this.holidays.map(h => `
      <div class="holiday-item" data-holiday-id="${h.id}">
        <div class="holiday-item-details">
          <span class="holiday-item-name">${h.name}</span>
          <div class="holiday-item-meta">
            <span>${h.date}</span>
            ${h.isRecurring ? '<span class="holiday-badge">Yearly Recurring</span>' : ''}
          </div>
        </div>
        <button type="button" class="btn-delete-holiday" data-holiday-id="${h.id}" title="Remove holiday">
          <i data-lucide="trash-2"></i>
        </button>
      </div>
    `).join('');
  }

  _bindEvents(container, lifecycle) {
    // 1. Tab Switching Listeners
    const tabBtns = container.querySelectorAll('.settings-tab-btn');
    const tabPanes = container.querySelectorAll('.settings-tab-pane');

    const handleTabClick = (e) => {
      const btn = e.currentTarget;
      const targetTab = btn.getAttribute('data-tab');

      tabBtns.forEach(b => b.classList.remove('active'));
      tabPanes.forEach(p => p.classList.remove('active'));

      btn.classList.add('active');
      const pane = container.querySelector(`#tab-${targetTab}`);
      if (pane) pane.classList.add('active');

      logger.info('StoreSettings', `Tab switched to: ${targetTab}`);
    };

    tabBtns.forEach(btn => btn.addEventListener('click', handleTabClick));

    // 2. Save Operations Settings Form Submit
    const operationsForm = container.querySelector('#form-store-settings');
    const onOperationsSubmit = async (e) => {
      e.preventDefault();

      const hours = operationsForm.querySelector('#settings-operating-hours').value.trim();
      const lowStock = parseInt(operationsForm.querySelector('#settings-low-stock').value, 10);
      const salesTarget = parseFloat(operationsForm.querySelector('#settings-sales-target').value);
      const wifiSsid = operationsForm.querySelector('#settings-wifi-ssid').value.trim();
      const wifiPassword = operationsForm.querySelector('#settings-wifi-password').value.trim();
      const receiptFooter = operationsForm.querySelector('#settings-receipt-footer').value.trim();

      if (!hours || !wifiSsid || !wifiPassword || !receiptFooter) {
        notificationStore.warning('All configurations must be filled out completely.');
        return;
      }

      if (isNaN(lowStock) || lowStock < 0) {
        notificationStore.warning('Low stock threshold must be a valid number of 0 or greater.');
        return;
      }

      if (isNaN(salesTarget) || salesTarget < 0) {
        notificationStore.warning('Daily sales target must be a valid currency value of 0 or greater.');
        return;
      }

      const saveBtn = operationsForm.querySelector('#btn-save-settings');
      if (saveBtn) saveBtn.disabled = true;

      try {
        const payload = {
          operatingHours: hours,
          lowStockThreshold: lowStock,
          salesTarget: salesTarget,
          wifiSsid: wifiSsid,
          wifiPassword: wifiPassword,
          receiptFooter: receiptFooter
        };

        const res = await apiClient.put(`/api/v1/stores/${this.storeId}/settings`, payload);
        if (res && res.success) {
          notificationStore.success('Store operations configurations saved successfully.');
          this.settings = res.data;
          this._render(container);
        } else {
          notificationStore.error(res?.message || 'Error occurred while saving configurations.');
        }
      } catch (err) {
        logger.error('StoreSettings', 'Failed to update store configurations.', err);
        notificationStore.error('Could not save store configurations due to server issues.');
      } finally {
        if (saveBtn) saveBtn.disabled = false;
      }
    };

    if (operationsForm) {
      operationsForm.addEventListener('submit', onOperationsSubmit);
    }

    // 3. Save Shift Cards Action Listener
    const onShiftSaveClick = async (e) => {
      const btn = e.target.closest('.btn-save-shift');
      if (!btn) return;

      const shiftId = btn.getAttribute('data-shift-id');
      const card = container.querySelector(`.shift-timing-card[data-shift-id="${shiftId}"]`);
      if (!card) return;

      const startTime = card.querySelector('.shift-start').value;
      const endTime = card.querySelector('.shift-end').value;
      const breakMinutes = parseInt(card.querySelector('.shift-break').value, 10);

      if (!startTime || !endTime || isNaN(breakMinutes) || breakMinutes < 0) {
        notificationStore.warning('Shift parameters must be fully configured.');
        return;
      }

      btn.disabled = true;

      try {
        const payload = {
          startTime: `${startTime}:00`,
          endTime: `${endTime}:00`,
          breakMinutes
        };

        const res = await apiClient.put(`/api/v1/shifts/${shiftId}`, payload);
        if (res && res.success) {
          notificationStore.success('Shift timings saved successfully.');
          // Update local copy
          const idx = this.shifts.findIndex(s => s.id === parseInt(shiftId, 10));
          if (idx !== -1) {
            this.shifts[idx] = res.data;
          }
          this._render(container);
        } else {
          notificationStore.error(res?.message || 'Error updating shift timings.');
        }
      } catch (err) {
        logger.error('StoreSettings', 'Failed to save shift details.', err);
        notificationStore.error('Could not save shift timing details.');
      } finally {
        btn.disabled = false;
      }
    };

    container.addEventListener('click', onShiftSaveClick);

    // 4. Add Holiday Submission
    const addHolidayForm = container.querySelector('#form-add-holiday');
    const onAddHolidaySubmit = async (e) => {
      e.preventDefault();

      const name = addHolidayForm.querySelector('#holiday-name-input').value.trim();
      const date = addHolidayForm.querySelector('#holiday-date-input').value;
      const isRecurring = addHolidayForm.querySelector('#holiday-recurring-input').checked;

      if (!name || !date) {
        notificationStore.warning('Holiday name and date are required.');
        return;
      }

      const submitBtn = addHolidayForm.querySelector('#btn-add-holiday-submit');
      if (submitBtn) submitBtn.disabled = true;

      try {
        const payload = {
          name,
          date,
          isRecurring,
          countryCode: 'FR' // default store country code
        };

        const res = await apiClient.post('/api/v1/leaves/holidays', payload);
        if (res && res.success) {
          notificationStore.success('Holiday added to calendar.');
          // Reset form inputs
          addHolidayForm.reset();
          // Reload holidays list
          await this._fetchHolidays();
          this._render(container);
        } else {
          notificationStore.error(res?.message || 'Could not register holiday.');
        }
      } catch (err) {
        logger.error('StoreSettings', 'Failed to add holiday.', err);
        notificationStore.error('Failed to register holiday due to server error.');
      } finally {
        if (submitBtn) submitBtn.disabled = false;
      }
    };

    if (addHolidayForm) {
      addHolidayForm.addEventListener('submit', onAddHolidaySubmit);
    }

    // 5. Delete Holiday Action Listener
    const onDeleteHolidayClick = async (e) => {
      const btn = e.target.closest('.btn-delete-holiday');
      if (!btn) return;

      const holidayId = btn.getAttribute('data-holiday-id');
      if (!confirm('Are you sure you want to remove this holiday from the calendar?')) {
        return;
      }

      btn.disabled = true;

      try {
        const res = await apiClient.delete(`/api/v1/leaves/holidays/${holidayId}`);
        if (res && res.success) {
          notificationStore.success('Holiday deleted successfully.');
          await this._fetchHolidays();
          this._render(container);
        } else {
          notificationStore.error(res?.message || 'Could not delete holiday.');
        }
      } catch (err) {
        logger.error('StoreSettings', 'Failed to delete holiday.', err);
        notificationStore.error('Could not delete holiday due to server issue.');
      }
    };

    container.addEventListener('click', onDeleteHolidayClick);

    // 6. Year Filter Selector Change
    const yearSelect = container.querySelector('#holiday-year-select');
    const onYearChange = async (e) => {
      this.selectedYear = parseInt(e.target.value, 10);
      await this._fetchHolidays();
      this._render(container);
    };

    if (yearSelect) {
      yearSelect.addEventListener('change', onYearChange);
    }

    // 7. Theme Selection Clicks & Preview Modal
    const themeCards = container.querySelectorAll('.theme-card-option');
    const themeModal = container.querySelector('#theme-preview-modal');
    const themeModalImg = container.querySelector('#theme-preview-img');
    const themeModalTitle = container.querySelector('#theme-modal-title');
    const themeModalDesc = container.querySelector('#theme-modal-desc');

    const themeMetadata = {
      'coffee-dark': { name: 'Coffee Dark', desc: 'Warm coffee & gold tones - default brand skin', img: 'imgs/theme-coffee-dark.png' },
      'light': { name: 'Light Minimal', desc: 'Clean white palette - minimal layout skin', img: 'imgs/theme-light.png' },
      'charcoal': { name: 'Charcoal Noir', desc: 'Sleek dark with gold - premium charcoal dark skin', img: 'imgs/theme-charcoal.png' },
      'cyber-dark': { name: 'Cyber Dark', desc: 'Vibrant neon on deep black - high-tech glow skin', img: 'imgs/theme-cyber-dark.png' },
      'nation': { name: 'Nation', desc: 'French flag color theme - blue, white & red accents skin', img: 'imgs/theme-nation.png' }
    };

    let pendingThemeVal = null;

    const handleThemeClick = (e) => {
      const card = e.currentTarget;
      const themeVal = card.getAttribute('data-theme-val');
      const meta = themeMetadata[themeVal] || {};

      pendingThemeVal = themeVal;

      if (themeModal && themeModalImg && themeModalTitle && themeModalDesc) {
        themeModalTitle.textContent = `${meta.name} - Theme Preview`;
        themeModalDesc.textContent = meta.desc || '';
        themeModalImg.src = meta.img || '';
        themeModal.classList.add('active');
      }
    };

    themeCards.forEach(card => card.addEventListener('click', handleThemeClick));

    // Modal Action Buttons
    const closeModal = () => {
      if (themeModal) themeModal.classList.remove('active');
      pendingThemeVal = null;
    };

    const btnCloseModal = container.querySelector('#btn-close-theme-modal');
    const btnCancelTheme = container.querySelector('#btn-cancel-theme');
    const btnApplyThemeModal = container.querySelector('#btn-apply-theme-modal');

    if (btnCloseModal) btnCloseModal.addEventListener('click', closeModal);
    if (btnCancelTheme) btnCancelTheme.addEventListener('click', closeModal);

    const onApplyThemeFromModal = () => {
      if (pendingThemeVal) {
        this.selectedTheme = pendingThemeVal;
        
        // Update visual checked state on cards
        themeCards.forEach(c => {
          const val = c.getAttribute('data-theme-val');
          if (val === this.selectedTheme) {
            c.classList.add('theme-card-option--selected');
            c.setAttribute('aria-checked', 'true');
          } else {
            c.classList.remove('theme-card-option--selected');
            c.setAttribute('aria-checked', 'false');
          }
        });
        
        notificationStore.success(`Selected theme: ${themeMetadata[this.selectedTheme]?.name}. Click Apply Preferences to save.`);
      }
      closeModal();
    };

    if (btnApplyThemeModal) btnApplyThemeModal.addEventListener('click', onApplyThemeFromModal);

    // Overlay click close
    const handleOverlayClick = (e) => {
      if (e.target === themeModal) closeModal();
    };
    if (themeModal) themeModal.addEventListener('click', handleOverlayClick);

    // 8. Save Personal Preferences Form Submit
    const preferencesForm = container.querySelector('#form-personal-preferences');
    const onPreferencesSubmit = (e) => {
      e.preventDefault();

      const currencyVal = preferencesForm.querySelector('#pref-currency').value;
      const languageVal = preferencesForm.querySelector('#pref-language').value;

      try {
        // Save currency & language to local storage
        localStorage.setItem('system_currency', currencyVal);
        localStorage.setItem('system_language', languageVal);

        // Apply theme using ThemeStore
        themeStore.setTheme(this.selectedTheme);

        notificationStore.success('Preferences and theme updated successfully.');
        this._render(container);
      } catch (err) {
        logger.error('StoreSettings', 'Failed to apply theme preferences.', err);
        notificationStore.error('Could not update theme preferences.');
      }
    };

    if (preferencesForm) {
      preferencesForm.addEventListener('submit', onPreferencesSubmit);
    }

    // Cleanup listeners
    if (lifecycle && typeof lifecycle.onCleanup === 'function') {
      lifecycle.onCleanup(() => {
        tabBtns.forEach(btn => btn.removeEventListener('click', handleTabClick));
        themeCards.forEach(card => card.removeEventListener('click', handleThemeClick));
        container.removeEventListener('click', onShiftSaveClick);
        container.removeEventListener('click', onDeleteHolidayClick);
        if (operationsForm) operationsForm.removeEventListener('submit', onOperationsSubmit);
        if (addHolidayForm) addHolidayForm.removeEventListener('submit', onAddHolidaySubmit);
        if (yearSelect) yearSelect.removeEventListener('change', onYearChange);
        if (preferencesForm) preferencesForm.removeEventListener('submit', onPreferencesSubmit);
      });
    }
  }

  destroy() {
    logger.info('StoreSettings', 'Destroying Store Settings Page context...');
  }
}
