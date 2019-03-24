package com.example.clarinet.scannertest;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.clarinet.scannertest.decorator.EventDecorator;
import com.example.clarinet.scannertest.decorator.OneDayDecorator;
import com.example.clarinet.scannertest.decorator.SaturdayDecorator;
import com.example.clarinet.scannertest.decorator.SundayDecorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;


public class Bill_activity extends AppCompatActivity {

    MaterialCalendarView materialCalendarView;
    ListView billlistview;
    Bill_ListViewAdapter  billlistviewadapter;
    Integer selectmonth;
    Integer selectday;

    public  ArrayList<Bill_ListViewItem> billlistViewItemList = new ArrayList<>();
    public ArrayList<Bill_ListViewItem> list = new ArrayList<Bill_ListViewItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_activity);


        // 검색에 사용할 데이터를 미리 저장 한다.
        settingList();

        // 캘린더 뷰
        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        // 리스트 뷰
        billlistview = (ListView) findViewById(R.id.bill_listview);
        billlistviewadapter = new Bill_ListViewAdapter( getApplicationContext(), // 현재화면의 제어권자
                R.layout.bill, list// 리스트뷰의 한행의 레이아웃
        );

        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

        // 캘린더 뷰 설정(주의 첫번째 날은 일요일, 월 단위 디스플레이모드)
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        // 데코레이터 추가(일요일, 토요일, 오늘)
        materialCalendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator(), new OneDayDecorator());

        // 특정 날짜 선택하기
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                //  Toast.makeText(Bill_activity.this, ""+date, Toast.LENGTH_SHORT).show();

                selectmonth = (date.getMonth());
                selectday = (date.getDay());

                search(selectmonth, selectday);

                billlistview.setAdapter(billlistviewadapter);

            }
        });



        // 리스트뷰 아이템 클릭 리스너
        billlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Bill.class); // 다음 넘어갈 클래스 지정

                intent.putExtra("store", list.get(position).store);
                intent.putExtra("total", list.get(position).total);
                intent.putExtra("time", list.get(position).time);
                intent.putExtra("item", list.get(position).item);

                startActivity(intent); // 다음 화면으로 넘어간다

            }
        });

        //new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

    }


    public ArrayList<Bill_ListViewItem> settingList(){

        String pathName = this.getExternalFilesDir(null).getPath();
        File fullName = new File(pathName + File.separator + SyncStateContract.Constants.CONTENT_DIRECTORY);

        try{
            FileInputStream fis = new FileInputStream(fullName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            billlistViewItemList = (ArrayList<Bill_ListViewItem>) ois.readObject();
            ois.close();
            fis.close();
        }catch(FileNotFoundException e){
            Toast.makeText(getApplicationContext(), "영수증이없습니다.", Toast.LENGTH_SHORT).show();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return billlistViewItemList;


       }

    public ArrayList <Bill_ListViewItem>  search(Integer month, Integer day) {


        list.clear();

        Bill_ListViewItem temp = new Bill_ListViewItem();
        temp.setMonth(month);
        temp.setDay(day);

        for(int i = 0;i < billlistViewItemList.size(); i++)
        {
            if (this.compareToMonth(temp,this.billlistViewItemList.get(i)))
            {
                //for(int u = 0;u < billlistViewItemList.size(); u++)
                // {
                if (this.compareToDay(temp,this.billlistViewItemList.get(i)))
                {
                    // list.clear();
                    list.add(billlistViewItemList.get(i));
                }
                // }
            }
        }

        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        return list;
    }


    // Month 비교 전용 메소드
    private boolean compareToMonth(Bill_ListViewItem o1, Bill_ListViewItem  o2) {
        return o1.getMonth().equals(o2.getMonth());
    }
    // Day 비교 전용 메소드
    private boolean compareToDay(Bill_ListViewItem o1, Bill_ListViewItem  o2) {
        return o1.getDay().equals(o2.getDay());
    }



    // 점찍는메소드
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {


        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            // 특정 요일에 점찍는 메소드 인데 결제확인이 되면 점 찍는걸로 할것임
            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            Integer Month;
            Integer Day;

            for (int i = 0; i < billlistViewItemList.size(); i++) {
                Month = billlistViewItemList.get(i).month;
                Day = billlistViewItemList.get(i).day;
                calendar.set(2018, (Month), (Day));
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }


            return dates;

        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));

        }
    }

}