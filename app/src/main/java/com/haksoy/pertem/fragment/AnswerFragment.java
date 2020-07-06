package com.haksoy.pertem.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.haksoy.pertem.R;
import com.haksoy.pertem.tools.Enums;
import com.haksoy.pertem.tools.INotifyAction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AnswerFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.wbvMain)
    WebView wbvMain;

    @BindView(R.id.rltvMain)
    RelativeLayout rltvMain;
    String htmlText;
    INotifyAction notifyAction;

    public AnswerFragment(String htmlText, INotifyAction notifyAction) {
        this.htmlText = htmlText;
        this.notifyAction = notifyAction;
    }

    public AnswerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";


        if (htmlText != null) {
            wbvMain.getLayoutParams().height = htmlText.length() * 2;
            if (wbvMain.getLayoutParams().height < 200)
                wbvMain.getLayoutParams().height = 200;
            wbvMain.requestLayout();
        }
        wbvMain.loadDataWithBaseURL("", htmlText, mimeType, encoding, "");

        rltvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyAction.onNotified(Enums.DismissFragment, null);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
