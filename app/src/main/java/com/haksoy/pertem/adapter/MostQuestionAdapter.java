package com.haksoy.pertem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.haksoy.pertem.R;
import com.haksoy.pertem.model.MostQuestionHolder;
import com.haksoy.pertem.model.MostQuestion;

import java.util.List;

public class MostQuestionAdapter extends BaseAdapter {
    Context context;
    List<MostQuestion> questionList;
    LayoutInflater layoutInflater;

    public MostQuestionAdapter(Context context, List<MostQuestion> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public Object getItem(int position) {
        return questionList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.most_question_item, parent, false);
        }

        MostQuestion currentQuestion = questionList.get(position);
        MostQuestionHolder holder = new MostQuestionHolder(convertView);
        holder.txtQuestionKey.setText(String.valueOf(position + 1));
        holder.txtQuestionTitle.setText(currentQuestion.getQuestion());


        return convertView;
    }

    public void setData(List<MostQuestion> newList) {
        questionList.clear();
        questionList.addAll(newList);
        notifyDataSetChanged();
    }
}

