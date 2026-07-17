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

  renderAttachment(url) {
    if (!url) return '';
    const lower = url.toLowerCase();
    if (lower.endsWith('.mp4') || lower.endsWith('.webm') || lower.endsWith('.ogg') || lower.endsWith('.mov')) {
      return `
        <div style="margin: 8px 0; border-radius: var(--radius-sm); overflow: hidden; border: 1px solid var(--border-color); max-height: 250px; background: #000;">
          <video src="${url}" controls style="width: 100%; height: auto; max-height: 250px;"></video>
        </div>
      `;
    } else if (lower.endsWith('.pdf')) {
      return `
        <div style="margin: 8px 0; padding: var(--spacing-sm); border-radius: var(--radius-sm); border: 1px solid var(--border-color); background: rgba(255,255,255,0.02); display: flex; align-items: center; justify-content: space-between; gap: var(--spacing-sm);">
          <div style="display: flex; align-items: center; gap: 8px;">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#ff6b6b" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M15 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7Z"/><path d="M14 2v4a2 2 0 0 0 2 2h4"/></svg>
            <span style="font-size: 0.78rem; color: var(--text-primary); font-weight: 600;">PDF Document Attachment</span>
          </div>
          <a href="${url}" target="_blank" class="btn" style="padding: 4px 10px; font-size: 0.7rem; border-radius: var(--radius-xs); border: 1px solid var(--accent-primary); background: transparent; color: var(--accent-primary); font-weight: 700; text-decoration: none;">Open PDF</a>
        </div>
      `;
    } else {
      return `
        <div style="margin: 8px 0; max-height: 180px; overflow: hidden; border-radius: var(--radius-sm); border: 1px solid var(--border-color);">
          <img src="${url}" alt="Attachment" style="width: 100%; height: auto; object-fit: cover; max-height: 180px;">
        </div>
      `;
    }
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
        const fileInput = container.querySelector('#input-ann-image');
        
        const title = titleInput.value.trim();
        const priority = prioritySelect.value;
        const content = contentInput.value.trim();
        
        if (!title || !content) {
          notificationStore.danger('Title and message content cannot be empty.');
          return;
        }

        let imageUrl = '';
        const file = fileInput && fileInput.files ? fileInput.files[0] : null;

        if (file) {
          try {
            notificationStore.info('Uploading attachment file to server...');
            const response = await fetch('/api/upload-announcement-attachment', {
              method: 'POST',
              headers: {
                'Content-Type': file.type,
                'X-File-Name': file.name
              },
              body: file
            });
            const data = await response.json();
            if (data && data.success && data.url) {
              imageUrl = data.url;
            } else {
              throw new Error(data.message || 'File server upload failed.');
            }
          } catch (uploadErr) {
            logger.error('SupervisorAnnouncements', 'Attachment upload failed:', uploadErr);
            notificationStore.danger(`File upload failed: ${uploadErr.message}`);
            return;
          }
        }

        try {
          const payload = { title, content, priority, imageUrl };
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
      const handleDelete = () => {
        const id = btn.getAttribute('data-id');
        this.showDeleteConfirmation(async () => {
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
        });
      };
      btn.addEventListener('click', handleDelete);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleDelete));
    });
  }

  showDeleteConfirmation(onConfirm) {
    const backdrop = document.createElement('div');
    backdrop.style.position = 'fixed';
    backdrop.style.top = '0';
    backdrop.style.left = '0';
    backdrop.style.width = '100vw';
    backdrop.style.height = '100vh';
    backdrop.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
    backdrop.style.backdropFilter = 'blur(8px)';
    backdrop.style.display = 'flex';
    backdrop.style.alignItems = 'center';
    backdrop.style.justifyContent = 'center';
    backdrop.style.zIndex = '9999';
    backdrop.style.animation = 'fadeIn 0.2s ease-out';

    const box = document.createElement('div');
    box.style.backgroundColor = '#1e1e1e';
    box.style.border = '1px solid #333';
    box.style.borderRadius = '12px';
    box.style.padding = '24px';
    box.style.maxWidth = '400px';
    box.style.width = '90%';
    box.style.boxShadow = '0 10px 25px rgba(0,0,0,0.5)';
    box.style.color = '#fff';
    box.style.textAlign = 'center';
    box.style.fontFamily = 'system-ui, -apple-system, sans-serif';
    box.style.animation = 'scaleUp 0.2s ease-out';

    box.innerHTML = `
      <div style="margin-bottom: 16px;">
        <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#ff6b6b" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="display: inline-block;"><path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>
      </div>
      <h3 style="margin: 0 0 8px 0; font-size: 1.25rem; font-weight: 700; color: #ff6b6b;">Delete Announcement</h3>
      <p style="margin: 0 0 24px 0; font-size: 0.9rem; color: #aaa; line-height: 1.5;">Are you sure you want to permanently delete this broadcast announcement? This action cannot be undone and will delete all database records and attachments.</p>
      <div style="display: flex; gap: 12px; justify-content: center;">
        <button id="confirm-cancel-btn" style="padding: 10px 18px; border-radius: 6px; border: 1px solid #444; background: transparent; color: #ccc; cursor: pointer; font-size: 0.9rem; font-weight: 600; transition: all 0.2s;">Cancel</button>
        <button id="confirm-delete-btn" style="padding: 10px 18px; border-radius: 6px; border: none; background: #ff6b6b; color: #fff; cursor: pointer; font-size: 0.9rem; font-weight: 600; transition: all 0.2s;">Delete Now</button>
      </div>
    `;

    backdrop.appendChild(box);
    document.body.appendChild(backdrop);

    const cancelBtn = box.querySelector('#confirm-cancel-btn');
    const deleteBtn = box.querySelector('#confirm-delete-btn');

    cancelBtn.onmouseenter = () => {
      cancelBtn.style.backgroundColor = 'rgba(255,255,255,0.05)';
      cancelBtn.style.color = '#fff';
    };
    cancelBtn.onmouseleave = () => {
      cancelBtn.style.backgroundColor = 'transparent';
      cancelBtn.style.color = '#ccc';
    };

    deleteBtn.onmouseenter = () => {
      deleteBtn.style.backgroundColor = '#ff5252';
    };
    deleteBtn.onmouseleave = () => {
      deleteBtn.style.backgroundColor = '#ff6b6b';
    };

    if (!document.getElementById('confirm-modal-animations')) {
      const style = document.createElement('style');
      style.id = 'confirm-modal-animations';
      style.innerHTML = `
        @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
        @keyframes scaleUp { from { transform: scale(0.95); opacity: 0; } to { transform: scale(1); opacity: 1; } }
      `;
      document.head.appendChild(style);
    }

    const close = () => {
      backdrop.style.animation = 'fadeIn 0.15s ease-in reverse';
      box.style.animation = 'scaleUp 0.15s ease-in reverse';
      setTimeout(() => {
        if (backdrop.parentNode) {
          document.body.removeChild(backdrop);
        }
      }, 140);
    };

    cancelBtn.onclick = () => {
      close();
    };

    deleteBtn.onclick = () => {
      close();
      onConfirm();
    };

    backdrop.onclick = (e) => {
      if (e.target === backdrop) {
        close();
      }
    };
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

      const attachContainer = clone.querySelector('.ann-attachment-container');
      if (attachContainer) {
        attachContainer.innerHTML = this.renderAttachment(ann.imageUrl);
      }

      if (publisherEl) publisherEl.textContent = ann.publisher + (ann.publisherRole ? ` (${ann.publisherRole})` : '');
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
