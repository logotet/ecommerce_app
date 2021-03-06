package com.logotet.ecommerceapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.logotet.ecommerceapp.R;
import com.logotet.ecommerceapp.data.firestore.FirestoreDb;
import com.logotet.ecommerceapp.databinding.ActivityRegisterBinding;
import com.logotet.ecommerceapp.models.User;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    private String email;
    private String password;
    private FirestoreDb firestoreDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        setUpToolbar();
        auth = FirebaseAuth.getInstance();
        firestoreDb = new FirestoreDb();

        binding.btnRegister.setOnClickListener(view -> {
            validateRegisterDetails();
            if (validateRegisterDetails()) {
                showProgressDialog(getResources().getString(R.string.please_wait));
                setUpFirebaseAccount();
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(binding.toolbarRegisterActivity);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back);
        binding.toolbarRegisterActivity.setNavigationOnClickListener(item -> {
            onBackPressed();
        });

    }

    private void setUpFirebaseAccount() {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressBar();
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    User user = new User(firebaseUser.getUid(),
                            binding.etFirstName.getText().toString().trim(),
                            binding.etLastName.getText().toString().trim(),
                            binding.etEmail.getText().toString().trim(),
                            "",
                            0,
                            "",
                            0
                    );

                    firestoreDb.registerUser(RegisterActivity.this, user);

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("email", firebaseUser.getEmail());
                    intent.putExtra("uid", firebaseUser.getUid());
                    startActivity(intent);

                } else {
                    Toast.makeText(RegisterActivity
                                    .this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean validateRegisterDetails() {
        if (TextUtils.isEmpty(binding.etFirstName.getText().toString().trim())) {
            showErrorSnackBar(getResources().getString(R.string.err_msg_enter_first_name), true);
            return false;
        }

        if (TextUtils.isEmpty(binding.etLastName.getText().toString().trim())) {
            showErrorSnackBar(getResources().getString(R.string.err_msg_enter_last_name), true);
            return false;
        }
        if (TextUtils.isEmpty(binding.etEmail.getText().toString().trim())) {
            showErrorSnackBar(getResources().getString(R.string.err_msg_enter_email), true);
            return false;
        }

        if (TextUtils.isEmpty(binding.etPassword.getText().toString().trim())) {
            showErrorSnackBar(getResources().getString(R.string.err_msg_enter_password), true);
            return false;
        }

        if (TextUtils.isEmpty(binding.etConfirmPassword.getText().toString().trim())) {
            showErrorSnackBar(getResources().getString(R.string.err_msg_enter_confirm_password), true);
            return false;
        }


        if (!binding.etPassword.getText().toString().trim().equals(binding.etConfirmPassword.getText().toString()
                .trim())) {
            showErrorSnackBar(getResources().getString(R.string.err_msg_password_and_confirm_password_mismatch), true);
            return false;
        }
        if (!binding.cbTermsAndCondition.isChecked()) {
            showErrorSnackBar(getResources().getString(R.string.err_msg_agree_terms_and_condition), true);
            return false;
        }
        showErrorSnackBar("Your details are valid.", false);
        email = binding.etEmail.getText().toString().trim();
        password = binding.etPassword.getText().toString().trim();
        return true;

    }
}

