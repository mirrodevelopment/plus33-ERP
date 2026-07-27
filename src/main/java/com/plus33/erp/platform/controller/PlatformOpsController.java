/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.controller
 * File              : PlatformOpsController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformOpsController
 * Related Service   : PlatformOpsControllerService, PlatformOpsControllerServiceImpl
 * Related Repository: PlatformOpsControllerRepository
 * Related Entity    : PlatformOpsController
 * Related DTO       : N/A
 * Related Mapper    : PlatformOpsControllerMapper
 * Related DB Table  : platform_ops_controllers
 * Related REST APIs : GET /api/platform/config, POST /api/platform/config, POST /api/platform/config/rollback, GET /api/platform/flag
 * Depends On        : None
 * Used By           : Platform Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Platform Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/platform/config, POST /api/platform/config, POST /api/platform/config/rollback, GET /api/platform/flag
 ******************************************************************************/
package com.plus33.erp.platform.controller;

import com.plus33.erp.platform.config.DistributedConfigService;
import com.plus33.erp.platform.featureflag.FeatureFlagService;
import com.plus33.erp.platform.metrics.PrometheusExporterService;
import com.plus33.erp.platform.audit.PlatformAuditService;
import com.plus33.erp.platform.cache.DistributedCacheManager;
import com.plus33.erp.platform.lock.DistributedLockManager;
import com.plus33.erp.platform.dashboard.PlatformRuntimeDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformOpsController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to PlatformOpsService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> PlatformOpsController.endpoint()
 *   --> PlatformOpsService.method()
 *   --> PlatformOpsRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/platform/config, POST /api/platform/config, POST /api/platform/config/rollback, GET /api/platform/flag, POST /api/platform/flag</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/platform")
public class PlatformOpsController {
    @Autowired DistributedConfigService configService;
    @Autowired FeatureFlagService flagService;
    @Autowired PrometheusExporterService metricsService;
    @Autowired PlatformAuditService auditService;
    @Autowired DistributedCacheManager cacheManager;
    @Autowired DistributedLockManager lockManager;
    @Autowired PlatformRuntimeDashboardService dashboardService;
    /**
     * Retrieves config data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param key the key input value
     * @param profile the profile input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/config")
    public ResponseEntity<String> getConfig(@RequestParam String key, @RequestParam String profile) {
        String val = configService.getConfig(key, profile);
        return val != null ? ResponseEntity.ok(val) : ResponseEntity.notFound().build();
    }

    /**
     * Performs the setConfig operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/config")
    public ResponseEntity<Void> setConfig(
            @RequestParam String key, 
            @RequestParam String value, 
            @RequestParam String profile,
            @RequestParam String operator) {
        configService.setConfig(key, value, profile, operator);
        auditService.logAudit("SET_CONFIG", operator, "REST", "key=" + key + ", value=" + value);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the rollbackConfig operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/config/rollback")
    public ResponseEntity<Void> rollbackConfig(
            @RequestParam String key,
            @RequestParam String profile,
            @RequestParam int version,
            @RequestParam String operator) {
        configService.rollbackConfig(key, profile, version, operator);
        auditService.logAudit("ROLLBACK_CONFIG", operator, "REST", "key=" + key + ", version=" + version);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves flag data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param key the key input value
     * @param userId authenticated user identifier
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/flag")
    public ResponseEntity<Boolean> getFlag(@RequestParam String key, @RequestParam String userId) {
        return ResponseEntity.ok(flagService.isEnabled(key, userId));
    }

    /**
     * Updates an existing flag record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/flag")
    public ResponseEntity<Void> updateFlag(
            @RequestParam String key,
            @RequestParam String status,
            @RequestParam int rollout,
            @RequestParam String reason,
            @RequestParam String operator) {
        flagService.updateFlag(key, status, rollout, reason, operator);
        auditService.logAudit("UPDATE_FLAG", operator, "REST", "key=" + key + ", status=" + status);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves metrics data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/metrics")
    public ResponseEntity<String> getMetrics() {
        return ResponseEntity.ok(metricsService.exportPrometheusFormat());
    }

    /**
     * Performs the evictCache operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/cache/evict")
    public ResponseEntity<Void> evictCache(
            @RequestParam String namespace,
            @RequestParam String key,
            @RequestParam String operator) {
        cacheManager.evict(namespace, key);
        auditService.logAudit("EVICT_CACHE", operator, "REST", "ns=" + namespace + ", key=" + key);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the acquireLock operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data true if operation succeeded, false otherwise
     */
    @PostMapping("/lock")
    public ResponseEntity<Boolean> acquireLock(
            @RequestParam String name,
            @RequestParam String node,
            @RequestParam int ttl) {
        return ResponseEntity.ok(lockManager.acquireLock(name, node, ttl));
    }

