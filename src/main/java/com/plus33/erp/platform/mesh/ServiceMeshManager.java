package com.plus33.erp.platform.mesh;

import com.plus33.erp.platform.entity.PlatformServiceMeshEndpoint;
import com.plus33.erp.platform.repository.PlatformServiceMeshEndpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceMeshManager {
    @Autowired PlatformServiceMeshEndpointRepository meshRepo;

    @Transactional
    public void registerSidecar(String serviceName, String sidecarIp, boolean mtls, String status) {
        PlatformServiceMeshEndpoint ep = meshRepo.findAll().stream()
                .filter(e -> e.getServiceName().equals(serviceName))
                .findFirst()
                .orElseGet(() -> {
                    PlatformServiceMeshEndpoint newEp = new PlatformServiceMeshEndpoint();
                    newEp.setServiceName(serviceName);
                    return newEp;
                });

        ep.setSidecarProxyIp(sidecarIp);
        ep.setMtlsEnabled(mtls);
        ep.setProxyStatus(status);
        meshRepo.save(ep);
    }
}