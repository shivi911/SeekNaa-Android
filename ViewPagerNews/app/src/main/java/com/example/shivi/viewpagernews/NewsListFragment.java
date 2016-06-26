package com.example.shivi.viewpagernews;

import android.support.v4.app.Fragment;
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
import android.widget.ListView;

import com.example.shivi.viewpagernews.adapters.NewsListAdapter;
import com.example.shivi.viewpagernews.models.News;
import com.example.shivi.viewpagernews.network.NewsFetcher;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnNewsItemClickListener}
 * interface.
 */
public class NewsListFragment extends Fragment {
    private NewsListAdapter newsListAdapter;
    RecyclerView newsRecyclerView;
    ListView newsListView;
    private List<News> newsList = new ArrayList<>();
    // http://api.nytimes.com/svc/topstories/v2/home.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873&offset=0
    // https://developer.nytimes.com/top_stories_v2.json#/Console/GET/%7Bsection%7D.%7Bformat%7D
    // http://api.nytimes.com/svc/topstories/v2/world.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873&offset=0
    // http://api.nytimes.com/svc/topstories/v2/business.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873
    // http://api.nytimes.com/svc/topstories/v2/technology.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873
    // http://api.nytimes.com/svc/topstories/v2/health.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873
    // http://api.nytimes.com/svc/topstories/v2/sports.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873
    // http://api.nytimes.com/svc/topstories/v2/movies.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873
    // http://api.nytimes.com/svc/topstories/v2/books.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873

    String nyTimesURL = nyTimesURL = "http://api.nytimes.com/svc/topstories/v2/home.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873";

    // String nyTimesURL = "http://api.nytimes.com/svc/mostpopular/v2/mostemailed/all-sections/1.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873";
    int count = 20; // how many news items to fetch
    // EndlessScrollListener2 scrollListener;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnNewsItemClickListener mListener;

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
        Log.d("FRAG", "ONCREATE");
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    // Call after View is created
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("FRAG", "ONVIEWCREATED");
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true); // save fragment across config changes


        Log.d("FRAG", "ON ACTIVITY CREATED");
        new NewsFetcherTask().execute(new Integer(0));

        /*
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
        */

        // newsRecyclerView.addOnScrollListener(scrollListener);

        // newsRecyclerView.setOnScrollListener(scrollListener);

    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("FRAG", "ONCREATVIEW");


        String categoryId = getArguments().getString("CategoryId");


        if (categoryId.equals("0")) {
            nyTimesURL = "http://api.nytimes.com/svc/topstories/v2/home.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873";
        }
        if (categoryId.equals("1")) {
            nyTimesURL = "http://api.nytimes.com/svc/topstories/v2/world.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873";
        }
        if (categoryId.equals("2")) {
            nyTimesURL = "http://api.nytimes.com/svc/topstories/v2/business.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873";
        }
        if (categoryId.equals("3")) {
            nyTimesURL = "http://api.nytimes.com/svc/topstories/v2/technology.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873";
        }
        if (categoryId.equals("4")) {
            nyTimesURL = "http://api.nytimes.com/svc/topstories/v2/health.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873";
        }
        if (categoryId.equals("5")) {
            nyTimesURL = "http://api.nytimes.com/svc/topstories/v2/sports.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873";
        }
        if (categoryId.equals("6")) {
            nyTimesURL = "http://api.nytimes.com/svc/topstories/v2/movies.json?api-key=fa5723452d7d2454cf24a2a3d920012c:10:66680873";
        }


        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        newsRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        // Reference: http://antonioleiva.com/recyclerview-listener/
        // handle news item click with the listener; delegate the real work to the listener in the hosting activity
        newsListAdapter = new NewsListAdapter(newsList, new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(News item) {
                Log.d("NLFRAG", "ONITEMCLICK");
                mListener.onNewsItemSelected(item.newsUrl);
            }
        });


        Context context = view.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        if (mColumnCount <= 1) {
            newsRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            newsRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        // newsRecyclerView.addOnScrollListener(scrollListener);


        // NewsListAdapter.EndlessScrollListener2 scrollListener = new NewsListAdapter.EndlessScrollListener2() {
        /*
        EndlessScrollListener scrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public boolean onLoadMore(int page, int count) {
                Log.d("ON SCROLL00 ", "PAGE " + page);
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                // new NewsFetcherTask().execute(new Integer(page*20));
                // new NewsFetcher(newsList, newsListAdapter).execute(new Integer(page*20));
                new NewsFetcherTask().execute(new Integer(page * 20));
                return true;
            }
        };
        */

        // newsListAdapter.setEndlessScrollListener(scrollListener);
        // newsRecyclerView.addOnScrollListener(scrollListener);
        newsRecyclerView.setAdapter(newsListAdapter);


        return view;
    }


    @Override
    public void onAttach(Context context) {
        Log.d("FRAG", "ONATTACH");
        super.onAttach(context);
        if (context instanceof OnNewsItemClickListener) {
            Log.d("FRAG", "ONATTACH -- I M FRAGMENT INTERACTION");
            mListener = (OnNewsItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnNewsItemClickListener {
        // called when a user selects a news item
        public void onNewsItemSelected(String newsUrl);
    }

    private class NewsFetcherTask extends AsyncTask<Integer, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... params) {
            StringBuffer nyURL0 = new StringBuffer(nyTimesURL);
            // nyURL0.append("&offset=" + params[0]);
            // if (params[0] > 0) count = params[0];


            try {
                String result = new NewsFetcher().getUrlData(nyURL0.toString());
                return new JSONObject(result);
            } catch (Exception ex) {
                Log.e("NewsListFragment", ex.getMessage());
                return null;
            }


        }

        protected void onPostExecute(JSONObject news) {
            News.makeNewsList(news, newsList);
            newsListAdapter.notifyDataSetChanged();
        }
    }
}
