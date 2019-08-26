package com.example.carpoolkarachi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.content.Intent;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class addRide extends AppCompatActivity {

//    EditText ;

    private TextView Date, Time,Source,Dest,Fare;
    private Button AddRideButton;
    private String SourceLocation,DestinationLocation,RideTime, RideFare,RideDate;
    private MarkerOptions SourcePosition,DestinationPosition;
    private LatLng SourceLatLng,DestLatLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_ride);
        initUI();



        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            public void updateLabel() {
                String myFormat = "dd-MMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

                Date.setText(sdf.format(myCalendar.getTime()));
            }
        };

        Date.setOnClickListener(v->{
                // TODO Auto-generated method stub
                new DatePickerDialog(addRide.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        );
        Time.setOnClickListener(v->{
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(addRide.this, (timePicker, selectedHour, selectedMinute) ->
                        Time.setText(selectedHour + ":" + selectedMinute), hour, minute, false);//Yes 24 hour time

            mTimePicker.setTitle("Select Time");
            mTimePicker.show();




            }
        );

        AddRideButton.setOnClickListener(v -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            RideDate = Date.getText().toString();
            RideTime = Time.getText().toString();
            RideInfo val  = new RideInfo(SourceLocation,DestinationLocation,RideDate,RideTime,Fare.getText().toString(),SourceLatLng,DestLatLng);
            String id = FirebaseAuth.getInstance().getUid();


            databaseReference.child("AvailableRides").child(id).setValue(val);
            Toast.makeText(this, "Added to Database", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(addRide.this,HomeActivity.class));
            finish();
        });

    }



    private void initUI() {
        Date   = findViewById(R.id.rideDate);
        Time   = findViewById(R.id.rideTime);
        Source = findViewById(R.id.rideSource);
        Dest   = findViewById(R.id.rideDestination);
        Fare   = findViewById(R.id.rideFare);
        AddRideButton = findViewById(R.id.AddRideButton);

        Bundle bundle = getIntent().getExtras();
        SourceLocation= bundle.getString("SourceLocation");
        Source.setText(SourceLocation);

        DestinationLocation= bundle.getString("DestinationLocation");
        Dest.setText(DestinationLocation);

        SourceLatLng=getIntent().getExtras().getParcelable("SourcePosition");
        DestLatLng=getIntent().getExtras().getParcelable("DestPosition");

//        SourcePosition = new MarkerOptions().position(SourceLatLng);
//        DestinationPosition = new MarkerOptions().position(DestLatLng);

    }

}