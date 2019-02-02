package com.oreilly.sacon.library.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CatalogService {

    @Autowired
    private BookRepository bookRepository;

    public List<Item> getAllBooks() {
        Iterable<Item> items = bookRepository.findAll();
        List<Item> books = StreamSupport.stream(items.spliterator(), false)
                .collect(Collectors.toList());
        return books;
    }
}
