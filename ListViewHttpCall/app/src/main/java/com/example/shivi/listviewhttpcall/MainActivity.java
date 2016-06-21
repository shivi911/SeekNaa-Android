package com.example.shivi.listviewhttpcall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.shivi.listviewhttpcall.adapters.NewsListAdapter;
import com.example.shivi.listviewhttpcall.models.News;
import com.example.shivi.listviewhttpcall.network.NewsFetcher;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private NewsListAdapter newsListAdapter;
    private ListView newsListView; // displays news info
    private List<News> newsList = new ArrayList<>();
    String nyTimesURL = "http://api.nytimes.com/svc/mostpopular/v2/mostemailed/all-sections/1.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873";
    EndlessScrollListener scrollListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsListView = (ListView) findViewById(R.id.newsList);
        newsListAdapter = new NewsListAdapter(this, newsList);
        newsListView.setAdapter(newsListAdapter);

        // NewsFetcherTask newsFetcher = new NewsFetcherTask();
        final NewsFetcher newsFetcher = new NewsFetcher(newsList, newsListAdapter);
        scrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.d("ON SCROLL ", "PAGE " + page + " COUNT " + totalItemsCount);
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                // new NewsFetcherTask().execute(new Integer(page*20));
                // new NewsFetcher(newsList, newsListAdapter).execute(new Integer(page*20));
                return true;
            }
        };


        newsListView.setOnScrollListener(scrollListener);


        int offset = 0;
       //  newsFetcher.execute(new Integer(offset));

    }


}
