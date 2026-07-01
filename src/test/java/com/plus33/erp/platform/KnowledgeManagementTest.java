package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.agent.knowledge.ChunkingService;
import com.plus33.erp.agent.knowledge.RetrievalService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class KnowledgeManagementTest {

    @Autowired ChunkingService chunkingService;
    @Autowired RetrievalService retrievalService;

    @Autowired PlatformKnowledgeSourceRepository sourceRepo;
    @Autowired PlatformKnowledgeChunkRepository chunkRepo;
    @Autowired PlatformKnowledgeVersionRepository versionRepo;
    @Autowired PlatformKnowledgeRefreshJobRepository refreshJobRepo;

    @Test
    void testKnowledgeManagementScenarios() {
        // Register 40 sources
        for (int i = 1; i <= 40; i++) {
            chunkingService.registerSource("Source-" + i, "PDF");
        }
        List<PlatformKnowledgeSource> sources = sourceRepo.findAll();
        assertTrue(sources.size() >= 40);

        // Chunk text for a source
        chunkingService.registerSource("Source-Chunk", "WIKI");
        StringBuilder text = new StringBuilder();
        for (int i = 1; i <= 40; i++) {
            text.append("Chunk paragraph content unique-").append(String.format("%03d", i)).append("\n\n");
        }
        chunkingService.importAndChunk("Source-Chunk", text.toString());

        List<PlatformKnowledgeChunk> chunks = chunkRepo.findAll().stream()
                .filter(c -> c.getChunkContent().startsWith("Chunk paragraph content"))
                .toList();
        assertEquals(40, chunks.size());

        // Perform 40 retrievals
        for (int i = 1; i <= 40; i++) {
            List<PlatformKnowledgeChunk> res = retrievalService.retrieveRelevantChunks("content unique-" + String.format("%03d", i));
            assertEquals(1, res.size());
        }

        // Register 40 indexing versions and jobs
        PlatformKnowledgeSource source = sources.get(0);
        for (int i = 1; i <= 40; i++) {
            PlatformKnowledgeVersion v = new PlatformKnowledgeVersion();
            v.setSourceId(source.getId());
            v.setIndexVersion("v1.0." + i);
            v.setTotalChunks(100);
            versionRepo.save(v);

            PlatformKnowledgeRefreshJob job = new PlatformKnowledgeRefreshJob();
            job.setSourceId(source.getId());
            job.setStatus("SUCCESS");
            job.setLogs("Completed refresh " + i);
            refreshJobRepo.save(job);
        }
        List<PlatformKnowledgeVersion> versions = versionRepo.findAll();
        assertTrue(versions.size() >= 40);

        List<PlatformKnowledgeRefreshJob> jobs = refreshJobRepo.findAll();
        assertTrue(jobs.size() >= 40);
    }
}
