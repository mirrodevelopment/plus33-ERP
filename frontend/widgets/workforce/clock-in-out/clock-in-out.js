/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Workforce / Store Employee
 * Component         : Clock In & Clock Out Widget
 * File              : clock-in-out.js
 * Path              : frontend/widgets/workforce/clock-in-out/clock-in-out.js
 * Purpose           : Controller class for Clock In/Out timecard & Geofence Map
 ******************************************************************************/

import { apiClient } from '../../../api/client.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';
import { htmlLoader } from '../../../core/htmlLoader.js';

const TEMPLATE_URL = 'widgets/workforce/clock-in-out/clock-in-out.html';
const CSS_ID = 'clock-in-out-widget-css';
const CSS_URL = 'widgets/workforce/clock-in-out/clock-in-out.css';

/** Lazy-load Leaflet script & CSS once */
let leafletPromise = null;
function ensureLeaflet() {
  if (window.L) return Promise.resolve();
  if (leafletPromise) return leafletPromise;
  leafletPromise = new Promise((resolve, reject) => {
    const link = document.createElement('link');
    link.rel = 'stylesheet';
    link.href = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.css';
    document.head.appendChild(link);

    const script = document.createElement('script');
    script.src = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.js';
    script.onload = () => resolve();
    script.onerror = () => reject(new Error('Failed to load Leaflet library'));
    document.head.appendChild(script);
  });
  return leafletPromise;
}

/** Haversine formula to compute distance in metres */
function haversineMeters(lat1, lon1, lat2, lon2) {
  const R = 6371000;
  const dLat = (lat2 - lat1) * Math.PI / 180;
  const dLon = (lon2 - lon1) * Math.PI / 180;
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2);
  return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
}

export class ClockInOutWidget {
  constructor(options = {}) {
    this.options = options;
    this.container = null;
    this.state = {
      clockedIn: false,
      checkInTime: null,
      shiftName: 'Morning Shift',
      shiftHours: '08:00 AM - 04:00 PM',
      breakName: '12:00 PM - 12:30 PM',
      breakHours: 'Lunch Break (Paid)',
      storeCoords: null
    };

    this._clockInterval = null;
    this._map = null;
    this._userMarker = null;
    this._storeMarker = null;
    this._geofenceCircle = null;
    this._routeLine = null;
    this._lastPos = null;
  }

  /** Mount the widget into a DOM container */
  async mount(container) {
    this.container = container;
    this._loadCss();

    // 1. Inject HTML layout template
    await htmlLoader.inject(TEMPLATE_URL, container);

    // 2. Fetch data (today's attendance + store coordinates)
    await this.loadData();

    // 3. Render state & start live clock
    this.render();
    this._startClock();

    // 4. Bind action buttons
    this._bindEvents();

    // 5. Auto-initialize GPS Map
    this._autoInitMap();
  }

  /** Load CSS stylesheet dynamically */
  _loadCss() {
    if (!document.getElementById(CSS_ID)) {
      const link = document.createElement('link');
      link.id = CSS_ID;
      link.rel = 'stylesheet';
      link.href = CSS_URL;
      document.head.appendChild(link);
    }
  }

  /** Fetch attendance state and store location */
  async loadData() {
    try {
      const todayRes = await apiClient.request('/attendance/today');
      if (todayRes?.success && todayRes.data) {
        this.state = { ...this.state, ...todayRes.data };
      }

      // Fetch user's store GPS location
      const meRes = await apiClient.get('/api/v1/auth/me');
      const storeId = meRes?.data?.storeId;
      if (storeId) {
        const storeRes = await apiClient.get(`/api/v1/stores/${storeId}`);
        if (storeRes?.success && storeRes.data) {
          const s = storeRes.data;
          if (s.latitude != null && s.longitude != null) {
            this.state.storeCoords = {
              lat: parseFloat(s.latitude),
              lon: parseFloat(s.longitude),
              radiusMeters: s.geofenceRadiusMeters || 200,
            };
          }
        }
      }
    } catch (err) {
      logger.error('ClockInOutWidget', 'Failed loading data', err);
    }
  }

