/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : PayrollProcessingServiceImpl.java
 * Purpose           : Business logic service layer for Workforce Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollProcessingController
 * Related Service   : PayrollProcessingServiceImpl
 * Related Repository: PayrollRunRepository, PayrollRunItemRepository, EmployeeRepository
 * Related Entity    : PayrollProcessing
 * Related DTO       : CalculationRequest, PayrollDashboardSummaryResponse, PayrollRunRequest, PayrollRunResponse, toResponse
 * Related Mapper    : PayrollProcessingMapper
 * Related DB Table  : payroll_processings
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : PayrollProcessingController, PayrollProcessingServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Workforce Module. Implements PayrollProcessingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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
import com.plus33.erp.workforce.repository.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollProcessingServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Workforce Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PayrollProcessingController
 *   --> PayrollProcessingServiceImpl (this)
 *   --> Validate business rules
 *   --> PayrollProcessingRepository (read/write 'payroll_processings')
 *   --> PayrollProcessingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code payroll_processings}</p>
 * <p><b>Module Deps      :</b> Common, Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PayrollProcessingServiceImpl implements PayrollProcessingService {

    private final PayrollRunRepository payrollRunRepository;
    private final PayrollRunItemRepository payrollRunItemRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollEngineRegistry payrollEngineRegistry;
    private final PayrollJournalService payrollJournalService;
    private final PayrollAuditTimelineService auditTimelineService;
    private final ApplicationEventPublisher eventPublisher;

    private final CountryBenefitPolicyRepository countryBenefitPolicyRepository;
    private final CountryWorkPolicyRepository countryWorkPolicyRepository;
    private final EmployeeSalaryStructureRepository employeeSalaryStructureRepository;
    private final EmployeeSalaryStructureItemRepository employeeSalaryStructureItemRepository;
    private final SalaryComponentRepository salaryComponentRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceBreakRepository attendanceBreakRepository;
    private final EmployeeLeaveRepository employeeLeaveRepository;
    private final HolidayCalendarRepository holidayCalendarRepository;
    private final PayrollPeriodRepository payrollPeriodRepository;
    private final UserStoreRepository userStoreRepository;

    public PayrollProcessingServiceImpl(PayrollRunRepository payrollRunRepository,
                                         PayrollRunItemRepository payrollRunItemRepository,
                                         EmployeeRepository employeeRepository,
                                         PayrollEngineRegistry payrollEngineRegistry,
                                         PayrollJournalService payrollJournalService,
                                         PayrollAuditTimelineService auditTimelineService,
                                         ApplicationEventPublisher eventPublisher,
                                         CountryBenefitPolicyRepository countryBenefitPolicyRepository,
                                         CountryWorkPolicyRepository countryWorkPolicyRepository,
                                         EmployeeSalaryStructureRepository employeeSalaryStructureRepository,
                                         EmployeeSalaryStructureItemRepository employeeSalaryStructureItemRepository,
                                         SalaryComponentRepository salaryComponentRepository,
                                         AttendanceRepository attendanceRepository,
                                         AttendanceBreakRepository attendanceBreakRepository,
                                         EmployeeLeaveRepository employeeLeaveRepository,
                                         HolidayCalendarRepository holidayCalendarRepository,
                                         PayrollPeriodRepository payrollPeriodRepository,
                                         UserStoreRepository userStoreRepository) {
        this.payrollRunRepository = payrollRunRepository;
        this.payrollRunItemRepository = payrollRunItemRepository;
        this.employeeRepository = employeeRepository;
        this.payrollEngineRegistry = payrollEngineRegistry;
        this.payrollJournalService = payrollJournalService;
        this.auditTimelineService = auditTimelineService;
        this.eventPublisher = eventPublisher;
        this.countryBenefitPolicyRepository = countryBenefitPolicyRepository;
        this.countryWorkPolicyRepository = countryWorkPolicyRepository;
        this.employeeSalaryStructureRepository = employeeSalaryStructureRepository;
        this.employeeSalaryStructureItemRepository = employeeSalaryStructureItemRepository;
        this.salaryComponentRepository = salaryComponentRepository;
        this.attendanceRepository = attendanceRepository;
        this.attendanceBreakRepository = attendanceBreakRepository;
        this.employeeLeaveRepository = employeeLeaveRepository;
        this.holidayCalendarRepository = holidayCalendarRepository;
        this.payrollPeriodRepository = payrollPeriodRepository;
        this.userStoreRepository = userStoreRepository;
    }

    /**
     * Creates a new payroll run and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the PayrollRunResponse result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Calculates payroll run totals including subtotal, tax, discounts, and net amount.
     *
     * @param runId the runId input value
     * @return the PayrollRunResponse result
     */
    @Override
    @Transactional
    public PayrollRunResponse calculatePayrollRun(Long runId) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));

        LocalDate start;
        LocalDate end;
        if (run.getPayrollPeriodId() != null) {
            PayrollPeriod period = payrollPeriodRepository.findById(run.getPayrollPeriodId())
                    .orElseThrow(() -> new ResourceNotFoundException("Payroll period not found: " + run.getPayrollPeriodId()));
            start = period.getStartDate();
            end = period.getEndDate();
        } else {
            start = LocalDate.now().withDayOfMonth(1);
            end = LocalDate.now().with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());
        }

        List<Employee> employees = employeeRepository.findByCompanyId(run.getCompanyId());

        BigDecimal totalGross = BigDecimal.ZERO;
        BigDecimal totalNet = BigDecimal.ZERO;
        BigDecimal totalEmpCont = BigDecimal.ZERO;
        BigDecimal totalTaxes = BigDecimal.ZERO;

        payrollRunItemRepository.deleteAll(payrollRunItemRepository.findByPayrollRunId(runId));

        for (Employee emp : employees) {
            String countryCode = "FR";
            if (emp.getUser() != null) {
                List<UserStore> userStores = userStoreRepository.findByIdUserId(emp.getUser().getId());
                if (userStores != null && !userStores.isEmpty()) {
                    com.plus33.erp.organization.entity.Store store = userStores.get(0).getStore();
                    if (store != null && store.getRegion() != null && store.getRegion().getCode() != null) {
                        String code = store.getRegion().getCode().toUpperCase();
                        if (code.startsWith("FR")) countryCode = "FR";
                        else if (code.startsWith("IN")) countryCode = "IN";
                        else if (code.startsWith("AE") || code.startsWith("UAE")) countryCode = "AE";
                        else if (code.startsWith("EU")) countryCode = "EU";
                        else countryCode = code;
                    }
                }
            }

            CountryWorkPolicy workPolicy = countryWorkPolicyRepository.findByCountryCode(countryCode).orElse(new CountryWorkPolicy());
            CountryBenefitPolicy benefitPolicy = countryBenefitPolicyRepository.findByCountryCode(countryCode).orElse(new CountryBenefitPolicy());

            Optional<EmployeeSalaryStructure> structOpt = employeeSalaryStructureRepository
                    .findByCompanyIdAndEmployeeIdAndStatus(run.getCompanyId(), emp.getId(), "ACTIVE");

            BigDecimal allowancesAmount = BigDecimal.ZERO;
            BigDecimal basicSalaryAmount = BigDecimal.valueOf(2400.00);
            BigDecimal hourlyRateVal = BigDecimal.valueOf(15.00);
            String currencyVal = "EUR";
            String empType = emp.getEmploymentType() != null ? emp.getEmploymentType() : "Permanent";

            if (structOpt.isPresent()) {
                currencyVal = structOpt.get().getCurrencyCode() != null ? structOpt.get().getCurrencyCode() : "EUR";
                List<EmployeeSalaryStructureItem> items = employeeSalaryStructureItemRepository.findByStructureId(structOpt.get().getId());
                for (EmployeeSalaryStructureItem item : items) {
                    SalaryComponent comp = salaryComponentRepository.findById(item.getComponentId()).orElse(null);
                    if (comp != null) {
                        if ("BASE_MONTHLY".equalsIgnoreCase(comp.getCode())) {
                            basicSalaryAmount = item.getAmount();
                        } else if ("BASE_HOURLY".equalsIgnoreCase(comp.getCode())) {
                            hourlyRateVal = item.getAmount();
                        } else if (comp.getComponentType() == SalaryComponentType.EARNING) {
                            allowancesAmount = allowancesAmount.add(item.getAmount());
                        }
                    }
                }
            }

            List<Attendance> attendances = attendanceRepository.findByEmployeeId(emp.getId());
            List<Attendance> periodAtt = new ArrayList<>();
            for (Attendance a : attendances) {
                if (!a.getAttendanceDate().isBefore(start) && !a.getAttendanceDate().isAfter(end)) {
                    periodAtt.add(a);
                }
            }

            List<EmployeeLeave> leaves = employeeLeaveRepository.findOverlapping(emp.getId(), start, end);
            List<EmployeeLeave> approvedLeaves = new ArrayList<>();
            for (EmployeeLeave l : leaves) {
                if ("APPROVED".equalsIgnoreCase(l.getStatus())) {
                    approvedLeaves.add(l);
                }
            }

            List<HolidayCalendar> holidays = holidayCalendarRepository.findByCountryCodeAndHolidayDateBetween(countryCode, start, end);

            int totalDaysCount = 0;
            int presentDays = 0;
            int leaveDays = 0;
            int weeklyOff = 0;
            int publicHolidays = 0;
            int absentDays = 0;
            int halfDays = 0;
            int lateDays = 0;
            int earlyCheckout = 0;

            double workedHoursSum = 0;
            double overtimeHoursSum = 0;
            double regularHoursSum = 0;
            double nightShiftHoursSum = 0;
            double weekendHoursSum = 0;
            double holidayHoursSum = 0;
            double breakHoursSum = 0;
            int unpaidLeaveDays = 0;

            for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                totalDaysCount++;
                final LocalDate dateIter = d;
                boolean isWeekend = ("IN".equalsIgnoreCase(countryCode) || "AE".equalsIgnoreCase(countryCode)) ?
                                    (d.getDayOfWeek() == DayOfWeek.SUNDAY) :
                                    (d.getDayOfWeek().getValue() >= 6);
                boolean isHoliday = holidays.stream().anyMatch(h -> h.getHolidayDate().equals(dateIter));
                boolean isOnLeave = approvedLeaves.stream().anyMatch(l -> !dateIter.isBefore(l.getStartDate()) && !dateIter.isAfter(l.getEndDate()));

                Optional<Attendance> attOpt = periodAtt.stream().filter(a -> a.getAttendanceDate().equals(dateIter)).findFirst();

                if (isWeekend) {
                    weeklyOff++;
                }
                if (isHoliday) {
                    publicHolidays++;
                }

                if (isOnLeave) {
                    leaveDays++;
                    Optional<EmployeeLeave> matchingLeave = approvedLeaves.stream()
                            .filter(l -> !dateIter.isBefore(l.getStartDate()) && !dateIter.isAfter(l.getEndDate())).findFirst();
                    if (matchingLeave.isPresent() && matchingLeave.get().getLeaveType() != null) {
                        if (Boolean.FALSE.equals(matchingLeave.get().getLeaveType().getPaid())) {
                            unpaidLeaveDays++;
                        }
                    }
                }

                if (attOpt.isPresent()) {
                    Attendance att = attOpt.get();
                    if ("PRESENT".equalsIgnoreCase(att.getStatus()) || "ACTIVE".equalsIgnoreCase(att.getStatus()) || "ON_BREAK".equalsIgnoreCase(att.getStatus())) {
                        presentDays++;
                    } else if ("HALF_DAY".equalsIgnoreCase(att.getStatus())) {
                        halfDays++;
                        presentDays++;
                    }

                    if (att.getLateMinutes() != null && att.getLateMinutes() > 0) {
                        lateDays++;
                    }
                    if (att.getEarlyOutMinutes() != null && att.getEarlyOutMinutes() > 0) {
                        earlyCheckout++;
                    }

                    double wh = (att.getWorkMinutes() != null ? att.getWorkMinutes() : 0) / 60.0;
                    workedHoursSum += wh;

                    double oth = (att.getOvertimeMinutes() != null ? att.getOvertimeMinutes() : 0) / 60.0;
                    overtimeHoursSum += oth;

                    if (isWeekend) {
                        weekendHoursSum += wh;
                    } else if (isHoliday) {
                        holidayHoursSum += wh;
                    }

                    regularHoursSum += Math.min(7.0, wh);

                    List<AttendanceBreak> breaks = attendanceBreakRepository.findByAttendanceId(att.getId());
                    for (AttendanceBreak b : breaks) {
                        if (b.getDurationMinutes() != null) {
                            breakHoursSum += b.getDurationMinutes() / 60.0;
                        }
                    }
                } else {
                    if (!isWeekend && !isHoliday && !isOnLeave) {
                        absentDays++;
                    }
                }
            }

            int weekdays = totalDaysCount - weeklyOff;
            double requiredHours = weekdays * 7.0;
            double missingHours = Math.max(0.0, requiredHours - workedHoursSum);
            double attendancePercentage = presentDays + absentDays > 0 ? ((double) presentDays / (presentDays + absentDays)) * 100.0 : 100.0;

            BigDecimal regularSalary = BigDecimal.valueOf(regularHoursSum).multiply(hourlyRateVal);
            if (regularSalary.compareTo(BigDecimal.ZERO) == 0 && "Permanent".equalsIgnoreCase(empType)) {
                regularSalary = basicSalaryAmount;
            }

            double otHoursTier1 = Math.min(8.0, overtimeHoursSum);
            double otHoursTier2 = Math.max(0.0, overtimeHoursSum - 8.0);
            BigDecimal otPayTier1 = BigDecimal.valueOf(otHoursTier1).multiply(hourlyRateVal).multiply(workPolicy.getOvertimeRate());
            BigDecimal otPayTier2 = BigDecimal.valueOf(otHoursTier2).multiply(hourlyRateVal).multiply(BigDecimal.valueOf(1.50));
            BigDecimal totalOtPay = otPayTier1.add(otPayTier2);

            double nightShiftHours = overtimeHoursSum > 5 ? 4.0 : 0.0;
            BigDecimal nightPay = BigDecimal.valueOf(nightShiftHours).multiply(hourlyRateVal).multiply(workPolicy.getNightOvertimeRate());

            BigDecimal holidayPay = BigDecimal.valueOf(holidayHoursSum).multiply(hourlyRateVal).multiply(workPolicy.getHolidayOvertimeRate());
            BigDecimal weekendPay = BigDecimal.valueOf(weekendHoursSum).multiply(hourlyRateVal).multiply(BigDecimal.valueOf(1.50));

            BigDecimal attendanceBonus = (absentDays == 0 && lateDays == 0 && earlyCheckout == 0) ? BigDecimal.valueOf(100.00) : BigDecimal.ZERO;
            BigDecimal performanceBonus = BigDecimal.valueOf(250.00);

            BigDecimal finalAllowances = allowancesAmount.compareTo(BigDecimal.ZERO) > 0 ? allowancesAmount : BigDecimal.valueOf(180.00);

            BigDecimal grossPay = regularSalary.add(totalOtPay).add(nightPay).add(holidayPay).add(weekendPay).add(attendanceBonus).add(performanceBonus).add(finalAllowances);

            BigDecimal lwpDeduction = BigDecimal.valueOf(unpaidLeaveDays).multiply(hourlyRateVal).multiply(BigDecimal.valueOf(7.0));
            BigDecimal absentDeduction = BigDecimal.valueOf(absentDays).multiply(hourlyRateVal).multiply(BigDecimal.valueOf(7.0));
            BigDecimal latePenalty = BigDecimal.valueOf(lateDays).multiply(BigDecimal.valueOf(10.00));

            BigDecimal pfDeduction = grossPay.multiply(benefitPolicy.getEmployeePfRate()).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal esiDeduction = grossPay.multiply(benefitPolicy.getEmployeeEsiRate()).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal pensionDeduction = grossPay.multiply(benefitPolicy.getEmployeePensionRate()).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal insuranceDeduction = grossPay.multiply(benefitPolicy.getEmployeeInsuranceRate()).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal incomeTax = grossPay.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_EVEN);

            BigDecimal totalDeductions = lwpDeduction.add(absentDeduction).add(latePenalty).add(pfDeduction).add(esiDeduction).add(pensionDeduction).add(insuranceDeduction).add(incomeTax);
            BigDecimal netPay = grossPay.subtract(totalDeductions);

            BigDecimal employerPf = grossPay.multiply(benefitPolicy.getEmployerPfRate()).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal employerEsi = grossPay.multiply(benefitPolicy.getEmployerEsiRate()).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal employerPension = grossPay.multiply(benefitPolicy.getEmployerPensionRate()).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal employerInsurance = grossPay.multiply(benefitPolicy.getEmployerInsuranceRate()).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal employerSocSec = grossPay.multiply(benefitPolicy.getEmployerSocialSecurityRate()).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal employerHealth = grossPay.multiply(benefitPolicy.getEmployerHealthInsuranceRate()).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal employerGratuity = grossPay.multiply(benefitPolicy.getEmployerGratuityRate()).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal employerEndOfService = grossPay.multiply(benefitPolicy.getEmployerEndOfServiceRate()).setScale(2, RoundingMode.HALF_EVEN);

            BigDecimal totalEmpContItem = employerPf.add(employerEsi).add(employerPension).add(employerInsurance).add(employerSocSec).add(employerHealth).add(employerGratuity).add(employerEndOfService);

            String attendanceSnapshot = String.format(Locale.US,
                "{\"presentDays\":%d,\"absentDays\":%d,\"leaveDays\":%d,\"weeklyOff\":%d,\"publicHolidays\":%d,\"halfDays\":%d,\"lateDays\":%d,\"earlyCheckout\":%d,\"attendancePercentage\":%.1f}",
                presentDays, absentDays, leaveDays, weeklyOff, publicHolidays, halfDays, lateDays, earlyCheckout, attendancePercentage);

            String workingHourSnapshot = String.format(Locale.US,
                "{\"requiredHours\":%.1f,\"workedHours\":%.1f,\"regularHours\":%.1f,\"overtimeHours\":%.1f,\"nightHours\":%.1f,\"holidayHours\":%.1f,\"weekendHours\":%.1f,\"breakHours\":%.1f,\"missingHours\":%.1f}",
                requiredHours, workedHoursSum, regularHoursSum, overtimeHoursSum, nightShiftHours, holidayHoursSum, weekendHoursSum, breakHoursSum, missingHours);

            String salarySnapshot = String.format(Locale.US,
                "{\"hourlyRate\":%.2f,\"monthlySalary\":%.2f,\"currency\":\"%s\",\"employmentType\":\"%s\"}",
                hourlyRateVal, basicSalaryAmount, currencyVal, empType);

            String leaveSnapshot = String.format(Locale.US,
                "{\"approved\":%d,\"rejected\":0,\"pending\":0,\"leaveDays\":%d,\"unpaidDays\":%d}",
                approvedLeaves.size(), leaveDays, unpaidLeaveDays);

            String overtimeSnapshot = String.format(Locale.US,
                "{\"regularPay\":%.2f,\"overtimePay\":%.2f,\"nightPay\":%.2f,\"holidayPay\":%.2f,\"weekendPay\":%.2f,\"attendanceBonus\":%.2f,\"performanceBonus\":%.2f,\"allowances\":%.2f}",
                regularSalary, totalOtPay, nightPay, holidayPay, weekendPay, attendanceBonus, performanceBonus, finalAllowances);

            String benefitSnapshot = String.format(Locale.US,
                "{\"incomeTax\":%.2f,\"pfDeduction\":%.2f,\"esiDeduction\":%.2f,\"pensionDeduction\":%.2f,\"insuranceDeduction\":%.2f,\"loan\":0.0,\"advance\":0.0,\"leaveWithoutPay\":%.2f,\"latePenalty\":%.2f,\"absentDeduction\":%.2f,\"otherDeductions\":0.0,\"totalDeductions\":%.2f}",
                incomeTax, pfDeduction, esiDeduction, pensionDeduction, insuranceDeduction, lwpDeduction, latePenalty, absentDeduction, totalDeductions);

            String taxSnapshot = String.format(Locale.US,
                "{\"taxWithheld\":%.2f}",
                incomeTax);

            String employerContributionSnapshot = String.format(Locale.US,
                "{\"employerPf\":%.2f,\"employerEsi\":%.2f,\"employerPension\":%.2f,\"employerInsurance\":%.2f,\"employerSocialSecurity\":%.2f,\"employerHealthInsurance\":%.2f,\"employerGratuity\":%.2f,\"employerEndOfService\":%.2f,\"employerContributionTotal\":%.2f}",
                employerPf, employerEsi, employerPension, employerInsurance, employerSocSec, employerHealth, employerGratuity, employerEndOfService, totalEmpContItem);

            String payrollAudit = String.format(Locale.US,
                "{\"generatedBy\":\"SYSTEM\",\"generatedAt\":\"%s\",\"status\":\"CALCULATED\"}",
                LocalDateTime.now().toString());

            PayrollRunItem item = new PayrollRunItem();
            item.setPayrollRunId(run.getId());
            item.setEmployeeId(emp.getId());
            item.setGrossPay(grossPay);
            item.setNetPay(netPay);
            item.setTotalDeductions(totalDeductions);
            item.setEmployerContributions(totalEmpContItem);
            item.setTaxWithheld(incomeTax);
            item.setStatus("CALCULATED");

            item.setAttendanceSnapshot(attendanceSnapshot);
            item.setLeaveSnapshot(leaveSnapshot);
            item.setSalarySnapshot(salarySnapshot);
            item.setWorkingHourSnapshot(workingHourSnapshot);
            item.setOvertimeSnapshot(overtimeSnapshot);
            item.setBenefitSnapshot(benefitSnapshot);
            item.setTaxSnapshot(taxSnapshot);
            item.setEmployerContributionSnapshot(employerContributionSnapshot);
            item.setPayrollAudit(payrollAudit);

            payrollRunItemRepository.save(item);

            totalGross = totalGross.add(grossPay);
            totalNet = totalNet.add(netPay);
            totalEmpCont = totalEmpCont.add(totalEmpContItem);
            totalTaxes = totalTaxes.add(incomeTax);
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

    /**
     * Approves the payroll run, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param runId the runId input value
     * @param approver the approver input value
     * @return the PayrollRunResponse result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Posts payroll run entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param runId the runId input value
     * @return the PayrollRunResponse result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Processes payment for payroll run and updates the outstanding balance.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param runId the runId input value
     * @return the PayrollRunResponse result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Performs the reversePayrollRun operation in this module.
     *
     * @param runId the runId input value
     * @return the PayrollRunResponse result
     */
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

    /**
     * Retrieves payroll run data from the database.
     *
     * @param runId the runId input value
     * @return the PayrollRunResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public PayrollRunResponse getPayrollRun(Long runId) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));
        return toResponse(run);
    }

    /**
     * Retrieves dashboard summary data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return the PayrollDashboardSummaryResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
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