package com.example.giftshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.giftshop.databinding.ActivityMainBinding;
import com.example.giftshop.models.Product;
import com.example.giftshop.R;
import com.example.giftshop.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatabaseReference mDatabase;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        ArrayList<Product> products = new ArrayList<>();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }

    public void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    public void onAuthorization(View view) {
        String login = binding.editName.getText().toString();
        String password = binding.editPassword.getText().toString();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!login.isEmpty() && !password.isEmpty())
                {
                    User user = snapshot.child(login).getValue(User.class);
                    if(snapshot.child(login).exists())
                    {
                        Log.d("firebase",user.password + " : " + password);
                        if(password.equals(user.password)){
                            Toast.makeText(MainActivity.this, "Авторизация успешна",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("firebase","test2");
                        }else{
                            Toast.makeText(MainActivity.this, "Проверьте данные",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Проверьте данные",
                                Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Заполните поля",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAllProducts()
    {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> products = new ArrayList<>();
                for(DataSnapshot valueRes: snapshot.getChildren())
                {
                    Product product = valueRes.getValue(Product.class);
                    Log.d("firebase",product.name);
//                    ArrayList<String> values = new ArrayList<>(product.tags.values());
//                    for(int i =0; i< product.tags.size();i++)
//                    {
//                        Log.d("firebase", values.get(i));
//                    }
                    products.add(product);
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