package com.shantanukamath.knockout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectToShareActivity extends AppCompatActivity {


    ArrayList<String> finalArray = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_to_share);
        ParseUser user = ParseUser.getCurrentUser();
        ArrayList<String> Logs  = (ArrayList<String>) user.get("friends");
        LinearLayout lw = (LinearLayout) findViewById(R.id.guest_list);
        CheckBox checkBox;
        for(int i=0; i<Logs.size(); i++)
        {
            checkBox=new CheckBox(this);
            checkBox.setId(i);
            checkBox.setText(Logs.get(i));
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            checkBox.setOnClickListener(getOnClickDo(checkBox));
            lw.addView(checkBox);
        }
//        sendMail();
    }

    View.OnClickListener getOnClickDo(final Button b)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(finalArray.contains(b.getText().toString()))
                    finalArray.remove(b.getText().toString());
                else
                    finalArray.add(b.getText().toString());
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selecttoshare, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.send) {
            sendMail();
            Intent i = new Intent(this, DrawerActivity.class);
            i.putStringArrayListExtra("Email", finalArray);
            startActivity(i);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void sendMail() {
        final Map<String, String> params = new HashMap<>();
        params.put("text", "Dear Jagganath Pratyum,\n" +
                " \n" +
                "Congratulations! You suck.\n" +
                " \n" +
                "We are pleased to welcome you as one of the specially selected candidates as an esteemed member of BlitzKrieg Pvt. Ltd.\n" +
                " \n" +
                "Your admission to this Company is conditional upon your reply to this email latest by 25 March 2016 (Friday) to indicate your acceptance to the programme.");
        params.put("subject", "Blitzkrieg application status");
        params.put("fromEmail", "admin_applications@blitzkrieg.sg");
        params.put("fromName", "Blitzkrieg Sg");
        params.put("toEmail", ParseUser.getCurrentUser().get("email").toString());
        params.put("toName", "Pratyum the loser");
        ParseCloud.callFunctionInBackground("sendMail", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object response, ParseException exc) {
                Log.e("cloud code example", "response: " + response);
            }
        });
        Log.v("NAME", finalArray.size()+""
        );
        for(int i=0; i<finalArray.size(); i++)
        {
            final String[] email = {""};
            Log.v("NAME", finalArray.get(i).toString());


            ParseQuery<ParseUser> locationQuery = ParseUser.getQuery();
            locationQuery.whereEqualTo("name", finalArray.get(i)); // Set query so it finds the "username", and the user (user = "Username");
            locationQuery.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> scoreList, ParseException e) {
                    if (e == null) {
                        Map<String, String> params = new HashMap<>();
                        for (ParseObject parseObj : scoreList) {
                            params.put("text", "Dear Jagganath Pratyum,\n" +
                                    " \n" +
                                    "Congratulations! You suck.\n" +
                                    " \n" +
                                    "We are pleased to welcome you as one of the specially selected candidates as an esteemed member of BlitzKrieg Pvt. Ltd.\n" +
                                    " \n" +
                                    "Your admission to this Company is conditional upon your reply to this email latest by 25 March 2016 (Friday) to indicate your acceptance to the programme.");
                            params.put("subject", "Blitzkrieg application status");
                            params.put("fromEmail", "admin_applications@blitzkrieg.sg");
                            params.put("fromName", "Blitzkrieg Sg");
                            params.put("toEmail", ParseUser.getCurrentUser().get("email").toString());
                            params.put("toName", "Pratyum the loser");
                            email[0] = parseObj.get("email").toString();
                            Log.v("BAK", email[0]);
                            params.put("toEmail", email[0]);
                            ParseCloud.callFunctionInBackground("sendMail", params, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object response, ParseException exc) {
                                    Log.e("cloud code example", "response: " + response);
                                }
                            });
                        }
                    }
                }
            });
        }

    }
}
