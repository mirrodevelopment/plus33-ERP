package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_agent_tool_execution")
public class PlatformAgentToolExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tool_id", nullable = false)
    @NotNull
    private Long toolId;

    @Column(name = "executor_node", nullable = false)
    @NotNull
    @Size(max = 100)
    private String executorNode;

    @Column(name = "input_parameters", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String inputParameters;

    @Column(name = "output_response", columnDefinition = "TEXT")
    private String outputResponse;

    @Column(nullable = false)
    @NotNull
    private Boolean success;

    @Column(name = "elapsed_ms", nullable = false)
    @NotNull
    private Long elapsedMs;

    @Column(name = "executed_at", nullable = false)
    @NotNull
    private LocalDateTime executedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getToolId() { return toolId; }
    public void setToolId(Long toolId) { this.toolId = toolId; }
    public String getExecutorNode() { return executorNode; }
    public void setExecutorNode(String executorNode) { this.executorNode = executorNode; }
    public String getInputParameters() { return inputParameters; }
    public void setInputParameters(String inputParameters) { this.inputParameters = inputParameters; }
    public String getOutputResponse() { return outputResponse; }
    public void setOutputResponse(String outputResponse) { this.outputResponse = outputResponse; }
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public Long getElapsedMs() { return elapsedMs; }
    public void setElapsedMs(Long elapsedMs) { this.elapsedMs = elapsedMs; }
    public LocalDateTime getExecutedAt() { return executedAt; }
    public void setExecutedAt(LocalDateTime executedAt) { this.executedAt = executedAt; }
}