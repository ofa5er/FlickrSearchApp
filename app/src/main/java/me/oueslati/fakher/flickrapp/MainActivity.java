package me.oueslati.fakher.flickrapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import me.oueslati.fakher.flickrapp.model.Photo;
import me.oueslati.fakher.flickrapp.util.FlickrJsonUtil;
import me.oueslati.fakher.flickrapp.util.FlickrNetworkUtils;
import me.oueslati.fakher.flickrapp.widget.AutoCompleteSearchView;

public class MainActivity extends AppCompatActivity {

    public static final int NUM_RESULT_IMAGES = 100;
    public static final int NUM_GRID_COLUMN = 3;


    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageAdapter mAdapter;
    private RecyclerView mRecyclerImagesList;
    private TextView mSearchResultsTextView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private AutoCompleteSearchView mSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_flickr_search_results_json);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecyclerImagesList = (RecyclerView) findViewById(R.id.rv_images);

        mRecyclerImagesList.setHasFixedSize(true);
        mRecyclerImagesList.setDrawingCacheEnabled(true);
        mRecyclerImagesList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUM_GRID_COLUMN);
        mRecyclerImagesList.setLayoutManager(layoutManager);
        mRecyclerImagesList.setHasFixedSize(true);
        mAdapter = new ImageAdapter(this);
        mRecyclerImagesList.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mSearchView = (AutoCompleteSearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.saveKeywordsHistory(query);
                mAdapter.clear();
                makeFlickrSearchQuery(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }


    private void makeFlickrSearchQuery(String keyword) {
        URL flickrSearchURL = FlickrNetworkUtils.buildURLWithPhotoSearchQuery(keyword, NUM_RESULT_IMAGES);
        new FlickSearchQueryTask().execute(flickrSearchURL);
    }

    public class FlickSearchQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String flickrSearchResults = null;

            try {
                flickrSearchResults = FlickrNetworkUtils.getResponseFromHttpUrl(searchURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return flickrSearchResults;
        }

        @Override
        protected void onPostExecute(String flickrSearchResults) {
            super.onPostExecute(flickrSearchResults);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (flickrSearchResults != null && !flickrSearchResults.equals("")) {
                mSearchResultsTextView.setText(flickrSearchResults);
                try {
                    Photo[] images = FlickrJsonUtil.getPhotosFromJson(flickrSearchResults);
                    mAdapter.setImageListData(images);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //TODO Error case
            }

        }
    }

}
