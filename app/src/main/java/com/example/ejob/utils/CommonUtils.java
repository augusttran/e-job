package com.example.ejob.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Patterns;
import android.widget.EditText;

import com.example.ejob.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class CommonUtils {

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean checkFields(EditText textField) {
        boolean valid = true;
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    public static CharSequence getTitlefromUser(int position) {
        String str1 = "Home";
        String str2 = "Favorite";
        String str3 = "Profile";

        switch (position) {
            case 0:
                return str1;

            case 1:
                return str2;

            case 2:
                return str3;

            default:
                return str1;
        }
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();

        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;

    }

}
