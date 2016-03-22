package com.shantanukamath.knockout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlannerActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter());
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 5;

        public SampleFragmentPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.create(position + 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + (position + 1);
        }
    }

    public static class PageFragment extends Fragment {
        public static final String ARG_PAGE = "ARG_PAGE";

        private int mPage;

        public static PageFragment create(int page) {
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            PageFragment fragment = new PageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPage = getArguments().getInt(ARG_PAGE);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_planner, container, false);
            LinearLayout parentL = (LinearLayout) view.findViewById(R.id.parentLLayout);
//            TextView textView = (TextView) view.findViewById(R.id.blah);
//            textView.setText("Fragment #" + mPage);
            parentL.setBackgroundColor(Color.parseColor("#E0E0E0"));
            LinearLayout[] blockSegment = new LinearLayout[15];
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

                TextView time = new TextView(getActivity());
                time.setText("9 AM");
                time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                time.setGravity(Gravity.CENTER);
                int timePadding = (int) (4 * scale + 0.5f);
                time.setPadding(0, 0, timePadding, 0);
                int zero = (int) (0 * scale + 0.5f);
                time.setLayoutParams(new LinearLayout.LayoutParams(zero, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
                blockSegment[i].addView(time);

                ////
                LinearLayout vert = new LinearLayout(getActivity());
                vert.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams vertParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,0.8f);
                ////

                TextView content = new TextView(getActivity());
                content.setText("Go cycling");
                content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                content.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                contentParams.gravity= Gravity.LEFT;
                content.setLayoutParams(contentParams);


                TextView desc = new TextView(getActivity());
                desc.setText("Enjoy the singapore skyline");
                desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                int descPadding = (int) (4*scale + 0.5f);
                desc.setPadding(0, 0, 0, descPadding);
                LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
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
}


