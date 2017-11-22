package es.codemotion.madrid.library.controllers;

import es.codemotion.madrid.library.borrow.BorrowService;
import es.codemotion.madrid.library.catalog.CatalogService;
import es.codemotion.madrid.library.catalog.Item;
import es.codemotion.madrid.library.borrow.ItemAvailability;
import es.codemotion.madrid.library.rating.ItemRating;
import es.codemotion.madrid.library.models.BookWithAvailabilityAndRating;
import es.codemotion.madrid.library.models.BorrowData;
import es.codemotion.madrid.library.rating.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private RatingService ratingService;

    @RequestMapping("/catalog")
    public String catalog(ModelMap model) {
        List<Item> books = catalogService.getAllBooks();
        List<BookWithAvailabilityAndRating> booksWithAvailability = addAvailability(books);
        model.addAttribute("books", booksWithAvailability);
        model.addAttribute("data", new BorrowData());
        return "catalog";
    }

    private List<BookWithAvailabilityAndRating> addAvailability(List<Item> books) {
        List<BookWithAvailabilityAndRating> booksWithAvailability = StreamSupport.stream(books.spliterator(), false)
                .map(book -> {
                    ItemAvailability availability = borrowService.getAvailability(book.getId());
                    ItemRating rating = ratingService.getRating(book.getId());
                    return new BookWithAvailabilityAndRating(book.getId(), book.getName(), book.getAuthor(), book.getDescription(), rating.getRating(), book.getImagePath(), availability.isAvailable());
                })
                .collect(Collectors.toList());
        return booksWithAvailability;
    }

    @PostMapping("/catalog/borrow")
    public String borrow(@ModelAttribute BorrowData data) {
        borrowService.changeAvailability(data.getId());
        return "redirect:/catalog";
    }
}