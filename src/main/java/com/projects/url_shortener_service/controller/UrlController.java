package com.projects.url_shortener_service.controller;

import com.projects.url_shortener_service.service.UrlShortenerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class serves as the REST Controller for all URL-related operations.
 *
 * @RestController: This is a specialized version of the @Controller annotation.
 * It marks this class as a request handler and also adds the @ResponseBody
 * annotation to every handler method. @ResponseBody tells Spring to serialize
 * the return object into a JSON response body, which is exactly what we need
 * for a RESTful API.
 *
 * @RequestMapping("/api/v1/url"): This annotation, when used at the class level,
 * maps a base URL path to this controller. All method-level mappings in this
 * class will be relative to "/api/v1/url". This is a best practice for
 * versioning and organizing your API endpoints.
 */
@RestController
@RequestMapping("/api/v1/url")
public class UrlController {

    private UrlShortenerService urlShortenerService;

    public UrlController (UrlShortenerService urlShortenerService){
        this.urlShortenerService = urlShortenerService;
    }

}
