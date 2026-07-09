/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Shared — Authentication
 * File              : login.js
 * Path              : frontend/shared/login/login.js
 * Purpose           : Login page controller
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/shared/login/login.html
 * Related CSS       : frontend/shared/login/login.css
 * Related API       : POST /api/v1/auth/login  (via authStore)
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in login.html — this file is a controller only.
 *
 * Controller Lifecycle:
 *   constructor → mount → loadTemplate → loadData → render → bindEvents → destroy
 ******************************************************************************/

import { htmlLoader } from '../../core/htmlLoader.js';
import { authStore } from '../../store/authStore.js';
import { notificationStore } from '../../store/notificationStore.js';
import { logger } from '../../core/logger.js';
import { eventBus } from '../../core/eventBus.js';

/** Path to the login HTML template */
const TEMPLATE_URL = 'shared/login/login.html';

/** Dev auto-fill credential map */
const CREDENTIALS_MAP = {
  ultimateAdmin:  { email: 'ultimate123@plus33.com',          pass: 'pass123' },
  nationalAdmin:  { email: 'regional_ind@plus33.com',         pass: 'pass123' },
  regionalAdmin:  { email: 'regional_south_ind@plus33.com',   pass: 'pass123' },
  warehouse:      { email: 'warehouse123@plus33.com',         pass: 'pass123' },
  store:          { email: 'store123@plus33.com',             pass: 'pass123' },
  storeEmployee:  { email: 'employee123@plus33.com',          pass: 'pass123' },
  shiftSupervisor:{ email: 'supervisor123@plus33.com',        pass: 'pass123' }
};

/** Post-login hash route map */
const ROLE_ROUTES = {
  ultimateAdmin:   '#ultimate-dashboard',
  nationalAdmin:   '#national-dashboard',
  regionalAdmin:   '#regional-dashboard',
  warehouse:       '#national-dashboard',
  store:           '#store-dashboard',
  storeEmployee:   '#employee-dashboard',
  shiftSupervisor: '#supervisor-dashboard'
};

