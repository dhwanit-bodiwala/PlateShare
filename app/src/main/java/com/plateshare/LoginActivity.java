package com.plateshare;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    EditText email_login , password_login;
    TextView forgot_password_login, signup_login, guest_login;
    Button login_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);

        forgot_password_login = findViewById(R.id.forgot_password_login);
        signup_login = findViewById(R.id.signup_login);
        guest_login = findViewById(R.id.guest_login);

        login_login = findViewById(R.id.login_login);

//        logic



        guest_login.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        });

        forgot_password_login.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        });

        signup_login.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });




        login_login.setOnClickListener(v -> {
            String email = email_login.getText().toString().trim();
            String password = password_login.getText().toString().trim();

            if (email.isEmpty()){
                email_login.setError("Enter Email");
                email_login.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                email_login.setError("Enter valid Email");
                email_login.requestFocus();
                return;
            }

            if (password.isEmpty()){
                password_login.setError("Enter Password");
                password_login.requestFocus();
                return;
            }

            if (password.length() < 6){
                password_login.setError("Password must be atleast 6 characters");
                password_login.requestFocus();
                return;
            }


            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query("users", null, null, null, null, null, null);

            int getEmailIndex = cursor.getColumnIndex("email");
            int getPasswordIndex = cursor.getColumnIndex("password");
            int getNameIndex = cursor.getColumnIndex("name");

            boolean flag = false;

            while (cursor.moveToNext()){
                String getEmail = cursor.getString(getEmailIndex);
                String getPassword = cursor.getString(getPasswordIndex);
                String getName = cursor.getString(getNameIndex);

                if (email.equals(getEmail) && password.equals(getPassword)){
                    flag = true;
                    Toast.makeText(this, "Welcome "+getName, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }

            }

            if (!flag) {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }

            cursor.close();

        });


    }
}