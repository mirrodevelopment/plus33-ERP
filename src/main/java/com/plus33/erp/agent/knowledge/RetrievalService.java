package com.plus33.erp.agent.knowledge;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RetrievalService {
    @Autowired PlatformKnowledgeChunkRepository chunkRepo;

    public List<PlatformKnowledgeChunk> retrieveRelevantChunks(String query) {
        return chunkRepo.findAll().stream()
                .filter(c -> c.getChunkContent().contains(query))
                .toList();
    }
}