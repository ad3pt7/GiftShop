package com.example.giftshop.listeners;

import android.view.View;

import com.example.giftshop.models.Product;

public interface LikeListener {
    void onLikeClicked(View view, Product product);
}
