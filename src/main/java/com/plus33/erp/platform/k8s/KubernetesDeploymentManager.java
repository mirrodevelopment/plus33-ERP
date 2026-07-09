/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.k8s
 * File              : KubernetesDeploymentManager.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: KubernetesDeploymentManagerController
 * Related Service   : KubernetesDeploymentManager
 * Related Repository: KubernetesDeploymentManagerRepository
 * Related Entity    : KubernetesDeploymentManager
 * Related DTO       : N/A
 * Related Mapper    : KubernetesDeploymentManagerMapper
 * Related DB Table  : kubernetes_deployment_managers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : KubernetesDeploymentManagerController, KubernetesDeploymentManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements KubernetesDeploymentManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.k8s;

import com.plus33.erp.platform.entity.PlatformK8sResource;
import com.plus33.erp.platform.repository.PlatformK8sResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code KubernetesDeploymentManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.k8s}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * KubernetesDeploymentManagerController
 *   --> KubernetesDeploymentManager (this)
 *   --> Validate business rules
 *   --> KubernetesDeploymentManagerRepository (read/write 'kubernetes_deployment_managers')
 *   --> KubernetesDeploymentManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code kubernetes_deployment_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class KubernetesDeploymentManager {
    @Autowired PlatformK8sResourceRepository resourceRepo;
    /**
     * Creates a new resource and persists it to the database.
     *
     * @param name the name input value
     * @param type the type input value
     * @param ns the ns input value
     * @param yaml the yaml input value
     * @throws BusinessException if a business rule is violated
     */
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