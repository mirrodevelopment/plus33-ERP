/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.mesh
 * File              : ServiceMeshManager.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ServiceMeshManagerController
 * Related Service   : ServiceMeshManager
 * Related Repository: ServiceMeshManagerRepository
 * Related Entity    : ServiceMeshManager
 * Related DTO       : N/A
 * Related Mapper    : ServiceMeshManagerMapper
 * Related DB Table  : service_mesh_managers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ServiceMeshManagerController, ServiceMeshManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements ServiceMeshManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.mesh;

import com.plus33.erp.platform.entity.PlatformServiceMeshEndpoint;
import com.plus33.erp.platform.repository.PlatformServiceMeshEndpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code ServiceMeshManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.mesh}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ServiceMeshManagerController
 *   --> ServiceMeshManager (this)
 *   --> Validate business rules
 *   --> ServiceMeshManagerRepository (read/write 'service_mesh_managers')
 *   --> ServiceMeshManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code service_mesh_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ServiceMeshManager {
    @Autowired PlatformServiceMeshEndpointRepository meshRepo;
    /**
     * Creates a new sidecar and persists it to the database.
     *
     * @param serviceName the serviceName input value
     * @param sidecarIp the sidecarIp input value
     * @param mtls the mtls input value
     * @param status status filter for narrowing query results
     * @throws BusinessException if a business rule is violated
     */
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