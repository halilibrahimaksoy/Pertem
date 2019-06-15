package com.haksoy.pertem.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.haksoy.pertem.model.Announce;
import com.haksoy.pertem.model.ExplanationModel;
import com.haksoy.pertem.model.MostQuestion;
import com.haksoy.pertem.model.Procurement;
import com.haksoy.pertem.model.UpdateConfig;
import com.haksoy.pertem.tools.Constant;
import com.haksoy.pertem.tools.Enums;
import com.haksoy.pertem.tools.INotifyAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FirebaseClient {
    private static String TAG = "FirebaseClient";
    private static FirebaseDatabase database;
    private static FirebaseClient instance;

    private FirebaseClient() {
        database = FirebaseDatabase.getInstance();
        try {
            database.setPersistenceEnabled(true);
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    public static FirebaseClient getInstance() {
        if (instance == null) {
            instance = new FirebaseClient();
        }
        return instance;
    }

    public static void getAnnounceList(final INotifyAction notifyAction) {
        final List<Announce> announceList;
        DatabaseReference mRef = database.getReference(Constant.ROOT_ANNOUNCE);
        mRef.keepSynced(true);
        announceList = new ArrayList<>();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    announceList.add(snapshot.getValue(Announce.class));
                }
                if (notifyAction != null)
                    notifyAction.onNotified(Enums.GetAnnounce, announceList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static void getProcurementList(final INotifyAction notifyAction) {
        final List<Procurement> procurementList;
        DatabaseReference mRef = database.getReference(Constant.ROOT_PROCUREMENT);
        mRef.keepSynced(true);
        procurementList = new ArrayList<>();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    procurementList.add(snapshot.getValue(Procurement.class));
                }
                if (notifyAction != null)
                    notifyAction.onNotified(Enums.GetProcurement, procurementList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getExplanationList(final INotifyAction notifyAction) {
        final HashMap<String, List<ExplanationModel>> listDataChild;
        listDataChild = new LinkedHashMap<>();
        final ArrayList<String> explanationCategoryList;
        final ArrayList<String> keys;
        explanationCategoryList = new ArrayList<>();
        keys = new ArrayList<>();
        DatabaseReference mRef = database.getReference(Constant.ROOT_EXPLANATION_CATEGORY);
        mRef.keepSynced(true);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String categoryText = snapshot.getValue(String.class);
                    String categoryKey = snapshot.getKey();
                    explanationCategoryList.add(categoryText);
                    keys.add(categoryKey);
                }
                DatabaseReference mRef2;
                for (int i = 0; i < explanationCategoryList.size(); i++) {

                    mRef2 = database.getReference(Constant.ROOT_EXPLANATION + "/" + keys.get(i));
                    final List<ExplanationModel> explanationModelList = new ArrayList<>();
                    final int finalI = i;
                    mRef2.keepSynced(true);
                    mRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ExplanationModel model = snapshot.getValue(ExplanationModel.class);
                                explanationModelList.add(model);
                            }
                            listDataChild.put(explanationCategoryList.get(finalI), explanationModelList);

                            if (explanationCategoryList.size() - finalI < 2)
                                notifyAction.onNotified(Enums.GetExplanation, listDataChild);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getMostQuestionCategory(final INotifyAction notifyAction) {
        DatabaseReference mRef = database.getReference(Constant.ROOT_MOST_QUESTION_CATEGORY);
        mRef.keepSynced(true);
        final Map<String, String> resulList = new LinkedHashMap<>();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String categoryText = snapshot.getValue(String.class);
                    String categoryKey = snapshot.getKey();
                    resulList.put(categoryKey, categoryText);
                }
                notifyAction.onNotified(Enums.GetMostQuestionCategory, resulList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getMostQuestion(final INotifyAction notifyAction, String categoryKey) {

        DatabaseReference mRef = database.getReference(Constant.ROOT_MOST_QUESTION + "/" + categoryKey);
        mRef.keepSynced(true);

        final List<MostQuestion> questionList = new ArrayList<>();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    questionList.add(snapshot.getValue(MostQuestion.class));

                }
                notifyAction.onNotified(Enums.GetMostQuestion, questionList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
