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

import java.util.ArrayList;
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        String u[] = {};
        //EventsEntity event = new EventsEntity("", "", u, "", ""); //TODO: fetchEvent

        final String locationid = "OsNCPc5URKSJwa5PsfnmHg";
        final LocationsEntity[] location = new LocationsEntity[1];

        RapidCollectionReference<EventsEntity> eventinstance = Rapid.getInstance().collection("events", EventsEntity.class);
        //RapidDocumentReference<EventsEntity> event = eventinstance.document(eventID);
        final RapidDocumentReference<EventsEntity> event = Rapid.getInstance().collection("events", EventsEntity.class).document(eventID);
        event.fetch(new RapidCallback.Document<EventsEntity>() {
            @Override
            public void onValueChanged(final RapidDocument<EventsEntity> document) {
                Rapid.getInstance()
                        .collection("locations", LocationsEntity.class)
                        .document(locationid)
                        .fetch(new RapidCallback.Document<LocationsEntity>() {
                            @Override
                            public void onValueChanged(RapidDocument<LocationsEntity> locationdocument) {
                                putEventIntoMap(googleMap, document, locationdocument);
                            }
                        });
            }
        });
    }

    private void putEventIntoMap(GoogleMap googleMap, final RapidDocument<EventsEntity> document, RapidDocument<LocationsEntity> locationdocument) {
        LocationsEntity location = locationdocument.getBody();
        LatLng eventLocation = new LatLng(location.lat, location.lon);
        mMap.addMarker(new MarkerOptions().position(eventLocation).title(locationdocument.getBody().name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eventLocation));
        Date startThresh = new Date(Long.parseLong(document.getBody().date + 30 * 60 * 1000));

        if (Calendar.getInstance().getTime().before(startThresh)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
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
            for (String useremail : document.getBody().userids) {
                Rapid.getInstance().collection("users", UsersEntity.class).equalTo("email", useremail)
                        .fetch(new RapidCallback.Collection<UsersEntity>() {
                            @Override
                            public void onValueChanged(List<RapidDocument<UsersEntity>> rapidDocuments) {
                                UsersEntity user = rapidDocuments.get(0).getBody();
                                mMap.addMarker(new MarkerOptions().position(
                                        new LatLng(user.location.lat, user.location.lon)).title(user.name));
                            }
                        });
            }
        }
    }
}