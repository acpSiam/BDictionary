package com.bmarpc.acpsiam.bdictionarydev.helpers;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.bmarpc.acpsiam.bdictionarydev.R;

import java.util.Locale;

public class LanguageHelper {

    public static void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getResources().getString(R.string.SHARED_PREFERENCES_APP_PROCESS),
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.CURRENT_LOCALE), languageCode);
        editor.apply();
    }



    public static String getCurrentLocalePref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getResources().getString(R.string.SHARED_PREFERENCES_APP_PROCESS),
                Context.MODE_PRIVATE
        );
        Toast.makeText(context, sharedPreferences.getString(context.getString(R.string.CURRENT_LOCALE), "en"), Toast.LENGTH_SHORT).show();
        Log.d("lan", sharedPreferences.getString(context.getString(R.string.CURRENT_LOCALE), "en"));
        return sharedPreferences.getString(context.getString(R.string.CURRENT_LOCALE), "en");
    }
}

