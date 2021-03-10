package com.logotet.ecommerceapp.data.firestore;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.logotet.ecommerceapp.models.User;
import com.logotet.ecommerceapp.ui.activities.RegisterActivity;
import com.logotet.ecommerceapp.ui.activities.UserProfileActivity;
import com.logotet.ecommerceapp.utils.AppConstants;

import java.util.HashMap;

public class FirestoreDb {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        SharedPreferences.Editor editor = activity.getSharedPreferences(AppConstants.ECOMMERCE_SHARED_PREFERENCES,
                Context.MODE_PRIVATE).edit();
        db.collection(AppConstants.USERS)
                .document(getCurrentUserId())
                .get()
                .addOnCompleteListener(task -> {
                    User user = task.getResult().toObject(User.class);
                    Log.i("First :", user.getFirstName());
                    Log.i("Last :", user.getLastName());
                    Log.i("Email :", user.getEmail());
                    editor.putInt(AppConstants.USER_PROFILE_COMPLETED, user.getProfileCompleted());
                    editor.apply();
                    Toast.makeText(activity.getBaseContext(), user.getFirstName(), Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(activity.getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show());
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
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("Firebase Image URL", taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                        Toast.makeText(activity.getBaseContext(), "Image", Toast.LENGTH_LONG).show();
                        if(activity instanceof UserProfileActivity){
                            ((UserProfileActivity) activity).imageUploadSuccess(imgFileUri.toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(activity.getClass().getSimpleName(), e.getMessage(), e);
                        Toast.makeText(activity.getBaseContext(), "Image Fail", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
