package com.bmarpc.acpsiam.bdictionarydev.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelperProverbs extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "proverbs.db";
    private static final int DATABASE_VERSION = 1;


    private static final String PROVERB_TABLE_NAME = "proverbs_table";
    private static final String ID_COL = "_id";
    private static final String PROVERB_COL = "proverb";
    private static final String PROVERB_MEANING= "proverb_meaning";

    public DBHelperProverbs(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {}
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}



    public Cursor fetchEnBnProverbsDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("SELECT "+ PROVERB_COL+", "+ PROVERB_MEANING +" FROM " + PROVERB_TABLE_NAME, null);
    }



    public Cursor fetchEnToBnProverbsData(String ENGLISH) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        if (ENGLISH.contains("'")){
            ENGLISH = ENGLISH.replace("'", "''");
        }

        return sqLiteDatabase.query(PROVERB_TABLE_NAME, new String[]{ID_COL, PROVERB_COL, PROVERB_MEANING},
                PROVERB_COL + " like " + "'" + ENGLISH + "'", null, null, null, null);
    }


    public Cursor getRandomItemsForQuiz(int numberOfItems) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + PROVERB_COL + ", " + PROVERB_MEANING + " FROM " + PROVERB_TABLE_NAME + " ORDER BY RANDOM() LIMIT " + numberOfItems;
        return db.rawQuery(query, null);
    }




    public Cursor getRandomWrongAnswerItemsForQuiz(int numberOfItems) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + PROVERB_MEANING + " FROM " + PROVERB_TABLE_NAME + " ORDER BY RANDOM() LIMIT " + numberOfItems;
        return db.rawQuery(query, null);
    }
}
