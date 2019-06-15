package com.haksoy.pertem.model;

public class Notification {
    String body;
    String title;

    public Notification(String title, String body) {
        this.body = body;
        this.title = title;
    }

    public Notification(String body) {
        this.body = body;
        this.title = "TSK Personel Alımları";
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
