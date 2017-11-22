package es.codemotion.madrid.library.catalog;

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
    private String imagePath;

    protected Item() {
    }

    public Item(String name, String author, String description, String imagePath) {
        this.name = name;
        this.author = author;
        this.description = description;
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

    public String getImagePath() {
        return imagePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
