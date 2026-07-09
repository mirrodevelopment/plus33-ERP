/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxPostingProfileService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxPostingProfileController
 * Related Service   : TaxPostingProfileService, TaxPostingProfileServiceImpl
 * Related Repository: TaxPostingProfileRepository
 * Related Entity    : TaxPostingProfile
 * Related DTO       : N/A
 * Related Mapper    : TaxPostingProfileMapper
 * Related DB Table  : tax_posting_profiles
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.entity.TaxPostingProfile;

public interface TaxPostingProfileService {
    TaxPostingProfile getPostingProfile(Long companyId, Long categoryId);
    TaxPostingProfile getPostingProfileByCategoryCode(Long companyId, String categoryCode);
}
