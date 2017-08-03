package com.stock.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stock.R;
import com.stock.entity.Meat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by m on 02.08.2017.
 */

public class MeatAdapter extends BaseAdapter {
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private Context context;
    private List<Meat> meats;


    public MeatAdapter(Context context, List<Meat> meats) {
        this.context = context;
        this.meats = meats;
    }

    @Override
    public int getCount() {
        return meats.size();
    }

    @Override
    public Object getItem(int position) {
        return meats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Meat meat = meats.get(position);
        String dateC = null, wiegthC = null, priceC = null, dateO = null, wiegthO = null, priceO = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_adapter, null);
        }
        if (meat.getDateComing() != 0) {
            dateC = sdf.format(new Date(meat.getDateComing()));
        }
        if (meat.getWeigthComing() != 0) {
            wiegthC = meat.getWeigthComing() + " кг";
        }
        if (meat.getPriceComing() != 0) {
            priceC = meat.getPriceComing() + " грн";
        }
        if (meat.getDateOutput() != 0) {
            dateO = sdf.format(new Date(meat.getDateOutput()));
        }
        if (meat.getWeigthOutput() != 0) {
            wiegthO = meat.getWeigthOutput() + " кг";
        }
        if (meat.getPriceOutput() != 0) {
            priceO = meat.getPriceOutput() + " грн";
        }

        ((TextView) convertView.findViewById(R.id.date_coming)).setText(dateC);
        ((TextView) convertView.findViewById(R.id.weigth_coming)).setText(wiegthC );
        ((TextView) convertView.findViewById(R.id.price_coming)).setText(priceC );
        ((TextView) convertView.findViewById(R.id.date_output)).setText(dateO);
        ((TextView) convertView.findViewById(R.id.weigth_output)).setText(wiegthO );
        ((TextView) convertView.findViewById(R.id.price_output)).setText(priceO );
        return convertView;
    }

    public void addMeatsToAdapter(ArrayList<Meat> list) {
        meats.addAll(list);
        this.notifyDataSetChanged();
    }
}
