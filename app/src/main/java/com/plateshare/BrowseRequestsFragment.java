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

public class BrowseRequestsFragment extends Fragment {

    RecyclerView browse_requests_recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_browse_requests, container, false);

        String email = getArguments().getString("user_email");

        browse_requests_recycler = view.findViewById(R.id.browse_requests_recycler);

        browse_requests_recycler.setLayoutManager(new LinearLayoutManager(getContext()));


        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT requests.id, requests.title, requests.description, " +
                        "requests.quantity_needed, requests.location, users.name AS receiver_name " +
                        "FROM requests " +
                        "JOIN users ON requests.receiver_id = users.id " +
                        "WHERE requests.status = ?",
                new String[]{"Open"}
        );

        browse_requests_recycler.setAdapter(new BrowseRequestAdapter(getContext(), cursor, email));




        return view;

    }


}