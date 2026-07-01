package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_operational_query_log")
public class PlatformOperationalQueryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_text", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String queryText;

    @Column(name = "parsed_intent", nullable = false)
    @NotNull
    @Size(max = 250)
    private String parsedIntent;

    @Column(name = "execution_plan_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String executionPlanJson;

    @Column(name = "queried_at", nullable = false)
    @NotNull
    private LocalDateTime queriedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getQueryText() { return queryText; }
    public void setQueryText(String queryText) { this.queryText = queryText; }
    public String getParsedIntent() { return parsedIntent; }
    public void setParsedIntent(String parsedIntent) { this.parsedIntent = parsedIntent; }
    public String getExecutionPlanJson() { return executionPlanJson; }
    public void setExecutionPlanJson(String executionPlanJson) { this.executionPlanJson = executionPlanJson; }
    public LocalDateTime getQueriedAt() { return queriedAt; }
    public void setQueriedAt(LocalDateTime queriedAt) { this.queriedAt = queriedAt; }
}