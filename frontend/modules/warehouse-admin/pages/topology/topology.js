/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Warehouse Admin — WMS Topology
 * File              : topology.js
 * Path              : frontend/modules/warehouse-admin/topology/topology.js
 * Purpose           : Controller component for WMS Topology layout page context
 * Version           : 2.0.0
 *
 * Related HTML      : frontend/modules/warehouse-admin/topology/topology.html
 * Related CSS       : frontend/modules/warehouse-admin/topology/topology.css
 *
 * Description
 * ---------------------------------------------------------------------------
 * Refactored to HTML + CSS + JS mixed architecture.
 * HTML structure lives in topology.html — this file is a controller only.
 ******************************************************************************/

import { logger } from '../../../../core/logger.js';
import { notificationStore } from '../../../../store/notificationStore.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';

/** Path to the topology HTML template */
const TEMPLATE_URL = 'modules/warehouse-admin/pages/topology/topology.html';

export default class WmsTopologyPage {

  // ---------------------------------------------------------------------------
  // LIFECYCLE: mount
  // ---------------------------------------------------------------------------

  /**
   * Mount the WMS Physical Topology component.
   * @param {HTMLElement} container
   * @param {{ onCleanup: Function }} lifecycle
   */
  async mount(container, lifecycle) {
    logger.info('WmsTopologyPage', 'Rendering WMS physical topology layout...');
    
    // Load CSS
    this._loadCss();

    // 1. Inject HTML template
    await this._loadTemplate(container);

    // 2. Bind event listeners
    this.bindEvents(container, lifecycle);
  }

  async _loadTemplate(container) {
    await htmlLoader.inject(TEMPLATE_URL, container);
  }

  // ---------------------------------------------------------------------------
  // LIFECYCLE: bindEvents
  // ---------------------------------------------------------------------------

  bindEvents(container, lifecycle) {
    const zones = [
      { id: 'zone-a', title: 'Zone A (Raw)', temp: '18.4°C', humidity: '42.1%', robots: '3 Active', occupancy: '65.2%', alert: false },
      { id: 'zone-b', title: 'Zone B (EV Assets)', temp: '22.8°C', humidity: '38.5%', robots: '5 Active', occupancy: '84.0%', alert: 'EV battery telemetry logs monitored: charging grid overload warning.' },
      { id: 'zone-c', title: 'Zone C (Dry Goods)', temp: '20.1°C', humidity: '45.0%', robots: '2 Active', occupancy: '48.9%', alert: false },
      { id: 'zone-d', title: 'Zone D (Shipping)', temp: '19.5°C', humidity: '43.2%', robots: '4 Active', occupancy: '76.8%', alert: false }
    ];

    zones.forEach(z => {
      const rect = container.querySelector(`#${z.id}`);
      if (rect) {
        const clickHandler = () => {
          logger.info('WmsTopologyPage', `Selected warehouse: ${z.title}`);
          
          // Reset all zones highlight
          container.querySelectorAll('.zone-rect').forEach(r => {
            r.style.fill = 'rgba(201, 164, 106, 0.05)';
            r.style.stroke = 'var(--border-color)';
          });

          // Highlight selected
          rect.style.fill = 'rgba(201, 164, 106, 0.15)';
          rect.style.stroke = 'var(--accent-primary)';

          // Update telemetry panel
          const panelTitle = container.querySelector('#panel-zone-title');
          const tempEl = container.querySelector('#txt-temp');
          const humidityEl = container.querySelector('#txt-humidity');
          const robotsEl = container.querySelector('#txt-robots');
          const occupancyEl = container.querySelector('#txt-occupancy');

          if (panelTitle) panelTitle.textContent = z.title;
          if (tempEl) tempEl.textContent = z.temp;
          if (humidityEl) humidityEl.textContent = z.humidity;
          if (robotsEl) robotsEl.textContent = z.robots;
          if (occupancyEl) occupancyEl.textContent = z.occupancy;

          if (z.alert) {
            notificationStore.warning(z.alert, 5000);
          } else {
            notificationStore.info(`Warehouse telemetry synced: ${z.title}`);
          }
        };

        rect.addEventListener('click', clickHandler);
        lifecycle.onCleanup(() => rect.removeEventListener('click', clickHandler));
        
        // Hover effects
        const handleMouseOver = () => {
          if (rect.style.stroke !== 'var(--accent-primary)') {
            rect.style.fill = 'rgba(201, 164, 106, 0.1)';
            rect.style.stroke = 'var(--border-color-hover)';
          }
        };
        const handleMouseOut = () => {
          if (rect.style.stroke !== 'var(--accent-primary)') {
            rect.style.fill = 'rgba(201, 164, 106, 0.05)';
            rect.style.stroke = 'var(--border-color)';
          }
        };

        rect.addEventListener('mouseover', handleMouseOver);
        rect.addEventListener('mouseout', handleMouseOut);

        lifecycle.onCleanup(() => {
          rect.removeEventListener('mouseover', handleMouseOver);
          rect.removeEventListener('mouseout', handleMouseOut);
        });
      }
    });

    // Set initial selection highlight
    const initRect = container.querySelector('#zone-a');
    if (initRect) {
      initRect.style.fill = 'rgba(201, 164, 106, 0.15)';
      initRect.style.stroke = 'var(--accent-primary)';
    }
  }

  // ---------------------------------------------------------------------------
  // PRIVATE STATE MANAGEMENT
  // ---------------------------------------------------------------------------

  _loadCss() {
    const cssId = 'wms-topology-page-css';
    if (!document.getElementById(cssId)) {
      const link = document.createElement('link');
      link.id = cssId;
      link.rel = 'stylesheet';
      link.href = 'modules/warehouse-admin/pages/topology/topology.css';
      document.head.appendChild(link);
    }
  }
}
