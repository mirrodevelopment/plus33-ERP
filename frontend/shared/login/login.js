/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : page.js
 * Path              : frontend/pages/login/page.js
 * Purpose           : Frontend page component for the Pages Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : store/authStore, store/notificationStore, core/logger, core/eventBus
 * Depends On        : store/authStore, store/notificationStore, core/logger, core/eventBus
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend page component for the Pages Module UI. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { authStore } from '../../store/authStore.js';
import { notificationStore } from '../../store/notificationStore.js';
import { logger } from '../../core/logger.js';
import { eventBus } from '../../core/eventBus.js';

export default class LoginPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  mount(container, lifecycle) {
    logger.info('LoginPage', 'Rendering login container layout...');
    
    container.innerHTML = `
      <div style="margin-bottom: var(--spacing-xl); text-align: center;">
        <img src="imgs/logo-gold.png" alt="PLUS33 Logo" onerror="this.style.display='none'"
             style="height: 64px; width: auto; object-fit: contain; margin-bottom: var(--spacing-md);">
        <div style="display:flex; align-items:center; justify-content:center; gap:10px; margin-bottom:var(--spacing-sm);">
          <i data-lucide="coffee" style="width:28px; height:28px; color:var(--accent-primary);"></i>
          <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.8rem; margin: 0; letter-spacing: -0.01em;">PLUS33 ERP</h2>
        </div>
        <p style="color: var(--text-muted); font-size: 0.85rem; margin: 0;">Enterprise Operations Control Center</p>
      </div>
      
      <form id="login-credentials-form" style="display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
        <div class="form-group flex flex-col gap-xs">
          <label style="font-size: 0.75rem; font-weight: 600; color: var(--text-muted); text-transform: uppercase; letter-spacing:0.05em;">Operator Email</label>
          <div style="position:relative; display:flex; align-items:center;">
            <i data-lucide="mail" style="position:absolute; left:12px; width:15px; height:15px; color:var(--text-muted); pointer-events:none;"></i>
            <input type="email" id="input-username" required 
              style="padding: var(--spacing-md) var(--spacing-md) var(--spacing-md) 36px; width:100%; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.85rem; outline: none; transition: var(--transition-fast); box-sizing:border-box;"
              placeholder="Enter your email address"
              onfocus="this.style.borderColor='var(--accent-primary)'" onblur="this.style.borderColor='var(--border-color)'" />
          </div>
        </div>
        
        <div class="form-group flex flex-col gap-xs">
          <label style="font-size: 0.75rem; font-weight: 600; color: var(--text-muted); text-transform: uppercase; letter-spacing:0.05em;">Password</label>
          <div style="position:relative; display:flex; align-items:center;">
            <i data-lucide="lock" style="position:absolute; left:12px; width:15px; height:15px; color:var(--text-muted); pointer-events:none;"></i>
            <input type="password" id="input-password" required 
              style="padding: var(--spacing-md) var(--spacing-md) var(--spacing-md) 36px; width:100%; border-radius: var(--radius-md); background: rgba(0,0,0,0.2); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.85rem; outline: none; transition: var(--transition-fast); box-sizing:border-box;"
              placeholder="Enter your password"
              onfocus="this.style.borderColor='var(--accent-primary)'" onblur="this.style.borderColor='var(--border-color)'" />
          </div>
        </div>
        
        <div class="form-group flex flex-col gap-xs">
          <label style="font-size: 0.75rem; font-weight: 600; color: var(--text-muted); text-transform: uppercase; letter-spacing:0.05em;">Role Profile</label>
          <div style="position:relative; display:flex; align-items:center;">
            <i data-lucide="shield" style="position:absolute; left:12px; width:15px; height:15px; color:var(--text-muted); pointer-events:none;"></i>
            <select id="select-role-profile" 
              style="padding: var(--spacing-md) var(--spacing-md) var(--spacing-md) 36px; width:100%; border-radius: var(--radius-md); background: var(--bg-card); border: 1px solid var(--border-color); color: var(--text-primary); font-size: 0.85rem; outline: none; cursor: pointer; transition: var(--transition-fast); appearance:none; box-sizing:border-box;">
              <option value="ultimateAdmin">Ultimate Admin</option>
              <option value="nationalAdmin">National Admin</option>
              <option value="regionalAdmin">Regional Admin</option>
              <option value="warehouse">Warehouse Admin</option>
              <option value="store">Store Admin</option>
              <option value="storeEmployee">Store Employee</option>
              <option value="shiftSupervisor">Shift Supervisor</option>
            </select>
          </div>
        </div>
        
        <!-- Error message area -->
        <div id="login-error-msg" style="display:none; background:rgba(220,38,38,0.1); border:1px solid var(--status-danger); border-radius:var(--radius-md); padding: 10px 14px; font-size:0.8rem; color:var(--status-danger); display:none; align-items:center; gap:8px;">
          <i data-lucide="alert-circle" style="width:14px; height:14px; flex-shrink:0;"></i>
          <span id="login-error-text">Authentication failed.</span>
        </div>
        
        <button type="submit" id="btn-login-submit" 
          style="padding: var(--spacing-md); margin-top: var(--spacing-sm); border-radius: var(--radius-md); background: var(--accent-primary); border: none; color: #000; font-weight: 700; cursor: pointer; text-transform: uppercase; font-size: 0.8rem; letter-spacing: 0.05em; transition: var(--transition-fast); display:flex; align-items:center; justify-content:center; gap:8px; position:relative;">
          <i data-lucide="log-in" style="width:16px; height:16px;" id="btn-login-icon"></i>
          <span id="btn-login-label">Authenticate</span>
        </button>
      </form>

    `;

