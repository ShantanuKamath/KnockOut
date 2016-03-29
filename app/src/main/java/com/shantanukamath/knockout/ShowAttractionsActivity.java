package com.shantanukamath.knockout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class ShowAttractionsActivity extends AppCompatActivity {

    ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hotel_list);
        try {
            JSONObject hotelData = new JSONObject(loadJSONFromAsset());
            JSONObject obj = hotelData.getJSONObject("Document");
            JSONArray hotelDetails = obj.getJSONArray("Placemark");
            for (int i = 0; i < hotelDetails.length(); i++) {
                JSONObject j_obj = hotelDetails.getJSONObject(i);
                String name = j_obj.getString("name");
                names.add(name);
            }
            Collections.sort(names.subList(0, names.size()));
            onPostExecute(names);
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    void onPostExecute(final ArrayList<String> nameList) {

        String[] names_list = new String[nameList.size()];
        names_list = nameList.toArray(names_list);
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
        ListView lw = (ListView) findViewById(R.id.rest_list_view);
        lw.setAdapter(rest_list_adapter);
        final AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.rest_name);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TEST", nameList.get((int) id));
                AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.rest_name);
                actv.setText(nameList.get((int) id));
            }
        });

        actv.setAdapter(rest_list_adapter);
        actv.setThreshold(1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Intent i = getIntent();
        if(i.getSerializableExtra("fromWhere").toString().equals("Drawer"))
            getMenuInflater().inflate(R.menu.attractions_menu, menu);
        else
            getMenuInflater().inflate(R.menu.menu_hotel_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        AutoCompleteTextView ac = (AutoCompleteTextView) findViewById(R.id.rest_name);
        String name = ac.getText().toString();

        //noinspection SimplifiableIfStatement
        if (id == R.id.next) {
            changeHotelData(name);
            return true;
        } else if (id == R.id.details) {
            Intent i = new Intent(this, MapsActivity.class);
            i.putExtra("name", name);
            i.putExtra("coordinates", getLatLong(name));
            i.putExtra("postal", getPostal(name));
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    public void changeHotelData(String name) {

        ItineraryFragment.hotelName.setText(name);
        super.onBackPressed();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("monuments.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String getLatLong(String name) {
        try {
            JSONObject hotelData = new JSONObject(loadJSONFromAsset());
            JSONObject obj = hotelData.getJSONObject("Document");
            JSONArray hotelDetails = obj.getJSONArray("Placemark");
            for (int i = 0; i < hotelDetails.length(); i++) {
                JSONObject j_obj = hotelDetails.getJSONObject(i);
                if (j_obj.getString("name").equals(name)) {
                    JSONObject point = j_obj.getJSONObject("Point");
                    return point.getString("coordinates");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }

    public String getPostal(String name) {
        try {
            JSONObject hotelData = new JSONObject(loadJSONFromAsset());
            JSONObject obj = hotelData.getJSONObject("Document");
            JSONArray hotelDetails = obj.getJSONArray("Placemark");
            for (int i = 0; i < hotelDetails.length(); i++) {
                JSONObject j_obj = hotelDetails.getJSONObject(i);
                if (j_obj.getString("name").equals(name)) {
                    JSONObject des = j_obj.getJSONObject("description");
                    JSONArray p = des.getJSONArray("p");
                    JSONObject p0 = p.getJSONObject(0);
                    JSONObject font = p0.getJSONObject("font");
                    String postal = font.getString("b");
                    Log.v("BLAH",(postal).substring(postal.length()-6, postal.length() ));
                    return (postal).substring(postal.length()-6, postal.length() );
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