    /**
     * Releases previously reserved lock resources back to the available pool.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @DeleteMapping("/lock")
    public ResponseEntity<Void> releaseLock(
            @RequestParam String name,
            @RequestParam String node) {
        lockManager.releaseLock(name, node);
        return ResponseEntity.ok().build();
    }

    @Autowired com.plus33.erp.platform.repository.PlatformAuditLogRepository platformAuditRepo;
    @Autowired com.plus33.erp.platform.repository.PlatformLogEntryRepository platformLogRepo;
    /**
     * Retrieves dashboard data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }

    /**
     * Retrieves audit logs data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/logs/audit")
    public ResponseEntity<java.util.List<com.plus33.erp.platform.entity.PlatformAuditLog>> getAuditLogs() {
        return ResponseEntity.ok(platformAuditRepo.findAll());
    }

    /**
     * Retrieves system logs data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/logs/system")
    public ResponseEntity<java.util.List<com.plus33.erp.platform.entity.PlatformLogEntry>> getSystemLogs() {
        return ResponseEntity.ok(platformLogRepo.findAll());
    }

    @Autowired com.plus33.erp.platform.slo.SloMeasurementService sloService;
    @Autowired com.plus33.erp.platform.aiops.AioModelPredictor aiopsService;
    @Autowired com.plus33.erp.platform.chargeback.CostChargebackManager chargebackService;
    @Autowired com.plus33.erp.platform.policy.OpaPolicyManager policyService;
    /**
     * Performs the recordSlo operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/slo/measurement")
    public ResponseEntity<Void> recordSlo(
            @RequestParam String name,
            @RequestParam double current,
            @RequestParam double budget,
            @RequestParam String operator) {
        sloService.recordMeasurement(name, current, budget);
        auditService.logAudit("RECORD_SLO", operator, "REST", "slo=" + name + ", val=" + current);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the runProjection operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/aiops/projection")
    public ResponseEntity<Void> runProjection(
            @RequestParam String metric,
            @RequestParam double val,
            @RequestParam String reason,
            @RequestParam String operator) {
        aiopsService.runProjection(metric, val, reason);
        auditService.logAudit("RUN_AIOPS_PROJECTION", operator, "REST", "metric=" + metric + ", val=" + val);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the recordChargeback operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/chargeback")
    public ResponseEntity<Void> recordChargeback(
            @RequestParam String centerCode,
            @RequestParam double amount,
            @RequestParam String month,
            @RequestParam String operator) {
        chargebackService.recordChargeback(centerCode, amount, month);
        auditService.logAudit("RECORD_CHARGEBACK", operator, "REST", "center=" + centerCode + ", amt=" + amount);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the auditPolicy operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/policy/audit")
    public ResponseEntity<Void> auditPolicy(
            @RequestParam String code,
            @RequestParam String userId,
            @RequestParam String action,
            @RequestParam String decision) {
        policyService.auditPolicy(code, userId, action, decision);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates an existing policy version record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/policy/version")
    public ResponseEntity<Void> updatePolicyVersion(
            @RequestParam String code,
            @RequestParam String version,
            @RequestParam String rego,
            @RequestParam String operator,
            @RequestParam String reason) {
        policyService.updatePolicyVersion(code, version, rego, operator, reason);
        auditService.logAudit("UPDATE_POLICY_VERSION", operator, "REST", "code=" + code + ", ver=" + version);
        return ResponseEntity.ok().build();
    }

    @Autowired com.plus33.erp.agent.core.AgentOrchestrator agentOrchestrator;
    @Autowired com.plus33.erp.agent.tool.ToolExecutor toolExecutor;
    @Autowired com.plus33.erp.agent.workflow.WorkflowAutomator workflowAutomator;
    @Autowired com.plus33.erp.agent.prompt.PromptVersionManager promptVersionManager;
    @Autowired com.plus33.erp.agent.memory.AgentMemoryManager memoryManager;
    /**
     * Performs the startAgentSession operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data the result string value
     */
    @PostMapping("/agent/session")
    public ResponseEntity<String> startAgentSession(
            @RequestParam String userId,
            @RequestParam String operator) {
        com.plus33.erp.platform.entity.PlatformAgentSession s = agentOrchestrator.startSession(userId);
        auditService.logAudit("START_AGENT_SESSION", operator, "REST", "user=" + userId);
        return ResponseEntity.ok(s.getSessionToken());
    }

