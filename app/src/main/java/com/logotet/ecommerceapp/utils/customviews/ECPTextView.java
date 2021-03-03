package com.logotet.ecommerceapp.utils.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class ECPTextView extends AppCompatTextView {
    public ECPTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyFont();
    }

    private void applyFont() {
        Typeface typeface =
                Typeface.createFromAsset(super.getContext().getAssets(), "Montserrat-Regular.ttf");
        setTypeface(typeface);

    }
}
