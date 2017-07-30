package company.unknown.eventually;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.SystemClock;
import java.util.Date;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import io.rapid.Rapid;
import io.rapid.RapidDocumentReference;

public class AddEventActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mDate;
    private EditText mTime;
    private PendingIntent pIntent;
    private Place place;

    int PLACE_PICKER_REQUEST = 1;
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mName = (EditText) findViewById(R.id.eventField);
        mDate = (EditText) findViewById(R.id.dateField);
        mTime = (EditText) findViewById(R.id.timeField);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void chooseLocation(View view){
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void eventAdded(View view){
//        if(! isValid()){
//            Toast.makeText(this,"Please input a valid date and time",Toast.LENGTH_SHORT).show();
//            return;
//        }
        String datetimeLong = dateAndTimeParser(mTime.getText(),mDate.getText());
        // TODO add item
        // Add location object to the locations collection
        RapidDocumentReference<LocationsEntity> newLocation = Rapid.getInstance().collection("locations", LocationsEntity.class).newDocument();
        newLocation.mutate(new LocationsEntity(newLocation.getId(), mName.getText().toString(), place.getName().toString(), (float)place.getLatLng().latitude, (float)place.getLatLng().longitude));

        // Add event object to the events collection
        RapidDocumentReference<EventsEntity> newEvent = Rapid.getInstance().collection("events", EventsEntity.class).newDocument();
        newEvent.mutate(new EventsEntity(newEvent.getId(), mName.getText().toString(), null, newLocation.getId(), datetimeLong));

//
        EventsEntity event = new EventsEntity(newEvent.getId(), mName.getText().toString(), null, newLocation.getId(), datetimeLong); //TODO: fetchEvent
        //TODO: args are mName.getText(), {} new LocationsEntity(id, place.getName(),place.getLatLng().latitude, place.getLatLng().longitude),datetimeLong)
        AlarmManager manager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent timeTrackerIntent = new Intent(this, LocationTracking.class);
        timeTrackerIntent.putExtra("Event ID", event.id);
        pIntent = PendingIntent.getService(this, 0, timeTrackerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //manager.set(AlarmManager.RTC_WAKEUP, Long.parseLong(event.date) - 30*60*1000, pIntent);
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3 * 1000, pIntent);

        //TODO UPDATE COLLECTION

        Intent intent = new Intent(this, MainEventsActivity.class);
        startActivity(intent);
    }

    private boolean isValid(){
        Editable date = mDate.getText();
        Editable time = mTime.getText();
        return (isDateValid(date) && isTimeValid(time));
    }

    /**
     * Standardised format of date to be US version (month-date-year)
     * Assume a variety of date formats (month can be 1 or 2 digits, day can be 1 or 2 digits,
     * year can be 2 or 4 digits, separated by '/','.','-', or no separation)
     * @param seq
     * @return
     */
    private static boolean isDateValid(CharSequence seq){
        int length = seq.length();
        if(length<4 || length > 10){
            return false;
        }
        CharSequence month;
        CharSequence day;
        CharSequence year;
        int mth;
        int days;
        int yrs;
        if(charsequenceContains('/',seq)||charsequenceContains('.',seq)||charsequenceContains('-',seq)){
            int connectors = noOfInstances('/',seq) + noOfInstances('.',seq) + noOfInstances('-',seq);
            if(connectors!=2){
                return false;
            }
            if(seq.charAt(1) == '/' || seq.charAt(1) == '.' || seq.charAt(1) == '-'){
                if(seq.charAt(3) == '/' || seq.charAt(3) == '.' || seq.charAt(3) == '-'){
                    month = seq.subSequence(0,1);
                    mth =Integer.parseInt(month.toString());
                    day = seq.subSequence(2,3);
                    days = Integer.parseInt(day.toString());
                    if(length==6){
                        year = seq.subSequence(4,6);
                    }
                    else if(length==8){
                        year = seq.subSequence(4,8);
                    }
                    else {
                        return false;
                    }
                    yrs =Integer.parseInt(year.toString());
                }
                else if(seq.charAt(4) == '/' || seq.charAt(4) == '.' || seq.charAt(4) == '-'){
                    month = seq.subSequence(0,1);
                    mth =Integer.parseInt(month.toString());
                    day = seq.subSequence(2,4);
                    days = Integer.parseInt(day.toString());
                    if(length==7){
                        year = seq.subSequence(5,7);
                    }
                    else if(length==9){
                        year = seq.subSequence(5,9);
                    }
                    else {
                        return false;
                    }
                    yrs =Integer.parseInt(year.toString());
                }
                else{
                    return false;
                }
            }
            else if(seq.charAt(2) == '/' || seq.charAt(2) == '.' || seq.charAt(2) == '-'){
                if(seq.charAt(4) == '/' || seq.charAt(4) == '.' || seq.charAt(4) == '-'){
                    month = seq.subSequence(0,2);
                    mth =Integer.parseInt(month.toString());
                    day = seq.subSequence(3,4);
                    days = Integer.parseInt(day.toString());
                    if(length==7){
                        year = seq.subSequence(5,7);
                    }
                    else if(length==9){
                        year = seq.subSequence(5,9);
                    }
                    else {
                        return false;
                    }
                    yrs =Integer.parseInt(year.toString());
                }
                else if(seq.charAt(5) == '/' || seq.charAt(5) == '.' || seq.charAt(5) == '-'){
                    month = seq.subSequence(0,2);
                    mth =Integer.parseInt(month.toString());
                    day = seq.subSequence(3,4);
                    days = Integer.parseInt(day.toString());
                    if(length==8){
                        year = seq.subSequence(6,8);
                    }
                    else if(length==10){
                        year = seq.subSequence(6,10);
                    }
                    else {
                        return false;
                    }
                    yrs =Integer.parseInt(year.toString());
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else {
            switch (length){
                case 4:
                    month = seq.subSequence(0,1);
                    mth =Integer.parseInt(month.toString());
                    day = seq.subSequence(1,2);
                    days = Integer.parseInt(day.toString());
                    year = seq.subSequence(2,4);
                    yrs =Integer.parseInt(year.toString());
                    break;
                case 6:
                    month = seq.subSequence(0,2);
                    mth =Integer.parseInt(month.toString());
                    day = seq.subSequence(2,4);
                    days = Integer.parseInt(day.toString());
                    year = seq.subSequence(4,6);
                    yrs =Integer.parseInt(year.toString());
                    break;
                case 8:
                    month = seq.subSequence(0,2);
                    mth =Integer.parseInt(month.toString());
                    day = seq.subSequence(2,4);
                    days = Integer.parseInt(day.toString());
                    year = seq.subSequence(4,8);
                    yrs =Integer.parseInt(year.toString());
                    break;
                default:
                    return false;
            }
        }
        if(mth < 1 || mth > 12 || days <1 || days > 31 || yrs < 2017 || yrs > 2019){
            return false;
        }
        switch (mth){
            case 2:
                if(yrs % 4 == 0){
                    return (days < 29);
                }
                return (days<28);
            case 4:
                return (days<28);
            case 6:
                return (days<28);
            case 9:
                return (days<28);
            case 11:
                return (days<28);
            default:
                break;
        }
        return true;
    }

    /**
     *
     * @param time
     * @param date
     * @return string value of time since 1 jan 1970 in milliseconds
     */
    private String dateAndTimeParser(CharSequence time, CharSequence date){
        int totaltime = 0;
        int timeLen = time.length();
        CharSequence hour;
        CharSequence minutes;
        int hrs = 0;
        int mins = 0;
        if(timeLen == 4) {
            hour = time.subSequence(0,2);
            hrs =Integer.parseInt(hour.toString());
            minutes = time.subSequence(2,4);
            mins = Integer.parseInt(minutes.toString());
        }
        else if(timeLen == 5){
            hour = time.subSequence(0,2);
            hrs =Integer.parseInt(hour.toString());
            minutes = time.subSequence(3,5);
            mins = Integer.parseInt(minutes.toString());
        }
        totaltime = 60000*(mins +60*hrs);

        final int TIME_IN_A_DAY_IN_MS = 60000*(24*60);
        int noDays = 0;
        int dateLen = date.length();

        CharSequence month;
        CharSequence day;
        CharSequence year = null;
        int mth = 0;
        int days = 0;
        int yrs = 0;

        if(charsequenceContains('/',date)||charsequenceContains('.',date)||charsequenceContains('-',date)){
            int connectors = noOfInstances('/',date) + noOfInstances('.',date) + noOfInstances('-',date);
            if(date.charAt(1) == '/' || date.charAt(1) == '.' || date.charAt(1) == '-'){
                if(date.charAt(3) == '/' || date.charAt(3) == '.' || date.charAt(3) == '-'){
                    month = date.subSequence(0,1);
                    mth =Integer.parseInt(month.toString());
                    day = date.subSequence(2,3);
                    days = Integer.parseInt(day.toString());
                    if(dateLen==6){
                        year = date.subSequence(4,6);
                    }
                    else if(dateLen==8){
                        year = date.subSequence(4,8);
                    }
                    yrs =Integer.parseInt(year.toString());
                }
                else if(date.charAt(4) == '/' || date.charAt(4) == '.' || date.charAt(4) == '-'){
                    month = date.subSequence(0,1);
                    mth =Integer.parseInt(month.toString());
                    day = date.subSequence(2,4);
                    days = Integer.parseInt(day.toString());
                    if(dateLen==7){
                        year = date.subSequence(5,7);
                    }
                    else if(dateLen==9){
                        year = date.subSequence(5,9);
                    }
                    yrs =Integer.parseInt(year.toString());
                }
            }
            else if(date.charAt(2) == '/' || date.charAt(2) == '.' || date.charAt(2) == '-'){
                if(date.charAt(4) == '/' || date.charAt(4) == '.' || date.charAt(4) == '-'){
                    month = date.subSequence(0,2);
                    mth =Integer.parseInt(month.toString());
                    day = date.subSequence(3,4);
                    days = Integer.parseInt(day.toString());
                    if(dateLen==7){
                        year = date.subSequence(5,7);
                    }
                    else if(dateLen==9){
                        year = date.subSequence(5,9);
                    }
                    yrs =Integer.parseInt(year.toString());
                }
                else if(date.charAt(5) == '/' || date.charAt(5) == '.' || date.charAt(5) == '-'){
                    month = date.subSequence(0,2);
                    mth =Integer.parseInt(month.toString());
                    day = date.subSequence(3,4);
                    days = Integer.parseInt(day.toString());
                    if(dateLen==8){
                        year = date.subSequence(6,8);
                    }
                    else if(dateLen==10){
                        year = date.subSequence(6,10);
                    }
                    yrs =Integer.parseInt(year.toString());
                }
            }
        }
        else {
            switch (dateLen){
                case 4:
                    month = date.subSequence(0,1);
                    mth =Integer.parseInt(month.toString());
                    day = date.subSequence(1,2);
                    days = Integer.parseInt(day.toString());
                    year = date.subSequence(2,4);
                    yrs =Integer.parseInt(year.toString());
                    break;
                case 6:
                    month = date.subSequence(0,2);
                    mth =Integer.parseInt(month.toString());
                    day = date.subSequence(2,4);
                    days = Integer.parseInt(day.toString());
                    year = date.subSequence(4,6);
                    yrs =Integer.parseInt(year.toString());
                    break;
                case 8:
                    month = date.subSequence(0,2);
                    mth =Integer.parseInt(month.toString());
                    day = date.subSequence(2,4);
                    days = Integer.parseInt(day.toString());
                    year = date.subSequence(4,8);
                    yrs =Integer.parseInt(year.toString());
                    break;
            }
        }

        int currYrCalc = 1970;
        while(currYrCalc<yrs){
            noDays = noDays+365;
            if(currYrCalc % 4 == 0){
                noDays++;
            }
            currYrCalc++;
        }
        switch (mth){
            case 1:
                noDays = noDays + days;
                break;
            case 2:
                noDays = 31 + days;
                break;
            case 3:
                noDays = 31 + 28 + days;
                if(yrs % 4 == 0){
                    noDays++;
                }
                break;
            case 4:
                noDays = 31*2 + 28 + days;
                if(yrs % 4 == 0){
                    noDays++;
                }
                break;
            case 5:
                noDays = 31*2 + 30 + 28 + days;
                if(yrs % 4 == 0){
                    noDays++;
                }
                break;
            case 6:
                noDays = 31*3 + 30 + 28 + days;
                if(yrs % 4 == 0){
                    noDays++;
                }
                break;
            case 7:
                noDays = 31*3 + 30*2 + 28 + days;
                if(yrs % 4 == 0){
                    noDays++;
                }
                break;
            case 8:
                noDays = 31*4 + 30*2 + 28 + days;
                if(yrs % 4 == 0){
                    noDays++;
                }
                break;
            case 9:
                noDays = 31*4 + 30*3 + 28 + days;
                if(yrs % 4 == 0){
                    noDays++;
                }
                break;
            case 10:
                noDays = 31*5 + 30*3 + 28 + days;
                if(yrs % 4 == 0){
                    noDays++;
                }
                break;
            case 11:
                noDays = 31*5 + 30*4 + 28 + days;
                if(yrs % 4 == 0){
                    noDays++;
                }
                break;
            case 12:
                noDays = 31*6 + 30*5 + 28 + days;
                if(yrs % 4 == 0){
                    noDays++;
                }
                break;
        }
        totaltime = totaltime + noDays*TIME_IN_A_DAY_IN_MS;
        return String.valueOf(totaltime);
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
