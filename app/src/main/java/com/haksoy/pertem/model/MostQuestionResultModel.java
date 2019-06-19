package com.haksoy.pertem.model;

import java.util.List;
import java.util.Map;

public class MostQuestionResultModel {
    Map<String, List<MostQuestion>> mostQuestionMap;

    public Map<String, List<MostQuestion>> getMostQuestionMap() {
        return mostQuestionMap;
    }

    public void setMostQuestionMap(Map<String, List<MostQuestion>> mostQuestionMap) {
        this.mostQuestionMap = mostQuestionMap;
    }
}
