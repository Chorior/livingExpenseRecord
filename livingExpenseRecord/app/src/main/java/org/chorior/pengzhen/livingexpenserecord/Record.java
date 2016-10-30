package org.chorior.pengzhen.livingexpenserecord;

import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pengzhen on 11/10/16.
 */

public class Record extends Object {
    private Date mDate;
    private int mBreakfast;
    private int mLunch;
    private int mDinner;
    private int mOthers;
    private int mTotal_today;

    private static final String JSON_DATE = "date";
    private static final String JSON_BREAKFAST = "breakfast";
    private static final String JSON_LUNCH = "lunch";
    private static final String JSON_DINNER = "dinner";
    private static final String JSON_OTHERS = "others";

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(JSON_DATE,toString());
        json.put(JSON_BREAKFAST,mBreakfast);
        json.put(JSON_LUNCH,mLunch);
        json.put(JSON_DINNER,mDinner);
        json.put(JSON_OTHERS,mOthers);

        return json;
    }

    public Record(JSONObject json) throws JSONException, ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        mDate =  df.parse(json.getString(JSON_DATE));
        mBreakfast = json.getInt(JSON_BREAKFAST);
        mLunch = json.getInt(JSON_LUNCH);
        mDinner = json.getInt(JSON_DINNER);
        mOthers = json.getInt(JSON_OTHERS);
    }

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
        mTotal_today = mBreakfast + mLunch + mDinner + mOthers;
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

    public int getmOthers() {
        return mOthers;
    }

    public void setmOthers(int mOthers) {
        this.mOthers = mOthers;
    }

    public Record() {
        mDate = new Date();
        mBreakfast = 0;
        mLunch = 0;
        mDinner = 0;
        mOthers = 0;
        mTotal_today = 0;
    }

    public Record(Record record){
        mDate = record.getmDate();
        mBreakfast = record.getmBreakfast();
        mLunch = record.getmLunch();
        mDinner = record.getmDinner();
        mOthers = record.getmOthers();
        mTotal_today = record.getmTotal_today();
    }

    @Override
    public String toString() {
        return DateFormat.format("yyyy-MM-dd",mDate).toString();
    }

    public String getYearAndMonthDate(){
        return DateFormat.format("yyyyMM",mDate).toString();
    }

    public float getDay(){
        return Float.parseFloat(DateFormat.format("dd",mDate).toString());
    }

    public static Comparator<Record> DateComparator
            = new Comparator<Record>() {
        @Override
        public int compare(Record o1, Record o2) {
            return o1.mDate.compareTo(o2.mDate);
        }
    };
}
