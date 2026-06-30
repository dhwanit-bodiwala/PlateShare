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
                "address TEXT,"+
                "role TEXT)");

        // Donations table
        db.execSQL("CREATE TABLE donations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "food_name TEXT," +
                "quantity TEXT," +
                "expiry_date TEXT," +
                "allergies TEXT," +
                "food_type TEXT," +
                "status TEXT,"+
                "claimed_by_user_id INTEGER,"+
                "FOREIGN KEY(user_id) REFERENCES users(id),"+
                "FOREIGN KEY(claimed_by_user_id) references users(id))");

        // Centers table
        db.execSQL("CREATE TABLE centers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "address TEXT," +
                "contact TEXT," +
                "stock_info TEXT)");

        // Donor_profile table
         db.execSQL("CREATE TABLE donor_profile (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "donor_type TEXT," +
                "organization_name TEXT," +
                "contact_person TEXT," +
                "FOREIGN KEY(user_id) REFERENCES users(id))");

         // Requests table
        db.execSQL("CREATE TABLE requests (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "receiver_id INTEGER," +
                "title TEXT," +
                "description TEXT," +
                "quantity_needed TEXT," +
                "location TEXT," +
                "status TEXT," +
                "accepted_by_user_id INTEGER," +
                "FOREIGN KEY(receiver_id) REFERENCES users(id)," +
                "FOREIGN KEY(accepted_by_user_id) REFERENCES users(id))");

        // Ratings table
        db.execSQL("CREATE TABLE ratings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "donation_id INTEGER," +
                "rated_by_user_id INTEGER," +
                "rated_user_id INTEGER," +
                "reliability INTEGER," +
                "service_quality INTEGER," +
                "communication INTEGER," +
                "FOREIGN KEY(donation_id) REFERENCES donations(id)," +
                "FOREIGN KEY(rated_by_user_id) REFERENCES users(id)," +
                "FOREIGN KEY(rated_user_id) REFERENCES users(id))");

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
        db.execSQL("DROP TABLE IF EXISTS donor_profile");
        db.execSQL("DROP TABLE IF EXISTS requests");
        db.execSQL("DROP TABLE IF EXISTS ratings");
        onCreate(db);
    }
}