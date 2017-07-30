package company.unknown.eventually;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by Lorenzo on 7/29/17.
 */

public class LocationTracking extends IntentService {
    public LocationTracking() {
        super("LocationTracking");
    }

    private FusedLocationProviderClient mFusedLocationClient;

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
                } else {
                    Log.e(null,"no location");
                }
            }
        });
    }
}
