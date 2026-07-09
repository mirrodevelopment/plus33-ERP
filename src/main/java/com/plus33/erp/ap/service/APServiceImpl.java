/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.service
 * File              : APServiceImpl.java
 * Purpose           : Business logic service layer for Ap Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: APController
 * Related Service   : APServiceImpl
 * Related Repository: SupplierInvoiceRepository, SupplierRepository, PaymentRepository, PaymentAllocationRepository, CompanyRepository
 * Related Entity    : AP
 * Related DTO       : APAgingResponse, APAnalyticsResponse, APDashboardResponse, APOverdueBillResponse, CashRequirementDTO
 * Related Mapper    : APMapper
 * Related DB Table  : a_ps
 * Related REST APIs : N/A
 * Depends On        : Common Module, Finance Module, Organization Module, Procurement Module
 * Used By           : APController, APServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Ap Module. Implements APService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.ap.service;

import com.plus33.erp.ap.dto.*;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.repository.PaymentAllocationRepository;
import com.plus33.erp.finance.repository.PaymentRepository;
import com.plus33.erp.finance.repository.SupplierInvoiceRepository;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.procurement.repository.SupplierRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <b>PLUS33 Coffee ERP -- Ap Module</b>
 *
 * <p><b>Class  :</b> {@code APServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.ap.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Ap Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * APController
 *   --> APServiceImpl (this)
 *   --> Validate business rules
 *   --> APRepository (read/write 'a_ps')
 *   --> APMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code a_ps}</p>
 * <p><b>Module Deps      :</b> Ap, Common, Finance, Organization, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class APServiceImpl implements APService {

    private final SupplierInvoiceRepository supplierInvoiceRepository;
    private final SupplierRepository supplierRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentAllocationRepository paymentAllocationRepository;
    private final CompanyRepository companyRepository;

    public APServiceImpl(
            SupplierInvoiceRepository supplierInvoiceRepository,
            SupplierRepository supplierRepository,
            PaymentRepository paymentRepository,
            PaymentAllocationRepository paymentAllocationRepository,
            CompanyRepository companyRepository) {
        this.supplierInvoiceRepository = supplierInvoiceRepository;
        this.supplierRepository = supplierRepository;
        this.paymentRepository = paymentRepository;
        this.paymentAllocationRepository = paymentAllocationRepository;
        this.companyRepository = companyRepository;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // AP Dashboard
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Retrieves a p dashboard data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return the APDashboardResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a p dashboard data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return the APDashboardResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public APDashboardResponse getAPDashboard(Long companyId) {
        validateCompany(companyId);

        List<SupplierInvoice> invoices = supplierInvoiceRepository.findByCompanyId(companyId);
        LocalDate today = LocalDate.now();

        // 1. Outstanding payables (APPROVED / PARTIALLY_PAID)
        BigDecimal totalPayables = invoices.stream()
                .filter(inv -> inv.getStatus() == SupplierInvoiceStatus.APPROVED || inv.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID)
                .map(SupplierInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // 2. Current Due (due_date >= today)
        BigDecimal currentDue = invoices.stream()
                .filter(inv -> (inv.getStatus() == SupplierInvoiceStatus.APPROVED || inv.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID)
                        && (inv.getDueDate() == null || !inv.getDueDate().isBefore(today)))
                .map(SupplierInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // 3. Overdue (due_date < today)
        BigDecimal overdue = invoices.stream()
                .filter(inv -> (inv.getStatus() == SupplierInvoiceStatus.APPROVED || inv.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID)
                        && inv.getDueDate() != null && inv.getDueDate().isBefore(today))
                .map(SupplierInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // 4. Due Today
        BigDecimal dueToday = invoices.stream()
                .filter(inv -> (inv.getStatus() == SupplierInvoiceStatus.APPROVED || inv.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID)
                        && inv.getDueDate() != null && inv.getDueDate().equals(today))
                .map(SupplierInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // 5. Due This Week (next 7 days, including today)
        LocalDate endOfWeek = today.plusDays(6);
        BigDecimal dueThisWeek = invoices.stream()
                .filter(inv -> (inv.getStatus() == SupplierInvoiceStatus.APPROVED || inv.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID)
                        && inv.getDueDate() != null
                        && !inv.getDueDate().isBefore(today)
                        && !inv.getDueDate().isAfter(endOfWeek))
                .map(SupplierInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // 6. Paid This Month (completed PAYABLE payments in current month)
        List<Payment> payments = paymentRepository.findByCompanyId(companyId);
        BigDecimal paidThisMonth = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.COMPLETED
                        && p.getSupplier() != null
                        && p.getPaymentDate().getMonth() == today.getMonth()
                        && p.getPaymentDate().getYear() == today.getYear())
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // 7. Open Bills Count
        long openBills = invoices.stream()
                .filter(inv -> inv.getStatus() == SupplierInvoiceStatus.APPROVED || inv.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID)
                .count();

        // 8. Average Payment Days
        BigDecimal averagePaymentDays = calculateAveragePaymentDays(invoices);

        // 9. Top 10 Suppliers
        List<TopSupplierDTO> topSuppliers = calculateTopSuppliers(invoices, 10);

        // 10. Cash Requirement (30/60/90 days cumulative)
        CashRequirementDTO cashRequirement = calculateCashRequirement(invoices, today);

        // 11. Bills Awaiting Approval (SUBMITTED status)
        long billsAwaitingApproval = invoices.stream()
                .filter(inv -> inv.getStatus() == SupplierInvoiceStatus.SUBMITTED)
                .count();

        // 12. Bills Due Today (count)
        long billsDueToday = invoices.stream()
                .filter(inv -> (inv.getStatus() == SupplierInvoiceStatus.APPROVED || inv.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID)
                        && inv.getDueDate() != null && inv.getDueDate().equals(today))
                .count();

        return new APDashboardResponse(
                companyId,
                totalPayables,
                currentDue,
                overdue,
                dueToday,
                dueThisWeek,
                paidThisMonth,
                openBills,
                averagePaymentDays,
                topSuppliers,
                cashRequirement,
                billsAwaitingApproval,
                billsDueToday
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Configurable Aging Report
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Retrieves aging report data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param supplierId the supplierId input value
     * @param intervals the intervals input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves aging report data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param supplierId the supplierId input value
     * @param intervals the intervals input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<APAgingResponse> getAgingReport(Long companyId, Long supplierId, List<Integer> intervals) {
        validateCompany(companyId);

        List<Integer> sortedIntervals = new ArrayList<>(intervals != null && !intervals.isEmpty() ? intervals : List.of(30, 60, 90));
        sortedIntervals.sort(Integer::compareTo);

        // Generate bucket names
        List<String> bucketNames = new ArrayList<>();
        bucketNames.add("Current");
        for (int i = 0; i < sortedIntervals.size(); i++) {
            if (i == 0) {
                bucketNames.add("1-" + sortedIntervals.get(0));
            } else {
                bucketNames.add((sortedIntervals.get(i - 1) + 1) + "-" + sortedIntervals.get(i));
            }
        }
        bucketNames.add(sortedIntervals.get(sortedIntervals.size() - 1) + "+");

        // Fetch target suppliers
        List<Supplier> suppliers = new ArrayList<>();
        if (supplierId != null) {
            Supplier supplier = supplierRepository.findById(supplierId)
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + supplierId));
            if (!supplier.getCompany().getId().equals(companyId)) {
                throw new ResourceNotFoundException("Supplier not found for company ID: " + companyId);
            }
            suppliers.add(supplier);
        } else {
            Specification<Supplier> spec = (root, query, cb) -> cb.equal(root.get("company").get("id"), companyId);
            suppliers = supplierRepository.findAll(spec);
        }

        List<APAgingResponse> agingResponses = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Supplier supplier : suppliers) {
            Specification<SupplierInvoice> invSpec = (root, query, cb) -> cb.and(
                    cb.equal(root.get("supplier").get("id"), supplier.getId()),
                    cb.equal(root.get("company").get("id"), companyId),
                    root.get("status").in(SupplierInvoiceStatus.APPROVED, SupplierInvoiceStatus.PARTIALLY_PAID)
            );
            List<SupplierInvoice> supplierInvoices = supplierInvoiceRepository.findAll(invSpec);

            BigDecimal totalOutstanding = BigDecimal.ZERO;
            Map<String, BigDecimal> bucketAmounts = new HashMap<>();
            for (String name : bucketNames) {
                bucketAmounts.put(name, BigDecimal.ZERO);
            }

            for (SupplierInvoice inv : supplierInvoices) {
                BigDecimal balance = inv.getOutstandingBalance();
                totalOutstanding = totalOutstanding.add(balance);

                if (inv.getDueDate() == null || !inv.getDueDate().isBefore(today)) {
                    bucketAmounts.put("Current", bucketAmounts.get("Current").add(balance));
                } else {
                    long daysPastDue = ChronoUnit.DAYS.between(inv.getDueDate(), today);
                    String matchedBucket = null;
                    for (int i = 0; i < sortedIntervals.size(); i++) {
                        int limit = sortedIntervals.get(i);
                        if (daysPastDue <= limit) {
                            if (i == 0) {
                                matchedBucket = "1-" + limit;
                            } else {
                                matchedBucket = (sortedIntervals.get(i - 1) + 1) + "-" + limit;
                            }
                            break;
                        }
                    }
                    if (matchedBucket == null) {
                        matchedBucket = sortedIntervals.get(sortedIntervals.size() - 1) + "+";
                    }
                    bucketAmounts.put(matchedBucket, bucketAmounts.get(matchedBucket).add(balance));
                }
            }

            List<APAgingBucket> buckets = new ArrayList<>();
            for (String name : bucketNames) {
                buckets.add(new APAgingBucket(name, bucketAmounts.get(name).setScale(2, RoundingMode.HALF_UP)));
            }

            agingResponses.add(new APAgingResponse(
                    supplier.getId(),
                    supplier.getName(),
                    companyId,
                    totalOutstanding.setScale(2, RoundingMode.HALF_UP),
                    buckets
            ));
        }

        // Sort aging reports by totalOutstanding descending
        agingResponses.sort(Comparator.comparing(APAgingResponse::totalOutstanding).reversed());
        return agingResponses;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Supplier AP Balance
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Retrieves supplier a p balance data from the database.
     *
     * @param supplierId the supplierId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return the SupplierAPBalanceResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves supplier a p balance data from the database.
     *
     * @param supplierId the supplierId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return the SupplierAPBalanceResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public SupplierAPBalanceResponse getSupplierAPBalance(Long supplierId, Long companyId) {
        validateCompany(companyId);

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + supplierId));
        if (!supplier.getCompany().getId().equals(companyId)) {
            throw new ResourceNotFoundException("Supplier not found for company ID: " + companyId);
        }

        Specification<SupplierInvoice> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("supplier").get("id"), supplierId),
                cb.equal(root.get("company").get("id"), companyId),
                root.get("status").in(
                        SupplierInvoiceStatus.APPROVED,
                        SupplierInvoiceStatus.PARTIALLY_PAID,
                        SupplierInvoiceStatus.PAID
                )
        );
        List<SupplierInvoice> invoices = supplierInvoiceRepository.findAll(spec);
        LocalDate today = LocalDate.now();

        BigDecimal totalOutstanding = invoices.stream()
                .filter(inv -> inv.getStatus() != SupplierInvoiceStatus.PAID)
                .map(SupplierInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalOverdue = invoices.stream()
                .filter(inv -> inv.getStatus() != SupplierInvoiceStatus.PAID
                        && inv.getDueDate() != null
                        && inv.getDueDate().isBefore(today))
                .map(SupplierInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalPaid = invoices.stream()
                .map(SupplierInvoice::getPaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        return new SupplierAPBalanceResponse(
                supplierId,
                supplier.getName(),
                companyId,
                totalOutstanding,
                totalOverdue,
                totalPaid,
                BigDecimal.ZERO // totalCredited is 0.00 (future feature)
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Supplier Chronological Statement
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Retrieves supplier statement data from the database.
     *
     * @param supplierId the supplierId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @param from the from input value
     * @param to the to input value
     * @return the SupplierStatementResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves supplier statement data from the database.
     *
     * @param supplierId the supplierId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @param from the from input value
     * @param to the to input value
     * @return the SupplierStatementResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public SupplierStatementResponse getSupplierStatement(Long supplierId, Long companyId, LocalDate from, LocalDate to) {
        validateCompany(companyId);

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + supplierId));
        if (!supplier.getCompany().getId().equals(companyId)) {
            throw new ResourceNotFoundException("Supplier not found for company ID: " + companyId);
        }

        Specification<SupplierInvoice> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("supplier").get("id"), supplierId),
                cb.equal(root.get("company").get("id"), companyId),
                root.get("status").in(
                        SupplierInvoiceStatus.APPROVED,
                        SupplierInvoiceStatus.PARTIALLY_PAID,
                        SupplierInvoiceStatus.PAID,
                        SupplierInvoiceStatus.CANCELLED
                )
        );
        List<SupplierInvoice> invoices = supplierInvoiceRepository.findAll(spec);

        BigDecimal openingBalance = BigDecimal.ZERO;
        List<SupplierStatementEntry> entriesInRange = new ArrayList<>();

        for (SupplierInvoice inv : invoices) {
            LocalDate invDate = inv.getInvoiceDate();

            // 1. Process the original bill
            if (invDate.isBefore(from)) {
                openingBalance = openingBalance.add(inv.getTotalAmount()); // Credits increase liability
            } else if (!invDate.isAfter(to)) {
                entriesInRange.add(new SupplierStatementEntry(
                        invDate,
                        inv.getInvoiceNumber(),
                        "BILL",
                        "Invoice " + inv.getInvoiceNumber(),
                        BigDecimal.ZERO,
                        inv.getTotalAmount(),
                        BigDecimal.ZERO
                ));
            }

            // 2. Process cancellations if status is CANCELLED
            if (inv.getStatus() == SupplierInvoiceStatus.CANCELLED) {
                LocalDate cancelDate = inv.getUpdatedAt().toLocalDate();
                if (cancelDate.isBefore(from)) {
                    openingBalance = openingBalance.subtract(inv.getTotalAmount()); // Debits decrease liability
                } else if (!cancelDate.isAfter(to)) {
                    entriesInRange.add(new SupplierStatementEntry(
                            cancelDate,
                            inv.getInvoiceNumber(),
                            "BILL_CANCEL",
                            "Cancelled Invoice " + inv.getInvoiceNumber(),
                            inv.getTotalAmount(),
                            BigDecimal.ZERO,
                            BigDecimal.ZERO
                    ));
                }
            }

            // 3. Process allocations (payments)
            List<PaymentAllocation> allocations = paymentAllocationRepository.findBySupplierInvoiceId(inv.getId());
            for (PaymentAllocation alloc : allocations) {
                if (alloc.getPayment().getStatus() == PaymentStatus.COMPLETED) {
                    LocalDate payDate = alloc.getPayment().getPaymentDate();
                    if (payDate.isBefore(from)) {
                        openingBalance = openingBalance.subtract(alloc.getAllocatedAmount()); // Debits decrease liability
                    } else if (!payDate.isAfter(to)) {
                        entriesInRange.add(new SupplierStatementEntry(
                                payDate,
                                alloc.getPayment().getPaymentNumber(),
                                "PAYMENT",
                                "Payment " + alloc.getPayment().getPaymentNumber() + " against " + inv.getInvoiceNumber(),
                                alloc.getAllocatedAmount(),
                                BigDecimal.ZERO,
                                BigDecimal.ZERO
                        ));
                    }
                }
            }
        }

        // Sort entries: date ascending, then type
        entriesInRange.sort(Comparator.comparing(SupplierStatementEntry::entryDate)
                .thenComparing(SupplierStatementEntry::entryType));

        // Compute running balance line-by-line starting from opening balance
        BigDecimal running = openingBalance;
        List<SupplierStatementEntry> ledger = new ArrayList<>();
        for (SupplierStatementEntry e : entriesInRange) {
            running = running.subtract(e.debitAmount()).add(e.creditAmount());
            ledger.add(new SupplierStatementEntry(
                    e.entryDate(),
                    e.referenceNumber(),
                    e.entryType(),
                    e.description(),
                    e.debitAmount(),
                    e.creditAmount(),
                    running.setScale(2, RoundingMode.HALF_UP)
            ));
        }

        BigDecimal closingBalance = running;

        return new SupplierStatementResponse(
                supplierId,
                supplier.getName(),
                companyId,
                from,
                to,
                openingBalance.setScale(2, RoundingMode.HALF_UP),
                closingBalance.setScale(2, RoundingMode.HALF_UP),
                ledger
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Overdue Bills
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Retrieves overdue bills data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves overdue bills data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public PageResponse<APOverdueBillResponse> getOverdueBills(Long companyId, Pageable pageable) {
        validateCompany(companyId);

        Specification<SupplierInvoice> spec = (root, query, cb) -> {
            LocalDate today = LocalDate.now();
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("company").get("id"), companyId));
            predicates.add(cb.isNotNull(root.get("dueDate")));
            predicates.add(cb.lessThan(root.get("dueDate"), today));
            predicates.add(cb.greaterThan(root.get("outstandingBalance"), BigDecimal.ZERO));
            predicates.add(root.get("status").in(SupplierInvoiceStatus.APPROVED, SupplierInvoiceStatus.PARTIALLY_PAID));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<SupplierInvoice> page = supplierInvoiceRepository.findAll(spec, pageable);
        LocalDate today = LocalDate.now();

        List<APOverdueBillResponse> content = page.getContent().stream()
                .map(inv -> new APOverdueBillResponse(
                        inv.getId(),
                        inv.getInvoiceNumber(),
                        inv.getSupplier().getId(),
                        inv.getSupplier().getName(),
                        inv.getCompany().getId(),
                        inv.getInvoiceDate(),
                        inv.getDueDate(),
                        ChronoUnit.DAYS.between(inv.getDueDate(), today),
                        inv.getTotalAmount(),
                        inv.getOutstandingBalance(),
                        inv.getStatus().name()
                ))
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // AP Analytics
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Retrieves a p analytics data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return the APAnalyticsResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a p analytics data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return the APAnalyticsResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public APAnalyticsResponse getAPAnalytics(Long companyId) {
        validateCompany(companyId);

        List<SupplierInvoice> invoices = supplierInvoiceRepository.findByCompanyId(companyId);
        LocalDate today = LocalDate.now();

        List<SupplierInvoice> postedInvoices = invoices.stream()
                .filter(inv -> inv.getStatus() == SupplierInvoiceStatus.APPROVED
                        || inv.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID
                        || inv.getStatus() == SupplierInvoiceStatus.PAID)
                .toList();

        // 1. Average Invoice Amount
        BigDecimal averageInvoiceAmount = BigDecimal.ZERO;
        if (!postedInvoices.isEmpty()) {
            BigDecimal totalSum = postedInvoices.stream()
                    .map(SupplierInvoice::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            averageInvoiceAmount = totalSum.divide(BigDecimal.valueOf(postedInvoices.size()), 2, RoundingMode.HALF_UP);
        }

        // 2. Average Days to Pay
        BigDecimal averageDaysToPay = calculateAveragePaymentDays(invoices);

        // 3. Early Payment Discounts Captured
        BigDecimal earlyPaymentDiscounts = postedInvoices.stream()
                .map(SupplierInvoice::getDiscountAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // 4. Supplier Concentration (Top 5 by outstanding)
        List<TopSupplierDTO> supplierConcentration = calculateTopSuppliers(invoices, 5);

        // 5. Largest Outstanding Suppliers (Top 5 by outstanding)
        List<TopSupplierDTO> largestOutstandingSuppliers = calculateTopSuppliers(invoices, 5);

        // 6. Cash Forecast (30/60/90 days cumulative)
        CashRequirementDTO cashForecast = calculateCashRequirement(invoices, today);

        return new APAnalyticsResponse(
                companyId,
                averageInvoiceAmount,
                averageDaysToPay,
                earlyPaymentDiscounts,
                supplierConcentration,
                largestOutstandingSuppliers,
                cashForecast
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private void validateCompany(Long companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("Company not found with ID: " + companyId);
        }
    }

    private BigDecimal calculateAveragePaymentDays(List<SupplierInvoice> invoices) {
        List<SupplierInvoice> paidInvoices = invoices.stream()
                .filter(inv -> inv.getStatus() == SupplierInvoiceStatus.PAID)
                .toList();

        if (paidInvoices.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        long totalDays = 0;
        int count = 0;

        for (SupplierInvoice inv : paidInvoices) {
            List<PaymentAllocation> allocs = paymentAllocationRepository.findBySupplierInvoiceId(inv.getId());
            LocalDate latestPayDate = null;
            for (PaymentAllocation alloc : allocs) {
                if (alloc.getPayment().getStatus() == PaymentStatus.COMPLETED) {
                    LocalDate payDate = alloc.getPayment().getPaymentDate();
                    if (latestPayDate == null || payDate.isAfter(latestPayDate)) {
                        latestPayDate = payDate;
                    }
                }
            }
            if (latestPayDate != null) {
                totalDays += ChronoUnit.DAYS.between(inv.getInvoiceDate(), latestPayDate);
                count++;
            }
        }

        if (count > 0) {
            return BigDecimal.valueOf(totalDays).divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    private List<TopSupplierDTO> calculateTopSuppliers(List<SupplierInvoice> invoices, int limit) {
        Map<Supplier, BigDecimal> supplierBalances = invoices.stream()
                .filter(inv -> inv.getStatus() == SupplierInvoiceStatus.APPROVED || inv.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID)
                .collect(Collectors.groupingBy(
                        SupplierInvoice::getSupplier,
                        Collectors.reducing(BigDecimal.ZERO, SupplierInvoice::getOutstandingBalance, BigDecimal::add)
                ));

        return supplierBalances.entrySet().stream()
                .map(entry -> new TopSupplierDTO(
                        entry.getKey().getId(),
                        entry.getKey().getName(),
                        entry.getValue().setScale(2, RoundingMode.HALF_UP)
                ))
                .sorted(Comparator.comparing(TopSupplierDTO::outstandingBalance).reversed())
                .limit(limit)
                .toList();
    }

    private CashRequirementDTO calculateCashRequirement(List<SupplierInvoice> invoices, LocalDate today) {
        BigDecimal next30 = invoices.stream()
                .filter(inv -> (inv.getStatus() == SupplierInvoiceStatus.APPROVED || inv.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID)
                        && (inv.getDueDate() == null || !inv.getDueDate().isAfter(today.plusDays(30))))
                .map(SupplierInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal next60 = invoices.stream()
                .filter(inv -> (inv.getStatus() == SupplierInvoiceStatus.APPROVED || inv.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID)
                        && (inv.getDueDate() == null || !inv.getDueDate().isAfter(today.plusDays(60))))
                .map(SupplierInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal next90 = invoices.stream()
                .filter(inv -> (inv.getStatus() == SupplierInvoiceStatus.APPROVED || inv.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID)
                        && (inv.getDueDate() == null || !inv.getDueDate().isAfter(today.plusDays(90))))
                .map(SupplierInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        return new CashRequirementDTO(next30, next60, next90);
    }
}