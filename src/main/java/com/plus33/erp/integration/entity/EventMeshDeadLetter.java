package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_event_mesh_dead_letter")
public class EventMeshDeadLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String eventId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String payload;

    @Column(name = "exception_message", columnDefinition = "TEXT")
    private String exceptionMessage;

    @Column(name = "retry_count", nullable = false)
    @NotNull
    private Integer retryCount = 0;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "PENDING_REPLAY";

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public String getExceptionMessage() { return exceptionMessage; }
    public void setExceptionMessage(String exceptionMessage) { this.exceptionMessage = exceptionMessage; }
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}