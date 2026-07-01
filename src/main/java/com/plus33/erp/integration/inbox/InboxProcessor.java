package com.plus33.erp.integration.inbox;

import com.plus33.erp.integration.entity.ConsumerCheckpoint;
import com.plus33.erp.integration.entity.ProcessedMessage;
import com.plus33.erp.integration.eventmesh.CloudEvent;
import com.plus33.erp.integration.repository.ConsumerCheckpointRepository;
import com.plus33.erp.integration.repository.ProcessedMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class InboxProcessor {
    @Autowired ProcessedMessageRepository processedMessageRepo;
    @Autowired ConsumerCheckpointRepository consumerCheckpointRepo;

    @Transactional
    public boolean processIncomingMessage(CloudEvent event, String consumerGroup, String consumerName) {
        boolean alreadyProcessed = processedMessageRepo.findAll().stream()
                .anyMatch(p -> p.getMessageId().equals(event.getId()) && p.getConsumerGroup().equals(consumerGroup));
        
        if (alreadyProcessed) {
            return false;
        }

        ProcessedMessage pm = new ProcessedMessage();
        pm.setMessageId(event.getId());
        pm.setConsumerGroup(consumerGroup);
        pm.setProcessedAt(LocalDateTime.now());
        processedMessageRepo.save(pm);

        ConsumerCheckpoint checkpoint = consumerCheckpointRepo.findAll().stream()
                .filter(c -> c.getGroupName().equals(consumerGroup) && c.getTopic().equals(event.getType()))
                .findFirst().orElse(null);

        if (checkpoint == null) {
            checkpoint = new ConsumerCheckpoint();
            checkpoint.setGroupName(consumerGroup);
            checkpoint.setConsumerName(consumerName);
            checkpoint.setTopic(event.getType());
            checkpoint.setPartitionAssignment("partition-0");
            checkpoint.setRebalanceGeneration(1);
            checkpoint.setCheckpointOffset(1L);
        } else {
            checkpoint.setCheckpointOffset(checkpoint.getCheckpointOffset() + 1);
            checkpoint.setConsumerName(consumerName);
        }
        checkpoint.setUpdatedAt(LocalDateTime.now());
        consumerCheckpointRepo.save(checkpoint);

        return true;
    }
}
