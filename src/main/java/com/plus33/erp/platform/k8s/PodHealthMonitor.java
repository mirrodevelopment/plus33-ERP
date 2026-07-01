package com.plus33.erp.platform.k8s;

import com.plus33.erp.platform.entity.PlatformK8sPodStatus;
import com.plus33.erp.platform.repository.PlatformK8sPodStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class PodHealthMonitor {
    @Autowired PlatformK8sPodStatusRepository podRepo;

    @Transactional
    public void updatePodState(String name, String ns, String status, String nodeIp) {
        PlatformK8sPodStatus pod = podRepo.findAll().stream()
                .filter(p -> p.getPodName().equals(name) && p.getNamespace().equals(ns))
                .findFirst()
                .orElseGet(() -> {
                    PlatformK8sPodStatus newPod = new PlatformK8sPodStatus();
                    newPod.setPodName(name);
                    newPod.setNamespace(ns);
                    return newPod;
                });

        pod.setStatus(status);
        pod.setNodeIp(nodeIp);
        if ("FAILED".equals(status)) {
            pod.setRestarts(pod.getRestarts() + 1);
        }
        pod.setUpdatedAt(LocalDateTime.now());
        podRepo.save(pod);
    }
}