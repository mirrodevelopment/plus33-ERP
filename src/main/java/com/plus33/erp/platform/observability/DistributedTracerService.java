package com.plus33.erp.platform.observability;

import com.plus33.erp.platform.entity.PlatformTraceSpan;
import com.plus33.erp.platform.repository.PlatformTraceSpanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DistributedTracerService {
    @Autowired PlatformTraceSpanRepository spanRepo;

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