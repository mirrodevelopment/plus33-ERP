/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Admin — Announcements
 * File              : announcements.js
 * Path              : frontend/modules/store-admin/pages/announcements/announcements.js
 * Purpose           : Controller component for store admin broadcast bulletins page
 * Version           : 5.0.0 (Pure DOM Controller - Multi-Media & Role Color Badges)
 *
 * Related HTML      : frontend/modules/store-admin/pages/announcements/announcements.html
 * Related CSS       : frontend/modules/store-admin/pages/announcements/announcements.css
 * Related APIs      : GET  /api/v1/announcements
 *                     POST /api/v1/announcements
 *                     DELETE /api/v1/announcements/{id}
 *                     POST /api/upload-announcement-attachment
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';

const TEMPLATE_URL = 'modules/store-admin/pages/announcements/announcements.html';

export default class StoreAnnouncements {

  constructor() {
    this.user = authStore.getUser();
    this.announcements = [];
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Lifecycle
  // ─────────────────────────────────────────────────────────────────────────

  async mount(container, lifecycle) {
    logger.info('StoreAnnouncements', 'Mounting store admin announcements view...');

    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);

    this._setupPolicyModal(container);

    await this._fetchAnnouncements();
    this._renderFeed(container);
    this._bindEvents(container, lifecycle);
  }

