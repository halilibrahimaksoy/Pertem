package com.haksoy.pertem.model;

import java.util.List;
import java.util.Map;

public class ExplanataionResultModel {
    Map<String, List<ExplanationModel>> explanationMap;

    public Map<String, List<ExplanationModel>> getExplanationMap() {
        return explanationMap;
    }

    public void setExplanationMap(Map<String, List<ExplanationModel>> explanationMap) {
        this.explanationMap = explanationMap;
    }
}
