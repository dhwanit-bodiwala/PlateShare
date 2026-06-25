package com.plateshare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;



public class DonateFoodActivity extends AppCompatActivity {

    TextView back_btn;

    EditText food_name_donate, quantity_donate, expiry_date_donate, allergies_donate;

    Spinner food_type_donate;

    Button submit_donate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_food_screen);


        back_btn = findViewById(R.id.back_btn);

        food_name_donate = findViewById(R.id.food_name_donate);
        quantity_donate = findViewById(R.id.quantity_donate);
        expiry_date_donate = findViewById(R.id.expiry_date_donate);
        allergies_donate = findViewById(R.id.allergies_donate);

        food_type_donate = findViewById(R.id.food_type_donate);

        submit_donate = findViewById(R.id.submit_donate);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                new String[]{"Select food type", "Solid", "Liquid", "Semi-Solid"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        food_type_donate.setAdapter(adapter);



        Intent intent = getIntent();
        String email = intent.getStringExtra("user_email");



        submit_donate.setOnClickListener(v ->{

            String foodName = food_name_donate.getText().toString().trim();
            String quantity = quantity_donate.getText().toString().trim();
            String expiry = expiry_date_donate.getText().toString().trim();
            String allergies = allergies_donate.getText().toString().trim();
            String foodType = food_type_donate.getSelectedItem().toString();

            if (foodName.isEmpty()){
                food_name_donate.setError("Enter Food name");
                food_name_donate.requestFocus();
                return;
            }

            if (quantity.isEmpty()){
                quantity_donate.setError("Enter Quantity");
                quantity_donate.requestFocus();
                return;
            }

            if (expiry.isEmpty()) {
                expiry_date_donate.setError("Enter Expiry date");
                expiry_date_donate.requestFocus();
                return;
            }

            if (foodType.equals("Select food type")) {
                Toast.makeText(this, "Select a food type", Toast.LENGTH_SHORT).show();
                return;
            }



            try {

                DatabaseHelper dbHelper = new DatabaseHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();


                Cursor cursor = db.query("users", null, "email=?", new String[]{email}, null, null, null, null);

                cursor.moveToFirst();

                int id_idx = cursor.getColumnIndex("id");

                String id = cursor.getString(id_idx);


                cursor.close();

                //////// insertion

                ContentValues values = new ContentValues();

                values.put("user_id", id);
                values.put("food_name", foodName);
                values.put("quantity", quantity);
                values.put("expiry_date", expiry);
                values.put("allergies", allergies);
                values.put("food_type", foodType);


                long result = db.insert("donations", null, values);

                if (result == -1) {
                    Toast.makeText(this, "Donation failed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Donation added!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            catch (Exception e){
                Snackbar.make(v, "Something went wrong", Snackbar.LENGTH_LONG)
                        .setAction("OK", view -> {
                            startActivity(new Intent(DonateFoodActivity.this, HomeActivity.class));
                        }).show();
            }

        });



        back_btn.setOnClickListener(v -> finish());



    }
}