package com.plus33.erp.finance.budget.service;

import com.plus33.erp.finance.budget.dto.ProjectRequest;
import com.plus33.erp.finance.budget.dto.ProjectResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse createProject(Long companyId, ProjectRequest request);
    ProjectResponse updateProject(Long id, ProjectRequest request);
    ProjectResponse getProject(Long id);
    List<ProjectResponse> getProjectsByCompany(Long companyId);
}
