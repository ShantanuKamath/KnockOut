package com.shantanukamath.knockout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ParseUser user = ParseUser.getCurrentUser();
        if((boolean)user.getBoolean("SurveyDone"))
        {
            Intent i = new Intent(this, DrawerActivity.class);
            startActivity(i);
            this.finish();
        }
        TextView Greeting = (TextView) findViewById(R.id.helloUser);
        Greeting.setText("Hello, " + ParseUser.getCurrentUser().getString("name"));
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        } else if (id == R.id.logout) {
            ParseUser.logOut();
            Intent i = new Intent(this, DispatchActivity.class);
            startActivity(i);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void analyseSurvey(View view) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("SurveyDone", true);
        int outgoing=0;
        CheckBox c1= (CheckBox) findViewById(R.id.MorningBook);
        if(c1.isChecked()) outgoing--;
        c1= (CheckBox) findViewById(R.id.MorningBreakfast);
        if(c1.isChecked()) outgoing++;
        c1= (CheckBox) findViewById(R.id.MorningYoga);
        if(c1.isChecked()) outgoing++;
        c1= (CheckBox) findViewById(R.id.MorningSleep);
        if(c1.isChecked()) outgoing--;
        c1= (CheckBox) findViewById(R.id.AfternoonExcursion);
        if(c1.isChecked()) outgoing++;
        c1= (CheckBox) findViewById(R.id.AfternoonNap);
        if(c1.isChecked()) outgoing--;
        c1= (CheckBox) findViewById(R.id.AfternoonNetflix);
        if(c1.isChecked()) outgoing--;
        c1= (CheckBox) findViewById(R.id.AfternoonNGO);
        if(c1.isChecked()) outgoing++;
        c1= (CheckBox) findViewById(R.id.EveningBbq);
        if(c1.isChecked()) outgoing++;
        c1= (CheckBox) findViewById(R.id.EveningGames);
        if(c1.isChecked()) outgoing--;
        c1= (CheckBox) findViewById(R.id.EveningSport);
        if(c1.isChecked()) outgoing++;
        c1= (CheckBox) findViewById(R.id.EveningShopping);
        if(c1.isChecked()) outgoing++;
        c1= (CheckBox) findViewById(R.id.NightClubbing);
        if(c1.isChecked()) outgoing++;
        c1= (CheckBox) findViewById(R.id.NightCycling);
        if(c1.isChecked()) outgoing++;
        c1= (CheckBox) findViewById(R.id.NightShow);
        if(c1.isChecked()) outgoing--;
        c1= (CheckBox) findViewById(R.id.NightSleep);
        if(c1.isChecked()) outgoing--;

        user.put("OutgoingValue", outgoing);
        user.saveInBackground();

        Intent i = new Intent(this, DrawerActivity.class);
        i.putExtra("OUTGOING", outgoing);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.shantanukamath.knockout/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.shantanukamath.knockout/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
