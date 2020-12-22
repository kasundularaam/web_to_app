package com.alakamandawalk.webtoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    WebView showWebsiteWv;
    String url;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        url = getIntent().getStringExtra("url");

        pd = new ProgressDialog(this);
        showWebsiteWv = findViewById(R.id.showWebsiteWv);
        WebSettings webSettings = showWebsiteWv.getSettings();
        webSettings.setJavaScriptEnabled(true);

        showWebsiteWv.getSettings().setLoadWithOverviewMode(true);
        showWebsiteWv.getSettings().setUseWideViewPort(true);

        showWebsiteWv.loadUrl(url);
        showWebsiteWv.setWebViewClient(new WebViewClient());

        showWebsiteWv.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(WebViewActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.setMessage("Loading...\n"+url);
                pd.setCanceledOnTouchOutside(false);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (showWebsiteWv.canGoBack()){
            showWebsiteWv.goBack();
        }else {
            super.onBackPressed();
        }
    }
}