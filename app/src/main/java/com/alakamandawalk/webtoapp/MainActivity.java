package com.alakamandawalk.webtoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton addWebBtn;
    EditText urlEt;
    RecyclerView webRv;
    LinearLayout emptyScreenLl;
    TextView appNameTv;
    WebView myWebView;
    ProgressDialog pd;
    LinearLayoutManager linearLayoutManager;
    public WebAdapter mAdapter;

    DbHelper dbHelper;
    public ArrayList<Websites> websiteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addWebBtn = findViewById(R.id.addWebBtn);
        urlEt = findViewById(R.id.urlEt);
        appNameTv = findViewById(R.id.appNameTv);
        webRv = findViewById(R.id.webRv);
        emptyScreenLl = findViewById(R.id.emptyScreenLl);
        myWebView = new WebView(this);
        pd = new ProgressDialog(this);

        webRv = findViewById(R.id.webRv);
        linearLayoutManager = new LinearLayoutManager(this);
        webRv.setLayoutManager(linearLayoutManager);
        webRv.setHasFixedSize(true);

        loadWebsites(this);

        addWebBtn.setOnClickListener(v -> addNewWebsite());

        appNameTv.setOnClickListener(v -> showAboutAppDialog());

    }

    private void showAboutAppDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.about_app_dialog, null);
        builder.setView(view);
        builder.setNegativeButton("close", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void loadWebsites(Context context) {
        pd.setMessage("loading sites...");
        pd.show();
        pd.setCanceledOnTouchOutside(false);

        dbHelper = new DbHelper(this);
        websiteList = dbHelper.getAllWebsites();

        if (websiteList.size() > 0) {
            emptyScreenLl.setVisibility(View.GONE);
            webRv.setVisibility(View.VISIBLE);
            mAdapter = new WebAdapter(context, websiteList);
            webRv.setAdapter(mAdapter);
            pd.dismiss();
        }
        else {
            webRv.setVisibility(View.GONE);
            emptyScreenLl.setVisibility(View.VISIBLE);
            Toast.makeText(this, "There is no websites in the database. Start adding now", Toast.LENGTH_LONG).show();
            pd.dismiss();
        }
    }

    private void addNewWebsite() {

        ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("loading site details from server...");
        pd.show();

        final String url = urlEt.getText().toString();

        if (TextUtils.isEmpty(url)) {
            pd.dismiss();
            Toast.makeText(MainActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
        }
        else{
            try {
                myWebView.loadUrl(url);
                myWebView.setWebChromeClient(new WebChromeClient(){
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        Websites newWebsite = new Websites(title, url);
                        dbHelper.addWebsite(newWebsite);
                        loadWebsites(MainActivity.this);
                        Toast.makeText(MainActivity.this, "New website added!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        urlEt.setText("");
                    }
                });
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}