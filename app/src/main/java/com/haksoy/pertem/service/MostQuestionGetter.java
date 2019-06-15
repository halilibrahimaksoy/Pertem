//package com.haksoy.pertem.service;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.haksoy.pertem.model.MostQuestion;
//import com.haksoy.pertem.tools.Constant;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MostQuestionGetter extends AsyncTask<Void, Void, Map<String, List<MostQuestion>>> {
//    public static String TAG = "MostQuestionGetter";
//    Context context;
//
//    public MostQuestionGetter(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    protected Map<String, List<MostQuestion>> doInBackground(Void... voids) {
//
//        Map<String, List<MostQuestion>> resultMap = new LinkedHashMap<>();
//        List<MostQuestion> mostQuestionList = new ArrayList<>();
//        Document doc;
//        MostQuestion mostQuestion;
//        try {
//            doc = Jsoup.connect(Constant.ROOT_URL + Constant.mostQuestionsUrl).ignoreContentType(true).get();
//            Elements elements = doc.getElementsByClass(Constant.mostQuestionsElement);
//            int count = 1;
//            for (Element element : elements.select("li")) {
//                Elements questionElements = doc.select("div[id=ac" + count + "]");
//
//                mostQuestionList = new ArrayList<>();
//                for (int i = 0; i < questionElements.get(0).getElementsByClass(Constant.subMostQuestionsQuestion).size(); i++) {
//                    mostQuestion = new MostQuestion(questionElements.get(0).getElementsByClass(Constant.subMostQuestionsQuestion).get(i).select("p").text(), questionElements.get(0).getElementsByClass(Constant.subMostQuestionsAnswer).get(i).select("p").toString());
//                    mostQuestionList.add(mostQuestion);
//                }
//                resultMap.put(element.text(), mostQuestionList);
//                count++;
//
//
//            }
//
//        } catch (IOException e) {
//            Log.e(TAG, e.getLocalizedMessage());
//        }
//        return resultMap;
//    }
//}
