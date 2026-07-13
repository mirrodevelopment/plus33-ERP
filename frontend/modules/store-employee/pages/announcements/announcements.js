/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Store Employee Module
 * File              : announcements.js
 * Path              : frontend/modules/store-employee/announcements/announcements.js
 * Purpose           : Controller component for Barista announcements feed Board UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/store-employee/announcements/announcements.html
 * Related CSS       : frontend/modules/store-employee/announcements/announcements.css
 * Related APIs      : GET /api/v1/announcements
 *                     POST /api/v1/announcements/:id/read
 *                     POST /api/v1/announcements/:id/react?type=:type
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

/** Path to the announcements HTML template */
const TEMPLATE_URL = 'modules/store-employee/pages/announcements/announcements.html';

export default class StoreEmployeeAnnouncements {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.announcementsList = [];
    this.filterStatus = 'all'; // 'all', 'unread', 'critical'
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the StoreEmployeeAnnouncements component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('StoreEmployeeAnnouncements', 'Mounting Barista Announcements Page...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML layout template
    await this._loadTemplate(container);

    // 2. Fetch announcements from PostgreSQL database
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
        this.announcementsList = response.data;
      } else {
        this.announcementsList = [];
      }
    } catch (err) {
      logger.error('StoreEmployeeAnnouncements', 'Error fetching announcements from DB:', err);
      notificationStore.danger('Failed to retrieve announcements from database.');
      this.announcementsList = [];
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
    const unreadCount = this.announcementsList.filter(a => !a.read).length;
    const totalCount = this.announcementsList.length;

    // 1. Sync header text
    const labelUnread = container.querySelector('#lbl-unread-alerts-count');
    if (labelUnread) labelUnread.textContent = String(unreadCount);

    // 2. Sync filters ribbon pills text & active state
    const btnAll = container.querySelector('#btn-filter-all');
    const btnUnread = container.querySelector('#btn-filter-unread');
    const btnCritical = container.querySelector('#btn-filter-critical');

    if (btnAll) {
      btnAll.textContent = `All (${totalCount})`;
      btnAll.className = `filter-pill ${this.filterStatus === 'all' ? 'active' : ''}`;
    }
    if (btnUnread) {
      btnUnread.textContent = `Unread (${unreadCount})`;
      btnUnread.className = `filter-pill ${this.filterStatus === 'unread' ? 'active' : ''}`;
    }
    if (btnCritical) {
      btnCritical.className = `filter-pill ${this.filterStatus === 'critical' ? 'active' : ''}`;
    }

    // 3. Render Announcements Feed List
    this._renderFeed(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const overlay = container.querySelector('#ann-modal-overlay');
    const modalContent = container.querySelector('#ann-modal-content');
    const markAllBtn = container.querySelector('#btn-mark-all-read');

    const showModal = (htmlContent) => {
      if (overlay && modalContent) {
        modalContent.innerHTML = htmlContent;
        overlay.style.display = 'flex';
        overlay.setAttribute('aria-hidden', 'false');
        
        const closeBtn = modalContent.querySelector('.btn-close-modal');
        if (closeBtn) {
          closeBtn.addEventListener('click', () => hideModal());
        }
        if (window.lucide) window.lucide.createIcons();
      }
    };

    const hideModal = () => {
      if (overlay) {
        overlay.style.display = 'none';
        overlay.setAttribute('aria-hidden', 'true');
      }
    };

    if (overlay) {
      const handleOverlayClick = (e) => {
        if (e.target === overlay) hideModal();
      };
      overlay.addEventListener('click', handleOverlayClick);
      lifecycle.onCleanup(() => overlay.removeEventListener('click', handleOverlayClick));
    }

    // 1. Click Announcement to View Details & Mark Read
    const annCards = container.querySelectorAll('.ann-card');
    annCards.forEach(card => {
      const handleOpenCard = async () => {
        const id = parseInt(card.getAttribute('data-id'));
        const ann = this.announcementsList.find(a => a.id === id);
        if (!ann) return;

        // Mark as Read in DB
        try {
          await apiClient.post(`/api/v1/announcements/${ann.id}/read`);
          ann.read = true;
        } catch (e) {
          logger.error('StoreEmployeeAnnouncements', 'Error marking read on modal view:', e);
        }

        const isCrit = ann.priority === 'Critical Alert';
        const pClass = isCrit ? 'priority-label--critical' : 
                       (ann.priority === 'Standard Notice' ? 'priority-label--standard' : 'priority-label--normal');

        const detailHtml = `
          <div class="modal-header-split">
            <h3 class="modal-title">Bulletin circular</h3>
            <button class="btn-close-modal" type="button" aria-label="Close modal">
              <i data-lucide="x" aria-hidden="true"></i>
            </button>
          </div>
          <div class="modal-details-body">
            <div class="modal-top-row">
              <span class="priority-label ${pClass}">${ann.priority}</span>
              <span class="date-label text-muted">${ann.date}</span>
            </div>
            
            <h4 class="modal-ann-title">${ann.title}</h4>
            <p class="modal-content-paragraph">${ann.content}</p>
            
            <div class="modal-publisher-row">
              <span>Publisher: <strong>${ann.publisher}</strong></span>
              <span>Status: <strong style="color:var(--status-success);">Read</strong></span>
            </div>
            
            <button class="btn btn-primary btn-ack-receipt btn-close-modal" type="button">
              Acknowledge Receipt
            </button>
          </div>
        `;
        showModal(detailHtml);

        // Bind inner closing btn
        const innerClose = modalContent.querySelector('.btn-close-modal');
        if (innerClose) {
          const handleInnerClose = () => {
            hideModal();
            this.render(container);
            this.bindEvents(container, lifecycle);
          };
          innerClose.addEventListener('click', handleInnerClose);
        }
      };
      card.addEventListener('click', handleOpenCard);
      lifecycle.onCleanup(() => card.removeEventListener('click', handleOpenCard));
    });

    // Readed button click (stops card details modal from opening)
    const readCardBtns = container.querySelectorAll('.btn-mark-read-card');
    readCardBtns.forEach(btn => {
      const handleMarkRead = async (e) => {
        e.stopPropagation();
        const id = parseInt(btn.getAttribute('data-id'));
        const ann = this.announcementsList.find(a => a.id === id);
        if (ann) {
          try {
            await apiClient.post(`/api/v1/announcements/${ann.id}/read`);
            ann.read = true;
            notificationStore.success('Announcement marked as readed.');
            this.render(container);
            this.bindEvents(container, lifecycle);
          } catch (err) {
            logger.error('StoreEmployeeAnnouncements', 'Error marking read:', err);
            notificationStore.danger('Failed to mark read in database.');
          }
        }
      };
      btn.addEventListener('click', handleMarkRead);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleMarkRead));
    });

    // Reaction button click (stops card details modal from opening)
    const reactBtns = container.querySelectorAll('.btn-react');
    reactBtns.forEach(btn => {
      const handleReact = async (e) => {
        e.stopPropagation();
        const id = parseInt(btn.getAttribute('data-id'));
        const type = btn.getAttribute('data-type');
        const ann = this.announcementsList.find(a => a.id === id);
        if (ann) {
          try {
            await apiClient.post(`/api/v1/announcements/${id}/react?type=${type}`);
            if (!ann.reactions) {
              ann.reactions = { thumbsUp: 0, heart: 0, lightbulb: 0, coffee: 0 };
            }
            ann.reactions[type] = (ann.reactions[type] || 0) + 1;
            
            await this.fetchAnnouncements();
            this.render(container);
            this.bindEvents(container, lifecycle);
          } catch (err) {
            logger.error('StoreEmployeeAnnouncements', 'Reaction failed:', err);
          }
        }
      };
      btn.addEventListener('click', handleReact);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleReact));
    });

    // 2. Mark All as Read
    if (markAllBtn) {
      const handleMarkAllRead = async () => {
        markAllBtn.disabled = true;
        markAllBtn.textContent = 'Processing...';
        try {
          for (let ann of this.announcementsList) {
            if (!ann.read) {
              await apiClient.post(`/api/v1/announcements/${ann.id}/read`);
            }
          }
          notificationStore.success('All bulletins marked as read.');
          await this.fetchAnnouncements();
          this.render(container);
          this.bindEvents(container, lifecycle);
        } catch (err) {
          logger.error('StoreEmployeeAnnouncements', 'Error marking all as read:', err);
        } finally {
          markAllBtn.disabled = false;
          markAllBtn.innerHTML = '<i data-lucide="check" aria-hidden="true"></i> Mark all as Read';
          if (window.lucide) window.lucide.createIcons();
        }
      };
      markAllBtn.addEventListener('click', handleMarkAllRead);
      lifecycle.onCleanup(() => markAllBtn.removeEventListener('click', handleMarkAllRead));
    }

    // 3. Filters click
    const filterBtns = container.querySelectorAll('[data-filter]');
    filterBtns.forEach(btn => {
      const handleFilterClick = () => {
        this.filterStatus = btn.getAttribute('data-filter');
        this.render(container);
        this.bindEvents(container, lifecycle);
      };
      btn.addEventListener('click', handleFilterClick);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleFilterClick));
    });
  }

  // ---------------------------------------------------------------------------
  // PRIVATE RENDERING SUB-ROUTINES
  // ---------------------------------------------------------------------------

  _renderFeed(container) {
    const listContainer = container.querySelector('#ann-feed-list-container');
    const emptyTpl = container.querySelector('#ann-empty-feed-tpl');
    const cardTpl = container.querySelector('#ann-card-tpl');

    if (!listContainer) return;

    listContainer.replaceChildren();

    // Apply filters
    const filteredAnnouncements = this.announcementsList.filter(a => {
      if (this.filterStatus === 'unread') return !a.read;
      if (this.filterStatus === 'critical') return a.priority === 'Critical Alert';
      return true;
    });

    if (filteredAnnouncements.length === 0) {
      if (emptyTpl) {
        listContainer.appendChild(emptyTpl.content.cloneNode(true));
      }
      return;
    }

    filteredAnnouncements.forEach(ann => {
      if (!cardTpl) return;
      const clone = cardTpl.content.cloneNode(true);

      const card = clone.querySelector('.ann-card');
      const priorityLabel = clone.querySelector('.priority-label');
      const dateLabel = clone.querySelector('.date-label');
      const titleText = clone.querySelector('.title-text');
      const unreadDot = clone.querySelector('.unread-dot');
      const contentParagraph = clone.querySelector('.content-paragraph');
      const publisherLabel = clone.querySelector('.publisher-label');
      const markReadBtn = clone.querySelector('.btn-mark-read-card');
      const readedCheckmark = clone.querySelector('.readed-checkmark');

      // Card custom class bindings
      if (card) {
        card.setAttribute('data-id', String(ann.id));
        if (!ann.read) card.classList.add('unread');
        if (ann.priority === 'Critical Alert') card.classList.add('critical');
      }

      // Priority colorings
      if (priorityLabel) {
        priorityLabel.textContent = ann.priority;
        if (ann.priority === 'Critical Alert') {
          priorityLabel.classList.add('priority-label--critical');
        } else if (ann.priority === 'Standard Notice') {
          priorityLabel.classList.add('priority-label--standard');
        } else {
          priorityLabel.classList.add('priority-label--normal');
        }
      }

      if (dateLabel) dateLabel.textContent = ann.date;
      if (titleText) titleText.textContent = ann.title;
      if (unreadDot && ann.read) unreadDot.style.display = 'none';
      if (contentParagraph) contentParagraph.textContent = ann.content;
      if (publisherLabel) publisherLabel.textContent = `Publisher: ${ann.publisher}`;

      // Status buttons
      if (ann.read) {
        if (markReadBtn) markReadBtn.style.display = 'none';
      } else {
        if (readedCheckmark) readedCheckmark.style.display = 'none';
      }
      if (markReadBtn) markReadBtn.setAttribute('data-id', String(ann.id));

      // Reactions binding
      const reactions = ann.reactions || { thumbsUp: 0, heart: 0, lightbulb: 0, coffee: 0 };
      const reactButtons = clone.querySelectorAll('.btn-react');
      reactButtons.forEach(btn => {
        btn.setAttribute('data-id', String(ann.id));
        const rType = btn.getAttribute('data-type');
        const countSpan = btn.querySelector('.react-count');
        if (countSpan) {
          countSpan.textContent = String(reactions[rType] || 0);
        }
      });

      listContainer.appendChild(clone);
    });
  }

  _loadCss() {
    const cssId = 'store-employee-ann-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/store-employee/pages/announcements/announcements.css';
      document.head.appendChild(link);
    }
  }
}
export { StoreEmployeeAnnouncements };
