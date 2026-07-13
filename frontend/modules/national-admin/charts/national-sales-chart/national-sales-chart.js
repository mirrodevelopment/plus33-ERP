/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : national-sales-chart.js
 * Path              : frontend/modules/national-admin/dashboard/components/national-sales-chart.js
 * Purpose           : Frontend utility: national-sales-chart for PLUS33 Coffee ERP
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
 * Frontend utility: national-sales-chart for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../../../core/logger.js';
import { htmlLoader } from '../../../../core/htmlLoader.js';
import { dashboardService } from '../../../../services/dashboard/DashboardService.js';
import { apiClient } from '../../../../api/client.js';

export class NationalSalesChart {
  constructor(config, salesOverview, regionalPerformance) {
    this.config = config;
    this.salesOverview = salesOverview || { totalSales: 0, targetSales: 0, targetAchievement: 0, trend: [] };
    this.regionalPerformance = regionalPerformance || [];
  }

  _loadCss() {
    const href = 'widgets/charts/sales-chart/sales-chart.css';
    if (!document.querySelector(`link[href="${href}"]`)) {
      const link = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = href;
      document.head.appendChild(link);
    }
  }

  async mount(container, lifecycle) {
    logger.debug('NationalSalesChart', 'Mounting National Sales Overview widget frame...');
    this._loadCss();

    let currencyCode = 'EUR';
    let locale = 'fr-FR';
    const storedGeneral = localStorage.getItem('plus33-settings-general');
    if (storedGeneral) {
      try {
        const parsed = JSON.parse(storedGeneral);
        if (parsed.defaultCurrency) {
          currencyCode = parsed.defaultCurrency;
          if (currencyCode === 'USD') locale = 'en-US';
          else if (currencyCode === 'INR') locale = 'en-IN';
          else if (currencyCode === 'AED') locale = 'en-US';
        }
      } catch (e) {
        // ignore
      }
    }

    // Fetch all regions dynamically to map child region names
    this.allRegions = [];
    try {
      const res = await apiClient.get('/api/v1/regions', { size: 100 });
      if (res && res.success && res.data && res.data.content) {
        this.allRegions = res.data.content;
      }
    } catch (e) {
      // ignore
    }

    const baseSales = Number(this.salesOverview.totalSales || 0);
    const baseTarget = Number(this.salesOverview.targetSales || 0);

    let html = await htmlLoader.load('modules/national-admin/charts/national-sales-chart/national-sales-chart.html');
    html = html.replace('{{TITLE}}', this.config.title);

    container.innerHTML = html;

    // Cache DOM refs
    const totalEl = container.querySelector('#sales-metric-total');
    const targetEl = container.querySelector('#sales-metric-target');
    const progressRing = container.querySelector('#sales-progress-ring');
    const progressText = container.querySelector('#sales-progress-text');
    const chartWrapper = container.querySelector('#sales-chart-wrapper');
    const tooltip = container.querySelector('#sales-chart-tooltip');
    const canvasContainer = container.querySelector('#sales-chart-canvas-container');
    const regionsList = container.querySelector('#sales-regions-list');
    const regionSelect = container.querySelector('#sales-region-select');

    // Populate region select options
    if (regionSelect) {
      try {
        const restrictRegionId = this.config.restrictRegionId;
        let targetRegionId = restrictRegionId;
        const matchingRegion = (this.allRegions || []).find(r => 
          String(r.id) === String(restrictRegionId) || 
          String(r.code) === String(restrictRegionId)
        );
        if (matchingRegion) {
          targetRegionId = matchingRegion.id;
        }

        const childRegions = (this.allRegions || []).filter(r => 
          targetRegionId && String(r.parentId) === String(targetRegionId)
        );

        regionSelect.innerHTML = '';
        const allOpt = document.createElement('option');
        allOpt.value = 'all';
        allOpt.textContent = 'All Regions';
        regionSelect.appendChild(allOpt);

        childRegions.forEach(r => {
          const opt = document.createElement('option');
          opt.value = r.id;
          opt.textContent = r.name;
          regionSelect.appendChild(opt);
        });
      } catch (err) {
        logger.error('NationalSalesChart', 'Failed to populate region selector options', err);
      }
    }

    // Bind info button click
    const infoBtn = container.querySelector('#sales-info-btn');
    if (infoBtn) {
      infoBtn.addEventListener('click', (e) => {
        e.preventDefault();
        e.stopPropagation();
        this._showExplanationModal(container);
      });
    }

    // 1. Process Regions
    const renderRegionsList = () => {
      let regions = [];
      if (this.regionalPerformance && this.regionalPerformance.length > 0) {
        regions = [...this.regionalPerformance];
      } else {
        regions = [];
      }
      regions.sort((a, b) => Number(b.sales || 0) - Number(a.sales || 0));

      const restrictRegionId = this.config.restrictRegionId;
      if (restrictRegionId && restrictRegionId !== 'ALL' && restrictRegionId !== 'all') {
        let targetRegionId = restrictRegionId;
        const matchingRegion = (this.allRegions || []).find(r => 
          String(r.id) === String(restrictRegionId) || 
          String(r.code) === String(restrictRegionId)
        );
        if (matchingRegion) {
          targetRegionId = matchingRegion.id;
        }

        const childRegionNames = (this.allRegions || [])
          .filter(r => String(r.parentId) === String(targetRegionId))
          .map(r => r.name.toLowerCase());
        regions = regions.filter(reg => childRegionNames.includes(reg.region.toLowerCase()));
      }

      regionsList.innerHTML = '';
      const emptyPlaceholder = container.querySelector('#sales-regions-empty');
      if (regions.length === 0) {
        if (emptyPlaceholder) emptyPlaceholder.removeAttribute('hidden');
      } else {
        if (emptyPlaceholder) emptyPlaceholder.setAttribute('hidden', '');
        const template = container.querySelector('#region-item-template');
        if (template) {
          regions.slice(0, 5).forEach((reg, i) => {
            const clone = template.content.cloneNode(true);
            const indexEl = clone.querySelector('.region-item-index');
            const nameEl = clone.querySelector('.region-item-name');
            const valueEl = clone.querySelector('.region-item-value');
            if (indexEl) indexEl.textContent = `${i + 1}.`;
            if (nameEl) nameEl.textContent = reg.region;
            if (valueEl) {
              valueEl.textContent = new Intl.NumberFormat(locale, {
                style: 'currency', currency: currencyCode, maximumFractionDigits: 0
              }).format(reg.sales);
            }
            regionsList.appendChild(clone);
          });
        }
      }
    };

    renderRegionsList();

    const resolveDatesForPeriod = (period) => {
      const today = new Date();
      let fromDate = new Date();
      let toDate   = new Date();

      switch (period) {
        case 'today':
          break;
        case 'yesterday':
          fromDate.setDate(today.getDate() - 1);
          toDate.setDate(today.getDate() - 1);
          break;
        case 'thisWeek': {
          const dow = today.getDay();
          fromDate.setDate(today.getDate() - dow);
          break;
        }
        case 'lastWeek':
          fromDate.setDate(today.getDate() - today.getDay() - 7);
          toDate.setDate(today.getDate() - today.getDay() - 1);
          break;
        case 'thisMonth':
        case 'MTD':
          fromDate = new Date(today.getFullYear(), today.getMonth(), 1);
          break;
        case 'lastMonth':
          fromDate = new Date(today.getFullYear(), today.getMonth() - 1, 1);
          toDate   = new Date(today.getFullYear(), today.getMonth(), 0);
          break;
        case 'quarter':
        case 'QTD': {
          const q = Math.floor(today.getMonth() / 3);
          fromDate = new Date(today.getFullYear(), q * 3, 1);
          break;
        }
        case 'year':
        case 'YTD':
          fromDate = new Date(today.getFullYear(), 0, 1);
          break;
        case 'custom': {
          const fromInput = container.querySelector('#sales-date-from');
          const toInput = container.querySelector('#sales-date-to');
          return {
            from: fromInput?.value || '',
            to: toInput?.value || ''
          };
        }
      }

      const fmt = (d) => {
        const y   = d.getFullYear();
        const m   = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        return `${y}-${m}-${day}`;
      };

      return { from: fmt(fromDate), to: fmt(toDate) };
    };

    const renderPeriod = async (period) => {
      const dates = resolveDatesForPeriod(period);
      
      if (totalEl) totalEl.textContent = 'Syncing...';
      if (targetEl) targetEl.textContent = 'Syncing...';

      try {
        const queryParams = { from: dates.from, to: dates.to };
        const restrictRegionId = this.config.restrictRegionId;
        const localRegionSelect = container.querySelector('#sales-region-select');
        const selectedRegionId = localRegionSelect ? localRegionSelect.value : 'all';

        if (selectedRegionId !== 'all') {
          queryParams.regionId = selectedRegionId;
        } else if (restrictRegionId && restrictRegionId !== 'ALL' && restrictRegionId !== 'all') {
          queryParams.regionId = restrictRegionId;
        } else {
          const storedFilters = localStorage.getItem('dashboard_filters');
          if (storedFilters) {
            try {
              const parsed = JSON.parse(storedFilters);
              if (parsed.regionId) {
                queryParams.regionId = parsed.regionId;
              } else if (parsed.nationId) {
                queryParams.regionId = parsed.nationId;
              }
              if (parsed.storeId) {
                queryParams.storeId = parsed.storeId;
              }
            } catch (e) {
              // ignore
            }
          }
        }
        
        const data = await dashboardService.getDashboardOverview(queryParams);
        if (data) {
          this.salesOverview = data.salesOverview || {};
          
          const isRestricted = restrictRegionId && restrictRegionId !== 'ALL' && restrictRegionId !== 'all';
          this.regionalPerformance = (isRestricted && data.subRegionalPerformance)
            ? data.subRegionalPerformance
            : (data.regionalPerformance || []);
          
          renderRegionsList();
        }
      } catch (err) {
        logger.error('NationalSalesChart', 'Failed to fetch sales overview data', err);
      }

      const salesVal = Number(this.salesOverview.totalSales || 0);
      const targetVal = Number(this.salesOverview.targetSales || 0);
      
      let trendPoints = [];
      if (this.salesOverview.trend && this.salesOverview.trend.length >= 2) {
        trendPoints = this.salesOverview.trend;
      } else if (this.salesOverview.trend && this.salesOverview.trend.length === 1) {
        trendPoints = [
          { date: 'Start', value: 0 },
          this.salesOverview.trend[0]
        ];
      } else {
        if (period === 'today') {
          trendPoints = [
            { date: '08:00', value: salesVal * 0.2 },
            { date: '12:00', value: salesVal * 0.5 },
            { date: '16:00', value: salesVal * 0.8 },
            { date: '20:00', value: salesVal }
          ];
        } else if (period === 'yesterday') {
          trendPoints = [
            { date: '08:00', value: salesVal * 0.15 },
            { date: '12:00', value: salesVal * 0.45 },
            { date: '16:00', value: salesVal * 0.75 },
            { date: '20:00', value: salesVal }
          ];
        } else if (period === 'thisWeek' || period === 'lastWeek') {
          trendPoints = [
            { date: 'Mon', value: salesVal * 0.15 },
            { date: 'Tue', value: salesVal * 0.3 },
            { date: 'Wed', value: salesVal * 0.45 },
            { date: 'Thu', value: salesVal * 0.6 },
            { date: 'Fri', value: salesVal * 0.75 },
            { date: 'Sat', value: salesVal * 0.9 },
            { date: 'Sun', value: salesVal }
          ];
        } else if (period === 'quarter') {
          trendPoints = [
            { date: 'Wk 01', value: salesVal * 0.35 },
            { date: 'Wk 02', value: salesVal * 0.5 },
            { date: 'Wk 03', value: salesVal * 0.75 },
            { date: 'Wk 04', value: salesVal }
          ];
        } else if (period === 'year') {
          trendPoints = [
            { date: 'Jan', value: salesVal * 0.4 },
            { date: 'Mar', value: salesVal * 0.7 },
            { date: 'May', value: salesVal }
          ];
        } else {
          trendPoints = [
            { date: 'Start', value: 0 },
            { date: 'End', value: salesVal }
          ];
        }
      }

      totalEl.textContent = new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 0
      }).format(salesVal);

