package bmarpc.acpsiam.bdictionary;

import static bmarpc.acpsiam.bdictionary.MainActivity.drawerLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import rikka.material.preference.MaterialSwitchPreference;
import rikka.preference.SimpleMenuPreference;

public class FragmentPreferences extends PreferenceFragmentCompat {

    MainActivity mainActivity = new MainActivity();
    MyMethods myMethods = new MyMethods();


    MaterialSwitchPreference adaptiveAppIconPref;
    SimpleMenuPreference appIconPref;


    MaterialSwitchPreference autoCheckUpdates;
    MaterialSwitchPreference updateAutomatically;
    Preference checkForUpdates;
    Preference rating;

    Preference disclaimer;
    Preference about;
    Preference color;
    Preference share;
    Preference contribute;
    Preference copyright;
    Preference contactDev;
    Preference reportBug;



    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference bugReportRef = firebaseDatabase.getReference().child("BUG_REPORTS");

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        sharedPreferences = requireActivity().getSharedPreferences(getString(R.string.color_preference_STR), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        adaptiveAppIconPref = findPreference(getString(R.string.adaptive_app_icon_pref));
        appIconPref = findPreference(getString(R.string.app_icon_pref));


        autoCheckUpdates = findPreference(getString(R.string.auto_check_updates_pref));
        updateAutomatically = findPreference(getString(R.string.auto_update_app_pref));
        checkForUpdates = findPreference(getString(R.string.check_for_update_pref));


        disclaimer = findPreference(getString(R.string.disclaimer_pref));
        about = findPreference(getString(R.string.about_pref));
        color = findPreference(getString(R.string.color_pref));
        share = findPreference(getString(R.string.share_pref));
        rating = findPreference(getString(R.string.rating_pref));
        contribute = findPreference(getString(R.string.contribute_pref));
        copyright = findPreference(getString(R.string.copyright_pref));
        contactDev = findPreference(getString(R.string.contact_dev_pref));

        reportBug = findPreference(getString(R.string.bug_report_pref));



        color.setOnPreferenceClickListener(preference -> {
            viewCustomDialogForColorChange();
            return false;
        });
        if (sharedPreferences.getString(getString(R.string.selected_color_pref_STR), "blue").toLowerCase(Locale.ROOT).equals("purple")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                color.setSummary(Html.fromHtml("Prefered color: \"PURPLE\" <small>for BTS lovers 🥴</small>",Html.FROM_HTML_MODE_COMPACT));
            }
        } else {
            color.setSummary("Prefered color \"" + sharedPreferences.getString(getString(R.string.selected_color_pref_STR), "blue").toUpperCase() + "\"");
        }




        adaptiveAppIconPref.setOnPreferenceChangeListener((preference, newValue) -> {
            appIconPref.setEnabled(!Boolean.parseBoolean(newValue.toString()));
            return true;
        });





