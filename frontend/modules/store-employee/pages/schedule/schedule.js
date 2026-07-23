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
    this.systemShifts = [];
    this.currentAwayPass = null;

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

    // 5. Start away pass timer interval
    this.startAwayPassTimer(container, lifecycle);
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
      
      const [assignRes, shiftsRes, swapsRes, otRes, attendanceRes, awayRes] = await Promise.all([
        apiClient.get(`/api/v1/shift-assignments/my-schedule?startDate=${todayStr}`),
        apiClient.get('/api/v1/shifts'),
        apiClient.get('/api/v1/shift-swaps/my-requests'),
        apiClient.get('/api/v1/overtime-requests/my').catch(() => null),
        apiClient.get('/api/v1/attendance/today').catch(() => null),
        apiClient.get('/api/v1/away-permission/my').catch(() => null)
      ]);

      const assignments = (assignRes && assignRes.success) ? assignRes.data : [];
      this.systemShifts = (shiftsRes && shiftsRes.success) ? shiftsRes.data : [];
      const swaps = (swapsRes && swapsRes.success) ? swapsRes.data : [];
      const overtimeRequests = (otRes && otRes.success) ? otRes.data : [];

      this.clockedIn = attendanceRes?.success && attendanceRes.data?.clockedIn;
      this.shiftStartTime = attendanceRes?.success ? attendanceRes.data?.shiftStartTime : '';
      this.shiftEndTime = attendanceRes?.success ? attendanceRes.data?.shiftEndTime : '';
      if (this.clockedIn && awayRes?.success && awayRes.data) {
        const list = awayRes.data;
        list.sort((a, b) => b.id - a.id);
        this.currentAwayPass = list[0] || null;
      } else {
        this.currentAwayPass = null;
      }

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
        overtimeRequests: overtimeRequests,
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
      overtimeRequests: [],
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

    // 5. Populate overtime shift options
    const otShiftSelect = container.querySelector('#ot-req-shift');
    if (otShiftSelect) {
      otShiftSelect.replaceChildren();
      const defaultOpt = document.createElement('option');
      defaultOpt.value = '';
      defaultOpt.textContent = '-- Choose shift type --';
      otShiftSelect.appendChild(defaultOpt);

      this.systemShifts.filter(s => s.active).forEach(s => {
        const opt = document.createElement('option');
        opt.value = s.id;
        opt.textContent = `${s.name} (${s.startTime} - ${s.endTime})`;
        otShiftSelect.appendChild(opt);
      });
    }

    // Set overtime date min/max to next 7 days
    const otDateInput = container.querySelector('#ot-req-date');
    if (otDateInput) {
      const todayD = new Date();
      todayD.setHours(0, 0, 0, 0);
      const minDate = new Date(todayD);
      minDate.setDate(minDate.getDate() + 1);
      const maxDate7 = new Date(todayD);
      maxDate7.setDate(maxDate7.getDate() + 7);
      otDateInput.min = this.formatDateYMD(minDate);
      otDateInput.max = this.formatDateYMD(maxDate7);
      otDateInput.value = '';
    }

    // 6. Render overtime request log table rows
    this._renderOvertimeLogs(container);
    this._renderAwayPassCard(container);
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

    // 0. Policy Card Expand / Collapse Toggle
    const policyToggle = container.querySelector('#policy-card-toggle');
    const policyBody   = container.querySelector('#policy-card-body');
    if (policyToggle && policyBody) {
      const togglePolicy = () => {
        const isOpen = policyToggle.getAttribute('aria-expanded') === 'true';
        policyToggle.setAttribute('aria-expanded', String(!isOpen));
        policyBody.setAttribute('aria-hidden', String(isOpen));
      };
      policyToggle.addEventListener('click', togglePolicy);
      policyToggle.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); togglePolicy(); }
      });
      lifecycle.onCleanup(() => {
        policyToggle.removeEventListener('click', togglePolicy);
      });
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

    // 7. Submit Overtime Request click
    const submitOtBtn = container.querySelector('#btn-submit-ot');
    if (submitOtBtn) {
      const handleOtSubmit = async () => {
        const otShiftSelect = container.querySelector('#ot-req-shift');
        const otDateInput = container.querySelector('#ot-req-date');
        const otReasonInput = container.querySelector('#ot-req-reason');

        const shiftId = parseInt(otShiftSelect?.value);
        const requestedDate = otDateInput?.value;
        const reason = otReasonInput?.value?.trim();

        if (isNaN(shiftId)) {
          notificationStore.danger('Please select a shift type.');
          return;
        }
        if (!requestedDate) {
          notificationStore.danger('Please select a date.');
          return;
        }
        if (!reason) {
          notificationStore.danger('Please provide a reason for the overtime request.');
          return;
        }

        const payload = {
          shiftId: shiftId,
          requestedDate: requestedDate,
          reason: reason
        };

        try {
          const res = await apiClient.post('/api/v1/overtime-requests', payload);
          if (res && res.success) {
            notificationStore.success('Overtime request submitted for supervisor approval.');
            if (otShiftSelect) otShiftSelect.value = '';
            if (otDateInput) otDateInput.value = '';
            if (otReasonInput) otReasonInput.value = '';

            await this.loadScheduleData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to submit overtime request.');
          }
        } catch (err) {
          logger.error('StoreEmployeeSchedule', 'Error submitting overtime request:', err);
          notificationStore.danger(err.message || 'Error submitting overtime request.');
        }
      };
      submitOtBtn.addEventListener('click', handleOtSubmit);
      lifecycle.onCleanup(() => submitOtBtn.removeEventListener('click', handleOtSubmit));
    }

    // Away Pass Manager event handlers
    const btnSubmitAway = container.querySelector('#btn-submit-away-pass');
    const selectAwayReason = container.querySelector('#away-pass-reason-select');
    const customReasonGroup = container.querySelector('#away-pass-custom-reason-group');
    const inputAwayReason = container.querySelector('#away-pass-reason');
    const selectAwayDuration = container.querySelector('#away-pass-duration-select');

    if (selectAwayReason) {
      const handleReasonSelectChange = () => {
        if (customReasonGroup) {
          customReasonGroup.style.display = selectAwayReason.value === 'custom' ? 'block' : 'none';
        }
      };
      selectAwayReason.addEventListener('change', handleReasonSelectChange);
      lifecycle.onCleanup(() => selectAwayReason.removeEventListener('change', handleReasonSelectChange));
    }
    
    if (btnSubmitAway) {
      const handleSubmitAway = async () => {
        let rawReason = '';
        if (selectAwayReason?.value === 'custom') {
          rawReason = inputAwayReason?.value.trim() || '';
          if (!rawReason) {
            notificationStore.danger('Please provide custom reason details.');
            return;
          }
        } else {
          rawReason = selectAwayReason?.value || 'No reason';
        }
        
        const startVal = container.querySelector('#away-pass-start-time-select')?.value || 'now';
        const mins = selectAwayDuration?.value || '30';
        const startLabel = startVal === 'now' ? 'Now' : startVal;
        const reason = `${rawReason} (Start: ${startLabel}) (Requested Duration: ${mins} mins)`;
        
        btnSubmitAway.disabled = true;
        try {
          const res = await apiClient.post('/api/v1/away-permission/request', { reason });
          if (res?.success) {
            notificationStore.success('Away pass request submitted successfully.');
            if (inputAwayReason) inputAwayReason.value = '';
            await this.loadScheduleData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to submit away pass request.');
            btnSubmitAway.disabled = false;
          }
        } catch (e) {
          notificationStore.danger('Failed to request away pass: ' + e.message);
          btnSubmitAway.disabled = false;
        }
      };
      btnSubmitAway.addEventListener('click', handleSubmitAway);
      lifecycle.onCleanup(() => btnSubmitAway.removeEventListener('click', handleSubmitAway));
    }

    const btnExtendAway = container.querySelector('#btn-extend-away-pass');
    if (btnExtendAway) {
      const handleExtendAway = async () => {
        const pass = this.currentAwayPass;
        if (!pass) return;

        const minsInput = prompt('How many additional minutes do you need? (e.g., 15, 30, 45, 60):', '30');
        if (minsInput === null) return; // User cancelled

        const mins = parseInt(minsInput.trim(), 10);
        if (isNaN(mins) || mins <= 0) {
          notificationStore.danger('Please enter a valid number of minutes.');
          return;
        }

        const reasonInput = prompt('Reason for extension (optional):');
        if (reasonInput === null) return; // User cancelled

        let extensionReason = `Requested ${mins} extra minutes.`;
        if (reasonInput.trim()) {
          extensionReason += ` Reason: ${reasonInput.trim()}`;
        }
        
        btnExtendAway.disabled = true;
        try {
          const res = await apiClient.post(`/api/v1/away-permission/${pass.id}/extend`, {
            reason: extensionReason
          });
          if (res?.success) {
            notificationStore.success('Extension request sent to your supervisor.');
            await this.loadScheduleData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to request extension.');
            btnExtendAway.disabled = false;
          }
        } catch (e) {
          notificationStore.danger('Failed to request extension: ' + e.message);
          btnExtendAway.disabled = false;
        }
      };
      btnExtendAway.addEventListener('click', handleExtendAway);
      lifecycle.onCleanup(() => btnExtendAway.removeEventListener('click', handleExtendAway));
    }

    const btnReturnAway = container.querySelector('#btn-return-away-pass');
    if (btnReturnAway) {
      const handleReturnAway = async () => {
        const pass = this.currentAwayPass;
        if (!pass) return;
        
        btnReturnAway.disabled = true;
        try {
          const res = await apiClient.post(`/api/v1/away-permission/${pass.id}/return`, {});
          if (res?.success) {
            notificationStore.success('Away pass ended successfully. Welcome back!');
            await this.loadScheduleData();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } else {
            notificationStore.danger(res?.message || 'Failed to end away pass.');
            btnReturnAway.disabled = false;
          }
        } catch (e) {
          notificationStore.danger('Failed to end away pass: ' + e.message);
          btnReturnAway.disabled = false;
        }
      };
      btnReturnAway.addEventListener('click', handleReturnAway);
      lifecycle.onCleanup(() => btnReturnAway.removeEventListener('click', handleReturnAway));
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

  _renderOvertimeLogs(container) {
    const tbody = container.querySelector('#ot-requests-tbody');
    const emptyTpl = container.querySelector('#ot-empty-row-tpl');
    const rowTpl = container.querySelector('#ot-row-tpl');

    if (!tbody) return;

    tbody.replaceChildren();

    const otRequests = this.state.overtimeRequests || [];
    if (otRequests.length === 0) {
      if (emptyTpl) {
        tbody.appendChild(emptyTpl.content.cloneNode(true));
      }
      return;
    }

    otRequests.forEach(ot => {
      if (!rowTpl) return;
      const clone = rowTpl.content.cloneNode(true);

      const dateEl = clone.querySelector('.ot-date-cell');
      const shiftEl = clone.querySelector('.ot-shift-cell');
      const reasonEl = clone.querySelector('.ot-reason-cell');
      const statusEl = clone.querySelector('.ot-status-cell');

      if (dateEl) dateEl.textContent = ot.requestedDateDisplay || ot.requestedDate || '';
      if (shiftEl) shiftEl.textContent = ot.shiftName || '';
      if (reasonEl) {
        reasonEl.textContent = ot.reason || '—';
        reasonEl.style.fontSize = '0.7rem';
        reasonEl.style.color = 'var(--text-muted)';
      }
      if (statusEl) {
        statusEl.replaceChildren();
        if (ot.status === 'APPROVED' || ot.status === 'Approved') {
          statusEl.className = 'ot-status-cell font-bold';
          statusEl.style.color = '#4caf50';
          statusEl.textContent = '✓ Approved';
        } else if (ot.status === 'DENIED' || ot.status === 'Denied') {
          statusEl.className = 'ot-status-cell font-bold';
          statusEl.style.color = '#ff6b6b';
          statusEl.textContent = '✗ Denied';
        } else if (ot.status === 'PENDING' || ot.status === 'Pending') {
          statusEl.className = 'ot-status-cell font-bold';
          statusEl.style.color = '#ffc107';
          statusEl.textContent = '⏳ Pending Review';
        } else {
          statusEl.className = 'ot-status-cell font-bold';
          statusEl.style.color = '#ffc107';
          statusEl.textContent = ot.status || 'Pending';
        }
      }

      tbody.appendChild(clone);
    });
  }

  _renderAwayPassCard(container) {
    const card = container.querySelector('#away-pass-card');
    if (!card) return;

    const notClocked = container.querySelector('#away-pass-not-clocked');
    const containerEl = container.querySelector('#away-pass-container');
    const activeInfo = container.querySelector('#away-pass-active-info');
    const statusText = container.querySelector('#away-pass-status-text');
    const statusDetail = container.querySelector('#away-pass-status-detail');
    const timerBlock = container.querySelector('#away-pass-timer-block');
    const requestForm = container.querySelector('#away-pass-request-form');
    const btnExtend = container.querySelector('#btn-extend-away-pass');

    if (!this.clockedIn) {
      if (notClocked) notClocked.style.display = 'block';
      if (containerEl) containerEl.style.display = 'none';
      return;
    }

    if (notClocked) notClocked.style.display = 'none';
    if (containerEl) containerEl.style.display = 'block';

    const btnReturn = container.querySelector('#btn-return-away-pass');
    const pass = this.currentAwayPass;
    const isResolved = !pass || ['RETURNED', 'DENIED', 'EXPIRED'].includes(pass.status);

    if (isResolved) {
      if (requestForm) requestForm.style.display = 'block';
      if (activeInfo) activeInfo.style.display = 'none';
      if (btnExtend) btnExtend.style.display = 'none';
      if (btnReturn) btnReturn.style.display = 'none';

      // Populate Start Time dropdown with Now and Shift timings
      const startSelect = container.querySelector('#away-pass-start-time-select');
      if (startSelect) {
        startSelect.innerHTML = '<option value="now" selected>Now</option>';
        const startTimeStr = this.shiftStartTime || '';
        const endTimeStr = this.shiftEndTime || '';
        if (startTimeStr && endTimeStr) {
          try {
            const parseTime = (str) => {
              const parts = str.split(':');
              return {
                hours: parseInt(parts[0], 10),
                minutes: parseInt(parts[1], 10)
              };
            };
            const start = parseTime(startTimeStr);
            const end = parseTime(endTimeStr);
            let startMins = start.hours * 60 + start.minutes;
            let endMins = end.hours * 60 + end.minutes;
            if (endMins < startMins) {
              endMins += 24 * 60;
            }
            for (let m = startMins; m <= endMins; m += 30) {
              const displayHour = Math.floor(m / 60) % 24;
              const displayMin = m % 60;
              const timeVal = `${String(displayHour).padStart(2, '0')}:${String(displayMin).padStart(2, '0')}`;
              const ampm = displayHour >= 12 ? 'PM' : 'AM';
              const hr12 = displayHour % 12 || 12;
              const timeLabel = `${hr12}:${String(displayMin).padStart(2, '0')} ${ampm}`;
              const opt = document.createElement('option');
              opt.value = timeVal;
              opt.textContent = timeLabel;
              startSelect.appendChild(opt);
            }
          } catch (e) {
            logger.error('StoreEmployeeSchedule', 'Failed to populate shift times dropdown', e);
          }
        }
      }
    } else {
      if (requestForm) requestForm.style.display = 'none';
      if (activeInfo) activeInfo.style.display = 'flex';

      const status = pass.status;
      if (status === 'PENDING') {
        if (statusText) {
          statusText.textContent = '⏳ Pending Approval';
          statusText.style.color = '#ff9800';
        }
        if (statusDetail) statusDetail.textContent = `Reason: ${pass.reason || '—'}`;
        if (timerBlock) timerBlock.style.display = 'none';
        if (btnExtend) btnExtend.style.display = 'none';
        if (btnReturn) btnReturn.style.display = 'none';
      } else if (status === 'APPROVED') {
        if (statusText) {
          statusText.textContent = '✓ Approved';
          statusText.style.color = '#4caf50';
        }
        const timeStr = pass.approvedUntil ? new Date(pass.approvedUntil).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '--:--';
        if (statusDetail) statusDetail.textContent = `Approved until ${timeStr}`;
        if (timerBlock) timerBlock.style.display = 'flex';
        if (btnExtend) {
          btnExtend.style.display = 'block';
          btnExtend.disabled = false;
        }
        if (btnReturn) {
          btnReturn.style.display = 'block';
          btnReturn.disabled = false;
        }
      } else if (status === 'EXTENSION_REQUESTED') {
        if (statusText) {
          statusText.textContent = '🔁 Extension Requested';
          statusText.style.color = '#ffc107';
        }
        if (statusDetail) statusDetail.textContent = 'Waiting for supervisor approval';
        if (timerBlock) timerBlock.style.display = 'flex';
        if (btnExtend) {
          btnExtend.style.display = 'block';
          btnExtend.disabled = true;
        }
        if (btnReturn) {
          btnReturn.style.display = 'block';
          btnReturn.disabled = false;
        }
      }
    }
    if (window.lucide) window.lucide.createIcons();
  }

  startAwayPassTimer(container, lifecycle) {
    const self = this;
    const updateAwayTimer = () => {
      self.updateAwayPassTimer(container);
    };
    updateAwayTimer();
    const interval = setInterval(updateAwayTimer, 1000);
    lifecycle.onCleanup(() => clearInterval(interval));
  }

  updateAwayPassTimer(container) {
    const timerBlock = container.querySelector('#away-pass-timer-block');
    const timerVal = container.querySelector('#away-pass-timer-value');
    if (!timerBlock || !timerVal) return;

    const pass = this.currentAwayPass;
    if (!pass || !this.clockedIn || (pass.status !== 'APPROVED' && pass.status !== 'EXTENSION_REQUESTED')) {
      timerBlock.style.display = 'none';
      return;
    }

    const deadlineStr = pass.graceUntil || pass.approvedUntil;
    if (!deadlineStr) {
      timerBlock.style.display = 'none';
      return;
    }

    const now = new Date();
    const deadline = new Date(deadlineStr);
    const diffMs = deadline - now;

    if (diffMs > 0) {
      timerBlock.style.display = 'flex';
      timerVal.textContent = this.formatDuration(diffMs);
      
      const approvedUntil = new Date(pass.approvedUntil);
      if (now > approvedUntil) {
        timerVal.style.color = '#ff6b6b';
      } else {
        timerVal.style.color = 'var(--accent-warning, #ff9800)';
      }
    } else {
      timerBlock.style.display = 'flex';
      timerVal.textContent = '00:00:00';
      timerVal.style.color = '#ff6b6b';
      if (!this._lastAwayReload || (now - this._lastAwayReload) > 10000) {
        this._lastAwayReload = now;
        this.loadScheduleData().then(() => this.render(container));
      }
    }
  }

  formatDuration(ms) {
    const totalSecs = Math.floor(ms / 1000);
    const hours = Math.floor(totalSecs / 3600);
    const minutes = Math.floor((totalSecs % 3600) / 60);
    const seconds = totalSecs % 60;
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
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
