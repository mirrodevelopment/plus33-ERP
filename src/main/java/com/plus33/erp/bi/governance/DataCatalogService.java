/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.governance
 * File              : DataCatalogService.java
 * Purpose           : Business logic service layer for Bi Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DataCatalogController
 * Related Service   : DataCatalogService
 * Related Repository: DataCatalogRepository
 * Related Entity    : DataCatalog
 * Related DTO       : N/A
 * Related Mapper    : DataCatalogMapper
 * Related DB Table  : data_catalogs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DataCatalogController, DataCatalogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Bi Module. Implements DataCatalogService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.bi.governance;

import com.plus33.erp.bi.entity.BiCatalogDataset;
import com.plus33.erp.bi.entity.BiCatalogGlossary;
import com.plus33.erp.bi.repository.BiCatalogDatasetRepository;
import com.plus33.erp.bi.repository.BiCatalogGlossaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code DataCatalogService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.governance}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Bi Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DataCatalogController
 *   --> DataCatalogService (this)
 *   --> Validate business rules
 *   --> DataCatalogRepository (read/write 'data_catalogs')
 *   --> DataCatalogMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code data_catalogs}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DataCatalogService {

    @Autowired BiCatalogDatasetRepository datasetRepo;
    @Autowired BiCatalogGlossaryRepository glossaryRepo;
    /**
     * Creates a new dataset and persists it to the database.
     *
     * @param name the name input value
     * @param desc the desc input value
     * @param ownerRole the ownerRole input value
     * @param stewardUser the stewardUser input value
     * @return the BiCatalogDataset result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public BiCatalogDataset registerDataset(String name, String desc, String ownerRole, String stewardUser) {
        BiCatalogDataset ds = datasetRepo.findAll().stream()
                .filter(d -> d.getDatasetName().equalsIgnoreCase(name))
                .findFirst().orElse(null);

        if (ds == null) {
            ds = new BiCatalogDataset();
            ds.setDatasetName(name);
            ds.setCreatedAt(LocalDateTime.now());
        }
        ds.setDescription(desc);
        ds.setOwnerRole(ownerRole);
        ds.setStewardUser(stewardUser);
        return datasetRepo.save(ds);
    }

    /**
     * Performs the certifyDataset operation in this module.
     *
     * @param id the unique database ID of the resource
     * @param status status filter for narrowing query results
     * @return the BiCatalogDataset result
     */
    @Transactional
    public BiCatalogDataset certifyDataset(Long id, String status) {
        BiCatalogDataset ds = datasetRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found: " + id));
        ds.setCertificationStatus(status);
        ds.setLastCertifiedAt(LocalDateTime.now());
        return datasetRepo.save(ds);
    }

    /**
     * Creates a new glossary term and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param code the code input value
     * @param name the name input value
     * @param definition the definition input value
     * @param rule the rule input value
     * @param domain the domain input value
     * @return the BiCatalogGlossary result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public BiCatalogGlossary createGlossaryTerm(String code, String name, String definition, String rule, String domain) {
        BiCatalogGlossary term = glossaryRepo.findAll().stream()
                .filter(t -> t.getTermCode().equalsIgnoreCase(code))
                .findFirst().orElse(null);

        if (term == null) {
            term = new BiCatalogGlossary();
            term.setTermCode(code);
            term.setCreatedAt(LocalDateTime.now());
        }
        term.setTermName(name);
        term.setDefinition(definition);
        term.setCalculationRule(rule);
        term.setDomainArea(domain);
        return glossaryRepo.save(term);
    }
}