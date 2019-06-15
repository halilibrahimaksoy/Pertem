//package com.haksoy.pertem.service;
//
//import android.content.Context;
//import android.nfc.Tag;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.haksoy.pertem.model.Procurement;
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
//public class ProcurementGetter extends AsyncTask<Void, Void, List<Procurement>> {
//
//    public static String TAG = "ProcurementGetter";
//    Context context;
//
//    public ProcurementGetter(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    protected List<Procurement> doInBackground(Void... voids) {
//        Document doc = null;
//        Document temp = null;
//        List<Procurement> procurementList = new ArrayList<>();
//        try {
//            doc = Jsoup.connect(Constant.ROOT_URL).ignoreContentType(true).get();
//
//            Elements procurementElements = doc.getElementsByClass(Constant.procurementsElement);
//
//            for (Element element : procurementElements.get(0).getElementsByClass(Constant.procurementsSubElement)) {
//
//                Procurement procurement = new Procurement();
//
//                procurement.setTitle(element.select("h3").text());
//                procurement.setExp(element.select("p").get(0).text());
//                procurement.setDate(element.select("p").get(1).text());
//                String subUrl = element.attr("onclick").split("'")[1];
//                temp = Jsoup.connect(Constant.ROOT_URL + subUrl).ignoreContentType(true).get();
//                procurement.setDesc(temp.getElementsByClass("container").get(2).attr("class", "container").toString());
//                procurementList.add(procurement);
//            }
//
//            Procurement procurement = new Procurement();
//            procurement.setTitle("aaaaa");
//
//        //    procurementList.add(procurement);
//
//        } catch (IOException e) {
//
//            Log.e(TAG, e.getLocalizedMessage());
//        }
//
//        return procurementList;
//    }
//
//
//}
