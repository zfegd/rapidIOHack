package company.unknown.eventually;

import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.List;

import io.rapid.ListUpdate;
import io.rapid.Rapid;
import io.rapid.RapidCallback;
import io.rapid.RapidCollectionReference;
import io.rapid.RapidDocument;
import io.rapid.RapidDocumentReference;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        RapidCollectionReference<EventsEntity> events = Rapid.getInstance().collection("events", EventsEntity.class);

        //might be the wrong datastring
        eventID = getIntent().getStringExtra("Event ID");
    }


    private RapidCallback.Document<EventsEntity> helperFunction(){
        return new RapidCallback.Document<EventsEntity>() {
            @Override
            public void onValueChanged(RapidDocument<EventsEntity> document) {
                document.getBody();
            }
        };
    }

    private RapidCallback.Document<LocationsEntity> locationHelper(){
        return new RapidCallback.Document<LocationsEntity>() {
            @Override
            public void onValueChanged(RapidDocument<LocationsEntity> document) {
                document.getBody();
            }
        };
    }

    private RapidCallback.Document<UsersEntity> userHelper(){
        return new RapidCallback.Document<UsersEntity>() {
            @Override
            public void onValueChanged(RapidDocument<UsersEntity> document) {
                document.getBody();
            }
        };
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String u[] = {};
        //EventsEntity event = new EventsEntity("", "", u, "", ""); //TODO: fetchEvent



        RapidCollectionReference<EventsEntity> eventinstance = Rapid.getInstance().collection("events", EventsEntity.class);
        //RapidDocumentReference<EventsEntity> event = eventinstance.document(eventID);
        RapidDocumentReference<EventsEntity> event = Rapid.getInstance().collection("events", EventsEntity.class).document(eventID);
        EventsEntity eventBody = event.fetch(helperFunction()).getDocument().getBody();

        String locationid = new String();

        locationid = eventBody.locationid;
        // Rapid.getInstance().collection("events", EventsEntity.class).document(eventID).fetch(helperFunction());

        // Add a marker at event location and move the camera
        //LatLng eventLocation = new LatLng(event.locationid.lat, event.locationid.lon);
        //TODO: Replace with the locationid coordinates
        RapidDocumentReference<LocationsEntity> location = Rapid.getInstance().collection("locations", LocationsEntity.class).document(locationid);
        LatLng eventLocation = new LatLng(location.fetch(locationHelper()).getDocument().getBody().lat, location.fetch(locationHelper()).getDocument().getBody().lon);
        mMap.addMarker(new MarkerOptions().position(eventLocation).title(eventBody.name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eventLocation));

        Date startThresh = new Date(Long.parseLong(eventBody.date + 30*60*1000));
        if (Calendar.getInstance().getTime().before(startThresh)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.event_early_dialog_message);
            builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            AlertDialog dialog = builder.create();
            WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

            wmlp.gravity = Gravity.TOP | Gravity.LEFT;
            wmlp.x = 100;   //x position
            wmlp.y = 100;   //y position
            wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.show();
        } else {
            for (String userId : eventBody.userids) {
                RapidDocumentReference<UsersEntity> users = Rapid.getInstance().collection("users", UsersEntity.class).document(userId);
                UsersEntity userBody = users.fetch(userHelper()).getDocument().getBody();
                RapidDocumentReference<LocationsEntity> locationDocument = Rapid.getInstance().collection("locations", LocationsEntity.class).document(userBody.locationid);
                LocationsEntity  locationBody = locationDocument.fetch(locationHelper()).getDocument().getBody();
                /*
                UsersEntity user = users.
                        new UsersEntity("","","", new LocationsEntity("","","",0f,0f)); // TODO: fetch userId
                */

                mMap.addMarker(new MarkerOptions().position(
                        new LatLng(locationBody.lat, locationBody.lon)).title(userBody.name));
            }
        }
    }


}
