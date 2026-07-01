package com.plus33.erp.intelligence.query;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class NaturalLanguageQueryService {
    @Autowired PlatformOperationalQueryLogRepository logRepo;
    @Autowired QueryPlanner queryPlanner;

    @Transactional
    public PlatformOperationalQueryLog executeQuery(String queryText) {
        String plan = queryPlanner.planQuery(queryText);

        PlatformOperationalQueryLog log = new PlatformOperationalQueryLog();
        log.setQueryText(queryText);
        log.setParsedIntent("QUERY_INTENT_DISCOVERY");
        log.setExecutionPlanJson(plan);
        log.setQueriedAt(LocalDateTime.now());
        return logRepo.save(log);
    }
}