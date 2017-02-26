package me.oueslati.fakher.flickrapp.util;

import org.junit.Test;

import me.oueslati.fakher.flickrapp.model.Photo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Fakher Oueslati on 2/26/2017.
 */

public class FlickrJsonInstrumentedTest {
    private final String JsonData = "{ 'photos': { 'page': 1, 'pages': '168', 'perpage': 5," +
            " 'total': '837', 'photo': [ { 'id': '32322296952', 'owner': '151219264@N02'," +
            " 'secret': '06fe042bf8', 'server': '476', 'farm': 1, " +
            "'title': 'Russian Mobile Saunas Terma:  www.terma.camp', 'ispublic': 1, " +
            "'isfriend': 0, 'isfamily': 0 }, { 'id': '32126527300', 'owner': '96396586@N07'," +
            " 'secret': '8c231ebd5d', 'server': '311', 'farm': 1, " +
            "'title': 'Termas en Baelo Claudia', 'ispublic': 1, 'isfriend': 0, 'isfamily': 0 }, " +
            "{ 'id': '31661784824', 'owner': '96396586@N07', 'secret': '680734526e', " +
            "'server': '259', 'farm': 1, 'title': 'Termas en Baelo Claudia', 'ispublic': 1, " +
            "'isfriend': 0, 'isfamily': 0 }, { 'id': '31661780444', 'owner': '96396586@N07', " +
            "'secret': 'daa36c02d8', 'server': '752', 'farm': 1, " +
            "'title': 'Termas en Baelo Claudia', 'ispublic': 1, 'isfriend': 0, 'isfamily': 0 }," +
            " { 'id': '32126515200', 'owner': '96396586@N07', 'secret': 'a04e32e7fb', " +
            "'server': '572', 'farm': 1, 'title': 'Termas en Baelo Claudia', 'ispublic': 1, " +
            "'isfriend': 0, 'isfamily': 0 }] }, stat': 'ok' }";

    @Test
    public void getImagesFromJson_isCorrect() throws Exception {
        Photo[] photo = FlickrJsonUtil.getPhotosFromJson(JsonData);
        String expectedImageURL =
                "https://farm1.staticflickr.com/476/32322296952_06fe042bf8_h.jpg";

        String expectedThumbnailURL =
                "https://farm1.staticflickr.com/572/32126515200_a04e32e7fb_q.jpg";
        assertNotNull(photo);
        assertEquals(photo[0].getImageURL(), expectedImageURL);
        assertEquals(photo[4].getThumbnailURL(), expectedThumbnailURL);
    }
}
