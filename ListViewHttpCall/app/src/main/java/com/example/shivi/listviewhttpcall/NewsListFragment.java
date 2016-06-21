package com.example.shivi.listviewhttpcall;

import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.shivi.EndlessScrollListener;
import com.example.shivi.com.example.shivi.adapters.NewsListAdapter;
import com.example.shivi.com.example.shivi.models.News;
import com.example.shivi.com.example.shivi.network.NewsFetcher;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NewsListFragment extends ListFragment {
    private NewsListAdapter newsListAdapter;
    ListView newsListView;
    private List<News> newsList = new ArrayList<>();
    String nyTimesURL = "http://api.nytimes.com/svc/mostpopular/v2/mostemailed/all-sections/1.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873";
    int count = 20; // how many news items to fetch
    EndlessScrollListener scrollListener;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NewsListFragment newInstance(int columnCount) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    // Call after View is created
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
        setRetainInstance(true ); // save fragment across config changes

        newsListView = getListView();
        newsListAdapter = new NewsListAdapter(getActivity(), newsList);
        newsListView.setAdapter(newsListAdapter);
        */

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true ); // save fragment across config changes
        newsListView = getListView();

        newsListAdapter = new NewsListAdapter(getActivity(), newsList);
        newsListView.setAdapter(newsListAdapter);

        Log.d("FRAG", "CALLING NEWSFETCH00");
        // new NewsFetcher(newsList, newsListAdapter).execute(new Integer(0));
        new NewsFetcherTask().execute(new Integer(0));

        scrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.d("ON SCROLL00 ", "PAGE " + page + " COUNT " + totalItemsCount);
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                // new NewsFetcherTask().execute(new Integer(page*20));
                // new NewsFetcher(newsList, newsListAdapter).execute(new Integer(page*20));
                new NewsFetcherTask().execute(new Integer(page*20));
                return true;
            }
        };

        newsListView.setOnScrollListener(scrollListener);

        newsListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                News news = (News) parent.getItemAtPosition(position);
                mListener.onNewsItemSelected(news.newsUrl); // pass selection to NewsActivity
                // Log.d("ITEM IS ", "URL " + news.newsUrl);
            }
        });



    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);


        // ListView newsListView = getListView();


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            // recyclerView.setAdapter(new NewsListAdapter(DummyContent.ITEMS, mListener));
        }
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // called when a user selects a news item
        public void onNewsItemSelected(String newsUrl);
    }

    private class NewsFetcherTask extends AsyncTask<Integer, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... params) {
            StringBuffer nyURL0 = new StringBuffer(nyTimesURL);
            nyURL0.append("&offset=" + params[0]);
            if (params[0] > 0) count = params[0];


            try {
                String result = new NewsFetcher().getUrlData(nyURL0.toString());
                return new JSONObject(result);
            }
            catch(Exception ex) {
                Log.e("NewsListFragment", ex.getMessage());
                return null;
            }


        }

        protected void onPostExecute(JSONObject news) {
            News.makeNewsList(news, newsList);


            Log.d("POST EXECT", "POST " + newsList.size());
            newsListAdapter.notifyDataSetChanged();
        }
    }
}
