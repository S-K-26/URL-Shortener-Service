package com.projects.url_shortener_service.exception;


public record ErrorResponseDto(int status, String errormessage , String message) {
}
