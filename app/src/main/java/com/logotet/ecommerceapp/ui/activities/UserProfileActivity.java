package com.logotet.ecommerceapp.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.logotet.ecommerceapp.R;
import com.logotet.ecommerceapp.data.firestore.FirestoreDb;
import com.logotet.ecommerceapp.databinding.ActivityUserProfileBinding;
import com.logotet.ecommerceapp.models.User;
import com.logotet.ecommerceapp.utils.AppConstants;
import com.logotet.ecommerceapp.utils.images.GalleryManager;
import com.logotet.ecommerceapp.utils.images.GlideHelper;

public class UserProfileActivity extends BaseActivity {

    private ActivityUserProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseUser firebaseUser;
    private FirestoreDb firestoreDb;
    private final static int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        sharedPreferences = this.getSharedPreferences(AppConstants.ECOMMERCE_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestoreDb = new FirestoreDb();
        firestoreDb.getUserDetails(this);

//        getIntent().getParcelableExtra()

        binding.flUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        binding.btnSave.setOnClickListener(view -> {
            if(validateRegisterDetails()){
                completeUserProfile();
                editor.putInt(AppConstants.USER_PROFILE_COMPLETED, 1);
                editor.apply();
            }});
    }

    private void completeUserProfile() {
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setFirstName(binding.etFirstName.getText().toString().trim());
        user.setLastName(binding.etLastName.getText().toString().trim());
        user.setEmail(binding.etEmail.getText().toString().trim());
        user.setImage("1");
        user.setMobile(Long.parseLong(binding.etMobileNumber.getText().toString().trim()));
        user.setGender("male");
        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
        intent.putExtra("email", user.getEmail());
        intent.putExtra("uid", user.getId());
        intent.putExtra(AppConstants.USER_DETAILS, user);
        startActivity(intent);
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

        if (TextUtils.isEmpty(binding.etMobileNumber.getText().toString().trim())) {
            showErrorSnackBar(getResources().getString(R.string.err_msg_enter_mobile), true);
            return false;
        }

//        if (TextUtils.isEmpty(binding.etConfirmPassword.getText().toString().trim())) {
//            showErrorSnackBar(getResources().getString(R.string.err_msg_enter_confirm_password), true);
//            return false;
//        }


        showErrorSnackBar("Your details are valid.", false);
        return true;
    }

    public void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){
            GalleryManager.chooseImageFromGallery(UserProfileActivity.this);
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                GalleryManager.chooseImageFromGallery(UserProfileActivity.this);
            }else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == GalleryManager.PICK_IMAGE_REQUEST_CODE){
                if(data != null){
                    try{
                        Uri dataUri = data.getData();
                        new GlideHelper(this).loadUserPicture(dataUri, binding.ivUserPhoto);
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            }
        }
    }
}