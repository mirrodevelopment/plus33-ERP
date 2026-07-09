# Ultimate Admin Dashboard - UI Design Guide

This manual documents the user interface structure and style tokens for the **Ultimate Admin Dashboard** inside the **PLUS33 Franchise ERP**.

## Visual Brand Identity

The interface anchors on a premium, coffee-brand-aligned design language.

### A. Color Palette
*   **Backdrop Background:** Charcoal Black (`#141414`)
*   **Typography Primary:** Off White (`#F5F5F5`)
*   **Accent Borders & Buttons:** Coffee Gold (`#C9A46A`)
*   **Secondary Elements:** Muted Grey-Brown (`#8C857B`)

### B. Typography
*   **Headers & Titles:** Montserrat (Weights: 600, 700, 800) loaded dynamically from Google Fonts.
*   **Body & Descriptions:** Inter (Weights: 300, 400, 500)

### C. Glassmorphism Styling
Cards and lists employ glassmorphic effects:
*   `background: rgba(30, 30, 30, 0.6)`
*   `backdrop-filter: blur(12px)`
*   `border: 1px solid rgba(201, 164, 106, 0.25)`

## 12-Column Responsive Layout Grid

Grid items are organized in a standard flexbox-based 12-column grid. Responsive breakpoints collapse 3-column cards (`col-3`) to `col-6` on medium sizes and `col-12` on small mobile viewports.
