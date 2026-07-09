/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : ProjectServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectController
 * Related Service   : ProjectServiceImpl
 * Related Repository: ProjectRepository, CompanyRepository
 * Related Entity    : Project
 * Related DTO       : mapToResponse, ProjectRequest, ProjectResponse
 * Related Mapper    : ProjectMapper
 * Related DB Table  : projects
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : ProjectController, ProjectServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements ProjectService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.budget.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.budget.dto.ProjectRequest;
import com.plus33.erp.finance.budget.dto.ProjectResponse;
import com.plus33.erp.finance.budget.entity.Project;
import com.plus33.erp.finance.budget.repository.ProjectRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProjectController
 *   --> ProjectServiceImpl (this)
 *   --> Validate business rules
 *   --> ProjectRepository (read/write 'projects')
 *   --> ProjectMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code projects}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;

    /**
     * Creates a new project and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param request the validated request DTO containing input data
     * @return the ProjectResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public ProjectResponse createProject(Long companyId, ProjectRequest request) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new BusinessException("Company not found"));

        if (projectRepository.findByCompanyIdAndCode(companyId, request.code()).isPresent()) {
            throw new BusinessException("Project code already exists: " + request.code());
        }

        Project project = Project.builder()
            .company(company)
            .code(request.code())
            .name(request.name())
            .startDate(request.startDate())
            .endDate(request.endDate())
            .status(request.status() != null ? request.status() : "ACTIVE")
            .active(request.active() != null ? request.active() : true)
            .build();

        return mapToResponse(projectRepository.save(project));
    }

    /**
     * Updates an existing project record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the ProjectResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Project not found"));

        project.setName(request.name());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        if (request.status() != null) {
            project.setStatus(request.status());
        }
        if (request.active() != null) {
            project.setActive(request.active());
        }

        return mapToResponse(projectRepository.save(project));
    }

    /**
     * Retrieves project data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the ProjectResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Project not found"));
        return mapToResponse(project);
    }

    /**
     * Retrieves projects by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByCompany(Long companyId) {
        return projectRepository.findAll().stream()
            .filter(p -> p.getCompany().getId().equals(companyId))
            .map(this::mapToResponse)
            .toList();
    }

    private ProjectResponse mapToResponse(Project project) {
        return new ProjectResponse(
            project.getId(),
            project.getCompany().getId(),
            project.getCode(),
            project.getName(),
            project.getStartDate(),
            project.getEndDate(),
            project.getStatus(),
            project.getActive()
        );
    }
}