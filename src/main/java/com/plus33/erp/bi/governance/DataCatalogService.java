package com.plus33.erp.bi.governance;

import com.plus33.erp.bi.entity.BiCatalogDataset;
import com.plus33.erp.bi.entity.BiCatalogGlossary;
import com.plus33.erp.bi.repository.BiCatalogDatasetRepository;
import com.plus33.erp.bi.repository.BiCatalogGlossaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DataCatalogService {

    @Autowired BiCatalogDatasetRepository datasetRepo;
    @Autowired BiCatalogGlossaryRepository glossaryRepo;

    @Transactional
    public BiCatalogDataset registerDataset(String name, String desc, String ownerRole, String stewardUser) {
        BiCatalogDataset ds = datasetRepo.findAll().stream()
                .filter(d -> d.getDatasetName().equalsIgnoreCase(name))
                .findFirst().orElse(null);

        if (ds == null) {
            ds = new BiCatalogDataset();
            ds.setDatasetName(name);
            ds.setCreatedAt(LocalDateTime.now());
        }
        ds.setDescription(desc);
        ds.setOwnerRole(ownerRole);
        ds.setStewardUser(stewardUser);
        return datasetRepo.save(ds);
    }

    @Transactional
    public BiCatalogDataset certifyDataset(Long id, String status) {
        BiCatalogDataset ds = datasetRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found: " + id));
        ds.setCertificationStatus(status);
        ds.setLastCertifiedAt(LocalDateTime.now());
        return datasetRepo.save(ds);
    }

    @Transactional
    public BiCatalogGlossary createGlossaryTerm(String code, String name, String definition, String rule, String domain) {
        BiCatalogGlossary term = glossaryRepo.findAll().stream()
                .filter(t -> t.getTermCode().equalsIgnoreCase(code))
                .findFirst().orElse(null);

        if (term == null) {
            term = new BiCatalogGlossary();
            term.setTermCode(code);
            term.setCreatedAt(LocalDateTime.now());
        }
        term.setTermName(name);
        term.setDefinition(definition);
        term.setCalculationRule(rule);
        term.setDomainArea(domain);
        return glossaryRepo.save(term);
    }
}