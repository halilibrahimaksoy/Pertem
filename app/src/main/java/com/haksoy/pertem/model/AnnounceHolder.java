package com.haksoy.pertem.model;

import android.view.View;
import android.widget.TextView;

import com.haksoy.pertem.R;

public class AnnounceHolder {
    public TextView txtTitle;
    public TextView txtAnnounceDateMoon;
    public TextView txtAnnounceDateDay;
    public TextView txtAnnounceDateDate;




    public AnnounceHolder(View itemView) {
        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txtAnnounceDateDate=itemView.findViewById(R.id.txtAnnounceDateDate);
        txtAnnounceDateDay=itemView.findViewById(R.id.txtAnnounceDateDay);
        txtAnnounceDateMoon=itemView.findViewById(R.id.txtAnnounceDateMoon);
    }
}
