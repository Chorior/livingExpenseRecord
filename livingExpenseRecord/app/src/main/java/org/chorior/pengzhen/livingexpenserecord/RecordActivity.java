package org.chorior.pengzhen.livingexpenserecord;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;

import org.chorior.pengzhen.livingexpenserecord.custom.MyPageTransformer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private RecordFragment mRecord_fragment;
    private RecordListFragment mRecordListFragment;
    private fragment_total_month mFragment_total_month;
    private List<Fragment> mFragmentList = new ArrayList<>();;
    private static final String KEY_INDEX = "index";
    private int savedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        if(null != savedInstanceState){
            savedIndex = savedInstanceState.getInt(KEY_INDEX);
        }
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setElevation(0);
        setSupportActionBar(mToolbar);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navi_open, R.string.navi_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navi_item_animation:
                        break;
                    default:
                }
                return false;
            }
        });

        mRecord_fragment = new RecordFragment();
        mRecordListFragment = new RecordListFragment();
        mFragment_total_month = new fragment_total_month();

        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        Fragment fragment0 = mRecord_fragment;
        Fragment fragment1 = mRecordListFragment;
        Fragment fragment2 = mFragment_total_month;

        mFragmentList.add(fragment0);
        mFragmentList.add(fragment1);
        mFragmentList.add(fragment2);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setOffscreenPageLimit(mFragmentList.size());
        mViewPager.setPageTransformer(true, new MyPageTransformer());
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch(position){
                    case 0:
                        return getString(R.string.title_page0);
                    case 1:
                        return getString(R.string.title_page1);
                    case 2:
                        return getString(R.string.title_page2);
                    default:
                        return null;
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        Date date = new Date();
                        setTitle(DateFormat.format("yyyy-MM-dd",date));
                        mRecord_fragment.updateRecordFragmentView();
                        break;
                    case 1:
                        setTitle(R.string.record_list_title);
                        mRecordListFragment.refreshData();
                        break;
                    case 2:
                        setTitle(R.string.total_month_title);
                        RecordLab.get(getApplicationContext()).updateRecords();
                        mFragment_total_month.updateTextView();
                        break;
                    default:
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager.setCurrentItem(savedIndex);
        switch(savedIndex){
            case 0:
                Date date = new Date();
                setTitle(DateFormat.format("yyyy-MM-dd",date));
                break;
            case 1:
                setTitle(R.string.record_list_title);
                break;
            case 2:
                setTitle(R.string.total_month_title);
                RecordLab.get(getApplicationContext()).updateRecords();
                break;
            default:
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        savedIndex = mViewPager.getCurrentItem();
        RecordLab.get(getApplicationContext()).saveRecords();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(null != mViewPager){
            outState.putInt(KEY_INDEX,mViewPager.getCurrentItem());
        }
    }
}
