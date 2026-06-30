package com.plateshare;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BrowseRequestAdapter extends RecyclerView.Adapter<BrowseRequestAdapter.BrowseViewHolder> {

    private Cursor cursor;
    private Context context;
    private String email;

    public BrowseRequestAdapter(Context context, Cursor cursor, String email) {
        this.context = context;
        this.cursor = cursor;
        this.email = email;
    }

    @Override
    public BrowseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_browse_request, parent, false);
        return new BrowseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BrowseViewHolder holder, int position) {
        cursor.moveToPosition(position);

        int id_idx = cursor.getColumnIndex("id");
        int title_idx = cursor.getColumnIndex("title");
        int description_idx = cursor.getColumnIndex("description");
        int quantity_needed_idx = cursor.getColumnIndex("quantity_needed");
        int location_idx = cursor.getColumnIndex("location");
        int receiver_name_idx = cursor.getColumnIndex("receiver_name");


        int request_id = cursor.getInt(id_idx);
        String title = cursor.getString(title_idx);
        String description = cursor.getString(description_idx);
        String quantity_needed = cursor.getString(quantity_needed_idx);
        String location = cursor.getString(location_idx);
        String receiver_name = cursor.getString(receiver_name_idx);

        holder.browse_request_title.setText(title);
        holder.browse_request_description.setText(description);
        holder.browse_request_quantity.setText("Needs : "+quantity_needed);
        holder.browse_request_location.setText("Location : "+location);
        holder.browse_request_receiver_name.setText("Requested by : "+receiver_name);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RequestDetailsActivity.class);
            intent.putExtra("request_id", request_id);
            intent.putExtra("user_email",email);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class BrowseViewHolder extends RecyclerView.ViewHolder {

        TextView browse_request_title, browse_request_description, browse_request_quantity, browse_request_location, browse_request_receiver_name;

        public BrowseViewHolder(View itemView){
            super(itemView);

            browse_request_title = itemView.findViewById(R.id.browse_request_title);
            browse_request_description = itemView.findViewById(R.id.browse_request_description);
            browse_request_quantity = itemView.findViewById(R.id.browse_request_quantity);
            browse_request_location = itemView.findViewById(R.id.browse_request_location);
            browse_request_receiver_name = itemView.findViewById(R.id.browse_request_receiver_name);


        }
    }
}