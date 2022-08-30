package com.secondary.aiche.Study;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.domain.firebaseapp.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Mohamed Gamal on 04/09/2018.
 */


public class Study extends AppCompatActivity {

    ImageView studyCircle, yearImageView, studyHeader, studyHeader2;
    TextView search,hard2use;
    int year, Semeter, width, height;
    double ratio, standardRatio, idealRatio, usedWidth, usedHeight;
    MyWebViewClient myWebViewClient;
    WebView webView,webViewDownload;
    String url, url_download, headerPosition, newUA;
    boolean OnMainScreen, onReadingMode;
    Spinner spinner_simister, spinner_year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        Toast.makeText(Study.this, "All Gratitude for owners of the data :)", Toast.LENGTH_LONG).show();

            setContentView(R.layout.study);
            OnMainScreen = true;
            headerPosition = "down";
            onReadingMode = true;

        /*final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                pullToRefresh.setRefreshing(false);
            }
        });*/

            //To get the screen Resolution

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;

            //عشان تناسب اي شاشه ب أي ابعاد

            ratio = height / width;
            standardRatio = 1280 / 720;
            idealRatio = ratio / standardRatio;


            // TODO: add tablet 10 inch later

            final boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            if (tabletSize) {
                // do something
                usedWidth = (double) width / 1728;
                usedHeight = (double) height / 1770;
                //System.out.println("ده تابلت يا معلم");

            } else {
                // do something else
                usedWidth = (double) width / 1080;
                usedHeight = (double) height * idealRatio / 1770;
                //System.out.println("لا مش تابلت لا");
            }


            hard2use = findViewById(R.id.hard2use);
            search = findViewById(R.id.search);

