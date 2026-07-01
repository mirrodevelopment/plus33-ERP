package com.plus33.erp.bi.controller;

import com.plus33.erp.bi.entity.BiCatalogDataset;
import com.plus33.erp.bi.entity.BiCatalogGlossary;
import com.plus33.erp.bi.governance.DataCatalogService;
import com.plus33.erp.bi.governance.DataMaskingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bi/governance")
public class BiGovernanceController {

    @Autowired DataMaskingService maskingService;
    @Autowired DataCatalogService catalogService;

    @GetMapping("/mask")
    public String maskField(@RequestParam String table, @RequestParam String column, @RequestParam String val) {
        return maskingService.maskValue(table, column, val);
    }

    @PostMapping("/dataset/register")
    public BiCatalogDataset registerDataset(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String ownerRole,
            @RequestParam(required = false) String stewardUser) {
        return catalogService.registerDataset(name, description, ownerRole, stewardUser);
    }

    @PostMapping("/dataset/certify")
    public BiCatalogDataset certifyDataset(@RequestParam Long id, @RequestParam String status) {
        return catalogService.certifyDataset(id, status);
    }

    @PostMapping("/glossary/term")
    public BiCatalogGlossary createGlossaryTerm(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String definition,
            @RequestParam(required = false) String rule,
            @RequestParam String domain) {
        return catalogService.createGlossaryTerm(code, name, definition, rule, domain);
    }
}