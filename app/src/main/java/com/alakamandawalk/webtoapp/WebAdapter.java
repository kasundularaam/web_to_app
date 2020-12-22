package com.alakamandawalk.webtoapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WebAdapter extends RecyclerView.Adapter<WebAdapter.WebViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Websites> websiteList;
    private DbHelper mDatabase;

    public WebAdapter(Context context, ArrayList<Websites> websiteList) {
        this.context = context;
        this.websiteList = websiteList;
        this.mDatabase = new DbHelper(context);

    }


    @NonNull
    @Override
    public WebViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.web_item, parent, false);
        return new WebViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WebViewHolder holder, int position) {

        final Websites websites = websiteList.get(position);
        holder.siteNameTv.setText(websites.getName());
        holder.deleteBtn.setOnClickListener(v -> deleteItem(position, websites));


        WebView myWebView = new WebView(context);
        myWebView.loadUrl(websites.getUrl());

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                holder.favIconIv.setImageBitmap(icon);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", websites.getUrl());
                context.startActivity(intent);
            }
        });

    }

    private void deleteItem(int position, Websites websites) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete website...");
        builder.setMessage("you are going to delete "+websites.getName()+" from your favorites..\nAre you sure..?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            ProgressDialog pd = new ProgressDialog(context);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("deleting "+ websites.getName()+" from favorites...");
            pd.show();
            mDatabase.deleteWebsite(websites.getId());
            websiteList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, websites.getName()+" Removed from favorites!", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        });
        builder.setNegativeButton("no", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public int getItemCount() {
        return websiteList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class WebViewHolder extends RecyclerView.ViewHolder{

        ImageView favIconIv;
        TextView siteNameTv;
        ImageButton deleteBtn;


        public WebViewHolder(@NonNull View itemView) {
            super(itemView);

            favIconIv = itemView.findViewById(R.id.favIconIv);
            siteNameTv = itemView.findViewById(R.id.siteNameTv);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
