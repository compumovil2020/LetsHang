package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.letshang.R;
import com.example.letshang.ui.dialog.CustomMapView;
import com.example.letshang.ui.dialog.DatePickerFragment;
import com.example.letshang.ui.dialog.TimePickerFragment;
import com.example.letshang.utils.PermissionHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.common.collect.Range;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrearEventoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText etNombre, etLugar, etPrecio, etInicio, etFin, etTags, etDescription, etCapacidad;
    private RadioButton rbSport, rbSocial, rbMusical, rbGaming, rbAcademic;
    private Button btnCrear;
    private Date startDate, endDate;
    private ArrayList<String> tags;
    private ChipGroup chipGroup;
    private AwesomeValidation validation;
    private RadioGroup radioGroup;
    private CustomMapView mapView;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private GoogleMap map;
    private Geocoder mGeocoder;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    private FusedLocationProviderClient mFusedLocationClient;
    private String justificacion = "Se necesita el GPS para mostrar la ubicación del evento";
    private static final int LOCATION_PERMISSION_CODE = 101;
    private static final int REQUEST_CHECK_SETTINGS = 99;
    private LatLng currentLocation;
    private LatLng location;

    public static final double lowerLeftLatitude = 4.373941;
    public static final double lowerLeftLongitude= -74.347902;
    public static final double upperRightLatitude=  5.443858;
    public static final double upperRigthLongitude= -73.286441;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflar
        setContentView(R.layout.activity_crear_evento);

        mGeocoder = new Geocoder(getBaseContext());

        etNombre = findViewById(R.id.etNombreCrearEvento);
        etLugar = findViewById(R.id.etLugarCrearEvento);
        etPrecio = findViewById(R.id.etPrecioCrearEvento);
        etInicio = findViewById(R.id.etFechaInicioCrearEvento);
        etFin = findViewById(R.id.etFechaFinCrearEvento);
        etTags = findViewById(R.id.etTagsCrearEvento);
        etDescription = findViewById(R.id.editTextTextMultiLine);
        etCapacidad = findViewById(R.id.etCapacidadCrearEvento);
        radioGroup = findViewById(R.id.rgTipoEventoCrearEvento);

        rbSocial = findViewById(R.id.rbSocialCrearEvento);
        rbSport = findViewById(R.id.rbSportCrearEvento);
        rbMusical = findViewById(R.id.rbMusicCrearEvento);
        rbGaming = findViewById(R.id.rbGameCrearEvento);
        rbAcademic = findViewById(R.id.rbAcademicCrearEvento);

        chipGroup = (ChipGroup) findViewById(R.id.cgTagsCrearEvento);
        btnCrear = findViewById(R.id.btnCrearEvento);

        mapView = findViewById(R.id.mpMapCrearEvento);

        // inicializar
        startDate = new Date();
        endDate = new Date();
        tags = new ArrayList<>();
        validation = new AwesomeValidation(ValidationStyle.BASIC);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (map != null) {
                    if (sensorEvent.values[0] < 2500) {
                        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(CrearEventoActivity.this, R.raw.dark_style_map));
                    } else {
                        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(CrearEventoActivity.this, R.raw.day));
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        // set map
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                if(map != null){
                    map.addMarker(new MarkerOptions().position(currentLocation).title(geoCoderSearch(currentLocation)).snippet("Ubicación Actual").alpha(0.8f)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }

            }
        };



        if (ContextCompat.checkSelfPermission(CrearEventoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            pedirPermisos();
        }

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        // Form validation
        validation.addValidation(this, R.id.etNombreCrearEvento, RegexTemplate.NOT_EMPTY, R.string.nameerror);
        validation.addValidation(this, R.id.etPrecioCrearEvento, Range.open(0,999999), R.string.priceerror);
        validation.addValidation(this, R.id.editTextTextMultiLine, RegexTemplate.NOT_EMPTY, R.string.requirederror);
        validation.addValidation(this, R.id.etCapacidadCrearEvento, RegexTemplate.NOT_EMPTY, R.string.requirederror);

        // listeners
        etInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(true);
            }
        });


        etFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(false);
            }
        });

        etTags.setOnEditorActionListener(new EditText.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if(textView.getText().toString().isEmpty()) {
                    return false;
                }
                else{
                    addTag(textView.getText().toString());
                    textView.setText("");
                    return true;
                }
            }
        });


        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()){

                    Log.i("selected location", String.valueOf(location.latitude) + " "+String.valueOf(location.longitude));
                    Toast.makeText(getApplicationContext() , "texto: " + etLugar.getText().toString() , Toast.LENGTH_SHORT).show();
                    Intent i= new Intent();

                    if(radioGroup.getCheckedRadioButtonId() == rbAcademic.getId()){
                        i = new Intent(getApplicationContext() , CrearEventoActivity.class);

                    }else if(radioGroup.getCheckedRadioButtonId() == rbSport.getId()){
                        i = new Intent(getApplicationContext() , CreateSportEventActivity.class);

                    }
                    else if(radioGroup.getCheckedRadioButtonId() == rbSocial.getId()){
                        i = new Intent(getApplicationContext() , CreateSocialEventActivity.class);

                    }
                    else if(radioGroup.getCheckedRadioButtonId() == rbGaming.getId()){
                        i = new Intent(getApplicationContext() , CreateGameEventActivity.class);

                    }
                    else if(radioGroup.getCheckedRadioButtonId() == rbMusical.getId()){
                        i = new Intent(getApplicationContext() , CreateMusicEventActivity.class);

                    }

                    i.putExtra("name", etNombre.getText().toString());
                    i.putExtra("location", location);
                    i.putExtra("price" , etPrecio.getText().toString());
                    i.putExtra("startDate" , startDate);
                    i.putExtra("endDate" , endDate);
                    i.putExtra("tags" , tags);
                    i.putExtra("capacidad" , Integer.parseInt(etCapacidad.getText().toString()));
                    i.putExtra("description" , etDescription.getText().toString());

                    startActivity(i);
                }
            }
        });

    }

    private boolean validateInput(){

        if(etFin.getText().toString().isEmpty() || etInicio.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext() , "Selecciona las fechas para tu evento" , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(startDate.compareTo(endDate) > 0){
            Toast.makeText(getApplicationContext() , "La fecha inicial debe ser antes de la fecha final" , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(radioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext() , "Escoge el tipo de evento" , Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!validation.validate()){
            return false;
        }

        if(location == null){
            Toast.makeText(getApplicationContext() , "Seleccione la ubicación del evento" , Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * calls a fragment to choose date and time
     */
    private void showDatePickerDialog(final boolean isStart) {
        DatePickerFragment dateFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                String selectedDate = day + "/" + (month+1) + "/" + year + " ";
                Date date = new Date();

                if(isStart) {
                    startDate.setYear(year);
                    startDate.setMonth(month);
                    startDate.setDate(day);
                }
                else{
                    endDate.setYear(year);
                    endDate.setMonth(month);
                    endDate.setDate(day);
                }

                showTimePickerDialog( isStart, selectedDate);
            }
        });

        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog( final boolean isStart, final String selectedDate){
        TimePickerFragment timeFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i("onTimeSet", "StartDate: "+startDate.toString());
                if(isStart) {
                    Toast.makeText(getApplicationContext() , "estoy en true" , Toast.LENGTH_SHORT);
                    startDate.setHours(hour);
                    startDate.setMinutes(minute);
                    etInicio.setText(selectedDate +  String.valueOf(hour) + ":" + String.valueOf(minute));

                }
                else{
                    Toast.makeText(getApplicationContext() , "estoy en true" , Toast.LENGTH_SHORT);
                    endDate.setHours(hour);
                    endDate.setMinutes(minute);
                    etFin.setText(selectedDate +  String.valueOf(hour) + ":" + String.valueOf(minute));

                }

            }
        });
        timeFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void addTag(String tag){
        for(String s: tags){
            if(s.equals(tag)){
                return;
            }
        }
        tags.add(tag);
        Chip chip = new Chip(this);
        chip.setText(tag);
        chip.setOnCloseIconClickListener(new Chip.OnClickListener(){

            @Override
            public void onClick(View view) {
                Chip currentChip = (Chip) view;
                chipGroup.removeView(view);
                tags.remove(currentChip.getText().toString());
            }
        });
        chip.setCloseIconVisible(true);
        chipGroup.addView(chip);
    }


    private LatLng getLocationFromAddress(Context context, String inputtedAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng resLatLng = null;

        try {
            // May throw an IOException

            address = coder.getFromLocationName(inputtedAddress, 3,
                    lowerLeftLatitude, lowerLeftLongitude,
                    upperRightLatitude, upperRigthLongitude);
            if (address == null) {
                return null;
            }

            if (address.size() == 0) {


                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            Log.i("GetLocationFromAdress", "adress: " + location.toString());

            resLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return resLatLng;
    }

    private String geoCoderSearch(LatLng latlng){
        String address = "";
        try{
            List<Address> res = mGeocoder.getFromLocation(latlng.latitude, latlng.longitude, 2);
            if(res != null && res.size() > 0){
                address = res.get(0).getAddressLine(0);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return address;
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(80000); //tasa de refresco en milisegundos
        locationRequest.setFastestInterval(40000); //máxima tasa de refresco
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    private void pedirPermisos(){

        PermissionHandler.requestPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                justificacion,
                LOCATION_PERMISSION_CODE);

        if (ContextCompat.checkSelfPermission(CrearEventoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // si me dieron el permiso
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            SettingsClient client = LocationServices.getSettingsClient(this);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
            task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    startLocationUpdates();
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                location = latLng;
                map.clear();
                map.addMarker(new MarkerOptions().position(currentLocation).title(geoCoderSearch(currentLocation)).snippet("Ubicación Actual").alpha(0.8f)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                map.addMarker(new MarkerOptions().position(location).title(geoCoderSearch(latLng)).snippet("Ubicación del evento").alpha(0.8f).
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            pedirPermisos();
        } else {
            map.setMyLocationEnabled(true);
        }

        // initialize map
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(4.65, -74.05), 13));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this,
                            "Sin acceso a localización, hardware deshabilitado!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
        sensorManager.unregisterListener(lightSensorListener);
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        sensorManager.registerListener(lightSensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        startLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        stopLocationUpdates();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }




}