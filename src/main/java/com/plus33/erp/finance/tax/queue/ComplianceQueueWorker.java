package com.plus33.erp.finance.tax.queue;

import com.plus33.erp.finance.tax.compliance.ComplianceProvider;
import com.plus33.erp.finance.tax.compliance.ComplianceProviderRegistry;
import com.plus33.erp.finance.tax.compliance.ComplianceSubmissionResult;
import com.plus33.erp.finance.tax.entity.ComplianceQueueItem;
import com.plus33.erp.finance.tax.event.EInvoiceSubmittedEvent;
import com.plus33.erp.finance.tax.repository.ComplianceQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Background worker that processes the compliance submission queue.
 * Supports retry with exponential backoff and dead-letter logic.
 *
 * Queue flow:
 *   Invoice Posted → Compliance Queue (PENDING) → Worker → Government Portal
 *   On failure: FAILED (retry) → DEAD_LETTER (if retries exceeded)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ComplianceQueueWorker {

    private final ComplianceQueueRepository queueRepository;
    private final ComplianceProviderRegistry providerRegistry;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processQueue() {
        List<ComplianceQueueItem> pending = queueRepository.findByStatusIn(List.of("PENDING", "FAILED"));

        for (ComplianceQueueItem item : pending) {
            if (item.getRetryCount() >= item.getMaxRetries()) {
                item.setStatus("DEAD_LETTER");
                item.setUpdatedAt(LocalDateTime.now());
                queueRepository.save(item);
                log.warn("Compliance queue item {} moved to DEAD_LETTER after {} retries for {}/{}",
                        item.getId(), item.getMaxRetries(), item.getDocumentType(), item.getDocumentId());
                continue;
            }

            item.setStatus("PROCESSING");
            item.setUpdatedAt(LocalDateTime.now());
            queueRepository.save(item);

            try {
                ComplianceProvider provider = providerRegistry.getProvider(item.getProviderType());
                ComplianceSubmissionResult result = provider.submitDocument(
                        item.getDocumentType(), item.getDocumentId(), item.getPayload());

                if (result.isSuccess()) {
                    item.setStatus("COMPLETED");
                    item.setLastError(null);
                    log.info("Compliance submission succeeded for {}/{} via {} — UUID: {}",
                            item.getDocumentType(), item.getDocumentId(),
                            item.getProviderType(), result.getGovernmentUuid());

                    // Publish event
                    eventPublisher.publishEvent(new EInvoiceSubmittedEvent(
                            this, item.getCompanyId(), item.getDocumentType(), item.getDocumentId(),
                            item.getProviderType(), result.getGovernmentUuid(), true));
                } else {
                    item.setStatus("FAILED");
                    item.setRetryCount(item.getRetryCount() + 1);
                    item.setLastError(result.getErrorDetails());
                    log.warn("Compliance submission failed for {}/{} via {}: {}",
                            item.getDocumentType(), item.getDocumentId(),
                            item.getProviderType(), result.getErrorDetails());

                    eventPublisher.publishEvent(new EInvoiceSubmittedEvent(
                            this, item.getCompanyId(), item.getDocumentType(), item.getDocumentId(),
                            item.getProviderType(), null, false));
                }
            } catch (Exception e) {
                item.setStatus("FAILED");
                item.setRetryCount(item.getRetryCount() + 1);
                item.setLastError(e.getMessage());
                log.error("Compliance submission error for {}/{} via {}: {}",
                        item.getDocumentType(), item.getDocumentId(),
                        item.getProviderType(), e.getMessage(), e);
            }

            item.setUpdatedAt(LocalDateTime.now());
            queueRepository.save(item);
        }
    }
}
