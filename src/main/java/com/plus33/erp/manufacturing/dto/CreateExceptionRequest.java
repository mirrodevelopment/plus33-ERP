package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class CreateExceptionRequest {
    @NotNull private LocalDate exceptionDate;
    @NotBlank private String exceptionType; // HOLIDAY, MAINTENANCE_WINDOW, OVERTIME, SHUTDOWN
    @NotNull private BigDecimal availableHours;
    private String description;

    public CreateExceptionRequest() {}

    public LocalDate getExceptionDate() { return exceptionDate; }
    public void setExceptionDate(LocalDate exceptionDate) { this.exceptionDate = exceptionDate; }
    public String getExceptionType() { return exceptionType; }
    public void setExceptionType(String exceptionType) { this.exceptionType = exceptionType; }
    public BigDecimal getAvailableHours() { return availableHours; }
    public void setAvailableHours(BigDecimal availableHours) { this.availableHours = availableHours; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