        autoCheckUpdates.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals(true)) {
                mainActivity.pushAppUpdate();
            }
            return true;
        });








        reportBug.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                viewCustomDialogForBugReport();
                return false;
            }
        });





        share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                myMethods.shareApp(requireActivity(), "https://github.com/acpSiam/BDictionary/releases", "Download BDictionary... An open source Bangla Dictionary for all. --> acpSiam");
                return false;
            }
        });














        if (sharedPreferences.getString(getString(R.string.contribute_done_str), "").equals("done")){
            contribute.setSummary(Html.fromHtml("<strike>" +getString(R.string.contribute_pref_summary)+ "</strike> "
                    + getString(R.string.contribute_pref_summary_2)));
            contribute.setEnabled(false);
            rating.setVisible(true);
        }
        contribute.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                preference.setSummary(Html.fromHtml("<strike>" +getString(R.string.contribute_pref_summary)+ "</strike> "
                        + getString(R.string.contribute_pref_summary_2)));
                preference.setEnabled(false);
                MainActivity.loveLottie.setVisibility(View.VISIBLE);
                MainActivity.loveLottie.playAnimation();
                MainActivity.loveLottie.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        MainActivity.loveLottie.setVisibility(View.GONE);
                        MainActivity.loveLottie.setVisibility(View.GONE);
                        rating.setVisible(true);
                        viewCustomDialogForRating();
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });

                editor.putString(getString(R.string.contribute_done_str),"done");
                editor.apply();
                return false;
            }
        });







        rating.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                viewCustomDialogForRating();
                return false;
            }
        });







        contactDev.setOnPreferenceClickListener(preference -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.anime_fade_in, R.anim.anime_fade_out);
            fragmentTransaction.replace(R.id.fragment_layout_id, new FragmentDev());
            fragmentTransaction.commit();

            MainActivity.bottomNavigationView.animate()
                    .alpha(0.0f)
                    .setDuration(500)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            MainActivity.bottomNavigationView.setVisibility(View.GONE);
                        }
                    })
                    .start();

            return false;
        });


        copyright.setOnPreferenceClickListener(preference -> {
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Copyright")
                    .setMessage(R.string.copyright_pref)
                    .setIcon(R.drawable.ic_round_copyright_24)
                    .setCancelable(true)
                    .show();
            return true;
        });


        disclaimer.setOnPreferenceClickListener(preference -> {
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Disclaimer")
                    .setMessage(R.string.disclaimer_text)
                    .setIcon(R.drawable.ic_round_history_edu_24)
                    .setCancelable(true)
                    .show();
            return true;
        });


        about.setOnPreferenceClickListener(preference -> {
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("About")
                    .setMessage(R.string.about_text)
                    .setIcon(R.drawable.ic_round_info_24)
                    .setCancelable(true)
                    .show();
            return true;
        });


    }


    public void viewCustomDialogForColorChange() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_color_preference);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LinearLayout blue, cyan, purple, lime;
        blue = dialog.findViewById(R.id.color_pref_blue_id);
        cyan = dialog.findViewById(R.id.color_pref_cyan_id);
        purple = dialog.findViewById(R.id.color_pref_purple_id);
        lime = dialog.findViewById(R.id.color_pref_lime_id);


        blue.setOnClickListener(view -> {
            setTheme(R.style.AppTheme, "blue");
        });

        cyan.setOnClickListener(view -> {
            setTheme(R.style.Cyan_AppTheme, "cyan");
        });

        purple.setOnClickListener(view -> {
            setTheme(R.style.Purple_AppTheme, "purple");
        });

        lime.setOnClickListener(view -> {
            setTheme(R.style.Lime_AppTheme, "lime");
        });


        dialog.show();
        dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

    }

    public void viewCustomDialogForRating() {


        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_rate_app);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RatingBar ratingBar;
        TextInputEditText userThought;
        Button dismissButton, sendButton;

        ratingBar = dialog.findViewById(R.id.rating_bar_id);
        userThought = dialog.findViewById(R.id.user_thought_rating_id);
        dismissButton = dialog.findViewById(R.id.dismiss_button_rating_dialog_id);
        sendButton = dialog.findViewById(R.id.positive_button_rating_dialog_id);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                switch ((int) v){
                    case 1:
                        userThought.setText("Work more. ");
                        break;
                    case 2:
                        userThought.setText("Needs improvement. ");
                        break;
                    case 3:
                        userThought.setText("Good job. ");
                        break;
                    case 4:
                        userThought.setText("Excellent job. ");
                        break;
                    case 5:
                        userThought.setText("Couldnt be any better. ");
                        break;
                }
            }
        });


        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        dialog.show();
        dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

    }



    public void viewCustomDialogForBugReport() {


        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_bug_report);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextInputEditText bugReportMessage, bugReportName, bugReportEmail;
        Button dismissButton, sendButton;
        LinearLayout mainLayout;
        LottieAnimationView noInternetLottie;


        mainLayout = dialog.findViewById(R.id.bug_report_main_layout_id);
        noInternetLottie = dialog.findViewById(R.id.bug_report_no_internet);
        bugReportMessage = dialog.findViewById(R.id.bug_report_message_id);
        bugReportName = dialog.findViewById(R.id.bug_report_name_id);
        bugReportEmail = dialog.findViewById(R.id.bug_report_email_id);
        dismissButton = dialog.findViewById(R.id.dismiss_button_bug_report_dialog_id);
        sendButton = dialog.findViewById(R.id.positive_button_bug_report_dialog_id);


        if (!myMethods.isNetworkConnected(getActivity()) && !myMethods.isInternetAvailable()){
            noInternetLottie.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
            dialog.setCancelable(true);
        } else {
            noInternetLottie.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
            dialog.setCancelable(false);
        }


        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!bugReportName.getText().toString().trim().equals("" ) 
                        && !bugReportEmail.getText().toString().trim().equals("" ) 
                        && !bugReportMessage.getText().toString().trim().equals("" ) ){
                    HashMap<String, String> hashMap = new HashMap();
                    hashMap.put("bug_report_user_name", bugReportName.getText().toString().trim());
                    hashMap.put("bug_report_email", bugReportEmail.getText().toString().trim());
                    hashMap.put("bug_report_message", bugReportMessage.getText().toString().trim());
                    hashMap.put("bug_report_date", Calendar.getInstance().getTime().toString());
                    hashMap.put("bug_report_device", Build.MANUFACTURER + " "  + Build.DEVICE   + " [" + Build.MODEL + "] : " +  Build.VERSION.SDK_INT  + "  Android " + Build.VERSION.RELEASE);

                    bugReportRef.child(String.valueOf(System.currentTimeMillis())).setValue(hashMap);
                    Toast.makeText(getActivity(), "Bug report sent. We will contact you soon regarding the bug", Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                } else {
                    Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
               
            }
        });

        dialog.onWindowFocusChanged(false);
        dialog.show();
        dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

    }


    public void setTheme(int theme, String color){
        getActivity().setTheme(theme);
        editor.putString(getString(R.string.selected_color_pref_STR), color);
        editor.apply();
        getActivity().recreate();
    }


}
