/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.scheduler
 * File              : BudgetReservationExpiryJob.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetReservationExpiryJobController
 * Related Service   : BudgetReservationExpiryJobService, BudgetReservationExpiryJobServiceImpl
 * Related Repository: BudgetReservationRepository
 * Related Entity    : BudgetReservationExpiryJob
 * Related DTO       : N/A
 * Related Mapper    : BudgetReservationExpiryJobMapper
 * Related DB Table  : budget_reservation_expiry_jobs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.budget.scheduler;

import com.plus33.erp.finance.budget.entity.BudgetReservation;
import com.plus33.erp.finance.budget.entity.ReservationStatus;
import com.plus33.erp.finance.budget.repository.BudgetReservationRepository;
import com.plus33.erp.finance.budget.service.BudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetReservationExpiryJob}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.scheduler}</p>
 * <p><b>Layer  :</b> Scheduled Task: runs periodic background jobs via @Scheduled annotation.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BudgetReservationExpiryJob {

    private final BudgetReservationRepository reservationRepository;
    private final BudgetService budgetService;

    // Run every day at midnight
    /**
     * Performs the sweepExpiredReservations operation in this module.
     *
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void sweepExpiredReservations() {
        log.info("Running budget reservation expiry job sweeper...");
        LocalDate today = LocalDate.now();
        List<BudgetReservation> expired = reservationRepository.findAllByStatusAndExpiryDateBefore(
            ReservationStatus.ACTIVE, today
        );

        log.info("Found {} expired budget reservations to release", expired.size());
        for (BudgetReservation res : expired) {
            try {
                budgetService.releaseReservation(res.getSourceModule(), res.getSourceReferenceId());
                log.info("Released expired reservation ID {} for reference {} in module {}", 
                    res.getId(), res.getReferenceNumber(), res.getSourceModule());
            } catch (Exception e) {
                log.error("Failed to release expired reservation ID: {}", res.getId(), e);
            }
        }
    }
}