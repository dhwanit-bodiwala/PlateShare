package com.plateshare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;



public class CreateRequestActivity extends AppCompatActivity {

    TextView back_btn_request;

    EditText title_request, description_request, quantity_needed_request, location_request;

    Button submit_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_request_screen);

        back_btn_request = findViewById(R.id.back_btn_request);

        title_request = findViewById(R.id.title_request);
        description_request = findViewById(R.id.description_request);
        quantity_needed_request = findViewById(R.id.quantity_needed_request);
        location_request = findViewById(R.id.location_request);

        submit_request = findViewById(R.id.submit_request);

        String email = getIntent().getStringExtra("user_email");


        submit_request.setOnClickListener(v -> {

            String title = title_request.getText().toString().trim();
            String description = description_request.getText().toString().trim();
            String quantity_needed = quantity_needed_request.getText().toString().trim();
            String location = location_request.getText().toString().trim();

            if (title.isEmpty()){
                title_request.setError("Enter Title");
                title_request.requestFocus();
                return;
            }

            if (description.isEmpty()){
                description_request.setError("Enter Description");
                description_request.requestFocus();
                return;
            }

            if (quantity_needed.isEmpty()){
                quantity_needed_request.setError("Enter Quantity");
                quantity_needed_request.requestFocus();
                return;
            }

            if (location.isEmpty()){
                location_request.setError("Enter Location");
                location_request.requestFocus();
                return;
            }


            try {


                DatabaseHelper dbHelper = new DatabaseHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                Cursor cursor = db.query("users", null, "email=?", new String[]{email}, null, null, null, null);

                cursor.moveToFirst();

                int receiver_id_idx = cursor.getColumnIndex("id");

                String receiver_id = cursor.getString(receiver_id_idx);



                ContentValues values = new ContentValues();

                values.put("receiver_id", receiver_id);
                values.put("title", title);
                values.put("description", description);
                values.put("quantity_needed", quantity_needed);
                values.put("location", location);
                values.put("status", "Open");

                long result = db.insert("requests", null, values);

                if (result == -1) {
                    Toast.makeText(this, "Request failed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Request added!", Toast.LENGTH_SHORT).show();
                    finish();
                }


            } catch (Exception e) {
                Snackbar.make(v,"Something went wrong",Snackbar.LENGTH_LONG)
                        .setAction("OK", view -> {
                            startActivity(new Intent(CreateRequestActivity.this,HomeActivity.class));
                        }).show();
            }
        });


        back_btn_request.setOnClickListener(v -> finish());



    }

}