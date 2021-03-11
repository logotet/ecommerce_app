package com.logotet.ecommerceapp.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.HashMap;

public class UserProfileActivity extends BaseActivity {

    private ActivityUserProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseUser firebaseUser;
    private FirestoreDb firestoreDb;
    private final static int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private User user;
    private Uri dataUri;
    private String userProfileImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        sharedPreferences = this.getSharedPreferences(AppConstants.ECOMMERCE_SHARED_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestoreDb = new FirestoreDb();
        try{
            updateUserFields(getIntent().getParcelableExtra(AppConstants.USER_DETAILS));
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        firestoreDb.getUserDetails(this);
        user = new User();

        binding.flUserImage.setOnClickListener(view -> checkPermission());

        binding.btnSave.setOnClickListener(view -> {
            if (validateRegisterDetails()) {
                showProgressDialog("P");
                editor.putInt(AppConstants.USER_PROFILE_COMPLETED, 1);
                editor.apply();
                firestoreDb.uploadImg(this, dataUri);
                updateUser();
                hideProgressBar();
            }
        });
    }

    public void updateUserFields(User user) {
        binding.etFirstName.setText(user.getFirstName());
        binding.etFirstName.setEnabled(false);
        binding.etLastName.setText(user.getLastName());
        binding.etLastName.setEnabled(false);
        binding.etEmail.setText(user.getEmail());
        binding.etEmail.setEnabled(false);
    }

    private String getGender() {
        return binding.rbMale.isChecked() ? AppConstants.MALE : AppConstants.FEMALE;
    }

    private void updateUser() {
        HashMap<String, Object> userHashMap = new HashMap<>();
        long mobileNumber = Long.parseLong(binding.etMobileNumber.getText().toString().trim());
        userHashMap.put(AppConstants.MOBILE, mobileNumber);
        String gender = getGender();
        userHashMap.put(AppConstants.GENDER, gender);
        userHashMap.put(AppConstants.IMAGE, userProfileImageUrl);
        firestoreDb.updateUserDetails(this, userHashMap);
    }


    private boolean validateRegisterDetails() {
        if (TextUtils.isEmpty(binding.etMobileNumber.getText().toString().trim())) {
            showErrorSnackBar(getResources().getString(R.string.err_msg_enter_mobile), true);
            return false;
        }
        showErrorSnackBar("Your details are valid.", false);
        return true;
    }

    public void userProfileUpdateSuccess() {
        Toast.makeText(
                this,
                getResources().getString(R.string.msg_profile_update_success),
                Toast.LENGTH_SHORT
        ).show();
        startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
        finish();
    }

    public void imageUploadSuccess(String imageURL) {
        hideProgressBar();
        userProfileImageUrl = imageURL;
        updateUser();
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            GalleryManager.chooseImageFromGallery(UserProfileActivity.this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryManager.chooseImageFromGallery(UserProfileActivity.this);
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GalleryManager.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        dataUri = data.getData();
                        new GlideHelper(this).loadUserPicture(dataUri, binding.ivUserPhoto);
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        dataUri = Uri.parse("android.resource://"+getBaseContext().getPackageName()+"/drawable/"
                                + getResources().getResourceName(R.drawable.ic_user_placeholder));

                    }
                }
            }
        }
    }
}