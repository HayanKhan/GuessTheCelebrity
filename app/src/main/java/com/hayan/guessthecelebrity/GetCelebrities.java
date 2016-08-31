package com.hayan.guessthecelebrity;

/**
 * Created by Hayan on 2016-08-30.
 */

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Async task that returns the source code of the web page
 */
public class GetCelebrities extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String sourceCode = "";
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while (data != -1) {
                char current = (char) data;
                sourceCode += current;
                data = reader.read();
            }
            return sourceCode;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
