package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_knowledge_chunk")
public class PlatformKnowledgeChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", nullable = false)
    @NotNull
    private Long sourceId;

    @Column(name = "chunk_content", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String chunkContent;

    @Column(name = "vector_placeholder")
    @Size(max = 250)
    private String vectorPlaceholder;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public String getChunkContent() { return chunkContent; }
    public void setChunkContent(String chunkContent) { this.chunkContent = chunkContent; }
    public String getVectorPlaceholder() { return vectorPlaceholder; }
    public void setVectorPlaceholder(String vectorPlaceholder) { this.vectorPlaceholder = vectorPlaceholder; }
}