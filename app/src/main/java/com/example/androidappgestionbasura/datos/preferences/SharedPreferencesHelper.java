package com.example.androidappgestionbasura.datos.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Ruben Pardo Casanova
 * Singleton SharedPreferences
 * La funcion de esta clase es centralizar las funciones de los shared preferences
 */
public class SharedPreferencesHelper {


    // nombre del fichero shared preferences
    private static final String PREF_NAME = "com.example.app.PREFERNCIAS";
    // claves de las variables
    private static final String KEY_USER_UID = "com.example.app.USER_UID";
    private static final String KEY_PRIMERA_VEZ_AUTO_START_PERMISOS = "com.example.app.AUTO_START_PERMISOS";

    private static SharedPreferencesHelper sInstance;
    private final SharedPreferences mPref;

    private SharedPreferencesHelper(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferencesHelper(context);
        }
    }
    public static synchronized SharedPreferencesHelper getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(SharedPreferencesHelper.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }


    // getters y setter
    public void setUID(String value) {
        mPref.edit()
                .putString(KEY_USER_UID, value)
                .apply();
    }
    public String getUID() {
        return mPref.getString(KEY_USER_UID,"");
    }


    // getters y setter
    public void setPrimerVezPermisoAutoStart(Boolean value) {
        mPref.edit()
                .putBoolean(KEY_PRIMERA_VEZ_AUTO_START_PERMISOS, value)
                .apply();
    }
    public Boolean getPrimerVezPermisoAutoStart() {
        return mPref.getBoolean(KEY_PRIMERA_VEZ_AUTO_START_PERMISOS,true);
    }


    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .apply();
    }
    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }



}
