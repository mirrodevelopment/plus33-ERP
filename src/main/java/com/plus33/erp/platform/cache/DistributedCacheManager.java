/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.cache
 * File              : DistributedCacheManager.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DistributedCacheManagerController
 * Related Service   : DistributedCacheManager
 * Related Repository: DistributedCacheManagerRepository
 * Related Entity    : DistributedCacheManager
 * Related DTO       : N/A
 * Related Mapper    : DistributedCacheManagerMapper
 * Related DB Table  : distributed_cache_managers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DistributedCacheManagerController, DistributedCacheManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements DistributedCacheManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.cache;

import com.plus33.erp.platform.entity.PlatformCacheInvalidationLog;
import com.plus33.erp.platform.repository.PlatformCacheInvalidationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code DistributedCacheManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.cache}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DistributedCacheManagerController
 *   --> DistributedCacheManager (this)
 *   --> Validate business rules
 *   --> DistributedCacheManagerRepository (read/write 'distributed_cache_managers')
 *   --> DistributedCacheManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code distributed_cache_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DistributedCacheManager {
    @Autowired PlatformCacheInvalidationLogRepository invalidationRepo;
    private final Map<String, String> cacheStore = new HashMap<>();

    /**
     * Retrieves platform data from the database.
     *
     * @param namespace the namespace input value
     * @param key the key input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String get(String namespace, String key) {
        return cacheStore.get(namespace + ":" + key);
    }

    /**
     * Performs the put operation in this module.
     *
     * @param namespace the namespace input value
     * @param key the key input value
     * @param value the value input value
     */
    @Transactional
    public void put(String namespace, String key, String value) {
        cacheStore.put(namespace + ":" + key, value);
    }

    /**
     * Performs the evict operation in this module.
     *
     * @param namespace the namespace input value
     * @param key the key input value
     */
    @Transactional
    public void evict(String namespace, String key) {
        cacheStore.remove(namespace + ":" + key);
        PlatformCacheInvalidationLog log = new PlatformCacheInvalidationLog();
        log.setNamespaceName(namespace);
        log.setCacheKey(key);
        log.setInvalidatedAt(LocalDateTime.now());
        invalidationRepo.save(log);
    }
}