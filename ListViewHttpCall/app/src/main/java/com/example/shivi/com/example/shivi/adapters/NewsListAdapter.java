package com.example.shivi.com.example.shivi.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shivi.com.example.shivi.models.News;
import com.example.shivi.com.example.shivi.network.ImageLoader;
import com.example.shivi.listviewhttpcall.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shivi on 5/14/16.
 */
public class NewsListAdapter extends ArrayAdapter<News> {
    private final Activity context;
    private final List<News> news;
    private Map<String, Bitmap> bitmaps = new HashMap<>();
    private Integer defaultThumbNail = R.mipmap.ic_launcher;

    public NewsListAdapter(Activity ctx, List<News> news) {
        super(ctx, R.layout.newsitem, news);
        this.news = news;
        this.context = ctx;

    }



    static class ViewHolderItem {
        TextView textViewItem;
        ImageView imageViewItem;
        TextView extratxtItem;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;
        News newsItem = news.get(position);

        if(convertView==null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.newsitem, null, true);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.item);
            viewHolder.imageViewItem = (ImageView) convertView.findViewById(R.id.newsThumbNail);
            viewHolder.extratxtItem = (TextView) convertView.findViewById(R.id.textView1);

            convertView.setTag(viewHolder);
        }
        else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
            Log.d("SET", "NULL");
            viewHolder.imageViewItem.setImageBitmap(null); // to prevent flicker of images when list view is scrolled
        }

        viewHolder.textViewItem.setText(newsItem.title);

        // viewHolder.imageViewItem.setImageResource(imgid[position]);
        viewHolder.extratxtItem.setText(newsItem.byline);

        if(newsItem.imageUrl != null) {
            if (bitmaps.containsKey(newsItem.imageUrl)) {
                viewHolder.imageViewItem.setImageBitmap(bitmaps.get(newsItem.imageUrl));
            }
            else {
                new ImageLoader(viewHolder.imageViewItem, bitmaps).execute(newsItem.imageUrl);
                // new LoadImageTask(viewHolder.imageViewItem).execute(newsItem.imageUrl);
            }
        }
        else {
            viewHolder.imageViewItem.setImageResource(defaultThumbNail);
        }
        return convertView;

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
