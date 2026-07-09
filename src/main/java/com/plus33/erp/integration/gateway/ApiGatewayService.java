/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.gateway
 * File              : ApiGatewayService.java
 * Purpose           : Business logic service layer for Integration Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ApiGatewayController
 * Related Service   : ApiGatewayService
 * Related Repository: ApiGatewayRepository
 * Related Entity    : ApiGateway
 * Related DTO       : N/A
 * Related Mapper    : ApiGatewayMapper
 * Related DB Table  : api_gateways
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ApiGatewayController, ApiGatewayServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Integration Module. Implements ApiGatewayService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code ApiGatewayService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.gateway}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Integration Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ApiGatewayController
 *   --> ApiGatewayService (this)
 *   --> Validate business rules
 *   --> ApiGatewayRepository (read/write 'api_gateways')
 *   --> ApiGatewayMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code api_gateways}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ApiGatewayService {
    @Autowired IntegrationGatewayKeyRepository gatewayKeyRepo;
    @Autowired IntegrationGatewayUsageLogRepository usageLogRepo;
    /**
     * Performs the authorizeAndRateLimit operation in this module.
     *
     * @param apiKey the apiKey input value
     * @param path the path input value
     * @param method the method input value
     * @return true if operation succeeded, false otherwise
     */
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