/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module
 * File              : performance.js
 * Path              : frontend/modules/store-employee/performance/performance.js
 * Purpose           : Barista skill radar charts, performance ratings, and manager reviews
 * Version           : 1.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { userStore } from '../../../store/userStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';

export default class StoreEmployeePerformance {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.stateKey = `emp_dashboard_state_${this.user?.username || 'neha'}`;
    
    this.loadState();
  }

  loadState() {
    const cachedState = localStorage.getItem(this.stateKey);
    if (cachedState) {
      try {
        this.state = JSON.parse(cachedState);
      } catch (err) {
        logger.error('StoreEmployeePerformance', 'Error parsing cached state', err);
        this.initDefaultState();
      }
    } else {
      this.initDefaultState();
    }
  }

  initDefaultState() {
    this.state = {
      name: this.profile.name || 'Neha Sharma',
      id: 'EMP10245',
      level: 'Senior Barista',
      store: 'Green Park Café, City Center',
      clockedIn: true,
      clockInTime: '08:02 AM',
      trainingProgress: 72,
      performanceScore: 4.6,
      tasks: [],
      leave: { available: 12.5, pending: 1, approved: 3 },
      attendanceLogs: [],
      activities: []
    };
    this.saveState();
  }

  saveState() {
    localStorage.setItem(this.stateKey, JSON.stringify(this.state));
  }

  async mount(container, lifecycle) {
    logger.info('StoreEmployeePerformance', 'Mounting Barista Performance Hub Page...');
    this.loadState();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  render(container) {
    const score = this.state.performanceScore;
    
    container.innerHTML = `
      <div class="workspace-container animate-fade-in" style="padding: var(--spacing-lg); display: flex; flex-direction: column; gap: var(--spacing-lg); max-width: 1400px; margin: 0 auto;">
        
        <!-- Header ribbon -->
        <div class="card glass flex justify-between align-center flex-wrap" style="padding: var(--spacing-md) var(--spacing-lg); border-radius: var(--radius-lg); background: var(--bg-card); border: 1px solid var(--border-color); gap: var(--spacing-md); text-align: left;">
          <div>
            <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.5rem; color: var(--text-primary); margin: 0;">
              Performance Analytics
            </h2>
            <p style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin: 2px 0 0 0;">
              Roster Rating &nbsp;·&nbsp; Profile: <span style="color: var(--accent-primary); font-weight: 700;">${this.state.level}</span>
            </p>
          </div>
          <div style="background: rgba(201,164,106,0.12); border: 1px solid var(--accent-primary); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.72rem; font-weight: 600; color: var(--accent-primary); display: flex; align-items: center; gap: 6px;">
            <i data-lucide="star" style="width: 14px; height: 14px;"></i> Q2 Evaluation Period
          </div>
        </div>

        <!-- 4 KPI Performance Cards -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: var(--spacing-md); width: 100%;">
          
          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(201,164,106,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--accent-primary); display:flex; align-items:center;">
              <i data-lucide="star" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display); display: flex; align-items: center; gap: 6px;">
                <span>${score}</span>
                <span style="display: inline-flex; align-items: center; gap: 1px;">
                  ${[1, 2, 3, 4, 5].map(i => `
                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" viewBox="0 0 24 24" fill="${i <= Math.round(score) ? 'var(--accent-primary)' : 'none'}" stroke="var(--accent-primary)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon>
                    </svg>
                  `).join('')}
                </span>
              </div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Overall Supervisor Score</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(74,222,128,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-success); display:flex; align-items:center;">
              <i data-lucide="percent" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">98.2%</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Grinder Calibration Accuracy</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(59,130,246,0.1); border-radius: var(--radius-md); padding: 10px; color: #3b82f6; display:flex; align-items:center;">
              <i data-lucide="heart" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">94 NPS</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Customer Net Promoter Score</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(130,163,125,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-success); display:flex; align-items:center;">
              <i data-lucide="clock" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">100%</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Attendance Adherence</div>
            </div>
          </div>

        </div>

        <!-- Main workspace grid layout -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: var(--spacing-lg); width: 100%;">
          
          <!-- Column Left: Skill Radar Competency SVG Chart -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: center; align-items: center; flex: 1.2;">
            <div style="width: 100%; border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center; text-align: left;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Barista Skill Radar</h3>
              <i data-lucide="radar" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
            </div>

            <!-- Radar chart SVG -->
            <div style="position: relative; width: 300px; height: 300px; margin: 10px 0;">
              <svg width="300" height="300" viewBox="0 0 300 300" style="overflow: visible;">
                <!-- Pentagon grids -->
                <!-- Grid 5 (100%) -->
                <polygon points="150,30 264,113 221,247 79,247 36,113" fill="none" stroke="rgba(255,255,255,0.08)" stroke-width="1"/>
                <!-- Grid 4 (80%) -->
                <polygon points="150,54 241,120 207,228 93,228 59,120" fill="none" stroke="rgba(255,255,255,0.06)" stroke-width="1"/>
                <!-- Grid 3 (60%) -->
                <polygon points="150,78 218,128 193,208 107,208 82,128" fill="none" stroke="rgba(255,255,255,0.04)" stroke-width="1"/>
                <!-- Grid 2 (40%) -->
                <polygon points="150,102 196,135 178,189 122,189 104,135" fill="none" stroke="rgba(255,255,255,0.03)" stroke-width="1"/>
                
                <!-- Axis lines -->
                <line x1="150" y1="150" x2="150" y2="30" stroke="rgba(255,255,255,0.1)" stroke-width="1"/>
                <line x1="150" y1="150" x2="264" y2="113" stroke="rgba(255,255,255,0.1)" stroke-width="1"/>
                <line x1="150" y1="150" x2="221" y2="247" stroke="rgba(255,255,255,0.1)" stroke-width="1"/>
                <line x1="150" y1="150" x2="79" y2="247" stroke="rgba(255,255,255,0.1)" stroke-width="1"/>
                <line x1="150" y1="150" x2="36" y2="113" stroke="rgba(255,255,255,0.1)" stroke-width="1"/>
                
                <!-- Barista Actual Skills Area (95% pull, 88% speed, 94% customer, 98% hygiene, 100% time) -->
                <!-- Points calculated scale: Center (150,150). Max radius 120 -->
                <!-- Pulls: 95% -> (150, 36) -->
                <!-- Speed: 88% -> (250, 117) -->
                <!-- Customer: 94% -> (217, 241) -->
                <!-- Hygiene: 98% -> (80, 245) -->
                <!-- Time: 100% -> (36, 113) -->
                <polygon points="150,36 250,117 217,241 80,245 36,113" fill="rgba(201,164,106,0.22)" stroke="var(--accent-primary)" stroke-width="2.5" stroke-linejoin="round"/>
                
                <!-- Radar dots -->
                <circle cx="150" cy="36" r="3" fill="var(--accent-primary)"/>
                <circle cx="250" cy="117" r="3" fill="var(--accent-primary)"/>
                <circle cx="217" cy="241" r="3" fill="var(--accent-primary)"/>
                <circle cx="80" cy="245" r="3" fill="var(--accent-primary)"/>
                <circle cx="36" cy="113" r="3" fill="var(--accent-primary)"/>
                
                <!-- Labels -->
                <text x="150" y="20" fill="var(--text-primary)" font-size="9" font-weight="700" text-anchor="middle">Espresso Pulls</text>
                <text x="270" y="113" fill="var(--text-primary)" font-size="9" font-weight="700" text-anchor="start">Speed (Serving)</text>
                <text x="226" y="260" fill="var(--text-primary)" font-size="9" font-weight="700" text-anchor="start">Customer Care</text>
                <text x="74" y="260" fill="var(--text-primary)" font-size="9" font-weight="700" text-anchor="end">Hygiene logs</text>
                <text x="30" y="113" fill="var(--text-primary)" font-size="9" font-weight="700" text-anchor="end">Punctuality</text>
              </svg>
            </div>

            <div style="font-size: 0.72rem; color: var(--text-muted); line-height: 1.4; border-top: 1px solid rgba(255,255,255,0.05); padding-top: 10px; width:100%;">
              Barista metrics are computed automatically based on calibration logs and supervisor appraisals.
            </div>
          </div>

          <!-- Column Right: Supervisor Reviews & Self Evaluation -->
          <div style="display: flex; flex-direction: column; gap: var(--spacing-lg); flex: 1.4;">
            
            <!-- Supervisor Reviews Board -->
            <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
              <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Manager Appraisals</h3>
                <i data-lucide="message-square" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
              </div>

              <div style="display: flex; flex-direction: column; gap: var(--spacing-md); font-size: 0.74rem;">
                <div style="border-left: 3.5px solid var(--accent-primary); padding-left: var(--spacing-sm);">
                  <strong style="color: var(--text-primary); font-size: 0.8rem; display: block;">Manoj Kumar (Store Manager)</strong>
                  <span style="font-size:0.62rem; color:var(--text-muted); display:block; margin-bottom:2px;">Performance Review Period Q1</span>
                  <span style="color: var(--text-muted); line-height: 1.4; display: block;">
                    "Neha maintains outstanding hygiene logs and grinder calibration scores. Espresso extraction yields have been consistent. Excellent customer interactions during busy morning rosters."
                  </span>
                </div>

                <div style="border-left: 3.5px solid var(--status-success); padding-left: var(--spacing-sm);">
                  <strong style="color: var(--text-primary); font-size: 0.8rem; display: block;">Rohan Sharma (Senior Barista)</strong>
                  <span style="font-size:0.62rem; color:var(--text-muted); display:block; margin-bottom:2px;">Shift Peer Feedback</span>
                  <span style="color: var(--text-muted); line-height: 1.4; display: block;">
                    "Great coordination during peak rush hours last Friday. Maintained high speed without compromising espresso beverage safety standards."
                  </span>
                </div>
              </div>
            </div>

            <!-- Self Evaluation Form -->
            <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
              <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Self Evaluation</h3>
                <i data-lucide="edit" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
              </div>

              <div style="display: flex; flex-direction: column; gap: var(--spacing-sm); font-size: 0.76rem;">
                <div style="display: flex; flex-direction: column; gap: 4px;">
                  <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Rate Yourself (1 to 5 Stars)</label>
                  <input type="number" id="self-eval-score" min="1" max="5" step="0.5" placeholder="e.g. 4.5" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none;">
                </div>

                <div style="display: flex; flex-direction: column; gap: 4px;">
                  <label style="font-weight: 700; color: var(--text-muted); text-transform: uppercase; font-size: 0.6rem;">Key accomplishments & Goals</label>
                  <textarea id="self-eval-text" placeholder="Detail your shift accomplishments..." rows="3" style="width: 100%; background: rgba(0,0,0,0.3); border: 1px solid var(--border-color); border-radius: var(--radius-md); color: var(--text-primary); padding: var(--spacing-sm); outline: none; font-family: inherit; resize: none;"></textarea>
                </div>

                <button class="btn" id="btn-submit-self-eval" style="background: var(--accent-primary); color: #000; font-weight: 800; border: none; border-radius: var(--radius-md); padding: var(--spacing-sm); cursor: pointer; transition: var(--transition-fast); margin-top: 4px;">
                  Submit Appraisal Review
                </button>
              </div>
            </div>

          </div>

        </div>

      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    const submitBtn = container.querySelector('#btn-submit-self-eval');
    if (submitBtn) {
      submitBtn.addEventListener('click', () => {
        const scoreInput = container.querySelector('#self-eval-score');
        const textInput = container.querySelector('#self-eval-text');
        
        const score = parseFloat(scoreInput.value);
        const text = textInput.value.trim();

        if (isNaN(score) || score < 1 || score > 5) {
          notificationStore.danger('Please input a valid self rating between 1.0 and 5.0.');
          return;
        }

        if (!text) {
          notificationStore.danger('Please enter details of your shift accomplishments.');
          return;
        }

        notificationStore.success('Self evaluation submitted successfully for manager Q2 review.');
        
        // Update local state performance score slightly based on self eval!
        this.state.performanceScore = Math.round(((this.state.performanceScore * 3 + score) / 4) * 10) / 10;
        
        scoreInput.value = '';
        textInput.value = '';

        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      });
    }
  }
}
