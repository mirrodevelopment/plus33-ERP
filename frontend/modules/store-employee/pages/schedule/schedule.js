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
import { apiClient } from '../../../../api/client.js';

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

    // 2. Load dynamic dates and shifts from backend
    await this.loadScheduleData();

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

  formatDateYMD(date) {
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const d = String(date.getDate()).padStart(2, '0');
    return `${y}-${m}-${d}`;
  }

  async loadScheduleData() {
    try {
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      const todayStr = this.formatDateYMD(today);
      
      const res = await apiClient.get(`/api/v1/shift-assignments/my-schedule?startDate=${todayStr}`);
      const assignments = (res && res.success) ? res.data : [];

      const shiftsRes = await apiClient.get('/api/v1/shifts');
      this.systemShifts = (shiftsRes && shiftsRes.success) ? shiftsRes.data : [];

      const swapsRes = await apiClient.get('/api/v1/shift-swaps/my-requests');
      const swaps = (swapsRes && swapsRes.success) ? swapsRes.data : [];

      const shifts = [];
      const weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
      const monthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

      for (let i = 0; i < 7; i++) {
        const d = new Date(today);
        d.setDate(d.getDate() + i);
        const dStr = this.formatDateYMD(d);

        const dateLbl = `${weekdays[d.getDay()]}, ${d.getDate()} ${monthNames[d.getMonth()]}`;

        const assign = assignments.find(a => {
          const from = a.date;
          const to = a.effectiveTo || from;
          return dStr >= from && dStr <= to;
        });

        if (assign) {
          shifts.push({
            id: assign.id || (200 + i),
            date: dateLbl,
            dateYMD: dStr,
            type: assign.type,
            time: assign.time,
            role: assign.role || 'Operations',
            coworkers: [],
            notes: ''
          });
        } else {
          shifts.push({
            id: 200 + i,
            date: dateLbl,
            dateYMD: dStr,
            type: 'Day Off',
            time: 'Rest Day',
            role: '--',
            coworkers: [],
            notes: 'Weekly holiday.'
          });
        }
      }

      this.state = {
        selectedShiftId: null,
        shifts: shifts,
        swaps: swaps,
        preferences: {
          preferredShift: 'Morning',
          availableWeekends: false,
          maxHoursPerWeek: 40
        }
      };
      
      this.saveState();
    } catch (e) {
      logger.error('StoreEmployeeSchedule', 'Error loading schedule data:', e);
      this.initDefaultState();
    }
  }

  initDefaultState() {
    this.state = {
      selectedShiftId: null,
      shifts: [],
      swaps: [],
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

  getWeekRangeLabel(startDate) {
    const endDate = new Date(startDate);
    endDate.setDate(endDate.getDate() + 6);
    
    const monthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    
    const formatDay = (d) => {
      return d.getDate() < 10 ? '0' + d.getDate() : d.getDate();
    };

    return `${formatDay(startDate)} ${monthNames[startDate.getMonth()]} - ${formatDay(endDate)} ${monthNames[endDate.getMonth()]} ${endDate.getFullYear()}`;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  render(container) {
    // 1. Sync header text
    const nameEl = container.querySelector('#lbl-employee-name');
    if (nameEl) nameEl.textContent = this.profile.name || 'Neha Sharma';

    const storeEl = container.querySelector('#lbl-store-name');
    if (storeEl) storeEl.textContent = this.profile.store || 'Green Park Café, City Center';

    const weekRangeEl = container.querySelector('#lbl-week-range');
    if (weekRangeEl) {
      const today = new Date();
      weekRangeEl.textContent = this.getWeekRangeLabel(today);
    }

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

      const today = new Date();
      today.setHours(0, 0, 0, 0);
      const todayStr = this.formatDateYMD(today);
      const maxDate = new Date(today);
      maxDate.setDate(maxDate.getDate() + 7);
      const maxDateStr = this.formatDateYMD(maxDate);

      this.state.shifts.filter(s => s.type !== 'Day Off' && s.dateYMD > todayStr && s.dateYMD <= maxDateStr).forEach(s => {
        const opt = document.createElement('option');
        opt.value = `${s.dateYMD}_${s.id}`;
        opt.textContent = `${s.date} — ${s.type} (${s.time})`;
        opt.setAttribute('data-shift-type', s.type);
        opt.setAttribute('data-date-ymd', s.dateYMD);
        myShiftsSelect.appendChild(opt);
      });
    }

    // 3.5 Preferred shift group: hide until a shift is selected
    const preferredShiftGroup = container.querySelector('#preferred-shift-group');
    const preferredShiftSelect = container.querySelector('#swap-peer-shift');
    const preferredShiftHint = container.querySelector('#preferred-shift-hint');
    const preferredDateGroup = container.querySelector('#preferred-date-group');
    const preferredDateInput = container.querySelector('#swap-preferred-date');
    if (preferredShiftGroup) {
      preferredShiftGroup.style.display = 'none';
    }
    if (preferredDateGroup) {
      preferredDateGroup.style.display = 'none';
    }
    if (preferredShiftSelect) {
      preferredShiftSelect.replaceChildren();
      const defaultOpt = document.createElement('option');
      defaultOpt.value = '';
      defaultOpt.textContent = '-- Choose preferred shift --';
      preferredShiftSelect.appendChild(defaultOpt);
    }
    // Set preferred date min/max to next 7 days
    if (preferredDateInput) {
      const todayD = new Date();
      todayD.setHours(0, 0, 0, 0);
      const minDate = new Date(todayD);
      minDate.setDate(minDate.getDate() + 1);
      const maxDate7 = new Date(todayD);
      maxDate7.setDate(maxDate7.getDate() + 7);
      preferredDateInput.min = this.formatDateYMD(minDate);
      preferredDateInput.max = this.formatDateYMD(maxDate7);
      preferredDateInput.value = '';
    }

    // 4. Render trades swap log table rows
    this._renderSwapLogs(container);


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
      const handleSwap = async () => {
        const swapMySelect = container.querySelector('#swap-my-shift');
        const swapPeerShiftSelect = container.querySelector('#swap-peer-shift');
        const swapPreferredDateInput = container.querySelector('#swap-preferred-date');

        const swapMyValue = swapMySelect.value;
        if (!swapMyValue) {
          notificationStore.danger('Please select which of your shifts to trade.');
          return;
        }
        const [selectedDate, selectedShiftIdStr] = swapMyValue.split('_');
        const myShiftId = parseInt(selectedShiftIdStr);
        const preferredShiftId = parseInt(swapPeerShiftSelect.value);
        const preferredDate = swapPreferredDateInput ? swapPreferredDateInput.value : '';

        if (isNaN(myShiftId)) {
          notificationStore.danger('Please select which of your shifts to trade.');
          return;
        }
        if (isNaN(preferredShiftId)) {
          notificationStore.danger('Please select your preferred shift type.');
          return;
        }
        if (!preferredDate) {
          notificationStore.danger('Please select your preferred date for the new shift.');
          return;
        }

        const myShift = this.state.shifts.find(s => s.dateYMD === selectedDate && s.id === myShiftId);
        if (!myShift) return;

        const payload = {
          shiftDate: myShift.dateYMD,
          currentShiftId: myShiftId,
          preferredShiftId: preferredShiftId,
          preferredDate: preferredDate
        };

        try {
          const res = await apiClient.post('/api/v1/shift-swaps', payload);
          if (res && res.success) {
            notificationStore.success('Roster swap request submitted for supervisor approval.');
            swapMySelect.value = '';
            swapPeerShiftSelect.value = '';
            if (swapPreferredDateInput) swapPreferredDateInput.value = '';
            const preferredShiftGroup = container.querySelector('#preferred-shift-group');
            const preferredDateGroup = container.querySelector('#preferred-date-group');
            if (preferredShiftGroup) preferredShiftGroup.style.display = 'none';
            if (preferredDateGroup) preferredDateGroup.style.display = 'none';
            await this.loadScheduleData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to submit swap request.');
          }
        } catch (err) {
          logger.error('StoreEmployeeSchedule', 'Error submitting swap request:', err);
          notificationStore.danger(err.message || 'Error submitting swap request.');
        }
      };
      submitSwapBtn.addEventListener('click', handleSwap);
      lifecycle.onCleanup(() => submitSwapBtn.removeEventListener('click', handleSwap));
    }

    // 2.5 My shift selection change → update preferred shift options
    const swapMyShiftSelect = container.querySelector('#swap-my-shift');
    const preferredShiftGroup = container.querySelector('#preferred-shift-group');
    const preferredShiftSelect = container.querySelector('#swap-peer-shift');
    const preferredShiftHint = container.querySelector('#preferred-shift-hint');
    const preferredDateGroup = container.querySelector('#preferred-date-group');
    const preferredDateInput = container.querySelector('#swap-preferred-date');

    if (swapMyShiftSelect) {
      const handleMyShiftChange = () => {
        const swapMyValue = swapMyShiftSelect.value;
        if (!swapMyValue || !preferredShiftGroup || !preferredShiftSelect) return;
        const [selectedDate, selectedShiftIdStr] = swapMyValue.split('_');
        const myShiftId = parseInt(selectedShiftIdStr);

        const myShift = this.state.shifts.find(s => s.dateYMD === selectedDate && s.id === myShiftId);
        if (!myShift) {
          preferredShiftGroup.style.display = 'none';
          if (preferredDateGroup) preferredDateGroup.style.display = 'none';
          return;
        }

        // Populate preferred shifts (all active shifts except current shift type)
        preferredShiftSelect.replaceChildren();
        const defaultOpt = document.createElement('option');
        defaultOpt.value = '';
        defaultOpt.textContent = '-- Choose preferred shift --';
        preferredShiftSelect.appendChild(defaultOpt);

        (this.systemShifts || []).filter(sh => sh.name !== myShift.type).forEach(sh => {
          const opt = document.createElement('option');
          opt.value = String(sh.id);
          opt.textContent = `${sh.name} (${sh.startTime} – ${sh.endTime})`;
          preferredShiftSelect.appendChild(opt);
        });

        // Show hint for preferred shift
        if (preferredShiftHint) {
          preferredShiftHint.textContent = `Requesting a shift change for ${myShift.date} (${myShift.type})`;
        }

        // Reset and hide preferred date until shift is chosen
        if (preferredDateGroup) preferredDateGroup.style.display = 'none';
        if (preferredDateInput) preferredDateInput.value = '';

        preferredShiftGroup.style.display = 'block';
      };
      swapMyShiftSelect.addEventListener('change', handleMyShiftChange);
      lifecycle.onCleanup(() => swapMyShiftSelect.removeEventListener('change', handleMyShiftChange));
    }

    // 2.6 When preferred shift is selected → reveal preferred date picker
    const preferredShiftSelectEl = container.querySelector('#swap-peer-shift');
    const preferredDateGroupEl = container.querySelector('#preferred-date-group');
    const preferredDateInputEl = container.querySelector('#swap-preferred-date');
    if (preferredShiftSelectEl) {
      const handleShiftSelect = () => {
        if (preferredShiftSelectEl.value && preferredDateGroupEl) {
          preferredDateGroupEl.style.display = 'block';
        } else if (preferredDateGroupEl) {
          preferredDateGroupEl.style.display = 'none';
          if (preferredDateInputEl) preferredDateInputEl.value = '';
        }
      };
      preferredShiftSelectEl.addEventListener('change', handleShiftSelect);
      lifecycle.onCleanup(() => preferredShiftSelectEl.removeEventListener('change', handleShiftSelect));
    }



    // 6. Escalated Swap click listeners
    const escalateBtns = container.querySelectorAll('.btn-escalate-swap');
    escalateBtns.forEach(btn => {
      const handleEscalate = async () => {
        const id = btn.getAttribute('data-id');
        try {
          const res = await apiClient.post(`/api/v1/shift-swaps/${id}/escalate`);
          if (res && res.success) {
            notificationStore.success('Roster swap request escalated successfully!');
            await this.loadScheduleData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to escalate request.');
          }
        } catch (err) {
          logger.error('StoreEmployeeSchedule', 'Error escalating request:', err);
          notificationStore.danger('Error escalating request.');
        }
      };
      btn.addEventListener('click', handleEscalate);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleEscalate));
    });
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
      const targetEl = clone.querySelector('.swap-target-cell');
      const statusEl = clone.querySelector('.swap-status-cell');

      if (myEl) myEl.textContent = `${sw.myShiftDate} (${sw.myShiftType || sw.currentShiftName || ''})`;
      if (targetEl) targetEl.textContent = sw.preferredShiftName || sw.peerShiftDate || '—';
      if (statusEl) {
        statusEl.replaceChildren();
        if (sw.status === 'Approved' || sw.status === 'APPROVED') {
          statusEl.className = 'swap-status-cell swap-status-cell--approved font-bold';
          statusEl.textContent = '✓ Approved';
        } else if (sw.status === 'REJECTED') {
          statusEl.className = 'swap-status-cell font-bold';
          statusEl.style.color = '#ff6b6b';
          statusEl.innerHTML = `
            <span>✗ Rejected: ${sw.rejectionReason || 'No reason'}</span>
            <button type="button" class="btn btn-warning btn-escalate-swap" data-id="${sw.id}" style="margin-left: 8px; font-size: 0.65rem; padding: 2px 6px; border-radius: 4px; cursor: pointer; background: var(--accent-warning); color: #000; border: none;">Escalate to Admin</button>
          `;
        } else if (sw.status === 'REJECTED_BY_ADMIN') {
          statusEl.className = 'swap-status-cell font-bold';
          statusEl.style.color = '#ff6b6b';
          statusEl.textContent = `✗ Admin Rejected: ${sw.adminRejectionReason || 'No reason'}`;
        } else if (sw.status === 'ESCALATED') {
          statusEl.className = 'swap-status-cell font-bold';
          statusEl.style.color = '#ff9800';
          statusEl.textContent = '⬆ Escalated to Admin';
        } else if (sw.status === 'PENDING') {
          statusEl.className = 'swap-status-cell font-bold';
          statusEl.style.color = '#ffc107';
          statusEl.textContent = '⏳ Pending Supervisor Review';
        } else {
          statusEl.className = 'swap-status-cell font-bold';
          statusEl.style.color = '#ffc107';
          statusEl.textContent = sw.status || 'Pending';
        }
      }

      tbody.appendChild(clone);
    });
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
