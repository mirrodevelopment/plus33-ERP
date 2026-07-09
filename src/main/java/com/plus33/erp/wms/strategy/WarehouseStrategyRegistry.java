/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.strategy
 * File              : WarehouseStrategyRegistry.java
 * Purpose           : Component of Wms Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseStrategyRegistryController
 * Related Service   : WarehouseStrategyRegistryService, WarehouseStrategyRegistryServiceImpl
 * Related Repository: WarehouseStrategyRegistryRepository
 * Related Entity    : WarehouseStrategyRegistry
 * Related DTO       : N/A
 * Related Mapper    : WarehouseStrategyRegistryMapper
 * Related DB Table  : warehouse_strategy_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Wms Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.wms.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Central O(1) strategy resolver for all WMS put-away and picking strategies.
 *
 * <p>At application startup, Spring auto-discovers all beans implementing
 * {@link PutAwayStrategy} and {@link PickingStrategy} and injects them here.
 * Strategies are keyed by their {@code strategyKey()} return value.</p>
 *
 * <p>To add a new strategy, simply implement the interface, annotate with
 * {@code @Component}, and it will be available in the registry without
 * any code change here.</p>
 */
@Component
public class WarehouseStrategyRegistry {

    private final Map<String, PutAwayStrategy> putAwayStrategies;
    private final Map<String, PickingStrategy> pickingStrategies;

    public WarehouseStrategyRegistry(List<PutAwayStrategy> putAwayStrategyList,
                                     List<PickingStrategy> pickingStrategyList) {
        this.putAwayStrategies = putAwayStrategyList.stream()
                .collect(Collectors.toMap(PutAwayStrategy::strategyKey, Function.identity()));
        this.pickingStrategies = pickingStrategyList.stream()
                .collect(Collectors.toMap(PickingStrategy::strategyKey, Function.identity()));
    }

    /**
     * Resolves a put-away strategy by key.
     *
     * @param key  e.g. "NEAREST_EMPTY_BIN", "CAPACITY_BASED", "FIXED_BIN"
     * @return     the strategy implementation
     * @throws IllegalArgumentException if the key is not registered
     */
    public PutAwayStrategy putAway(String key) {
        return Optional.ofNullable(putAwayStrategies.get(key))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown put-away strategy: " + key +
                        ". Available: " + putAwayStrategies.keySet()));
    }

    /**
     * Resolves a picking strategy by key.
     *
     * @param key  e.g. "FEFO", "FIFO", "LIFO", "BATCH", "CLUSTER", "ZONE"
     * @return     the strategy implementation
     * @throws IllegalArgumentException if the key is not registered
     */
    public PickingStrategy picking(String key) {
        return Optional.ofNullable(pickingStrategies.get(key))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown picking strategy: " + key +
                        ". Available: " + pickingStrategies.keySet()));
    }

    /**
     * Resolves a picking strategy by key, returning a default if not found.
     *
     * @param key          the requested strategy key
     * @param defaultKey   fallback strategy key (e.g. "FEFO")
     */
    public PickingStrategy pickingWithFallback(String key, String defaultKey) {
        return Optional.ofNullable(pickingStrategies.get(key))
                .orElse(pickingStrategies.getOrDefault(defaultKey,
                        pickingStrategies.values().iterator().next()));
    }

    /** Returns all registered put-away strategy keys. */
    public java.util.Set<String> availablePutAwayStrategies() {
        return putAwayStrategies.keySet();
    }

    /** Returns all registered picking strategy keys. */
    public java.util.Set<String> availablePickingStrategies() {
        return pickingStrategies.keySet();
    }
}
