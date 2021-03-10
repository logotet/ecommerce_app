package com.logotet.ecommerceapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.logotet.ecommerceapp.R;
import com.logotet.ecommerceapp.databinding.ActivityLoginBinding;
import com.logotet.ecommerceapp.utils.AppConstants;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private String email;
    private String password;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        sharedPreferences = this.getSharedPreferences(AppConstants.ECOMMERCE_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);

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
                FirebaseUser user = auth.getCurrentUser();
                int profileCompleted = sharedPreferences.getInt(AppConstants.USER_PROFILE_COMPLETED, 8);
                if(profileCompleted == 0){
                    goToProfileSetup();
                }else{
                    goToMain(user);
                }
            } else {
                Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void goToProfileSetup() {
        Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToMain(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("email", user.getEmail());
        intent.putExtra("uid", user.getUid());
        startActivity(intent);
    }
}