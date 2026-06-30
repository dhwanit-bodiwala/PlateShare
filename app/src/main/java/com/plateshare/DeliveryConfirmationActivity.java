package com.plateshare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DeliveryConfirmationActivity extends AppCompatActivity {

    Button yes_btn_delivery, no_btn_delivery;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_confirmation_screen);

        yes_btn_delivery = findViewById(R.id.yes_btn_delivery);
        no_btn_delivery = findViewById(R.id.no_btn_delivery);

        String email = getIntent().getStringExtra("user_email");
        int donation_id = getIntent().getIntExtra("donation_id",-1);


        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        yes_btn_delivery.setOnClickListener(v -> {

            Cursor donor_user_id_cursor = db.query("donations",null,"id=?",new String[] {String.valueOf(donation_id)},null,null,null);
            donor_user_id_cursor.moveToFirst();
            int donor_user_id_idx = donor_user_id_cursor.getColumnIndex("user_id");
            int donor_user_id = donor_user_id_cursor.getInt(donor_user_id_idx);
            donor_user_id_cursor.close();

            Cursor donor_name_cursor = db.query("users",null,"id=?",new String[] {String.valueOf(donor_user_id)},null,null,null);
            donor_name_cursor.moveToFirst();
            int donor_name_idx = donor_name_cursor.getColumnIndex("name");
            String donor_name = donor_name_cursor.getString(donor_name_idx);
            donor_name_cursor.close();

            Cursor rater_user_id_cursor = db.query("users",null,"email=?",new String[] {email},null,null,null);
            rater_user_id_cursor.moveToFirst();
            int rater_user_id_idx = rater_user_id_cursor.getColumnIndex("id");
            int rater_user_id = rater_user_id_cursor.getInt(rater_user_id_idx);
            rater_user_id_cursor.close();

            ContentValues values = new ContentValues();

            values.put("status","Completed");

            int rowsAffected = db.update(
                    "donations",
                    values,
                    "id=?",
                    new String[] {String.valueOf(donation_id)}
            );


            if (rowsAffected > 0){
                Toast.makeText(this, "Delivery Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DeliveryConfirmationActivity.this,RatingActivity.class);
                intent.putExtra("donation_id",donation_id);
                intent.putExtra("donor_user_id",donor_user_id);
                intent.putExtra("donor_name",donor_name);
                intent.putExtra("rater_user_id",rater_user_id);
                intent.putExtra("user_email",email);
                intent.putExtra("user_role","receiver");
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        no_btn_delivery.setOnClickListener(v -> {

            ContentValues values = new ContentValues();
            values.put("status","Available");
            values.putNull("claimed_by_user_id");

            db.update(
                    "donations",
                    values,
                    "id=?",
                    new String[] {String.valueOf(donation_id)}
            );

            Toast.makeText(this, "Transaction or Claiming failed", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(DeliveryConfirmationActivity.this,HomeActivity.class);
            intent.putExtra("user_email",email);
            intent.putExtra("user_role","receiver");
            startActivity(intent);
            finish();

        });

    }

    @Override
    public void onBackPressed() {
        // Intentionally disabled — delivery confirmation is a committed decision
    }
}