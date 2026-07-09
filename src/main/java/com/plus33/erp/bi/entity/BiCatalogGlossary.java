/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiCatalogGlossary.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiCatalogGlossaryController
 * Related Service   : BiCatalogGlossaryService, BiCatalogGlossaryServiceImpl
 * Related Repository: BiCatalogGlossaryRepository
 * Related Entity    : BiCatalogGlossary
 * Related DTO       : N/A
 * Related Mapper    : BiCatalogGlossaryMapper
 * Related DB Table  : bi_catalog_glossary
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiCatalogGlossaryRepository, BiCatalogGlossaryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_catalog_glossary'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiCatalogGlossary}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_catalog_glossary'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_catalog_glossary}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_catalog_glossary")
public class BiCatalogGlossary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "term_code", nullable = false, unique = true)
    private String termCode;
    @Column(name = "term_name", nullable = false)
    private String termName;
    @Column(nullable = false)
    private String definition;
    @Column(name = "calculation_rule")
    private String calculationRule;
    @Column(name = "domain_area", nullable = false)
    private String domainArea;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves term code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTermCode() { return termCode; }
    /**
     * Performs the setTermCode operation in this module.
     *
     * @param termCode the termCode input value
     */
    public void setTermCode(String termCode) { this.termCode = termCode; }
    /**
     * Retrieves term name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTermName() { return termName; }
    /**
     * Performs the setTermName operation in this module.
     *
     * @param termName the termName input value
     */
    public void setTermName(String termName) { this.termName = termName; }
    /**
     * Retrieves definition data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDefinition() { return definition; }
    /**
     * Performs the setDefinition operation in this module.
     *
     * @param definition the definition input value
     */
    public void setDefinition(String definition) { this.definition = definition; }
    /**
     * Retrieves calculation rule data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCalculationRule() { return calculationRule; }
    /**
     * Performs the setCalculationRule operation in this module.
     *
     * @param calculationRule the calculationRule input value
     */
    public void setCalculationRule(String calculationRule) { this.calculationRule = calculationRule; }
    /**
     * Retrieves domain area data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDomainArea() { return domainArea; }
    /**
     * Performs the setDomainArea operation in this module.
     *
     * @param domainArea the domainArea input value
     */
    public void setDomainArea(String domainArea) { this.domainArea = domainArea; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}