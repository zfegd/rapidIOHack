package company.unknown.eventually;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by Lorenzo on 7/29/17.
 */

public class LocationTracking extends IntentService {
    public LocationTracking() {
        super("LocationTracking");
        Log.e("LocationTracker","super called");

    }

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("LocationTracker","initiated");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Toast.makeText(getBaseContext(), location.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Log.e(null,"no location");
                }
            }
        });
    }
}
