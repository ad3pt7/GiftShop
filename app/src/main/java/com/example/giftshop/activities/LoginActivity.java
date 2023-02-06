package com.example.giftshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.giftshop.databinding.ActivityLoginBinding;
import com.example.giftshop.models.Product;
import com.example.giftshop.models.ProductsLikes;
import com.example.giftshop.models.User;
import com.example.giftshop.utilities.Constants;
import com.example.giftshop.utilities.Database;
import com.example.giftshop.utilities.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private PreferenceManager preferenceManager;
    private ArrayList<ProductsLikes> productsLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productsLikes = new ArrayList<>();
        fetchRequests();
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), CatalogActivity.class);
            startActivity(intent);
            finish();
        }
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
    }

    private void setListeners() {
        binding.buttonLogin.setOnClickListener(v -> {
            if (isValidSignInDetails()) {
                signIn();
            }
        });
    }

    private void fetchRequests()
    {
        Database.getInstance().setProductsLikes(new ArrayList<>());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("productsLikes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot valueRes: snapshot.getChildren())
                {
                    ProductsLikes like = valueRes.getValue(ProductsLikes.class);
                    productsLikes.add(like);
                    Database.getInstance().addProductLike(like);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Log.d("firebase", String.valueOf(productsLikes.size()));
        Log.d("firebase","start");
        //Database.getInstance().setProductsLikes(productsLikes);
        Log.d("firebase","end");
    }

    private void signIn() {
        loading(true);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        String login = binding.inputLogin.getText().toString();
        String password = binding.inputPassword.getText().toString();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child(login).getValue(User.class);
                if (snapshot.child(login).exists() && password.equals(user.password)) {
                    Log.d("firebase", user.password + " : " + password);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_NAME, login);
                    Database.getInstance().setCurrentUserId(user.id);
                    preferenceManager.putInteger(Constants.KEY_USER_ID,user.id);
                    //preferenceManager.putString(Constants.KEY_USER_ID, String.valueOf(user.id));
                    Intent intent = new Intent(getApplicationContext(), CatalogActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    loading(false);
                    showToast("Unable to sign in");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loading(false);
            }
        });
    }

    public void loading(boolean isLoading) {
        if (isLoading) {
            binding.buttonLogin.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonLogin.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails() {
        if (binding.inputLogin.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else {
            return true;
        }
    }
}