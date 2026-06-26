package com.plus33.erp.finance.assets.dto;

import java.time.LocalDate;

public record DepreciationRunRequest(
    LocalDate depreciationDate,
    Boolean dryRun
) {}
