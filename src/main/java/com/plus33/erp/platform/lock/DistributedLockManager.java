package com.plus33.erp.platform.lock;

import com.plus33.erp.platform.entity.PlatformDistributedLock;
import com.plus33.erp.platform.repository.PlatformDistributedLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DistributedLockManager {
    @Autowired PlatformDistributedLockRepository lockRepo;

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

    @Transactional
    public void releaseLock(String lockName, String ownerNode) {
        lockRepo.findAll().stream()
                .filter(l -> l.getLockName().equals(lockName) && l.getOwnerNode().equals(ownerNode))
                .findFirst()
                .ifPresent(lockRepo::delete);
    }
}