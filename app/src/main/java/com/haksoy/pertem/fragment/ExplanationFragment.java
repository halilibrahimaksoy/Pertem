package com.haksoy.pertem.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.haksoy.pertem.R;
import com.haksoy.pertem.adapter.ExplanationAdapter;
import com.haksoy.pertem.firebase.FirebaseClient;
import com.haksoy.pertem.model.ExplanationModel;
import com.haksoy.pertem.tools.AnimatedExpandableListView;
import com.haksoy.pertem.tools.Enums;
import com.haksoy.pertem.tools.INotifyAction;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExplanationFragment extends Fragment implements INotifyAction {

    private Unbinder unbinder;

    @BindView(R.id.lstMainExplanation)
    AnimatedExpandableListView lstMainExplanation;
    private HashMap<String, List<ExplanationModel>> listDataChild;
    private ExplanationAdapter explanationAdapter;
    private int lastExpandedPosition;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;

    public ExplanationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explanation, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        listDataChild = new LinkedHashMap<>();

        explanationAdapter = new ExplanationAdapter(getActivity(), listDataChild);

        lstMainExplanation.setAdapter(explanationAdapter);

        FirebaseClient.getInstance().getExplanationList(this);
        lstMainExplanation.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String exp = listDataChild.get(explanationAdapter.getHeadString(groupPosition)).get(childPosition).getExp();
                showAnswer(exp);
                return false;
            }
        });

        lstMainExplanation.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    lstMainExplanation.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        lstMainExplanation.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (lstMainExplanation.isGroupExpanded(groupPosition)) {
                    lstMainExplanation.collapseGroupWithAnimation(groupPosition);
                } else {
                    lstMainExplanation.expandGroupWithAnimation(groupPosition);
                }
                return true;
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
        getChildFragmentManager().beginTransaction().replace(R.id.frmMainContainer, new AnswerFragment(answerText, this)).addToBackStack("Explantation_detail").commit();
    }

    @Override
    public void onNotified(Object key, Object value) {
        if (key == Enums.GetExplanation) {
            listDataChild = (HashMap<String, List<ExplanationModel>>) value;
            explanationAdapter.setData(listDataChild);

        } else if (key == Enums.DismissFragment) {
            getChildFragmentManager().popBackStack();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
