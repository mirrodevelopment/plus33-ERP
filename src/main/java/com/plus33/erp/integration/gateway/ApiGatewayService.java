package com.plus33.erp.integration.gateway;

import com.plus33.erp.integration.entity.IntegrationGatewayKey;
import com.plus33.erp.integration.entity.IntegrationGatewayUsageLog;
import com.plus33.erp.integration.repository.IntegrationGatewayKeyRepository;
import com.plus33.erp.integration.repository.IntegrationGatewayUsageLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApiGatewayService {
    @Autowired IntegrationGatewayKeyRepository gatewayKeyRepo;
    @Autowired IntegrationGatewayUsageLogRepository usageLogRepo;

    @Transactional
    public boolean authorizeAndRateLimit(String apiKey, String path, String method) {
        IntegrationGatewayKey key = gatewayKeyRepo.findAll().stream()
                .filter(k -> k.getApiKey().equals(apiKey) && k.getActive())
                .findFirst().orElse(null);

        if (key == null) {
            return false;
        }

        if (!"*".equals(key.getAllowedRoutes()) && !path.startsWith(key.getAllowedRoutes())) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        List<IntegrationGatewayUsageLog> logs = usageLogRepo.findAll().stream()
                .filter(l -> l.getApiKey().equals(apiKey))
                .toList();

        long oneMinuteAgoCalls = logs.stream()
                .filter(l -> l.getCalledAt().isAfter(now.minusMinutes(1)))
                .count();
        if (oneMinuteAgoCalls >= key.getRateLimitPerMin()) {
            logGatewayCall(apiKey, path, method, 429, 0L);
            return false;
        }

        long todayCalls = logs.stream()
                .filter(l -> l.getCalledAt().toLocalDate().isEqual(now.toLocalDate()))
                .count();
        if (todayCalls >= key.getQuotaPerDay()) {
            logGatewayCall(apiKey, path, method, 403, 0L);
            return false;
        }

        logGatewayCall(apiKey, path, method, 200, 10L);
        return true;
    }

    private void logGatewayCall(String apiKey, String path, String method, int statusCode, long responseTimeMs) {
        IntegrationGatewayUsageLog log = new IntegrationGatewayUsageLog();
        log.setApiKey(apiKey);
        log.setRequestPath(path);
        log.setHttpMethod(method);
        log.setStatusCode(statusCode);
        log.setResponseTimeMs(responseTimeMs);
        log.setCalledAt(LocalDateTime.now());
        usageLogRepo.save(log);
    }
}
