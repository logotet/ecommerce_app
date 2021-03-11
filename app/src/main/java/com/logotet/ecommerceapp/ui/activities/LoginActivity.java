package com.logotet.ecommerceapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.logotet.ecommerceapp.R;
import com.logotet.ecommerceapp.data.firestore.FirestoreDb;
import com.logotet.ecommerceapp.databinding.ActivityLoginBinding;
import com.logotet.ecommerceapp.models.User;
import com.logotet.ecommerceapp.utils.AppConstants;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private String email;
    private String password;
    private SharedPreferences sharedPreferences;
    private FirestoreDb firestoreDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        sharedPreferences = this.getSharedPreferences(AppConstants.ECOMMERCE_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        firestoreDb = new FirestoreDb();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            goToMain(user);
            finish();
        }

        binding.btnLogin.setOnClickListener(view -> {
            if (binding.etEmail.getText().toString().trim() != null && binding.etPassword.getText().toString().trim() != null) {
                email = binding.etEmail.getText().toString().trim();
                password = binding.etPassword.getText().toString().trim();
                showProgressDialog(getResources().getString(R.string.please_wait));
                login();
            }
        });

        binding.tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.tvForgotPassword.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class))
        );

    }

    private void login() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            hideProgressBar();
            if (task.isSuccessful()) {
                firestoreDb.getUserDetails(this);
            } else {
                Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void goToMain(FirebaseUser user) {
//        for now go to dashboard activity
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("email", user.getEmail());
        intent.putExtra("uid", user.getUid());
        startActivity(intent);
    }

    public void userLoggedInSuccess(User user) {
        hideProgressBar();
        Log.i("First Name: ", user.getFirstName());
        Log.i("Last Name: ", user.getLastName());
        Log.i("Email: ", user.getEmail());
        if (user.getProfileCompleted() == 0) {
            Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
            intent.putExtra(AppConstants.USER_DETAILS, user);
            startActivity(intent);
        } else {
            //        for now go to dashboard activity
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        }
        finish();
    }
}