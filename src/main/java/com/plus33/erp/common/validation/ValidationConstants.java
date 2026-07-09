/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * Package           : com.plus33.erp.common.validation
 * File              : ValidationConstants.java
 * Purpose           : Component of Common Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ValidationConstantsController
 * Related Service   : ValidationConstantsService, ValidationConstantsServiceImpl
 * Related Repository: ValidationConstantsRepository
 * Related Entity    : ValidationConstants
 * Related DTO       : N/A
 * Related Mapper    : ValidationConstantsMapper
 * Related DB Table  : validation_constantss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Common Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Common Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.common.validation;

public final class ValidationConstants {

    public static final int NAME_MAX_LENGTH = 100;
    public static final int CODE_MAX_LENGTH = 50;
    public static final int EMAIL_MAX_LENGTH = 150;
    public static final int PHONE_MAX_LENGTH = 30;

    private ValidationConstants() {}
}
