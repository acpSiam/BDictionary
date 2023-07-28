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
        if (sharedPreferences.getString(getString(R.string.SELECTED_THEME_COLOR),
                getString(R.string.PREFERRED_COLOR_BLUE)).equals(getString(R.string.PREFERRED_COLOR_PURPLE))) {
            appColorPreference.setSummary(Html.fromHtml(getString(R.string.color_preference_summary) + " \""
                    + getString(R.string.PREFERRED_COLOR_PURPLE)
                    +"\" " + getString(R.string.purple_message_string), Html.FROM_HTML_MODE_COMPACT));
        } else {
            appColorPreference.setSummary(getString(R.string.color_preference_summary) + " \"" + sharedPreferences.getString(getString(R.string.SELECTED_THEME_COLOR),
                    getString(R.string.PREFERRED_COLOR_BLUE)).replace("_", " ").toUpperCase() + "\"");
        }




















                


    }







    public void viewCustomDialogForColorChange() {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_color_preference);


        LinearLayout blue, cyan, purple, lime, mintGreen, coral, goldenYellow, violet, indigo, orange, red;


        blue = dialog.findViewById(R.id.color_pref_blue_id);
        cyan = dialog.findViewById(R.id.color_pref_cyan_id);
        purple = dialog.findViewById(R.id.color_pref_purple_id);
        lime = dialog.findViewById(R.id.color_pref_lime_id);
        mintGreen = dialog.findViewById(R.id.color_pref_mint_green_id);
        coral = dialog.findViewById(R.id.color_pref_coral_id);
        goldenYellow = dialog.findViewById(R.id.color_pref_golden_yellow_id);
        violet = dialog.findViewById(R.id.color_pref_violet_id);
        indigo = dialog.findViewById(R.id.color_pref_indigo_id);
        orange = dialog.findViewById(R.id.color_pref_orange_id);
        red = dialog.findViewById(R.id.color_pref_red_id);


        cyan.setOnClickListener(view -> setTheme(getString(R.string.PREFERRED_COLOR_CYAN)));
        purple.setOnClickListener(view -> setTheme(getString(R.string.PREFERRED_COLOR_PURPLE)));
        lime.setOnClickListener(view -> setTheme(getString(R.string.PREFERRED_COLOR_LIME)));
        coral.setOnClickListener(view -> setTheme(getString(R.string.PREFERRED_COLOR_CORAL)));
        violet.setOnClickListener(view -> setTheme(getString(R.string.PREFERRED_COLOR_VIOLET)));
        indigo.setOnClickListener(view -> setTheme(getString(R.string.PREFERRED_COLOR_INDIGO)));
        blue.setOnClickListener(view -> setTheme(getString(R.string.PREFERRED_COLOR_BLUE)));
        mintGreen.setOnClickListener(view -> setTheme(getString(R.string.PREFERRED_COLOR_GREEN)));
        goldenYellow.setOnClickListener(view -> setTheme(getString(R.string.PREFERRED_COLOR_YELLOW)));
        orange.setOnClickListener(view -> setTheme(getString(R.string.PREFERRED_COLOR_ORANGE)));
        red.setOnClickListener(view -> setTheme(getString(R.string.PREFERRED_COLOR_RED)));



        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public void setTheme(String color) {
        editor.putString(getString(R.string.SELECTED_THEME_COLOR), color);
        editor.apply();
        getActivity().recreate();
        dialog.dismiss();
    }











}
