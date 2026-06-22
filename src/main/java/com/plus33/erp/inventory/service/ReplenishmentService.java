package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.ReplenishmentSuggestionStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
