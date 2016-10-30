package org.chorior.pengzhen.livingexpenserecord.custom;

import android.content.Context;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.chorior.pengzhen.livingexpenserecord.RecordLab;

/**
 * Created by pengzhen on 30/10/16.
 */

public class MyXAxisValueFormatter implements IAxisValueFormatter {

    private Context context;

    public MyXAxisValueFormatter(Context context){
        this.context = context;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return RecordLab.get(context).getRecord_Months((int)value);
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
