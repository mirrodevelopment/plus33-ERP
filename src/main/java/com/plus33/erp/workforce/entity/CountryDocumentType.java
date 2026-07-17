/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : CountryDocumentType.java
 * Purpose           : JPA Entity for country-specific employee document requirements
 * DB Table          : country_document_types
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;

/**
 * Represents a single document requirement for employees of a specific country.
 * Each row defines one document (e.g. "PAN Card") within a category (e.g. "tax")
 * for a country code (e.g. "IN", "EU", "AE").
 */
@Entity
@Table(name = "country_document_types")
public class CountryDocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ISO-style country code: 'IN', 'EU', 'AE' */
    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode;

    /** Logical category key e.g. 'personal', 'tax', 'social_security' */
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    /** Human-readable category label displayed as the card heading */
    @Column(name = "category_label", nullable = false, length = 100)
    private String categoryLabel;

    /** Lucide icon name for the category header */
    @Column(name = "category_icon", nullable = false, length = 50)
    private String categoryIcon;

    /** Unique document key matching documentType in employee_upload_documents */
    @Column(name = "doc_key", nullable = false, length = 100)
    private String docKey;

    /** Short display title shown in the document row */
    @Column(name = "doc_title", nullable = false, length = 200)
    private String docTitle;

    /** Description text shown below the document title */
    @Column(name = "doc_description")
    private String docDescription;

    /** Whether this document is mandatory for the employee's country */
    @Column(name = "is_required", nullable = false)
    private boolean required;

    /** Sort order of the category (lower = shown first) */
    @Column(name = "category_sort", nullable = false)
    private int categorySort;

    /** Sort order of the document within its category */
    @Column(name = "doc_sort", nullable = false)
    private int docSort;

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCategoryLabel() { return categoryLabel; }
    public void setCategoryLabel(String categoryLabel) { this.categoryLabel = categoryLabel; }

    public String getCategoryIcon() { return categoryIcon; }
    public void setCategoryIcon(String categoryIcon) { this.categoryIcon = categoryIcon; }

    public String getDocKey() { return docKey; }
    public void setDocKey(String docKey) { this.docKey = docKey; }

    public String getDocTitle() { return docTitle; }
    public void setDocTitle(String docTitle) { this.docTitle = docTitle; }

    public String getDocDescription() { return docDescription; }
    public void setDocDescription(String docDescription) { this.docDescription = docDescription; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public int getCategorySort() { return categorySort; }
    public void setCategorySort(int categorySort) { this.categorySort = categorySort; }

    public int getDocSort() { return docSort; }
    public void setDocSort(int docSort) { this.docSort = docSort; }
}