export default class LoginPage {
  constructor() {
    /** @type {AbortController} For cleanup of event listeners */
    this._cleanupFns = [];
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the login page into the given container.
   * @param {HTMLElement} container - The #main-content element inside auth layout
   * @param {{ onCleanup: Function }} lifecycle - Lifecycle hooks from the router
   */
  async mount(container, lifecycle) {
    logger.info('LoginPage', 'Mounting login page...');

    // 1. Load HTML template
    await this._loadTemplate(container);

    // 2. Load CSS (dynamic, not blocking)
    this._loadCss();

    // 3. Populate initial data
    this._loadData(container);

    // 4. Bind all event listeners
    this._bindEvents(container, lifecycle);

    logger.info('LoginPage', 'Login page ready.');
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: loadTemplate
  // ---------------------------------------------------------------------------

  /**
   * Fetch and inject the HTML template into the container.
   * @param {HTMLElement} container
   */
  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: loadData
  // ---------------------------------------------------------------------------

  /**
   * Populate initial state — auto-fill credentials for default role.
   * @param {HTMLElement} container
   */
  _loadData(container) {
    this._autoFill(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render  (no async data to render on this page)
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  /**
   * Attach all event listeners. Register cleanup via lifecycle.onCleanup().
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  _bindEvents(container, lifecycle) {
    const roleSelect    = container.querySelector('#select-role-profile');
    const form          = container.querySelector('#login-credentials-form');
    const togglePassBtn = container.querySelector('#btn-toggle-password');
    const passwordInput = container.querySelector('#input-password');
    const eyeIcon       = container.querySelector('#password-eye-icon');

    // Role dropdown → auto-fill credentials
    if (roleSelect) {
      const onRoleChange = () => this._autoFill(container);
      roleSelect.addEventListener('change', onRoleChange);
      lifecycle.onCleanup(() => roleSelect.removeEventListener('change', onRoleChange));
    }

    // Password visibility toggle
    if (togglePassBtn && passwordInput && eyeIcon) {
      const onTogglePass = () => {
        const isHidden = passwordInput.type === 'password';
        passwordInput.type = isHidden ? 'text' : 'password';
        eyeIcon.dataset.lucide = isHidden ? 'eye-off' : 'eye';
        if (window.lucide) window.lucide.createIcons();
      };
      togglePassBtn.addEventListener('click', onTogglePass);
      lifecycle.onCleanup(() => togglePassBtn.removeEventListener('click', onTogglePass));
    }

    // Auth failure event from backend
    const onLoginFailed = ({ message }) => {
      this._showError(container, message || 'Authentication failed. Check credentials.');
      this._setSubmitState(container, false);
      notificationStore.danger('Authentication failed. Check credentials.');
    };
    eventBus.on('auth:login-failed', onLoginFailed);
    lifecycle.onCleanup(() => eventBus.off('auth:login-failed', onLoginFailed));

    // Form submit
    if (form) {
      const onSubmit = async (e) => {
        e.preventDefault();
        await this._handleSubmit(container);
      };
      form.addEventListener('submit', onSubmit);
      lifecycle.onCleanup(() => form.removeEventListener('submit', onSubmit));
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  /**
   * Clean up any resources. Called by router on navigation away.
   */
  destroy() {
    logger.debug('LoginPage', 'Destroyed.');
  }

  // ---------------------------------------------------------------------------
  // PRIVATE: Actions
  // ---------------------------------------------------------------------------

  /**
   * Auto-fill email and password based on selected role (dev helper).
   * @param {HTMLElement} container
   */
  _autoFill(container) {
    const roleSelect    = container.querySelector('#select-role-profile');
    const emailInput    = container.querySelector('#input-username');
    const passwordInput = container.querySelector('#input-password');

    if (!roleSelect || !emailInput || !passwordInput) return;

    const creds = CREDENTIALS_MAP[roleSelect.value];
    if (creds) {
      emailInput.value    = creds.email;
      passwordInput.value = creds.pass;
    }
  }

  /**
   * Handle login form submission.
   * @param {HTMLElement} container
   */
  async _handleSubmit(container) {
    const emailInput    = container.querySelector('#input-username');
    const passwordInput = container.querySelector('#input-password');
    const roleSelect    = container.querySelector('#select-role-profile');

    const username     = emailInput?.value.trim()  || '';
    const password     = passwordInput?.value       || '';
    const selectedRole = roleSelect?.value          || '';

    // Hide previous error, set loading state
    this._hideError(container);
    this._setSubmitState(container, true);

    logger.info('LoginPage', `Authenticating: ${username} [${selectedRole}]`);

    const success = await authStore.login(username, password, selectedRole);

    if (success) {
      notificationStore.success('Authenticated successfully.');
      const role = authStore.getRole();
      window.location.hash = ROLE_ROUTES[role] || '#dashboard';
    } else {
      this._setSubmitState(container, false);
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE: UI State helpers
  // ---------------------------------------------------------------------------

  /**
   * Show the error message box with the given message.
   * @param {HTMLElement} container
   * @param {string} message
   */
  _showError(container, message) {
    const errorBox  = container.querySelector('#login-error-msg');
    const errorText = container.querySelector('#login-error-text');
    if (errorBox && errorText) {
      errorText.textContent = message;
      errorBox.hidden = false;
      if (window.lucide) window.lucide.createIcons();
    }
  }

  /**
   * Hide the error message box.
   * @param {HTMLElement} container
   */
  _hideError(container) {
    const errorBox = container.querySelector('#login-error-msg');
    if (errorBox) errorBox.hidden = true;
  }

  /**
   * Enable or disable the submit button with appropriate loading label.
   * @param {HTMLElement} container
   * @param {boolean} isLoading
   */
  _setSubmitState(container, isLoading) {
    const submitBtn = container.querySelector('#btn-login-submit');
    const label     = container.querySelector('#btn-login-label');

    if (submitBtn) submitBtn.disabled = isLoading;
    if (label)     label.textContent  = isLoading ? 'Authenticating…' : 'Authenticate';
  }

  // ---------------------------------------------------------------------------
  // PRIVATE: CSS Loader
  // ---------------------------------------------------------------------------

  /**
   * Dynamically inject the login CSS link if not already loaded.
   */
  _loadCss() {
    const cssId = 'login-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id   = cssId;
      link.rel  = 'stylesheet';
      link.href = 'shared/login/login.css';
      document.head.appendChild(link);
    }
  }
}
