/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : compliance-gauge.js
 * Path              : frontend/widgets/charts/compliance-gauge.js
 * Purpose           : Frontend utility: compliance-gauge for PLUS33 Coffee ERP
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/logger
 * Depends On        : core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend utility: compliance-gauge for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../../core/logger.js';
import { htmlLoader } from '../../../core/htmlLoader.js';

export class ComplianceGauge {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config, complianceOverview) {
    this.config = config;
    this.complianceOverview = complianceOverview || { complianceScore: 0, auditsCompleted: 0, correctiveActionsOpen: 0, overdueActions: 0 };
  }

  _loadCss() {
    const href = 'widgets/charts/compliance-gauge/compliance-gauge.css';
    if (!document.querySelector(`link[href="${href}"]`)) {
      const link = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = href;
      document.head.appendChild(link);
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  async mount(container, lifecycle) {
    logger.debug('ComplianceGauge', 'Rendering compliance radial gauge...');
    this._loadCss();

    const score = Number(this.complianceOverview.complianceScore || 0).toFixed(1);

    let html = await htmlLoader.load('widgets/charts/compliance-gauge/compliance-gauge.html');
    html = html.replace('{{TITLE}}', this.config.title)
               .replace(/{{SCORE}}/g, score)
               .replace('{{AUDITS}}', this.complianceOverview.auditsCompleted)
               .replace('{{CORRECTIVE}}', this.complianceOverview.correctiveActionsOpen)
               .replace('{{OVERDUE}}', this.complianceOverview.overdueActions);

    container.innerHTML = html;

    // Bind info button click
    const infoBtn = container.querySelector('#compliance-info-btn');
    if (infoBtn) {
      infoBtn.addEventListener('click', (e) => {
        e.preventDefault();
        e.stopPropagation();
        this._showExplanationModal(container);
      });
    }

    if (window.lucide) window.lucide.createIcons();
  }

  _showExplanationModal(container) {
    const modal = container.querySelector('#compliance-explanation-modal');
    if (modal) {
      modal.removeAttribute('hidden');
      
      if (!modal.dataset.bound) {
        modal.dataset.bound = 'true';
        
        const closeModal = () => modal.setAttribute('hidden', '');
        modal.querySelector('#close-compliance-expl-modal').addEventListener('click', closeModal);
        modal.querySelector('#close-compliance-expl-modal-btn').addEventListener('click', closeModal);
        modal.addEventListener('click', (e) => {
          if (e.target === modal) {
            closeModal();
          }
        });
      }
    }
  }
}
