package com.haksoy.pertem.model;

import android.view.View;
import android.widget.TextView;

import com.haksoy.pertem.R;

public class MostQuestionHolder {
    public TextView txtQuestionTitle;
    public TextView txtQuestionKey;

    public MostQuestionHolder(View view) {

        txtQuestionKey = view.findViewById(R.id.txtQuestionKey);
        txtQuestionTitle = view.findViewById(R.id.txtQuestionTitle);
    }
}
