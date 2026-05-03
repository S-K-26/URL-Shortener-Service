package com.projects.url_shortener_service.controller;

import com.projects.url_shortener_service.dto.UrlStatsResponse;
import com.projects.url_shortener_service.exception.UrlNotFoundException;
import com.projects.url_shortener_service.service.UrlShortenerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    // As you already have this from the previous step, we ensure our controller
    // has access to the service layer for when we need it.
    private final UrlShortenerService urlShortenerService;

    public PageController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    // --- METHOD SIGNATURE TO BE EXPLAINED ---

    /**
     * This method handles the form submission from our index.html page.
     * We are now defining its parameters to capture the submitted data.
     *
     * @param longUrl This parameter is bound to the form's input data.
     *                The @RequestParam("longUrl") annotation is the key. It tells Spring:
     *                "Find the value from the form submission associated with the key 'longUrl'
     *                (which matches our <input name="longUrl">) and assign it to this String variable."
     * @param model   The Model object is a map-like container provided by Spring. We will use
     *                it in the upcoming tasks to pass data (like the result of the shortening)
     *                from the controller back to the Thymeleaf view for rendering.
     * @return        The string "index", which tells Spring to re-render the index.html page.
     *                This way, the user stays on the same page to see the result.
     */
    @PostMapping("/shorten-web")
    public String handleShortenForm(@RequestParam("longUrl") String longUrl, Model model) {
        // This is the moment we connect the UI interaction to our core business logic.
        // 1. We call the shortenUrl method on our injected service instance.
        // 2. We pass the 'longUrl' variable, which contains the data submitted by the user.
        // 3. The service executes all the steps to create and persist the URL mapping.
        // 4. Upon successful completion, the service returns the unique short code (e.g., "aB1cDe").
        // 5. We store this returned code in a new local variable named 'shortCode'.
        String shortCode = urlShortenerService.shortenUrl(longUrl);

        // 1. Construct the full, clickable short URL.
        // The service provides the code, but the controller is responsible for knowing
        // the application's base URL and constructing the final link.
        String fullShortUrl = "http://localhost:8080/" + shortCode;

        // 2. Add the results to the Model object.
        // The Model acts as a container to pass data from the controller to the view.
        // Each attribute we add becomes a variable accessible within our Thymeleaf template.

        // We add the user's original submission for their reference.
        // In index.html, we can now access this value using ${originalUrl}.
        model.addAttribute("originalUrl", longUrl);

        // We add the final result.
        // In index.html, we can now access this value using ${shortUrlResult}.
        model.addAttribute("shortUrlResult", fullShortUrl);

        return "index";
    }

    /**
     * Handles the form submission for checking URL statistics.
     *
     * @PostMapping("/check-stats"): Maps POST requests from our new stats form to this method.
     * @param shortCode The @RequestParam("checkShortCode") annotation tells Spring to find the
     *                  form data with the key "checkShortCode" (matching our input's 'name' attribute)
     *                  and inject its value into this String parameter.
     * @param model     The Model object, which we will use in the next task to pass the
     *                  retrieved statistics back to the view for rendering.
     * @return The string "index", telling Spring to re-render the index.html page to display the results.
     */
    @PostMapping("/check-stats")
    public String handleStatsCheckForm(@RequestParam("checkShortCode") String shortCode, Model model) {
        try {
            // 1. Call the service to get the statistics. If successful, this returns our DTO.
            UrlStatsResponse stats = urlShortenerService.getStats(shortCode);
            // 2. Add the successfully retrieved stats object to the model.
            // We will use the key "urlStats" to reference this object in our HTML.
            model.addAttribute("urlStats", stats);
        } catch (UrlNotFoundException e) {
            // 3. If the service throws UrlNotFoundException, we catch it here.
            // This prevents the application from showing a generic error page.
            // Instead, we add a user-friendly error message to the model.
            // We will use the key "statsError" to check for this message in our HTML.
            model.addAttribute("statsError", "Statistics not found for short code: " + shortCode);
        }

        // --- HIGHLIGHTED CHANGE END ---

        // 4. Return "index" to re-render the page. The Thymeleaf template will now have
        // access to either the "urlStats" object or the "statsError" message.
        return "index";
    }
}