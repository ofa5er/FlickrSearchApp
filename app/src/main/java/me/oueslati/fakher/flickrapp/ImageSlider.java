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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;

import me.oueslati.fakher.flickrapp.model.Photo;
import me.oueslati.fakher.flickrapp.util.FlickrJsonUtil;


public class ImageSlider extends FragmentActivity {

    public static Photo[] photos;
    public static int currentImagePosition;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;

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
            Bundle bundle = getArguments();
            int position = bundle.getInt("position");
            String imageURL = photos[position].getPhotoURL();
            Glide.with(this)
                    .load(imageURL)
                    .crossFade()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            return swipeView;
        }
    }
}
