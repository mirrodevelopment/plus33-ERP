/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.service
 * File              : IntegrationLockCoordinator.java
 * Purpose           : Business logic service layer for Integration Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationLockCoordinatorController
 * Related Service   : IntegrationLockCoordinator
 * Related Repository: IntegrationLockCoordinatorRepository
 * Related Entity    : IntegrationLockCoordinator
 * Related DTO       : N/A
 * Related Mapper    : IntegrationLockCoordinatorMapper
 * Related DB Table  : integration_lock_coordinators
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationLockCoordinatorController, IntegrationLockCoordinatorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Integration Module. Implements IntegrationLockCoordinatorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.integration.service;

import com.plus33.erp.integration.entity.IntegrationLock;
import com.plus33.erp.integration.repository.IntegrationLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationLockCoordinator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Integration Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * IntegrationLockCoordinatorController
 *   --> IntegrationLockCoordinator (this)
 *   --> Validate business rules
 *   --> IntegrationLockCoordinatorRepository (read/write 'integration_lock_coordinators')
 *   --> IntegrationLockCoordinatorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code integration_lock_coordinators}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class IntegrationLockCoordinator {
    @Autowired IntegrationLockRepository lockRepo;
    /**
     * Performs the acquireLock operation in this module.
     *
     * @param lockKey the lockKey input value
     * @param ownerId the ownerId input value
     * @param durationSeconds the durationSeconds input value
     * @return true if operation succeeded, false otherwise
     */
    @Transactional
    public boolean acquireLock(String lockKey, String ownerId, long durationSeconds) {
        IntegrationLock lock = lockRepo.findById(lockKey).orElse(null);
        LocalDateTime now = LocalDateTime.now();
        if (lock != null) {
            if (now.isAfter(lock.getExpiresAt())) {
                lock.setOwnerId(ownerId);
                lock.setExpiresAt(now.plusSeconds(durationSeconds));
                lockRepo.save(lock);
                return true;
            }
            return lock.getOwnerId().equals(ownerId);
        } else {
            lock = new IntegrationLock();
            lock.setLockKey(lockKey);
            lock.setOwnerId(ownerId);
            lock.setExpiresAt(now.plusSeconds(durationSeconds));
            lockRepo.save(lock);
            return true;
        }
    }

    /**
     * Releases previously reserved lock resources back to the available pool.
     *
     * @param lockKey the lockKey input value
     * @param ownerId the ownerId input value
     */
    @Transactional
    public void releaseLock(String lockKey, String ownerId) {
        IntegrationLock lock = lockRepo.findById(lockKey).orElse(null);
        if (lock != null && lock.getOwnerId().equals(ownerId)) {
            lockRepo.delete(lock);
        }
    }
}