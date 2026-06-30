package com.plateshare;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestAdapter.RequestViewHolder> {

    private Cursor cursor;
    private Context context;

    public MyRequestAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {
        cursor.moveToPosition(position);

        int title_idx = cursor.getColumnIndex("title");
        int status_idx = cursor.getColumnIndex("status");
        int description_idx = cursor.getColumnIndex("description");
        int quantity_idx = cursor.getColumnIndex("quantity_needed");
        int location_idx = cursor.getColumnIndex("location");

        String title = cursor.getString(title_idx);
        String status = cursor.getString(status_idx);
        String description = cursor.getString(description_idx);
        String quantity = cursor.getString(quantity_idx);
        String location = cursor.getString(location_idx);

        holder.my_request_title.setText(title);
        holder.my_request_status.setText(status);
        holder.my_request_description.setText(description);
        holder.my_request_quantity.setText(quantity);
        holder.my_request_location.setText(location);



    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView my_request_title, my_request_status, my_request_description, my_request_quantity, my_request_location;

        public RequestViewHolder(View itemView) {
            super(itemView);

            my_request_title = itemView.findViewById(R.id.my_request_title);
            my_request_status = itemView.findViewById(R.id.my_request_status);
            my_request_description = itemView.findViewById(R.id.my_request_description);
            my_request_quantity = itemView.findViewById(R.id.my_request_quantity);
            my_request_location = itemView.findViewById(R.id.my_request_location);

        }
    }
}