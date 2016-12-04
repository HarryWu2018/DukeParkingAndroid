package com.example.harry.dukeparking;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;

public class LotListActivity extends AppCompatActivity {
    //key String: lot UID; value String: name, Stirng: capacity
    public final static String LOT_ID = "LOT_IDDDD";
    public static HashMap<String,Lot> lotMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AVOSCloud.initialize(this, "J7yIyoEr5rUfhlvoby9ca8Q9-gzGzoHsz", "7BUbdP89682ApkdG9LoagYf2");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_list);
        initialize();
        pullServerData();
        final List<Lot> lotList = new ArrayList<>(lotMap.values());
        Collections.sort(lotList, new Comparator<Lot>() {

            public int compare(Lot o1, Lot o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        LotListAdapter adapter = new LotListAdapter(lotList,getApplicationContext());
        final ListView lView = (ListView) findViewById(R.id.parking_lot_list);
        lView.setAdapter(adapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(getApplicationContext(), LotDetailActivity.class);
                intent.putExtra(LOT_ID, lotList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    public void initialize(){
        lotMap = new HashMap<>();
        InputStream inputStream = getResources().openRawResource(R.raw.facilitylookup);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                Log.i("Test",row[0]+"      "+row[1]);
                int cap;
                if(row.length>2){
                    cap = Integer.parseInt(row[2]);
                }
                else{
                    cap = 0;
                }
                if(cap!=0) {
                    Lot oneLot = new Lot(row[1], row[0], cap);
                    lotMap.put(row[1], oneLot);
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
    }

    public void updateData(){
        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int rawId = getResources().getIdentifier("period" + hours, "raw", getPackageName());
        InputStream inputStream = getResources().openRawResource(rawId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            int jump = 0;
            while ((csvLine = reader.readLine()) != null) {
                if(jump!=0){
                    String[] row = csvLine.split(",");
                    Log.i("Test", row[0] + "      "+row[1]);
                    Lot oneLot = lotMap.get(row[0]);
                    oneLot.setCurrent(Integer.parseInt(row[1]));
                }
                jump = 1;
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
    }

    public void pullServerData(){
        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);
        AVQuery<AVObject> query = new AVQuery<>("dukeParkInfo");
        query.whereEqualTo("period", hours);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject one : list) {
                    Log.i("Server data", one.getInt("parkinglot") + ", " + one.getInt("numberofcars"));
                    Lot oneLot = lotMap.get("" + one.getInt("parkinglot"));
                    oneLot.setCurrent(one.getInt("numberofcars"));
                }
            }
        });

    }
}
