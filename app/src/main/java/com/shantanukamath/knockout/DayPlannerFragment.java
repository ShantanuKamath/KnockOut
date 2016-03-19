package com.shantanukamath.knockout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class DayPlannerFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day_schedule, container, false);
        FrameLayout parentLayout = (FrameLayout) rootView.findViewById(R.id.parentLayout);
        Bundle args = getArguments();
//        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                Integer.toString(args.getInt(ARG_OBJECT)));

        LinearLayout[] lLayout = new LinearLayout[15];
        for (int i = 0; i < lLayout.length; i++) {
            lLayout[i] = new LinearLayout(getActivity());
            lLayout[i].setId(i);
            lLayout[i].setOrientation(LinearLayout.HORIZONTAL);
            if (i % 2 == 0) {
                lLayout[i].setBackgroundColor(Color.GREEN);
            } else {
                lLayout[i].setBackgroundColor(Color.MAGENTA);
            }
            LinearLayout.LayoutParams myLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lLayout[i].setLayoutParams(myLayoutParams);
            parentLayout.addView(lLayout[i]);

        }
        return rootView;
    }

}