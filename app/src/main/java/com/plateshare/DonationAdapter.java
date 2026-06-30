package com.plateshare;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {

    private Cursor cursor;
    private Context context;

    public DonationAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public DonationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donation, parent, false);
        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DonationViewHolder holder, int position) {
        cursor.moveToPosition(position);

        int food_name_idx = cursor.getColumnIndex("food_name");
        int quantity_idx = cursor.getColumnIndex("quantity");
        int expiry_idx = cursor.getColumnIndex("expiry_date");
        int food_type_idx = cursor.getColumnIndex("food_type");
        int allergies_idx = cursor.getColumnIndex("allergies");
        int status_idx = cursor.getColumnIndex("status");
        int reliability_idx = cursor.getColumnIndex("reliability");
        int communication_idx = cursor.getColumnIndex("communication");

        holder.donation_food_name.setText(cursor.getString(food_name_idx));
        holder.donation_quantity.setText(cursor.getString(quantity_idx));
        holder.donation_expiry.setText(cursor.getString(expiry_idx));
        holder.donation_food_type.setText(cursor.getString(food_type_idx));
        holder.donation_allergies.setText(cursor.getString(allergies_idx));
        holder.donation_status.setText(cursor.getString(status_idx));

        if (!cursor.isNull(reliability_idx)){
            int reliability = cursor.getInt(reliability_idx);
            int communication = cursor.getInt(communication_idx);
            int overall = reliability+communication;
            String evaluation;
            switch (overall){
                case 2:
                    evaluation = "Very Poor";
                    break;
                case 4:
                    evaluation = "Unsatisfactory";
                    break;
                case 6:
                    evaluation = "Satisfactory";
                    break;
                case 8:
                    evaluation = "Very Good";
                    break;
                default:
                    evaluation = "Outstanding";
            }
            holder.donation_rating_text.setText("Reliability: "+reliability+"/5\nCommunication: "+communication+"/5\nOverall: "+overall+"/10\nEvaluation: "+evaluation);
            holder.donation_rating_selection.setVisibility(View.VISIBLE);
        } else {
            holder.donation_rating_selection.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {

        TextView donation_food_name, donation_quantity, donation_expiry, donation_food_type, donation_allergies, donation_status, donation_rating_text;
        LinearLayout donation_rating_selection;

        public DonationViewHolder(View itemView) {
            super(itemView);
            donation_quantity = itemView.findViewById(R.id.donation_quantity);
            donation_expiry = itemView.findViewById(R.id.donation_expiry);
            donation_food_type = itemView.findViewById(R.id.donation_food_type);
            donation_allergies = itemView.findViewById(R.id.donation_allergies);
            donation_food_name = itemView.findViewById(R.id.donation_food_name);
            donation_status = itemView.findViewById(R.id.donation_status);
            donation_rating_text = itemView.findViewById(R.id.donation_rating_text);
            donation_rating_selection = itemView.findViewById(R.id.donation_rating_section);

        }
    }
}