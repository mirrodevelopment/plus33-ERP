/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : page.js
 * Path              : frontend/pages/docs/page.js
 * Purpose           : Frontend page component for the Pages Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/logger
 * Depends On        : core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend page component for the Pages Module UI. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../core/logger.js';

export default class DocsPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  mount(container, lifecycle) {
    logger.info('DocsPage', 'Rendering architecture documentation page...');
    
    container.innerHTML = `
      <div class="docs-header mb-lg">
        <h2 class="m-0" style="font-family: var(--font-display); font-weight: 800; color: var(--accent-primary);">Platform Architecture & UI Standards</h2>
        <p class="m-0" style="color: var(--text-muted); font-size: 0.85rem;">Official Developer Guidelines & Layout References</p>
      </div>

      <div class="grid grid-cols-12 gap-lg">
        <!-- Sidebar layout links -->
        <div class="col-4 glass p-md" style="border: 1px solid var(--border-color); border-radius: var(--radius-lg); height: fit-content;">
          <h4 style="font-family: var(--font-display); font-weight: 700; margin-bottom: var(--spacing-md);">Manual Categories</h4>
          <div class="flex flex-col gap-sm">
            <button class="doc-tab-btn active-tab" data-tab="arch" style="padding: var(--spacing-sm); border: none; background: rgba(255,255,255,0.03); color: var(--text-primary); text-align: left; cursor: pointer; border-radius: var(--radius-md); font-weight: 600; font-size: 0.8rem; border-left: 2px solid var(--accent-primary);">1. Platform Architecture</button>
            <button class="doc-tab-btn" data-tab="ui" style="padding: var(--spacing-sm); border: none; background: transparent; color: var(--text-secondary); text-align: left; cursor: pointer; border-radius: var(--radius-md); font-weight: 500; font-size: 0.8rem; border-left: 2px solid transparent;">2. UI / Styling Standards</button>
            <button class="doc-tab-btn" data-tab="widget" style="padding: var(--spacing-sm); border: none; background: transparent; color: var(--text-secondary); text-align: left; cursor: pointer; border-radius: var(--radius-md); font-weight: 500; font-size: 0.8rem; border-left: 2px solid transparent;">3. 12-Column Widget Engine</button>
            <button class="doc-tab-btn" data-tab="permissions" style="padding: var(--spacing-sm); border: none; background: transparent; color: var(--text-secondary); text-align: left; cursor: pointer; border-radius: var(--radius-md); font-weight: 500; font-size: 0.8rem; border-left: 2px solid transparent;">4. Permission & Roles Grid</button>
          </div>
        </div>

        <!-- Document Viewer Content Area -->
        <div class="col-8 glass p-lg" id="doc-viewer-mount" style="border: 1px solid var(--border-color); border-radius: var(--radius-lg); min-height: 400px; display: flex; flex-direction: column;">
          <!-- Tab content injected here -->
        </div>
      </div>
    `;

