/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * File              : themeStore.js
 * Path              : frontend/store/themeStore.js
 * Purpose           : Frontend state store managing Store Module UI state
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/eventBus, core/storage, core/logger
 * Depends On        : core/eventBus, core/storage, core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend state store managing Store Module UI state. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { eventBus } from '../core/eventBus.js';
import { storage } from '../core/storage.js';
import { logger } from '../core/logger.js';

class ThemeStore {
  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  constructor() {
    const VALID_THEMES = ['coffee-dark', 'light', 'charcoal', 'cyber-dark', 'nation'];
    const stored = storage.get('plus33-theme', 'coffee-dark');
    // Guard against old/removed theme keys surviving in localStorage
    this.theme = VALID_THEMES.includes(stored) ? stored : 'coffee-dark';
    // Persist the sanitised value so next boot is clean
    if (this.theme !== stored) {
      storage.set('plus33-theme', this.theme);
      logger.warn('ThemeStore', `Stored theme "${stored}" is no longer valid. Reset to "${this.theme}".`);
    }
  }

  /**
   * Set theme
   * @param {'coffee-dark'|'light'|'charcoal'|'cyber-dark'} newTheme 
   */
  setTheme(newTheme) {
    const validThemes = ['coffee-dark', 'light', 'charcoal', 'cyber-dark', 'nation'];
    if (!validThemes.includes(newTheme)) {
      logger.warn('ThemeStore', `Invalid theme "${newTheme}" requested.`);
      return;
    }

    this.theme = newTheme;
    storage.set('plus33-theme', newTheme);
    document.documentElement.setAttribute('data-theme', newTheme);
    
    logger.info('ThemeStore', `Theme set to: ${newTheme}`);
    eventBus.emit('theme:changed', newTheme);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Store Module
   */
  getTheme() {
    return this.theme;
  }
}

export const themeStore = new ThemeStore();
