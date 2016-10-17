package org.chorior.pengzhen.livingexpenserecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

        getActivity().setTitle(DateFormat.format("MMMM dd",mRecord.getmDate()));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_record,parent,false);

        mBreakfastField = (EditText)v.findViewById(R.id.record_breakfast);
        mLunchField = (EditText)v.findViewById(R.id.record_lunch);
        mDinnerField = (EditText)v.findViewById(R.id.record_dinner);

        for(Record r : RecordLab.get(getActivity()).getRecords()){
            if(mRecord.getmDate().equals(r.getmDate())){
                mRecord.setmBreakfast(r.getmBreakfast());
                mRecord.setmLunch(r.getmLunch());
                mRecord.setmDinner(r.getmDinner());

                mBreakfastField.setText(String.valueOf(r.getmBreakfast()));
                mLunchField.setText(String.valueOf(r.getmLunch()));
                mDinnerField.setText(String.valueOf(r.getmDinner()));
                break;
            }
        }

        mTotal_today = (TextView)v.findViewById(R.id.record_total_today);
        mTotal_today.setText(str_total_today_prefix + "" +
                mRecord.getmTotal_today());

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_record_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_save_record:
                if(0 != mBreakfastField.getText().length()) {
                    mRecord.setmBreakfast(
                            Integer.parseInt(mBreakfastField.getText().toString()));
                } else {
                    mRecord.setmBreakfast(0);
                }
                if(0 != mLunchField.getText().length()) {
                    mRecord.setmLunch(
                            Integer.parseInt(mLunchField.getText().toString()));
                } else {
                    mRecord.setmLunch(0);
                }
                if(0 != mDinnerField.getText().length()) {
                    mRecord.setmDinner(
                            Integer.parseInt(mDinnerField.getText().toString()));
                } else {
                    mRecord.setmDinner(0);
                }
                mTotal_today.setText(str_total_today_prefix + "" +
                        mRecord.getmTotal_today());

                RecordLab.get(getActivity()).addRecord(mRecord);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
