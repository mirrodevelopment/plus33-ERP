/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : quick-actions.js
 * Path              : frontend/widgets/system/quick-actions.js
 * Purpose           : Frontend utility: quick-actions for PLUS33 Coffee ERP
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : store/notificationStore, core/logger
 * Depends On        : store/notificationStore, core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend utility: quick-actions for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { notificationStore } from '../../store/notificationStore.js';
import { logger } from '../../core/logger.js';

export class QuickActions {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config) {
    this.config = config;
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    container.innerHTML = `
      <h3 style="font-size: 0.9rem; font-weight: 700; text-transform: uppercase; color: var(--text-muted); margin-bottom: var(--spacing-md);">${this.config.title}</h3>
      
      <div class="grid grid-cols-12 gap-sm" style="width: 100%;">
        <button class="col-6 q-btn" id="btn-trigger-esg" style="padding: var(--spacing-md); border-radius: var(--radius-md); background: rgba(201, 164, 106, 0.05); border: 1px solid var(--border-color); color: var(--text-primary); cursor: pointer; text-align: center; font-weight: 600; font-size: 0.8rem; transition: var(--transition-fast);">
          🌱 Recalculate Scope 1/2
        </button>
        <button class="col-6 q-btn" id="btn-trigger-mrp" style="padding: var(--spacing-md); border-radius: var(--radius-md); background: rgba(201, 164, 106, 0.05); border: 1px solid var(--border-color); color: var(--text-primary); cursor: pointer; text-align: center; font-weight: 600; font-size: 0.8rem; transition: var(--transition-fast);">
          ⚙️ Run MRP Explosions
        </button>
        <button class="col-6 q-btn" id="btn-trigger-sync" style="padding: var(--spacing-md); border-radius: var(--radius-md); background: rgba(201, 164, 106, 0.05); border: 1px solid var(--border-color); color: var(--text-primary); cursor: pointer; text-align: center; font-weight: 600; font-size: 0.8rem; transition: var(--transition-fast);">
          🔄 Sync DWH Snapshots
        </button>
        <button class="col-6 q-btn" id="btn-trigger-attest" style="padding: var(--spacing-md); border-radius: var(--radius-md); background: rgba(201, 164, 106, 0.05); border: 1px solid var(--border-color); color: var(--text-primary); cursor: pointer; text-align: center; font-weight: 600; font-size: 0.8rem; transition: var(--transition-fast);">
          🔏 Attest Compliance
        </button>
      </div>
    `;

    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  bindEvents(container, lifecycle) {
    const buttons = [
      { id: 'btn-trigger-esg', text: 'ESG carbon footprint calculations updated.', type: 'success' },
      { id: 'btn-trigger-mrp', text: 'MRP II materials requirements explosion run triggered successfully.', type: 'info' },
      { id: 'btn-trigger-sync', text: 'BI DWH dimensional snapshots synchronized.', type: 'success' },
      { id: 'btn-trigger-attest', text: 'Zero-trust compliance attestation signed immutably.', type: 'success' }
    ];

    buttons.forEach(b => {
      const btn = container.querySelector(`#${b.id}`);
      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      if (btn) {
        /**
         * Handles the handler event or exception in the business workflow.
         * @memberof Widgets Module
         */
        const handler = () => {
          logger.info('QuickActions', `Triggered action action: ${b.id}`);
          notificationStore.push(b.text, b.type);
        };
        btn.addEventListener('click', handler);
        lifecycle.onCleanup(() => btn.removeEventListener('click', handler));
        
        // Hover effects
        btn.addEventListener('mouseover', () => {
          btn.style.background = 'rgba(201, 164, 106, 0.15)';
          btn.style.borderColor = 'var(--accent-primary)';
        });
        btn.addEventListener('mouseout', () => {
          btn.style.background = 'rgba(201, 164, 106, 0.05)';
          btn.style.borderColor = 'var(--border-color)';
        });
      }
    });
  }
}
