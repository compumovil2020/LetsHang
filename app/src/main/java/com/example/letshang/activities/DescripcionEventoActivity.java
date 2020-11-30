package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.letshang.DTO.UserDTO;
import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.model.Participant;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;
import com.example.letshang.ui.dialog.CustomMapView;
import com.example.letshang.utils.PermissionHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DescripcionEventoActivity extends AppCompatActivity implements OnMapReadyCallback, RoutingListener, GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "DescripcionEvento";
    private String idEvento;
    private String fromActivity;
    private Event evento;
    private TextView tvHost;
    private Button btnDescripcion;
    private Button btnChatEvento;
    private TextView tvTituloEvento;
    private TextView tvLocationEvento;
    private TextView tvFechaEvento;
    private TextView tvTiempoEvento;
    private TextView tvPrecioEvento;
    private TextView tvDescripcionEvento;
    private ChipGroup chipGroup;

    private TextView tvCuidad;
    private TextView tvTemp;
    private TextView tvDescripcion;
    private ImageView ivClima;

    private CustomMapView mapView;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private LatLng currentLocation;
    private GoogleMap map;
    private Geocoder mGeocoder;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    private FusedLocationProviderClient mFusedLocationClient;
    private List<Polyline> route = null;
    private int LOCATION_PERMISSION_CODE = 101;
    private static final int REQUEST_CHECK_SETTINGS = 99;
    private String justificacion = "Se necesita el GPS para mostrar la ubicación del evento";
    private EventProvider evProv = EventProvider.getInsatance();
    private UserProvider usProv = UserProvider.getInstance();
    private Marker eventMarker;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UserDTO data;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private boolean initRoute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_evento);

        idEvento = getIntent().getStringExtra("idevento");
        fromActivity = getIntent().getStringExtra("from");

        initRoute = false;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        if(fromActivity.equalsIgnoreCase("Principal")){
            getSupportActionBar().setTitle("Inscribir evento");
        } else if (fromActivity.equalsIgnoreCase("Insritos")){
            getSupportActionBar().setTitle("Descripción del evento");
        }

        // inflar
        mGeocoder = new Geocoder(getBaseContext());
        tvHost = findViewById(R.id.tvHostDescripcionEvento);
        tvHost.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        btnDescripcion = findViewById(R.id.btnCancelarDescripcionEvento);
        tvTituloEvento = findViewById(R.id.tvTituloDescripcionEvento);
        tvLocationEvento = findViewById(R.id.tvLocationDescripcionEvento);
        tvFechaEvento = findViewById(R.id.tvFechaDescripcionEvento);
        tvTiempoEvento = findViewById(R.id.tvTiempoDescripcionEvento);
        tvPrecioEvento = findViewById(R.id.tvPrecioDescripcionEvento);
        tvDescripcionEvento = findViewById(R.id.tvResumenDescripcionEvento);
        chipGroup = (ChipGroup) findViewById(R.id.cgTagsDescripcionEvento);
        mapView = findViewById(R.id.mpMapDescripcionEvento);
        btnChatEvento = findViewById(R.id.btChatDescripcionEvento);

        tvCuidad = findViewById(R.id.tvCuidadDescripcion);
        tvCuidad.setText("");
        tvTemp = findViewById(R.id.tvTempDescripcion);
        tvTemp.setText("");
        tvDescripcion = findViewById(R.id.tvDescrDescripcion);
        tvDescripcion.setText("");
        ivClima = findViewById(R.id.ivClimaDescripcion);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (map != null) {
                    if (sensorEvent.values[0] < 2500) {
                        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(DescripcionEventoActivity.this, R.raw.dark_style_map));
                    } else {
                        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(DescripcionEventoActivity.this, R.raw.day));
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        initializeEvent();

        if(fromActivity.equalsIgnoreCase("Principal")){
            btnChatEvento.setVisibility(View.GONE);
            btnDescripcion.setText("Siguiente");
        } else if(fromActivity.equalsIgnoreCase("Inscritos")){
            btnDescripcion.setText("Cancelar evento");
        }

        // set map
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null && data != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    if(map != null){
                        map.addMarker(new MarkerOptions().position(eventMarker.getPosition()).title(geoCoderSearch(eventMarker.getPosition())).snippet("Ubicación del evento").alpha(0.8f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        map.addMarker(new MarkerOptions().position(currentLocation).title(geoCoderSearch(currentLocation)).snippet("Ubicación Actual").alpha(0.8f)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        showRoute(currentLocation.latitude, currentLocation.longitude, eventMarker.getPosition().latitude, eventMarker.getPosition().longitude);

                        if(location.getLatitude() != data.getLatitud() && location.getLongitude() != data.getLongitud()){
                            map.clear();
                            map.addMarker(new MarkerOptions().position(eventMarker.getPosition()).title(geoCoderSearch(eventMarker.getPosition())).snippet("Ubicación del evento").alpha(0.8f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            map.addMarker(new MarkerOptions().position(currentLocation).title(geoCoderSearch(currentLocation)).snippet("Ubicación Actual").alpha(0.8f)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            data.setLongitud(location.getLongitude());
                            data.setLatitud(location.getLatitude());
                            mRef = mDatabase.getReference("users/"+user.getUid()+"/latitud");
                            mRef.setValue(location.getLatitude());
                            mRef = mDatabase.getReference("users/"+user.getUid()+"/longitud");
                            mRef.setValue(location.getLongitude());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
                            showRoute(currentLocation.latitude, currentLocation.longitude, eventMarker.getPosition().latitude, eventMarker.getPosition().longitude);
                        }

                    }
                }


            }
        };



        if (ContextCompat.checkSelfPermission(DescripcionEventoActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           pedirPermisos();
        }

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // listeners
        btnDescripcion.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(fromActivity.equalsIgnoreCase("Principal")){
                    Intent intent = new Intent(view.getContext(), ReglasCondicionesActivity.class);
                    intent.putExtra("idevento", "" + evento.getID());
                    startActivity(intent);
                } else if(fromActivity.equalsIgnoreCase("Inscritos")){
                    Intent intent = new Intent(view.getContext(), EventosInscritosActivity.class);
                    evProv.desinscribirEvento(evento);
                    startActivity(intent);
                }
            }
        });

        tvHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PerfilHostActivity.class);
                startActivity(intent);
            }
        });


        btnChatEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatEventoActivity.class);
                intent.putExtra("idevento", "" + evento.getID());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        initUser(user);
    }

    private void initUser(FirebaseUser user) {
        if(user != null){
            mRef = mDatabase.getReference("users/" + user.getUid());
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    data = dataSnapshot.getValue(UserDTO.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void initializeEvent(){

        evento = evProv.getEventByID(idEvento);

        tvTituloEvento.setText(evento.getTitle());
        tvDescripcionEvento.setText(evento.getDescription());
        tvPrecioEvento.setText( Long.toString(evento.getPrice()));
        tvLocationEvento.setText(geoCoderSearch(evento.getLocation()));
        tvHost.setText(evProv.getEventHostName(idEvento));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fecha =  formatter.format(evento.getStartDate().getTime())  + " - " + formatter.format(evento.getEndDate().getTime());
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
        String horario = formatter2.format(evento.getStartDate().getTime()) + " - " + formatter2.format(evento.getEndDate().getTime());
        tvTiempoEvento.setText(horario);
        tvFechaEvento.setText(fecha);

        for(String t: evento.getTags()){
            Chip chip = new Chip(this);
            chip.setText(t);
            chipGroup.addView(chip);
        }

        api_key(String.valueOf(evento.getLocation().latitude),
                String.valueOf(evento.getLocation().longitude),
                evento.getLocationName());

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
        locationRequest.setInterval(10000); //tasa de refresco en milisegundos
        locationRequest.setFastestInterval(5000); //máxima tasa de refresco
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

        if (ContextCompat.checkSelfPermission(DescripcionEventoActivity.this,
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

        LatLng eventLocation = evento.getLocation();
        eventMarker = map.addMarker( new MarkerOptions().position(eventLocation).title(geoCoderSearch(eventLocation)).alpha(0.8f).snippet("Ubicación del evento").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 12));

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        /*Si el GPS esta apagado pregunta si lo puede prender*/
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode){
                    case CommonStatusCodes
                            .RESOLUTION_REQUIRED:
                        try{
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(DescripcionEventoActivity.this,REQUEST_CHECK_SETTINGS); //Empieza una actividad.
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes
                            .SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

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

    public void showRoute(double myLat, double myLong, double otherLat, double otherLong){
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(new LatLng(myLat, myLong), new LatLng(otherLat, otherLong))
                .key("AIzaSyCKD2CKSOFHTBP4jpve7iQHg-Cf9_sPphU")
                .build();
        routing.execute();
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
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> r, int index) {

        if(route != null){
            route.clear();
        }
        PolylineOptions pOptions = new PolylineOptions();
        LatLng polyStart = null;
        LatLng polyEnd = null;

        route = new ArrayList<>();
        for(int i = 0; i < r.size(); i++){
            if(i == index){
                pOptions.color(Color.rgb(218, 0, 255));
                pOptions.width(12);
                pOptions.addAll(r.get(index).getPoints());
                Polyline polyline = map.addPolyline(pOptions);
                polyStart = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polyEnd = polyline.getPoints().get(k - 1);
                route.add(polyline);
            }
        }
    }

    @Override
    public void onRoutingCancelled() {
        showRoute(currentLocation.latitude, currentLocation.longitude, evento.getLocation().latitude, evento.getLocation().longitude);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showRoute(currentLocation.latitude, currentLocation.longitude, evento.getLocation().latitude, evento.getLocation().longitude);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
    private void api_key(String lat, String lng, final String City){
        OkHttpClient client = new OkHttpClient();
        String url2 = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lng+"&appid=8a998cbdb679f9c2baca0dfa1a2e7b20&units=metric";
        Log.i("LlamadaREST","Entre1");
        Request request = new Request.Builder()
                .url(url2)
                .get()
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("LamadaREST","No pude");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData = response.body().string();
                    try {
                        Log.i("LlamadaREST","Entre");
                        JSONObject jsonObject  = new JSONObject(responseData);
                        JSONArray array = jsonObject.getJSONArray("weather");
                        JSONObject object = array.getJSONObject(0);

                        String description = object.getString("description");
                        String icons = object.getString("icon");

                        JSONObject templ = jsonObject.getJSONObject("main");
                        Double temperatura = templ.getDouble("temp");

                        setText(tvCuidad,City);

                        String temps = Math.round(temperatura)+" °C";
                        setText(tvTemp,temps);

                        setText(tvDescripcion,description);

                        setImage(ivClima,icons);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setText(final TextView text, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }
    private void setImage(final ImageView imageView, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (value){
                    case "01d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d01d));
                        break;
                    case "01n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d01d));
                        break;
                    case "02d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d02d));
                        break;
                    case "02n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d02d));
                        break;
                    case "03d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d03d));
                        break;
                    case "03n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d03d));
                        break;
                    case "04d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d04d));
                        break;
                    case "04n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d04d));
                        break;
                    case "09d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d09d));
                        break;
                    case "09n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d09d));
                        break;
                    case "10d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d10d));
                        break;
                    case "10n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d10d));
                        break;
                    case "11d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d11d));
                        break;
                    case "11n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d11d));
                        break;
                    case "13d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d13d));
                        break;
                    case "13n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d13d));
                        break;
                    default:
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.wheather));

                }
            }
        });
    }

}