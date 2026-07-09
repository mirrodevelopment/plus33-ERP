# RBAC Clearances & Permission Matrix

Clearance matrix mapping roles to backend authority codes.

| Clearance Level | Code String | Description / Section Clearance |
| :--- | :--- | :--- |
| **ULTIMATE_ADMIN** | * (Wildcard clearance) | Unlimited platform master access |
| **REGIONAL_ADMIN** | ANALYTICS_VIEW, INVENTORY_VIEW, STORE_VIEW | Regional operations inspection and summaries |
| **STORE_ADMIN** | INVENTORY_VIEW, INVENTORY_EDIT | Local store operations management |
| **SHIFT_SUPERVISOR**| WMS_VIEW, WMS_OPERATE | Warehouse bin management and stock allocations |
| **SENIOR_EMPLOYEE** | EMPLOYEE_SELF_SERVICE | Roster entries self-management |
| **JUNIOR_EMPLOYEE** | EMPLOYEE_SELF_SERVICE | Timesheets logs entry |
| **TRAINEE**         | EMPLOYEE_SELF_SERVICE | Training certifications log |
