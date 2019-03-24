package com.example.clarinet.scannertest;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sabi on 2018-02-22.
 */


public class Bill_ListViewItem implements Serializable{

    String store = "" ;
    String total = "";
    String time = "" ;
    Integer month ;
    Integer day ;
    ArrayList <ItemofBilllist> item;

    public Bill_ListViewItem(String store, String total, String time, Integer month, Integer day,  ArrayList <ItemofBilllist> item) {
        super();
        this.store = store;
        this.total = total;
        this.time = time;
        this.month = month;
        this.day = day;
        this.item = item;
    }

    public void setMonth(Integer Month) { month = Month; }
    public void setDay(Integer Day) {
        day = Day;
    }


    public Integer getMonth() {return this.month ;}
    public Integer getDay() {
        return this.day ;
    }


    public Bill_ListViewItem() {}

}
