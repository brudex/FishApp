package com.inspierra.fishapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.inspierra.fishapp.ChartModels.GraphChart;
import com.inspierra.fishapp.ChartModels.GraphService;
import com.inspierra.fishapp.ChartUtils.MyMarkerView;
import com.inspierra.fishapp.HelpingClasses.SaveEconomicIndicatorRequest;
import com.inspierra.fishapp.HelpingClasses.SaveEconomicIndicatorResponse;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.AcquaApiClient;
import com.inspierra.fishapp.Utilities.AcquahService;
import com.inspierra.fishapp.Utilities.PrefsUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EconomicIndicatorOutput extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,OnChartValueSelectedListener
{

    private BarChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;
    protected Typeface tfRegular;
    protected Typeface tfLight;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.econ_output_layout);

        setTitle("BarChartActivityMultiDataset");
        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        tvX = findViewById(R.id.tvXMax);
        tvX.setTextSize(10);
        tvY = findViewById(R.id.tvYMax);

        seekBarX = findViewById(R.id.seekBar1);
        seekBarX.setMax(50);
        seekBarX.setOnSeekBarChangeListener(this);

        seekBarY = findViewById(R.id.seekBar2);
        seekBarY.setOnSeekBarChangeListener(this);

        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);

//        chart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);

        chart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        seekBarX.setProgress(10);
        seekBarY.setProgress(100);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setTypeface(tfLight);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTypeface(tfLight);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        chart.getAxisRight().setEnabled(false);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        int groupCount = seekBarX.getProgress() + 1;
        int startYear = 1980;
        int endYear = startYear + groupCount;

        tvX.setText(String.format(Locale.ENGLISH, "%d-%d", startYear, endYear));
        tvY.setText(String.valueOf(seekBarY.getProgress()));
/***Graphing from server**/
        List<GraphChart> chartList = GraphService.getEconomicIndicatorGraph("","");

        if(chart.getData() == null){
            List<IBarDataSet> dataSets = new ArrayList<>();
            for (int i=0;i<chartList.size();i++) {
                dataSets.add(chartList.get(i).getBarDataSets(i));
            }
            BarData data = new BarData(dataSets);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTypeface(tfLight);
            chart.setData(data);
        }else{
            int datasetCount =chart.getData().getDataSetCount();
            if( datasetCount> 0){
                for (int i=0;i<chartList.size();i++) {
                    BarDataSet set = (BarDataSet) chart.getData().getDataSetByIndex(i);
                    set.setValues(chartList.get(i).getBarEntries());
                }

            }
        }

/*********/


//
//        ArrayList<BarEntry> values1 = new ArrayList<>();
//        ArrayList<BarEntry> values2 = new ArrayList<>();
//        ArrayList<BarEntry> values3 = new ArrayList<>();
//        ArrayList<BarEntry> values4 = new ArrayList<>();
//
//        float randomMultiplier = seekBarY.getProgress() * 100000f;
//
//        for (int i = startYear; i < endYear; i++) {
//            values1.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
//            values2.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
//            values3.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
//            values4.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
//        }
//
//        BarDataSet set1, set2, set3, set4;
//
//        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
//            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
//            set3 = (BarDataSet) chart.getData().getDataSetByIndex(2);
//            set4 = (BarDataSet) chart.getData().getDataSetByIndex(3);
//            set1.setValues(values1);
//            set2.setValues(values2);
//            set3.setValues(values3);
//            set4.setValues(values4);
//            chart.getData().notifyDataChanged();
//            chart.notifyDataSetChanged();
//        } else {
//            // create 4 DataSets
//            set1 = new BarDataSet(values1, "Company A");
//            set1.setColor(Color.rgb(104, 241, 175));
//            set2 = new BarDataSet(values2, "Company B");
//            set2.setColor(Color.rgb(164, 228, 251));
//            set3 = new BarDataSet(values3, "Company C");
//            set3.setColor(Color.rgb(242, 247, 158));
//            set4 = new BarDataSet(values4, "Company D");
//            set4.setColor(Color.rgb(255, 102, 0));
//            BarData data = new BarData(set1, set2, set3, set4);
//            data.setValueFormatter(new LargeValueFormatter());
//            data.setValueTypeface(tfLight);
//            chart.setData(data);
//        }

        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);
        // restrict the x-axis range
        chart.getXAxis().setAxisMinimum(startYear);
        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis().setAxisMaximum(startYear + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(startYear, groupSpace, barSpace);
        chart.invalidate();
    }






    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Barchart", "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Barchart", "Nothing selected.");
    }



    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}


}
