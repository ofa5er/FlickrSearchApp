package me.oueslati.fakher.flickrapp.util;

import org.junit.Test;

import me.oueslati.fakher.flickrapp.model.Photo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Fakher Oueslati on 2/26/2017.
 */

public class FlickrJsonInstrumentedTest {
    private final String photoSearchJsonData = "{ 'photos': { 'page': 1, 'pages': '168', " +
            "'perpage': 5, 'total': '837', 'photo': [ { 'id': '32322296952', " +
            "'owner': '151219264@N02', 'secret': '06fe042bf8', 'server': '476', 'farm': 1, " +
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

    private final String locationJsonData = "{ 'photo': { 'id': '32322296952','location': " +
            "{ 'latitude': 55.049152, 'longitude': 82.772777, 'accuracy': 16, 'context': 0, " +
            "'locality': { '_content': 'Marusino', 'place_id': 'jOIhMRJUVL6GfJ69Rg'," +
            " 'woeid': '56451360' }, 'county': { '_content': 'Novosibirskiy Raion', " +
            "'place_id': 'DPFbGHVQUL.vTLiRrw', 'woeid': '12598865' }, 'region': { '_content': " +
            "'Novosibirsk Oblast', 'place_id': 'NSuidCVTUb5AoaEE', 'woeid': '2346914' }, 'country':" +
            " { '_content': 'Russia', 'place_id': 'gMMKN7VTUb7Dg.7SoQ', 'woeid': '23424936' }, " +
            "'place_id': 'jOIhMRJUVL6GfJ69Rg', 'woeid': '56451360' } }, 'stat': 'ok' }";

    private final String photoInfoJsonData = "{'photo':{'id':'32139556723'," +
            "'secret':'a0f385caae','server':'2876','farm':3,'dateuploaded':'1487348602'," +
            "'isfavorite':0,'license':0,'safety_level':0,'rotation':0,'originalsecret':" +
            "'df73de88de','originalformat':'jpg','owner':{'nsid':'42415395@N07','username':" +
            "'deserttoad','realname':'Lou Feltz','location':'Albuquerque, NM, USA','iconserver':" +
            "'7446','iconfarm':8,'path_alias':'lvfeltz'},'title':{'_content':''},'description':" +
            "{'_content':''},'visibility':{'ispublic':1},'dates':{'posted':'1487348602'," +
            "'taken':'2017-01-31 01:54:49','takengranularity':0,'takenunknown':0,'lastupdate':" +
            "'1487533063'},'views':'130','editability':{'cancomment':0},'publiceditability':" +
            "{'cancomment':1},'usage':{'candownload':1},'comments':{'_content':0},'notes'" +
            ":{'note':[]},'people':{'haspeople':0},'urls':{'url':[{'type':'photopage'," +
            "'_content':''}]},'media':'photo'},'stat':'ok'}";

    @Test
    public void getImagesFromJson_isCorrect() throws Exception {
        Photo[] photo = FlickrJsonUtil.getPhotosFromJson(photoSearchJsonData);
        String expectedImageURL =
                "https://farm1.staticflickr.com/476/32322296952_06fe042bf8_h.jpg";

        String expectedThumbnailURL =
                "https://farm1.staticflickr.com/572/32126515200_a04e32e7fb_q.jpg";
        assertNotNull(photo);
        assertEquals(photo[0].getPhotoURL(), expectedImageURL);
        assertEquals(photo[4].getThumbnailURL(), expectedThumbnailURL);
    }

    @Test
    public void getPhotoLocationFromJson_isCorrect() throws Exception {
        Photo photo = new Photo();
        Photo result = FlickrJsonUtil.getPhotoLocationFromJson(photo, locationJsonData);
        String expectedLocation = "Russia";
        assertNotNull(result);
        assertEquals(result.getCountry(), expectedLocation);
    }

    @Test
    public void getPhotoInfoFromJson_isCorrect() throws Exception {
        Photo photo = new Photo();
        Photo result = FlickrJsonUtil.getPhotoInfoFromJson(photo, photoInfoJsonData);
        int expectedDate = 1487348602;
        String expectedRealname = "Lou Feltz";
        String expectedrofilePictureURL = "http://farm8.staticflickr.com/" +
                "7446/buddyicons/42415395@N07.jpg";
        assertNotNull(result);
        assertEquals(result.getPostedDate(), expectedDate);
        assertEquals(result.getOwner().getRealname(), expectedRealname);
        assertEquals(result.getOwner().getProfilePictureURL(), expectedrofilePictureURL);
    }
}