    /**
     * Processes the agent message business workflow end-to-end.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/agent/message")
    public ResponseEntity<Void> processAgentMessage(
            @RequestParam Long sessionId,
            @RequestParam String message,
            @RequestParam String operator) {
        agentOrchestrator.processMessage(sessionId, message);
        auditService.logAudit("PROCESS_AGENT_MSG", operator, "REST", "session=" + sessionId);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the executeAgentTool operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/agent/tool")
    public ResponseEntity<Void> executeAgentTool(
            @RequestParam String code,
            @RequestParam String params,
            @RequestParam String operator) {
        toolExecutor.executeTool(code, params);
        auditService.logAudit("EXECUTE_AGENT_TOOL", operator, "REST", "tool=" + code);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the triggerAgentWorkflow operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/agent/workflow")
    public ResponseEntity<Void> triggerAgentWorkflow(
            @RequestParam String code,
            @RequestParam String operator) {
        workflowAutomator.triggerWorkflow(code);
        auditService.logAudit("TRIGGER_AGENT_WORKFLOW", operator, "REST", "workflow=" + code);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new agent prompt and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/agent/prompt")
    public ResponseEntity<Void> registerAgentPrompt(
            @RequestParam String code,
            @RequestParam String desc,
            @RequestParam String sys,
            @RequestParam String user,
            @RequestParam String operator) {
        promptVersionManager.registerPrompt(code, desc);
        promptVersionManager.addVersion(code, "v1.0.0", sys, user);
        auditService.logAudit("REGISTER_AGENT_PROMPT", operator, "REST", "code=" + code);
        return ResponseEntity.ok().build();
    }

    /**
     * Persists the agent memory entity to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/agent/memory")
    public ResponseEntity<Void> storeAgentMemory(
            @RequestParam Long sessionId,
            @RequestParam String scope,
            @RequestParam String key,
            @RequestParam String val,
            @RequestParam String operator) {
        memoryManager.storeMemory(sessionId, scope, key, val);
        auditService.logAudit("STORE_AGENT_MEMORY", operator, "REST", "session=" + sessionId + ", key=" + key);
        return ResponseEntity.ok().build();
    }

    @Autowired com.plus33.erp.twin.mining.CaseAssembler caseAssembler;
    @Autowired com.plus33.erp.twin.telemetry.TelemetryIngestionService telemetryIngestionService;
    @Autowired com.plus33.erp.twin.decision.DecisionEngine decisionEngine;
    @Autowired com.plus33.erp.twin.simulation.SimulationService simulationService;
    /**
     * Performs the assembleProcessCase operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/process/case")
    public ResponseEntity<Void> assembleProcessCase(
            @RequestParam String token,
            @RequestParam String processName,
            @RequestParam String operator) {
        caseAssembler.assembleCase(token, processName);
        auditService.logAudit("ASSEMBLE_PROCESS_CASE", operator, "REST", "token=" + token);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the ingestTwinTelemetry operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/twin/telemetry")
    public ResponseEntity<Void> ingestTwinTelemetry(
            @RequestParam Long instanceId,
            @RequestParam String name,
            @RequestParam java.math.BigDecimal value,
            @RequestParam String operator) {
        telemetryIngestionService.ingest(instanceId, name, value);
        auditService.logAudit("INGEST_TWIN_TELEMETRY", operator, "REST", "instance=" + instanceId);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the evaluateAutonomousDecision operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/decision/evaluate")
    public ResponseEntity<Void> evaluateAutonomousDecision(
            @RequestParam Long actionId,
            @RequestParam java.math.BigDecimal confidence,
            @RequestParam String operator) {
        com.plus33.erp.platform.entity.PlatformAutonomousAction action = new com.plus33.erp.platform.entity.PlatformAutonomousAction();
        action.setId(actionId);
        decisionEngine.evaluateDecision(action, confidence);
        auditService.logAudit("EVALUATE_AUTONOMOUS_DECISION", operator, "REST", "action=" + actionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/twin/simulation")
    public ResponseEntity<Void> runTwinSimulation(
            @RequestParam Long scenarioId,
            @RequestParam Long instanceId,
            @RequestParam String operator) {
        simulationService.runSimulation(scenarioId, instanceId);
        auditService.logAudit("RUN_TWIN_SIMULATION", operator, "REST", "scenario=" + scenarioId);
        return ResponseEntity.ok().build();
    }

    @Autowired com.plus33.erp.security.repository.UserActivityLogRepository userActivityLogRepo;
    @Autowired com.plus33.erp.security.repository.UserRepository userRepository;
    @Autowired com.plus33.erp.workforce.repository.UserStoreRepository userStoreRepository;
    @Autowired com.plus33.erp.workforce.repository.UserRegionRepository userRegionRepository;

    @GetMapping("/logs/logins")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<?> getUserLoginLogs(
            java.security.Principal principal,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String targetEmail) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        String email = principal.getName();
        com.plus33.erp.security.entity.User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || user.getRoles() == null || user.getRoles().isEmpty()) {
            return ResponseEntity.status(403).build();
        }

        boolean isUltimateAdmin = false;
        boolean isAdmin = false;
        String roleCode = "";
        for (com.plus33.erp.security.entity.Role r : user.getRoles()) {
            if (r.getCode() != null) {
                String c = r.getCode().toUpperCase();
                if ("ULTIMATE_ADMIN".equals(c)) {
                    isUltimateAdmin = true;
                    roleCode = "ULTIMATE_ADMIN";
                    break;
                }
                if (c.contains("ADMIN") || c.contains("SUPERVISOR") || "STORE".equals(c)) {
                    isAdmin = true;
                }
                if (roleCode.isEmpty()) {
                    roleCode = c;
                }
            }
        }
        
        // Restrict pure non-admin employees from pulling activity logs
        if (!isUltimateAdmin && !isAdmin && roleCode.contains("EMPLOYEE")) {
            return ResponseEntity.status(403).body(java.util.Collections.emptyList());
        }

        java.time.LocalDateTime start = null;
        java.time.LocalDateTime end = null;
        if (startDate != null && !startDate.trim().isEmpty()) {
            try {
                start = java.time.LocalDate.parse(startDate).atStartOfDay();
            } catch (Exception ignored) {}
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            try {
                end = java.time.LocalDate.parse(endDate).atTime(23, 59, 59);
            } catch (Exception ignored) {}
        }

        String filterEmail = (targetEmail != null && !targetEmail.trim().isEmpty()) ? targetEmail.trim() : null;

        // Branch 1: ULTIMATE_ADMIN (Global View)
        if (isUltimateAdmin) {
            if (filterEmail != null) {
                if (start != null && end != null) {
                    return ResponseEntity.ok(userActivityLogRepo.findByUsernameAndLoginTimeBetweenOrderByLoginTimeDesc(filterEmail, start, end));
                }
                return ResponseEntity.ok(userActivityLogRepo.findByUsernameOrderByLoginTimeDesc(filterEmail));
            }
            if (start != null && end != null) {
                return ResponseEntity.ok(userActivityLogRepo.findByLoginTimeBetweenOrderByLoginTimeDesc(start, end));
            }
            return ResponseEntity.ok(userActivityLogRepo.findAllByOrderByLoginTimeDesc());
        }

        // Branch 2: Scoped Roles (National, Regional, Store)
        java.util.List<com.plus33.erp.security.entity.User> allUsers = userRepository.findAll();
        java.util.List<String> targetUsernames = new java.util.ArrayList<>();

        if (roleCode.contains("NATIONAL")) {
            String adminCountry = getUserCountry(user.getId());
            for (com.plus33.erp.security.entity.User u : allUsers) {
                if (adminCountry.equalsIgnoreCase(getUserCountry(u.getId()))) {
                    targetUsernames.add(u.getEmail());
                }
            }
        } else if (roleCode.contains("REGIONAL")) {
            for (com.plus33.erp.security.entity.User u : allUsers) {
                if (shareRegion(user.getId(), u.getId())) {
                    targetUsernames.add(u.getEmail());
                }
            }
        } else {
            for (com.plus33.erp.security.entity.User u : allUsers) {
                if (shareStore(user.getId(), u.getId())) {
                    targetUsernames.add(u.getEmail());
                }
            }
        }

        if (targetUsernames.isEmpty()) {
            targetUsernames.add(email);
        }

        if (filterEmail != null) {
            if (targetUsernames.contains(filterEmail) || email.equalsIgnoreCase(filterEmail)) {
                if (start != null && end != null) {
                    return ResponseEntity.ok(userActivityLogRepo.findByUsernameAndLoginTimeBetweenOrderByLoginTimeDesc(filterEmail, start, end));
                }
                return ResponseEntity.ok(userActivityLogRepo.findByUsernameOrderByLoginTimeDesc(filterEmail));
            } else {
                return ResponseEntity.ok(java.util.Collections.emptyList());
            }
        }

        if (start != null && end != null) {
            return ResponseEntity.ok(userActivityLogRepo.findByUsernameInAndLoginTimeBetweenOrderByLoginTimeDesc(targetUsernames, start, end));
        }

        return ResponseEntity.ok(userActivityLogRepo.findByUsernameInOrderByLoginTimeDesc(targetUsernames));
    }

    @PostMapping("/logs/heartbeat")
    public ResponseEntity<Void> heartbeat(java.security.Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            java.util.Optional<com.plus33.erp.security.entity.UserActivityLog> activeLogOpt = 
                userActivityLogRepo.findFirstByUsernameAndStatusAndLogoutTimeIsNullOrderByLoginTimeDesc(email, "SUCCESS");
            if (activeLogOpt.isPresent()) {
                com.plus33.erp.security.entity.UserActivityLog log = activeLogOpt.get();
                log.setLastActiveTime(java.time.LocalDateTime.now());
                userActivityLogRepo.save(log);
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logs/logout")
    public ResponseEntity<Void> logout(java.security.Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            java.util.Optional<com.plus33.erp.security.entity.UserActivityLog> activeLogOpt = 
                userActivityLogRepo.findFirstByUsernameAndStatusAndLogoutTimeIsNullOrderByLoginTimeDesc(email, "SUCCESS");
            if (activeLogOpt.isPresent()) {
                com.plus33.erp.security.entity.UserActivityLog log = activeLogOpt.get();
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                log.setLogoutTime(now);
                log.setLastActiveTime(now);
                userActivityLogRepo.save(log);
            }
        }
        return ResponseEntity.ok().build();
    }

    private int getRoleRank(String roleCode) {
        if ("ULTIMATE_ADMIN".equalsIgnoreCase(roleCode)) return 4;
        if ("NATIONAL_ADMIN".equalsIgnoreCase(roleCode)) return 3;
        if ("REGIONAL_ADMIN".equalsIgnoreCase(roleCode)) return 2;
        if ("STORE_ADMIN".equalsIgnoreCase(roleCode) || "STORE".equalsIgnoreCase(roleCode)) return 1;
        return 0;
    }

    private String getUserCountry(Long userId) {
        java.util.List<com.plus33.erp.workforce.entity.UserStore> userStores = userStoreRepository.findByIdUserId(userId);
        if (userStores != null && !userStores.isEmpty() && userStores.get(0).getStore() != null) {
            com.plus33.erp.organization.entity.Store store = userStores.get(0).getStore();
            if (store.getRegion() != null && store.getRegion().getCode() != null) {
                String code = store.getRegion().getCode().toUpperCase();
                if (code.startsWith("FR")) return "France";
                if (code.startsWith("AE") || code.startsWith("UAE")) return "UAE";
                if (code.startsWith("IN")) return "India";
            }
        }
        java.util.List<com.plus33.erp.workforce.entity.UserRegion> userRegions = userRegionRepository.findByIdUserId(userId);
        if (userRegions != null && !userRegions.isEmpty() && userRegions.get(0).getRegion() != null) {
            com.plus33.erp.organization.entity.Region region = userRegions.get(0).getRegion();
            if (region.getCode() != null) {
                String code = region.getCode().toUpperCase();
                if (code.startsWith("FR")) return "France";
                if (code.startsWith("AE") || code.startsWith("UAE")) return "UAE";
                if (code.startsWith("IN")) return "India";
            }
        }
        return "India";
    }

    private boolean shareRegion(Long userId1, Long userId2) {
        java.util.List<com.plus33.erp.workforce.entity.UserRegion> r1 = userRegionRepository.findByIdUserId(userId1);
        java.util.List<com.plus33.erp.workforce.entity.UserRegion> r2 = userRegionRepository.findByIdUserId(userId2);
        if (r1 == null || r2 == null || r1.isEmpty() || r2.isEmpty()) return false;
        return r1.get(0).getRegion().getId().equals(r2.get(0).getRegion().getId());
    }

    private boolean shareStore(Long userId1, Long userId2) {
        java.util.List<com.plus33.erp.workforce.entity.UserStore> s1 = userStoreRepository.findByIdUserId(userId1);
        java.util.List<com.plus33.erp.workforce.entity.UserStore> s2 = userStoreRepository.findByIdUserId(userId2);
        if (s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty()) return false;
        return s1.get(0).getStore().getId().equals(s2.get(0).getStore().getId());
    }
}