  /** Render widget UI state */
  render() {
    if (!this.container) return;

    const btnClockIn = this.container.querySelector('#btn-clock-in');
    const btnClockOut = this.container.querySelector('#btn-clock-out');
    const badge = this.container.querySelector('#clock-duty-badge');
    const badgeText = this.container.querySelector('#clock-duty-text');

    const elapsedBlock = this.container.querySelector('#clock-elapsed-block');
    const breakBlock = this.container.querySelector('#clock-break-block');

    if (this.state.clockedIn) {
      if (btnClockIn) btnClockIn.disabled = true;
      if (btnClockOut) btnClockOut.disabled = false;
      if (badge) badge.className = 'clock-status-badge clock-status-in';
      if (badgeText) {
        const timeStr = this.state.checkInTime ? new Date(this.state.checkInTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '';
        badgeText.textContent = `Clocked In ${timeStr ? 'since ' + timeStr : ''}`;
      }
      if (elapsedBlock) elapsedBlock.style.display = 'block';
      if (breakBlock) breakBlock.style.display = 'none';
    } else {
      if (btnClockIn) btnClockIn.disabled = false;
      if (btnClockOut) btnClockOut.disabled = true;
      if (badge) badge.className = 'clock-status-badge clock-status-out';
      if (badgeText) badgeText.textContent = 'Clocked Out';
      if (elapsedBlock) elapsedBlock.style.display = 'none';
      if (breakBlock) breakBlock.style.display = 'block';
    }

    if (window.lucide) window.lucide.createIcons();
  }

  /** Start live digital clock */
  _startClock() {
    const timeEl = this.container.querySelector('#clock-run-time');
    const dateEl = this.container.querySelector('#clock-run-date');

    const tick = () => {
      const now = new Date();
      if (timeEl) {
        timeEl.textContent = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true });
      }
      if (dateEl) {
        dateEl.textContent = now.toLocaleDateString([], { weekday: 'short', month: 'short', day: 'numeric', year: 'numeric' });
      }

      // Update elapsed time if clocked in
      if (this.state.clockedIn && this.state.checkInTime) {
        const elapsedEl = this.container.querySelector('#clock-elapsed-value');
        if (elapsedEl) {
          const diffMs = now - new Date(this.state.checkInTime);
          const hrs = String(Math.floor(diffMs / 3600000)).padStart(2, '0');
          const mins = String(Math.floor((diffMs % 3600000) / 60000)).padStart(2, '0');
          const secs = String(Math.floor((diffMs % 60000) / 1000)).padStart(2, '0');
          elapsedEl.textContent = `${hrs}:${mins}:${secs}`;
        }
      }
    };

