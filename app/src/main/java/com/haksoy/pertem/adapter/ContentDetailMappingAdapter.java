package com.haksoy.pertem.adapter;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ContentDetailMappingAdapter extends Converter.Factory {
    private String TAG = "ContentDetailMappingAdapter";

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, String>() {
            @Override
            public String convert(ResponseBody value) throws IOException {
                Document doc = null;
                String result = null;
                try {
                    doc = Jsoup.parse(value.string());
                    result = doc.getElementsByClass("container").get(3).attr("class", "container").toString();

                    if(result.isEmpty())
                        Log.e(TAG, "ContentDetailMappingAdapter result is empty");
                } catch (IOException e) {

                    Log.e(TAG, e.getLocalizedMessage());
                }

                return result;
            }
        };
    }
}