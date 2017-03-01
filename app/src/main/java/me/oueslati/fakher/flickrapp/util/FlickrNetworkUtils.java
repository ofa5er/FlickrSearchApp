package me.oueslati.fakher.flickrapp.util;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Fakher Oueslati on 2/26/2017.
 */

public class FlickrNetworkUtils {
    private static final String TAG = FlickrNetworkUtils.class.getSimpleName();
    private static final String FLICKR_API_URL = "https://api.flickr.com/services/rest/";

    private static final String SEARCH_METHOD_PARAM = "method";
    private static final String API_KEY_PARAM = "api_key";
    private static final String SEARCH_TEXT_PARAM = "text";
    private static final String RESULT_PER_PAGE_PARAM = "per_page";
    private static final String RESULT_PAGE_PARAM = "page";
    private static final String FORMAT_PARAM = "format";
    private static final String NO_JSON_CALLBACK_PARAM = "nojsoncallback";
    private static final String FLCIKR_PHOTO_ID_PARAM = "photo_id";
    private static final String FLICKR_SECRET_PARAM = "secret";


    private static final String searchMethod = "flickr.photos.search";
    private static final String photoGetInfoMethod = "flickr.photos.getInfo";
    private static final String photoGeoGetLocationMethod = "flickr.photos.geo.getLocation";
    private static final String format = "json";
    private static final int resultPage = 1;
    private static final int nojsoncallback = 1;
    private static final String apiKey = "FLICKR_API_KEY";


    /**
     * Build an URL to search for photos using flickr api (flickr.photos.search).
     *
     * @param keyword            Keyword to use to look for photos.
     * @param resultPhotosNumber Number of photos to fetch for the result.
     * @return URL of the query or null if there is an error.
     */
    public static URL buildURLWithPhotoSearchQuery(String keyword, int resultPhotosNumber) {
        Uri searchQueryUri = Uri.parse(FLICKR_API_URL).buildUpon()
                .appendQueryParameter(SEARCH_METHOD_PARAM, searchMethod)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .appendQueryParameter(SEARCH_TEXT_PARAM, keyword)
                .appendQueryParameter(RESULT_PER_PAGE_PARAM, String.valueOf(resultPhotosNumber))
                .appendQueryParameter(RESULT_PAGE_PARAM, String.valueOf(resultPage))
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(NO_JSON_CALLBACK_PARAM, String.valueOf(nojsoncallback))
                .build();
        try {
            return new URL(searchQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Build an URL to perform a flickr.photo.get.info request.
     *
     * @param photoID ID of the photo.
     * @param secret  secret of the photo.
     * @return URL of the request or null if there is an error.
     */
    public static URL buildURLWithPhotoGetInfoQuery(String photoID, String secret) {
        Uri searchQueryUri = Uri.parse(FLICKR_API_URL).buildUpon()
                .appendQueryParameter(SEARCH_METHOD_PARAM, photoGetInfoMethod)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .appendQueryParameter(FLCIKR_PHOTO_ID_PARAM, photoID)
                .appendQueryParameter(FLICKR_SECRET_PARAM, secret)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(NO_JSON_CALLBACK_PARAM, String.valueOf(nojsoncallback))
                .build();
        try {
            return new URL(searchQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  Build an URL to perform a flickr.geo.get.location query.
     * @param photoID ID of the photo.
     * @return URL of the request or null if there is any error.
     */
    public static URL buildURLWithPhotoGetLocationQuery(String photoID) {
        Uri searchQueryUri = Uri.parse(FLICKR_API_URL).buildUpon()
                .appendQueryParameter(SEARCH_METHOD_PARAM, photoGeoGetLocationMethod)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .appendQueryParameter(FLCIKR_PHOTO_ID_PARAM, photoID)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(NO_JSON_CALLBACK_PARAM, String.valueOf(nojsoncallback))
                .build();
        try {
            return new URL(searchQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Perform a HTTP request using an URL and return the response in a String format.
     *
     * @param url the URL of the request.
     * @return Return the response of the HTTP request.
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        Log.v(TAG, "getResponseFromHttpUrl - URL :" + url.toString());
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            Log.v(TAG, "getResponseFromHttpUrl - Response :" + response);
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
