package org.chorior.pengzhen.livingexpenserecord;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        Fragment fragment0 = new RecordFragment();
        Fragment fragment1 = new RecordListFragment();
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
    }
}
