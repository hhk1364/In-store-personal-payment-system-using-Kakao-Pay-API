package com.example.clarinet.scannertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sabi on 2018-02-23.
 */


// 영수증 리스트를 클릭하면 해당하는 영수증이 나오게 하는 클래스
public class Bill extends AppCompatActivity {

    ListView itemlistview;
    ItemofBilllistAdapter adapter;
    public ArrayList<ItemofBilllist> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill);

        itemlistview = (ListView) findViewById(R.id.itemlist);

        TextView store = (TextView) findViewById(R.id.store);
        TextView total = (TextView) findViewById(R.id.total);
        TextView time = (TextView) findViewById(R.id.time);

        Intent intent = getIntent();
        store.setText(intent.getStringExtra("store"));
        total.setText(intent.getStringExtra("total"));
        time.setText(intent.getStringExtra("time"));

       list = (ArrayList<ItemofBilllist>) getIntent().getSerializableExtra("item");

        adapter = new ItemofBilllistAdapter(getApplicationContext(),
                R.layout.bill, list);
        itemlistview.setAdapter(adapter);
    }
}

