package com.haksoy.pertem.retrofit;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.haksoy.pertem.model.SendToTopicRequest;
import com.haksoy.pertem.tools.Constant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    static Retrofit getSendNotificationToTopicClient(final String key) {

        final Interceptor headInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "key=" + key)
                        .build();

                return chain.proceed(request);
            }
        };
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(headInterceptor).addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.SEND_NOTIFICATION_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        return retrofit;
    }


    static Retrofit getAnnounceClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(60, TimeUnit.SECONDS)
        .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT_URL)
                .addConverterFactory(new AnnounceMappingAdapter())
                .client(client)
                .build();


        return retrofit;
    }

    static Retrofit getVersionStoreVersionClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.STORE_ROOT_URL)
                .addConverterFactory(new StoreVersionMappingAdapter())
                .client(client)
                .build();


        return retrofit;
    }
    static Retrofit getProcurementClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT_URL)
                .addConverterFactory(new ProcurementMappingAdapter())
                .client(client)
                .build();


        return retrofit;
    }

    static Retrofit getExplanationClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT_URL)
                .addConverterFactory(new ExplanationMappingAdapter())
                .client(client)
                .build();


        return retrofit;
    }

    static Retrofit getMostQuestionClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT_URL)
                .addConverterFactory(new MostQuestionMappingAdapter())
                .client(client)
                .build();


        return retrofit;
    }

    public static void getAnnounceList(Callback callback) {

        FirebaseApi api = getAnnounceClient().create(FirebaseApi.class);
        api.getAnnounce().enqueue(callback);
    }

    public static void getExplanation(Callback callback) {

        FirebaseApi api = getExplanationClient().create(FirebaseApi.class);
        api.getExplanation().enqueue(callback);
    }

    public static void getProcurement(Callback callback) {

        FirebaseApi api = getProcurementClient().create(FirebaseApi.class);
        api.getProcurement().enqueue(callback);
    }

    public static void getMostQuestion(Callback callback) {

        FirebaseApi api = getMostQuestionClient().create(FirebaseApi.class);
        api.getMostQuestion().enqueue(callback);
    }

    public static void getStoreVersion(Callback callback){
        FirebaseApi api = getVersionStoreVersionClient().create(FirebaseApi.class);
        api.getStoreVersion().enqueue(callback);
    }
    public static void sendNotification(final SendToTopicRequest request) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constant.NEWS).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mRef = database.getReference();
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FirebaseApi api = getSendNotificationToTopicClient(dataSnapshot.child("ServerKey").getValue(String.class)).create(FirebaseApi.class);

                        api.senNotificationToTopic(request).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                Log.d("FirebaseApi", response.message());
                                FirebaseMessaging.getInstance().subscribeToTopic(Constant.NEWS);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("FirebaseApi", t.getLocalizedMessage());
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }
}
