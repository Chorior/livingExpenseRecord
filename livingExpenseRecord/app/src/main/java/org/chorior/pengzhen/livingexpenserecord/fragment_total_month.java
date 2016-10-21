package org.chorior.pengzhen.livingexpenserecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by pengzhen on 20/10/16.
 */

public class fragment_total_month extends Fragment {
    private TextView record_month3;
    private TextView record_month2;
    private TextView record_month1;
    private TextView record_month0;

    private TextView record_month3_total;
    private TextView record_month2_total;
    private TextView record_month1_total;
    private TextView record_month0_total;

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

        record_month3_total = (TextView)v.findViewById(R.id.record_month3_total);
        record_month2_total = (TextView)v.findViewById(R.id.record_month2_total);
        record_month1_total = (TextView)v.findViewById(R.id.record_month1_total);
        record_month0_total = (TextView)v.findViewById(R.id.record_month0_total);

        updateTextView();

        return v;
    }

    public void updateTextView(){
        if(null != record_month3){
            if(RecordLab.get(getActivity()).getRecord_Months(3).equals("")){
                Calendar cal = Calendar.getInstance();

                record_month3.setText(DateFormat.format("yyyy-MM",cal.getTime()));
            }else{
                record_month3.setText(RecordLab.get(getActivity()).getRecord_Months(3));
            }
        }
        if(null != record_month2){
            if(RecordLab.get(getActivity()).getRecord_Months(2).equals("")){
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH,-1);

                record_month2.setText(DateFormat.format("yyyy-MM",cal.getTime()));
            }else{
                record_month2.setText(RecordLab.get(getActivity()).getRecord_Months(2));
            }
        }
        if(null != record_month1){
            if(RecordLab.get(getActivity()).getRecord_Months(1).equals("")){
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH,-2);

                record_month1.setText(DateFormat.format("yyyy-MM",cal.getTime()));
            }else{
                record_month1.setText(RecordLab.get(getActivity()).getRecord_Months(1));
            }
        }
        if(null != record_month0){
            if(RecordLab.get(getActivity()).getRecord_Months(0).equals("")){
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH,-3);

                record_month0.setText(DateFormat.format("yyyy-MM",cal.getTime()));
            }else{
                record_month0.setText(RecordLab.get(getActivity()).getRecord_Months(0));
            }
        }

        if(null != record_month3_total){
            record_month3_total.setText(String.valueOf(
                    RecordLab.get(getActivity()).getTotal_month(3)
            ));
        }
        if(null != record_month2_total){
            record_month2_total.setText(String.valueOf(
                    RecordLab.get(getActivity()).getTotal_month(2)
            ));
        }
        if(null != record_month1_total){
            record_month1_total.setText(String.valueOf(
                    RecordLab.get(getActivity()).getTotal_month(1)
            ));
        }
        if(null != record_month0_total){
            record_month0_total.setText(String.valueOf(
                    RecordLab.get(getActivity()).getTotal_month(0)
            ));
        }
    }
}
