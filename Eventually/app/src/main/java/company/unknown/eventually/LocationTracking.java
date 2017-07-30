package company.unknown.eventually;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by Lorenzo on 7/29/17.
 */

public class LocationTracking extends IntentService {
    public LocationTracking() {
        super("LocationTracking");
    }

    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private String eventId;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Me", "init onCreate");
        mLocationRequest = new LocationRequest();
        createLocationRequest();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.i("me", locationResult.toString());
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    Log.e("location", location.toString());
                }
            }

            ;
        };
        Log.i("Me", "fin onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("Entered","Here");
        String thisID = intent.getStringExtra("Event ID");
        Log.v(thisID,thisID);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Log.i(null, location.toString());
                    mCurrentLocation = location;
                    LocationsEntity l = new LocationsEntity(eventId, "", "New location", (float) location.getLatitude(), (float) location.getLongitude());
                } else {
                    Log.e(null, "no location");
                }
            }
        });
        LocationRequest mLocationRequest = new LocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        Log.i("Me", "starting location updates");
        startLocationUpdates();
        Log.i("Me", "finished location updates");
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        // TODO: save state? https://developer.android.com/training/location/receive-location-updates.html#save-state
    }

    protected void createLocationRequest() {
        mLocationRequest.setInterval(60 * 1000);
        mLocationRequest.setFastestInterval(30 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Log.e("Me", "Permission error");
            return;
        }
        int i = 0;
        while (i < 5) { // change to check global user position?
            Log.i("Me", String.valueOf(i));

            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
            i++;
        }
    }

}
