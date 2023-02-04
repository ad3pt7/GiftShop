package com.example.giftshop;

import java.util.ArrayList;
import java.util.HashMap;

public class Product {
    public String description;
    public String name;
    public int likes;
    public HashMap<String,String> tags;

    public Product(String name, int likes, String description, HashMap<String,String> tags) {
        this.name = name;
        this.description = description;
        this.likes = likes;
        this.tags = tags;
    }
    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}
