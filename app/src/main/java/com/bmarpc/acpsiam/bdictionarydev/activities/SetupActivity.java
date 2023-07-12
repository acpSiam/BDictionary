package com.bmarpc.acpsiam.bdictionarydev.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bmarpc.acpsiam.bdictionarydev.R;
import com.bmarpc.acpsiam.bdictionarydev.helpers.LanguageHelper;
import com.bmarpc.acpsiam.bdictionarydev.otherclasses.MyMethods;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

public class SetupActivity extends AppCompatActivity {

    //*To know if databases were added/changed/removed/updated comparing with our SETUP_DB_VERSION. Change this if any of them occurred
    private final int SETUP_DB_VERSION = 4;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyMethods.makeStatusBarTransparent(SetupActivity.this);//*Decorating the status bar
        setContentView(R.layout.activity_setup);

        sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFERENCES_APP_PROCESS), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //*If all our databases are setup correctly then start MainActivity
        //*else go through the processes of setting up the databases for our dictionary
        if (getSETUP_DB_VERSION()==SETUP_DB_VERSION) {
            startMainActivity();
        }

        else {
            //*Available databases in assets
            final String DB_NAME_DICTIONARY = "dictionary.db";
            final String DB_NAME_IDIOMS = "idioms.db";
            final String DB_NAME_PROVERBS = "proverbs.db";
            final String DB_NAME_PREPOSITIONS = "prepositions.db";

            ArrayList<String> DATABASES_AVAILABLE= new ArrayList<>();
            DATABASES_AVAILABLE.add(DB_NAME_DICTIONARY);
            DATABASES_AVAILABLE.add(DB_NAME_IDIOMS);
            DATABASES_AVAILABLE.add(DB_NAME_PROVERBS);
            DATABASES_AVAILABLE.add(DB_NAME_PREPOSITIONS);

            ExtendedFloatingActionButton startButton = findViewById(R.id.setup_activity_start_button_id);
            startButton.setOnClickListener(view -> new Thread(() -> {

                for (String DATABASES : DATABASES_AVAILABLE){
                    if (!databaseExist(DATABASES)){
                        try {
                            copyDataBase(DATABASES); //*Copy only the database that is not already copied
                        } catch (IOException e) {
                            Toast.makeText(SetupActivity.this, R.string.error_copying, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                if (!oldDeviceInstalation()){
                    sendInstallationRecognitionData(); //* If this is not a new installation then don't send the device data to cloud
                }

                savePref(); //*Save the preferences to know that everything is set up to use our dictionary



                //*Once the copying process/database setup process is done, start MainActivity
                runOnUiThread(() -> {
                    Toast.makeText(SetupActivity.this, R.string.dictionary_set_up_to_use, Toast.LENGTH_SHORT).show();
                    startMainActivity();
                });
            }).start());




            //*Setting Language Preference
            Chip enLanguageChip = findViewById(R.id.setup_en_language_pref_chip_id);
            Chip bnLanguageChip = findViewById(R.id.setup_bn_language_pref_chip_id);
            ChipGroup languageChipGroup = findViewById(R.id.setup_language_chip_group_id);
            if (LanguageHelper.getCurrentLocalePref(this).equals("en")){
                enLanguageChip.setSelected(true);
                bnLanguageChip.setSelected(false);
                languageChipGroup.check(enLanguageChip.getId());
            } else {
                enLanguageChip.setSelected(false);
                bnLanguageChip.setSelected(true);
                languageChipGroup.check(bnLanguageChip.getId());
            }
            enLanguageChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                LanguageHelper.setLocale(SetupActivity.this, "en");
                MyMethods.restartActivity(this); //*Restart the activity to apply the new locale effect
            });

            bnLanguageChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                LanguageHelper.setLocale(SetupActivity.this, "bn");
                MyMethods.restartActivity(this); //*Restart the activity to apply the new locale effect
            });
        }
    }



    private void startMainActivity(){
        editor.putBoolean(getString(R.string.DICTIONARY_ADAPTERS_LOADED_BOOLEAN), false);
        editor.apply();
        Intent intent = new Intent(SetupActivity.this, MainActivity.class); //*Intent to MainActivity
        startActivity(intent);
        finish();
    }


    private void copyDataBase(String DATABASE_NAME) throws IOException {
        //*Copy all the databases to our app

        // Open the source database file from the assets folder
        InputStream myInput = SetupActivity.this.getAssets().open(DATABASE_NAME);

        // Get the destination database file path
        File outFileName = SetupActivity.this.getDatabasePath(DATABASE_NAME);

        OutputStream myOutput;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // For Android Oreo (API level 26) and above, use newOutputStream to get OutputStream from the file path
            myOutput = Files.newOutputStream(outFileName.toPath());
        } else {
            // For older Android versions, use FileOutputStream to get OutputStream from the file path
            myOutput = Files.newOutputStream(outFileName.toPath());
        }

        byte[] buffer = new byte[1024];
        int length;

        // Copy data from the source database file to the destination database file
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Flush and close the output stream
        myOutput.flush();
        myOutput.close();

        // Close the input stream
        myInput.close();
    }





    private boolean databaseExist(String DATABASE_NAME) {
        //*Check if a specific database already exists.
        File dbFile = this.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }




    private void savePref() {
        //*Save the preferences to know that everything is set up to use our dictionary

        //    Getting a unique id for each device [TO PROTECT FROM SPAM AND GETTING A RECORD OF APP INSTALLATION]
        @SuppressLint("HardwareIds")
        String UNIQUE_ID = Settings.Secure.getString(SetupActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String DEVICE_INFO = Build.MANUFACTURER + " " + Build.DEVICE + " [" + Build.MODEL + "] : API " + Build.VERSION.SDK_INT + "  Android " + Build.VERSION.RELEASE;

        if (!oldDeviceInstalation()){
            editor.putString(getString(R.string.DEVICE_UID), UNIQUE_ID);
            editor.putString(getString(R.string.DEVICE_INFO), DEVICE_INFO);
            editor.putBoolean(getString(R.string.OLD_DEVICE), true);
        }

        editor.putInt(getString(R.string.SETUP_DB_VERSION), SETUP_DB_VERSION);
        editor.apply();
    }


    private boolean oldDeviceInstalation(){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFERENCES_APP_PROCESS), MODE_PRIVATE);
        return sharedPreferences.getBoolean(getResources().getString(R.string.OLD_DEVICE), false);
    }




    private int getSETUP_DB_VERSION() {
        //*To know if databases were added/changed/removed/updated comparing with our SETUP_DB_VERSION
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFERENCES_APP_PROCESS), MODE_PRIVATE);
        return sharedPreferences.getInt(getResources().getString(R.string.SETUP_DB_VERSION), 0);
    }





    private void sendInstallationRecognitionData(){
        //*Send installation data to recognize devices installing your app. Apply your own logics to handle the service.
        //*I used firebase to collect the installation information. Everything is setup already, just need to connect Firebase

//        ComponentName componentName = new ComponentName(SetupActivity.this, ServiceBackgroundJob.class);
//        JobInfo info = new JobInfo.Builder(5143, componentName)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                .setPersisted(true)
//                .setPeriodic(15 * 60 * 1000)
//                .build();
//
//        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//        int resultCode = scheduler.schedule(info);
//        if (resultCode == JobScheduler.RESULT_SUCCESS) {
//            Log.d(TAG, "Job scheduled");
//        } else {
//            Log.d(TAG, "Job scheduling failed");
//        }
    }


}