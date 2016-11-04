package org.chorior.pengzhen.livingexpenserecord;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MenuItem;

import org.chorior.pengzhen.livingexpenserecord.custom.MyPageTransformer;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pengzhen on 15/10/16.
 */

public class finalRecordActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ArrayList<Record> mRecords;

    @TargetApi(11)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_final);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if(null != NavUtils.getParentActivityName(this)){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mRecords = RecordLab.get(this).getRecords();
        FragmentManager fm = getSupportFragmentManager();

        mViewPager = (ViewPager)findViewById(R.id.viewPagerFinal);
        mViewPager.setPageTransformer(true, new MyPageTransformer());
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

        Date recordDate = (Date)getIntent()
                .getSerializableExtra(finalRecordFragment.EXTRA_RECORD_DATE);
        for(int i = 0; i < mRecords.size(); ++ i){
            if(mRecords.get(i).getmDate().equals(recordDate)){
                mViewPager.setCurrentItem(i);
                setTitle(DateFormat.format("yyyy-MM-dd",recordDate));
                break;
            }
        }

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
}
