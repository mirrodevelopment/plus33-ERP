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
│   │   │               ├── esm/             # Field Service (work orders, van inventory, SLAs)
│   │   │               ├── exception/       # Global exception handlers
│   │   │               ├── finance/         # Finance & General Ledger (GL, fixed assets)
│   │   │               ├── grc/             # GRC, Audit ledger, and OPA policy engine
│   │   │               ├── hcm/             # HCM (employee records, payroll configurations)
│   │   │               ├── integration/     # V42: Event mesh gateway, brokers, connectors
│   │   │               ├── inventory/       # WMS integration, inventory adjustment journals
│   │   │               ├── manufacturing/   # BOM configurations, manufacturing orders
│   │   │               ├── organization/    # Multi-tenant structure, cost centers
│   │   │               ├── paymentrun/      # Automated payment runs engine
│   │   │               ├── platform/        # Platform core (config versioning, permissions)
│   │   │               ├── procurement/     # Purchase requests, vendor invoices
│   │   │               ├── project/         # Project tracking, resource scheduling
│   │   │               ├── sales/           # Sales orders, shipments, invoices
│   │   │               ├── security/        # Cryptography and role permissions
│   │   │               ├── store/           # Document store and file management
│   │   │               ├── twin/            # V47: Digital twins & process mining engine
│   │   │               ├── warehouse/       # Warehousing journals, location structures
│   │   │               ├── wms/             # Warehouse management cycles & counts
│   │   │               └── workforce/       # Workforce payroll processors
│   │   └── resources/
│   │       ├── application.yml              # Main application configuration
│   │       ├── application-test.yml         # Test profile configuration
│   │       └── db/
│   │           └── migration/               # Flyway SQL schema migrations (V1 to V250)
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
│                       │   ├── AgentLifecycleTest.java
│                       │   ├── AutonomousDecisionTest.java
│                       │   ├── DigitalTwinTest.java
│                       │   ├── KnowledgeManagementTest.java
│                       │   ├── ProcessMiningTest.java
│                       │   ├── ToolExecutionTest.java
│                       │   └── WorkflowAutomationTest.java
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

### `integration` (Enterprise Service Bus)
* Event broker connections, topic route mappings, CloudEvent envelopes parsing, Outbox message pattern implementations, and OpenTelemetry trace propagation.

### `grc` (Governance & Compliance)
* Policy lifecycle managers, Open Policy Agent (OPA) rule compliance evaluators, and system activity auditing registries.

---

## 3. Flyway SQL Database Migrations Mapping

The database schema spans version migrations from `V1` up to `V250`:
* **`V1`–`V100`**: Core transactional schema (Finance, GL, CRM, Purchasing, Inventory, Sales, HCM, Manufacturing).
* **`V101`–`V121`**: Field service management (Van stocks, Work orders, Checklists, SLAs, Dispatch routing).
* **`V122`–`V180`**: Advanced WMS & Project Management (Cycle counts, Warehouse ledger, Resource schedules).
* **`V181`–`V216`**: Platforms HA, Leader elections, Blue/Green deployers, Tenants routing, Distributed cache nodes.
* **`V217`–`V230`**: Observability logs, AIOps registry, FinOps chargeback, GRC compliance framework.
* **`V231`–`V240`**: V46 AI agents metadata, Conversation logs, Prompt versions, Chunk indexes, Context memory tables.
* **`V241`–`V250`**: V47 Process mining, Twin states, Telemetries, Simulation runs, Configuration versions, and Telemetry archival records.
