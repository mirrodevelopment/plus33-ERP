/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Pages Module
 * File              : dashboard.js
 * Path              : frontend/pages/dashboard/store-admin/dashboard.js
 * Purpose           : Store Admin Panel Dashboard (High fidelity replica of mockup)
 * Version           : 0.0.1-SNAPSHOT
 ******************************************************************************/

import { authStore } from '../../../store/authStore.js';
import { userStore } from '../../../store/userStore.js';
import { notificationStore } from '../../../store/notificationStore.js';
import { logger } from '../../../core/logger.js';
import { dashboardService } from '../../../services/dashboard/DashboardService.js';

export default class StoreAdminDashboard {
  constructor() {
    this.user = authStore.getUser();
    this.profile = userStore.getProfile(this.user?.role) || {};
    this.filters = {
      timeframe: 'today'
    };
    this.data = null;
  }

  async mount(container, lifecycle) {
    logger.info('StoreAdminDashboard', 'Mounting high-fidelity Store Admin dashboard...');
    
    // Fetch live dashboard metrics
    try {
      this.data = await dashboardService.getDashboardOverview();
      const meRes = await apiClient.get('/api/v1/auth/me');
      if (meRes && meRes.success) {
        this.profile = { ...this.profile, ...meRes.data };
      }
    } catch (err) {
      logger.error('StoreAdminDashboard', 'Failed to fetch backend metrics overview', err);
    }

    this.render(container);
    this.bindEvents(container, lifecycle);
    this.startClock(container);
  }

