package com.plus33.erp.platform.k8s;

import com.plus33.erp.platform.entity.PlatformK8sResource;
import com.plus33.erp.platform.repository.PlatformK8sResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class KubernetesDeploymentManager {
    @Autowired PlatformK8sResourceRepository resourceRepo;

    @Transactional
    public void registerResource(String name, String type, String ns, String yaml) {
        PlatformK8sResource res = resourceRepo.findAll().stream()
                .filter(r -> r.getResourceName().equals(name) && r.getResourceType().equals(type) && r.getNamespace().equals(ns))
                .findFirst()
                .orElseGet(() -> {
                    PlatformK8sResource newRes = new PlatformK8sResource();
                    newRes.setResourceName(name);
                    newRes.setResourceType(type);
                    newRes.setNamespace(ns);
                    return newRes;
                });

        res.setManifestYaml(yaml);
        res.setUpdatedAt(LocalDateTime.now());
        resourceRepo.save(res);
    }
}