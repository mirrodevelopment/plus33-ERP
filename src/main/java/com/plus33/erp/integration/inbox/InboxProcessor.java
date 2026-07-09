/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.inbox
 * File              : InboxProcessor.java
 * Purpose           : Business logic service layer for Integration Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InboxProcessorController
 * Related Service   : InboxProcessor
 * Related Repository: InboxProcessorRepository
 * Related Entity    : InboxProcessor
 * Related DTO       : N/A
 * Related Mapper    : InboxProcessorMapper
 * Related DB Table  : inbox_processors
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InboxProcessorController, InboxProcessorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Integration Module. Implements InboxProcessorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code InboxProcessor}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.inbox}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Integration Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * InboxProcessorController
 *   --> InboxProcessor (this)
 *   --> Validate business rules
 *   --> InboxProcessorRepository (read/write 'inbox_processors')
 *   --> InboxProcessorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code inbox_processors}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class InboxProcessor {
    @Autowired ProcessedMessageRepository processedMessageRepo;
    @Autowired ConsumerCheckpointRepository consumerCheckpointRepo;
    /**
     * Processes the incoming message business workflow end-to-end.
     *
     * @param event the event input value
     * @param consumerGroup the consumerGroup input value
     * @param consumerName the consumerName input value
     * @return true if operation succeeded, false otherwise
     */
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