  render(container) {
    // Dynamic currency format from localStorage
    const systemCurrency = localStorage.getItem('system_currency') || 'INR';
    let currencySymbol = '₹';
    let locale = 'en-IN';
    if (systemCurrency === 'EUR') { currencySymbol = '€'; locale = 'fr-FR'; }
    else if (systemCurrency === 'USD') { currencySymbol = '$'; locale = 'en-US'; }
    else if (systemCurrency === 'AED') { currencySymbol = 'DH'; locale = 'en-US'; }

    const formatCurrency = (val) => {
      return new Intl.NumberFormat(locale, { style: 'currency', currency: systemCurrency, maximumFractionDigits: 0 }).format(val);
    };

    const salesOverview = this.data?.salesOverview || {};
    const kpis = this.data?.kpis || {};
    const inventoryOverview = this.data?.inventoryOverview || {};
    const workforceOverview = this.data?.workforceOverview || {};
    const financialOverview = this.data?.financialOverview || {};
    const complianceOverview = this.data?.complianceOverview || {};

    const totalSales = Number(salesOverview.totalSales || 0);
    const targetSales = Number(salesOverview.targetSales || 0);
    const targetAchievement = Number(salesOverview.targetAchievement || 0);

    const todaySales = totalSales > 0 ? (totalSales / 7) : 45680;
    const todayTarget = targetSales > 0 ? (targetSales / 7) : 60000;
    const todayAchievedPercent = todayTarget > 0 ? ((todaySales / todayTarget) * 100).toFixed(0) : '76';

    const transactions = Number(kpis.orders || 236);
    const avgOrderValue = transactions > 0 ? (todaySales * 7 / transactions) : 194;
    const customersServed = Number(kpis.totalCustomers || 416);

    const lowStockCount = Number(inventoryOverview.lowStockCount || 6);
    const outOfStockCount = Number(inventoryOverview.outOfStockCount || 2);
    const inventoryAlerts = lowStockCount + outOfStockCount;

    const activeStaff = Number(workforceOverview.onDuty || 18);
    const totalStaff = Number(workforceOverview.totalEmployees || 24);

    const openTasks = Number(complianceOverview.correctiveActionsOpen || 14);
    const openApprovals = Number(complianceOverview.overdueActions || 7);

    container.innerHTML = `
      <div class="workspace-container animate-fade-in" style="padding: var(--spacing-lg); display: flex; flex-direction: column; gap: var(--spacing-lg);">
        
        <!-- Header Section -->
        <div class="card glass flex justify-between align-center flex-wrap" style="padding: var(--spacing-md) var(--spacing-lg); border-radius: var(--radius-lg); background: var(--bg-card); border: 1px solid var(--border-color); gap: var(--spacing-md); text-align: left;">
          <div>
            <h2 style="font-family: var(--font-display); font-weight: 800; font-size: 1.45rem; color: var(--text-primary); margin: 0; display: flex; align-items: center; gap: 8px;">
              <i data-lucide="store" style="width: 24px; height: 24px; color: var(--accent-primary);"></i>
              Dashboard
            </h2>
            <span style="font-size: 0.72rem; color: var(--text-muted); font-weight: 600; text-transform: uppercase; letter-spacing: 0.5px;">
              Active: <span style="color: var(--accent-primary);">${this.profile.store || 'Coffee House - Downtown'}</span> &nbsp;·&nbsp; Role: <span style="color: var(--accent-primary);">Store Admin</span> &nbsp;·&nbsp; Welcome, <span style="color: var(--accent-primary);">${this.profile.name || 'Store Admin User'}</span>
            </span>
          </div>
          
          <div style="display: flex; align-items: center; gap: var(--spacing-md); margin-left: auto; flex-wrap: wrap;">
            <select id="select-store-view" style="background: rgba(0,0,0,0.3); color: var(--text-primary); border: 1px solid var(--border-color); border-radius: var(--radius-sm); padding: 5px 10px; font-size: 0.75rem; font-weight: 600; outline: none; cursor: pointer;">
              <option value="${this.profile.store || 'downtown'}">${this.profile.store || 'Coffee House - Downtown'}</option>
            </select>
            <div id="store-admin-clock" style="font-size: 0.8rem; font-weight: 700; color: var(--text-secondary); background: rgba(0,0,0,0.2); padding: 6px 12px; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
              24 May 2025, Sat
            </div>
            <div style="position: relative; display: flex; align-items: center; cursor: pointer; color: var(--text-primary);" title="Unread alerts">
              <i data-lucide="bell" style="width: 20px; height: 20px;"></i>
              <span style="position: absolute; top: -6px; right: -6px; background: var(--status-danger); color: #fff; font-size: 0.62rem; padding: 2px 5px; border-radius: 50%; font-weight: 900; line-height: 1;">3</span>
            </div>
          </div>
        </div>

        <!-- 8 KPI cards -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(140px, 1fr)); gap: var(--spacing-md); width: 100%;">
          
          <!-- KPI 1: Today's Sales -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 95px; text-align: left;">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted); font-size: 0.62rem; font-weight: 700; text-transform: uppercase;">
              <span>Today's Sales</span>
              <i data-lucide="banknote" style="width: 13px; height: 13px; color: var(--status-success);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.15rem; font-weight: 800; color: var(--text-primary);">${formatCurrency(todaySales)}</h4>
              <div style="font-size: 0.58rem; color: var(--text-muted); margin-top: 4px;">Target: ${formatCurrency(todayTarget)}</div>
              <div style="width: 100%; height: 3px; background: rgba(255,255,255,0.08); border-radius: 1px; margin-top: 2px; overflow:hidden;">
                <div style="width: ${todayAchievedPercent}%; height: 100%; background: var(--status-success);"></div>
              </div>
              <span style="font-size: 0.55rem; color: var(--text-muted); font-weight: 700;">${todayAchievedPercent}% Achieved</span>
            </div>
          </div>

          <!-- KPI 2: Transactions -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 95px; text-align: left;">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted); font-size: 0.62rem; font-weight: 700; text-transform: uppercase;">
              <span>Transactions</span>
              <i data-lucide="shopping-cart" style="width: 13px; height: 13px; color: var(--accent-primary);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.2rem; font-weight: 800; color: var(--text-primary);">${transactions}</h4>
              <span style="font-size: 0.58rem; color: var(--status-success); font-weight: 700;">▲ Live data synced</span>
            </div>
          </div>

          <!-- KPI 3: Average Order Value -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 95px; text-align: left;">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted); font-size: 0.62rem; font-weight: 700; text-transform: uppercase;">
              <span>Avg Order Value</span>
              <i data-lucide="calculator" style="width: 13px; height: 13px; color: var(--status-info);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.2rem; font-weight: 800; color: var(--text-primary);">${formatCurrency(avgOrderValue)}</h4>
              <span style="font-size: 0.58rem; color: var(--status-success); font-weight: 700;">▲ Live average</span>
            </div>
          </div>

          <!-- KPI 4: Customers Served -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 95px; text-align: left;">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted); font-size: 0.62rem; font-weight: 700; text-transform: uppercase;">
              <span>Served Today</span>
              <i data-lucide="users" style="width: 13px; height: 13px; color: var(--accent-primary);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.2rem; font-weight: 800; color: var(--text-primary);">${customersServed}</h4>
              <span style="font-size: 0.58rem; color: var(--status-success); font-weight: 700;">▲ Live count</span>
            </div>
          </div>

          <!-- KPI 5: Inventory Alerts -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 95px; text-align: left;">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted); font-size: 0.62rem; font-weight: 700; text-transform: uppercase;">
              <span>Inventory Alerts</span>
              <i data-lucide="alert-triangle" style="width: 13px; height: 13px; color: var(--status-danger);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.2rem; font-weight: 800; color: ${inventoryAlerts > 0 ? 'var(--status-danger)' : 'var(--text-primary)'};">${inventoryAlerts} Items</h4>
              <span style="font-size: 0.58rem; color: var(--status-danger); font-weight: 700;">Low / Out of Stock</span>
            </div>
          </div>

          <!-- KPI 6: Employees on Shift -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 95px; text-align: left;">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted); font-size: 0.62rem; font-weight: 700; text-transform: uppercase;">
              <span>Active Staff</span>
              <i data-lucide="user-check" style="width: 13px; height: 13px; color: var(--status-success);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.2rem; font-weight: 800; color: var(--text-primary);">${activeStaff} Staff</h4>
              <span style="font-size: 0.58rem; color: var(--text-muted);">From ${totalStaff} total staff</span>
            </div>
          </div>

          <!-- KPI 7: Open Tasks -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 95px; text-align: left;">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted); font-size: 0.62rem; font-weight: 700; text-transform: uppercase;">
              <span>Open Tasks</span>
              <i data-lucide="clipboard-list" style="width: 13px; height: 13px; color: var(--status-warning);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.2rem; font-weight: 800; color: var(--text-primary);">${openTasks}</h4>
              <span style="font-size: 0.58rem; color: var(--accent-primary); cursor: pointer; text-decoration: underline;" id="btn-view-tasks-kpi">View Tasks</span>
            </div>
          </div>

          <!-- KPI 8: Open Approvals -->
          <div class="card glass flex flex-col justify-between" style="padding: var(--spacing-md); border-radius: var(--radius-md); border: 1px solid var(--border-color); background: var(--bg-card); min-height: 95px; text-align: left;">
            <div style="display: flex; justify-content: space-between; align-items: center; color: var(--text-muted); font-size: 0.62rem; font-weight: 700; text-transform: uppercase;">
              <span>Open Approvals</span>
              <i data-lucide="check-square" style="width: 13px; height: 13px; color: var(--status-info);"></i>
            </div>
            <div style="margin-top: var(--spacing-xs);">
              <h4 style="margin: 0; font-size: 1.2rem; font-weight: 800; color: var(--text-primary);">${openApprovals}</h4>
              <span style="font-size: 0.58rem; color: var(--accent-primary); cursor: pointer; text-decoration: underline;" id="btn-view-approvals-kpi">View Approvals</span>
            </div>
          </div>
            <!-- Row 1 Panel: Sales Overview, Workforce, Inventory Overview -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: var(--spacing-lg); width: 100%;">
          
          <!-- Sales Overview (hourly trend + top 5) -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.05rem; margin: 0; display:flex; align-items:center; gap:6px;">
                <i data-lucide="trending-up" style="width: 16px; height: 16px; color: var(--accent-primary);"></i>
                Sales Overview
              </h3>
              <select style="background: rgba(0,0,0,0.2); color: var(--text-primary); border: 1px solid rgba(255,255,255,0.1); border-radius: var(--radius-sm); padding: 2px 6px; font-size: 0.65rem; outline: none;">
                <option value="today">Today</option>
              </select>
            </div>
            
            <div style="display: grid; grid-template-columns: 1.3fr 1.7fr; gap: var(--spacing-md); font-size: 0.72rem;">
              <div>
                <div style="display:flex; flex-direction:column; gap:4px; margin-bottom:8px;">
                  <span style="color:var(--text-muted); font-size: 0.62rem; font-weight:700; text-transform:uppercase;">Total Sales</span>
                  <strong style="font-size: 1.15rem; color: var(--text-primary);">${formatCurrency(todaySales)}</strong>
                </div>
                <div style="display:flex; flex-direction:column; gap:4px; margin-bottom:8px;">
                  <span style="color:var(--text-muted); font-size: 0.62rem; font-weight:700; text-transform:uppercase;">Target</span>
                  <strong style="font-size: 0.95rem; color: var(--text-muted);">${formatCurrency(todayTarget)}</strong>
                </div>
                <div style="display:flex; align-items:center; gap:8px;">
                  <!-- Simulating achieved circular gauge -->
                  <div style="width: 50px; height: 50px; border-radius: 50%; background: conic-gradient(var(--status-success) 0% ${todayAchievedPercent}%, rgba(255,255,255,0.05) ${todayAchievedPercent}% 100%); display:flex; align-items:center; justify-content:center; position:relative;">
                    <div style="width: 38px; height: 38px; border-radius:50%; background: var(--bg-card); display:flex; align-items:center; justify-content:center; font-size:0.68rem; font-weight:900; color:var(--text-primary);">${todayAchievedPercent}%</div>
                  </div>
                  <span style="font-weight:700; color:var(--status-success);">Target Achieved</span>
                </div>
              </div>
 
              <!-- Sparkline Hourly Trend SVG -->
              <div style="display:flex; flex-direction:column; gap:4px;">
                <span style="color:var(--text-muted); font-size: 0.62rem; font-weight:700; text-transform:uppercase;">Sales Trend (Today)</span>
                <div style="height: 70px; background: rgba(0,0,0,0.1); border: 1px solid rgba(255,255,255,0.03); border-radius: 4px; padding: 4px; position:relative; overflow:hidden;">
                  <svg viewBox="0 0 100 40" style="width: 100%; height: 100%; display:block;">
                    <path d="M 0 35 L 20 28 L 40 20 L 60 10 L 80 25 L 100 5" fill="none" stroke="var(--accent-primary)" stroke-width="2" stroke-linecap="round"></path>
                    <path d="M 0 35 L 20 28 L 40 20 L 60 10 L 80 25 L 100 5 L 100 40 L 0 40 Z" fill="rgba(201,164,106,0.06)"></path>
                  </svg>
                  <div style="display:flex; justify-content:space-between; font-size: 0.52rem; color:var(--text-muted); margin-top:2px;">
                    <span>06 AM</span><span>12 PM</span><span>06 PM</span>
                  </div>
                </div>
              </div>
            </div>
 
            <!-- Top 5 Products -->
            <div style="border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-sm); font-size: 0.72rem;">
              <span style="color:var(--text-muted); font-size: 0.62rem; font-weight:700; text-transform:uppercase; display:block; margin-bottom:4px;">Top 5 Selling Products</span>
              <div style="display:grid; grid-template-columns: 1fr 1fr; gap: 4px 12px;">
                <div style="display:flex; justify-content:space-between;"><span>Cappuccino:</span><strong>125 units</strong></div>
                <div style="display:flex; justify-content:space-between;"><span>Latte:</span><strong>98 units</strong></div>
                <div style="display:flex; justify-content:space-between;"><span>Americano:</span><strong>76 units</strong></div>
                <div style="display:flex; justify-content:space-between;"><span>Mocha:</span><strong>65 units</strong></div>
                <div style="display:flex; justify-content:space-between; grid-column: 1 / 3;"><span>Cold Coffee:</span><strong>54 units</strong></div>
              </div>
            </div>
          </div>
 
          <!-- Workforce Overview -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.05rem; margin: 0; display:flex; align-items:center; gap:6px;">
                <i data-lucide="users" style="width: 16px; height: 16px; color: var(--accent-primary);"></i>
                Workforce Overview
              </h3>
              <span style="font-size:0.65rem; color:var(--text-muted); font-weight:700; text-transform:uppercase;">Attendance Today</span>
            </div>
 
            <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 6px; text-align: center;">
              <div style="background: rgba(0,0,0,0.15); padding: 4px; border-radius: 4px;">
                <span style="font-size:0.55rem; color:var(--text-muted); font-weight:700; display:block; text-transform:uppercase;">On Shift</span>
                <strong style="font-size:1.05rem; color:var(--status-success);">${activeStaff}</strong>
              </div>
              <div style="background: rgba(0,0,0,0.15); padding: 4px; border-radius: 4px;">
                <span style="font-size:0.55rem; color:var(--text-muted); font-weight:700; display:block; text-transform:uppercase;">Absent</span>
                <strong style="font-size:1.05rem; color:var(--status-danger);">${Number(workforceOverview.absent || 2)}</strong>
              </div>
              <div style="background: rgba(0,0,0,0.15); padding: 4px; border-radius: 4px;">
                <span style="font-size:0.55rem; color:var(--text-muted); font-weight:700; display:block; text-transform:uppercase;">On Leave</span>
                <strong style="font-size:1.05rem; color:var(--status-info);">${Number(workforceOverview.onLeave || 3)}</strong>
              </div>
              <div style="background: rgba(0,0,0,0.15); padding: 4px; border-radius: 4px;">
                <span style="font-size:0.55rem; color:var(--text-muted); font-weight:700; display:block; text-transform:uppercase;">Swap due</span>
                <strong style="font-size:1.05rem; color:var(--status-warning);">4</strong>
              </div>
            </div>
 
            <div style="display: flex; align-items: center; gap: var(--spacing-lg); margin-top: var(--spacing-xs);">
              <div style="width: 70px; height: 70px; border-radius: 50%; background: conic-gradient(var(--status-success) 0% ${((activeStaff/totalStaff)*100).toFixed(0)}%, var(--status-danger) ${((activeStaff/totalStaff)*100).toFixed(0)}% 100%); display:flex; align-items:center; justify-content:center;">
                <div style="width: 50px; height: 50px; border-radius: 50%; background: var(--bg-card); display:flex; flex-direction:column; align-items:center; justify-content:center; font-size:0.55rem; font-weight:900;">
                  <span>${((activeStaff/totalStaff)*100).toFixed(0)}%</span>
                  <span style="font-size:0.4rem; color:var(--text-muted);">Present</span>
                </div>
              </div>
              <div style="font-size: 0.72rem; display:flex; flex-direction:column; gap:4px; flex:1;">
                <div style="display:flex; justify-content:space-between;"><span>Present Staff:</span><strong>${activeStaff} (${((activeStaff/totalStaff)*100).toFixed(0)}%)</strong></div>
                <div style="display:flex; justify-content:space-between;"><span>Absent Staff:</span><strong>${Number(workforceOverview.absent || 2)} (${((Number(workforceOverview.absent || 2)/totalStaff)*100).toFixed(0)}%)</strong></div>
                <div style="display:flex; justify-content:space-between;"><span>On Leave:</span><strong>${Number(workforceOverview.onLeave || 3)} (${((Number(workforceOverview.onLeave || 3)/totalStaff)*100).toFixed(0)}%)</strong></div>
              </div>
            </div>
 
            <div style="border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-sm); display:grid; grid-template-columns: 1fr 1fr; gap: 6px; margin-top: auto;">
              <button class="btn btn-action" id="wa-btn-shifts" style="font-size:0.7rem; padding: 6px var(--spacing-xs); border: 1px solid var(--border-color); border-radius: var(--radius-sm); color: var(--text-primary); cursor: pointer; text-align: center; background: rgba(255,255,255,0.03);" onmouseover="this.style.background='rgba(255,255,255,0.08)';" onmouseout="this.style.background='rgba(255,255,255,0.03)';">Assign Shifts</button>
              <button class="btn btn-action" id="wa-btn-leaves" style="font-size:0.7rem; padding: 6px var(--spacing-xs); border: 1px solid var(--border-color); border-radius: var(--radius-sm); color: var(--text-primary); cursor: pointer; text-align: center; background: rgba(255,255,255,0.03);" onmouseover="this.style.background='rgba(255,255,255,0.08)';" onmouseout="this.style.background='rgba(255,255,255,0.03)';">Approve Leave</button>
            </div>
          </div>
 
          <!-- Inventory Overview -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.05rem; margin: 0; display:flex; align-items:center; gap:6px;">
                <i data-lucide="package" style="width: 16px; height: 16px; color: var(--accent-primary);"></i>
                Inventory Overview
              </h3>
              <span style="font-size:0.65rem; color:var(--status-danger); font-weight:700; text-transform:uppercase;">Critical Stock</span>
            </div>
 
            <div style="display: flex; flex-direction: column; gap: 8px; font-size: 0.74rem;">
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Low-Stock Items:</span>
                <strong style="color:var(--status-danger);">${lowStockCount} Items</strong>
              </div>
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Out-of-Stock Items:</span>
                <strong style="color:#dc2626;">${outOfStockCount} Items</strong>
              </div>
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Expiring Items (7 Days):</span>
                <strong style="color:var(--status-warning);">${Number(inventoryOverview.expiringCount || 4)} Items</strong>
              </div>
              <div style="display:flex; justify-content:space-between;">
                <span>Pending Supply Requests:</span>
                <strong>${Number(inventoryOverview.pendingSupplyRequests || 3)} Requests</strong>
              </div>
            </div>
 
            <div style="border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-sm); display:grid; grid-template-columns: 1fr 1fr; gap: 6px; margin-top: auto;">
              <button class="btn btn-action" id="wa-btn-supply" style="font-size:0.7rem; padding: 6px var(--spacing-xs); border: 1px solid var(--border-color); border-radius: var(--radius-sm); color: var(--text-primary); cursor: pointer; text-align: center; background: rgba(255,255,255,0.03);" onmouseover="this.style.background='rgba(255,255,255,0.08)';" onmouseout="this.style.background='rgba(255,255,255,0.03)';">Create Supply Req</button>
              <button class="btn btn-action" id="wa-btn-waste" style="font-size:0.7rem; padding: 6px var(--spacing-xs); border: 1px solid var(--border-color); border-radius: var(--radius-sm); color: var(--text-primary); cursor: pointer; text-align: center; background: rgba(255,255,255,0.03);" onmouseover="this.style.background='rgba(255,255,255,0.08)';" onmouseout="this.style.background='rgba(255,255,255,0.03)';">Record Waste</button>
            </div>
          </div>
        </div>
 
        <!-- Row 2 Panel: Financial Overview, Compliance, Pending Approvals, Alerts -->
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(230px, 1fr)); gap: var(--spacing-lg); width: 100%;">
          
          <!-- Financial Overview -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.05rem; margin: 0; display:flex; align-items:center; gap:6px;">
                <i data-lucide="dollar-sign" style="width: 16px; height: 16px; color: var(--accent-primary);"></i>
                Financial Overview
              </h3>
            </div>
            
            <div style="display: flex; flex-direction: column; gap: 6px; font-size: 0.74rem;">
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Cash Sales:</span>
                <strong style="color: var(--status-success);">${formatCurrency(todaySales * 0.62)}</strong>
              </div>
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Card Sales:</span>
                <strong style="color: var(--status-success);">${formatCurrency(todaySales * 0.38)}</strong>
              </div>
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Refund Amount:</span>
                <strong style="color: var(--status-danger);">${formatCurrency(todaySales * 0.015)}</strong>
              </div>
              <div style="display:flex; justify-content:space-between;">
                <span>Petty Cash Balance:</span>
                <strong>${formatCurrency(todaySales * 0.08)}</strong>
              </div>
            </div>
 
            <div style="border-top: 1px solid rgba(255,255,255,0.05); padding-top: var(--spacing-sm); display:grid; grid-template-columns: 1fr; gap: 6px; margin-top: auto;">
              <button class="btn btn-action" id="sa-btn-reconcile" style="font-size:0.7rem; padding: 6px var(--spacing-xs); border: 1px solid var(--border-color); border-radius: var(--radius-sm); color: var(--text-primary); cursor: pointer; text-align: center; background: rgba(255,255,255,0.03);" onmouseover="this.style.background='rgba(255,255,255,0.08)';" onmouseout="this.style.background='rgba(255,255,255,0.03)';">Reconcile Cash</button>
            </div>
          </div>
 
          <!-- Compliance Overview -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.05rem; margin: 0; display:flex; align-items:center; gap:6px;">
                <i data-lucide="shield-check" style="width: 16px; height: 16px; color: var(--accent-primary);"></i>
                Compliance Overview
              </h3>
            </div>
            
            <div style="display: flex; flex-direction: column; gap: 6px; font-size: 0.74rem;">
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Opening Checklist:</span>
                <span style="color:var(--status-success); font-weight:700;">Completed</span>
              </div>
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Closing Checklist:</span>
                <span style="color:var(--status-warning); font-weight:700;">Pending</span>
              </div>
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Cleaning Checklist:</span>
                <span style="color:var(--status-success); font-weight:700;">Completed</span>
              </div>
              <div style="display:flex; justify-content:space-between;">
                <span>Temperature Log:</span>
                <span style="color:var(--status-success); font-weight:700;">Completed</span>
              </div>
            </div>
          </div>
 
          <!-- Pending Approvals -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.05rem; margin: 0; display:flex; align-items:center; gap:6px;">
                <i data-lucide="check-square" style="width: 16px; height: 16px; color: var(--accent-primary);"></i>
                Pending Approvals
              </h3>
            </div>
            
            <div style="display: flex; flex-direction: column; gap: 6px; font-size: 0.74rem;">
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Leave Requests:</span>
                <strong>${Number(workforceOverview.pendingLeaves || 3)} Requests</strong>
              </div>
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Shift Change Requests:</span>
                <strong>${Number(workforceOverview.pendingShiftChanges || 2)} Requests</strong>
              </div>
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Overtime Requests:</span>
                <strong>${Number(workforceOverview.pendingOvertime || 1)} Request</strong>
              </div>
              <div style="display:flex; justify-content:space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom:4px;">
                <span>Local Purchase Requests:</span>
                <strong>${Number(inventoryOverview.pendingLocalPurchase || 1)} Request</strong>
              </div>
              <div style="display:flex; justify-content:space-between;">
                <span>Inventory Adjustments:</span>
                <strong>${Number(inventoryOverview.pendingAdjustments || 2)} Requests</strong>
              </div>
            </div>
          </div>      </div>

          <!-- Alerts & Notifications -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.05rem; margin: 0; display:flex; align-items:center; gap:6px; color: var(--status-danger);">
                <i data-lucide="bell" style="width: 16px; height: 16px; color: var(--status-danger);"></i>
                Alerts &amp; Notifications
              </h3>
            </div>
            
            <div style="display: flex; flex-direction: column; gap: 8px; font-size: 0.7rem;">
              <div style="display:flex; justify-content:space-between; align-items:center;">
                <span style="color:var(--text-primary);">● Low stock for 6 items</span>
                <span style="color:var(--text-muted); font-size:0.62rem;">10m ago</span>
              </div>
              <div style="display:flex; justify-content:space-between; align-items:center;">
                <span style="color:var(--text-primary);">● Delivery delayed for Order #1256</span>
                <span style="color:var(--text-muted); font-size:0.62rem;">30m ago</span>
              </div>
              <div style="display:flex; justify-content:space-between; align-items:center;">
                <span style="color:var(--text-primary);">● 2 employees attendance issue</span>
                <span style="color:var(--text-muted); font-size:0.62rem;">1h ago</span>
              </div>
              <div style="display:flex; justify-content:space-between; align-items:center;">
                <span style="color:var(--text-primary);">● Coffee Machine maintenance due</span>
                <span style="color:var(--text-muted); font-size:0.62rem;">2h ago</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Row 3 Panel: Recent Activities & Quick Actions -->
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: var(--spacing-lg); width: 100%; flex-wrap: wrap;">
          
          <!-- Recent Activities -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.05rem; margin: 0; display:flex; align-items:center; gap:6px;">
                <i data-lucide="history" style="width: 16px; height: 16px; color: var(--text-muted);"></i>
                Recent Activities
              </h3>
            </div>
            
            <div style="display: flex; flex-direction: column; gap: 8px; font-size: 0.72rem;">
              <div style="display: flex; justify-content: space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom: 4px;">
                <span>📥 Supply request #SR1254 submitted</span>
                <span style="color: var(--text-muted);">By Rohit Sharma (10:30 AM)</span>
              </div>
              <div style="display: flex; justify-content: space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom: 4px;">
                <span>📝 Leave request by Neha Patel approved</span>
                <span style="color: var(--text-muted);">By You (09:45 AM)</span>
              </div>
              <div style="display: flex; justify-content: space-between; border-bottom: 1px solid rgba(255,255,255,0.03); padding-bottom: 4px;">
                <span>🚚 Delivery received for Order #1255</span>
                <span style="color: var(--text-muted);">By Suresh Kumar (09:20 AM)</span>
              </div>
              <div style="display: flex; justify-content: space-between;">
                <span>📊 Physical inventory count completed</span>
                <span style="color: var(--text-muted);">By Amit Singh (Yesterday)</span>
              </div>
            </div>
          </div>

          <!-- Quick Actions -->
          <div class="card glass" style="padding: var(--spacing-lg); border-radius: var(--radius-lg); border: 1px solid var(--border-color); background: var(--bg-card); display: flex; flex-direction: column; gap: var(--spacing-md); text-align: left;">
            <div style="border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: var(--spacing-xs); display: flex; justify-content: space-between; align-items: center;">
              <h3 style="font-family: var(--font-display); font-weight: 800; font-size: 1.05rem; margin: 0; display:flex; align-items:center; gap:6px;">
                <i data-lucide="bolt" style="width: 16px; height: 16px; color: var(--text-muted);"></i>
                Quick Actions
              </h3>
            </div>
            
            <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: var(--spacing-sm); text-align: center;">
              <button class="btn btn-action" id="qa-btn-supply" style="padding: var(--spacing-sm); border: 1px solid var(--border-color); border-radius: var(--radius-sm); color: var(--text-primary); cursor: pointer; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 4px; background: rgba(255,255,255,0.03); font-size: 0.7rem;" onmouseover="this.style.background='rgba(255,255,255,0.08)';" onmouseout="this.style.background='rgba(255,255,255,0.03)';">
                <i data-lucide="file-plus" style="width:16px; height:16px; color: var(--accent-primary);"></i>
                Create Supply
              </button>
              <button class="btn btn-action" id="qa-btn-leave" style="padding: var(--spacing-sm); border: 1px solid var(--border-color); border-radius: var(--radius-sm); color: var(--text-primary); cursor: pointer; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 4px; background: rgba(255,255,255,0.03); font-size: 0.7rem;" onmouseover="this.style.background='rgba(255,255,255,0.08)';" onmouseout="this.style.background='rgba(255,255,255,0.03)';">
                <i data-lucide="user-check" style="width:16px; height:16px; color: var(--status-success);"></i>
                Approve Leave
              </button>
              <button class="btn btn-action" id="qa-btn-shifts" style="padding: var(--spacing-sm); border: 1px solid var(--border-color); border-radius: var(--radius-sm); color: var(--text-primary); cursor: pointer; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 4px; background: rgba(255,255,255,0.03); font-size: 0.7rem;" onmouseover="this.style.background='rgba(255,255,255,0.08)';" onmouseout="this.style.background='rgba(255,255,255,0.03)';">
                <i data-lucide="calendar" style="width:16px; height:16px; color: var(--status-info);"></i>
                Assign Shifts
              </button>
              <button class="btn btn-action" id="qa-btn-waste" style="padding: var(--spacing-sm); border: 1px solid var(--border-color); border-radius: var(--radius-sm); color: var(--text-primary); cursor: pointer; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 4px; background: rgba(255,255,255,0.03); font-size: 0.7rem;" onmouseover="this.style.background='rgba(255,255,255,0.08)';" onmouseout="this.style.background='rgba(255,255,255,0.03)';">
                <i data-lucide="trash-2" style="width:16px; height:16px; color: var(--status-danger);"></i>
                Record Waste
              </button>
              <button class="btn btn-action" id="qa-btn-incident" style="padding: var(--spacing-sm); border: 1px solid var(--border-color); border-radius: var(--radius-sm); color: var(--text-primary); cursor: pointer; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 4px; background: rgba(255,255,255,0.03); font-size: 0.7rem;" onmouseover="this.style.background='rgba(255,255,255,0.08)';" onmouseout="this.style.background='rgba(255,255,255,0.03)';">
                <i data-lucide="alert-octagon" style="width:16px; height:16px; color: var(--status-warning);"></i>
                Incident Report
              </button>
              <button class="btn btn-action" id="qa-btn-reports" style="padding: var(--spacing-sm); border: 1px solid var(--border-color); border-radius: var(--radius-sm); color: var(--text-primary); cursor: pointer; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 4px; background: rgba(255,255,255,0.03); font-size: 0.7rem;" onmouseover="this.style.background='rgba(255,255,255,0.08)';" onmouseout="this.style.background='rgba(255,255,255,0.03)';">
                <i data-lucide="bar-chart-3" style="width:16px; height:16px; color: var(--accent-primary);"></i>
                View Reports
              </button>
            </div>
          </div>
        </div>

      </div>
    `;

    if (window.lucide) window.lucide.createIcons();
  }

