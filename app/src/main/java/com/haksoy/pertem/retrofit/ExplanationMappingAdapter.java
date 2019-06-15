package com.haksoy.pertem.retrofit;

import android.util.Log;

import com.haksoy.pertem.model.ExplanationModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

public class ExplanationMappingAdapter extends Converter.Factory {
    private String TAG = "ExplanationMappingAdapter";

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, Map<String, List<ExplanationModel>>>() {
            @Override
            public Map<String, List<ExplanationModel>> convert(ResponseBody value) throws IOException {
                Map<String, List<ExplanationModel>> resultMap = new LinkedHashMap<>();
                List<ExplanationModel> subResul = new ArrayList<>();
                Document doc = null;
                try {
                    doc = Jsoup.parse(value.string());
                    String h1 = "";
                    String old_h1 = "";
                    String h2 = "";
                    String text = "";
                    ExplanationModel model;
                    for (Element ee : doc.getElementsByClass("ac-category")) {
                        for (Element element : ee.children()) {
                            switch (element.tagName()) {
                                case "h1": {
                                    h1 = element.text().replace(element.select("small").text(), "");
                                    if (old_h1.isEmpty())
                                        old_h1 = h1;
                                    if (!old_h1.equals(h1)) {
                                        if(subResul.size()>0)
                                        resultMap.put(old_h1, subResul);
                                        old_h1 = h1;
                                        subResul = new ArrayList<>();
                                    }
                                    break;
                                }
                                case "h2": {
                                    if (!h2.isEmpty()) {
                                        model = new ExplanationModel(h2, text);
                                        subResul.add(model);
                                    }
                                    h2 = element.text();
                                    text = "";
                                    break;
                                }
                                default:
                                    if (!h2.isEmpty())
                                        text += element.toString();
                            }
                        }

                        if (!h2.isEmpty()) {
                            model = new ExplanationModel(h2, text);
                            subResul.add(model);
                        }
                        text = "";
                        h2 = "";

                    }
                    if (!h1.isEmpty() && subResul.size() > 0)
                        resultMap.put(h1, subResul);


                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());

                }
                return resultMap;
            }
        };
    }
}