            webView = findViewById(R.id.webView);
            webViewDownload = findViewById(R.id.webViewDownload);
            webView.getSettings().setJavaScriptEnabled(true);
            webViewDownload.getSettings().setJavaScriptEnabled(true);
            myWebViewClient = new MyWebViewClient();
            newUA = "Mozilla/5.0 (Android; Mobile; rv:24.0) Gecko/24.0 Firefox/24.0";
            webView.getSettings().setUserAgentString(newUA);
            webViewDownload.getSettings().setUserAgentString(newUA);
            webView.setDownloadListener(new DownloadListener() {
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            webViewDownload.setDownloadListener(new DownloadListener() {
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            yearImageView = findViewById(R.id.yearImageView);
            studyCircle = findViewById(R.id.studycircle);
            studyHeader = findViewById(R.id.study_header);
            studyHeader2 = findViewById(R.id.study_header2);

            studyCircle.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View view, MotionEvent motionEvent) {

                    float x = motionEvent.getX();
                    float y = motionEvent.getY();


                    //String message = String.format("Coordinates: (%.2f, %.2f)",x,y);
                    //Toast.makeText(Study.this,message,Toast.LENGTH_SHORT).show();

                    //To make the image clickable by Coordinates of touch
                    if (x > 179 * usedWidth && x < 405 * usedWidth && y > 37 * usedHeight && y < 145 * usedHeight) {//prep
                        year = 0;
                        yearImageView.setImageResource(R.drawable.bprep);
                    } else if ((x >= 405 * usedWidth && x < 610 * usedWidth && y >= 30 * usedHeight && y < 135 * usedHeight && !tabletSize)|| (x >= 405 * usedWidth && x < 570 * usedWidth && y >= 30 * usedHeight && y < 135 * usedHeight && tabletSize)) {//1
                        year = 1;
                        yearImageView.setImageResource(R.drawable.b1);
                    } else if ((x >= 610 * usedWidth && y >= 49 * usedHeight && y < 193 * usedHeight && !tabletSize) || (x >= 570 * usedWidth && y >= 49 * usedHeight && y < 120 * usedHeight && tabletSize)) {//2
                        year = 2;
                        yearImageView.setImageResource(R.drawable.b2);
                    } else if ((x >= 675 * usedWidth && y >= 143 * usedHeight && y < 315 * usedHeight && !tabletSize) || (x >= 675 * usedWidth && y >= 120 * usedHeight && y < 180 * usedHeight && tabletSize)) {
                        //3
                        year = 3;
                        yearImageView.setImageResource(R.drawable.b3);
                    } else if ((x >= 730 * usedWidth && y >= 290 * usedHeight && y < 447 * usedHeight && !tabletSize) || (x >= 730 * usedWidth && y >= 184 * usedHeight && y < 280 * usedHeight && tabletSize)) {
                        //4
                        year = 4;
                        yearImageView.setImageResource(R.drawable.b4);
                    } else if ((x >= 346 * usedWidth && x < 686 * usedWidth && y >= 665 * usedHeight && !tabletSize) || (x >= 346 * usedWidth && x < 686 * usedWidth && y >= 480 * usedHeight && tabletSize)) {
                        //1st
                        Semeter = 1;
                        studyCircle.setImageResource(R.drawable.b1st);
                    } else if ((x >= 30 * usedWidth && x < 300 * usedWidth && y >= 460 * usedHeight && !tabletSize) || (x >= 30 * usedWidth && x < 240 * usedWidth && y >= 320 * usedHeight && tabletSize)) {
                        //2nd
                        Semeter = 2;
                        studyCircle.setImageResource(R.drawable.b2nd);
                    } else if ((x >= 240 * usedWidth && x < 730 * usedWidth && y >= 150 * usedHeight && y < 650 * usedHeight && !tabletSize) || (x >= 240 * usedWidth && x < 730 * usedWidth && y >= 150 * usedHeight && y < 460 * usedHeight && tabletSize)) {
                        //Search


                        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = cm.getActiveNetworkInfo();
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                            //check for the input
                            switch (year) {

                                case 0:
                                    switch (Semeter) {
                                        case 1:
                                            circulate();
                                            url = "https://drive.google.com/open?id=1zEN_zswcmgCjksWx_ol0l4aD0kCgQSFj";
                                            url_download = "https://www.mediafire.com/folder/06bbile2219k9/1st";
                                            getFromNetwork();
                                            break;
                                        case 2:
                                            circulate();
                                            url = "https://drive.google.com/open?id=16_jqR-fUzNWeQuj5sjqfhJVczbciFfGy";
                                            url_download = "https://www.mediafire.com/folder/9a3tefvzrlwjo/2nd";
                                            getFromNetwork();
                                            break;
                                    }
                                    break;

                                case 1:
                                    switch (Semeter) {
                                        case 1:
                                            circulate();
                                            url = "https://drive.google.com/open?id=1ZOf1M1nvYF5wI8jx8mgALJ6IENbRR2EW";
                                            url_download = "https://www.mediafire.com/folder/6g4icskgpyjmg/1st";
                                            getFromNetwork();
                                            break;
                                        case 2:
                                            circulate();
                                            url = "https://drive.google.com/open?id=1BAe1-jrpR3l40O5Nf7dhrxNlc6rPOhEr";
                                            url_download = "https://www.mediafire.com/folder/tyig090qui48t/2nd";
                                            getFromNetwork();
                                            break;
                                    }
                                    break;

                                case 2:
                                    switch (Semeter) {
                                        case 1:
                                            circulate();
                                            url = "https://drive.google.com/open?id=1ltfC43-QYEwqTnrTqDc9RXzaWCz-xv-v";
                                            url_download = "https://www.mediafire.com/folder/o447tflyq9isq/1st";
                                            getFromNetwork();
                                            break;
                                        case 2:
                                            circulate();
                                            url = "https://drive.google.com/open?id=1wm9JvfFBZSL7E1xl4LryniP5gOakWOzL";
                                            url_download = "https://www.mediafire.com/folder/zladjucn6exfj/2nd";
                                            getFromNetwork();
                                            break;
                                    }
                                    break;

                                case 3:
                                    switch (Semeter) {
                                        case 1:
                                            circulate();
                                            url = "https://drive.google.com/open?id=1jZHPA2wyZkrZcKOBEJi6lyyB4RESnVzQ";
                                            url_download = "https://www.mediafire.com/folder/gvx2e6t0g6lsr/1st";
                                            getFromNetwork();
                                            break;
                                        case 2:
                                            circulate();
                                            url = "https://drive.google.com/open?id=1gs7kzNKIxKKRSnq26Ik182YgNMgFDo6W";
                                            url_download = "https://www.mediafire.com/folder/1eg0lfl1w6wbm/2nd";
                                            getFromNetwork();
                                            break;
                                    }
                                    break;

                                case 4:
                                    switch (Semeter) {
                                        case 1: //needed
                                            circulate();
                                            url = "https://drive.google.com/open?id=1IfPHJAYKMzqavuCkE5DF3bIUqt3XpjDq";
                                            url_download = "https://www.mediafire.com/folder/4cl0ic0nyl1yj/1st";
                                            getFromNetwork();
                                            break;
                                        case 2:
                                            circulate();
                                            url = "https://drive.google.com/open?id=1Rweo_-JTEZOYYIXn37-W9eZ5hmzjCzkB";
                                            url_download = "https://www.mediafire.com/folder/5j5y97fzzai2k/2nd";
                                            getFromNetwork();
                                            break;
                                    }
                                    break;
                            }


                        } else {
                            Toast.makeText(Study.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    return false;
                }

            });

            studyHeader2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //circleBackPressed();
                    if (onReadingMode){
                        studyHeader2.setImageResource(R.drawable.study_header_download);
                        if(!OnMainScreen){
                            webView.setVisibility(View.GONE);
                            webViewDownload.setVisibility(View.VISIBLE);
                            webViewDownload.setWebViewClient(myWebViewClient);}
                        onReadingMode = false;
                    }else{
                        studyHeader2.setImageResource(R.drawable.study_header2);
                        if(!OnMainScreen){
                            webView.setVisibility(View.VISIBLE);
                            webView.setWebViewClient(myWebViewClient);
                            webViewDownload.setVisibility(View.GONE);
                        }
                        onReadingMode = true;
                    }

                }
            });

    }

    public void refresh() {
        webView.loadUrl(url);
        //webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(myWebViewClient);
    }

    public void getFromNetwork() {
        webView.loadUrl(url);
        webView.setWebViewClient(myWebViewClient);
        webViewDownload.loadUrl(url_download);
        webViewDownload.setWebViewClient(myWebViewClient);

        Toast.makeText(Study.this, "Click the icon to switch between Reading and Downloading modes", Toast.LENGTH_SHORT).show();
    }

    public void circulate() {

            studyCircle.animate().rotation(-360f).scaleY(.3f).scaleX(.3f).alpha(0).setDuration(2000);
            yearImageView.animate().rotation(-360f).scaleY(.3f).scaleX(.3f).alpha(0).setDuration(2000);
            search.animate().alpha(0).setDuration(2000);
            hard2use.animate().alpha(0).setDuration(2000);
            hard2use.setVisibility(View.GONE);

        OnMainScreen = false;

        //to delay the invisibility lines
        Runnable r = new Runnable() { //for Delaying gif response
            @Override
            public void run() {
                    studyCircle.setVisibility(View.GONE);
                    yearImageView.setVisibility(View.GONE);

                if (onReadingMode){webView.setVisibility(View.VISIBLE);}else{webViewDownload.setVisibility(View.VISIBLE);}
                if (headerPosition == "down") {
                    animateUp();
                }
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 2000);
    }

    public void circleBackPressed() {


            if (studyCircle.getVisibility() == View.GONE) {
                studyCircle.setVisibility(View.VISIBLE);
                yearImageView.setVisibility(View.VISIBLE);
                studyCircle.animate().alpha(1).rotation(360f).scaleY(1f).scaleX(1f).setDuration(2000);
                yearImageView.animate().alpha(1).rotation(360f).scaleY(1f).scaleX(1f).setDuration(2000);
                search.animate().alpha(1).setDuration(2000);
                hard2use.setVisibility(View.VISIBLE);
                hard2use.animate().alpha(1).setDuration(2000);

                webView.setVisibility(View.GONE);
                webViewDownload.setVisibility(View.GONE);

                if (headerPosition == "up") {
                    animateDown();
                }
                OnMainScreen = true;
            }
    }


    @Override
    public void onBackPressed() {

        try {
            if (OnMainScreen) {
                super.onBackPressed();
            } else {
                if (webView.getVisibility() == View.VISIBLE){
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        circleBackPressed();
                    }
                }else if (webViewDownload.getVisibility() == View.VISIBLE){
                    if (webViewDownload.canGoBack()) {
                        webViewDownload.goBack();
                    } else {
                        circleBackPressed();
                    }
                }else{ circleBackPressed();}
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }

    public void animateUp() {
        studyHeader.animate().translationYBy(-200 * height / 1770);   // TODO: transform into usedHeight Variable if you want
        studyHeader2.animate().translationYBy(-300 * height / 1770).alpha(.4f).scaleX(.5f).scaleY(.5f);
        headerPosition = "up";
    }

    public void animateDown() {
        studyHeader.animate().translationYBy(200 * height / 1770);
        studyHeader2.animate().translationYBy(300 * height / 1770).alpha(1f).scaleX(1f).scaleY(1f);
        headerPosition = "down";
    }

    public void go2study4low(View view){
        Intent intent = new Intent(Study.this,Studyby.class);
        startActivity(intent);
    }


    // To manage and block ads

    public class MyWebViewClient extends WebViewClient {
        public boolean shuldOverrideKeyEvent (WebView view, KeyEvent event) {

            return true;
        }

        public boolean shouldOverrideUrlLoading (WebView view, String url) {
            //if (Uri.parse(url).getHost().equals("drive.google.com")) {
            if (url.contains("drive.google.com") || url.contains("mediafire.com")) {
                return false;
            }
            // reject anything other
            return true;
        }
    }
}