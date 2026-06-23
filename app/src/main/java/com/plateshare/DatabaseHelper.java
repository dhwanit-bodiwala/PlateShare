package com.plateshare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "plateshare.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Users table
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT UNIQUE," +
                "password TEXT," +
                "phone TEXT," +
                "address TEXT)");

        // Donations table
        db.execSQL("CREATE TABLE donations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "food_name TEXT," +
                "quantity TEXT," +
                "expiry_date TEXT," +
                "allergies TEXT," +
                "food_type TEXT," +
                "FOREIGN KEY(user_id) REFERENCES users(id))");

        // Centers table
        db.execSQL("CREATE TABLE centers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "address TEXT," +
                "contact TEXT," +
                "stock_info TEXT)");

        // Insert sample centers
        db.execSQL("INSERT INTO centers VALUES (1, 'Ahmedabad Food Bank', 'Navrangpura, Ahmedabad', '079-12345678', 'Needs rice and dal')");
        db.execSQL("INSERT INTO centers VALUES (2, 'Roti Ghar', 'Maninagar, Ahmedabad', '079-87654321', 'Accepting all food types')");
        db.execSQL("INSERT INTO centers VALUES (3, 'Anna Kshetra Trust', 'Satellite, Ahmedabad', '079-11223344', 'Low on vegetables')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS donations");
        db.execSQL("DROP TABLE IF EXISTS centers");
        onCreate(db);
    }
}