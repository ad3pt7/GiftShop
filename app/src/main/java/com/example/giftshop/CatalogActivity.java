package com.example.giftshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {

    ProductsAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        products = new ArrayList<>();
        recyclerView = findViewById(R.id.productsRecyclerView);
        adapter = new ProductsAdapter();
        adapter.setOnClickListener(new ProductsAdapter.onLikeClickListener() {
            @Override
            public void onLikeClick(Product product) {
                onLikeProduct(product);
            }
        });
        recyclerView.setAdapter(adapter);
        getAllProducts();
        Log.d("firebase", String.valueOf(products.size()));
        Log.d("firebase", String.valueOf(products.size()));

    }

    public void onLikeProduct(Product product){
        Log.d("firebase",product.name);
    }

    public void getAllProducts()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot valueRes: snapshot.getChildren())
                {
                    Product product = valueRes.getValue(Product.class);
                    Log.d("firebase",product.name);
//                    ArrayList<String> values = new ArrayList<>(product.tags.values());
//                    for(int i =0; i< product.tags.size();i++)
//                    {
//                        Log.d("firebase", values.get(i));
//                    }
                    //products.add(product);
                    adapter.addProduct(product);
                    Log.d("firebase", String.valueOf(products.size()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("products").addListenerForSingleValueEvent(valueEventListener);
    }
}