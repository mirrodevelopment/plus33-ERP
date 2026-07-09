/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * Package           : com.plus33.erp.common.mapper
 * File              : GlobalMapperConfig.java
 * Purpose           : Service interface contract defining the API for Common Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GlobalMapperConfigController
 * Related Service   : GlobalMapperConfigService, GlobalMapperConfigServiceImpl
 * Related Repository: GlobalMapperConfigRepository
 * Related Entity    : GlobalMapperConfig
 * Related DTO       : N/A
 * Related Mapper    : GlobalMapperConfigMapper
 * Related DB Table  : global_mapper_configs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Common Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Common Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.common.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
/**
 * <b>PLUS33 Coffee ERP -- Common Module</b>
 *
 * <p><b>Class  :</b> {@code GlobalMapperConfig}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.common.mapper}</p>
 * <p><b>Layer  :</b> Component of Common Module in the PLUS33 Coffee ERP platform.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface GlobalMapperConfig {
}
