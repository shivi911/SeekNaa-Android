package com.example.shivi.recycleviewhttpcall;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

// the NewsActivity is the MainActivity; the main difference is that it uses fragments
public class NewsActivity extends AppCompatActivity implements NewsListFragment.OnNewsItemClickListener, NewsDetailFragment.OnFragmentInteractionListener {

    NewsListFragment newsListFragment; // displayes list of news

    // display NewsListFragment when MainActivity first loads
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.news_articles);


        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            // If the Activity is being restored after being shut down or recreated from a configuration change,
            // savedInstanceState will not be null
            if (savedInstanceState != null) {
                return;
            }

            newsListFragment = new NewsListFragment();
            newsListFragment.setArguments(getIntent().getExtras());
            FragmentManager fragmentManager = getFragmentManager();
            // fragment transaction is used to add and remove fragments
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, newsListFragment);
            fragmentTransaction.commit(); // causes ContactListFragment to display
        }
        getSupportActionBar().setTitle("Top News");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // if newsListFragment is null, it was running on tablet

        if (newsListFragment == null) {
            newsListFragment = (NewsListFragment) getFragmentManager().findFragmentById(R.id.newsListFragment);

        }
    }

    @Override
    public void onNewsItemSelected(String newsUrl) {
        // launch the fragment to show the news as a webview
        Log.d("NEWSACT", "NEWS URL IS " + newsUrl);

        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString("NEWS_URL", newsUrl);
        newsDetailFragment.setArguments(arguments);

        // use a FragmentTransaction to display the NewsDetailsFragment
        FragmentTransaction transaction =
                getFragmentManager().beginTransaction();
        int viewID = R.id.fragment_container;
        // transaction.replace(viewID, newsDetailFragment);
        transaction.add(viewID, newsDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes NewsDetailsFragment to display

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
