package me.oueslati.fakher.flickrapp.model;


import android.util.Log;

/**
 * Created by Fakher Oueslati on 2/26/2017.
 */

public class Photo {
    private static final String TAG = Photo.class.getSimpleName();
    private String owner;
    private String secret;
    private String id;
    private String farm;
    private String serverId;
    private String title;

    public Photo(String owner, String secret, String id, String farm, String serverId, String title) {
        this.owner = owner;
        this.secret = secret;
        this.id = id;
        this.farm = farm;
        this.serverId = serverId;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailURL() {
        String thumbURL = getURL("q");
        Log.v(TAG, "Thumbnail URL: " + thumbURL);
        return thumbURL;
    }

    public String getPhotoURL() {
        String imageURL = getURL("h");
        Log.v(TAG, "Image URL: " + imageURL);
        return imageURL;
    }

    private String getURL(String size) {
        return "https://farm" + farm + ".staticflickr.com/" +
                serverId + "/" + id + "_" + secret + "_" + size + ".jpg";
    }
}
