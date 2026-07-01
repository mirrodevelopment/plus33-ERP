package com.plus33.erp.esm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "esm_surveys")
public class ServiceSurvey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_order_id", nullable = false, unique = true)
    private Long workOrderId;

    @Column(name = "csat_score", nullable = false)
    private Integer csatScore;

    @Column(name = "nps_score", nullable = false)
    private Integer npsScore;

    @Column(name = "ces_score", nullable = false)
    private Integer cesScore;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }
    public Integer getCsatScore() { return csatScore; }
    public void setCsatScore(Integer csatScore) { this.csatScore = csatScore; }
    public Integer getNpsScore() { return npsScore; }
    public void setNpsScore(Integer npsScore) { this.npsScore = npsScore; }
    public Integer getCesScore() { return cesScore; }
    public void setCesScore(Integer cesScore) { this.cesScore = cesScore; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
