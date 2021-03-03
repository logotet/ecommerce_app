package com.logotet.ecommerceapp.utils.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

public class ECEditText extends AppCompatEditText {
    public ECEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyFont();
    }

    private void applyFont() {
        Typeface typeface =
                Typeface.createFromAsset(super.getContext().getAssets(), "Montserrat-Regular.ttf");
        setTypeface(typeface);

    }
}
