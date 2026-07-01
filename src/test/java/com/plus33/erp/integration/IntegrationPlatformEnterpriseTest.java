package com.plus33.erp.integration;

import com.plus33.erp.integration.entity.*;
import com.plus33.erp.integration.eventmesh.*;
import com.plus33.erp.integration.outbox.*;
import com.plus33.erp.integration.inbox.*;
import com.plus33.erp.integration.resilience.*;
import com.plus33.erp.integration.connector.*;
import com.plus33.erp.integration.gateway.*;
import com.plus33.erp.integration.workflow.*;
import com.plus33.erp.integration.saga.*;
import com.plus33.erp.integration.registry.*;
import com.plus33.erp.integration.service.*;
import com.plus33.erp.integration.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationPlatformEnterpriseTest {

    @Autowired EventMeshRegistryRepository eventMeshRegistryRepo;
    @Autowired EventMeshDeadLetterRepository deadLetterRepo;
    @Autowired IntegrationOutboxRepository outboxRepo;
    @Autowired IntegrationInboxRepository inboxRepo;
    @Autowired ProcessedMessageRepository processedMessageRepo;
    @Autowired ConsumerCheckpointRepository consumerCheckpointRepo;
    @Autowired BiBrokerRegistryRepository brokerRegistryRepo;
    @Autowired IntegrationConnectorRepository connectorRepo;
    @Autowired IntegrationConnectorExecutionRepository connectorExecutionRepo;
    @Autowired IntegrationGatewayKeyRepository gatewayKeyRepo;
    @Autowired IntegrationGatewayUsageLogRepository usageLogRepo;
    @Autowired IntegrationWorkflowDefinitionRepository workflowDefRepo;
    @Autowired IntegrationWorkflowInstanceRepository workflowInstanceRepo;
    @Autowired IntegrationWorkflowTaskRepository workflowTaskRepo;
    @Autowired IntegrationSagaRepository sagaRepo;
    @Autowired IntegrationSagaStepRepository sagaStepRepo;
    @Autowired IntegrationSchemaRegistryRepository schemaRegistryRepo;
    @Autowired IntegrationMonitoringMetricRepository metricsRepo;
    @Autowired IntegrationLockRepository lockRepo;

    @Autowired BrokerRegistry brokerRegistry;
    @Autowired OutboxPublisher outboxPublisher;
    @Autowired OutboxDispatcher outboxDispatcher;
    @Autowired InboxProcessor inboxProcessor;
    @Autowired SecretManager secretManager;
    @Autowired IntegrationLockCoordinator lockCoordinator;
    @Autowired ConnectorRegistry connectorRegistry;
    @Autowired ApiGatewayService gatewayService;
    @Autowired WorkflowEngine workflowEngine;
    @Autowired SagaCoordinator sagaCoordinator;
    @Autowired SchemaRegistryService schemaRegistryService;

    @Autowired org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    private static boolean dbCleaned = false;

    @BeforeEach
    void cleanDbOnce() {
        if (!dbCleaned) {
            jdbcTemplate.execute("TRUNCATE TABLE processed_messages, consumer_checkpoint, integration_inbox, integration_outbox, bi_event_mesh_dead_letter, bi_event_mesh_registry CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE integration_connector_execution, integration_connector, bi_broker_registry CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE integration_gateway_usage_log, integration_gateway_key CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE integration_workflow_task, integration_workflow_instance, integration_workflow_definition CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE integration_saga_step, integration_saga CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE integration_lock, integration_monitoring_metric, integration_schema_registry CASCADE");
            dbCleaned = true;
        }
    }

    @Test @Order(10)
    void eventMeshRegistry_savesAndRetrievesTopics() {
        for (int i = 1; i <= 60; i++) {
            EventMeshRegistry reg = new EventMeshRegistry();
            reg.setTopic("topic-" + i);
            reg.setPublisherModule("MODULE-" + i);
            eventMeshRegistryRepo.save(reg);
        }
        List<EventMeshRegistry> registries = eventMeshRegistryRepo.findAll();
        assertEquals(60, registries.size());
        for (int i = 0; i < 60; i++) {
            assertNotNull(registries.get(i).getId());
            assertTrue(registries.get(i).getTopic().startsWith("topic-"));
            assertNotNull(registries.get(i).getCreatedAt());
        }
    }

    @Test @Order(20)
    void outboxPublisher_persistsPendingEvents() {
        for (int i = 1; i <= 60; i++) {
            outboxPublisher.publishEvent("topic-outbox-" + i, "OrderCreated", "{\"orderId\":" + i + "}", "00-trace-" + i + "-span-01", "corr-" + i, "tenant-" + i);
        }
        List<IntegrationOutbox> pending = outboxRepo.findAll();
        assertEquals(60, pending.size());
        for (int i = 0; i < 60; i++) {
            assertEquals("PENDING", pending.get(i).getStatus());
            assertEquals(0, pending.get(i).getAttempts());
            assertNotNull(pending.get(i).getEventId());
        }
    }

    @Test @Order(30)
    void outboxDispatcher_processesAndPublishesPendingEvents() {
        outboxDispatcher.dispatchPendingEvents();
        List<IntegrationOutbox> dispatched = outboxRepo.findAll();
        assertEquals(60, dispatched.size());
        for (int i = 0; i < 60; i++) {
            assertEquals("DISPATCHED", dispatched.get(i).getStatus());
            assertEquals(1, dispatched.get(i).getAttempts());
        }
    }

    @Test @Order(40)
    void inboxProcessor_deduplicatesAndCheckpointsMessages() {
        for (int i = 1; i <= 60; i++) {
            CloudEvent ce = new CloudEvent("evt-" + i, "PLUS33", "OrderCreated", "{\"orderId\":" + i + "}");
            ce.setTraceparent("00-trace-" + i);
            ce.setCorrelationId("corr-" + i);
            ce.setTenantId("tenant-" + i);

            boolean processedFirst = inboxProcessor.processIncomingMessage(ce, "group-1", "consumer-1");
            assertTrue(processedFirst);

            boolean processedSecond = inboxProcessor.processIncomingMessage(ce, "group-1", "consumer-1");
            assertFalse(processedSecond);
        }

        List<ProcessedMessage> processed = processedMessageRepo.findAll();
        assertEquals(60, processed.size());

        List<ConsumerCheckpoint> checkpoints = consumerCheckpointRepo.findAll();
        assertEquals(1, checkpoints.size());
        assertEquals("group-1", checkpoints.get(0).getGroupName());
        assertEquals(60L, checkpoints.get(0).getCheckpointOffset());
    }

    @Test @Order(50)
    void brokerRegistry_resolvesBrokerImplementation() {
        for (int i = 1; i <= 60; i++) {
            BiBrokerRegistry reg = new BiBrokerRegistry();
            reg.setBrokerName("broker-" + i);
            reg.setBrokerType(i % 3 == 0 ? "KAFKA" : (i % 3 == 1 ? "RABBITMQ" : "IN_MEMORY"));
            reg.setConnectionUrl("localhost:" + (9090 + i));
            reg.setActive(true);
            brokerRegistryRepo.save(reg);
        }
        List<BiBrokerRegistry> brokers = brokerRegistryRepo.findAll();
        assertEquals(60, brokers.size());

        MessageBroker active = brokerRegistry.getActiveBroker();
        assertNotNull(active);
    }

    @Test @Order(60)
    void secretManager_resolvesSecretReferences() {
        for (int i = 1; i <= 60; i++) {
            secretManager.storeSecret("ref-" + i, "secret-val-" + i);
        }
        for (int i = 1; i <= 60; i++) {
            String value = secretManager.resolveSecret("ref-" + i);
            assertEquals("secret-val-" + i, value);
        }
    }

    @Test @Order(70)
    void resilienceRetry_calculatesCorrectBackoffDelays() {
        RetryPolicy policyFixed = new RetryPolicy("p1", "FIXED", 3, 1000, 2.0);
        RetryPolicy policyLinear = new RetryPolicy("p2", "LINEAR", 3, 1000, 2.0);
        RetryPolicy policyExp = new RetryPolicy("p3", "EXPONENTIAL", 3, 1000, 2.0);

        for (int i = 1; i <= 20; i++) {
            assertEquals(1000, policyFixed.calculateDelay(i));
            assertEquals(1000 * i, policyLinear.calculateDelay(i));
            assertEquals((long) (1000 * Math.pow(2.0, i - 1)), policyExp.calculateDelay(i));
        }
    }

    @Test @Order(80)
    void resilienceCircuitBreaker_tracksStateTransitions() {
        CircuitBreaker cb = new CircuitBreaker("cb-1", 5, 200);
        for (int i = 1; i <= 5; i++) {
            assertTrue(cb.allowCall());
            cb.recordFailure();
        }
        assertFalse(cb.allowCall());
        assertEquals("OPEN", cb.getState());
    }

    @Test @Order(90)
    void resilienceBulkhead_restrictsConcurrentCalls() {
        Bulkhead bulkhead = new Bulkhead("bh-1", 20);
        for (int i = 1; i <= 20; i++) {
            assertTrue(bulkhead.tryAcquire());
        }
        assertFalse(bulkhead.tryAcquire());
        for (int i = 1; i <= 20; i++) {
            bulkhead.release();
        }
        assertTrue(bulkhead.tryAcquire());
    }

    @Test @Order(100)
    void lockCoordinator_acquiresAndReleasesLocks() {
        for (int i = 1; i <= 60; i++) {
            boolean acquired = lockCoordinator.acquireLock("lock-" + i, "owner-a", 10);
            assertTrue(acquired);
            boolean acquiredAgain = lockCoordinator.acquireLock("lock-" + i, "owner-b", 10);
            assertFalse(acquiredAgain);
            lockCoordinator.releaseLock("lock-" + i, "owner-a");
            boolean acquiredFinally = lockCoordinator.acquireLock("lock-" + i, "owner-b", 10);
            assertTrue(acquiredFinally);
        }
    }

    @Test @Order(110)
    void connectors_executeRestCallsWithCircuitBreakers() {
        for (int i = 1; i <= 30; i++) {
            Connector connector = connectorRegistry.getConnector("REST");
            assertNotNull(connector);
            ConnectorResult success = connector.execute("localhost-" + i, 8080, "partner-service-creds", "valid-payload");
            assertTrue(success.isSuccess());

            ConnectorResult fail = connector.execute("localhost-" + i, 8080, "partner-service-creds", "TIMEOUT");
            assertFalse(fail.isSuccess());
        }
    }

    @Test @Order(120)
    void gatewayService_rateLimitsAndEnforcesDailyQuotas() {
        IntegrationGatewayKey key = new IntegrationGatewayKey();
        key.setApiKey("key-abc-123");
        key.setPartnerCode("PARTNER-XYZ");
        key.setRateLimitPerMin(30);
        key.setQuotaPerDay(30);
        key.setAllowedRoutes("*");
        key.setActive(true);
        gatewayKeyRepo.save(key);

        for (int i = 1; i <= 30; i++) {
            boolean authorized = gatewayService.authorizeAndRateLimit("key-abc-123", "/api/data", "GET");
            assertTrue(authorized);
        }

        boolean blocked = gatewayService.authorizeAndRateLimit("key-abc-123", "/api/data", "GET");
        assertFalse(blocked);
    }

    @Test @Order(130)
    void workflowEngine_managesWorkflowInstances() {
        IntegrationWorkflowDefinition def = new IntegrationWorkflowDefinition();
        def.setDefinitionCode("WF-ORDER-PROC");
        def.setName("Order Process workflow");
        def.setLayoutJson("{\"nodes\":[]}");
        workflowDefRepo.save(def);

        for (int i = 1; i <= 60; i++) {
            IntegrationWorkflowInstance inst = workflowEngine.startWorkflow("WF-ORDER-PROC", "{\"orderId\":" + i + "}");
            assertNotNull(inst);
            assertEquals("RUNNING", inst.getStatus());
            assertNotNull(inst.getInstanceCode());
        }

        List<IntegrationWorkflowInstance> instances = workflowInstanceRepo.findAll();
        assertEquals(60, instances.size());
    }

    @Test @Order(140)
    void sagaCoordinator_coordinatesSagaStepsAndCompensations() {
        for (int i = 1; i <= 60; i++) {
            List<IntegrationSagaStep> steps = new ArrayList<>();
            IntegrationSagaStep s1 = new IntegrationSagaStep();
            s1.setStepName("step-1-" + i);
            s1.setStatus(i % 5 == 0 ? "FAILED" : "COMPLETED");
            s1.setExecutionOrder(1);
            steps.add(s1);

            IntegrationSagaStep s2 = new IntegrationSagaStep();
            s2.setStepName("step-2-" + i);
            s2.setStatus("COMPLETED");
            s2.setExecutionOrder(2);
            steps.add(s2);

            IntegrationSaga saga = sagaCoordinator.executeSaga("ORDER-SAGA", "{\"sagaId\":" + i + "}", steps);
            assertNotNull(saga);
            if (i % 5 == 0) {
                assertEquals("FAILED", saga.getStatus());
            } else {
                assertEquals("COMPLETED", saga.getStatus());
            }
        }
    }

    @Test @Order(150)
    void schemaRegistry_validatesCompatibilityRules() {
        for (int i = 1; i <= 60; i++) {
            schemaRegistryService.registerSchema("OrderCreated-" + i, "1.0", "{\"properties\":[]}", "BACKWARD");
        }
        for (int i = 1; i <= 60; i++) {
            boolean valid = schemaRegistryService.validateEvent("OrderCreated-" + i, "1.0", "{\"orderId\":" + i + "}");
            assertTrue(valid);

            boolean invalid = schemaRegistryService.validateEvent("OrderCreated-" + i, "1.0", "invalid-json");
            assertFalse(invalid);
        }
    }

    @Test @Order(160)
    void telemetryTraceContext_propagatesCorrectly() {
        for (int i = 1; i <= 60; i++) {
            String traceId = "a1b2c3d4e5f6a1b2c3d4e5f6" + String.format("%08d", i);
            String spanId = "1a2b3c4d5e" + String.format("%06d", i);
            String traceparent = TelemetryContext.inject(traceId, spanId);
            assertNotNull(traceparent);

            TelemetryContext.TraceInfo info = TelemetryContext.extract(traceparent);
            assertEquals(traceId, info.traceId);
            assertEquals(spanId, info.spanId);
        }
    }
}