  destroy() {
    logger.info('StoreAnnouncements', 'Announcements page destroyed.');
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Setup Helpers
  // ─────────────────────────────────────────────────────────────────────────

  _loadCss() {
    const id = 'store-admin-announcements-css';
    if (!document.getElementById(id)) {
      const link = document.createElement('link');
      link.id = id;
      link.rel = 'stylesheet';
      link.href = 'modules/store-admin/pages/announcements/announcements.css';
      document.head.appendChild(link);
    }
  }

  _setupPolicyModal(container) {
    const policyBar = container.querySelector('#btn-store-ann-policy-bar');
    const modal     = container.querySelector('#modal-store-ann-policy');
    const closeBtn  = container.querySelector('#btn-close-store-policy-modal');

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
      logger.error('StoreAnnouncements', 'Error reading published announcements:', e);
      this.announcements = [];
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Template Cloning Render Engine
  // ─────────────────────────────────────────────────────────────────────────

  _renderFeed(container) {
    const list  = container.querySelector('#store-ann-feed-list');
    const count = container.querySelector('#store-ann-feed-count');
    if (!list) return;

    if (count) count.textContent = this.announcements.length;
    list.innerHTML = '';

    if (this.announcements.length === 0) {
      const emptyTmpl = container.querySelector('#tmpl-store-ann-empty');
      if (emptyTmpl) {
        list.appendChild(emptyTmpl.content.cloneNode(true));
      }
      return;
    }

    const bulletinTmpl = container.querySelector('#tmpl-store-ann-bulletin');
    if (!bulletinTmpl) return;

    this.announcements.forEach(ann => {
      const fragment  = bulletinTmpl.content.cloneNode(true);
      const cardEl    = fragment.querySelector('.store-ann-bulletin');
      const badgeEl   = fragment.querySelector('.store-ann-bulletin__priority-badge');
      const roleBadge = fragment.querySelector('.store-ann-bulletin__role-badge');
      const titleEl   = fragment.querySelector('.store-ann-bulletin__title');
      const btnDel    = fragment.querySelector('.btn-delete-announcement');
      const bodyEl    = fragment.querySelector('.store-ann-bulletin__content');
      const slotEl    = fragment.querySelector('.store-ann-bulletin__attachment-slot');
      const pubEl     = fragment.querySelector('.store-ann-bulletin__publisher');
      const dateEl    = fragment.querySelector('.store-ann-bulletin__date');

      // Priority Badge
      badgeEl.textContent = ann.priority;
      badgeEl.className = 'store-ann-bulletin__priority-badge';
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

      // Title & Body
      titleEl.textContent = ann.title;
      bodyEl.textContent  = ann.content;

      // Role Hierarchy Deletion Check: Store Admin (Rank 2) cannot delete superior messages (Rank 3, 4, 5)
      const userRank = this._getRoleRank('STORE_ADMIN');
      const pubRank  = this._getRoleRank(ann.publisherRole);
      if (btnDel) {
        if (userRank >= pubRank) {
          btnDel.setAttribute('data-id', ann.id);
        } else {
          btnDel.remove(); // Remove Delete button if message was published by superior role
        }
      }

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

  _getRoleRank(role) {
    if (!role) return 0;
    const norm = String(role).toUpperCase().replace(/-/g, '_').replace(/ /g, '_');
    if (norm.includes('ULTIMATE')) return 5;
    if (norm.includes('NATIONAL')) return 4;
    if (norm.includes('REGIONAL')) return 3;
    if (norm.includes('STORE_ADMIN') || norm === 'STORE') return 2;
    if (norm.includes('SUPERVISOR')) return 1;
    return 0;
  }

  _createAttachmentNode(container, url, mediaType) {
    if (!url) return null;
    const lower = url.toLowerCase();
    const type  = mediaType ? mediaType.toUpperCase() : '';

    // Video (.mp4, .webm, .ogg, .mov)
    if (type === 'VIDEO' || ['.mp4', '.webm', '.ogg', '.mov'].some(ext => lower.endsWith(ext))) {
      const tmpl = container.querySelector('#tmpl-store-ann-attach-video');
      if (!tmpl) return null;
      const frag  = tmpl.content.cloneNode(true);
      const video = frag.querySelector('video');
      if (video) video.src = url;
      return frag;
    }

    // Audio (.mp3, .wav, .aac, .m4a)
    if (type === 'AUDIO' || ['.mp3', '.wav', '.aac', '.m4a'].some(ext => lower.endsWith(ext))) {
      const tmpl = container.querySelector('#tmpl-store-ann-attach-audio');
      if (!tmpl) return null;
      const frag  = tmpl.content.cloneNode(true);
      const audio = frag.querySelector('audio');
      if (audio) audio.src = url;
      return frag;
    }

    // PDF (.pdf)
    if (type === 'PDF' || lower.endsWith('.pdf')) {
      const tmpl = container.querySelector('#tmpl-store-ann-attach-pdf');
      if (!tmpl) return null;
      const frag = tmpl.content.cloneNode(true);
      const link = frag.querySelector('a');
      if (link) link.href = url;
      return frag;
    }

    // Office Documents (.doc, .docx, .xls, .xlsx, .zip)
    if (type === 'DOCUMENT' || ['.doc', '.docx', '.xls', '.xlsx', '.zip'].some(ext => lower.endsWith(ext))) {
      const tmpl = container.querySelector('#tmpl-store-ann-attach-doc');
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
    const tmpl = container.querySelector('#tmpl-store-ann-attach-img');
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
    this._bindForm(container);
    this._bindDeleteDelegation(container);
  }

  _bindForm(container) {
    const form = container.querySelector('#form-store-broadcast-announcement');
    if (!form) return;

    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      const titleInput     = container.querySelector('#input-store-ann-title');
      const prioritySelect = container.querySelector('#select-store-ann-priority');
      const contentInput   = container.querySelector('#input-store-ann-content');
      const fileInput      = container.querySelector('#input-store-ann-file');
      const submitBtn      = container.querySelector('#btn-store-broadcast-submit');

      const title    = titleInput.value.trim();
      const priority = prioritySelect.value;
      const content  = contentInput.value.trim();

      if (!title || !content) {
        notificationStore.danger('Title and message content cannot be empty.');
        return;
      }

      if (submitBtn) submitBtn.disabled = true;

      let imageUrl = '';
      const file = fileInput?.files?.[0];

      if (file) {
        try {
          notificationStore.info('Uploading attachment file to server...');
          const uploadRes = await fetch('/api/upload-announcement-attachment', {
            method: 'POST',
            headers: { 'Content-Type': file.type, 'X-File-Name': file.name },
            body: file
          });
          const uploadData = await uploadRes.json();
          if (uploadData?.success && uploadData.url) {
            imageUrl = uploadData.url;
          } else {
            throw new Error(uploadData?.message || 'File server upload failed.');
          }
        } catch (uploadErr) {
          logger.error('StoreAnnouncements', 'Error uploading media attachment:', uploadErr);
          notificationStore.danger('Failed to upload file attachment: ' + uploadErr.message);
          if (submitBtn) submitBtn.disabled = false;
          return;
        }
      }

      try {
        const payload  = { title, content, priority, imageUrl };
        const response = await apiClient.post('/api/v1/announcements', payload);

        if (response?.success) {
          notificationStore.success('Storewide bulletin broadcasted successfully!');
          titleInput.value = '';
          contentInput.value = '';
          if (fileInput) fileInput.value = '';
          prioritySelect.selectedIndex = 0;

          await this._fetchAnnouncements();
          this._renderFeed(container);
        } else {
          notificationStore.danger(response?.message || 'Failed to broadcast announcement.');
        }
      } catch (err) {
        logger.error('StoreAnnouncements', 'Error composing store bulletin:', err);
        notificationStore.danger('Database error publishing bulletin circular.');
      } finally {
        if (submitBtn) submitBtn.disabled = false;
      }
    });
  }

  _bindDeleteDelegation(container) {
    const list = container.querySelector('#store-ann-feed-list');
    if (!list) return;

    list.addEventListener('click', async (e) => {
      const btn = e.target.closest('.btn-delete-announcement');
      if (!btn) return;

      const id = btn.getAttribute('data-id');
      if (!confirm('Are you sure you want to archive/delete this store bulletin?')) return;

      btn.disabled = true;

      try {
        const res = await apiClient.delete(`/api/v1/announcements/${id}`);
        if (res?.success) {
          notificationStore.success('Bulletin archived & marked as deleted (kept in DB 60 days).');
          await this._fetchAnnouncements();
          this._renderFeed(container);
        } else {
          notificationStore.danger(res?.message || 'Failed to delete bulletin.');
          btn.disabled = false;
        }
      } catch (err) {
        logger.error('StoreAnnouncements', 'Error deleting store bulletin:', err);
        notificationStore.danger('Failed to execute database delete command.');
        btn.disabled = false;
      }
    });
  }
}
