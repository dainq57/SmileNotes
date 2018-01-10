package com.example.dainq.smilenotes.ui.other;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import nq.dai.smilenotes.R;

public class GoldKeyInfoActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressBar mBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_key_info);

//        initWebview();
        initToolBar();
    }

//    private void initWebview() {
//        mWebView = (WebView) findViewById(R.id.webview_gold_key);
//        mWebView.setWebViewClient(new MyClientWebView());
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.loadUrl("https://drive.google.com/file/d/1uoBPEZWjfgEVgF07mY5PCaENAFSwpHfM/view");
//
//        mBar = (ProgressBar) findViewById(R.id.progress_gold_key);
//    }

    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.tw_ic_ab_back_mtrl);
        mToolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        getSupportActionBar().setTitle("CHÌA KHOÁ VÀNG");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
//            mWebView.goBack();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

    private class MyClientWebView extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            view.loadUrl(request);
            return super.shouldOverrideUrlLoading(view, request);
        }
    }
}
