/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Regional Admin — Announcements
 * File              : announcements.js
 * Path              : frontend/modules/regional-admin/pages/announcements/announcements.js
 * Purpose           : Controller component for regional broadcast bulletin page
 * Version           : 5.0.0 (Pure DOM Controller - Multi-Media, Audit Toggle & Role Colors)
 *
 * Related HTML      : frontend/modules/regional-admin/pages/announcements/announcements.html
 * Related CSS       : frontend/modules/regional-admin/pages/announcements/announcements.css
 * Related APIs      : GET  /api/v1/announcements?includeDeleted=true
 *                     POST /api/v1/announcements
 *                     DELETE /api/v1/announcements/{id}
 *                     POST /api/upload-announcement-attachment
 ******************************************************************************/

import { authStore } from '../../../../store/authStore.js';
import { userStore } from '../../../../store/userStore.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';

const TEMPLATE_URL = 'modules/regional-admin/pages/announcements/announcements.html';

export default class RegionalAnnouncements {

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.announcements = [];
    this.includeDeleted = false;
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Lifecycle
  // ─────────────────────────────────────────────────────────────────────────

  async mount(container, lifecycle) {
    logger.info('RegionalAnnouncements', 'Mounting regional admin announcements view...');

    this._loadCss();
    await htmlLoader.inject(TEMPLATE_URL, container);

    this._applyScope(container);
    this._setupAuditToggle(container);
    this._setupPolicyModal(container);

    await this._fetchAnnouncements();
    this._renderFeed(container);
    this._bindEvents(container, lifecycle);
  }

  destroy() {
    logger.info('RegionalAnnouncements', 'Announcements page destroyed.');
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Setup Helpers
  // ─────────────────────────────────────────────────────────────────────────

  _loadCss() {
    const id = 'regional-announcements-css';
    if (!document.getElementById(id)) {
      const link = document.createElement('link');
      link.id = id;
      link.rel = 'stylesheet';
      link.href = 'modules/regional-admin/pages/announcements/announcements.css';
      document.head.appendChild(link);
    }
  }

  _applyScope(container) {
    const isNational = this.user?.role === 'nationalAdmin';
    const isUltimate = this.user?.role === 'ultimateAdmin';
    const scopeLabel  = isUltimate ? 'Enterprise' : isNational ? 'National' : 'Regional';
    const scopeLower  = isUltimate ? 'enterprise' : isNational ? 'nation'   : 'region';

    const setText = (id, text) => {
      const el = container.querySelector(`#${id}`);
      if (el) el.textContent = text;
    };

    setText('ann-scope-title',    `Broadcast ${scopeLabel} Bulletins`);
    setText('ann-scope-badge',    `${scopeLabel.toUpperCase()} BROADCAST`);
    setText('ann-compose-title',  `Compose ${scopeLabel} Bulletin`);
    setText('ann-scope-subtitle', `Publish storewide updates and compliance alerts to all stores & employees under your ${scopeLower}`);
    setText('ann-compose-desc',   `Broadcast a notification to all store managers & staff under your ${scopeLower}`);

    const placeholder = container.querySelector('#input-ann-title');
    if (placeholder) placeholder.placeholder = `e.g. ${scopeLabel} Standards Update`;

    const textarea = container.querySelector('#input-ann-content');
    if (textarea) textarea.placeholder = `Enter message for all ${scopeLower} store managers and store staff...`;

    const btnLabel = container.querySelector('#btn-broadcast-label');
    if (btnLabel) btnLabel.textContent = `Broadcast ${scopeLabel}wide`;
  }

  _setupAuditToggle(container) {
    const toggleBtn = container.querySelector('#btn-ann-audit-toggle');
    if (!toggleBtn) return;

    if (this.user?.role === 'ultimateAdmin') {
      toggleBtn.style.display = 'flex';
      toggleBtn.addEventListener('click', async () => {
        this.includeDeleted = !this.includeDeleted;
        toggleBtn.classList.toggle('ann-audit-toggle-btn--active', this.includeDeleted);
        const label = container.querySelector('#btn-ann-audit-label');
        if (label) {
          label.textContent = this.includeDeleted ? 'Hide Soft-Deleted (Audit)' : 'Show Soft-Deleted (Audit)';
        }
        await this._fetchAnnouncements();
        this._renderFeed(container);
      });
    }
  }

  _setupPolicyModal(container) {
    const policyBar = container.querySelector('#btn-ann-policy-bar');
    const modal     = container.querySelector('#modal-ann-policy');
    const closeBtn  = container.querySelector('#btn-close-policy-modal');

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
      const endpoint = `/api/v1/announcements${this.includeDeleted ? '?includeDeleted=true' : ''}`;
      const response = await apiClient.get(endpoint);
      this.announcements = (response?.success && response.data) ? response.data : [];
    } catch (e) {
      logger.error('RegionalAnnouncements', 'Error reading published announcements:', e);
      this.announcements = [];
    }
  }

  // ─────────────────────────────────────────────────────────────────────────
  // Template Cloning Render Engine
  // ─────────────────────────────────────────────────────────────────────────