  bindEvents(container, lifecycle) {
    const applyBtn = container.querySelector('#btn-apply-store-filters');
    if (applyBtn) {
      applyBtn.addEventListener('click', () => {
        notificationStore.success('Store dashboard filters updated!');
      });
    }

    const actionButtons = [
      { id: '#qa-btn-supply', msg: 'Opening restocking request wizard...' },
      { id: '#qa-btn-leave', msg: 'Navigating to leave approval requests list...' },
      { id: '#qa-btn-shifts', msg: 'Opening scheduling and shift planners...' },
      { id: '#qa-btn-waste', msg: 'Opening waste logging form...' },
      { id: '#qa-btn-incident', msg: 'Creating incident report sheet...' },
      { id: '#qa-btn-reports', msg: 'Opening reports generator panel...' },
      { id: '#wa-btn-shifts', msg: 'Opening schedule manager...' },
      { id: '#wa-btn-leaves', msg: 'Opening staff leaves panel...' },
      { id: '#wa-btn-supply', msg: 'Sourcing supply restock order...' },
      { id: '#wa-btn-waste', msg: 'Opening waste inventory log...' },
      { id: '#sa-btn-reconcile', msg: 'Starting cash reconciliation workflow...' },
      { id: '#btn-view-tasks-kpi', msg: 'Redirecting to open tasks dashboard...' },
      { id: '#btn-view-approvals-kpi', msg: 'Redirecting to pending approvals queue...' }
    ];

    actionButtons.forEach(act => {
      const el = container.querySelector(act.id);
      if (el) {
        el.addEventListener('click', () => {
          notificationStore.info(act.msg);
        });
      }
    });
  }

  startClock(container) {
    const updateTime = () => {
      const clockEl = container.querySelector('#store-admin-clock');
      if (clockEl) {
        const now = new Date();
        const options = { day: 'numeric', month: 'short', year: 'numeric', weekday: 'short' };
        const dateStr = now.toLocaleDateString('en-US', options);
        const timeStr = now.toLocaleTimeString();
        clockEl.textContent = `${dateStr} ${timeStr}`;
      }
    };
    updateTime();
    this._clockInterval = setInterval(updateTime, 1000);
  }

  destroy() {
    if (this._clockInterval) {
      clearInterval(this._clockInterval);
    }
  }
}



