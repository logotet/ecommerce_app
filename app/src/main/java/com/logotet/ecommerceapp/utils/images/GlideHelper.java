package com.logotet.ecommerceapp.utils.images;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.logotet.ecommerceapp.R;

public class GlideHelper {

    private Context context;

    public GlideHelper(Context context) {
        this.context = context;
    }

    public void loadUserPicture(Object image, ImageView imageView) {
        try {
            Glide
                    .with(context)
                    .load(Uri.parse(image.toString()))
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_placeholder)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
