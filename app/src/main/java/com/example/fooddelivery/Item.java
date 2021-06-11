package com.example.fooddelivery;

public class Item {

    String category, name, image,description;

    public Item(String category, String name, String description, String image)
    {
        this.image = image;
        this.category = category;
        this.name = name;
        this.description=description;
    }
    public Item()
    {}

    public String getName() {
        return name;
    }

    public String getCategory()
    {
        return category;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
