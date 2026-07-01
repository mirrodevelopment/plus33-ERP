package com.plus33.erp.bi.contract;

import com.plus33.erp.bi.entity.BiDataContract;
import com.plus33.erp.bi.entity.BiSchemaEvolutionHistory;
import com.plus33.erp.bi.repository.BiDataContractRepository;
import com.plus33.erp.bi.repository.BiSchemaEvolutionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataContractValidator {

    @Autowired BiDataContractRepository contractRepo;
    @Autowired BiSchemaEvolutionHistoryRepository evolutionRepo;

    @Transactional
    public boolean validateContract(String contractName, List<ColumnMetadata> actualColumns) {
        BiDataContract contract = contractRepo.findAll().stream()
                .filter(c -> c.getContractName().equalsIgnoreCase(contractName) && "ACTIVE".equalsIgnoreCase(c.getStatus()))
                .findFirst().orElse(null);

        if (contract == null) {
            return true;
        }

        Map<String, String> expectedColumns = new HashMap<>();
        String[] parts = contract.getSchemaDefinition().split(",");
        for (String p : parts) {
            String[] kv = p.split(":");
            if (kv.length == 2) {
                expectedColumns.put(kv[0].trim().toLowerCase(), kv[1].trim().toLowerCase());
            }
        }

        Map<String, String> actualColumnsMap = new HashMap<>();
        for (ColumnMetadata col : actualColumns) {
            actualColumnsMap.put(col.getName().toLowerCase(), col.getType().toLowerCase());
        }

        List<BiSchemaEvolutionHistory> changes = new ArrayList<>();
        String compat = contract.getCompatibilityLevel().toUpperCase();

        for (String expectedName : expectedColumns.keySet()) {
            if (!actualColumnsMap.containsKey(expectedName)) {
                if ("FULL".equals(compat) || "BACKWARD".equals(compat)) {
                    BiSchemaEvolutionHistory evo = new BiSchemaEvolutionHistory();
                    evo.setTableName(contractName);
                    evo.setActionType("COLUMN_REMOVED");
                    evo.setActionDetail("Removed column: " + expectedName + " violates compatibility mode: " + compat);
                    evo.setValidationStatus("REJECTED");
                    evo.setDetectedAt(LocalDateTime.now());
                    evolutionRepo.save(evo);
                    changes.add(evo);
                }
            }
        }

        for (String actualName : actualColumnsMap.keySet()) {
            if (!expectedColumns.containsKey(actualName)) {
                if ("FULL".equals(compat) || "FORWARD".equals(compat)) {
                    BiSchemaEvolutionHistory evo = new BiSchemaEvolutionHistory();
                    evo.setTableName(contractName);
                    evo.setActionType("COLUMN_ADDED");
                    evo.setActionDetail("Added column: " + actualName + " violates compatibility mode: " + compat);
                    evo.setValidationStatus("REJECTED");
                    evo.setDetectedAt(LocalDateTime.now());
                    evolutionRepo.save(evo);
                    changes.add(evo);
                } else {
                    BiSchemaEvolutionHistory evo = new BiSchemaEvolutionHistory();
                    evo.setTableName(contractName);
                    evo.setActionType("COLUMN_ADDED");
                    evo.setActionDetail("Added column: " + actualName + " is allowed in compatibility mode: " + compat);
                    evo.setValidationStatus("ACCEPTED");
                    evo.setDetectedAt(LocalDateTime.now());
                    evolutionRepo.save(evo);
                }
            }
        }

        for (String colName : expectedColumns.keySet()) {
            if (actualColumnsMap.containsKey(colName)) {
                String expectedType = expectedColumns.get(colName);
                String actualType = actualColumnsMap.get(colName);
                if (!expectedType.equalsIgnoreCase(actualType)) {
                    BiSchemaEvolutionHistory evo = new BiSchemaEvolutionHistory();
                    evo.setTableName(contractName);
                    evo.setActionType("TYPE_CHANGED");
                    evo.setActionDetail("Type changed for " + colName + " from " + expectedType + " to " + actualType);
                    evo.setValidationStatus("REJECTED");
                    evo.setDetectedAt(LocalDateTime.now());
                    evolutionRepo.save(evo);
                    changes.add(evo);
                }
            }
        }

        return changes.isEmpty();
    }

    public static class ColumnMetadata {
        private String name;
        private String type;

        public ColumnMetadata(String name, String type) {
            this.name = name;
            this.type = type;
        }
        public String getName() { return name; }
        public String getType() { return type; }
    }
}