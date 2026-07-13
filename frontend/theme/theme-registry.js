/**
 * ******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Frontend Design System
 * File              : theme-registry.js
 * Path              : frontend/theme/theme-registry.js
 * Purpose           : Single source of truth for all theme color tokens.
 *                     JS components (charts, canvas, dynamic renderers) read
 *                     colors from here so they adapt when the theme changes.
 * Version           : 2.0.0
 *
 * Themes            : coffee-dark | light | charcoal | cyber-dark
 *
 * Usage
 * ─────────────────────────────────────────────────────────────────────────────
 *   import ThemeRegistry from '/frontend/theme/theme-registry.js';
 *
 *   const c = ThemeRegistry.getThemeColors();
 *   console.log(c.accent.primary);              // e.g. "#c9a46a"
 *   ThemeRegistry.token('bg.card');              // dot-path shortcut
 *   ThemeRegistry.getFlatTokens();              // flat { 'bg.card': '#...' }
 *   ThemeRegistry.onThemeChange((c, name) => { chart.update(); });
 *   ThemeRegistry.getPalette('light');           // specific theme, no switch
 * ******************************************************************************
 */

const THEMES = {

  // ── Coffee Dark  (PLUS33 Brand Default) ───────────────────────────────────
  'coffee-dark': {
    label: 'Coffee Dark',
    isDark: true,
    bg: {
      app:       '#141414',
      sidebar:   '#1a1a1a',
      header:    '#1e1e1e',
      card:      'rgba(30,30,30,0.6)',
      cardHover: 'rgba(45,45,45,0.85)',
    },
    border: {
      default: 'rgba(201,164,106,0.25)',
      hover:   'rgba(201,164,106,0.55)',
    },
    text: {
      primary:   '#f5f5f5',
      secondary: '#d4cfc7',
      muted:     '#8c857b',
    },
    accent: {
      primary:   '#c9a46a',   // Coffee Gold
      secondary: '#e6c897',
      glow:      'rgba(201,164,106,0.2)',
    },
    status: {
      success: '#82a37d',
      warning: '#d99f59',
      danger:  '#c95c5c',
      info:    '#799fc4',
    },
  },

  // ── Light Minimal ──────────────────────────────────────────────────────────
  light: {
    label: 'Light Minimal',
    isDark: false,
    bg: {
      app:       '#f1f5f9',
      sidebar:   '#ffffff',
      header:    '#ffffff',
      card:      '#ffffff',
      cardHover: '#f8fafc',
    },
    border: {
      default: '#e2e8f0',
      hover:   '#cbd5e1',
    },
    text: {
      primary:   '#0f172a',
      secondary: '#475569',
      muted:     '#94a3b8',
    },
    accent: {
      primary:   '#2563eb',
      secondary: '#0284c7',
      glow:      'rgba(37,99,235,0.15)',
    },
    status: {
      success: '#059669',
      warning: '#d97706',
      danger:  '#dc2626',
      info:    '#0284c7',
    },
  },

  // ── Charcoal Noir ──────────────────────────────────────────────────────────
  charcoal: {
    label: 'Charcoal Noir',
    isDark: true,
    bg: {
      app:       '#121212',
      sidebar:   '#181818',
      header:    '#181818',
      card:      '#1e1e1e',
      cardHover: '#262626',
    },
    border: {
      default: '#2e2e2e',
      hover:   '#3f3f3f',
    },
    text: {
      primary:   '#f5f5f5',
      secondary: '#d4d4d8',
      muted:     '#71717a',
    },
    accent: {
      primary:   '#e5e5e5',   // Premium half-white
      secondary: '#c9a46a',   // Coffee Gold
      glow:      'rgba(229,229,229,0.15)',
    },
    status: {
      success: '#10b981',
      warning: '#f59e0b',
      danger:  '#ef4444',
      info:    '#38bdf8',
    },
  },

  // ── Cyber Dark (Neon Magenta × Teal) ────────────────────────────────────────
  'cyber-dark': {
    label: 'Cyber Dark',
    isDark: true,
    bg: {
      app:       '#070b18',   // Deep Navy Black
      sidebar:   '#0a0e22',
      header:    '#0a0e22',
      card:      '#0f1330',   // Dark Purple-Navy
      cardHover: '#181c45',
    },
    border: {
      default: 'rgba(212,0,232,0.18)',
      hover:   'rgba(0,212,170,0.5)',
    },
    text: {
      primary:   '#f0eeff',
      secondary: '#a8b0d8',
      muted:     '#556090',
    },
    accent: {
      primary:   '#d400e8',   // Neon Magenta / Violet
      secondary: '#00d4aa',   // Neon Teal / Cyan
      glow:      'rgba(212,0,232,0.30)',
    },
    status: {
      success: '#00d4aa',
      warning: '#f59e0b',
      danger:  '#ff3b6e',
      info:    '#7b3bcc',
    },
  },

  // ── Nation (French Colors: Blue & Red) ──────────────────────────────────────
  'nation': {
    label: 'Nation',
    isDark: false,
    bg: {
      app:       '#f5f7fa',   // Clean light grey-blue
      sidebar:   '#000091',   // French Deep Blue
      header:    '#ffffff',
      card:      '#ffffff',
      cardHover: '#edf1f7',
    },
    border: {
      default: '#d3dbe8',
      hover:   '#000091',
    },
    text: {
      primary:   '#0a0f24',   // Deep navy
      secondary: '#4a5a70',
      muted:     '#8c9cb2',
    },
    accent: {
      primary:   '#e1000f',   // French Red
      secondary: '#000091',   // French Blue
      glow:      'rgba(225,0,15,0.12)',
    },
    status: {
      success: '#10b981',
      warning: '#f59e0b',
      danger:  '#e1000f',
      info:    '#000091',
    },
  },

};

