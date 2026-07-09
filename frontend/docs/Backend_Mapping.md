# Backend Module Synchronisation

Maps frontend module structure to the Spring Boot package bounded contexts.

| Frontend Services Folder | Java Package Bounded Context | Core Responsibility |
| :--- | :--- | :--- |
| rontend/services/auth/ | com.plus33.erp.auth | Security credentials login and token session management |
| rontend/services/organization/ | com.plus33.erp.organization | Enterprise hierarchy: Companies, Regions, Warehouses, Stores |
| rontend/services/inventory/ | com.plus33.erp.inventory | Physical product ledger, lot allocations, traceability |
| rontend/services/procurement/ | com.plus33.erp.procurement | Supplier relations, purchase orders, goods receipts, invoices |
| rontend/services/finance/ | com.plus33.erp.finance | Account ledger, journal entries, payments |
| rontend/services/sales/ | com.plus33.erp.sales | Sales orders, customer invoicing, return shipments |
| rontend/services/warehouse/ | com.plus33.erp.wms | Bin topologies, robotics node routing, wave picking |
| rontend/services/crm/ | com.plus33.erp.crm | Omnichannel complaints, feedback sentiment logs |
| rontend/services/dashboard/ | com.plus33.erp.dashboard | Multi-role homepage layouts |
| rontend/services/analytics/ | com.plus33.erp.analytics | Historical BI snapshot compilations |
| rontend/services/workforce/ | com.plus33.erp.workforce | Timesheet rotas, employee payroll calculations |
| rontend/services/grc/ | com.plus33.erp.grc | SoD checks, compliance evidence attestation, policy versions |
| rontend/services/platform/ | com.plus33.erp.platform | Cache nodes status, K8s cluster SLO measurements, feature flags |
| rontend/services/integration/ | com.plus33.erp.integration | Resilient events mesh saga transactions |
