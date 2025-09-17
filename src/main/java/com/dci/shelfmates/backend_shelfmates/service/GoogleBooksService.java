package com.dci.shelfmates.backend_shelfmates.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class GoogleBooksService {

    @Value("${google.books.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> searchBooks(String query) {
        String url = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/books/v1/volumes")
                .queryParam("q", query)
                .queryParam("key", apiKey)
                .queryParam("maxResults", 10)
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }
}