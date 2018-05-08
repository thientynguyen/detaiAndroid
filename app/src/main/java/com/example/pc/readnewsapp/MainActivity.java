package com.example.pc.readnewsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pc.readnewsapp.adapter.ReadNewAdapter;
import com.example.pc.readnewsapp.model.ReadNew;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ImageButton imgButton;
    private ListView lvTitle;
    private ArrayList<ReadNew> arrayList;
    private ReadNewAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTitle = (ListView) findViewById(R.id.listviewTitle);
        arrayList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar1);
        progressBar.setMax(100);
        adapter = new ReadNewAdapter(this, R.layout.activity_display, arrayList);
        lvTitle.setAdapter(adapter);

        Intent intent = getIntent();
        String linkTM = intent.getStringExtra("linkTM");
        if(linkTM != null) {
            new ReadRSS().execute(linkTM);
        } else  {
            new ReadRSS().execute("https://vnexpress.net/rss/thoi-su.rss");
        }
        

        lvTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                intent.putExtra("linkNew", arrayList.get(i).getLink());//để link vào
                startActivity(intent);
            }
        });
    }




    //tạo class để đọc dữ liệu
    private class ReadRSS extends AsyncTask<String, Void, String> {

        //đọc
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                do {
                    content = getDataFromServer(strings[0]);
                } while ("".equals(content.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        //trả dữ liệu ra
        @Override
        protected void onPostExecute(String s) {
            try {
                if (!"".equals(s)) {
                    super.onPostExecute(s);

                    XMLDOMParser parser = new XMLDOMParser();
                    //chứa toàn bộ nội dung rss để mình đọc
                    Document document = parser.getDocument(s);

                    //khai báo nodelist để chứa toàn bộ item
                    //getElementsByTagName:trong danh sách đó nó lấy ra cái tên gì
                    NodeList nodeList = document.getElementsByTagName("item");
                    NodeList nodeListDescription = document.getElementsByTagName("description");

                    //đọc tiêu đề
                    String title = "";
                    String link = "";


                    //duyệt hết tất cả
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        //đọc phía trong của từng item
                        //lấy từng phần tử
                        String picture = "";


                        String cdata = nodeListDescription.item(i + 1).getTextContent();


                        org.jsoup.nodes.Document documentx = Jsoup.parse(cdata);
                        Elements imgs = documentx.select("img");
                        boolean hasOriginal = false;
                        for (org.jsoup.nodes.Element imgx : imgs) {
                            if (imgx.hasAttr("data-original")) {
                                hasOriginal = true;
                                picture = imgx.attr("data-original");
                            }
                        }
                        if (!hasOriginal) {
                            for (org.jsoup.nodes.Element imgx : imgs) {
                                if (imgx.hasAttr("src")) {
                                    hasOriginal = true;
                                    picture = imgx.attr("src");
                                }
                            }
                        }
                        Element element = (Element) nodeList.item(i);
                        title = parser.getValue(element, "title");

                        link = parser.getValue(element, "link");

                        String date = parser.getValue(element, "pubDate");

                        ReadNew read = new ReadNew(title, link, picture, date);

                        arrayList.add(read);
                    }

                    // ẩn progessbar
                    progressBar.setVisibility(View.GONE);

                    adapter.notifyDataSetChanged();//cập nhật lại dữ liệu đã thay đổi
                } else {
                    //Toast.makeText(getApplicationContext(), "not connected!", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(MainActivity.this,title,Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public StringBuilder getDataFromServer(String urlName) {
            StringBuilder content = new StringBuilder();

            try {
                URL url = new URL(urlName);//đường dẫn

                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());//truyền vào dữ liệu

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                //đọc dữ liệu
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
                bufferedReader.close();
                Toast.makeText(getApplicationContext(), "not connected!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                //  Toast.makeText(getApplicationContext(), "not connected!", Toast.LENGTH_SHORT).show();
            }
            return content;
        }
    }

    public void theme(View view) {

        Intent intent = new Intent(MainActivity.this, ThemeActivity.class);
        startActivity(intent);

    }

    public void menu(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    public void newtin(View view) {
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        intent.putExtra("linkTM","https://vnexpress.net/rss/tin-moi-nhat.rss");
        startActivity(intent);
    }

}