    tick();
    this._clockInterval = setInterval(tick, 1000);
  }

  /** Bind event listeners */
  _bindEvents() {
    const btnClockIn = this.container.querySelector('#btn-clock-in');
    const btnClockOut = this.container.querySelector('#btn-clock-out');
    const btnRecenter = this.container.querySelector('#btn-recenter-my-location');
    const btnRefresh = this.container.querySelector('#btn-refresh-map');
    const btnRoute = this.container.querySelector('#btn-get-fastest-route');

    if (btnClockIn) btnClockIn.addEventListener('click', () => this.handleClockIn());
    if (btnClockOut) btnClockOut.addEventListener('click', () => this.handleClockOut());
    if (btnRecenter) btnRecenter.addEventListener('click', () => this._recenterMap());
    if (btnRefresh) btnRefresh.addEventListener('click', () => this._autoInitMap());
    if (btnRoute) btnRoute.addEventListener('click', () => this._calculateFastestRoute());
  }

  /** Handle Clock In action */
  async handleClockIn() {
    const btnClockIn = this.container.querySelector('#btn-clock-in');
    if (btnClockIn) btnClockIn.disabled = true;

    this._updateGpsStatus('locating', 'Acquiring GPS location…');

    let pos = null;
    try {
      pos = await new Promise((resolve, reject) => {
        if (!navigator.geolocation) { reject(new Error('NO_GPS')); return; }
        navigator.geolocation.getCurrentPosition(resolve, reject,
          { enableHighAccuracy: true, timeout: 10000, maximumAge: 0 });
      });
      await this._showLocationOnMap(pos.coords.latitude, pos.coords.longitude, pos.coords.accuracy);
    } catch (_) {
      this._updateGpsStatus('error', 'GPS required to clock in');
    }

    const gpsStr = pos ? `${pos.coords.latitude},${pos.coords.longitude}` : '';
    try {
      const res = await apiClient.post('/attendance/check-in', { gps: gpsStr });
      if (res?.success) {
        notificationStore.success('Clock In Successful! Welcome on shift.');
        this.state.clockedIn = true;
        this.state.checkInTime = new Date().toISOString();
        this.render();
        if (this.options.onClockIn) this.options.onClockIn(res);
      } else {
        notificationStore.danger(res?.message || 'Clock In Failed.');
        if (btnClockIn) btnClockIn.disabled = false;
      }
    } catch (err) {
      notificationStore.danger('Error clocking in: ' + err.message);
      if (btnClockIn) btnClockIn.disabled = false;
    }
  }

  /** Handle Clock Out action */
  async handleClockOut() {
    const btnClockOut = this.container.querySelector('#btn-clock-out');
    if (btnClockOut) btnClockOut.disabled = true;

    this._updateGpsStatus('locating', 'Acquiring GPS location…');

    let pos = null;
    try {
      pos = await new Promise((resolve, reject) => {
        if (!navigator.geolocation) { reject(new Error('NO_GPS')); return; }
        navigator.geolocation.getCurrentPosition(resolve, reject,
          { enableHighAccuracy: true, timeout: 10000, maximumAge: 0 });
      });
      await this._showLocationOnMap(pos.coords.latitude, pos.coords.longitude, pos.coords.accuracy);
    } catch (_) {
      this._updateGpsStatus('error', 'GPS location acquired');
    }

    const gpsStr = pos ? `${pos.coords.latitude},${pos.coords.longitude}` : '';
    try {
      const res = await apiClient.post('/attendance/check-out', { gps: gpsStr });
      if (res?.success) {
        notificationStore.success('Clock Out Successful. Have a great rest!');
        this.state.clockedIn = false;
        this.state.checkInTime = null;
        this.render();
        if (this.options.onClockOut) this.options.onClockOut(res);
      } else {
        notificationStore.danger(res?.message || 'Clock Out Failed.');
        if (btnClockOut) btnClockOut.disabled = false;
      }
    } catch (err) {
      notificationStore.danger('Error clocking out: ' + err.message);
      if (btnClockOut) btnClockOut.disabled = false;
    }
  }

  /** Auto-initialize map on load */
  async _autoInitMap() {
    await new Promise(r => setTimeout(r, 400));
    this._updateGpsStatus('locating', 'Acquiring GPS location…');

    try {
      const pos = await new Promise((resolve, reject) => {
        if (!navigator.geolocation) { reject(new Error('no-geo')); return; }
        navigator.geolocation.getCurrentPosition(resolve, reject,
          { enableHighAccuracy: true, timeout: 12000, maximumAge: 10000 });
      });
      this._lastPos = pos;
      await this._showLocationOnMap(pos.coords.latitude, pos.coords.longitude, pos.coords.accuracy);
    } catch (err) {
      if (this.state.storeCoords) {
        await ensureLeaflet();
        const mapEl = this.container.querySelector('#clock-geofence-map-canvas');
        const placeholder = this.container.querySelector('#clock-map-placeholder');
        if (mapEl && !this._map) {
          if (placeholder) placeholder.style.display = 'none';
          this._map = window.L.map(mapEl, {
            zoomControl: true, scrollWheelZoom: false, attributionControl: false
          }).setView([this.state.storeCoords.lat, this.state.storeCoords.lon], 15);
          window.L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 19 }).addTo(this._map);

          const storeIcon = window.L.divIcon({
            className: '',
            html: `<div style="width:22px;height:22px;border-radius:50%;background:#c9a46a;border:3px solid #fff;display:flex;align-items:center;justify-content:center;font-size:12px;box-shadow:0 2px 8px rgba(0,0,0,.4)">☕</div>`,
            iconSize: [22, 22], iconAnchor: [11, 11]
          });
          this._storeMarker = window.L.marker([this.state.storeCoords.lat, this.state.storeCoords.lon], { icon: storeIcon })
            .addTo(this._map).bindPopup('<b>☕ Store Location</b>');

          this._geofenceCircle = window.L.circle(
            [this.state.storeCoords.lat, this.state.storeCoords.lon],
            { radius: this.state.storeCoords.radiusMeters || 200, color: '#c9a46a', fillColor: '#c9a46a', fillOpacity: 0.08, weight: 2, dashArray: '6,4' }
          ).addTo(this._map);

          setTimeout(() => { if (this._map) this._map.invalidateSize(); }, 200);
        }
        this._updateGpsStatus('error', '⚠ GPS unavailable — showing store location');
      } else {
        this._updateGpsStatus('error', 'GPS unavailable — enable location access');
      }
    }
  }

  /** Render location pin & store geofence on map */
  async _showLocationOnMap(userLat, userLon, accuracy) {
    this._updateGpsStatus('locating', 'Getting location…');
    await ensureLeaflet();

    const mapEl = this.container.querySelector('#clock-geofence-map-canvas');
    if (!mapEl) return;

    const placeholder = this.container.querySelector('#clock-map-placeholder');
    if (placeholder) placeholder.style.display = 'none';

    if (!this._map) {
      this._map = window.L.map(mapEl, {
        zoomControl: true, scrollWheelZoom: false, attributionControl: false
      }).setView([userLat, userLon], 16);
      window.L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 19 }).addTo(this._map);
    }

    // User location blue dot marker
    const userIcon = window.L.divIcon({
      className: '',
      html: `<div style="position:relative;width:18px;height:18px;">
        <div style="position:absolute;inset:-6px;border-radius:50%;background:rgba(59,130,246,0.35);animation:pulse-green 1.8s infinite;"></div>
        <div style="width:18px;height:18px;border-radius:50%;background:#3b82f6;border:3px solid #fff;box-shadow:0 2px 8px rgba(0,0,0,.4);"></div>
      </div>`,
      iconSize: [18, 18], iconAnchor: [9, 9]
    });

    if (this._userMarker) {
      this._userMarker.setLatLng([userLat, userLon]);
    } else {
      this._userMarker = window.L.marker([userLat, userLon], { icon: userIcon }).addTo(this._map).bindPopup('<b>📍 Your Location</b>');
    }

    if (this.state.storeCoords) {
      const { lat: sLat, lon: sLon, radiusMeters } = this.state.storeCoords;
      if (!this._storeMarker) {
        const storeIcon = window.L.divIcon({
          className: '',
          html: `<div style="width:22px;height:22px;border-radius:50%;background:#c9a46a;border:3px solid #fff;display:flex;align-items:center;justify-content:center;font-size:12px;box-shadow:0 2px 8px rgba(0,0,0,.4)">☕</div>`,
          iconSize: [22, 22], iconAnchor: [11, 11]
        });
        this._storeMarker = window.L.marker([sLat, sLon], { icon: storeIcon }).addTo(this._map).bindPopup('<b>☕ Store Location</b>');
      }

      if (!this._geofenceCircle) {
        this._geofenceCircle = window.L.circle([sLat, sLon], {
          radius: radiusMeters, color: '#c9a46a', fillColor: '#c9a46a', fillOpacity: 0.08, weight: 2, dashArray: '6,4'
        }).addTo(this._map);
      }

      // Distance calculation
      const dist = haversineMeters(userLat, userLon, sLat, sLon);
      const inside = dist <= radiusMeters;
      const distStr = dist < 1000 ? `${Math.round(dist)} m from store` : `${(dist / 1000).toFixed(2)} km from store`;

      this._updateGpsStatus(inside ? 'inside' : 'outside', inside ? `✓ Within geofence (${distStr})` : `⚠ Outside geofence (${distStr})`);

      const bounds = window.L.latLngBounds([[userLat, userLon], [sLat, sLon]]);
      this._map.fitBounds(bounds, { padding: [50, 50], maxZoom: 17 });

      const distChip = this.container.querySelector('#gps-distance-chip');
      const distText = this.container.querySelector('#gps-distance-text');
      if (distChip) distChip.style.display = 'flex';
      if (distText) distText.textContent = distStr;
    } else {
      this._map.setView([userLat, userLon], 16);
      this._updateGpsStatus('inside', 'Location acquired');
    }

    const coordsChip = this.container.querySelector('#gps-coords-chip');
    const coordsText = this.container.querySelector('#gps-coords-text');
    if (coordsChip) coordsChip.style.display = 'flex';
    if (coordsText) coordsText.textContent = `${userLat.toFixed(5)}, ${userLon.toFixed(5)}`;

    const accChip = this.container.querySelector('#gps-accuracy-chip');
    const accText = this.container.querySelector('#gps-accuracy-text');
    if (accChip) accChip.style.display = 'flex';
    if (accText) accText.textContent = accuracy ? `±${Math.round(accuracy)} m accuracy` : 'Accuracy unknown';

    setTimeout(() => { if (this._map) this._map.invalidateSize(); }, 200);
  }

  /** Recenter map to user location */
  async _recenterMap() {
    if (!this._map) return;
    try {
      const pos = await new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(resolve, reject, { enableHighAccuracy: true, timeout: 8000 });
      });
      this._map.setView([pos.coords.latitude, pos.coords.longitude], 16);
    } catch (_) { /* silent */ }
  }

  /** Calculate route to store */
  async _calculateFastestRoute() {
    try {
      const pos = this._lastPos || await new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(resolve, reject, { enableHighAccuracy: true, timeout: 10000 });
      });
      const uLat = pos.coords.latitude;
      const uLon = pos.coords.longitude;

      if (!this._map) await this._showLocationOnMap(uLat, uLon, pos.coords.accuracy);

      const panel = this.container.querySelector('#clock-route-info-panel');
      const details = this.container.querySelector('#clock-route-details-text');
      const googleBtn = this.container.querySelector('#clock-btn-open-google-maps');

      if (this.state.storeCoords) {
        const { lat: sLat, lon: sLon } = this.state.storeCoords;
        const dist = haversineMeters(uLat, uLon, sLat, sLon);
        const distStr = dist < 1000 ? `${Math.round(dist)} m` : `${(dist / 1000).toFixed(2)} km`;
        const estMin = Math.ceil(dist / 500);

        if (details) details.textContent = `📍 ${distStr} away · ~${estMin} min (Driving)`;
        if (googleBtn) googleBtn.href = `https://www.google.com/maps/dir/${uLat},${uLon}/${sLat},${sLon}/@${sLat},${sLon},15z`;

        if (this._map) {
          if (this._routeLine) this._map.removeLayer(this._routeLine);
          this._routeLine = window.L.polyline([[uLat, uLon], [sLat, sLon]], {
            color: '#3b82f6', weight: 3, dashArray: '8,5', opacity: 0.8
          }).addTo(this._map);
          const bounds = window.L.latLngBounds([[uLat, uLon], [sLat, sLon]]);
          this._map.fitBounds(bounds, { padding: [40, 40] });
        }
        if (panel) panel.style.display = 'block';
      }
    } catch (_) {
      notificationStore.danger('Could not get GPS for route calculation.');
    }
  }

  /** Update status pill and geofence banner styling */
  _updateGpsStatus(state, text) {
    if (!this.container) return;
    const pill = this.container.querySelector('#gps-status-pill');
    const pillLabel = this.container.querySelector('#gps-status-text');
    if (pill) pill.className = `gps-status-pill gps-status-${state}`;
    if (pillLabel) pillLabel.textContent = text;

    const banner = this.container.querySelector('#clock-geofence-status-text');
    if (banner) {
      banner.textContent = text;
      const colorMap = {
        inside:    { color: '#82a37d', bg: 'rgba(130,163,125,0.08)', border: 'rgba(130,163,125,0.2)' },
        outside:   { color: '#ff6b6b', bg: 'rgba(255,107,107,0.08)', border: 'rgba(255,107,107,0.2)' },
        locating:  { color: '#c9a46a', bg: 'rgba(201,164,106,0.08)', border: 'rgba(201,164,106,0.2)' },
        error:     { color: '#ffa500', bg: 'rgba(255,165,0,0.08)',   border: 'rgba(255,165,0,0.2)'   },
        idle:      { color: 'var(--text-muted)', bg: 'rgba(255,255,255,0.02)', border: 'rgba(255,255,255,0.05)' },
      };
      const c = colorMap[state] || colorMap.idle;
      banner.style.color = c.color;
      banner.style.background = c.bg;
      banner.style.borderColor = c.border;
    }
  }

  /** Clean up widget resources */
  destroy() {
    if (this._clockInterval) {
      clearInterval(this._clockInterval);
      this._clockInterval = null;
    }
    if (this._map) {
      this._map.remove();
      this._map = null;
      this._userMarker = null;
      this._storeMarker = null;
      this._geofenceCircle = null;
      this._routeLine = null;
    }
  }
}
