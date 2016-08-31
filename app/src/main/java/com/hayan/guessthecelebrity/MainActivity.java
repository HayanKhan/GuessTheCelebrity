package com.hayan.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private String[] celebrityNameArray;
    private String[] celebrityImagesURLArray;
    private Button[] buttonArray;

    private ImageView image;
    private int chosenCelebButtonPosition;

    private String sourceCode;
    private String url = "http://www.posh24.com/celebrities";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celebrityNameArray = new String[100];
        celebrityImagesURLArray = new String[200];
        buttonArray = new Button[4];
        image = (ImageView) findViewById(R.id.imageView);

        buttonArray[0] = (Button) findViewById(R.id.buttonAnswer0);
        buttonArray[1] = (Button) findViewById(R.id.buttonAnswer1);
        buttonArray[2] = (Button) findViewById(R.id.buttonAnswer2);
        buttonArray[3] = (Button) findViewById(R.id.buttonAnswer3);

        GetCelebrities getCeleb = new GetCelebrities();

        try {
            sourceCode = getCeleb.execute(url).get();
            String[] uniqueSourceCode = sourceCode.split("<div class=\"sidebarContainer\">");

            Pattern p = Pattern.compile("alt=\"(.*?)\"");
            Matcher m = p.matcher(uniqueSourceCode[0]);

            int i = 0;
            while (m.find()){
                celebrityNameArray[i++] = m.group(1);
            }

            p = Pattern.compile("<img src=\"(.*?)\"");
            m = p.matcher(uniqueSourceCode[0]);

            i = 0;
            while (m.find()){
                celebrityImagesURLArray[i++] = m.group(1);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        createQuestion();
    }

    /**
     * Async task that returns the source code of the web page
     */
    public class GetCelebrities extends AsyncTask<String, Void, String>{

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


    /**
     * Async task that returns the celebrity image from website as bitmap.
     */
    public class GetCelebritiesImage extends AsyncTask<String, Void, Bitmap>{

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

    /**
     * Resets the page, and create a new question for the user.
     */
    public void createQuestion(){
        Random rgen = new Random();
        int chosenCeleb = rgen.nextInt(100);
        url = celebrityImagesURLArray[chosenCeleb];

        GetCelebritiesImage getCelebImage = new GetCelebritiesImage();
        try {
            Bitmap chosenCelebImage = getCelebImage.execute(url).get();
            image.setImageBitmap(chosenCelebImage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        chosenCelebButtonPosition = rgen.nextInt(4);
        buttonArray[chosenCelebButtonPosition].setText(celebrityNameArray[chosenCeleb]);

        for (int i = 0 ; i < buttonArray.length ; i++){
            if ( i != chosenCelebButtonPosition){
                buttonArray[i].setText(celebrityNameArray[rgen.nextInt(100)]);
            }
        }
    }

    /**
     * Executes event for when a specific button tapped is correct or incorrect.
     * @param view The button that was tapped.
     */
    public void buttonTapped(View view){
        int celebNameChosen = Integer.parseInt((String) view.getTag());

        if (celebNameChosen == chosenCelebButtonPosition){
            Toast.makeText(getApplicationContext(), "CORRECT!", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(getApplicationContext(), "INCORRECT!", Toast.LENGTH_SHORT).show();
        }
        createQuestion();
    }
}