package com.haksoy.pertem.service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.haksoy.pertem.firebase.FirebaseClient;
import com.haksoy.pertem.model.Announce;
import com.haksoy.pertem.model.ExplanationModel;
import com.haksoy.pertem.model.MostQuestion;
import com.haksoy.pertem.model.Notification;
import com.haksoy.pertem.model.Procurement;
import com.haksoy.pertem.model.SendToTopicRequest;
import com.haksoy.pertem.model.UpdateConfig;
import com.haksoy.pertem.retrofit.RetrofitClient;
import com.haksoy.pertem.tools.BaseModelPresenter;
import com.haksoy.pertem.tools.Constant;
import com.haksoy.pertem.tools.Enums;
import com.haksoy.pertem.tools.INotifyAction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataUpdateHelper {
    static String TAG = "DataUpdateService";

    private INotifyAction notifyAction;
    private boolean Procurement;
    private boolean Announce;
    private boolean Explanation;
    private boolean MostQuestions;
    private boolean StoreVersion;

    Context context;

    public DataUpdateHelper(INotifyAction notifyAction) {
        this.notifyAction = notifyAction;
    }

    public void controlAndUpdate(Context context) {
        this.context = context;
//        versionControl();
        Log.d(TAG, "controlAndUpdate started");
        UpdatedControlAndSetData(Enums.Procurement.name());
        UpdatedControlAndSetData(Enums.Announce.name());
        UpdatedControlAndSetData(Enums.Explanation.name());
        UpdatedControlAndSetData(Enums.MostQuestions.name());

    }

    private void versionControl() {

        RetrofitClient.getStoreVersion(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                controlAndNotifyVersion(response.body().toString());
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void controlAndNotifyVersion(String newVersion) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(context.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (!pInfo.versionName.equals(newVersion)) {
            notifyAction.onNotified(Enums.NEED_UPDATE, null);
        } else
            controlAndNotifyAction(Enums.StoreVersion.name());
    }


    private void procurementsUpdate() {
        RetrofitClient.getProcurement(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                List<Procurement> procurementList = (List<Procurement>) response.body();
                onNewProcurementControl(procurementList);

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
                controlAndNotifyAction(Enums.Procurement.name());
            }
        });

    }

    private void announcesUpdate() {
        RetrofitClient.getAnnounceList(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                List<Announce> announceList = (List<Announce>) response.body();
                onNewAnnounceControl(announceList);


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
                controlAndNotifyAction(Enums.Announce.name());
            }
        });

    }

    private void onNewProcurementControl(final List<Procurement> newList) {
        FirebaseClient.getInstance().getProcurementList(new INotifyAction() {
            @Override
            public void onNotified(Object key, Object value) {
                if (key == Enums.GetProcurement) {
                    List<BaseModelPresenter> currentList = (List<BaseModelPresenter>) value;
                    List<BaseModelPresenter> newList1 = (List<BaseModelPresenter>) ((Object) newList);
                    if (onCompareAndNotifyList(currentList, newList1)) {
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mRef = database.getReference(Constant.ROOT_PROCUREMENT);

                        mRef.removeValue();
                        for (Procurement procurement : newList) {
                            mRef.child(mRef.push().getKey()).setValue(procurement);
                        }


                    }
                    setLastUpdatedTime(Enums.Procurement.name());
                    controlAndNotifyAction(Enums.Procurement.name());
                }
            }
        });
    }

    private void onNewAnnounceControl(final List<Announce> newList) {


        FirebaseClient.getInstance().getAnnounceList(new INotifyAction() {
            @Override
            public void onNotified(Object key, Object value) {
                if (key == Enums.GetAnnounce) {
                    List<BaseModelPresenter> currentList = (List<BaseModelPresenter>) value;
                    List<BaseModelPresenter> newList1 = (List<BaseModelPresenter>) ((Object) newList);
                    if (onCompareAndNotifyList(currentList, newList1)) {

                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference mRef = database.getReference(Constant.ROOT_ANNOUNCE);
                        mRef.removeValue();
                        for (Announce announce : newList) {
                            mRef.child(mRef.push().getKey()).setValue(announce);
                        }

                    }
                    setLastUpdatedTime(Enums.Announce.name());
                    controlAndNotifyAction(Enums.Announce.name());
                }
            }
        });

    }

    private boolean onCompareAndNotifyList
            (List<BaseModelPresenter> oldList, List<BaseModelPresenter> newList) {
        boolean isNew = true;
        if (newList == null || newList.size() == 0)
            return false;
        for (BaseModelPresenter newItem : newList) {
            isNew = true;
            for (BaseModelPresenter oldItem : oldList) {
                if (newItem.onTitle().equals(oldItem.onTitle()))
                    isNew = false;
            }

            if (isNew) {
                notifyOtherUser(newItem);
                break;
            }
        }
        return isNew;

    }

    private void notifyOtherUser(BaseModelPresenter modelPresenter) {
        RetrofitClient.sendNotification(new SendToTopicRequest(Constant.NEWS_TOPIC, new Notification(modelPresenter.onTitle())));
        Log.d(TAG, "notifyOtherUser  : " + modelPresenter.onTitle() + " : " + modelPresenter.onDesc());
    }

    private void mostQuestionsUpdate() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = database.getReference(Constant.ROOT_MOST_QUESTION_CATEGORY);
        final DatabaseReference mRef2 = database.getReference(Constant.ROOT_MOST_QUESTION);

        RetrofitClient.getMostQuestion(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Map<String, List<MostQuestion>> mostQuestionMap = (Map<String, List<MostQuestion>>) response.body();

                mRef.removeValue();
                mRef2.removeValue();

                for (String key : mostQuestionMap.keySet()) {
                    String keys = mRef.push().getKey();

                    mRef.child(keys).setValue(key);
                    mRef2.child(keys).setValue(mostQuestionMap.get(key));
                }
                setLastUpdatedTime(Enums.MostQuestions.name());

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
        controlAndNotifyAction(Enums.MostQuestions.name());
    }

    private void explanationUpdate() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = database.getReference(Constant.ROOT_EXPLANATION_CATEGORY);
        final DatabaseReference mRef2 = database.getReference(Constant.ROOT_EXPLANATION);

        RetrofitClient.getExplanation(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {
                Map<String, List<ExplanationModel>> explanationMap = (Map<String, List<ExplanationModel>>) response.body();

                mRef.removeValue();
                mRef2.removeValue();
                for (String key : explanationMap.keySet()) {
                    String keys = mRef.push().getKey();

                    mRef.child(keys).setValue(key);
                    mRef2.child(keys).setValue(explanationMap.get(key));
                }

                setLastUpdatedTime(Enums.Explanation.name());

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
        controlAndNotifyAction(Enums.Explanation.name());
    }

    private void setLastUpdatedTime(String type) {
        Log.d(TAG, "setLastUpdatedTime  for:" + type);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String updateString = dateFormat.format(new Date());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference(Constant.UPDATED_CONFIG);
        mRef.child(type).child(Constant.LAST_UPDATE_TIME).setValue(ServerValue.TIMESTAMP);
        try {
            mRef.child(type).child(Constant.LAST_UPDATE_DATE).setValue(ServerValue.TIMESTAMP +  " : "+updateString+" : "+context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void UpdatedControlAndSetData(final String type) {

        getUpdateConfig(type, new INotifyAction() {
            @Override
            public void onNotified(Object key, Object value) {
                if (key == Enums.GetUpdateConfig) {
                    UpdateConfig config = (UpdateConfig) value;
                    if ((System.currentTimeMillis() - ((UpdateConfig) value).getLastUpdateTime()) > ((UpdateConfig) value).getUpdateCycle()) {
                        setData(type);
                    } else
                        controlAndNotifyAction(type);
                }

            }
        });
    }

    private void setData(String type) {
        Log.d(TAG, "set data call for:" + type);
        switch (Enums.valueOf(type)) {
            case Announce: {
                announcesUpdate();
                break;
            }
            case Procurement: {
                procurementsUpdate();
                break;
            }
            case MostQuestions: {
                mostQuestionsUpdate();
                break;
            }
            case Explanation: {
                explanationUpdate();
                break;
            }
        }
    }

    private void controlAndNotifyAction(String type) {
        Log.d(TAG, "controlAndNotifyAction for:" + type);
        switch (Enums.valueOf(type)) {
            case Announce: {
                Announce = true;
                break;
            }
            case Procurement: {
                Procurement = true;
                break;
            }
            case MostQuestions: {
                MostQuestions = true;
                break;
            }
            case Explanation: {
                Explanation = true;
                break;
            }
            case StoreVersion: {
                StoreVersion = true;
                break;
            }
        }

        if (Announce && Procurement && MostQuestions && Explanation) {
            Log.d(TAG, "controlAndUpdate finished");
            notifyAction.onNotified(Enums.UpdateCompleted, null);
        }

    }

    private static void getUpdateConfig(String type, final INotifyAction notifyAction) {
        Log.d(TAG, "getUpdateConfig for:" + type);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference(Constant.UPDATED_CONFIG);
        mRef.child(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UpdateConfig config = dataSnapshot.getValue(UpdateConfig.class);
                notifyAction.onNotified(Enums.GetUpdateConfig, config);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
