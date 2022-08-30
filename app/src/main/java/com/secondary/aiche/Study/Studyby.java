package com.secondary.aiche.Study;

import android.app.Activity;
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
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.domain.firebaseapp.MainActivity;
import com.domain.firebaseapp.R;

import androidx.annotation.Nullable;

public class Studyby extends Activity {


    ImageView studyCircle, studyHeader, studyHeader2;
    TextView year_text, simister_text;
    int year, Semeter, width, height;
    MyWebViewClient myWebViewClient;
    WebView webView,webViewDownload;
    String url, url_download, headerPosition, newUA;
    boolean OnMainScreen, onReadingMode;
    Spinner spinner_semester, spinner_year;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        setContentView(R.layout.study_for_low);
        OnMainScreen = true;
        headerPosition = "down";
        onReadingMode = true;


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        webView = findViewById(R.id.webView);
        webViewDownload = findViewById(R.id.webViewDownload);
        webView.getSettings().setJavaScriptEnabled(true);
        webViewDownload.getSettings().setJavaScriptEnabled(true);
        myWebViewClient = new MyWebViewClient();
        newUA ="Mozilla/5.0 (Android; Mobile; rv:24.0) Gecko/24.0 Firefox/24.0";
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
        studyHeader = findViewById(R.id.study_header);
        studyHeader2 = findViewById(R.id.study_header2);
        simister_text = findViewById(R.id.text_simister);
        year_text = findViewById(R.id.text_year);

        studyCircle = findViewById(R.id.studycircle);

        spinner_year = findViewById(R.id.spinner_year);
        spinner_semester = findViewById(R.id.spinner_semester);

        ArrayAdapter<CharSequence> adapter_year = ArrayAdapter.createFromResource(this, R.array.years, R.layout.spinner_layout);
        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(adapter_year);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        year = 0;

                        break;
                    case 2:
                        year = 1;

                        break;
                    case 3:
                        year = 2;

                        break;
                    case 4:
                        year = 3;

                        break;
                    case 5:
                        year = 4;

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> adapter_simister = ArrayAdapter.createFromResource(this, R.array.simisters, R.layout.spinner_layout);
        adapter_simister.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_semester.setAdapter(adapter_simister);
        spinner_semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        Semeter = 1;

                        break;
                    case 2:
                        Semeter = 2;

                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        studyCircle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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
                    Toast.makeText(Studyby.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }

            }

        });

        studyHeader2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        webView.setWebViewClient(myWebViewClient);
    }

    public void getFromNetwork() {


        webView.loadUrl(url);
        webView.setWebViewClient(myWebViewClient);
        webViewDownload.loadUrl(url_download);
        webViewDownload.setWebViewClient(myWebViewClient);

        Toast.makeText(Studyby.this, "Click the icon to switch between Reading and Downloading modes", Toast.LENGTH_SHORT).show();
    }

    public void circulate() {

        OnMainScreen = false;

        //to delay the invisibility lines
        Runnable r = new Runnable() { //for Delaying gif response
            @Override
            public void run() {

                spinner_semester.setVisibility(View.GONE);
                spinner_year.setVisibility(View.GONE);
                year_text.setVisibility(View.GONE);
                simister_text.setVisibility(View.GONE);
                studyCircle.setVisibility(View.GONE);
                if (onReadingMode){webView.setVisibility(View.VISIBLE);}else{webViewDownload.setVisibility(View.VISIBLE);}
                if (headerPosition.equals("down")) {
                    animateUp();
                }
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 2000);
    }


    public void circleBackPressed() {

        webView.clearView();
        webView.setVisibility(View.GONE);
        webViewDownload.clearView();
        webViewDownload.setVisibility(View.GONE);
        spinner_semester.setVisibility(View.VISIBLE);
        spinner_year.setVisibility(View.VISIBLE);
        year_text.setVisibility(View.VISIBLE);
        simister_text.setVisibility(View.VISIBLE);
        studyCircle.setVisibility(View.VISIBLE);

        if (headerPosition.equals("up")) {
            animateDown();
        }

        OnMainScreen = true;
    }

    @Override
    public void onBackPressed() {

        try {
            if (OnMainScreen) {
                Intent intent = new Intent(Studyby.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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

            Intent intent = new Intent(Studyby.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void animateUp() {
        studyHeader.animate().translationYBy(-200* height / 1770);   // TODO: transform into usedHeight Variable if you want
        studyHeader2.animate().translationYBy(-300* height / 1770).alpha(.4f).scaleX(.5f).scaleY(.5f);

        headerPosition = "up";
    }

    public void animateDown() {
        studyHeader.animate().translationYBy(200* height / 1770);
        studyHeader2.animate().translationYBy(300* height / 1770).alpha(1f).scaleX(1f).scaleY(1f);

        headerPosition = "down";
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