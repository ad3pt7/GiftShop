package com.example.giftshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.giftshop.R;
import com.example.giftshop.adapters.ProductsAdapter;
import com.example.giftshop.listeners.LikeListener;
import com.example.giftshop.adapters.ProductsAdapterr;
import com.example.giftshop.databinding.ActivityCatalogBinding;
import com.example.giftshop.models.Product;
import com.example.giftshop.utilities.Constants;
import com.example.giftshop.utilities.Database;
import com.example.giftshop.utilities.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CatalogActivity extends AppCompatActivity implements LikeListener {

    private ActivityCatalogBinding binding;
    private PreferenceManager preferenceManager;
    //private ProductsAdapterr productsAdapterr;
    private ProductsAdapter productsAdapter;
    private ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatalogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        loadUserDetails();
        setListeners();
        getAllProducts();
    }

    private void init() {
        products = new ArrayList<>();
        //productsAdapterr = new ProductsAdapterr(products,this);
        //binding.productsRecyclerView.setAdapter(productsAdapterr);
        productsAdapter = new ProductsAdapter();
        binding.productsRecyclerView.setAdapter(productsAdapter);
        binding.productsRecyclerView.getAdapter().notifyDataSetChanged();

    }

    private void setListeners() {
        binding.imageSignOut.setOnClickListener(v -> signOut());
    }

    private void loadUserDetails() {
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
    }

    public void getAllProducts()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot valueRes: snapshot.getChildren())
                {
                    Product product = valueRes.getValue(Product.class);
                    products.add(product);
                }
                //productsAdapterr.notifyDataSetChanged();
                productsAdapter.notifyDataSetChanged();
                Collections.sort(products);
                productsAdapter.setProducts(products);
                binding.productsRecyclerView.smoothScrollToPosition(0);
                binding.productsRecyclerView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("products").addListenerForSingleValueEvent(valueEventListener);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void signOut() {
        showToast("Sign out...");
        preferenceManager.clear();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    public void onLikeClicked(View view, Product product) {
        //Log.d("firebase", String.valueOf(product.id));
    }
}