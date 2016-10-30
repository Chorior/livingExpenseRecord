package org.chorior.pengzhen.livingexpenserecord;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by pengzhen on 14/10/16.
 */

public class RecordLab {
    private static RecordLab sRecordLab;
    private Context mAppContext;
    private ArrayList<Record> mRecords;
    private ArrayList<Record> mRecords_month0;
    private ArrayList<Record> mRecords_month1;
    private ArrayList<Record> mRecords_month2;
    private ArrayList<Record> mRecords_month3;
    private int[] total_month;
    private int[] total_month_others;
    private String[] record_months;

    private static final String FILENAME = "records.json";
    private RecordJSONSerializer mSerializer;

    private RecordLab(Context appContext){
        mAppContext = appContext;
        total_month = new int[4];
        total_month_others = new int[4];
        record_months = new String[4];
        mRecords_month0 = new ArrayList<>();
        mRecords_month1 = new ArrayList<>();
        mRecords_month2 = new ArrayList<>();
        mRecords_month3 = new ArrayList<>();

        mSerializer = new RecordJSONSerializer(mAppContext,FILENAME);
        try{
            mRecords = mSerializer.loadRecords();
        }catch(Exception e){
            mRecords = new ArrayList<>();
        }

        updateRecords();
    }

    public boolean saveRecords(){
        try{
            mSerializer.saveRecords(mRecords);
            return true;
        }catch(Exception e){
            return false;
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

    public void deleteRecord(Record record)
    {
        if(!mRecords.isEmpty()) {
            for(int i = mRecords.size() - 1; i >= 0; -- i){
                if(mRecords.get(i).toString().equals(record.toString())){
                    mRecords.remove(i);
                    break;
                }
            }
        }
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
    public void updateRecords(){
        for(int i = 0; i < total_month.length; ++ i){
            total_month[i] = 0;
            total_month_others[i] = 0;
            record_months[i] = "";
        }
        mRecords_month0.clear();
        mRecords_month1.clear();
        mRecords_month2.clear();
        mRecords_month3.clear();

        if(!mRecords.isEmpty()){
            Collections.sort(mRecords,Record.DateComparator);

            int index_totalMonth = 3;
            ArrayList<Record> temp_records_month = mRecords_month3;
            String str_temp = mRecords.get(mRecords.size() - 1).getYearAndMonthDate();
            for(int i = mRecords.size() - 1; i >= 0; -- i){
                if(!str_temp.equals(mRecords.get(i).getYearAndMonthDate())){
                    -- index_totalMonth;
                    if(0 > index_totalMonth){
                        mRecords.subList(0,i + 1).clear();
                        return;
                    }
                    str_temp = mRecords.get(i).getYearAndMonthDate();
                    if(temp_records_month == mRecords_month3){
                        temp_records_month = mRecords_month2;
                    }else if(temp_records_month == mRecords_month2){
                        temp_records_month = mRecords_month1;
                    }else if(temp_records_month == mRecords_month1){
                        temp_records_month = mRecords_month0;
                    }
                }
                total_month[index_totalMonth] += mRecords.get(i).getmTotal_today();
                total_month_others[index_totalMonth] += mRecords.get(i).getmOthers();
                record_months[index_totalMonth] = mRecords.get(i).getYearAndMonthDate();
                temp_records_month.add(new Record(mRecords.get(i)));
            }
        }
    }

    public int getTotal_month(int i){
        if(i >= 0 && i <= 3){
            return total_month[i];
        }
        return 0;
    }

    public int getTotal_others(int i){
        if(i >= 0 && i <= 3){
            return total_month_others[i];
        }
        return 0;
    }

    public String getRecord_Months(int i){
        if(i >= 0 &&i <= 3){
            return record_months[i];
        }
        return "";
    }

    public ArrayList<Record> getmRecords_month0() {
        return mRecords_month0;
    }

    public ArrayList<Record> getmRecords_month1() {
        return mRecords_month1;
    }

    public ArrayList<Record> getmRecords_month2() {
        return mRecords_month2;
    }

    public ArrayList<Record> getmRecords_month3() {
        return mRecords_month3;
    }

}
