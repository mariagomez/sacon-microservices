package com.oreilly.sacon.library.library.availability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AvailabilityClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${availability.url}")
    private String availabilityUrl;

    public boolean inStock(long bookId) {
        Book book = restTemplate.getForObject(availabilityUrl + "availability/" + bookId, Book.class);
        return book.inStock();
    }

    public void borrow(long bookId) {
        HttpEntity<Long> requestUpdate = new HttpEntity<>(bookId);
        restTemplate.exchange(availabilityUrl + "borrow/", HttpMethod.PUT, requestUpdate, Void.class);
    }

}