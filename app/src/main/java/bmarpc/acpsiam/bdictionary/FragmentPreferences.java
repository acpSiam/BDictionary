package bmarpc.acpsiam.bdictionary;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import rikka.material.preference.MaterialSwitchPreference;
import rikka.preference.SimpleMenuPreference;

public class FragmentPreferences extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        MaterialSwitchPreference adaptiveAppIconPref = findPreference(getString(R.string.adaptive_app_icon_pref));
        MaterialSwitchPreference nightModePref = findPreference(getString(R.string.night_mode_pref));
        SimpleMenuPreference appIconPref = findPreference(getString(R.string.app_icon_pref));
        Preference disclaimer = findPreference(getString(R.string.disclaimer_pref));
        Preference about = findPreference(getString(R.string.about_pref));




        nightModePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                if (!nightModePref.isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                return true;
            }
        });



        adaptiveAppIconPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                if (adaptiveAppIconPref.isChecked()){
                    appIconPref.setEnabled(false);
                }
                else {
                    appIconPref.setEnabled(true);
                }
                return true;
            }
        });





        disclaimer.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Disclaimer")
                        .setMessage("App is still in development process. We cannot guarantee yet that every information regarding the dictionary in the app is fully accurate. We are constantly trying to improve.  Thanks for using our app ❤")
                        .setIcon(R.drawable.ic_round_history_edu_24)
                        .setCancelable(true)
                        .show();
                return true;
            }
        });


        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("About")
                        .setMessage("Evaluation Version: \n App created with 🤍 for MD. Delower Sarker \n\n version: 1.0")
                        .setIcon(R.drawable.ic_round_info_24)
                        .setCancelable(true)
                        .show();
                return true;
            }
        });


    }





}
