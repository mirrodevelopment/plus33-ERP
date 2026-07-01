package com.plus33.erp.bi.contract;

import com.plus33.erp.bi.entity.BiSchemaEvolutionHistory;
import com.plus33.erp.bi.repository.BiSchemaEvolutionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class SchemaEvolutionTracker {

    @Autowired BiSchemaEvolutionHistoryRepository historyRepo;

    @Transactional
    public BiSchemaEvolutionHistory logDrift(String tableName, String actionType, String detail, String status) {
        BiSchemaEvolutionHistory h = new BiSchemaEvolutionHistory();
        h.setTableName(tableName);
        h.setActionType(actionType);
        h.setActionDetail(detail);
        h.setValidationStatus(status);
        h.setDetectedAt(LocalDateTime.now());
        return historyRepo.save(h);
    }
}