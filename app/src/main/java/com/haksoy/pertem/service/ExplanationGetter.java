//package com.haksoy.pertem.service;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.haksoy.pertem.model.ExplanationModel;
//import com.haksoy.pertem.tools.Constant;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ExplanationGetter extends AsyncTask<Void, Void, Map<String, List<ExplanationModel>>> {
//
//    Context context;
//    private String TAG = "ExplanationGetter";
//
//    public ExplanationGetter(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    protected Map<String, List<ExplanationModel>> doInBackground(Void... voids) {
//        Map<String, List<ExplanationModel>> resultMap = new LinkedHashMap<>();
//        List<ExplanationModel> subResul = new ArrayList<>();
//        Document doc = null;
//        try {
//            doc = Jsoup.connect(Constant.ROOT_URL + Constant.explanationUrl).ignoreContentType(true).get();
//            String h1 = "";
//            String old_h1 = "";
//            String h2 = "";
//            String text = "";
//            ExplanationModel model;
//            for (Element ee : doc.getElementsByClass("ac-category")) {
//                for (Element element : ee.children()) {
//                    switch (element.tagName()) {
//                        case "h1": {
//                            h1 = element.text().replace(element.select("small").text(), "");
//                            if (old_h1.isEmpty())
//                                old_h1 = h1;
//                            if (!old_h1.equals(h1)) {
//                                resultMap.put(old_h1, subResul);
//                                old_h1 = h1;
//                                subResul = new ArrayList<>();
//                            }
//                            break;
//                        }
//                        case "h2": {
//                            if (!h2.isEmpty()) {
//                                model = new ExplanationModel(h2, text);
//                                subResul.add(model);
//                            }
//                            h2 = element.text();
//                            text = "";
//                            break;
//                        }
//                        default:
//                            if (!h2.isEmpty())
//                                text += element.toString();
//                    }
//                }
//
//                if (!h2.isEmpty()) {
//                    model = new ExplanationModel(h2, text);
//                    subResul.add(model);
//                }
//                text = "";
//                h2 = "";
//
//            }
//            if (!h1.isEmpty() && subResul.size() > 0)
//                resultMap.put(h1, subResul);
//
//
//        } catch (IOException e) {
//            Log.e(TAG, e.getLocalizedMessage());
//
//        }
//        return resultMap;
//    }
//}
