package com.haksoy.pertem.model;

import java.util.List;

public class AnnounceResultModel {
    public AnnounceResultModel() {
    }

    public AnnounceResultModel(List<Announce> announceList) {
        this.announceList = announceList;
    }

    List<Announce> announceList;

    public List<Announce> getAnnounceList() {
        return announceList;
    }

    public void setAnnounceList(List<Announce> announceList) {
        this.announceList = announceList;
    }
}
