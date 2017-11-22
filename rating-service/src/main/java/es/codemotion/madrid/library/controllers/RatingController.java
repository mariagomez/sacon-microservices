package es.codemotion.madrid.library.controllers;


import es.codemotion.madrid.library.rating.ItemRating;
import es.codemotion.madrid.library.rating.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @RequestMapping(path = "/rating/{id}", method = GET, produces = "application/json; charset=UTF-8")
    public ItemRating rating(@PathVariable long id) {
        ItemRating rating = ratingService.getRating(id);
        return rating;
    }

}