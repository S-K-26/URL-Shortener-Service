package com.projects.url_shortener_service.dto;

import java.time.LocalDateTime;

public record UrlStatsResponse(
    String originalUrl,
    String shortUrl,
    LocalDateTime creationDate,
    long clickCount
) {
}