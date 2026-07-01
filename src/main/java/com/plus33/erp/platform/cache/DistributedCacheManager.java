package com.plus33.erp.platform.cache;

import com.plus33.erp.platform.entity.PlatformCacheInvalidationLog;
import com.plus33.erp.platform.repository.PlatformCacheInvalidationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class DistributedCacheManager {
    @Autowired PlatformCacheInvalidationLogRepository invalidationRepo;
    private final Map<String, String> cacheStore = new HashMap<>();

    public String get(String namespace, String key) {
        return cacheStore.get(namespace + ":" + key);
    }

    @Transactional
    public void put(String namespace, String key, String value) {
        cacheStore.put(namespace + ":" + key, value);
    }

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