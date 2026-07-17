/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : CountryDocumentTypeRepository.java
 * DB Table          : country_document_types
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.CountryDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link CountryDocumentType}.
 * Provides queries to retrieve country-specific document requirements
 * ordered by category then document position.
 */
@Repository
public interface CountryDocumentTypeRepository extends JpaRepository<CountryDocumentType, Long> {

    /**
     * Returns all document requirements for the given country code,
     * sorted by category display order then individual document sort order.
     *
     * @param countryCode e.g. "IN", "EU", "AE"
     * @return ordered list of country document type records
     */
    List<CountryDocumentType> findByCountryCodeOrderByCategorySortAscDocSortAsc(String countryCode);
}
