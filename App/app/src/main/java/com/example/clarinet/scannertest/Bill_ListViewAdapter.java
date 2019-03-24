package com.example.clarinet.scannertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sabi on 2018-02-22.
 */

public class Bill_ListViewAdapter extends BaseAdapter {

    Context context;
    int layout;
    LayoutInflater inf;
    ArrayList<Bill_ListViewItem> billListViewItems;


    public Bill_ListViewAdapter(Context context, int layout, ArrayList<Bill_ListViewItem> billListViewItems) {
        this.context = context;
        this.layout = layout;
        this.billListViewItems = billListViewItems;
        inf = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return billListViewItems.size() ;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return billListViewItems.get(position) ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bill_list, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView store = (TextView) convertView.findViewById(R.id.store) ;
        TextView total = (TextView) convertView.findViewById(R.id.total) ;
        TextView time = (TextView) convertView.findViewById(R.id.time) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득

        Bill_ListViewItem listViewItem1 = billListViewItems.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        store.setText(listViewItem1.store);
        total.setText(listViewItem1.total);
        time.setText(listViewItem1.time);

        return convertView;
    }

}
