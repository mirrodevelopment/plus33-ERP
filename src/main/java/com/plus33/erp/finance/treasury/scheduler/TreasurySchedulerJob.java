package com.plus33.erp.finance.treasury.scheduler;

import com.plus33.erp.finance.treasury.entity.CashPool;
import com.plus33.erp.finance.treasury.repository.CashPoolRepository;
import com.plus33.erp.finance.treasury.service.BankAccountService;
import com.plus33.erp.finance.treasury.service.TreasuryForecastService;
import com.plus33.erp.finance.treasury.service.TreasuryInvestmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TreasurySchedulerJob {

    private final BankAccountService bankAccountService;
    private final TreasuryInvestmentService treasuryInvestmentService;
    private final TreasuryForecastService treasuryForecastService;
    private final CashPoolRepository cashPoolRepository;

    /**
     * Executes daily treasury jobs at midnight:
     * 1. Cash pool sweeps (ZBP and target sweeps).
     * 2. Accrues intercompany and investment interest.
     * 3. Settle matured short-term deposits and Money Market Funds.
     * 4. Perform company limit compliance auditing checks.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void executeDailyTreasurySweeps() {
        log.info("Starting daily treasury operations sweeping job...");
        String systemUser = "SYSTEM";

        // 1. Accrue daily interest on investments
        try {
            treasuryInvestmentService.executeDailyAccruals(systemUser);
            log.info("Accrued daily investment yields successfully.");
        } catch (Exception e) {
            log.error("Error running daily investment accruals", e);
        }

        // 2. Liquidate matured investments
        try {
            treasuryInvestmentService.executeDailyMaturities(systemUser);
            log.info("Processed matured short-term deposits successfully.");
        } catch (Exception e) {
            log.error("Error processing matured investments", e);
        }

        // 3. Execute ZBP / cash pool sweeping rules
        try {
            List<CashPool> activePools = cashPoolRepository.findByActiveTrue();
            for (CashPool pool : activePools) {
                bankAccountService.executeCashPoolSweeps(pool.getId(), systemUser);
            }
            log.info("Executed physical cash pool sweeps successfully.");
        } catch (Exception e) {
            log.error("Error running cash pool sweeps", e);
        }

        // 4. Perform exposure limit checks
        try {
            // Check for company 1 as default, or list all companies
            treasuryForecastService.checkExposureLimits(1L);
            log.info("Completed counterparty exposure check successfully.");
        } catch (Exception e) {
            log.error("Error verifying counterparty exposure limits", e);
        }

        log.info("Daily treasury operations sweeping job completed.");
    }
}
