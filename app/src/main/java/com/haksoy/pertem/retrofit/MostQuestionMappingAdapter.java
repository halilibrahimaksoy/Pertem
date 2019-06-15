package com.haksoy.pertem.retrofit;

import android.util.Log;

import com.haksoy.pertem.model.MostQuestion;
import com.haksoy.pertem.tools.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class MostQuestionMappingAdapter extends Converter.Factory {

    private String TAG = "MostQuestionMappingAdapter";

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, Map<String, List<MostQuestion>>>() {

            @Override
            public Map<String, List<MostQuestion>> convert(ResponseBody value) throws IOException {
                Map<String, List<MostQuestion>> resultMap = new LinkedHashMap<>();
                List<MostQuestion> mostQuestionList = new ArrayList<>();
                Document doc;
                MostQuestion mostQuestion;
                try {
                    doc = Jsoup.parse(value.string());
                    Elements elements = doc.getElementsByClass(Constant.mostQuestionsElement);
                    int count = 1;
                    for (Element element : elements.select("li")) {
                        Elements questionElements = doc.select("div[id=ac" + count + "]");

                        mostQuestionList = new ArrayList<>();
                        for (int i = 0; i < questionElements.get(0).getElementsByClass(Constant.subMostQuestionsQuestion).size(); i++) {
                            mostQuestion = new MostQuestion(questionElements.get(0).getElementsByClass(Constant.subMostQuestionsQuestion).get(i).select("p").text(), questionElements.get(0).getElementsByClass(Constant.subMostQuestionsAnswer).get(i).select("p").toString());
                            mostQuestionList.add(mostQuestion);
                        }
                        resultMap.put(element.text(), mostQuestionList);
                        count++;


                    }

                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                return resultMap;
            }
        };
    }
}
