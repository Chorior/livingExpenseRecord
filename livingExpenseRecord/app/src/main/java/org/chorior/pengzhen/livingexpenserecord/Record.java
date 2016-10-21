package org.chorior.pengzhen.livingexpenserecord;

import android.text.format.DateFormat;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by pengzhen on 11/10/16.
 */

public class Record extends Object {
    private Date mDate;
    private int mBreakfast;
    private int mLunch;
    private int mDinner;
    private int mTotal_today;

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public int getmTotal_today() {
        updatemTotal_today();
        return mTotal_today;
    }

    public void updatemTotal_today() {
        mTotal_today = mBreakfast + mLunch + mDinner;
    }

    public int getmLunch() {
        return mLunch;
    }

    public void setmLunch(int mLunch) {
        this.mLunch = mLunch;
    }

    public int getmDinner() {
        return mDinner;
    }

    public void setmDinner(int mDinner) {
        this.mDinner = mDinner;
    }

    public int getmBreakfast() {
        return mBreakfast;
    }

    public void setmBreakfast(int mBreakfast) {
        this.mBreakfast = mBreakfast;
    }

    public Record() {
        mDate = new Date();
        mBreakfast = 0;
        mLunch = 0;
        mDinner = 0;
        mTotal_today = 0;
    }

    public Record(Record record){
        mDate = record.getmDate();
        mBreakfast = record.getmBreakfast();
        mLunch = record.getmLunch();
        mDinner = record.getmDinner();
        mTotal_today = record.getmTotal_today();
    }

    @Override
    public String toString() {
        return DateFormat.format("yyyy-MM-dd",mDate).toString();
    }

    public String getYearAndMonthDate(){
        return DateFormat.format("yyyy-MM",mDate).toString();
    }

    public static Comparator<Record> DateComparator
            = new Comparator<Record>() {
        @Override
        public int compare(Record o1, Record o2) {
            return o1.mDate.compareTo(o2.mDate);
        }
    };
}
