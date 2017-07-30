package company.unknown.eventually;

/**
 * Created by Wen Rui on 7/29/2017.
 */

import com.google.gson.annotations.SerializedName;


public class LocationsEntity {


    @SerializedName("mapid")
    public String mapid;

    @SerializedName("userid")
    public String userid;

    @SerializedName("lat")
    public Float lat;

    @SerializedName("lon")
    public Float lon;


    public LocationsEntity() {}


    public LocationsEntity(String mapid, String userid, Float lat, Float lon) {
        this.mapid = mapid;
        this.userid = userid;
        this.lat = lat;
        this.lon = lon;
    }
}

