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
    String userRole;

    TextView profile_btn;
    Button donate_btn;
    BottomNavigationView bottom_nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        userName = getIntent().getStringExtra("user_name");
        userEmail = getIntent().getStringExtra("user_email");
        userRole = getIntent().getStringExtra("user_role");

        profile_btn = findViewById(R.id.profile_btn);
        donate_btn = findViewById(R.id.donate_btn);
        bottom_nav = findViewById(R.id.bottom_nav);


        if (!userRole.equals("donor")) {
            donate_btn.setVisibility(View.GONE);
            bottom_nav.getMenu().findItem(R.id.nav_donations).setVisible(false);
        }




        MyDonationsFragment donationsFragment = new MyDonationsFragment();
        Bundle donationsBundle = new Bundle();
        donationsBundle.putString("user_email", userEmail);
        donationsFragment.setArguments(donationsBundle);

        NearbyCentersFragment centersFragment = new NearbyCentersFragment();
        Bundle centersBundle = new Bundle();
        centersBundle.putString("user_email", userEmail);
        centersFragment.setArguments(centersBundle);




        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,  centersFragment)
                .commit();



        bottom_nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_centers) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, centersFragment)
                        .commit();
            } else if (item.getItemId() == R.id.nav_donations) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, donationsFragment)
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