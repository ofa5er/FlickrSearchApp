package me.oueslati.fakher.flickrapp;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import me.oueslati.fakher.flickrapp.model.Photo;
import me.oueslati.fakher.flickrapp.util.FlickrJsonUtil;
import me.oueslati.fakher.flickrapp.util.FlickrNetworkUtils;
import me.oueslati.fakher.flickrapp.widget.AutoCompleteSearchView;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String> {

    private static final int NUM_RESULT_IMAGES = 100;
    private static final int NUM_GRID_COLUMN = 3;
    private static final int FLICKR_PHOTO_SEARCH_LOADER = 11;
    private static final String FLICKR_PHOTO_SEARCH_URL = "search_url";

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageAdapter mAdapter;
    private RecyclerView mRecyclerImagesList;
    private TextView mSearchHintTextView;
    private ProgressBar mLoadingIndicator;
    private AutoCompleteSearchView mSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        mSearchHintTextView = (TextView) findViewById(R.id.tv_flickr_search_hint);
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

        getSupportLoaderManager().initLoader(FLICKR_PHOTO_SEARCH_LOADER, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoadingIndicator.setVisibility(View.INVISIBLE);
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
        Bundle queryBundle = new Bundle();
        if (flickrSearchURL == null || flickrSearchURL.toString().equals("")) {
            Log.e(TAG, "makeFlickrSearchQuery: Empty flickrSearchURL");
            return;
        }
        queryBundle.putString(FLICKR_PHOTO_SEARCH_URL, flickrSearchURL.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> flickSearch = loaderManager.getLoader(FLICKR_PHOTO_SEARCH_LOADER);
        if (flickSearch == null) {
            loaderManager.initLoader(FLICKR_PHOTO_SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(FLICKR_PHOTO_SEARCH_LOADER, queryBundle, this);
        }

    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String mResultCache = null;

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                mSearchHintTextView.setVisibility(View.INVISIBLE);
                if (mResultCache == null) {
                    forceLoad();
                } else {
                    deliverResult(mResultCache);
                }
            }

            @Override
            public String loadInBackground() {
                String searchURL = args.getString(FLICKR_PHOTO_SEARCH_URL);
                if (searchURL == null || searchURL.equals("")) {
                    return null;
                }
                try {
                    URL url = new URL(searchURL);
                    return FlickrNetworkUtils.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                mResultCache = data;
                super.deliverResult(mResultCache);
            }
        };
    }

    private void showErrorMessage() {
        Toast.makeText(this, "Error, please try again...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerImagesList.setVisibility(View.VISIBLE);
        mSearchHintTextView.setVisibility(View.INVISIBLE);
        if (data != null && !data.equals("")) {
            try {
                Photo[] images = FlickrJsonUtil.getPhotosFromJson(data);
                mAdapter.setImageListData(images);
                //TODO(fakher): Change to parcelable.
                mAdapter.setFlickrJsonResults(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
