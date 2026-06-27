package com.plus33.erp.finance.assets.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.assets.dto.*;
import com.plus33.erp.finance.assets.entity.*;
import com.plus33.erp.finance.assets.repository.*;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.repository.*;
import com.plus33.erp.finance.reporting.service.PeriodLockValidator;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.plus33.erp.finance.budget.service.BudgetService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FixedAssetServiceImpl implements FixedAssetService {

    private final CompanyRepository companyRepository;
    private final BudgetService budgetService;
    private final AssetCategoryRepository assetCategoryRepository;
    private final FixedAssetRepository fixedAssetRepository;
    private final FixedAssetDepreciationLogRepository fixedAssetDepreciationLogRepository;
    private final FixedAssetTransferRepository fixedAssetTransferRepository;
    private final FixedAssetMaintenanceRepository fixedAssetMaintenanceRepository;
    private final FixedAssetAssignmentRepository fixedAssetAssignmentRepository;
    private final FixedAssetAuditRepository fixedAssetAuditRepository;
    private final FixedAssetAuditItemRepository fixedAssetAuditItemRepository;
    
    // Enterprise repositories
    private final FixedAssetRevaluationRepository fixedAssetRevaluationRepository;
    private final FixedAssetImpairmentRepository fixedAssetImpairmentRepository;
    private final FixedAssetLeaseRepository fixedAssetLeaseRepository;
    private final FixedAssetHistoryRepository fixedAssetHistoryRepository;
    private final FixedAssetUtilizationRepository fixedAssetUtilizationRepository;
    private final FixedAssetMaintenancePlanRepository fixedAssetMaintenancePlanRepository;
    private final FixedAssetReservationRepository fixedAssetReservationRepository;
    private final FixedAssetWorkOrderRepository fixedAssetWorkOrderRepository;
    private final FixedAssetDowntimeRepository fixedAssetDowntimeRepository;
    
    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final UserRepository userRepository;
    
    private final WarehouseRepository warehouseRepository;
    private final StoreRepository storeRepository;
    private final EmployeeRepository employeeRepository;
    private final SupplierInvoiceRepository supplierInvoiceRepository;
    
    private final PeriodLockValidator periodLockValidator;

    // ─────────────────────────────────────────────────────────────────────────
    // Category Operations
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public AssetCategoryResponse createCategory(Long companyId, AssetCategoryRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));

        if (assetCategoryRepository.findByCompanyIdAndCode(companyId, request.code()).isPresent()) {
            throw new BusinessException("Asset category with code " + request.code() + " already exists for this company.");
        }

        Account assetAcc = accountRepository.findById(request.assetAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset account not found with ID: " + request.assetAccountId()));
        Account accumAcc = accountRepository.findById(request.accumulatedDepreciationAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Accumulated depreciation account not found with ID: " + request.accumulatedDepreciationAccountId()));
        Account expenseAcc = accountRepository.findById(request.depreciationExpenseAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Depreciation expense account not found with ID: " + request.depreciationExpenseAccountId()));
        Account gainLossAcc = accountRepository.findById(request.gainLossAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Gain/Loss account not found with ID: " + request.gainLossAccountId()));

        AssetCategory category = AssetCategory.builder()
                .company(company)
                .code(request.code())
                .name(request.name())
                .depreciationMethod(DepreciationMethod.valueOf(request.depreciationMethod()))
                .depreciationRate(request.depreciationRate())
                .usefulLifeYears(request.usefulLifeYears())
                .assetAccount(assetAcc)
                .accumulatedDepreciationAccount(accumAcc)
                .depreciationExpenseAccount(expenseAcc)
                .gainLossAccount(gainLossAcc)
                .build();

        AssetCategory saved = assetCategoryRepository.save(category);
        return mapToCategoryResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetCategoryResponse> getCategories(Long companyId) {
        return assetCategoryRepository.findAllByCompanyId(companyId).stream()
                .map(this::mapToCategoryResponse)
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Asset Register Operations
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public FixedAssetResponse registerAsset(Long companyId, FixedAssetRequest request, String username) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));

        AssetCategory category = assetCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset category not found with ID: " + request.categoryId()));

        FixedAsset parent = null;
        if (request.parentAssetId() != null) {
            parent = fixedAssetRepository.findByCompanyIdAndId(companyId, request.parentAssetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent asset not found with ID: " + request.parentAssetId() + " for this company."));
            if (parent.getStatus() != FixedAssetStatus.ACTIVE) {
                throw new BusinessException("Parent asset must be in ACTIVE status to add components. Current status: " + parent.getStatus());
            }
        }

        Warehouse warehouse = null;
        if (request.warehouseId() != null) {
            warehouse = warehouseRepository.findById(request.warehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.warehouseId()));
        }

        Store store = null;
        if (request.storeId() != null) {
            store = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.storeId()));
        }

        SupplierInvoice invoice = null;
        if (request.supplierInvoiceId() != null) {
            invoice = supplierInvoiceRepository.findById(request.supplierInvoiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier invoice not found with ID: " + request.supplierInvoiceId()));
        }

        Account cwipAccount = null;
        if (request.cwipAccountId() != null) {
            cwipAccount = accountRepository.findById(request.cwipAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("CWIP account not found with ID: " + request.cwipAccountId()));
        }

        // Generate unique asset code using sequence
        Long seqVal = fixedAssetRepository.getNextSequenceValue();
        String assetCode = String.format("AST-%d-%06d", LocalDate.now().getYear(), seqVal);

        // Fallback to category defaults if null
        DepreciationMethod method = request.depreciationMethod() != null ? 
                DepreciationMethod.valueOf(request.depreciationMethod()) : category.getDepreciationMethod();
        BigDecimal rate = request.depreciationRate() != null ? 
                request.depreciationRate() : category.getDepreciationRate();
        Integer usefulLife = request.usefulLifeYears() != null ? 
                request.usefulLifeYears() : category.getUsefulLifeYears();

        if (usefulLife <= 0 && method != DepreciationMethod.NONE) {
            throw new BusinessException("Useful life must be greater than 0 for depreciable assets.");
        }

        boolean isCwip = Boolean.TRUE.equals(request.isCwip());

        FixedAsset asset = FixedAsset.builder()
                .company(company)
                .category(category)
                .parentAsset(parent)
                .assetCode(assetCode)
                .name(request.name())
                .description(request.description())
                .acquisitionDate(request.acquisitionDate())
                .acquisitionCost(request.acquisitionCost())
                .salvageValue(request.salvageValue() != null ? request.salvageValue() : BigDecimal.ZERO)
                .usefulLifeYears(usefulLife)
                .depreciationRate(rate)
                .depreciationMethod(method)
                .status(isCwip ? FixedAssetStatus.UNDER_CONSTRUCTION : FixedAssetStatus.DRAFT)
                .warehouse(warehouse)
                .store(store)
                .supplierInvoice(invoice)
                .originalCost(request.acquisitionCost())
                .currentBookValue(request.acquisitionCost())
                .accumulatedDepreciation(BigDecimal.ZERO)
                
                // Documents
                .purchaseInvoiceUrl(request.purchaseInvoiceUrl())
                .warrantyDocUrl(request.warrantyDocUrl())
                .insuranceDocUrl(request.insuranceDocUrl())
                .photoUrl(request.photoUrl())
                .manualUrl(request.manualUrl())
                
                // Warranty
                .warrantyStartDate(request.warrantyStartDate())
                .warrantyEndDate(request.warrantyEndDate())
                .warrantyVendor(request.warrantyVendor())
                .amcExpiryDate(request.amcExpiryDate())
                .amcRenewalDate(request.amcRenewalDate())
                
                // Insurance
                .insurancePolicyNumber(request.insurancePolicyNumber())
                .insuranceCompany(request.insuranceCompany())
                .insurancePremium(request.insurancePremium())
                .insuranceExpiryDate(request.insuranceExpiryDate())
                .insuredValue(request.insuredValue())
                
                // CWIP & Budgeting
                .isCwip(isCwip)
                .cwipAccount(cwipAccount)
                .budgetedCost(request.budgetedCost() != null ? request.budgetedCost() : BigDecimal.ZERO)
                
                // Barcode/NFC
                .barcodeOrNfcTag(request.barcodeOrNfcTag())
                
                // Advanced Warranty
                .warrantyType(request.warrantyType())
                .warrantyCoveredComponents(request.warrantyCoveredComponents())
                .warrantyServiceContact(request.warrantyServiceContact())
                
                // GIS Location
                .latitude(request.latitude())
                .longitude(request.longitude())
                .site(request.site())
                .building(request.building())
                .floor(request.floor())
                .room(request.room())
                .region(request.region())
                
                // IoT Readiness
                .sensorId(request.sensorId())
                .deviceId(request.deviceId())
                
                // Multi-Currency
                .acquisitionCurrency(request.acquisitionCurrency() != null ? request.acquisitionCurrency() : "AED")
                .functionalCurrency(request.functionalCurrency() != null ? request.functionalCurrency() : "AED")
                .historicalExchangeRate(request.historicalExchangeRate() != null ? request.historicalExchangeRate() : BigDecimal.ONE)
                .reportingCurrency(request.reportingCurrency() != null ? request.reportingCurrency() : "AED")
                
                .createdBy(username != null ? username : "system")
                .build();

        FixedAsset saved = fixedAssetRepository.save(asset);
        
        // Record history
        recordHistory(saved, AssetHistoryEventType.ACQUISITION, saved.getAcquisitionDate(),
                "Asset registered: " + saved.getName(), saved.getAcquisitionCost(), null, username);

        return mapToAssetResponse(saved, true);
    }

    @Override
    @Transactional(readOnly = true)
    public FixedAssetResponse getAsset(Long companyId, Long id) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found with ID: " + id + " for this company."));
        if (Boolean.TRUE.equals(asset.getIsDeleted())) {
            throw new ResourceNotFoundException("Fixed asset not found with ID: " + id + " for this company.");
        }
        return mapToAssetResponse(asset, true);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FixedAssetResponse> searchAssets(Long companyId, String name, String status, Long categoryId, Pageable pageable) {
        Specification<FixedAsset> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("company").get("id"), companyId));
            predicates.add(cb.equal(root.get("isDeleted"), false));
            
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), FixedAssetStatus.valueOf(status)));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<FixedAsset> page = fixedAssetRepository.findAll(spec, pageable);
        List<FixedAssetResponse> content = page.getContent().stream()
                .map(asset -> mapToAssetResponse(asset, false)) // Do not nest recursively in list views to avoid performance bloat
                .toList();

        return new PageResponse<>(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Capitalization & GL Integration
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public FixedAssetResponse acquireAsset(Long companyId, Long id, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found with ID: " + id + " for this company."));

        if (asset.getStatus() != FixedAssetStatus.DRAFT) {
            throw new BusinessException("Only assets in DRAFT status can be capitalized. Current status: " + asset.getStatus());
        }

        // Verify accounting period lock
        periodLockValidator.verifyNotLocked(asset.getCompany(), asset.getAcquisitionDate(), null);

        User currentUser = lookupUser(username);

        BigDecimal threshold = asset.getCategory().getCapitalizationThreshold();
        boolean belowThreshold = threshold != null && threshold.compareTo(BigDecimal.ZERO) > 0 
                && asset.getAcquisitionCost().compareTo(threshold) < 0;

        // CapEx Budget Check
        try {
            com.plus33.erp.finance.budget.dto.BudgetDimensionSetRequest dimReq = new com.plus33.erp.finance.budget.dto.BudgetDimensionSetRequest(
                null,
                null,
                null,
                asset.getWarehouse() != null ? asset.getWarehouse().getId() : null,
                asset.getCategory().getId(),
                null,
                asset.getStore() != null ? asset.getStore().getId() : null
            );

            Long accountId = belowThreshold ? asset.getCategory().getDepreciationExpenseAccount().getId() : asset.getCategory().getAssetAccount().getId();

            budgetService.createDirectConsumption(
                asset.getCompany().getId(),
                accountId,
                dimReq,
                asset.getAcquisitionCost(),
                "FIXED_ASSETS",
                asset.getId(),
                asset.getAssetCode(),
                asset.getAcquisitionDate()
            );
        } catch (Exception e) {
            log.error("CapEx Budget Check Failed for Asset ID: {}", asset.getId(), e);
            throw e;
        }

        JournalEntry je;
        if (belowThreshold) {
            je = postThresholdExpensingJournal(asset, currentUser);
            asset.setStatus(FixedAssetStatus.EXPENSED);
        } else {
            je = postCapitalizationJournal(asset, currentUser);
            asset.setStatus(FixedAssetStatus.ACTIVE);
        }

        FixedAsset saved = fixedAssetRepository.save(asset);

        recordHistory(saved, AssetHistoryEventType.CAPITALIZATION, asset.getAcquisitionDate(),
                belowThreshold ? "Asset expensed (below threshold)" : "Asset capitalized", 
                asset.getAcquisitionCost(), je.getId(), username);

        return mapToAssetResponse(saved, true);
    }

    private JournalEntry postThresholdExpensingJournal(FixedAsset asset, User user) {
        Company company = asset.getCompany();
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        String jeNumber = String.format("JE-%d-%d-%06d", company.getId(), LocalDate.now().getYear(), nextSeq);

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(company)
                .entryDate(asset.getAcquisitionDate())
                .description("Threshold Expensing of Fixed Asset " + asset.getAssetCode() + " — " + asset.getName())
                .sourceModule("FIXED_ASSETS")
                .sourceReference(asset.getAssetCode())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(user)
                .currencyCode(asset.getAcquisitionCurrency() != null ? asset.getAcquisitionCurrency() : "AED")
                .lines(new ArrayList<>())
                .build();

        // Debit: Category Depreciation Expense Account
        je.getLines().add(JournalEntryLine.builder()
                .journalEntry(je)
                .account(asset.getCategory().getDepreciationExpenseAccount())
                .debitAmount(asset.getAcquisitionCost())
                .creditAmount(BigDecimal.ZERO)
                .build());

        // Credit: Accounts Payable (2100)
        Account creditAcc = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "2100")
                .orElseThrow(() -> new BusinessException("Accounts Payable account (2100) not found for company. Please configure COA."));

        je.getLines().add(JournalEntryLine.builder()
                .journalEntry(je)
                .account(creditAcc)
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(asset.getAcquisitionCost())
                .build());

        return journalEntryRepository.save(je);
    }

    private JournalEntry postCapitalizationJournal(FixedAsset asset, User user) {
        Company company = asset.getCompany();
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        String jeNumber = String.format("JE-%d-%d-%06d", company.getId(), LocalDate.now().getYear(), nextSeq);

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(company)
                .entryDate(asset.getAcquisitionDate())
                .description("Capitalization of Fixed Asset " + asset.getAssetCode() + " — " + asset.getName())
                .sourceModule("FIXED_ASSETS")
                .sourceReference(asset.getAssetCode())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(user)
                .currencyCode(asset.getAcquisitionCurrency() != null ? asset.getAcquisitionCurrency() : "AED")
                .lines(new ArrayList<>())
                .build();

        // Debit: Category Asset Account
        je.getLines().add(JournalEntryLine.builder()
                .journalEntry(je)
                .account(asset.getCategory().getAssetAccount())
                .debitAmount(asset.getAcquisitionCost())
                .creditAmount(BigDecimal.ZERO)
                .build());

        // Credit: Accounts Payable (2100) or Clearing
        Account creditAcc = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "2100")
                .orElseThrow(() -> new BusinessException("Accounts Payable account (2100) not found for company. Please configure COA."));

        je.getLines().add(JournalEntryLine.builder()
                .journalEntry(je)
                .account(creditAcc)
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(asset.getAcquisitionCost())
                .build());

        return journalEntryRepository.save(je);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Location & Operational Assignments
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public AssetAssignmentResponse assignAsset(Long companyId, Long id, AssetAssignmentRequest request, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found with ID: " + id + " for this company."));

        if (asset.getStatus() == FixedAssetStatus.DISPOSED || asset.getStatus() == FixedAssetStatus.WRITTEN_OFF) {
            throw new BusinessException("Cannot assign a disposed or written off asset.");
        }
        
        verifyNotLegalHold(asset);

        Employee employee = null;
        if (request.assignedEmployeeId() != null) {
            employee = employeeRepository.findById(request.assignedEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + request.assignedEmployeeId()));
        }

        Warehouse warehouse = null;
        if (request.assignedWarehouseId() != null) {
            warehouse = warehouseRepository.findById(request.assignedWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.assignedWarehouseId()));
        }

        Store store = null;
        if (request.assignedStoreId() != null) {
            store = storeRepository.findById(request.assignedStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.assignedStoreId()));
        }

        // Release previous active assignment
        fixedAssetAssignmentRepository.findTopByFixedAssetIdAndReleasedAtIsNull(id).ifPresent(active -> {
            active.setReleasedAt(LocalDateTime.now());
            fixedAssetAssignmentRepository.save(active);
        });

        // Create new assignment log entry
        FixedAssetAssignment assignment = FixedAssetAssignment.builder()
                .fixedAsset(asset)
                .assignedEmployee(employee)
                .assignedDepartment(request.assignedDepartment())
                .assignedWarehouse(warehouse)
                .assignedStore(store)
                .assignedBy(username != null ? username : "system")
                .build();

        FixedAssetAssignment saved = fixedAssetAssignmentRepository.save(assignment);

        // Update asset reference fields
        if (employee != null) {
            asset.setAssignedEmployee(employee);
            asset.setAssignedDepartment(request.assignedDepartment());
        }
        if (warehouse != null) {
            asset.setWarehouse(warehouse);
            asset.setStore(null);
            asset.setStatus(FixedAssetStatus.TRANSFERRED);
        } else if (store != null) {
            asset.setStore(store);
            asset.setWarehouse(null);
            asset.setStatus(FixedAssetStatus.TRANSFERRED);
        }

        fixedAssetRepository.save(asset);

        recordHistory(asset, AssetHistoryEventType.ASSIGNMENT, LocalDate.now(),
                "Asset assigned to " + (employee != null ? employee.getFirstName() + " " + employee.getLastName() : "location"),
                null, saved.getId(), username);

        return mapToAssignmentResponse(saved);
    }

    @Override
    public AssetTransferResponse transferAsset(Long companyId, Long id, AssetTransferRequest request, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found with ID: " + id + " for this company."));

        if (asset.getStatus() == FixedAssetStatus.DISPOSED || asset.getStatus() == FixedAssetStatus.WRITTEN_OFF) {
            throw new BusinessException("Cannot transfer a disposed or written off asset.");
        }
        
        verifyNotLegalHold(asset);

        Warehouse toWarehouse = null;
        if (request.toWarehouseId() != null) {
            toWarehouse = warehouseRepository.findById(request.toWarehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Destination warehouse not found with ID: " + request.toWarehouseId()));
        }

        Store toStore = null;
        if (request.toStoreId() != null) {
            toStore = storeRepository.findById(request.toStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Destination store not found with ID: " + request.toStoreId()));
        }

        Company toCompany = null;
        if (request.toCompanyId() != null) {
            toCompany = companyRepository.findById(request.toCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Destination company not found with ID: " + request.toCompanyId()));
        }

        FixedAssetTransfer transfer = FixedAssetTransfer.builder()
                .fixedAsset(asset)
                .transferDate(request.transferDate())
                .fromWarehouse(asset.getWarehouse())
                .toWarehouse(toWarehouse)
                .fromStore(asset.getStore())
                .toStore(toStore)
                .toCompany(toCompany)
                .status(TransferStatus.REQUESTED)
                .reason(request.reason())
                .transferredBy(username != null ? username : "system")
                .build();

        FixedAssetTransfer saved = fixedAssetTransferRepository.save(transfer);

        recordHistory(asset, AssetHistoryEventType.TRANSFER, request.transferDate(),
                "Transfer requested" + (toCompany != null ? " (inter-company)" : ""),
                null, saved.getId(), username);

        return mapToTransferResponse(saved);
    }

    @Override
    public AssetTransferResponse approveTransfer(Long companyId, Long transferId, String username) {
        FixedAssetTransfer transfer = fixedAssetTransferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found with ID: " + transferId));
        
        if (transfer.getStatus() != TransferStatus.REQUESTED) {
            throw new BusinessException("Only REQUESTED transfers can be approved. Current status: " + transfer.getStatus());
        }
        
        transfer.setStatus(TransferStatus.MANAGER_APPROVED);
        fixedAssetTransferRepository.save(transfer);
        
        return mapToTransferResponse(transfer);
    }

    @Override
    public AssetTransferResponse receiveTransfer(Long companyId, Long transferId, String username) {
        FixedAssetTransfer transfer = fixedAssetTransferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found with ID: " + transferId));

        if (transfer.getStatus() != TransferStatus.MANAGER_APPROVED && transfer.getStatus() != TransferStatus.IN_TRANSIT) {
            throw new BusinessException("Transfer must be APPROVED or IN_TRANSIT to be received. Current: " + transfer.getStatus());
        }

        transfer.setStatus(TransferStatus.ACTIVE);
        fixedAssetTransferRepository.save(transfer);

        // Apply the physical transfer
        FixedAsset asset = transfer.getFixedAsset();
        asset.setWarehouse(transfer.getToWarehouse());
        asset.setStore(transfer.getToStore());
        
        if (transfer.getToCompany() != null) {
            asset.setCompany(transfer.getToCompany());
        }
        
        asset.setStatus(FixedAssetStatus.ACTIVE);
        fixedAssetRepository.save(asset);

        // Release old assignment, create new
        fixedAssetAssignmentRepository.findTopByFixedAssetIdAndReleasedAtIsNull(asset.getId()).ifPresent(active -> {
            active.setReleasedAt(LocalDateTime.now());
            fixedAssetAssignmentRepository.save(active);
        });

        FixedAssetAssignment assignment = FixedAssetAssignment.builder()
                .fixedAsset(asset)
                .assignedWarehouse(transfer.getToWarehouse())
                .assignedStore(transfer.getToStore())
                .assignedDepartment(asset.getAssignedDepartment())
                .assignedEmployee(asset.getAssignedEmployee())
                .assignedBy(username != null ? username : "system")
                .build();
        fixedAssetAssignmentRepository.save(assignment);

        recordHistory(asset, AssetHistoryEventType.TRANSFER, LocalDate.now(),
                "Transfer received and completed", null, transfer.getId(), username);

        return mapToTransferResponse(transfer);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Capitalized Maintenance Operations
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public AssetMaintenanceResponse maintainAsset(Long companyId, Long id, AssetMaintenanceRequest request, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found with ID: " + id + " for this company."));

        if (asset.getStatus() == FixedAssetStatus.DISPOSED || asset.getStatus() == FixedAssetStatus.WRITTEN_OFF) {
            throw new BusinessException("Cannot log maintenance on a disposed or written off asset.");
        }

        User currentUser = lookupUser(username);

        JournalEntry je = null;
        if (Boolean.TRUE.equals(request.capitalize())) {
            // Verify accounting period lock before capitalizing maintenance costs
            periodLockValidator.verifyNotLocked(asset.getCompany(), request.maintenanceDate(), null);

            // Post GL Entry: DR Category Asset Account / CR Bank (1200)
            je = postMaintenanceCapitalizationJournal(asset, request.cost(), request.maintenanceDate(), request.description(), currentUser);

            // Update asset book value and original cost
            asset.setOriginalCost(asset.getOriginalCost().add(request.cost()).setScale(2, RoundingMode.HALF_UP));
            asset.setCurrentBookValue(asset.getCurrentBookValue().add(request.cost()).setScale(2, RoundingMode.HALF_UP));
            fixedAssetRepository.save(asset);
        }

        FixedAssetMaintenance maintenance = FixedAssetMaintenance.builder()
                .fixedAsset(asset)
                .maintenanceDate(request.maintenanceDate())
                .description(request.description())
                .cost(request.cost())
                .capitalize(request.capitalize())
                .journalEntry(je)
                .performedBy(request.performedBy())
                .build();

        FixedAssetMaintenance saved = fixedAssetMaintenanceRepository.save(maintenance);
        
        // Temporarily transition status to UNDER_MAINTENANCE during log, returning back to ACTIVE
        if (asset.getStatus() != FixedAssetStatus.UNDER_MAINTENANCE) {
            asset.setStatus(FixedAssetStatus.UNDER_MAINTENANCE);
            fixedAssetRepository.save(asset);
        }
        asset.setStatus(FixedAssetStatus.ACTIVE);
        fixedAssetRepository.save(asset);

        recordHistory(asset, AssetHistoryEventType.MAINTENANCE, request.maintenanceDate(),
                "Maintenance: " + request.description(), request.cost(), saved.getId(), username);

        return mapToMaintenanceResponse(saved);
    }

    private JournalEntry postMaintenanceCapitalizationJournal(FixedAsset asset, BigDecimal cost, LocalDate date, String desc, User user) {
        Company company = asset.getCompany();
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        String jeNumber = String.format("JE-%d-%d-%06d", company.getId(), LocalDate.now().getYear(), nextSeq);

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(company)
                .entryDate(date)
                .description("Capitalized Maintenance for Fixed Asset " + asset.getAssetCode() + " — " + desc)
                .sourceModule("FIXED_ASSETS")
                .sourceReference(asset.getAssetCode())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(user)
                .currencyCode("AED")
                .lines(new ArrayList<>())
                .build();

        // Debit: Category Asset Account
        je.getLines().add(JournalEntryLine.builder()
                .journalEntry(je)
                .account(asset.getCategory().getAssetAccount())
                .debitAmount(cost)
                .creditAmount(BigDecimal.ZERO)
                .build());

        // Credit: Bank (1200)
        Account bankAcc = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "1200")
                .orElseThrow(() -> new BusinessException("Bank account (1200) not found for company. Please configure COA."));

        je.getLines().add(JournalEntryLine.builder()
                .journalEntry(je)
                .account(bankAcc)
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(cost)
                .build());

        return journalEntryRepository.save(je);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Non-Destructive Asset Disposal Operations
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public AssetDisposalResponse disposeAsset(Long companyId, Long id, AssetDisposalRequest request, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found with ID: " + id + " for this company."));

        if (asset.getStatus() == FixedAssetStatus.DISPOSED || asset.getStatus() == FixedAssetStatus.WRITTEN_OFF) {
            throw new BusinessException("Asset is already disposed or written off.");
        }
        if (asset.getStatus() == FixedAssetStatus.DRAFT) {
            throw new BusinessException("Draft assets cannot be disposed. They must be capitalized first.");
        }
        
        verifyNotLegalHold(asset);

        // Verify period lock
        periodLockValidator.verifyNotLocked(asset.getCompany(), request.disposalDate(), null);

        User currentUser = lookupUser(username);

        BigDecimal originalCost = asset.getOriginalCost();
        BigDecimal accumDepr = asset.getAccumulatedDepreciation();
        BigDecimal netBookValue = originalCost.subtract(accumDepr).setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal proceeds = request.proceeds() != null ? request.proceeds() : BigDecimal.ZERO;
        BigDecimal gainLoss = proceeds.subtract(netBookValue).setScale(2, RoundingMode.HALF_UP);

        // Post balanced disposal GL entry
        JournalEntry je = postDisposalJournal(asset, proceeds, accumDepr, originalCost, gainLoss, request.disposalDate(), currentUser);

        // Update asset registers
        FixedAssetStatus finalStatus = FixedAssetStatus.valueOf(request.disposalType());
        if (finalStatus != FixedAssetStatus.DISPOSED && finalStatus != FixedAssetStatus.WRITTEN_OFF) {
            finalStatus = FixedAssetStatus.DISPOSED;
        }
        
        asset.setStatus(finalStatus);
        asset.setCurrentBookValue(BigDecimal.ZERO);
        asset.setAccumulatedDepreciation(originalCost); // Clear GL balances by shifting remaining to accumulated depreciation
        fixedAssetRepository.save(asset);

        // Release any active location/employee assignment
        fixedAssetAssignmentRepository.findTopByFixedAssetIdAndReleasedAtIsNull(id).ifPresent(active -> {
            active.setReleasedAt(LocalDateTime.now());
            fixedAssetAssignmentRepository.save(active);
        });

        recordHistory(asset, AssetHistoryEventType.DISPOSAL, request.disposalDate(),
                "Asset " + finalStatus.name() + ". Proceeds: " + proceeds + ", Gain/Loss: " + gainLoss,
                proceeds, je.getId(), username);

        return new AssetDisposalResponse(
                asset.getId(),
                asset.getStatus().name(),
                originalCost,
                accumDepr,
                netBookValue,
                proceeds,
                gainLoss,
                je.getId()
        );
    }

    @Override
    public AssetDisposalResponse partialDispose(Long companyId, Long id, AssetPartialDisposalRequest request, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found with ID: " + id + " for this company."));

        if (asset.getStatus() != FixedAssetStatus.ACTIVE && asset.getStatus() != FixedAssetStatus.TRANSFERRED) {
            throw new BusinessException("Only ACTIVE assets can be partially disposed.");
        }
        
        verifyNotLegalHold(asset);
        periodLockValidator.verifyNotLocked(asset.getCompany(), request.disposalDate(), null);

        BigDecimal disposalAmount = request.disposalAmount();
        if (disposalAmount.compareTo(asset.getOriginalCost()) >= 0) {
            throw new BusinessException("Partial disposal amount must be less than the original cost. Use full disposal instead.");
        }

        User currentUser = lookupUser(username);

        // Calculate proportional accumulated depreciation
        BigDecimal proportion = disposalAmount.divide(asset.getOriginalCost(), 6, RoundingMode.HALF_UP);
        BigDecimal proportionalAccumDepr = asset.getAccumulatedDepreciation().multiply(proportion).setScale(2, RoundingMode.HALF_UP);
        BigDecimal nbvDisposed = disposalAmount.subtract(proportionalAccumDepr).setScale(2, RoundingMode.HALF_UP);
        BigDecimal proceeds = request.saleProceeds() != null ? request.saleProceeds() : BigDecimal.ZERO;
        BigDecimal gainLoss = proceeds.subtract(nbvDisposed).setScale(2, RoundingMode.HALF_UP);

        // Post GL entry
        JournalEntry je = postDisposalJournal(asset, proceeds, proportionalAccumDepr, disposalAmount, gainLoss, request.disposalDate(), currentUser);

        // Reduce the asset's cost and accumulated depreciation
        asset.setOriginalCost(asset.getOriginalCost().subtract(disposalAmount).setScale(2, RoundingMode.HALF_UP));
        asset.setAccumulatedDepreciation(asset.getAccumulatedDepreciation().subtract(proportionalAccumDepr).setScale(2, RoundingMode.HALF_UP));
        asset.setCurrentBookValue(asset.getOriginalCost().subtract(asset.getAccumulatedDepreciation()).setScale(2, RoundingMode.HALF_UP));
        fixedAssetRepository.save(asset);

        recordHistory(asset, AssetHistoryEventType.DISPOSAL, request.disposalDate(),
                "Partial disposal: " + disposalAmount + " of original cost. " + request.reason(),
                proceeds, je.getId(), username);

        return new AssetDisposalResponse(
                asset.getId(),
                asset.getStatus().name(),
                disposalAmount,
                proportionalAccumDepr,
                nbvDisposed,
                proceeds,
                gainLoss,
                je.getId()
        );
    }

    private JournalEntry postDisposalJournal(FixedAsset asset, BigDecimal proceeds, BigDecimal accumDepr, 
                                             BigDecimal originalCost, BigDecimal gainLoss, LocalDate date, User user) {
        Company company = asset.getCompany();
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        String jeNumber = String.format("JE-%d-%d-%06d", company.getId(), LocalDate.now().getYear(), nextSeq);

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(company)
                .entryDate(date)
                .description("Disposal of Fixed Asset " + asset.getAssetCode() + " — " + asset.getName())
                .sourceModule("FIXED_ASSETS")
                .sourceReference(asset.getAssetCode())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(user)
                .currencyCode("AED")
                .lines(new ArrayList<>())
                .build();

        // 1. Debit Bank/Cash (proceeds) if > 0
        if (proceeds.compareTo(BigDecimal.ZERO) > 0) {
            Account bankAcc = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "1200")
                    .orElseThrow(() -> new BusinessException("Bank account (1200) not found for company. Please configure COA."));

            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(bankAcc)
                    .debitAmount(proceeds)
                    .creditAmount(BigDecimal.ZERO)
                    .build());
        }

        // 2. Debit Accumulated Depreciation Account to clear
        if (accumDepr.compareTo(BigDecimal.ZERO) > 0) {
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(asset.getCategory().getAccumulatedDepreciationAccount())
                    .debitAmount(accumDepr)
                    .creditAmount(BigDecimal.ZERO)
                    .build());
        }

        // 3. Credit Asset Account to clear
        je.getLines().add(JournalEntryLine.builder()
                .journalEntry(je)
                .account(asset.getCategory().getAssetAccount())
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(originalCost)
                .build());

        // 4. Debit/Credit Gain/Loss on Disposal Account
        if (gainLoss.compareTo(BigDecimal.ZERO) > 0) {
            // Credit Gain
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(asset.getCategory().getGainLossAccount())
                    .debitAmount(BigDecimal.ZERO)
                    .creditAmount(gainLoss)
                    .build());
        } else if (gainLoss.compareTo(BigDecimal.ZERO) < 0) {
            // Debit Loss
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(asset.getCategory().getGainLossAccount())
                    .debitAmount(gainLoss.abs())
                    .creditAmount(BigDecimal.ZERO)
                    .build());
        }

        return journalEntryRepository.save(je);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Revaluation (IAS 16 / IFRS)
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public AssetRevaluationResponse revalueAsset(Long companyId, Long id, AssetRevaluationRequest request, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found with ID: " + id));

        if (asset.getStatus() != FixedAssetStatus.ACTIVE && asset.getStatus() != FixedAssetStatus.TRANSFERRED) {
            throw new BusinessException("Only ACTIVE or TRANSFERRED assets can be revalued.");
        }
        
        verifyNotLegalHold(asset);
        periodLockValidator.verifyNotLocked(asset.getCompany(), request.revaluationDate(), null);

        Account revalReserveAccount = accountRepository.findById(request.revaluationReserveAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Revaluation reserve account not found."));
        
        User currentUser = lookupUser(username);

        BigDecimal previousValue = asset.getCurrentBookValue();
        BigDecimal newFairValue = request.newFairValue();
        BigDecimal difference = newFairValue.subtract(previousValue);

        // Post GL entry
        JournalEntry je = postRevaluationJournal(asset, difference, revalReserveAccount, request.revaluationDate(), currentUser);

        // Update the asset's book value
        asset.setCurrentBookValue(newFairValue);
        // Adjust original cost proportionally to preserve future depreciation base
        asset.setOriginalCost(asset.getOriginalCost().add(difference).setScale(2, RoundingMode.HALF_UP));
        fixedAssetRepository.save(asset);

        FixedAssetRevaluation reval = FixedAssetRevaluation.builder()
                .fixedAsset(asset)
                .revaluationDate(request.revaluationDate())
                .previousValue(previousValue)
                .newFairValue(newFairValue)
                .revaluationReserveAccount(revalReserveAccount)
                .journalEntry(je)
                .reason(request.reason())
                .performedBy(username != null ? username : "system")
                .build();

        FixedAssetRevaluation saved = fixedAssetRevaluationRepository.save(reval);

        recordHistory(asset, AssetHistoryEventType.REVALUATION, request.revaluationDate(),
                "Revalued from " + previousValue + " to " + newFairValue + ". " + request.reason(),
                difference, saved.getId(), username);

        return mapToRevaluationResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetRevaluationResponse> getRevaluations(Long companyId, Long assetId) {
        // Verify asset belongs to company
        fixedAssetRepository.findByCompanyIdAndId(companyId, assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));
        return fixedAssetRevaluationRepository.findAllByFixedAssetIdOrderByRevaluationDateDesc(assetId).stream()
                .map(this::mapToRevaluationResponse)
                .toList();
    }

    private JournalEntry postRevaluationJournal(FixedAsset asset, BigDecimal difference, Account revalReserve, LocalDate date, User user) {
        Company company = asset.getCompany();
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        String jeNumber = String.format("JE-%d-%d-%06d", company.getId(), LocalDate.now().getYear(), nextSeq);

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(company)
                .entryDate(date)
                .description("Revaluation of " + asset.getAssetCode() + " — " + asset.getName())
                .sourceModule("FIXED_ASSETS")
                .sourceReference(asset.getAssetCode())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(user)
                .currencyCode("AED")
                .lines(new ArrayList<>())
                .build();

        if (difference.compareTo(BigDecimal.ZERO) > 0) {
            // Upward: DR Asset Account, CR Revaluation Reserve
            je.getLines().add(JournalEntryLine.builder().journalEntry(je)
                    .account(asset.getCategory().getAssetAccount())
                    .debitAmount(difference).creditAmount(BigDecimal.ZERO).build());
            je.getLines().add(JournalEntryLine.builder().journalEntry(je)
                    .account(revalReserve)
                    .debitAmount(BigDecimal.ZERO).creditAmount(difference).build());
        } else {
            // Downward: DR Revaluation Reserve (or P&L), CR Asset Account
            BigDecimal absVal = difference.abs();
            je.getLines().add(JournalEntryLine.builder().journalEntry(je)
                    .account(revalReserve)
                    .debitAmount(absVal).creditAmount(BigDecimal.ZERO).build());
            je.getLines().add(JournalEntryLine.builder().journalEntry(je)
                    .account(asset.getCategory().getAssetAccount())
                    .debitAmount(BigDecimal.ZERO).creditAmount(absVal).build());
        }

        return journalEntryRepository.save(je);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Impairment (IAS 36)
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public AssetImpairmentResponse impairAsset(Long companyId, Long id, AssetImpairmentRequest request, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found with ID: " + id));

        if (asset.getStatus() != FixedAssetStatus.ACTIVE && asset.getStatus() != FixedAssetStatus.TRANSFERRED) {
            throw new BusinessException("Only ACTIVE or TRANSFERRED assets can be impaired.");
        }
        
        verifyNotLegalHold(asset);
        periodLockValidator.verifyNotLocked(asset.getCompany(), request.impairmentDate(), null);

        User currentUser = lookupUser(username);

        BigDecimal impairmentAmount = request.impairmentAmount();
        if (impairmentAmount.compareTo(asset.getCurrentBookValue()) > 0) {
            throw new BusinessException("Impairment amount cannot exceed current book value.");
        }

        // Post GL: DR Impairment Loss (use Gain/Loss account), CR Asset Account
        JournalEntry je = postImpairmentJournal(asset, impairmentAmount, request.impairmentDate(), currentUser);

        asset.setCurrentBookValue(asset.getCurrentBookValue().subtract(impairmentAmount).setScale(2, RoundingMode.HALF_UP));
        asset.setAccumulatedDepreciation(asset.getAccumulatedDepreciation().add(impairmentAmount).setScale(2, RoundingMode.HALF_UP));
        fixedAssetRepository.save(asset);

        FixedAssetImpairment impairment = FixedAssetImpairment.builder()
                .fixedAsset(asset)
                .impairmentDate(request.impairmentDate())
                .impairmentAmount(impairmentAmount)
                .recoverableAmount(request.recoverableAmount())
                .journalEntry(je)
                .reason(request.reason())
                .performedBy(username != null ? username : "system")
                .build();

        FixedAssetImpairment saved = fixedAssetImpairmentRepository.save(impairment);

        recordHistory(asset, AssetHistoryEventType.IMPAIRMENT, request.impairmentDate(),
                "Impairment: " + impairmentAmount + ". " + request.reason(),
                impairmentAmount, saved.getId(), username);

        return mapToImpairmentResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetImpairmentResponse> getImpairments(Long companyId, Long assetId) {
        fixedAssetRepository.findByCompanyIdAndId(companyId, assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));
        return fixedAssetImpairmentRepository.findAllByFixedAssetIdOrderByImpairmentDateDesc(assetId).stream()
                .map(this::mapToImpairmentResponse)
                .toList();
    }

    private JournalEntry postImpairmentJournal(FixedAsset asset, BigDecimal amount, LocalDate date, User user) {
        Company company = asset.getCompany();
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        String jeNumber = String.format("JE-%d-%d-%06d", company.getId(), LocalDate.now().getYear(), nextSeq);

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(company)
                .entryDate(date)
                .description("Impairment of " + asset.getAssetCode() + " — " + asset.getName())
                .sourceModule("FIXED_ASSETS")
                .sourceReference(asset.getAssetCode())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(user)
                .currencyCode("AED")
                .lines(new ArrayList<>())
                .build();

        // DR Impairment Loss (use Gain/Loss account)
        je.getLines().add(JournalEntryLine.builder().journalEntry(je)
                .account(asset.getCategory().getGainLossAccount())
                .debitAmount(amount).creditAmount(BigDecimal.ZERO).build());
        // CR Accumulated Depreciation
        je.getLines().add(JournalEntryLine.builder().journalEntry(je)
                .account(asset.getCategory().getAccumulatedDepreciationAccount())
                .debitAmount(BigDecimal.ZERO).creditAmount(amount).build());

        return journalEntryRepository.save(je);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CWIP Capitalization
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public FixedAssetResponse capitalizeCwip(Long companyId, Long id, AssetCapitalizeCwipRequest request, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found with ID: " + id));

        if (asset.getStatus() != FixedAssetStatus.UNDER_CONSTRUCTION) {
            throw new BusinessException("Only CWIP assets can be capitalized. Current status: " + asset.getStatus());
        }

        periodLockValidator.verifyNotLocked(asset.getCompany(), request.capitalizationDate(), null);

        User currentUser = lookupUser(username);

        // Optionally update category
        if (request.assetCategoryId() != null) {
            AssetCategory newCategory = assetCategoryRepository.findById(request.assetCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
            asset.setCategory(newCategory);
        }

        BigDecimal capitalizedCost = request.totalCapitalizedCost() != null ? request.totalCapitalizedCost() : asset.getActualCost();
        
        // Post GL: DR Asset Account, CR CWIP Account
        JournalEntry je = postCwipCapitalizationJournal(asset, capitalizedCost, request.capitalizationDate(), currentUser);

        asset.setIsCwip(false);
        asset.setStatus(FixedAssetStatus.ACTIVE);
        asset.setAcquisitionDate(request.capitalizationDate());
        asset.setAcquisitionCost(capitalizedCost);
        asset.setOriginalCost(capitalizedCost);
        asset.setCurrentBookValue(capitalizedCost);
        
        if (request.usefulLifeYears() != null) {
            asset.setUsefulLifeYears(request.usefulLifeYears());
        }
        if (request.salvageValue() != null) {
            asset.setSalvageValue(request.salvageValue());
        }
        if (request.depreciationMethod() != null) {
            asset.setDepreciationMethod(DepreciationMethod.valueOf(request.depreciationMethod()));
        }

        fixedAssetRepository.save(asset);

        recordHistory(asset, AssetHistoryEventType.CAPITALIZATION, request.capitalizationDate(),
                "CWIP capitalized at " + capitalizedCost, capitalizedCost, je.getId(), username);

        return mapToAssetResponse(asset, true);
    }

    private JournalEntry postCwipCapitalizationJournal(FixedAsset asset, BigDecimal amount, LocalDate date, User user) {
        Company company = asset.getCompany();
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        String jeNumber = String.format("JE-%d-%d-%06d", company.getId(), LocalDate.now().getYear(), nextSeq);

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(company)
                .entryDate(date)
                .description("CWIP Capitalization of " + asset.getAssetCode())
                .sourceModule("FIXED_ASSETS")
                .sourceReference(asset.getAssetCode())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(user)
                .currencyCode("AED")
                .lines(new ArrayList<>())
                .build();

        // DR Asset Account
        je.getLines().add(JournalEntryLine.builder().journalEntry(je)
                .account(asset.getCategory().getAssetAccount())
                .debitAmount(amount).creditAmount(BigDecimal.ZERO).build());

        // CR CWIP Account (fallback to AP if not set)
        Account cwipAcc = asset.getCwipAccount();
        if (cwipAcc == null) {
            cwipAcc = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "2100")
                    .orElseThrow(() -> new BusinessException("CWIP/AP account not found. Please configure COA."));
        }
        je.getLines().add(JournalEntryLine.builder().journalEntry(je)
                .account(cwipAcc)
                .debitAmount(BigDecimal.ZERO).creditAmount(amount).build());

        return journalEntryRepository.save(je);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Lease Management
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public AssetLeaseResponse createLease(Long companyId, Long assetId, AssetLeaseRequest request, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));

        Account liabilityAccount = accountRepository.findById(request.leaseLiabilityAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Lease liability account not found."));

        FixedAssetLease lease = FixedAssetLease.builder()
                .fixedAsset(asset)
                .leaseType(LeaseType.valueOf(request.leaseType()))
                .leaseStartDate(request.leaseStartDate())
                .leaseEndDate(request.leaseEndDate())
                .monthlyLeasePayment(request.monthlyLeasePayment())
                .lessorName(request.lessorName())
                .leaseLiabilityAccount(liabilityAccount)
                .build();

        FixedAssetLease saved = fixedAssetLeaseRepository.save(lease);
        return mapToLeaseResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetLeaseResponse> getLeases(Long companyId, Long assetId) {
        fixedAssetRepository.findByCompanyIdAndId(companyId, assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));
        return fixedAssetLeaseRepository.findAllByFixedAssetId(assetId).stream()
                .map(this::mapToLeaseResponse)
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Split & Merge
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public List<FixedAssetResponse> splitAsset(Long companyId, Long id, AssetSplitRequest request, String username) {
        FixedAsset source = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));

        if (source.getStatus() != FixedAssetStatus.ACTIVE) {
            throw new BusinessException("Only ACTIVE assets can be split.");
        }
        
        verifyNotLegalHold(source);

        // Validate ratios sum to 1.0
        BigDecimal totalRatio = request.targets().stream()
                .map(AssetSplitRequest.SplitTarget::allocationRatio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalRatio.compareTo(BigDecimal.ONE) != 0) {
            throw new BusinessException("Allocation ratios must sum to 1.0. Current total: " + totalRatio);
        }

        List<FixedAssetResponse> results = new ArrayList<>();

        for (AssetSplitRequest.SplitTarget target : request.targets()) {
            BigDecimal ratio = target.allocationRatio();
            BigDecimal allocCost = source.getOriginalCost().multiply(ratio).setScale(2, RoundingMode.HALF_UP);
            BigDecimal allocDepr = source.getAccumulatedDepreciation().multiply(ratio).setScale(2, RoundingMode.HALF_UP);
            BigDecimal allocNbv = allocCost.subtract(allocDepr).setScale(2, RoundingMode.HALF_UP);

            Long seqVal = fixedAssetRepository.getNextSequenceValue();
            String assetCode = String.format("AST-%d-%06d", LocalDate.now().getYear(), seqVal);

            FixedAsset newAsset = FixedAsset.builder()
                    .company(source.getCompany())
                    .category(source.getCategory())
                    .parentAsset(source.getParentAsset())
                    .assetCode(assetCode)
                    .name(target.name())
                    .description(target.description())
                    .acquisitionDate(source.getAcquisitionDate())
                    .acquisitionCost(allocCost)
                    .salvageValue(source.getSalvageValue().multiply(ratio).setScale(2, RoundingMode.HALF_UP))
                    .usefulLifeYears(source.getUsefulLifeYears())
                    .depreciationRate(source.getDepreciationRate())
                    .depreciationMethod(source.getDepreciationMethod())
                    .status(FixedAssetStatus.ACTIVE)
                    .warehouse(source.getWarehouse())
                    .store(source.getStore())
                    .originalCost(allocCost)
                    .currentBookValue(allocNbv)
                    .accumulatedDepreciation(allocDepr)
                    .lastDepreciationDate(source.getLastDepreciationDate())
                    .acquisitionCurrency(source.getAcquisitionCurrency())
                    .functionalCurrency(source.getFunctionalCurrency())
                    .historicalExchangeRate(source.getHistoricalExchangeRate())
                    .reportingCurrency(source.getReportingCurrency())
                    .createdBy(username != null ? username : "system")
                    .build();

            FixedAsset saved = fixedAssetRepository.save(newAsset);

            recordHistory(saved, AssetHistoryEventType.SPLIT, LocalDate.now(),
                    "Split from " + source.getAssetCode() + " with ratio " + ratio,
                    allocCost, source.getId(), username);

            results.add(mapToAssetResponse(saved, false));
        }

        // Retire the source asset
        source.setStatus(FixedAssetStatus.DISPOSED);
        source.setCurrentBookValue(BigDecimal.ZERO);
        fixedAssetRepository.save(source);

        recordHistory(source, AssetHistoryEventType.SPLIT, LocalDate.now(),
                "Asset split into " + request.targets().size() + " components",
                null, null, username);

        return results;
    }

    @Override
    public FixedAssetResponse mergeAssets(Long companyId, AssetMergeRequest request, String username) {
        List<FixedAsset> sources = new ArrayList<>();
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalDepr = BigDecimal.ZERO;
        BigDecimal totalSalvage = BigDecimal.ZERO;

        for (Long sourceId : request.sourceAssetIds()) {
            FixedAsset src = fixedAssetRepository.findByCompanyIdAndId(companyId, sourceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + sourceId));
            if (src.getStatus() != FixedAssetStatus.ACTIVE) {
                throw new BusinessException("All source assets must be ACTIVE. Asset " + src.getAssetCode() + " is " + src.getStatus());
            }
            verifyNotLegalHold(src);
            sources.add(src);
            totalCost = totalCost.add(src.getOriginalCost());
            totalDepr = totalDepr.add(src.getAccumulatedDepreciation());
            totalSalvage = totalSalvage.add(src.getSalvageValue());
        }

        if (sources.isEmpty()) {
            throw new BusinessException("At least one source asset is required.");
        }

        FixedAsset firstSource = sources.get(0);
        Long seqVal = fixedAssetRepository.getNextSequenceValue();
        String assetCode = String.format("AST-%d-%06d", LocalDate.now().getYear(), seqVal);

        FixedAsset merged = FixedAsset.builder()
                .company(firstSource.getCompany())
                .category(firstSource.getCategory())
                .assetCode(assetCode)
                .name(request.targetName())
                .description(request.targetDescription())
                .acquisitionDate(firstSource.getAcquisitionDate())
                .acquisitionCost(totalCost)
                .salvageValue(totalSalvage)
                .usefulLifeYears(firstSource.getUsefulLifeYears())
                .depreciationRate(firstSource.getDepreciationRate())
                .depreciationMethod(firstSource.getDepreciationMethod())
                .status(FixedAssetStatus.ACTIVE)
                .warehouse(firstSource.getWarehouse())
                .store(firstSource.getStore())
                .originalCost(totalCost)
                .currentBookValue(totalCost.subtract(totalDepr).setScale(2, RoundingMode.HALF_UP))
                .accumulatedDepreciation(totalDepr)
                .acquisitionCurrency(firstSource.getAcquisitionCurrency())
                .functionalCurrency(firstSource.getFunctionalCurrency())
                .reportingCurrency(firstSource.getReportingCurrency())
                .createdBy(username != null ? username : "system")
                .build();

        FixedAsset saved = fixedAssetRepository.save(merged);

        // Retire all source assets
        for (FixedAsset src : sources) {
            src.setStatus(FixedAssetStatus.DISPOSED);
            src.setCurrentBookValue(BigDecimal.ZERO);
            fixedAssetRepository.save(src);

            recordHistory(src, AssetHistoryEventType.MERGE, LocalDate.now(),
                    "Merged into " + assetCode, null, saved.getId(), username);
        }

        recordHistory(saved, AssetHistoryEventType.MERGE, LocalDate.now(),
                "Merged from " + sources.size() + " assets", totalCost, null, username);

        return mapToAssetResponse(saved, true);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Utilization Tracking
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public AssetUtilizationResponse recordUtilization(Long companyId, Long assetId, AssetUtilizationRequest request) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));

        FixedAssetUtilization util = FixedAssetUtilization.builder()
                .fixedAsset(asset)
                .readingDate(request.recordDate())
                .usageHours(request.hoursUsed() != null ? request.hoursUsed() : BigDecimal.ZERO)
                .productionCount(request.outputUnits() != null ? request.outputUnits().intValue() : 0)
                .build();

        FixedAssetUtilization saved = fixedAssetUtilizationRepository.save(util);

        // Update telemetry runtime hours
        if (request.hoursUsed() != null) {
            asset.setTelemetryRuntimeHours(
                    asset.getTelemetryRuntimeHours().add(request.hoursUsed()).setScale(2, RoundingMode.HALF_UP));
            fixedAssetRepository.save(asset);
        }

        return new AssetUtilizationResponse(
                saved.getId(), asset.getId(), saved.getReadingDate(),
                saved.getUsageHours(), BigDecimal.valueOf(saved.getProductionCount()), request.notes());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetUtilizationResponse> getUtilization(Long companyId, Long assetId) {
        fixedAssetRepository.findByCompanyIdAndId(companyId, assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));
        return fixedAssetUtilizationRepository.findAllByFixedAssetIdOrderByReadingDateDesc(assetId).stream()
                .map(u -> new AssetUtilizationResponse(u.getId(), assetId, u.getReadingDate(),
                        u.getUsageHours(), BigDecimal.valueOf(u.getProductionCount()), null))
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TCO Analysis
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public AssetTcoResponse getTco(Long companyId, Long assetId) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));

        // Sum capitalized maintenance
        BigDecimal capitalizedMaint = fixedAssetMaintenanceRepository.findAllByFixedAssetId(assetId).stream()
                .filter(m -> Boolean.TRUE.equals(m.getCapitalize()))
                .map(FixedAssetMaintenance::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Sum operating maintenance
        BigDecimal operatingMaint = fixedAssetMaintenanceRepository.findAllByFixedAssetId(assetId).stream()
                .filter(m -> !Boolean.TRUE.equals(m.getCapitalize()))
                .map(FixedAssetMaintenance::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDepreciation = asset.getAccumulatedDepreciation();
        BigDecimal totalInsurance = asset.getInsurancePremium() != null ? asset.getInsurancePremium() : BigDecimal.ZERO;

        // Downtime cost (sum of work order costs)
        BigDecimal totalDowntimeCost = fixedAssetWorkOrderRepository.findAllByFixedAssetIdOrderByCreatedAtDesc(assetId).stream()
                .map(wo -> wo.getLaborCost().add(wo.getPartsCost()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalDowntimeHours = fixedAssetWorkOrderRepository.findAllByFixedAssetIdOrderByCreatedAtDesc(assetId).stream()
                .map(wo -> wo.getDowntimeHours() != null ? wo.getDowntimeHours().intValue() : 0)
                .reduce(0, Integer::sum);

        int totalOperatingHours = asset.getTelemetryRuntimeHours() != null ? asset.getTelemetryRuntimeHours().intValue() : 0;

        BigDecimal tco = asset.getAcquisitionCost()
                .add(capitalizedMaint)
                .add(operatingMaint)
                .add(totalDowntimeCost)
                .add(totalInsurance);

        BigDecimal costPerHour = totalOperatingHours > 0 ?
                tco.divide(BigDecimal.valueOf(totalOperatingHours), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        // Production cost
        int totalOutput = fixedAssetUtilizationRepository.findAllByFixedAssetIdOrderByReadingDateDesc(assetId).stream()
                .map(u -> u.getProductionCount() != null ? u.getProductionCount() : 0)
                .reduce(0, Integer::sum);
        BigDecimal costPerUnit = totalOutput > 0 ?
                tco.divide(BigDecimal.valueOf(totalOutput), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        return new AssetTcoResponse(
                asset.getId(), asset.getAssetCode(), asset.getName(),
                asset.getAcquisitionCost(), capitalizedMaint, operatingMaint,
                totalDepreciation, totalInsurance, totalDowntimeCost,
                tco.setScale(2, RoundingMode.HALF_UP),
                costPerHour, costPerUnit,
                totalDowntimeHours, totalOperatingHours
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Maintenance Plans
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public AssetMaintenancePlanResponse createMaintenancePlan(Long companyId, AssetMaintenancePlanRequest request) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, request.fixedAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));

        FixedAssetMaintenancePlan plan = FixedAssetMaintenancePlan.builder()
                .fixedAsset(asset)
                .maintenanceType(request.frequency())
                .serviceIntervalDays(30) // Default to 30 days
                .nextMaintenanceDate(request.nextDueDate())
                .maintenanceVendor(request.assignedVendor())
                .estimatedCost(request.estimatedCost() != null ? request.estimatedCost() : BigDecimal.ZERO)
                .active(true)
                .build();

        FixedAssetMaintenancePlan saved = fixedAssetMaintenancePlanRepository.save(plan);
        return mapToMaintenancePlanResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetMaintenancePlanResponse> getMaintenancePlans(Long companyId, Long assetId) {
        fixedAssetRepository.findByCompanyIdAndId(companyId, assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));
        return fixedAssetMaintenancePlanRepository.findAllByFixedAssetIdAndActiveTrue(assetId).stream()
                .map(this::mapToMaintenancePlanResponse)
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Reservations
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public AssetReservationResponse createReservation(Long companyId, AssetReservationRequest request, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, request.fixedAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));

        Employee employee = null;
        if (request.reservedForEmployee() != null) {
            employee = employeeRepository.findByEmployeeCode(request.reservedForEmployee())
                    .orElseThrow(() -> new BusinessException("Employee not found with code: " + request.reservedForEmployee()));
        } else {
            throw new BusinessException("Employee code is required for reservation.");
        }

        FixedAssetReservation reservation = FixedAssetReservation.builder()
                .fixedAsset(asset)
                .assignedEmployee(employee)
                .reservationDate(LocalDate.now())
                .expectedCheckoutDate(request.startDate())
                .expectedReturnDate(request.endDate())
                .status(ReservationStatus.REQUESTED)
                .build();

        FixedAssetReservation saved = fixedAssetReservationRepository.save(reservation);

        return new AssetReservationResponse(
                saved.getId(), asset.getId(), asset.getAssetCode(), asset.getName(),
                request.reservedForEmployee(), request.reservedForDepartment(),
                request.startDate(), request.endDate(), request.purpose(),
                saved.getStatus().name()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetReservationResponse> getReservations(Long companyId, Long assetId) {
        fixedAssetRepository.findByCompanyIdAndId(companyId, assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));
        return fixedAssetReservationRepository.findAllByFixedAssetIdOrderByReservationDateDesc(assetId).stream()
                .map(r -> new AssetReservationResponse(
                        r.getId(), r.getFixedAsset().getId(), r.getFixedAsset().getAssetCode(), r.getFixedAsset().getName(),
                        r.getAssignedEmployee() != null ? r.getAssignedEmployee().getFirstName() + " " + r.getAssignedEmployee().getLastName() : null,
                        null, r.getExpectedCheckoutDate(), r.getExpectedReturnDate(), null,
                        r.getStatus().name()
                ))
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Work Orders
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public AssetWorkOrderResponse createWorkOrder(Long companyId, AssetWorkOrderRequest request, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, request.fixedAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));

        Long seqVal = fixedAssetRepository.getNextSequenceValue();
        String woNumber = String.format("WO-%d-%06d", LocalDate.now().getYear(), seqVal);

        FixedAssetWorkOrder wo = FixedAssetWorkOrder.builder()
                .fixedAsset(asset)
                .workOrderNumber(woNumber)
                .description(request.description())
                .status(WorkOrderStatus.DRAFT)
                .laborCost(request.estimatedCost() != null ? request.estimatedCost() : BigDecimal.ZERO)
                .build();

        FixedAssetWorkOrder saved = fixedAssetWorkOrderRepository.save(wo);

        recordHistory(asset, AssetHistoryEventType.WORK_ORDER, LocalDate.now(),
                "Work order created: " + woNumber, request.estimatedCost(), saved.getId(), username);

        return mapToWorkOrderResponse(saved);
    }

    @Override
    public AssetWorkOrderResponse completeWorkOrder(Long companyId, Long workOrderId, BigDecimal actualCost, String username) {
        FixedAssetWorkOrder wo = fixedAssetWorkOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Work order not found."));

        wo.setStatus(WorkOrderStatus.COMPLETED);
        if (actualCost != null) {
            wo.setLaborCost(actualCost);
        }
        fixedAssetWorkOrderRepository.save(wo);

        return mapToWorkOrderResponse(wo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetWorkOrderResponse> getWorkOrders(Long companyId, Long assetId) {
        fixedAssetRepository.findByCompanyIdAndId(companyId, assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));
        return fixedAssetWorkOrderRepository.findAllByFixedAssetIdOrderByCreatedAtDesc(assetId).stream()
                .map(this::mapToWorkOrderResponse)
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Asset History
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<AssetHistoryResponse> getAssetHistory(Long companyId, Long assetId) {
        fixedAssetRepository.findByCompanyIdAndId(companyId, assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));
        return fixedAssetHistoryRepository.findAllByFixedAssetIdOrderByEventDateDescCreatedAtDesc(assetId).stream()
                .map(h -> new AssetHistoryResponse(
                        h.getId(), h.getFixedAsset().getId(), h.getEventType().name(),
                        h.getEventDate(), h.getDescription(), h.getAmount(),
                        h.getReferenceId(), h.getPerformedBy()))
                .toList();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Legal Hold
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public FixedAssetResponse toggleLegalHold(Long companyId, Long id, boolean hold, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));

        asset.setIsLegalHold(hold);
        fixedAssetRepository.save(asset);

        recordHistory(asset, AssetHistoryEventType.ACQUISITION, LocalDate.now(),
                "Legal hold " + (hold ? "ENABLED" : "DISABLED"), null, null, username);

        return mapToAssetResponse(asset, true);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Hierarchy Roll-up
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public FixedAssetDashboardResponse getHierarchyRollup(Long companyId, Long assetId) {
        FixedAsset root = fixedAssetRepository.findByCompanyIdAndId(companyId, assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found."));

        // Collect all descendants recursively
        List<FixedAsset> allAssets = new ArrayList<>();
        allAssets.add(root);
        collectDescendants(root.getId(), allAssets);

        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalNbv = BigDecimal.ZERO;
        BigDecimal totalDepr = BigDecimal.ZERO;
        Map<String, BigDecimal> categoryBreakdown = new HashMap<>();

        for (FixedAsset asset : allAssets) {
            totalCost = totalCost.add(asset.getOriginalCost());
            totalNbv = totalNbv.add(asset.getCurrentBookValue());
            totalDepr = totalDepr.add(calculateMonthlyDepreciation(asset));
            String cat = asset.getCategory().getName();
            categoryBreakdown.merge(cat, asset.getCurrentBookValue(), BigDecimal::add);
        }

        return new FixedAssetDashboardResponse(
                totalCost.setScale(2, RoundingMode.HALF_UP),
                totalNbv.setScale(2, RoundingMode.HALF_UP),
                totalDepr.setScale(2, RoundingMode.HALF_UP),
                categoryBreakdown,
                0, 0, 0, 0,
                Map.of()
        );
    }

    private void collectDescendants(Long parentId, List<FixedAsset> accumulator) {
        List<FixedAsset> children = fixedAssetRepository.findAllByParentAssetId(parentId);
        for (FixedAsset child : children) {
            accumulator.add(child);
            collectDescendants(child.getId(), accumulator);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Depreciation Calculations & Previews (Dry Run)
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public DepreciationRunResponse runDepreciation(Long companyId, DepreciationRunRequest request, String username) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));

        LocalDate targetDate = request.depreciationDate();
        boolean dryRun = Boolean.TRUE.equals(request.dryRun());

        if (!dryRun) {
            // Verify period lock for live runs
            periodLockValidator.verifyNotLocked(company, targetDate, null);
        }

        User currentUser = lookupUser(username);

        // Fetch all active assets
        List<FixedAsset> activeAssets = fixedAssetRepository.findAllByCompanyId(companyId).stream()
                .filter(asset -> asset.getStatus() == FixedAssetStatus.ACTIVE || asset.getStatus() == FixedAssetStatus.TRANSFERRED)
                .toList();

        List<DepreciationPreviewEntry> previews = new ArrayList<>();
        Map<AssetCategory, BigDecimal> categoryDeprSum = new HashMap<>();

        for (FixedAsset asset : activeAssets) {
            // Check if already processed in the target month/year
            if (asset.getLastDepreciationDate() != null &&
                asset.getLastDepreciationDate().getYear() == targetDate.getYear() &&
                asset.getLastDepreciationDate().getMonthValue() == targetDate.getMonthValue()) {
                continue; // Skip
            }

            BigDecimal deprAmount = calculateMonthlyDepreciation(asset);
            if (deprAmount.compareTo(BigDecimal.ZERO) <= 0) {
                continue; // Skip assets with zero depreciation
            }

            BigDecimal bookValueBefore = asset.getCurrentBookValue();
            BigDecimal bookValueAfter = bookValueBefore.subtract(deprAmount).setScale(2, RoundingMode.HALF_UP);

            previews.add(new DepreciationPreviewEntry(
                    asset.getId(),
                    asset.getAssetCode(),
                    asset.getName(),
                    asset.getOriginalCost(),
                    bookValueBefore,
                    deprAmount,
                    bookValueAfter
            ));

            categoryDeprSum.put(asset.getCategory(), categoryDeprSum.getOrDefault(asset.getCategory(), BigDecimal.ZERO).add(deprAmount));
        }

        List<ProjectedJournalEntry> projectedEntries = new ArrayList<>();

        if (dryRun) {
            // Construct projected journal entries for dry-run report
            for (Map.Entry<AssetCategory, BigDecimal> entry : categoryDeprSum.entrySet()) {
                AssetCategory cat = entry.getKey();
                BigDecimal amt = entry.getValue();

                List<ProjectedJournalEntryLine> lines = new ArrayList<>();
                lines.add(new ProjectedJournalEntryLine(
                        cat.getDepreciationExpenseAccount().getId(),
                        cat.getDepreciationExpenseAccount().getAccountCode(),
                        cat.getDepreciationExpenseAccount().getAccountName(),
                        amt,
                        BigDecimal.ZERO
                ));
                lines.add(new ProjectedJournalEntryLine(
                        cat.getAccumulatedDepreciationAccount().getId(),
                        cat.getAccumulatedDepreciationAccount().getAccountCode(),
                        cat.getAccumulatedDepreciationAccount().getAccountName(),
                        BigDecimal.ZERO,
                        amt
                ));

                projectedEntries.add(new ProjectedJournalEntry(
                        "PROJ-JE-DEPR-" + cat.getCode(),
                        targetDate,
                        "Projected Depreciation — Category " + cat.getName(),
                        amt,
                        lines
                ));
            }
        } else {
            // Live run: Create actual journal entries, logs, and update assets
            for (Map.Entry<AssetCategory, BigDecimal> entry : categoryDeprSum.entrySet()) {
                AssetCategory cat = entry.getKey();
                BigDecimal amt = entry.getValue();

                JournalEntry je = postDepreciationJournal(company, cat, amt, targetDate, currentUser);

                // Filter assets in this category that actually depreciated
                List<FixedAsset> catAssets = activeAssets.stream()
                        .filter(a -> a.getCategory().getId().equals(cat.getId()))
                        .toList();

                for (FixedAsset asset : catAssets) {
                    BigDecimal deprAmount = calculateMonthlyDepreciation(asset);
                    if (deprAmount.compareTo(BigDecimal.ZERO) <= 0) {
                        continue;
                    }

                    BigDecimal before = asset.getCurrentBookValue();
                    BigDecimal after = before.subtract(deprAmount).setScale(2, RoundingMode.HALF_UP);

                    // Create log
                    FixedAssetDepreciationLog log = FixedAssetDepreciationLog.builder()
                            .fixedAsset(asset)
                            .depreciationDate(targetDate)
                            .depreciationAmount(deprAmount)
                            .bookValueBefore(before)
                            .bookValueAfter(after)
                            .journalEntry(je)
                            .build();
                    fixedAssetDepreciationLogRepository.save(log);

                    // Update Asset
                    asset.setCurrentBookValue(after);
                    asset.setAccumulatedDepreciation(asset.getAccumulatedDepreciation().add(deprAmount).setScale(2, RoundingMode.HALF_UP));
                    asset.setLastDepreciationDate(targetDate);
                    fixedAssetRepository.save(asset);
                }
            }
        }

        BigDecimal grandTotal = previews.stream()
                .map(DepreciationPreviewEntry::depreciationAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DepreciationRunResponse(
                targetDate,
                dryRun,
                previews.size(),
                grandTotal,
                previews,
                projectedEntries
        );
    }

    private BigDecimal calculateMonthlyDepreciation(FixedAsset asset) {
        BigDecimal salvage = asset.getSalvageValue();
        BigDecimal bookValue = asset.getCurrentBookValue();

        if (bookValue.compareTo(salvage) <= 0) {
            return BigDecimal.ZERO; // Fully depreciated
        }

        BigDecimal rawMonthly = BigDecimal.ZERO;

        if (asset.getDepreciationMethod() == DepreciationMethod.STRAIGHT_LINE) {
            BigDecimal depreciableAmount = asset.getOriginalCost().subtract(salvage);
            int months = asset.getUsefulLifeYears() * 12;
            if (months > 0) {
                rawMonthly = depreciableAmount.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
            }
        } else if (asset.getDepreciationMethod() == DepreciationMethod.WDV) {
            BigDecimal annualRate = asset.getDepreciationRate();
            rawMonthly = bookValue.multiply(annualRate.divide(BigDecimal.valueOf(100)))
                    .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        }

        // Cap depreciation to prevent dropping below salvage value
        BigDecimal maxAllowedDepr = bookValue.subtract(salvage).setScale(2, RoundingMode.HALF_UP);
        if (rawMonthly.compareTo(maxAllowedDepr) > 0) {
            return maxAllowedDepr;
        }

        return rawMonthly;
    }

    private JournalEntry postDepreciationJournal(Company company, AssetCategory cat, BigDecimal amount, LocalDate date, User user) {
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        String jeNumber = String.format("JE-%d-%d-%06d", company.getId(), LocalDate.now().getYear(), nextSeq);

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(company)
                .entryDate(date)
                .description("Monthly Depreciation Run — Category " + cat.getName())
                .sourceModule("FIXED_ASSETS")
                .sourceReference("DEPR-" + cat.getCode() + "-" + date.toString())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(user)
                .currencyCode("AED")
                .lines(new ArrayList<>())
                .build();

        // Debit: Depreciation Expense Account
        je.getLines().add(JournalEntryLine.builder()
                .journalEntry(je)
                .account(cat.getDepreciationExpenseAccount())
                .debitAmount(amount)
                .creditAmount(BigDecimal.ZERO)
                .build());

        // Credit: Accumulated Depreciation Account
        je.getLines().add(JournalEntryLine.builder()
                .journalEntry(je)
                .account(cat.getAccumulatedDepreciationAccount())
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(amount)
                .build());

        return journalEntryRepository.save(je);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Physical Verification & Auditing
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public AssetAuditResponse submitAudit(Long companyId, AssetAuditRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));

        Warehouse warehouse = null;
        if (request.warehouseId() != null) {
            warehouse = warehouseRepository.findById(request.warehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.warehouseId()));
        }

        Store store = null;
        if (request.storeId() != null) {
            store = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.storeId()));
        }

        FixedAssetAudit audit = FixedAssetAudit.builder()
                .company(company)
                .auditDate(request.auditDate())
                .auditorName(request.auditorName())
                .warehouse(warehouse)
                .store(store)
                .build();

        FixedAssetAudit savedAudit = fixedAssetAuditRepository.save(audit);
        List<AssetAuditItemResponse> itemResponses = new ArrayList<>();

        for (AssetAuditItemRequest itemReq : request.items()) {
            FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, itemReq.fixedAssetId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found with ID: " + itemReq.fixedAssetId() + " for this company."));

            AssetAuditResult result = AssetAuditResult.valueOf(itemReq.result());

            FixedAssetAuditItem item = FixedAssetAuditItem.builder()
                    .audit(savedAudit)
                    .fixedAsset(asset)
                    .result(result)
                    .remarks(itemReq.remarks())
                    .photoEvidenceUrl(itemReq.photoEvidenceUrl())
                    .build();

            fixedAssetAuditItemRepository.save(item);

            // Audit Status Transition Side-Effects
            if (result == AssetAuditResult.MISSING) {
                asset.setStatus(FixedAssetStatus.LOST);
                fixedAssetRepository.save(asset);
            } else if (result == AssetAuditResult.DAMAGED) {
                asset.setStatus(FixedAssetStatus.UNDER_MAINTENANCE);
                fixedAssetRepository.save(asset);
            }

            itemResponses.add(new AssetAuditItemResponse(
                    item.getId(),
                    asset.getId(),
                    asset.getAssetCode(),
                    asset.getName(),
                    result.name(),
                    itemReq.remarks(),
                    itemReq.photoEvidenceUrl()
            ));
        }

        return new AssetAuditResponse(
                savedAudit.getId(),
                savedAudit.getAuditDate(),
                savedAudit.getAuditorName(),
                warehouse != null ? warehouse.getId() : null,
                warehouse != null ? warehouse.getName() : null,
                store != null ? store.getId() : null,
                store != null ? store.getName() : null,
                itemResponses
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // KPI Dashboard Services
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public FixedAssetDashboardResponse getDashboard(Long companyId) {
        List<FixedAsset> assets = fixedAssetRepository.findAllByCompanyId(companyId);

        BigDecimal totalAssetValue = BigDecimal.ZERO;
        BigDecimal netBookValue = BigDecimal.ZERO;
        BigDecimal monthlyDepr = BigDecimal.ZERO;

        Map<String, BigDecimal> categoryBreakdown = new HashMap<>();
        Map<String, BigDecimal> locationBreakdown = new HashMap<>();

        int nearEndOfLife = 0;
        int expiringWarranty = 0;
        int expiredInsurance = 0;
        int underMaintenance = 0;

        LocalDate today = LocalDate.now();
        LocalDate boundaryDate = today.plusMonths(6); // Expiring within 6 months

        for (FixedAsset asset : assets) {
            if (asset.getStatus() == FixedAssetStatus.DRAFT) {
                continue;
            }

            BigDecimal cost = asset.getOriginalCost();
            BigDecimal nbv = asset.getCurrentBookValue();
            BigDecimal depr = calculateMonthlyDepreciation(asset);

            totalAssetValue = totalAssetValue.add(cost);
            netBookValue = netBookValue.add(nbv);
            monthlyDepr = monthlyDepr.add(depr);

            // Category breakdown by Net Book Value
            String catName = asset.getCategory().getName();
            categoryBreakdown.put(catName, categoryBreakdown.getOrDefault(catName, BigDecimal.ZERO).add(nbv));

            // Location breakdown
            String locName = "Unassigned";
            if (asset.getWarehouse() != null) {
                locName = "Warehouse: " + asset.getWarehouse().getName();
            } else if (asset.getStore() != null) {
                locName = "Store: " + asset.getStore().getName();
            }
            locationBreakdown.put(locName, locationBreakdown.getOrDefault(locName, BigDecimal.ZERO).add(nbv));

            // KPIs & Warnings
            if (asset.getStatus() == FixedAssetStatus.UNDER_MAINTENANCE) {
                underMaintenance++;
            }

            // Near end of life (useful life completed, or current book value close to salvage value)
            if (asset.getCurrentBookValue().compareTo(asset.getSalvageValue()) <= 0) {
                nearEndOfLife++;
            }

            // Warranty Expiring within 6 months
            if (asset.getWarrantyEndDate() != null && 
                !asset.getWarrantyEndDate().isBefore(today) && 
                !asset.getWarrantyEndDate().isAfter(boundaryDate)) {
                expiringWarranty++;
            }

            // Insurance Expiring or Expired
            if (asset.getInsuranceExpiryDate() != null && asset.getInsuranceExpiryDate().isBefore(boundaryDate)) {
                expiredInsurance++;
            }
        }

        return new FixedAssetDashboardResponse(
                totalAssetValue.setScale(2, RoundingMode.HALF_UP),
                netBookValue.setScale(2, RoundingMode.HALF_UP),
                monthlyDepr.setScale(2, RoundingMode.HALF_UP),
                categoryBreakdown,
                nearEndOfLife,
                expiringWarranty,
                expiredInsurance,
                underMaintenance,
                locationBreakdown
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helper Methods & Manual Mappings
    // ─────────────────────────────────────────────────────────────────────────

    private void verifyNotLegalHold(FixedAsset asset) {
        if (Boolean.TRUE.equals(asset.getIsLegalHold())) {
            throw new BusinessException("Asset " + asset.getAssetCode() + " is under legal hold. Operations blocked.");
        }
    }

    private void recordHistory(FixedAsset asset, AssetHistoryEventType eventType, LocalDate eventDate,
                               String description, BigDecimal amount, Long referenceId, String username) {
        FixedAssetHistory history = FixedAssetHistory.builder()
                .fixedAsset(asset)
                .eventType(eventType)
                .eventDate(eventDate)
                .description(description)
                .amount(amount)
                .referenceId(referenceId)
                .performedBy(username != null ? username : "system")
                .build();
        fixedAssetHistoryRepository.save(history);
    }

    private User lookupUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return userRepository.findByEmail("admin@plus33.com")
                    .orElseThrow(() -> new ResourceNotFoundException("Default admin user not found"));
        }
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));
    }

    private AssetCategoryResponse mapToCategoryResponse(AssetCategory cat) {
        return new AssetCategoryResponse(
                cat.getId(),
                cat.getCode(),
                cat.getName(),
                cat.getDepreciationMethod().name(),
                cat.getDepreciationRate(),
                cat.getUsefulLifeYears(),
                cat.getAssetAccount().getId(),
                cat.getAssetAccount().getAccountCode(),
                cat.getAssetAccount().getAccountName(),
                cat.getAccumulatedDepreciationAccount().getId(),
                cat.getAccumulatedDepreciationAccount().getAccountCode(),
                cat.getDepreciationExpenseAccount().getId(),
                cat.getDepreciationExpenseAccount().getAccountCode(),
                cat.getGainLossAccount().getId(),
                cat.getGainLossAccount().getAccountCode()
        );
    }

    private FixedAssetResponse mapToAssetResponse(FixedAsset asset, boolean fetchComponents) {
        List<FixedAssetResponse> childComponents = new ArrayList<>();
        if (fetchComponents) {
            childComponents = fixedAssetRepository.findAllByParentAssetId(asset.getId()).stream()
                    .map(component -> mapToAssetResponse(component, false))
                    .toList();
        }

        return new FixedAssetResponse(
                asset.getId(),
                asset.getAssetCode(),
                asset.getName(),
                asset.getDescription(),
                asset.getCategory().getId(),
                asset.getCategory().getCode(),
                asset.getCategory().getName(),
                asset.getParentAsset() != null ? asset.getParentAsset().getId() : null,
                asset.getParentAsset() != null ? asset.getParentAsset().getAssetCode() : null,
                asset.getAcquisitionDate(),
                asset.getAcquisitionCost(),
                asset.getSalvageValue(),
                asset.getUsefulLifeYears(),
                asset.getDepreciationRate(),
                asset.getDepreciationMethod().name(),
                asset.getStatus().name(),
                asset.getWarehouse() != null ? asset.getWarehouse().getId() : null,
                asset.getWarehouse() != null ? asset.getWarehouse().getName() : null,
                asset.getStore() != null ? asset.getStore().getId() : null,
                asset.getStore() != null ? asset.getStore().getName() : null,
                asset.getSupplierInvoice() != null ? asset.getSupplierInvoice().getId() : null,
                asset.getOriginalCost(),
                asset.getCurrentBookValue(),
                asset.getAccumulatedDepreciation(),
                asset.getLastDepreciationDate(),
                asset.getAssetCode(), // QR code string is the asset code itself for scanning
                
                // Documents
                asset.getPurchaseInvoiceUrl(),
                asset.getWarrantyDocUrl(),
                asset.getInsuranceDocUrl(),
                asset.getPhotoUrl(),
                asset.getManualUrl(),
                
                // Warranty
                asset.getWarrantyStartDate(),
                asset.getWarrantyEndDate(),
                asset.getWarrantyVendor(),
                asset.getAmcExpiryDate(),
                asset.getAmcRenewalDate(),
                
                // Insurance
                asset.getInsurancePolicyNumber(),
                asset.getInsuranceCompany(),
                asset.getInsurancePremium(),
                asset.getInsuranceExpiryDate(),
                asset.getInsuredValue(),
                
                // Assignment
                asset.getAssignedEmployee() != null ? asset.getAssignedEmployee().getId() : null,
                asset.getAssignedEmployee() != null ? asset.getAssignedEmployee().getFirstName() + " " + asset.getAssignedEmployee().getLastName() : null,
                asset.getAssignedDepartment(),
                
                // CWIP & Budgeting
                asset.getIsCwip(),
                asset.getCwipAccount() != null ? asset.getCwipAccount().getId() : null,
                asset.getBudgetedCost(),
                asset.getActualCost(),
                
                // Barcode/NFC
                asset.getBarcodeOrNfcTag(),
                
                // Advanced Warranty
                asset.getWarrantyType(),
                asset.getWarrantyCoveredComponents(),
                asset.getWarrantyServiceContact(),
                
                // Health Score
                asset.getHealthScore(),
                
                // GIS Location
                asset.getLatitude(),
                asset.getLongitude(),
                asset.getSite(),
                asset.getBuilding(),
                asset.getFloor(),
                asset.getRoom(),
                asset.getRegion(),
                
                // IoT
                asset.getSensorId(),
                asset.getDeviceId(),
                asset.getLastHeartbeat(),
                asset.getTelemetryTemp(),
                asset.getTelemetryVibration(),
                asset.getTelemetryRuntimeHours(),
                
                // Multi-Currency
                asset.getAcquisitionCurrency(),
                asset.getFunctionalCurrency(),
                asset.getHistoricalExchangeRate(),
                asset.getReportingCurrency(),
                
                // Compliance
                asset.getIsDeleted(),
                asset.getIsLegalHold(),
                asset.getRetentionExpiryDate(),
                
                childComponents
        );
    }

    private AssetAssignmentResponse mapToAssignmentResponse(FixedAssetAssignment assignment) {
        return new AssetAssignmentResponse(
                assignment.getId(),
                assignment.getFixedAsset().getId(),
                assignment.getAssignedEmployee() != null ? assignment.getAssignedEmployee().getId() : null,
                assignment.getAssignedEmployee() != null ? assignment.getAssignedEmployee().getFirstName() + " " + assignment.getAssignedEmployee().getLastName() : null,
                assignment.getAssignedDepartment(),
                assignment.getAssignedWarehouse() != null ? assignment.getAssignedWarehouse().getId() : null,
                assignment.getAssignedWarehouse() != null ? assignment.getAssignedWarehouse().getName() : null,
                assignment.getAssignedStore() != null ? assignment.getAssignedStore().getId() : null,
                assignment.getAssignedStore() != null ? assignment.getAssignedStore().getName() : null,
                assignment.getAssignedAt(),
                assignment.getReleasedAt(),
                assignment.getAssignedBy()
        );
    }

    private AssetTransferResponse mapToTransferResponse(FixedAssetTransfer transfer) {
        return new AssetTransferResponse(
                transfer.getId(),
                transfer.getFixedAsset().getId(),
                transfer.getFixedAsset().getAssetCode(),
                transfer.getFixedAsset().getName(),
                transfer.getTransferDate(),
                transfer.getFromWarehouse() != null ? transfer.getFromWarehouse().getId() : null,
                transfer.getFromWarehouse() != null ? transfer.getFromWarehouse().getName() : null,
                transfer.getFromStore() != null ? transfer.getFromStore().getId() : null,
                transfer.getFromStore() != null ? transfer.getFromStore().getName() : null,
                transfer.getToWarehouse() != null ? transfer.getToWarehouse().getId() : null,
                transfer.getToWarehouse() != null ? transfer.getToWarehouse().getName() : null,
                transfer.getToStore() != null ? transfer.getToStore().getId() : null,
                transfer.getToStore() != null ? transfer.getToStore().getName() : null,
                transfer.getToCompany() != null ? transfer.getToCompany().getId() : null,
                transfer.getToCompany() != null ? transfer.getToCompany().getName() : null,
                transfer.getStatus().name(),
                transfer.getReason(),
                transfer.getTransferredBy()
        );
    }

    private AssetMaintenanceResponse mapToMaintenanceResponse(FixedAssetMaintenance m) {
        return new AssetMaintenanceResponse(
                m.getId(),
                m.getFixedAsset().getId(),
                m.getMaintenanceDate(),
                m.getDescription(),
                m.getCost(),
                m.getCapitalize(),
                m.getJournalEntry() != null ? m.getJournalEntry().getId() : null,
                m.getPerformedBy(),
                m.getCreatedAt()
        );
    }

    private AssetRevaluationResponse mapToRevaluationResponse(FixedAssetRevaluation r) {
        return new AssetRevaluationResponse(
                r.getId(),
                r.getFixedAsset().getId(),
                r.getRevaluationDate(),
                r.getPreviousValue(),
                r.getNewFairValue(),
                r.getRevaluationReserveAccount().getId(),
                r.getJournalEntry() != null ? r.getJournalEntry().getId() : null,
                r.getReason(),
                r.getPerformedBy()
        );
    }

    private AssetImpairmentResponse mapToImpairmentResponse(FixedAssetImpairment i) {
        return new AssetImpairmentResponse(
                i.getId(),
                i.getFixedAsset().getId(),
                i.getImpairmentDate(),
                i.getImpairmentAmount(),
                i.getRecoverableAmount(),
                i.getJournalEntry() != null ? i.getJournalEntry().getId() : null,
                i.getReason(),
                i.getPerformedBy()
        );
    }

    private AssetLeaseResponse mapToLeaseResponse(FixedAssetLease l) {
        return new AssetLeaseResponse(
                l.getId(),
                l.getFixedAsset().getId(),
                l.getLeaseType().name(),
                l.getLeaseStartDate(),
                l.getLeaseEndDate(),
                l.getMonthlyLeasePayment(),
                l.getLessorName(),
                l.getLeaseLiabilityAccount().getId()
        );
    }

    private AssetMaintenancePlanResponse mapToMaintenancePlanResponse(FixedAssetMaintenancePlan p) {
        return new AssetMaintenancePlanResponse(
                p.getId(),
                p.getFixedAsset().getId(),
                p.getFixedAsset().getAssetCode(),
                p.getFixedAsset().getName(),
                p.getMaintenanceType(),
                p.getMaintenanceType(),
                p.getNextMaintenanceDate(),
                p.getEstimatedCost(),
                p.getMaintenanceVendor(),
                null,
                p.getActive()
        );
    }

    private AssetWorkOrderResponse mapToWorkOrderResponse(FixedAssetWorkOrder wo) {
        return new AssetWorkOrderResponse(
                wo.getId(),
                wo.getFixedAsset().getId(),
                wo.getFixedAsset().getAssetCode(),
                wo.getFixedAsset().getName(),
                wo.getWorkOrderNumber(),
                wo.getDescription(),
                null, // priority not on entity directly
                wo.getStatus().name(),
                null, // scheduledDate not on entity directly
                null, // completedDate
                wo.getLaborCost().add(wo.getPartsCost()),
                wo.getLaborCost().add(wo.getPartsCost()),
                null
        );
    }

    @Override
    public void deleteAsset(Long companyId, Long id, String username) {
        FixedAsset asset = fixedAssetRepository.findByCompanyIdAndId(companyId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Fixed asset not found with ID: " + id));

        verifyNotLegalHold(asset);

        asset.setIsDeleted(true);
        fixedAssetRepository.save(asset);

        recordHistory(asset, AssetHistoryEventType.RETIREMENT, LocalDate.now(),
                "Asset soft deleted", null, null, username);
    }
}
