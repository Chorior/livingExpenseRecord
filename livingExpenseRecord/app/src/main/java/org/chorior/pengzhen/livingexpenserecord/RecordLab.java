package org.chorior.pengzhen.livingexpenserecord;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

/**
 * Created by pengzhen on 14/10/16.
 */

public class RecordLab {
    private static RecordLab sRecordLab;
    private Context mAppContext;
    private ArrayList<Record> mRecords;
    private int[] total_month;
    private String[] record_months;

    private RecordLab(Context appContext){
        mAppContext = appContext;
        mRecords = new ArrayList<Record>();
        total_month = new int[4];
        record_months = new String[4];

        for(int i = 0; i < total_month.length; ++ i){
            total_month[i] = 0;
            record_months[i] = "";
        }
    }

    public static RecordLab get(Context c){
        if(null == sRecordLab){
            sRecordLab = new RecordLab(c.getApplicationContext());
        }
        return sRecordLab;
    }

    public ArrayList<Record> getRecords(){
        return mRecords;
    }

    public Record getRecord(Date date){
        for(Record r : mRecords){
            if(r.getmDate().equals(date)){
                return r;
            }
        }
        return null;
    }

    public void addRecord(Record record)
    {
        if(!mRecords.isEmpty()) {
            for(int i = mRecords.size() - 1; i >= 0; -- i){
                if(mRecords.get(i).toString().equals(record.toString())){
                    mRecords.remove(i);
                    break;
                }
            }
        }

        mRecords.add(new Record(record));
        updateRecords();
    }

    // only record the past 3 months and this month's cost at most
    private void updateRecords(){
        if(!mRecords.isEmpty()){

            Collections.sort(mRecords,Record.DateComparator);
            for(int i = 0; i < total_month.length; ++ i){
                total_month[i] = 0;
                record_months[i] = "";
            }

            int index_totalMonth = 3;
            String str_temp = mRecords.get(mRecords.size() - 1).getYearAndMonthDate();
            for(int i = mRecords.size() - 1; i >= 0; -- i){
                if(!str_temp.equals(mRecords.get(i).getYearAndMonthDate())){
                    -- index_totalMonth;
                    str_temp = mRecords.get(i).getYearAndMonthDate();
                    if(0 > index_totalMonth){
                        mRecords.subList(0,i + 1).clear();
                        break;
                    }
                }
                total_month[index_totalMonth] += mRecords.get(i).getmTotal_today();
                record_months[index_totalMonth] = mRecords.get(i).getYearAndMonthDate();
            }
        }
    }

    public int getTotal_month(int i){
        if(i >= 0 && i <= 3){
            return total_month[i];
        }
        return 0;
    }

    public String getRecord_Months(int i){
        if(i >= 0 &&i <= 3){
            return record_months[i];
        }
        return "";
    }
}
