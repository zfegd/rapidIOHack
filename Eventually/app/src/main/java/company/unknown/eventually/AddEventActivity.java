package company.unknown.eventually;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Date;

public class AddEventActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private PendingIntent pIntent;

    public void eventAdded(View view){
        Context context = this;
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent timeTrackerIntent = new Intent(context, LocationTracking.class);
        pIntent = PendingIntent.getBroadcast(context, 0, timeTrackerIntent, 0);

        EventsEntity event = new EventsEntity("", "", null, new LocationsEntity("","",0f,0f)); //TODO: fetchEvent
        manager.set(AlarmManager.RTC_WAKEUP, Long.parseLong(event.date) - 30*60*1000, pIntent);

        //TODO UPDATE COLLECTION
        Intent intent = new Intent(this, MainEventsActivity.class);
        startActivity(intent);
    }

}
