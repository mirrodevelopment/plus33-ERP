/******************************************************************************
 * Project    : PLUS33 Coffee ERP
 * Component  : Store Admin & Manager Support Controller
 * File       : store-support.js
 ******************************************************************************/

import { authStore }         from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger }            from '../../../../core/logger.js';
import { htmlLoader }        from '../../../../core/htmlLoader.js';
import { apiClient }         from '../../../../api/client.js';

const TEMPLATE_URL = 'modules/store-admin/pages/support/store-support.html';
const CSS_ID       = 'store-support-sep-css';
const CSS_URL      = 'modules/store-admin/pages/support/store-support.css';

export default class StoreAdminSupport {

  constructor() {
    this.user = authStore.getUser();
    this.container = null;
  }

  async mount(container) {
    logger.info('StoreAdminSupport', 'Mounting Store Admin Support Hub…');
    this.container = container;
    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);
    this._initStoreInfo();
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

  _initStoreInfo() {
    const el = this.container.querySelector('#ss-store-name');
    if (el && this.user) {
      el.value = this.user.storeName || (this.user.storeId ? `Store #${this.user.storeId}` : 'Store #1 Flagship');
    }
  }

  _bindEvents() {
    this.container.querySelector('#ss-submit-form')?.addEventListener('submit', e => this._handleSubmit(e));
  }

  async _handleSubmit(e) {
    e.preventDefault();
    const targetRole = this.container.querySelector('#ss-target-select')?.value || 'REGIONAL_ADMIN';
    const category   = this.container.querySelector('#ss-category-select')?.value || 'POS_HARDWARE';
    const priority   = this.container.querySelector('#ss-priority-select')?.value || 'MEDIUM';
    const subject    = this.container.querySelector('#ss-subject-inp')?.value?.trim();
    const desc       = this.container.querySelector('#ss-desc-inp')?.value?.trim();

    if (!subject || !desc) {
      notificationStore.warning('Subject and description are required.');
      return;
    }

    try {
      const res = await apiClient.post('/api/v1/support/tickets', {
        category, targetRole, priority, subject, description: desc,
        reporterId: this.user?.id,
        reporterName: this.user?.username || 'Store Manager',
        reporterRole: 'storeManager',
        storeId: this.user?.storeId || 1
      });
      const code = res?.data?.ticketCode || res?.ticketCode || '';
      notificationStore.success(`Store ticket ${code} submitted to ${targetRole}.`);
      e.target.reset();
      this._initStoreInfo();
    } catch (err) {
      logger.error('StoreAdminSupport', 'Failed to submit store ticket', err);
      notificationStore.error(`Failed to submit store ticket: ${err.message || 'Server error'}`);
    }
  }

  destroy() {
    logger.info('StoreAdminSupport', 'Store Admin Support Hub unmounted.');
  }
}
