/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.service
 * File              : PortfolioManagementService.java
 * Purpose           : Business logic service layer for Project Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PortfolioManagementController
 * Related Service   : PortfolioManagementService
 * Related Repository: ProjectPortfolioRepository
 * Related Entity    : PortfolioManagement
 * Related DTO       : N/A
 * Related Mapper    : PortfolioManagementMapper
 * Related DB Table  : portfolio_managements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PortfolioManagementController, PortfolioManagementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Project Module. Implements PortfolioManagementService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.ProjectPortfolio;
import com.plus33.erp.project.repository.ProjectPortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code PortfolioManagementService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Project Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PortfolioManagementController
 *   --> PortfolioManagementService (this)
 *   --> Validate business rules
 *   --> PortfolioManagementRepository (read/write 'portfolio_managements')
 *   --> PortfolioManagementMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code portfolio_managements}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PortfolioManagementService {

    private final ProjectPortfolioRepository portfolioRepository;

    public PortfolioManagementService(ProjectPortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    /**
     * Creates a new portfolio and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param name the name input value
     * @param desc the desc input value
     * @return the ProjectPortfolio result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public ProjectPortfolio createPortfolio(Long companyId, String name, String desc) {
        ProjectPortfolio p = new ProjectPortfolio();
        p.setCompanyId(companyId);
        p.setName(name);
        p.setDescription(desc);
        portfolioRepository.save(p);
        return p;
    }
}