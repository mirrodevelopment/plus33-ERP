/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.service
 * File              : FixedAssetDepreciationScheduler.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetDepreciationSchedulerController
 * Related Service   : FixedAssetDepreciationSchedulerService, FixedAssetDepreciationSchedulerServiceImpl
 * Related Repository: CompanyRepository
 * Related Entity    : FixedAssetDepreciationScheduler
 * Related DTO       : DepreciationRunRequest, DepreciationRunResponse
 * Related Mapper    : FixedAssetDepreciationSchedulerMapper
 * Related DB Table  : fixed_asset_depreciation_schedulers
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetDepreciationScheduler}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.service}</p>
 * <p><b>Layer  :</b> Scheduled Task: runs periodic background jobs via @Scheduled annotation.</p>
 *
 * <p><b>Module Deps      :</b> Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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