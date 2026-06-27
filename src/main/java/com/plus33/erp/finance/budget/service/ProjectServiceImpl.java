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

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;

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

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Project not found"));
        return mapToResponse(project);
    }

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
