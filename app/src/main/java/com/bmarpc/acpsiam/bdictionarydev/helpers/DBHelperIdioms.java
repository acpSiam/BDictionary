package com.bmarpc.acpsiam.bdictionarydev.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelperIdioms extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "idioms.db";
    private static final int DATABASE_VERSION = 1;


    private static final String IDIOM_TABLE_NAME = "idioms_table";
    private static final String ID_COL = "_id";
    private static final String IDIOM_COL = "idiom";
    private static final String IDIOM_MEANING = "idiom_meaning";
    private static final String IDIOM_EXAMPLE = "idiom_example";
    private static final String IDIOM_DEF = "idiom_def";

    public DBHelperIdioms(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public Cursor fetchEnIdiomsDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("SELECT " + IDIOM_COL + " FROM " + IDIOM_TABLE_NAME, null);
    }

    public Cursor fetchEnToBnIdiomsData(String ENGLISH) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        if (ENGLISH.contains("'")) {
            ENGLISH = ENGLISH.replace("'", "''");
        }

        return sqLiteDatabase.query(IDIOM_TABLE_NAME, new String[]{ID_COL, IDIOM_COL, IDIOM_MEANING, IDIOM_EXAMPLE, IDIOM_DEF},
                IDIOM_COL + " like " + "'" + ENGLISH + "'", null, null, null, null);
    }




    public Cursor getRandomItemsForQuiz(int numberOfItems) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + IDIOM_COL + ", " + IDIOM_MEANING + " FROM " + IDIOM_TABLE_NAME + " ORDER BY RANDOM() LIMIT " + numberOfItems;
        return db.rawQuery(query, null);
    }


    public Cursor getRandomWrongAnswerItemsForQuiz(int numberOfItems) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + IDIOM_MEANING + " FROM " + IDIOM_TABLE_NAME + " ORDER BY RANDOM() LIMIT " + numberOfItems;
        return db.rawQuery(query, null);
    }

}






