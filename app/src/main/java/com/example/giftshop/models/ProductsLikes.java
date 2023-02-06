package com.example.giftshop.models;

public class ProductsLikes {
    public int productId;
    public int userId;
    public String name;

    public ProductsLikes() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ProductsLikes(String name,int productId, int userId) {
        this.name = name;
        this.productId = productId;
        this.userId = userId;
    }
}
