package com.oreilly.sacon.library.controllers;

public class BookAvailability {

    private boolean available;

    public BookAvailability(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }
}
