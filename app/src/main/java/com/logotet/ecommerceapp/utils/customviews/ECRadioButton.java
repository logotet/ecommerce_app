package com.logotet.ecommerceapp.utils.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

public class ECRadioButton  extends AppCompatRadioButton {

    public ECRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyFont();
    }

    private void applyFont() {
        Typeface typeface =
                Typeface.createFromAsset(super.getContext().getAssets(), "Montserrat-Bold.ttf");
        setTypeface(typeface);
    }
}
