/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : RegionalAdmin — Help
 * File              : help.js
 * Purpose           : Controller component for Help & Support page
 * Version           : 1.0.0
 *
 * Related HTML      : frontend/modules/regional-admin/pages/help/help.html
 * Related CSS       : frontend/modules/regional-admin/pages/help/help.css
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';

const TEMPLATE_URL = 'modules/regional-admin/pages/help/help.html';

export default class RegionalAdminHelp {

  constructor() {
    this.user = authStore.getUser();
  }

  async mount(container, lifecycle) {
    logger.info('RegionalAdminHelp', 'Mounting Help & Support page...');
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);
    this._bindEvents(container, lifecycle);
  }

  _loadCss() {
    const id = 'regional-help-css';
    if (!document.getElementById(id)) {
      const link = document.createElement('link');
      link.id = id;
      link.rel = 'stylesheet';
      link.href = 'modules/regional-admin/pages/help/help.css';
      document.head.appendChild(link);
    }
  }

  _bindEvents(container, lifecycle) {
    // TODO: Bind page-specific events
  }

  destroy() {
    logger.info('RegionalAdminHelp', 'Help & Support page destroyed.');
  }
}