package com.example.financemaneger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private final ArrayList web;
    private final ArrayList itemm;
    private final ArrayList amount;

    public CustomGrid(Context c, ArrayList web, ArrayList item, ArrayList amount) {
        mContext = c;
        this.web = web;
        this.itemm = item;
        this.amount = amount;
    }


    @Override
    public int getCount() {
        return web.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.card_layout, null);
            TextView textView = grid.findViewById(R.id.homeText);
            ImageView imageView = grid.findViewById(R.id.homeImage);
            TextView amount_z = grid.findViewById(R.id.totalAmount);
            textView.setText((String) web.get(position));
            amount_z.setText(amount.get(position) +"p");

            if ("Transport".equals(itemm.get(position))) {
                imageView.setImageResource(R.drawable.ic_transport);
            } else if ("Food".equals(itemm.get(position))) {
                imageView.setImageResource(R.drawable.ic_food);
            } else if ("House".equals(itemm.get(position))) {
                imageView.setImageResource(R.drawable.ic_house);
            } else if ("Entertainment".equals(itemm.get(position))) {
                imageView.setImageResource(R.drawable.ic_entertainment);
            } else if ("Education".equals(itemm.get(position))) {
                imageView.setImageResource(R.drawable.ic_education);
            } else if ("Charity".equals(itemm.get(position))) {
                imageView.setImageResource(R.drawable.ic_consultancy);
            } else if ("Apparel".equals(itemm.get(position))) {
                imageView.setImageResource(R.drawable.ic_shirt);
            } else if ("Health".equals(itemm.get(position))) {
                imageView.setImageResource(R.drawable.ic_health);
            } else if ("Personal".equals(itemm.get(position))) {
                imageView.setImageResource(R.drawable.ic_personalcare);
            } else if ("Other".equals(itemm.get(position))) {
                imageView.setImageResource(R.drawable.ic_other);
            }
        return grid;
    }
}
