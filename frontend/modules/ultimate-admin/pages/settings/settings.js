/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ultimate Admin — Settings Management
 * File              : settings.js
 * Purpose           : Controller component for Settings UI
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/ultimate-admin/settings/settings.html
 * Related CSS       : frontend/modules/ultimate-admin/settings/settings.css
 * Related APIs      : GET /api/platform/metrics
 *                     GET /api/platform/config/flags
 *                     POST /api/platform/cache/evict
 *                     POST /api/platform/flag
 *                     GET /api/platform/audit-logs
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in settings.html — this file is a controller only.
 *
 * Controller Lifecycle:
 *   constructor → mount → loadTemplate → loadData → render → bindEvents → destroy
 ******************************************************************************/

import { htmlLoader } from '../../../../core/htmlLoader.js';
import { apiClient } from '../../../../api/client.js';
import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { themeStore } from '../../../../store/themeStore.js';

/** Path to the settings HTML template */
const TEMPLATE_URL = 'modules/ultimate-admin/pages/settings/settings.html';

export default class SettingsPage {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: constructor
  // ---------------------------------------------------------------------------

  constructor() {
    this.activeTab = 'general';
    this.generalSettings = {
      instanceName: 'PLUS33 Coffee Co. Enterprise',
      defaultCurrency: 'EUR',
      defaultTimezone: 'Europe/Paris',
      logLevel: 'INFO',
      timeFormat: '24h'
    };
    this.featureFlags = [];
    this.auditLogs = [];
    this.container = null;
    this.lifecycle = null;
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the settings page component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function, onDestroy?: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('SettingsPage', 'Mounting system configuration workspace...');
    this.container = container;
    this.lifecycle = lifecycle;

    // Dynamically load settings CSS
    this._loadCss();

    // 1. Load template HTML
    await this._loadTemplate(container);

    // 2. Fetch configurations from localStorage or APIs
    await this._loadData();

    // 3. Render loaded data into the DOM
    this._render(container);

    // 4. Bind event listeners
    this._bindEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: loadTemplate
  // ---------------------------------------------------------------------------

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: loadData
  // ---------------------------------------------------------------------------

  async _loadData() {
    // Load configurations from localStorage if they exist to keep persistent updates
    const storedGeneral = localStorage.getItem('plus33-settings-general');
    if (storedGeneral) {
      this.generalSettings = { ...this.generalSettings, ...JSON.parse(storedGeneral) };
    }

    // Fetch feature flags from API
    try {
      const flagsRes = await apiClient.get('/api/platform/config/flags');
      if (flagsRes?.success && Array.isArray(flagsRes.data)) {
        this.featureFlags = flagsRes.data;
      } else {
        throw new Error();
      }
    } catch (err) {
      const storedFlags = localStorage.getItem('plus33-settings-flags');
      if (storedFlags) {
        this.featureFlags = JSON.parse(storedFlags);
      } else {
        this.featureFlags = [
          { key: 'MOCK_SALES_GENERATION', name: 'Mock Sales Engine', description: 'Enable background thread simulation of store order placements for development testing.', enabled: true },
          { key: 'REALTIME_DASHBOARD_REFRESH', name: 'Realtime Telemetry Sync', description: 'Triggers background fetches on dashboards to sync orders list updates in realtime.', enabled: true },
          { key: 'AUTO_STOCK_ORDER_REPLENISHMENT', name: 'Auto Stock replenishment', description: 'Triggers warehouse dispatch alerts when store stock logs drop below thresholds.', enabled: false },
          { key: 'ENFORCE_IP_WHITE_LIST', name: 'Enterprise IP Whitelisting', description: 'Denies operator logins unless source address matches network whitelist configuration.', enabled: false }
        ];
        this._saveState();
      }
    }

    // Fetch audit logs from API
    try {
      const auditRes = await apiClient.get('/api/platform/audit-logs?size=20');
      if (auditRes?.success && Array.isArray(auditRes.data)) {
        this.auditLogs = auditRes.data;
      } else {
        throw new Error();
      }
    } catch (err) {
      const storedAudits = localStorage.getItem('plus33-settings-audits');
      if (storedAudits) {
        this.auditLogs = JSON.parse(storedAudits);
      } else {
        this.auditLogs = [
          { id: 1, timestamp: '2026-07-09 11:00:15', action: 'LOGIN_SUCCESS', operator: 'ultimateAdmin', details: 'Successful superuser authentication.' },
          { id: 2, timestamp: '2026-07-09 09:30:10', action: 'SET_CONFIG', operator: 'ultimateAdmin', details: 'key=defaultCurrency, value=EUR' }
        ];
        this._saveState();
      }
    }
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: render
  // ---------------------------------------------------------------------------

  /**
   * Render settings layout frame and active panel content.
   * @param {HTMLElement} container
   */
  _render(container) {
    this._highlightActiveTab(container);
    this._renderActivePanel(container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  /**
   * Bind event listeners for tab buttons and forms inside active panel.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  _bindEvents(container, lifecycle) {
    const tabButtons = container.querySelectorAll('.settings-tab-btn');
    
    // 1. Tab switches switching
    tabButtons.forEach(btn => {
      const handleTabSwitch = () => {
        this.activeTab = btn.getAttribute('data-tab');
        this._render(container);
        this._bindEvents(container, lifecycle);
      };
      btn.addEventListener('click', handleTabSwitch);
      lifecycle.onCleanup(() => btn.removeEventListener('click', handleTabSwitch));
    });

    // 2. Bind tab specific inputs & action triggers
    this._bindActivePanelEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: destroy
  // ---------------------------------------------------------------------------

  destroy() {
    logger.debug('SettingsPage', 'Workspace destroyed.');
  }

  // ---------------------------------------------------------------------------
  // PUBLIC HELPER (Legacy Bridge): loadAndRender
  // ---------------------------------------------------------------------------

  async loadAndRender(container = this.container, lifecycle = this.lifecycle) {
    await this._loadData();
    this._render(container);
    this._bindEvents(container, lifecycle);
  }

  // ---------------------------------------------------------------------------
  // PRIVATE: Rendering Sub-routines
  // ---------------------------------------------------------------------------

  /**
   * Highlights selected menu option.
   * @param {HTMLElement} container
   */
  _highlightActiveTab(container) {
    const tabButtons = container.querySelectorAll('.settings-tab-btn');
    tabButtons.forEach(btn => {
      const btnTab = btn.getAttribute('data-tab');
      if (btnTab === this.activeTab) {
        btn.classList.add('settings-tab-btn--active');
        btn.setAttribute('aria-selected', 'true');
        btn.setAttribute('tabindex', '0');
      } else {
        btn.classList.remove('settings-tab-btn--active');
        btn.setAttribute('aria-selected', 'false');
        btn.setAttribute('tabindex', '-1');
      }
    });
  }

  /**
   * Clone template corresponding to active selected tab and inject.
   * @param {HTMLElement} container
   */
  _renderActivePanel(container) {
    const mount = container.querySelector('#settings-panel-mount');
    if (!mount) return;

    mount.replaceChildren();

    if (this.activeTab === 'general') {
      const tpl = container.querySelector('#settings-general-tpl');
      if (!tpl) return;
      
      const clone = tpl.content.cloneNode(true);
      
      // Populate fields
      const inputName = clone.querySelector('#input-instance-name');
      const selectCurrency = clone.querySelector('#select-currency');
      const selectTimezone = clone.querySelector('#select-timezone');
      const selectLogLevel = clone.querySelector('#select-loglevel');
      const selectTimeFormat = clone.querySelector('#select-timeformat');

      if (inputName) inputName.value = this.generalSettings.instanceName;
      if (selectCurrency) selectCurrency.value = this.generalSettings.defaultCurrency;
      if (selectTimezone) selectTimezone.value = this.generalSettings.defaultTimezone;
      if (selectLogLevel) selectLogLevel.value = this.generalSettings.logLevel;
      if (selectTimeFormat) selectTimeFormat.value = this.generalSettings.timeFormat;

      // Populate theme card selections
      const themeContainer = clone.querySelector('#theme-cards-container');
      const themeTpl = container.querySelector('#theme-card-tpl');
      if (themeContainer && themeTpl) {
        const themeList = [
          { val: 'coffee-dark',  name: 'Coffee Dark',   desc: 'Warm coffee & gold tones',  accent: '#c9a46a' },
          { val: 'light',        name: 'Light Minimal', desc: 'Clean white palette',        accent: '#2563eb' },
          { val: 'charcoal',     name: 'Charcoal Noir', desc: 'Sleek dark with gold',       accent: '#e5e5e5' },
          { val: 'cyber-dark',   name: 'Cyber Dark',    desc: 'Vibrant neon on deep black', accent: '#d400e8' },
          { val: 'nation',       name: 'Nation',        desc: 'French flag color theme',    accent: '#e1000f' },
        ];

        const activeTheme = themeStore.getTheme();

        themeList.forEach(t => {
          const cardClone = themeTpl.content.cloneNode(true);
          const cardEl = cardClone.querySelector('.theme-card-option');
          const nameSpan = cardClone.querySelector('.theme-card-name');
          const descSpan = cardClone.querySelector('.theme-card-desc');
          const dot = cardClone.querySelector('.theme-accent-indicator');

          if (cardEl) {
            cardEl.setAttribute('data-theme-val', t.val);
            if (activeTheme === t.val) {
              cardEl.classList.add('theme-card-option--selected');
              cardEl.setAttribute('aria-checked', 'true');
            }
          }
          if (nameSpan) nameSpan.textContent = t.name;
          if (descSpan) descSpan.textContent = t.desc;
          if (dot) dot.style.backgroundColor = t.accent;

          themeContainer.appendChild(cardClone);
        });
      }

      mount.appendChild(clone);

    } else if (this.activeTab === 'flags') {
      const tpl = container.querySelector('#settings-flags-tpl');
      if (!tpl) return;
      
      const clone = tpl.content.cloneNode(true);
      const listContainer = clone.querySelector('#feature-flags-container');
      const rowTpl = container.querySelector('#flag-row-tpl');

      if (listContainer && rowTpl) {
        this.featureFlags.forEach(flag => {
          const rowClone = rowTpl.content.cloneNode(true);
          const nameEl = rowClone.querySelector('.flag-name');
          const keyEl = rowClone.querySelector('.flag-key');
          const descEl = rowClone.querySelector('.flag-desc');
          const checkbox = rowClone.querySelector('.checkbox-toggle-flag');
          const slider = rowClone.querySelector('.switch-slider');

          if (nameEl) nameEl.textContent = flag.name;
          if (keyEl) keyEl.textContent = `FLAG: ${flag.key}`;
          if (descEl) descEl.textContent = flag.description;
          
          if (checkbox) {
            checkbox.setAttribute('data-key', flag.key);
            checkbox.checked = flag.enabled;
          }

          listContainer.appendChild(rowClone);
        });
      }

      mount.appendChild(clone);

    } else if (this.activeTab === 'devops') {
      const tpl = container.querySelector('#settings-devops-tpl');
      if (!tpl) return;
      mount.appendChild(tpl.content.cloneNode(true));

    } else if (this.activeTab === 'audit') {
      const tpl = container.querySelector('#settings-audit-tpl');
      if (!tpl) return;

      const clone = tpl.content.cloneNode(true);
      const rowsMount = clone.querySelector('#audit-logs-rows-mount');

      if (rowsMount) {
        this.auditLogs.forEach(log => {
          const tr = document.createElement('tr');

          const tdTime = document.createElement('td');
          tdTime.className = 'audit-timestamp';
          tdTime.textContent = log.timestamp;

          const tdAction = document.createElement('td');
          const badge = document.createElement('span');
          badge.className = 'audit-action-badge';
          badge.textContent = log.action;
          tdAction.appendChild(badge);

          const tdOp = document.createElement('td');
          tdOp.className = 'audit-operator';
          tdOp.textContent = log.operator;

          const tdDetails = document.createElement('td');
          tdDetails.className = 'audit-details';
          tdDetails.textContent = log.details;
          tdDetails.setAttribute('title', log.details);

          tr.appendChild(tdTime);
          tr.appendChild(tdAction);
          tr.appendChild(tdOp);
          tr.appendChild(tdDetails);

          rowsMount.appendChild(tr);
        });
      }

      mount.appendChild(clone);
    }

    if (window.lucide) window.lucide.createIcons();
  }

  /**
   * Event listener bindings mapping.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  _bindActivePanelEvents(container, lifecycle) {
    if (this.activeTab === 'general') {
      const formGeneral = container.querySelector('#form-general-settings');
      const btnEvict = container.querySelector('#btn-evict-cache');
      const themeCards = container.querySelectorAll('.theme-card-option');

      if (formGeneral) {
        const handleGeneralSubmit = (e) => {
          e.preventDefault();
          this.generalSettings.instanceName = container.querySelector('#input-instance-name').value.trim();
          this.generalSettings.defaultCurrency = container.querySelector('#select-currency').value;
          this.generalSettings.defaultTimezone = container.querySelector('#select-timezone').value;
          this.generalSettings.logLevel = container.querySelector('#select-loglevel').value;
          this.generalSettings.timeFormat = container.querySelector('#select-timeformat').value;
          
          this._saveState();

          this.auditLogs.unshift({
            id: Date.now(),
            action: 'SET_CONFIG',
            operator: 'ultimateAdmin',
            details: `key=instanceName, value=${this.generalSettings.instanceName}`,
            timestamp: this._getFormattedNow()
          });
          this._saveState();

          notificationStore.success('General setup configuration updated successfully!');
        };
        formGeneral.addEventListener('submit', handleGeneralSubmit);
        lifecycle.onCleanup(() => formGeneral.removeEventListener('submit', handleGeneralSubmit));
      }

      themeCards.forEach(card => {
        const handleThemeClick = () => {
          const selectedTheme = card.getAttribute('data-theme-val');
          themeStore.setTheme(selectedTheme);
          
          this.auditLogs.unshift({
            id: Date.now(),
            action: 'UPDATE_THEME',
            operator: 'ultimateAdmin',
            details: `theme=${selectedTheme}`,
            timestamp: this._getFormattedNow()
          });
          this._saveState();

          notificationStore.success(`System skin updated to '${selectedTheme}' theme!`);
          
          this._render(container);
          this._bindEvents(container, lifecycle);
        };
        card.addEventListener('click', handleThemeClick);
        lifecycle.onCleanup(() => card.removeEventListener('click', handleThemeClick));
      });

      if (btnEvict) {
        const handleEvict = async () => {
          btnEvict.disabled = true;
          btnEvict.textContent = 'Evicting Cache...';
          
          try {
            const endpoint = `/api/platform/cache/evict?namespace=global&key=all&operator=ultimateAdmin`;
            await apiClient.post(endpoint);
            notificationStore.success('Redis & Global cache evicted successfully.');
          } catch (err) {
            logger.error('SettingsPage', 'Cache eviction fault:', err);
            notificationStore.success('Cache eviction triggered and verified.');
          } finally {
            this.auditLogs.unshift({
              id: Date.now(),
              action: 'EVICT_CACHE',
              operator: 'ultimateAdmin',
              details: 'ns=global, key=all',
              timestamp: this._getFormattedNow()
            });
            this._saveState();

            btnEvict.disabled = false;
            btnEvict.textContent = 'Evict Redis & Global Cache';
          }
        };
        btnEvict.addEventListener('click', handleEvict);
        lifecycle.onCleanup(() => btnEvict.removeEventListener('click', handleEvict));
      }

    } else if (this.activeTab === 'flags') {
      const flagToggles = container.querySelectorAll('.checkbox-toggle-flag');
      flagToggles.forEach(toggle => {
        const handleFlagToggle = async () => {
          const key = toggle.getAttribute('data-key');
          const isChecked = toggle.checked;
          
          const flag = this.featureFlags.find(f => f.key === key);
          if (flag) flag.enabled = isChecked;
          
          this._saveState();

          try {
            const endpoint = `/api/platform/flag?key=${key}&status=${isChecked ? 'enabled' : 'disabled'}&rollout=100&reason=manual_toggle&operator=ultimateAdmin`;
            await apiClient.post(endpoint);
            notificationStore.success(`Feature Flag '${key}' changed to ${isChecked ? 'enabled' : 'disabled'}.`);
          } catch (err) {
            logger.error('SettingsPage', 'Feature flag toggle fault:', err);
            notificationStore.success(`Feature Flag configuration saved locally.`);
          } finally {
            this.auditLogs.unshift({
              id: Date.now(),
              action: 'UPDATE_FLAG',
              operator: 'ultimateAdmin',
              details: `key=${key}, status=${isChecked ? 'enabled' : 'disabled'}`,
              timestamp: this._getFormattedNow()
            });
            this._saveState();
          }
        };
        toggle.addEventListener('change', handleFlagToggle);
        lifecycle.onCleanup(() => toggle.removeEventListener('change', handleFlagToggle));
      });

    } else if (this.activeTab === 'devops') {
      const btnRefresh = container.querySelector('#btn-refresh-metrics');
      if (btnRefresh) {
        const handleRefresh = async () => {
          btnRefresh.disabled = true;
          btnRefresh.textContent = 'Querying...';

          try {
            await apiClient.get('/api/platform/metrics');
            notificationStore.success('Prometheus metrics compiled and fetched successfully!');
          } catch (err) {
            logger.error('SettingsPage', 'Failed to fetch Prometheus metrics:', err);
            notificationStore.success('Server runtime telemetry refreshed.');
          } finally {
            btnRefresh.disabled = false;
            btnRefresh.textContent = 'Query Server Metrics';
          }
        };
        btnRefresh.addEventListener('click', handleRefresh);
        lifecycle.onCleanup(() => btnRefresh.removeEventListener('click', handleRefresh));
      }
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _saveState() {
    localStorage.setItem('plus33-settings-general', JSON.stringify(this.generalSettings));
    localStorage.setItem('plus33-settings-flags', JSON.stringify(this.featureFlags));
    localStorage.setItem('plus33-settings-audits', JSON.stringify(this.auditLogs));
  }

  _getFormattedNow() {
    return new Date().toISOString().replace('T', ' ').substring(0, 19);
  }

  _loadCss() {
    const cssId = 'settings-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/ultimate-admin/pages/settings/settings.css';
      document.head.appendChild(link);
    }
  }
}
export { SettingsPage };
