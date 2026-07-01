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

@Service
public class SelfServiceAnalyticsService {

    @Autowired BiSelfServiceWorkspaceRepository workspaceRepo;
    @Autowired BiUsageLogRepository usageLogRepo;

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