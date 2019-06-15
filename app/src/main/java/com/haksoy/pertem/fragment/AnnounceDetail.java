package com.haksoy.pertem.fragment;


import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.haksoy.pertem.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnounceDetail extends Fragment {


    @BindView(R.id.wbvMain)
    WebView wbvMain;
    String htmlText;

    private Unbinder unbinder;

    public AnnounceDetail(String htmlText) {
        this.htmlText = htmlText;
    }

    public AnnounceDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announce_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        wbvMain.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                try {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder().setToolbarColor(getActivity().getResources().getColor(R.color.colorPrimary));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(getActivity(), request.getUrl());
                    return true;
                } catch (ActivityNotFoundException ex) {
                    Log.e("AnnounceDetail", ex.getLocalizedMessage());
                    return super.shouldOverrideUrlLoading(view, request);
                }

            }
        });
        wbvMain.loadDataWithBaseURL("", htmlText, mimeType, encoding, "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
