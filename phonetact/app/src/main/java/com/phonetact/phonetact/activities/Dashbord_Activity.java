package com.phonetact.phonetact.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.phonetact.R;
import com.phonetact.phonetact.Utils.MaterialRippleLayout;
import com.phonetact.phonetact.Utils.SlidingTabLayout;
import com.phonetact.phonetact.fragment.ChatFragment;
import com.phonetact.phonetact.fragment.ContatcFragment;
import com.phonetact.phonetact.fragment.HomeFragment;
import com.phonetact.phonetact.fragment.RequestFragment;
import com.phonetact.phonetact.fragment.TestFragment;

/**
 * Created by hp on 28/05/2015.
 */
public class Dashbord_Activity extends ActionBarActivity {
    //element layout
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private MaterialRippleLayout iconmenu;

    //adaptater

    //variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);

        // intialiser the element layout
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        iconmenu = (MaterialRippleLayout) findViewById(R.id.iconmenu);

        //set viewpage and tabs
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mTabs.setViewPager(mPager);
        mTabs.setBackgroundColor(getResources().getColor(R.color.color_toolbar));
        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.color_sick));
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

        //click button menu
        iconmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



    public class MyPagerAdapter extends FragmentPagerAdapter {


        private  int[] ICONS = new int[] {
                R.drawable.home_icon,
                R.drawable.chat_icon,
                R.drawable.request_icon,
                R.drawable.profil_icon
        };

        public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = new Fragment();
            switch (position) {
                case 0:
                    f = HomeFragment.newInstance(position);
                    break;
                case 1:
                    f = ChatFragment.newInstance(position);
                    break;
                case 2:
                    f =  RequestFragment.newInstance(position);
                    break;
                case 3:
                    f =  ContatcFragment.newInstance(position);
                    break;
            }
            return f;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ""+ICONS[position];
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
