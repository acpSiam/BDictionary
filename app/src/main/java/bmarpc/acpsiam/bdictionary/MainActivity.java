package bmarpc.acpsiam.bdictionary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.elevation.SurfaceColors;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String DB_NAME = "dictionary.db";
    public static ArrayList<String> autoCompleteTexts = new ArrayList<>();
    public static ArrayAdapter[] arrayAdapter;
    SharedPreferences sharedPreferences;
    BottomNavigationView bottomNavigationView;
    FrameLayout fragmentLayout;
    DBHandler dbHandler = new DBHandler(this);

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialButton drawerButton, aboutButton;
    LottieAnimationView darkLightToggle;


    static ArrayList<String> enWords;
    static ArrayList<String> bnWords; //For Library RecyclerView

    public static final int APP_VERSION = BuildConfig.VERSION_CODE;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setNavigationBarColor(SurfaceColors.SURFACE_2.getColor(this));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //*Finding IDs
        bottomNavigationView = findViewById(R.id.bottom_navigation_view_id);
        fragmentLayout = findViewById(R.id.fragment_layout_id);
        drawerLayout = findViewById(R.id.drawer_layout_id);
        navigationView = findViewById(R.id.navigation_view_id);
        drawerButton = findViewById(R.id.drawer_button_id);
        darkLightToggle = findViewById(R.id.dark_light_toggle_button_id);
        aboutButton = findViewById(R.id.about_button_id);



        if (!doesDatabaseExist()) {
            try {
                copyDataBase();
            } catch (IOException e) {
                Toast.makeText(this, "Error Copying", Toast.LENGTH_SHORT).show();
            }
        }

        if (nightTheme()){
            darkLightToggle.setProgress(1);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            darkLightToggle.setProgress(0);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        darkLightToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nightTheme()){
                    darkLightToggle.animate();
                    darkLightToggle.setSpeed(-1);
                    saveThemeState(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    darkLightToggle.animate();
                    darkLightToggle.setSpeed(1);
                    saveThemeState(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });


        setAutCompleteTexts();
        selectFragment(new FragmentTranslate());



        drawerButton.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_translate);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.bottom_nav_library:
                    darkLightToggle.setVisibility(View.GONE);
                    aboutButton.setVisibility(View.VISIBLE);
                    selectFragment(new FragmentLibrary());
                    break;

                case R.id.bottom_nav_favourites:
                    selectFragment(new FragmentTranslate());
                    break;


                case R.id.bottom_nav_translate:
                    darkLightToggle.setVisibility(View.VISIBLE);
                    aboutButton.setVisibility(View.GONE);
                    selectFragment(new FragmentTranslate());
                    break;

                case R.id.bottom_nav_history:
                    selectFragment(new FragmentTranslate());
                    break;

                case R.id.bottom_nav_settings:
                    darkLightToggle.setVisibility(View.GONE);
                    aboutButton.setVisibility(View.VISIBLE);
                    selectFragment(new FragmentPreferences());
                    break;
            }
            return true;
        });




    }





    public void selectFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anime_fade_in, R.anim.anime_fade_out);
        fragmentTransaction.replace(R.id.fragment_layout_id, fragment);
        fragmentTransaction.commit();

        if (drawerLayout.isOpen()) {
            drawerLayout.close();
        }

    }


    private void copyDataBase() throws IOException {
        InputStream myInput = MainActivity.this.getAssets().open(MainActivity.DB_NAME);
        File outFileName = MainActivity.this.getDatabasePath(MainActivity.DB_NAME);
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        Toast.makeText(this, "Dictionary set up to use", Toast.LENGTH_SHORT).show();
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    private boolean doesDatabaseExist() {
        File dbFile = this.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }


    private void setAutCompleteTexts() {
        Cursor cursor = dbHandler.fetchEnWordsDatabase();
        while (cursor.moveToNext()) {
            if(cursor.getString(0).trim().equals("")){
                continue;
            }
            autoCompleteTexts.add(cursor.getString(0));
        }
        arrayAdapter = new ArrayAdapter[]{new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteTexts)};
    }



    private boolean nightTheme(){
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.THEME_TOGGLE_SP_STR), MODE_PRIVATE);
        return sharedPreferences.getBoolean(getString(R.string.THEME_STATE_STR), true);
    }

    private void saveThemeState(Boolean state){
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.THEME_TOGGLE_SP_STR), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.THEME_STATE_STR), state);
        editor.apply();
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            this.finishAffinity();
        }
    }


    @Override
    public void recreate() {
        this.finish();
        overridePendingTransition(R.anim.anime_fade_in,
                R.anim.anime_fade_out);
        startActivity(getIntent());
        overridePendingTransition(R.anim.anime_fade_in,
                R.anim.anime_fade_out);

    }





    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Toast.makeText(this, "onResumeFragments", Toast.LENGTH_SHORT).show();

        enWords = new ArrayList<>();
        bnWords = new ArrayList<>();

        Cursor cursor = dbHandler.fetchAllWordsDatabase();
        while (cursor.moveToNext()){
            enWords.add(cursor.getString(1));
            bnWords.add(cursor.getString(2));
        }
    }
}