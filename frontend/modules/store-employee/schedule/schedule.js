/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module
 * File              : schedule.js
 * Path              : frontend/modules/store-employee/schedule/schedule.js
 * Purpose           : Barista shift rosters, schedule swap forms, and shift preference management
 * Version           : 1.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { userStore } from '../../../store/userStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';

export default class StoreEmployeeSchedule {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};

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

  async mount(container, lifecycle) {
    logger.info('StoreEmployeeSchedule', 'Mounting Barista Shift Roster page...');
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  render(container) {
    container.innerHTML = `
      <style>
        .schedule-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
          gap: var(--spacing-sm);
        }
        .shift-card {
          background: rgba(0,0,0,0.15);
          border: 1px solid var(--border-color);
          border-radius: var(--radius-md);
          padding: var(--spacing-md);
          text-align: left;
          cursor: pointer;
          transition: all 0.25s ease-out;
        }
        .shift-card:hover {
          transform: translateY(-2px);
          border-color: var(--accent-primary);
          box-shadow: 0 4px 12px rgba(201,164,106,0.1);
        }
        .shift-card.day-off {
          opacity: 0.65;
          background: rgba(255,255,255,0.02);
        }
        .coworker-badge {
          display: inline-block;
          font-size: 0.55rem;
          font-weight: 700;
          padding: 2px 6px;
          border-radius: 4px;
          background: rgba(255,255,255,0.05);
          margin-right: 4px;
          color: var(--text-muted);
        }
      </style>

      <div class="workspace-container animate-fade-in" style="padding: var(--spacing-lg); display: flex; flex-direction: column; gap: var(--spacing-lg); max-width: 1400px; margin: 0 auto;">
        
        <!-- Header Ribbon -->
        <div class="card glass flex justify-between align-center flex-wrap" style="padding: var(--spacing-md) var(--spacing-lg); border-radius: var(--radius-lg); background: var(--bg-card); border: 1px solid var(--border-color); gap: var(--spacing-md); text-align: left;">
          <div>
            <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.5rem; color: var(--text-primary); margin: 0; display: flex; align-items: center; gap: 8px;">
              My Barista Roster
            </h2>
            <p style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin: 2px 0 0 0;">
              Employee: <span style="color: var(--accent-primary); font-weight: 700;">${this.profile.name || 'Neha Sharma'}</span> &nbsp;·&nbsp; Location: <span style="color: var(--accent-primary); font-weight: 700;">Green Park Café</span>
            </p>
          </div>
          <div style="background: rgba(130,163,125,0.12); border: 1px solid rgba(130,163,125,0.3); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.72rem; font-weight: 600; color: var(--status-success); display: flex; align-items: center; gap: 6px;">
            <span style="width:6px; height:6px; border-radius:50%; background: var(--status-success); display:inline-block;"></span> Schedule Synced
          </div>
        </div>

        <!-- Roster Capacity Info Ribbon -->
        <div style="background: rgba(201,164,106,0.06); border: 1px solid rgba(201,164,106,0.15); border-radius: var(--radius-md); padding: 8px 12px; font-size: 0.72rem; color: var(--accent-primary); font-weight: 700; display: flex; align-items: center; gap: 8px; text-align: left;">
          <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" style="flex-shrink:0;"><circle cx="12" cy="12" r="10"/><line x1="12" x2="12" y1="8" y2="12"/><line x1="12" x2="12.01" y1="16" y2="16"/></svg>
          <span>Shift Capacity Rules: Maximum store capacity of 30 employees. Maximum 10 employees allowed per active shift.</span>
        </div>

        <!-- Weekly Shift Calendar Grid -->
        <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
          <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs);">
            <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Shift Calendar: Current Week</h3>
            <span style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600;">06 July - 12 July 2026</span>
          </div>

          <div class="schedule-grid">
            ${this.state.shifts.map(s => {
              const isOff = s.type === 'Day Off';
              return `
                <div class="shift-card ${isOff ? 'day-off' : ''}" data-id="${s.id}">
                  <span style="font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase;">${s.date}</span>
                  <div style="font-weight: 800; font-size: 0.85rem; color: ${isOff ? 'var(--text-muted)' : 'var(--text-primary)'}; margin-top: 6px;">${s.type}</div>
                  <div style="font-size: 0.65rem; color: ${isOff ? 'rgba(255,255,255,0.2)' : 'var(--accent-primary)'}; font-weight: 700; margin-top: 2px;">${s.time}</div>
                  
                  ${!isOff ? `
                    <div style="font-size: 0.58rem; color: var(--text-muted); font-weight: 600; margin-top: var(--spacing-sm); border-top: 1px solid rgba(255,255,255,0.03); padding-top: 6px;">
                      Station: <span style="color:var(--text-primary);">${s.role}</span>
                    </div>
                    <div style="margin-top: 6px;">
                      ${s.coworkers.map(c => `<span class="coworker-badge">${c}</span>`).join('')}
                    </div>
                  ` : `
                    <div style="font-size: 0.58rem; color: rgba(255,255,255,0.15); font-style: italic; margin-top: var(--spacing-sm); border-top: 1px solid rgba(255,255,255,0.03); padding-top: 6px;">
                      Enjoy your holiday
                    </div>
                  `}
                </div>
              `;
            }).join('')}
          </div>
        </div>

        <!-- Main Roster Operations row -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: var(--spacing-lg); width: 100%;">
          
          <!-- Column Left: Shift Swap Trade Station -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Request Shift Swap</h3>
              <i data-lucide="refresh-cw" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
            </div>
            
            <div style="display: flex; flex-direction: column; gap: var(--spacing-sm); font-size: 0.76rem;">
              <div style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Select My Shift</label>
                <select id="swap-my-shift" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none;">
                  <option value="">-- Choose one of your shifts --</option>
                  ${this.state.shifts.filter(s => s.type !== 'Day Off').map(s => `
                    <option value="${s.id}">${s.date} - ${s.type}</option>
                  `).join('')}
                </select>
              </div>

              <div style="display: grid; grid-template-columns: 1fr 1fr; gap: var(--spacing-sm);">
                <div style="display: flex; flex-direction: column; gap: 4px;">
                  <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Peer Barista Name</label>
                  <input type="text" id="swap-peer-name" placeholder="e.g. Rohan Sharma" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none;">
                </div>
                <div style="display: flex; flex-direction: column; gap: 4px;">
                  <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Peer Shift Details</label>
                  <input type="text" id="swap-peer-shift" placeholder="e.g. Tue (Morning Shift)" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none;">
                </div>
              </div>

              <button class="btn" id="btn-submit-swap" style="background: var(--accent-primary); color: #000; font-weight: 800; border: none; border-radius: var(--radius-md); padding: var(--spacing-sm); cursor: pointer; transition: var(--transition-fast); margin-top: 4px;">
                Submit Roster Swap Request
              </button>
            </div>

            <!-- Swap Log Table -->
            <div style="margin-top: var(--spacing-sm);">
              <h4 style="font-weight: 800; font-size: 0.8rem; color: var(--text-muted); text-transform: uppercase; margin-bottom: 8px;">Pending & Past Swap Requests</h4>
              <div style="max-height: 180px; overflow-y: auto;">
                <table style="width: 100%; border-collapse: collapse; font-size: 0.72rem; text-align: left;">
                  <thead>
                    <tr style="border-bottom: 1.5px solid var(--border-color); color: var(--text-muted); text-transform: uppercase; font-weight: 700;">
                      <th style="padding: 6px;">My Shift</th>
                      <th style="padding: 6px;">Peer Barista</th>
                      <th style="padding: 6px;">Peer Target</th>
                      <th style="padding: 6px;">Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    ${this.state.swaps.map(sw => `
                      <tr style="border-bottom: 1px solid rgba(255,255,255,0.05);">
                        <td style="padding: 8px 6px; font-weight:600;">${sw.myShiftDate}</td>
                        <td style="padding: 8px 6px;">${sw.peerName}</td>
                        <td style="padding: 8px 6px;">${sw.peerShiftDate}</td>
                        <td style="padding: 8px 6px; font-weight: 800; color: ${sw.status === 'Approved' ? 'var(--status-success)' : 'var(--status-warning)'};">${sw.status}</td>
                      </tr>
                    `).join('')}
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <!-- Column Right: Availability Preference Dashboard -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Shift Preferences</h3>
              <i data-lucide="settings" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
            </div>
            
            <div style="display: flex; flex-direction: column; gap: var(--spacing-md); font-size: 0.76rem;">
              <div style="display: flex; flex-direction: column; gap: 6px;">
                <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Preferred Shift Slot</label>
                <div style="display: flex; gap: var(--spacing-sm); margin-top: 4px;">
                  ${['Morning', 'Mid-Day', 'Evening'].map(s => {
                    const isSel = this.state.preferences.preferredShift === s;
                    return `
                      <button class="btn btn-preference-slot" data-val="${s}" style="flex: 1; padding: 6px; border-radius: var(--radius-md); border: 1px solid ${isSel ? 'var(--accent-primary)' : 'rgba(255,255,255,0.08)'}; background: ${isSel ? 'rgba(201,164,106,0.1)' : 'transparent'}; color: ${isSel ? 'var(--accent-primary)' : 'var(--text-muted)'}; font-weight: 700; font-size: 0.7rem; cursor: pointer;">
                        ${s}
                      </button>
                    `;
                  }).join('')}
                </div>
              </div>

              <div style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem; display: block; margin-bottom: 4px;">Weekend Availability</label>
                <label style="display: flex; align-items: center; gap: 8px; cursor: pointer; margin: 0; background: rgba(0,0,0,0.15); padding: 8px var(--spacing-md); border-radius: var(--radius-md); border: 1px solid rgba(255,255,255,0.02);">
                  <input type="checkbox" id="pref-weekend" ${this.state.preferences.availableWeekends ? 'checked' : ''} style="display: none;">
                  <span id="pref-weekend-box" style="width: 16px; height: 16px; border-radius: 4px; border: 1.5px solid ${this.state.preferences.availableWeekends ? 'var(--accent-primary)' : 'var(--text-muted)'}; display: inline-flex; align-items: center; justify-content: center; background: ${this.state.preferences.availableWeekends ? 'var(--accent-primary)' : 'transparent'}; transition: all 0.2s;">
                    ${this.state.preferences.availableWeekends ? `
                      <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="#000" stroke-width="3.5" stroke-linecap="round" stroke-linejoin="round">
                        <polyline points="20 6 9 17 4 12"></polyline>
                      </svg>
                    ` : ''}
                  </span>
                  <span style="font-weight: 600; color: var(--text-primary);">I am available to work weekend holiday shifts</span>
                </label>
              </div>

              <div style="display: flex; flex-direction: column; gap: 4px;">
                <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Target Target Hours Per Week</label>
                <input type="number" id="pref-hours" min="10" max="60" value="${this.state.preferences.maxHoursPerWeek}" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none;">
              </div>

              <button class="btn" id="btn-save-preferences" style="background: transparent; border: 1px solid var(--accent-primary); color: var(--accent-primary); font-weight: 800; border-radius: var(--radius-md); padding: var(--spacing-sm); cursor: pointer; transition: var(--transition-fast); margin-top: 4px;" onmouseover="this.style.background='rgba(201,164,106,0.15)';" onmouseout="this.style.background='transparent';">
                Update Schedule Preferences
              </button>
            </div>
          </div>

        </div>

      </div>

      <!-- Schedule Modal Mount Point Overlay -->
      <div id="schedule-modal-overlay" style="position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background: rgba(0,0,0,0.65); backdrop-filter: blur(8px); display: none; align-items: center; justify-content: center; z-index: 10000; padding: var(--spacing-md);">
        <div id="schedule-modal-content" class="card glass animate-slide-up" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); width: 100%; max-width: 500px; padding: var(--spacing-lg); max-height: 90vh; overflow-y: auto; text-align: left;">
          <!-- Modal content injected here -->
        </div>
      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    const overlay = container.querySelector('#schedule-modal-overlay');
    const modalContent = container.querySelector('#schedule-modal-content');

    const showModal = (htmlContent) => {
      if (overlay && modalContent) {
        modalContent.innerHTML = htmlContent;
        overlay.style.display = 'flex';
        
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
      }
    };

    if (overlay) {
      overlay.addEventListener('click', (e) => {
        if (e.target === overlay) hideModal();
      });
    }

    // 1. Click Calendar Card for Shift Details
    const shiftCards = container.querySelectorAll('.shift-card');
    shiftCards.forEach(card => {
      card.addEventListener('click', () => {
        const id = parseInt(card.getAttribute('data-id'));
        const shift = this.state.shifts.find(s => s.id === id);
        if (!shift) return;

        const isOff = shift.type === 'Day Off';
        const detailsHtml = `
          <div style="display:flex; justify-content:space-between; align-items:center; border-bottom:1.5px solid var(--accent-primary); padding-bottom:var(--spacing-xs); margin-bottom:var(--spacing-md);">
            <h3 style="margin:0; font-weight:800; color:var(--accent-primary); font-family:var(--font-display);">Shift Details: ${shift.date}</h3>
            <button class="btn-close-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer;"><i data-lucide="x" style="width:18px; height:18px;"></i></button>
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
      });
    });

    // 2. Submit Swap request
    const swapMySelect = container.querySelector('#swap-my-shift');
    const swapPeerInput = container.querySelector('#swap-peer-name');
    const swapPeerShiftInput = container.querySelector('#swap-peer-shift');
    const submitSwapBtn = container.querySelector('#btn-submit-swap');

    if (submitSwapBtn) {
      submitSwapBtn.addEventListener('click', () => {
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
      });
    }

    // 3. Shift Preference Slot buttons
    const preferenceBtns = container.querySelectorAll('.btn-preference-slot');
    preferenceBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        const val = btn.getAttribute('data-val');
        this.state.preferences.preferredShift = val;
        
        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      });
    });

    // 4. Weekend Availability toggle
    const weekendCheckbox = container.querySelector('#pref-weekend');
    const weekendBox = container.querySelector('#pref-weekend-box');
    if (weekendCheckbox && weekendBox) {
      weekendBox.addEventListener('click', () => {
        const nextVal = !this.state.preferences.availableWeekends;
        this.state.preferences.availableWeekends = nextVal;
        
        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      });
    }

    // 5. Update preferences button
    const savePrefBtn = container.querySelector('#btn-save-preferences');
    const prefHoursInput = container.querySelector('#pref-hours');
    if (savePrefBtn && prefHoursInput) {
      savePrefBtn.addEventListener('click', () => {
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
      });
    }
  }
}
