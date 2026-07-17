/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Widgets Module
 * File              : financial-chart.js
 * Path              : frontend/widgets/charts/financial-chart.js
 * Purpose           : Frontend utility: financial-chart for PLUS33 Coffee ERP
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
 * Frontend utility: financial-chart for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../../core/logger.js';
import { htmlLoader } from '../../../core/htmlLoader.js';
import { dashboardService } from '../../../services/dashboard/DashboardService.js';
import { apiClient } from '../../../api/client.js';
import { authStore } from '../../../store/authStore.js';

export class FinancialChart {
  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  constructor(config, financialOverview) {
    this.config = config;
    this.financialOverview = financialOverview || { totalRevenue: 0, totalExpenses: 0, totalProfit: 0, profitMargin: 0, trend: [] };
  }

  _loadCss() {
    const href = 'widgets/charts/financial-chart/financial-chart.css';
    if (!document.querySelector(`link[href="${href}"]`)) {
      const link = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = href;
      document.head.appendChild(link);
    }
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  async mount(container, lifecycle) {
    logger.debug('FinancialChart', 'Rendering Financial Overview bar chart...');
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

    let html = await htmlLoader.load('widgets/charts/financial-chart/financial-chart.html');
    html = html.replace('{{TITLE}}', this.config.title)
               .replace('{{REV}}', '--')
               .replace('{{EXP}}', '--')
               .replace('{{MARGIN}}', '--');

    container.innerHTML = html;

    // Calculate and assign week option texts dynamically
    const optCurrent = container.querySelector('#opt-current-week');
    const optLast = container.querySelector('#opt-last-week');
    const optTwo = container.querySelector('#opt-two-weeks');

    const formatShort = (d) => {
      const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
      return `${months[d.getMonth()]} ${d.getDate()}`;
    };

    const today = new Date();
    const curStart = new Date(today);
    curStart.setDate(today.getDate() - today.getDay());
    const curEnd = new Date(curStart);
    curEnd.setDate(curStart.getDate() + 6);

    const lstStart = new Date(curStart);
    lstStart.setDate(curStart.getDate() - 7);
    const lstEnd = new Date(curStart);
    lstEnd.setDate(curStart.getDate() - 1);

    const twoStart = new Date(curStart);
    twoStart.setDate(curStart.getDate() - 14);
    const twoEnd = new Date(curStart);
    twoEnd.setDate(curStart.getDate() - 1);

    if (optCurrent) optCurrent.textContent = `Current Week (${formatShort(curStart)} - ${formatShort(curEnd)})`;
    if (optLast) optLast.textContent = `Last Week (${formatShort(lstStart)} - ${formatShort(lstEnd)})`;
    if (optTwo) optTwo.textContent = `Two Weeks (${formatShort(twoStart)} - ${formatShort(twoEnd)})`;

    const width = 450;
    const height = 150;
    const padding = 20;

    const barsGroup = container.querySelector('#financial-chart-bars');
    const trendWrapper = container.querySelector('.trend-svg-wrapper');
    if (trendWrapper) {
      trendWrapper.style.height = `${height}px`;
    }

    const renderFinancialPeriod = async (weekValue) => {
      let fromDateStr = '';
      let toDateStr = '';
      
      const currentYear = today.getFullYear();
      const currentMonth = today.getMonth();

      const fmt = (d) => {
        const y   = d.getFullYear();
        const m   = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        return `${y}-${m}-${day}`;
      };

      if (weekValue === 'mtd') {
        let defaultFrom = fmt(new Date(currentYear, currentMonth, 1));
        let defaultTo = fmt(today);
        const username = authStore.getUser()?.username || 'default';
        const hash = window.location.hash || '';
        let key = `dashboard_filters_${username}`;
        if (hash.includes('regional-warehouse')) {
          key = `regional_warehouse_admin_dashboard_filters_${username}`;
        } else if (hash.includes('national-warehouse')) {
          key = `national_wh_admin_dashboard_filters_${username}`;
        } else if (hash.includes('regional-dashboard')) {
          key = `regional_admin_dashboard_filters_${username}`;
        } else if (hash.includes('national-dashboard')) {
          key = `national_admin_dashboard_filters_${username}`;
        }
        const storedFilters = localStorage.getItem(key);
        if (storedFilters) {
          try {
            const parsed = JSON.parse(storedFilters);
            if (parsed.from) defaultFrom = parsed.from;
            if (parsed.to) defaultTo = parsed.to;
          } catch (e) {
            // ignore
          }
        }
        fromDateStr = defaultFrom;
        toDateStr = defaultTo;
      } else if (weekValue === 'current') {
        fromDateStr = fmt(curStart);
        toDateStr = fmt(curEnd);
      } else if (weekValue === 'last') {
        fromDateStr = fmt(lstStart);
        toDateStr = fmt(lstEnd);
      } else if (weekValue === 'twoWeeks') {
        fromDateStr = fmt(twoStart);
        toDateStr = fmt(twoEnd);
      } else if (weekValue === 'custom') {
        const startInput = container.querySelector('#financial-date-start');
        if (startInput && startInput.value) {
          const startD = new Date(startInput.value);
          const endD = new Date(startD);
          endD.setDate(startD.getDate() + 6);
          fromDateStr = fmt(startD);
          toDateStr = fmt(endD);
        }
      }

      if (!fromDateStr || !toDateStr) return; // Wait for custom inputs if empty

      // Calculate previous comparison period start/end dates
      let prevFromStr = '';
      let prevToStr = '';
      try {
        const fromD = new Date(fromDateStr);
        const toD = new Date(toDateStr);
        const diffTime = Math.abs(toD - fromD);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;

        const prevToD = new Date(fromD);
        prevToD.setDate(fromD.getDate() - 1);
        const prevFromD = new Date(prevToD);
        prevFromD.setDate(prevToD.getDate() - diffDays + 1);

        prevFromStr = fmt(prevFromD);
        prevToStr = fmt(prevToD);
      } catch (e) {
        // ignore
      }

      const valueRevEl = container.querySelector('#financial-card-rev');
      const valueExpEl = container.querySelector('#financial-card-exp');
      const valueMarginEl = container.querySelector('#financial-card-margin');
      if (valueRevEl) valueRevEl.textContent = 'Syncing...';
      if (valueExpEl) valueExpEl.textContent = 'Syncing...';

      let prevProfitVal = 0;
      try {
        const queryParams = { from: fromDateStr, to: toDateStr };
        
        const localNationSelect = container.querySelector('#financial-nation-select');
        const selectedNationId = localNationSelect ? localNationSelect.value : 'all';

        if (selectedNationId !== 'all') {
          queryParams.regionId = selectedNationId;
        } else {
          const username = authStore.getUser()?.username || 'default';
          const hash = window.location.hash || '';
          let key = `dashboard_filters_${username}`;
          if (hash.includes('regional-warehouse')) {
            key = `regional_warehouse_admin_dashboard_filters_${username}`;
          } else if (hash.includes('national-warehouse')) {
            key = `national_wh_admin_dashboard_filters_${username}`;
          } else if (hash.includes('regional-dashboard')) {
            key = `regional_admin_dashboard_filters_${username}`;
          } else if (hash.includes('national-dashboard')) {
            key = `national_admin_dashboard_filters_${username}`;
          }
          const storedFilters = localStorage.getItem(key);
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
        if (data && data.financialOverview) {
          this.financialOverview = data.financialOverview;
        }

        // Fetch previous period's data to calculate Profit Range growth percentage
        if (prevFromStr && prevToStr) {
          const prevQueryParams = { ...queryParams, from: prevFromStr, to: prevToStr };
          const prevData = await dashboardService.getDashboardOverview(prevQueryParams);
          if (prevData && prevData.financialOverview) {
            const prevRev = Number(prevData.financialOverview.totalRevenue || 0);
            const prevExp = Number(prevData.financialOverview.totalExpenses || 0);
            prevProfitVal = prevRev - prevExp;
          }
        }
      } catch (err) {
        logger.error('FinancialChart', 'Failed to fetch weekly financial overview', err);
      }

      const currentRevVal = Number(this.financialOverview.totalRevenue || 0);
      const currentTargetVal = Number(this.financialOverview.totalExpenses || 0);
      const currentProfitVal = currentRevVal - currentTargetVal;

      const formattedRev = new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 0
      }).format(currentRevVal);

      const formattedExp = new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 0
      }).format(currentTargetVal);

