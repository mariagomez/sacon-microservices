package com.oreilly.sacon.library.controllers;


import com.oreilly.sacon.library.availability.Availability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AvailabilityController {

    @Autowired
    private Availability availability;

    @GetMapping(path = "/availability/{id}", produces = "application/json; charset=UTF-8")
    public BookAvailability rating(@PathVariable long id) {
        boolean available = availability.inStock(id);
        return new BookAvailability(available);
    }

    @PutMapping(path = "/borrow")
    public ResponseEntity borrow(@RequestBody long id) {
        availability.borrow(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}