/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * File              : exportUtils.js
 * Path              : frontend/modules/core/exportUtils.js
 * Purpose           : Utility functions for exporting task workspaces to PDF/Print and HTML5 Canvas PNG images
 ******************************************************************************/

export function exportPdfOrPrint() {
  window.print();
}

export function exportTasksAsImage(tasks = [], title = 'Store Tasks Report', filename = 'plus33-tasks-report.png') {
  const canvas = document.createElement('canvas');
  const ctx = canvas.getContext('2d');

  const width = 960;
  const itemHeight = 85;
  const headerHeight = 150;
  const padding = 20;
  const totalHeight = headerHeight + (tasks.length === 0 ? 90 : tasks.length * itemHeight) + padding * 2;

  canvas.width = width;
  canvas.height = totalHeight;

  // Background
  ctx.fillStyle = '#121212';
  ctx.fillRect(0, 0, width, totalHeight);

  // Header Banner Box
  ctx.fillStyle = 'rgba(201, 164, 106, 0.12)';
  ctx.fillRect(padding, padding, width - (padding * 2), 110);
  ctx.strokeStyle = 'rgba(201, 164, 106, 0.35)';
  ctx.lineWidth = 1.5;
  ctx.strokeRect(padding, padding, width - (padding * 2), 110);

  // Title Branding
  ctx.fillStyle = '#c9a46a';
  ctx.font = 'bold 22px sans-serif';
  ctx.fillText('PLUS33 COFFEE ERP', padding + 20, padding + 35);

  ctx.fillStyle = '#ffffff';
  ctx.font = 'bold 17px sans-serif';
  ctx.fillText(title, padding + 20, padding + 65);

  const dateStr = new Date().toLocaleString();
  ctx.fillStyle = '#a1a1aa';
  ctx.font = '12px sans-serif';
  ctx.fillText(`Generated: ${dateStr} | Total Records: ${tasks.length}`, padding + 20, padding + 92);

  // Items List
  let y = headerHeight + padding;

  if (tasks.length === 0) {
    ctx.fillStyle = '#71717a';
    ctx.font = '15px sans-serif';
    ctx.fillText('No task records found.', padding + 20, y + 30);
  } else {
    tasks.forEach((t, index) => {
      // Card Container
      ctx.fillStyle = 'rgba(28, 28, 28, 0.95)';
      ctx.fillRect(padding, y, width - (padding * 2), 72);
      ctx.strokeStyle = 'rgba(255, 255, 255, 0.08)';
      ctx.lineWidth = 1;
      ctx.strokeRect(padding, y, width - (padding * 2), 72);

      // Index Number
      ctx.fillStyle = '#71717a';
      ctx.font = 'bold 12px sans-serif';
      ctx.fillText(`#${index + 1}`, padding + 15, y + 25);

      // Priority Badge
      const prio = (t.priority || 'COMMON').toUpperCase();
      ctx.fillStyle = prio === 'HIGH' || prio === 'CRITICAL' || prio === 'IMMEDIATE' ? '#f87171' : '#c9a46a';
      ctx.font = 'bold 12px sans-serif';
      ctx.fillText(`[${prio}]`, padding + 48, y + 25);

      // Status Badge
      const st = (t.status || 'PENDING').toUpperCase();
      ctx.fillStyle = st === 'COMPLETED' ? '#34d399' : st === 'STARTED' ? '#60a5fa' : st === 'SUBMITTED_FOR_REVIEW' ? '#c084fc' : '#fbbf24';
      ctx.font = 'bold 12px sans-serif';
      ctx.fillText(`● ${st}`, padding + 150, y + 25);

      // Title
      ctx.fillStyle = '#ffffff';
      ctx.font = 'bold 14px sans-serif';
      const titleStr = (t.title || 'Untitled Task').substring(0, 60);
      ctx.fillText(titleStr, padding + 15, y + 50);

      // Assignee & Due Date
      ctx.fillStyle = '#a1a1aa';
      ctx.font = '12px sans-serif';
      const assigneeStr = `Assignee: ${t.assignedEmployeeName || 'Store Team'}`;
      const dueStr = `Due: ${t.dueDate ? String(t.dueDate).replace('T', ' ').substring(0, 16) : 'End of Shift'}`;
      ctx.fillText(`${assigneeStr} | ${dueStr}`, padding + 480, y + 50);

      y += itemHeight;
    });
  }

  // Trigger Download PNG
  const dataUrl = canvas.toDataURL('image/png');
  const link = document.createElement('a');
  link.href = dataUrl;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}
