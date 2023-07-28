package com.bmarpc.acpsiam.bdictionarydev.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.bmarpc.acpsiam.bdictionarydev.R;
import com.bmarpc.acpsiam.bdictionarydev.fragments.DeveloperContactFragment;
import com.bmarpc.acpsiam.bdictionarydev.fragments.FavouritesFragment;
import com.bmarpc.acpsiam.bdictionarydev.fragments.HistoryFragment;
import com.bmarpc.acpsiam.bdictionarydev.fragments.IdiomsFragment;
import com.bmarpc.acpsiam.bdictionarydev.fragments.PreferenceScreenFragment;
import com.bmarpc.acpsiam.bdictionarydev.fragments.PrepositionsFragment;
import com.bmarpc.acpsiam.bdictionarydev.fragments.ProverbsFragment;
import com.bmarpc.acpsiam.bdictionarydev.fragments.QuizFragment;
import com.bmarpc.acpsiam.bdictionarydev.fragments.TranslationFragment;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelper;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelperIdioms;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelperPrepositions;
import com.bmarpc.acpsiam.bdictionarydev.helpers.DBHelperProverbs;
import com.bmarpc.acpsiam.bdictionarydev.helpers.LanguageHelper;
import com.bmarpc.acpsiam.bdictionarydev.otherclasses.LibraryModel;
import com.bmarpc.acpsiam.bdictionarydev.otherclasses.MyMethods;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferenceEditor;
    static BottomNavigationView bottomNavigationView;
    FrameLayout fragmentLayout;
    static DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialButton drawerButton, aboutButton;
    LottieAnimationView darkLightToggle;
    static LottieAnimationView loveLottie;


