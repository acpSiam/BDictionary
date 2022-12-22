package bmarpc.acpsiam.bdictionary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.elevation.SurfaceColors;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    public static final String APP_VERSION = "assad";
    public static final String UPDATE_PUSH_SERVER_LINK = "https://raw.githubusercontent.com/acpSiam/BDictionary/master/app/src/main/res/raw/push_updates.json";
    SharedPreferences sharedPreferences;
    SharedPreferences themeColorPreferences;
    SharedPreferences.Editor themeColorPreferencesEditor;
    SharedPreferences.Editor sharedPreferenceEditor;
    static BottomNavigationView bottomNavigationView;
    FrameLayout fragmentLayout;
    static DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialButton drawerButton, aboutButton;
    LottieAnimationView darkLightToggle;
    static LottieAnimationView loveLottie;


    DBHandler dbHandler;

    // File url to download
    String DOWNLOAD_URL = "https://github.com/acpSiam/BDictionary/releases/download/v1.0-alpha/BDictionary-v1.0-alpha.apk";
    private RequestQueue requestQueue;
    private String version, versionName, updateLink, updateTitle, updateDesc, updateMandatory, additionalText;
    final DownloadTask downloadTask = new DownloadTask(MainActivity.this);


    static TextView dialogUpdateTitle;
    static TextView dialogUpdateDesc;
    static TextView dialogUpdateVersion;
    static TextView dialogUpdateDownloadButtonLabel;
    static LottieAnimationView updateDiaalogDownloadLottie;
    static Button cancelUpdate;
    static MaterialCardView updateDialogDownloadButton;
    static LinearProgressIndicator progressIndicator;
    private Button dismissDialog;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeColorPreferences = getSharedPreferences(getString(R.string.color_preference_STR), MODE_PRIVATE);
        themeColorPreferencesEditor = themeColorPreferences.edit();
        String currentTheme = themeColorPreferences.getString(getString(R.string.selected_color_pref_STR), "blue");
        dbHandler = new DBHandler(MainActivity.this);


        if (nightTheme()) {
            if (currentTheme.equals("blue")) {
                setTheme(R.style.AppTheme_Dark);
            } else if (currentTheme.equals("purple")) {
                setTheme(R.style.Purple_AppTheme_Dark);
            } else if (currentTheme.equals("cyan")) {
                setTheme(R.style.Cyan_AppTheme_Dark);
            } else if (currentTheme.equals("lime")) {
                setTheme(R.style.Lime_AppTheme_Dark);
            }

        } else {
            if (currentTheme.equals("blue")) {
                setTheme(R.style.AppTheme);
            } else if (currentTheme.equals("purple")) {
                setTheme(R.style.Purple_AppTheme);
            } else if (currentTheme.equals("cyan")) {
                setTheme(R.style.Cyan_AppTheme);
            } else if (currentTheme.equals("lime")) {
                setTheme(R.style.Lime_AppTheme);
            }
        }

        setContentView(R.layout.activity_main);
        getWindow().setNavigationBarColor(SurfaceColors.SURFACE_2.getColor(this));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferenceEditor = sharedPreferences.edit();


//        pushAppUpdate();
//        if (!APP_VERSION.equals(version)){
//            viewCustomDialog();
//        }


        //*Finding IDs
        bottomNavigationView = findViewById(R.id.bottom_navigation_view_id);
        fragmentLayout = findViewById(R.id.fragment_layout_id);
        drawerLayout = findViewById(R.id.drawer_layout_id);
        navigationView = findViewById(R.id.navigation_view_id);
        drawerButton = findViewById(R.id.drawer_button_id);
        darkLightToggle = findViewById(R.id.dark_light_toggle_button_id);
        aboutButton = findViewById(R.id.about_button_id);
        loveLottie = findViewById(R.id.love_lottie_id);


        if (nightTheme()) {
            darkLightToggle.setProgress(1);
        } else {
            darkLightToggle.setProgress(0);
        }

        requestQueue = Volley.newRequestQueue(this);

        if (sharedPreferences.getBoolean(getString(R.string.auto_check_updates_pref), false)) {
            pushAppUpdate();
            if (!version.equals(APP_VERSION)) {
                Toast.makeText(this, "New update available. :-D", Toast.LENGTH_SHORT).show();
                viewCustomDialog();
            }
        }


        darkLightToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightTheme()) {
                    darkLightToggle.animate();
                    darkLightToggle.setSpeed(-1);
                    saveThemeState(false);
                    if (currentTheme.equals("blue")) {
                        setTheme(R.style.AppTheme_Dark);
                        recreate();
                    } else if (currentTheme.equals("purple")) {
                        setTheme(R.style.Purple_AppTheme_Dark);
                        recreate();
                    } else if (currentTheme.equals("cyan")) {
                        setTheme(R.style.Cyan_AppTheme_Dark);
                        recreate();
                    } else if (currentTheme.equals("lime")) {
                        setTheme(R.style.Lime_AppTheme_Dark);
                        recreate();
                    }
                } else {
                    darkLightToggle.animate();
                    darkLightToggle.setSpeed(1);
                    saveThemeState(true);
                    if (currentTheme.equals("blue")) {
                        setTheme(R.style.AppTheme);
                        recreate();
                    } else if (currentTheme.equals("purple")) {
                        setTheme(R.style.Purple_AppTheme);
                        recreate();
                    } else if (currentTheme.equals("cyan")) {
                        setTheme(R.style.Cyan_AppTheme);
                        recreate();
                    } else if (currentTheme.equals("lime")) {
                        setTheme(R.style.Lime_AppTheme);
                        recreate();
                    }
                }
            }
        });


        selectFragment(new FragmentTranslate(), true);
        drawerButton.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_translate);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.bottom_nav_library:
