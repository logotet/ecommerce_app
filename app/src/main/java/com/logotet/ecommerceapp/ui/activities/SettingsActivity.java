package com.logotet.ecommerceapp.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.logotet.ecommerceapp.R;
import com.logotet.ecommerceapp.data.firestore.FirestoreDb;
import com.logotet.ecommerceapp.databinding.ActivitySettingsBinding;
import com.logotet.ecommerceapp.models.User;
import com.logotet.ecommerceapp.utils.AppConstants;
import com.logotet.ecommerceapp.utils.images.GlideHelper;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;
    private FirestoreDb firestoreDb;
    private User userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        setupActionbar();
        showProgressDialog(getResources().getString(R.string.please_wait));
        firestoreDb = new FirestoreDb();
        firestoreDb.getUserDetails(this);

        binding.tvEdit.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, UserProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(AppConstants.USER_DETAILS, userDetails);
            startActivity(intent);
            finish();
        });
    }

    private void setupActionbar() {
        setSupportActionBar(binding.toolbarSettingsActivity);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back);
        }

        binding.toolbarSettingsActivity.setNavigationOnClickListener(view -> onBackPressed());
    }

    public void setUserInfo(User user){
        userDetails = user;
        binding.tvName.setText(user.getFirstName() + " " + user.getLastName());
        binding.tvGender.setText(user.getGender());
        binding.tvEmail.setText(user.getEmail());
        binding.tvMobileNumber.setText((String.valueOf(user.getMobile())));
        new GlideHelper(this).loadUserPicture(user.getImage(), binding.ivUserPhoto);
        hideProgressBar();
    }
}