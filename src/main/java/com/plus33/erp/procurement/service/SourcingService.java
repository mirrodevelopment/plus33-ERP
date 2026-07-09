/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : SourcingService.java
 * Purpose           : Business logic service layer for Procurement Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SourcingController
 * Related Service   : SourcingService
 * Related Repository: ProcurementRequisitionRepository, ProcurementRfqRepository, ProcurementRfqVersionRepository, SupplierResponseRepository, PurchaseOrderRepository, CompanyRepository, SupplierRepository, UserRepository
 * Related Entity    : Sourcing
 * Related DTO       : SupplierResponse
 * Related Mapper    : SourcingMapper
 * Related DB Table  : sourcings
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : SourcingController, SourcingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Procurement Module. Implements SourcingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SourcingService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Procurement Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SourcingController
 *   --> SourcingService (this)
 *   --> Validate business rules
 *   --> SourcingRepository (read/write 'sourcings')
 *   --> SourcingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code sourcings}</p>
 * <p><b>Module Deps      :</b> Procurement, Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Creates a new requisition and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param requisitionNumber the requisitionNumber input value
     * @param createdBy the createdBy input value
     * @return the ProcurementRequisition result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Performs the transitionRequisitionStatus operation in this module.
     *
     * @param requisitionId the requisitionId input value
     * @param targetStatus the targetStatus input value
     */
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

    /**
     * Creates a new rfq and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param rfqNumber the rfqNumber input value
     * @return the ProcurementRfq result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Creates a new new rfq version and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param rfqId the rfqId input value
     * @param notes the notes input value
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Submits the bid for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param rfqVersionId the rfqVersionId input value
     * @param supplierId the supplierId input value
     * @param amount the amount input value
     * @return the SupplierResponse result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Performs the awardAndGeneratePo operation in this module.
     *
     * @param rfqVersionId the rfqVersionId input value
     * @param supplierId the supplierId input value
     * @return the PurchaseOrder result
     */
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