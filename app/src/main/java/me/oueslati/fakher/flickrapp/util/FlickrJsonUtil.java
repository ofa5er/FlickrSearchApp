package me.oueslati.fakher.flickrapp.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.oueslati.fakher.flickrapp.model.Owner;
import me.oueslati.fakher.flickrapp.model.Photo;

/**
 * Created by Fakher Oueslati on 2/26/2017.
 */


public class FlickrJsonUtil {
    private static final String TAG = FlickrJsonUtil.class.getSimpleName();

    private static final String FLICKR_PHOTOS = "photos";
    private static final String FLICKR_IMAGE_LIST = "photo";

    private static final String FLICKR_PHOTO_LOCATION = "location";
    private static final String FLICKR_PHOTO_COUNTRY = "country";
    private static final String FLICKR_PHOTO_CONTENT = "_content";

    private static final String FLICKR_PHOTO_OWNER_REALNAME = "realname";
    private static final String FLICKR_PHOTO_OWNER_ICONSERVER = "iconserver";
    private static final String FLICKR_PHOTO_OWNER_ICONFARM = "iconfarm";
    private static final String FLICKR_PHOTO_OWNER_NSID = "nsid";

    private static final String FLICKR_PHOTO_DATES = "dates";
    private static final String FLICKR_PHOTO_DATES_POSTED = "posted";

    private static final String FLICKR_ERROR_CODE = "code";
    private static final String FLICKR_STAT = "stat";
    private static final String FLICKR_STAT_FAIL = "fail";
    private static final String FLICKR_ERROR_MESSAGE = "message";

    private static final String FLICKR_OWNER = "owner";
    private static final String FLICKR_SECRET = "secret";
    private static final String FLICKR_ID = "id";
    private static final String FLICKR_FARM = "farm";
    private static final String FLICKR_SERVER_ID = "server";
    private static final String FLICKR_TITLE = "title";

    /**
     * Parse the result of flickr.photos.search request and return an array of photos
     *
     * @param photoJsonStr Result from the flickr.photos.search request
     * @return An array of photos or null if there is an error
     * @throws JSONException JSON parsing exception
     */
    public static Photo[] getPhotosFromJson(String photoJsonStr) throws JSONException {
        Log.v(TAG, photoJsonStr);
        JSONObject photoJson = new JSONObject(photoJsonStr);

        /* Is there an error ?*/
        if (photoJson.has(FLICKR_STAT)) {
            String status = photoJson.getString(FLICKR_STAT);
            if (status.equals(FLICKR_STAT_FAIL)) {
                if (photoJson.has(FLICKR_ERROR_MESSAGE)) {
                    String errorMessage = photoJson.getString(FLICKR_ERROR_MESSAGE);
                    Log.e(TAG, "Failed Request: " + errorMessage);
                } else {
                    Log.e(TAG, "Failed Request: Unknown Reason");
                }
                return null;
            }
        }

        JSONArray photoArray =
                photoJson.getJSONObject(FLICKR_PHOTOS).getJSONArray(FLICKR_IMAGE_LIST);

        Photo[] result = new Photo[photoArray.length()];

        for (int i = 0; i < photoArray.length(); i++) {
            String owner, secret, id, farm, serverId, title;
            JSONObject photo = photoArray.getJSONObject(i);
            owner = photo.getString(FLICKR_OWNER);
            secret = photo.getString(FLICKR_SECRET);
            id = photo.getString(FLICKR_ID);
            farm = photo.getString(FLICKR_FARM);
            serverId = photo.getString(FLICKR_SERVER_ID);
            title = photo.getString(FLICKR_TITLE);
            result[i] = new Photo(owner, secret, id, farm, serverId, title);
        }
        return result;
    }

    public static Photo getPhotoLocationFromJson(Photo photo, String jsonStr) throws JSONException {
        Log.v(TAG, "getPhotoLocationFromJson - LocationJsonStr:" + jsonStr);
        JSONObject jsonObject = new JSONObject(jsonStr);
                /* Is there an error ?*/
        if (jsonObject.has(FLICKR_STAT)) {
            String status = jsonObject.getString(FLICKR_STAT);
            if (status.equals(FLICKR_STAT_FAIL)) {
                if (jsonObject.has(FLICKR_ERROR_MESSAGE)) {
                    if (jsonObject.getInt(FLICKR_ERROR_MESSAGE) == 2) {
                        photo.setCountry("Unknown Location");
                        return photo;
                    }
                    String errorMessage = jsonObject.getString(FLICKR_ERROR_MESSAGE);
                    Log.e(TAG, "Failed Request: " + errorMessage);
                } else {
                    Log.e(TAG, "Failed Request: Unknown Reason");
                }
                return null;
            }
        }

        String country =
                jsonObject.getJSONObject(FLICKR_IMAGE_LIST)
                        .getJSONObject(FLICKR_PHOTO_LOCATION)
                        .getJSONObject(FLICKR_PHOTO_COUNTRY)
                        .getString(FLICKR_PHOTO_CONTENT);
        photo.setCountry(country);
        return photo;
    }


    public static Photo getPhotoInfoFromJson(Photo photo, String jsonStr) throws JSONException {
        Log.v(TAG, "getPhotoInfoFromJson - PhotoJsonStr:" + jsonStr);
        JSONObject jsonObject = new JSONObject(jsonStr);
                /* Is there an error ?*/
        if (jsonObject.has(FLICKR_STAT)) {
            String status = jsonObject.getString(FLICKR_STAT);
            if (status.equals(FLICKR_STAT_FAIL)) {
                if (jsonObject.has(FLICKR_ERROR_MESSAGE)) {
                    String errorMessage = jsonObject.getString(FLICKR_ERROR_MESSAGE);
                    Log.e(TAG, "Failed Request: " + errorMessage);
                } else {
                    Log.e(TAG, "Failed Request: Unknown Reason");
                }
                return null;
            }
        }

        int postedDate =
                jsonObject.getJSONObject(FLICKR_IMAGE_LIST)
                        .getJSONObject(FLICKR_PHOTO_DATES)
                        .getInt(FLICKR_PHOTO_DATES_POSTED);
        photo.setPostedDate(postedDate);

        JSONObject ownerJsonObject = jsonObject.getJSONObject(FLICKR_IMAGE_LIST)
                .getJSONObject(FLICKR_OWNER);

        String realname = ownerJsonObject.getString(FLICKR_PHOTO_OWNER_REALNAME);
        String nsid = ownerJsonObject.getString(FLICKR_PHOTO_OWNER_NSID);
        String iconserver = ownerJsonObject.getString(FLICKR_PHOTO_OWNER_ICONSERVER);
        int iconfarm = ownerJsonObject.getInt(FLICKR_PHOTO_OWNER_ICONFARM);

        Owner owner = new Owner(realname, nsid, iconserver, iconfarm);
        photo.setOwner(owner);

        return photo;
    }


}
