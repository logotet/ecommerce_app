package com.logotet.ecommerceapp.ui.activities;

import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.logotet.ecommerceapp.R;

public class BaseActivity extends AppCompatActivity {
    private Dialog progressDialog;
    private boolean doebleBackPressedOnce = false;

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

    public void showProgressDialog(String text){
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.dialog_progress);
        TextView textView = progressDialog.findViewById(R.id.tv_progress_text);
        textView.setText(text);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();
    }

    public void hideProgressBar(){
        progressDialog.dismiss();
    }

    public void doubleBackToExit(){
        if(doebleBackPressedOnce){
            finishAndRemoveTask();
            return;
        }
        this.doebleBackPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doebleBackPressedOnce = false, 2000);
    }
}
