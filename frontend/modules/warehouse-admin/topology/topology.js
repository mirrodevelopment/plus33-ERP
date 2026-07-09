/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Modules Module
 * File              : topology.js
 * Path              : frontend/modules/warehouse/pages/topology.js
 * Purpose           : Frontend page component for the Modules Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/logger, store/notificationStore
 * Depends On        : core/logger, store/notificationStore
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend page component for the Modules Module UI. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../../core/logger.js';
import { notificationStore } from '../../../store/notificationStore.js';

export default class WmsTopologyPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Modules Module
   */
  mount(container, lifecycle) {
    logger.info('WmsTopologyPage', 'Rendering WMS physical topology layout...');
    
    container.innerHTML = `
      <div class="mb-lg">
        <h2 class="m-0" style="font-family: var(--font-display); font-weight: 800;">WMS Physical Topology</h2>
        <p class="m-0" style="color: var(--text-muted); font-size: 0.85rem;">Interactive physical warehousing layout and telemetry nodes</p>
      </div>

      <div class="grid grid-cols-12 gap-lg">
        <!-- SVG Layout Grid -->
        <div class="col-8 glass p-lg" style="border: 1px solid var(--border-color); border-radius: var(--radius-lg); height: 420px; display: flex; align-items: center; justify-content: center;">
          <svg width="100%" height="100%" viewBox="0 0 400 300" id="wms-svg-map" style="max-width: 400px; max-height: 300px;">
            <!-- Zone A (Top Left) -->
            <rect id="zone-a" class="zone-rect" x="20" y="20" width="160" height="110" rx="6" fill="rgba(201, 164, 106, 0.05)" stroke="var(--border-color)" stroke-width="2" style="cursor: pointer; transition: all 0.25s;" />
            <text x="100" y="80" fill="var(--text-secondary)" font-size="12" font-weight="700" text-anchor="middle" pointer-events="none">ZONE A (Raw)</text>
            
            <!-- Zone B (Top Right) -->
            <rect id="zone-b" class="zone-rect" x="220" y="20" width="160" height="110" rx="6" fill="rgba(201, 164, 106, 0.05)" stroke="var(--border-color)" stroke-width="2" style="cursor: pointer; transition: all 0.25s;" />
            <text x="300" y="80" fill="var(--text-secondary)" font-size="12" font-weight="700" text-anchor="middle" pointer-events="none">ZONE B (EV Assets)</text>
            
            <!-- Zone C (Bottom Left) -->
            <rect id="zone-c" class="zone-rect" x="20" y="170" width="160" height="110" rx="6" fill="rgba(201, 164, 106, 0.05)" stroke="var(--border-color)" stroke-width="2" style="cursor: pointer; transition: all 0.25s;" />
            <text x="100" y="230" fill="var(--text-secondary)" font-size="12" font-weight="700" text-anchor="middle" pointer-events="none">ZONE C (Dry Goods)</text>
            
            <!-- Zone D (Bottom Right) -->
            <rect id="zone-d" class="zone-rect" x="220" y="170" width="160" height="110" rx="6" fill="rgba(201, 164, 106, 0.05)" stroke="var(--border-color)" stroke-width="2" style="cursor: pointer; transition: all 0.25s;" />
            <text x="300" y="230" fill="var(--text-secondary)" font-size="12" font-weight="700" text-anchor="middle" pointer-events="none">ZONE D (Shipping)</text>
          </svg>
        </div>

        <!-- Telemetry Panel -->
        <div class="col-4 glass p-lg" style="border: 1px solid var(--border-color); border-radius: var(--radius-lg); display: flex; flex-direction: column; justify-content: space-between;">
          <div>
            <h3 id="panel-zone-title" style="font-family: var(--font-display); font-weight: 800; color: var(--accent-primary); border-bottom: 1px solid var(--border-color); padding-bottom: var(--spacing-sm); margin-bottom: var(--spacing-md);">Zone A (Raw)</h3>
            
            <div class="flex flex-col gap-md" id="panel-metrics-body">
              <div class="flex justify-between align-center">
                <span style="color: var(--text-secondary); font-size: 0.8rem;">Current Temperature:</span>
                <span style="font-weight: 700; font-size: 0.85rem;" id="txt-temp">18.4°C</span>
              </div>
              <div class="flex justify-between align-center">
                <span style="color: var(--text-secondary); font-size: 0.8rem;">Humidity Index:</span>
                <span style="font-weight: 700; font-size: 0.85rem;" id="txt-humidity">42.1%</span>
              </div>
              <div class="flex justify-between align-center">
                <span style="color: var(--text-secondary); font-size: 0.8rem;">AMR Robotics Nodes:</span>
                <span style="font-weight: 700; font-size: 0.85rem; color: var(--status-success);" id="txt-robots">3 Active</span>
              </div>
              <div class="flex justify-between align-center">
                <span style="color: var(--text-secondary); font-size: 0.8rem;">Occupancy Ratio:</span>
                <span style="font-weight: 700; font-size: 0.85rem;" id="txt-occupancy">65.2%</span>
              </div>
            </div>
          </div>

          <div style="font-size: 0.75rem; color: var(--text-muted); text-align: center; border-top: 1px solid var(--border-color); padding-top: var(--spacing-sm); margin-top: var(--spacing-md);">
            Click zones on the SVG map to check telemetries.
          </div>
        </div>
      </div>
    `;

    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Modules Module
   */
  bindEvents(container, lifecycle) {
    const zones = [
      { id: 'zone-a', title: 'Zone A (Raw)', temp: '18.4°C', humidity: '42.1%', robots: '3 Active', occupancy: '65.2%', alert: false },
      { id: 'zone-b', title: 'Zone B (EV Assets)', temp: '22.8°C', humidity: '38.5%', robots: '5 Active', occupancy: '84.0%', alert: 'EV battery telemetry logs monitored: charging grid overload warning.' },
      { id: 'zone-c', title: 'Zone C (Dry Goods)', temp: '20.1°C', humidity: '45.0%', robots: '2 Active', occupancy: '48.9%', alert: false },
      { id: 'zone-d', title: 'Zone D (Shipping)', temp: '19.5°C', humidity: '43.2%', robots: '4 Active', occupancy: '76.8%', alert: false }
    ];

    zones.forEach(z => {
      const rect = container.querySelector(`#${z.id}`);
      /**
       * Performs the fn operation in this module.
       * @memberof Modules Module
       */
      if (rect) {
        // Click handler
        /**
         * Performs the clickHandler operation in this module.
         * @memberof Modules Module
         */
        const clickHandler = () => {
          logger.info('WmsTopologyPage', `Selected warehouse: ${z.title}`);
          
          // Reset all zones highlight
          container.querySelectorAll('.zone-rect').forEach(r => {
            r.setAttribute('fill', 'rgba(201, 164, 106, 0.05)');
            r.setAttribute('stroke', 'var(--border-color)');
          });

          // Highlight selected
          rect.setAttribute('fill', 'rgba(201, 164, 106, 0.15)');
          rect.setAttribute('stroke', 'var(--accent-primary)');

          // Update telemetry panel
          container.querySelector('#panel-zone-title').innerText = z.title;
          container.querySelector('#txt-temp').innerText = z.temp;
          container.querySelector('#txt-humidity').innerText = z.humidity;
          container.querySelector('#txt-robots').innerText = z.robots;
          container.querySelector('#txt-occupancy').innerText = z.occupancy;

          /**
           * Performs the fn operation in this module.
           * @memberof Modules Module
           */
          if (z.alert) {
            notificationStore.warning(z.alert, 5000);
          } else {
            notificationStore.info(`Warehouse telemetry synced: ${z.title}`);
          }
        };

        rect.addEventListener('click', clickHandler);
        lifecycle.onCleanup(() => rect.removeEventListener('click', clickHandler));
        
        // Hover effects
        rect.addEventListener('mouseover', () => {
          if (rect.getAttribute('stroke') !== 'var(--accent-primary)') {
            rect.setAttribute('fill', 'rgba(201, 164, 106, 0.1)');
            rect.setAttribute('stroke', 'var(--border-color-hover)');
          }
        });
        rect.addEventListener('mouseout', () => {
          if (rect.getAttribute('stroke') !== 'var(--accent-primary)') {
            rect.setAttribute('fill', 'rgba(201, 164, 106, 0.05)');
            rect.setAttribute('stroke', 'var(--border-color)');
          }
        });
      }
    });

    // Set initial selection highlight
    const initRect = container.querySelector('#zone-a');
    /**
     * Performs the fn operation in this module.
     * @memberof Modules Module
     */
    if (initRect) {
      initRect.setAttribute('fill', 'rgba(201, 164, 106, 0.15)');
      initRect.setAttribute('stroke', 'var(--accent-primary)');
    }
  }
}



