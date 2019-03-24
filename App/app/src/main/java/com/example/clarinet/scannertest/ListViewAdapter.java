package com.example.clarinet.scannertest;

/**
 * Created by KIM on 2017-12-10.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter implements Serializable {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView barcodeTextView = (TextView) convertView.findViewById(R.id.bar) ;
        TextView nameTextView = (TextView) convertView.findViewById(R.id.name) ;
        TextView priceTextView = (TextView) convertView.findViewById(R.id.price) ;
        TextView weightTextView = (TextView) convertView.findViewById(R.id.weight) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        barcodeTextView.setText(listViewItem.getBar());
        nameTextView.setText(listViewItem.getName());
        priceTextView.setText(listViewItem.getPrice());
        //weightTextView.setText(listViewItem.getWeight());
        weightTextView.setText(listViewItem.getCount());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String Bar, String Name, String Price, String Weight) {
        ListViewItem item = new ListViewItem();

        item.setBar(Bar);
        item.setGoods(Name);
        item.setPrice(Price);
        item.setWeight(Weight);

        listViewItemList.add(item);

    }

    //아이템 삭제
    public void removeItem(int position){

        listViewItemList.remove(position);
    }

    public ListViewItem getInfo(int position)
    {
        ListViewItem reVal=listViewItemList.get(position);

        return reVal;
    }
    public int getPrice()
    {

        int sum=0;

        for(int i=0;i<listViewItemList.size();i++)
        {
            ListViewItem listViewItem=listViewItemList.get(i);
            sum+=Integer.parseInt(listViewItem.getPrice())*Integer.parseInt(listViewItem.getCount());
        }
        return sum;
    }

    public ArrayList<ListViewItem> returnArray()
    {
        return listViewItemList;
    }
}