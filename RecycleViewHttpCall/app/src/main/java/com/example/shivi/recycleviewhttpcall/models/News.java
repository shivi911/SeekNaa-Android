package com.example.shivi.recycleviewhttpcall.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by shivi on 5/14/16.
 */
public class News {
    public String title;
    public String imageUrl;
    public String byline;
    public String newsUrl;

    public static News makeNews(JSONObject newsItemJson) {
        News news = null;
        try {
            String title = newsItemJson.getString("title");
            String byline = newsItemJson.getString("byline");
            news = new News();
            news.title = title;
            news.byline = byline;
            news.newsUrl = newsItemJson.getString("url");
            try {
                JSONArray mediaJson = newsItemJson.getJSONArray("media");
                if (mediaJson != null) {

                    JSONArray mediaMetaJson = mediaJson.getJSONObject(0).getJSONArray("media-metadata");
                    if (mediaMetaJson != null) {
                        String imageUrl = mediaMetaJson.getJSONObject(0).getString("url");
                        //  Log.d("IMAGE URL IS: ", imageUrl);
                        news.imageUrl = imageUrl;

                    }
                }
            } catch (Exception ex) {
                //  Log.d("IMAGE URL :", "UNSUAL GUY");
            }
        }
        catch(Exception ex) {

        }
        return news;
    }

    public static void makeNewsList(JSONObject newsJson, List<News> newsList) {
        try {
            JSONArray list = newsJson.getJSONArray("results");

            for (int i = 0; i < list.length(); i++) {
                JSONObject newsItemJson = list.getJSONObject(i);
                News news = News.makeNews(newsItemJson);
                if(news!=null) newsList.add(news);
            }
        }
        catch(Exception ex) {

        }

    }
}
