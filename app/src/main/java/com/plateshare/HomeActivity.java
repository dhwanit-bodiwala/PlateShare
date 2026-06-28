package com.plateshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {


    String userName;
    String userEmail;
    String userRole;

    TextView profile_btn;
    Button donate_btn, create_request_btn;
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
        create_request_btn = findViewById(R.id.create_request_btn);
        bottom_nav = findViewById(R.id.bottom_nav);


        NearbyCentersFragment centersFragment = new NearbyCentersFragment();
        Bundle centersBundle = new Bundle();
        centersBundle.putString("user_email", userEmail);
        centersFragment.setArguments(centersBundle);


        Fragment donationsTabFragment;

        if (userRole.equals("donor")) {
            MyDonationsFragment donationsFragment = new MyDonationsFragment();
            Bundle donationsBundle = new Bundle();
            donationsBundle.putString("user_email", userEmail);
            donationsFragment.setArguments(donationsBundle);
            donationsTabFragment = donationsFragment;
        } else if (userRole.equals("receiver")) {
            BrowseDonationsFragment browseFragment = new BrowseDonationsFragment();
            Bundle browseBundle = new Bundle();
            browseBundle.putString("user_email", userEmail);
            browseFragment.setArguments(browseBundle);
            donationsTabFragment = browseFragment;
        } else {
            donationsTabFragment = null;
        }

        Fragment requestsTabFragment;

        if (userRole.equals("donor")) {
            BrowseRequestsFragment browseRequestsFragment = new BrowseRequestsFragment();
            Bundle browseRequestsBundle = new Bundle();
            browseRequestsBundle.putString("user_email", userEmail);
            browseRequestsFragment.setArguments(browseRequestsBundle);
            requestsTabFragment = browseRequestsFragment;
        } else if (userRole.equals("receiver")) {
            MyRequestsFragment myRequestsFragment = new MyRequestsFragment();
            Bundle myRequestsBundle = new Bundle();
            myRequestsBundle.putString("user_email", userEmail);
            myRequestsFragment.setArguments(myRequestsBundle);
            requestsTabFragment = myRequestsFragment;
        } else {
            requestsTabFragment = null;
        }


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, centersFragment)
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
                        .replace(R.id.fragment_container, donationsTabFragment)
                        .commit();
            } else if (item.getItemId() == R.id.nav_requests){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,requestsTabFragment)
                        .commit();
            }
            return true;
        });


        if (userRole.equals("guest")) {
            donate_btn.setVisibility(View.GONE);
            bottom_nav.getMenu().findItem(R.id.nav_donations).setVisible(false);
            bottom_nav.getMenu().findItem(R.id.nav_requests).setVisible(false);
        } else if (userRole.equals("receiver")) {
            donate_btn.setVisibility(View.GONE);
            create_request_btn.setVisibility(View.VISIBLE);
            bottom_nav.getMenu().findItem(R.id.nav_donations).setTitle("Browse Donations");
        } else if (userRole.equals("donor")){
            donate_btn.setVisibility(View.VISIBLE);
            create_request_btn.setVisibility(View.GONE);
        }


        profile_btn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        donate_btn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, DonateFoodActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        create_request_btn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CreateRequestActivity.class);
            intent.putExtra("user_email",userEmail);
            startActivity(intent);
        });

    }
}