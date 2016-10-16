package org.chorior.pengzhen.livingexpenserecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by pengzhen on 11/10/16.
 */

public class RecordFragment extends Fragment {
    private Record mRecord;
    private Button mDateButton;
    private EditText mBreakfastField;
    private EditText mLunchField;
    private EditText mDinnerField;
    private TextView mTotal_today;
    private final String str_total_today_prefix = "TOTAL ";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mRecord = new Record();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_record,parent,false);

        mDateButton = (Button)v.findViewById(R.id.record_date);
        mDateButton.setText(DateFormat.format("EEEE, MMMM dd",mRecord.getmDate()));
        mDateButton.setEnabled(false);

        mTotal_today = (TextView)v.findViewById(R.id.record_total_today);
        mTotal_today.setText(str_total_today_prefix + "" +
                mRecord.getmTotal_today());

        mBreakfastField = (EditText)v.findViewById(R.id.record_breakfast);
        mBreakfastField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // wait to update
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(0 != s.length()) {
                    mRecord.setmBreakfast(Integer.parseInt(s.toString()));
                } else {
                    mRecord.setmBreakfast(0);
                }

                mTotal_today.setText(str_total_today_prefix + "" +
                        mRecord.getmTotal_today());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // wait to update
            }
        });

        mLunchField = (EditText)v.findViewById(R.id.record_lunch);
        mLunchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // wait to update
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(0 != s.length()) {
                    mRecord.setmLunch(Integer.parseInt(s.toString()));
                } else {
                    mRecord.setmLunch(0);
                }

                mTotal_today.setText(str_total_today_prefix + "" +
                        mRecord.getmTotal_today());

            }

            @Override
            public void afterTextChanged(Editable s) {
                // wait to update
            }
        });

        mDinnerField = (EditText)v.findViewById(R.id.record_dinner);
        mDinnerField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // wait to update
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(0 != s.length()) {
                    mRecord.setmDinner(Integer.parseInt(s.toString()));
                } else {
                    mRecord.setmDinner(0);
                }

                mTotal_today.setText(str_total_today_prefix + "" +
                        mRecord.getmTotal_today());

            }

            @Override
            public void afterTextChanged(Editable s) {
                // wait to update
            }
        });
        return v;
    }
}
