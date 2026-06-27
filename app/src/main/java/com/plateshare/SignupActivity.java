package com.plateshare;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;



public class SignupActivity extends AppCompatActivity {


    EditText name_signup, email_signup, password_signup, confirm_password_signup, phone_signup, address_signup, organization_name_signup, contact_person_signup;
    TextView login_signup;
    Button signup_signup;

    RadioGroup role_group_signup, donor_type_group_signup;

    RadioButton radio_donor,radio_receiver, radio_individual, radio_organization;

    LinearLayout donor_type_section, organization_fields_section;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        organization_name_signup = findViewById(R.id.organization_name_signup);
        contact_person_signup = findViewById(R.id.contact_person_signup);

        role_group_signup = findViewById(R.id.role_group_signup);
        donor_type_group_signup = findViewById(R.id.donor_type_group_signup);

        radio_donor = findViewById(R.id.radio_donor);
        radio_receiver = findViewById(R.id.radio_receiver);
        radio_individual = findViewById(R.id.radio_individual);
        radio_organization = findViewById(R.id.radio_organization);

        donor_type_section = findViewById(R.id.donor_type_section);
        organization_fields_section = findViewById(R.id.organization_fields_section);

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


        role_group_signup.setOnCheckedChangeListener((group,checkedID)->{
            if (radio_donor.isChecked()){
                donor_type_section.setVisibility(View.VISIBLE);
            }

            if (radio_receiver.isChecked()){
                donor_type_section.setVisibility(View.GONE);
                organization_fields_section.setVisibility(View.GONE);
            }
        });

        donor_type_group_signup.setOnCheckedChangeListener((group, checkedId) -> {

            if (radio_individual.isChecked()){
                organization_fields_section.setVisibility(View.GONE);
            }

            if (radio_organization.isChecked()){
                organization_fields_section.setVisibility(View.VISIBLE);
            }



        });





        // signup

        signup_signup.setOnClickListener(view  ->{
            String name = name_signup.getText().toString().trim();
            String email = email_signup.getText().toString().trim();
            String password = password_signup.getText().toString().trim();
            String confirm_password = confirm_password_signup.getText().toString().trim();
            String phone = phone_signup.getText().toString().trim();
            String address = address_signup.getText().toString();
            String org_name = organization_name_signup.getText().toString().trim();
            String org_contact = contact_person_signup.getText().toString().trim();



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


            int role_group_checkedID = role_group_signup.getCheckedRadioButtonId();

            if (role_group_signup.getCheckedRadioButtonId() == -1){
                Toast.makeText(this, "Select your Role", Toast.LENGTH_SHORT).show();
                return;
            }

            String role = (role_group_checkedID == R.id.radio_donor) ? "donor" : "receiver";

            //

            String donor_type = null;

            if (role.equals("donor")){
                int donor_type_checkedID = donor_type_group_signup.getCheckedRadioButtonId();

                if (donor_type_checkedID == -1){
                    Toast.makeText(this, "Select Donor Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                donor_type = (donor_type_checkedID == R.id.radio_individual) ? "Individual" : "Organization";

                if (donor_type.equals("Organization")){
                    if (org_name.isEmpty()){
                        organization_name_signup.setError("Enter Name");
                        organization_name_signup.requestFocus();
                        return;
                    }
                    if (org_contact.isEmpty()){
                        contact_person_signup.setError("Enter Contact Person");
                        contact_person_signup.requestFocus();
                        return;
                    }

                }
            }


            try {
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();


                ContentValues values = new ContentValues();

                values.put("name", name);
                values.put("email", email);
                values.put("password", password);
                values.put("phone", phone);
                values.put("address", address);
                values.put("role",role);

                long result = db.insert("users", null, values);
                long result1 = 0;


                if (role.equals("donor") && result != -1){
                    ContentValues values1 = new ContentValues();

                    Cursor userCursor = db.query("users",null,"email=?",new String[] {email},null,null,null);
                    userCursor.moveToFirst();

                    int user_id_idx = userCursor.getColumnIndex("id");

                    String user_id = userCursor.getString(user_id_idx);

                    if (donor_type.equals("Organization")){
                        values1.put("organization_name",org_name);
                        values1.put("contact_person",org_contact);

                    }


                    values1.put("user_id",user_id);
                    values1.put("donor_type",donor_type);


                    result1 = db.insert("donor_profile",null,values1);


                }



                if (result == -1 || result1 == -1) {
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