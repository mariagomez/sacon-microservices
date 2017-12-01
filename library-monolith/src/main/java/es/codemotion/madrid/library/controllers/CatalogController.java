package es.codemotion.madrid.library.controllers;

import es.codemotion.madrid.library.dao.Item;
import es.codemotion.madrid.library.models.Book;
import es.codemotion.madrid.library.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class CatalogController {

    @Autowired
    private BookRepository bookRepository;

    @RequestMapping("/catalog")
    public String catalog(ModelMap model) {
        Iterable<Item> items = bookRepository.findAll();
        List<Book> books = StreamSupport.stream(items.spliterator(), false)
                .map(item -> new Book(item.getId(), item.getName(), item.getAuthor(), item.getDescription(), item.getRating(),
                        item.getImagePath(), item.isAvailable()))
                .sorted(Comparator.comparingLong(item -> item.getId()))
                .collect(Collectors.toList());
        model.addAttribute("books", books);
        return "catalog";
    }

    @RequestMapping(value = "/catalog/borrow", params={"bookId"})
    public String borrow(final HttpServletRequest request) {
        String parameter = request.getParameter("bookId");
        Item book = bookRepository.findOne(Integer.valueOf(parameter).longValue());
        book.setAvailable(false);
        bookRepository.save(book);
        return "redirect:/catalog";
    }
    
}
