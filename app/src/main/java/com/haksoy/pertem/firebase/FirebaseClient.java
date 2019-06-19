package com.haksoy.pertem.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.haksoy.pertem.model.Announce;
import com.haksoy.pertem.model.AnnounceResultModel;
import com.haksoy.pertem.model.ExplanataionResultModel;
import com.haksoy.pertem.model.MostQuestionResultModel;
import com.haksoy.pertem.model.Procurement;
import com.haksoy.pertem.model.ProcurementResultModel;
import com.haksoy.pertem.tools.Constant;
import com.haksoy.pertem.tools.Enums;
import com.haksoy.pertem.tools.INotifyAction;

import java.util.ArrayList;
import java.util.List;

public class FirebaseClient {
    private static String TAG = "FirebaseClient";
    private static FirebaseClient instance;
    private static FirebaseFirestore db;
    private static FirebaseFirestore offlineDb;

    private FirebaseClient() {
        db = FirebaseFirestore.getInstance();
        offlineDb = FirebaseFirestore.getInstance();
        try {
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            offlineDb.setFirestoreSettings(settings);
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
        db.collection(Constant.ROOT_ANNOUNCE).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<Announce> resultList = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        resultList = queryDocumentSnapshot.toObject(AnnounceResultModel.class).getAnnounceList();
                    }
                    if (notifyAction != null)
                        notifyAction.onNotified(Enums.GetAnnounce, resultList);
                } else
                    notifyAction.onNotified(Enums.GetAnnounce, resultList);
            }
        });

    }

    public static void uploadAnnounceList(final AnnounceResultModel resultModel) {
        db.collection(Constant.ROOT_ANNOUNCE).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        queryDocumentSnapshot.getReference().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                db.collection(Constant.ROOT_ANNOUNCE).add(resultModel);
                            }
                        });
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("sadfg");
            }
        });

    }

    public static void getProcurementList(final INotifyAction notifyAction) {
        db.collection(Constant.ROOT_PROCUREMENT).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                List<Procurement> resultList = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        resultList = queryDocumentSnapshot.toObject(ProcurementResultModel.class).getProcurementList();
                    }
                    if (notifyAction != null)
                        notifyAction.onNotified(Enums.GetProcurement, resultList);
                } else
                    notifyAction.onNotified(Enums.GetProcurement, resultList);
            }
        });

    }

    public static void uploadProcurementList(final ProcurementResultModel resultModel) {
        db.collection(Constant.ROOT_PROCUREMENT).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        queryDocumentSnapshot.getReference().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                db.collection(Constant.ROOT_ANNOUNCE).add(resultModel);
                            }
                        });
                    }
                }

            }
        });

    }

    public static void getExplanationList(final INotifyAction notifyAction) {
        offlineDb.collection(Constant.ROOT_EXPLANATION).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    notifyAction.onNotified(Enums.GetExplanation, queryDocumentSnapshot.toObject(ExplanataionResultModel.class).getExplanationMap());
                }
            }
        });
    }

    public static void uploadExplanationList(final ExplanataionResultModel resultModel) {
        db.collection(Constant.ROOT_EXPLANATION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        queryDocumentSnapshot.getReference().delete();
                    }
                    db.collection(Constant.ROOT_EXPLANATION).add(resultModel);
                }
            }
        });
    }

    public static void getMostQuestionList(final INotifyAction notifyAction) {
        offlineDb.collection(Constant.ROOT_MOST_QUESTION).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {

                    notifyAction.onNotified(Enums.GetMostQuestion, queryDocumentSnapshot.toObject(MostQuestionResultModel.class).getMostQuestionMap());
                }
            }
        });
    }

    public static void uploadMostQuestionList(final MostQuestionResultModel resultModel) {
        db.collection(Constant.ROOT_MOST_QUESTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        queryDocumentSnapshot.getReference().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                db.collection(Constant.ROOT_MOST_QUESTION).add(resultModel);
                            }
                        });
                    }
                }
            }
        });
    }

}
