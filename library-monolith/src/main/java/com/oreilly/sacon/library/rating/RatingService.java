package com.oreilly.sacon.library.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public ItemRating getRating(long itemId) {
        return ratingRepository.findOne(itemId);
    }

}
