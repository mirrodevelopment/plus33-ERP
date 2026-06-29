package com.plus33.erp.finance.tax.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.finance.tax.dto.*;
import com.plus33.erp.finance.tax.entity.*;
import com.plus33.erp.finance.tax.repository.TaxCalculationLogRepository;
import com.plus33.erp.finance.tax.repository.TaxConfigurationVersionRepository;
import com.plus33.erp.finance.tax.repository.TaxExemptionCertificateRepository;
import com.plus33.erp.finance.tax.repository.TaxOverrideRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TaxCalculationEngineImpl implements TaxCalculationEngine {

    private final TaxDeterminationRuleEngine determinationRuleEngine;
    private final TaxEngineRegistry taxEngineRegistry;
    private final TaxExemptionCertificateRepository exemptionCertificateRepository;
    private final TaxOverrideRequestRepository overrideRequestRepository;
    private final TaxCalculationLogRepository calculationLogRepository;
    private final TaxConfigurationVersionRepository configurationVersionRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;
    private final TaxMetricsRegistry metricsRegistry;

    @Override
    @Transactional
    public TaxCalculationResult calculateTax(TaxCalculationRequest request) {
        long startTime = System.currentTimeMillis();

        Long companyId = request.getCompanyId();
        LocalDate date = request.getTransactionDate();
        String docType = request.getDocumentType();

        // 0. Resolve active configuration version
        Integer activeConfigVersion = null;
        Optional<TaxConfigurationVersion> configVersion = configurationVersionRepository
                .findActiveVersionAt(companyId, date.atStartOfDay());
        if (configVersion.isPresent()) {
            activeConfigVersion = configVersion.get().getVersionNumber();
        }

        // 1. Manual Override Check (Precedence 1)
        if (request.getDocumentId() != null) {
            Optional<TaxOverrideRequest> override = overrideRequestRepository
                    .findByCompanyIdAndDocumentTypeAndDocumentId(companyId, docType, request.getDocumentId());
            if (override.isPresent() && "APPROVED".equals(override.get().getStatus())) {
                TaxOverrideRequest ovr = override.get();
                TaxCalculationResult overrideResult = buildOverrideResult(request, ovr);
                overrideResult.setOverrideApplied(true);
                overrideResult.setOverrideReason(ovr.getReason());
                overrideResult.setConfigurationVersion(activeConfigVersion);

                logCalculation(request, overrideResult, null, startTime, true);

                // Publish event
                eventPublisher.publishEvent(new com.plus33.erp.finance.tax.event.TaxOverrideAppliedEvent(this, companyId, docType, request.getDocumentId(), ovr.getReason()));

                return overrideResult;
            }
        }

        // 2. Exemption Certificate Check
        boolean isExempt = false;
        if (request.getCustomerId() != null) {
            List<TaxExemptionCertificate> certificates = exemptionCertificateRepository
                    .findActiveCertificates(companyId, request.getCustomerId(), date);
            if (!certificates.isEmpty()) {
                isExempt = true;
            }
        }

        // 3. Rule Determination (uses first line to determine engine type)
        // The rule engine returns the matching TaxGroup which tells us the tax type
        TaxCalculationLineRequest firstLine = request.getLines().get(0);
        TaxGroup taxGroup = determinationRuleEngine.determineTaxGroup(
                companyId,
                docType,
                request.getCustomerTaxProfile(),
                request.getSupplierTaxProfile(),
                firstLine.getProductTaxCategory(),
                request.getOriginCountry(),
                request.getOriginState(),
                request.getDestCountry(),
                request.getDestState(),
                request.getIncoterms(),
                date
        );

        // 4. Resolve engine provider from registry based on the tax group type
        String taxType = taxGroup.getTaxType() != null ? taxGroup.getTaxType() : "VAT";
        TaxEngineProvider provider = taxEngineRegistry.resolve(taxType);

        // 5. Delegate calculation to the resolved provider
        TaxCalculationResult result = provider.calculateTax(request, taxGroup, isExempt);

        // 6. Populate diagnostic metadata
        result.setConfigurationVersion(activeConfigVersion);
        result.setOverrideApplied(false);
        if (result.getProviderName() == null) {
            result.setProviderName(taxType);
        }

        // 7. Audit log
        logCalculation(request, result, taxGroup, startTime, false);

        // 8. Publish event
        eventPublisher.publishEvent(new com.plus33.erp.finance.tax.event.TaxCalculatedEvent(this, companyId, docType, request.getDocumentId(), result.getTotalTaxAmount()));

        return result;
    }

    private TaxCalculationResult buildOverrideResult(TaxCalculationRequest request, TaxOverrideRequest override) {
        // Build a minimal result with the overridden tax amount distributed proportionally
        BigDecimal overrideTax = override.getRequestedTaxAmount();
        BigDecimal totalAmount = request.getLines().stream()
                .map(TaxCalculationLineRequest::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        java.util.List<TaxCalculationLineResult> lineResults = new java.util.ArrayList<>();
        BigDecimal distributedTax = BigDecimal.ZERO;

        for (int i = 0; i < request.getLines().size(); i++) {
            TaxCalculationLineRequest lineReq = request.getLines().get(i);
            BigDecimal lineTax;
            if (i == request.getLines().size() - 1) {
                lineTax = overrideTax.subtract(distributedTax);
            } else {
                lineTax = totalAmount.compareTo(BigDecimal.ZERO) > 0
                        ? overrideTax.multiply(lineReq.getAmount()).divide(totalAmount, 2, java.math.RoundingMode.HALF_EVEN)
                        : BigDecimal.ZERO;
                distributedTax = distributedTax.add(lineTax);
            }

            lineResults.add(TaxCalculationLineResult.builder()
                    .lineId(lineReq.getLineId())
                    .netAmount(lineReq.getAmount())
                    .taxAmount(lineTax)
                    .grossAmount(lineReq.getAmount().add(lineTax))
                    .taxComponents(java.util.Collections.emptyList())
                    .build());
        }

        return TaxCalculationResult.builder()
                .totalNetAmount(totalAmount)
                .totalTaxAmount(overrideTax)
                .totalGrossAmount(totalAmount.add(overrideTax))
                .lines(lineResults)
                .build();
    }

    @Transactional
    protected void logCalculation(TaxCalculationRequest request, TaxCalculationResult result,
                                   TaxGroup taxGroup, long startTime, boolean overrideApplied) {
        try {
            long duration = System.currentTimeMillis() - startTime;
            metricsRegistry.recordCalculation(result.getProviderName(), duration);

            String requestJson = objectMapper.writeValueAsString(request);

            TaxCalculationLog logEntry = TaxCalculationLog.builder()
                    .companyId(request.getCompanyId())
                    .documentType(request.getDocumentType())
                    .documentId(request.getDocumentId())
                    .requestPayload(requestJson)
                    .resolvedRuleId(taxGroup != null ? taxGroup.getId() : null)
                    .appliedRatePercent(result.getTotalTaxAmount() != null && result.getTotalNetAmount() != null
                            && result.getTotalNetAmount().compareTo(BigDecimal.ZERO) > 0
                            ? result.getTotalTaxAmount().multiply(BigDecimal.valueOf(100))
                                .divide(result.getTotalNetAmount(), 2, java.math.RoundingMode.HALF_EVEN)
                            : BigDecimal.ZERO)
                    .providerName(result.getProviderName())
                    .calculatedTaxAmount(result.getTotalTaxAmount() != null ? result.getTotalTaxAmount() : BigDecimal.ZERO)
                    .overrideApplied(overrideApplied)
                    .executionDurationMs(duration)
                    .occurredAt(LocalDateTime.now())
                    .build();

            calculationLogRepository.save(logEntry);
        } catch (Exception e) {
            log.warn("Failed to persist tax calculation audit log", e);
        }
    }
}
