package com.example.registration.helpers;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class Utils {
    public static void longToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void toast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String getText(EditText text) {
        return String.valueOf(text.getText());
    }
}
