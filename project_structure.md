# PLUS33 ERP Project Structure

This document details the complete directory and package layout tree for the **PLUS33 ERP** platform, showing the relationship between modular packages, resource directories, database migration scripts, and test suites.

---

## 1. Project Folder Tree

```text
plus33-erp/
├── pom.xml                                  # Maven Project Object Model definition
├── mvnw / mvnw.cmd                          # Maven wrapper executables
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── plus33/
│   │   │           └── erp/
│   │   │               ├── Plus33ErpApplication.java  # Main Spring Boot application class
│   │   │               ├── agent/           # V46: AI Agent, prompt, tool, & memory runtime
│   │   │               ├── analytics/       # Self-service BI query analyzers & validators
│   │   │               ├── ap/              # Accounts Payable (vendor invoices, matches)
│   │   │               ├── ar/              # Accounts Receivable (billing, credit checks)
│   │   │               ├── auth/            # OAuth2 & JWT authentications controllers
│   │   │               ├── bi/              # BI schema managers, dw sync schedulers
│   │   │               ├── common/          # Common base classes, models, & utilities
│   │   │               ├── config/          # Global Spring configuration classes
│   │   │               ├── crm/             # CRM (customer leads, marketing channels)
│   │   │               ├── edge/            # V52: Edge computing gateways, nodes config sync
│   │   │               ├── esm/             # Field Service (work orders, van inventory, SLAs)
│   │   │               ├── exception/       # Global exception handlers
│   │   │               ├── finance/         # Finance & General Ledger (GL, fixed assets)
│   │   │               ├── fleet/           # V53-V54: OTA deployments, zero-trust compliance
│   │   │               ├── grc/             # GRC, Audit ledger, and OPA policy engine
│   │   │               ├── hcm/             # HCM (employee records, payroll configurations)
│   │   │               ├── integration/     # V42: Event mesh gateway, brokers, connectors
│   │   │               ├── inventory/       # WMS integration, inventory adjustment journals
│   │   │               ├── iot/             # V50: Machinery telemetry, SCADA controls
│   │   │               ├── logistics/       # V49: Network nodes, topology, routing
│   │   │               ├── manufacturing/   # BOM configurations, manufacturing orders
│   │   │               ├── organization/    # Multi-tenant structure, cost centers
│   │   │               ├── paymentrun/      # Automated payment runs engine
│   │   │               ├── platform/        # Platform core (config versioning, permissions)
│   │   │               ├── predictive/      # V55: Predictive maintenance policies
│   │   │               ├── procurement/     # Purchase requests, vendor invoices
│   │   │               ├── project/         # Project tracking, resource scheduling
│   │   │               ├── routing/         # V56-V60: Route optimization, dispatch, fuel, EV, ESG
│   │   │               ├── sales/           # Sales orders, shipments, invoices
│   │   │               ├── security/        # Cryptography and role permissions
│   │   │               ├── spatial/         # V51: Geofences, spatial queries indexes
│   │   │               ├── store/           # Document store and file management
│   │   │               ├── twin/            # V47: Digital twins & process mining engine
│   │   │               ├── warehouse/       # Warehousing journals, location structures
│   │   │               ├── wms/             # Warehouse management cycles & counts
│   │   │               └── workforce/       # Workforce payroll processors
│   │   └── resources/
│   │       ├── application.yml              # Main application configuration
│   │       ├── application-test.yml         # Test profile configuration
│   │       └── db/
│   │           └── migration/               # Flyway SQL schema migrations (V1 to V376)
│   └── test/
│       └── java/
│           └── com/
│               └── plus33/
│                   └── erp/
│                       ├── APEnterpriseIntegrationTest.java
│                       ├── AREnterpriseIntegrationTest.java
│                       ├── BiEnterpriseIntegrationTest.java
│                       ├── EsmEnterpriseIntegrationTest.java
│                       ├── HcmEnterpriseIntegrationTest.java
│                       ├── IntegrationPlatformIntegrationTest.java
│                       ├── InventoryEnterpriseIntegrationTest.java
│                       ├── ManufacturingEnterpriseIntegrationTest.java
│                       ├── OpsPlatformIntegrationTest.java
│                       ├── OrganizationIntegrationTest.java
│                       ├── ProcurementEnterpriseIntegrationTest.java
│                       ├── ProjectEnterpriseIntegrationTest.java
│                       ├── SalesEnterpriseIntegrationTest.java
│                       ├── StoreIntegrationTest.java
│                       ├── platform/
│                       │   ├── AgentConversationTest.java
│                       │   ├── BatteryHealthTest.java
│                       │   ├── CarbonFootprintTest.java
│                       │   ├── ChargingSchedulerTest.java
│                       │   ├── DeviceComplianceTest.java
│                       │   ├── DispatchOptimizationTest.java
│                       │   ├── EcoDrivingTest.java
│                       │   ├── EsgComplianceTest.java
│                       │   ├── EvEnergyTest.java
│                       │   ├── FuelOptimizationTest.java
│                       │   ├── RouteOptimizationTest.java
│                       │   ├── Scope1EmissionsTest.java
│                       │   ├── Scope2EmissionsTest.java
│                       │   └── ...
│                       ├── wms/
│                       │   └── WarehouseManagementIntegrationTest.java
│                       └── workforce/
│                           └── PayrollEnterpriseIntegrationTest.java
```

