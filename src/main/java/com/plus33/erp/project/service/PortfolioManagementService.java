package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.ProjectPortfolio;
import com.plus33.erp.project.repository.ProjectPortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PortfolioManagementService {

    private final ProjectPortfolioRepository portfolioRepository;

    public PortfolioManagementService(ProjectPortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

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
