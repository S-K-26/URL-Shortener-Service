package com.projects.url_shortener_service.service;

import com.projects.url_shortener_service.model.UrlMapping;
import com.projects.url_shortener_service.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
}