---

## 2. Core Module Descriptions & File Structures

### `agent` (AI Cognitive Engine)
* **`provider`**: Vendor-agnostic AI LLM execution bindings (`AIProvider.java`, `LocalLlmProvider.java`).
* **`core`**: Agent execution orchestration cycles (`AgentOrchestrator.java`).
* **`tool`**: Permission-aware tool execution history trackers (`ToolExecutor.java`).
* **`knowledge`**: Text splitting pipelines & semantic retrieval (`ChunkingService.java`, `RetrievalService.java`).
* **`workflow`**: Multi-agent process orchestrators (`WorkflowAutomator.java`).
* **`prompt`**: Versioned prompts template managers (`PromptVersionManager.java`).
* **`memory`**: Multi-scoped contextual memories (`AgentMemoryManager.java`).
* **`vector`**: Embedding providers & vector indexing stores (`VectorStoreCoordinator.java`).

### `twin` (Digital Twins & Process Mining)
* **`telemetry`**: Sensor data ingestion pipelines (`TelemetryIngestionService.java`, `TelemetryNormalizer.java`).
* **`device`**: Digital twin state projectors (`TwinStateProjector.java`).
* **`anomaly`**: State anomaly checkers (`AnomalyDetector.java`).
* **`mining`**: Pathway variant discovery engines (`CaseAssembler.java`, `VariantDiscoveryService.java`, `PerformanceAnalyzer.java`).
* **`conformance`**: BPMN conformance checks & SLA risk predictions (`ConformanceEngine.java`, `SlaPredictionService.java`).
* **`decision`**: Staged autonomous actions evaluator (`DecisionEngine.java`, `RecommendationEngine.java`).
* **`simulation`**: Scenario forecasting engines (`SimulationService.java`).

### `routing` (Fleet Routing & Emissions Compliance)
* **`optimization`**: V56 route cost minimization algorithms and emissions factor engine (`RouteOptimizationEngine.java`).
* **`dispatch`**: V57 AI-driven vehicle/driver fleet dispatching optimizer (`DispatchOptimizationEngine.java`).
* **`fuel`**: V58 dynamic fuel optimization engines and eco-driving diagnostics logging (`FuelOptimizationEngine.java`).
* **`ev`**: V59 EV energy management, battery health analytics, and charging schedules reservation scheduler (`EvEnergyManagementService.java`).
* **`esg`**: V60 Scope 1 / Scope 2 greenhouse gas carbon accounting reporting compliance mappings (`CarbonAccountingService.java`).

---

## 3. Flyway SQL Database Migrations Mapping

The database schema spans version migrations from `V1` up to `V376`:
* **`V1`–`V100`**: Core transactional schema (Finance, GL, CRM, Purchasing, Inventory, Sales, HCM, Manufacturing).
* **`V101`–`V121`**: Field service management (Van stocks, Work orders, Checklists, SLAs, Dispatch routing).
* **`V122`–`V180`**: Advanced WMS & Project Management (Cycle counts, Warehouse ledger, Resource schedules).
* **`V181`–`V216`**: Platforms HA, Leader elections, Blue/Green deployers, Tenants routing, Distributed cache nodes.
* **`V217`–`V230`**: Observability logs, AIOps registry, FinOps chargeback, GRC compliance framework.
* **`V231`–`V240`**: V46 AI agents metadata, Conversation logs, Prompt versions, Chunk indexes, Context memory tables.
* **`V241`–`V250`**: V47 Process mining, Twin states, Telemetries, Simulation runs, Configuration versions, and Telemetry archival records.
* **`V251`–`V280`**: V48 Knowledge graph entity resolutions, V49 Logistics network nodes registry maps.
* **`V281`–`V310`**: V50 Machinery telemetry & SCADA triggers, V51 Spatial query indices, V52 Edge Node sync state registers.
* **`V311`–`V330`**: V53 OTA deployment registry, V54 Zero-Trust attestation, V55 Predictive Maintenance policy tables.
* **`V331`–`V350`**: V56 Route Carbon Footprints, V57 Dispatch AI-assignments, constraint validations, and Monte Carlo runs.
* **`V351`–`V360`**: V58 Dynamic fuel optimization and Eco-Driving driver behaviors logs.
* **`V361`–`V370`**: V59 EV Battery telemetry status and Charging reservation schedule allocations.
* **`V371`–`V376`**: V60 ESG Greenhouse Gas Scope 1/2 carbon emissions logs and auditing verification signatures.
