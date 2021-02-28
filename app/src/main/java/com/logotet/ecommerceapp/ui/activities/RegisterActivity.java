package com.logotet.ecommerceapp.ui.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.navigation.NavigationView;
import com.logotet.ecommerceapp.R;
import com.logotet.ecommerceapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends BaseActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        setUpToolbar();
    }

    private void setUpToolbar() {
        setSupportActionBar(binding.toolbarRegisterActivity);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back);
        binding.toolbarRegisterActivity.setNavigationOnClickListener((View.OnClickListener) item -> {
            onBackPressed();
        });
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateRegisterDetails();
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
        if (TextUtils.isEmpty(binding.etEmail.getText().toString().trim())){
            showErrorSnackBar(getResources().getString(R.string.err_msg_enter_email), true);
            return false;
        }

        if (TextUtils.isEmpty(binding.etPassword.getText().toString().trim())){
            showErrorSnackBar(getResources().getString(R.string.err_msg_enter_password), true);
            return false;
        }

        if (TextUtils.isEmpty(binding.etConfirmPassword.getText().toString().trim())){
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
        return true;

    }
}

