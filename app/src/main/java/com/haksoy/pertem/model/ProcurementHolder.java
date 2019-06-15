package com.haksoy.pertem.model;

import android.view.View;
import android.widget.TextView;

import com.haksoy.pertem.R;

public class ProcurementHolder {

    public TextView txtTitle;
    public TextView txtExp;
    public TextView txtDate;


    public ProcurementHolder(View view) {
        txtTitle = view.findViewById(R.id.txtTitle);
        txtExp = view.findViewById(R.id.txtExp);
        txtDate = view.findViewById(R.id.txtDate);
    }

}
