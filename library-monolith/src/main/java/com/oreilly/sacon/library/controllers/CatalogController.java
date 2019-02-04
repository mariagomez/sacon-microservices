package com.oreilly.sacon.library.controllers;

import com.oreilly.sacon.library.availability.Availability;
import com.oreilly.sacon.library.catalog.CatalogService;
import com.oreilly.sacon.library.catalog.Item;
import com.oreilly.sacon.library.models.BookWithAvailabilityAndRating;
import com.oreilly.sacon.library.rating.ItemRating;
import com.oreilly.sacon.library.rating.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private Availability availability;

    @Autowired
    private RatingService ratingService;

    @RequestMapping("/catalog")
    public String catalog(ModelMap model) {
        List<Item> books = catalogService.getAllBooks();
        List<BookWithAvailabilityAndRating> booksWithAvailability = addAvailability(books);
        model.addAttribute("books", booksWithAvailability);
        return "catalog";
    }

    private List<BookWithAvailabilityAndRating> addAvailability(List<Item> books) {
        List<BookWithAvailabilityAndRating> booksWithAvailability = StreamSupport.stream(books.spliterator(), false)
                .map(book -> {
                    ItemRating rating = ratingService.getRating(book.getId());
                    return new BookWithAvailabilityAndRating(
                            book.getId(),
                            book.getName(),
                            book.getAuthor(),
                            book.getDescription(),
                            rating.getRating(),
                            book.getImagePath(),
                            availability.inStock(book.getId()));
                })
                .collect(Collectors.toList());
        return booksWithAvailability;
    }

    @RequestMapping(value = "/catalog/borrow", params={"bookId"})
    public String borrow(final HttpServletRequest request) {
        String parameter = request.getParameter("bookId");
        availability.borrow(Integer.valueOf(parameter).longValue());
        return "redirect:/catalog";
    }
}