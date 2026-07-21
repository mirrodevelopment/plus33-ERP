/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * File              : userStore.js
 * Path              : frontend/store/userStore.js
 * Purpose           : In-memory profile state store managing user details, employee metadata, and banking records across all ERP roles (ultimateAdmin, nationalAdmin, regionalAdmin, store, shiftSupervisor, storeEmployee).
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend user profile state manager.
 * Features:
 *   - Holds fallback default profile records for all role types (name, email, department, store, region, country, avatarUrl, designation, banking info).
 *   - Serves profile data via getProfile(role).
 *   - Updates profile memory state via updateProfile(role, updatedData) and emits 'user:profile-updated' events to synchronize UI headers and avatar elements.
 ******************************************************************************/

import { eventBus } from '../core/eventBus.js';
import { logger } from '../core/logger.js';
import { storage } from '../core/storage.js';

class UserStore {
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
        gender: 'Male',
        designation: 'Ultimate System Administrator',
        baseSalary: '₹185,000 / month',
        bankName: 'HDFC Bank',
        bankAccount: '50100492810492',
        ifscCode: 'HDFC0001049',
        branchName: 'Corporate HQ Branch, New Delhi',
        swiftCode: 'HDFCINBBXXX'
      },
      'nationalWarehouseAdmin': {
        name: 'Geordi La Forge',
        email: 'geordi.laforge@plus33.coffee',
        department: 'Logistics & Supply Chain',
        store: 'Central Logistics Hub',
        storeRegion: 'Île-de-France',
        country: 'France',
        avatarUrl: 'imgs/male-avatar.png',
        joinedDate: '2025-03-22',
        phone: '+33 6 12 34 56 78',
        gender: 'Male',
        designation: 'National Warehouse Admin',
        baseSalary: '€4,200.00 / month',
        bankName: 'BNP Paribas',
        bankAccount: 'FR76 3000 4012 3456 7890 1234 567',
        ifscCode: 'BNPAFRPPXXX',
        branchName: 'Paris Central Logistics Branch',
        swiftCode: 'BNPAFRPPXXX'
      },
      'regionalWarehouseAdmin': {
        name: 'Miles O\'Brien',
        email: 'miles.obrien@plus33.coffee',
        department: 'Logistics & Supply Chain',
        store: 'North France Logistics',
        storeRegion: 'North France',
        country: 'France',
        avatarUrl: 'imgs/male-avatar.png',
        joinedDate: '2025-04-12',
        phone: '+33 6 87 65 43 21',
        gender: 'Male',
        designation: 'Regional Warehouse Admin',
        baseSalary: '€3,450.00 / month',
        bankName: 'Crédit Agricole',
        bankAccount: 'FR76 1820 6002 9876 5432 1098 765',
        ifscCode: 'AGRIFRPPXXX',
        branchName: 'Lille North Logistics Branch',
        swiftCode: 'AGRIFRPPXXX'
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
        phone: '+91 98765 11223',
        gender: 'Female',
        designation: 'Store Manager',
        baseSalary: '₹75,000 / month',
        bankName: 'ICICI Bank',
        bankAccount: '000401589201',
        ifscCode: 'ICIC0000004',
        branchName: 'Green Park Main Branch, New Delhi',
        swiftCode: 'ICICINBBXXX'
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
        gender: 'Female',
        designation: 'Certified Senior Barista',
        baseSalary: '₹45,000 / month',
        bankName: 'HDFC Bank',
        bankAccount: '50100492810492',
        ifscCode: 'HDFC0001049',
        branchName: 'Green Park Branch, New Delhi',
        swiftCode: 'HDFCINBBXXX'
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
        designation: 'Senior Shift Lead & Barista Master',
        baseSalary: '₹58,000 / month',
        bankName: 'Axis Bank',
        bankAccount: '91802004829104',
        ifscCode: 'UTIB0000124',
        branchName: 'Connaught Place Branch, New Delhi',
        swiftCode: 'AXISINBBXXX'
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
        gender: 'Male',
        designation: 'National Operations Director',
        baseSalary: '₹140,000 / month',
        bankName: 'State Bank of India',
        bankAccount: '30492810492',
        ifscCode: 'SBIN0000691',
        branchName: 'Parliament Street Branch, New Delhi',
        swiftCode: 'SBININBBXXX'
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
        gender: 'Male',
        designation: 'Regional Managing Director',
        baseSalary: '₹115,000 / month',
        bankName: 'Canara Bank',
        bankAccount: '110029384710',
        ifscCode: 'CNRB0000104',
        branchName: 'MG Road Branch, Bengaluru',
        swiftCode: 'CNRBINBBXXX'
      }
    };
  }

  getProfile(role = 'storeEmployee') {
    const key = (role === 'storeAdmin') ? 'store' : ((role === 'supervisor') ? 'shiftSupervisor' : role);
    return this.profiles[key] || this.profiles['storeEmployee'];
  }

  updateProfile(role = 'storeEmployee', updatedData = {}) {
    const key = (role === 'storeAdmin') ? 'store' : ((role === 'supervisor') ? 'shiftSupervisor' : role);
    if (this.profiles[key]) {
      this.profiles[key] = { ...this.profiles[key], ...updatedData };
      eventBus.emit('user:profile-updated', this.profiles[key]);
      logger.info(`Updated user profile for role [${role}]`, updatedData);
    }
  }
}

export const userStore = new UserStore();
