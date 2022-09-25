package bmarpc.acpsiam.bdictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHandler extends SQLiteOpenHelper {


    public static final String DB_NAME = "dictionary.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "b_dictionary";
    public static final String EN_COLUMN = "en";
    public static final String BN_COLUMN = "bn";
    public static final String PRON_COLUMN = "pron";
    public static final String EN_SYNS_COLUMN = "en_syns";
    public static final String BN_SYNS_COLUMN = "bn_syns";
    public static final String SENTS_COLUMN = "sents";


    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    public void addWords(String english, String bangla, String prons,
                         String engSyns, String banSyns, String sents) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EN_COLUMN, english);
        values.put(BN_COLUMN, bangla);
        values.put(PRON_COLUMN, prons);
        values.put(EN_SYNS_COLUMN, engSyns);
        values.put(BN_SYNS_COLUMN, banSyns);
        values.put(SENTS_COLUMN, sents);

        database.insert(TABLE_NAME, null, values);

        database.close();
    }


    public Cursor fetchEnToBnData(Context context, String ENGLISH) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, new String[]{"_id", "en", "bn", "pron", "en_syns", "bn_syns", "sents", "word_type", "definition_en", "definition_bn", "en_ants"},
                "en like " + "'" + ENGLISH + "'", null, null, null, null);
        return cursor;
    }


    public Cursor fetchEnWordsDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT en FROM " + TABLE_NAME, null);
        return cursor;
    }

    public Cursor fetchAllWordsDatabase() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
