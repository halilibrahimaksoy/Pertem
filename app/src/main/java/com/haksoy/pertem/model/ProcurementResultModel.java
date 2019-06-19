package com.haksoy.pertem.model;

import java.util.List;

public class ProcurementResultModel {
    List<Procurement> procurementList;

    public ProcurementResultModel() {
    }

    public ProcurementResultModel(List<Procurement> procurementList) {
        this.procurementList = procurementList;
    }

    public List<Procurement> getProcurementList() {
        return procurementList;
    }

    public void setProcurementList(List<Procurement> procurementList) {
        this.procurementList = procurementList;
    }
}
