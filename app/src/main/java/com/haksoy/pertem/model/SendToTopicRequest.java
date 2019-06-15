package com.haksoy.pertem.model;

import com.haksoy.pertem.tools.Constant;

public class SendToTopicRequest {
    String to;
    Notification notification;

    public SendToTopicRequest(String to, Notification notification) {
        this.to = Constant.TOPIC_TAG + to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = Constant.TOPIC_TAG + to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
