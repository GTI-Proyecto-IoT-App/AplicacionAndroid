package com.example.androidappgestionbasura.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.EditText;

import com.example.androidappgestionbasura.R;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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

    public static HashMap<String, Object> objectToHashMap(Object obj) {
        HashMap<String, Object> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try { map.put(field.getName(), field.get(obj)); } catch (IllegalAccessException e) {
                Log.e("Error", "Se ha producio un error en el Mapeo de un objeto.", e);
            }
        }
        return map;
    }
}
