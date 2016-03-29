package com.shantanukamath.knockout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;

public class ItineraryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public static TextView TripName;
    static final Calendar toCalendar = Calendar.getInstance();
    static final Calendar fromCalendar = Calendar.getInstance();

    static int quantity1;
    static int quantity2;
    public static TextView q1, q2, hotelName;

    public ItineraryFragment() {
        // Required empty public constructor
    }


    public static ItineraryFragment newInstance(String param1, String param2) {
        ItineraryFragment fragment = new ItineraryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.fragment_itinerary, container, false);

        final TextView toText = (TextView) v.findViewById(R.id.to_text);
        final TextView fromText = (TextView) v.findViewById(R.id.from_text);

        final DatePickerDialog.OnDateSetListener todate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                toCalendar.set(Calendar.YEAR, year);
                toCalendar.set(Calendar.MONTH, monthOfYear);
                toCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                DrawerActivity.updateLabelTo(v);
            }

        };

        final DatePickerDialog.OnDateSetListener fromdate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                fromCalendar.set(Calendar.YEAR, year);
                fromCalendar.set(Calendar.MONTH, monthOfYear);
                fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                DrawerActivity.updateLabelFrom(v);
            }

        };

        if (toText != null) {
            toText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(getActivity(), todate, toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH),
                            toCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }
        if (fromText != null) {
            fromText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(getActivity(), fromdate, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH),
                            fromCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }

        q1 = (TextView) v.findViewById(R.id.quantity1);
        q2 = (TextView) v.findViewById(R.id.quantity2);
        TripName = (TextView) v.findViewById(R.id.TripName);
        hotelName = (TextView) v.findViewById(R.id.HotelName);
        Button submitBtn = (Button) v.findViewById(R.id.submitBtn);

        final CheckBox business = (CheckBox) v.findViewById(R.id.PBusiness);
        final CheckBox education = (CheckBox) v.findViewById(R.id.PEducation);
        final CheckBox leisure = (CheckBox) v.findViewById(R.id.PLeisure);

        submitBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getActivity(), PlannerActivity.class);
                        i.putExtra("HotelName", hotelName.getText().toString());
                        i.putExtra("ToDate", toText.getText().toString());
                        i.putExtra("FromDate", fromText.getText().toString());
                        i.putExtra("NoOfAdults", quantity1);
                        i.putExtra("NoOfKids", quantity2);
                        i.putExtra("ItinName", TripName.getText().toString());
                        ArrayList<String> checked = new ArrayList<String>();
                        if(leisure.isChecked())
                            checked.add("Leisure");
                        if(education.isChecked())
                            checked.add("Education");
                        if(business.isChecked())
                            checked.add("Business");
                        i.putExtra("Purpose", checked);
                        i.putExtra("UserName", ParseUser.getCurrentUser().getString("name"));
                        int days = Integer.parseInt(toText.getText().toString().substring(0,2))-Integer.parseInt(fromText.getText().toString().substring(0,2));

                        i.putExtra("noOfDays", ""+days);
                        Log.v("BLAH", "" + days);
                        startActivity(i);

                    }
                }
        );
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
