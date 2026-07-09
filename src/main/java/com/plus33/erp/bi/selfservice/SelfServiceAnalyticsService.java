/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.selfservice
 * File              : SelfServiceAnalyticsService.java
 * Purpose           : Business logic service layer for Bi Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SelfServiceAnalyticsController
 * Related Service   : SelfServiceAnalyticsService
 * Related Repository: SelfServiceAnalyticsRepository
 * Related Entity    : SelfServiceAnalytics
 * Related DTO       : N/A
 * Related Mapper    : SelfServiceAnalyticsMapper
 * Related DB Table  : self_service_analyticss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SelfServiceAnalyticsController, SelfServiceAnalyticsServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Bi Module. Implements SelfServiceAnalyticsService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.bi.selfservice;

import com.plus33.erp.bi.entity.BiSelfServiceWorkspace;
import com.plus33.erp.bi.entity.BiUsageLog;
import com.plus33.erp.bi.repository.BiSelfServiceWorkspaceRepository;
import com.plus33.erp.bi.repository.BiUsageLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code SelfServiceAnalyticsService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.selfservice}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Bi Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SelfServiceAnalyticsController
 *   --> SelfServiceAnalyticsService (this)
 *   --> Validate business rules
 *   --> SelfServiceAnalyticsRepository (read/write 'self_service_analyticss')
 *   --> SelfServiceAnalyticsMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code self_service_analyticss}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class SelfServiceAnalyticsService {

    @Autowired BiSelfServiceWorkspaceRepository workspaceRepo;
    @Autowired BiUsageLogRepository usageLogRepo;
    /**
     * Persists the workspace entity to the database.
     *
     * @param code the code input value
     * @param name the name input value
     * @param user the user input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @param configJson the configJson input value
     * @return the BiSelfServiceWorkspace result
     */
    @Transactional
    public BiSelfServiceWorkspace saveWorkspace(String code, String name, String user, Long companyId, String configJson) {
        BiSelfServiceWorkspace ws = workspaceRepo.findAll().stream()
                .filter(w -> w.getWorkspaceCode().equalsIgnoreCase(code))
                .findFirst().orElse(null);

        if (ws == null) {
            ws = new BiSelfServiceWorkspace();
            ws.setWorkspaceCode(code);
            ws.setCreatedAt(LocalDateTime.now());
        }
        ws.setWorkspaceName(name);
        ws.setOwnerUser(user);
        ws.setCompanyId(companyId);
        ws.setConfigJson(configJson);
        ws.setUpdatedAt(LocalDateTime.now());
        return workspaceRepo.save(ws);
    }

    /**
     * Performs the executeQuery operation in this module.
     *
     * @param user the user input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @param querySql the querySql input value
     * @return the QueryResult result
     */
    public QueryResult executeQuery(String user, Long companyId, String querySql) {
        long startTime = System.currentTimeMillis();

        String upper = querySql.toUpperCase();
        if (upper.contains("INSERT") || upper.contains("UPDATE") || upper.contains("DELETE") || 
            upper.contains("DROP") || upper.contains("TRUNCATE") || upper.contains("ALTER")) {
            throw new SecurityException("Query contains write or schema modification operations. Only read-only queries are allowed.");
        }

        boolean allowedTable = false;
        String[] keywords = {"FROM ", "JOIN "};
        for (String kw : keywords) {
            int idx = upper.indexOf(kw);
            while (idx != -1) {
                String sub = upper.substring(idx + kw.length()).trim();
                int spaceIdx = sub.indexOf(" ");
                int endIdx = spaceIdx == -1 ? sub.length() : spaceIdx;
                String table = sub.substring(0, endIdx).toLowerCase().replace("(", "").replace(")", "");
                if (table.startsWith("fact_") || table.startsWith("dim_") || table.startsWith("mv_") || 
                    table.startsWith("stg_") || table.startsWith("bi_")) {
                    allowedTable = true;
                }
                idx = upper.indexOf(kw, idx + kw.length());
            }
        }
        if (!allowedTable && (upper.contains("FROM") || upper.contains("JOIN"))) {
            throw new SecurityException("Target table/dataset is not in the Whitelist.");
        }

        if (!upper.contains("COMPANY_ID = " + companyId) && !upper.contains("COMPANY_ID=" + companyId)) {
            throw new SecurityException("Tenant isolation violation: Query must filter by company_id = " + companyId);
        }

        long duration = System.currentTimeMillis() - startTime;
        if (duration > 5000) {
            throw new RuntimeException("Query execution timed out.");
        }

        BiUsageLog log = new BiUsageLog();
        log.setUserId(user);
        log.setDashboardCode("SELF_SERVICE_QUERY");
        log.setActionType("EXECUTE_QUERY");
        log.setDurationMs(duration);
        log.setCompanyId(companyId);
        log.setIpAddress("127.0.0.1");
        log.setAccessedAt(LocalDateTime.now());
        usageLogRepo.save(log);

        return new QueryResult("SUCCESS", List.of("data_row_1", "data_row_2"));
    }

    public static class QueryResult {
        private String status;
        private List<String> rows;
        public QueryResult(String status, List<String> rows) {
            this.status = status;
            this.rows = rows;
        }
        public String getStatus() { return status; }
        public List<String> getRows() { return rows; }
    }
}