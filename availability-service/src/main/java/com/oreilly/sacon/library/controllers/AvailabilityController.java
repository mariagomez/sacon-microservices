package com.oreilly.sacon.library.controllers;


import com.oreilly.sacon.library.availability.Availability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AvailabilityController {

    @Autowired
    private Availability availability;

    @GetMapping(path = "/availability/{id}", produces = "application/json; charset=UTF-8")
    public BookAvailability rating(@PathVariable long id) {
        boolean available = availability.inStock(id);
        return new BookAvailability(available);
    }

    @PutMapping(path = "/borrow/{id}")
    public ResponseEntity borrow(@PathVariable long id) {
        availability.borrow(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}