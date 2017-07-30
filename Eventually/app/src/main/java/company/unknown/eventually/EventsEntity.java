package company.unknown.eventually;

/**
 * Created by Wen Rui on 7/29/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class EventsEntity {


    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("userids")
    public String[] userids;

    @SerializedName("location")
    public LocationsEntity location;

    @SerializedName("date")
    public String date;


    public EventsEntity() {}


    public EventsEntity(String id, String name, String[] userids, LocationsEntity location, String date) {
        this.id = id;
        this.name = name;
        this.userids = userids;
        this.location = location;
        this.date = date;
    }

}
