package com.example.clarinet.scannertest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class KakaoActivity extends Activity {

    private WebView mainWebView;
    private final String APP_SCHEME = "iamportkakao://";

    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao);

        mainWebView = (WebView) findViewById(R.id.mainWebView);
        mainWebView.setWebViewClient(new KakaoWebViewClient(this));
        WebSettings settings = mainWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        mainWebView.loadUrl("http://122.45.214.122:13241//jdbc_test/WebContent/test/test_pay4.jsp?amount="+getIntent().getIntExtra("payPrice",1)); // 임시 서버 (카카오 페이)

        mainWebView.addJavascriptInterface(new AndroidBridge(), "android");
    }

    public class AndroidBridge{

        @JavascriptInterface
        public void fisishActivity() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }

        @JavascriptInterface
        public void doPay(){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent();
                    setResult(RESULT_OK,intent);

                    finish();
                }
            });
        }
    }
/*
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
*/
    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if ( intent != null ) {
            Uri intentData = intent.getData();

            if ( intentData != null ) {
                //카카오페이 인증 후 복귀했을 때 결제 후속조치
                String url = intentData.toString();

                if ( url.startsWith(APP_SCHEME) ) {
                    String path = url.substring(APP_SCHEME.length());
                    if ( "process".equalsIgnoreCase(path) ) {
                        mainWebView.loadUrl("javascript:IMP.communicate({result:'process'})");
                    } else {
                        mainWebView.loadUrl("javascript:IMP.communicate({result:'cancel'})");
                    }

                }
            }
        }

    }
}
