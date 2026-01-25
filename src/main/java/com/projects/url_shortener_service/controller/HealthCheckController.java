
package com.projects.url_shortener_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * A REST Controller is a core component in Spring for building web services.
 * The @RestController annotation combines two other annotations: @Controller and @ResponseBody.
 *
 * @Controller: Marks this class as a Spring MVC controller, a component that handles web requests.
 * @ResponseBody: Indicates that the return value of the methods in this controller
 *                should be written directly to the HTTP response body, rather than being
 *                interpreted as a view name. This is perfect for APIs that return data (like JSON or text).
 *
 * This controller will handle basic health checks for the service.
 */
@RestController
public class HealthCheckController {

    /**
     * This method handles GET requests to the /health endpoint.
     * It returns a simple string message indicating that the service is operational.
     * @GetMapping: Maps HTTP GET requests to this method to specific URL path
     *
     * When client (like browser) sends a GET request to /health, Springs DispatcherServlet routes the
     * request to this method.
     * The method executes and returns the string "Service is up and running!".
     * This string is then written directly to the HTTP response body because of the @RestController
     *
     * Spring automatically takes this return value and writes it as the response body of the HTTP response.
     * with content type text/plain.
     * @return A simple string message indicating the service status.
     * */
    @GetMapping("/health")
    public String isHealthy() {
        return "Service is up and running!";
    }
}
