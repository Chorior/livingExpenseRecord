package org.chorior.pengzhen.livingexpenserecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by pengzhen on 11/10/16.
 */

public class RecordFragment extends Fragment {
    private Record mRecord;
    private EditText mBreakfastField;
    private EditText mLunchField;
    private EditText mDinnerField;
    private EditText mOthersField;
    private TextView mTotal_today;
    private final String str_total_today_prefix = "总计 ";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mRecord = new Record();

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
        mOthersField = (EditText)v.findViewById(R.id.record_others);

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
                Toast.makeText(getActivity(), R.string.saved_toast, Toast.LENGTH_SHORT).show();
                mRecord.setmDate(new Date());

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
                if(0 != mOthersField.getText().length()) {
                    mRecord.setmOthers(
                            Integer.parseInt(mOthersField.getText().toString()));
                } else {
                    mRecord.setmOthers(0);
                }
                mTotal_today.setText(str_total_today_prefix + "" +
                        mRecord.getmTotal_today());

                RecordLab.get(getActivity()).addRecord(mRecord);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateRecordFragmentView(){
        if(0 != RecordLab.get(getActivity()).getRecords().size()){
            boolean isFound = false;
            for(Record r : RecordLab.get(getActivity()).getRecords()){
                Record record = new Record();
                if(record.toString().equals(r.toString())){
                    if(0 != r.getmBreakfast()){
                        if(null != mBreakfastField){
                            mBreakfastField.setText(String.valueOf(r.getmBreakfast()));
                        }
                    }
                    if(0 != r.getmLunch()){
                        if(null != mLunchField){
                            mLunchField.setText(String.valueOf(r.getmLunch()));
                        }
                    }
                    if(0 != r.getmDinner()) {
                        if(null != mDinnerField){
                            mDinnerField.setText(String.valueOf(r.getmDinner()));
                        }
                    }
                    if(0 != r.getmOthers()) {
                        if(null != mOthersField){
                            mOthersField.setText(String.valueOf(r.getmOthers()));
                        }
                    }
                    if(null != mTotal_today){
                        mTotal_today.setText(str_total_today_prefix + "" +
                                r.getmTotal_today());
                    }
                    isFound = true;
                    break;
                }
            }

            if(!isFound){
                if(null != mBreakfastField){
                    mBreakfastField.setText("");
                }
                if(null != mLunchField){
                    mLunchField.setText("");
                }
                if(null != mDinnerField){
                    mDinnerField.setText("");
                }
                if(null != mOthersField){
                    mOthersField.setText("");
                }
                if(null != mTotal_today){
                    mTotal_today.setText(str_total_today_prefix + "0");
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecordFragmentView();
    }
}
