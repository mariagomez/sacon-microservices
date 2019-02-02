package com.oreilly.sacon.library.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RatingServiceClient {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${rating.url}")
    private String ratingUrl;

    public ItemRating getRating(long itemId) {
        return restTemplate.getForObject(ratingUrl + itemId, ItemRating.class);
    }

}