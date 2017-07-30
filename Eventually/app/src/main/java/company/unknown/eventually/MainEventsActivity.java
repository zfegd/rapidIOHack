package company.unknown.eventually;

import android.*;
import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import io.rapid.ListUpdate;
import io.rapid.Rapid;
import io.rapid.RapidCallback;
import io.rapid.RapidCollectionReference;
import io.rapid.RapidDocument;


public class MainEventsActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 205;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_GET_ACCOUNTS = 206;
    private List<EventsEntity> events;
    private RecyclerView rv;

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
             //   Log.e("Does it fail here?","Does it fail here?");
            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.GET_ACCOUNTS},
                    MY_PERMISSIONS_REQUEST_ACCESS_GET_ACCOUNTS);
        }

        rv = (RecyclerView)findViewById(R.id.eventsListView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        initializeData();
        initializeAdapter();

        //Following template below, can edit to meet our needs

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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_ACCESS_GET_ACCOUNTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.i("Accounts", "Account permissions granted");
                        Log.i("AccountFound", getUserEmail());
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.e("Accounts", "Account permissions not granted");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    public String getUserEmail() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                return possibleEmail;
            }
        }
        return "";
    }

    private void initializeData() {
        events = new ArrayList<>();
        events.add(new EventsEntity("1","Soccer",null,"name","1000000"));
        events.add(new EventsEntity("2","Swimming",null,"21","1003000"));
        events.add(new EventsEntity("3","Soccer",null,"22","21000000"));
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(events);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_events, menu);
        return true;
    }

    public void goToThatEvent(View view){
        Intent intent = new Intent(this, CurrentEventActivity.class);
        //TODO Test
        intent.putExtra("Event ID", R.id.theid);
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
