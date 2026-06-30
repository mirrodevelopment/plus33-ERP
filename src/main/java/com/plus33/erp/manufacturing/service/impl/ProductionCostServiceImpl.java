package com.plus33.erp.manufacturing.service.impl;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.JournalEntryLine;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.repository.JournalEntryRepository;
import com.plus33.erp.manufacturing.dto.*;
import com.plus33.erp.manufacturing.entity.*;
import com.plus33.erp.manufacturing.repository.ProductionCostRepository;
import com.plus33.erp.manufacturing.repository.ProductionOrderRepository;
import com.plus33.erp.manufacturing.service.ProductionCostService;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class ProductionCostServiceImpl implements ProductionCostService {

    private final ProductionCostRepository costRepository;
    private final ProductionOrderRepository orderRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final AccountRepository accountRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public ProductionCostServiceImpl(ProductionCostRepository costRepository,
                                     ProductionOrderRepository orderRepository,
                                     JournalEntryRepository journalEntryRepository,
                                     AccountRepository accountRepository,
                                     CompanyRepository companyRepository,
                                     UserRepository userRepository) {
        this.costRepository = costRepository;
        this.orderRepository = orderRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.accountRepository = accountRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductionCostDto getCostByOrder(Long companyId, Long productionOrderId) {
        ProductionCost cost = costRepository.findByProductionOrderId(productionOrderId)
                .orElseThrow(() -> new NoSuchElementException("No costing record found for order id: " + productionOrderId));
        return mapToDto(cost);
    }

    @Override
    public ProductionCostDto finalizeProductionCosts(Long companyId, Long productionOrderId, Long userId) {
        ProductionOrder order = orderRepository.findById(productionOrderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + productionOrderId));

        ProductionCost cost = costRepository.findByProductionOrderId(productionOrderId)
                .orElseGet(() -> {
                    ProductionCost newCost = new ProductionCost();
                    newCost.setProductionOrder(order);
                    newCost.setCostingMethod(order.getCostingMethod().name());
                    return costRepository.save(newCost);
                });

        // Integrate co-products and by-products allocations
        if (cost.getByproductCredit().compareTo(BigDecimal.ZERO) == 0) {
            cost.setByproductCredit(cost.getActualMaterialCost().multiply(new BigDecimal("0.02"))); // 2% byproduct credit default
        }
        if (cost.getCoproductAllocation().compareTo(BigDecimal.ZERO) == 0) {
            cost.setCoproductAllocation(cost.getActualTotalCost().multiply(new BigDecimal("0.05"))); // 5% coproduct allocation default
        }
        cost.setActualTotalCost(cost.getActualMaterialCost()
                .add(cost.getActualLaborCost())
                .add(cost.getActualMachineCost())
                .add(cost.getActualOverheadCost())
                .subtract(cost.getByproductCredit())
                .add(cost.getCoproductAllocation()));

        cost.recalculateVariances();
        cost.setWipBalance(BigDecimal.ZERO);
        cost.setStatus("FINALIZED");
        cost.setFinalizedAt(LocalDateTime.now());
        cost = costRepository.save(cost);

        // Generate and post variance settlements to General Ledger
        postVarianceJournal(order, cost);

        return mapToDto(cost);
    }

    @Override
    public ProductionCostDto reverseProductionCosts(Long companyId, Long productionOrderId, String reversalReason, Long userId) {
        ProductionOrder order = orderRepository.findById(productionOrderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + productionOrderId));

        ProductionCost cost = costRepository.findByProductionOrderId(productionOrderId)
                .orElseThrow(() -> new NoSuchElementException("No costing record found for order id: " + productionOrderId));

        cost.setStatus("REVERSED");
        cost.setWipBalance(cost.getActualTotalCost() != null ? cost.getActualTotalCost() : BigDecimal.ZERO);
        cost = costRepository.save(cost);

        postReversalJournal(order, cost, reversalReason);

        return mapToDto(cost);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductionCostSummaryDto getCostVarianceSummary(Long companyId, Long productionOrderId) {
        ProductionCost cost = costRepository.findByProductionOrderId(productionOrderId)
                .orElseThrow(() -> new NoSuchElementException("No costing record found for order id: " + productionOrderId));

        ProductionCostSummaryDto summary = new ProductionCostSummaryDto();
        summary.setProductionOrderId(productionOrderId);
        summary.setMaterialVariance(cost.getMaterialVariance());
        summary.setLaborVariance(cost.getLaborVariance());
        summary.setMachineVariance(cost.getMachineVariance());
        summary.setOverheadVariance(cost.getOverheadVariance());
        summary.setTotalVariance(cost.getTotalVariance());
        summary.setWipBalance(cost.getWipBalance());
        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductionCostDto> getOpenWipCosts(Long companyId) {
        return costRepository.findAll().stream()
                .filter(c -> c.getProductionOrder().getCompanyId().equals(companyId) && "IN_PROGRESS".equals(c.getStatus()))
                .map(this::mapToDto).toList();
    }

    private void postVarianceJournal(ProductionOrder order, ProductionCost cost) {
        Company company = companyRepository.findById(order.getCompanyId())
                .orElseThrow(() -> new BusinessException("Company not found for ID: " + order.getCompanyId()));

        User sysUser = getSystemUser();

        JournalEntry je = JournalEntry.builder()
                .company(company)
                .entryNumber("MFG-VAR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .entryDate(LocalDate.now())
                .sourceModule("MANUFACTURING")
                .sourceReference(order.getOrderNumber())
                .status("POSTED")
                .createdBy(sysUser)
                .description("Manufacturing Cost Variance Settlement for: " + order.getOrderNumber())
                .lines(new ArrayList<>())
                .build();

        Account wipAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "1400")
                .orElseGet(() -> accountRepository.findAll().stream().filter(a -> a.getCompany().getId().equals(company.getId()) && a.getAccountType().equals("ASSET")).findFirst().orElseThrow());

        Account varianceAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "5500")
                .orElseGet(() -> accountRepository.findAll().stream().filter(a -> a.getCompany().getId().equals(company.getId()) && a.getAccountType().equals("EXPENSE")).findFirst().orElseThrow());

        BigDecimal totalVar = cost.getTotalVariance();

        if (totalVar.compareTo(BigDecimal.ZERO) > 0) {
            // Debit Variance (Expense increase), Credit WIP (Asset decrease)
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(varianceAccount)
                    .debitAmount(totalVar)
                    .creditAmount(BigDecimal.ZERO)
                    .build());
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(wipAccount)
                    .debitAmount(BigDecimal.ZERO)
                    .creditAmount(totalVar)
                    .build());
        } else if (totalVar.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal absVar = totalVar.abs();
            // Debit WIP (Asset increase), Credit Variance (Expense decrease)
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(wipAccount)
                    .debitAmount(absVar)
                    .creditAmount(BigDecimal.ZERO)
                    .build());
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(varianceAccount)
                    .debitAmount(BigDecimal.ZERO)
                    .creditAmount(absVar)
                    .build());
        } else {
            return; // No variance to book
        }

        journalEntryRepository.save(je);
    }

    private void postReversalJournal(ProductionOrder order, ProductionCost cost, String reason) {
        Company company = companyRepository.findById(order.getCompanyId())
                .orElseThrow(() -> new BusinessException("Company not found"));

        User sysUser = getSystemUser();

        JournalEntry je = JournalEntry.builder()
                .company(company)
                .entryNumber("MFG-REV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .entryDate(LocalDate.now())
                .sourceModule("MANUFACTURING")
                .sourceReference(order.getOrderNumber())
                .status("POSTED")
                .createdBy(sysUser)
                .description("Reversal of Variance Settlement: " + reason)
                .lines(new ArrayList<>())
                .build();

        Account wipAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "1400")
                .orElseGet(() -> accountRepository.findAll().stream().filter(a -> a.getCompany().getId().equals(company.getId()) && a.getAccountType().equals("ASSET")).findFirst().orElseThrow());

        Account varianceAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "5500")
                .orElseGet(() -> accountRepository.findAll().stream().filter(a -> a.getCompany().getId().equals(company.getId()) && a.getAccountType().equals("EXPENSE")).findFirst().orElseThrow());

        BigDecimal totalVar = cost.getTotalVariance();

        if (totalVar.compareTo(BigDecimal.ZERO) > 0) {
            // Reverse standard: Debit WIP (Asset increase), Credit Variance (Expense decrease)
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(wipAccount)
                    .debitAmount(totalVar)
                    .creditAmount(BigDecimal.ZERO)
                    .build());
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(varianceAccount)
                    .debitAmount(BigDecimal.ZERO)
                    .creditAmount(totalVar)
                    .build());
        } else if (totalVar.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal absVar = totalVar.abs();
            // Reverse standard: Debit Variance, Credit WIP
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(varianceAccount)
                    .debitAmount(absVar)
                    .creditAmount(BigDecimal.ZERO)
                    .build());
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(wipAccount)
                    .debitAmount(BigDecimal.ZERO)
                    .creditAmount(absVar)
                    .build());
        } else {
            return;
        }

        journalEntryRepository.save(je);
    }

    private User getSystemUser() {
        return userRepository.findAll().stream().findFirst().orElseGet(() -> {
            User u = new User();
            u.setEmail("system@plus33.com");
            u.setPassword("password");
            u.setFirstName("System");
            u.setLastName("Admin");
            return userRepository.save(u);
        });
    }

    private ProductionCostDto mapToDto(ProductionCost cost) {
        ProductionCostDto dto = new ProductionCostDto();
        dto.setId(cost.getId());
        dto.setProductionOrderId(cost.getProductionOrder().getId());
        dto.setCostingMethod(cost.getCostingMethod());
        dto.setActualMaterialCost(cost.getActualMaterialCost());
        dto.setActualLaborCost(cost.getActualLaborCost());
        dto.setActualMachineCost(cost.getActualMachineCost());
        dto.setActualOverheadCost(cost.getActualOverheadCost());
        dto.setActualSubcontractCost(cost.getActualSubcontractCost());
        dto.setActualTotalCost(cost.getActualMaterialCost()
                .add(cost.getActualLaborCost())
                .add(cost.getActualMachineCost())
                .add(cost.getActualOverheadCost())
                .add(cost.getActualSubcontractCost()));
        dto.setStandardMaterialCost(cost.getStandardMaterialCost());
        dto.setStandardLaborCost(cost.getStandardLaborCost());
        dto.setStandardMachineCost(cost.getStandardMachineCost());
        dto.setStandardOverheadCost(cost.getStandardOverheadCost());
        dto.setStandardSubcontractCost(cost.getStandardSubcontractCost());
        dto.setStandardTotalCost(cost.getStandardTotalCost());
        dto.setWipBalance(cost.getWipBalance());
        dto.setStatus(cost.getStatus());
        return dto;
    }
}
