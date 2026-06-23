package com.plateshare;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;



public class SignupActivity extends AppCompatActivity {


    EditText name_signup, email_signup, password_signup, confirm_password_signup, phone_signup, address_signup;
    TextView login_signup;
    Button signup_signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);


        name_signup = findViewById(R.id.name_signup);
        email_signup = findViewById(R.id.email_signup);
        password_signup = findViewById(R.id.password_signup);
        confirm_password_signup = findViewById(R.id.confirm_password_signup);
        phone_signup = findViewById(R.id.phone_signup);
        address_signup = findViewById(R.id.address_signup);

        login_signup = findViewById(R.id.login_signup);

        signup_signup = findViewById(R.id.signup_signup);



        // logic

        // validation

        login_signup.setOnClickListener(view ->{
            startActivity(new Intent(SignupActivity.this,LoginActivity.class));
        });

        // signup

        signup_signup.setOnClickListener(view  ->{
            String name = name_signup.getText().toString().trim();
            String email = email_signup.getText().toString().trim();
            String password = password_signup.getText().toString().trim();
            String confirm_password = confirm_password_signup.getText().toString().trim();
            String phone = phone_signup.getText().toString().trim();
            String address = address_signup.getText().toString();



            if (name.isEmpty()){
                name_signup.setError("Enter Name");
                name_signup.requestFocus();
                return;
            }

            if (email.isEmpty()){
                email_signup.setError("Enter Email");
                email_signup.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                email_signup.setError("Enter valid Email");
                email_signup.requestFocus();
                return;
            }


            if (password.isEmpty()){
                password_signup.setError("Enter Password");
                password_signup.requestFocus();
                return;
            }

            if (password.length() < 6){
                password_signup.setError("Password must be at least 6 characters");
                password_signup.requestFocus();
                return;
            }

            if (confirm_password.isEmpty()){
                confirm_password_signup.setError("Enter Password");
                confirm_password_signup.requestFocus();
                return;
            }

            if (!password.equals(confirm_password)){
                confirm_password_signup.setError("Both Passwords must be matching");
                confirm_password_signup.requestFocus();
                return;
            }

            if (phone.isEmpty()){
                phone_signup.setError("Enter Phone number");
                phone_signup.requestFocus();
                return;
            }

            if (phone.length() != 10){
                phone_signup.setError("Enter valid Phone number");
                phone_signup.requestFocus();
                return;
            }

            if (address.isEmpty()){
                address_signup.setError("Enter Address");
                address_signup.requestFocus();
                return;
            }

            // replace this with insert query
//            Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
//            startActivity(intent);

            try {
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();


                ContentValues values = new ContentValues();

                values.put("name", name);
                values.put("email", email);
                values.put("password", password);
                values.put("phone", phone);
                values.put("address", address);


                long result = db.insert("users", null, values);


                if (result == -1) {
                    Toast.makeText(this, "Account creation failed", Toast.LENGTH_LONG).show();
                }

                else {
                    Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                }

            }
            catch (Exception e){
                Snackbar.make(view, "Something went wrong", Snackbar.LENGTH_LONG)
                        .setAction("OK", v -> {}).show();
            }

        });



    }
}