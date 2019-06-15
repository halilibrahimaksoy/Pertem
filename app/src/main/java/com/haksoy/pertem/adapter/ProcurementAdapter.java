package com.haksoy.pertem.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.haksoy.pertem.R;
import com.haksoy.pertem.model.Procurement;
import com.haksoy.pertem.model.ProcurementHolder;

import java.util.List;

public class ProcurementAdapter extends BaseAdapter {

    Context context;
    List<Procurement> procurementList;
    LayoutInflater layoutInflater;

    public ProcurementAdapter(Context context, List<Procurement> procurementList) {
        this.context = context;
        this.procurementList = procurementList;
    }

    @Override
    public int getCount() {
        return procurementList.size();
    }

    @Override
    public Object getItem(int position) {
        return procurementList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.procurement_item, parent, false);
        }

        Procurement currentProcurement = procurementList.get(position);
        ProcurementHolder holder = new ProcurementHolder(convertView);
        holder.txtTitle.setText(currentProcurement.getTitle());
        holder.txtTitle.setTypeface(null,Typeface.BOLD);
        holder.txtExp.setText(currentProcurement.getExp());
        holder.txtDate.setText(currentProcurement.getDate());
        holder.txtDate.setTypeface(null,Typeface.BOLD);
        return convertView;
    }

    public void setData(List<Procurement> newList) {
        procurementList.clear();
        procurementList.addAll(newList);
        notifyDataSetChanged();
    }
}
