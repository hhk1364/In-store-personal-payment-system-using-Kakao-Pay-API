package com.example.clarinet.scannertest;

import java.io.Serializable;

/**
 * Created by sabi on 2018-03-06.
 */

public class ItemofBilllist implements Serializable {

    String  Name;
    String Price;

    public ItemofBilllist(String name,  String price) {
        super();
        this.Name = name;
        this.Price = price;
    }


    public void setName(String name) { Name = name; }
    public void setPrice(String price) {
        Price = price;
    }

    public void getName(String name) { Name = name; }
    public void getPrice(String price) {
        Price = price;
    }


    public ItemofBilllist() {}

}
