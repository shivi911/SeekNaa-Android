package com.example.shivi.com.example.shivi.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.shivi.com.example.shivi.models.News;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by shivi on 5/24/16.
 */
public class NewsFetcher extends AsyncTask<Integer, Void, JSONObject> {
    int count = 20;
    String nyTimesURL = "http://api.nytimes.com/svc/mostpopular/v2/mostemailed/all-sections/1.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873";


    List<News> newsList;
    ArrayAdapter<News> newsArrayAdapter;

    public NewsFetcher(List<News> newsList, ArrayAdapter<News> newsArrayAdapter) {
        this.newsList = newsList;
        this.newsArrayAdapter = newsArrayAdapter;
    }
    @Override
    protected JSONObject doInBackground(Integer... params) {
        HttpURLConnection connection = null;

        try {
            StringBuffer nyURL0 = new StringBuffer(nyTimesURL);
            nyURL0.append("&offset="+params[0]);
            if(params[0]>0) count = params[0];

            Log.d("URL ", nyURL0.toString());

            URL nyURL = new URL(nyURL0.toString());
            connection = (HttpURLConnection) nyURL.openConnection();
            int response = connection.getResponseCode();
            if (response != HttpURLConnection.HTTP_OK) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            // Log.d("NEWSFEED: ", builder.toString());
            return new JSONObject(builder.toString());

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            connection.disconnect();
        }
        return null;
    }

    protected void onPostExecute(JSONObject news) {
        News.makeNewsList(news, newsList);


        Log.d("POST EXECT", "POST " + newsList.size());
        newsArrayAdapter.notifyDataSetChanged();
    }
}
