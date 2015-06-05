package com.phonetact.phonetact.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.phonetact.R;
import com.phonetact.phonetact.Utils.ContactsQuery;
import com.phonetact.phonetact.Utils.MaterialRippleLayout;
import com.phonetact.phonetact.Utils.SlidingTabLayout;
import com.phonetact.phonetact.Utils.SlidingTabLayoutTextview;
import com.software.shell.fab.ActionButton;

/**
 * Created by hp on 30/05/2015.
 */
public class ContatcFragment extends Fragment {
    //element layout
    private ViewPager mPager;
    private SlidingTabLayoutTextview mTabs;
    private ActionButton actionButton;

    //adaptater

    //variable
    protected static final String ARG_PAGE = "ARG_PAGE";
    LayoutInflater inflate;
    private int mPage;

    public static ContatcFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ContatcFragment fragment = new ContatcFragment();
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
        inflate = inflater;
        View view = inflater.inflate(R.layout.fragment_contact, container,
                false);
        FloatingActionsMenu flotingmenu = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions);
        intialiserbutton(view);
        //set viewpage and tabs
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        mTabs = (SlidingTabLayoutTextview) view.findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mTabs.setViewPager(mPager);
        //mTabs.setBackgroundColor(getResources().getColor(R.color.color_toolbar));
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.transparent));
        mTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return view;
    }


    private void intialiserbutton(View v) {
        FloatingActionButton actionButton;
        actionButton = (FloatingActionButton) v.findViewById(R.id.action_b);
        actionButton.setImageResource(R.drawable.fab_plus_icon);
        actionButton.setColorNormal(getResources().getColor(R.color.color_vert_indicator));
        actionButton.setColorPressed(Color.parseColor("#808DC73F"));

        FloatingActionButton actionButton1;
        actionButton1 = (FloatingActionButton) v.findViewById(R.id.action_a);
        actionButton1.setImageResource(R.drawable.magnify);
        actionButton1.setColorNormal(getResources().getColor(R.color.color_vert_indicator));
        actionButton1.setColorPressed(Color.parseColor("#808DC73F"));
        actionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new TestFragment().getPhoneBook(Uri.withAppendedPath(ContactsQuery.FILTER_URI, Uri.encode("j")));
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {


        private  String[] ICONS = new String[] {
                "Fontact",
                "All",
                "My phone book",
                "Others phone book",
                "Know me"
        };

        public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = new Fragment();
            switch (position) {
                case 0:
                    f = TestFragment.newInstance(position);
                    break;
                case 1:
                    f = TestFragment.newInstance(position);
                    break;
                case 2:
                    f =  TestFragment.newInstance(position);
                    break;
                case 3:
                    f =  TestFragment.newInstance(position);
                    break;
                case 4:
                    f =  TestFragment.newInstance(position);
                    break;
            }
            return f;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ICONS[position];
        }

        @Override
        public int getCount() {
            return 4;
        }
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }
}
