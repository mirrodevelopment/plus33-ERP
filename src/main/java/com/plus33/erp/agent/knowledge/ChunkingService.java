package com.plus33.erp.agent.knowledge;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChunkingService {
    @Autowired PlatformKnowledgeSourceRepository sourceRepo;
    @Autowired PlatformKnowledgeChunkRepository chunkRepo;

    @Transactional
    public void registerSource(String name, String type) {
        PlatformKnowledgeSource src = new PlatformKnowledgeSource();
        src.setSourceName(name);
        src.setSourceType(type);
        src.setActive(true);
        sourceRepo.save(src);
    }

    @Transactional
    public void importAndChunk(String sourceName, String text) {
        PlatformKnowledgeSource src = sourceRepo.findAll().stream()
                .filter(s -> s.getSourceName().equals(sourceName))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Source not found"));

        // Split text into chunks
        String[] parts = text.split("\n\n");
        for (int i = 0; i < parts.length; i++) {
            PlatformKnowledgeChunk chunk = new PlatformKnowledgeChunk();
            chunk.setSourceId(src.getId());
            chunk.setChunkContent(parts[i]);
            chunk.setVectorPlaceholder("emb-placeholder-" + i);
            chunkRepo.save(chunk);
        }
    }
}