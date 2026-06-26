package com.plateshare;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ChangePasswordActivity extends AppCompatActivity {


    EditText new_password_change, confirm_password_change;

    Button submit_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_screen);

        new_password_change = findViewById(R.id.new_password_change);
        confirm_password_change = findViewById(R.id.confirm_password_change);

        submit_change = findViewById(R.id.submit_change);




        String userEmail = getIntent().getStringExtra("user_email");





        submit_change.setOnClickListener(v -> {

            String new_password = new_password_change.getText().toString().trim();
            String confirm_password = confirm_password_change.getText().toString().trim();

            if (new_password.isEmpty()){
                new_password_change.setError("Enter Password");
                new_password_change.requestFocus();
                return;
            }

            if (new_password.length() < 6){
                new_password_change.setError("Password must be at least 6 characters");
                new_password_change.requestFocus();
                return;
            }

            if (confirm_password.isEmpty()){
                confirm_password_change.setError("Enter Password");
                confirm_password_change.requestFocus();
                return;
            }

            if (!new_password.equals(confirm_password)){
                confirm_password_change.setError("Both Passwords must be matching");
                confirm_password_change.requestFocus();
                return;
            }


            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("password",new_password);

            int rowsAffected = db.update("users",values,"email=?",new String[] {userEmail});

            if (rowsAffected > 0){
                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}