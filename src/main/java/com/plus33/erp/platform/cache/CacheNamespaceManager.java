package com.plus33.erp.platform.cache;

import com.plus33.erp.platform.entity.PlatformCacheNamespace;
import com.plus33.erp.platform.repository.PlatformCacheNamespaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CacheNamespaceManager {
    @Autowired PlatformCacheNamespaceRepository nsRepo;

    @Transactional
    public void createNamespace(String name, int ttl, String policy) {
        PlatformCacheNamespace ns = new PlatformCacheNamespace();
        ns.setNamespaceName(name);
        ns.setTtlSeconds(ttl);
        ns.setEvictionPolicy(policy);
        nsRepo.save(ns);
    }
}