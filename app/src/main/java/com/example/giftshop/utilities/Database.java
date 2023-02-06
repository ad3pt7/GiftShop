package com.example.giftshop.utilities;

import com.example.giftshop.models.Product;
import com.example.giftshop.models.ProductsLikes;

import java.util.ArrayList;

public class Database {
    private static Database instance;
    //private static int currentUserId;
    private ArrayList<ProductsLikes> productsLikes;

    public static Database getInstance(){
        if(instance == null){
            instance = new Database();
        }
        return instance;
    }

//    public void setCurrentUserId(int currentUserId) {
//        Database.currentUserId = currentUserId;
//    }

//    public int getCurrentUserId() {
//        return currentUserId;
//    }

    public ArrayList<ProductsLikes> getProductsLikes() {
        return productsLikes;
    }

    public void addProductLike(ProductsLikes like){
        productsLikes.add(like);
    }

    public void setProductsLikes(ArrayList<ProductsLikes> productsLikes) {
        this.productsLikes = productsLikes;
    }
}
