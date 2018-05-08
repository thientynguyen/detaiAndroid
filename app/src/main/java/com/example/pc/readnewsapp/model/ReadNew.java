package com.example.pc.readnewsapp.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PC on 3/24/2018.
 */

public class ReadNew implements Serializable{
    private String title;
    private String link;
    private String picture;
    private String pubDate;

    public ReadNew(String title, String link, String picture, String pubDate) {
        this.title = title;
        this.link = link;
        this.picture = picture;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }


}
