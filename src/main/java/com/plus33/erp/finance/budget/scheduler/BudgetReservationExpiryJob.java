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

@Slf4j
@Component
@RequiredArgsConstructor
public class BudgetReservationExpiryJob {

    private final BudgetReservationRepository reservationRepository;
    private final BudgetService budgetService;

    // Run every day at midnight
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
