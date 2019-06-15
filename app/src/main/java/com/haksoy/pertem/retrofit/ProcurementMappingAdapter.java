package com.haksoy.pertem.retrofit;

import android.util.Log;

import com.haksoy.pertem.model.Procurement;
import com.haksoy.pertem.tools.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ProcurementMappingAdapter extends Converter.Factory {
    private String TAG = "ProcurementMappingAdapter";

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, List<Procurement>>() {
            @Override
            public List<Procurement> convert(ResponseBody value) throws IOException {
                Document doc = null;
                Document temp = null;
                List<Procurement> procurementList = new ArrayList<>();
                try {
                    doc = Jsoup.parse(value.string());
                    Elements procurementElements = doc.getElementsByClass(Constant.procurementsElement);
                    if (procurementElements.size()>0)
                    for (Element element : procurementElements.get(0).getElementsByClass(Constant.procurementsSubElement)) {

                        Procurement procurement = new Procurement();

                        procurement.setTitle(element.select("h3").text());
                        procurement.setExp(element.select("p").get(0).text());
                        procurement.setDate(element.select("p").get(1).text());
                        String subUrl = element.attr("onclick").split("'")[1];
                        temp = Jsoup.connect(Constant.ROOT_URL + subUrl).ignoreContentType(true).get();
                        procurement.setDesc(temp.getElementsByClass("container").get(2).attr("class", "container").toString());
                        procurementList.add(procurement);
                    }

                } catch (IOException e) {

                    Log.e(TAG, e.getLocalizedMessage());
                }

                return procurementList;

            }
        };
    }
}
