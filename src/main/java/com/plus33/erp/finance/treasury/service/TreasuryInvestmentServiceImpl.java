package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.treasury.dto.InvestmentAndRiskDtos.*;
import com.plus33.erp.finance.treasury.entity.*;
import com.plus33.erp.finance.treasury.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TreasuryInvestmentServiceImpl implements TreasuryInvestmentService {

    private final TreasuryInvestmentRepository treasuryInvestmentRepository;
    private final BankAccountRepository bankAccountRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public TreasuryInvestmentResponse createInvestment(TreasuryInvestmentRequest request) {
        BankAccount bankAccount = bankAccountRepository.findById(request.bankAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found with ID: " + request.bankAccountId()));

        if (treasuryInvestmentRepository.findByReferenceNumber(request.referenceNumber()).isPresent()) {
            throw new BusinessException("Investment reference number already exists: " + request.referenceNumber());
        }

        if (bankAccount.getCurrentBalance().compareTo(request.principalAmount()) < 0) {
            throw new BusinessException("Insufficient balance to establish investment. Required: " + request.principalAmount());
        }

        // Deduct principal from bank account
        bankAccount.setCurrentBalance(bankAccount.getCurrentBalance().subtract(request.principalAmount()));
        bankAccountRepository.save(bankAccount);

        TreasuryInvestment investment = TreasuryInvestment.builder()
            .bankAccount(bankAccount)
            .referenceNumber(request.referenceNumber())
            .investmentType(request.investmentType())
            .principalAmount(request.principalAmount())
            .interestRate(request.interestRate())
            .expectedYield(request.expectedYield() != null ? request.expectedYield() : BigDecimal.ZERO)
            .effectiveYield(request.effectiveYield() != null ? request.effectiveYield() : BigDecimal.ZERO)
            .compoundedInterest(request.compoundedInterest() != null ? request.compoundedInterest() : BigDecimal.ZERO)
            .taxWithholding(request.taxWithholding() != null ? request.taxWithholding() : BigDecimal.ZERO)
            .earlyLiquidationPenalty(request.earlyLiquidationPenalty() != null ? request.earlyLiquidationPenalty() : BigDecimal.ZERO)
            .startDate(request.startDate())
            .maturityDate(request.maturityDate())
            .status("ACTIVE")
            .build();

        TreasuryInvestment saved = treasuryInvestmentRepository.save(investment);
        return mapToInvestmentResponse(saved);
    }

    @Override
    public TreasuryInvestmentResponse getInvestmentById(Long id) {
        TreasuryInvestment investment = treasuryInvestmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Investment not found: " + id));
        return mapToInvestmentResponse(investment);
    }

    @Override
    public List<TreasuryInvestmentResponse> getInvestmentsByCompany(Long companyId) {
        return treasuryInvestmentRepository.findByBankAccountCompanyId(companyId).stream().map(this::mapToInvestmentResponse).toList();
    }

    @Override
    @Transactional
    public void accrueInterest(Long investmentId) {
        TreasuryInvestment investment = treasuryInvestmentRepository.findById(investmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Investment not found: " + investmentId));

        if (!"ACTIVE".equals(investment.getStatus())) return;

        // Daily simple interest: Principal * InterestRate% / 365
        BigDecimal rateFraction = investment.getInterestRate().divide(BigDecimal.valueOf(100.00), 6, RoundingMode.HALF_UP);
        BigDecimal dailyInterest = investment.getPrincipalAmount().multiply(rateFraction)
            .divide(BigDecimal.valueOf(365.00), 2, RoundingMode.HALF_UP);

        investment.setAccruedInterest(investment.getAccruedInterest().add(dailyInterest));
        treasuryInvestmentRepository.save(investment);
    }

    @Override
    @Transactional
    public void liquidateInvestment(Long investmentId) {
        TreasuryInvestment investment = treasuryInvestmentRepository.findById(investmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Investment not found: " + investmentId));

        if (!"ACTIVE".equals(investment.getStatus())) {
            throw new BusinessException("Investment is not active: " + investment.getStatus());
        }

        // Apply penalty and tax withholding
        BigDecimal interestToPay = investment.getAccruedInterest()
            .subtract(investment.getEarlyLiquidationPenalty())
            .subtract(investment.getTaxWithholding());

        if (interestToPay.compareTo(BigDecimal.ZERO) < 0) {
            interestToPay = BigDecimal.ZERO;
        }

        BigDecimal refundAmount = investment.getPrincipalAmount().add(interestToPay);

        BankAccount bankAccount = investment.getBankAccount();
        bankAccount.setCurrentBalance(bankAccount.getCurrentBalance().add(refundAmount));
        bankAccountRepository.save(bankAccount);

        investment.setInterestEarned(interestToPay);
        investment.setStatus("LIQUIDATED");
        treasuryInvestmentRepository.save(investment);

        log.info("LIQUIDATE: Investment ID {} liquidated. Refunded {} (including {} interest) after penalty/tax deductions.",
            investmentId, refundAmount, interestToPay);
    }

    @Override
    @Transactional
    public void executeDailyMaturities(String username) {
        List<TreasuryInvestment> activeInvestments = treasuryInvestmentRepository.findByStatus("ACTIVE");
        LocalDate today = LocalDate.now();

        for (TreasuryInvestment inv : activeInvestments) {
            if (!today.isBefore(inv.getMaturityDate())) {
                // Settle maturity
                BigDecimal interestToPay = inv.getExpectedYield().compareTo(BigDecimal.ZERO) > 0 ? inv.getExpectedYield() : inv.getAccruedInterest();
                interestToPay = interestToPay.subtract(inv.getTaxWithholding());
                if (interestToPay.compareTo(BigDecimal.ZERO) < 0) {
                    interestToPay = BigDecimal.ZERO;
                }

                BigDecimal payout = inv.getPrincipalAmount().add(interestToPay);
                BankAccount account = inv.getBankAccount();
                account.setCurrentBalance(account.getCurrentBalance().add(payout));
                bankAccountRepository.save(account);

                inv.setInterestEarned(interestToPay);
                inv.setStatus("MATURED");
                treasuryInvestmentRepository.save(inv);

                log.info("MATURITY SWEEP: Settled matured investment ID {} for payout amount {}", inv.getId(), payout);

                // Publish Event
                eventPublisher.publishEvent(new com.plus33.erp.finance.treasury.entity.InvestmentMaturedEvent(this, inv.getId()));
            }
        }
    }

    @Override
    @Transactional
    public void executeDailyAccruals(String username) {
        List<TreasuryInvestment> activeInvestments = treasuryInvestmentRepository.findByStatus("ACTIVE");
        for (TreasuryInvestment inv : activeInvestments) {
            accrueInterest(inv.getId());
        }
    }

    @Override
    @Transactional
    public void executeFXRevaluations(Long companyId, String username) {
        List<BankAccount> companyAccounts = bankAccountRepository.findByCompanyId(companyId);
        
        for (BankAccount account : companyAccounts) {
            if (!"AED".equals(account.getCurrencyCode())) {
                // Mock FX rate change (e.g. USD revalues slightly up)
                BigDecimal mockSpotRateAdjustment = new BigDecimal("1.01"); // 1% gain
                BigDecimal unrealizedGain = account.getCurrentBalance().multiply(mockSpotRateAdjustment.subtract(BigDecimal.ONE));
                
                if (unrealizedGain.compareTo(BigDecimal.ZERO) > 0) {
                    log.info("FX REVAL: Account {} currency {} revalued with gain {}", account.getAccountNumber(), account.getCurrencyCode(), unrealizedGain);
                }
            }
        }
        
        eventPublisher.publishEvent(new com.plus33.erp.finance.treasury.entity.FXRevaluationCompletedEvent(this, companyId));
    }

    // Mapping Helpers
    private TreasuryInvestmentResponse mapToInvestmentResponse(TreasuryInvestment inv) {
        return new TreasuryInvestmentResponse(
            inv.getId(),
            inv.getBankAccount().getId(),
            inv.getBankAccount().getAccountNumber(),
            inv.getReferenceNumber(),
            inv.getInvestmentType(),
            inv.getPrincipalAmount(),
            inv.getInterestRate(),
            inv.getExpectedYield(),
            inv.getEffectiveYield(),
            inv.getCompoundedInterest(),
            inv.getTaxWithholding(),
            inv.getEarlyLiquidationPenalty(),
            inv.getStartDate(),
            inv.getMaturityDate(),
            inv.getInterestEarned(),
            inv.getAccruedInterest(),
            inv.getStatus(),
            inv.getCreatedAt()
        );
    }
}
