package org.chorior.pengzhen.livingexpenserecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pengzhen on 20/10/16.
 */

public class fragment_total_month extends Fragment {
    private TextView record_month3;
    private TextView record_month2;
    private TextView record_month1;
    private TextView record_month0;

    private TextView record_month3_value;
    private TextView record_month2_value;
    private TextView record_month1_value;
    private TextView record_month0_value;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_total_month,parent,false);
        record_month3 = (TextView)v.findViewById(R.id.record_month3);
        record_month2 = (TextView)v.findViewById(R.id.record_month2);
        record_month1 = (TextView)v.findViewById(R.id.record_month1);
        record_month0 = (TextView)v.findViewById(R.id.record_month0);

        record_month3_value = (TextView)v.findViewById(R.id.record_month3_value);
        record_month2_value = (TextView)v.findViewById(R.id.record_month2_value);
        record_month1_value = (TextView)v.findViewById(R.id.record_month1_value);
        record_month0_value = (TextView)v.findViewById(R.id.record_month0_value);

        updateTextView();

        return v;
    }

    public void updateTextView(){
        if(null != record_month3){
            record_month3.setText((RecordLab.get(getActivity()).getRecord_Months())[3]);
        }
        if(null != record_month2){
            record_month2.setText((RecordLab.get(getActivity()).getRecord_Months())[2]);
        }
        if(null != record_month1){
            record_month1.setText((RecordLab.get(getActivity()).getRecord_Months())[1]);
        }
        if(null != record_month0){
            record_month0.setText((RecordLab.get(getActivity()).getRecord_Months())[0]);
        }

        if(null != record_month3_value){
            record_month3_value.setText((RecordLab.get(getActivity()).getTotal_month())[3]);
        }
        if(null != record_month2_value){
            record_month2_value.setText((RecordLab.get(getActivity()).getTotal_month())[2]);
        }
        if(null != record_month1_value){
            record_month1_value.setText((RecordLab.get(getActivity()).getTotal_month())[1]);
        }
        if(null != record_month0_value){
            record_month0_value.setText((RecordLab.get(getActivity()).getTotal_month())[0]);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(R.string.total_month_title);
    }
}
