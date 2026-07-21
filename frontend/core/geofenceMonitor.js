/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Core — Geofence & Attendance Monitor
 * File              : geofenceMonitor.js
 * Path              : frontend/core/geofenceMonitor.js
 * Purpose           : Manages 5-minute GPS ping polling, auto clock-out,
 *                     away pass grace detection, and network-resilient
 *                     location checks. All user-facing messages are delivered
 *                     via custom popup cards — no alert() / confirm().
 * Version           : 1.0.0
 *
 * Related APIs      : POST /api/v1/attendance/ping-location
 *                     POST /api/v1/away-permission/request
 *                     GET  /api/v1/away-permission/my
 *
 * Description
 * ---------------------------------------------------------------------------
 * Usage:
 *   import { geofenceMonitor } from '../../core/geofenceMonitor.js';
 *   geofenceMonitor.start(onAutoClockOut, onPopupCard);
 *   geofenceMonitor.stop();
 *
 * onAutoClockOut() — called when server responds AUTO_CLOCKED_OUT.
 * onPopupCard(type, title, message, actions) — called to show a popup card.
 *   type: 'info' | 'warning' | 'danger' | 'success'
 *   actions: [{ label, id, variant }]
 ******************************************************************************/

import { apiClient } from '../api/client.js';
import { logger }    from './logger.js';

/** Polling interval: 5 minutes (300 000 ms) */
const PING_INTERVAL_MS = 5 * 60 * 1000;

class GeofenceMonitor {
  constructor() {
    this._intervalId     = null;
    this._onAutoClockOut = null;
    this._onPopupCard    = null;
    this._isOnline       = navigator.onLine;
    this._wasOffline     = false;      // flag: we went offline at some point
    this._lastGps        = null;       // last known GPS string "lat,lng"

    this._handleOnline  = this._onNetworkOnline.bind(this);
    this._handleOffline = this._onNetworkOffline.bind(this);
  }

  // ─── Public API ────────────────────────────────────────────────────────────

  /**
   * Start the geofence monitor.
   * @param {Function} onAutoClockOut — called when employee is auto-clocked out
   * @param {Function} onPopupCard    — called to show a popup card
   */
  start(onAutoClockOut, onPopupCard) {
    if (this._intervalId) this.stop();

    this._onAutoClockOut = onAutoClockOut;
    this._onPopupCard    = onPopupCard;

    window.addEventListener('online',  this._handleOnline);
    window.addEventListener('offline', this._handleOffline);

    logger.info('GeofenceMonitor', 'Starting 5-min GPS ping loop.');
    // Run immediately, then every 5 minutes
    this._ping();
    this._intervalId = setInterval(() => this._ping(), PING_INTERVAL_MS);
  }

  /** Stop the geofence monitor. */
  stop() {
    if (this._intervalId) {
      clearInterval(this._intervalId);
      this._intervalId = null;
    }
    window.removeEventListener('online',  this._handleOnline);
    window.removeEventListener('offline', this._handleOffline);
    logger.info('GeofenceMonitor', 'Stopped.');
  }

  // ─── Network event handlers ─────────────────────────────────────────────────

  _onNetworkOffline() {
    this._isOnline  = false;
    this._wasOffline = true;
    logger.warn('GeofenceMonitor', 'Network offline — pausing auto clock-out logic.');
  }

  _onNetworkOnline() {
    this._isOnline = true;
    logger.info('GeofenceMonitor', 'Network restored — running location check now.');
    // Immediately ping after reconnect with networkRestored flag
    this._ping(true);
  }

  // ─── Core ping ─────────────────────────────────────────────────────────────

  async _ping(networkRestored = false) {
    if (!this._isOnline && !networkRestored) {
      logger.debug('GeofenceMonitor', 'Skip ping — offline.');
      return;
    }

    let gps = null;
    try {
      gps = await this._getCurrentGps();
      this._lastGps = gps;
    } catch (err) {
      // GPS unavailable — send last known or null; backend treats GPS_REQUIRED gently
      gps = this._lastGps;
      logger.warn('GeofenceMonitor', 'GPS unavailable, using last known:', gps);
    }

    try {
      const res = await apiClient.post('/api/v1/attendance/ping-location', {
        gps,
        networkRestored: networkRestored || this._wasOffline,
      });
      this._wasOffline = false;

      if (res?.success && res.data) {
        this._handlePingResponse(res.data);
      }
    } catch (err) {
      logger.error('GeofenceMonitor', 'Ping failed:', err);
    }
  }

  // ─── Response handler ───────────────────────────────────────────────────────

  _handlePingResponse(data) {
    const action = data.action;
    logger.debug('GeofenceMonitor', 'Ping response:', action, data);

    switch (action) {
      case 'OK':
        // All good — no popup
        break;

      case 'WARNING':
        // Close to boundary (75% of max radius)
        this._popup('warning',
          '📍 Approaching Store Boundary',
          data.message || `You are getting close to the store boundary (${data.distance} m away).`,
          []
        );
        break;

      case 'AWAY_PASS_ACTIVE': {
        // Active away pass — show status silently in info card
        const until = data.approvedUntil ? new Date(data.approvedUntil).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '';
        logger.debug('GeofenceMonitor', `Away pass active until ${until}`);
        break;
      }

      case 'IN_GRACE_PERIOD': {
        // Away pass expired — 10 min grace period to return or request extension
        const graceUntil = data.graceExpiresAt ? new Date(data.graceExpiresAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '';
        this._popup('warning',
          '⏰ Away Pass Expired — Grace Period Active',
          `Your away pass has expired. You have 10 minutes to return to the store or request an extension. Grace period ends at ${graceUntil}.`,
          [
            { label: 'Request Extension', id: 'btn-geofence-extend', variant: 'btn-warning' },
            { label: 'I\'m Returning', id: 'btn-geofence-dismiss', variant: 'btn-secondary' },
          ]
        );
        break;
      }

      case 'AUTO_CLOCKED_OUT':
        // Employee auto-clocked out — serious popup
        this._popup('danger',
          '🚪 Automatically Clocked Out',
          data.message || `You have been automatically clocked out because you moved too far from the store.`,
          [{ label: 'OK, understood', id: 'btn-geofence-ack', variant: 'btn-secondary' }]
        );
        if (this._onAutoClockOut) this._onAutoClockOut();
        this.stop(); // stop monitoring after clock-out
        break;

      case 'NOT_CLOCKED_IN':
        // Not clocked in — stop monitoring
        this.stop();
        break;

      case 'GPS_REQUIRED':
        logger.warn('GeofenceMonitor', 'GPS required but not available.');
        break;

      default:
        break;
    }
  }

  // ─── GPS helper ─────────────────────────────────────────────────────────────

  _getCurrentGps() {
    return new Promise((resolve, reject) => {
      if (!navigator.geolocation) {
        reject(new Error('Geolocation not supported'));
        return;
      }
      navigator.geolocation.getCurrentPosition(
        pos => resolve(`${pos.coords.latitude},${pos.coords.longitude}`),
        err => reject(err),
        { enableHighAccuracy: true, timeout: 10000, maximumAge: 60000 }
      );
    });
  }

  // ─── Popup helper ───────────────────────────────────────────────────────────

  _popup(type, title, message, actions) {
    if (this._onPopupCard) {
      this._onPopupCard(type, title, message, actions);
    }
  }
}

/** Singleton instance */
export const geofenceMonitor = new GeofenceMonitor();
