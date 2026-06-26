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

public class NearbyCentersFragment extends Fragment {

    RecyclerView centers_recycler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_nearby_centers, container, false);


        centers_recycler = view.findViewById(R.id.centers_recycler);

        centers_recycler.setLayoutManager(new LinearLayoutManager(getContext()));



        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("centers",null,null,null,null,null,null,null);

        centers_recycler.setAdapter(new CenterAdapter(getContext(), cursor));


        return view;
    }
}