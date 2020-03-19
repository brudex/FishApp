package com.inspierra.fishapp.ChartModels;

import java.util.ArrayList;
import java.util.List;

public class GraphService {
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
}
