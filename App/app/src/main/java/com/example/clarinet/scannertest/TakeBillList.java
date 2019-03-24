package com.example.clarinet.scannertest;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sabi on 2018-03-07.
 */

public class TakeBillList extends AppCompatActivity {

    //public ArrayList<ItemofBilllist> Item1 = new ArrayList<>();
    public ArrayList<Bill_ListViewItem> billlistViewItemList2 = new ArrayList<>();
    private ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ok_buy_activity);

        Intent intent=getIntent();
        listViewAdapter=(ListViewAdapter)intent.getSerializableExtra("adapter");
        save(listViewAdapter);

        Button button = (Button) findViewById(R.id.buttone);
        Button button2=findViewById(R.id.button3);

        button.setOnClickListener(mclick);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private Button.OnClickListener mclick = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(
                    getApplicationContext(), // 현재 화면의 제어권자
                    Bill_activity.class); // 다음 넘어갈 클래스 지정
            startActivity(intent); // 다음 화면으로 넘어간다
        }
    });


        // 저장 메소드 // 문제 있음
    public void save(ListViewAdapter listViewAdapter) {

        int sum=0;
        ArrayList<ItemofBilllist> Item1 = new ArrayList<>();
        ListViewItem listViewItem;
        for(int i=0;i<listViewAdapter.getCount();i++)
        {
            listViewItem=listViewAdapter.getInfo(i);
            sum+=Integer.parseInt(listViewItem.getPrice())*Integer.parseInt(listViewItem.getCount());
            Item1.add(new ItemofBilllist(listViewItem.getName(),listViewItem.getPrice()));
        }
        // 예 // listViewAdapter 변환 필요
        //Item1.add(new ItemofBilllist("우유", "222"));
        //Item1.add(new ItemofBilllist("바나나", "222"));

        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat mon=new SimpleDateFormat("MM");
        SimpleDateFormat day=new SimpleDateFormat("dd");
        SimpleDateFormat time=new SimpleDateFormat("hh:mm:ss");
        int intMon=Integer.parseInt(mon.format(date));
        int intDay=Integer.parseInt(day.format(date));
        String stringTime=time.format(date);

        //Toast.makeText(this, intMon+" "+intDay,Toast.LENGTH_SHORT).show();
        billlistViewItemList2.add(new Bill_ListViewItem("남서울대점", Integer.toString(sum), stringTime,intMon-1,intDay, Item1));
        String pathName = this.getExternalFilesDir(null).getPath();
        File isPath = new File(pathName);
        if (!isPath.isDirectory()) isPath.mkdirs();
        File fullName = new File(pathName + File.separator + SyncStateContract.Constants.CONTENT_DIRECTORY);


        try {
            FileOutputStream fos = new FileOutputStream(fullName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for(int i=0; i<billlistViewItemList2.size(); i++) {
                oos.writeObject(billlistViewItemList2);
            }
            oos.close();
            fos.close();

        }catch(IOException e) {
            e.printStackTrace();}
    }


}
