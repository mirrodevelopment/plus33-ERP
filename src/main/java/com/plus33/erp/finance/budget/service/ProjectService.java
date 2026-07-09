/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : ProjectService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectController
 * Related Service   : ProjectService, ProjectServiceImpl
 * Related Repository: ProjectRepository
 * Related Entity    : Project
 * Related DTO       : ProjectRequest, ProjectResponse
 * Related Mapper    : ProjectMapper
 * Related DB Table  : projects
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.budget.service;

import com.plus33.erp.finance.budget.dto.ProjectRequest;
import com.plus33.erp.finance.budget.dto.ProjectResponse;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProjectService {
    ProjectResponse createProject(Long companyId, ProjectRequest request);
    ProjectResponse updateProject(Long id, ProjectRequest request);
    ProjectResponse getProject(Long id);
    List<ProjectResponse> getProjectsByCompany(Long companyId);
}
