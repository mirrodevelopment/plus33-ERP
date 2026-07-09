/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ai Module
 * File              : assistant.js
 * Path              : frontend/ai/chat/assistant.js
 * Purpose           : Frontend utility: assistant for PLUS33 Coffee ERP
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related API       : N/A
 * Related CSS       : theme/variables.css, theme/coffee-dark.css
 * Related HTML      : index.html
 * Imports           : core/logger, store/notificationStore
 * Depends On        : core/logger, store/notificationStore
 *
 * Description
 * ---------------------------------------------------------------------------
 * Frontend utility: assistant for PLUS33 Coffee ERP. Part of the PLUS33 Coffee ERP vanilla JS SPA with hash-based
 * routing, JWT authentication, and a premium glassmorphism design system.
 ******************************************************************************/

import { logger } from '../../core/logger.js';
import { notificationStore } from '../../store/notificationStore.js';

export class AiAssistant {
  /**
   * Performs the fn operation in this module.
   * @memberof Ai Module
   */
  constructor() {
    this.expanded = false;
    this.history = [
      { sender: 'ai', text: 'Greetings, Operator. I am the PLUS33 AI Cognitive Assistant. How may I support your supply chain or fleet operations today?' }
    ];
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Ai Module
   */
  init() {
    logger.info('AiAssistant', 'Initializing floating AI Copilot assistant...');
    
    // 1. Create floating action button
    const fab = document.createElement('button');
    fab.id = "ai-fab-button";
    fab.className = "animate-pulse";
    fab.innerHTML = "✨";
    fab.setAttribute('style', `
      position: fixed;
      bottom: 25px;
      right: 25px;
      width: 56px;
      height: 56px;
      border-radius: 50%;
      background: var(--accent-primary, #c9a46a);
      border: 1px solid var(--border-color);
      color: #000;
      font-size: 1.5rem;
      cursor: pointer;
      box-shadow: var(--shadow-xl);
      z-index: 9999;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: var(--transition-fast);
      outline: none;
    `);
    
    // 2. Create chat panel container
    const panel = document.createElement('div');
    panel.id = "ai-chat-panel";
    panel.className = "glass";
    panel.setAttribute('style', `
      position: fixed;
      bottom: 95px;
      right: 25px;
      width: 350px;
      height: 450px;
      border-radius: var(--radius-lg);
      border: 1px solid var(--border-color);
      box-shadow: var(--shadow-xl);
      z-index: 9999;
      display: none;
      flex-direction: column;
      overflow: hidden;
      transition: var(--transition-normal);
      font-size: 0.8rem;
    `);
    
    document.body.appendChild(fab);
    document.body.appendChild(panel);
    
    this.bindEvents(fab, panel);
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Ai Module
   */
  bindEvents(fab, panel) {
    fab.addEventListener('click', () => {
      this.expanded = !this.expanded;
      panel.style.display = this.expanded ? 'flex' : 'none';
      fab.innerHTML = this.expanded ? '✕' : '✨';
      fab.style.background = this.expanded ? 'var(--bg-card)' : 'var(--accent-primary)';
      fab.style.color = this.expanded ? 'var(--text-primary)' : '#000';
      /**
       * Performs the fn operation in this module.
       * @memberof Ai Module
       */
      if (this.expanded) {
        this.renderChat(panel);
      }
    });
    
    // Hover effects
    fab.addEventListener('mouseover', () => {
      if (!this.expanded) fab.style.transform = 'scale(1.1)';
    });
    fab.addEventListener('mouseout', () => {
      if (!this.expanded) fab.style.transform = 'scale(1)';
    });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Ai Module
   */
  renderChat(panel) {
    panel.innerHTML = `
      <div style="padding: var(--spacing-md); background: rgba(0,0,0,0.2); border-bottom: 1px solid var(--border-color); display: flex; align-items: center; gap: var(--spacing-sm);">
        <span>✨</span>
        <strong style="font-family: var(--font-display); font-weight: 700; color: var(--accent-primary);">PLUS33 AI Assistant</strong>
      </div>
      
      <div id="ai-chat-messages" style="flex: 1; padding: var(--spacing-md); overflow-y: auto; display: flex; flex-direction: column; gap: var(--spacing-sm); background: rgba(0,0,0,0.15);">
        ${this.history.map(h => `
          <div style="align-self: ${h.sender === 'user' ? 'flex-end' : 'flex-start'}; max-width: 80%; padding: var(--spacing-sm) var(--spacing-md); border-radius: var(--radius-md); background: ${h.sender === 'user' ? 'var(--accent-glow)' : 'rgba(255,255,255,0.03)'}; border: 1px solid ${h.sender === 'user' ? 'var(--accent-primary)' : 'var(--border-color)'}; color: var(--text-primary);">
            ${h.text}
          </div>
        `).join('')}
      </div>
      
      <form id="ai-chat-input-form" style="padding: var(--spacing-sm); background: rgba(0,0,0,0.2); border-top: 1px solid var(--border-color); display: flex; gap: var(--spacing-sm);">
        <input type="text" id="ai-chat-text-input" placeholder="Ask about stocks, compliance, etc..." required style="flex: 1; padding: var(--spacing-sm); border-radius: var(--radius-sm); border: 1px solid var(--border-color); background: rgba(0,0,0,0.1); color: var(--text-primary); outline: none;" />
        <button type="submit" style="background: var(--accent-primary); border: none; color: #000; padding: var(--spacing-sm) var(--spacing-md); border-radius: var(--radius-sm); font-weight: 700; cursor: pointer;">Send</button>
      </form>
    `;
    
    // Auto scroll to bottom
    const box = panel.querySelector('#ai-chat-messages');
    box.scrollTop = box.scrollHeight;
    
    const form = panel.querySelector('#ai-chat-input-form');
    form.addEventListener('submit', (e) => {
      e.preventDefault();
      const input = panel.querySelector('#ai-chat-text-input');
      const text = input.value;
      if (!text.trim()) return;
      
      // User message
      this.history.push({ sender: 'user', text });
      input.value = '';
      this.renderChat(panel);
      
      // Trigger dynamic response
      setTimeout(() => {
        const reply = this.getResponse(text);
        this.history.push({ sender: 'ai', text: reply });
        this.renderChat(panel);
        notificationStore.info('AI assistant status updated.');
      }, 800);
    });
  }

  /**
   * Performs the fn operation in this module.
   * @memberof Ai Module
   */
  getResponse(query) {
    const text = query.toLowerCase();
    
    if (text.includes('stock') || text.includes('ledger') || text.includes('recall')) {
      return `Based on WMS real-time ledger, the <strong>EV Fleet Battery Pack (LOT-EV-BATT-009)</strong> has been successfully flagged for quarantine due to safety recalls in Zone B. Active inventory holds are in force.`;
    }
    if (text.includes('temp') || text.includes('temperature') || text.includes('topology')) {
      return `Current warehouse metrics report Zone B at 22.8°C (Warning status: charging grid load limits) and Zone A at 18.4°C (Operational).`;
    }
    if (text.includes('esg') || text.includes('emissions') || text.includes('carbon')) {
      return `ESG Accounting Service reports current CO2e direct Scope 1 emissions logs are signed and audited up to date (Framework: GRI / CSRD).`;
    }
    
    return `Query received. I am querying the backend platform services registry for operational telemetry. Let me know if you would like me to trigger an ESG calculation run or retrieve cycle count tolerances.`;
  }
}

// Automatically instantiate on load
const assistant = new AiAssistant();
window.addEventListener('load', () => {
  // Only load if on dashboard view
  /**
   * Performs the fn operation in this module.
   * @memberof Ai Module
   */
  if (window.location.hash !== '#login') {
    assistant.init();
  }
});

// Watch hash change
window.addEventListener('hashchange', () => {
  const fab = document.getElementById('ai-fab-button');
  const panel = document.getElementById('ai-chat-panel');
  
  /**
   * Performs the fn operation in this module.
   * @memberof Ai Module
   */
  if (window.location.hash === '#login') {
    if (fab) fab.remove();
    if (panel) panel.remove();
    assistant.expanded = false;
  } else {
    if (!document.getElementById('ai-fab-button')) {
      assistant.init();
    }
  }
});
export default assistant;
