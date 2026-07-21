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
    this.latitude = null;
    this.longitude = null;
    this.geofenceRadiusMeters = null;
    
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

      // Fetch the store details to get geofence / coordinates
      const storeRes = await apiClient.get(`/api/v1/stores/${this.storeId}`);
      if (storeRes && storeRes.success && storeRes.data) {
        this.latitude = storeRes.data.latitude;
        this.longitude = storeRes.data.longitude;
        this.geofenceRadiusMeters = storeRes.data.geofenceRadiusMeters;
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
      notificationStore.danger('Could not load workspace configurations.');
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

    // Fill store geolocation fields and lock them
    const latInput = container.querySelector('#settings-store-latitude');
    const lngInput = container.querySelector('#settings-store-longitude');
    const geofenceInput = container.querySelector('#settings-store-geofence');
    if (latInput) {
      latInput.value = this.latitude ?? '';
      latInput.disabled = true;
    }
    if (lngInput) {
      lngInput.value = this.longitude ?? '';
      lngInput.disabled = true;
    }
    if (geofenceInput) {
      geofenceInput.value = this.geofenceRadiusMeters ?? 200;
      geofenceInput.disabled = true;
    }

    // Reset Edit Location button lock states
    const editLocBtn = container.querySelector('#btn-edit-location');
    const getGeoBtn = container.querySelector('#btn-get-current-location');
    if (editLocBtn && getGeoBtn) {
      const btnText = editLocBtn.querySelector('span');
      if (btnText) btnText.textContent = 'Edit Location';
      editLocBtn.style.background = 'rgba(255, 255, 255, 0.05)';
      editLocBtn.style.borderColor = 'rgba(255, 255, 255, 0.15)';
      getGeoBtn.disabled = true;
      getGeoBtn.style.display = 'none';
    }

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
    const containers = [
      container.querySelector('#ops-shifts-config-container'),
      container.querySelector('#shifts-config-container')
    ].filter(Boolean);

    if (containers.length === 0) return;

    if (this.shifts.length === 0) {
      containers.forEach(c => c.innerHTML = '<p class="section-desc">No shift schedules loaded.</p>');
      return;
    }

    const html = this.shifts.map(s => {
      const start = s.startTime ? s.startTime.substring(0, 5) : '08:00';
      const end = s.endTime ? s.endTime.substring(0, 5) : '17:00';
      const shiftType = s.shiftType || 'CUSTOM';

      const presetLabels = {
        'MORNING': '🌅 Morning',
        'MID': '☀️ Mid-Day',
        'LATE': '🌆 Late',
        'NIGHT': '🌙 Night',
        'CUSTOM': '⚡ Custom'
      };

      return `
        <div class="shift-timing-card" data-shift-id="${s.id}" style="background: rgba(255,255,255,0.03); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: var(--spacing-md); margin-bottom: var(--spacing-sm);">
          <div class="shift-card-header" style="display:flex; justify-content:space-between; align-items:center; margin-bottom: var(--spacing-sm);">
            <div style="display:flex; align-items:center; gap: 8px;">
              <span class="shift-card-title" style="font-weight: 700; color: var(--text-primary); font-size: 0.95rem;">${s.name} (${s.code})</span>
              <span class="badge" style="font-size: 0.7rem; padding: 2px 8px; border-radius: 4px; background: rgba(201,164,106,0.15); color: var(--accent-primary); border: 1px solid rgba(201,164,106,0.3);">${presetLabels[shiftType] || shiftType}</span>
            </div>
            <div class="shift-card-actions" style="display:flex; align-items:center; gap: 10px;">
              <label style="font-size: 0.75rem; color: var(--text-muted); display:flex; align-items:center; gap: 4px; cursor: pointer;">
                <input type="checkbox" class="shift-active-toggle" ${s.active ? 'checked' : ''}> Active
              </label>
              <button type="button" class="btn btn-primary btn-save-shift" data-shift-id="${s.id}" style="padding: 5px 12px; font-size: 0.72rem;">
                <i data-lucide="save"></i> Save Shift
              </button>
              <button type="button" class="btn btn-secondary btn-delete-shift" data-shift-id="${s.id}" style="padding: 5px 12px; font-size: 0.72rem; background: rgba(239,68,68,0.15); color: #f87171; border: 1px solid rgba(239,68,68,0.3);">
                <i data-lucide="trash-2"></i> Delete
              </button>
            </div>
          </div>

          <div class="form-grid-3col" style="gap: 8px;">
            <div class="form-group">
              <label class="form-label" style="font-size:0.75rem;">Start Time</label>
              <input type="time" class="form-input shift-start" value="${start}" required>
            </div>
            <div class="form-group">
              <label class="form-label" style="font-size:0.75rem;">End Time</label>
              <input type="time" class="form-input shift-end" value="${end}" required>
            </div>
            <div class="form-group">
              <label class="form-label" style="font-size:0.75rem;">Break (Minutes)</label>
              <input type="number" class="form-input shift-break" value="${s.breakMinutes ?? 30}" min="0" required>
            </div>
          </div>
          <div class="form-grid-2col" style="gap: 8px; margin-top: 8px;">
            <div class="form-group">
              <label class="form-label" style="font-size:0.75rem;">Min Required Staff</label>
              <input type="number" class="form-input shift-min-emp" value="${s.minEmployees ?? 2}" min="1" required>
            </div>
            <div class="form-group">
              <label class="form-label" style="font-size:0.75rem;">Max Allowed Staff Capacity</label>
              <input type="number" class="form-input shift-max-emp" value="${s.maxEmployees ?? 8}" min="1" required>
            </div>
          </div>
        </div>
      `;
    }).join('');

    containers.forEach(c => c.innerHTML = html);
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

      // Geofencing Coordinates
      const latVal = operationsForm.querySelector('#settings-store-latitude').value.trim();
      const lngVal = operationsForm.querySelector('#settings-store-longitude').value.trim();
      const geofenceVal = operationsForm.querySelector('#settings-store-geofence').value.trim();

      if (!hours || !wifiSsid || !wifiPassword || !receiptFooter || !latVal || !lngVal || !geofenceVal) {
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

      const latitude = parseFloat(latVal);
      const longitude = parseFloat(lngVal);
      const geofenceRadius = parseInt(geofenceVal, 10);

      if (isNaN(latitude) || latitude < -90 || latitude > 90) {
        notificationStore.warning('Latitude must be a valid numeric coordinate between -90 and 90.');
        return;
      }
      if (isNaN(longitude) || longitude < -180 || longitude > 180) {
        notificationStore.warning('Longitude must be a valid numeric coordinate between -180 and 180.');
        return;
      }
      if (isNaN(geofenceRadius) || geofenceRadius < 10) {
        notificationStore.warning('Geofence boundary radius must be a valid number of 10 or greater.');
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

        const locationPayload = {
          latitude: latitude,
          longitude: longitude,
          geofenceRadiusMeters: geofenceRadius
        };

        // Fire both updates in parallel
        const [res, locationRes] = await Promise.all([
          apiClient.put(`/api/v1/stores/${this.storeId}/settings`, payload),
          apiClient.put(`/api/v1/stores/${this.storeId}/location`, locationPayload)
        ]);

        if (res && res.success && locationRes && locationRes.success) {
          notificationStore.success('Store operations configurations and location boundaries saved successfully.');
          this.settings = res.data;
          this.latitude = locationRes.data.latitude;
          this.longitude = locationRes.data.longitude;
          this.geofenceRadiusMeters = locationRes.data.geofenceRadiusMeters;
          this._render(container);
        } else {
          const errMsg = (!res || !res.success) ? (res?.message || 'Error occurred while saving configurations.') : (locationRes?.message || 'Error occurred while saving location boundaries.');
          notificationStore.danger(errMsg);
        }
      } catch (err) {
        logger.error('StoreSettings', 'Failed to update store configurations.', err);
        notificationStore.danger('Could not save store configurations due to server issues.');
      } finally {
        if (saveBtn) saveBtn.disabled = false;
      }
    };

    if (operationsForm) {
      operationsForm.addEventListener('submit', onOperationsSubmit);
    }

    // 2b. Geolocation API Lookup Button Event
    const geoBtn = container.querySelector('#btn-get-current-location');
    if (geoBtn) {
      geoBtn.addEventListener('click', () => {
        if (!navigator.geolocation) {
          notificationStore.danger('Geolocation is not supported by your browser.');
          return;
        }

        geoBtn.disabled = true;
        const btnText = geoBtn.querySelector('span');
        if (btnText) btnText.textContent = '📍 Acquiring Location...';

        navigator.geolocation.getCurrentPosition(
          (position) => {
            const latField = container.querySelector('#settings-store-latitude');
            const lngField = container.querySelector('#settings-store-longitude');
            if (latField) latField.value = position.coords.latitude.toFixed(6);
            if (lngField) lngField.value = position.coords.longitude.toFixed(6);
            
            notificationStore.success('Successfully retrieved current coordinates!');
            geoBtn.disabled = false;
            if (btnText) btnText.textContent = '📍 Use Current GPS Location';
          },
          (error) => {
            logger.error('StoreSettings', 'Geolocation retrieval failed:', error);
            notificationStore.danger('Failed to retrieve location: ' + error.message);
            geoBtn.disabled = false;
            if (btnText) btnText.textContent = '📍 Use Current GPS Location';
          },
          { enableHighAccuracy: true, timeout: 8000 }
        );
      });
    }

    // 2c. Edit Location Button Toggle Event
    const editLocBtn = container.querySelector('#btn-edit-location');
    if (editLocBtn) {
      editLocBtn.addEventListener('click', () => {
        const latField = container.querySelector('#settings-store-latitude');
        const lngField = container.querySelector('#settings-store-longitude');
        const geofenceField = container.querySelector('#settings-store-geofence');
        const getGeoBtn = container.querySelector('#btn-get-current-location');
        const btnText = editLocBtn.querySelector('span');

        if (!latField || !lngField || !geofenceField || !getGeoBtn) return;

        const isCurrentlyDisabled = latField.disabled;

        if (isCurrentlyDisabled) {
          // Enable inputs for editing
          latField.disabled = false;
          lngField.disabled = false;
          geofenceField.disabled = false;
          getGeoBtn.disabled = false;
          getGeoBtn.style.display = 'inline-flex';
          
          if (btnText) btnText.textContent = 'Lock Location';
          editLocBtn.style.background = 'rgba(201, 164, 106, 0.2)';
          editLocBtn.style.borderColor = 'var(--accent-primary, #c9a46a)';
        } else {
          // Disable and Lock inputs
          latField.disabled = true;
          lngField.disabled = true;
          geofenceField.disabled = true;
          getGeoBtn.disabled = true;
          getGeoBtn.style.display = 'none';

          // Restore original values since editing is cancelled/locked
          latField.value = this.latitude ?? '';
          lngField.value = this.longitude ?? '';
          geofenceField.value = this.geofenceRadiusMeters ?? 200;

          if (btnText) btnText.textContent = 'Edit Location';
          editLocBtn.style.background = 'rgba(255, 255, 255, 0.05)';
          editLocBtn.style.borderColor = 'rgba(255, 255, 255, 0.15)';
        }
      });
    }

    // 2d. Shift Preset Auto-Population Handler
    const presetSelect = container.querySelector('#shift-preset-select');
    if (presetSelect) {
      presetSelect.addEventListener('change', (e) => {
        const val = e.target.value;
        const nameInput = container.querySelector('#new-shift-name');
        const codeInput = container.querySelector('#new-shift-code');
        const startInput = container.querySelector('#new-shift-start');
        const endInput = container.querySelector('#new-shift-end');
        const breakInput = container.querySelector('#new-shift-break');
        const minEmpInput = container.querySelector('#new-shift-min-emp');
        const maxEmpInput = container.querySelector('#new-shift-max-emp');

        const presets = {
          'MORNING': { name: 'Morning Opening Shift', code: 'ST_SHIFT_MRN', start: '06:00', end: '14:00', breakMin: 30, minEmp: 2, maxEmp: 6 },
          'MID': { name: 'Mid-Day Peak Shift', code: 'ST_SHIFT_MID', start: '10:00', end: '18:00', breakMin: 30, minEmp: 2, maxEmp: 8 },
          'LATE': { name: 'Late Closing Shift', code: 'ST_SHIFT_LTE', start: '14:00', end: '22:00', breakMin: 30, minEmp: 2, maxEmp: 6 },
          'NIGHT': { name: 'Night Operations Shift', code: 'ST_SHIFT_NGT', start: '22:00', end: '06:00', breakMin: 45, minEmp: 1, maxEmp: 4 }
        };

        if (presets[val]) {
          const p = presets[val];
          if (nameInput) nameInput.value = p.name;
          if (codeInput) codeInput.value = p.code + '_' + Math.floor(10 + Math.random() * 90);
          if (startInput) startInput.value = p.start;
          if (endInput) endInput.value = p.end;
          if (breakInput) breakInput.value = p.breakMin;
          if (minEmpInput) minEmpInput.value = p.minEmp;
          if (maxEmpInput) maxEmpInput.value = p.maxEmp;
        }
      });
    }

    // 2e. Create New Shift Submission Handler
    const addShiftBtn = container.querySelector('#btn-add-shift-submit');
    if (addShiftBtn) {
      addShiftBtn.addEventListener('click', async () => {
        const shiftType = container.querySelector('#shift-preset-select')?.value || 'CUSTOM';
        const name = container.querySelector('#new-shift-name')?.value.trim();
        const code = container.querySelector('#new-shift-code')?.value.trim();
        const startTime = container.querySelector('#new-shift-start')?.value;
        const endTime = container.querySelector('#new-shift-end')?.value;
        const breakMinutes = parseInt(container.querySelector('#new-shift-break')?.value, 10);
        const minEmployees = parseInt(container.querySelector('#new-shift-min-emp')?.value, 10);
        const maxEmployees = parseInt(container.querySelector('#new-shift-max-emp')?.value, 10);

        if (!name || !code || !startTime || !endTime || isNaN(breakMinutes) || isNaN(minEmployees) || isNaN(maxEmployees)) {
          notificationStore.warning('All shift creation fields are required.');
          return;
        }

        if (minEmployees > maxEmployees) {
          notificationStore.warning('Minimum required staff cannot exceed maximum staff capacity.');
          return;
        }

        addShiftBtn.disabled = true;

        try {
          const payload = {
            name,
            code,
            shiftType,
            startTime: `${startTime}:00`,
            endTime: `${endTime}:00`,
            breakMinutes,
            minEmployees,
            maxEmployees
          };

          const res = await apiClient.post('/api/v1/shifts', payload);
          if (res && res.success) {
            notificationStore.success('New shift schedule created & broadcasted to Shift Supervisors!');
            this.shifts.push(res.data);
            this._render(container);
          } else {
            notificationStore.danger(res?.message || 'Failed to create shift schedule.');
          }
        } catch (err) {
          logger.error('StoreSettings', 'Failed to create shift schedule', err);
          notificationStore.danger('Could not create shift schedule.');
        } finally {
          addShiftBtn.disabled = false;
        }
      });
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
      const minEmployees = parseInt(card.querySelector('.shift-min-emp').value, 10);
      const maxEmployees = parseInt(card.querySelector('.shift-max-emp').value, 10);
      const active = card.querySelector('.shift-active-toggle')?.checked ?? true;

      if (!startTime || !endTime || isNaN(breakMinutes) || isNaN(minEmployees) || isNaN(maxEmployees)) {
        notificationStore.warning('Shift parameters must be fully configured.');
        return;
      }

      if (minEmployees > maxEmployees) {
        notificationStore.warning('Minimum staff cannot exceed maximum staff capacity.');
        return;
      }

      btn.disabled = true;

      try {
        const payload = {
          startTime: `${startTime}:00`,
          endTime: `${endTime}:00`,
          breakMinutes,
          minEmployees,
          maxEmployees,
          active
        };

        const res = await apiClient.put(`/api/v1/shifts/${shiftId}`, payload);
        if (res && res.success) {
          notificationStore.success('Shift schedule & headcount rules saved! Shift Supervisors notified.');
          // Update local copy
          const idx = this.shifts.findIndex(s => s.id === parseInt(shiftId, 10));
          if (idx !== -1) {
            this.shifts[idx] = res.data;
          }
          this._render(container);
        } else {
          notificationStore.danger(res?.message || 'Error updating shift timings.');
        }
      } catch (err) {
        logger.error('StoreSettings', 'Failed to save shift details.', err);
        notificationStore.danger('Could not save shift timing details.');
      } finally {
        btn.disabled = false;
      }
    };

    container.addEventListener('click', onShiftSaveClick);

    // 3b. Delete Shift Card Action Listener
    const onShiftDeleteClick = async (e) => {
      const btn = e.target.closest('.btn-delete-shift');
      if (!btn) return;

      const shiftId = btn.getAttribute('data-shift-id');
      if (!shiftId) return;

      const shift = this.shifts.find(s => s.id === parseInt(shiftId, 10));
      const shiftName = shift ? shift.name : 'this shift';

      if (!confirm(`Are you sure you want to delete or deactivate '${shiftName}'?`)) {
        return;
      }

      btn.disabled = true;

      try {
        const res = await apiClient.delete(`/api/v1/shifts/${shiftId}`);
        if (res && res.success) {
          notificationStore.success(res.message || 'Shift schedule deleted successfully.');
          this.shifts = this.shifts.filter(s => s.id !== parseInt(shiftId, 10));
          this._render(container);
        } else {
          notificationStore.danger(res?.message || 'Failed to delete shift schedule.');
        }
      } catch (err) {
        logger.error('StoreSettings', 'Failed to delete shift schedule.', err);
        notificationStore.danger('Could not delete shift schedule.');
      } finally {
        btn.disabled = false;
      }
    };

    container.addEventListener('click', onShiftDeleteClick);



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
          notificationStore.danger(res?.message || 'Could not register holiday.');
        }
      } catch (err) {
        logger.error('StoreSettings', 'Failed to add holiday.', err);
        notificationStore.danger('Failed to register holiday due to server error.');
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
          notificationStore.danger(res?.message || 'Could not delete holiday.');
        }
      } catch (err) {
        logger.error('StoreSettings', 'Failed to delete holiday.', err);
        notificationStore.danger('Could not delete holiday due to server issue.');
      } finally {
        btn.disabled = false;
      }
    };

    container.addEventListener('click', onDeleteHolidayClick);

    // 6. Year Filter Selector Change
    const yearSelect = container.querySelector('#holiday-year-select');
    const onYearChange = async (e) => {
      try {
        this.selectedYear = parseInt(e.target.value, 10);
        await this._fetchHolidays();
        this._render(container);
      } catch (err) {
        logger.error('StoreSettings', 'Failed to load holidays for selected year.', err);
        notificationStore.danger('Could not load holiday calendar for the selected year.');
      }
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
        notificationStore.danger('Could not update theme preferences.');
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
