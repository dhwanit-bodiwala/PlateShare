package com.plateshare;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterViewHolder> {

    private Cursor cursor;
    private Context context;

    public CenterAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public CenterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_center, parent, false);
        return new CenterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CenterViewHolder holder, int position) {
        cursor.moveToPosition(position);

        int nameIdx = cursor.getColumnIndex("name");
        int addressIdx = cursor.getColumnIndex("address");
        int contactIdx = cursor.getColumnIndex("contact");
        int stockIdx = cursor.getColumnIndex("stock_info");

        holder.center_name.setText(cursor.getString(nameIdx));
        holder.center_address.setText(cursor.getString(addressIdx));
        holder.center_contact.setText(cursor.getString(contactIdx));
        holder.center_stock.setText(cursor.getString(stockIdx));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class CenterViewHolder extends RecyclerView.ViewHolder {

        TextView center_name, center_address, center_contact, center_stock;

        public CenterViewHolder(View itemView) {
            super(itemView);
            center_name = itemView.findViewById(R.id.center_name);
            center_address = itemView.findViewById(R.id.center_address);
            center_contact = itemView.findViewById(R.id.center_contact);
            center_stock = itemView.findViewById(R.id.center_stock);
        }
    }
}