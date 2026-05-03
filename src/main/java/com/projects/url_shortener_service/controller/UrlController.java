package com.projects.url_shortener_service.controller;

import com.projects.url_shortener_service.dto.ShortenUrlRequest;
import com.projects.url_shortener_service.dto.ShortenUrlResponse;
import com.projects.url_shortener_service.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

        String originalUrl = urlShortenerService.getOriginalUrlWithIncrementalCLicks(shortCode);

        // It uses the ResponseEntity's fluent builder pattern for a more concise and readable result.
        //
        // 1. ResponseEntity.status(HttpStatus.FOUND): This static method starts the build process.
        //    It sets the HTTP status code to 302 Found and returns a builder object.
        //
        // 2. .location(URI.create(originalUrl)): This method is called on the builder object.
        //    It sets the 'Location' header to the provided URI. It also returns the same
        //    builder object, allowing for method chaining.
        //
        // 3. .build(): This final method completes the process. It constructs the immutable
        //    ResponseEntity<Void> object from the configured builder. Since we never called
        //    the .body() method, the response has an empty body, which is correct for a redirect.
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    }

    /**
     * This endpoint retrieves statistics for a specific short URL.
     *
     * @GetMapping("/api/v1/url/stats/{shortCode}"): Maps HTTP GET requests to this method.
     *   - The path is namespaced under our API standard /api/v1/url.
     *   - 'stats' clearly indicates the purpose of the endpoint.
     *   - {shortCode} is a path variable to specify which URL's stats to fetch.
     *
     * @param shortCode The short code captured from the URL path via @PathVariable.
     * @return A ResponseEntity containing the statistics in a UrlStatsResponse DTO.
     *         The actual implementation will be built in the following tasks.
     */
    @GetMapping("/api/v1/url/stats/{shortCode}")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode) {
        // In the next tasks, we will:
        // 1. Create the UrlStatsResponse DTO.
        // 2. Add a new method to the UrlShortenerService to fetch the stats.
        // 3. Call that service method here and return its result.

        // For now, returning null is a placeholder for the logic to come.
        return null;
    }
}
