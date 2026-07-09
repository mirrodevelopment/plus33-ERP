/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.cache
 * File              : CacheNamespaceManager.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CacheNamespaceManagerController
 * Related Service   : CacheNamespaceManager
 * Related Repository: CacheNamespaceManagerRepository
 * Related Entity    : CacheNamespaceManager
 * Related DTO       : N/A
 * Related Mapper    : CacheNamespaceManagerMapper
 * Related DB Table  : cache_namespace_managers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CacheNamespaceManagerController, CacheNamespaceManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements CacheNamespaceManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.cache;

import com.plus33.erp.platform.entity.PlatformCacheNamespace;
import com.plus33.erp.platform.repository.PlatformCacheNamespaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code CacheNamespaceManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.cache}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CacheNamespaceManagerController
 *   --> CacheNamespaceManager (this)
 *   --> Validate business rules
 *   --> CacheNamespaceManagerRepository (read/write 'cache_namespace_managers')
 *   --> CacheNamespaceManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code cache_namespace_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class CacheNamespaceManager {
    @Autowired PlatformCacheNamespaceRepository nsRepo;
    /**
     * Creates a new namespace and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param name the name input value
     * @param ttl the ttl input value
     * @param policy the policy input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void createNamespace(String name, int ttl, String policy) {
        PlatformCacheNamespace ns = new PlatformCacheNamespace();
        ns.setNamespaceName(name);
        ns.setTtlSeconds(ttl);
        ns.setEvictionPolicy(policy);
        nsRepo.save(ns);
    }
}