  _renderFeed(container) {
    const list  = container.querySelector('#ann-feed-list');
    const count = container.querySelector('#ann-feed-count');
    if (!list) return;

    if (count) count.textContent = this.announcements.length;
    list.innerHTML = ''; // Clear container cleanly

    if (this.announcements.length === 0) {
      const emptyTmpl = container.querySelector('#tmpl-ann-empty');
      if (emptyTmpl) {
        list.appendChild(emptyTmpl.content.cloneNode(true));
      }
      return;
    }

    const bulletinTmpl = container.querySelector('#tmpl-ann-bulletin');
    if (!bulletinTmpl) return;

    this.announcements.forEach(ann => {
      const fragment  = bulletinTmpl.content.cloneNode(true);
      const cardEl    = fragment.querySelector('.ann-bulletin');
      const badgeEl   = fragment.querySelector('.ann-bulletin__priority-badge');
      const roleBadge = fragment.querySelector('.ann-bulletin__role-badge');
      const delBadge  = fragment.querySelector('.ann-bulletin__deleted-badge');
      const titleEl   = fragment.querySelector('.ann-bulletin__title');
      const btnDel    = fragment.querySelector('.btn-delete-announcement');
      const bodyEl    = fragment.querySelector('.ann-bulletin__content');
      const slotEl    = fragment.querySelector('.ann-bulletin__attachment-slot');
      const pubEl     = fragment.querySelector('.ann-bulletin__publisher');
      const dateEl    = fragment.querySelector('.ann-bulletin__date');

      // Soft deleted styling for audit view
      if (ann.isDeleted) {
        cardEl.classList.add('ann-bulletin--deleted');
        if (delBadge) delBadge.style.display = 'inline-block';
        if (btnDel) btnDel.style.display = 'none';
      }

      // Priority Badge
      badgeEl.textContent = ann.priority;
      badgeEl.className = 'ann-bulletin__priority-badge';
      if (ann.priority === 'Critical Alert') {
        badgeEl.classList.add('priority-badge--critical');
      } else if (ann.priority === 'Company Bulletin') {
        badgeEl.classList.add('priority-badge--bulletin');
      } else {
        badgeEl.classList.add('priority-badge--notice');
      }
      if (ann.priority === 'Critical Alert') {
        badgeEl.classList.add('ann-bulletin__priority-badge--critical');
      } else if (ann.priority === 'Company Bulletin') {
        badgeEl.classList.add('ann-bulletin__priority-badge--bulletin');
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

      // Role Hierarchy Deletion Check: Regional Admin (Rank 3) cannot delete superior messages (Rank 4, 5)
      const userRank = this._getRoleRank('REGIONAL_ADMIN');
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
      const tmpl = container.querySelector('#tmpl-ann-attach-video');
      if (!tmpl) return null;
      const frag  = tmpl.content.cloneNode(true);
      const video = frag.querySelector('video');
      if (video) video.src = url;
      return frag;
    }

    // Audio (.mp3, .wav, .aac, .m4a)
    if (type === 'AUDIO' || ['.mp3', '.wav', '.aac', '.m4a'].some(ext => lower.endsWith(ext))) {
      const tmpl = container.querySelector('#tmpl-ann-attach-audio');
      if (!tmpl) return null;
      const frag  = tmpl.content.cloneNode(true);
      const audio = frag.querySelector('audio');
      if (audio) audio.src = url;
      return frag;
    }

    // PDF (.pdf)
    if (type === 'PDF' || lower.endsWith('.pdf')) {
      const tmpl = container.querySelector('#tmpl-ann-attach-pdf');
      if (!tmpl) return null;
      const frag = tmpl.content.cloneNode(true);
      const link = frag.querySelector('a');
      if (link) link.href = url;
      return frag;
    }

    // Office Documents (.doc, .docx, .xls, .xlsx, .zip)
    if (type === 'DOCUMENT' || ['.doc', '.docx', '.xls', '.xlsx', '.zip'].some(ext => lower.endsWith(ext))) {
      const tmpl = container.querySelector('#tmpl-ann-attach-doc');
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
    const tmpl = container.querySelector('#tmpl-ann-attach-img');
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
    const form = container.querySelector('#form-broadcast-announcement');
    if (!form) return;

    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      const titleInput     = container.querySelector('#input-ann-title');
      const prioritySelect = container.querySelector('#select-ann-priority');
      const contentInput   = container.querySelector('#input-ann-content');
      const fileInput      = container.querySelector('#input-ann-file');
      const submitBtn      = container.querySelector('#btn-broadcast-submit');

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
          logger.error('RegionalAnnouncements', 'Error uploading media attachment:', uploadErr);
          notificationStore.danger('Failed to upload file attachment: ' + uploadErr.message);
          if (submitBtn) submitBtn.disabled = false;
          return;
        }
      }

      try {
        const payload  = { title, content, priority, imageUrl };
        const response = await apiClient.post('/api/v1/announcements', payload);

        if (response?.success) {
          notificationStore.success('Regional bulletin broadcasted successfully!');
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
        logger.error('RegionalAnnouncements', 'Error composing regional bulletin:', err);
        notificationStore.danger('Database error publishing bulletin circular.');
      } finally {
        if (submitBtn) submitBtn.disabled = false;
      }
    });
  }

  _bindDeleteDelegation(container) {
    const list = container.querySelector('#ann-feed-list');
    if (!list) return;

    list.addEventListener('click', async (e) => {
      const btn = e.target.closest('.btn-delete-announcement');
      if (!btn) return;

      const id = btn.getAttribute('data-id');
      if (!confirm('Are you sure you want to archive/delete this regional bulletin?')) return;

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
        logger.error('RegionalAnnouncements', 'Error deleting regional bulletin:', err);
        notificationStore.danger('Failed to execute database delete command.');
        btn.disabled = false;
      }
    });
  }
}
