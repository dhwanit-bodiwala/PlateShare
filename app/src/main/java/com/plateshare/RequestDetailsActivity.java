package com.plateshare;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class RequestDetailsActivity extends AppCompatActivity {

    TextView back_btn_request_details, details_request_title, details_request_description, details_request_quantity, details_request_location, details_request_receiver_name;

    Button accept_btn_request_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_details_screen);

        back_btn_request_details = findViewById(R.id.back_btn_request_details);
        details_request_title = findViewById(R.id.details_request_title);
        details_request_description = findViewById(R.id.details_request_description);
        details_request_quantity = findViewById(R.id.details_request_quantity);
        details_request_location = findViewById(R.id.details_request_location);
        details_request_receiver_name = findViewById(R.id.details_request_receiver_name);

        accept_btn_request_details = findViewById(R.id.accept_btn_request_details);

        int request_id = getIntent().getIntExtra("request_id",-1);
        String email = getIntent().getStringExtra("user_email");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT requests.id, requests.title, requests.description, " +
                        "requests.quantity_needed, requests.location, users.name AS receiver_name " +
                        "FROM requests " +
                        "JOIN users ON requests.receiver_id = users.id " +
                        "WHERE requests.id = ?",
                new String[]{String.valueOf(request_id)}
        );

        cursor.moveToFirst();

        int title_idx = cursor.getColumnIndex("title");
        int description_idx = cursor.getColumnIndex("description");
        int quantity_needed_idx = cursor.getColumnIndex("quantity_needed");
        int location_idx = cursor.getColumnIndex("location");
        int receiver_name_idx = cursor.getColumnIndex("receiver_name");

        String title = cursor.getString(title_idx);
        String description = cursor.getString(description_idx);
        String quantity_needed = cursor.getString(quantity_needed_idx);
        String location = cursor.getString(location_idx);
        String receiver_name = cursor.getString(receiver_name_idx);

        details_request_title.setText(title);
        details_request_description.setText(description);
        details_request_quantity.setText(quantity_needed);
        details_request_location.setText(location);
        details_request_receiver_name.setText(receiver_name);


        back_btn_request_details.setOnClickListener(v -> finish());

        accept_btn_request_details.setOnClickListener(v -> {

            String user_id;

            Cursor cursor1 = db.query("users",null,"email=?",new String[] {email},null,null,null);

            cursor1.moveToFirst();

            int user_id_idx = cursor1.getColumnIndex("id");

            user_id = cursor1.getString(user_id_idx);

            cursor1.close();

            ContentValues values = new ContentValues();

            values.put("status","Accepted");
            values.put("accepted_by_user_id",user_id);

            int rowsAffected = db.update(
                    "requests",
                    values,
                    "id=? AND status=?",
                    new String[]{String.valueOf(request_id),"Open"}
            );

            if (rowsAffected > 0){
                Toast.makeText(this, "Request Accepted", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                Toast.makeText(this, "Sorry, this was just accepted by someone else", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}