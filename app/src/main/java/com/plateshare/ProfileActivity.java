package com.plateshare;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class ProfileActivity extends AppCompatActivity {

    TextView avatar_initial, profile_name, profile_email, profile_phone, profile_address;

    Button edit_profile_btn, logout_btn;

    TextView stat_value_1, stat_label_1, stat_value_2, stat_label_2, stat_value_3, stat_label_3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);



        avatar_initial = findViewById(R.id.avatar_initial);
        profile_name = findViewById(R.id.profile_name);
        profile_email = findViewById(R.id.profile_email);
        profile_phone = findViewById(R.id.profile_phone);
        profile_address = findViewById(R.id.profile_address);

        edit_profile_btn = findViewById(R.id.edit_profile_btn);
        logout_btn = findViewById(R.id.logout_btn);

        stat_value_1 = findViewById(R.id.stat_value_1);
        stat_label_1 = findViewById(R.id.stat_label_1);
        stat_value_2 = findViewById(R.id.stat_value_2);
        stat_label_2 = findViewById(R.id.stat_label_2);
        stat_value_3 = findViewById(R.id.stat_value_3);
        stat_label_3 = findViewById(R.id.stat_label_3);




        Intent intent = getIntent();
        String email = intent.getStringExtra("user_email");
        String user_role = intent.getStringExtra("user_role");



        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        Cursor cursor = db.query("users",null,"email=?",new String[] {email},null,null,null,null);

        cursor.moveToFirst();

        int name_idx = cursor.getColumnIndex("name");
        int phone_idx = cursor.getColumnIndex("phone");
        int address_idx = cursor.getColumnIndex("address");

        String name = cursor.getString(name_idx);
        String phone = cursor.getString(phone_idx);
        String address = cursor.getString(address_idx);
        String name_initial = name.substring(0,1);

        cursor.close();

        avatar_initial.setText(name_initial);
        profile_name.setText(name);
        profile_email.setText(email);
        profile_phone.setText(phone);
        profile_address.setText(address);

        Cursor user_idCursor = db.query("users",null,"email=?",new String[] {email},null,null,null);

        user_idCursor.moveToFirst();
        int user_id_idx = user_idCursor.getColumnIndex("id");

        int user_id = user_idCursor.getInt(user_id_idx);

        user_idCursor.close();


        if (user_role.equals("donor")){
            stat_label_1.setText("Total Donations");
            stat_label_2.setText("Completed");
            stat_label_3.setText("Avg Rating");

            Cursor stat_cursor_1 = db.rawQuery("SELECT COUNT(*) FROM donations WHERE user_id=?",new String[] {String.valueOf(user_id)});
            stat_cursor_1.moveToFirst();
            int totalDonations = stat_cursor_1.getInt(0);
            stat_cursor_1.close();

            Cursor stat_cursor_2 = db.rawQuery("SELECT COUNT(*) FROM donations WHERE user_id=? AND status='Completed'",new String[] {String.valueOf(user_id)});
            stat_cursor_2.moveToFirst();
            int donationsCompleted = stat_cursor_2.getInt(0);
            stat_cursor_2.close();

            Cursor stat_cursor_3 = db.rawQuery(
                    "SELECT ROUND(AVG((reliability + communication) / 2.0), 1) FROM ratings WHERE rated_user_id=?",
                    new String[]{String.valueOf(user_id)}
            );
            double avgRatings = 0;
            if (stat_cursor_3.moveToFirst()) {
                avgRatings = stat_cursor_3.getDouble(0);
            }
            stat_cursor_3.close();

            stat_value_1.setText(String.valueOf(totalDonations));
            stat_value_2.setText(String.valueOf(donationsCompleted));
            stat_value_3.setText(String.valueOf(avgRatings));


        }
        else {
            stat_label_1.setText("Total Claimed");
            stat_label_2.setText("Request Posted");
            stat_label_3.setText("Fulfilled");


            Cursor stat_cursor_4 = db.rawQuery("SELECT COUNT(*) FROM donations WHERE claimed_by_user_id=? AND status IN ('Claimed','Completed')",new String[] {String.valueOf(user_id)});
            stat_cursor_4.moveToFirst();
            int totalClaimed = stat_cursor_4.getInt(0);
            stat_cursor_4.close();

            Cursor stat_cursor_5 = db.rawQuery("SELECT COUNT(*) FROM requests WHERE receiver_id=?",new String[] {String.valueOf(user_id)});
            stat_cursor_5.moveToFirst();
            int requestPosted = stat_cursor_5.getInt(0);
            stat_cursor_5.close();

            Cursor stat_cursor_6 = db.rawQuery("SELECT COUNT(*) FROM requests WHERE receiver_id=? AND status='Accepted'",new String[]{String.valueOf(user_id)});
            stat_cursor_6.moveToFirst();
            int requestFulfilled = stat_cursor_6.getInt(0);
            stat_cursor_6.close();

            stat_value_1.setText(String.valueOf(totalClaimed));
            stat_value_2.setText(String.valueOf(requestPosted));
            stat_value_3.setText(String.valueOf(requestFulfilled));

        }



        logout_btn.setOnClickListener(v -> {
            Intent logoutIntent = new Intent(ProfileActivity.this, LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
        });

        edit_profile_btn.setOnClickListener(v -> {
            Intent editProfileIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            editProfileIntent.putExtra("user_email",email);
            startActivity(editProfileIntent);
        });

    }
}