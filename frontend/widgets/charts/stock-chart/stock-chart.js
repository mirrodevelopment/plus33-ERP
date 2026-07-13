/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : stock-chart.js
 * Path              : frontend/widgets/charts/stock-chart.js
 * Purpose           : Frontend utility: stock-chart for PLUS33 Coffee ERP
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/logger
 * Depends On        : core/logger
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend utility: stock-chart for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../../core/logger.js';
import { htmlLoader } from '../../../core/htmlLoader.js';

export class StockChart {
  constructor(config, storeStatusOverview) {
    this.config = config;
    this.storeStatusOverview = storeStatusOverview || { highProfit: 0, midProfit: 0, lowProfit: 0, loss: 0 };
  }

  _loadCss() {
    const href = 'widgets/charts/stock-chart/stock-chart.css';
    if (!document.querySelector(`link[href="${href}"]`)) {
      const link = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = href;
      document.head.appendChild(link);
    }
  }

  async mount(container, lifecycle) {
    logger.debug('StockChart', 'Rendering store profit breakdown donut graph...');
    this._loadCss();

    const highProfit = this.storeStatusOverview.highProfit || 0;
    const midProfit = this.storeStatusOverview.midProfit || 0;
    const lowProfit = this.storeStatusOverview.lowProfit || 0;
    const loss = this.storeStatusOverview.loss || 0;
    const total = highProfit + midProfit + lowProfit + loss || 1;

    const hpPct = ((highProfit / total) * 100).toFixed(1);
    const mpPct = ((midProfit / total) * 100).toFixed(1);
    const lpPct = ((lowProfit / total) * 100).toFixed(1);
    const lsPct = ((loss / total) * 100).toFixed(1);

    // Draw donut segment offsets dynamically
    let currentOffset = 25;
    const dataSegments = [
      { name: 'High Profit', count: highProfit, percent: Number(hpPct), color: 'var(--status-success)' },
      { name: 'Mid Profit', count: midProfit, percent: Number(mpPct), color: 'var(--status-warning)' },
      { name: 'Low Profit', count: lowProfit, percent: Number(lpPct), color: '#eab308' },
      { name: 'Loss', count: loss, percent: Number(lsPct), color: 'var(--status-danger)' }
    ];

    let html = await htmlLoader.load('widgets/charts/stock-chart/stock-chart.html');
    html = html.replace('{{TITLE}}', this.config.title)
               .replace('{{TOTAL}}', total)
               .replace('{{HIGH_VAL}}', highProfit)
               .replace('{{HIGH_PCT}}', hpPct)
               .replace('{{MID_VAL}}', midProfit)
               .replace('{{MID_PCT}}', mpPct)
               .replace('{{LOW_VAL}}', lowProfit)
               .replace('{{LOW_PCT}}', lpPct)
               .replace('{{LOSS_VAL}}', loss)
               .replace('{{LOSS_PCT}}', lsPct);

    container.innerHTML = html;

    const segmentsGroup = container.querySelector('#donut-segments');
    if (segmentsGroup) {
      segmentsGroup.innerHTML = '';
      dataSegments.forEach(seg => {
        const dash = `${seg.percent} ${100 - seg.percent}`;
        const offset = currentOffset;
        currentOffset = (currentOffset - seg.percent + 100) % 100;

        const circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
        circle.setAttribute('class', 'donut-segment');
        circle.setAttribute('cx', '21');
        circle.setAttribute('cy', '21');
        circle.setAttribute('r', '15.915');
        circle.setAttribute('fill', 'transparent');
        circle.setAttribute('stroke', seg.color);
        circle.setAttribute('stroke-width', '4');
        circle.setAttribute('stroke-dasharray', dash);
        circle.setAttribute('stroke-dashoffset', offset.toString());
        segmentsGroup.appendChild(circle);
      });
    }

    // Bind info button click
    const infoBtn = container.querySelector('#stock-info-btn');
    if (infoBtn) {
      infoBtn.addEventListener('click', (e) => {
        e.preventDefault();
        e.stopPropagation();
        this._showExplanationModal(container);
      });
    }

    this.bindEvents(container, lifecycle);
  }

  bindEvents(container, lifecycle) {
    import('../../../store/notificationStore.js').then(({ notificationStore }) => {
      const approveBtn = container.querySelector('.btn-action-approve-stores');
      const perfBtn = container.querySelector('.btn-action-view-perf');
      const visitsBtn = container.querySelector('.btn-action-visits');

      if (approveBtn) {
        const h = () => notificationStore.success('Opening pending new store applications queue...');
        approveBtn.addEventListener('click', h);
        lifecycle.onCleanup(() => approveBtn.removeEventListener('click', h));
      }
      if (perfBtn) {
        const h = () => window.location.hash = '#stores';
        perfBtn.addEventListener('click', h);
        lifecycle.onCleanup(() => perfBtn.removeEventListener('click', h));
      }
      if (visitsBtn) {
        const h = () => notificationStore.success('Loading regional audit checklist logs...');
        visitsBtn.addEventListener('click', h);
        lifecycle.onCleanup(() => visitsBtn.removeEventListener('click', h));
      }
    });
  }

  _showExplanationModal(container) {
    const modal = container.querySelector('#stock-explanation-modal');
    if (modal) {
      modal.removeAttribute('hidden');
      
      if (!modal.dataset.bound) {
        modal.dataset.bound = 'true';
        
        const closeModal = () => modal.setAttribute('hidden', '');
        modal.querySelector('#close-stock-expl-modal').addEventListener('click', closeModal);
        modal.querySelector('#close-stock-expl-modal-btn').addEventListener('click', closeModal);
        modal.addEventListener('click', (e) => {
          if (e.target === modal) {
            closeModal();
          }
        });
      }
    }
  }
}
