package com.plus33.erp.workforce.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.workforce.dto.PayrollDashboardSummaryResponse;
import com.plus33.erp.workforce.dto.PayrollRunRequest;
import com.plus33.erp.workforce.dto.PayrollRunResponse;
import com.plus33.erp.workforce.entity.*;
import com.plus33.erp.workforce.event.PayrollCalculatedEvent;
import com.plus33.erp.workforce.event.PayrollPaidEvent;
import com.plus33.erp.workforce.event.PayrollPostedEvent;
import com.plus33.erp.workforce.event.PayrollReversedEvent;
import com.plus33.erp.workforce.payroll.PayrollEngineProvider;
import com.plus33.erp.workforce.payroll.PayrollEngineRegistry;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.repository.PayrollRunItemRepository;
import com.plus33.erp.workforce.repository.PayrollRunRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PayrollProcessingServiceImpl implements PayrollProcessingService {

    private final PayrollRunRepository payrollRunRepository;
    private final PayrollRunItemRepository payrollRunItemRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollEngineRegistry payrollEngineRegistry;
    private final PayrollJournalService payrollJournalService;
    private final PayrollAuditTimelineService auditTimelineService;
    private final ApplicationEventPublisher eventPublisher;

    public PayrollProcessingServiceImpl(PayrollRunRepository payrollRunRepository,
                                         PayrollRunItemRepository payrollRunItemRepository,
                                         EmployeeRepository employeeRepository,
                                         PayrollEngineRegistry payrollEngineRegistry,
                                         PayrollJournalService payrollJournalService,
                                         PayrollAuditTimelineService auditTimelineService,
                                         ApplicationEventPublisher eventPublisher) {
        this.payrollRunRepository = payrollRunRepository;
        this.payrollRunItemRepository = payrollRunItemRepository;
        this.employeeRepository = employeeRepository;
        this.payrollEngineRegistry = payrollEngineRegistry;
        this.payrollJournalService = payrollJournalService;
        this.auditTimelineService = auditTimelineService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public PayrollRunResponse createPayrollRun(PayrollRunRequest request) {
        PayrollRun run = new PayrollRun();
        run.setCompanyId(request.companyId());
        run.setPayrollPeriodId(request.payrollPeriodId());
        run.setRunNumber(request.runNumber());
        run.setPayrollCalendarType(request.calendarType() != null ? request.calendarType() : PayrollCalendarType.MONTHLY);
        run.setCountryCode(request.countryCode() != null ? request.countryCode() : "US");
        run.setRunType(request.runType() != null ? request.runType() : "REGULAR");
        run.setStatus(PayrollRunStatus.DRAFT);

        PayrollRun saved = payrollRunRepository.save(run);
        auditTimelineService.logAuditEvent(saved.getCompanyId(), saved.getId(), "PAYROLL_CREATED", "Payroll run created in DRAFT state", "SYSTEM");
        return toResponse(saved);
    }

    @Override
    @Transactional
    public PayrollRunResponse calculatePayrollRun(Long runId) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));

        List<Employee> employees = employeeRepository.findByCompanyId(run.getCompanyId());
        PayrollEngineProvider provider = payrollEngineRegistry.getProvider(run.getCountryCode());

        BigDecimal totalGross = BigDecimal.ZERO;
        BigDecimal totalNet = BigDecimal.ZERO;
        BigDecimal totalEmpCont = BigDecimal.ZERO;
        BigDecimal totalTaxes = BigDecimal.ZERO;

        payrollRunItemRepository.deleteAll(payrollRunItemRepository.findByPayrollRunId(runId));

        for (Employee emp : employees) {
            BigDecimal baseSalary = new BigDecimal("5000.00");
            PayrollEngineProvider.CalculationRequest req = new PayrollEngineProvider.CalculationRequest(emp.getId(), baseSalary, new BigDecimal("160"), BigDecimal.ZERO);
            PayrollEngineProvider.CalculationResult res = provider.calculateEmployeePayroll(req);

            PayrollRunItem item = new PayrollRunItem();
            item.setPayrollRunId(run.getId());
            item.setEmployeeId(emp.getId());
            item.setGrossPay(res.getGrossPay());
            item.setNetPay(res.getNetPay());
            item.setTotalDeductions(res.getTotalDeductions());
            item.setEmployerContributions(res.getEmployerContributions());
            item.setTaxWithheld(res.getTaxWithheld());
            item.setStatus("CALCULATED");
            payrollRunItemRepository.save(item);

            totalGross = totalGross.add(res.getGrossPay());
            totalNet = totalNet.add(res.getNetPay());
            totalEmpCont = totalEmpCont.add(res.getEmployerContributions());
            totalTaxes = totalTaxes.add(res.getTaxWithheld());
        }

        run.setTotalGross(totalGross);
        run.setTotalNet(totalNet);
        run.setTotalEmployerContributions(totalEmpCont);
        run.setTotalTaxes(totalTaxes);
        run.setStatus(PayrollRunStatus.CALCULATED);

        PayrollRun saved = payrollRunRepository.save(run);
        auditTimelineService.logAuditEvent(saved.getCompanyId(), saved.getId(), "PAYROLL_CALCULATED", "Payroll calculated for " + employees.size() + " employees", "SYSTEM");
        eventPublisher.publishEvent(new PayrollCalculatedEvent(saved.getId(), saved.getCompanyId()));

        return toResponse(saved);
    }

    @Override
    @Transactional
    public PayrollRunResponse approvePayrollRun(Long runId, String approver) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));
        run.setStatus(PayrollRunStatus.APPROVED);
        run.setApprovedBy(approver != null ? approver : "MANAGER");

        PayrollRun saved = payrollRunRepository.save(run);
        auditTimelineService.logAuditEvent(saved.getCompanyId(), saved.getId(), "PAYROLL_APPROVED", "Payroll approved by " + run.getApprovedBy(), run.getApprovedBy());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public PayrollRunResponse postPayrollRun(Long runId) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));
        payrollJournalService.postPayrollJournal(run);
        run.setStatus(PayrollRunStatus.POSTED);
        run.setPostedAt(LocalDateTime.now());

        PayrollRun saved = payrollRunRepository.save(run);
        auditTimelineService.logAuditEvent(saved.getCompanyId(), saved.getId(), "PAYROLL_POSTED", "Payroll posted to GL", "SYSTEM");
        eventPublisher.publishEvent(new PayrollPostedEvent(saved.getId(), saved.getCompanyId()));
        return toResponse(saved);
    }

    @Override
    @Transactional
    public PayrollRunResponse payPayrollRun(Long runId) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));
        run.setStatus(PayrollRunStatus.PAID);
        run.setPaidAt(LocalDateTime.now());

        PayrollRun saved = payrollRunRepository.save(run);
        auditTimelineService.logAuditEvent(saved.getCompanyId(), saved.getId(), "PAYROLL_PAID", "Payroll disbursal executed", "SYSTEM");
        eventPublisher.publishEvent(new PayrollPaidEvent(saved.getId(), saved.getCompanyId()));
        return toResponse(saved);
    }

    @Override
    @Transactional
    public PayrollRunResponse reversePayrollRun(Long runId) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));
        run.setStatus(PayrollRunStatus.REVERSED);

        PayrollRun saved = payrollRunRepository.save(run);
        auditTimelineService.logAuditEvent(saved.getCompanyId(), saved.getId(), "PAYROLL_REVERSED", "Payroll run reversed", "SYSTEM");
        eventPublisher.publishEvent(new PayrollReversedEvent(saved.getId(), saved.getCompanyId()));
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PayrollRunResponse getPayrollRun(Long runId) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));
        return toResponse(run);
    }

    @Override
    @Transactional(readOnly = true)
    public PayrollDashboardSummaryResponse getDashboardSummary(Long companyId) {
        List<PayrollRun> runs = payrollRunRepository.findByCompanyId(companyId);
        long totalRuns = runs.size();
        BigDecimal aggregateGross = runs.stream().map(PayrollRun::getTotalGross).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal aggregateNet = runs.stream().map(PayrollRun::getTotalNet).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal aggregateEmpCont = runs.stream().map(PayrollRun::getTotalEmployerContributions).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal aggregateTaxes = runs.stream().map(PayrollRun::getTotalTaxes).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PayrollDashboardSummaryResponse(companyId, totalRuns, aggregateGross, aggregateNet, aggregateEmpCont, aggregateTaxes);
    }

    private PayrollRunResponse toResponse(PayrollRun run) {
        return new PayrollRunResponse(
                run.getId(),
                run.getCompanyId(),
                run.getRunNumber(),
                run.getPayrollCalendarType(),
                run.getCountryCode(),
                run.getRunType(),
                run.getStatus(),
                run.getTotalGross(),
                run.getTotalNet(),
                run.getTotalEmployerContributions(),
                run.getTotalTaxes(),
                run.getExecutedBy(),
                run.getApprovedBy(),
                run.getPostedAt(),
                run.getPaidAt(),
                run.getCreatedAt()
        );
    }
}
