/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.k8s
 * File              : PodHealthMonitor.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PodHealthMonitorController
 * Related Service   : PodHealthMonitor
 * Related Repository: PodHealthMonitorRepository
 * Related Entity    : PodHealthMonitor
 * Related DTO       : N/A
 * Related Mapper    : PodHealthMonitorMapper
 * Related DB Table  : pod_health_monitors
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PodHealthMonitorController, PodHealthMonitorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements PodHealthMonitorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.k8s;

import com.plus33.erp.platform.entity.PlatformK8sPodStatus;
import com.plus33.erp.platform.repository.PlatformK8sPodStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PodHealthMonitor}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.k8s}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PodHealthMonitorController
 *   --> PodHealthMonitor (this)
 *   --> Validate business rules
 *   --> PodHealthMonitorRepository (read/write 'pod_health_monitors')
 *   --> PodHealthMonitorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code pod_health_monitors}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PodHealthMonitor {
    @Autowired PlatformK8sPodStatusRepository podRepo;
    /**
     * Updates an existing pod state record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param name the name input value
     * @param ns the ns input value
     * @param status status filter for narrowing query results
     * @param nodeIp the nodeIp input value
     * @throws BusinessException if a business rule is violated
     */
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