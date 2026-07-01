package com.plus33.erp.intelligence.causal;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CausalInferenceEngine {
    @Autowired PlatformCausalModelRepository modelRepo;

    @Transactional
    public PlatformCausalModel registerModel(String code, String name, String structure) {
        PlatformCausalModel model = new PlatformCausalModel();
        model.setModelCode(code);
        model.setModelName(name);
        model.setStructureJson(structure);
        return modelRepo.save(model);
    }
}