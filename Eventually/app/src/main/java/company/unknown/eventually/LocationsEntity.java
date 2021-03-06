package company.unknown.eventually;

/**
 * Created by Wen Rui on 7/29/2017.
 */


import com.google.gson.annotations.SerializedName;


public class LocationsEntity {


    @SerializedName("mapid")
    public String mapid;

    //can be event or user name
    @SerializedName("name")
    public String name;

    @SerializedName("locationname")
    public String locationname;

    @SerializedName("lat")
    public Float lat;

    @SerializedName("lon")
    public Float lon;


    public LocationsEntity() {}

    //name can be either the user or event name
    public LocationsEntity(String mapid, String name, String locationname, Float lat, Float lon) {
        this.mapid = mapid;
        this.name = name;
        this.locationname = locationname;
        this.lat = lat;
        this.lon = lon;
    }
}

