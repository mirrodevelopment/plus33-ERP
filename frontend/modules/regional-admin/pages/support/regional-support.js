/******************************************************************************
 * Project    : PLUS33 Coffee ERP
 * Component  : Regional Admin Support & Operations Controller
 * File       : regional-support.js
 ******************************************************************************/

import { authStore }         from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger }            from '../../../../core/logger.js';
import { htmlLoader }        from '../../../../core/htmlLoader.js';
import { apiClient }         from '../../../../api/client.js';

const TEMPLATE_URL = 'modules/regional-admin/pages/support/regional-support.html';
const CSS_ID       = 'regional-support-sep-css';
const CSS_URL      = 'modules/regional-admin/pages/support/regional-support.css';

export default class RegionalAdminSupport {

  constructor() {
    this.user = authStore.getUser();
    this.container = null;
  }

  async mount(container) {
    logger.info('RegionalAdminSupport', 'Mounting Regional Admin Support Portal…');
    this.container = container;
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);
    this._bindEvents();
    if (window.lucide) window.lucide.createIcons();
  }

  _loadCss() {
    if (!document.getElementById(CSS_ID)) {
      const link = document.createElement('link');
      link.id   = CSS_ID;
      link.rel  = 'stylesheet';
      link.href = CSS_URL;
      document.head.appendChild(link);
    }
  }

  _bindEvents() {
    this.container.querySelector('#rs-submit-form')?.addEventListener('submit', e => this._handleSubmit(e));
  }

  async _handleSubmit(e) {
    e.preventDefault();
    const targetRole = this.container.querySelector('#rs-target-select')?.value || 'ULTIMATE_ADMIN';
    const category   = this.container.querySelector('#rs-cat-select')?.value || 'WORKPLACE_COMPLAINT';
    const storeId    = this.container.querySelector('#rs-store-select')?.value || 1;
    const priority   = this.container.querySelector('#rs-priority-select')?.value || 'MEDIUM';
    const subject    = this.container.querySelector('#rs-subject-inp')?.value?.trim();
    const desc       = this.container.querySelector('#rs-desc-inp')?.value?.trim();

    if (!subject || !desc) {
      notificationStore.warning('Subject and description are required.');
      return;
    }

    try {
      const res = await apiClient.post('/api/v1/support/tickets', {
        category, subcategory: 'Regional Escalation', targetRole, priority, subject, description: desc,
        reporterId: this.user?.id,
        reporterName: this.user?.username || 'Regional Admin',
        reporterRole: 'regionalAdmin',
        storeId
      });
      const code = res?.data?.ticketCode || res?.ticketCode || '';
      notificationStore.success(`Regional ticket ${code} submitted to ${targetRole}.`);
      e.target.reset();
    } catch (err) {
      logger.error('RegionalAdminSupport', 'Failed to submit regional ticket', err);
      notificationStore.error(`Failed to submit regional ticket: ${err.message || 'Server error'}`);
    }
  }

  destroy() {
    logger.info('RegionalAdminSupport', 'Regional Admin Support Portal unmounted.');
  }
}
