package com.goufang.webviewapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webview;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setCancelable(true);
        progressBar.setCanceledOnTouchOutside(false);


        final AppCompatActivity activity = this;
        webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Toast.makeText(MainActivity.this, "onProgressChanged： " + progress, Toast.LENGTH_SHORT).show();
                activity.setProgress(progress * 1000);
            }
        });
/*        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(MainActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });*/


        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setMessage("loading...");
                progressBar.show();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.show();
            }
        });

        ;
    }

    private long backtime = 0;
    @Override
    public void onBackPressed() {
        if(webview.canGoBack()){
            webview.goBack();
            return;
        }
        long now = System.currentTimeMillis();
        long past = now - backtime;
        if (past < 3000) {
            finish();
        } else {
            Toast.makeText(this, "再按退出", Toast.LENGTH_SHORT).show();
        }
        backtime = now;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_1) {
            webview.loadUrl("http://developer.android.com/");
            return true;
        }
        if (id == R.id.action_2) {
            webview.loadUrl("http://www.sina.com.cn");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
