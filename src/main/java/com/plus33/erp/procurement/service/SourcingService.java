package com.plus33.erp.procurement.service;

import com.plus33.erp.procurement.entity.*;
import com.plus33.erp.procurement.repository.*;
import com.plus33.erp.procurement.event.ProcurementEventBus;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SourcingService {

    private final ProcurementRequisitionRepository requisitionRepository;
    private final ProcurementRfqRepository rfqRepository;
    private final ProcurementRfqVersionRepository rfqVersionRepository;
    private final SupplierResponseRepository responseRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final CompanyRepository companyRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final ProcurementEventBus eventBus;

    public SourcingService(ProcurementRequisitionRepository requisitionRepository,
                           ProcurementRfqRepository rfqRepository,
                           ProcurementRfqVersionRepository rfqVersionRepository,
                           SupplierResponseRepository responseRepository,
                           PurchaseOrderRepository purchaseOrderRepository,
                           CompanyRepository companyRepository,
                           SupplierRepository supplierRepository,
                           UserRepository userRepository,
                           ProcurementEventBus eventBus) {
        this.requisitionRepository = requisitionRepository;
        this.rfqRepository = rfqRepository;
        this.rfqVersionRepository = rfqVersionRepository;
        this.responseRepository = responseRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.companyRepository = companyRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
        this.eventBus = eventBus;
    }

    @Transactional
    public ProcurementRequisition createRequisition(Long companyId, String requisitionNumber, Long createdBy) {
        ProcurementRequisition req = new ProcurementRequisition();
        req.setCompanyId(companyId);
        req.setRequisitionNumber(requisitionNumber);
        req.setCreatedBy(createdBy);
        req.setStatus("REQUISITION_DRAFT");
        requisitionRepository.save(req);

        eventBus.publish("RequisitionCreated", companyId, req.getId(), "Requisition " + requisitionNumber + " created");
        return req;
    }

    @Transactional
    public void transitionRequisitionStatus(Long requisitionId, String targetStatus) {
        ProcurementRequisition req = requisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new IllegalArgumentException("Requisition not found"));

        req.setStatus(targetStatus);
        req.setUpdatedAt(LocalDateTime.now());
        requisitionRepository.save(req);

        if ("APPROVED".equals(targetStatus)) {
            eventBus.publish("RequisitionApproved", req.getCompanyId(), requisitionId, "Requisition approved");
        }
    }

    @Transactional
    public ProcurementRfq createRfq(Long companyId, String rfqNumber) {
        ProcurementRfq rfq = new ProcurementRfq();
        rfq.setCompanyId(companyId);
        rfq.setRfqNumber(rfqNumber);
        rfq.setCurrentVersion(1);
        rfq.setStatus("DRAFT");
        rfqRepository.save(rfq);

        ProcurementRfqVersion version = new ProcurementRfqVersion();
        version.setRfqId(rfq.getId());
        version.setVersionNumber(1);
        version.setStatus("ACTIVE");
        version.setEffectiveDate(LocalDate.now());
        rfqVersionRepository.save(version);

        eventBus.publish("RFQPublished", companyId, rfq.getId(), "RFQ Published");
        return rfq;
    }

    @Transactional
    public void createNewRfqVersion(Long rfqId, String notes) {
        ProcurementRfq rfq = rfqRepository.findById(rfqId)
                .orElseThrow(() -> new IllegalArgumentException("RFQ not found"));

        // Deprecate old versions
        rfqVersionRepository.findByRfqId(rfqId).forEach(v -> {
            v.setStatus("DEPRECATED");
            rfqVersionRepository.save(v);
        });

        int nextVer = rfq.getCurrentVersion() + 1;
        rfq.setCurrentVersion(nextVer);
        rfqRepository.save(rfq);

        ProcurementRfqVersion newVer = new ProcurementRfqVersion();
        newVer.setRfqId(rfqId);
        newVer.setVersionNumber(nextVer);
        newVer.setStatus("ACTIVE");
        newVer.setEffectiveDate(LocalDate.now());
        rfqVersionRepository.save(newVer);
    }

    @Transactional
    public SupplierResponse submitBid(Long rfqVersionId, Long supplierId, BigDecimal amount) {
        SupplierResponse response = new SupplierResponse();
        response.setRfqVersionId(rfqVersionId);
        response.setSupplierId(supplierId);
        response.setBidAmount(amount);
        responseRepository.save(response);

        // Fetch RFQ ID to get company context
        ProcurementRfqVersion rfqVer = rfqVersionRepository.findById(rfqVersionId).get();
        ProcurementRfq rfq = rfqRepository.findById(rfqVer.getRfqId()).get();

        eventBus.publish("SupplierBidReceived", rfq.getCompanyId(), rfq.getId(), "Bid received from supplier " + supplierId);
        return response;
    }

    @Transactional
    public PurchaseOrder awardAndGeneratePo(Long rfqVersionId, Long supplierId) {
        ProcurementRfqVersion rfqVer = rfqVersionRepository.findById(rfqVersionId)
                .orElseThrow(() -> new IllegalArgumentException("RFQ Version not found"));
        ProcurementRfq rfq = rfqRepository.findById(rfqVer.getRfqId()).get();

        Company company = companyRepository.findById(rfq.getCompanyId()).get();
        Supplier supplier = supplierRepository.findById(supplierId).get();
        User dummyUser = userRepository.findAll().stream().findFirst().orElseGet(() -> {
            User u = new User();
            u.setFirstName("Procurement");
            u.setLastName("Admin");
            u.setEmail("proc@company.com");
            u.setPassword("encoded_pass");
            return userRepository.save(u);
        });

        PurchaseOrder po = new PurchaseOrder();
        po.setCompany(company);
        po.setSupplier(supplier);
        po.setOrderNumber("PO-AWARD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        po.setOrderedBy(dummyUser);
        po.setStatus(PurchaseOrderStatus.DRAFT);
        purchaseOrderRepository.save(po);

        rfq.setStatus("CLOSED");
        rfqRepository.save(rfq);

        eventBus.publish("SupplierAwarded", rfq.getCompanyId(), rfq.getId(), "Supplier " + supplierId + " awarded");
        eventBus.publish("PurchaseOrderCreated", rfq.getCompanyId(), po.getId(), "Awarded PO " + po.getOrderNumber() + " created");

        return po;
    }
}