      const formattedMargin = new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 0
      }).format(currentProfitVal);

      if (valueRevEl) valueRevEl.textContent = formattedRev;
      if (valueExpEl) valueExpEl.textContent = formattedExp;
      if (valueMarginEl) valueMarginEl.textContent = formattedMargin;

      // Calculate profit growth comparison percentage
      let growthPctText = '0.0%';
      let growthColor = 'var(--text-muted)';
      if (prevProfitVal === 0) {
        if (currentProfitVal > 0) {
          growthPctText = '+100.0%';
          growthColor = 'var(--status-success)';
        } else if (currentProfitVal < 0) {
          growthPctText = '-100.0%';
          growthColor = 'var(--status-danger)';
        }
      } else {
        const growth = ((currentProfitVal - prevProfitVal) / Math.abs(prevProfitVal)) * 100;
        growthPctText = (growth >= 0 ? '+' : '') + growth.toFixed(1) + '%';
        growthColor = growth >= 0 ? 'var(--status-success)' : 'var(--status-danger)';
      }

      this.growthPctText = growthPctText;
      this.growthColor = growthColor;

      const growthEl = container.querySelector('#financial-card-growth');
      if (growthEl) {
        growthEl.textContent = growthPctText;
        growthEl.style.color = growthColor;
      }

      const titleEl = container.querySelector('#financial-card-title');
      if (titleEl) {
        if (weekValue === 'mtd') titleEl.textContent = 'MTD Overall';
        else if (weekValue === 'current') titleEl.textContent = 'Current Week';
        else if (weekValue === 'last') titleEl.textContent = 'Last Week';
        else if (weekValue === 'twoWeeks') titleEl.textContent = `${formatShort(twoStart)} - ${formatShort(twoEnd)}`;
        else titleEl.textContent = 'Custom Range';
      }

      // Redraw SVG Bar chart
      const currentTrend = this.financialOverview.trend || [];
      let currentDaysData = [];

      if (currentTrend.length > 0) {
        currentDaysData = currentTrend.map((t, index) => {
          const dateParts = t.month.split('-');
          const label = dateParts.length === 3 ? `${dateParts[1]}/${dateParts[2]}` : t.month;
          return {
            label: label,
            revenue: Number(t.revenue || 0),
            expense: Number(t.expense || 0),
            isToday: (index === currentTrend.length - 1)
          };
        });
      } else {
        const daysDiff = Math.max(1, Math.round((new Date(toDateStr) - new Date(fromDateStr)) / (1000 * 60 * 60 * 24)) + 1);
        const startDayDate = new Date(fromDateStr);
        for (let i = 0; i < daysDiff; i++) {
          const d = new Date(startDayDate);
          d.setDate(startDayDate.getDate() + i);
          const dayLabel = `${String(d.getMonth() + 1).padStart(2, '0')}/${String(d.getDate()).padStart(2, '0')}`;
          currentDaysData.push({
            label: dayLabel,
            revenue: i === daysDiff - 1 ? currentRevVal : 0,
            expense: 0,
            isToday: i === daysDiff - 1
          });
        }
      }

      const maxVal = Math.max(...currentDaysData.map(d => Number(d.revenue))) * 1.15 || 1;
      const stepWidth = (width - 2 * padding) / currentDaysData.length;
      const barWidth = Math.min(24, Math.max(12, Math.floor(stepWidth * 0.55)));

      if (barsGroup) {
        barsGroup.innerHTML = '';
        currentDaysData.forEach((d, i) => {
          const x = padding + i * stepWidth;
          const barHeight = (Number(d.revenue) / maxVal) * (height - 2 * padding);
          const barY = height - padding - barHeight;
          const barX = x + (stepWidth - barWidth) / 2;
          const barColorId = d.isToday ? 'grad-today' : 'grad-prev';

          const dailyRevenue = Number(d.revenue || 0);
          const dailyExpense = Number(d.expense || 0);
          const dailyProfit = dailyRevenue - dailyExpense;
          const dailyMargin = dailyRevenue > 0 ? ((dailyProfit / dailyRevenue) * 100).toFixed(0) : '0';

          const r = Math.min(2, barWidth / 2);
          const bx = barX, by = barY, bw = barWidth, bh = barHeight;
          const barPath = bh > r
            ? `M${bx},${by + bh} L${bx},${by + r} Q${bx},${by} ${bx + r},${by} L${bx + bw - r},${by} Q${bx + bw},${by} ${bx + bw},${by + r} L${bx + bw},${by + bh} Z`
            : `M${bx},${by + bh} L${bx},${by} L${bx + bw},${by} L${bx + bw},${by + bh} Z`;

          const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
          path.setAttribute('class', 'financial-bar');
          path.setAttribute('d', barPath);
          path.setAttribute('fill', `url(#${barColorId})`);
          path.setAttribute('data-date', d.label);
          path.setAttribute('data-revenue', dailyRevenue.toString());
          path.setAttribute('data-expense', dailyExpense.toString());
          path.setAttribute('data-margin', dailyMargin);
          barsGroup.appendChild(path);

          const textVal = document.createElementNS('http://www.w3.org/2000/svg', 'text');
          textVal.setAttribute('x', (barX + barWidth / 2).toString());
          textVal.setAttribute('y', (barY - 5).toString());
          textVal.setAttribute('fill', 'var(--text-primary)');
          textVal.setAttribute('font-size', '8');
          textVal.setAttribute('font-weight', '700');
          textVal.setAttribute('text-anchor', 'middle');
          textVal.setAttribute('class', 'bar-label-val');
          textVal.textContent = `${dailyMargin}%`;
          barsGroup.appendChild(textVal);

          const textLabel = document.createElementNS('http://www.w3.org/2000/svg', 'text');
          textLabel.setAttribute('x', (x + stepWidth / 2).toString());
          textLabel.setAttribute('y', (height - 3).toString());
          textLabel.setAttribute('fill', 'var(--text-muted)');
          textLabel.setAttribute('font-size', '8');
          textLabel.setAttribute('text-anchor', 'middle');
          textLabel.setAttribute('class', 'bar-label-date');
          textLabel.textContent = d.label;
          barsGroup.appendChild(textLabel);
        });

        // Re-bind click handlers to new bars
        const bars = container.querySelectorAll('.financial-bar');
        bars.forEach(bar => {
          bar.addEventListener('click', (e) => {
            e.stopPropagation();
            const date = bar.getAttribute('data-date');
            const rev = Number(bar.getAttribute('data-revenue'));
            const exp = Number(bar.getAttribute('data-expense'));
            const margin = bar.getAttribute('data-margin');

            const formattedRev = new Intl.NumberFormat(locale, {
              style: 'currency', currency: currencyCode, maximumFractionDigits: 0
            }).format(rev);

            const periodDays = Number(this.financialOverview.periodDays || 30);
            const dailyTarget = currentTargetVal / periodDays;
            const dailyProfit = rev - dailyTarget;

            const formattedTarget = new Intl.NumberFormat(locale, {
              style: 'currency', currency: currencyCode, maximumFractionDigits: 0
            }).format(dailyTarget);

            const formattedProfit = new Intl.NumberFormat(locale, {
              style: 'currency', currency: currencyCode, maximumFractionDigits: 0
            }).format(dailyProfit);

            const cardTitle = container.querySelector('#financial-card-title');
            const cardRev = container.querySelector('#financial-card-rev');
            const cardExp = container.querySelector('#financial-card-exp');
            const cardMargin = container.querySelector('#financial-card-margin');
            const cardGrowth = container.querySelector('#financial-card-growth');

            if (cardTitle) cardTitle.textContent = `Day: ${date}`;
            if (cardRev) cardRev.textContent = formattedRev;
            if (cardExp) cardExp.textContent = formattedTarget;
            if (cardMargin) cardMargin.textContent = formattedProfit;
            if (cardGrowth) {
              cardGrowth.textContent = "N/A";
              cardGrowth.style.color = "var(--text-muted)";
            }

            bars.forEach(b => {
              b.setAttribute('opacity', '0.4');
              b.setAttribute('stroke', 'none');
            });
            bar.setAttribute('opacity', '1.0');
            bar.setAttribute('stroke', 'var(--text-primary)');
            bar.setAttribute('stroke-width', '1.5');
          });
        });
      }
    };

    // Load nations dynamically
    const nationSelect = container.querySelector('#financial-nation-select');
    if (nationSelect) {
      try {
        const res = await apiClient.get('/api/v1/regions', { size: 100 });
        if (res && res.success && res.data && res.data.content) {
          const allRegions = res.data.content;
          
          const restrictRegionId = this.config.restrictRegionId;
          if (restrictRegionId && restrictRegionId !== 'ALL' && restrictRegionId !== 'all') {
            const restrictedRegion = allRegions.find(r => String(r.id) === String(restrictRegionId));
            if (restrictedRegion) {
              nationSelect.innerHTML = '';
              const opt = document.createElement('option');
              opt.value = restrictedRegion.id;
              opt.textContent = restrictedRegion.name;
              opt.selected = true;
              nationSelect.appendChild(opt);
              nationSelect.disabled = true;
            }
          } else {
            // Filter parent regions (nations) where parentId is null/undefined
            const nations = allRegions.filter(r => r.parentId === null || r.parentId === undefined);
            nations.forEach(n => {
              const opt = document.createElement('option');
              opt.value = n.id;
              opt.textContent = n.name;
              nationSelect.appendChild(opt);
            });
          }
        }
      } catch (err) {
        logger.error('FinancialChart', 'Failed to load nations for dropdown filter', err);
      }

      const onNationChange = (e) => {
        e.stopPropagation();
        renderFinancialPeriod(weekSelect ? weekSelect.value : 'current');
      };
      nationSelect.addEventListener('change', onNationChange);
      lifecycle.onCleanup(() => {
        nationSelect.removeEventListener('change', onNationChange);
      });
    }

    // Load initial view
    await renderFinancialPeriod('current');

    // Period select dropdown interactivity
    const weekSelect = container.querySelector('#financial-week-select');
    const customInputsDiv = container.querySelector('#financial-custom-date-inputs');
    const startInput = container.querySelector('#financial-date-start');

    const updateCustomVisibility = () => {
      if (weekSelect && customInputsDiv) {
        if (weekSelect.value === 'custom') {
          customInputsDiv.removeAttribute('hidden');
          customInputsDiv.style.display = 'flex';
        } else {
          customInputsDiv.setAttribute('hidden', '');
          customInputsDiv.style.display = 'none';
        }
      }
    };

    if (weekSelect) {
      const onWeekChange = (e) => {
        e.stopPropagation();
        updateCustomVisibility();
        renderFinancialPeriod(weekSelect.value);
      };
      weekSelect.addEventListener('change', onWeekChange);
      lifecycle.onCleanup(() => {
        weekSelect.removeEventListener('change', onWeekChange);
      });
    }

    if (startInput) {
      const onCustomDateChange = () => {
        if (startInput.value) {
          renderFinancialPeriod('custom');
        }
      };
      startInput.addEventListener('change', onCustomDateChange);
      lifecycle.onCleanup(() => {
        startInput.removeEventListener('change', onCustomDateChange);
      });
    }

    // Reset metrics to active period when clicking elsewhere on the card
    const cardResetHandler = () => {
      const cardTitle = container.querySelector('#financial-card-title');
      const cardRev = container.querySelector('#financial-card-rev');
      const cardExp = container.querySelector('#financial-card-exp');
      const cardMargin = container.querySelector('#financial-card-margin');
      const cardGrowth = container.querySelector('#financial-card-growth');

      const activeWeekValue = weekSelect ? weekSelect.value : 'current';

      const currentRevVal = Number(this.financialOverview.totalRevenue || 0);
      const currentTargetVal = Number(this.financialOverview.totalExpenses || 0);
      const currentProfitVal = currentRevVal - currentTargetVal;

      const formattedRev = new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 0
      }).format(currentRevVal);

      const formattedExp = new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 0
      }).format(currentTargetVal);

      const formattedMargin = new Intl.NumberFormat(locale, {
        style: 'currency', currency: currencyCode, maximumFractionDigits: 0
      }).format(currentProfitVal);

      if (cardTitle) {
        if (activeWeekValue === 'mtd') cardTitle.textContent = "MTD Overall";
        else if (activeWeekValue === 'current') cardTitle.textContent = "Current Week";
        else if (activeWeekValue === 'last') cardTitle.textContent = "Last Week";
        else if (activeWeekValue === 'twoWeeks') cardTitle.textContent = `${formatShort(twoStart)} - ${formatShort(twoEnd)}`;
        else cardTitle.textContent = "Custom Range";
      }
      if (cardRev) cardRev.textContent = formattedRev;
      if (cardExp) cardExp.textContent = formattedExp;
      if (cardMargin) cardMargin.textContent = formattedMargin;
      if (cardGrowth) {
        cardGrowth.textContent = this.growthPctText || '--';
        cardGrowth.style.color = this.growthColor || 'var(--text-muted)';
      }

      const bars = container.querySelectorAll('.financial-bar');
      bars.forEach(b => {
        b.setAttribute('opacity', '1.0');
        b.setAttribute('stroke', 'none');
      });
    };

    container.addEventListener('click', cardResetHandler);
    lifecycle.onCleanup(() => {
      container.removeEventListener('click', cardResetHandler);
    });

    // Bind info button click
    const infoBtn = container.querySelector('#financial-info-btn');
    if (infoBtn) {
      const onInfoClick = (e) => {
        e.preventDefault();
        e.stopPropagation();
        this._showExplanationModal(container);
      };
      infoBtn.addEventListener('click', onInfoClick);
      lifecycle.onCleanup(() => {
        infoBtn.removeEventListener('click', onInfoClick);
      });
    }

    this.bindEvents(container, lifecycle);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Widgets Module
   */
  bindEvents(container, lifecycle) {
    import('../../../store/notificationStore.js').then(({ notificationStore }) => {
      const approveBtn = container.querySelector('.btn-action-approve-budgets');
      const reportsBtn = container.querySelector('.btn-action-financial-reports');
      const forecastsBtn = container.querySelector('.btn-action-review-forecasts');

      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      if (approveBtn) {
        /**
         * Performs the h operation in this module.
         * @memberof Widgets Module
         */
        const h = () => notificationStore.success('Opening budget pending approvals workspace...');
        approveBtn.addEventListener('click', h);
        lifecycle.onCleanup(() => approveBtn.removeEventListener('click', h));
      }
      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      if (reportsBtn) {
        /**
         * Performs the h operation in this module.
         * @memberof Widgets Module
         */
        const h = () => notificationStore.success('Consolidated profit & loss statement loading...');
        reportsBtn.addEventListener('click', h);
        lifecycle.onCleanup(() => reportsBtn.removeEventListener('click', h));
      }
      /**
       * Performs the fn operation in this module.
       * @memberof Widgets Module
       */
      if (forecastsBtn) {
        /**
         * Performs the h operation in this module.
         * @memberof Widgets Module
         */
        const h = () => notificationStore.success('Consolidated multi-region cost forecasting report verified.');
        forecastsBtn.addEventListener('click', h);
        lifecycle.onCleanup(() => forecastsBtn.removeEventListener('click', h));
      }
    });
  }

  _showExplanationModal(container) {
    const modal = container.querySelector('#financial-explanation-modal');
    if (modal) {
      modal.removeAttribute('hidden');
      
      if (!modal.dataset.bound) {
        modal.dataset.bound = 'true';
        
        const closeModal = () => modal.setAttribute('hidden', '');
        modal.querySelector('#close-financial-expl-modal').addEventListener('click', closeModal);
        modal.querySelector('#close-financial-expl-modal-btn').addEventListener('click', closeModal);
        modal.addEventListener('click', (e) => {
          if (e.target === modal) {
            closeModal();
          }
        });
      }
    }
  }
}
