package com.nminh.nytsearch.article_api;

import com.google.gson.annotations.SerializedName;
import com.nminh.nytsearch.models.Article;

import java.util.List;

/**
 * Created by nminh on 6/24/17.
 */

public class SearchResult {
    @SerializedName("docs")
    List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }
}
