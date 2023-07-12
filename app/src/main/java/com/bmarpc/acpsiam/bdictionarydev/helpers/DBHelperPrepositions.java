package com.bmarpc.acpsiam.bdictionarydev.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelperPrepositions extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "prepositions.db";
    private static final int DATABASE_VERSION = 1;


    private static final String PREPOSITIONS_TABLE = "prepositions_table";
    private static final String ID_COL = "_id";
    private static final String PREPOSITION_COL = "prep";
    private static final String PREPOSITION_MEANING = "prep_bn";
    private static final String PREPOSITION_EXAMPLE = "prep_example";

    public DBHelperPrepositions(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {}
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}



    public Cursor fetchEnPrepositionsDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("SELECT "+ PREPOSITION_COL +" FROM " + PREPOSITIONS_TABLE, null);
    }



    public Cursor fetchEnToBnPrepositionsData(String ENGLISH) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        if (ENGLISH.contains("'")){
            ENGLISH = ENGLISH.replace("'", "''");
        }

        return sqLiteDatabase.query(PREPOSITIONS_TABLE, new String[]{ID_COL, PREPOSITION_COL, PREPOSITION_MEANING, PREPOSITION_EXAMPLE},
                PREPOSITION_COL + " like " + "'" + ENGLISH + "'", null, null, null, null);
    }





    public Cursor getRandomItemsForQuiz(int numberOfItems) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + PREPOSITION_COL + ", " + PREPOSITION_MEANING + " FROM " + PREPOSITIONS_TABLE + " ORDER BY RANDOM() LIMIT " + numberOfItems;
        return db.rawQuery(query, null);
    }




    public Cursor getRandomWrongAnswerItemsForQuiz(int numberOfItems) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + PREPOSITION_MEANING + " FROM " + PREPOSITIONS_TABLE + " ORDER BY RANDOM() LIMIT " + numberOfItems;
        return db.rawQuery(query, null);
    }
}
