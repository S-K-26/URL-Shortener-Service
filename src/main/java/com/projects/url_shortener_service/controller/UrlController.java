package com.projects.url_shortener_service.controller;

import com.projects.url_shortener_service.dto.ShortenUrlRequest;
import com.projects.url_shortener_service.dto.ShortenUrlResponse;
import com.projects.url_shortener_service.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class serves as the REST Controller for all URL-related operations.
 *
 * @RestController: This is a specialized version of the @Controller annotation.
 * It marks this class as a request handler and also adds the @ResponseBody
 * annotation to every handler method. @ResponseBody tells Spring to serialize
 * the return object into a JSON response body, which is exactly what we need
 * for a RESTful API.
 */
@RestController
public class UrlController {

    private UrlShortenerService urlShortenerService;

    public UrlController (UrlShortenerService urlShortenerService){
        this.urlShortenerService = urlShortenerService;
    }

    /**
     * This method handles the creation of a new short URL.
     *
     * @PostMapping("/shorten"): Maps HTTP POST requests sent to /api/v1/url/shorten to this method.
     * @param request The incoming request body, which Spring automatically deserializes from JSON
     *                into our ShortenUrlRequest DTO.
     * @Valid: This annotation triggers the validation rules we defined in the ShortenUrlRequest
     *         record (e.g., @NotEmpty, @URL). If validation fails, Spring automatically
     *         returns a 400 Bad Request error before our method is even called.
     * @return A ResponseEntity containing the ShortenUrlResponse DTO and an HTTP status of 201 Created.
     */
    @PostMapping("/api/v1/url/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        String shortCode = urlShortenerService.shortenUrl(request.url());

        String fullShortUrl = "http://localhost:8080/" + shortCode;
        ShortenUrlResponse response = new ShortenUrlResponse(fullShortUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        // For now, this method is a placeholder. In the upcoming tasks, we will add the
        // logic to call the service, find the original URL, and build the
        // redirect response.
        return null;
    }
}
