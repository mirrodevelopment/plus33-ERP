package com.plus33.erp.finance.tax.compliance;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ComplianceSubmissionResult {
    boolean success;
    String status; // ACCEPTED, REJECTED, WARNINGS
    String errorDetails;
    String governmentUuid;
}
