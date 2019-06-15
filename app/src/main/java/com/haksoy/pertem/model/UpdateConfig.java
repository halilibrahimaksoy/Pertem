package com.haksoy.pertem.model;

public class UpdateConfig {
    private   long LastUpdateTime;
    private   long UpdateCycle;

    public long getLastUpdateTime() {
        return LastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        LastUpdateTime = lastUpdateTime;
    }

    public long getUpdateCycle() {
        return UpdateCycle;
    }

    public void setUpdateCycle(long updateCycle) {
        UpdateCycle = updateCycle;
    }
}
