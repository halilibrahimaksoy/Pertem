package com.haksoy.pertem.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haksoy.pertem.R;
import com.haksoy.pertem.model.Announce;
import com.haksoy.pertem.model.ExplanationModel;
import com.haksoy.pertem.tools.AnimatedExpandableListView;

import java.util.HashMap;
import java.util.List;

public class ExplanationAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    Context context;

    HashMap<String, List<ExplanationModel>> listHashMap;

    public ExplanationAdapter(Context context, HashMap<String, List<ExplanationModel>> listHashMap) {
        this.context = context;
        this.listHashMap = listHashMap;


    }

    public String getHeadString(int position) {
        return (String) listHashMap.keySet().toArray()[position];
    }

    @Override
    public int getGroupCount() {
        return listHashMap.keySet().size();
    }

//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return listHashMap.get(getHeadString(groupPosition)).size();
//    }

    @Override
    public Object getGroup(int groupPosition) {
        return getHeadString(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(getHeadString(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headString = getHeadString(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.explanation_group_item, null);
        }
        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        txtTitle.setText(headString);
        txtTitle.setTypeface(null, Typeface.BOLD);
        return convertView;

    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String itemString = listHashMap.get(getHeadString(groupPosition)).get(childPosition).getTitle();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.explanation_item, null);
        }
        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        txtTitle.setText(itemString);
        txtTitle.setTypeface(null, Typeface.NORMAL);


        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return listHashMap.get(getHeadString(groupPosition)).size();

    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setData(HashMap<String, List<ExplanationModel>> newList) {
        listHashMap.clear();
        listHashMap= (HashMap<String, List<ExplanationModel>>) newList;
        notifyDataSetChanged();
    }
}
