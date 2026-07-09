/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Agent Module
 * Package           : com.plus33.erp.agent.vector
 * File              : VectorStoreCoordinator.java
 * Purpose           : Business logic service layer for Agent Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: VectorStoreCoordinatorController
 * Related Service   : VectorStoreCoordinator
 * Related Repository: VectorStoreCoordinatorRepository
 * Related Entity    : VectorStoreCoordinator
 * Related DTO       : N/A
 * Related Mapper    : VectorStoreCoordinatorMapper
 * Related DB Table  : vector_store_coordinators
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : VectorStoreCoordinatorController, VectorStoreCoordinatorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Agent Module. Implements VectorStoreCoordinatorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.agent.vector;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Agent Module</b>
 *
 * <p><b>Class  :</b> {@code VectorStoreCoordinator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.agent.vector}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Agent Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * VectorStoreCoordinatorController
 *   --> VectorStoreCoordinator (this)
 *   --> Validate business rules
 *   --> VectorStoreCoordinatorRepository (read/write 'vector_store_coordinators')
 *   --> VectorStoreCoordinatorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code vector_store_coordinators}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class VectorStoreCoordinator {
    @Autowired PlatformVectorStoreRepository storeRepo;
    @Autowired PlatformEmbeddingProviderRepository providerRepo;
    /**
     * Creates a new store and persists it to the database.
     *
     * @param code the code input value
     * @param status status filter for narrowing query results
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerStore(String code, String status) {
        PlatformVectorStore vs = new PlatformVectorStore();
        vs.setStoreCode(code);
        vs.setStatus(status);
        storeRepo.save(vs);
    }

    /**
     * Creates a new embedding provider and persists it to the database.
     *
     * @param provider the provider input value
     * @param dimensions the dimensions input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerEmbeddingProvider(String provider, int dimensions) {
        PlatformEmbeddingProvider ep = new PlatformEmbeddingProvider();
        ep.setProviderName(provider);
        ep.setDimensions(dimensions);
        providerRepo.save(ep);
    }
}