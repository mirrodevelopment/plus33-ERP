/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.observability
 * File              : DistributedTracerService.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DistributedTracerController
 * Related Service   : DistributedTracerService
 * Related Repository: DistributedTracerRepository
 * Related Entity    : DistributedTracer
 * Related DTO       : N/A
 * Related Mapper    : DistributedTracerMapper
 * Related DB Table  : distributed_tracers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DistributedTracerController, DistributedTracerServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements DistributedTracerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.observability;

import com.plus33.erp.platform.entity.PlatformTraceSpan;
import com.plus33.erp.platform.repository.PlatformTraceSpanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code DistributedTracerService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.observability}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DistributedTracerController
 *   --> DistributedTracerService (this)
 *   --> Validate business rules
 *   --> DistributedTracerRepository (read/write 'distributed_tracers')
 *   --> DistributedTracerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code distributed_tracers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DistributedTracerService {
    @Autowired PlatformTraceSpanRepository spanRepo;
    /**
     * Performs the startSpan operation in this module.
     *
     * @param traceId the traceId input value
     * @param spanId the spanId input value
     * @param parentSpanId the parentSpanId input value
     * @param operation the operation input value
     * @param duration the duration input value
     */
    @Transactional
    public void startSpan(String traceId, String spanId, String parentSpanId, String operation, long duration) {
        PlatformTraceSpan span = new PlatformTraceSpan();
        span.setTraceId(traceId);
        span.setSpanId(spanId);
        span.setParentSpanId(parentSpanId);
        span.setOperationName(operation);
        span.setDurationMs(duration);
        span.setTimestamp(LocalDateTime.now());
        spanRepo.save(span);
    }
}