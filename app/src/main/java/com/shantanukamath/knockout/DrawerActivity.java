package com.shantanukamath.knockout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, WeatherFragment.OnFragmentInteractionListener, ItineraryFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener,ShowFriendsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //ALERT DIALOG : OUTGOING RESULTS OF SURVEY
        Intent intent = getIntent();
        if(intent.getSerializableExtra("OUTGOING")!=null) {
            Log.v("BLAH", "Intent not null");
            int outgoing = (int) intent.getSerializableExtra("OUTGOING");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Results are in!");
            if (outgoing > 0) {
                builder.setMessage("Wow your day looks fun!");
                builder.setPositiveButton("Yay!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
            }
            else {
                builder.setMessage("You're are not as outgoing as we thought you would be!");
                builder.setPositiveButton("Ill try harder!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
            }
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header=navigationView.getHeaderView(0);

        TextView drawerDetails = (TextView) header.findViewById(R.id.DrawerName);
        drawerDetails.setText(ParseUser.getCurrentUser().getString("name"));
        drawerDetails= (TextView) header.findViewById(R.id.DrawerEmail);
        drawerDetails.setText(ParseUser.getCurrentUser().getString("email"));



        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = WeatherFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        setTitle("Home");

        Intent i = getIntent();
        if(i.getSerializableExtra("addedFriends")!=null) {
            boolean flag = (boolean) i.getSerializableExtra("addedFriends");
            if (flag) {
                addedFriendsDone();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,PlannerActivity.class);
            startActivity(i);
            return true;
        }else if (id == R.id.logout) {
            ParseUser.logOut();
            Intent i = new Intent(this, DispatchActivity.class);
            startActivity(i);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_home) {
            fragmentClass = WeatherFragment.class;
        } else if (id == R.id.planATrip) {
            fragmentClass = ItineraryFragment.class;
        } else if (id == R.id.nav_friends) {
            fragmentClass = ShowFriendsFragment.class;
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            fragmentClass = SettingsFragment.class;
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }






    //////////////


    public void increment1(View view) {
        if (ItineraryFragment.quantity1 < 100)
            displayQuantity(++ItineraryFragment.quantity1, ItineraryFragment.q1);
        else {
            Context context = getApplicationContext();
            CharSequence text = "You cannot have more than 100 adults";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void decrement1(View view) {
        if (ItineraryFragment.quantity1 > 0)
            displayQuantity(--ItineraryFragment.quantity1, ItineraryFragment.q1);
        else {
            Context context = getApplicationContext();
            CharSequence text = "You cannot have less than 0 adults";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }
    public void increment2(View view) {
        if (ItineraryFragment.quantity2 < 100)
            displayQuantity(++ItineraryFragment.quantity2, ItineraryFragment.q2);
        else {
            Context context = getApplicationContext();
            CharSequence text = "You cannot have more than 100 kids";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void decrement2(View view) {
        if (ItineraryFragment.quantity2 > 0)
            displayQuantity(--ItineraryFragment.quantity2, ItineraryFragment.q2);
        else {
            Context context = getApplicationContext();
            CharSequence text = "You cannot have less than 0 kids";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }
    private void displayQuantity(int number, TextView tv) {
        tv.setText("" + number);
    }
    public static void updateLabelTo(View v) {
        TextView dateText = (TextView) v.findViewById(R.id.to_text);
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateText.setText(sdf.format(ItineraryFragment.toCalendar.getTime()));
    }

    public static void updateLabelFrom(View v) {
        TextView dateText = (TextView) v.findViewById(R.id.from_text);
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateText.setText(sdf.format(ItineraryFragment.fromCalendar.getTime()));
    }

    public void openHotelList(View view) {
        Intent i = new Intent(this, HotelListActivity.class);
        startActivity(i);
    }

    public void openProfile(View view) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void addedFriendsDone() {

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = ShowFriendsFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        setTitle("Friends");
    }

    public void logout(View view) {
        ParseUser.logOut();
        Intent i = new Intent(this, DispatchActivity.class);
        startActivity(i);
        this.finish();
    }


}
