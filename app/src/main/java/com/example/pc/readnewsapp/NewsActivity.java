package com.example.pc.readnewsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.Socket;

public class NewsActivity extends AppCompatActivity {

    ProgressBar pr;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        pr=findViewById(R.id.progressBar);

        webView = (WebView) findViewById(R.id.webviewNew);


        //new AsycHttpYTask().execute("www.google.com");

        // new ReadRSS().execute("linkNew");

        Intent intent = getIntent();
        String link = intent.getStringExtra("linkNew");
        //Toast.makeText(this, link,Toast.LENGTH_SHORT).show();
        CountDownTimer countdowntime = new CountDownTimer(  1000, 1000) {
            @Override
            public void onTick(long l) {
                int current = pr.getProgress();
                if (current >=pr.getMax()) {
                    current = 0;
                }
                ///  progressBar.setProgress(current + 100);

            }

            @Override
            public void onFinish() {
                pr.setVisibility(View.GONE);
                new AsycHttpYTask().execute("linkNew");
            }


        };
        countdowntime.start();
        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());//giúp cho nó vẫn nằm trong app của mình mà ko nhảy ra trình duyệt mặc định

    }

    public class AsycHttpYTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0 ;
            int  x= 1;
            do{

                result = checkConnectServer(params[0]);
            }
            while(result == 0);
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                Intent intent = getIntent();
                String link = intent.getStringExtra("linkNew");
                //Toast.makeText(this, link,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();

                webView.loadUrl(link);

                //  startActivity(new Intent(NewsActivity.this));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "not connected!", Toast.LENGTH_SHORT).show();


            }
        }

        public Integer checkConnectServer(String param ){
            try {
                Socket s = new Socket(param, 80);
                s.close();
                return 1;
            } catch (Exception ex) {
                return 0;
            }
        }
    }
}
