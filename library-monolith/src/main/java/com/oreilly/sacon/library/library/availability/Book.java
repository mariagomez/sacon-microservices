package com.oreilly.sacon.library.library.availability;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean available;

    protected Book() {
    }

    public Book(boolean available) {
        this.available = available;
    }
    public boolean borrow() {
        if (available) {
            available = false;
            return true;
        }
        return false;
    }

    public boolean inStock() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}
