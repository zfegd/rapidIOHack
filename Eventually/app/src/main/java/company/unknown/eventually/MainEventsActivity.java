package company.unknown.eventually;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.rapid.ListUpdate;
import io.rapid.Rapid;
import io.rapid.RapidCallback;
import io.rapid.RapidCollectionReference;
import io.rapid.RapidDocument;


public class MainEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainEventsActivity.this, AddEventActivity.class);
                startActivity(intent);
                Log.e("Does it fail here?","Does it fail here?");
            }
        });



        AlarmManager manager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent timeTrackerIntent = new Intent(this, LocationTracking.class);

        startService(timeTrackerIntent);

//        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, timeTrackerIntent, 0);
//
//        //manager.set(AlarmManager.RTC_WAKEUP, Long.parseLong(event.date) - 30*60*1000, pIntent);
//        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20, pIntent);

        Log.e("Test", String.valueOf(System.currentTimeMillis()));




        final LinkedList<EventsEntity> toprint = new LinkedList<>();

        RapidCollectionReference<EventsEntity> events = Rapid.getInstance().collection("events", EventsEntity.class);
        events.subscribeWithListUpdates(new RapidCallback.CollectionUpdates<EventsEntity>() {
            @Override
            public void onValueChanged(List<RapidDocument<EventsEntity>> rapidDocuments, ListUpdate listUpdate) {
                for (RapidDocument<EventsEntity> rapidDocument : rapidDocuments) {
                    toprint.addLast(new EventsEntity(rapidDocument.getBody().id, rapidDocument.getBody().name,
                            rapidDocument.getBody().userids, rapidDocument.getBody().locationid,rapidDocument.getBody().date));}
            }
        });
        
        // TODO transform linkedlist (toprint) to a readable format

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_events, menu);
        return true;
    }

    public void testMethod(View view){
        Intent intent = new Intent(this, CurrentEventActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
