package org.chorior.pengzhen.livingexpenserecord;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by pengzhen on 14/10/16.
 */

public class RecordLab {
    private static RecordLab sRecordLab;
    private Context mAppContext;
    private ArrayList<Record> mRecords;

    private RecordLab(Context appContext){
        mAppContext = appContext;
        mRecords = new ArrayList<Record>();

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
        if(0 != mRecords.size() &&
                record.getmDate().equals(
                        mRecords.get(mRecords.size() - 1).getmDate())){
            mRecords.get(mRecords.size() - 1).setmBreakfast(record.getmBreakfast());
            mRecords.get(mRecords.size() - 1).setmLunch(record.getmLunch());
            mRecords.get(mRecords.size() - 1).setmDinner(record.getmDinner());
            mRecords.get(mRecords.size() - 1).updatemTotal_today();
        }else{
            mRecords.add(record);
        }
    }
}
