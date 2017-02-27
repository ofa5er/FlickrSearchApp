package me.oueslati.fakher.flickrapp.util;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Created by Fakher Oueslati on 2/26/2017.
 */
@RunWith(AndroidJUnit4.class)
public class FlickrNetworkUtilsInstrumentedTest {
    @Test
    public void buildURLWithPhotoSearchQuery_isCorrect() throws Exception {
        URL expectedURL =
                new URL("https://api.flickr.com/services/rest/?method=flickr.photos.search" +
                        "&api_key=b3380a67070b4cb848414a17c9b58433&" +
                        "text=keyword&per_page=100&" +
                        "page=1&" +
                        "format=json&" +
                        "nojsoncallback=1");

        URL receivedURL = FlickrNetworkUtils.buildURLWithPhotoSearchQuery("keyword", 100);
        assertEquals(receivedURL, expectedURL);
    }

    public void buildURLWithPhotoGetInfoQuery_isCorrect() throws Exception {
        URL expectedURL =
                new URL("https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&" +
                        "api_key=c5edaf134bb9c5806b38e2cee648c29b&" +
                        "photo_id=32139556723&secret=a0f385caae&format=json");

        URL receivedURL = FlickrNetworkUtils.buildURLWithPhotoGetInfoQuery(
                "32139556723", "a0f385caae");
        assertEquals(receivedURL, expectedURL);
    }

    public void buildURLWithPhotoGetLocationQuery_isCorrect() throws Exception {
        URL expectedURL =
                new URL("https://api.flickr.com/services/rest/?" +
                        "method=flickr.photos.geo.getLocation&" +
                        "api_key=c5edaf134bb9c5806b38e2cee648c29b&" +
                        "photo_id=32322296952&format=json&nojsoncallback=1");

        URL receivedURL = FlickrNetworkUtils.buildURLWithPhotoGetLocationQuery(
                "32322296952");
        assertEquals(receivedURL, expectedURL);
    }

}
