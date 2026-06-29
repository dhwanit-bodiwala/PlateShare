package com.plateshare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class DonationDetailsActivity extends AppCompatActivity {

    TextView back_btn_details, details_food_name, details_food_type, details_quantity, details_expiry, details_allergies, details_donor_name;

    Button claim_btn_details;

    int donation_id;
    String user_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donation_details_screen);

        back_btn_details = findViewById(R.id.back_btn_details);
        details_food_name = findViewById(R.id.details_food_name);
        details_food_type = findViewById(R.id.details_food_type);
        details_quantity = findViewById(R.id.details_quantity);
        details_expiry = findViewById(R.id.details_expiry);
        details_allergies = findViewById(R.id.details_allergies);
        details_donor_name = findViewById(R.id.details_donor_name);

        claim_btn_details = findViewById(R.id.claim_btn_details);

        donation_id = getIntent().getIntExtra("donation_id",-1);
        user_email = getIntent().getStringExtra("user_email");


        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT donations.id, donations.food_name, donations.quantity, " +
                        "donations.food_type, donations.expiry_date, donations.allergies, users.name AS donor_name " +
                        "FROM donations " +
                        "JOIN users ON donations.user_id = users.id " +
                        "WHERE donations.id = ?",
                new String[]{String.valueOf(donation_id)}
        );

        cursor.moveToFirst();

        int food_name_idx = cursor.getColumnIndex("food_name");
        int food_type_idx = cursor.getColumnIndex("food_type");
        int quantity_idx = cursor.getColumnIndex("quantity");
        int expiry_idx = cursor.getColumnIndex("expiry_date");
        int allergies_idx = cursor.getColumnIndex("allergies");
        int donor_name_idx = cursor.getColumnIndex("donor_name");

        String food_name = cursor.getString(food_name_idx);
        String food_type = cursor.getString(food_type_idx);
        String quantity = cursor.getString(quantity_idx);
        String expiry = cursor.getString(expiry_idx);
        String allergies = cursor.getString(allergies_idx);
        String donor_name = cursor.getString(donor_name_idx);

        details_food_name.setText(food_name);
        details_food_type.setText(food_type);
        details_quantity.setText(quantity);
        details_expiry.setText(expiry);
        details_allergies.setText(allergies);
        details_donor_name.setText(donor_name);

        cursor.close();

        back_btn_details.setOnClickListener(v -> finish());



        claim_btn_details.setOnClickListener(v -> {

            String user_id;

            Cursor cursor1 = db.query("users",null,"email=?",new String[] {user_email},null,null,null);

            cursor1.moveToFirst();

            int user_id_idx = cursor1.getColumnIndex("id");

            user_id = cursor1.getString(user_id_idx);

            cursor1.close();

            ContentValues values = new ContentValues();

            values.put("status","Claimed");
            values.put("claimed_by_user_id",user_id);

            int rowsAffected = db.update(
                    "donations",
                    values,
                    "id=? AND status=?",
                    new String[]{String.valueOf(donation_id), "Available"}
            );

            if (rowsAffected > 0){
                // changes here
                Toast.makeText(this, "Donation Claimed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DonationDetailsActivity.this,DeliveryConfirmationActivity.class);
                intent.putExtra("user_email",user_email);
                intent.putExtra("donation_id",donation_id);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this, "Sorry, this was just claimed by someone else", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }
}