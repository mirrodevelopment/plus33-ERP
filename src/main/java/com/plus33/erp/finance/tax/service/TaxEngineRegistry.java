package com.plus33.erp.finance.tax.service;

public interface TaxEngineRegistry {
    TaxEngineProvider resolve(String taxCategoryCode);
    void register(String taxCategoryCode, TaxEngineProvider provider);
}
