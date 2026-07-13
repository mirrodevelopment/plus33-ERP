/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : training.js
 * Path              : frontend/modules/store-employee/training/training.js
 * Purpose           : Controller component for Barista training modules academy UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/training/training.html
 * Related CSS       : frontend/modules/store-employee/training/training.css
 * Related APIs      : None (uses LocalStorage states caching)
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in training.html — this file is a controller only.
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the training HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/training/training.html';

export default class StoreEmployeeTraining {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.stateKey = `emp_dashboard_state_${this.user?.username || 'neha'}`;
    
    this.loadState();
    this.activeQuizQuestion = 0;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the StoreEmployeeTraining component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreEmployeeTraining', 'Mounting Barista Training Hub Page...');
    
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
    const cachedState = localStorage.getItem(this.stateKey);
    if (cachedState) {
      try {
        this.state = JSON.parse(cachedState);
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
    const modules = this.state.trainingModules;
    const avg = Math.round(modules.reduce((sum, m) => sum + m.progress, 0) / modules.length);
    this.state.trainingProgress = avg;
    
    localStorage.setItem(this.stateKey, JSON.stringify(this.state));
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  render(container) {
    // 1. Sync header text
    const levelEl = container.querySelector('#lbl-employee-level');
    if (levelEl) levelEl.textContent = this.state.level;

    // 2. Sync KPIs values
    const modules = this.state.trainingModules;
    const certifiedCount = modules.filter(m => m.status === 'Certified').length;
    const inProgressCount = modules.filter(m => m.status === 'In Progress').length;

    const progressEl = container.querySelector('#kpi-training-progress');
    const certifiedEl = container.querySelector('#kpi-certified-count');
    const enrolledEl = container.querySelector('#kpi-enrolled-count');

    if (progressEl) progressEl.textContent = `${this.state.trainingProgress}%`;
    if (certifiedEl) certifiedEl.textContent = `${certifiedCount} Certified`;
    if (enrolledEl) enrolledEl.textContent = `${inProgressCount} Enrolled`;

    // 3. Render Curriculum modules list
    this._renderModulesList(container);

    // 4. Render Active Qualifications sidebar badges list
    this._renderQualificationsList(container);

    if (window.lucide) window.lucide.createIcons();
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const overlay = container.querySelector('#training-modal-overlay');
    const modalContent = container.querySelector('#training-modal-content');

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

    // 1. Download Certificate button click handlers
    const downloadBtns = container.querySelectorAll('.btn-download-cert');
    downloadBtns.forEach(btn => {
      const handleDownloadCert = () => {
        const code = btn.getAttribute('data-code');
        const certHtml = `
          <div class="modal-header-split">
            <h3 class="modal-title">Qualification Certificate</h3>
            <button class="btn-close-modal" type="button" aria-label="Close modal">
              <i data-lucide="x" aria-hidden="true"></i>
            </button>
          </div>
          <div class="cert-dashed-outline">
            <i data-lucide="award" style="width:44px; height:44px; color:var(--accent-primary);" aria-hidden="true"></i>
            <h4>PLUS33 COFFEE L&amp;D</h4>
            <p class="cert-intro">This is proudly presented to</p>
            <h3>${this.state.name}</h3>
            <p class="cert-desc">For successfully completing all standards and examination models for course: <strong>${code}</strong></p>
            <span class="cert-footer">Issued on 01 July 2026 &nbsp;·&nbsp; Credential ID: P33-${code}-881</span>
          </div>
        `;
        showModal(certHtml);
      };
      btn.addEventListener('click', handleDownloadCert);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDownloadCert));
    });

    // 2. Start S2 Espresso Craft Quiz handler
    const startQuizBtn = container.querySelector('#btn-start-bar-quiz');
    if (startQuizBtn) {
      const handleStartQuiz = () => {
        const quizHtml = `
          <div class="modal-header-split">
            <h3 class="modal-title">Espresso &amp; Beverage Craft (S2) Quiz</h3>
            <button class="btn-close-modal" type="button" aria-label="Close modal">
              <i data-lucide="x" aria-hidden="true"></i>
            </button>
          </div>
          <div class="quiz-body">
            <p class="quiz-intro-para">Answer this question to progress your S2 Espresso and Beverage module progress.</p>
            
            <div class="quiz-question-box">
              <strong class="quiz-box-lbl">Question:</strong>
              <span class="quiz-question-text">What is the target extraction time range for a standard double espresso shot using standard grinder settings?</span>
            </div>
            
            <div class="quiz-options-group">
              <button class="btn-quiz-option" data-correct="false" type="button">A) 10 to 15 seconds</button>
              <button class="btn-quiz-option" data-correct="true" type="button">B) 25 to 30 seconds (Optimal Extraction Window)</button>
              <button class="btn-quiz-option" data-correct="false" type="button">C) 45 to 50 seconds (Over-extracted)</button>
            </div>
          </div>
        `;
        showModal(quizHtml);

        // Bind inner quiz option buttons
        const options = modalContent.querySelectorAll('.btn-quiz-option');
        options.forEach(opt => {
          const handleAnswerClick = () => {
            const isCorrect = opt.getAttribute('data-correct') === 'true';
            if (isCorrect) {
              notificationStore.success('Correct answer! +20% Espresso Craft Progress.');
              
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
              opt.classList.add('option-incorrect');
            }
          };
          opt.addEventListener('click', handleAnswerClick);
        });
      };
      startQuizBtn.addEventListener('click', handleStartQuiz);
      lifecycle.onCleanup(() => startQuizBtn.removeEventListener('click', handleStartQuiz));
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderModulesList(container) {
    const feed = container.querySelector('#training-modules-feed');
    const cardTpl = container.querySelector('#training-module-card-tpl');

    if (!feed || !cardTpl) return;

    feed.replaceChildren();

    this.state.trainingModules.forEach(m => {
      const clone = cardTpl.content.cloneNode(true);

      const row = clone.querySelector('.training-module-row');
      const codeLbl = clone.querySelector('.module-code-lbl');
      const nameEl = clone.querySelector('.module-name-text');
      const statusBadge = clone.querySelector('.module-status-badge');
      const progressBarFill = clone.querySelector('.module-progress-fill');
      const progressPercent = clone.querySelector('.module-progress-percent');
      const buttonsRow = clone.querySelector('.module-action-buttons-row');

      const isCert = m.status === 'Certified';
      const isLocked = m.status === 'Locked';

      if (row) {
        if (isCert) row.className = 'training-module-row training-module-row--certified';
        if (isLocked) row.className = 'training-module-row training-module-row--locked';
      }

      if (codeLbl) codeLbl.textContent = `Module ${m.id} · Code: ${m.code}`;
      if (nameEl) nameEl.textContent = m.name;

      if (statusBadge) {
        statusBadge.textContent = m.status;
        if (isCert) {
          statusBadge.className = 'module-status-badge module-status-badge--certified font-bold';
        } else if (!isLocked) {
          statusBadge.className = 'module-status-badge module-status-badge--inprogress font-bold';
        } else {
          statusBadge.className = 'module-status-badge module-status-badge--locked font-bold';
        }
      }

      if (progressBarFill) {
        progressBarFill.style.width = `${m.progress}%`;
      }
      if (progressPercent) progressPercent.textContent = `${m.progress}%`;

      // Actions buttons layout injector
      if (buttonsRow) {
        if (isCert) {
          buttonsRow.innerHTML = `
            <button class="btn btn-secondary btn-download-cert" data-code="${m.code}" type="button">
              <i data-lucide="download" aria-hidden="true"></i> Download Certificate
            </button>`;
        } else if (m.id === 'S2' && !isLocked) {
          buttonsRow.innerHTML = `
            <button class="btn btn-resume-quiz" id="btn-start-bar-quiz" type="button">
              <i data-lucide="edit-3" aria-hidden="true"></i> Resume &amp; Start S2 Quiz
            </button>`;
        }
      }

      feed.appendChild(clone);
    });
  }

  _renderQualificationsList(container) {
    const listBody = container.querySelector('#qualifications-list-body');
    const badgeTpl = container.querySelector('#qualification-badge-tpl');

    if (!listBody || !badgeTpl) return;

    listBody.replaceChildren();

    this.state.trainingModules.forEach(m => {
      // Show S1 and S2 badge cards only
      if (m.id !== 'S1' && m.id !== 'S2') return;

      const clone = badgeTpl.content.cloneNode(true);
      const row = clone.querySelector('.qualification-badge-row');
      const titleEl = clone.querySelector('.badge-title');
      const subtitleEl = clone.querySelector('.badge-subtitle');
      const icon = clone.querySelector('.badge-icon-circle i');

      const isCert = m.status === 'Certified';

      if (row) {
        row.className = `qualification-badge-row ${isCert ? 'qualification-badge-row--active' : 'qualification-badge-row--locked'}`;
      }

      if (titleEl) {
        titleEl.textContent = m.id === 'S1' ? 'Food Safety Certified (S1)' : 'Espresso Bar Lead (S2)';
      }

      if (subtitleEl) {
        subtitleEl.textContent = isCert ? `Credential: PLUS33-${m.code} · Active` : 'Pass Espresso & Milk Quiz to unlock';
      }

      if (icon && !isCert) {
        icon.setAttribute('data-lucide', 'lock');
      }

      listBody.appendChild(clone);
    });
  }

  _loadCss() {
    const cssId = 'store-employee-training-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/training/training.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreEmployeeTraining };
