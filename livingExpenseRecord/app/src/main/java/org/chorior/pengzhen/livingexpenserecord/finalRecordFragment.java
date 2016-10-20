package org.chorior.pengzhen.livingexpenserecord;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by pengzhen on 15/10/16.
 */

public class finalRecordFragment extends Fragment {
    public static final String EXTRA_RECORD_DATE =
            "org.chorior.pengzhen.recordIntent.record_date";

    private Record mRecord;
    private TextView mBreakfast;
    private TextView mLunch;
    private TextView mDinner;
    private TextView mTotal_today;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Date recordDate = (Date)getArguments().getSerializable(EXTRA_RECORD_DATE);
        mRecord = RecordLab.get(getActivity()).getRecord(recordDate);

    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_record_final,parent,false);

        mBreakfast = (TextView)v.findViewById(R.id.record_breakfast_final);
        mBreakfast.setText(String.valueOf(mRecord.getmBreakfast()));

        mLunch = (TextView)v.findViewById(R.id.record_lunch_final);
        mLunch.setText(String.valueOf(mRecord.getmLunch()));

        mDinner = (TextView)v.findViewById(R.id.record_dinner_final);
        mDinner.setText(String.valueOf(mRecord.getmDinner()));

        mTotal_today = (TextView)v.findViewById(R.id.record_total_today_final);
        mTotal_today.setText(String.valueOf(mRecord.getmTotal_today()));

        return v;
    }

    public static finalRecordFragment newInstance(Date recordDate){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_RECORD_DATE,recordDate);

        finalRecordFragment finalFragment = new finalRecordFragment();
        finalFragment.setArguments(args);

        return finalFragment;
    }
}
