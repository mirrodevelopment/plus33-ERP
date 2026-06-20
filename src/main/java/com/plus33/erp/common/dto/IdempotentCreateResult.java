package com.plus33.erp.common.dto;

/**
 * Wraps the result of an idempotent create operation.
 *
 * @param data    The response data (always non-null).
 * @param created {@code true} if the resource was newly created, {@code false} if it was a repeat
 *                submission for an already-existing resource (idempotent replay).
 */
public record IdempotentCreateResult<T>(T data, boolean created) {}
