package me.oueslati.fakher.flickrapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;

import me.oueslati.fakher.flickrapp.model.Photo;
import me.oueslati.fakher.flickrapp.util.FlickrJsonUtil;


public class ImageSlider extends FragmentActivity {
    //        implements LoaderManager.LoaderCallbacks<String> {
    private static final String TAG = ImageSlider.class.getSimpleName();
    private static Photo[] photos;
    private static int currentImagePosition;
    private ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_page);
        Intent intent = getIntent();
        String flickrJsonResults = intent.getStringExtra("flickr_search_json_results");
        try {
            photos = FlickrJsonUtil.getPhotosFromJson(flickrJsonResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        currentImagePosition = intent.getIntExtra("image_current_position", 0);
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);
        viewPager.setCurrentItem(currentImagePosition);
    }

    public String getOwnerProfilePictureURL(Photo photo) {

        return "";
    }

    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return photos.length;
        }

        @Override
        public Fragment getItem(int position) {
            return SwipeFragment.newInstance(position);
        }
    }

    public static class SwipeFragment extends Fragment {
        static SwipeFragment newInstance(int position) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
            TextView mPhotoTitleTextView = (TextView) swipeView.findViewById(R.id.tv_photo_title);
            Bundle bundle = getArguments();
            int position = bundle.getInt("position");
            String photoURL = photos[position].getPhotoURL();
            String photoTitle = photos[position].getTitle();
            Glide.with(this)
                    .load(photoURL)
                    .crossFade()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            mPhotoTitleTextView.setText(photoTitle);
            return swipeView;
        }
    }
/*
    private void requestPhotoInfo(Photo photo) {
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
               // mLoadingIndicator.setVisibility(View.VISIBLE);

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

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        //mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null && !data.equals("")) {
          //  mSearchResultsTextView.setText(data);
            try {
                Photo[] images = FlickrJsonUtil.getPhotosFromJson(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //TODO Error case
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }*/
}
