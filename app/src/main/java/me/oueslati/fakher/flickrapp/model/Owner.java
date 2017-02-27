package me.oueslati.fakher.flickrapp.model;

/**
 * Created by Fakher Oueslati on 2/26/2017.
 */

public class Owner {
    private String realname;
    private String nsid;
    private String iconserver;
    private int iconfarm;

    public Owner(String realname, String nsid, String iconserver, int iconfarm) {
        this.realname = realname;
        this.nsid = nsid;
        this.iconserver = iconserver;
        this.iconfarm = iconfarm;
    }

    public String getRealname() {
        return realname;
    }

    public String getProfilePictureURL() {
        return "http://farm" + String.valueOf(iconfarm) + ".staticflickr.com/" +
                iconserver + "/buddyicons/" + nsid + ".jpg";
    }
}
