package com.plus33.erp.agent.vector;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VectorStoreCoordinator {
    @Autowired PlatformVectorStoreRepository storeRepo;
    @Autowired PlatformEmbeddingProviderRepository providerRepo;

    @Transactional
    public void registerStore(String code, String status) {
        PlatformVectorStore vs = new PlatformVectorStore();
        vs.setStoreCode(code);
        vs.setStatus(status);
        storeRepo.save(vs);
    }

    @Transactional
    public void registerEmbeddingProvider(String provider, int dimensions) {
        PlatformEmbeddingProvider ep = new PlatformEmbeddingProvider();
        ep.setProviderName(provider);
        ep.setDimensions(dimensions);
        providerRepo.save(ep);
    }
}