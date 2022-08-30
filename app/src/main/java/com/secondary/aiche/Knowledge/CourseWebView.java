package com.secondary.aiche.Knowledge;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.secondary.aiche.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


// Show your course in webView


public class CourseWebView extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_webview);

        if (savedInstanceState == null) { loadWebsite(); }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }


    private void loadWebsite() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            webView = findViewById(R.id.web_view);
            startWebView(SampleViewHolders.url);
        } else {
            Toast.makeText(CourseWebView.this, "Connection Error or TEData Subscriber", Toast.LENGTH_LONG).show();
        }
    }

    private void startWebView(String url) {

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are open in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            /*public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(CourseWebView.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }*/

        });

        // To make fullScreen possible
        webView.setWebViewClient(new Browser());
        webView.setWebChromeClient(new MyWebClient());


        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }


    // TODO : edit study wheel backpressed function
    // TODO : add webView.loadUrl("about:blank"); to study too


    @Override
    public void onBackPressed() {
        try{
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                super.onBackPressed();
                webView.loadUrl("about:blank");
            }
        }catch(Exception e){
            super.onBackPressed();
        }
    }


    class Browser
            extends WebViewClient {
        Browser() {
        }

        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
            paramWebView.loadUrl(paramString);
            return true;
        }
    }

    public class MyWebClient
            extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        public MyWebClient() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (CourseWebView.this == null) {
                return null;
            }
            return BitmapFactory.decodeResource(CourseWebView.this.getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) CourseWebView.this.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            CourseWebView.this.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            CourseWebView.this.setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = CourseWebView.this.getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = CourseWebView.this.getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) CourseWebView.this.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            CourseWebView.this.getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }
}
