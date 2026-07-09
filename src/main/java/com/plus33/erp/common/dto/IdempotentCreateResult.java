/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * Package           : com.plus33.erp.common.dto
 * File              : IdempotentCreateResult.java
 * Purpose           : Component of Common Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IdempotentCreateResultController
 * Related Service   : IdempotentCreateResultService, IdempotentCreateResultServiceImpl
 * Related Repository: IdempotentCreateResultRepository
 * Related Entity    : IdempotentCreateResult
 * Related DTO       : N/A
 * Related Mapper    : IdempotentCreateResultMapper
 * Related DB Table  : idempotent_create_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Common Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Common Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.common.dto;

/**
 * Wraps the result of an idempotent create operation.
 *
 * @param data    The response data (always non-null).
 * @param created {@code true} if the resource was newly created, {@code false} if it was a repeat
 *                submission for an already-existing resource (idempotent replay).
 */
public record IdempotentCreateResult<T>(T data, boolean created) {}
