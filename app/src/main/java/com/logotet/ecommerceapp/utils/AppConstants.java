package com.logotet.ecommerceapp.utils;

import android.app.Activity;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class AppConstants {
    public static final String USERS = "users";
    public static final String ECOMMERCE_SHARED_PREFERENCES = "ecommercePrefs";
    public static final String LOGGED_IN_USERNAME = "logged_in_username";
    public static final String USER_DETAILS = "user_details";
    public static final String USER_PROFILE_COMPLETED = "user_profile_completed";
    public static final String GENDER = "gender";
    public static final String MOBILE = "mobile";
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    public static final String USER_PROFILE_IMAGE = "user_profile_image";

    public static String getFileExtension(Activity activity, Uri uri){
        return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(activity.getContentResolver().getType(uri));
    }
}
