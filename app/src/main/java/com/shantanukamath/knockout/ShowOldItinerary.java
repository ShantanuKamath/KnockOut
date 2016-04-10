package com.shantanukamath.knockout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ShowOldItinerary extends AppCompatActivity {
    int NUM_ITEMS;
    String[] arrays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_old_itinerary);
        Bundle b= getIntent().getExtras();
        String[] schedule=b.getStringArray("Schedules");
        NUM_ITEMS=schedule.length/15;
        arrays = new String [NUM_ITEMS*15];
        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter pAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(pAdapter);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(ShowOldItinerary.this,
//                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_share_itin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.share) {
            Intent i = new Intent(this, SendItineraryActivity.class);
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
            Bundle b= getIntent().getExtras();
            String[] schedule=b.getStringArray("Schedules");
            for(int i =0; i<15; i++)
                arrays[position*15+i]= schedule[i];
            return FirstFragment.newInstance(position, schedule);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Day " + (position+1);
        }

    }
}


