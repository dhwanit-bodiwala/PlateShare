package com.plateshare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RatingActivity extends AppCompatActivity {


    TextView rating_donor_name;

    Button reliability_bad, reliability_average, reliability_good;
    Button communication_bad, communication_average, communication_good;
    Button submit_rating_btn;

    int reliabilityScore = -1;
    int communicationScore = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_screen);

        rating_donor_name = findViewById(R.id.rating_donor_name);

        reliability_bad = findViewById(R.id.reliability_bad);
        reliability_average = findViewById(R.id.reliability_average);
        reliability_good = findViewById(R.id.reliability_good);

        communication_bad = findViewById(R.id.communication_bad);
        communication_average = findViewById(R.id.communication_average);
        communication_good = findViewById(R.id.communication_good);

        submit_rating_btn = findViewById(R.id.submit_rating_btn);

        int donation_id = getIntent().getIntExtra("donation_id",-1);
        int donor_user_id = getIntent().getIntExtra("donor_user_id",-1);
        String donor_name = getIntent().getStringExtra("donor_name");
        int rater_user_id = getIntent().getIntExtra("rater_user_id",-1);
        String email = getIntent().getStringExtra("user_email");
        String user_role = getIntent().getStringExtra("user_role");

        rating_donor_name.setText(donor_name);


        reliability_bad.setOnClickListener(v -> {
            reliabilityScore = 1;
            resetReliabilityButtons();
            reliability_bad.setBackgroundResource(R.drawable.bg_button_primary);
            reliability_bad.setTextColor(getResources().getColor(R.color.white));
        });

        reliability_average.setOnClickListener(v -> {
            reliabilityScore = 3;
            resetReliabilityButtons();
            reliability_average.setBackgroundResource(R.drawable.bg_button_primary);
            reliability_average.setTextColor(getResources().getColor(R.color.white));
        });

        reliability_good.setOnClickListener(v -> {
            reliabilityScore = 5;
            resetReliabilityButtons();
            reliability_good.setBackgroundResource(R.drawable.bg_button_primary);
            reliability_good.setTextColor(getResources().getColor(R.color.white));
        });

        communication_bad.setOnClickListener(v -> {
            communicationScore = 1;
            resetCommunicationButtons();
            communication_bad.setBackgroundResource(R.drawable.bg_button_primary);
            communication_bad.setTextColor(getResources().getColor(R.color.white));
        });

        communication_average.setOnClickListener(v -> {
            communicationScore = 3;
            resetCommunicationButtons();
            communication_average.setBackgroundResource(R.drawable.bg_button_primary);
            communication_average.setTextColor(getResources().getColor(R.color.white));
        });

        communication_good.setOnClickListener(v -> {
            communicationScore = 5;
            resetCommunicationButtons();
            communication_good.setBackgroundResource(R.drawable.bg_button_primary);
            communication_good.setTextColor(getResources().getColor(R.color.white));
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        submit_rating_btn.setOnClickListener(v ->{

            if (reliabilityScore == -1){
                Toast.makeText(this, "Please rate Reliability", Toast.LENGTH_SHORT).show();
                return;
            }

            if (communicationScore == -1){
                Toast.makeText(this, "Please rate Communication", Toast.LENGTH_SHORT).show();
                return;
            }


            ContentValues values = new ContentValues();
            values.put("donation_id",donation_id);
            values.put("rated_by_user_id",rater_user_id);
            values.put("rated_user_id",donor_user_id);
            values.put("reliability",reliabilityScore);
            values.put("communication",communicationScore);

            db.insert("ratings",null,values);

            Toast.makeText(this, "Thanks for rating!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RatingActivity.this,HomeActivity.class);
            intent.putExtra("user_email",email);
            intent.putExtra("user_role",user_role);
            startActivity(intent);
            finish();

        });

    }

    private void resetReliabilityButtons() {
        reliability_bad.setBackgroundResource(R.drawable.bg_button_secondary);
        reliability_average.setBackgroundResource(R.drawable.bg_button_secondary);
        reliability_good.setBackgroundResource(R.drawable.bg_button_secondary);

        int saffron = getResources().getColor(R.color.saffron_dark);
        reliability_bad.setTextColor(saffron);
        reliability_average.setTextColor(saffron);
        reliability_good.setTextColor(saffron);
    }

    private void resetCommunicationButtons() {
        communication_bad.setBackgroundResource(R.drawable.bg_button_secondary);
        communication_average.setBackgroundResource(R.drawable.bg_button_secondary);
        communication_good.setBackgroundResource(R.drawable.bg_button_secondary);

        int saffron = getResources().getColor(R.color.saffron_dark);
        communication_bad.setTextColor(saffron);
        communication_average.setTextColor(saffron);
        communication_good.setTextColor(saffron);
    }


}