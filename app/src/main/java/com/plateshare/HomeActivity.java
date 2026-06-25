package com.plateshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {


    String userName;
    String userEmail;

    TextView profile_btn;
    Button donate_btn;
    BottomNavigationView bottom_nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        userName = getIntent().getStringExtra("user_name");
        userEmail = getIntent().getStringExtra("user_email");

        profile_btn = findViewById(R.id.profile_btn);
        donate_btn = findViewById(R.id.donate_btn);
        bottom_nav = findViewById(R.id.bottom_nav);


        if (userName.equals("guest")) {
            donate_btn.setVisibility(View.GONE);
        }


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,  new NearbyCentersFragment())
                .commit();



        bottom_nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_centers) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new NearbyCentersFragment())
                        .commit();
            } else if (item.getItemId() == R.id.nav_donations) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new MyDonationsFragment())
                        .commit();
            }
            return true;
        });



        profile_btn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        donate_btn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, DonateFoodActivity.class);
            intent.putExtra("user_email",userEmail);
            startActivity(intent);
        });





    }
}