package bmarpc.acpsiam.bdictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SetupActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton startButton;
    private static final String DB_NAME = "dictionary.db";
    DBHandler dbHandler;
    public static ArrayList<String> enWords = new ArrayList<>();
    static ArrayList<String> bnWords = new ArrayList<>();
    static ArrayList<String> enFavs = new ArrayList<>();
    static String viewTypeLibrary = "LIBRARY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_setup);

        startButton = findViewById(R.id.setup_activity_start_button_id);


        dbHandler = new DBHandler(SetupActivity.this);
        Intent intent = new Intent(SetupActivity.this, MainActivity.class);


        if (getPref()) {
            startActivity(intent);
            bgTask();
        }



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    copyDataBase();
                    bgTask();
                    startActivity(intent);
                } catch (IOException e) {
                    Toast.makeText(SetupActivity.this, "Error Copying", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void copyDataBase() throws IOException {
        InputStream myInput = SetupActivity.this.getAssets().open(SetupActivity.DB_NAME);
        File outFileName = SetupActivity.this.getDatabasePath(SetupActivity.DB_NAME);
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
        savePref();
    }


    private boolean databaseExist() {
        File dbFile = this.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }


    private void savePref(){
        SharedPreferences sharedPreferences = SetupActivity.this.getSharedPreferences("dct_setup", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("setup_done", true);
        editor.apply();
    }

    private boolean getPref(){
        SharedPreferences sharedPreferences = getSharedPreferences("dct_setup", MODE_PRIVATE);
        return sharedPreferences.getBoolean("setup_done", false);
    }


    private void bgTask(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                Cursor cursor = dbHandler.fetchEnBnWordsDatabase();
                while (cursor.moveToNext()){
                    enWords.add(cursor.getString(0));
                    bnWords.add(cursor.getString(1));
                }
                SetupActivity.this.runOnUiThread(() -> {
                    Toast.makeText(SetupActivity.this, "Called", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

}