/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Core Infrastructure
 * File              : html-loader.js
 * Path              : frontend/core/html-loader.js
 * Purpose           : Reusable HTML template loader with in-memory cache
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Fetches HTML template files from the server and caches them in memory to
 * prevent duplicate network requests. All page controllers must use this
 * utility instead of writing HTML as JavaScript template literals.
 ******************************************************************************/

const _cache = new Map();

/**
 * Load an HTML file and inject it into a container element.
 * Caches the response so repeated navigation does not re-fetch.
 *
 * @param {string} path         - Relative path to the HTML file
 * @param {HTMLElement} container - DOM element to inject the HTML into
 * @returns {Promise<HTMLElement>} The populated container element
 */
export async function loadHTML(path, container) {
  let html;
  if (_cache.has(path)) {
    html = _cache.get(path);
  } else {
    const response = await fetch(path);
    if (!response.ok) {
      throw new Error(`Failed to load HTML template: ${path} (${response.status})`);
    }
    html = await response.text();
    _cache.set(path, html);
  }
  container.innerHTML = html;
  return container;
}

/**
 * Pre-fetch and cache an HTML file without injecting it.
 * @param {string} path - Relative path to the HTML file
 * @returns {Promise<string>} The raw HTML string
 */
export async function prefetchHTML(path) {
  if (_cache.has(path)) return _cache.get(path);
  const response = await fetch(path);
  if (!response.ok) throw new Error(`Pre-fetch failed: ${path} (${response.status})`);
  const html = await response.text();
  _cache.set(path, html);
  return html;
}

/** Clear the HTML cache. */
export function clearHTMLCache() {
  _cache.clear();
}
