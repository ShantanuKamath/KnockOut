package com.shantanukamath.knockout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

public class PlannerActivity extends AppCompatActivity {
    int NUM_ITEMS;
    String[] arrays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);
        Intent i = getIntent();
        NUM_ITEMS = Integer.parseInt(i.getSerializableExtra("noOfDays").toString());
        arrays = new String [NUM_ITEMS*15];
        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter pAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(pAdapter);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(PlannerActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_planner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            ParseObject Itineraries = new ParseObject("Itineraries");
            Intent i = getIntent();
            Itineraries.put("HotelName", i.getSerializableExtra("HotelName").toString());
            Log.v("BLAH", i.getSerializableExtra("HotelName").toString());
            Itineraries.put("ToDate", i.getSerializableExtra("ToDate").toString());
            Itineraries.put("FromDate", i.getSerializableExtra("FromDate").toString());
            Itineraries.put("NoOfAdults", i.getSerializableExtra("NoOfAdults").toString());
            Itineraries.put("NoOfKids", i.getSerializableExtra("NoOfKids").toString());
            Itineraries.put("ItinName", i.getSerializableExtra("ItinName").toString());
            Itineraries.put("Purpose", i.getSerializableExtra("Purpose"));
            Itineraries.put("UserName", ParseUser.getCurrentUser().getString("name"));
            Itineraries.put("Schedules", Arrays.asList(arrays));
            Itineraries.saveInBackground();
            i = new Intent(this, DrawerActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
//        private int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;

        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            String[] schedule = randomSchedule();
            for(int i =0; i<15; i++)
            arrays[position*15+i]= schedule[i];
            return FirstFragment.newInstance(position, schedule);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

        String[] randomSchedule() {
            String[] schedule = new String[15];
            for (int i = 0; i < 15; i++)
                schedule[i] = "";
            String[] breakfast = {"Wild Honey", "Two Men Bagel House", "Clinton Street Baking Co.", "Boomarang", "Privé Café"};
            String[] lunch = {"Crust", "Yoshimaru Ramen Bar", "Breko Cafe", "Cha Cha Cha", "Everything With Fries", "Da Paolo Pizza Bar"};
            String[] dinner = {"Hai Di Lao", "Beer Market", "Pind Balluchi Bar & Grill", "The Steakhouse", "Hooters", "Moghul Mahal"};
            String[] clubbing = {"Sleep off early", "Catch sleep early", "Go for a Movie", "Go clubbing at Zouk", "Go clubbing at Canvas", "Go clubbing at Altimate"};
            Random r = new Random();
            int randint = Math.abs(r.nextInt()) % breakfast.length;
            schedule[0] = "Eat @" + breakfast[randint];

            randint = Math.abs(r.nextInt()) % lunch.length;
            schedule[4] = "Eat @" + lunch[randint];

            randint = Math.abs(r.nextInt()) % dinner.length;
            schedule[13] = "Eat @" + dinner[randint];

            randint = Math.abs(r.nextInt()) % clubbing.length;
            schedule[14] = clubbing[randint];
            for (int i = 0; i < 15; i++) {
                if (schedule[i].equals("")) {
                    try {
                        JSONObject hotelData = new JSONObject(loadJSONFromAsset("monuments.json"));
                        JSONObject obj = hotelData.getJSONObject("Document");
                        JSONArray hotelDetails = obj.getJSONArray("Placemark");
                        int j = Math.abs(r.nextInt()) % hotelDetails.length();
                        JSONObject j_obj = hotelDetails.getJSONObject(j);
                        String name = j_obj.getString("name");
                        schedule[i] = "Visit the " + name;

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }
            return schedule;

        }

        public String loadJSONFromAsset(String filename) {
            String json = null;
            try {
                InputStream is = getAssets().open(filename);
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
    }
}


