package com.plus33.erp.workforce.dto;

import jakarta.validation.constraints.NotBlank;

public record AnnouncementRequest(
    @NotBlank String title,
    @NotBlank String content,
    @NotBlank String priority,
    String imageUrl
) {}
