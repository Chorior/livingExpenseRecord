package org.chorior.pengzhen.livingexpenserecord.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.chorior.pengzhen.livingexpenserecord.R;

/**
 * Created by pengzhen on 30/10/16.
 */

public class GroupedBarChartItem extends ChartItem {
    private Context context;
    private float FromX;
    private float groupSpace;
    private float barSpace;

    public GroupedBarChartItem(ChartData<?> cd, Context context,
                               float FromX, float groupSpace, float barSpace){
        super(cd);

        this.context = context;
        this.FromX = FromX;
        this.groupSpace = groupSpace;
        this.barSpace = barSpace;
    }

    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }

    private static class ViewHolder {
        BarChart chart;
    }

    @Override
    public View getView(int position, View convertView, Context c) {
        ViewHolder holder = null;

        if (null == convertView) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_groupedbarchart, null);
            holder.chart = (BarChart) convertView.findViewById(R.id.GroupedBarChart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.chart.getDescription().setEnabled(false);
        //holder.chart.setPinchZoom(false);
        holder.chart.setDrawBarShadow(false);
        holder.chart.setDrawValueAboveBar(true);

        IAxisValueFormatter xAxisFormatter = new MyXAxisValueFormatter(context);

        XAxis xAxis= holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(4);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(5);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setCenterAxisLabels(true);

        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setDrawGridLines(true); // no grid lines
        holder.chart.getAxisRight().setEnabled(false); // no right axis

        // set data
        holder.chart.setData((BarData) mChartData);

        holder.chart.animateXY(1000,1000);
        holder.chart.groupBars(FromX,groupSpace,barSpace);
        holder.chart.invalidate(); // refresh

        return convertView;
    }
}
