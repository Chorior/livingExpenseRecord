package org.chorior.pengzhen.livingexpenserecord;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private RecordFragment mRecord_fragment;
    private RecordListFragment mRecordListFragment;
    private List<Fragment> mFragmentList = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Date date = new Date();
        setTitle(DateFormat.format("yyyy-MM-dd",date));

        mRecord_fragment = new RecordFragment();
        mRecordListFragment = new RecordListFragment();

        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        Fragment fragment0 = mRecord_fragment;
        Fragment fragment1 = mRecordListFragment;
        mFragmentList.add(fragment0);
        mFragmentList.add(fragment1);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
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
                        break;
                    case 1:
                        setTitle(R.string.record_list_title);
                        mRecordListFragment.refreshData();
                        break;
                    default:
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Date date = new Date();
        setTitle(DateFormat.format("yyyy-MM-dd",date));
    }
}
