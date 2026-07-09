/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.lock
 * File              : DistributedLockManager.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DistributedLockManagerController
 * Related Service   : DistributedLockManager
 * Related Repository: DistributedLockManagerRepository
 * Related Entity    : DistributedLockManager
 * Related DTO       : N/A
 * Related Mapper    : DistributedLockManagerMapper
 * Related DB Table  : distributed_lock_managers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DistributedLockManagerController, DistributedLockManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements DistributedLockManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.lock;

import com.plus33.erp.platform.entity.PlatformDistributedLock;
import com.plus33.erp.platform.repository.PlatformDistributedLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code DistributedLockManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.lock}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DistributedLockManagerController
 *   --> DistributedLockManager (this)
 *   --> Validate business rules
 *   --> DistributedLockManagerRepository (read/write 'distributed_lock_managers')
 *   --> DistributedLockManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code distributed_lock_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DistributedLockManager {
    @Autowired PlatformDistributedLockRepository lockRepo;
    /**
     * Performs the acquireLock operation in this module.
     *
     * @param lockName the lockName input value
     * @param ownerNode the ownerNode input value
     * @param ttlSeconds the ttlSeconds input value
     * @return true if operation succeeded, false otherwise
     */
    @Transactional
    public boolean acquireLock(String lockName, String ownerNode, int ttlSeconds) {
        PlatformDistributedLock lock = lockRepo.findAll().stream()
                .filter(l -> l.getLockName().equals(lockName))
                .findFirst().orElse(null);

        LocalDateTime now = LocalDateTime.now();

        if (lock == null) {
            lock = new PlatformDistributedLock();
            lock.setLockName(lockName);
            lock.setOwnerNode(ownerNode);
            lock.setExpiresAt(now.plusSeconds(ttlSeconds));
            lock.setHeartbeat(now);
            lockRepo.save(lock);
            return true;
        } else if (lock.getExpiresAt().isBefore(now)) {
            lock.setOwnerNode(ownerNode);
            lock.setExpiresAt(now.plusSeconds(ttlSeconds));
            lock.setHeartbeat(now);
            lockRepo.save(lock);
            return true;
        }

        return false;
    }

    /**
     * Releases previously reserved lock resources back to the available pool.
     *
     * @param lockName the lockName input value
     * @param ownerNode the ownerNode input value
     */
    @Transactional
    public void releaseLock(String lockName, String ownerNode) {
        lockRepo.findAll().stream()
                .filter(l -> l.getLockName().equals(lockName) && l.getOwnerNode().equals(ownerNode))
                .findFirst()
                .ifPresent(lockRepo::delete);
    }
}