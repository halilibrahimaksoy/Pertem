package com.haksoy.pertem.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.haksoy.pertem.R;
import com.haksoy.pertem.firebase.FirebaseClient;
import com.haksoy.pertem.tools.Enums;
import com.haksoy.pertem.tools.INotifyAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MostQuestionFragment extends Fragment implements INotifyAction {


    @BindView(R.id.tabs)
    TabLayout mTabsLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    private Unbinder unbinder;
    private ViewPagerAdapter mPagerAdapter;
    INotifyAction notifyAction;
    Map<String, String> mostQuestionList;

    public MostQuestionFragment() {
        // Required empty public constructor
    }

    public MostQuestionFragment(INotifyAction notifyAction) {
        this.notifyAction = notifyAction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_most_questions, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupViewPager(mViewPager);


        mTabsLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabsLayout));
        mTabsLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((SubMostQuestionsFragment) mPagerAdapter.getItem(tab.getPosition())).sendNotifyAction();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((SubMostQuestionsFragment) mPagerAdapter.getItem(tab.getPosition())).filter("");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        mPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseClient.getInstance().getMostQuestionCategory(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onNotified(Object key, Object value) {
        if (key == Enums.GetMostQuestionCategory) {
            mostQuestionList = (Map<String, String>) value;
            for (String s : mostQuestionList.keySet()) {
                mPagerAdapter.addFragment(new SubMostQuestionsFragment(s, notifyAction), mostQuestionList.get(s));
            }

            mPagerAdapter.notifyDataSetChanged();
        }
    }
}
