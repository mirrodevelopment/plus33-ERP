package com.plus33.erp.ar.service;

import com.plus33.erp.ar.dto.*;
import com.plus33.erp.ar.repository.ARWriteOffRepository;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.entity.PaymentAllocation;
import com.plus33.erp.finance.repository.PaymentAllocationRepository;
import com.plus33.erp.sales.entity.CreditNote;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerInvoice;
import com.plus33.erp.sales.entity.CustomerInvoiceStatus;
import com.plus33.erp.sales.repository.CreditNoteRepository;
import com.plus33.erp.sales.repository.CustomerInvoiceRepository;
import com.plus33.erp.sales.repository.CustomerRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ARServiceImpl implements ARService {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRepository customerRepository;
    private final CustomerInvoiceRepository customerInvoiceRepository;
    private final CreditNoteRepository creditNoteRepository;
    private final PaymentAllocationRepository paymentAllocationRepository;
    private final ARWriteOffRepository arWriteOffRepository;

    public ARServiceImpl(
            JdbcTemplate jdbcTemplate,
            CustomerRepository customerRepository,
            CustomerInvoiceRepository customerInvoiceRepository,
            CreditNoteRepository creditNoteRepository,
            PaymentAllocationRepository paymentAllocationRepository,
            ARWriteOffRepository arWriteOffRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRepository = customerRepository;
        this.customerInvoiceRepository = customerInvoiceRepository;
        this.creditNoteRepository = creditNoteRepository;
        this.paymentAllocationRepository = paymentAllocationRepository;
        this.arWriteOffRepository = arWriteOffRepository;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Aging Report
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public List<ARAgingResponse> getAgingReport(Long companyId, Long customerId) {
        String sql;
        Object[] params;

        if (customerId != null) {
            sql = """
                    SELECT company_id, customer_id, customer_name,
                           total_outstanding, aging_current, aging_1_30,
                           aging_31_60, aging_61_90, aging_90_plus
                    FROM mv_customer_aging
                    WHERE company_id = ? AND customer_id = ?
                    ORDER BY total_outstanding DESC
                    """;
            params = new Object[]{companyId, customerId};
        } else {
            sql = """
                    SELECT company_id, customer_id, customer_name,
                           total_outstanding, aging_current, aging_1_30,
                           aging_31_60, aging_61_90, aging_90_plus
                    FROM mv_customer_aging
                    WHERE company_id = ?
                    ORDER BY total_outstanding DESC
                    """;
            params = new Object[]{companyId};
        }

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ARAgingResponse(
                rs.getLong("company_id"),
                rs.getLong("customer_id"),
                rs.getString("customer_name"),
                rs.getBigDecimal("total_outstanding"),
                rs.getBigDecimal("aging_current"),
                rs.getBigDecimal("aging_1_30"),
                rs.getBigDecimal("aging_31_60"),
                rs.getBigDecimal("aging_61_90"),
                rs.getBigDecimal("aging_90_plus")
        ), params);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // AR Summary
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public ARSummaryResponse getARSummary(Long companyId) {
        // KPI totals from mv_sales_kpis
        String kpiSql = """
                SELECT total_invoices, total_invoiced_amount, total_paid_amount, total_outstanding_amount
                FROM mv_sales_kpis
                WHERE company_id = ?
                """;

        final long[] totalInvoices = {0L};
        final BigDecimal[] totalInvoicedAmount = {BigDecimal.ZERO};
        final BigDecimal[] totalPaidAmount = {BigDecimal.ZERO};
        final BigDecimal[] totalOutstandingAmount = {BigDecimal.ZERO};

        jdbcTemplate.query(kpiSql, rs -> {
            totalInvoices[0] = rs.getLong("total_invoices");
            totalInvoicedAmount[0] = rs.getBigDecimal("total_invoiced_amount");
            totalPaidAmount[0] = rs.getBigDecimal("total_paid_amount");
            totalOutstandingAmount[0] = rs.getBigDecimal("total_outstanding_amount");
        }, companyId);

        // Status breakdown from mv_receivables_dashboard
        String dashSql = """
                SELECT status, invoice_count, total_amount, outstanding_amount
                FROM mv_receivables_dashboard
                WHERE company_id = ?
                ORDER BY status
                """;

        List<ARStatusBucket> buckets = jdbcTemplate.query(dashSql,
                (rs, rowNum) -> new ARStatusBucket(
                        rs.getString("status"),
                        rs.getLong("invoice_count"),
                        rs.getBigDecimal("total_amount"),
                        rs.getBigDecimal("outstanding_amount")
                ), companyId);

        return new ARSummaryResponse(
                companyId,
                totalInvoices[0],
                totalInvoicedAmount[0],
                totalPaidAmount[0],
                totalOutstandingAmount[0],
                buckets
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Customer AR Balance
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public CustomerARBalanceResponse getCustomerARBalance(Long customerId, Long companyId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        if (!customer.getCompany().getId().equals(companyId)) {
            throw new ResourceNotFoundException("Customer not found for company ID: " + companyId);
        }

        // Overdue: outstanding > 0 AND due_date < today
        List<CustomerInvoice> overdueInvoices = customerInvoiceRepository.findAll(
                overdueSpec(companyId, customerId));

        BigDecimal totalOverdue = overdueInvoices.stream()
                .map(CustomerInvoice::getOutstandingBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Sum paid and credited from all non-cancelled invoices
        Specification<CustomerInvoice> allSpec = (root, query, cb) -> cb.and(
                cb.equal(root.get("company").get("id"), companyId),
                cb.equal(root.get("customer").get("id"), customerId),
                cb.notEqual(root.get("status"), CustomerInvoiceStatus.CANCELLED),
                cb.notEqual(root.get("status"), CustomerInvoiceStatus.VOID)
        );

        List<CustomerInvoice> allInvoices = customerInvoiceRepository.findAll(allSpec);
        BigDecimal totalPaid = allInvoices.stream().map(CustomerInvoice::getPaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCredited = allInvoices.stream().map(CustomerInvoice::getCreditedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CustomerARBalanceResponse(
                customerId,
                customer.getName(),
                companyId,
                customer.getCreditLimit(),
                customer.getOutstandingBalance(),
                totalOverdue,
                totalPaid,
                totalCredited
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Customer Statement
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public CustomerStatementResponse getCustomerStatement(Long customerId, Long companyId,
                                                          LocalDate from, LocalDate to) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        if (!customer.getCompany().getId().equals(companyId)) {
            throw new ResourceNotFoundException("Customer not found for company ID: " + companyId);
        }

        List<CustomerStatementEntry> entries = new ArrayList<>();

        // 1. Invoices in range (use invoiceDate)
        Specification<CustomerInvoice> invoiceSpec = (root, query, cb) -> cb.and(
                cb.equal(root.get("company").get("id"), companyId),
                cb.equal(root.get("customer").get("id"), customerId),
                cb.greaterThanOrEqualTo(root.get("invoiceDate"), from),
                cb.lessThanOrEqualTo(root.get("invoiceDate"), to)
        );
        customerInvoiceRepository.findAll(invoiceSpec).forEach(inv ->
                entries.add(new CustomerStatementEntry(
                        inv.getInvoiceDate(),
                        inv.getInvoiceNumber(),
                        "INVOICE",
                        "Invoice " + inv.getInvoiceNumber(),
                        inv.getTotalAmount(),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO // running balance computed below
                ))
        );

        // 2. Payments in range (via allocations → payment date)
        Specification<CustomerInvoice> allCustInvSpec = (root, query, cb) -> cb.and(
                cb.equal(root.get("company").get("id"), companyId),
                cb.equal(root.get("customer").get("id"), customerId)
        );
        List<CustomerInvoice> customerInvoices = customerInvoiceRepository.findAll(allCustInvSpec);

        customerInvoices.forEach(inv -> {
            List<PaymentAllocation> allocations = paymentAllocationRepository.findByCustomerInvoiceId(inv.getId());
            allocations.forEach(alloc -> {
                LocalDate payDate = alloc.getPayment().getPaymentDate();
                if (!payDate.isBefore(from) && !payDate.isAfter(to)) {
                    entries.add(new CustomerStatementEntry(
                            payDate,
                            alloc.getPayment().getPaymentNumber(),
                            "PAYMENT",
                            "Payment " + alloc.getPayment().getPaymentNumber()
                                    + " against " + inv.getInvoiceNumber(),
                            BigDecimal.ZERO,
                            alloc.getAllocatedAmount(),
                            BigDecimal.ZERO
                    ));
                }
            });
        });

        // 3. Credit notes in range (use createdAt date)
        Specification<CreditNote> cnSpec = (root, query, cb) -> cb.and(
                cb.equal(root.get("company").get("id"), companyId),
                cb.equal(root.get("customer").get("id"), customerId),
                cb.greaterThanOrEqualTo(root.get("createdAt").as(LocalDate.class), from),
                cb.lessThanOrEqualTo(root.get("createdAt").as(LocalDate.class), to)
        );
        creditNoteRepository.findAll(cnSpec).forEach(cn ->
                entries.add(new CustomerStatementEntry(
                        cn.getCreatedAt().toLocalDate(),
                        cn.getCreditNoteNumber(),
                        "CREDIT_NOTE",
                        "Credit Note " + cn.getCreditNoteNumber(),
                        BigDecimal.ZERO,
                        cn.getTotalAmount(),
                        BigDecimal.ZERO
                ))
        );

        // 4. Write-offs in range (use writeOffDate)
        arWriteOffRepository.findByCustomerIdAndCompanyId(customerId, companyId).stream()
                .filter(wo -> !wo.getWriteOffDate().isBefore(from) && !wo.getWriteOffDate().isAfter(to))
                .forEach(wo ->
                        entries.add(new CustomerStatementEntry(
                                wo.getWriteOffDate(),
                                wo.getWriteOffNumber(),
                                "WRITE_OFF",
                                "Write-off " + wo.getWriteOffNumber()
                                        + " against " + wo.getCustomerInvoice().getInvoiceNumber(),
                                BigDecimal.ZERO,
                                wo.getWriteOffAmount(),
                                BigDecimal.ZERO
                        ))
                );

        // Sort chronologically, compute running balance
        entries.sort(Comparator.comparing(CustomerStatementEntry::entryDate)
                .thenComparing(CustomerStatementEntry::entryType));

        BigDecimal running = BigDecimal.ZERO;
        List<CustomerStatementEntry> ledger = new ArrayList<>();
        for (CustomerStatementEntry e : entries) {
            running = running.add(e.debitAmount()).subtract(e.creditAmount());
            ledger.add(new CustomerStatementEntry(
                    e.entryDate(), e.referenceNumber(), e.entryType(),
                    e.description(), e.debitAmount(), e.creditAmount(), running));
        }

        BigDecimal openingBalance = BigDecimal.ZERO;
        BigDecimal closingBalance = ledger.isEmpty() ? BigDecimal.ZERO
                : ledger.get(ledger.size() - 1).runningBalance();

        return new CustomerStatementResponse(
                customerId, customer.getName(), companyId,
                from, to, openingBalance, closingBalance, ledger);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Overdue Invoices
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public PageResponse<AROverdueInvoiceResponse> getOverdueInvoices(Long companyId, Pageable pageable) {
        Specification<CustomerInvoice> spec = overdueSpec(companyId, null);
        Page<CustomerInvoice> page = customerInvoiceRepository.findAll(spec, pageable);

        LocalDate today = LocalDate.now();
        List<AROverdueInvoiceResponse> content = page.getContent().stream()
                .map(inv -> new AROverdueInvoiceResponse(
                        inv.getId(),
                        inv.getInvoiceNumber(),
                        inv.getCustomer().getId(),
                        inv.getCustomer().getName(),
                        inv.getCompany().getId(),
                        inv.getInvoiceDate(),
                        inv.getDueDate(),
                        ChronoUnit.DAYS.between(inv.getDueDate(), today),
                        inv.getTotalAmount(),
                        inv.getOutstandingBalance(),
                        inv.getStatus().name()
                ))
                .toList();

        return new PageResponse<>(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Shared Specifications
    // ─────────────────────────────────────────────────────────────────────────

    private Specification<CustomerInvoice> overdueSpec(Long companyId, Long customerId) {
        return (root, query, cb) -> {
            LocalDate today = LocalDate.now();
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("company").get("id"), companyId));
            predicates.add(cb.isNotNull(root.get("dueDate")));
            predicates.add(cb.lessThan(root.get("dueDate"), today));
            predicates.add(cb.greaterThan(root.get("outstandingBalance"), BigDecimal.ZERO));
            predicates.add(root.get("status").in(
                    CustomerInvoiceStatus.APPROVED,
                    CustomerInvoiceStatus.PARTIALLY_PAID,
                    CustomerInvoiceStatus.PARTIALLY_CREDITED
            ));
            if (customerId != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), customerId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
