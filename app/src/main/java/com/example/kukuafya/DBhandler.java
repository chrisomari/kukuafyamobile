package com.example.kukuafya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBhandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "kukuAfyaDB";

    private static final int DB_VERSION = 1;


    private static final String TABLE_NAME = "reminders";


    private static final String ID_COL = "id";


    private static final String title = "title";


    private static final String flock = "flock";


    private static final String date = "date";


    private static final String about = "about";


    public DBhandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + title + " TEXT,"
                + flock + " TEXT,"
                + date + " TEXT,"
                + about + " TEXT)";


        db.execSQL(query);
    }


    public void addNewReminder(String Title, String Flock, String Date, String About) {


        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(title, Title);
        values.put(flock, Flock);
        values.put(date, Date);
        values.put(about, About);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    // we have created a new method for reading all the courses.
    public List<rem_item> getReminders() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        List<rem_item> remItemsArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                remItemsArrayList.add(new rem_item(cursorCourses.getString(1),
                        cursorCourses.getString(4),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3)));
            } while (cursorCourses.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorCourses.close();
        return remItemsArrayList;
    }



    // below is the method for deleting our course.
    public void deleteData(String a,String nm,String c,String d) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_NAME, "title=? AND flock=? AND date=? AND about=?" , new String[]{a,nm,c,d});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
