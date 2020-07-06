package com.haksoy.pertem.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.haksoy.pertem.R;
import com.haksoy.pertem.adapter.MostQuestionAdapter;
import com.haksoy.pertem.firebase.FirebaseClient;
import com.haksoy.pertem.model.MostQuestion;
import com.haksoy.pertem.tools.Enums;
import com.haksoy.pertem.tools.INotifyAction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubMostQuestionsFragment extends Fragment implements INotifyAction {

    String categoryKey;
    private Unbinder unbinder;

    @BindView(R.id.lstMainQuestions)
    ListView lstMainQuestions;
    private List<MostQuestion> questionList;
    private List<String> keys;

    INotifyAction notifyAction;
    private List<MostQuestion> originalValue;
    private MostQuestionAdapter mAdapter;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;

    public SubMostQuestionsFragment() {
        // Required empty public constructor
    }


    public SubMostQuestionsFragment(String categoryKey, INotifyAction notifyAction) {
        this.categoryKey = categoryKey;
        this.notifyAction = notifyAction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_most_questions, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    public void sendNotifyAction() {
        if (notifyAction != null)
            notifyAction.onNotified(Enums.SetNotifyAction, (INotifyAction) this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        questionList = new ArrayList<>();
        mAdapter = new MostQuestionAdapter(getActivity(), questionList);
        lstMainQuestions.setAdapter(mAdapter);

        FirebaseClient.getInstance().getMostQuestion(this, categoryKey);
        lstMainQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAnswer(questionList.get(position).getAnswer());
            }
        });

        adRequest = new AdRequest.Builder().build();
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
    }


    private void showAnswer(String answerText) {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            mInterstitialAd.loadAd(adRequest);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.frmMainContainer, new AnswerFragment(answerText, this)).addToBackStack("SubMostQuestions Detail").commit();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onNotified(Object key, Object value) {
        if (key == Enums.Search) {
            filter((String) value);
        } else if (key == Enums.GetMostQuestion) {
            mAdapter.setData((List<MostQuestion>) value);
        } else if (key == Enums.DismissFragment) {
            getChildFragmentManager().popBackStack();
        }
    }


    public void filter(String filterString) {

        if (originalValue == null)
            originalValue = new ArrayList<>(questionList);

        if (filterString == null || filterString.isEmpty())
            mAdapter.setData(originalValue);
        else {
            List<MostQuestion> filteredList = new ArrayList<>();

            for (MostQuestion announce : originalValue) {
                if (announce.getQuestion().toLowerCase().contains(filterString.toLowerCase()))
                    filteredList.add(announce);
            }
            mAdapter.setData(filteredList);
        }
    }

}
