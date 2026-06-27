package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.entity.TaxPostingProfile;
import com.plus33.erp.finance.tax.repository.TaxPostingProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaxPostingProfileServiceImpl implements TaxPostingProfileService {

    private final TaxPostingProfileRepository postingProfileRepository;

    @Override
    public TaxPostingProfile getPostingProfile(Long companyId, Long categoryId) {
        return postingProfileRepository.findByCompanyIdAndCategoryId(companyId, categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Tax Posting Profile not found for company " + companyId + " and category " + categoryId));
    }

    @Override
    public TaxPostingProfile getPostingProfileByCategoryCode(Long companyId, String categoryCode) {
        return postingProfileRepository.findByCompanyIdAndCategoryCode(companyId, categoryCode)
                .orElseThrow(() -> new IllegalArgumentException("Tax Posting Profile not found for company " + companyId + " and category code " + categoryCode));
    }
}
