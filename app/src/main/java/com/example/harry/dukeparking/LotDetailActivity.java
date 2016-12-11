package com.example.harry.dukeparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harry on period11/period10/period16.
 */
public class LotDetailActivity extends AppCompatActivity {
    private Lot lot;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lot_detail);
        lot = LotListActivity.lotMap.get(getIntent().getStringExtra(LotListActivity.LOT_ID));
        TextView name = (TextView)findViewById(R.id.lot_detail_name);
        TextView capacity = (TextView)findViewById(R.id.lot_detail_capacity);
        TextView available = (TextView)findViewById(R.id.lot_detail_available);
        TextView availableLbl = (TextView) findViewById(R.id.lotAvailableLbl);
        name.setText(lot.getName());
        capacity.setText(Integer.toString(lot.getCapacity()));
        if(lot.getCurrent()==-1){
            availableLbl.setText("Open Parking Lot");
            available.setText("");
        }
        else{
            available.setText(Integer.toString(lot.getCapacity()-lot.getCurrent()));
        }
    }
}
