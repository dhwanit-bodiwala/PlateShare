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



        Intent intent = getIntent();
        String email = intent.getStringExtra("user_email");



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