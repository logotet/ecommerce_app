package com.logotet.ecommerceapp.utils.images;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

public class GalleryManager {
    public static final int PICK_IMAGE_REQUEST_CODE = 12;

    public static void chooseImageFromGallery(Activity activity){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE);
    }
}
