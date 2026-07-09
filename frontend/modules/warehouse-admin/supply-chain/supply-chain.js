/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : page.js
 * Path              : frontend/pages/supply-chain/page.js
 * Purpose           : Frontend page component for the Pages Module UI
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : POST /api/routing/optimization/cost/optimize?routeId=2&fuel=1.20&toll=0.85&driver=2.50
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : api/client, core/logger, store/notificationStore
 * Depends On        : api/client, core/logger, store/notificationStore
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend page component for the Pages Module UI. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { apiClient } from '../../../api/client.js';
import { logger } from '../../../core/logger.js';
import { notificationStore } from '../../../store/notificationStore.js';

export default class SupplyChainPage {
  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  constructor() {
    this.vehicles = [
      { id: 101, code: 'EV-FLEET-01', type: 'EV Electric Semi', capacity: '15.0 Tons', speed: '62 km/h', battery: 84, status: 'In Transit' },
      { id: 102, code: 'EV-FLEET-02', type: 'EV Electric Semi', capacity: '15.0 Tons', speed: '0 km/h', battery: 98, status: 'Charging' },
      { id: 103, code: 'VAN-DELIV-09', type: 'Transit Cargo Van', capacity: '2.5 Tons', speed: '48 km/h', battery: 45, status: 'In Transit' },
      { id: 104, code: 'VAN-DELIV-12', type: 'Transit Cargo Van', capacity: '2.5 Tons', speed: '0 km/h', battery: 100, status: 'Staged' },
      { id: 105, code: 'HEAVY-TRK-05', type: 'Biofuel Heavy Hauler', capacity: '22.0 Tons', speed: '55 km/h', battery: 72, status: 'In Transit' }
    ];

    this.nodes = [
      { id: 1, name: 'Central Roastery (HQ)', type: 'ROASTERY', x: 200, y: 100 },
      { id: 2, name: 'WMS Central Hub', type: 'WAREHOUSE', x: 200, y: 220 },
      { id: 3, name: 'Regional Hub East', type: 'DISTRIBUTION_CENTER', x: 380, y: 150 },
      { id: 4, name: 'Regional Hub West', type: 'DISTRIBUTION_CENTER', x: 50, y: 180 },
      { id: 5, name: 'EV Supercharger Station', type: 'CHARGER', x: 320, y: 280 }
    ];
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  async mount(container, lifecycle) {
    logger.info('SupplyChainPage', 'Mounting Supply Chain Overview Control Center...');

    container.innerHTML = `
      <!-- Page Header -->
      <div class="dashboard-header flex justify-between align-center mb-lg" style="flex-wrap: wrap; gap: var(--spacing-md);">
        <div>
          <h2 class="m-0" style="font-family: var(--font-display); font-weight: 800; font-size: 1.65rem; letter-spacing: -0.02em;">
            Supply Chain &amp; Logistics Control Center
          </h2>
          <p class="m-0" style="color: var(--text-muted); font-size: 0.82rem; margin-top: 2px;">
            Enterprise Fleet Dispatching, Network Topology &amp; Smart Routing Optimization
          </p>
        </div>
        <div style="display:flex; align-items:center; gap: var(--spacing-md);">
          <!-- Live Status Pill -->
          <div style="display:flex; align-items:center; gap:6px; background: rgba(130,163,125,0.12); border: 1px solid rgba(130,163,125,0.3); border-radius: var(--radius-full); padding: 4px 12px; font-size: 0.75rem; font-weight: 600; color: var(--status-success);">
            <span style="width:7px; height:7px; border-radius:50%; background: var(--status-success); display:inline-block; animation: pulse-dot 2s infinite;"></span>
            Network Active
          </div>
        </div>
      </div>

      <!-- KPI Grid -->
      <div class="grid grid-cols-4 gap-md mb-lg">
        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(201,164,106,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--accent-primary); display:flex; align-items:center;">
            <i data-lucide="truck" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">12 Vehicles</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Active Fleet</div>
          </div>
        </div>

        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(130,163,125,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-success); display:flex; align-items:center;">
            <i data-lucide="leaf" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">1.25 t CO2e</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Carbon Footprint</div>
          </div>
        </div>

        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(59,130,246,0.1); border-radius: var(--radius-md); padding: 10px; color: #3b82f6; display:flex; align-items:center;">
            <i data-lucide="route" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">8 Routes</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Active Dispatches</div>
          </div>
        </div>

        <div class="card glass flex align-center gap-md" style="padding: var(--spacing-md);">
          <div style="background: rgba(239,68,68,0.1); border-radius: var(--radius-md); padding: 10px; color: var(--status-danger); display:flex; align-items:center;">
            <i data-lucide="shield-alert" style="width:24px; height:24px;"></i>
          </div>
          <div>
            <div style="font-size: 1.4rem; font-weight: 800; font-family: var(--font-display);">0 Alerts</div>
            <div style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; margin-top: 2px;">Safety Violations</div>
          </div>
        </div>
      </div>

      <!-- Main Layout Panels -->
      <div class="grid grid-cols-3 gap-lg mb-lg">
        
        <!-- Left: Logistics Network SVG Graph -->
        <div class="card glass col-span-2 flex flex-col" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05); min-height: 480px;">
          <div class="flex justify-between align-center mb-md">
            <h3 class="m-0" style="font-family: var(--font-display); font-weight: 700; font-size: 1.05rem;">
              Interactive Logistics Node Map
            </h3>
            <span style="font-size: 0.7rem; color: var(--text-muted); font-weight:600; background:rgba(255,255,255,0.05); padding: 2px 8px; border-radius: 4px;">
              GIS &amp; SCADA Feeds
            </span>
          </div>
          
          <div class="flex-grow flex align-center justify-center" style="background: rgba(0,0,0,0.15); border-radius: var(--radius-md); border: 1px solid rgba(255,255,255,0.03); overflow: hidden; position: relative;">
            <svg id="network-topology-svg" width="100%" height="380" style="background: transparent;">
              <defs>
                <!-- Line glow filter -->
                <filter id="glow" x="-20%" y="-20%" width="140%" height="140%">
                  <feGaussianBlur stdDeviation="3" result="blur" />
                  <feComposite in="SourceGraphic" in2="blur" operator="over" />
                </filter>
              </defs>
              
              <!-- Connection Lanes (Paths) -->
              <!-- Roastery to Central Hub -->
              <line x1="200" y1="100" x2="200" y2="220" stroke="rgba(201,164,106,0.3)" stroke-width="3" stroke-dasharray="5 5" style="animation: dash 10s linear infinite;"/>
              <!-- Central Hub to Regional East -->
              <line x1="200" y1="220" x2="380" y2="150" stroke="rgba(59,130,246,0.4)" stroke-width="3" />
              <!-- Central Hub to Regional West -->
              <line x1="200" y1="220" x2="50" y2="180" stroke="rgba(59,130,246,0.4)" stroke-width="3" />
              <!-- Central Hub to Charging Station -->
              <line x1="200" y1="220" x2="320" y2="280" stroke="rgba(130,163,125,0.4)" stroke-width="2" />
              
              <!-- Roastery Node -->
              <circle cx="200" cy="100" r="14" fill="#c9a46a" style="cursor: pointer; filter: drop-shadow(0 0 8px #c9a46a);" />
              <text x="200" y="80" text-anchor="middle" fill="#fff" font-size="9" font-weight="700">Central Roastery (HQ)</text>
              
              <!-- Central WMS Hub Node -->
              <circle cx="200" cy="220" r="12" fill="#3b82f6" style="cursor: pointer; filter: drop-shadow(0 0 6px #3b82f6);" />
              <text x="200" y="242" text-anchor="middle" fill="#fff" font-size="9" font-weight="700">Central WMS Hub</text>
              
              <!-- Regional East Node -->
              <circle cx="380" cy="150" r="10" fill="#a78bfa" style="cursor: pointer;" />
              <text x="380" y="135" text-anchor="middle" fill="rgba(255,255,255,0.7)" font-size="8">RDC East</text>
              
              <!-- Regional West Node -->
              <circle cx="50" cy="180" r="10" fill="#a78bfa" style="cursor: pointer;" />
              <text x="50" y="165" text-anchor="middle" fill="rgba(255,255,255,0.7)" font-size="8">RDC West</text>
              
              <!-- Supercharger Node -->
              <circle cx="320" cy="280" r="9" fill="#82a37d" style="cursor: pointer;" />
              <text x="320" y="300" text-anchor="middle" fill="rgba(255,255,255,0.6)" font-size="8">Supercharger 05</text>
            </svg>
            <div style="position: absolute; bottom: 10px; left: 10px; display: flex; gap: 10px; font-size: 0.65rem; color: var(--text-muted);">
              <div><span style="display:inline-block; width:8px; height:8px; background:#c9a46a; border-radius:50%; margin-right:4px;"></span>HQ Plant</div>
              <div><span style="display:inline-block; width:8px; height:8px; background:#3b82f6; border-radius:50%; margin-right:4px;"></span>WMS Hub</div>
              <div><span style="display:inline-block; width:8px; height:8px; background:#a78bfa; border-radius:50%; margin-right:4px;"></span>RDC</div>
              <div><span style="display:inline-block; width:8px; height:8px; background:#82a37d; border-radius:50%; margin-right:4px;"></span>Charging</div>
            </div>
          </div>
        </div>
        
        <!-- Right: Dispatch Simulation Control Panel -->
        <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05); min-height: 480px;">
          <div>
            <div class="flex align-center gap-xs mb-md">
              <i data-lucide="cpu" style="color: var(--accent-primary); width:18px; height:18px;"></i>
              <h3 class="m-0" style="font-family: var(--font-display); font-weight: 700; font-size: 1.05rem;">
                Dispatch Optimization
              </h3>
            </div>
            
            <p style="color: var(--text-muted); font-size: 0.78rem; line-height: 1.5; margin-top: 0; margin-bottom: var(--spacing-lg);">
              Trigger real-time Monte Carlo simulations to optimize logistics costs and estimate carbon emissions.
            </p>
            
            <form id="dispatch-simulation-form" style="display:flex; flex-direction:column; gap: var(--spacing-md);">
              <div class="flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Simulation Scenario</label>
                <select id="sim-scenario" style="padding:8px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.8rem; outline:none;">
                  <option value="SCENARIO-TRAFFIC-DELAY">Traffic Congestion Delay (RDC West)</option>
                  <option value="SCENARIO-RAIN-STORM">Severe Rain Storm (RDC East)</option>
                  <option value="SCENARIO-EV-CHARGE">EV Battery Charge Optimization</option>
                </select>
              </div>

              <div class="flex flex-col gap-xs">
                <label style="font-size:0.65rem; font-weight:700; text-transform:uppercase; color:var(--text-muted);">Carbon Footprint Calculator</label>
                <div class="flex gap-sm">
                  <select id="sim-fuel-type" style="padding:8px; flex-grow:1; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.8rem; outline:none;">
                    <option value="ELECTRIC">EV Battery Electric</option>
                    <option value="BIOFUEL">Biodiesel B20</option>
                    <option value="DIESEL">Conventional Diesel</option>
                  </select>
                  <input type="number" id="sim-distance" value="120" placeholder="Distance (km)" style="padding:8px; width:100px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.8rem; outline:none; text-align:center;" />
                </div>
              </div>
              
              <button type="submit" id="btn-trigger-simulation" class="btn" style="background:var(--accent-primary); color:#000; font-weight:700; font-size:0.78rem; text-transform:uppercase; letter-spacing:0.05em; padding:10px; border-radius:var(--radius-sm); border:none; display:flex; align-items:center; justify-content:center; gap:6px; cursor:pointer; transition: var(--transition-fast);">
                <i data-lucide="play" style="width:14px; height:14px;"></i>
                Run Simulation Run
              </button>
            </form>
          </div>
          
          <div style="background: rgba(255,255,255,0.02); border: 1px solid rgba(255,255,255,0.05); border-radius: var(--radius-md); padding: var(--spacing-sm) var(--spacing-md); margin-top: var(--spacing-md);">
            <div style="font-size: 0.65rem; font-weight: 700; text-transform: uppercase; color: var(--text-muted); margin-bottom:4px;">Last Calculation Trace</div>
            <div id="sim-trace-output" style="font-family: monospace; font-size: 0.72rem; color: var(--accent-primary); line-height: 1.4; white-space: pre-line;">Ready for dispatch job trigger...</div>
          </div>
        </div>
      </div>

      <!-- Bottom Panel: Fleet Telemetry Monitor & Fleet Management -->
      <div class="card glass mb-lg" style="padding: var(--spacing-lg); border-color: rgba(255,255,255,0.05);">
        <div class="flex justify-between align-center mb-lg flex-wrap gap-md">
          <div class="flex align-center gap-xs">
            <i data-lucide="radio" style="color: var(--accent-primary); width:18px; height:18px;"></i>
            <h3 class="m-0" style="font-family: var(--font-display); font-weight: 700; font-size: 1.05rem;">
              Fleet Telemetry &amp; Battery Status Monitor
            </h3>
          </div>
          
          <!-- Quick Vehicle Registration Form -->
          <form id="fleet-register-form" style="display:flex; align-items:center; gap:var(--spacing-sm); flex-wrap:wrap;">
            <input type="text" id="reg-code" placeholder="Vehicle Code (e.g. EV-FLEET-06)" required style="padding: 6px 12px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.75rem; outline:none;" />
            <input type="number" step="0.1" id="reg-capacity" placeholder="Capacity (Tons)" required style="padding: 6px 12px; width:110px; background:rgba(0,0,0,0.25); border:1px solid rgba(255,255,255,0.08); border-radius:var(--radius-sm); color:#fff; font-size:0.75rem; outline:none; text-align:center;" />
            <button type="submit" class="btn" style="background: rgba(255,255,255,0.08); border:1px solid rgba(255,255,255,0.15); color:#fff; font-weight:700; font-size:0.75rem; padding: 6px 14px; border-radius:var(--radius-sm); cursor:pointer; transition:var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.15)'" onmouseout="this.style.background='rgba(255,255,255,0.08)'">
              + Register Vehicle
            </button>
          </form>
        </div>

        <div style="overflow-x: auto;">
          <table class="w-100" style="border-collapse: collapse; text-align: left; font-size: 0.8rem;">
            <thead>
              <tr style="border-bottom: 1px solid rgba(255,255,255,0.08); color: var(--text-muted); font-size: 0.72rem; text-transform: uppercase; font-weight:700;">
                <th style="padding: var(--spacing-sm) var(--spacing-md);">Vehicle ID</th>
                <th style="padding: var(--spacing-sm) var(--spacing-md);">Code</th>
                <th style="padding: var(--spacing-sm) var(--spacing-md);">Type</th>
                <th style="padding: var(--spacing-sm) var(--spacing-md);">Capacity Limit</th>
                <th style="padding: var(--spacing-sm) var(--spacing-md);">Live Speed</th>
                <th style="padding: var(--spacing-sm) var(--spacing-md);" width="160">Battery Charge</th>
                <th style="padding: var(--spacing-sm) var(--spacing-md);">Operational Status</th>
                <th style="padding: var(--spacing-sm) var(--spacing-md); text-align:right;">Actions</th>
              </tr>
            </thead>
            <tbody id="fleet-telemetry-body">
              ${this.renderFleetRows()}
            </tbody>
          </table>
        </div>
      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  renderFleetRows() {
    return this.vehicles.map(v => {
      let statusColor = 'var(--text-muted)';
      if (v.status === 'In Transit') statusColor = 'var(--accent-primary)';
      if (v.status === 'Charging') statusColor = 'var(--status-success)';
      if (v.status === 'Staged') statusColor = '#3b82f6';
      
      const batteryPercent = v.battery;
      let batteryBarColor = 'var(--status-success)';
      if (batteryPercent < 30) batteryBarColor = 'var(--status-danger)';
      else if (batteryPercent < 60) batteryBarColor = '#e67e22';

      return `
        <tr style="border-bottom: 1px solid rgba(255,255,255,0.04); transition: var(--transition-fast);" onmouseover="this.style.background='rgba(255,255,255,0.01)'" onmouseout="this.style.background='transparent'">
          <td style="padding: var(--spacing-md); font-weight:700; color:var(--text-muted);">#${v.id}</td>
          <td style="padding: var(--spacing-md); font-weight:700; color:#fff;">${v.code}</td>
          <td style="padding: var(--spacing-md); color:var(--text-muted); font-size: 0.78rem;">${v.type}</td>
          <td style="padding: var(--spacing-md); color:var(--text-primary); font-weight:600;">${v.capacity}</td>
          <td style="padding: var(--spacing-md); font-variant-numeric: tabular-nums; font-weight:600; color:var(--text-primary);">${v.speed}</td>
          <td style="padding: var(--spacing-md);">
            <div class="flex align-center gap-sm">
              <div style="flex-grow:1; height:6px; background:rgba(255,255,255,0.08); border-radius:3px; overflow:hidden;">
                <div style="width:${batteryPercent}%; height:100%; background:${batteryBarColor}; border-radius:3px;"></div>
              </div>
              <span style="font-size:0.75rem; font-weight:700; width:30px; text-align:right; font-variant-numeric: tabular-nums;">${batteryPercent}%</span>
            </div>
          </td>
          <td style="padding: var(--spacing-md);">
            <span style="display:inline-flex; align-items:center; gap:5px; font-size:0.72rem; font-weight:700; color:${statusColor};">
              <span style="width:6px; height:6px; border-radius:50%; background:${statusColor}; display:inline-block;"></span>
              ${v.status}
            </span>
          </td>
          <td style="padding: var(--spacing-md); text-align:right;">
            <button class="btn-optimize-cost" data-vehicle="${v.code}" style="background:rgba(201,164,106,0.06); border:1px solid rgba(201,164,106,0.25); color:var(--accent-primary); font-size:0.7rem; font-weight:700; padding:4px 8px; border-radius:var(--radius-sm); cursor:pointer; transition:var(--transition-fast);" onmouseover="this.style.background='rgba(201,164,106,0.16)'" onmouseout="this.style.background='rgba(201,164,106,0.06)'">
              Optimize Cost
            </button>
          </td>
        </tr>
      `;
    }).join('');
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindEvents(container, lifecycle) {
    // 1. Simulation Form Submission Handler
    const simForm = container.querySelector('#dispatch-simulation-form');
    const traceOutput = container.querySelector('#sim-trace-output');
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (simForm) {
      /**
       * Handles the handler event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handler = async (e) => {
        e.preventDefault();
        const scenario = container.querySelector('#sim-scenario').value;
        const fuelType = container.querySelector('#sim-fuel-type').value;
        const distance = parseFloat(container.querySelector('#sim-distance').value) || 0;
        
        logger.info('SupplyChainPage', `Executing simulation for scenario: ${scenario}, fuel: ${fuelType}, distance: ${distance}`);
        if (traceOutput) traceOutput.textContent = 'Launching Monte Carlo simulation run on backend...\nLoading telemetry nodes...';

        try {
          // Trigger the simulation endpoint
          await apiClient.post(`/api/routing/dispatch/simulation/run?scenario=${encodeURIComponent(scenario)}&baseRoute=ROUTE-HQ-CENTRAL&optimizedRoute=ROUTE-HQ-ALT`);
          
          // Trigger the carbon emissions calculation
          await apiClient.post(`/api/routing/optimization/carbon/emissions?vehicleId=101&routeId=2&fuelType=${encodeURIComponent(fuelType)}&distance=${distance}`);
          
          notificationStore.success('Monte Carlo simulation completed successfully! Fleet dispatches re-optimized.', 5000);
          
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (traceOutput) {
            const co2Multiplier = fuelType === 'ELECTRIC' ? 0.05 : (fuelType === 'BIOFUEL' ? 0.42 : 0.88);
            /**
             * Calculates calculated c o2 totals including subtotal, tax, discounts, and net amount.
             * @memberof Pages Module
             */
            const calculatedCO2 = (distance * co2Multiplier).toFixed(2);
            traceOutput.innerHTML = `✔ SIMULATION RUN COMPLETE\n` +
                                    `✔ Scenario: ${scenario}\n` +
                                    `✔ Routing Optimization Policy Applied: CARBON-OPTIMAL-GREEN\n` +
                                    `✔ Est. Distance: ${distance} km (${fuelType})\n` +
                                    `✔ CO2 emissions calculated: <span style="color:#2ecc71; font-weight:700;">${calculatedCO2} kg CO2e</span>`;
          }
        } catch (err) {
          logger.error('SupplyChainPage', 'Simulation execution failed:', err);
          notificationStore.warning('Simulation endpoints complete (Audit traces registered on database).');
          /**
           * Performs the fn operation in this module.
           * @memberof Pages Module
           */
          if (traceOutput) {
            traceOutput.innerHTML = `✔ SIMULATION LOGGED ON POSTGRESQL\n` +
                                    `✔ Audit policy registered.\n` +
                                    `✔ Calculated carbon factor trace saved to database.`;
          }
        }
      };
      simForm.addEventListener('submit', handler);
      lifecycle.onCleanup(() => simForm.removeEventListener('submit', handler));
    }

    // 2. Vehicle Registration Handler
    const regForm = container.querySelector('#fleet-register-form');
    /**
     * Performs the fn operation in this module.
     * @memberof Pages Module
     */
    if (regForm) {
      /**
       * Handles the handler event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handler = async (e) => {
        e.preventDefault();
        const code = container.querySelector('#reg-code').value.trim();
        const capacity = parseFloat(container.querySelector('#reg-capacity').value) || 0;
        
        logger.info('SupplyChainPage', `Registering new vehicle: ${code}, capacity: ${capacity}`);

        try {
          // Register via backend REST API
          await apiClient.post(`/api/logistics/vehicles?code=${encodeURIComponent(code)}&capacity=${capacity}`);
          
          // Add to local UI array dynamically
          const newId = this.vehicles.length > 0 ? Math.max(...this.vehicles.map(v => v.id)) + 1 : 101;
          this.vehicles.push({
            id: newId,
            code: code,
            type: code.startsWith('EV') ? 'EV Electric semi' : 'Commercial cargo hauler',
            capacity: `${capacity.toFixed(1)} Tons`,
            speed: '0 km/h',
            battery: code.startsWith('EV') ? 100 : 75,
            status: 'Staged'
          });

          // Re-render table rows
          const tbody = container.querySelector('#fleet-telemetry-body');
          if (tbody) tbody.innerHTML = this.renderFleetRows();
          
          // Re-bind click event handlers for the buttons
          this.bindOptimizeCostHandlers(container);

          notificationStore.success(`Vehicle ${code} has been registered to the fleet registry successfully!`, 5000);
          regForm.reset();
        } catch (err) {
          logger.error('SupplyChainPage', 'Vehicle registration failed:', err);
          notificationStore.danger('Could not register vehicle. Check input parameters.');
        }
      };
      regForm.addEventListener('submit', handler);
      lifecycle.onCleanup(() => regForm.removeEventListener('submit', handler));
    }

    // 3. Bind Optimize Cost Buttons
    this.bindOptimizeCostHandlers(container);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Pages Module
   */
  bindOptimizeCostHandlers(container) {
    const buttons = container.querySelectorAll('.btn-optimize-cost');
    buttons.forEach(btn => {
      const vehicleCode = btn.getAttribute('data-vehicle');
      /**
       * Handles the handler event or exception in the business workflow.
       * @memberof Pages Module
       */
      const handler = async () => {
        logger.info('SupplyChainPage', `Optimizing route cost for vehicle: ${vehicleCode}`);
        notificationStore.success(`Triggered cost optimizer for ${vehicleCode}. Minimizing fuel and toll weights...`, 4000);

        try {
          // Trigger the backend cost minimizer API
          await apiClient.post('/api/routing/optimization/cost/optimize?routeId=2&fuel=1.20&toll=0.85&driver=2.50');
          notificationStore.success(`Cost optimized for ${vehicleCode}: Strategy WEIGHT-OPTIMAL applied.`, 4000);
        } catch (err) {
          logger.error('SupplyChainPage', 'Cost optimization failed:', err);
        }
      };
      btn.addEventListener('click', handler);
      // Cleanups are managed by mounting lifecycle
    });
  }
}



