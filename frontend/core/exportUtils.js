/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * File              : exportUtils.js
 * Path              : frontend/core/exportUtils.js
 * Purpose           : Task Status Report exporter using the exact Payslip logo, layout, styling & print method with Date Range Selection
 ******************************************************************************/

import { authStore } from '../store/authStore.js';

function getUserInfo() {
  const user = authStore.getUser() || {};
  const userName = user.name || user.username || user.email || 'Supervisor1 ST_FR_REG_1_01';
  const userId = user.employeeCode || user.id || 'ST_FR_REG_1_01';
  const userRole = user.designation || user.role || 'Shift Lead';
  return { userName, userId, userRole };
}

/**
 * Opens Date Range selection modal (Today, This Week, Last 30 Days, Custom) before triggering PDF/Print or Image export.
 */
export function openExportModal(tasks = [], exportType = 'PDF', defaultTitle = 'Store Task Status Report', filename = 'store-task-status-report.png') {
  const existing = document.getElementById('modal-export-date-range');
  if (existing) existing.remove();

  const now = new Date();
  const formatDateISO = (d) => d.toISOString().slice(0, 10);
  const todayStr = formatDateISO(now);
  const monthAgo = new Date(now.getTime() - (30 * 86400000));
  const monthAgoStr = formatDateISO(monthAgo);

  const modalHtml = `
    <div id="modal-export-date-range" style="position: fixed; inset: 0; background: rgba(0,0,0,0.75); backdrop-filter: blur(8px); z-index: 10000; display: flex; align-items: center; justify-content: center; padding: 20px; font-family: 'Inter', Arial, sans-serif;">
      <div style="background: #18181b; color: #fff; border: 1px solid rgba(201,164,106,0.4); border-radius: 16px; padding: 28px; width: 100%; max-width: 480px; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.8);">
        
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 12px;">
          <h3 style="font-size: 1.15rem; font-weight: 800; color: #c9a46a; margin: 0; display: flex; align-items: center; gap: 8px;">
            <span>📊 Select Report Date Range</span>
          </h3>
          <button type="button" id="btn-close-export-range-modal" style="background: none; border: none; color: #a1a1aa; font-size: 1.2rem; cursor: pointer;">✕</button>
        </div>

        <div style="display: flex; flex-direction: column; gap: 14px; margin-bottom: 24px;">
          <label style="font-size: 0.82rem; font-weight: 600; color: #a1a1aa;">Choose Audit Period:</label>

          <label style="display: flex; align-items: center; gap: 10px; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.1); padding: 12px 14px; border-radius: 10px; cursor: pointer;">
            <input type="radio" name="export-date-range" value="TODAY" checked />
            <div>
              <div style="font-size: 0.9rem; font-weight: 700; color: #fff;">📅 Today</div>
              <div style="font-size: 0.75rem; color: #a1a1aa;">Tasks assigned or due today</div>
            </div>
          </label>

          <label style="display: flex; align-items: center; gap: 10px; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.1); padding: 12px 14px; border-radius: 10px; cursor: pointer;">
            <input type="radio" name="export-date-range" value="THIS_WEEK" />
            <div>
              <div style="font-size: 0.9rem; font-weight: 700; color: #fff;">📅 This Week</div>
              <div style="font-size: 0.75rem; color: #a1a1aa;">Tasks within current calendar week</div>
            </div>
          </label>

          <label style="display: flex; align-items: center; gap: 10px; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.1); padding: 12px 14px; border-radius: 10px; cursor: pointer;">
            <input type="radio" name="export-date-range" value="LAST_30" />
            <div>
              <div style="font-size: 0.9rem; font-weight: 700; color: #fff;">📅 Last 30 Days</div>
              <div style="font-size: 0.75rem; color: #a1a1aa;">Tasks from the last 30 days</div>
            </div>
          </label>

          <label style="display: flex; align-items: center; gap: 10px; background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.1); padding: 12px 14px; border-radius: 10px; cursor: pointer;">
            <input type="radio" name="export-date-range" value="CUSTOM" id="radio-custom" />
            <div>
              <div style="font-size: 0.9rem; font-weight: 700; color: #fff;">📅 Custom Date Range</div>
              <div style="font-size: 0.75rem; color: #a1a1aa;">Specify custom start &amp; end dates</div>
            </div>
          </label>

          <div id="export-custom-dates-box" style="display: none; background: rgba(0,0,0,0.3); border: 1px solid rgba(201,164,106,0.3); border-radius: 10px; padding: 14px; margin-top: 4px;">
            <div style="display: flex; gap: 12px;">
              <div style="flex: 1;">
                <label style="font-size: 0.75rem; color: #a1a1aa; display: block; margin-bottom: 4px;">From Date</label>
                <input type="date" id="export-date-from" value="${monthAgoStr}" style="width: 100%; background: #000; border: 1px solid rgba(255,255,255,0.2); color: #fff; padding: 8px; border-radius: 6px; font-size: 0.82rem;" />
              </div>
              <div style="flex: 1;">
                <label style="font-size: 0.75rem; color: #a1a1aa; display: block; margin-bottom: 4px;">To Date</label>
                <input type="date" id="export-date-to" value="${todayStr}" style="width: 100%; background: #000; border: 1px solid rgba(255,255,255,0.2); color: #fff; padding: 8px; border-radius: 6px; font-size: 0.82rem;" />
              </div>
            </div>
          </div>
        </div>

        <div style="display: flex; justify-content: flex-end; gap: 12px; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 16px;">
          <button type="button" id="btn-cancel-export-range" style="background: rgba(255,255,255,0.08); color: #fff; border: 1px solid rgba(255,255,255,0.15); padding: 9px 18px; border-radius: 8px; font-weight: 600; cursor: pointer;">Cancel</button>
          <button type="button" id="btn-confirm-export-range" style="background: linear-gradient(135deg, #c9a46a, #8c6a38); color: #000; border: none; padding: 9px 20px; border-radius: 8px; font-weight: 800; cursor: pointer;">
            ${exportType === 'PDF' ? '📄 Generate &amp; Print PDF' : '🖼️ Generate &amp; Download Image'}
          </button>
        </div>

      </div>
    </div>
  `;

  document.body.insertAdjacentHTML('beforeend', modalHtml);

  const modalEl = document.getElementById('modal-export-date-range');
  const closeBtn = document.getElementById('btn-close-export-range-modal');
  const cancelBtn = document.getElementById('btn-cancel-export-range');
  const confirmBtn = document.getElementById('btn-confirm-export-range');
  const customBox = document.getElementById('export-custom-dates-box');

  document.querySelectorAll('input[name="export-date-range"]').forEach(r => {
    r.addEventListener('change', (e) => {
      if (e.target.value === 'CUSTOM') {
        customBox.style.display = 'block';
      } else {
        customBox.style.display = 'none';
      }
    });
  });

  const closeModal = () => modalEl.remove();
  closeBtn.addEventListener('click', closeModal);
  cancelBtn.addEventListener('click', closeModal);

  confirmBtn.addEventListener('click', () => {
    const selectedRange = document.querySelector('input[name="export-date-range"]:checked').value;
    let filteredTasks = tasks;
    let periodLabel = 'Today';

    const nowMs = Date.now();
    const startOfToday = new Date(now.getFullYear(), now.getMonth(), now.getDate()).getTime();
    const endOfToday = startOfToday + 86400000;

    if (selectedRange === 'TODAY') {
      periodLabel = `Today (${now.toLocaleDateString()})`;
      filteredTasks = tasks.filter(t => {
        const time = new Date(t.createdAt || t.dueDate || Date.now()).getTime();
        return time >= startOfToday && time < endOfToday;
      });
    } else if (selectedRange === 'THIS_WEEK') {
      periodLabel = 'This Week';
      const startOfWeek = new Date(now.setDate(now.getDate() - now.getDay())).getTime();
      filteredTasks = tasks.filter(t => {
        const time = new Date(t.createdAt || t.dueDate || Date.now()).getTime();
        return time >= startOfWeek;
      });
    } else if (selectedRange === 'LAST_30') {
      periodLabel = 'Last 30 Days';
      const thirtyDaysAgo = nowMs - (30 * 86400000);
      filteredTasks = tasks.filter(t => {
        const time = new Date(t.createdAt || t.dueDate || Date.now()).getTime();
        return time >= thirtyDaysAgo;
      });
    } else if (selectedRange === 'CUSTOM') {
      const fromVal = document.getElementById('export-date-from').value;
      const toVal = document.getElementById('export-date-to').value;
      periodLabel = `Custom (${fromVal} to ${toVal})`;

      const fromMs = fromVal ? new Date(fromVal).getTime() : 0;
      const toMs = toVal ? new Date(toVal).getTime() + 86400000 : Infinity;

      filteredTasks = tasks.filter(t => {
        const time = new Date(t.createdAt || t.dueDate || Date.now()).getTime();
        return time >= fromMs && time <= toMs;
      });
    }

    closeModal();

    const reportTitle = `${defaultTitle} (${periodLabel})`;

    if (exportType === 'PDF') {
      exportPdfOrPrint(filteredTasks, reportTitle);
    } else {
      exportTasksAsImage(filteredTasks, reportTitle, filename);
    }
  });
}

