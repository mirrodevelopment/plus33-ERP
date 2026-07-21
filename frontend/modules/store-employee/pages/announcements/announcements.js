/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Employee — Announcements Board
 * File              : announcements.js
 * Path              : frontend/modules/store-employee/pages/announcements/announcements.js
 * Purpose           : Read-only announcements board for store employees / baristas
 * Version           : 5.0.0 (Pure DOM Controller - Multi-Media, Reactions & Role Color Badges)
 *
 * Related HTML      : frontend/modules/store-employee/pages/announcements/announcements.html
 * Related CSS       : frontend/modules/store-employee/pages/announcements/announcements.css
 * Related APIs      : GET /api/v1/announcements
 *                     POST /api/v1/announcements/{id}/read
 *                     POST /api/v1/announcements/{id}/react
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';

const TEMPLATE_URL = 'modules/store-employee/pages/announcements/announcements.html';

export default class StoreEmployeeAnnouncements {

  constructor() {
    this.user = authStore.getUser();
    this.announcements = [];
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Lifecycle
  // ─────────────────────────────────────────────────────────────────────────

  async mount(container, lifecycle) {
    logger.info('StoreEmployeeAnnouncements', 'Mounting employee announcements board...');

    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);

    this._setupPolicyModal(container);

    await this._fetchAnnouncements();
    this._renderFeed(container);
    this._bindEvents(container, lifecycle);
  }

