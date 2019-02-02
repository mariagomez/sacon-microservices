package com.oreilly.sacon.library.rating;

public class ItemRating {

    private Long id;

    private int rating;

    protected ItemRating() {
    }

    public ItemRating(int rating) {
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
