/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * File              : userStore.js
 * Path              : frontend/store/userStore.js
 * Purpose           : Frontend state store managing Store Module UI state
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/eventBus, core/logger
 * Depends On        : core/eventBus, core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend state store managing Store Module UI state. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { eventBus } from '../core/eventBus.js';
import { logger } from '../core/logger.js';

class UserStore {
  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  constructor() {
    this.profiles = {
      'ultimateAdmin': {
        name: 'Arjun Mehta',
        email: 'arjun.mehta@plus33.coffee',
        department: 'Executive Administration',
        store: 'Corporate Head Office',
        storeRegion: 'Delhi NCR',
        country: 'India',
        avatarUrl: 'imgs/male-avatar.png',
        joinedDate: '2024-01-15',
        phone: '+91 98765 43210',
        gender: 'Male'
      },
      'warehouse': {
        name: 'Geordi La Forge',
        email: 'geordi.laforge@plus33.coffee',
        department: 'Logistics & Supply Chain',
        store: 'Central Logistics Hub',
        storeRegion: 'Île-de-France',
        country: 'France',
        avatarUrl: 'imgs/male-avatar.png',
        joinedDate: '2025-03-22',
        phone: '+33 6 12 34 56 78',
        gender: 'Male'
      },
      'store': {
        name: 'Beverly Crusher',
        email: 'beverly.crusher@plus33.coffee',
        department: 'Retail Operations',
        store: 'Green Park Café, City Center',
        storeRegion: 'Delhi NCR',
        country: 'India',
        avatarUrl: 'imgs/female-avatar.jpg',
        joinedDate: '2025-06-11',
        phone: '+33 6 98 76 54 32',
        gender: 'Female'
      },
      'storeEmployee': {
        name: 'Neha Sharma',
        email: 'neha.sharma@plus33.coffee',
        department: 'Retail Operations',
        store: 'Green Park Café, City Center',
        storeRegion: 'Delhi NCR',
        country: 'India',
        avatarUrl: 'imgs/female-avatar.jpg',
        joinedDate: '2025-05-15',
        phone: '+91 99999 88888',
        gender: 'Female'
      },
      'shiftSupervisor': {
        name: 'Rohan Sharma',
        email: 'rohan.sharma@plus33.coffee',
        department: 'Retail Operations',
        store: 'Green Park Café, City Center',
        storeRegion: 'Delhi NCR',
        country: 'India',
        avatarUrl: 'imgs/male-avatar.png',
        joinedDate: '2024-08-10',
        phone: '+91 98888 77777',
        gender: 'Male',
        designation: 'Shift Supervisor'
      },
      'nationalAdmin': {
        name: 'Rajesh Kumar',
        email: 'national.admin@plus33.coffee',
        department: 'National Operations',
        store: 'National HQ',
        storeRegion: 'All Regions',
        country: 'India',
        avatarUrl: 'imgs/male-avatar.png',
        joinedDate: '2024-05-10',
        phone: '+91 99000 88000',
        gender: 'Male'
      },
      'regionalAdmin': {
        name: 'Vijay Iyer',
        email: 'regional.admin@plus33.coffee',
        department: 'Regional Operations',
        store: 'Regional Hub',
        storeRegion: 'South India',
        country: 'India',
        avatarUrl: 'imgs/male-avatar.png',
        joinedDate: '2025-01-20',
        phone: '+91 98000 77000',
        gender: 'Male'
      }
    };
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  getProfile(role) {
    return this.profiles[role] || {
      name: 'Standard Employee',
      email: 'employee@plus33.coffee',
      department: 'General Staff',
      store: 'Green Park Café, City Center',
      storeRegion: 'Delhi NCR',
      country: 'India',
      avatarUrl: '',
      joinedDate: '2026-01-01',
      phone: '',
      gender: 'Other'
    };
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  updateProfile(role, newData) {
    /**
     * Performs the fn operation in this module.
     * @memberof Store Module
     */
    if (this.profiles[role]) {
      this.profiles[role] = { ...this.profiles[role], ...newData };
      logger.info('UserStore', `Profile updated for role: ${role}`);
      eventBus.emit('user:profile-updated', { role, profile: this.profiles[role] });
    }
  }
}

export const userStore = new UserStore();
