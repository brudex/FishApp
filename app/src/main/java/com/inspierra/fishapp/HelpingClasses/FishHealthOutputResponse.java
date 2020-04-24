package com.inspierra.fishapp.HelpingClasses;

import java.util.List;

public class FishHealthOutputResponse {
    public List<WaterQualityField> waterQuality;
    public List<LabelValueField> mortality;
    public List<LabelValueField> phLevel;
    public List<LabelValueField> dissolvedOxygen;
    public List<LabelValueField> temperature;
    public List<LabelValueField> ammonia;
    public List<LabelValueField> nitrite;
    public List<LabelValueField> turbidity;

    public class WaterQualityField{
        public String label;
        public Float good;
        public Float bad;

    }

}




