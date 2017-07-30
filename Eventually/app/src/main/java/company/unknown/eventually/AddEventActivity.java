package company.unknown.eventually;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class AddEventActivity extends AppCompatActivity {

    private EditText mName = (EditText) findViewById(R.id.eventField);
    private EditText mDate = (EditText) findViewById(R.id.dateField);
    private EditText mTime = (EditText) findViewById(R.id.timeField);
    private EditText mLocation = (EditText) findViewById(R.id.locationField);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void eventAdded(View view){
        if(! isValid()){
            //TODO handle error message
        }
        //TODO UPDATE COLLECTION incl parse datetime to 1970 format
        Intent intent = new Intent(this, MainEventsActivity.class);
        startActivity(intent);
    }

    private boolean isValid(){
        //TODO check if each field is valid
        Editable date = mDate.getText();
        Editable time = mTime.getText();
        // TODO CHECK IF location is valid in maps
        return false;
    }

    private static boolean isDateValid(CharSequence seq){
        int length = seq.length();

    }

    /**
     * Assuming people only do times in two formats (16:00 or 1600)
     * @param seq is the timefield value
     * @return boolean of whether time is valid
     */
    private static boolean isTimeValid(CharSequence seq){
        int length = seq.length();
        if(length == 4) {
            if(charsequenceContains(':',seq)){
                return false;
            }
            CharSequence hour = seq.subSequence(0,2);
            int hrs =Integer.parseInt(hour.toString());
            CharSequence minutes = seq.subSequence(2,4);
            int mins = Integer.parseInt(minutes.toString());
            return (hrs<24 && mins <60);
        }
        else if (length == 5){
            if(seq.charAt(2) == ':' && noOfInstances(':',seq)==1){
                CharSequence hour = seq.subSequence(0,2);
                int hrs =Integer.parseInt(hour.toString());
                CharSequence minutes = seq.subSequence(3,5);
                int mins = Integer.parseInt(minutes.toString());
                return (hrs<24 && mins <60);
            }
        }
        return false;
    }

    private static boolean charsequenceContains(char c, CharSequence seq){
        int len = seq.length();
        for(int i=0; i<len; i++){
            if(seq.charAt(i) == c){
                return true;
            }
        }
        return false;
    }

    /*
    * Returns an array with the no of instances at index 0, and indexes of instances
    * */
    private static int noOfInstances(char c, CharSequence seq){
        int len = seq.length();
//        int[] carryArray = new int[len];
        int noInstances = 0;
        for(int i=0; i<len; i++){
            if(seq.charAt(i) == c){
 //               carryArray[noInstances] = i;
                noInstances++;
            }
        }
//        int[] instances = new int[noInstances+1];
//        instances[0] = noInstances;
//        for(int j=0; j<noInstances; j++){
//            instances[j+1] = carryArray[j];
//        }
//        return instances;
        return noInstances;
    }

}