    this.bindEvents(container, lifecycle);
    this.renderTab('arch'); // Render first tab by default
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindEvents(container, lifecycle) {
    const tabs = container.querySelectorAll('.doc-tab-btn');
    tabs.forEach(tab => {
      /**
       * Handles the handler event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handler = () => {
        tabs.forEach(t => {
          t.classList.remove('active-tab');
          t.style.background = 'transparent';
          t.style.color = 'var(--text-secondary)';
          t.style.borderLeftColor = 'transparent';
        });
        tab.classList.add('active-tab');
        tab.style.background = 'rgba(255,255,255,0.03)';
        tab.style.color = 'var(--text-primary)';
        tab.style.borderLeftColor = 'var(--accent-primary)';
        
        const tabKey = tab.getAttribute('data-tab');
        this.renderTab(tabKey);
      };
      tab.addEventListener('click', handler);
      lifecycle.onCleanup(() => tab.removeEventListener('click', handler));
    });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  renderTab(key) {
    const docViewer = document.getElementById('doc-viewer-mount');
    if (!docViewer) return;

    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (key === 'arch') {
      docViewer.innerHTML = `
        <h3 style="font-family: var(--font-display); font-weight: 800; border-bottom: 1px solid var(--border-color); padding-bottom: var(--spacing-sm);">1. Platform Architecture</h3>
        <p>The PLUS33 ERP client application is structured as a **Vanilla JS Single Page Application (SPA)** that utilizes native standard ES modules (`import`/`export`).</p>
        
        <h4 style="font-weight: 700; margin-top: var(--spacing-md);">A. Request Lifecycle Mappings</h4>
        <pre style="background: rgba(0,0,0,0.15); padding: var(--spacing-md); border-radius: var(--radius-md); font-family: var(--font-mono); font-size: 0.75rem; border: 1px solid var(--border-color);">
Page View Component
  ↓
Service Layer API Trigger (e.g. DashboardService)
  ↓
Central ApiClient (Authorization: Bearer header sync)
  ↓
Spring Boot REST Controller Endpoint
  ↓
Standardized Envelope parsing (ApiResponse&lt;T&gt;)
        </pre>
        
        <h4 style="font-weight: 700; margin-top: var(--spacing-md);">B. Bootstrapping Flow</h4>
        <p>On initial page load, `index.html` loads `app/main.js` which triggers the boot loader `boot/app.js`. The loader sequentially loads the following subsystems:</p>
        <ol style="font-size: 0.85rem; padding-left: 1.2rem; color: var(--text-secondary);">
          <li><strong>Stores Recovery:</strong> Initializes Auth, Permission, and User states.</li>
          <li><strong>Session Recovery:</strong> Validates if active JWT exists to bypass logins.</li>
          <li><strong>Theme Applier:</strong> Inspects localStorage theme skins and mounts tags.</li>
          <li><strong>Global listeners:</strong> Triggers safety event hooks for unhandled faults.</li>
          <li><strong>Hash Router:</strong> Runs path compilation, matching page layouts.</li>
        </ol>
      `;
    } else if (key === 'ui') {
      docViewer.innerHTML = `
        <h3 style="font-family: var(--font-display); font-weight: 800; border-bottom: 1px solid var(--border-color); padding-bottom: var(--spacing-sm);">2. UI & Styling Standards</h3>
        <p>To deliver a premium visual representation, the platform complies with the following visual brand guidelines:</p>
        
        <ul style="font-size: 0.85rem; padding-left: 1.2rem; color: var(--text-secondary); display: flex; flex-direction: column; gap: var(--spacing-sm);">
          <li><strong>Typography:</strong> Montserrat display headers combined with Inter body styles loaded dynamically from Google Fonts.</li>
          <li><strong>Visual Hierarchy:</strong> Coffee Dark as default skin (`data-theme="coffee-dark"`), styled with Charcoal Black (#141414), Off White (#F5F5F5), and Coffee Gold accents (#C9A46A).</li>
          <li><strong>Frost Glass UI:</strong> Glassmorphic effects using `backdrop-filter: blur(12px)` and translucent border borders to simulate layers depth.</li>
          <li><strong>Micro-Animations:</strong> CSS transitions applied on interactions and entries (`animations.css` classes).</li>
        </ul>
      `;
    } else if (key === 'widget') {
      docViewer.innerHTML = `
        <h3 style="font-family: var(--font-display); font-weight: 800; border-bottom: 1px solid var(--border-color); padding-bottom: var(--spacing-sm);">3. 12-Column Widget Engine</h3>
        <p>Dashboards in PLUS33 ERP are constructed dynamically by loading role configurations (`layout.json` and `dashboard.json`) and instantiating widgets into their coordinate columns grid.</p>
        
        <h4 style="font-weight: 700; margin-top: var(--spacing-md);">Responsive Grid Column Classes</h4>
        <table style="width: 100%; border-collapse: collapse; text-align: left; font-size: 0.8rem;">
          <thead>
            <tr style="border-bottom: 1px solid var(--border-color); color: var(--text-muted);">
              <th style="padding: var(--spacing-sm) 0;">Class</th>
              <th style="padding: var(--spacing-sm);">Grid Spans</th>
              <th style="padding: var(--spacing-sm); text-align: right;">Screen Target</th>
            </tr>
          </thead>
          <tbody>
            <tr style="border-bottom: 1px solid rgba(255,255,255,0.03);">
              <td style="padding: var(--spacing-sm) 0; font-family: var(--font-mono);">col-3</td>
              <td style="padding: var(--spacing-sm);">Spans 3/12 width (4 items per row)</td>
              <td style="padding: var(--spacing-sm); text-align: right; color: var(--accent-primary);">KPI Metric Cards</td>
            </tr>
            <tr style="border-bottom: 1px solid rgba(255,255,255,0.03);">
              <td style="padding: var(--spacing-sm) 0; font-family: var(--font-mono);">col-4</td>
              <td style="padding: var(--spacing-sm);">Spans 4/12 width (3 items per row)</td>
              <td style="padding: var(--spacing-sm); text-align: right; color: var(--accent-primary);">Donut Distribution Charts</td>
            </tr>
            <tr style="border-bottom: 1px solid rgba(255,255,255,0.03);">
              <td style="padding: var(--spacing-sm) 0; font-family: var(--font-mono);">col-6</td>
              <td style="padding: var(--spacing-sm);">Spans 6/12 width (2 items per row)</td>
              <td style="padding: var(--spacing-sm); text-align: right; color: var(--accent-primary);">Operation Logs, Alerts Lists</td>
            </tr>
            <tr style="border-bottom: 1px solid rgba(255,255,255,0.03);">
              <td style="padding: var(--spacing-sm) 0; font-family: var(--font-mono);">col-8</td>
              <td style="padding: var(--spacing-sm);">Spans 8/12 width (66% row width)</td>
              <td style="padding: var(--spacing-sm); text-align: right; color: var(--accent-primary);">Line Performance Graphs</td>
            </tr>
            <tr style="border-bottom: 1px solid rgba(255,255,255,0.03);">
              <td style="padding: var(--spacing-sm) 0; font-family: var(--font-mono);">col-12</td>
              <td style="padding: var(--spacing-sm);">Spans 12/12 width (Full line banner)</td>
              <td style="padding: var(--spacing-sm); text-align: right; color: var(--accent-primary);">Full Width Detail Tables</td>
            </tr>
          </tbody>
        </table>
      `;
    } else if (key === 'permissions') {
      docViewer.innerHTML = `
        <h3 style="font-family: var(--font-display); font-weight: 800; border-bottom: 1px solid var(--border-color); padding-bottom: var(--spacing-sm);">4. Permission & Roles Grid</h3>
        <p>The client interface secures menus and page views dynamically depending on RBAC permissions maps. Backend authority checks enforce security permanently.</p>
        
        <table style="width: 100%; border-collapse: collapse; text-align: left; font-size: 0.8rem; margin-top: var(--spacing-md);">
          <thead>
            <tr style="border-bottom: 1px solid var(--border-color); color: var(--text-muted);">
              <th style="padding: var(--spacing-sm) 0;">Role Profile</th>
              <th style="padding: var(--spacing-sm);">Permission clearance Tag</th>
              <th style="padding: var(--spacing-sm); text-align: right;">Clearance Level</th>
            </tr>
          </thead>
          <tbody>
            <tr style="border-bottom: 1px solid rgba(255,255,255,0.03);">
              <td style="padding: var(--spacing-sm) 0; font-weight: 600;">Ultimate Admin</td>
              <td style="padding: var(--spacing-sm);">* (All Wildcard Permissions)</td>
              <td style="padding: var(--spacing-sm); text-align: right; color: var(--status-success);">Ultimate System Admin</td>
            </tr>
            <tr style="border-bottom: 1px solid rgba(255,255,255,0.03);">
              <td style="padding: var(--spacing-sm) 0; font-weight: 600;">Warehouse Admin</td>
              <td style="padding: var(--spacing-sm);">warehouse:view, warehouse:write, wms:count</td>
              <td style="padding: var(--spacing-sm); text-align: right; color: var(--accent-primary);">WMS Operations Coordinator</td>
            </tr>
            <tr style="border-bottom: 1px solid rgba(255,255,255,0.03);">
              <td style="padding: var(--spacing-sm) 0; font-weight: 600;">Store Admin</td>
              <td style="padding: var(--spacing-sm);">inventory:view, inventory:write</td>
              <td style="padding: var(--spacing-sm); text-align: right; color: var(--accent-primary);">Franchise Store Manager</td>
            </tr>
          </tbody>
        </table>
      `;
    }
  }
}



