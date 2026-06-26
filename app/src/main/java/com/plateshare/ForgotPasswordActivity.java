package com.plateshare;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class ForgotPasswordActivity extends AppCompatActivity {


    EditText email_forgot;

    Button submit_forgot;

    TextView back_login_forgot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_screen);



        email_forgot = findViewById(R.id.email_forgot);
        submit_forgot = findViewById(R.id.submit_forgot);
        back_login_forgot = findViewById(R.id.back_login_forgot);


        submit_forgot.setOnClickListener(v -> {

            String email = email_forgot.getText().toString().trim();

            if (email.isEmpty()){
                email_forgot.setError("Enter Email");
                email_forgot.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                email_forgot.setError("Enter valid Email");
                email_forgot.requestFocus();
                return;
            }


            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query("users",null,"email=?",new String[] {email},null,null,null);



            if (cursor.getCount() == 0){
                Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                email_forgot.requestFocus();
                cursor.close();
            }
            else {
                cursor.close();
                Intent intent = new Intent(ForgotPasswordActivity.this,ChangePasswordActivity.class);
                intent.putExtra("user_email",email);
                startActivity(intent);
            }


        });




        back_login_forgot.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
            startActivity(intent);
        });




    }
}