      targetEl.textContent = new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 0
      }).format(targetVal);

      const achievement = targetVal > 0 ? ((salesVal / targetVal) * 100).toFixed(1) : '0.0';
      progressRing.setAttribute('stroke-dasharray', `${Math.min(100, Math.max(0, parseFloat(achievement)))}, 100`);
      progressText.textContent = `${achievement}%`;

      const width = 400;
      const height = 160;
      const padding = 25;

      const vals = trendPoints.map(d => Number(d.value));
      const minVal = Math.min(...vals) * 0.95;
      const maxVal = Math.max(...vals) * 1.05;
      const range = maxVal - minVal || 1;

      const points = trendPoints.map((d, index) => {
        const step = trendPoints.length > 1 ? (width - 2 * padding) / (trendPoints.length - 1) : (width - 2 * padding);
        const x = padding + (index * step);
        const y = height - padding - ((Number(d.value) - minVal) * (height - 2 * padding) / range);
        const labelText = d.date.includes('-') ? d.date.split('-').slice(1).join('/') : d.date;
        return { x, y, label: labelText, value: Number(d.value) };
      });

      const linePath = points.map(p => `${p.x},${p.y}`).join(' ');
      const areaPath = `M ${padding} ${height - padding} L ${linePath} L ${points[points.length - 1].x} ${height - padding} Z`;

      const areaPathEl = container.querySelector('#sales-chart-area');
      const linePathEl = container.querySelector('#sales-chart-line');
      const pointsGroup = container.querySelector('#sales-chart-points');
      const labelsGroup = container.querySelector('#sales-chart-labels');

      if (areaPathEl) areaPathEl.setAttribute('d', areaPath);
      if (linePathEl) linePathEl.setAttribute('d', `M ${linePath}`);

      if (pointsGroup) {
        pointsGroup.innerHTML = '';
        points.forEach((p, idx) => {
          const circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
          circle.setAttribute('id', `sales-point-${idx}`);
          circle.setAttribute('class', 'chart-point');
          circle.setAttribute('cx', p.x.toString());
          circle.setAttribute('cy', p.y.toString());
          circle.setAttribute('r', '3.5');
          circle.setAttribute('fill', 'var(--bg-app)');
          circle.setAttribute('stroke', 'var(--accent-primary)');
          circle.setAttribute('stroke-width', '2');
          pointsGroup.appendChild(circle);
        });
      }

      if (labelsGroup) {
        labelsGroup.innerHTML = '';
        points.filter((_, i) => {
          if (period === 'thisMonth' || period === 'MTD' || period === 'lastMonth') return i % 2 === 0;
          return true;
        }).forEach(p => {
          const text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
          text.setAttribute('x', p.x.toString());
          text.setAttribute('y', (height - padding + 15).toString());
          text.setAttribute('fill', 'var(--text-muted)');
          text.setAttribute('font-size', '8');
          text.setAttribute('text-anchor', 'middle');
          text.textContent = p.label;
          labelsGroup.appendChild(text);
        });
      }

      const overlay = container.querySelector('#sales-chart-hover-overlay');
      if (overlay) {
        const mouseMoveHandler = (e) => {
          const rect = canvasContainer.getBoundingClientRect();
          if (rect.width === 0) return;

          const svgMouseX = ((e.clientX - rect.left) / rect.width) * width;

          let minDistance = Infinity;
          let closestPoint = null;
          let closestIndex = -1;

          points.forEach((p, idx) => {
            const dist = Math.abs(p.x - svgMouseX);
            if (dist < minDistance) {
              minDistance = dist;
              closestPoint = p;
              closestIndex = idx;
            }
          });

          if (closestPoint) {
            points.forEach((_, idx) => {
              const circle = canvasContainer.querySelector(`#sales-point-${idx}`);
              if (circle) {
                if (idx === closestIndex) {
                  circle.setAttribute('r', '5.5');
                  circle.setAttribute('fill', 'var(--accent-primary)');
                } else {
                  circle.setAttribute('r', '3.5');
                  circle.setAttribute('fill', 'var(--bg-app)');
                }
              }
            });

            const formatted = new Intl.NumberFormat(locale, {
              style: 'currency', currency: currencyCode, maximumFractionDigits: 0
            }).format(closestPoint.value);

            const dateEl = tooltip.querySelector('.tooltip-date');
            const valueEl = tooltip.querySelector('.tooltip-value');
            if (dateEl) dateEl.textContent = closestPoint.label;
            if (valueEl) valueEl.textContent = formatted;

            const parentRect = chartWrapper.getBoundingClientRect();
            
            const elementPixelX = (closestPoint.x / width) * rect.width;
            const elementPixelY = (closestPoint.y / height) * rect.height;

            const relativeX = elementPixelX + (rect.left - parentRect.left);
            const relativeY = elementPixelY + (rect.top - parentRect.top);

            tooltip.style.left = `${relativeX - 40}px`;
            tooltip.style.top = `${relativeY - 45}px`;
            tooltip.style.display = 'block';
          }
        };

        const mouseLeaveHandler = () => {
          points.forEach((_, idx) => {
            const circle = canvasContainer.querySelector(`#sales-point-${idx}`);
            if (circle) {
              circle.setAttribute('r', '3.5');
              circle.setAttribute('fill', 'var(--bg-app)');
            }
          });
          tooltip.style.display = 'none';
        };

        overlay.addEventListener('mousemove', mouseMoveHandler);
        overlay.addEventListener('mouseleave', mouseLeaveHandler);

        lifecycle.onCleanup(() => {
          overlay.removeEventListener('mousemove', mouseMoveHandler);
          overlay.removeEventListener('mouseleave', mouseLeaveHandler);
        });
      }
    };

    renderPeriod('thisMonth');

    const selectEl = container.querySelector('#sales-period-select');
    const customInputsDiv = container.querySelector('#sales-custom-date-inputs');
    const dateFromInput = container.querySelector('#sales-date-from');
    const dateToInput = container.querySelector('#sales-date-to');

    const updateCustomVisibility = () => {
      if (selectEl && customInputsDiv) {
        if (selectEl.value === 'custom') {
          customInputsDiv.removeAttribute('hidden');
          customInputsDiv.style.display = 'flex';
        } else {
          customInputsDiv.setAttribute('hidden', '');
          customInputsDiv.style.display = 'none';
        }
      }
    };

    const handleCustomDateChange = async () => {
      if (dateFromInput && dateToInput && dateFromInput.value && dateToInput.value) {
        await renderPeriod('custom');
      }
    };

    if (selectEl) {
      const onChange = (e) => {
        e.stopPropagation();
        updateCustomVisibility();
        if (selectEl.value === 'custom') {
          handleCustomDateChange();
        } else {
          renderPeriod(selectEl.value);
        }
      };
      selectEl.addEventListener('change', onChange);
      lifecycle.onCleanup(() => {
        selectEl.removeEventListener('change', onChange);
      });
    }

    if (regionSelect) {
      const onRegionSelectChange = (e) => {
        e.stopPropagation();
        const activePeriod = selectEl ? selectEl.value : 'thisMonth';
        if (activePeriod === 'custom') {
          handleCustomDateChange();
        } else {
          renderPeriod(activePeriod);
        }
      };
      regionSelect.addEventListener('change', onRegionSelectChange);
      lifecycle.onCleanup(() => {
        regionSelect.removeEventListener('change', onRegionSelectChange);
      });
    }

    let defaultFrom = '';
    let defaultTo = '';
    const storedFilters = localStorage.getItem('dashboard_filters');
    if (storedFilters) {
      try {
        const parsed = JSON.parse(storedFilters);
        if (parsed.from) defaultFrom = parsed.from;
        if (parsed.to) defaultTo = parsed.to;
      } catch (e) {
        // ignore
      }
    }
    if (!defaultFrom || !defaultTo) {
      const today = new Date();
      const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
      const fmt = (d) => {
        const y   = d.getFullYear();
        const m   = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        return `${y}-${m}-${day}`;
      };
      defaultFrom = fmt(firstDay);
      defaultTo = fmt(today);
    }

    if (dateFromInput) {
      dateFromInput.value = defaultFrom;
      dateFromInput.addEventListener('change', handleCustomDateChange);
      lifecycle.onCleanup(() => {
        dateFromInput.removeEventListener('change', handleCustomDateChange);
      });
    }
    if (dateToInput) {
      dateToInput.value = defaultTo;
      dateToInput.addEventListener('change', handleCustomDateChange);
      lifecycle.onCleanup(() => {
        dateToInput.removeEventListener('change', handleCustomDateChange);
      });
    }

    updateCustomVisibility();
  }

  _showExplanationModal(container) {
    const modal = container.querySelector('#sales-explanation-modal');
    if (modal) {
      modal.removeAttribute('hidden');
      
      if (!modal.dataset.bound) {
        modal.dataset.bound = 'true';
        
        const closeModal = () => modal.setAttribute('hidden', '');
        modal.querySelector('#close-sales-expl-modal').addEventListener('click', closeModal);
        modal.querySelector('#close-sales-expl-modal-btn').addEventListener('click', closeModal);
        modal.addEventListener('click', (e) => {
          if (e.target === modal) {
            closeModal();
          }
        });
      }
    }
  }
}
