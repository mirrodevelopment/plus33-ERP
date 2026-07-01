package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "consumer_checkpoint", uniqueConstraints = @UniqueConstraint(columnNames = {"group_name", "topic"}))
public class ConsumerCheckpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String groupName;

    @Column(name = "consumer_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String consumerName;

    @Column(name = "partition_assignment")
    @Size(max = 250)
    private String partitionAssignment;

    @Column(name = "rebalance_generation", nullable = false)
    @NotNull
    private Integer rebalanceGeneration = 0;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String topic;

    @Column(name = "checkpoint_offset", nullable = false)
    @NotNull
    private Long checkpointOffset = 0L;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public String getConsumerName() { return consumerName; }
    public void setConsumerName(String consumerName) { this.consumerName = consumerName; }
    public String getPartitionAssignment() { return partitionAssignment; }
    public void setPartitionAssignment(String partitionAssignment) { this.partitionAssignment = partitionAssignment; }
    public Integer getRebalanceGeneration() { return rebalanceGeneration; }
    public void setRebalanceGeneration(Integer rebalanceGeneration) { this.rebalanceGeneration = rebalanceGeneration; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public Long getCheckpointOffset() { return checkpointOffset; }
    public void setCheckpointOffset(Long checkpointOffset) { this.checkpointOffset = checkpointOffset; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}