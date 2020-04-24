package com.inspierra.fishapp.ChartModels;

import android.graphics.Color;
import android.util.Pair;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.inspierra.fishapp.HelpingClasses.EconIndicatorOutputResponse;
import com.inspierra.fishapp.HelpingClasses.FishHealthOutputResponse;
import com.inspierra.fishapp.HelpingClasses.LabelValueField;

import java.util.ArrayList;
import java.util.List;

public class GraphService {

     public static Pair<BarData, ValueFormatter> getProfitabilityBarChartItem( List<EconIndicatorOutputResponse.ProfitablityField> profitabilityFields){
        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < profitabilityFields.size(); i++) {
            values1.add(new BarEntry(i,   Float.parseFloat(profitabilityFields.get(i).moneySpent)));
            values2.add(new BarEntry(i, Float.parseFloat(profitabilityFields.get(i).moneyReceived)));
            labels.add(profitabilityFields.get(i).label);
        }
        BarDataSet set1, set2;
        for (int i = 0; i < profitabilityFields.size(); i++) {
            BarDataSet set  = new BarDataSet(values1, profitabilityFields.get(i).label);
        }
        set1 = new BarDataSet(values1, "MoneySpent");
        set1.setColor(Color.rgb(230, 0, 92));
        set2 = new BarDataSet(values2, "MoneyReceived");
        set2.setColor(Color.rgb(164, 228, 251));
        BarData data = new BarData(set1, set2);
        MyCustomValueFormater valueFormater = new MyCustomValueFormater(labels);
        return new Pair<BarData, ValueFormatter>(data,valueFormater);

    }

    public static Pair<BarData, ValueFormatter> getWaterQualityChartItem( List<FishHealthOutputResponse.WaterQualityField> waterQualityFields){
        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < waterQualityFields.size(); i++) {
            values1.add(new BarEntry(i,   waterQualityFields.get(i).good));
            values2.add(new BarEntry(i, waterQualityFields.get(i).bad));
            labels.add(waterQualityFields.get(i).label);
        }
        BarDataSet set1, set2;
        for (int i = 0; i < waterQualityFields.size(); i++) {
            BarDataSet set  = new BarDataSet(values1, waterQualityFields.get(i).label);
        }
        set1 = new BarDataSet(values1, "Good");
        set1.setColor(Color.rgb(230, 0, 92));
        set2 = new BarDataSet(values2, "Bad");
        set2.setColor(Color.rgb(164, 228, 251));
        BarData data = new BarData(set1, set2);
        MyCustomValueFormater valueFormater = new MyCustomValueFormater(labels);
        return new Pair<BarData, ValueFormatter>(data,valueFormater);
    }


    public static List<GraphChart> getEconomicIndicatorGraph(String fromDate, String toDate){
        List<GraphChart> list = new ArrayList<>();
        {
            GraphChart graph = new GraphChart();
            graph.XTitle = "Profitability";
            graph.YTitle = "Week";
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            list.add(graph);
        }
        {
            GraphChart graph = new GraphChart();
            graph.XTitle = "Indicators";
            graph.YTitle = "Weeks";
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            list.add(graph);
        }
        return list;
    }

    public static List<GraphChart> getFishHealthGraph(String fromDate, String toDate){
        List<GraphChart> list = new ArrayList<>();
        {
            GraphChart graph = new GraphChart();
            graph.XTitle = "Profitability";
            graph.YTitle = "Week";
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            list.add(graph);
        }
        {
            GraphChart graph = new GraphChart();
            graph.XTitle = "Indicators";
            graph.YTitle = "Weeks";
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            graph.addPoint(Math.random() * 10000, Math.random() * 2000);
            list.add(graph);
        }
        return list;
    }

    public static Pair<LineData, ValueFormatter> getLineDataFromValueLabels(String chartTitle, List<LabelValueField> valueLabelFieldList) {
        ArrayList<Entry> values1 = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        try {
            for (int i = 0; i < valueLabelFieldList.size(); i++) {
                values1.add(new Entry(i, (float) valueLabelFieldList.get(i).value));
                labels.add(valueLabelFieldList.get(i).label);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        LineDataSet d1 = new LineDataSet(values1, chartTitle );
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        LineData ln = new LineData(sets);
        MyCustomValueFormater valueFormater = new MyCustomValueFormater(labels);
        return new Pair<LineData, ValueFormatter>(ln,valueFormater);
    }

    public static class MyCustomValueFormater extends ValueFormatter {
        private final List<String> _labels;
        public MyCustomValueFormater(List<String> labels) {
            this._labels = labels;
        }
        @Override
        public String getFormattedValue(float value) {
            return _labels.get((int)value);
        }
    }
}
