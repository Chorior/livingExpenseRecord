package org.chorior.pengzhen.livingexpenserecord.custom;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import org.chorior.pengzhen.livingexpenserecord.R;
import org.chorior.pengzhen.livingexpenserecord.Record;
import org.chorior.pengzhen.livingexpenserecord.RecordLab;

import java.util.ArrayList;

/**
 * Created by pengzhen on 31/10/16.
 */

public class MyMarkerView extends MarkerView {
    private Context context;
    private int index;
    private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource, int index){
        super(context,layoutResource);

        this.context = context;
        this.index = index;
        tvContent = (TextView)findViewById(R.id.tvContent);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth()/2),-getHeight());
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        ArrayList<Record> records = RecordLab.get(context).getmRecords_month(index);

        int x = (int)highlight.getX();
        int total = records.get(records.size() - 1 - x).getmTotal_today();
        int others = records.get(records.size() - 1 - x).getmOthers();
        int meal = total - others;

        tvContent.setText("total: " + String.valueOf(total) +
                            "\nmeal: " + String.valueOf(meal) +
                            "\nothers: " + String.valueOf(others));

        super.refreshContent(e,highlight);
    }
}
