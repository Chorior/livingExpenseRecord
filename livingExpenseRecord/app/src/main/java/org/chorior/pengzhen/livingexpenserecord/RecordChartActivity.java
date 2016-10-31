package org.chorior.pengzhen.livingexpenserecord;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.chorior.pengzhen.livingexpenserecord.custom.ChartItem;
import org.chorior.pengzhen.livingexpenserecord.custom.GroupedBarChartItem;
import org.chorior.pengzhen.livingexpenserecord.custom.LineChartItem;
import org.chorior.pengzhen.livingexpenserecord.custom.MyLineChartXAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengzhen on 29/10/16.
 */

public class RecordChartActivity extends Activity{

    private static class GroupedChartParameters{
        float FromX;
        float groupSpace;
        float barSpace;
        float barWidth;
    }

    private static class LineChartParameters{
        float miniNum;
        float maxiNum;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_chart);
        RecordLab.get(getApplicationContext()).updateRecords();

        ListView lv = (ListView) findViewById(R.id.listView_chart);

        List<ChartItem> list = new ArrayList<>();

        GroupedChartParameters gcp = new GroupedChartParameters();
        BarData barData = generateDataGroupedBar(gcp);
        list.add(new GroupedBarChartItem(barData,getApplicationContext(),
                gcp.FromX,gcp.groupSpace,gcp.barSpace));

        LineChartParameters lcp = new LineChartParameters();
        if(1 < RecordLab.get(getApplication()).getmRecords_month0().size()) {
            list.add(new LineChartItem(
                    generateDataLine(RecordLab.get(getApplication()).getmRecords_month0(), 0),
                    new MyLineChartXAxisValueFormatter(getApplicationContext(),0)
            ));
        }
        if(1 < RecordLab.get(getApplication()).getmRecords_month1().size()) {
            list.add(new LineChartItem(
                    generateDataLine(RecordLab.get(getApplication()).getmRecords_month1(), 1),
                    new MyLineChartXAxisValueFormatter(getApplicationContext(),1)
            ));
        }
        if(1 < RecordLab.get(getApplication()).getmRecords_month2().size()) {
            list.add(new LineChartItem(
                    generateDataLine(RecordLab.get(getApplication()).getmRecords_month2(), 2),
                    new MyLineChartXAxisValueFormatter(getApplicationContext(),2)
            ));
        }
        if(1 < RecordLab.get(getApplication()).getmRecords_month3().size()) {
            list.add(new LineChartItem(
                    generateDataLine(RecordLab.get(getApplication()).getmRecords_month3(), 3),
                    new MyLineChartXAxisValueFormatter(getApplicationContext(),3)
            ));
        }

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);
    }

    /** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }

    private BarData generateDataGroupedBar(GroupedChartParameters gcp ) {
        Utils.init(getApplicationContext());

        List<BarEntry> entries0 = new ArrayList<>();
        List<BarEntry> entries1 = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();
        for(int i = 0; i< 4; ++ i){
            entries0.add(new BarEntry(i,RecordLab.get(getApplicationContext()).getTotal_month(i)));
            entries1.add(new BarEntry(i,
                    RecordLab.get(getApplicationContext()).getTotal_month(i) -
                            RecordLab.get(getApplicationContext()).getTotal_others(i)));
            entries2.add(new BarEntry(i,RecordLab.get(getApplicationContext()).getTotal_others(i)));
        }

        BarDataSet set0 = new BarDataSet(entries0, "total");
        BarDataSet set1 = new BarDataSet(entries1, "meal");
        BarDataSet set2 = new BarDataSet(entries2, "others");

        set0.setColor(ColorTemplate.JOYFUL_COLORS[0]);
        set1.setColor(ColorTemplate.JOYFUL_COLORS[1]);
        set2.setColor(ColorTemplate.JOYFUL_COLORS[2 ]);

        gcp.FromX = 0;
        gcp.groupSpace = 0.07f;
        gcp.barSpace = 0.03f; // x3 dataset
        gcp.barWidth = 0.28f; // x3 dataset
        // (0.03 + 0.28) * 3 + 0.07 = 1.00 -> interval per "group"

        BarData data = new BarData(set0,set1,set2);
        data.setBarWidth(gcp.barWidth);
        data.setValueFormatter(new IValueFormatter(){
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                            ViewPortHandler viewPortHandler) {
                return String.valueOf(Math.round(value));
            }
        });

        return data;
    }

    private LineData generateDataLine(ArrayList<Record> mRecords_month, int index) {
        Utils.init(getApplicationContext());

        List<Entry> entries = new ArrayList<>();

        for(int i = 0; i < mRecords_month.size(); ++ i){
            entries.add(new Entry(
                    i,
                    mRecords_month.get(i).getmTotal_today()
            ));
        }

        List<ILineDataSet> dataSets = new ArrayList<>();
        if(!entries.isEmpty()){
            LineDataSet set = new LineDataSet(entries,
                    mRecords_month.get(0).getYearAndMonthDate());
            set.setColor(ColorTemplate.VORDIPLOM_COLORS[index]);
            set.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[index]);

            dataSets.add(set);
        }

        LineData data = new LineData(dataSets);
        data.setValueFormatter(new IValueFormatter(){
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                            ViewPortHandler viewPortHandler) {
                return String.valueOf(Math.round(value));
            }
        });

        return data;
    }
}
