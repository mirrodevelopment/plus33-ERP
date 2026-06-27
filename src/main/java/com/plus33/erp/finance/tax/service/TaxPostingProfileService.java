package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.entity.TaxPostingProfile;

public interface TaxPostingProfileService {
    TaxPostingProfile getPostingProfile(Long companyId, Long categoryId);
    TaxPostingProfile getPostingProfileByCategoryCode(Long companyId, String categoryCode);
}