/**
 * Generates a PNG Canvas Image using the exact Payslip styling, colors (#1e3a5f), logo, and 2-column info grid.
 */
export function exportTasksAsImage(tasks = [], title = 'Store Task Status Report', filename = 'store-task-status-report.png') {
  const { userName, userId, userRole } = getUserInfo();
  const logoUrl = `${window.location.origin}/imgs/logo-gold.png`;

  const canvas = document.createElement('canvas');
  const ctx = canvas.getContext('2d');

  const width = 1000;
  const padding = 35;
  const headerHeight = 220;
  const tableHeaderHeight = 40;
  const rowHeight = 60;

  const totalRows = tasks.length === 0 ? 1 : tasks.length;
  const tableHeight = tableHeaderHeight + (totalRows * rowHeight);
  const footerHeight = 80;

  const totalHeight = padding + headerHeight + tableHeight + footerHeight + padding;

  canvas.width = width;
  canvas.height = totalHeight;

  // Background
  ctx.fillStyle = '#ffffff';
  ctx.fillRect(0, 0, width, totalHeight);

  // Outer Border Box
  ctx.strokeStyle = '#d1d5db';
  ctx.lineWidth = 1;
  ctx.strokeRect(padding, padding, width - (padding * 2), totalHeight - (padding * 2));

  let currentY = padding + 20;
  const contentX = padding + 20;
  const contentW = width - (padding * 2) - 40;

  // Info Bar Top
  ctx.fillStyle = '#374151';
  ctx.font = '11px sans-serif';
  ctx.fillText('PLUS33 Coffee Enterprise ERP • Official Store Operations & Task Governance Audit Report', contentX, currentY);
  currentY += 20;

  // Top Header Line Divider
  ctx.strokeStyle = '#d1d5db';
  ctx.lineWidth = 1;
  ctx.beginPath();
  ctx.moveTo(contentX, currentY);
  ctx.lineTo(contentX + contentW, currentY);
  ctx.stroke();

  currentY += 15;

  // Header Title & Company Box (Navy #1e3a5f)
  ctx.fillStyle = '#1e3a5f';
  ctx.font = 'bold 20px sans-serif';
  ctx.fillText('PLUS33 Coffee Company', contentX, currentY + 20);

  ctx.fillStyle = '#6b7280';
  ctx.font = '12px sans-serif';
  ctx.fillText('Enterprise Store Operations & Task Governance', contentX, currentY + 38);

  ctx.fillStyle = '#1e3a5f';
  ctx.font = 'bold 20px sans-serif';
  ctx.fillText(title.substring(0, 36), contentX + contentW - 360, currentY + 24);

  const dateStr = new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
  ctx.fillStyle = '#6b7280';
  ctx.font = '12px sans-serif';
  ctx.fillText(`Date: ${dateStr}`, contentX + contentW - 360, currentY + 44);

  currentY += 65;

  // 2-Column Info Grid Box (.ps-info-grid)
  const gridX = contentX;
  const gridW = contentW;
  const gridH = 80;

  ctx.strokeStyle = '#374151';
  ctx.lineWidth = 1.5;
  ctx.strokeRect(gridX, currentY, gridW, gridH);

  // Vertical split line
  ctx.beginPath();
  ctx.moveTo(gridX + (gridW / 2), currentY);
  ctx.lineTo(gridX + (gridW / 2), currentY + gridH);
  ctx.stroke();

  // Left Column
  ctx.fillStyle = '#111827';
  ctx.font = 'bold 12px sans-serif';
  ctx.fillText('Report Manager: ', gridX + 14, currentY + 24);
  ctx.font = '12px sans-serif';
  ctx.fillText(userName, gridX + 130, currentY + 24);

  ctx.font = 'bold 12px sans-serif';
  ctx.fillText('User ID / Code: ', gridX + 14, currentY + 46);
  ctx.font = '12px sans-serif';
  ctx.fillText(userId, gridX + 130, currentY + 46);

  ctx.font = 'bold 12px sans-serif';
  ctx.fillText('Designation: ', gridX + 14, currentY + 66);
  ctx.font = '12px sans-serif';
  ctx.fillText(userRole, gridX + 130, currentY + 66);

  // Right Column
  const completedCount = tasks.filter(t => t.status === 'COMPLETED').length;
  const pendingCount = tasks.filter(t => t.status !== 'COMPLETED').length;
  const rightX = gridX + (gridW / 2) + 14;

  ctx.fillStyle = '#111827';
  ctx.font = 'bold 12px sans-serif';
  ctx.fillText('Execution Status: ', rightX, currentY + 24);
  ctx.fillStyle = pendingCount === 0 ? '#166534' : '#2563eb';
  ctx.fillText(pendingCount === 0 ? 'COMPLETED' : 'IN_PROGRESS', rightX + 130, currentY + 24);

  ctx.fillStyle = '#111827';
  ctx.font = 'bold 12px sans-serif';
  ctx.fillText('Completed Directives: ', rightX, currentY + 46);
  ctx.font = '12px sans-serif';
  ctx.fillText(`${completedCount} of ${tasks.length}`, rightX + 140, currentY + 46);

  ctx.font = 'bold 12px sans-serif';
  ctx.fillText('Generated Date: ', rightX, currentY + 66);
  ctx.font = '12px sans-serif';
  ctx.fillText(dateStr, rightX + 140, currentY + 66);

  currentY += gridH + 20;

  // Table Column Headers
  ctx.fillStyle = '#e8eef5';
  ctx.fillRect(gridX, currentY, gridW, tableHeaderHeight);
  ctx.strokeStyle = '#94a3b8';
  ctx.lineWidth = 1;
  ctx.strokeRect(gridX, currentY, gridW, tableHeaderHeight);

  ctx.fillStyle = '#1e3a5f';
  ctx.font = 'bold 12px sans-serif';
  ctx.fillText('DIRECTIVE OVERVIEW', gridX + 14, currentY + 24);
  ctx.fillText('ASSIGNEE / OWNER', gridX + 420, currentY + 24);
  ctx.fillText('STATUS & NOTES', gridX + 680, currentY + 24);

  currentY += tableHeaderHeight;

  // Table Data Rows
  if (tasks.length === 0) {
    ctx.fillStyle = '#ffffff';
    ctx.fillRect(gridX, currentY, gridW, rowHeight);
    ctx.strokeStyle = '#e5e7eb';
    ctx.strokeRect(gridX, currentY, gridW, rowHeight);

    ctx.fillStyle = '#6b7280';
    ctx.font = '13px sans-serif';
    ctx.fillText('No task directives found for the selected date range.', gridX + 14, currentY + 35);
    currentY += rowHeight;
  } else {
    tasks.forEach((t, i) => {
      ctx.fillStyle = i % 2 === 0 ? '#ffffff' : '#f9fafb';
      ctx.fillRect(gridX, currentY, gridW, rowHeight);
      ctx.strokeStyle = '#e5e7eb';
      ctx.strokeRect(gridX, currentY, gridW, rowHeight);

      const prio = (t.priority || 'COMMON').toUpperCase();
      ctx.fillStyle = prio === 'HIGH' || prio === 'CRITICAL' || prio === 'IMMEDIATE' ? '#dc2626' : '#c9a46a';
      ctx.font = 'bold 11px sans-serif';
      ctx.fillText(`[${prio}]`, gridX + 14, currentY + 22);

      ctx.fillStyle = '#1a1a1a';
      ctx.font = 'bold 13px sans-serif';
      ctx.fillText((t.title || 'Untitled Task').substring(0, 42), gridX + 14, currentY + 42);

      ctx.fillStyle = '#374151';
      ctx.font = '12px sans-serif';
      ctx.fillText((t.assignedEmployeeName || 'Shift Team').substring(0, 26), gridX + 420, currentY + 34);

      const st = t.status || 'PENDING';
      const stColor = st === 'COMPLETED' ? '#166534' : st === 'STARTED' ? '#2563eb' : st === 'SUBMITTED_FOR_REVIEW' ? '#9333ea' : '#d97706';
      ctx.fillStyle = stColor;
      ctx.font = 'bold 12px sans-serif';
      ctx.fillText(`● ${st}`, gridX + 680, currentY + 22);

      ctx.fillStyle = '#6b7280';
      ctx.font = '11px sans-serif';
      const noteStr = t.rejectReason ? `Rejected: ${t.rejectReason}` : (t.blockReason ? `Blocked: ${t.blockReason}` : `Due: ${t.dueDate ? String(t.dueDate).replace('T',' ').substring(0,16) : 'Shift End'}`);
      ctx.fillText(noteStr.substring(0, 32), gridX + 680, currentY + 42);

      currentY += rowHeight;
    });
  }

  currentY += 15;

  // Footer Bar
  ctx.fillStyle = '#1e3a5f';
  ctx.fillRect(gridX, currentY, gridW, 36);

  ctx.fillStyle = '#ffffff';
  ctx.font = 'bold 11px sans-serif';
  ctx.fillText('PLUS33 Coffee Enterprise ERP • Official Store Operations Statement', gridX + 14, currentY + 22);
  ctx.fillText(`Verified by ${userName} (${userId})`, gridX + gridW - 240, currentY + 22);

  // Trigger Download PNG
  const dataUrl = canvas.toDataURL('image/png');
  const link = document.createElement('a');
  link.href = dataUrl;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

/**
 * Opens an Executive Print / PDF preview matching the EXACT Payslip HTML layout, logo, typography & print method.
 */
export function exportPdfOrPrint(tasks = [], title = 'Store Task Status Report') {
  const { userName, userId, userRole } = getUserInfo();
  const logoUrl = `${window.location.origin}/imgs/logo-gold.png`;

  const existing = document.getElementById('payslip-style-print-modal');
  if (existing) existing.remove();

  const completedCount = tasks.filter(t => t.status === 'COMPLETED').length;
  const pendingCount = tasks.filter(t => t.status !== 'COMPLETED').length;
  const dateStr = new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });

  const rowsHtml = tasks.length === 0 ? `
    <tr>
      <td colspan="4" style="padding: 16px; color: #6b7280; font-size: 12px; text-align: center;">No active task directives recorded for the selected date range.</td>
    </tr>
  ` : tasks.map((t, idx) => {
    const prio = (t.priority || 'COMMON').toUpperCase();
    const prioColor = prio === 'HIGH' || prio === 'CRITICAL' || prio === 'IMMEDIATE' ? '#dc2626' : '#c9a46a';
    const st = t.status || 'PENDING';
    const stColor = st === 'COMPLETED' ? '#166534' : st === 'STARTED' ? '#2563eb' : st === 'SUBMITTED_FOR_REVIEW' ? '#9333ea' : '#d97706';
    const noteStr = t.rejectReason ? `❌ Rejection: ${t.rejectReason}` : (t.blockReason ? `🚫 Blocked: ${t.blockReason}` : `Due: ${t.dueDate ? String(t.dueDate).replace('T',' ').substring(0,16) : 'Shift End'}`);
    const rowBg = idx % 2 === 0 ? '#ffffff' : '#f9fafb';

    return `
      <tr style="background: ${rowBg}; border-bottom: 1px solid #e5e7eb;">
        <td style="padding: 8px 10px;">
          <div style="font-size: 10px; font-weight: 700; color: ${prioColor};">[${prio}]</div>
          <div style="font-size: 12px; font-weight: 700; color: #1a1a1a; margin-top: 2px;">${t.title}</div>
        </td>
        <td style="padding: 8px 10px; color: #374151; font-size: 12px;">
          ${t.assignedEmployeeName || 'Shift Team'}
        </td>
        <td style="padding: 8px 10px; text-align: right; font-size: 11px; font-weight: 700; color: ${prioColor};">
          ${prio}
        </td>
        <td style="padding: 8px 10px; text-align: right;">
          <div style="font-size: 11px; font-weight: 700; color: ${stColor};">● ${st}</div>
          <div style="font-size: 10px; color: #6b7280; margin-top: 2px;">${noteStr}</div>
        </td>
      </tr>
    `;
  }).join('');

  const modalHtml = `
    <div id="payslip-style-print-modal" style="position: fixed; inset: 0; background: rgba(0,0,0,0.75); backdrop-filter: blur(8px); z-index: 9999; display: flex; align-items: center; justify-content: center; padding: 20px; overflow-y: auto;">
      <div id="payslip-print-card" style="background: #ffffff; color: #1a1a1a; width: 100%; max-width: 880px; border-radius: 12px; padding: 32px; box-shadow: 0 25px 50px -12px rgba(0,0,0,0.3); font-family: 'Inter', Arial, sans-serif; position: relative;">
        
        <!-- Action Toolbar (Hidden during print) -->
        <div class="no-print" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #d1d5db; padding-bottom: 14px;">
          <span style="font-size: 13px; font-weight: 700; color: #1e3a5f;">📄 Task Status Report (${title})</span>
          <div style="display: flex; gap: 10px;">
            <button type="button" onclick="window.print()" style="background: #1e3a5f; color: #fff; border: none; padding: 8px 18px; border-radius: 6px; font-weight: 700; font-size: 12px; cursor: pointer; display: flex; align-items: center; gap: 6px;">
              <span>🖨️ Print / Save PDF</span>
            </button>
            <button type="button" onclick="document.getElementById('payslip-style-print-modal').remove()" style="background: #f3f4f6; color: #374151; border: 1px solid #d1d5db; padding: 8px 16px; border-radius: 6px; font-weight: 600; font-size: 12px; cursor: pointer;">
              Close
            </button>
          </div>
        </div>

        <!-- Info Bar -->
        <div style="font-size: 10.5px; color: #374151; margin-bottom: 14px; line-height: 1.8;">
          <strong>Store Operations Helpdesk</strong> – For task &amp; shift queries, contact Store Manager or Regional HR.<br>
          <strong>PLUS33 Coffee Enterprise ERP</strong> – This is an official system-generated task status audit statement.
        </div>

        <!-- Header -->
        <div style="display: flex; align-items: flex-start; gap: 16px; margin-bottom: 20px; border-bottom: 1px solid #d1d5db; padding-bottom: 14px;">
          <img src="${logoUrl}" alt="PLUS33 Logo" style="width: 64px; height: 64px; object-fit: contain;" onerror="this.style.display='none'">
          <div style="flex: 1;">
            <div style="font-size: 15px; font-weight: 800; color: #1a1a1a;">PLUS33 Coffee Company</div>
            <div style="font-size: 11px; color: #6b7280; margin-top: 3px; line-height: 1.6;">
              Enterprise Store Operations &amp; Task Governance<br>
              Store Code: <span style="font-weight: 700; color: #1e3a5f;">${userId}</span>
            </div>
          </div>
          <div style="font-size: 15px; font-weight: 800; text-align: right; color: #1e3a5f;">
            ${title}<br>
            <span style="font-size: 11px; font-weight: 500; color: #6b7280;">Date: ${dateStr}</span>
          </div>
        </div>

        <!-- Employee / Manager Info Grid -->
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 0; border: 1.5px solid #374151; margin-bottom: 18px;">
          <div style="padding: 10px 14px; font-size: 12px; line-height: 1.9; border-right: 1px solid #374151;">
            <div><span style="font-weight: 700;">Report Manager: </span><span>${userName}</span></div>
            <div><span style="font-weight: 700;">User Code / ID: </span><span style="font-weight: 600; color: #1e3a5f;">${userId}</span></div>
            <div><span style="font-weight: 700;">Designation: </span><span>${userRole}</span></div>
          </div>
          <div style="padding: 10px 14px; font-size: 12px; line-height: 1.9;">
            <div><span style="font-weight: 700;">Execution Status: </span><span style="font-weight: 700; color: ${pendingCount === 0 ? '#166534' : '#2563eb'};">${pendingCount === 0 ? 'COMPLETED' : 'IN_PROGRESS'}</span></div>
            <div><span style="font-weight: 700;">Completed Directives: </span><span>${completedCount} of ${tasks.length}</span></div>
            <div><span style="font-weight: 700;">Statement Date: </span><span>${dateStr}</span></div>
          </div>
        </div>

        <!-- Table -->
        <table style="width: 100%; border-collapse: collapse; margin-bottom: 14px;">
          <thead>
            <tr style="background: #e8eef5; color: #1e3a5f; font-weight: 700; font-size: 11px; border-bottom: 1px solid #94a3b8;">
              <th style="padding: 8px 10px; text-align: left;">DIRECTIVE OVERVIEW</th>
              <th style="padding: 8px 10px; text-align: left;">ASSIGNEE / OWNER</th>
              <th style="padding: 8px 10px; text-align: right;">PRIORITY</th>
              <th style="padding: 8px 10px; text-align: right;">STATUS &amp; NOTES</th>
            </tr>
          </thead>
          <tbody>
            ${rowsHtml}
          </tbody>
        </table>

        <!-- Summary Net Section Box -->
        <div style="border: 2px solid #1e3a5f; margin-bottom: 14px;">
          <div style="font-size: 12px; font-weight: 800; background: #1e3a5f; color: #fff; padding: 6px 10px;">
            OPERATIONAL AUDIT SUMMARY
          </div>
          <div style="padding: 10px 14px; font-size: 12px; color: #374151; display: flex; justify-content: space-between; align-items: center;">
            <span>Total Directives: <strong>${tasks.length}</strong> &bull; Completed: <strong style="color: #166534;">${completedCount}</strong> &bull; Pending/Ongoing: <strong style="color: #2563eb;">${pendingCount}</strong></span>
            <span style="font-size: 11px; font-weight: 700; color: #1e3a5f;">VERIFIED VIA ERP</span>
          </div>
        </div>

        <!-- Footer Bar -->
        <div style="background: #1e3a5f; color: #fff; font-weight: 700; font-size: 12px; padding: 8px 14px; margin-top: 14px; display: flex; justify-content: space-between;">
          <span>PLUS33 Coffee Enterprise ERP &bull; Official Store Operations Statement</span>
          <span>Verified by ${userName} (${userId})</span>
        </div>

      </div>
    </div>

    <style>
      @media print {
        .no-print, .tasks-kpi-grid, .tasks-kpi-card, .tasks-card, .my-tasks-header, .supervisor-tasks-header, .store-tasks-header, .tasks-tabs, .tasks-toolbar, header, nav, aside, footer {
          display: none !important;
        }
        body > *:not(#payslip-style-print-modal) {
          display: none !important;
        }
        #payslip-style-print-modal {
          position: absolute !important;
          inset: 0 !important;
          background: #ffffff !important;
          padding: 0 !important;
          display: block !important;
        }
        #payslip-print-card {
          box-shadow: none !important;
          max-width: 100% !important;
          width: 100% !important;
          padding: 0 !important;
          margin: 0 !important;
          border-radius: 0 !important;
        }
      }
    </style>
  `;

  document.body.insertAdjacentHTML('beforeend', modalHtml);
}
