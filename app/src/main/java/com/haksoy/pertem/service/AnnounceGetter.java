//package com.haksoy.pertem.service;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.haksoy.pertem.model.Announce;
//import com.haksoy.pertem.tools.Constant;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AnnounceGetter extends AsyncTask<Void, Void, List<Announce>> {
//
//    private static final String TAG = "AnnounceGetter";
//    Context context;
//
//    public AnnounceGetter(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    protected List<Announce> doInBackground(Void... voids) {
//        Document doc = null;
//        Document temp = null;
//        List<Announce> announceList = new ArrayList<>();
//        try {
//            doc = Jsoup.connect(Constant.ROOT_URL).ignoreContentType(true).get();
//
//            Elements announceElements = doc.getElementsByClass(Constant.announcesElement);
//
//            for (Element element : announceElements.get(0).getElementsByClass(Constant.announcesSubElement)) {
//                Announce announce = new Announce();
//
//                announce.setTitle(element.select("h3").text());
//                announce.setDate(element.select("p").get(3).text());
//                String subUrl = element.attr("onclick").split("'")[1];
//                temp = Jsoup.connect(Constant.ROOT_URL + subUrl).ignoreContentType(true).get();
//                announce.setDesc(temp.getElementsByClass("container").get(2).attr("class", "container").toString());
//                announceList.add(announce);
//            }
//
//        } catch (IOException e) {
//
//            Log.e(TAG, e.getLocalizedMessage());
//        }
//
//        return announceList;
//    }
//
//
//}
