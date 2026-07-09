/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.controller
 * File              : BiGovernanceController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiGovernanceController
 * Related Service   : BiGovernanceControllerService, BiGovernanceControllerServiceImpl
 * Related Repository: BiGovernanceControllerRepository
 * Related Entity    : BiGovernanceController
 * Related DTO       : N/A
 * Related Mapper    : BiGovernanceControllerMapper
 * Related DB Table  : bi_governance_controllers
 * Related REST APIs : GET /api/bi/governance/mask, POST /api/bi/governance/dataset/register, POST /api/bi/governance/dataset/certify, POST /api/bi/governance/glossary/term
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Bi Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/bi/governance/mask, POST /api/bi/governance/dataset/register, POST /api/bi/governance/dataset/certify, POST /api/bi/governance/glossary/term
 ******************************************************************************/
package com.plus33.erp.bi.controller;

import com.plus33.erp.bi.entity.BiCatalogDataset;
import com.plus33.erp.bi.entity.BiCatalogGlossary;
import com.plus33.erp.bi.governance.DataCatalogService;
import com.plus33.erp.bi.governance.DataMaskingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiGovernanceController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to BiGovernanceService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> BiGovernanceController.endpoint()
 *   --> BiGovernanceService.method()
 *   --> BiGovernanceRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/bi/governance/mask, POST /api/bi/governance/dataset/register, POST /api/bi/governance/dataset/certify, POST /api/bi/governance/glossary/term</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/bi/governance")
public class BiGovernanceController {

    @Autowired DataMaskingService maskingService;
    @Autowired DataCatalogService catalogService;
    /**
     * Performs the maskField operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param table the table input value
     * @param column the column input value
     * @param val the val input value
     * @return the result string value
     */
    @GetMapping("/mask")
    public String maskField(@RequestParam String table, @RequestParam String column, @RequestParam String val) {
        return maskingService.maskValue(table, column, val);
    }

    /**
     * Creates a new dataset and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return the BiCatalogDataset result
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/dataset/register")
    public BiCatalogDataset registerDataset(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String ownerRole,
            @RequestParam(required = false) String stewardUser) {
        return catalogService.registerDataset(name, description, ownerRole, stewardUser);
    }

    /**
     * Performs the certifyDataset operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param status status filter for narrowing query results
     * @return the BiCatalogDataset result
     */
    @PostMapping("/dataset/certify")
    public BiCatalogDataset certifyDataset(@RequestParam Long id, @RequestParam String status) {
        return catalogService.certifyDataset(id, status);
    }

    /**
     * Creates a new glossary term and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return the BiCatalogGlossary result
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/glossary/term")
    public BiCatalogGlossary createGlossaryTerm(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String definition,
            @RequestParam(required = false) String rule,
            @RequestParam String domain) {
        return catalogService.createGlossaryTerm(code, name, definition, rule, domain);
    }
}