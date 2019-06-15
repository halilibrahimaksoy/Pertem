package com.haksoy.pertem.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.haksoy.pertem.R;
import com.haksoy.pertem.model.Announce;
import com.haksoy.pertem.model.AnnounceHolder;

import java.util.List;

public class AnnounceAdapter extends BaseAdapter {

    Context context;
    List<Announce> announceList;
    LayoutInflater layoutInflater;

    public AnnounceAdapter(Context context, List<Announce> announceList) {
        this.context = context;
        this.announceList = announceList;
    }

    @Override
    public int getCount() {
        return announceList.size();
    }

    @Override
    public Object getItem(int position) {
        return announceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.announce_item, parent, false);
        }

        Announce currentAnnounce = announceList.get(position);
        AnnounceHolder holder = new AnnounceHolder(convertView);
        holder.txtTitle.setText(currentAnnounce.getTitle());


        holder.txtAnnounceDateMoon.setText(currentAnnounce.getMoon());
        holder.txtAnnounceDateDate.setText(currentAnnounce.getDay());
        holder.txtAnnounceDateDay.setText(currentAnnounce.getDayString());
        return convertView;
    }

    public void setData(List<Announce> newList) {
        announceList.clear();
        announceList.addAll(newList);
        notifyDataSetChanged();
    }

}
