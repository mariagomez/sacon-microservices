package es.codemotion.madrid.library.dao;

import javax.persistence.*;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String author;
    @Lob
    private String description;
    private int rating;
    private boolean available;
    private String imagePath;

    protected Item() {
    }

    public Item(String name, String author, String description, int rating, boolean available, String imagePath) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.rating = rating;
        this.available = available;
        this.imagePath = imagePath;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Long getId() {
        return id;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
