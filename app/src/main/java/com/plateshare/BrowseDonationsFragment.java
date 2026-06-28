package com.plateshare;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BrowseDonationsFragment extends Fragment {

    RecyclerView browse_donations_recycler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_browse_donations, container, false);

        String email = getArguments().getString("user_email");

        browse_donations_recycler = view.findViewById(R.id.browse_donations_recycler);

        browse_donations_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT donations.id, donations.food_name, donations.quantity, " +
                        "donations.food_type, donations.expiry_date, users.name AS donor_name " +
                        "FROM donations " +
                        "JOIN users ON donations.user_id = users.id " +
                        "WHERE donations.status = ?",
                new String[]{"Available"}
        );

        browse_donations_recycler.setAdapter(new BrowseDonationAdapter(getContext(), cursor, email));



       return view;
    }
}