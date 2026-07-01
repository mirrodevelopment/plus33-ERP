package com.plus33.erp.integration.service;

import com.plus33.erp.integration.entity.IntegrationLock;
import com.plus33.erp.integration.repository.IntegrationLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class IntegrationLockCoordinator {
    @Autowired IntegrationLockRepository lockRepo;

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

    @Transactional
    public void releaseLock(String lockKey, String ownerId) {
        IntegrationLock lock = lockRepo.findById(lockKey).orElse(null);
        if (lock != null && lock.getOwnerId().equals(ownerId)) {
            lockRepo.delete(lock);
        }
    }
}
