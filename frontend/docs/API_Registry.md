# API Registry

This index maps API endpoints to frontend services and controllers.

| HTTP Method | Endpoint | Required Permission | Service Called | Backend Controller |
|---|---|---|---|---|
| POST | `/api/v1/auth/login` | None | AuthService.js | AuthController.java |
| POST | `/api/v1/supplier-invoices` | `SUPPLIER_INVOICE_CREATE` | SupplierInvoiceService.js | SupplierInvoiceController.java |
| POST | `/api/v1/payments` | `PAYMENT_CREATE` | PaymentService.js | PaymentController.java |
| POST | `/api/v1/inventory-transfers` | `INVENTORY_TRANSFER_CREATE` | InventoryTransferService.js | InventoryTransferController.java |
| POST | `/api/v1/products` | `INVENTORY_EDIT` | ProductService.js | ProductController.java |
