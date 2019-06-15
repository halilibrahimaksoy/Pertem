package com.haksoy.pertem.model;

import com.haksoy.pertem.tools.BaseModelPresenter;

public class Procurement implements BaseModelPresenter {
    String title;
    String exp;
    String desc;
    String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String onTitle() {
        return getTitle();
    }

    @Override
    public String onDesc() {
        return getExp();
    }
}
