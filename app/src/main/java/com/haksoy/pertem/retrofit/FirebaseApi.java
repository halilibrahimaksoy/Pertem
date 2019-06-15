package com.haksoy.pertem.retrofit;

import com.haksoy.pertem.model.Announce;
import com.haksoy.pertem.model.ExplanationModel;
import com.haksoy.pertem.model.MostQuestion;
import com.haksoy.pertem.model.Procurement;
import com.haksoy.pertem.model.SendToTopicRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FirebaseApi {

    @POST("/fcm/send")
    Call<Void> senNotificationToTopic(@Body SendToTopicRequest request);


    @GET("/")
    Call<List<Announce>> getAnnounce();

    @GET("/")
    Call<List<Procurement>> getProcurement();

    @GET("/Anasayfa/IcerikWeb/MDS02")
    Call<Map<String, List<ExplanationModel>>> getExplanation();

    @GET("/Anasayfa/IcerikWeb/MDS05")
    Call<Map<String, List<MostQuestion>>> getMostQuestion();

    @GET("details?id=com.haksoy.pertem")
    Call<String> getStoreVersion();
}
