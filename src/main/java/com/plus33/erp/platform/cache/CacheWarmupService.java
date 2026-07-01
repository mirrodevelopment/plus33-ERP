package com.plus33.erp.platform.cache;

import com.plus33.erp.platform.entity.PlatformCacheWarmupJob;
import com.plus33.erp.platform.repository.PlatformCacheWarmupJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class CacheWarmupService {
    @Autowired PlatformCacheWarmupJobRepository jobRepo;

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