package com.projects.url_shortener_service.service;

import com.projects.url_shortener_service.dto.UrlStatsResponse;
import com.projects.url_shortener_service.exception.UrlNotFoundException;
import com.projects.url_shortener_service.model.UrlMapping;
import com.projects.url_shortener_service.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlShortenerService {

    private final UrlMappingRepository urlMappingRepository;

    // A constant holding all the characters for our base-62 encoding.
    // It is 'static' and 'final' because it's a constant value that never changes
    // and is shared across all instances of this service.
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * This is the constructor for our service. This is where Dependency Injection happens.
     * When Spring creates the UrlShortenerService bean, it will see this constructor
     * and look for a bean of type UrlMappingRepository in its context.
     * Since Spring Data JPA automatically created an implementation for our repository
     * interface, Spring finds it and "injects" or passes it into this constructor.
     * @param urlMappingRepository The instance of UrlMappingRepository provided by Spring.
     */
    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    // By default, repository methods are transactional. However, our method orchestrates
    // multiple database operations. Wrapping it in @Transactional ensures that these
    // operations are executed as a single, atomic unit. If any part fails, all
    // previous operations in the method are rolled back.
    @Transactional
    public String shortenUrl(String originalUrl) {

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setCreationDate(LocalDateTime.now());

        UrlMapping savedEntity = urlMappingRepository.save(urlMapping);

        String shortCode = encodeBase62(savedEntity.getId());

        savedEntity.setShortCode(shortCode);

        urlMappingRepository.save(savedEntity);

        return shortCode;
    }

    @Transactional
    public String getOriginalUrlWithIncrementalCLicks(String shortCode) {

        // Here, we use the custom query method we defined in our repository.
        // Spring Data JPA implements this method for us based on its name.
        // It executes a query to find a UrlMapping entity where the 'shortCode' column
        // matches the value passed to the method.
        // The result is wrapped in an Optional, which is a container that may or may not
        // hold a value. This is a robust way to handle cases where the short code might not exist.
        Optional<UrlMapping> urlMappingOptional = Optional.ofNullable(urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortCode)));

        if(urlMappingOptional.isPresent()){
            UrlMapping urlMapping = urlMappingOptional.get();

            // Increment the click count. We get the current count, add 1, and set it back.
            // These getter/setter methods were automatically generated for us by Lombok.
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);

            // Save the updated entity. Because the 'urlMapping' object was retrieved
            // from the database, it has a non-null ID. Spring Data JPA is smart enough
            // to know that calling save() on an entity with an existing ID should
            // result in an UPDATE statement, not a new INSERT.
            urlMappingRepository.save(urlMapping);

            // After successfully updating the click count, we return the original URL,
            // which is the data the controller needs to perform the redirect.
            return urlMapping.getOriginalUrl();
        }

        return urlMappingOptional.toString();
    }

    private String encodeBase62(Long id) {
        // If no is 0 we return the first character of our character set
        if (id == 0){
            return String.valueOf(BASE62_CHARS.charAt(0));
        }

        StringBuilder stringBuilder = new StringBuilder();
        long num = id;// using mutable copy of our id for calculations.

        while (num >0){

            int remainder = (int)(num % 62);

            stringBuilder.append(BASE62_CHARS.charAt(remainder));

            num /= 62;

        }
        return stringBuilder.reverse().toString();
    }

    /**
     * Retrieves statistics for a given short code.
     * This is a read-only operation and doesn't need to be @Transactional by itself,
     * but adding it is harmless and keeps it consistent with other data-access methods.
     *
     * @param shortCode The unique code to look up.
     * @return A UrlStatsResponse DTO containing the statistics.
     * @throws UrlNotFoundException if the short code does not exist.
     */
    public UrlStatsResponse getStats(String shortCode) {
        // Step 1: Find the entity. We reuse our repository's custom find method.
        // Step 2: Validate. We reuse the .orElseThrow() pattern with our existing
        // custom exception. This ensures our API's error handling is consistent.
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("No statistics found for short code: " + shortCode));

        // Step 3: Transform (Map) the entity to the DTO.
        // We construct the full URL here for user convenience, as the DTO contract requires it.
        String fullShortUrl = "http://localhost:8080/" + urlMapping.getShortCode();

        // We create a new instance of our immutable UrlStatsResponse record,
        // populating it with data from the UrlMapping entity we just fetched.
        return new UrlStatsResponse(
                urlMapping.getOriginalUrl(),
                fullShortUrl,
                urlMapping.getCreationDate(),
                urlMapping.getClickCount()
        );
    }
}
