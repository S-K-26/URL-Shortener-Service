package com.projects.url_shortener_service.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public record ShortenUrlRequest(@NotEmpty(message = "URL must not be empty")
                                @URL(message = "Valid URL must be provided")
                                String url) {

}
