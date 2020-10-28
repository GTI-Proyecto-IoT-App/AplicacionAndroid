package com.example.androidappgestionbasura.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;

import com.example.androidappgestionbasura.R;

public class Utility {

    public static boolean isEmptyOrNull(String value) {
        return (value == null || value.isEmpty());
    }

    // Context -> isConnected() -> T/F
    public static boolean isConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static void setError(Context context, String error, EditText editText){
        Drawable customErrorDrawable = context.getResources().getDrawable(R.drawable.ic_alert);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
        editText.setError(error,customErrorDrawable);
    }
}
