/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee Module
 * File              : training.js
 * Path              : frontend/modules/store-employee/training/training.js
 * Purpose           : Barista training hub, safety modules, and interactive certifications
 * Version           : 1.0.0
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { userStore } from '../../../store/userStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';

export default class StoreEmployeeTraining {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.stateKey = `emp_dashboard_state_${this.user?.username || 'neha'}`;
    
    this.loadState();
    this.activeQuizQuestion = 0;
  }

  loadState() {
    const cachedState = localStorage.getItem(this.stateKey);
    if (cachedState) {
      try {
        this.state = JSON.parse(cachedState);
        // Ensure specific modules exist in training state
        if (!this.state.trainingModules) {
          this.state.trainingModules = [
            { id: 'S1', name: 'Food Safety & Hygiene Standards', progress: 100, status: 'Certified', code: 'SAFE-101' },
            { id: 'S2', name: 'Espresso Extraction & Milk Craft', progress: 60, status: 'In Progress', code: 'BAR-202' },
            { id: 'S3', name: 'Customer Experience & Cashier Audit', progress: 40, status: 'In Progress', code: 'SERV-303' },
            { id: 'S4', name: 'Store Opening & Inventory Systems', progress: 0, status: 'Locked', code: 'OPS-404' }
          ];
        }
      } catch (err) {
        logger.error('StoreEmployeeTraining', 'Error parsing cached state', err);
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
      trainingProgress: 50,
      performanceScore: 4.6,
      tasks: [],
      leave: { available: 12.5, pending: 1, approved: 3 },
      attendanceLogs: [],
      activities: [],
      trainingModules: [
        { id: 'S1', name: 'Food Safety & Hygiene Standards', progress: 100, status: 'Certified', code: 'SAFE-101' },
        { id: 'S2', name: 'Espresso Extraction & Milk Craft', progress: 60, status: 'In Progress', code: 'BAR-202' },
        { id: 'S3', name: 'Customer Experience & Cashier Audit', progress: 40, status: 'In Progress', code: 'SERV-303' },
        { id: 'S4', name: 'Store Opening & Inventory Systems', progress: 0, status: 'Locked', code: 'OPS-404' }
      ]
    };
    this.saveState();
  }

  saveState() {
    // Recalculate overall trainingProgress as the average of modules
    const modules = this.state.trainingModules;
    const avg = Math.round(modules.reduce((sum, m) => sum + m.progress, 0) / modules.length);
    this.state.trainingProgress = avg;
    
    localStorage.setItem(this.stateKey, JSON.stringify(this.state));
  }

  async mount(container, lifecycle) {
    logger.info('StoreEmployeeTraining', 'Mounting Barista Training Hub Page...');
    this.loadState();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  render(container) {
    const modules = this.state.trainingModules;
    const certifiedCount = modules.filter(m => m.status === 'Certified').length;
    const inProgressCount = modules.filter(m => m.status === 'In Progress').length;
    
    container.innerHTML = `
      <div class="workspace-container animate-fade-in" style="padding: var(--spacing-lg); display: flex; flex-direction: column; gap: var(--spacing-lg); max-width: 1400px; margin: 0 auto;">
        
        <!-- Header ribbon -->
        <div class="card glass flex justify-between align-center flex-wrap" style="padding: var(--spacing-md) var(--spacing-lg); border-radius: var(--radius-lg); background: var(--bg-card); border: 1px solid var(--border-color); gap: var(--spacing-md); text-align: left;">
          <div>
            <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.5rem; color: var(--text-primary); margin: 0;">
              L&D Training Academy
            </h2>
            <p style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin: 2px 0 0 0;">
              Barista Curriculum &nbsp;·&nbsp; Profile: <span style="color: var(--accent-primary); font-weight: 700;">${this.state.level}</span>
            </p>
          </div>
          <div style="background: rgba(59,130,246,0.12); border: 1px solid rgba(59,130,246,0.3); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.72rem; font-weight: 600; color: #3b82f6; display: flex; align-items: center; gap: 6px;">
            <i data-lucide="award" style="width: 14px; height: 14px;"></i> Curriculum Active
          </div>
        </div>

        <!-- 4 Academy Stats Cards -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: var(--spacing-md); width: 100%;">
          
          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(201,164,106,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--accent-primary); display:flex; align-items:center;">
              <i data-lucide="graduation-cap" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${this.state.trainingProgress}%</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Overall Completion</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(74,222,128,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-success); display:flex; align-items:center;">
              <i data-lucide="shield-check" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${certifiedCount} Certified</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Active Credentials</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(59,130,246,0.1); border-radius: var(--radius-md); padding: 10px; color: #3b82f6; display:flex; align-items:center;">
              <i data-lucide="book-open" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">${inProgressCount} Enrolled</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">In-Progress Modules</div>
            </div>
          </div>

          <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md); border: 1px solid var(--border-color); background: var(--bg-card); text-align: left;">
            <div style="background: rgba(255,255,255,0.05); border-radius: var(--radius-md); padding: 10px; color: var(--text-muted); display:flex; align-items:center;">
              <i data-lucide="lock" style="width: 22px; height: 22px;"></i>
            </div>
            <div>
              <div style="font-size: 1.35rem; font-weight: 800; font-family: var(--font-display);">1 Locked</div>
              <div style="font-size: 0.65rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Prerequisites Remaining</div>
            </div>
          </div>

        </div>

        <!-- Main Grid Layout -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: var(--spacing-lg); width: 100%;">
          
          <!-- Column Left: Module Course Cards -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left; flex: 1.6;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Curriculum Track Syllabus</h3>
              <i data-lucide="bookmark" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
            </div>

            <!-- Modules List -->
            <div style="display: flex; flex-direction: column; gap: var(--spacing-md);">
              ${modules.map(m => {
                const isCert = m.status === 'Certified';
                const isLocked = m.status === 'Locked';
                let statusColor = 'var(--text-muted)';
                if (isCert) statusColor = 'var(--status-success)';
                else if (!isLocked) statusColor = 'var(--status-info)';

                return `
                  <div style="background: rgba(0,0,0,0.15); border: 1px solid ${isCert ? 'rgba(74,222,128,0.1)' : 'var(--border-color)'}; border-radius: var(--radius-md); padding: var(--spacing-md); display: flex; flex-direction: column; gap: var(--spacing-sm); opacity: ${isLocked ? '0.5' : '1'};">
                    <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 6px;">
                      <div>
                        <span style="font-size: 0.6rem; color: var(--accent-primary); font-weight: 700; text-transform: uppercase;">Module ${m.id} · Code: ${m.code}</span>
                        <h4 style="margin: 4px 0 0 0; font-size: 0.9rem; font-weight: 800; color: var(--text-primary);">${m.name}</h4>
                      </div>
                      <span style="font-size: 0.62rem; font-weight: 700; padding: 2px 8px; border-radius: 4px; background: rgba(255,255,255,0.03); color: ${statusColor}; text-transform: uppercase;">
                        ${m.status}
                      </span>
                    </div>

                    <div style="display: flex; align-items: center; gap: var(--spacing-md); margin-top: 4px;">
                      <div style="flex: 1; background: rgba(255,255,255,0.05); height: 6px; border-radius: 3px; overflow: hidden;">
                        <div style="width: ${m.progress}%; height: 100%; background: linear-gradient(90deg, var(--accent-primary), ${isCert ? 'var(--status-success)' : 'var(--status-info)'}); border-radius: 3px;"></div>
                      </div>
                      <span style="font-size: 0.7rem; font-weight: 700; color: var(--text-primary); font-variant-numeric: tabular-nums;">${m.progress}%</span>
                    </div>

                    ${isCert ? `
                      <div style="display: flex; gap: var(--spacing-sm); margin-top: 4px;">
                        <button class="btn btn-secondary btn-download-cert" data-code="${m.code}" style="padding: 4px 10px; font-size: 0.65rem; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; cursor: pointer;">
                          <i data-lucide="download" style="width: 11px; height: 11px; color: var(--accent-primary);"></i> Download Certificate
                        </button>
                      </div>
                    ` : ''}

                    ${m.id === 'S2' && !isCert ? `
                      <div style="display: flex; gap: var(--spacing-sm); margin-top: 4px;">
                        <button class="btn" id="btn-start-bar-quiz" style="background: transparent; border: 1px solid var(--accent-primary); color: var(--accent-primary); padding: 4px 12px; font-size: 0.65rem; font-weight: 700; display: inline-flex; align-items: center; gap: 4px; cursor: pointer;" onmouseover="this.style.background='rgba(201,164,106,0.1)'" onmouseout="this.style.background='transparent'">
                          <i data-lucide="edit-3" style="width: 11px; height: 11px;"></i> Resume &amp; Start S2 Quiz
                        </button>
                      </div>
                    ` : ''}
                  </div>
                `;
              }).join('')}
            </div>

          </div>

          <!-- Column Right: Certifications Achievement Board -->
          <div style="display: flex; flex-direction: column; gap: var(--spacing-lg); flex: 1;">
            
            <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
              <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Active Qualifications</h3>
                <i data-lucide="award" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
              </div>

              <div style="display: flex; flex-direction: column; gap: var(--spacing-sm);">
                <div style="background: rgba(130,163,125,0.08); border: 1px solid rgba(130,163,125,0.2); border-radius: var(--radius-md); padding: var(--spacing-sm); display: flex; align-items: center; gap: var(--spacing-md);">
                  <div style="background: rgba(130,163,125,0.15); color: var(--status-success); padding: 8px; border-radius: 50%; display: flex; align-items: center;">
                    <i data-lucide="award" style="width: 18px; height: 18px;"></i>
                  </div>
                  <div>
                    <strong style="color:var(--text-primary); font-size:0.78rem; display:block;">Food Safety Certified (S1)</strong>
                    <span style="font-size:0.65rem; color:var(--text-muted);">Credential: PLUS33-SAFE-101 &nbsp;·&nbsp; Active</span>
                  </div>
                </div>

                <div style="background: rgba(255,255,255,0.02); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: var(--spacing-sm); display: flex; align-items: center; gap: var(--spacing-md); opacity: 0.55;">
                  <div style="background: rgba(255,255,255,0.04); color: var(--text-muted); padding: 8px; border-radius: 50%; display: flex; align-items: center;">
                    <i data-lucide="lock" style="width: 18px; height: 18px;"></i>
                  </div>
                  <div>
                    <strong style="color:var(--text-primary); font-size:0.78rem; display:block;">Espresso Bar Lead (S2)</strong>
                    <span style="font-size:0.65rem; color:var(--text-muted);">Pass Espresso &amp; Milk Quiz to unlock</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- Training Guideline Memo -->
            <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
              <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.1rem; margin: 0; color: var(--accent-primary);">Academy Guidelines</h3>
                <i data-lucide="info" style="width: 18px; height: 18px; color: var(--accent-primary);"></i>
              </div>
              <p style="margin: 0; font-size: 0.72rem; line-height: 1.45; color: var(--text-muted);">
                All store baristas must hold active S1 Food Safety credentials. Senior Baristas must complete S2 Espresso Extraction modules within 30 days of roster assignments to lead active store barista slots.
              </p>
            </div>

          </div>

        </div>

      </div>

      <!-- Training Modal Mount Point Overlay -->
      <div id="training-modal-overlay" style="position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background: rgba(0,0,0,0.65); backdrop-filter: blur(8px); display: none; align-items: center; justify-content: center; z-index: 10000; padding: var(--spacing-md);">
        <div id="training-modal-content" class="card glass animate-slide-up" style="background: var(--bg-card); border: 1px solid var(--border-color); border-radius: var(--radius-lg); width: 100%; max-width: 500px; padding: var(--spacing-lg); max-height: 90vh; overflow-y: auto; text-align: left;">
          <!-- Modal content injected here -->
        </div>
      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    const overlay = container.querySelector('#training-modal-overlay');
    const modalContent = container.querySelector('#training-modal-content');

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

    // 1. Download Certificate
    const downloadBtns = container.querySelectorAll('.btn-download-cert');
    downloadBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        const code = btn.getAttribute('data-code');
        const certHtml = `
          <div style="display:flex; justify-content:space-between; align-items:center; border-bottom:1.5px solid var(--accent-primary); padding-bottom:var(--spacing-xs); margin-bottom:var(--spacing-md);">
            <h3 style="margin:0; font-weight:800; color:var(--accent-primary); font-family:var(--font-display);">Qualification Certificate</h3>
            <button class="btn-close-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer;"><i data-lucide="x" style="width:18px; height:18px;"></i></button>
          </div>
          <div style="border: 2px dashed var(--accent-primary); padding: var(--spacing-lg); border-radius: var(--radius-md); text-align: center; background: rgba(0,0,0,0.2); display:flex; flex-direction:column; gap:12px; align-items:center;">
            <svg xmlns="http://www.w3.org/2000/svg" width="44" height="44" viewBox="0 0 24 24" fill="none" stroke="var(--accent-primary)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-award"><circle cx="12" cy="8" r="7"/><polyline points="8.21 13.89 7 23 12 20 17 23 15.79 13.88"/></svg>
            <h4 style="margin: 0; font-family: var(--font-display); font-size: 1.15rem; color: #fff; font-weight: 800;">PLUS33 COFFEE L&amp;D</h4>
            <p style="margin: 0; font-size: 0.7rem; color: var(--text-muted); text-transform: uppercase; font-weight:700; letter-spacing:0.5px;">This is proudly presented to</p>
            <h3 style="margin: 0; font-size: 1.35rem; color: var(--accent-primary); font-family: var(--font-display); font-weight: 800;">${this.state.name}</h3>
            <p style="margin: 0; font-size: 0.72rem; line-height: 1.4; color: var(--text-muted);">For successfully completing all standards and examination models for course: <strong>${code}</strong></p>
            <span style="font-size: 0.6rem; color: var(--text-muted); margin-top:10px;">Issued on 01 July 2026 &nbsp;·&nbsp; Credential ID: P33-${code}-881</span>
          </div>
        `;
        showModal(certHtml);
      });
    });

    // 2. Start S2 Espresso Craft Quiz
    const startQuizBtn = container.querySelector('#btn-start-bar-quiz');
    if (startQuizBtn) {
      startQuizBtn.addEventListener('click', () => {
        const quizHtml = `
          <div style="display:flex; justify-content:space-between; align-items:center; border-bottom:1.5px solid var(--accent-primary); padding-bottom:var(--spacing-xs); margin-bottom:var(--spacing-md);">
            <h3 style="margin:0; font-weight:800; color:var(--accent-primary); font-family:var(--font-display);">Espresso &amp; Beverage Craft (S2) Quiz</h3>
            <button class="btn-close-modal" style="background:none; border:none; color:var(--text-muted); cursor:pointer;"><i data-lucide="x" style="width:18px; height:18px;"></i></button>
          </div>
          <div style="font-size:0.75rem; color:var(--text-primary); display:flex; flex-direction:column; gap:12px;">
            <p style="margin:0; line-height:1.4;">Answer this question to progress your S2 Espresso and Beverage module progress.</p>
            
            <div style="background:rgba(255,255,255,0.02); border:1px solid var(--border-color); padding:12px; border-radius:var(--radius-md);">
              <strong style="color:var(--accent-primary); font-size:0.78rem; display:block; margin-bottom:8px;">Question:</strong>
              <span style="font-weight:600; line-height:1.35; display:block;">What is the target extraction time range for a standard double espresso shot using standard grinder settings?</span>
            </div>
            
            <div style="display:flex; flex-direction:column; gap:6px;">
              <button class="btn btn-secondary btn-s2-option" data-correct="false" style="padding:10px; border-radius:var(--radius-md); text-align:left; font-weight:600; font-size:0.72rem;">A) 10 to 15 seconds</button>
              <button class="btn btn-secondary btn-s2-option" data-correct="true" style="padding:10px; border-radius:var(--radius-md); text-align:left; font-weight:600; font-size:0.72rem;">B) 25 to 30 seconds (Optimal Extraction Window)</button>
              <button class="btn btn-secondary btn-s2-option" data-correct="false" style="padding:10px; border-radius:var(--radius-md); text-align:left; font-weight:600; font-size:0.72rem;">C) 45 to 50 seconds (Over-extracted)</button>
            </div>
          </div>
        `;
        showModal(quizHtml);

        // Bind quiz option clicks
        const options = modalContent.querySelectorAll('.btn-s2-option');
        options.forEach(opt => {
          opt.addEventListener('click', () => {
            const isCorrect = opt.getAttribute('data-correct') === 'true';
            if (isCorrect) {
              notificationStore.success('Correct answer! +20% Espresso Craft Progress.');
              
              // Update S2 progress and status
              const s2 = this.state.trainingModules.find(m => m.id === 'S2');
              if (s2) {
                s2.progress = Math.min(s2.progress + 20, 100);
                if (s2.progress === 100) {
                  s2.status = 'Certified';
                  notificationStore.success('Congratulations! You are now S2 Espresso Lead Certified!');
                }
              }
              
              this.saveState();
              hideModal();
              this.render(container);
              this.bindEvents(container, lifecycle);
            } else {
              notificationStore.danger('Incorrect answer. Please review the espresso recipe sheet and try again!');
              opt.style.borderColor = 'var(--status-danger)';
              opt.style.background = 'rgba(239,68,68,0.1)';
            }
          });
        });
      });
    }
  }
}
