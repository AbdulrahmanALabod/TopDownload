package com.Abdulrohman.TopDownload;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView lisViewTop;
    private String strFeedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int feedLimit = 10;
    public static final String LIMIT_FEED = "limitFeed";
    public static final String URL_FEED = "urlFeed";
    public String checkUrlValid = "unvalid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lisViewTop = (ListView) findViewById(R.id.lstViewNtop);
        if (savedInstanceState != null) {
            feedLimit = savedInstanceState.getInt(LIMIT_FEED);
            strFeedUrl = savedInstanceState.getString(URL_FEED);
            Log.i(TAG, "onCreate: "+strFeedUrl +"\n"+feedLimit);
        }
        downloadUrl(String.format(strFeedUrl, feedLimit));
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feed, menu);

        if (feedLimit == 10) {
            menu.findItem(R.id.groupTop10).setChecked(true) ;
        } else {
            feedLimit = 25;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectItem = item.getItemId();
        switch (selectItem) {
            case R.id.mnueFreeApp:
                strFeedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                break;
            case R.id.mnuePaidApp:
                strFeedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                break;
            case R.id.menuSongApp:
                strFeedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                break;
            case R.id.groupTop10:
            case R.id.groupTop25:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    feedLimit = 35 - feedLimit;
                    Log.i("selc", String.valueOf(feedLimit));
                    Log.i(TAG, "onOptionsItemSelected: " + item.getTitle());
                } else {
                    Log.i(TAG, "onOptionsItemSelected: " + item.getTitle() + "unchanged");
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        downloadUrl(String.format(strFeedUrl, feedLimit));
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(LIMIT_FEED, feedLimit);
        outState.putString(URL_FEED, strFeedUrl);
        super.onSaveInstanceState(outState);
    }

    public void downloadUrl(String url) {
        Log.d(TAG, "onCreate: starting Asynctask"+url);
        DownloadData downloadData = new DownloadData();
        if (!strFeedUrl.equals(checkUrlValid)) {
            downloadData.execute(url);
            checkUrlValid = strFeedUrl;
            Log.i(TAG, "downloadUrl: "+checkUrlValid);
        } else {
            Log.i(TAG, "un change for refresh or orintion ");
        }

    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is " + s);
            ParseApp parseApp = new ParseApp();
            parseApp.parse(s);
            Log.d(TAG, "onCreate: done");
//            ArrayAdapter<RssFeed> rssFeedArrayAdapter = new ArrayAdapter<RssFeed>(MainActivity.this, R.layout.txt_item, parseApp.getRssFeed());
//            lisViewTop.setAdapter(rssFeedArrayAdapter);
            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.item_demo, parseApp.getRssFeed());
            lisViewTop.setAdapter(feedAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was " + response);
//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader reader = new BufferedReader(inputStreamReader);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];
                while (true) {
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) {
                        break;
                    }
                    if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();
            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: IO Exception reading data: " + e.getMessage());
            }
            return xmlResult.toString();
        }

    }
}
