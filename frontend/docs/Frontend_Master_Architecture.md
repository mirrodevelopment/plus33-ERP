# Frontend Master Architecture

This document defines the core architecture principles of the PLUS33 ERP frontend.

## Modularity & Framework Independence
The frontend is completely framework-independent, built using only Vanilla HTML5, CSS3, and ES6 modules. It establishes a highly reusable structure that can be integrated with any backend engine or scaled as the codebase expands.

## Layout Rendering Pattern
A centralized application wrapper manages layouts (`blank.html`, `dashboard.html`) and triggers page changes dynamically through client-side routers:
- **Blank Layout:** Utilized for authentication screens.
- **Dashboard Layout:** Utilized for main ERP dashboards with Sidebar and Header panels.

## State Management
State is persisted in storage models (`storage.js`) and synchronized with browser memory context.
