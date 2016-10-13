package org.chorior.pengzhen.livingexpenserecord;

import java.util.Date;
import java.util.UUID;

/**
 * Created by pengzhen on 11/10/16.
 */

public class Record extends Object {
    private UUID mId;
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
        return mTotal_today;
    }

    public void updatemTotal_today() {
        mTotal_today = mBreakfast + mLunch + mDinner;
    }

    public double getmLunch() {
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

    public UUID getmId() {
        return mId;
    }

    public int getmBreakfast() {
        return mBreakfast;
    }

    public void setmBreakfast(int mBreakfast) {
        this.mBreakfast = mBreakfast;
    }

    public Record()
    {
        mId = UUID.randomUUID();
        mDate = new Date();
        mBreakfast = 0;
        mLunch = 0;
        mDinner = 0;
        mTotal_today = 0;
    }
}
