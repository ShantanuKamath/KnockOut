package com.shantanukamath.knockout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SendItineraryActivity extends AppCompatActivity {

    ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_itinerary);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Itineraries");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    for (ParseObject parseObj : scoreList) {
                        names.add(parseObj.get("ItinName").toString());
                    }
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                    Log.v("BAK", names.size() + "");
                    Collections.sort(names.subList(0, names.size()));
                    onPostExecute();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

    void onPostExecute() {

        String[] names_list = new String[names.size()];
        Log.v("BAK1", ""+names_list.length);
        names_list = names.toArray(names_list);
        ArrayAdapter<String> rest_list_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, names_list) {
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
            /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        ListView lw = (ListView) findViewById(R.id.lv_itinerary);
        lw.setAdapter(rest_list_adapter);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                final ParseQuery<ParseObject> query = ParseQuery.getQuery("Itineraries");
                query.whereEqualTo("ItinName", names.get((int) id));
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> scoreList, ParseException e) {
                        if (e == null) {
                            for (ParseObject parseObj : scoreList) {
                                ArrayList<String> schedules = (ArrayList<String>)parseObj.get("Schedules");
                                String[] array = schedules.toArray(new String[schedules.size()]);
                                int days = (schedules.size()/15);

                                Bundle b=new Bundle();
                                b.putStringArray("Schedules", array);
                                Intent i=new Intent(getBaseContext(), ShowOldItinerary.class);
                                i.putExtras(b);
                                startActivity(i);
                                Log.v("BAK2", schedules.size()+"");
                            }
                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                        }
                    }
                });

                Log.d("TEST", names.get((int) id));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Intent i = getIntent();
        if (i.getSerializableExtra("fromWhere").toString().equals("Drawer"))
            getMenuInflater().inflate(R.menu.attractions_menu, menu);
        else
            getMenuInflater().inflate(R.menu.menu_hotel_list, menu);
        return true;
    }
}