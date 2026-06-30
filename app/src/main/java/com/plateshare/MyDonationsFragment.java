package com.plateshare;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyDonationsFragment extends Fragment {


    RecyclerView donation_recycler;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_donations, container, false);

        donation_recycler = view.findViewById(R.id.donation_recycler);

        donation_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String email = getArguments().getString("user_email");
        Cursor userCursor = db.query("users", null, "email=?", new String[]{email}, null, null, null);
        userCursor.moveToFirst();
        String userId = userCursor.getString(userCursor.getColumnIndex("id"));
        userCursor.close();


        Cursor cursor1 = db.rawQuery(
                "SELECT donations.*, ratings.reliability, ratings.communication " +
                        "FROM donations " +
                        "LEFT JOIN ratings ON donations.id = ratings.donation_id " +
                        "WHERE donations.user_id = ?",
                new String[]{userId}
        );

        donation_recycler.setAdapter(new DonationAdapter(getContext(), cursor1));

        return view;
    }
}