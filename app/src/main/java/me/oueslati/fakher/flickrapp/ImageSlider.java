package me.oueslati.fakher.flickrapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.oueslati.fakher.flickrapp.model.Photo;
import me.oueslati.fakher.flickrapp.util.FlickrJsonUtil;
import me.oueslati.fakher.flickrapp.util.FlickrNetworkUtils;


public class ImageSlider extends FragmentActivity {
    private static final String TAG = ImageSlider.class.getSimpleName();
    private static final String FLICKR_PHOTO_LOCATION_URL = "photo_location";
    private static final String FLICKR_PHOTO_INFO_URL = "photo_info";
    private static final int FLICKR_PHOTO_LOCATION_LOADER = 22;

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

        //Loads 5 views in advance, it helps in providing a smooth navigation for a small increase
        // memory usage.
        viewPager.setOffscreenPageLimit(5);

    }

    public static class ImageFragmentPagerAdapter extends FragmentStatePagerAdapter {
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

    public static class SwipeFragment extends Fragment
            implements LoaderManager.LoaderCallbacks<String[]> {
        private TextView mLocationTextView;
        private TextView mPostedDateTextView;
        private ImageView mProfilePictureImageView;
        private TextView mOwnerRealNameTextView;

        static SwipeFragment newInstance(int position) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }

        private void requestPhotoInfo(Photo photo) {
            URL photoLocationURL =
                    FlickrNetworkUtils.buildURLWithPhotoGetLocationQuery(photo.getId());
            URL photoInfoURL =
                    FlickrNetworkUtils.buildURLWithPhotoGetInfoQuery(photo.getId(),
                            photo.getSecret());

            Bundle queryBundle = new Bundle();
            if (photoLocationURL == null || photoLocationURL.toString().equals("")) {
                Log.e(TAG, "requestPhotoInfo: Empty photoLocationURL");
                return;
            }
            if (photoInfoURL == null || photoInfoURL.toString().equals("")) {
                Log.e(TAG, "requestPhotoInfo: Empty photoInfoURL");
                return;
            }

            queryBundle.putString(FLICKR_PHOTO_LOCATION_URL, photoLocationURL.toString());
            queryBundle.putString(FLICKR_PHOTO_INFO_URL, photoInfoURL.toString());


            LoaderManager loaderManager = getLoaderManager();
            Loader<String> flickSearch = loaderManager.getLoader(FLICKR_PHOTO_LOCATION_LOADER);
            if (flickSearch == null) {
                loaderManager.initLoader(FLICKR_PHOTO_LOCATION_LOADER, queryBundle, this);
            } else {
                loaderManager.restartLoader(FLICKR_PHOTO_LOCATION_LOADER, queryBundle, this);
            }
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
            TextView mPhotoTitleTextView = (TextView) swipeView.findViewById(R.id.tv_photo_title);
            mLocationTextView = (TextView) swipeView.findViewById(R.id.tv_photo_location);
            mPostedDateTextView = (TextView) swipeView.findViewById(R.id.tv_posted_date);
            mProfilePictureImageView = (ImageView) swipeView.findViewById(R.id.iv_profile_picture);
            mOwnerRealNameTextView = (TextView) swipeView.findViewById(R.id.tv_owner_name);
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
            requestPhotoInfo(photos[position]);
            Log.v(TAG, "onCreateView(): " + position);
            return swipeView;
        }

        @Override
        public Loader<String[]> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<String[]>(getActivity()) {
                String[] mResultCache = null;

                @Override
                protected void onStartLoading() {
                    if (args == null) {
                        Log.v(TAG, "args are null");
                        return;
                    }
                    Log.v(TAG, "onStartLoading()");
                    if (mResultCache == null) {
                        forceLoad();
                    } else {
                        deliverResult(mResultCache);
                    }
                }

                @Override
                public String[] loadInBackground() {
                    String locationURLStr = args.getString(FLICKR_PHOTO_LOCATION_URL);
                    String infoURLStr = args.getString(FLICKR_PHOTO_INFO_URL);
                    Log.v(TAG, "loadInBackground()");
                    if (locationURLStr == null || locationURLStr.equals("")) {
                        Log.e(TAG, "loadInBackground():" + "Empty locationURL ");
                        return null;
                    }
                    if (infoURLStr == null || infoURLStr.equals("")) {
                        Log.e(TAG, "loadInBackground():" + "Empty infoURL ");
                        return null;
                    }
                    try {
                        URL locationURL = new URL(locationURLStr);
                        URL infoURL = new URL(infoURLStr);

                        String result[] = new String[2];
                        result[0] = FlickrNetworkUtils.getResponseFromHttpUrl(locationURL);
                        result[1] = FlickrNetworkUtils.getResponseFromHttpUrl(infoURL);
                        return result;

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(String[] data) {
                    mResultCache = data;
                    super.deliverResult(mResultCache);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<String[]> loader, String[] data) {
            if (data != null) {
                try {
                    Photo photo = FlickrJsonUtil.getPhotoLocationFromJson(new Photo(), data[0]);
                    photo = FlickrJsonUtil.getPhotoInfoFromJson(photo, data[1]);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String date = sdf.format(new Date(photo.getPostedDate() * 1000));
                    mPostedDateTextView.setText(date);
                    mLocationTextView.setText(photo.getLocation());
                    mOwnerRealNameTextView.setText(photo.getOwner().getRealname());

                    Glide.with(getActivity())
                            .load(photo.getOwner().getProfilePictureURL())
                            .asBitmap()
                            .centerCrop()
                            .into(new BitmapImageViewTarget(mProfilePictureImageView) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(
                                                    getView().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    mProfilePictureImageView.
                                            setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showErrorMessage();
            }
        }

        private void showErrorMessage() {
            Toast.makeText(getContext(), "Error, please try again...", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onLoaderReset(Loader<String[]> loader) {

        }

    }

}
