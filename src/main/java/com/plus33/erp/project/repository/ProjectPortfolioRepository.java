/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectPortfolioRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectPortfolioController
 * Related Service   : ProjectPortfolioService, ProjectPortfolioServiceImpl
 * Related Repository: ProjectPortfolioRepository
 * Related Entity    : ProjectPortfolio
 * Related DTO       : N/A
 * Related Mapper    : ProjectPortfolioMapper
 * Related DB Table  : project_portfolios
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectPortfolioService, ProjectPortfolioServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_portfolios' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectPortfolioRepository extends JpaRepository<ProjectPortfolio, Long> {
}
