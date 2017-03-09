package com.hamnya.webapptest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
    private String id = "";
    private WebView mWebView;
    private ProgressBar web_progress;
    final Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        id = "***********";
        web_progress = (ProgressBar)findViewById(R.id.pBar);
        web_progress.setVisibility(View.GONE);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setBackgroundColor(0); //배경색
        mWebView.setHorizontalScrollBarEnabled(false); //가로 스크롤
        mWebView.setVerticalScrollBarEnabled(false); //세로 스크롤0
        //mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); //스크롤 노출타입

        //HTML을 파싱하여 웹뷰에서 보여주거나 하는 작업에서
        //width , height 가 화면 크기와 맞지 않는 현상이 발생한다
        //이를 잡아주기 위한 코드
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //캐시파일 사용 금지(운영중엔 주석처리 할 것)
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        //zoom 허용
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);

        //javascript의 window.open 허용
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //javascript 허용
        mWebView.getSettings().setJavaScriptEnabled(true);

        //meta태그의 viewport사용 가능
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.loadUrl("http://***************.jsp?id="+id);
        mWebView.setWebViewClient(new WishWebViewClient());

        mWebView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress) {
                web_progress.setVisibility(View.VISIBLE);
                activity.setProgress(newProgress*100);
            }
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Alert title")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }
        });
        mWebView.getSettings().setSupportMultipleWindows(true);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WishWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }

        public void onReceivedError(WebView view, int errorCode, String description, String fallingUrl) {
            view.loadData("<html><body>오류 발생</body></html>", "text/html", "UTF-8");
            Toast.makeText(MainActivity.this, "로딩오류" + description, Toast.LENGTH_SHORT).show();
        }
    }
}
