package com.example.giftshop.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Product implements Comparable<Product>{
    public int id;
    public String description;
    public String name;
    public String likeName;
    public int likes;

    public int getLikes() {
        return likes;
    }

    public HashMap<String,String> tags;

    public Product(int id,String name,String likeName, int likes, String description, HashMap<String,String> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.likes = likes;
        this.tags = tags;
        this.likeName=likeName;
    }
    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    @Override
    public int compareTo(Product product) {
        int compareLikes = ((Product)product).getLikes();
        return  compareLikes - this.likes;
    }
}
