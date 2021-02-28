package com.logotet.ecommerceapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

public class ECButton extends AppCompatButton {

    public ECButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyFont();
    }

    private void applyFont() {

        Typeface typeface =
                Typeface.createFromAsset(super.getContext().getAssets(), "Montserrat-Bold.ttf");
        setTypeface(typeface);

    }
}
