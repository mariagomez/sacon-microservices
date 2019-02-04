package com.oreilly.sacon.library.library.availability;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Availability {

    @Autowired
    private BookAvailabilityRepository bookAvailabilityRepository;

    public boolean inStock(long bookId) {
        Book book = bookAvailabilityRepository.findOne(bookId);
        return book.inStock();
    }

    public void borrow(long bookId) {
        Book book = bookAvailabilityRepository.findOne(bookId);
        book.borrow();
        bookAvailabilityRepository.save(book);
    }
}
