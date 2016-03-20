package com.shantanukamath.knockout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView Name = (TextView) findViewById(R.id.profileName);
        if (Name != null) {
            Name.setText(ParseUser.getCurrentUser().getString("name"));
        }
    }
}
