package com.haksoy.pertem.retrofit;

import android.util.Log;

import com.haksoy.pertem.tools.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class StoreVersionMappingAdapter extends Converter.Factory {
    private String TAG = "StoreVersionMappingAdapter";

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, String>() {
            @Override
            public String convert(ResponseBody value) throws IOException {
                Document doc = null;
                Document temp = null;
                String storeVersion = "";
                try {
                    doc = Jsoup.parse(value.string());

                    Elements elements = doc.getElementsByClass(Constant.storeVersionElement);
                    storeVersion = elements.get(6).text();

                } catch (IOException e) {

                    Log.e(TAG, e.getLocalizedMessage());
                }

                return storeVersion;
            }
        };
    }
}