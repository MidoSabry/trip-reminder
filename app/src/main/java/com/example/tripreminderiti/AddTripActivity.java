package com.example.tripreminderiti;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tripreminderiti.database.TripDatabase;
import com.example.tripreminderiti.database.TripDatabase_Impl;
import com.example.tripreminderiti.database.trip.Trip;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTripActivity extends AppCompatActivity {

    private static final String TAG = "Tag";
    private static final int REQ_CODE = 111;
    final Calendar myCalendar = Calendar.getInstance();
    private TextInputLayout startPointEd;
    private TextInputLayout endPointEd;
    private TextInputLayout nameEd;
    private TextInputLayout dateEd;
    private TextInputLayout timeEd;
    private Button btnAddNewTrip;
    private TripDatabase tripDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        startPointEd = findViewById(R.id.details_ed_startPoint);
        endPointEd = findViewById(R.id.details_ed_endPoint);
        nameEd = findViewById(R.id.details_ed_name);
        dateEd = findViewById(R.id.details_ed_Date);
        timeEd = findViewById(R.id.details_ed_Time);
        btnAddNewTrip = findViewById(R.id.add_new_trip_btn);
        tripDatabase = TripDatabase_Impl.getInstance(this);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        dateEd.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTripActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeEd.getEditText().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeEd.getEditText().setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        btnAddNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateError() == true) {
                    Trip trip = new Trip(nameEd.getEditText().getText().toString(), startPointEd.getEditText().getText().toString(), endPointEd.getEditText().getText().toString(), dateEd.getEditText().getText().toString(), timeEd.getEditText().getText().toString());
                    tripDatabase.tripDao().insertTrip(trip);
                }
            }
        });


    }




    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEd.getEditText().setText(sdf.format(myCalendar.getTime()));
    }




    //Mido
    //Validation
    private Boolean validateError() {
        String tNameVal = nameEd.getEditText().getText().toString();
        String spointVal = startPointEd.getEditText().getText().toString();
        String epointVal = endPointEd.getEditText().getText().toString();
        String dateVal = dateEd.getEditText().getText().toString();
        String timeVal = timeEd.getEditText().getText().toString();
        if(tNameVal.isEmpty() ){
            nameEd.setError("TripName Required");
            nameEd.requestFocus();
            return false;
        }
        else if(spointVal.isEmpty()) {
            startPointEd.setError("Start Point required");
            startPointEd.requestFocus();

            return false;
        }
        else if(epointVal.isEmpty()) {
            endPointEd.setError("End Point required");
            endPointEd.requestFocus();
            return false;
        }
        else if(dateVal.isEmpty()) {
            dateEd.setError("date reqquired");
            dateEd.requestFocus();
            return false;
        }
        else if(timeVal.isEmpty()) {
            timeEd.setError("Time required");
            timeEd.requestFocus();
            return false;
        }
        else {
            nameEd.setError(null);
            nameEd.setErrorEnabled(false);

            startPointEd.setError(null);
            startPointEd.setErrorEnabled(false);

            endPointEd.setError(null);
            endPointEd.setErrorEnabled(false);

            dateEd.setError(null);
            dateEd.setErrorEnabled(false);

            timeEd.setError(null);
            timeEd.setErrorEnabled(false);

            return true;
        }

    }


}






        /*
        startPointEd.setFocusable(false);


        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyBKi2ywXL9LzzlXUj27fckj2upkQmAgxjU");

        // Create a new PlacesClient instance
        // PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.


        startPointEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(AddTripActivity.this);
                startActivityForResult(intent, REQ_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            startPointEd.setText(place.getAddress());
            
        }else {
            Status status = Autocomplete.getStatusFromIntent(data);

            Toast.makeText(this, "error  "+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}*/