const DEFAULT_THEME = 'coffee-dark';

// ─────────────────────────────────────────────────────────────────────────────
// PUBLIC API
// ─────────────────────────────────────────────────────────────────────────────
const ThemeRegistry = {

  /** Active theme name from html[data-theme], falls back to DEFAULT_THEME */
  getActiveThemeName() {
    const attr = (typeof document !== 'undefined')
      ? document.documentElement.getAttribute('data-theme')
      : null;
    const name = attr || DEFAULT_THEME;
    return THEMES[name] ? name : DEFAULT_THEME;
  },

  /** Full palette for the currently active theme */
  getThemeColors() { return THEMES[this.getActiveThemeName()]; },

  /** Full palette for a specific theme by name (no side effects) */
  getPalette(themeName) { return THEMES[themeName] ?? null; },

  /**
   * Flat key->value map of all tokens for the active (or specified) theme.
   *   { 'bg.card':'#fff', 'accent.primary':'#2563eb', ... }
   */
  getFlatTokens(themeName) {
    const palette = themeName ? this.getPalette(themeName) : this.getThemeColors();
    const flat = {};
    const walk = (obj, prefix) => {
      for (const [k, v] of Object.entries(obj)) {
        const key = prefix ? `${prefix}.${k}` : k;
        if (v && typeof v === 'object') walk(v, key);
        else flat[key] = v;
      }
    };
    walk(palette, '');
    return flat;
  },

  /** All registered theme names */
  getThemeNames() { return Object.keys(THEMES); },

  /** Metadata for building theme switcher UIs: [{name, label, isDark}] */
  getAllThemeMeta() {
    return Object.entries(THEMES).map(([name, t]) => ({
      name, label: t.label, isDark: t.isDark,
    }));
  },

  /**
   * Dot-path token accessor.
   *   ThemeRegistry.token('accent.primary')           // active theme
   *   ThemeRegistry.token('bg.card', 'light')         // specific theme
   */
  token(path, themeName) {
    const palette = themeName ? this.getPalette(themeName) : this.getThemeColors();
    return path.split('.').reduce((o, k) => o?.[k], palette);
  },

  /**
   * Subscribe to theme changes (MutationObserver on html[data-theme]).
   * Fires immediately with the current theme, then on every change.
   * Returns an unsubscribe function.
   *
   *   const stop = ThemeRegistry.onThemeChange((colors, name) => { ... });
   *   stop(); // cleanup
   */
  onThemeChange(callback) {
    if (typeof window === 'undefined') return () => {};
    const fire = () => callback(this.getThemeColors(), this.getActiveThemeName());
    const obs = new MutationObserver(fire);
    obs.observe(document.documentElement, { attributes: true, attributeFilter: ['data-theme'] });
    fire();
    return () => obs.disconnect();
  },

};

export default ThemeRegistry;
if (typeof window !== 'undefined') window.ThemeRegistry = ThemeRegistry;
