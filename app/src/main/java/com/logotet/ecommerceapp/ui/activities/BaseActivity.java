package com.logotet.ecommerceapp.ui.activities;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.logotet.ecommerceapp.R;

public class BaseActivity extends AppCompatActivity {

    public void showErrorSnackBar(String message, Boolean errorMessage) {
        Snackbar snackBar =
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackBar.getView();

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                            BaseActivity.this,
                            R.color.colorSnackBarError
                    )
            );
        } else {
            snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                            BaseActivity.this,
                            R.color.colorSnackBarSuccess
                    )
            );
        }
        snackBar.show();
    }
}
