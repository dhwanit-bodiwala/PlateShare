package com.plateshare;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BrowseDonationAdapter extends RecyclerView.Adapter<BrowseDonationAdapter.BrowseViewHolder> {

    private Cursor cursor;
    private Context context;

    private String email;

    public BrowseDonationAdapter(Context context, Cursor cursor, String email) {
        this.context = context;
        this.cursor = cursor;
        this.email = email;
    }

    @Override
    public BrowseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_browse_donation, parent, false);
        return new BrowseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BrowseViewHolder holder, int position) {
        cursor.moveToPosition(position);

        int id_idx = cursor.getColumnIndex("id");
        int food_name_idx = cursor.getColumnIndex("food_name");
        int food_type_idx = cursor.getColumnIndex("food_type");
        int quantity_idx = cursor.getColumnIndex("quantity");
        int expiry_idx = cursor.getColumnIndex("expiry_date");
        int donor_name_idx = cursor.getColumnIndex("donor_name");

        int donationId = cursor.getInt(id_idx);
        String foodName = cursor.getString(food_name_idx);
        String foodType = cursor.getString(food_type_idx);
        String quantity = cursor.getString(quantity_idx);
        String expiry = cursor.getString(expiry_idx);
        String donorName = cursor.getString(donor_name_idx);

        holder.food_name.setText(foodName);
        holder.food_type.setText(foodType);
        holder.quantity.setText("Qty: " + quantity);
        holder.expiry_date.setText("Expires: " + expiry);
        holder.donor_name.setText("DONATED BY: " + donorName);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DonationDetailsActivity.class);
            intent.putExtra("donation_id", donationId);
            intent.putExtra("user_email",email);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class BrowseViewHolder extends RecyclerView.ViewHolder {

        TextView food_name, food_type, quantity, expiry_date, donor_name;

        public BrowseViewHolder(View itemView){
            super(itemView);

             food_name = itemView.findViewById(R.id.browse_food_name);
             food_type = itemView.findViewById(R.id.browse_food_type);
             quantity = itemView.findViewById(R.id.browse_quantity);
             expiry_date = itemView.findViewById(R.id.browse_expiry);
             donor_name = itemView.findViewById(R.id.browse_donor_name);

        }
    }
}