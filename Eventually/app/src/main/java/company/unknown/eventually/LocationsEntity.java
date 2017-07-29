package company.unknown.eventually;

/**
 * Created by Wen Rui on 7/29/2017.
 */

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;


public class LocationsEntity extends BaseObservable {


    @SerializedName("mapid")
    public String mapid;

    @SerializedName("userid")
    public String userid;

    @SerializedName("lat")
    private Float lat;

    @SerializedName("lon")
    private Float lon;


    public LocationsEntity() {}


    public LocationsEntity(String mapid, String userid, Float lat, Float lon) {
        this.mapid = mapid;
        this.userid = userid;
        this.lat = lat;
        this.lon = lon;
    }
}

