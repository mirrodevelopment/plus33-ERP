/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : ReplenishmentSuggestionCancelRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentSuggestionCancelController
 * Related Service   : ReplenishmentSuggestionCancelService, ReplenishmentSuggestionCancelServiceImpl
 * Related Repository: ReplenishmentSuggestionCancelRepository
 * Related Entity    : ReplenishmentSuggestionCancel
 * Related DTO       : ReplenishmentSuggestionCancelRequest
 * Related Mapper    : ReplenishmentSuggestionCancelMapper
 * Related DB Table  : replenishment_suggestion_cancels
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReplenishmentSuggestionCancelController, ReplenishmentSuggestionCancelService, ReplenishmentSuggestionCancelServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import jakarta.validation.constraints.NotBlank;

public record ReplenishmentSuggestionCancelRequest(
        @NotBlank(message = "Notes are required for cancellation")
        String notes
) {}
