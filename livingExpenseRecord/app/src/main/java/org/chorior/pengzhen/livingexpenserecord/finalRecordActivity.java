package org.chorior.pengzhen.livingexpenserecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pengzhen on 15/10/16.
 */

public class finalRecordActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ArrayList<Record> mRecords;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_final);

        mViewPager = (ViewPager)findViewById(R.id.viewPagerFinal);
        mRecords = RecordLab.get(this).getRecords();

        FragmentManager fm = getSupportFragmentManager();

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
                break;
            }
        }
    }
}
