package com.haksoy.pertem.model;

public class ExplanationModel {
    String title;
    String exp;

    public ExplanationModel() {
    }

    public ExplanationModel(String title, String exp) {
        this.title = title;
        this.exp = exp;
    }

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
}
