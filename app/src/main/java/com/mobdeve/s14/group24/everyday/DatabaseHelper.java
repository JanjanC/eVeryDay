package com.mobdeve.s14.group24.everyday;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance = null;

    private static final String DATABASE_NAME = "MediaEntries.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "media_entries";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CAPTION = "caption";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_MOOD = "mood";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creates the SQL database with the appropriate column names
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " DATE NOT NULL, " +
                COLUMN_IMAGE_PATH + " TEXT UNIQUE, " +
                COLUMN_CAPTION + " TEXT DEFAULT '', " +
                COLUMN_MOOD + " INTEGER(1) DEFAULT 0" +
                ");";
        db.execSQL(query);
    }

    //Changes database to the newer version in the case a new one appears
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Inserts media entry with the date, imagepath, caption, and mood to the database
    public MediaEntry addEntry(CustomDate date, String imagePath, String caption, int mood){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, date.toStringDB());
        cv.put(COLUMN_IMAGE_PATH, imagePath);
        cv.put(COLUMN_CAPTION, caption);
        cv.put(COLUMN_MOOD, mood);
        long id = db.insert(TABLE_NAME,null, cv);
        return getRowById(id);
    }

    //Inserts media entry with date and imagepath to the databasee
    public long addEntry(String imagePath){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, new CustomDate().toStringDB());
        cv.put(COLUMN_IMAGE_PATH, imagePath);
        return db.insert(TABLE_NAME,null, cv);
    }

    //Returns cursor that can point to all the data within the database
    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
            cursor = db.rawQuery(query, null);

        return cursor;
    }

    //Retrieves and returns the mediaEntry with the specified id from the database
    public MediaEntry getRowById(long id){
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if(db != null)
            cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 0)
            return null;

        return cursorToMediaEntry(cursor);
    }

    //Retrieves and returns the ArrayList of media entries within the specified start date and end date from the database
    public ArrayList<MediaEntry> getRowByDateRange(CustomDate dateStart, CustomDate dateEnd) {
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_DATE + " BETWEEN '" +
                dateStart.toStringDB() + "' AND '" +
                dateEnd.toStringDB() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Log.d("aaa", query);
        if(db != null)
            cursor = db.rawQuery(query, null);

        ArrayList<MediaEntry> mediaEntries = new ArrayList<MediaEntry>();

        while (cursor.moveToNext()) {
            mediaEntries.add(0,
                    new MediaEntry(cursor.getInt(0),
                            new CustomDate(cursor.getString(1)),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getInt(4)));
        }

        return mediaEntries;
    }

    //Fetches the media entry with the specified entry within the database and updates its column values accordingly
    public void updateData(int id, String imagePath, String caption, int mood){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IMAGE_PATH, imagePath);
        cv.put(COLUMN_CAPTION, caption);
        cv.put(COLUMN_MOOD, mood);
        db.update(TABLE_NAME, cv, "_id=?", new String[]{String.valueOf(id)});
    }

    //Deletes a media entry from the database
    public void deleteOneRow(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
    }

    //Deletes all entries within the database
    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    //Retrieves the mediaEntry from the database as to where the cursor is currently pointing at
    private MediaEntry cursorToMediaEntry(Cursor cursor) {
        cursor.moveToNext();
        return new MediaEntry(
                cursor.getInt(0),
                new CustomDate(cursor.getString(1)),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4)
        );
    }

    //Returns database helper with context
    public static DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context.getApplicationContext());
        return instance;
    }

}