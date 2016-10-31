package org.chorior.pengzhen.livingexpenserecord.custom;

import android.content.Context;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.chorior.pengzhen.livingexpenserecord.RecordLab;

/**
 * Created by pengzhen on 31/10/16.
 */

public class MyLineChartXAxisValueFormatter implements IAxisValueFormatter {
    private Context context;
    private int index;

    public MyLineChartXAxisValueFormatter(Context context, int index){
        this.context = context;
        this.index = index;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return RecordLab.get(context).getmRecords_month(index).get((int)value).getDay();
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
