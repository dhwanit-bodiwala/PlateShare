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

public class MyRequestsFragment extends Fragment{

    RecyclerView my_request_recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_requests, container, false);

        my_request_recycler = view.findViewById(R.id.my_requests_recycler);

        my_request_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String email = getArguments().getString("user_email");

        Cursor cursor = db.query("users",null,"email=?",new String[] {email},null,null,null);

        cursor.moveToFirst();

        int receiver_id_idx = cursor.getColumnIndex("id");

        String receiver_id = cursor.getString(receiver_id_idx);
        cursor.close();

        Cursor cursor1 = db.query("requests",null,"receiver_id=?", new String[] {receiver_id},null,null,null);

        my_request_recycler.setAdapter(new MyRequestAdapter(getContext(), cursor1));
        return view;

    }

}