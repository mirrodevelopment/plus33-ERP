/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Core Module
 * File              : htmlLoader.js
 * Path              : frontend/core/htmlLoader.js
 * Purpose           : Centralized HTML template loader with caching
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Single entry point for loading all HTML page templates.
 * Features:
 *   - In-memory template cache (never downloads the same HTML twice)
 *   - Loading indicator while templates are fetching
 *   - Graceful error page when a template cannot be loaded
 *   - Cache invalidation support via bust flag
 ******************************************************************************/

class HtmlLoader {
  constructor() {
    /** @type {Map<string, string>} In-memory cache keyed by URL */
    this._cache = new Map();

    /** @type {Map<string, Promise<string>>} Inflight request deduplication */
    this._inflight = new Map();
  }

  /**
   * Load an HTML template from a URL.
   * Returns cached content immediately on subsequent calls.
   *
   * @param {string} url - Path to the .html file
   * @param {Object} [options]
   * @param {boolean} [options.bustCache=false] - Force re-fetch even if cached
   * @returns {Promise<string>} Raw HTML string
   */
  async load(url, options = {}) {
    const { bustCache = false } = options;

    // Return from cache unless busting
    if (!bustCache && this._cache.has(url)) {
      return this._cache.get(url);
    }

    // Deduplicate concurrent requests for the same URL
    if (this._inflight.has(url)) {
      return this._inflight.get(url);
    }

    const request = this._fetch(url);
    this._inflight.set(url, request);

    try {
      const html = await request;
      this._cache.set(url, html);
      return html;
    } finally {
      this._inflight.delete(url);
    }
  }

  /**
   * Internal fetch with error handling.
   * @param {string} url
   * @returns {Promise<string>}
   */
  async _fetch(url) {
    try {
      const response = await fetch(url);
      if (!response.ok) {
        throw new Error(`HTTP ${response.status} — ${response.statusText}`);
      }
      return await response.text();
    } catch (err) {
      console.error(`[HtmlLoader] Failed to load template: ${url}`, err);
      return this._errorTemplate(url, err.message);
    }
  }

  /**
   * Inject a template into a container element.
   * Shows a loading spinner during fetch, then replaces it with content.
   * Automatically calls lucide.createIcons() after injection.
   *
   * @param {string} url - Path to the .html file
   * @param {HTMLElement} container - Target DOM element
   * @param {Object} [options]
   * @param {boolean} [options.bustCache=false]
   * @returns {Promise<void>}
   */
  async inject(url, container, options = {}) {
    // Show loading state immediately
    container.innerHTML = this._loadingTemplate();

    const html = await this.load(url, options);
    container.innerHTML = html;

    // Re-initialize Lucide icons after HTML injection
    if (window.lucide) {
      window.lucide.createIcons();
    }
  }

  /**
   * Invalidate a specific cached template.
   * @param {string} url
   */
  invalidate(url) {
    this._cache.delete(url);
  }

  /**
   * Clear the entire template cache.
   */
  clearCache() {
    this._cache.clear();
  }

  /**
   * Loading spinner shown while a template is being fetched.
   * @returns {string} HTML string
   */
  _loadingTemplate() {
    return `
      <div class="template-loading" style="
        display: flex;
        align-items: center;
        justify-content: center;
        height: 200px;
        gap: var(--spacing-sm, 12px);
        color: var(--text-muted, #888);
        font-size: 0.85rem;
      ">
        <div class="logo-spinner" style="width: 24px; height: 24px;"></div>
        <span>Loading workspace...</span>
      </div>
    `;
  }

  /**
   * Error page shown when a template fails to load.
   * @param {string} url
   * @param {string} message
   * @returns {string} HTML string
   */
  _errorTemplate(url, message) {
    return `
      <div class="template-error" style="
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 300px;
        gap: var(--spacing-md, 16px);
        color: var(--text-secondary, #ccc);
        text-align: center;
        padding: var(--spacing-xl, 32px);
      ">
        <i data-lucide="alert-triangle" style="width: 40px; height: 40px; color: var(--status-danger, #ef4444);"></i>
        <h3 style="color: var(--status-danger, #ef4444); margin: 0; font-size: 1rem;">Failed to Load Page</h3>
        <p style="margin: 0; font-size: 0.8rem; color: var(--text-muted, #888);">
          Could not load template: <code>${url}</code>
        </p>
        <p style="margin: 0; font-size: 0.75rem; color: var(--text-muted, #888);">${message}</p>
        <button onclick="window.location.reload()" style="
          background: var(--accent-primary, #c9a46a);
          color: #000;
          border: none;
          padding: 8px 20px;
          border-radius: var(--radius-md, 6px);
          cursor: pointer;
          font-weight: 700;
          font-size: 0.8rem;
        ">Reload</button>
      </div>
    `;
  }
}

/** Singleton HTML loader instance — use this everywhere */
export const htmlLoader = new HtmlLoader();
export default htmlLoader;
