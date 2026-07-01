package com.plus33.erp.intelligence.semantic;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class TwinRelationshipService {
    @Autowired PlatformTwinRelationRepository relationRepo;

    @Transactional
    public void relate(Long src, Long target, String type) {
        PlatformTwinRelation rel = new PlatformTwinRelation();
        rel.setSourceInstanceId(src);
        rel.setTargetInstanceId(target);
        rel.setRelationshipType(type);
        rel.setUpdatedAt(LocalDateTime.now());
        relationRepo.save(rel);
    }
}