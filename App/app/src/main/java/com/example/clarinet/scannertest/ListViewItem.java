package com.example.clarinet.scannertest;

import java.io.Serializable;

/**
 * Created by KIM on 2017-12-10.
 */

public class ListViewItem implements Serializable{
    private String bar ;
    private String goods ;
    private String price;
    private String weight;
    private String realWeight;
    private String count;

    public void setBar(String barcode) {
        bar = barcode;
    }
    public void setGoods(String Goods) {
        goods = Goods;
    }
    public void setWeight(String Weight) {
        weight = Weight;
    }
    public void setPrice(String Price) {
        price = Price;
    }
    public void setRealWeight(String realWeight)
    {
        this.realWeight=realWeight;
    }
    public void setCount(String count){ this.count=count;}

    public String getBar() {
        return this.bar ;
    }
    public String getName() {
        return this.goods ;
    }
    public String getPrice() {
        return this.price ;
    }
    public String getWeight() {
        return this.weight ;
    }
    public String getRealWeight()
    {
        return this.realWeight;
    }
    public String getCount() { return this.count;}

}