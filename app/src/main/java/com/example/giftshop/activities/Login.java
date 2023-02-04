package com.example.giftshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.giftshop.databinding.ActivityLoginBinding;
import com.example.giftshop.models.User;
import com.example.giftshop.utilities.Constants;
import com.example.giftshop.utilities.PreferenceManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        setContentView(binding.getRoot());
    }

    private void setListeners() {
        binding.buttonLogin.setOnClickListener(v -> {
            if (isValidSignInDetails()) {
                signIn();
            }
        });
    }

    private void signIn() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        loading(true);
        String login = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();
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
                            Toast.makeText(Login.this, "Авторизация успешна",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("firebase","test2");
                        }else{
                            Toast.makeText(Login.this, "Проверьте данные",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Login.this, "Проверьте данные",
                                Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(Login.this, "Заполните поля",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loading(false);
            }
        });
        loading(false);
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        database.collection(Constants.KEY_COLLECTION_USERS)
//                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
//                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult() != null
//                            && task.getResult().getDocuments().size() > 0) {
//                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
//                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
//                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
//                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
//                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                    } else {
//                        loading(false);
//                        showToast("Unable to sign in");
//                    }
//                });
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
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails() {
        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else {
            return true;
        }
    }
}