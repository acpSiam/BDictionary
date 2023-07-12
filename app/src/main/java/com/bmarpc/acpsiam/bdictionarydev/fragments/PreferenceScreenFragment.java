package com.bmarpc.acpsiam.bdictionarydev.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bmarpc.acpsiam.bdictionarydev.R;

import java.util.Locale;

import rikka.material.preference.MaterialSwitchPreference;

public class PreferenceScreenFragment extends PreferenceFragmentCompat {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Dialog dialog;

    Preference appColorPreference;




    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey);

        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.SHARED_PREFERENCES_APP_PROCESS), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        appColorPreference = findPreference(getString(R.string.COLOR_PREFERENCE));


        appColorPreference.setOnPreferenceClickListener(preference -> {
            viewCustomDialogForColorChange();
            return false;
        });
        if (sharedPreferences.getString(getString(R.string.SELECTED_THEME_COLOR), "blue").toLowerCase(Locale.ROOT).equals("purple")) {
            appColorPreference.setSummary(Html.fromHtml(getString(R.string.color_preference_summary) + " \"PURPLE\" " + getString(R.string.purple_message_string), Html.FROM_HTML_MODE_COMPACT));
        } else {
            appColorPreference.setSummary(getString(R.string.color_preference_summary) + " \"" + sharedPreferences.getString(getString(R.string.SELECTED_THEME_COLOR), "blue").toUpperCase() + "\"");
        }




















                


    }







    public void viewCustomDialogForColorChange() {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_color_preference);
        LinearLayout blue, cyan, purple, lime;
        blue = dialog.findViewById(R.id.color_pref_blue_id);
        cyan = dialog.findViewById(R.id.color_pref_cyan_id);
        purple = dialog.findViewById(R.id.color_pref_purple_id);
        lime = dialog.findViewById(R.id.color_pref_lime_id);


        blue.setOnClickListener(view -> setTheme(R.style.AppTheme, "blue"));

        cyan.setOnClickListener(view -> setTheme(R.style.Cyan_AppTheme, "cyan"));

        purple.setOnClickListener(view -> setTheme(R.style.Purple_AppTheme, "purple"));

        lime.setOnClickListener(view -> setTheme(R.style.Lime_AppTheme, "lime"));


        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public void setTheme(int theme, String color) {
        getActivity().setTheme(theme);
        editor.putString(getString(R.string.SELECTED_THEME_COLOR), color);
        editor.apply();
        getActivity().recreate();
        dialog.dismiss();
    }











}