    if (window.lucide) window.lucide.createIcons();
    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindEvents(container, lifecycle) {
    const form      = container.querySelector('#login-credentials-form');
    const submitBtn = container.querySelector('#btn-login-submit');
    const errorBox  = container.querySelector('#login-error-msg');
    const errorText = container.querySelector('#login-error-text');
    const label     = container.querySelector('#btn-login-label');

    // Auto-fill credentials on role select change (Dev shortcut mode)
    const roleSelect = container.querySelector('#select-role-profile');
    const inputUsername = container.querySelector('#input-username');
    const inputPassword = container.querySelector('#input-password');

    const credentialsMap = {
      ultimateAdmin: { email: 'ultimate123@plus33.com', pass: 'pass123' },
      nationalAdmin: { email: 'regional_ind@plus33.com', pass: 'pass123' },
      regionalAdmin: { email: 'regional_south_ind@plus33.com', pass: 'pass123' },
      warehouse: { email: 'warehouse123@plus33.com', pass: 'pass123' },
      store: { email: 'store123@plus33.com', pass: 'pass123' },
      storeEmployee: { email: 'employee123@plus33.com', pass: 'pass123' },
      shiftSupervisor: { email: 'supervisor123@plus33.com', pass: 'pass123' }
    };

    const autoFill = () => {
      if (roleSelect && inputUsername && inputPassword) {
        const selected = roleSelect.value;
        if (credentialsMap[selected]) {
          inputUsername.value = credentialsMap[selected].email;
          inputPassword.value = credentialsMap[selected].pass;
        }
      }
    };

    // Auto fill on mount
    autoFill();

    // Auto fill on dropdown change
    if (roleSelect) {
      const handleRoleChange = () => autoFill();
      roleSelect.addEventListener('change', handleRoleChange);
      lifecycle.onCleanup(() => roleSelect.removeEventListener('change', handleRoleChange));
    }

    // Show backend auth errors
    /**
     * Authenticates the user credentials and generates a signed JWT token.
     * @memberof Pages Module
     */
    const loginFailedHandler = ({ message }) => {
      /**
       * Performs the fn operation in this module.
       * @memberof Pages Module
       */
      if (errorBox) {
        errorBox.style.display = 'flex';
        errorText.textContent  = message || 'Authentication failed. Check credentials.';
        if (window.lucide) window.lucide.createIcons();
      }
      notificationStore.danger('Authentication failed. Check credentials.');
      if (label) label.textContent = 'Authenticate';
      if (submitBtn) submitBtn.disabled = false;
    };
    eventBus.on('auth:login-failed', loginFailedHandler);
    lifecycle.onCleanup(() => eventBus.off('auth:login-failed', loginFailedHandler));
    
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (form) {
      /**
       * Submits the submit handler for approval. Transitions DRAFT to SUBMITTED status.
       * @memberof Pages Module
       */
      const submitHandler = async (e) => {
        e.preventDefault();

        const username     = container.querySelector('#input-username').value.trim();
        const password     = container.querySelector('#input-password').value;
        const selectedRole = container.querySelector('#select-role-profile').value;

        // Loading state
        if (errorBox) errorBox.style.display = 'none';
        if (label) label.textContent = 'Authenticating…';
        if (submitBtn) submitBtn.disabled = true;

        logger.info('LoginPage', `Authenticating: ${username} [${selectedRole}]`);

        const success = await authStore.login(username, password, selectedRole);
        /**
         * Performs the fn operation in this module.
         * @memberof Pages Module
         */
        if (success) {
          notificationStore.success('Authenticated successfully.');
          const role = authStore.getRole();
          let targetHash = '#dashboard';
          if (role === 'ultimateAdmin') targetHash = '#ultimate-dashboard';
          else if (role === 'nationalAdmin') targetHash = '#national-dashboard';
          else if (role === 'regionalAdmin') targetHash = '#regional-dashboard';
          else if (role === 'warehouse') targetHash = '#national-dashboard';
          else if (role === 'store') targetHash = '#store-dashboard';
          else if (role === 'storeEmployee') targetHash = '#employee-dashboard';
          else if (role === 'shiftSupervisor') targetHash = '#supervisor-dashboard';
          window.location.hash = targetHash;
        } else {
          if (label) label.textContent = 'Authenticate';
          if (submitBtn) submitBtn.disabled = false;
        }
      };
      
      form.addEventListener('submit', submitHandler);
      lifecycle.onCleanup(() => form.removeEventListener('submit', submitHandler));
    }
  }
}



