package com.example.financemaneger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

class DataBaseHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String DATABASE_NAME = "userbase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_finans";
    public static final String COLUMN_I = "_id";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_WEEKS = "week";
    public static final String COLUMN_NUM = "NUM";


    private static final String TABLE_NAME2 = "my_zagl";
    public static final String COLUMN_ID_Z = "_id_z";
    public static final String COLUMN_NOTES_Z = "notes_z";
    public static final String COLUMN_ITEM_Z = "item_z";

    DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_I + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTES + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_AMOUNT + " INTEGER," +
                COLUMN_IMAGE + " TEXT," +
                COLUMN_MONTH + " INTEGER," +
                COLUMN_WEEKS + " INTEGER," +
                COLUMN_NUM + " TEXT);";
        String query2 = "CREATE TABLE " + TABLE_NAME2 + " (" + COLUMN_ID_Z + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTES_Z + " TEXT, " +
                COLUMN_ITEM_Z + " TEXT);";

        db.execSQL(query2);
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }

    void addBook(String date, String notes, int amount, int mount, String i, int week,String num) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DataBaseHelper.COLUMN_NOTES, notes);
        cv.put(DataBaseHelper.COLUMN_DATE, date);
        cv.put(DataBaseHelper.COLUMN_AMOUNT, amount);
        cv.put(DataBaseHelper.COLUMN_MONTH, mount);
        cv.put(DataBaseHelper.COLUMN_IMAGE, i);
        cv.put(DataBaseHelper.COLUMN_WEEKS, week);
        cv.put(DataBaseHelper.COLUMN_NUM, num);

        db.insert(TABLE_NAME, null, cv);
    }

    void addZagl(String item, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DataBaseHelper.COLUMN_NOTES_Z, notes);
        cv.put(DataBaseHelper.COLUMN_ITEM_Z, item);

        db.insert(TABLE_NAME2, null, cv);
    }

    Cursor readAllData1() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor readAllData2() {
        String query = "SELECT * FROM " + TABLE_NAME2;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    void deleteAllData2() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME2);
    }

    void updateData(String row_id, String notes, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DataBaseHelper.COLUMN_NOTES, notes);
        cv.put(DataBaseHelper.COLUMN_AMOUNT, amount);

        db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});

    }

    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
    }
    void deleteOneZagl(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME2, "_id_z=?", new String[]{row_id});
    }

}
