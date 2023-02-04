package com.example.giftshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.giftshop.activities.listeners.LikeListener;
import com.example.giftshop.adapters.ProductsAdapterr;
import com.example.giftshop.databinding.ActivityCatalogBinding;
import com.example.giftshop.models.Product;
import com.example.giftshop.adapters.ProductsAdapter;
import com.example.giftshop.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity implements LikeListener {

    private ActivityCatalogBinding binding;
//    ProductsAdapter adapter;
//    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCatalogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        products = new ArrayList<>();
//        adapter = new ProductsAdapter();
//        adapter.setOnClickListener(new ProductsAdapter.onLikeClickListener() {
//            @Override
//            public void onLikeClick(Product product) {
//                onLikeProduct(product);
//            }
//        });
//        binding.productsRecyclerView.setAdapter(adapter);
        getAllProducts();
//        Log.d("firebase", String.valueOf(products.size()));
//        Log.d("firebase", String.valueOf(products.size()));

    }

    public void onLikeProduct(Product product){
        Log.d("firebase",product.name);
    }

    public void getAllProducts()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Product> products = new ArrayList<Product>();
                for(DataSnapshot valueRes: snapshot.getChildren())
                {
                    Product product = valueRes.getValue(Product.class);
                    products.add(product);
                    Log.d("firebase",product.name);
//                    ArrayList<String> values = new ArrayList<>(product.tags.values());
//                    for(int i =0; i< product.tags.size();i++)
//                    {
//                        Log.d("firebase", values.get(i));
//                    }
                    //products.add(product);
//                    adapter.addProduct(product);
//                    Log.d("firebase", String.valueOf(products.size()));
                }
                if(products.size() > 0) {
                    ProductsAdapterr productsAdapterr = new ProductsAdapterr(products, CatalogActivity.this);
                    binding.productsRecyclerView.setAdapter(productsAdapterr);
                }
                else {
                    showErrorMessage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("products").addListenerForSingleValueEvent(valueEventListener);
    }

    private void showErrorMessage() {
//        binding.textErrorMessage.setText(String.format("%s", "No product available"));
//        binding.textErrorMessage.setVisibility(View.VISIBLE);
        Log.d("error", "error");
    }


    @Override
    public void onLikeClicked(Product product) {

    }
}