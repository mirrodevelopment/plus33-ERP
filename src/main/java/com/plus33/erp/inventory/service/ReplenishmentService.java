/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.service
 * File              : ReplenishmentService.java
 * Purpose           : Service interface contract defining the API for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentController
 * Related Service   : ReplenishmentService, ReplenishmentServiceImpl
 * Related Repository: ReplenishmentRepository
 * Related Entity    : Replenishment
 * Related DTO       : PageResponse, ReplenishmentRuleRequest, ReplenishmentRuleResponse, ReplenishmentRuleUpdateRequest, ReplenishmentSuggestionResponse
 * Related Mapper    : ReplenishmentMapper
 * Related DB Table  : replenishments
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Inventory Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.ReplenishmentSuggestionStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ReplenishmentService {

    // Rules CRUD
    IdempotentCreateResult<ReplenishmentRuleResponse> createRule(ReplenishmentRuleRequest request);
    ReplenishmentRuleResponse updateRule(Long ruleId, ReplenishmentRuleUpdateRequest request);
    void deleteRule(Long ruleId);
    ReplenishmentRuleResponse getRule(Long ruleId);
    PageResponse<ReplenishmentRuleResponse> listRules(Long companyId, Long warehouseId, Long storeId, Long productId, Boolean active, Pageable pageable);

    // Evaluation
    List<ReplenishmentSuggestionResponse> evaluateAll(Long companyId);
    ReplenishmentSuggestionResponse evaluateRule(Long ruleId);

    // Suggestion lifecycle
    ReplenishmentSuggestionResponse acknowledgeSuggestion(Long suggestionId);
    ReplenishmentSuggestionResponse cancelSuggestion(Long suggestionId, String notes);
    ReplenishmentSuggestionResponse createPurchaseRequestFromSuggestion(Long suggestionId);
    ReplenishmentSuggestionResponse createTransferFromSuggestion(Long suggestionId);
    PageResponse<ReplenishmentSuggestionResponse> listSuggestions(Long companyId, ReplenishmentSuggestionStatus status, Long productId, Pageable pageable);
    ReplenishmentSuggestionResponse getSuggestion(Long suggestionId);
}
