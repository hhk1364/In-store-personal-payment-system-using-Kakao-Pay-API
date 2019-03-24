package com.example.clarinet.scannertest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;


import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class MainActivity extends Activity {

    private BeaconManager beaconManager;
    private BeaconRegion region;

    private static final String TAG = MainActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private String lastWeight;
    private float totalWeight=0;
    private int totalPrice=0;
    private int totalList=0;

    private String myIp="122.45.214.122";
    private String inUrl="http://"+myIp+":13241/jdbc_test/WebContent/test/android_customer_insert.jsp";
    private String delUrl="http://"+myIp+":13241/jdbc_test/WebContent/test/android_customer_delete.jsp";
    private String clrUrl="http://"+myIp+":13241/jdbc_test/WebContent/test/android_customer_clear.jsp";
    private String upUrl="http://"+myIp+":13241/jdbc_test/WebContent/test/android_customer_update.jsp";
    private InsertAndDelete inDel=new InsertAndDelete(inUrl,delUrl,clrUrl,upUrl); // jjh

    private int inOrDel;

    ListViewAdapter listAdapter;
    ListView listView;

    AlertDialog.Builder dialog;
    EditText etEdit;


    // 전체 무게 합산 텍스트 뷰
    TextView totalWei;
    // 전체 상품수 합산 텍스트 뷰
    TextView totallist;

    // JSONArray에서 String형변환 객체
    JtoS jtos;

    //pay
    private Button payButton;
    private Intent payintent;
    private Intent mainIntent;

    //test 05 24
    private String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> list) {
                Log.e("tag"," ");
                if (!list.isEmpty()) {
                    payintent=new Intent(MainActivity.this,KakaoActivity.class);
                    payintent.putExtra("payPrice",listAdapter.getPrice());

                    startActivityForResult(payintent,1);

                    Beacon nearestBeacon = list.get(0);

                    //List<String> places = placesNearBeacon(nearestBeacon);
                    // TODO: update the UI here
                    Log.d("Airport", "Nearest places: " + nearestBeacon.getRssi());

                }
            }
        });

        region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 10001, 19641);

        //test ( dont need it)
        //IntentIntegrator integrator = new IntentIntegrator(this); // .initiateScan()
        //integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        //integrator.setOrientationLocked(false);
        //integrator.initiateScan();

        mainIntent=getIntent();

        /*
        //payButton
        payButton=findViewById(R.id.payButton);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(),listAdapter.getPrice()+" 입니다.",Toast.LENGTH_SHORT).show();

                payintent=new Intent(MainActivity.this,KakaoActivity.class);
                payintent.putExtra("payPrice",listAdapter.getPrice());

                startActivityForResult(payintent,1);
            }
        });
        */
        //리스트 뷰 어댑터
        listAdapter = new ListViewAdapter();
        listView = (ListView) findViewById(R.id.list_preview);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final ListViewItem listViewItem;
                listViewItem=listAdapter.getInfo(i);

                String message = "해당 데이터를 삭제하시겠습니까?\n" +
                        "목록 번호 : " + (i+1) + "\n" +
                        "선택한 상품 : " + listViewItem.getName() + "\n";

                // 선택된 아이템을 리스트에서 삭제한다.
                DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        // 아이템 삭제 및 갱신
                        listAdapter.removeItem(i);

                        listAdapter.notifyDataSetChanged();
                        inDel.Delete(listViewItem.getBar());

                        totalWeight-=Float.parseFloat(listViewItem.getWeight());
                        //test 05 24
                        totalPrice-=Integer.parseInt(listViewItem.getPrice());

                        totalList--;
                        //전체 무게, 상품수 텍스트 뷰에 출력
                        //totalWei.setText("총 무게 : "+(int) totalWeight);
                        totalWei.setText("총   계 : "+(int) totalPrice);
                        totallist.setText("상품 수 : "+(int)totalList);
                    }
                };

                // 삭제를 물어보는 다이얼로그를 생성한다.
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("상품 취소")
                        .setMessage(message)
                        .setPositiveButton("삭제", deleteListener)
                        .show();
                /*
                listAdapter.removeItem(i);

                Toast.makeText(MainActivity.this, infoClass.name + " 삭제",Toast.LENGTH_LONG).show();
                listAdapter.notifyDataSetChanged();
                inDel.Delete(infoClass.barcode);

                totalWeight-=Float.parseFloat(infoClass.weight);
                totalList--;
                //전체 무게, 상품수 텍스트 뷰에 출력
                totalWei.setText("총 무게 : "+(int) totalWeight);
                totallist.setText("상품 수 : "+(int)totalList);*/

            }
        });

        // 전체무게 합산 텍스트 뷰
        totalWei = (TextView)findViewById(R.id.total_weight);
        // 총 상품수 합산 텍스트 뷰
        totallist = (TextView)findViewById(R.id.total_list);

        // callback
        BarcodeCallback callback = new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                // 중복 바코드 막기
                if(result.getText() == null || result.getText().equals(lastText)) {
                    return;
                }

                // 바코드 변수에 입력
                lastText = result.getText();
                // 스캐너와 리스트 뷰 사이 상태창에 바코드 입력
                barcodeView.setStatusText(result.getText());
                // 삑 소리
                beepManager.playBeepSoundAndVibrate();

                //Added preview of scanned barcode
                // 무게 입력 다이얼로그

                inDel.Insert(lastText);
                /*
                etEdit = new EditText(MainActivity.this);
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("카트에 물건을 담아주세요.");
                dialog.setView(etEdit);
                // OK 버튼 이벤트
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        lastWeight = etEdit.getText().toString();

                        inDel.Insert(lastText);
                        //test
                        try{

                        }
                        catch(Exception e) {}

                        //Log.e("test",listAdapter.getCount()+" ");
                        if(Float.parseFloat(lastWeight) > 0){
                            //전체 무게,상품수 추가
                            totalWeight+=Float.parseFloat(lastWeight);
                            totalList++;
                            //전체 무게, 상품수 텍스트 뷰에 출력
                            totalWei.setText("총 무게 : "+(int) totalWeight);
                            totallist.setText("상품 수 : "+(int)totalList);

                        }else{
                            // 바코드만 찍고 무게가 늘어나지 않을때
                            Toast.makeText(MainActivity.this, "바코드 재입력후 물건을 넣어주세요.",Toast.LENGTH_SHORT).show();
                            lastText = null;
                        }
                    }
                });
                dialog.show();*/


                /*ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
                imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));*/
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        };

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
        beepManager = new BeepManager(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });

    }

    @Override
    protected void onPause() {

        beaconManager.stopRanging(region);

        super.onPause();

        barcodeView.pause();
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()
            //tv_outPut.setText(s);


            if(inOrDel==1)
            {
                if(s=="")
                    Toast.makeText(MainActivity.this, "존재 하지 않는 상품입니다.", Toast.LENGTH_SHORT).show();

                try{
                    JSONObject jsonObject= new JSONObject(s);
                    final String barcode=(String)jsonObject.get("barcode");
                    String name=(String)jsonObject.get("name");
                    final String price = (String)jsonObject.get("price");
                    String weight = (String)jsonObject.get("weight");
                    listAdapter.addItem(barcode, name, price, weight);
                    //listAdapter.notifyDataSetChanged();
                    Log.e("inserttest", s+"");

                    // 바코드 입력 이벤트
                    etEdit = new EditText(MainActivity.this);
                    dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("카트에 물건을 담아주세요.");
                    dialog.setView(etEdit);
                    //test 05 24
                    dialog.setCancelable(false);
                    // OK 버튼 이벤트
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            lastWeight = etEdit.getText().toString();

                            //inDel.Insert(lastText);
                            //test
                            try{

                            }
                            catch(Exception e) {}

                            Log.e("test",listAdapter.getCount()+" ");
                            if(Float.parseFloat(lastWeight) > 0){
                                //전체 무게,상품수 추가
                                totalWeight+=Float.parseFloat(lastWeight);
                                totalPrice+=Integer.parseInt(price)*Integer.parseInt(listAdapter.getInfo(listAdapter.getCount()-1).getCount());
                                totalList++;
                                //전체 무게, 상품수 텍스트 뷰에 출력
                                //totalWei.setText("총 무게 : "+(int) totalWeight);
                                totalWei.setText("총   계 : "+(int) totalPrice);
                                totallist.setText("상품 종류 : "+(int)totalList);


                            }else{
                                // 바코드만 찍고 무게가 늘어나지 않을때
                                Toast.makeText(MainActivity.this, "바코드 재입력후 물건을 넣어주세요.",Toast.LENGTH_SHORT).show();
                                lastText = null;

                                listAdapter.removeItem(listAdapter.getCount()-1);
                                inDel.Delete(barcode);
                            }
                        }
                    });
                    dialog.show();

                    // 수량 입력 이벤트 05 24
                    etEdit = new EditText(MainActivity.this);
                    dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("수량을 입력해 주세요.");
                    dialog.setView(etEdit);
                    //test 05 24
                    dialog.setCancelable(false);
                    // OK 버튼 이벤트
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            count=etEdit.getText().toString();
                            inDel.Update(barcode,etEdit.getText().toString());

                            listAdapter.getInfo(listAdapter.getCount()-1).setCount(count);

                        }
                    });
                    dialog.show();

                    listAdapter.notifyDataSetChanged();
                }
                catch(Exception e){

                }
            }
            if(inOrDel==0)
            {

            }


        }
    }

    public class InsertAndDelete {

        public String InsertUrl;
        public String DeleteUrl;
        public String ClearUrl;
        public String UpdateUrl;
        public JSONArray result1=null;

        private NetworkTask networkTask;
        final ContentValues contentValues=new ContentValues();

        InsertAndDelete(String InsertUrl,String DeleteUrl,String ClearUrl,String UpdateUrl){

            this.InsertUrl=InsertUrl;
            this.DeleteUrl=DeleteUrl;
            this.ClearUrl=ClearUrl;
            this.UpdateUrl=UpdateUrl;
        }
        public void Insert(String barcode){

            if(barcode!=null) {
                contentValues.put("barcode", barcode);

                inOrDel=1;

                networkTask = new NetworkTask(InsertUrl, contentValues);
                networkTask.execute();

            }

        }

        public void Delete(String barcode){
            if(barcode!=null) {
                contentValues.put("barcode", barcode);

                inOrDel=0;
                networkTask = new NetworkTask(DeleteUrl, contentValues);
                networkTask.execute();
            }
        }

        public void Clear(){
            inOrDel=2;

            networkTask=new NetworkTask(ClearUrl,contentValues);
            networkTask.execute();
        }

        public void Update(String barcode,String count){

            if(barcode!=null) {
                contentValues.put("barcode", barcode);
                contentValues.put("count", count);

                inOrDel=0;

                networkTask = new NetworkTask(UpdateUrl, contentValues);
                networkTask.execute();

            }

        }

        public JSONArray getResult(){
            return result1;
        }
    }

    /*
    public void onItemClick(AdapterView<?> parent, View v, final int position, long id)
    {
        Item = new ArrayList<ListViewItem>();

        // 리스트에서 데이터를 받아온다.
        //String data = (String) parent.getItemAtPosition(position);
        ListViewItem data = Item.get(position);

        // 삭제 다이얼로그에 보여줄 메시지를 만든다.
        String message = "해당 데이터를 삭제하시겠습니까?<br />" +
                "position : " + position + "<br />" +
                "data : " + data + "<br />";

        DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                int pos;

                pos = listView.getCheckedItemPosition();

                if(pos != ListView.INVALID_POSITION){
                    // 선택된 아이템을 리스트에서 삭제한다.
                    Item.remove(position);

                    listView.clearChoices();

                    // Adapter에 데이터가 바뀐걸 알리고 리스트뷰에 다시 그린다.
                    listAdapter.notifyDataSetChanged();
                }
            }
        };

        // 삭제를 물어보는 다이얼로그를 생성한다.
        new AlertDialog.Builder(this)
                .setTitle("아이템 삭제")
                .setMessage("삭제할래?")
                .setPositiveButton("삭제", deleteListener)
                .show();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK)
        {

            inDel.Clear();

            //ArrayList<ListViewItem> arrayList=listAdapter.returnArray();
            //mainIntent.putExtra("list",arrayList);
            mainIntent.putExtra("adapter",listAdapter);
            setResult(RESULT_OK,mainIntent);

            finish();
        }

    }
}
