package company.unknown.eventually;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
    }

    public void gotoMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("Event ID", eventID);
        startActivity(intent);
    }

}
