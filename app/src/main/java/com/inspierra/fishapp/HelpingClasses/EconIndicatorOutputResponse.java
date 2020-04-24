package com.inspierra.fishapp.HelpingClasses;

import java.util.List;

public class EconIndicatorOutputResponse {
 public  List<ProfitablityField> profitability;
 public  List<LabelValueField> growthInWorkforce;
 public  List<LabelValueField> growthInProductionUnits;

    public static class ProfitablityField{
        public String label;
        public String moneySpent;
        public String moneyReceived;
    }

}

