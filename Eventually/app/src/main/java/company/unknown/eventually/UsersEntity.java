package company.unknown.eventually;

/**
 * Created by Wen Rui on 7/29/2017.
 */

import com.google.gson.annotations.SerializedName;


public class UsersEntity {


    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("email")
    private String email;

    @SerializedName("location")
    private LocationsEntity location;


    public UsersEntity() {}


    public UsersEntity(String id, String name, String email, LocationsEntity location) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.location = location;
    }

    /* Unused from Thomas's Skeleton Code - Wen Rui
    @Bindable
    public int getClicked() {
        return mClicked;
    }


    public void setClicked(int newValue) {
        mClicked = newValue;
        notifyPropertyChanged(BR.clicked);
    }
    */
}

