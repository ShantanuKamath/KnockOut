package com.shantanukamath.knockout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FirstFragment extends Fragment {
    // Store instance variables
    private String[] schedule;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static FirstFragment newInstance(int page, String[] s) {
        FirstFragment fragmentFirst = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putStringArray("someTitle", s);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        schedule = getArguments().getStringArray("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        LinearLayout parentL = (LinearLayout) view.findViewById(R.id.parentLLayout);
//            TextView textView = (TextView) view.findViewById(R.id.blah);
//            textView.setText("Fragment #" + mPage);
        parentL.setBackgroundColor(Color.parseColor("#E0E0E0"));
        LinearLayout[] blockSegment = new LinearLayout[15];
        int timeNumber = 9;
        String timeStr = "\nAM";
        for (int i = 0; i < blockSegment.length; i++) {
            blockSegment[i] = new LinearLayout(getActivity());
            blockSegment[i].setId(i);
            blockSegment[i].setOrientation(LinearLayout.HORIZONTAL);
            blockSegment[i].setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams blockSegParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            float scale = getResources().getDisplayMetrics().density;
            blockSegParams.setMargins(0, 0, 0, 10);
            blockSegment[i].setLayoutParams(blockSegParams);
            int lLayoutPadding = (int) (8*scale + 0.5f);
            blockSegment[i].setPadding(lLayoutPadding, lLayoutPadding, 0, lLayoutPadding);
            blockSegment[i].setClickable(true);
            blockSegment[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = new Intent(getActivity(), DrawerActivity.class);
//                    startActivity(i);

                }
            });
            TextView time = new TextView(getActivity());
            time.setText(timeNumber+""+timeStr);
            timeNumber++;
            if(timeNumber >12)
            {
                timeNumber-=12;
                timeStr = "\nPM";
            }
            time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            time.setGravity(Gravity.CENTER);
            int timePadding = (int) ( 8* scale + 0.5f);
            time.setPadding(0, 0, timePadding, 0);
            int zero = (int) (0 * scale + 0.5f);
            time.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            blockSegment[i].addView(time);

            ////
            LinearLayout vert = new LinearLayout(getActivity());
            vert.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams vertParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,0.9f);
            ////

            TextView content = new TextView(getActivity());
            content.setText(schedule[i]);
            content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            content.setTextColor(Color.parseColor("#000000"));
            LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            contentParams.gravity= Gravity.LEFT;
            content.setLayoutParams(contentParams);


            TextView desc = new TextView(getActivity());
            desc.setText("Enjoy the singapore skyline");
            desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            int descPadding = (int) (4*scale + 0.5f);
            desc.setPadding(0, 0, 0, descPadding);
            LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            descParams.gravity= Gravity.LEFT;
            desc.setLayoutParams(descParams);
            vert.addView(content);
            vert.addView(desc);
            blockSegment[i].addView(vert, vertParams);
            parentL.addView(blockSegment[i]);
        }
        return view;
    }
}