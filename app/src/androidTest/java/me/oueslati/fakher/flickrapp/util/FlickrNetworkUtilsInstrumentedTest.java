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
}
