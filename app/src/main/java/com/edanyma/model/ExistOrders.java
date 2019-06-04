package com.edanyma.model;

import java.io.Serializable;
import java.util.List;

public class ExistOrders implements Serializable {

    List<ClientOrderModel> existOrders;

    public List< ClientOrderModel > getExistOrders() {
        return existOrders;
    }

    public void setExistOrders( List< ClientOrderModel > existOrders ) {
        this.existOrders = existOrders;
    }
}
