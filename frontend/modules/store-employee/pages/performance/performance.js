/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : performance.js
 * Path              : frontend/modules/store-employee/performance/performance.js
 * Purpose           : Controller component for Barista skills analytics radar UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/performance/performance.html
 * Related CSS       : frontend/modules/store-employee/performance/performance.css
 * Related APIs      : None (uses LocalStorage states caching)
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in performance.html — this file is a controller only.
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the performance HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/performance/performance.html';

export default class StoreEmployeePerformance {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.stateKey = `emp_dashboard_state_${this.user?.username || 'neha'}`;
    
    this.loadState();
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the StoreEmployeePerformance component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreEmployeePerformance', 'Mounting Barista Performance Hub Page...');
    
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

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  render(container) {
    const score = this.state.performanceScore;

    // 1. Sync header texts
    const roleEl = container.querySelector('#lbl-employee-role');
    if (roleEl) roleEl.textContent = this.state.level;

    // 2. Sync score stats text
    const scoreTextEl = container.querySelector('#kpi-supervisor-score');
    if (scoreTextEl) scoreTextEl.textContent = String(score);

    // 3. Render dynamic stars block
    this._renderStars(container, score);

    // 4. Render skills SVG Radar Chart
    this._renderRadarChart(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const submitBtn = container.querySelector('#btn-submit-self-eval');
    if (submitBtn) {
      const handleSubmitSelfEval = () => {
        const scoreInput = container.querySelector('#self-eval-score');
        const textInput = container.querySelector('#self-eval-text');
        
        const selfRating = parseFloat(scoreInput.value);
        const text = textInput.value.trim();

        if (isNaN(selfRating) || selfRating < 1 || selfRating > 5) {
          notificationStore.danger('Please input a valid self rating between 1.0 and 5.0.');
          return;
        }

        if (!text) {
          notificationStore.danger('Please enter details of your shift accomplishments.');
          return;
        }

        notificationStore.success('Self evaluation submitted successfully for manager Q2 review.');
        
        // Update local state performance score slightly based on self eval
        this.state.performanceScore = Math.round(((this.state.performanceScore * 3 + selfRating) / 4) * 10) / 10;
        
        scoreInput.value = '';
        textInput.value = '';

        this.saveState();
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      submitBtn.addEventListener('click', handleSubmitSelfEval);
      lifecycle.onCleanup(() => submitBtn.removeEventListener('click', handleSubmitSelfEval));
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderStars(container, score) {
    const starsMount = container.querySelector('#kpi-stars-mount');
    const starTpl = container.querySelector('#perf-star-svg-tpl');

    if (!starsMount || !starTpl) return;

    starsMount.replaceChildren();

    const roundedScore = Math.round(score);

    for (let i = 1; i <= 5; i++) {
      const clone = starTpl.content.cloneNode(true);
      const svg = clone.querySelector('.star-svg');
      if (svg) {
        if (i <= roundedScore) {
          svg.setAttribute('fill', 'var(--accent-primary)');
        }
      }
      starsMount.appendChild(clone);
    }
  }

  _renderRadarChart(container) {
    const radarContainer = container.querySelector('#radar-chart-container');
    if (!radarContainer) return;

    // Actual coordinates center: (150,150)
    radarContainer.innerHTML = `
      <svg width="300" height="300" viewBox="0 0 300 300">
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
    `;
  }

  _loadCss() {
    const cssId = 'store-employee-perf-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/performance/performance.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreEmployeePerformance };
