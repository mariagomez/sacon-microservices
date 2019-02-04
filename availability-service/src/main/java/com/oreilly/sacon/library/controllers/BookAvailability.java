package com.oreilly.sacon.library.controllers;

public class BookAvailability {

    private boolean inStock;

    public BookAvailability(boolean inStock) {
        this.inStock = inStock;
    }

    public boolean isInStock() {
        return inStock;
    }
}
