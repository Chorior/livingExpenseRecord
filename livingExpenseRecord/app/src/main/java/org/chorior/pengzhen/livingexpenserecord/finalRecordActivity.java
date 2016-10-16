package org.chorior.pengzhen.livingexpenserecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.Date;

/**
 * Created by pengzhen on 15/10/16.
 */

public class finalRecordActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_final);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.finalFragmentContainer);

        if(null == fragment){
            Date recordDate = (Date)getIntent()
                    .getSerializableExtra(finalRecordFragment.EXTRA_RECORD_DATE);
            fragment = finalRecordFragment.newInstance(recordDate);

            fm.beginTransaction()
                    .add(R.id.finalFragmentContainer, fragment)
                    .commit();
        }
    }
}
