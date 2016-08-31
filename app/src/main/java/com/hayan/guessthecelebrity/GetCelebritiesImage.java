package com.hayan.guessthecelebrity;

/**
 * Created by Hayan on 2016-08-30.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Async task that returns the celebrity image from website as bitmap.
 */
public class GetCelebritiesImage extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... params) {
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            Bitmap celebMap = BitmapFactory.decodeStream(in);
            return celebMap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}