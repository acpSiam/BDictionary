package com.bmarpc.acpsiam.bdictionarydev.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "dictionary.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "b_dictionary";
    public static final String FAVORITES_TABLE_NAME = "b_dictionary_favs";
    public static final String HISTORY_TABLE_NAME = "b_dictionary_history";


    public static final String ID_COLUMN = "_id";
    public static final String EN_COLUMN = "en";
    public static final String BN_COLUMN = "bn";
    public static final String PRON_COLUMN = "pron";
    public static final String EN_SYNS_COLUMN = "en_syns";
    public static final String BN_SYNS_COLUMN = "bn_syns";
    public static final String SENTS_COLUMN = "sents";
    public static final String WORD_TYPE_COLUMN = "word_type";
    public static final String DEF_EN_COLUMN = "definition_en";
    public static final String DEF_BN_COLUMN = "definition_bn";
    public static final String EN_ANTS_COLUMN = "en_ants";





    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ FAVORITES_TABLE_NAME + " ("+ ID_COLUMN +" INTEGER PRIMARY KEY AUTOINCREMENT, " + EN_COLUMN + " TEXT NOT NULL UNIQUE, "+BN_COLUMN+" TEXT NOT NULL);");
        db.execSQL("CREATE TABLE "+ HISTORY_TABLE_NAME + " ("+ID_COLUMN+" INTEGER PRIMARY KEY AUTOINCREMENT, " +EN_COLUMN+ " TEXT NOT NULL, "+BN_COLUMN+" TEXT NOT NULL);");
    }


    public void addFavWords(String english, String bangla) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EN_COLUMN, english);
        values.put(BN_COLUMN, bangla);

        database.insert(FAVORITES_TABLE_NAME, null, values);

        database.close();
    }

    public void deleteFromFav(String enWord){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM "+FAVORITES_TABLE_NAME+" WHERE "+EN_COLUMN+" LIKE '%"+enWord+"%'");
        database.close();
    }



    public void addToHistory(String english, String bangla) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EN_COLUMN, english);
        values.put(BN_COLUMN, bangla);

        database.insert(HISTORY_TABLE_NAME, null, values);

        database.close();
    }

    public void clearHistory(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + HISTORY_TABLE_NAME);
        database.close();
    }


    public Cursor fetchEnToBnData(String ENGLISH) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.query(TABLE_NAME, new String[]{ID_COLUMN, EN_COLUMN, BN_COLUMN, PRON_COLUMN, EN_SYNS_COLUMN,
                        BN_SYNS_COLUMN, SENTS_COLUMN, WORD_TYPE_COLUMN, DEF_EN_COLUMN, DEF_BN_COLUMN, EN_ANTS_COLUMN},
                EN_COLUMN + " like " + "'" + ENGLISH + "'", null, null, null, null);
    }


    public Cursor fetchEnWordsDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("SELECT "+ EN_COLUMN + " FROM " + TABLE_NAME, null);
    }


    public Cursor fetchFavouritesDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + FAVORITES_TABLE_NAME, null);
    }


    public Boolean favouritesExists(String en){
        boolean exixts = false;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT "+ EN_COLUMN +" FROM "+ FAVORITES_TABLE_NAME +" WHERE "+ EN_COLUMN +" LIKE \""+ en + "\"", null);
        if (cursor.getCount()!=0){
            exixts = true;
        }
        cursor.close();
        return exixts;
    }


    public Cursor fetchHistoryDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + HISTORY_TABLE_NAME, null);
    }





    public Cursor getRandomItemsForQuiz(int numberOfItems) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + EN_COLUMN + ", " + BN_COLUMN + " FROM " + TABLE_NAME + " ORDER BY RANDOM() LIMIT " + numberOfItems;
        return db.rawQuery(query, null);
    }

    public Cursor getRandomWrongAnswerItemsForQuiz(int numberOfItems) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + BN_COLUMN + " FROM " + TABLE_NAME + " ORDER BY RANDOM() LIMIT " + numberOfItems;
        return db.rawQuery(query, null);
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