//    MaterialButton sideNavSignIn, sideNavSignUp, sideNavSignOut;
    TextView sideNavTextViewTitle, sideNavTextViewAppVersion, activityHeaderTextview;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //*Set the language based on user's previous preference
        LanguageHelper.setLocale(this, LanguageHelper.getCurrentLocalePref(this));


        //*Initializing important Shared Preferences
        sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFERENCES_APP_PROCESS), MODE_PRIVATE);
        sharedPreferenceEditor = sharedPreferences.edit();

        //Currently chosen Theme by user
        String currentTheme = sharedPreferences.getString(getString(R.string.SELECTED_THEME_COLOR),
                getString(R.string.PREFERRED_COLOR_BLUE));

        // Check if it's a night theme
        if (nightTheme()) {
            // Night theme is enabled
            // Set the appropriate theme based on the current selected theme
            switch (currentTheme) {
                case "CYAN":
                    setTheme(R.style.Cyan_AppTheme_Dark); // Apply the dark version of the "CYAN" theme
                    break;
                case "PURPLE":
                    setTheme(R.style.Purple_AppTheme_Dark); // Apply the dark version of the "PURPLE" theme
                    break;
                case "LIME":
                    setTheme(R.style.Lime_AppTheme_Dark); // Apply the dark version of the "LIME" theme
                    break;
                case "CORAL":
                    setTheme(R.style.Coral_AppTheme_Dark); // Apply the dark version of the "CORAL" theme
                    break;
                case "VIOLET":
                    setTheme(R.style.Violet_AppTheme_Dark); // Apply the dark version of the "VIOLET" theme
                    break;
                case "INDIGO":
                    setTheme(R.style.Indigo_AppTheme_Dark); // Apply the dark version of the "INDIGO" theme
                    break;
                case "MINT_GREEN":
                    setTheme(R.style.Mint_Green_AppTheme_Dark); // Apply the dark version of the "GREEN" theme
                    break;
                case "GOLDEN_YELLOW":
                    setTheme(R.style.Golden_Yellow_AppTheme_Dark); // Apply the dark version of the "YELLOW" theme
                    break;
                case "ORANGE":
                    setTheme(R.style.Orange_AppTheme_Dark); // Apply the dark version of the "ORANGE" theme
                    break;
                case "RED":
                    setTheme(R.style.Red_AppTheme_Dark); // Apply the dark version of the "RED" theme
                    break;
                default:
                    // Default theme if the color name is not recognized
                    setTheme(R.style.Blue_AppTheme_Dark);
                    break;

            }
        } else {
            // Night theme is not enabled (day theme)
            // Set the appropriate theme based on the current selected theme
            switch (currentTheme) {
                case "CYAN":
                    setTheme(R.style.Cyan_AppTheme); // Apply the dark version of the "CYAN" theme
                    break;
                case "PURPLE":
                    setTheme(R.style.Purple_AppTheme); // Apply the dark version of the "PURPLE" theme
                    break;
                case "LIME":
                    setTheme(R.style.Lime_AppTheme); // Apply the dark version of the "LIME" theme
                    break;
                case "CORAL":
                    setTheme(R.style.Coral_AppTheme); // Apply the dark version of the "CORAL" theme
                    break;
                case "VIOLET":
                    setTheme(R.style.Violet_AppTheme); // Apply the dark version of the "VIOLET" theme
                    break;
                case "INDIGO":
                    setTheme(R.style.Indigo_AppTheme); // Apply the dark version of the "INDIGO" theme
                    break;
                case "MINT_GREEN":
                    setTheme(R.style.Mint_Green_AppTheme); // Apply the dark version of the "GREEN" theme
                    break;
                case "GOLDEN_YELLOW":
                    setTheme(R.style.Golden_Yellow_AppTheme); // Apply the dark version of the "YELLOW" theme
                    break;
                case "ORANGE":
                    setTheme(R.style.Orange_AppTheme); // Apply the dark version of the "ORANGE" theme
                    break;
                case "RED":
                    setTheme(R.style.Red_AppTheme); // Apply the dark version of the "RED" theme
                    break;
                default:
                    // Default theme if the color name is not recognized
                    setTheme(R.style.Blue_AppTheme);
                    break;

            }
        }



        MyMethods.makeStatusBarTransparent(MainActivity.this); //*Decorating the status bar
        setContentView(R.layout.activity_main);

        boolean dictionaryLoaded = sharedPreferences.getBoolean(getString(R.string.DICTIONARY_ADAPTERS_LOADED_BOOLEAN), false);
        if (!dictionaryLoaded){
            // Execute the background task
            bgTask();
        }



        //*Finding IDs
        activityHeaderTextview = findViewById(R.id.activity_header_title_text_view_id);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view_id);
        fragmentLayout = findViewById(R.id.fragment_layout_id);
        drawerLayout = findViewById(R.id.drawer_layout_id);
        navigationView = findViewById(R.id.side_navigation_view_id);
        drawerButton = findViewById(R.id.drawer_button_id);
        darkLightToggle = findViewById(R.id.dark_light_toggle_button_id);
        aboutButton = findViewById(R.id.about_button_id);
        loveLottie = findViewById(R.id.love_lottie_id);


        //*Not Implemented Methods Yet
        //ToDo: Implement Methods for user authentication
        /*
        sideNavSignIn = navigationView.getHeaderView(0).findViewById(R.id.side_nav_button_sign_in);
        sideNavSignUp = navigationView.getHeaderView(0).findViewById(R.id.side_nav_button_sign_up);
        sideNavSignOut = navigationView.getHeaderView(0).findViewById(R.id.side_nav_button_sign_out);
        */

        sideNavTextViewTitle = navigationView.getHeaderView(0).findViewById(R.id.side_nav_title_textview_id);
        sideNavTextViewAppVersion = navigationView.getHeaderView(0).findViewById(R.id.side_nav_app_version_textview_id);

        // Generate a random number between 0 and 3 (inclusive)
        // Just an easter egg to show the version number
        int randomNumber = new Random().nextInt(3);
        if (randomNumber == 0) {
            // Show the TextView
            sideNavTextViewAppVersion.setVisibility(View.VISIBLE);
        } else {
            // Hide the TextView
            sideNavTextViewAppVersion.setVisibility(View.GONE);
        }

        sideNavTextViewTitle.setOnLongClickListener(v -> {
            sideNavTextViewAppVersion.setVisibility(View.VISIBLE);

            // Hide it after 4 seconds
            new Handler().postDelayed(() -> sideNavTextViewAppVersion.setVisibility(View.GONE), 4000);

            return true;
        });


        //Getting the app's version name and code
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        String versionName = packageInfo.versionName;
        int versionCode = packageInfo.versionCode;
        sideNavTextViewAppVersion.setText("v" + versionCode + " - " + versionName);


        //*Theme preferences
        darkLightToggle.setProgress(nightTheme() ? 1 : 0); //*Set theme according to users previous preference
        darkLightToggle.setOnClickListener(view -> {
            darkLightToggle.animate();
            darkLightToggle.setSpeed(nightTheme() ? -1 : 1);
            saveThemeState(!nightTheme());

            int themeResId = 0;
            switch (currentTheme) {
                case "CYAN":
                    themeResId = nightTheme() ? R.style.Cyan_AppTheme_Dark : R.style.Cyan_AppTheme;
                    break;
                case "PURPLE":
                    themeResId = nightTheme() ? R.style.Purple_AppTheme_Dark : R.style.Purple_AppTheme;
                    break;
                case "LIME":
                    themeResId = nightTheme() ? R.style.Lime_AppTheme_Dark : R.style.Lime_AppTheme;
                    break;
                case "CORAL":
                    themeResId = nightTheme() ? R.style.Coral_AppTheme_Dark : R.style.Coral_AppTheme;
                    break;
                case "VIOLET":
                    themeResId = nightTheme() ? R.style.Violet_AppTheme_Dark : R.style.Violet_AppTheme;
                    break;
                case "INDIGO":
                    themeResId = nightTheme() ? R.style.Indigo_AppTheme_Dark : R.style.Indigo_AppTheme;
                    break;
                case "MINT_GREEN":
                    themeResId = nightTheme() ? R.style.Mint_Green_AppTheme_Dark : R.style.Mint_Green_AppTheme;
                    break;
                case "GOLDEN_YELLOW":
                    themeResId = nightTheme() ? R.style.Golden_Yellow_AppTheme_Dark : R.style.Golden_Yellow_AppTheme;
                    break;
                case "ORANGE":
                    themeResId = nightTheme() ? R.style.Orange_AppTheme_Dark : R.style.Orange_AppTheme;
                    break;
                case "RED":
                    themeResId = nightTheme() ? R.style.Red_AppTheme_Dark : R.style.Red_AppTheme;
                    break;
                default:
                    // Default theme if the color name is not recognized
                    themeResId = nightTheme() ? R.style.Blue_AppTheme_Dark : R.style.Blue_AppTheme;
                    break;
            }
            if (themeResId != 0) {
                setTheme(themeResId);
                //*Recreate/Restart the activity to make the theme change effect take place
                MyMethods.restartActivityLongTransition(MainActivity.this);
            }
        });


        selectFragment(new TranslationFragment(), true, "BDictionary"); //*Set Translation as the default fragment
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_translate); //*Set Translation menu as the default item selected
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_nav_favourites) {
                selectFragment(new FavouritesFragment(), true, "BDictionary");
            } else if (itemId == R.id.bottom_nav_translate) {
                selectFragment(new TranslationFragment(), true, "BDictionary");
            } else if (itemId == R.id.bottom_nav_history) {
                selectFragment(new HistoryFragment(), true, "BDictionary");
            }
            sharedPreferenceEditor.apply();
            return true;
        });




        drawerButton.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START)); //*Open the side navigation drawer

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_drawer_home_id) {
                selectFragment(new TranslationFragment(), true, "BDictionary");
            } else if (itemId == R.id.nav_drawer_idioms_id) {
                selectFragment(new IdiomsFragment(), false, getString(R.string.nav_drawer_idioms));
            } else if (itemId == R.id.nav_drawer_proverbs_id) {
                selectFragment(new ProverbsFragment(), false, getString(R.string.nav_drawer_proverbs));
            } else if (itemId == R.id.nav_drawer_prepositions_id) {
                selectFragment(new PrepositionsFragment(), false, getString(R.string.nav_drawer_prepositions));
            } else if (itemId == R.id.nav_drawer_developer_info_id) {
                selectFragment(new DeveloperContactFragment(), false, getString(R.string.nav_drawer_developer_info));
            } else if (itemId == R.id.nav_drawer_settings_id) {
                selectFragment(new PreferenceScreenFragment(), false, getString(R.string.nav_drawer_settings));
            } else if (itemId == R.id.nav_drawer_copyright_id) {
                MyMethods.simpleMaterialDialogue(this, R.drawable.ic_round_copyright_24, R.string.copyright_notice, R.string.copyright_notice_app_dialog);
            } else if (itemId == R.id.nav_drawer_disclaimer_id) {
                MyMethods.simpleMaterialDialogue(this, R.drawable.ic_round_history_edu_24, R.string.nav_drawer_disclaimer, R.string.disclaimer_app_dialog);
            } else if (itemId == R.id.nav_drawer_about_id) {
                MyMethods.simpleMaterialDialogue(this, R.drawable.img_dev_transparent, R.string.nav_drawer_about, R.string.dev_info);
            } else if (itemId == R.id.nav_drawer_quiz_id) {
                selectFragment(new QuizFragment(), false, getString(R.string.nav_drawer_quiz));
            }
            return true;
        });


    }




    public void selectFragment(Fragment fragment, boolean homeFragment, String title) {

        activityHeaderTextview.setText(title);

        // Close the drawer if it is open
        if (drawerLayout.isOpen()) {
            drawerLayout.close();
        }




        findViewById(R.id.dictionary_load_progress_bar_id).setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            findViewById(R.id.dictionary_load_progress_bar_id).setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Set custom animations for fragment transition
            fragmentTransaction.setCustomAnimations(R.anim.anime_fade_in, R.anim.anime_fade_out);

            // Replace the current fragment with the new fragment
            fragmentTransaction.replace(R.id.fragment_layout_id, fragment);
            fragmentTransaction.commit();
        }, 250);







        // Hide the BottomNavigationView if it's not the home fragment
        if (!homeFragment) {
            bottomNavigationView.animate()
                    .alpha(0.0f)
                    .setDuration(200)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            bottomNavigationView.setVisibility(View.GONE);
                        }
                    })
                    .start();
        } else {
            bottomNavigationView.animate()
                    .alpha(1.0f)
                    .setDuration(200)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    })
                    .start();
        }


        // Store the boolean value indicating if the selected fragment is the home fragment
        sharedPreferenceEditor.putBoolean(getString(R.string.HOME_FRAGMENT_BOOLEAN), homeFragment);
        sharedPreferenceEditor.apply();
    }




    private boolean nightTheme() {
        // Retrieve the current theme state from shared preferences
        return sharedPreferences.getBoolean(getString(R.string.THEME_STATE), true);
    }

    private void saveThemeState(Boolean state) {
        // Save the theme state to shared preferences
        sharedPreferenceEditor.putBoolean(getString(R.string.THEME_STATE), state);
        sharedPreferenceEditor.apply();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // If the drawer is open, close it
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!sharedPreferences.getBoolean(getString(R.string.HOME_FRAGMENT_BOOLEAN), true)) {
            // If the current fragment is not the home fragment, navigate to the home fragment
            selectFragment(new TranslationFragment(), true, "BDictionary");

            // Animate and show the bottom navigation view
            bottomNavigationView.animate()
                    .alpha(1.0f)
                    .setDuration(200)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    })
                    .start();

            // Set the selected item in the bottom navigation view to the home fragment
            bottomNavigationView.setSelectedItemId(R.id.bottom_nav_translate);
        } else {
            // If the current fragment is the home fragment, perform the default back button behavior
            super.onBackPressed();

            // Finish all activities and exit the app
            ActivityCompat.finishAffinity(this);
            finishAndRemoveTask();
            System.exit(0);
        }
    }



    private void bgTask() {
        findViewById(R.id.dictionary_load_progress_bar_id).setVisibility(View.VISIBLE);

        new Thread(() -> {
            DBHelper dbHelper = new DBHelper(this);
            DBHelperIdioms dbHelperIdioms = new DBHelperIdioms(this);
            DBHelperProverbs dbHelperProverbs = new DBHelperProverbs(this);
            DBHelperPrepositions dbHelperPrepositions = new DBHelperPrepositions(this);

            LinkedHashSet<String> enWordsUniques = new LinkedHashSet<>();
            ArrayList<String> idiomsArrayList = new ArrayList<>();
            ArrayList<String> proverbsArrayList = new ArrayList<>();
            ArrayList<String> prepositionsArrayList = new ArrayList<>();

            // Retrieve data from the database
            Cursor cursor = dbHelper.fetchEnWordsDatabase();
            Cursor idiomCursor = dbHelperIdioms.fetchEnIdiomsDatabase();
            Cursor proverbsCursor = dbHelperProverbs.fetchEnBnProverbsDatabase();
            Cursor prepositionsCursor = dbHelperPrepositions.fetchEnPrepositionsDatabase();

            // Process data and extract unique words
            while (cursor.moveToNext()) {
                enWordsUniques.add(cursor.getString(0));
            }
            while (idiomCursor.moveToNext()) {
//                String idiom = idiomCursor.getString(0) + " (IDIOM)";
                String idiom = idiomCursor.getString(0);
                enWordsUniques.add(idiom);
                idiomsArrayList.add(idiom);
            }
            while (proverbsCursor.moveToNext()) {
//                String proverbs = proverbsCursor.getString(0) + " (PROVERB)";
                String proverbs = proverbsCursor.getString(0);
                enWordsUniques.add(proverbs);
                proverbsArrayList.add(proverbs);
            }
            while (prepositionsCursor.moveToNext()) {
//                String prepositions = prepositionsCursor.getString(0) + " (PREPOSITION)";
                String prepositions = prepositionsCursor.getString(0);
                enWordsUniques.add(prepositions);
                prepositionsArrayList.add(prepositions);
            }

            // Close the cursors
            cursor.close();
            idiomCursor.close();
            proverbsCursor.close();
            prepositionsCursor.close();

            // Convert the set of unique words to an ArrayList
            ArrayList<String> enWords = new ArrayList<>(enWordsUniques);

            LibraryModel libraryModel = new LibraryModel();
            libraryModel.setAllEnWordsArrayList(enWords);
            libraryModel.setIdiomsArrayList(idiomsArrayList);
            libraryModel.setProverbsArrayList(proverbsArrayList);
            libraryModel.setPrepositionsArrayList(prepositionsArrayList);

            sharedPreferenceEditor.putBoolean(getString(R.string.DICTIONARY_ADAPTERS_LOADED_BOOLEAN), true);

            // Perform UI operations on the main thread once the background process is done
            runOnUiThread(() -> {
                // Display a toast indicating the dictionary is loaded
                Toast.makeText(MainActivity.this, R.string.dictionary_loaded, Toast.LENGTH_SHORT).show();

                // Hide the dictionary load progress bar
                findViewById(R.id.dictionary_load_progress_bar_id).setVisibility(View.GONE);

                // Select the translation fragment as the active fragment
                selectFragment(new TranslationFragment(), true, "BDictionary");
            });
        }).start();
    }

}