package com.plus33.erp.procurement.service;

import com.plus33.erp.procurement.entity.SupplierContract;
import com.plus33.erp.procurement.repository.SupplierContractRepository;
import com.plus33.erp.procurement.event.ProcurementEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ContractService {

    private final SupplierContractRepository contractRepository;
    private final ProcurementEventBus eventBus;

    public ContractService(SupplierContractRepository contractRepository, ProcurementEventBus eventBus) {
        this.contractRepository = contractRepository;
        this.eventBus = eventBus;
    }

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
