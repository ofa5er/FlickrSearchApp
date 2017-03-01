package me.oueslati.fakher.flickrapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import me.oueslati.fakher.flickrapp.model.Photo;

/**
 * Created by Fakher Oueslati on 2/23/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private static final String TAG = ImageAdapter.class.getSimpleName();
    private Photo[] mImageListData;
    private Context mContext;
    private String flickrJsonResults;

    public void setFlickrJsonResults(String jsonStr) {
        flickrJsonResults = jsonStr;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutIdForListItem = R.layout.image_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        String imageTitle = mImageListData[position].getTitle();
        holder.mImageTitleItemTextView.setText(imageTitle);
        String ImageURL = mImageListData[position].getThumbnailURL();
        Glide.with(mContext)
                .load(ImageURL)
                .crossFade()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mImageViewItem);
        holder.mImageViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODOV change flickrJsonResults to parcelable
                Intent myIntent = new Intent(mContext, ImageSlider.class);
                myIntent.putExtra("flickr_search_json_results", flickrJsonResults);
                myIntent.putExtra("image_current_position", position);
                mContext.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mImageListData) return 0;
        return mImageListData.length;
    }

    public void setImageListData(Photo[] imageListData) {
        mImageListData = imageListData;
        notifyDataSetChanged();
    }

    public void clear() {
        mImageListData = null;
        notifyDataSetChanged();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public final TextView mImageTitleItemTextView;
        public final ImageView mImageViewItem;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageTitleItemTextView = (TextView) itemView.findViewById(R.id.tv_image_title_item);
            mImageViewItem = (ImageView) itemView.findViewById(R.id.iv_image_item);
        }

    }

}
