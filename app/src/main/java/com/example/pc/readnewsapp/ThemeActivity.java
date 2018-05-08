package com.example.pc.readnewsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
    }

    public void clickNews(View view)  {
        Intent intent = new Intent(ThemeActivity.this,TSActivity.class);
        intent.putExtra("linkTS","https://vnexpress.net/rss/phap-luat.rss");
        startActivity(intent);

    }

    public void theme(View view) {
        Intent intent = new Intent(ThemeActivity.this,ThemeActivity.class);
        startActivity(intent);

    }

    public void clickWorld(View view) {
        Intent intent = new Intent(ThemeActivity.this,TSActivity.class);
        intent.putExtra("linkTG","https://vnexpress.net/rss/the-gioi.rss");
        startActivity(intent);
    }

    public void clickBusiness(View view) {
        Intent intent = new Intent(ThemeActivity.this,TSActivity.class);
        intent.putExtra("linkKD","https://vnexpress.net/rss/kinh-doanh.rss");
        startActivity(intent);
    }

    public void clickEntertainment(View view) {
        Intent intent = new Intent(ThemeActivity.this,TSActivity.class);
        intent.putExtra("linkGT","https://vnexpress.net/rss/giai-tri.rss");
        startActivity(intent);
    }

    public void clickHealth(View view) {
        Intent intent = new Intent(ThemeActivity.this,TSActivity.class);
        intent.putExtra("linkSK","https://vnexpress.net/rss/suc-khoe.rss");
        startActivity(intent);
    }

    public void clickFamily(View view) {
        Intent intent = new Intent(ThemeActivity.this,TSActivity.class);
        intent.putExtra("linkGD","https://vnexpress.net/rss/gia-dinh.rss");
        startActivity(intent);
    }

    public void menu(View view) {
        Intent intent = new Intent(ThemeActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void newhot(View view) {
        Intent intent = new Intent(ThemeActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void newtin(View view) {
        Intent intent = new Intent(ThemeActivity.this,MainActivity.class);
        intent.putExtra("linkTM","https://vnexpress.net/rss/tin-moi-nhat.rss");
        startActivity(intent);
    }
}
