package com.haksoy.pertem.retrofit;

import android.util.Log;

import com.haksoy.pertem.model.Announce;
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

public class AnnounceMappingAdapter extends Converter.Factory {
    private String TAG = "AnnounceMappingAdapter";

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, List<Announce>>() {
            @Override
            public List<Announce> convert(ResponseBody value) throws IOException {
                Document doc = null;
                Document temp = null;
                List<Announce> announceList = new ArrayList<>();
                try {
                    doc = Jsoup.parse(value.string());

                    Elements announceElements = doc.getElementsByClass(Constant.announcesElement);
                    if (announceElements.size() > 0)
                        for (Element element : announceElements.get(0).getElementsByClass(Constant.announcesSubElement)) {
                            Announce announce = new Announce();

                            announce.setTitle(element.select("h3").text());
                            announce.setDate(element.select("p").get(3).text());
                            String subUrl = element.attr("onclick").split("'")[1];
                            temp = Jsoup.connect(Constant.ROOT_URL + subUrl).ignoreContentType(true).get();
                            announce.setDesc(temp.getElementsByClass("container").get(3).attr("class", "container").toString());
                            announceList.add(announce);
                        }

                } catch (IOException e) {

                    Log.e(TAG, e.getLocalizedMessage());
                }

                return announceList;
            }
        };
    }
}