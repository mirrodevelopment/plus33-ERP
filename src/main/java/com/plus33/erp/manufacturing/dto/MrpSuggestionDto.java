/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.dto
 * File              : MrpSuggestionDto.java
 * Purpose           : Data Transfer Object for request/response in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MrpSuggestionDtoController
 * Related Service   : MrpSuggestionDtoService, MrpSuggestionDtoServiceImpl
 * Related Repository: MrpSuggestionDtoRepository
 * Related Entity    : MrpSuggestionDto
 * Related DTO       : MrpPlannedOrderDto, MrpSuggestionDto
 * Related Mapper    : MrpSuggestionDtoMapper
 * Related DB Table  : mrp_suggestion_dtos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MrpSuggestionDtoController, MrpSuggestionDtoService, MrpSuggestionDtoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Manufacturing Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.dto;

public class MrpSuggestionDto extends MrpPlannedOrderDto {
    public MrpSuggestionDto() {
        super();
    }
}
