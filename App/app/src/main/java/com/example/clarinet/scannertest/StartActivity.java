package com.example.clarinet.scannertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//import com.kmshack.splash.bill.Bill_activity;

public class StartActivity extends AppCompatActivity {

    private String name="";
    private String price="";
    TextView textView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.barcode_button).setOnClickListener(clickListener);
        findViewById(R.id.bill_button).setOnClickListener(clickListener);

    }

    private Button.OnClickListener clickListener = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.barcode_button){
                intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        MainActivity.class); // 다음 넘어갈 클래스 지정
                startActivityForResult(intent,1); // 다음 화면으로 넘어간다
            }else if(view.getId() == R.id.bill_button){
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        Bill_activity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK)
        {

            ListViewAdapter listViewAdapter=(ListViewAdapter)data.getSerializableExtra("adapter");
            ListViewItem listViewItem = listViewAdapter.getInfo(0);
            //Toast.makeText(this, listViewItem.getBar()+" "+listViewItem.getPrice(),Toast.LENGTH_SHORT).show();

            intent = new Intent(StartActivity.this, TakeBillList.class);
            intent.putExtra("adapter", listViewAdapter);

            startActivityForResult(intent,1);
        }
        if(resultCode==RESULT_CANCELED)
        {
            //모든걸 끝내고 복귀
        }
    }
}
