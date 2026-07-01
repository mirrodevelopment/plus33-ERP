package com.plus33.erp.platform.routing;

import com.plus33.erp.platform.entity.PlatformTenantRouting;
import com.plus33.erp.platform.repository.PlatformTenantRoutingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class TenantRoutingService {
    @Autowired PlatformTenantRoutingRepository routingRepo;

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

    public String resolveTargetReplica(String tenantId) {
        return routingRepo.findAll().stream()
                .filter(r -> r.getTenantId().equals(tenantId))
                .filter(PlatformTenantRouting::getHealthy)
                .map(PlatformTenantRouting::getReplicaUrl)
                .findFirst()
                .orElse("http://localhost:5432/primary");
    }
}