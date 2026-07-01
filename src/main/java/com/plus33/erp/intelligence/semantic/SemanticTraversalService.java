package com.plus33.erp.intelligence.semantic;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SemanticTraversalService {
    @Autowired PlatformTwinRelationRepository relationRepo;

    public List<PlatformTwinRelation> findDependencies(Long instanceId) {
        return relationRepo.findAll().stream()
                .filter(r -> r.getSourceInstanceId().equals(instanceId) && "DependsOn".equals(r.getRelationshipType()))
                .toList();
    }
}