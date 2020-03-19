package com.inspierra.fishapp.ChartModels;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GraphChart {



    public GraphChart(){
        items = new ArrayList<>();
        initColors();
    }

    public String XTitle;
    public String YTitle;
    public String chartType;
    List<GraphXYItem> items;

    public void addPoint(double x,double y) {
        items.add(new GraphXYItem(x,y));
    }

    public BarDataSet getBarDataSets(int index) {
        ArrayList<BarEntry> values = new ArrayList<>();
        for(int i=0;i<items.size();i++){
            values.add(new BarEntry((float)items.get(i).pointX, (float)items.get(i).pointY ));
        }
        BarDataSet barDataSet= new BarDataSet(values, XTitle);
        Log.i("Barchart","Barchart index is >>"+index);
        barDataSet.setColor(chartColors.get(index));
        return barDataSet;
    }

    public LineDataSet getLineDataSets(int index) {
        ArrayList<Entry> values = new ArrayList<>();
        for(int i=0;i<items.size();i++){
            values.add(new Entry((float)items.get(i).pointX, (float)items.get(i).pointY ));
        }
        LineDataSet lineDataSet= new LineDataSet(values, XTitle);
        Log.i("LineDataGraph","Linegraph index is >>"+index);
        lineDataSet.setColor(chartColors.get(index));
        lineDataSet.setCircleColor(chartColors.get(index));
        return lineDataSet;
    }

    public List<BarEntry> getBarEntries() {
        ArrayList<BarEntry> values = new ArrayList<>();
        for(int i=0;i<items.size();i++){
            values.add(new BarEntry((float)items.get(i).pointX, (float)items.get(i).pointY ));
        }
        return values;
    }

    public ArrayList<Entry> getLineEntries() {
        ArrayList<Entry> values = new ArrayList<>();
        for(int i=0;i<items.size();i++){
            values.add(new Entry((float)items.get(i).pointX, (float)items.get(i).pointY ));
        }
        return values;
    }


    private static List<Integer> chartColors = new ArrayList();
    private void initColors(){
        {
            if(chartColors.size()==0){
                chartColors.add(Color.rgb(104, 241, 175));
                chartColors.add(Color.rgb(164, 228, 251));
                chartColors.add(Color.rgb(242, 247, 158));
                chartColors.add(Color.rgb(255, 102, 0));
            }

        }
    }

}
