package com.plus33.erp.finance.assets.service;

import com.plus33.erp.finance.assets.dto.DepreciationRunRequest;
import com.plus33.erp.finance.assets.dto.DepreciationRunResponse;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FixedAssetDepreciationScheduler {

    private final FixedAssetService fixedAssetService;
    private final CompanyRepository companyRepository;

    /**
     * Automatically run depreciation at 11:00 PM on the last day of every month.
     */
    @Scheduled(cron = "0 0 23 L * ?")
    public void runMonthlyDepreciation() {
        log.info("Starting scheduled monthly fixed assets depreciation run...");
        LocalDate today = LocalDate.now();

        List<Company> companies = companyRepository.findAll();
        for (Company company : companies) {
            if (Boolean.FALSE.equals(company.getActive())) {
                continue;
            }

            try {
                DepreciationRunRequest request = new DepreciationRunRequest(today, false); // Live run
                // Runs depreciation using the system context
                DepreciationRunResponse response = fixedAssetService.runDepreciation(company.getId(), request, "system");
                
                log.info("Scheduled depreciation completed for company: {} ({}). Assets processed: {}, Total depreciation: {}", 
                        company.getName(), company.getCode(), response.assetsProcessedCount(), response.totalDepreciationAmount());
            } catch (Exception e) {
                log.error("Failed to run scheduled depreciation for company: {} ({}). Error: {}", 
                        company.getName(), company.getCode(), e.getMessage(), e);
            }
        }
        log.info("Scheduled monthly fixed assets depreciation run finished.");
    }
}