//                    darkLightToggle.setVisibility(View.GONE);
//                    aboutButton.setVisibility(View.GONE);
                    selectFragment(new FragmentLibrary(), false);
                    break;

                case R.id.bottom_nav_favourites:
//                    darkLightToggle.setVisibility(View.GONE);
//                    aboutButton.setVisibility(View.GONE);
                    selectFragment(new FragmentFavourites(), false);
                    break;


                case R.id.bottom_nav_translate:
//                    darkLightToggle.setVisibility(View.VISIBLE);
//                    aboutButton.setVisibility(View.GONE);
                    selectFragment(new FragmentTranslate(), true);
                    break;

                case R.id.bottom_nav_history:
//                    darkLightToggle.setVisibility(View.GONE);
//                    aboutButton.setVisibility(View.GONE);
                    selectFragment(new FragmentHistory(), false);
                    break;

                case R.id.bottom_nav_settings:
//                    darkLightToggle.setVisibility(View.GONE);
//                    aboutButton.setVisibility(View.VISIBLE);
                    selectFragment(new FragmentPreferences(), false);
                    break;
            }
            sharedPreferenceEditor.apply();
            return true;
        });


    }


    public void pushAppUpdate() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, UPDATE_PUSH_SERVER_LINK, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            version = response.getString("version");
                            versionName = response.getString("versionName");
                            updateLink = response.getString("updateLink");
                            updateTitle = response.getString("updateTitle");
                            updateDesc = response.getString("updateDesc");
                            updateMandatory = response.getString("updateMandatory");
                            additionalText = response.getString("additionalText");

                            Log.d("Volley", version + " " + versionName + " " + updateLink + " " + updateTitle + " " + updateDesc + " " + updateMandatory + " " + additionalText);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley", error.toString());
                    }
                });

        requestQueue.add(jsonObjectRequest);

    }


    public void selectFragment(Fragment fragment, boolean homeFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anime_fade_in, R.anim.anime_fade_out);
        fragmentTransaction.replace(R.id.fragment_layout_id, fragment);
        fragmentTransaction.commit();

        if (drawerLayout.isOpen()) {
            drawerLayout.close();
        }

        sharedPreferenceEditor.putBoolean(getString(R.string.home_frag_pref_BOOL), homeFragment);
        sharedPreferenceEditor.apply();

//        String currentFragment = sharedPreferences.getString(getString(R.string.current_frag_pref_STR), "translate");

//        if (currentFragment.equals("library") || currentFragment.equals("favourites") || currentFragment.equals("translate")
//                || currentFragment.equals("history") || currentFragment.equals("settings")){
//            bottomNavigationView.setVisibility(View.VISIBLE);
//        }else{
//            bottomNavigationView.setVisibility(View.GONE);
//        }

    }


    private boolean nightTheme() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.THEME_TOGGLE_SP_STR), MODE_PRIVATE);
        return sharedPreferences.getBoolean(getString(R.string.THEME_STATE_STR), true);
    }

    private void saveThemeState(Boolean state) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.THEME_TOGGLE_SP_STR), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.THEME_STATE_STR), state);
        editor.apply();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!sharedPreferences.getBoolean(getString(R.string.home_frag_pref_BOOL), true)) {
            selectFragment(new FragmentTranslate(), true);
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
            bottomNavigationView.setSelectedItemId(R.id.bottom_nav_translate);
        } else {
            super.onBackPressed();
            this.finishAffinity();
        }
    }


    @Override
    public void recreate() {
        this.finish();
        overridePendingTransition(R.anim.anime_fade_out,
                R.anim.anime_fade_in);
        startActivity(getIntent());
        overridePendingTransition(R.anim.anime_fade_in,
                R.anim.anime_fade_out);
    }


    public void viewCustomDialog() {

        DOWNLOAD_URL = updateLink;

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_update_notification);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialogUpdateTitle = dialog.findViewById(R.id.update_dialog_title_id);
        dialogUpdateDesc = dialog.findViewById(R.id.update_dialog_description_id);
        dialogUpdateVersion = dialog.findViewById(R.id.update_dialog_version_id);
        dialogUpdateDownloadButtonLabel = dialog.findViewById(R.id.update_dialog_download_label);
        updateDiaalogDownloadLottie = dialog.findViewById(R.id.update_dialog_download_lottie_id);
        updateDialogDownloadButton = dialog.findViewById(R.id.update_dialog_download_button_id);
        cancelUpdate = dialog.findViewById(R.id.cancelDownloadId);
        progressIndicator = dialog.findViewById(R.id.progressbarid);
        dismissDialog = dialog.findViewById(R.id.update_dialog_dismiss_button_id);


        dialogUpdateTitle.setText(updateTitle);
        dialogUpdateDesc.setText(updateDesc);
        dialogUpdateVersion.setText(updateDesc);


        updateDialogDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDiaalogDownloadLottie.setVisibility(View.VISIBLE);
                downloadTask.execute(DOWNLOAD_URL);
            }
        });


        cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadTask.cancel(true);
                updateDiaalogDownloadLottie.setVisibility(View.GONE);
            }
        });


        dismissDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            downloadTask.cancel(true);
        }
    }
}