package com.nminh.nytsearch.models;


import com.nminh.nytsearch.utils.Constant;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 20.06.2017.
 */

public class Article {

    @SerializedName("snippet")
    private String snippet;

    @SerializedName("web_url")
    private String webUrl;

    @SerializedName("multimedia")
    private List<Multimedia> multimedia;

    // sth wrong with this
    // maybe its not String???
    // its an object, now i see, keep it up ok? ok
    @SerializedName("headline")
    private Headline headline;

    public static class Multimedia {

        private String url;
        private String type;
        private int width;
        private int height;

        public String getUrl() {
            return Constant.STATIC_BASE_URL + url;
        }

        public String getType() {
            return type;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public static class Headline {

        private String main;

        public String getMain() {
            return main;
        }
    }


    public Article(String snippet, String webUrl, List<Multimedia> multimedia, Headline headline) {
        this.snippet = snippet;
        this.webUrl = webUrl;
        this.multimedia = multimedia;
        this.headline = headline;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public List<Multimedia> getMultimedia() {
        return multimedia;
    }

    public Headline getHeadline() {
        return headline;
    }
}