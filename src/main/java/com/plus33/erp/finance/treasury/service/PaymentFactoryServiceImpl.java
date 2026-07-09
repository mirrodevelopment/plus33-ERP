/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : PaymentFactoryServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentFactoryController
 * Related Service   : PaymentFactoryServiceImpl
 * Related Repository: PaymentBatchRepository, PaymentFileRepository, PaymentTransmissionLogRepository, CashTransferRepository, TreasuryApprovalStepRepository, TreasuryApprovalRepository, BankAccountRepository, PaymentRepository, CompanyRepository
 * Related Entity    : PaymentFactory
 * Related DTO       : CashTransferRequest, CashTransferResponse, mapToBatchResponse, mapToFileResponse, mapToTransferResponse
 * Related Mapper    : PaymentFactoryMapper
 * Related DB Table  : payment_factorys
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : PaymentFactoryController, PaymentFactoryServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements PaymentFactoryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.entity.Payment;
import com.plus33.erp.finance.repository.PaymentRepository;
import com.plus33.erp.finance.treasury.dto.PaymentFactoryDtos.*;
import com.plus33.erp.finance.treasury.entity.*;
import com.plus33.erp.finance.treasury.repository.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentFactoryServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PaymentFactoryController
 *   --> PaymentFactoryServiceImpl (this)
 *   --> Validate business rules
 *   --> PaymentFactoryRepository (read/write 'payment_factorys')
 *   --> PaymentFactoryMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code payment_factorys}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentFactoryServiceImpl implements PaymentFactoryService {

    private final PaymentBatchRepository paymentBatchRepository;
    private final PaymentFileRepository paymentFileRepository;
    private final PaymentTransmissionLogRepository paymentTransmissionLogRepository;
    private final CashTransferRepository cashTransferRepository;
    private final TreasuryApprovalStepRepository treasuryApprovalStepRepository;
    private final TreasuryApprovalRepository treasuryApprovalRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PaymentRepository paymentRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Creates a new payment batch and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @param username the username input value
     * @return the PaymentBatchResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PaymentBatchResponse createPaymentBatch(PaymentBatchRequest request, String username) {
        Company company = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + request.companyId()));
        BankAccount sourceAcct = bankAccountRepository.findById(request.sourceBankAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Source Account not found: " + request.sourceBankAccountId()));

        if (paymentBatchRepository.findByCompanyIdAndBatchNumber(request.companyId(), request.batchNumber()).isPresent()) {
            throw new BusinessException("Payment Batch already exists: " + request.batchNumber());
        }

        BigDecimal total = BigDecimal.ZERO;
        List<Payment> batchPayments = new ArrayList<>();
        for (Long pId : request.paymentIds()) {
            Payment p = paymentRepository.findById(pId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + pId));
            total = total.add(p.getAmount());
            batchPayments.add(p);
        }

        PaymentBatch batch = PaymentBatch.builder()
            .company(company)
            .batchNumber(request.batchNumber())
            .sourceBankAccount(sourceAcct)
            .status("DRAFT")
            .totalAmount(total)
            .currencyCode(sourceAcct.getCurrencyCode())
            .createdBy(username)
            .build();

        PaymentBatch savedBatch = paymentBatchRepository.save(batch);

        for (Payment p : batchPayments) {
            p.setPaymentBatchId(savedBatch.getId());
            paymentRepository.save(p);
        }

        // Initialize approval matrix routing
        List<TreasuryApprovalStep> steps = treasuryApprovalStepRepository.findByActiveTrueOrderByStepSequenceAsc();
        for (TreasuryApprovalStep step : steps) {
            if (total.compareTo(step.getMinAmount()) >= 0 && total.compareTo(step.getMaxAmount()) <= 0) {
                TreasuryApproval app = TreasuryApproval.builder()
                    .paymentBatch(savedBatch)
                    .approvalStep(step.getStepSequence())
                    .roleCode(step.getRoleCode())
                    .status("PENDING")
                    .build();
                treasuryApprovalRepository.save(app);
            }
        }

        savedBatch.setStatus("PENDING_APPROVAL");
        paymentBatchRepository.save(savedBatch);

        return mapToBatchResponse(savedBatch);
    }

    /**
     * Retrieves a single payment batch by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the PaymentBatchResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single payment batch by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the PaymentBatchResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public PaymentBatchResponse getPaymentBatchById(Long id) {
        PaymentBatch batch = paymentBatchRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment Batch not found: " + id));
        return mapToBatchResponse(batch);
    }

    /**
     * Retrieves payment batches by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves payment batches by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<PaymentBatchResponse> getPaymentBatchesByCompany(Long companyId) {
        return paymentBatchRepository.findByCompanyId(companyId).stream().map(this::mapToBatchResponse).toList();
    }

    /**
     * Approves the payment batch, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param batchId the batchId input value
     * @param remarks the remarks input value
     * @param username the username input value
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public void approvePaymentBatch(Long batchId, String remarks, String username) {
        PaymentBatch batch = paymentBatchRepository.findById(batchId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment Batch not found: " + batchId));

        if (!"PENDING_APPROVAL".equals(batch.getStatus())) {
            throw new BusinessException("Batch is not in PENDING_APPROVAL status: " + batch.getStatus());
        }

        // Enforce 4-Eyes Principle
        if (batch.getCreatedBy().equals(username)) {
            throw new BusinessException("Creator of payment batch cannot approve it: " + username);
        }

        List<TreasuryApproval> approvals = treasuryApprovalRepository.findByPaymentBatchId(batchId);
        TreasuryApproval nextApproval = approvals.stream()
            .filter(a -> "PENDING".equals(a.getStatus()))
            .sorted((a1, a2) -> a1.getApprovalStep().compareTo(a2.getApprovalStep()))
            .findFirst()
            .orElse(null);

        if (nextApproval == null) {
            batch.setStatus("APPROVED");
            batch.setApprovedBy(username);
            paymentBatchRepository.save(batch);

            // Publish Event
            eventPublisher.publishEvent(new com.plus33.erp.finance.treasury.entity.PaymentBatchApprovedEvent(this, batchId));
            return;
        }

        // Verify next approver role or just process the step
        nextApproval.setStatus("APPROVED");
        nextApproval.setApproverUsername(username);
        nextApproval.setApprovedAt(LocalDateTime.now());
        nextApproval.setRemarks(remarks);
        treasuryApprovalRepository.save(nextApproval);

        // Re-check if any pending approvals left
        boolean pendingLeft = approvals.stream().anyMatch(a -> "PENDING".equals(a.getStatus()));
        if (!pendingLeft) {
            batch.setStatus("APPROVED");
            batch.setApprovedBy(username);
            paymentBatchRepository.save(batch);

            // Publish Event
            eventPublisher.publishEvent(new com.plus33.erp.finance.treasury.entity.PaymentBatchApprovedEvent(this, batchId));
        }
    }

    /**
     * Performs the rejectPaymentBatch operation in this module.
     *
     * @param batchId the batchId input value
     * @param remarks the remarks input value
     * @param username the username input value
     */
    @Override
    @Transactional
    public void rejectPaymentBatch(Long batchId, String remarks, String username) {
        PaymentBatch batch = paymentBatchRepository.findById(batchId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment Batch not found: " + batchId));

        batch.setStatus("REJECTED");
        batch.setApprovedBy(username);
        paymentBatchRepository.save(batch);

        List<TreasuryApproval> approvals = treasuryApprovalRepository.findByPaymentBatchId(batchId);
        for (TreasuryApproval a : approvals) {
            if ("PENDING".equals(a.getStatus())) {
                a.setStatus("REJECTED");
                a.setApproverUsername(username);
                a.setApprovedAt(LocalDateTime.now());
                a.setRemarks(remarks);
                treasuryApprovalRepository.save(a);
            }
        }
    }

    /**
     * Generates the payment file based on input parameters and business rules.
     *
     * @param batchId the batchId input value
     * @param format the format input value
     * @return the PaymentFileResponse result
     */
    @Override
    @Transactional
    public PaymentFileResponse generatePaymentFile(Long batchId, String format) {
        PaymentBatch batch = paymentBatchRepository.findById(batchId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment Batch not found: " + batchId));

        if (!"APPROVED".equals(batch.getStatus())) {
            throw new BusinessException("Cannot generate file for unapproved batch.");
        }

        // ISO 20022 XML generation mock
        String mockContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.001.001.03\">\n" +
            "  <CstmrCdtTrfInitn>\n" +
            "    <GrpHdr>\n" +
            "      <MsgId>" + batch.getBatchNumber() + "</MsgId>\n" +
            "      <CreDtTm>" + LocalDateTime.now() + "</CreDtTm>\n" +
            "      <CtrlSum>" + batch.getTotalAmount() + "</CtrlSum>\n" +
            "    </GrpHdr>\n" +
            "  </CstmrCdtTrfInitn>\n" +
            "</Document>";

        PaymentFile file = PaymentFile.builder()
            .batch(batch)
            .fileName("PMT_BATCH_" + batch.getBatchNumber() + "." + format.toLowerCase())
            .fileFormat(format)
            .fileContent(mockContent)
            .build();
        PaymentFile savedFile = paymentFileRepository.save(file);

        return mapToFileResponse(savedFile);
    }

    /**
     * Performs the transmitPaymentFile operation in this module.
     *
     * @param fileId the fileId input value
     * @param method the method input value
     */
    @Override
    @Transactional
    public void transmitPaymentFile(Long fileId, String method) {
        PaymentFile file = paymentFileRepository.findById(fileId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment File not found: " + fileId));

        // Mock Transmission API logic
        PaymentTransmissionLog logEntry = PaymentTransmissionLog.builder()
            .file(file)
            .transmissionMethod(method)
            .status("SUCCESS")
            .requestPayload(file.getFileContent())
            .responsePayload("ACK_RECEIVED: 200 OK")
            .build();
        
        paymentTransmissionLogRepository.save(logEntry);

        PaymentBatch batch = file.getBatch();
        batch.setStatus("TRANSMITTED");
        paymentBatchRepository.save(batch);
    }

    /**
     * Creates a new cash transfer and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @param username the username input value
     * @return the CashTransferResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public CashTransferResponse createCashTransfer(CashTransferRequest request, String username) {
        BankAccount source = bankAccountRepository.findById(request.sourceBankAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Source Account not found: " + request.sourceBankAccountId()));
        BankAccount dest = bankAccountRepository.findById(request.destinationBankAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Destination Account not found: " + request.destinationBankAccountId()));
        Company company = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + request.companyId()));

        if (source.getCurrentBalance().compareTo(request.amount()) < 0) {
            throw new BusinessException("Insufficient funds. Available: " + source.getCurrentBalance());
        }

        CashTransfer transfer = CashTransfer.builder()
            .company(company)
            .sourceBankAccount(source)
            .destinationBankAccount(dest)
            .transferDate(request.transferDate())
            .amount(request.amount())
            .exchangeRate(request.exchangeRate() != null ? request.exchangeRate() : BigDecimal.ONE)
            .fees(request.fees() != null ? request.fees() : BigDecimal.ZERO)
            .referenceNumber(request.referenceNumber())
            .status("DRAFT")
            .createdBy(username)
            .build();

        CashTransfer savedTransfer = cashTransferRepository.save(transfer);

        // Setup approvals based on transfer amount
        List<TreasuryApprovalStep> steps = treasuryApprovalStepRepository.findByActiveTrueOrderByStepSequenceAsc();
        for (TreasuryApprovalStep step : steps) {
            if (request.amount().compareTo(step.getMinAmount()) >= 0 && request.amount().compareTo(step.getMaxAmount()) <= 0) {
                TreasuryApproval app = TreasuryApproval.builder()
                    .transfer(savedTransfer)
                    .approvalStep(step.getStepSequence())
                    .roleCode(step.getRoleCode())
                    .status("PENDING")
                    .build();
                treasuryApprovalRepository.save(app);
            }
        }

        savedTransfer.setStatus("PENDING_APPROVAL");
        cashTransferRepository.save(savedTransfer);

        return mapToTransferResponse(savedTransfer);
    }

    /**
     * Retrieves a single cash transfer by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the CashTransferResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single cash transfer by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the CashTransferResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public CashTransferResponse getCashTransferById(Long id) {
        CashTransfer transfer = cashTransferRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cash Transfer not found: " + id));
        return mapToTransferResponse(transfer);
    }

    /**
     * Retrieves cash transfers by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves cash transfers by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<CashTransferResponse> getCashTransfersByCompany(Long companyId) {
        return cashTransferRepository.findByCompanyId(companyId).stream().map(this::mapToTransferResponse).toList();
    }

    /**
     * Approves the cash transfer, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param transferId the transferId input value
     * @param remarks the remarks input value
     * @param username the username input value
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public void approveCashTransfer(Long transferId, String remarks, String username) {
        CashTransfer transfer = cashTransferRepository.findById(transferId)
            .orElseThrow(() -> new ResourceNotFoundException("Cash Transfer not found: " + transferId));

        if (!"PENDING_APPROVAL".equals(transfer.getStatus())) {
            throw new BusinessException("Transfer is not pending approval.");
        }

        // 4-Eyes Principle check
        if (transfer.getCreatedBy().equals(username)) {
            throw new BusinessException("Creator of cash transfer cannot approve it: " + username);
        }

        List<TreasuryApproval> approvals = treasuryApprovalRepository.findByTransferId(transferId);
        TreasuryApproval nextApproval = approvals.stream()
            .filter(a -> "PENDING".equals(a.getStatus()))
            .sorted((a1, a2) -> a1.getApprovalStep().compareTo(a2.getApprovalStep()))
            .findFirst()
            .orElse(null);

        if (nextApproval != null) {
            nextApproval.setStatus("APPROVED");
            nextApproval.setApproverUsername(username);
            nextApproval.setApprovedAt(LocalDateTime.now());
            nextApproval.setRemarks(remarks);
            treasuryApprovalRepository.save(nextApproval);
        }

        boolean pendingLeft = approvals.stream().anyMatch(a -> "PENDING".equals(a.getStatus()));
        if (!pendingLeft) {
            // Process funds movement on final approval
            BankAccount source = transfer.getSourceBankAccount();
            BankAccount dest = transfer.getDestinationBankAccount();

            BigDecimal deduct = transfer.getAmount().add(transfer.getFees());
            BigDecimal credit = transfer.getAmount().multiply(transfer.getExchangeRate());

            source.setCurrentBalance(source.getCurrentBalance().subtract(deduct));
            dest.setCurrentBalance(dest.getCurrentBalance().add(credit));

            bankAccountRepository.save(source);
            bankAccountRepository.save(dest);

            transfer.setStatus("COMPLETED");
            transfer.setApprovedBy(username);
            transfer.setApprovedAt(LocalDateTime.now());
            cashTransferRepository.save(transfer);

            // Publish Event
            eventPublisher.publishEvent(new com.plus33.erp.finance.treasury.entity.CashTransferCompletedEvent(this, transferId));
        }
    }

    /**
     * Performs the rejectCashTransfer operation in this module.
     *
     * @param transferId the transferId input value
     * @param remarks the remarks input value
     * @param username the username input value
     */
    @Override
    @Transactional
    public void rejectCashTransfer(Long transferId, String remarks, String username) {
        CashTransfer transfer = cashTransferRepository.findById(transferId)
            .orElseThrow(() -> new ResourceNotFoundException("Cash Transfer not found: " + transferId));

        transfer.setStatus("CANCELLED");
        transfer.setApprovedBy(username);
        transfer.setApprovedAt(LocalDateTime.now());
        cashTransferRepository.save(transfer);

        List<TreasuryApproval> approvals = treasuryApprovalRepository.findByTransferId(transferId);
        for (TreasuryApproval a : approvals) {
            if ("PENDING".equals(a.getStatus())) {
                a.setStatus("REJECTED");
                a.setApproverUsername(username);
                a.setApprovedAt(LocalDateTime.now());
                a.setRemarks(remarks);
                treasuryApprovalRepository.save(a);
            }
        }
    }

    // Mapping Helpers
    private PaymentBatchResponse mapToBatchResponse(PaymentBatch batch) {
        return new PaymentBatchResponse(
            batch.getId(),
            batch.getCompany().getId(),
            batch.getBatchNumber(),
            batch.getSourceBankAccount().getId(),
            batch.getSourceBankAccount().getAccountNumber(),
            batch.getStatus(),
            batch.getTotalAmount(),
            batch.getCurrencyCode(),
            batch.getCreatedBy(),
            batch.getApprovedBy(),
            batch.getCreatedAt()
        );
    }

    private PaymentFileResponse mapToFileResponse(PaymentFile file) {
        return new PaymentFileResponse(
            file.getId(),
            file.getBatch().getId(),
            file.getFileName(),
            file.getFileFormat(),
            file.getFileContent(),
            file.getGeneratedAt()
        );
    }

    private CashTransferResponse mapToTransferResponse(CashTransfer transfer) {
        return new CashTransferResponse(
            transfer.getId(),
            transfer.getCompany().getId(),
            transfer.getSourceBankAccount().getId(),
            transfer.getSourceBankAccount().getAccountNumber(),
            transfer.getDestinationBankAccount().getId(),
            transfer.getDestinationBankAccount().getAccountNumber(),
            transfer.getTransferDate(),
            transfer.getAmount(),
            transfer.getExchangeRate(),
            transfer.getFees(),
            transfer.getReferenceNumber(),
            transfer.getStatus(),
            transfer.getCreatedBy(),
            transfer.getApprovedBy(),
            transfer.getApprovedAt(),
            transfer.getCreatedAt()
        );
    }
}