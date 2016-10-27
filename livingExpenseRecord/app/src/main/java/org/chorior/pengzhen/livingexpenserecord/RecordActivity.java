package org.chorior.pengzhen.livingexpenserecord;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    private List<ImageView> dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        if(null != savedInstanceState){
            savedIndex = savedInstanceState.getInt(KEY_INDEX);
        }

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

        addDots();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setOffscreenPageLimit(mFragmentList.size());
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
                        mRecord_fragment.updateRecordFragmentView();
                        dots.get(0).setImageResource(R.drawable.ic_dot_selected);
                        dots.get(1).setImageResource(R.drawable.ic_dot_normal);
                        dots.get(2).setImageResource(R.drawable.ic_dot_normal);
                        break;
                    case 1:
                        setTitle(R.string.record_list_title);
                        mRecordListFragment.refreshData();
                        dots.get(0).setImageResource(R.drawable.ic_dot_normal);
                        dots.get(1).setImageResource(R.drawable.ic_dot_selected);
                        dots.get(2).setImageResource(R.drawable.ic_dot_normal);
                        break;
                    case 2:
                        setTitle(R.string.total_month_title);
                        mFragment_total_month.updateTextView();
                        dots.get(0).setImageResource(R.drawable.ic_dot_normal);
                        dots.get(1).setImageResource(R.drawable.ic_dot_normal);
                        dots.get(2).setImageResource(R.drawable.ic_dot_selected);
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
    protected void onResume() {
        super.onResume();
        mViewPager.setCurrentItem(savedIndex);
        switch(savedIndex){
            case 0:
                Date date = new Date();
                setTitle(DateFormat.format("yyyy-MM-dd",date));
                dots.get(0).setImageResource(R.drawable.ic_dot_selected);
                dots.get(1).setImageResource(R.drawable.ic_dot_normal);
                dots.get(2).setImageResource(R.drawable.ic_dot_normal);
                break;
            case 1:
                setTitle(R.string.record_list_title);
                dots.get(0).setImageResource(R.drawable.ic_dot_normal);
                dots.get(1).setImageResource(R.drawable.ic_dot_selected);
                dots.get(2).setImageResource(R.drawable.ic_dot_normal);
                break;
            case 2:
                setTitle(R.string.total_month_title);
                dots.get(0).setImageResource(R.drawable.ic_dot_normal);
                dots.get(1).setImageResource(R.drawable.ic_dot_normal);
                dots.get(2).setImageResource(R.drawable.ic_dot_selected);
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

    public void addDots() {
        dots = new ArrayList<>();
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.dots);

        for (int i = 0; i < mFragmentList.size(); i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_dot_normal));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            dotsLayout.addView(dot, params);

            dots.add(dot);
        }
    }
}
