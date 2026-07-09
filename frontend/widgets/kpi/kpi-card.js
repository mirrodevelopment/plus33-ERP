/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : kpi-card.js
 * Path              : frontend/widgets/kpi/kpi-card.js
 * Purpose           : Frontend utility: kpi-card for PLUS33 Coffee ERP
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : 
 * Depends On        : 
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend utility: kpi-card for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

export class KpiCard {
  constructor(config, metrics) {
    this.config = config;
    this.metrics = metrics || {};
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  mount(container, lifecycle) {
    // Primary value from backend metric field
    const rawVal = this.metrics[this.config.metric] !== undefined
      ? this.metrics[this.config.metric]
      : 0;
    const formattedVal = this.formatValue(rawVal, this.config.format);

    // Dynamic trend value — resolve from backend trendField or fall back to config
    let trendDisplay = this.config.trendValue || '';
    let trendStatus  = this.config.trendStatus || 'info';

    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (this.config.trendField && this.metrics[this.config.trendField] !== undefined) {
      const trendRaw = Number(this.metrics[this.config.trendField]);
      const sign     = trendRaw >= 0 ? '+' : '';
      trendDisplay   = `${sign}${trendRaw.toFixed(1)}%`;
      trendStatus    = trendRaw >= 0 ? 'success' : 'danger';
    } else if (this.config.trendCountField && this.metrics[this.config.trendCountField] !== undefined) {
      const trendRaw = Number(this.metrics[this.config.trendCountField]);
      const sign     = trendRaw >= 0 ? '+' : '';
      trendDisplay   = `${sign}${Math.round(trendRaw)}`;
      trendStatus    = trendRaw >= 0 ? 'success' : 'danger';
    }

    const trendColor = trendStatus === 'success'
      ? 'var(--status-success)'
      : (trendStatus === 'danger' ? 'var(--status-danger)' : 'var(--text-muted)');

    const trendChevronName = trendStatus === 'success'
      ? 'chevron-up'
      : (trendStatus === 'danger' ? 'chevron-down' : 'minus');

    const isLucideName = this.config.icon && /^[a-z0-9-]+$/.test(this.config.icon);
    const iconEl = isLucideName
      ? `<i data-lucide="${this.config.icon}" style="width:15px; height:15px; color: var(--accent-primary); stroke-width:2.5;"></i>`
      : `<span style="font-size:0.9rem; line-height:1;">${this.config.icon}</span>`;

    container.innerHTML = `
      <div class="kpi-card-content flex flex-col justify-between h-full" style="gap: var(--spacing-xs); position:relative; overflow:hidden;">
        <!-- Gold accent bottom line -->
        <div style="position:absolute; bottom:0; left:0; right:0; height:2px; background: linear-gradient(90deg, var(--accent-primary), transparent); border-radius:0 0 var(--radius-lg) var(--radius-lg);"></div>

        <div class="flex align-center gap-sm">
          <div class="kpi-icon-wrapper" style="background: rgba(201,164,106,0.1); width:28px; height:28px; border-radius:7px; display:flex; align-items:center; justify-content:center; border: 1px solid rgba(201,164,106,0.2); flex-shrink:0;">
            ${iconEl}
          </div>
          <div class="kpi-title" style="font-size:0.62rem; font-weight:700; color:var(--text-muted); text-transform:uppercase; letter-spacing:0.05em; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
            ${this.config.title}
          </div>
        </div>

        <div class="kpi-value" style="font-family:var(--font-display); font-size:1.18rem; font-weight:800; color:var(--text-primary); margin:2px 0; white-space:nowrap; overflow:hidden; text-overflow:ellipsis; letter-spacing:-0.02em;">
          ${formattedVal}
        </div>

        <div class="kpi-trend" style="font-size:0.65rem; color:var(--text-muted); display:flex; align-items:center; gap:3px; white-space:nowrap; overflow:hidden;">
          <i data-lucide="${trendChevronName}" style="width:12px; height:12px; color:${trendColor}; stroke-width:2.5; flex-shrink:0;"></i>
          <span style="color:${trendColor}; font-weight:700;">${trendDisplay}</span>
          <span style="overflow:hidden; text-overflow:ellipsis;">${this.config.trend}</span>
        </div>
      </div>
    `;
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  formatValue(value, format) {
    const num = Number(value);
    if (isNaN(num)) return String(value);

    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    let currencyCode = 'EUR';
    let symbol = '€';
    let locale = 'fr-FR';
    const storedGeneral = localStorage.getItem('plus33-settings-general');
    if (storedGeneral) {
      try {
        const parsed = JSON.parse(storedGeneral);
        if (parsed.defaultCurrency) {
          currencyCode = parsed.defaultCurrency;
          if (currencyCode === 'USD') {
            symbol = '$';
            locale = 'en-US';
          } else if (currencyCode === 'INR') {
            symbol = '₹';
            locale = 'en-IN';
          } else if (currencyCode === 'AED') {
            symbol = 'AED ';
            locale = 'en-US';
          }
        }
      } catch (e) {
        // ignore
      }
    }

    if (format === 'currency') {
      // Use compact notation for large numbers (≥ 1M)
      if (Math.abs(num) >= 1_000_000) {
        /**
         * Performs the compact operation in this module.
         * @memberof Widgets Module
         */
        const compact = (num / 1_000_000).toFixed(2);
        return `${symbol}${compact}M`;
      }
      if (Math.abs(num) >= 1_000) {
        /**
         * Performs the compact operation in this module.
         * @memberof Widgets Module
         */
        const compact = (num / 1_000).toFixed(1);
        return `${symbol}${compact}K`;
      }
      return new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 2
      }).format(num);
    }
    /**
     * Performs the fn operation in this module.
     * @memberof Widgets Module
     */
    if (format === 'percent') {
      return `${Number(num).toFixed(1)}%`;
    }
    // number — compact for large values
    if (Math.abs(num) >= 1_000_000) {
      return (num / 1_000_000).toFixed(1) + 'M';
    }
    if (Math.abs(num) >= 1_000) {
      return new Intl.NumberFormat(locale).format(Math.round(num));
    }
    return new Intl.NumberFormat(locale).format(num);
  }
}
