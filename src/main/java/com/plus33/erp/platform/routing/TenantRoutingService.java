/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.routing
 * File              : TenantRoutingService.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TenantRoutingController
 * Related Service   : TenantRoutingService
 * Related Repository: TenantRoutingRepository
 * Related Entity    : TenantRouting
 * Related DTO       : N/A
 * Related Mapper    : TenantRoutingMapper
 * Related DB Table  : tenant_routings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TenantRoutingController, TenantRoutingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements TenantRoutingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.routing;

import com.plus33.erp.platform.entity.PlatformTenantRouting;
import com.plus33.erp.platform.repository.PlatformTenantRoutingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code TenantRoutingService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.routing}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * TenantRoutingController
 *   --> TenantRoutingService (this)
 *   --> Validate business rules
 *   --> TenantRoutingRepository (read/write 'tenant_routings')
 *   --> TenantRoutingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code tenant_routings}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class TenantRoutingService {
    @Autowired PlatformTenantRoutingRepository routingRepo;
    /**
     * Configures the routing bean and registers it in the Spring ApplicationContext.
     *
     * @param tenantId the tenantId input value
     * @param region the region input value
     * @param policy the policy input value
     * @param replicaUrl the replicaUrl input value
     */
    @Transactional
    public void configureRouting(String tenantId, String region, String policy, String replicaUrl) {
        PlatformTenantRouting route = routingRepo.findAll().stream()
                .filter(r -> r.getTenantId().equals(tenantId))
                .findFirst()
                .orElseGet(() -> {
                    PlatformTenantRouting newRoute = new PlatformTenantRouting();
                    newRoute.setTenantId(tenantId);
                    return newRoute;
                });

        route.setRegion(region);
        route.setRoutingPolicy(policy);
        route.setReplicaUrl(replicaUrl);
        route.setHealthy(true);
        route.setUpdatedAt(LocalDateTime.now());
        routingRepo.save(route);
    }

    /**
     * Performs the resolveTargetReplica operation in this module.
     *
     * @param tenantId the tenantId input value
     * @return the result string value
     */
    public String resolveTargetReplica(String tenantId) {
        return routingRepo.findAll().stream()
                .filter(r -> r.getTenantId().equals(tenantId))
                .filter(PlatformTenantRouting::getHealthy)
                .map(PlatformTenantRouting::getReplicaUrl)
                .findFirst()
                .orElse("http://localhost:5432/primary");
    }
}