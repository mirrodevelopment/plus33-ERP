/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.dto
 * File              : TopSupplierDTO.java
 * Purpose           : Data Transfer Object for request/response in Ap Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TopSupplierDTOController
 * Related Service   : TopSupplierDTOService, TopSupplierDTOServiceImpl
 * Related Repository: TopSupplierDTORepository
 * Related Entity    : TopSupplierDTO
 * Related DTO       : TopSupplierDTO
 * Related Mapper    : TopSupplierDTOMapper
 * Related DB Table  : top_supplier_d_t_os
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TopSupplierDTOController, TopSupplierDTOService, TopSupplierDTOServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ap Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ap.dto;

import java.math.BigDecimal;

public record TopSupplierDTO(
        Long supplierId,
        String supplierName,
        BigDecimal outstandingBalance
) {}
