package com.example.shivi.com.example.shivi.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by shivi on 5/24/16.
 */
public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView; // displays the thumbnail
    private Map<String, Bitmap> bitmaps;

    public ImageLoader(ImageView imageView, Map<String, Bitmap> bitmaps) {
        this.imageView = imageView;
        this.bitmaps = bitmaps;
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
            if (connection != null) connection.disconnect();
        }
        return bitmap;

    }

    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
