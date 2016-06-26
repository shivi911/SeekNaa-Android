package com.example.shivi.viewpagernews.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shivi.viewpagernews.R;
import com.example.shivi.viewpagernews.models.News;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shivi on 5/14/16.
 */
// public class NewsListAdapter extends ArrayAdapter<News> {
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsHolder>  {
    // private final Activity context;
    private final List<News> news;
    private Map<String, Bitmap> bitmaps = new HashMap<>();
    private Integer defaultThumbNail = R.mipmap.ic_launcher;
    private OnItemClickListener mListener;

    public NewsListAdapter(List<News> news, OnItemClickListener listener) {
        // super(ctx, R.layout.newsitem, news);
        this.news = news;
        this.mListener = listener;
        // this.context = ctx;

    }


    public interface OnItemClickListener {
        void onItemClick(News item);
    }

    public class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewItem;
        ImageView imageViewItem;
        TextView extratxtItem;

        News mNewsItem;

        public NewsHolder(View v) {
            super(v);
            v.setOnClickListener(this);


            textViewItem = (TextView) v.findViewById(R.id.item);
            imageViewItem = (ImageView) v.findViewById(R.id.newsThumbNail);
            extratxtItem = (TextView) v.findViewById(R.id.textView1);
        }

        public void bindNewsItem(News newsItem) {
            mNewsItem = newsItem;
            imageViewItem.setImageBitmap(null); // to prevent flicker of images when list view is scrolled
            textViewItem.setText(newsItem.title);
            extratxtItem.setText(newsItem.byline);

            if (newsItem.imageUrl != null) {
                // Log.d("NLADAP", "PICASSO");
                Picasso.with(itemView.getContext()).load(newsItem.imageUrl).into(imageViewItem);
                /*
                if (bitmaps.containsKey(newsItem.imageUrl)) {
                    Log.d("NLADAPT", "From cache");
                    imageViewItem.setImageBitmap(bitmaps.get(newsItem.imageUrl));
                } else {
                    new LoadImageTask(imageViewItem).execute(newsItem.imageUrl);
                    // new ImageLoader(imageViewItem, bitmaps).execute(newsItem.imageUrl);
                }
                */
            } else {
                imageViewItem.setImageResource(defaultThumbNail);
            }

        }

        @Override
        public void onClick(View v) {
            Log.d("NLADAPT", "ONCLICK"); mListener.onItemClick(mNewsItem);
        }
    }

    public void add(int position, News item) {
        news.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(News item) {
        int position = news.indexOf(item);
        news.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public NewsListAdapter.NewsHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.newsitem, parent, false);

        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        final News newsItem = news.get(position);
        holder.bindNewsItem(newsItem);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        // Log.d("ADAPTER", "# OF ITEMS " + news.size());
        return news.size();
    }



    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView; // displays the thumbnail


        // store ImageView on which to set the downloaded Bitmap
        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... params) {
            HttpURLConnection connection = null;
            Bitmap bitmap = null;


            try {
                URL imgURL = new URL(params[0]);
                connection = (HttpURLConnection) imgURL.openConnection();

                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                bitmaps.put(params[0], bitmap); // cache for later use

            } catch (Exception ex) {
                ex.printStackTrace();

            } finally {
                connection.disconnect();
            }
            return bitmap;

        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
