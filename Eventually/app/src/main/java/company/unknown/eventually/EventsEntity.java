package company.unknown.eventually;

/**
 * Created by Wen Rui on 7/29/2017.
 */

import com.google.gson.annotations.SerializedName;


public class EventsEntity {


    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("userids")
    public String[] userids;

    @SerializedName("location")
    private LocationsEntity location;

    @SerializedName("date")
    private String date;


    public EventsEntity() {}


    public EventsEntity(String id, String name, String[] userids, LocationsEntity location) {
        this.id = id;
        this.name = name;
        this.userids = userids;
        this.location = location;
    }
}

