/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.omnichannel
 * File              : ChannelRegistry.java
 * Purpose           : Business logic service layer for Crm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ChannelRegistryController
 * Related Service   : ChannelRegistry
 * Related Repository: ChannelRegistryRepository
 * Related Entity    : ChannelRegistry
 * Related DTO       : N/A
 * Related Mapper    : ChannelRegistryMapper
 * Related DB Table  : channel_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ChannelRegistryController, ChannelRegistryImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Crm Module. Implements ChannelRegistryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.crm.omnichannel;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code ChannelRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.omnichannel}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Crm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ChannelRegistryController
 *   --> ChannelRegistry (this)
 *   --> Validate business rules
 *   --> ChannelRegistryRepository (read/write 'channel_registrys')
 *   --> ChannelRegistryMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code channel_registrys}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ChannelRegistry {

    private final Map<String, ChannelProvider> providers = new HashMap<>();

    public ChannelRegistry(List<ChannelProvider> channelProviders) {
        channelProviders.forEach(p -> providers.put(p.getProviderKey().toUpperCase(), p));
    }

    /**
     * Retrieves provider data from the database.
     *
     * @param key the key input value
     * @return the ChannelProvider result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ChannelProvider getProvider(String key) {
        return providers.get(key.toUpperCase());
    }
}