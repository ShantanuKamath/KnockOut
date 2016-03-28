package com.shantanukamath.knockout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import java.util.Random;

public class PlannerActivity extends FragmentActivity {
    int NUM_ITEMS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);
        Intent i = getIntent();
        NUM_ITEMS = Integer.parseInt(i.getSerializableExtra("noOfDays").toString());
        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter pAdapter =new MyPagerAdapter(getSupportFragmentManager());
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
            return FirstFragment.newInstance(position, schedule);

//            switch (position) {
//                case 0: // Fragment # 0 - This will show FirstFragment
//                    String[] schedule = randomSchedule();
//                    return FirstFragment.newInstance(0, schedule);
//                case 1: // Fragment # 0 - This will show FirstFragment different title
//                    schedule = randomSchedule();
//                    return FirstFragment.newInstance(1, schedule);
//                default:
//                    return null;
//            }
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
            String[] clubbing = {"Sleep off early","Catch sleep early", "Go for a Movie","Go clubbing at Zouk","Go clubbing at Canvas","Go clubbing at Altimate"};
            Random r = new Random();
            int randint = Math.abs(r.nextInt()) % breakfast.length;
            schedule[0] = "Eat @"+breakfast[randint];

            randint = Math.abs(r.nextInt()) % lunch.length;
            schedule[4] = "Eat @"+lunch[randint];

            randint = Math.abs(r.nextInt()) % dinner.length;
            schedule[13] = "Eat @"+dinner[randint];

            randint = Math.abs(r.nextInt()) % clubbing.length;
            schedule[14] = clubbing[randint];
//
//            try {
//                JSONObject hotelData = new JSONObject(loadJSONFromAsset("Hotel.json"));
//                JSONObject obj = hotelData.getJSONObject("Document");
//                JSONArray hotelDetails = obj.getJSONArray("Placemark");
//                for (int i = 0; i < hotelDetails.length(); i++) {
//                    JSONObject j_obj = hotelDetails.getJSONObject(i);
//                    String name = j_obj.getString("name");
//                    schedule[i] (name);
//                }
//                onPostExecute(names);
//            } catch (JSONException e) {
//                e.printStackTrace();
//
//            }
            return schedule;
        }

//        public String loadJSONFromAsset(String filename) {
//            String json = null;
//            try {
//                InputStream is = this.getAssets().open(filename);
//                int size = is.available();
//                byte[] buffer = new byte[size];
//                is.read(buffer);
//                is.close();
//                json = new String(buffer, "UTF-8");
//
//            } catch (IOException ex) {
//                ex.printStackTrace();
//                return null;
//            }
//            return json;
//        }
    }
}


