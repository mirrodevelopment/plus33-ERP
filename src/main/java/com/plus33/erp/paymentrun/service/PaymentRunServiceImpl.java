/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.service
 * File              : PaymentRunServiceImpl.java
 * Purpose           : Business logic service layer for Paymentrun Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunController
 * Related Service   : PaymentRunServiceImpl
 * Related Repository: PaymentRunRepository, PaymentRunInvoiceRepository, PaymentRunSupplierResultRepository, SupplierInvoiceRepository, CompanyRepository, SupplierRepository, UserRepository
 * Related Entity    : PaymentRun
 * Related DTO       : paymentResponse, PaymentRunDashboardResponse, PaymentRunInvoiceRequest, PaymentRunRequest, PaymentRunResponse
 * Related Mapper    : PaymentRunMapper
 * Related DB Table  : payment_runs
 * Related REST APIs : N/A
 * Depends On        : Common Module, Finance Module, Organization Module, Procurement Module, Security Module
 * Used By           : PaymentRunController, PaymentRunServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Paymentrun Module. Implements PaymentRunService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.paymentrun.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.entity.Payment;
import com.plus33.erp.finance.entity.SupplierInvoice;
import com.plus33.erp.finance.entity.SupplierInvoiceStatus;
import com.plus33.erp.finance.repository.SupplierInvoiceRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.paymentrun.dto.*;
import com.plus33.erp.paymentrun.entity.*;
import com.plus33.erp.paymentrun.mapper.PaymentRunMapper;
import com.plus33.erp.paymentrun.repository.PaymentRunInvoiceRepository;
import com.plus33.erp.paymentrun.repository.PaymentRunRepository;
import com.plus33.erp.paymentrun.repository.PaymentRunSupplierResultRepository;
import com.plus33.erp.paymentrun.service.export.BankExportGenerator;
import com.plus33.erp.paymentrun.service.export.CsvExportGenerator;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.procurement.repository.SupplierRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRunServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Paymentrun Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PaymentRunController
 *   --> PaymentRunServiceImpl (this)
 *   --> Validate business rules
 *   --> PaymentRunRepository (read/write 'payment_runs')
 *   --> PaymentRunMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code payment_runs}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization, Paymentrun, Procurement, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PaymentRunServiceImpl implements PaymentRunService {

    private final PaymentRunRepository paymentRunRepository;
    private final PaymentRunInvoiceRepository paymentRunInvoiceRepository;
    private final PaymentRunSupplierResultRepository supplierResultRepository;
    private final SupplierInvoiceRepository supplierInvoiceRepository;
    private final CompanyRepository companyRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final PaymentRunExecutionHelper executionHelper;
    private final FileStorageService fileStorageService;
    private final PaymentRunMapper paymentRunMapper;
    private final List<BankExportGenerator> exportGenerators;
    private final CsvExportGenerator csvExportGenerator;

    @PersistenceContext
    private EntityManager entityManager;

    public PaymentRunServiceImpl(
            PaymentRunRepository paymentRunRepository,
            PaymentRunInvoiceRepository paymentRunInvoiceRepository,
            PaymentRunSupplierResultRepository supplierResultRepository,
            SupplierInvoiceRepository supplierInvoiceRepository,
            CompanyRepository companyRepository,
            SupplierRepository supplierRepository,
            UserRepository userRepository,
            PaymentRunExecutionHelper executionHelper,
            FileStorageService fileStorageService,
            PaymentRunMapper paymentRunMapper,
            List<BankExportGenerator> exportGenerators,
            CsvExportGenerator csvExportGenerator) {
        this.paymentRunRepository = paymentRunRepository;
        this.paymentRunInvoiceRepository = paymentRunInvoiceRepository;
        this.supplierResultRepository = supplierResultRepository;
        this.supplierInvoiceRepository = supplierInvoiceRepository;
        this.companyRepository = companyRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
        this.executionHelper = executionHelper;
        this.fileStorageService = fileStorageService;
        this.paymentRunMapper = paymentRunMapper;
        this.exportGenerators = exportGenerators;
        this.csvExportGenerator = csvExportGenerator;
    }

    /**
     * Creates a new payment run and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the PaymentRunResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PaymentRunResponse createPaymentRun(PaymentRunRequest request) {
        User currentUser = getCurrentUser();
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        
        Supplier filterSupplier = null;
        if (request.filterSupplierId() != null) {
            filterSupplier = supplierRepository.findById(request.filterSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        }

        Long seq = paymentRunRepository.getNextSequenceValue();
        String runNumber = String.format("PR-%s-%06d", LocalDate.now().toString().replace("-", ""), seq);

        PaymentRun run = PaymentRun.builder()
                .runNumber(runNumber)
                .company(company)
                .status(PaymentRunStatus.DRAFT)
                .paymentDate(request.paymentDate())
                .paymentMethod(request.paymentMethod())
                .currencyCode(request.currencyCode())
                .filterDueDate(request.filterDueDate())
                .filterSupplier(filterSupplier)
                .bankAccountCode(request.bankAccountCode() != null ? request.bankAccountCode() : "1200")
                .exportFormat(request.exportFormat() != null ? request.exportFormat() : "CSV")
                .clientReferenceId(request.clientReferenceId())
                .createdBy(currentUser)
                .build();

        PaymentRun saved = paymentRunRepository.save(run);
        return paymentRunMapper.toResponse(saved);
    }

    /**
     * Calculates payment run totals including subtotal, tax, discounts, and net amount.
     *
     * @param id the unique database ID of the resource
     * @return the PaymentRunResponse result
     */
    @Override
    @Transactional
    public PaymentRunResponse calculatePaymentRun(Long id) {
        PaymentRun run = paymentRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Run not found"));
        
        if (run.getStatus() != PaymentRunStatus.DRAFT && run.getStatus() != PaymentRunStatus.CALCULATED) {
            throw new BusinessException("Payment run must be in DRAFT or CALCULATED status to calculate");
        }

        // Release existing invoice reservations first if recalculating
        supplierInvoiceRepository.releaseInvoicesForPaymentRun(id);


        run.getInvoices().clear();
        paymentRunRepository.saveAndFlush(run);

        // Fetch eligible invoices
        Long supplierId = run.getFilterSupplier() != null ? run.getFilterSupplier().getId() : null;
        List<SupplierInvoice> eligible = supplierInvoiceRepository.findEligibleForPaymentRun(
                run.getCompany().getId(),
                run.getCurrencyCode(),
                supplierId,
                run.getFilterDueDate()
        );

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (SupplierInvoice invoice : eligible) {
            // Reserve invoice
            invoice.setPaymentRun(run);
            supplierInvoiceRepository.save(invoice);

            PaymentRunInvoice runInvoice = PaymentRunInvoice.builder()
                    .paymentRun(run)
                    .supplierInvoice(invoice)
                    .invoiceOutstandingBalance(invoice.getOutstandingBalance())
                    .paymentAmount(invoice.getOutstandingBalance())
                    .paymentReference(run.getRunNumber())
                    .build();

            run.getInvoices().add(runInvoice);
            totalAmount = totalAmount.add(runInvoice.getPaymentAmount());
        }

        run.setTotalAmount(totalAmount);
        run.setStatus(PaymentRunStatus.CALCULATED);
        PaymentRun saved = paymentRunRepository.save(run);
        return paymentRunMapper.toResponse(saved);
    }

    /**
     * Updates an existing payment run invoices record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param requests the requests input value
     * @return the PaymentRunResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PaymentRunResponse updatePaymentRunInvoices(Long id, List<PaymentRunInvoiceRequest> requests) {
        PaymentRun run = paymentRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Run not found"));
        
        if (run.getStatus() != PaymentRunStatus.DRAFT && run.getStatus() != PaymentRunStatus.CALCULATED) {
            throw new BusinessException("Payment run must be in DRAFT or CALCULATED status to update");
        }

        Map<Long, PaymentRunInvoiceRequest> requestMap = requests.stream()
                .collect(Collectors.toMap(PaymentRunInvoiceRequest::supplierInvoiceId, Function.identity()));

        List<PaymentRunInvoice> currentItems = new ArrayList<>(run.getInvoices());
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PaymentRunInvoice item : currentItems) {
            Long invoiceId = item.getSupplierInvoice().getId();
            if (requestMap.containsKey(invoiceId)) {
                PaymentRunInvoiceRequest req = requestMap.get(invoiceId);
                BigDecimal amt = req.paymentAmount();
                
                if (amt == null || amt.compareTo(BigDecimal.ZERO) <= 0) {
                    // Exclude invoice: release reservation and remove from run list
                    SupplierInvoice inv = item.getSupplierInvoice();
                    inv.setPaymentRun(null);
                    supplierInvoiceRepository.save(inv);
                    run.getInvoices().remove(item);
                } else {
                    // Validate amount does not exceed outstanding balance
                    if (amt.compareTo(item.getInvoiceOutstandingBalance()) > 0) {
                        throw new BusinessException("Payment amount of " + amt + " cannot exceed outstanding balance of " +
                                item.getInvoiceOutstandingBalance() + " for invoice " + item.getSupplierInvoice().getInvoiceNumber());
                    }
                    item.setPaymentAmount(amt);
                    if (req.paymentReference() != null && !req.paymentReference().isBlank()) {
                        item.setPaymentReference(req.paymentReference());
                    }
                    totalAmount = totalAmount.add(amt);
                }
            } else {
                // If not mentioned in update, keep original amount
                totalAmount = totalAmount.add(item.getPaymentAmount());
            }
        }

        run.setTotalAmount(totalAmount);
        run.setStatus(PaymentRunStatus.CALCULATED);
        PaymentRun saved = paymentRunRepository.save(run);
        return paymentRunMapper.toResponse(saved);
    }

    /**
     * Approves the payment run, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the PaymentRunResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PaymentRunResponse approvePaymentRun(Long id) {
        User currentUser = getCurrentUser();
        PaymentRun run = paymentRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Run not found"));
        
        if (run.getStatus() != PaymentRunStatus.CALCULATED) {
            throw new BusinessException("Only CALCULATED payment runs can be approved");
        }

        run.setStatus(PaymentRunStatus.APPROVED);
        run.setApprovedBy(currentUser);
        run.setApprovedAt(LocalDateTime.now());
        PaymentRun saved = paymentRunRepository.save(run);
        return paymentRunMapper.toResponse(saved);
    }

    /**
     * Performs the executePaymentRun operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the PaymentRunResponse result
     */
    @Override
    @Transactional
    public PaymentRunResponse executePaymentRun(Long id) {
        User currentUser = getCurrentUser();
        PaymentRun run = paymentRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Run not found"));

        // Idempotency: Execution status validation
        if (run.getStatus() == PaymentRunStatus.COMPLETED) {
            return paymentRunMapper.toResponse(run);
        }
        if (run.getStatus() == PaymentRunStatus.PROCESSING) {
            throw new BusinessException("Payment run is currently processing");
        }
        if (run.getStatus() != PaymentRunStatus.APPROVED) {
            throw new BusinessException("Only APPROVED payment runs can be executed");
        }

        // Idempotency: client_reference_id validation
        if (run.getClientReferenceId() != null) {
            Optional<PaymentRun> duplicate = paymentRunRepository.findByClientReferenceId(run.getClientReferenceId());
            if (duplicate.isPresent() && !duplicate.get().getId().equals(run.getId())) {
                PaymentRun dupRun = duplicate.get();
                if (dupRun.getStatus() == PaymentRunStatus.COMPLETED || dupRun.getStatus() == PaymentRunStatus.PROCESSING) {
                    return paymentRunMapper.toResponse(dupRun);
                }
            }
        }

        // Mark processing to lock the run in the DB
        run.setStatus(PaymentRunStatus.PROCESSING);
        run.setExecutedBy(currentUser);
        run.setExecutedAt(LocalDateTime.now());
        paymentRunRepository.saveAndFlush(run);

        // Group invoices by Supplier to execute consolidated payments
        Map<Long, List<PaymentRunInvoice>> invoicesBySupplier = run.getInvoices().stream()
                .collect(Collectors.groupingBy(item -> item.getSupplierInvoice().getSupplier().getId()));

        int successCount = 0;
        int failedCount = 0;
        int invoiceCount = 0;
        String latestFailureReason = null;

        for (Map.Entry<Long, List<PaymentRunInvoice>> entry : invoicesBySupplier.entrySet()) {
            Long supplierId = entry.getKey();
            List<PaymentRunInvoice> supplierItems = entry.getValue();
            BigDecimal totalSupplierAmount = supplierItems.stream()
                    .map(PaymentRunInvoice::getPaymentAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            LocalDateTime startedAt = LocalDateTime.now();
            try {
                // Execute consolidated payment for this supplier in a separate REQUIRES_NEW transaction
                var paymentResponse = executionHelper.executeSupplierPayment(supplierId, supplierItems, run);

                // Record SUCCESS supplier result
                PaymentRunSupplierResult result = PaymentRunSupplierResult.builder()
                        .paymentRun(run)
                        .supplier(supplierItems.get(0).getSupplierInvoice().getSupplier())
                        .payment(entityManager.getReference(Payment.class, paymentResponse.getId()))
                        .status("SUCCESS")
                        .amount(totalSupplierAmount)
                        .startedAt(startedAt)
                        .completedAt(LocalDateTime.now())
                        .build();

                supplierResultRepository.save(result);
                run.getSupplierResults().add(result);

                successCount++;
                invoiceCount += supplierItems.size();
            } catch (Exception e) {
                // Record FAILED supplier result
                PaymentRunSupplierResult result = PaymentRunSupplierResult.builder()
                        .paymentRun(run)
                        .supplier(supplierItems.get(0).getSupplierInvoice().getSupplier())
                        .status("FAILED")
                        .amount(totalSupplierAmount)
                        .errorMessage(e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName())
                        .startedAt(startedAt)
                        .completedAt(LocalDateTime.now())
                        .build();

                supplierResultRepository.save(result);
                run.getSupplierResults().add(result);

                failedCount++;
                latestFailureReason = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            }
        }

        // Update run execution counters and metadata
        run.setSuccessfulPaymentsCount(successCount);
        run.setFailedPaymentsCount(failedCount);
        run.setProcessedInvoicesCount(invoiceCount);
        if (latestFailureReason != null) {
            run.setFailureReason(latestFailureReason);
        }

        // Release invoice reservations (so they are not locked forever, even if failed/partially failed)
        supplierInvoiceRepository.releaseInvoicesForPaymentRun(id);


        // Generate bank export files ONLY for successfully paid invoices
        List<PaymentRunInvoice> successfulInvoices = run.getInvoices().stream()
                .filter(item -> {
                    Long sId = item.getSupplierInvoice().getSupplier().getId();
                    return run.getSupplierResults().stream()
                            .anyMatch(r -> r.getSupplier().getId().equals(sId) && "SUCCESS".equals(r.getStatus()));
                })
                .collect(Collectors.toList());

        if (!successfulInvoices.isEmpty()) {
            BankExportGenerator generator = exportGenerators.stream()
                    .filter(g -> g.getFormatName().equalsIgnoreCase(run.getExportFormat()))
                    .findFirst()
                    .orElse(csvExportGenerator);

            String exportContent = generator.generate(run, successfulInvoices);

            String ext = run.getExportFormat().equalsIgnoreCase("CSV") ? "csv" : "xml";
            if (run.getExportFormat().equalsIgnoreCase("NEFT_RTGS")) {
                ext = "txt";
            }
            String fileName = String.format("%s_export.%s", run.getRunNumber(), ext);
            
            // Store bank export in filesystem and get metadata
            var fileMetadata = fileStorageService.storeExportFile(fileName, exportContent);
            
            run.setExportFileName(fileMetadata.fileName());
            run.setExportStoragePath(fileMetadata.storagePath());
            run.setExportChecksum(fileMetadata.checksum());
            run.setExportGeneratedAt(fileMetadata.generatedAt());
        }

        run.setStatus(PaymentRunStatus.COMPLETED);
        PaymentRun saved = paymentRunRepository.save(run);
        return paymentRunMapper.toResponse(saved);
    }

    /**
     * Cancels the payment run and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the PaymentRunResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public PaymentRunResponse cancelPaymentRun(Long id) {
        User currentUser = getCurrentUser();
        PaymentRun run = paymentRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Run not found"));

        if (run.getStatus() == PaymentRunStatus.COMPLETED ||
            run.getStatus() == PaymentRunStatus.PROCESSING ||
            run.getStatus() == PaymentRunStatus.CANCELLED) {
            throw new BusinessException("Cannot cancel payment run in " + run.getStatus() + " status");
        }

        // Release invoice reservations
        supplierInvoiceRepository.releaseInvoicesForPaymentRun(id);


        run.setStatus(PaymentRunStatus.CANCELLED);
        run.setCancelledBy(currentUser);
        run.setCancelledAt(LocalDateTime.now());
        PaymentRun saved = paymentRunRepository.save(run);
        return paymentRunMapper.toResponse(saved);
    }

    /**
     * Retrieves payment run dashboard data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return the PaymentRunDashboardResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public PaymentRunDashboardResponse getPaymentRunDashboard(Long companyId) {
        List<PaymentRun> runs = paymentRunRepository.findAll().stream()
                .filter(r -> r.getCompany().getId().equals(companyId))
                .collect(Collectors.toList());

        // Basic Financial KPIs
        long scheduledPaymentsCount = runs.stream()
                .filter(r -> r.getStatus() == PaymentRunStatus.APPROVED || r.getStatus() == PaymentRunStatus.PROCESSING)
                .count();

        BigDecimal scheduledPaymentsAmount = runs.stream()
                .filter(r -> r.getStatus() == PaymentRunStatus.APPROVED || r.getStatus() == PaymentRunStatus.PROCESSING)
                .map(PaymentRun::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Fetch unpaid or partially paid supplier invoices for outstanding due dates
        List<SupplierInvoice> openInvoices = supplierInvoiceRepository.findByCompanyId(companyId).stream()
                .filter(si -> si.getStatus() == SupplierInvoiceStatus.APPROVED || si.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID)
                .collect(Collectors.toList());

        BigDecimal paymentsDueTodayAmount = openInvoices.stream()
                .filter(si -> si.getDueDate() != null && si.getDueDate().equals(LocalDate.now()))
                .map(SupplierInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal paymentsDueThisWeekAmount = openInvoices.stream()
                .filter(si -> si.getDueDate() != null && !si.getDueDate().isAfter(LocalDate.now().plusDays(7)))
                .map(SupplierInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal cashRequiredAmount = runs.stream()
                .filter(r -> r.getStatus() != PaymentRunStatus.COMPLETED && r.getStatus() != PaymentRunStatus.CANCELLED)
                .map(PaymentRun::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal executedPaymentsAmount = runs.stream()
                .filter(r -> r.getStatus() == PaymentRunStatus.COMPLETED)
                .map(PaymentRun::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long failedPaymentsCount = runs.stream()
                .mapToLong(PaymentRun::getFailedPaymentsCount)
                .sum();

        // Rich Operational KPIs
        long totalPaymentRunsCount = runs.size();
        long completedTodayCount = runs.stream()
                .filter(r -> r.getStatus() == PaymentRunStatus.COMPLETED && r.getExecutedAt() != null && r.getExecutedAt().toLocalDate().equals(LocalDate.now()))
                .count();

        long processingNowCount = runs.stream()
                .filter(r -> r.getStatus() == PaymentRunStatus.PROCESSING)
                .count();

        long cancelledRunsCount = runs.stream()
                .filter(r -> r.getStatus() == PaymentRunStatus.CANCELLED)
                .count();

        List<PaymentRun> completedRuns = runs.stream()
                .filter(r -> r.getStatus() == PaymentRunStatus.COMPLETED)
                .collect(Collectors.toList());

        double avgBatchSizeVal = completedRuns.stream()
                .mapToDouble(r -> r.getInvoices().size())
                .average()
                .orElse(0.0);
        BigDecimal averagePaymentBatchSize = BigDecimal.valueOf(avgBatchSizeVal).setScale(2, RoundingMode.HALF_UP);

        BigDecimal largestPaymentRunAmount = completedRuns.stream()
                .map(PaymentRun::getTotalAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        double avgExecutionSeconds = completedRuns.stream()
                .filter(r -> r.getExecutedAt() != null && r.getCreatedAt() != null)
                .mapToLong(r -> java.time.Duration.between(r.getCreatedAt(), r.getExecutedAt()).toSeconds())
                .average()
                .orElse(0.0);
        BigDecimal averageExecutionTimeSeconds = BigDecimal.valueOf(avgExecutionSeconds).setScale(2, RoundingMode.HALF_UP);

        return new PaymentRunDashboardResponse(
                companyId,
                scheduledPaymentsCount,
                scheduledPaymentsAmount,
                paymentsDueTodayAmount,
                paymentsDueThisWeekAmount,
                cashRequiredAmount,
                executedPaymentsAmount,
                failedPaymentsCount,
                totalPaymentRunsCount,
                completedTodayCount,
                processingNowCount,
                cancelledRunsCount,
                averagePaymentBatchSize,
                largestPaymentRunAmount,
                averageExecutionTimeSeconds
        );
    }

    /**
     * Retrieves a single payment run by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the PaymentRunResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public PaymentRunResponse getPaymentRunById(Long id) {
        PaymentRun run = paymentRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Run not found"));
        return paymentRunMapper.toResponse(run);
    }

    /**
     * Returns a filtered paginated list of payment runs records.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param status status filter for narrowing query results
     * @return List of matching records
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaymentRunResponse> searchPaymentRuns(Long companyId, String status) {
        List<PaymentRun> runs = paymentRunRepository.findAll().stream()
                .filter(r -> r.getCompany().getId().equals(companyId))
                .filter(r -> status == null || r.getStatus().name().equalsIgnoreCase(status))
                .collect(Collectors.toList());
        return paymentRunMapper.toResponseList(runs);
    }

    private User getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return userRepository.findByEmail("admin@plus33.com")
                    .orElseThrow(() -> new ResourceNotFoundException("Default admin user not found"));
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found with email: " + email));
    }
}