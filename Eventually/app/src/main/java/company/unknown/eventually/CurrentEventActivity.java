package company.unknown.eventually;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import io.rapid.Rapid;
import io.rapid.RapidCallback;
import io.rapid.RapidCollectionReference;
import io.rapid.RapidDocument;
import io.rapid.RapidDocumentReference;

public class CurrentEventActivity extends AppCompatActivity {

    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventID = getIntent().getStringExtra("Event ID");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RapidDocumentReference<EventsEntity> event = Rapid.getInstance().collection("events", EventsEntity.class).document(eventID);
        event.fetch(new RapidCallback.Document<EventsEntity>() {
            @Override
            public void onValueChanged(final RapidDocument<EventsEntity> document) {
                TextView textToChange = (TextView) findViewById(R.id.eventNameView);
                textToChange.setText(document.getBody().name);
            }
        });
    }

    public void gotoMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("Event ID", eventID);
        startActivity(intent);
    }

}
