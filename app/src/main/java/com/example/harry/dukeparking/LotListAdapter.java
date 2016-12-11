package com.example.harry.dukeparking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

/**
 * Created by Harry on period10/period20/period16.
 */
public class LotListAdapter extends ArrayAdapter<Lot> {
    private List<Lot> lotList;
    private Context context;

    public LotListAdapter(List<Lot> lotList, Context context) {
        super(context, 0, lotList);
        this.lotList = lotList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lotList.size();
    }

    @Override
    public Lot getItem(int pos) {
        return lotList.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lot_list_item, null);
        }

        TextView lotNameView = (TextView)view.findViewById(R.id.lot_name);
        lotNameView.setText(lotList.get(position).getName());

        TextView lotInfoView = (TextView)view.findViewById(R.id.lot_info);
        double percentAvailable = 100;
        if(lotList.get(position).getCapacity()!=0){
            percentAvailable = 100*((double)(lotList.get(position).getCapacity()-lotList.get(position).getCurrent())/(double)lotList.get(position).getCapacity());
        }
        ImageView indicator = (ImageView) view.findViewById(R.id.indicator);
        if(percentAvailable>=75){
            indicator.setImageResource(R.drawable.green);
        }
        else if(percentAvailable<75&&percentAvailable>25){
            indicator.setImageResource(R.drawable.yellow);
        }
        else if(percentAvailable <= 25){
            indicator.setImageResource(R.drawable.red);
        }
        if(lotList.get(position).getCurrent()==-1){
            lotInfoView.setText("Open Parking Lot");
        }
        else{
            lotInfoView.setText((int)percentAvailable+"% Available");
        }

        return view;
    }

}