  destroy() {
    logger.info('StoreEmployeeAnnouncements', 'Employee announcements board destroyed.');
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Setup Helpers
  // ─────────────────────────────────────────────────────────────────────────

  _loadCss() {
    const id = 'store-employee-announcements-css';
    if (!document.getElementById(id)) {
      const link = document.createElement('link');
      link.id = id;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/announcements/announcements.css';
      document.head.appendChild(link);
    }
  }

  _setupPolicyModal(container) {
    const policyBar = container.querySelector('#btn-emp-ann-policy-bar');
    const modal     = container.querySelector('#modal-emp-ann-policy');
    const closeBtn  = container.querySelector('#btn-close-emp-policy-modal');

    if (policyBar && modal) {
      policyBar.addEventListener('click', () => {
        modal.style.display = 'flex';
      });
    }

    if (closeBtn && modal) {
      closeBtn.addEventListener('click', () => {
        modal.style.display = 'none';
      });
    }

    if (modal) {
      modal.addEventListener('click', (e) => {
        if (e.target === modal) {
          modal.style.display = 'none';
        }
      });
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Data Fetching
  // ─────────────────────────────────────────────────────────────────────────

  async _fetchAnnouncements() {
    try {
      const response = await apiClient.get('/api/v1/announcements');
      this.announcements = (response?.success && response.data) ? response.data : [];
    } catch (e) {
      logger.error('StoreEmployeeAnnouncements', 'Error reading published announcements:', e);
      this.announcements = [];
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Template Cloning Render Engine
  // ─────────────────────────────────────────────────────────────────────────

  _renderFeed(container) {
    const list = container.querySelector('#emp-ann-feed-list');
    if (!list) return;

    list.innerHTML = '';

    if (this.announcements.length === 0) {
      const emptyTmpl = container.querySelector('#tmpl-emp-ann-empty');
      if (emptyTmpl) {
        list.appendChild(emptyTmpl.content.cloneNode(true));
      }
      return;
    }

    const bulletinTmpl = container.querySelector('#tmpl-emp-ann-bulletin');
    if (!bulletinTmpl) return;

    this.announcements.forEach(ann => {
      const fragment  = bulletinTmpl.content.cloneNode(true);
      const cardEl    = fragment.querySelector('.emp-ann-bulletin');
      const badgeEl   = fragment.querySelector('.emp-ann-bulletin__priority-badge');
      const roleBadge = fragment.querySelector('.emp-ann-bulletin__role-badge');
      const readBadge = fragment.querySelector('.emp-ann-bulletin__read-badge');
      const btnRead   = fragment.querySelector('.btn-mark-read');
      const titleEl   = fragment.querySelector('.emp-ann-bulletin__title');
      const bodyEl    = fragment.querySelector('.emp-ann-bulletin__content');
      const slotEl    = fragment.querySelector('.emp-ann-bulletin__attachment-slot');
      const pubEl     = fragment.querySelector('.emp-ann-bulletin__publisher');
      const dateEl    = fragment.querySelector('.emp-ann-bulletin__date');

      // Priority Badge
      badgeEl.textContent = ann.priority;
      badgeEl.className = 'emp-ann-bulletin__priority-badge';
      if (ann.priority === 'Critical Alert') {
        badgeEl.classList.add('priority-badge--critical');
      } else if (ann.priority === 'Company Bulletin') {
        badgeEl.classList.add('priority-badge--bulletin');
      } else {
        badgeEl.classList.add('priority-badge--notice');
      }

      // Publisher Role Badge & Color Theme
      if (roleBadge && ann.publisherRole) {
        roleBadge.textContent = ann.publisherRole.replace('_', ' ');
        const color = ann.publisherColor || '#c9a46a';
        roleBadge.style.background = `${color}20`;
        roleBadge.style.color = color;
        roleBadge.style.border = `1px solid ${color}40`;

        // Apply role border color theme to bulletin card
        cardEl.style.borderLeftColor = color;
      }

      // Read status
      if (ann.read) {
        if (readBadge) readBadge.style.display = 'inline-block';
        if (btnRead) btnRead.style.display = 'none';
      } else if (btnRead) {
        btnRead.setAttribute('data-id', ann.id);
      }

      // Reactions count population
      const reactions = ann.reactions || {};
      ['thumbsUp', 'heart', 'lightbulb', 'coffee'].forEach(type => {
        const countSpan = fragment.querySelector(`.react-count-${type}`);
        if (countSpan) countSpan.textContent = reactions[type] || 0;

        const reactBtn = fragment.querySelector(`.btn-react[data-type="${type}"]`);
        if (reactBtn) reactBtn.setAttribute('data-id', ann.id);
      });

      // Title & Body
      titleEl.textContent = ann.title;
      bodyEl.textContent  = ann.content;

      // Media attachment rendering
      if (ann.imageUrl) {
        const attachNode = this._createAttachmentNode(container, ann.imageUrl, ann.mediaType);
        if (attachNode) slotEl.appendChild(attachNode);
      }

      // Metadata footer
      pubEl.textContent  = `Publisher: ${ann.publisher || 'Admin'} (${ann.publisherRole || 'System'})`;
      dateEl.textContent = `Date: ${ann.date || ''}${ann.expiresAt ? ` • Expires: ${ann.expiresAt}` : ''}`;

      list.appendChild(fragment);
    });
  }

  _createAttachmentNode(container, url, mediaType) {
    if (!url) return null;
    const lower = url.toLowerCase();
    const type  = mediaType ? mediaType.toUpperCase() : '';

    // Video (.mp4, .webm, .ogg, .mov)
    if (type === 'VIDEO' || ['.mp4', '.webm', '.ogg', '.mov'].some(ext => lower.endsWith(ext))) {
      const tmpl = container.querySelector('#tmpl-emp-ann-attach-video');
      if (!tmpl) return null;
      const frag  = tmpl.content.cloneNode(true);
      const video = frag.querySelector('video');
      if (video) video.src = url;
      return frag;
    }

    // Audio (.mp3, .wav, .aac, .m4a)
    if (type === 'AUDIO' || ['.mp3', '.wav', '.aac', '.m4a'].some(ext => lower.endsWith(ext))) {
      const tmpl = container.querySelector('#tmpl-emp-ann-attach-audio');
      if (!tmpl) return null;
      const frag  = tmpl.content.cloneNode(true);
      const audio = frag.querySelector('audio');
      if (audio) audio.src = url;
      return frag;
    }

    // PDF (.pdf)
    if (type === 'PDF' || lower.endsWith('.pdf')) {
      const tmpl = container.querySelector('#tmpl-emp-ann-attach-pdf');
      if (!tmpl) return null;
      const frag = tmpl.content.cloneNode(true);
      const link = frag.querySelector('a');
      if (link) link.href = url;
      return frag;
    }

    // Office Documents (.doc, .docx, .xls, .xlsx, .zip)
    if (type === 'DOCUMENT' || ['.doc', '.docx', '.xls', '.xlsx', '.zip'].some(ext => lower.endsWith(ext))) {
      const tmpl = container.querySelector('#tmpl-emp-ann-attach-doc');
      if (!tmpl) return null;
      const frag = tmpl.content.cloneNode(true);
      const link = frag.querySelector('a');
      if (link) {
        link.href = url;
        const fileName = url.substring(url.lastIndexOf('/') + 1);
        link.textContent = `Download ${fileName.length > 20 ? fileName.substring(0, 17) + '...' : fileName}`;
      }
      return frag;
    }

    // Fallback Image (.png, .jpg, .jpeg, .webp, .gif, .svg)
    const tmpl = container.querySelector('#tmpl-emp-ann-attach-img');
    if (!tmpl) return null;
    const frag = tmpl.content.cloneNode(true);
    const img  = frag.querySelector('img');
    if (img) img.src = url;
    return frag;
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Event Delegation & Handling
  // ─────────────────────────────────────────────────────────────────────────

  _bindEvents(container, lifecycle) {
    const list = container.querySelector('#emp-ann-feed-list');
    if (!list) return;

    list.addEventListener('click', async (e) => {
      // Mark as read click
      const readBtn = e.target.closest('.btn-mark-read');
      if (readBtn) {
        const id = readBtn.getAttribute('data-id');
        try {
          const res = await apiClient.post(`/api/v1/announcements/${id}/read`);
          if (res?.success) {
            notificationStore.success('Bulletin marked as read.');
            await this._fetchAnnouncements();
            this._renderFeed(container);
          }
        } catch (err) {
          logger.error('StoreEmployeeAnnouncements', 'Error marking announcement as read:', err);
        }
        return;
      }

      // Reaction button click
      const reactBtn = e.target.closest('.btn-react');
      if (reactBtn) {
        const id   = reactBtn.getAttribute('data-id');
        const type = reactBtn.getAttribute('data-type');
        try {
          const res = await apiClient.post(`/api/v1/announcements/${id}/react?type=${type}`);
          if (res?.success) {
            await this._fetchAnnouncements();
            this._renderFeed(container);
          }
        } catch (err) {
          logger.error('StoreEmployeeAnnouncements', 'Error reacting to announcement:', err);
        }
      }
    });
  }
}
