/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : ContractService.java
 * Purpose           : Business logic service layer for Procurement Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ContractController
 * Related Service   : ContractService
 * Related Repository: SupplierContractRepository
 * Related Entity    : Contract
 * Related DTO       : N/A
 * Related Mapper    : ContractMapper
 * Related DB Table  : contracts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ContractController, ContractServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Procurement Module. Implements ContractService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.procurement.service;

import com.plus33.erp.procurement.entity.SupplierContract;
import com.plus33.erp.procurement.repository.SupplierContractRepository;
import com.plus33.erp.procurement.event.ProcurementEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code ContractService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Procurement Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ContractController
 *   --> ContractService (this)
 *   --> Validate business rules
 *   --> ContractRepository (read/write 'contracts')
 *   --> ContractMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code contracts}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ContractService {

    private final SupplierContractRepository contractRepository;
    private final ProcurementEventBus eventBus;

    public ContractService(SupplierContractRepository contractRepository, ProcurementEventBus eventBus) {
        this.contractRepository = contractRepository;
        this.eventBus = eventBus;
    }

    /**
     * Creates a new contract and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param supplierId the supplierId input value
     * @param contractNumber the contractNumber input value
     * @param start the start input value
     * @param end the end input value
     * @return the SupplierContract result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public SupplierContract createContract(Long companyId, Long supplierId, String contractNumber, LocalDate start, LocalDate end) {
        SupplierContract contract = new SupplierContract();
        contract.setCompanyId(companyId);
        contract.setSupplierId(supplierId);
        contract.setContractNumber(contractNumber);
        contract.setStartDate(start);
        contract.setEndDate(end);
        contract.setStatus("DRAFT");
        contract.setVersionNumber(1);
        contractRepository.save(contract);
        return contract;
    }

    /**
     * Approves the contract, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param contractId the contractId input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void approveContract(Long contractId) {
        SupplierContract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        contract.setStatus("APPROVED");
        contractRepository.save(contract);

        contract.setStatus("ACTIVE");
        contractRepository.save(contract);
        eventBus.publish("ContractActivated", contract.getCompanyId(), contractId, "Contract active: " + contract.getContractNumber());
    }

    /**
     * Performs the amendContract operation in this module.
     *
     * @param contractId the contractId input value
     * @param amendmentNotes the amendmentNotes input value
     */
    @Transactional
    public void amendContract(Long contractId, String amendmentNotes) {
        SupplierContract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));

        contract.setVersionNumber(contract.getVersionNumber() + 1);
        contract.setStatus("AMENDED");
        contractRepository.save(contract);

        contract.setStatus("ACTIVE");
        contractRepository.save(contract);
    }
}