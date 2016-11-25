package org.chorior.pengzhen.livingexpenserecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;

import org.chorior.pengzhen.livingexpenserecord.custom.MyPageTransformer;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pengzhen on 15/10/16.
 */

public class finalRecordActivity extends AppCompatActivity {
    private ArrayList<Record> mRecords;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_final);
        initToolbar();
        initViewPager();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                if(null != NavUtils.getParentActivityName(this)){
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initToolbar(){
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setElevation(0);
        setSupportActionBar(mToolbar);
        if(null != getSupportActionBar()){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void initViewPager(){
        mRecords = RecordLab.get(this).getRecords();
        FragmentManager fm = getSupportFragmentManager();
        ViewPager mViewPager = (ViewPager)findViewById(R.id.viewPagerFinal);
        mViewPager.setPageTransformer(true, new MyPageTransformer(MyPageTransformer.TransitionEffect.ZoomOut));
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Record record = mRecords.get(position);
                return finalRecordFragment.newInstance(record.getmDate());
            }
            @Override
            public int getCount() {
                return mRecords.size();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(null != mRecords){
                    setTitle(DateFormat.format("yyyy-MM-dd",
                            mRecords.get(position).getmDate()));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Date recordDate = (Date)getIntent()
                .getSerializableExtra(finalRecordFragment.EXTRA_RECORD_DATE);
        for(int i = 0; i < mRecords.size(); ++ i){
            if(mRecords.get(i).getmDate().equals(recordDate)){
                mViewPager.setCurrentItem(i);
                setTitle(DateFormat.format("yyyy-MM-dd",recordDate));
                break;
            }
        }
    }
}
