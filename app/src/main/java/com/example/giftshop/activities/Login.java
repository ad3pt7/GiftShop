package com.example.giftshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.giftshop.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


}