/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module - Shift Supervisor Sub-Role
 * File              : announcements.js
 * Path              : frontend/modules/store-employee/supervisor-announcements/announcements.js
 * Purpose           : Controller component for Shift Supervisor announcements broadcast UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/supervisor-announcements/announcements.html
 * Related CSS       : frontend/modules/store-employee/supervisor-announcements/announcements.css
 * Related APIs      : GET /api/v1/announcements
 *                     POST /api/v1/announcements
 *                     DELETE /api/v1/announcements/:id
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in announcements.html — this file is a controller only.
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { apiClient } from '../../../../api/client.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the supervisor announcements HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/supervisor-announcements/announcements.html';

export default class SupervisorAnnouncements {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.announcements = [];
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the SupervisorAnnouncements component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('SupervisorAnnouncements', 'Mounting supervisor announcements view...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML layout template
    await this._loadTemplate(container);

    // 2. Fetch announcements from DB
    await this.fetchAnnouncements();

    // 3. Render layout elements
    this.render(container);

    // 4. Bind event listeners
    this.bindEvents(container, lifecycle);
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // DATA TELEMETRY SUB-ROUTINES
  // ---------------------------------------------------------------------------

  async fetchAnnouncements() {
    try {
      const response = await apiClient.get('/api/v1/announcements');
      if (response?.success && response.data) {
        this.announcements = response.data;
      } else {
        this.announcements = [];
      }
    } catch (e) {
      logger.error('SupervisorAnnouncements', 'Error reading announcements from database:', e);
      this.announcements = [];
    }
  }

  // ---------------------------------------------------------------------------
  // PUBLIC HELPER (Legacy Bridge): loadAndRender
  // ---------------------------------------------------------------------------

  async loadAndRender(container, lifecycle) {
    await this.fetchAnnouncements();
    this.render(container);
    this.bindEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  render(container) {
    // 1. Sync header text count
    const countEl = container.querySelector('#lbl-broadcast-count');
    if (countEl) countEl.textContent = String(this.announcements.length);

    // 2. Render Active Broadcast feed list
    this._renderFeed(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const form = container.querySelector('#form-broadcast-announcement');
    const deleteButtons = container.querySelectorAll('.btn-delete-announcement');

    // 1. Form submit handler to compose a new broadcast circular
    if (form) {
      const handleSubmitBroadcast = async (e) => {
        e.preventDefault();
        
        const titleInput = container.querySelector('#input-ann-title');
        const prioritySelect = container.querySelector('#select-ann-priority');
        const contentInput = container.querySelector('#input-ann-content');
        
        const title = titleInput.value.trim();
        const priority = prioritySelect.value;
        const content = contentInput.value.trim();
        
        if (!title || !content) {
          notificationStore.danger('Title and message content cannot be empty.');
          return;
        }

        try {
          const payload = { title, content, priority };
          const response = await apiClient.post('/api/v1/announcements', payload);

          if (response?.success) {
            notificationStore.success('Announcement broadcasted and saved to database successfully!');
            form.reset();
            await this.loadAndRender(container, lifecycle);
          } else {
            throw new Error(response.message || 'Database insert failed.');
          }
        } catch (err) {
          logger.error('SupervisorAnnouncements', 'Failed to publish announcement:', err);
          notificationStore.danger(`Broadcast failed: ${err.message}`);
        }
      };
      form.addEventListener('submit', handleSubmitBroadcast);
      lifecycle.onCleanup(() => form.removeEventListener('submit', handleSubmitBroadcast));
    }

    // 2. Delete announcement button handler
    deleteButtons.forEach(btn => {
      const handleDelete = async () => {
        const id = btn.getAttribute('data-id');
        try {
          const response = await apiClient.delete(`/api/v1/announcements/${id}`);
          if (response?.success) {
            notificationStore.success('Announcement removed from database.');
            await this.loadAndRender(container, lifecycle);
          } else {
            throw new Error(response.message || 'Database delete failed.');
          }
        } catch (err) {
          logger.error('SupervisorAnnouncements', 'Failed to delete announcement:', err);
          notificationStore.danger('Failed to delete announcement from database.');
        }
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderFeed(container) {
    const feedContainer = container.querySelector('#ann-feed-container');
    const emptyTpl = container.querySelector('#ann-empty-feed-tpl');
    const cardTpl = container.querySelector('#ann-item-card-tpl');

    if (!feedContainer) return;

    feedContainer.replaceChildren();

    if (this.announcements.length === 0) {
      if (emptyTpl) {
        feedContainer.appendChild(emptyTpl.content.cloneNode(true));
      }
      return;
    }

    this.announcements.forEach(ann => {
      if (!cardTpl) return;
      const clone = cardTpl.content.cloneNode(true);

      const row = clone.querySelector('.ann-item-row');
      const badge = clone.querySelector('.ann-priority-badge');
      const titleEl = clone.querySelector('.ann-title-text');
      const deleteBtn = clone.querySelector('.btn-delete-announcement');
      const contentEl = clone.querySelector('.ann-content-para');
      const publisherEl = clone.querySelector('.publisher-lbl strong');
      const dateEl = clone.querySelector('.date-lbl strong');

      if (row && ann.priority === 'Critical Alert') {
        row.className = 'ann-item-row ann-item-row--critical';
      }

      if (badge) {
        badge.textContent = ann.priority;
        if (ann.priority === 'Critical Alert') {
          badge.className = 'ann-priority-badge ann-priority-badge--critical font-bold';
        } else if (ann.priority === 'Company Bulletin') {
          badge.className = 'ann-priority-badge ann-priority-badge--company font-bold';
        } else {
          badge.className = 'ann-priority-badge ann-priority-badge--standard font-bold';
        }
      }

      if (titleEl) titleEl.textContent = ann.title;
      if (deleteBtn) deleteBtn.setAttribute('data-id', String(ann.id));
      if (contentEl) contentEl.textContent = ann.content;
      if (publisherEl) publisherEl.textContent = ann.publisher;
      if (dateEl) dateEl.textContent = ann.date;

      feedContainer.appendChild(clone);
    });
  }

  _loadCss() {
    const cssId = 'store-employee-supervisor-announcements-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/supervisor-announcements/announcements.css';
      document.head.appendChild(link);
    }
  }
}
export { SupervisorAnnouncements };
