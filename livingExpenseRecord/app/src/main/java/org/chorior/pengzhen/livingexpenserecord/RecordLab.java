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

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,1);

        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i = 0; i < days; ++ i,cal.add(Calendar.DATE,1)){
            Record r = new Record();
            r.setmDate(cal.getTime());
            r.setmBreakfast(12 + i);
            r.updatemTotal_today();
            mRecords.add(r);
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
}
