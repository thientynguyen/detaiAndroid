package com.example.pc.readnewsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TSActivity extends AppCompatActivity {
    private ImageButton imgButton;
    private ListView lvTitle;
    private ArrayList<ReadNew> arrayList;
    private ReadNewAdapter adapter;
    private ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ts);
        lvTitle = (ListView) findViewById(R.id.listviewTitle);
        arrayList = new ArrayList<>();
        pr=findViewById(R.id.progressBar);

        adapter = new ReadNewAdapter(this,R.layout.activity_display,arrayList);
        lvTitle.setAdapter(adapter);
///////////////////////////////////
        Intent intent = getIntent();
        String linkTS = intent.getStringExtra("linkTS");
        //Intent intent1 = getIntent();
        String linkTG = intent.getStringExtra("linkTG");
        String linkKD = intent.getStringExtra("linkKD");
        String linkGT = intent.getStringExtra("linkGT");
        String linkSK = intent.getStringExtra("linkSK");
        String linkGD = intent.getStringExtra("linkGD");
        if(linkTS != null) {
            new ReadRSS().execute(linkTS);
        } else if(linkTG != null) {
            new ReadRSS().execute(linkTG);
        } else if(linkKD != null) {
            new ReadRSS().execute(linkKD);
        } else if(linkGT != null) {
            new ReadRSS().execute(linkGT);
        } else if(linkSK != null) {
            new ReadRSS().execute(linkSK);
        } else if(linkGD != null) {
            new ReadRSS().execute(linkGD);
        }
/////////////////////////////////////////////////
        lvTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(TSActivity.this,NewsActivity.class);
                intent.putExtra("linkNew", arrayList.get(i).getLink());//để link vào
                startActivity(intent);
            }
        });
    }



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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeListDescription = document.getElementsByTagName("description");


            String title = "";
            String link = "";


            for(int i = 0; i < nodeList.getLength(); i++) {

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
                title = parser.getValue(element,"title");

                link = parser.getValue(element,"link");

                String date = parser.getValue(element,"pubDate");

                ReadNew read = new ReadNew(title,link,picture,date);

                arrayList.add(read);
            }
            pr.setVisibility(View.GONE);

            adapter.notifyDataSetChanged();



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
        Intent intent = new Intent(TSActivity.this,ThemeActivity.class);
        startActivity(intent);
    }
    public void menu(View view) {
        Intent intent = new Intent(TSActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
