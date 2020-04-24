package com.inspierra.fishapp.listviewitems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.inspierra.fishapp.R;

public class BarChartItem extends ChartItem {

    private final Typeface mTf;
    private String mDescription;
    private ValueFormatter mFormater;
    private int xAxisSize ;

    public BarChartItem(ChartData<?> cd, Context c) {
        super(cd);
        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
    }

    public BarChartItem(ChartData<?> cd,String title,ValueFormatter valueFormatter,int xAxisLength, Context c) {
        super(cd);
        mDescription=title;
        mFormater = valueFormatter;
        xAxisSize=xAxisLength;
        mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder;
        float groupSpace = 0.04f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.46f; // x2 dataset
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_barchart, null);
            holder.chart = convertView.findViewById(R.id.chart);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // apply styling
        holder.chart.getDescription().setEnabled(false);
        if(mDescription!=null){
            Description desc =new Description();
            desc.setText(mDescription);
            holder.chart.setDescription(desc);
        }
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(false);
        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setTypeface(mTf);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
       // xAxis.setDrawAxisLine(true);
        if(mFormater!=null){
            xAxis.setValueFormatter(mFormater);
        }else{
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });
        }
//        YAxis leftAxis = holder.chart.getAxisLeft();
//        leftAxis.setTypeface(mTf);
//        leftAxis.setLabelCount(5, false);
//        leftAxis.setSpaceTop(20f);
//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        YAxis rightAxis = holder.chart.getAxisRight();
//        rightAxis.setTypeface(mTf);
//        rightAxis.setLabelCount(5, false);
//        rightAxis.setSpaceTop(20f);
        //rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mChartData.setValueTypeface(mTf);
        // set data
        holder.chart.setData((BarData) mChartData);
        //holder.chart.setFitBars(true);
        // do not forget to refresh the chart
//        holder.chart.invalidate();
        holder.chart.animateY(700);
        holder.chart.getBarData().setBarWidth(barWidth);
        // restrict the x-axis range
        holder.chart.getXAxis().setAxisMinimum(0);
        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        holder.chart.getXAxis().setAxisMaximum(xAxisSize);
        holder.chart.groupBars(0, groupSpace, barSpace);
        holder.chart.invalidate();
        return convertView;
    }

    private static class ViewHolder {
        BarChart chart;
    }
}
