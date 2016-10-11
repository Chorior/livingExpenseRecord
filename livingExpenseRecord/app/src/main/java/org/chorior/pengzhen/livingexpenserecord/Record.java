package org.chorior.pengzhen.livingexpenserecord;

import java.util.UUID;

/**
 * Created by pengzhen on 11/10/16.
 */

public class Record extends Object {
    private UUID mId;
    private String mBreakfast;

    public UUID getmId() {
        return mId;
    }

    public String getmBreakfast() {
        return mBreakfast;
    }

    public void setmBreakfast(String mBreakfast) {
        this.mBreakfast = mBreakfast;
    }

    public Record()
    {
        mId = UUID.randomUUID();
    }
}
