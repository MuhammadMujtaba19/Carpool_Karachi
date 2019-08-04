package com.example.carpoolkarachi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class addRide extends AppCompatActivity {

//    EditText ;

    TextView Date, Time;
    Button AddRideButton;
    AutoCompleteAdapter adapter,adapter2;
    AutoCompleteTextView Source,Destination;
    PlacesClient placesClient1,placesClient2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ride);

        Source = (AutoCompleteTextView) findViewById(R.id.rideSource);
//        abc = (PlacesAutocompleteTextView) findViewById(R.id.rideSource);
        Destination = (AutoCompleteTextView) findViewById(R.id.rideDestination);
        Date = (TextView) findViewById(R.id.rideDate);
        Time = (TextView) findViewById(R.id.rideTime);
        AddRideButton = (Button) findViewById(R.id.AddRideButton);


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyD8dRpnz6dp37FJ6FVZEbF4iTS9s_-_5sg");
        }

        placesClient1 = Places.createClient(this);
        placesClient2 = Places.createClient(this);

        initAutoCompleteTextView();

//        Source.setAdapter(mPlaceAutocompleteAdapter);
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
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                Date.setText(sdf.format(myCalendar.getTime()));
            }
        };

        Date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(addRide.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        Time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(addRide.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        AddRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = FirebaseAuth.getInstance().getUid();
                Toast.makeText(addRide.this, "hello" + id, Toast.LENGTH_SHORT).show();
            }
        });
    }
//       Source.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               try {
//                   Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                           .build(addRide.this);
//                   startActivityForResult(intent, 1);
//                   Toast.makeText(addRide.this, "Try block", Toast.LENGTH_SHORT).show();
//               } catch (GooglePlayServicesRepairableException e) {
//                   Toast.makeText(addRide.this, "catch1", Toast.LENGTH_SHORT).show();
//
//                   e.printStackTrace();
//               } catch (GooglePlayServicesNotAvailableException e) {
//                   Toast.makeText(addRide.this, "catch2", Toast.LENGTH_SHORT).show();
//
//                   e.printStackTrace();
//               }
//
//           }
//       });
//    }
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//            if (requestCode == 1) {
//                if (resultCode == RESULT_OK) {
//                    Place place = PlaceAutocomplete.getPlace(this, data);
//                    Log.i("TAG", "Place: " + place.getName());
//                    Toast.makeText(addRide.this, "success", Toast.LENGTH_SHORT).show();
//
//                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                    Status status = PlaceAutocomplete.getStatus(this, data);
//                    // TODO: Handle the error.
//
//                } else if (resultCode == RESULT_CANCELED) {
//                    // The user canceled the operation.
//                }
//            }
//
        private void initAutoCompleteTextView() {

            Source.setThreshold(1);
            Source.setOnItemClickListener(autocompleteClickListener);
            adapter = new AutoCompleteAdapter(this, placesClient1);
            Source.setAdapter(adapter);

            Destination.setThreshold(1);
            Destination.setOnItemClickListener(autocompleteClickListener);
            adapter2 = new AutoCompleteAdapter(this, placesClient2);
            Destination.setAdapter(adapter2);




        }

    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    final AutocompletePrediction item = adapter.getItem(i);
                    String placeID = null;
                    if (item != null) {
                        placeID = item.getPlaceId();
                    }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                    List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

                    FetchPlaceRequest request = null;
                    if (placeID != null) {
                        request = FetchPlaceRequest.builder(placeID, placeFields)
                                .build();
                    }

                    if (request != null) {
                        placesClient1.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSuccess(FetchPlaceResponse task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

}

