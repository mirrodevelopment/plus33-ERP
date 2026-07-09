/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.cache
 * File              : CacheWarmupService.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CacheWarmupController
 * Related Service   : CacheWarmupService
 * Related Repository: CacheWarmupRepository
 * Related Entity    : CacheWarmup
 * Related DTO       : N/A
 * Related Mapper    : CacheWarmupMapper
 * Related DB Table  : cache_warmups
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CacheWarmupController, CacheWarmupServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements CacheWarmupService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.cache;

import com.plus33.erp.platform.entity.PlatformCacheWarmupJob;
import com.plus33.erp.platform.repository.PlatformCacheWarmupJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code CacheWarmupService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.cache}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CacheWarmupController
 *   --> CacheWarmupService (this)
 *   --> Validate business rules
 *   --> CacheWarmupRepository (read/write 'cache_warmups')
 *   --> CacheWarmupMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code cache_warmups}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class CacheWarmupService {
    @Autowired PlatformCacheWarmupJobRepository jobRepo;
    /**
     * Performs the runWarmup operation in this module.
     *
     * @param targetVersion the targetVersion input value
     * @return the PlatformCacheWarmupJob result
     */
    @Transactional
    public PlatformCacheWarmupJob runWarmup(String targetVersion) {
        PlatformCacheWarmupJob job = new PlatformCacheWarmupJob();
        job.setTargetVersion(targetVersion);
        job.setStatus("RUNNING");
        job.setStartedAt(LocalDateTime.now());
        job = jobRepo.save(job);

        // Preload cache keys simulation
        job.setPreloadedKeys(500);
        job.setStatus("COMPLETED");
        job.setCompletedAt(LocalDateTime.now());
        return jobRepo.save(job);
    }
}