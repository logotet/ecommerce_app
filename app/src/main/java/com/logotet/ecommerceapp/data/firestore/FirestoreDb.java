package com.logotet.ecommerceapp.data.firestore;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.logotet.ecommerceapp.models.User;
import com.logotet.ecommerceapp.ui.activities.BaseActivity;
import com.logotet.ecommerceapp.ui.activities.LoginActivity;
import com.logotet.ecommerceapp.ui.activities.RegisterActivity;
import com.logotet.ecommerceapp.ui.activities.SettingsActivity;
import com.logotet.ecommerceapp.ui.activities.UserProfileActivity;
import com.logotet.ecommerceapp.utils.AppConstants;

import java.util.HashMap;

public class FirestoreDb {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User user;

    public void registerUser(RegisterActivity activity, User firebaseUser) {
        if (firebaseUser != null) {
            db.collection(AppConstants.USERS)
                    .document(firebaseUser.getId())
                    .set(firebaseUser, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                                Toast.makeText(activity.getBaseContext(), "Success",
                                        Toast.LENGTH_SHORT).show();
//                            activity.userRegistrationSuccess();
                            }
                    )
                    .addOnFailureListener(e -> {
                                Toast.makeText(activity.getBaseContext(), e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                activity.hideProgressBar();
                            }
                    );
        }
    }

    public String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : "";
    }

    public void getUserDetails(Activity activity) {
        db.collection(AppConstants.USERS)
                .document(getCurrentUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (activity instanceof LoginActivity) {
                        user = task.getResult().toObject(User.class);
                        ((LoginActivity) activity).userLoggedInSuccess(user);
                    }
                    if (activity instanceof SettingsActivity) {
                        user = task.getResult().toObject(User.class);
                        ((SettingsActivity) activity).setUserInfo(user);
                    }
                })
                .addOnFailureListener(e -> {
                    if (activity instanceof LoginActivity || activity instanceof SettingsActivity) {
                        ((BaseActivity) activity).hideProgressBar();
                    }
                });
    }

    public void updateUserDetails(Activity activity, HashMap<String, Object> userDetails) {
        db.collection(AppConstants.USERS)
                .document(getCurrentUserId())
                .update(userDetails)
                .addOnCompleteListener(task -> {
                    if (activity instanceof UserProfileActivity) {
                        ((UserProfileActivity) activity).userProfileUpdateSuccess();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(activity.getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show());
    }

    public void uploadImg(Activity activity, Uri imgFileUri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(AppConstants.USER_PROFILE_IMAGE
                + System.currentTimeMillis()
                + "."
                + AppConstants.getFileExtension(activity, imgFileUri));


        storageReference.putFile(imgFileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Log.e("Firebase Image URL", taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    Toast.makeText(activity.getBaseContext(), "Image Success", Toast.LENGTH_LONG).show();
                    taskSnapshot.getMetadata().getReference().getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                if (activity instanceof UserProfileActivity) {
                                    ((UserProfileActivity) activity).imageUploadSuccess(uri.toString());
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(activity.getClass().getSimpleName(), e.getMessage(), e);
                    Toast.makeText(activity.getBaseContext(), "Image Fail", Toast.LENGTH_LONG).show();
                });
    }

}
