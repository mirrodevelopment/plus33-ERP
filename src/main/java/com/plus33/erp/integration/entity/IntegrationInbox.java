package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_inbox")
public class IntegrationInbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String eventId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String topic;

    @Column(name = "received_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime receivedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public LocalDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; }
}