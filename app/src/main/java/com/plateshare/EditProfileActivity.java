package com.plateshare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class EditProfileActivity extends AppCompatActivity {

    EditText name_edit_profile, phone_edit_profile, address_edit_profile;

    Button submit_edit_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_screen);

        name_edit_profile = findViewById(R.id.name_edit_profile);
        phone_edit_profile = findViewById(R.id.phone_edit_profile);
        address_edit_profile = findViewById(R.id.address_edit_profile);

        submit_edit_profile = findViewById(R.id.submit_edit_profile);

        Intent intent = getIntent();
        String email = intent.getStringExtra("user_email");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("users", null, "email=?", new String[]{email}, null, null, null, null);

        cursor.moveToFirst();

        int name_idx = cursor.getColumnIndex("name");
        int phone_idx = cursor.getColumnIndex("phone");
        int address_idx = cursor.getColumnIndex("address");

        String name = cursor.getString(name_idx);
        String phone = cursor.getString(phone_idx);
        String address = cursor.getString(address_idx);

        cursor.close();

        name_edit_profile.setText(name);
        phone_edit_profile.setText(phone);
        address_edit_profile.setText(address);


        submit_edit_profile.setOnClickListener(v -> {

            String new_name = name_edit_profile.getText().toString().trim();
            String new_phone = phone_edit_profile.getText().toString().trim();
            String new_address = address_edit_profile.getText().toString().trim();

            ContentValues values = new ContentValues();

            if (!new_name.isEmpty()) {
                values.put("name", new_name);
            }

            if (!new_phone.isEmpty()) {
                if (new_phone.length() != 10) {
                    phone_edit_profile.setError("Enter valid Phone number");
                    phone_edit_profile.requestFocus();
                    return;
                }
                values.put("phone", new_phone);
            }

            if (!new_address.isEmpty()) {
                values.put("address", new_address);
            }

            if (values.size() == 0) {
                Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
                return;
            } else {

                int rowsAffected = db.update("users", values, "email=?", new String[]{email});

                if (rowsAffected > 0) {
                    Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
                    Intent final_intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    final_intent.putExtra("user_email", email);
                    startActivity(final_intent);
                    finish();
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}