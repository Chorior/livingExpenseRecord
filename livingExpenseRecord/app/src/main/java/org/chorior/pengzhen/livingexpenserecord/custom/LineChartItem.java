package org.chorior.pengzhen.livingexpenserecord.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;

import org.chorior.pengzhen.livingexpenserecord.R;

/**
 * Created by pengzhen on 30/10/16.
 */

public class LineChartItem extends ChartItem {
    //private Context context;

    public LineChartItem(ChartData<?> cd) {
        super(cd);

        //this.context = context;
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }

    private static class ViewHolder {
        LineChart chart;
    }

    @Override
    public View getView(int position, View convertView, Context c) {
        ViewHolder holder = null;

        if (null == convertView) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_linechart, null);
            holder.chart = (LineChart) convertView.findViewById(R.id.lineChart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.chart.getDescription().setEnabled(false);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(31f);
        xAxis.setGranularity(1f);

        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setDrawGridLines(true); // no grid lines
        leftAxis.setLabelCount(5);
        holder.chart.getAxisRight().setEnabled(false); // no right axis

        // set data
        holder.chart.setData((LineData) mChartData);

        holder.chart.animateXY(1000, 1000);
        holder.chart.invalidate(); // refresh

        return convertView;
    }
}
