/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : schedule.js
 * Path              : frontend/modules/store-employee/schedule/schedule.js
 * Purpose           : Controller component for Barista weekly shift rosters UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/schedule/schedule.html
 * Related CSS       : frontend/modules/store-employee/schedule/schedule.css
 * Related APIs      : None (uses LocalStorage states caching)
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in schedule.html — this file is a controller only.
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the schedule HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/schedule/schedule.html';

export default class StoreEmployeeSchedule {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};

    this.loadState();
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the StoreEmployeeSchedule component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreEmployeeSchedule', 'Mounting Barista Shift Roster page...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML layout template
    await this._loadTemplate(container);

    // 2. Read state details
    this.loadState();

    // 3. Render layout elements
    this.render(container);

    // 4. Bind event listeners
    this.bindEvents(container, lifecycle);
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  loadState() {
    const cachedState = localStorage.getItem(`emp_schedule_state_${this.user?.username || 'neha'}`);
    if (cachedState) {
      try {
        this.state = JSON.parse(cachedState);
      } catch (err) {
        logger.error('StoreEmployeeSchedule', 'Error parsing cached state, resetting', err);
        this.initDefaultState();
      }
    } else {
      this.initDefaultState();
    }
  }

  initDefaultState() {
    this.state = {
      selectedShiftId: null,
      shifts: [
        { id: 101, date: 'Mon, 06 July', type: 'Morning Shift', time: '08:00 AM - 04:00 PM', role: 'Espresso Bar Lead', coworkers: ['Rohan D.', 'Simran K.'], notes: 'Log grinder temp checklist.' },
        { id: 102, date: 'Tue, 07 July', type: 'Morning Shift', time: '08:00 AM - 04:00 PM', role: 'Espresso Bar Lead', coworkers: ['Amit S.', 'Siddharth M.'], notes: 'Milk stock delivery expected at 09:30 AM.' },
        { id: 103, date: 'Wed, 08 July', type: 'Mid-Day Shift', time: '11:00 AM - 07:00 PM', role: 'Customer Service & Till', coworkers: ['Rohan D.', 'Simran K.'], notes: 'Till closing audit check.' },
        { id: 104, date: 'Thu, 09 July', type: 'Morning Shift', time: '08:00 AM - 04:00 PM', role: 'Espresso Bar Lead', coworkers: ['Amit S.', 'Rohan D.'], notes: 'Calibrate pressure gauge in grinder #2.' },
        { id: 105, date: 'Fri, 10 July', type: 'Evening Shift', time: '02:00 PM - 10:00 PM', role: 'Closing Supervisor', coworkers: ['Siddharth M.', 'Rohan D.'], notes: 'Store cleanup list checklist.' },
        { id: 106, date: 'Sat, 11 July', type: 'Day Off', time: 'Rest Day', role: '--', coworkers: [], notes: 'Weekly holiday.' },
        { id: 107, date: 'Sun, 12 July', type: 'Day Off', time: 'Rest Day', role: '--', coworkers: [], notes: 'Weekly holiday.' }
      ],
      swaps: [
        { id: 1, myShiftDate: 'Fri, 10 July', myShiftType: 'Evening Shift', peerName: 'Rohan Sharma', peerShiftDate: 'Fri, 10 July (Morning Shift)', status: 'Approved' },
        { id: 2, myShiftDate: 'Wed, 08 July', myShiftType: 'Mid-Day Shift', peerName: 'Simran Kaur', peerShiftDate: 'Wed, 08 July (Morning Shift)', status: 'Pending' }
      ],
      preferences: {
        preferredShift: 'Morning',
        availableWeekends: false,
        maxHoursPerWeek: 40
      }
    };
    this.saveState();
  }

  saveState() {
    localStorage.setItem(`emp_schedule_state_${this.user?.username || 'neha'}`, JSON.stringify(this.state));
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  render(container) {
    // 1. Sync header text
    const nameEl = container.querySelector('#lbl-employee-name');
    if (nameEl) nameEl.textContent = this.profile.name || 'Neha Sharma';

    // 2. Render weekly Day cards grid
    this._renderCalendarGrid(container);

    // 3. Render request select my shifts options
    const myShiftsSelect = container.querySelector('#swap-my-shift');
    if (myShiftsSelect) {
      myShiftsSelect.replaceChildren();
      const defaultOpt = document.createElement('option');
      defaultOpt.value = '';
      defaultOpt.textContent = '-- Choose one of your shifts --';
      myShiftsSelect.appendChild(defaultOpt);

      this.state.shifts.filter(s => s.type !== 'Day Off').forEach(s => {
        const opt = document.createElement('option');
        opt.value = String(s.id);
        opt.textContent = `${s.date} - ${s.type}`;
        myShiftsSelect.appendChild(opt);
      });
    }

    // 4. Render trades swap log table rows
    this._renderSwapLogs(container);

    // 5. Render Shift preferences form settings
    this._renderPreferences(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const overlay = container.querySelector('#schedule-modal-overlay');
    const modalContent = container.querySelector('#schedule-modal-content');

    const showModal = (htmlContent) => {
      if (overlay && modalContent) {
        modalContent.innerHTML = htmlContent;
        overlay.style.display = 'flex';
        overlay.setAttribute('aria-hidden', 'false');
        
        const closeBtn = modalContent.querySelector('.btn-close-modal');
        if (closeBtn) {
          closeBtn.addEventListener('click', () => hideModal());
        }
        if (window.lucide) window.lucide.createIcons();
      }
    };

    const hideModal = () => {
      if (overlay) {
        overlay.style.display = 'none';
        overlay.setAttribute('aria-hidden', 'true');
      }
    };

    if (overlay) {
      const handleOverlayClick = (e) => {
        if (e.target === overlay) hideModal();
      };
      overlay.addEventListener('click', handleOverlayClick);
      lifecycle.onCleanup(() => overlay.removeEventListener('click', handleOverlayClick));
    }

    // 1. Click Calendar Card for Shift Details
    const shiftCards = container.querySelectorAll('.shift-card');
    shiftCards.forEach(card => {
      const handleOpenShift = () => {
        const id = parseInt(card.getAttribute('data-id'));
        const shift = this.state.shifts.find(s => s.id === id);
        if (!shift) return;

        const isOff = shift.type === 'Day Off';
        const detailsHtml = `
          <div class="modal-header-split">
            <h3 class="modal-title">Shift Details: ${shift.date}</h3>
            <button class="btn-close-modal" type="button" aria-label="Close modal">
              <i data-lucide="x" aria-hidden="true"></i>
            </button>
          </div>
          <div style="font-size:0.75rem; color:var(--text-primary); display:flex; flex-direction:column; gap:12px;">
            <div>
              <strong>Shift Type:</strong> <span style="color:var(--accent-primary); font-weight:700;">${shift.type}</span>
            </div>
            <div>
              <strong>Timings:</strong> <span>${shift.time}</span>
            </div>
            <div>
              <strong>Station Assignment:</strong> <span>${shift.role}</span>
            </div>
            
            <strong style="font-size:0.8rem; color:var(--accent-primary); margin-top:4px;">Supervisor & Peers</strong>
            <div style="background:rgba(255,255,255,0.02); border:1px solid var(--border-color); padding:10px; border-radius:var(--radius-md); display:flex; flex-direction:column; gap:6px;">
              <div style="display:flex; justify-content:space-between;">
                <span>Shift Supervisor:</span>
                <span style="font-weight:700;">Manoj Kumar (Store Manager)</span>
              </div>
              ${!isOff ? `
                <div style="display:flex; justify-content:space-between;">
                  <span>Co-workers on duty:</span>
                  <span style="font-weight:700;">${shift.coworkers.join(', ')}</span>
                </div>
              ` : ''}
            </div>

            <strong style="font-size:0.8rem; color:var(--accent-primary); margin-top:4px;">Shift Memo</strong>
            <p style="margin:0; background:rgba(0,0,0,0.15); padding:10px; border-radius:var(--radius-md); line-height:1.4; color:var(--text-muted);">${shift.notes}</p>
          </div>
        `;
        showModal(detailsHtml);
      };
      card.addEventListener('click', handleOpenShift);
      lifecycle.onCleanup(() => card.removeEventListener('click', handleOpenShift));
    });

    // 2. Submit Swap request click
    const submitSwapBtn = container.querySelector('#btn-submit-swap');
    if (submitSwapBtn) {
      const handleSwap = () => {
        const swapMySelect = container.querySelector('#swap-my-shift');
        const swapPeerInput = container.querySelector('#swap-peer-name');
        const swapPeerShiftInput = container.querySelector('#swap-peer-shift');

        const myShiftId = parseInt(swapMySelect.value);
        const peerName = swapPeerInput.value.trim();
        const peerShift = swapPeerShiftInput.value.trim();

        if (isNaN(myShiftId)) {
          notificationStore.danger('Please select which of your shifts to trade.');
          return;
        }

        if (!peerName || !peerShift) {
          notificationStore.danger('Please fill out all peer trade shift details.');
          return;
        }

        const myShift = this.state.shifts.find(s => s.id === myShiftId);
        if (!myShift) return;

        // Enforce Shift Capacity Constraint (Max 10 workers allowed to work at a shift)
        if (myShift.coworkers && (myShift.coworkers.length + 1) >= 10) {
          notificationStore.danger('Cannot request swap: Shift has already reached the maximum limit of 10 workers.');
          return;
        }

        const newSwap = {
          id: this.state.swaps.length ? Math.max(...this.state.swaps.map(s => s.id)) + 1 : 1,
          myShiftDate: myShift.date,
          myShiftType: myShift.type,
          peerName,
          peerShiftDate: peerShift,
          status: 'Pending'
        };

        this.state.swaps.unshift(newSwap);
        notificationStore.success('Roster swap request submitted for supervisor approval.');

        // Clear forms
        swapMySelect.value = '';
        swapPeerInput.value = '';
        swapPeerShiftInput.value = '';

        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      submitSwapBtn.addEventListener('click', handleSwap);
      lifecycle.onCleanup(() => submitSwapBtn.removeEventListener('click', handleSwap));
    }

    // 3. Shift Preference Slot buttons listener
    const preferenceBtns = container.querySelectorAll('.btn-preference-slot');
    preferenceBtns.forEach(btn => {
      const handlePref = () => {
        const val = btn.getAttribute('data-val');
        this.state.preferences.preferredShift = val;
        
        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      btn.addEventListener('click', handlePref);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handlePref));
    });

    // 4. Weekend Availability toggle click
    const weekendBox = container.querySelector('#pref-weekend-box');
    if (weekendBox) {
      const handleWeekendToggle = () => {
        const nextVal = !this.state.preferences.availableWeekends;
        this.state.preferences.availableWeekends = nextVal;
        
        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      weekendBox.addEventListener('click', handleWeekendToggle);
      lifecycle.onCleanup(() => weekendBox.removeEventListener('click', handleWeekendToggle));
    }

    // 5. Update preferences button listener
    const savePrefBtn = container.querySelector('#btn-save-preferences');
    const prefHoursInput = container.querySelector('#pref-hours');
    if (savePrefBtn && prefHoursInput) {
      const handleSavePref = () => {
        const hours = parseInt(prefHoursInput.value);
        if (isNaN(hours) || hours < 10 || hours > 60) {
          notificationStore.danger('Please specify preferred hours between 10 and 60 per week.');
          return;
        }

        this.state.preferences.maxHoursPerWeek = hours;
        notificationStore.success('Shift availability preferences updated.');
        
        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      savePrefBtn.addEventListener('click', handleSavePref);
      lifecycle.onCleanup(() => savePrefBtn.removeEventListener('click', handleSavePref));
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderCalendarGrid(container) {
    const cardsGrid = container.querySelector('#schedule-cards-grid');
    if (!cardsGrid) return;

    cardsGrid.replaceChildren();

    this.state.shifts.forEach(s => {
      const isOff = s.type === 'Day Off';
      const div = document.createElement('div');
      div.className = `shift-card ${isOff ? 'day-off' : ''}`;
      div.setAttribute('data-id', String(s.id));

      let content = `
        <span class="shift-date-lbl">${s.date}</span>
        <div class="shift-type-lbl">${s.type}</div>
        <div class="shift-time-lbl">${s.time}</div>`;

      if (!isOff) {
        content += `
          <div class="shift-meta-line">
            Station: <span>${s.role}</span>
          </div>
          <div class="coworkers-row">
            ${s.coworkers.map(c => `<span class="coworker-badge">${c}</span>`).join('')}
          </div>`;
      } else {
        content += `
          <div class="shift-meta-line--dayoff">
            Enjoy your holiday
          </div>`;
      }

      div.innerHTML = content;
      cardsGrid.appendChild(div);
    });
  }

  _renderSwapLogs(container) {
    const tbody = container.querySelector('#swap-requests-tbody');
    const emptyTpl = container.querySelector('#swap-empty-row-tpl');
    const rowTpl = container.querySelector('#swap-row-tpl');

    if (!tbody) return;

    tbody.replaceChildren();

    if (this.state.swaps.length === 0) {
      if (emptyTpl) {
        tbody.appendChild(emptyTpl.content.cloneNode(true));
      }
      return;
    }

    this.state.swaps.forEach(sw => {
      if (!rowTpl) return;
      const clone = rowTpl.content.cloneNode(true);

      const myEl = clone.querySelector('.swap-my-cell');
      const peerEl = clone.querySelector('.swap-peer-cell');
      const targetEl = clone.querySelector('.swap-target-cell');
      const statusEl = clone.querySelector('.swap-status-cell');

      if (myEl) myEl.textContent = sw.myShiftDate;
      if (peerEl) peerEl.textContent = sw.peerName;
      if (targetEl) targetEl.textContent = sw.peerShiftDate;
      if (statusEl) {
        statusEl.textContent = sw.status;
        if (sw.status === 'Approved') {
          statusEl.className = 'swap-status-cell swap-status-cell--approved font-bold';
        }
      }

      tbody.appendChild(clone);
    });
  }

  _renderPreferences(container) {
    // 1. Slots Pills Row
    const prefSlotsRow = container.querySelector('#pref-slots-row');
    if (prefSlotsRow) {
      prefSlotsRow.replaceChildren();
      ['Morning', 'Mid-Day', 'Evening'].forEach(slot => {
        const isSel = this.state.preferences.preferredShift === slot;
        const btn = document.createElement('button');
        btn.type = 'button';
        btn.className = `btn-preference-slot ${isSel ? 'active' : ''}`;
        btn.setAttribute('data-val', slot);
        btn.textContent = slot;
        prefSlotsRow.appendChild(btn);
      });
    }

    // 2. Weekend Checkbox
    const nativeCheckbox = container.querySelector('#pref-weekend');
    const customBox = container.querySelector('#pref-weekend-box');

    if (nativeCheckbox) {
      nativeCheckbox.checked = this.state.preferences.availableWeekends;
    }

    if (customBox) {
      if (this.state.preferences.availableWeekends) {
        customBox.className = 'custom-checkbox-span checked';
        customBox.setAttribute('aria-checked', 'true');
        customBox.innerHTML = `
          <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="#000" stroke-width="3.5" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="20 6 9 17 4 12"></polyline>
          </svg>`;
      } else {
        customBox.className = 'custom-checkbox-span';
        customBox.setAttribute('aria-checked', 'false');
        customBox.replaceChildren();
      }
    }

    // 3. Target Hours Per Week Input
    const hoursInput = container.querySelector('#pref-hours');
    if (hoursInput) {
      hoursInput.value = String(this.state.preferences.maxHoursPerWeek);
    }
  }

  _loadCss() {
    const cssId = 'store-employee-schedule-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/schedule/schedule.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreEmployeeSchedule };
