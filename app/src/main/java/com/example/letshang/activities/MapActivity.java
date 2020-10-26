package com.example.letshang.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.ui.dialog.CustomMapView;
import com.example.letshang.utils.PermissionHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private ImageView ivFilter;
    private TextView btnLista;
    private ActionBarDrawerToggle menuToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;
    private Geocoder gcd ;
    private LatLng currentLocation;
    private CustomMapView mapView;
    private EventProvider evProv = EventProvider.getInsatance();
    private static final int REQUEST_CHECK_SETTINGS = 99;
    private int LOCATION_PERMISSION_CODE = 101;
    private String justificacion = "Se necesita el GPS para mostrar la ubicación del evento";
    private ArrayList<Marker> listMarkers;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ivFilter = findViewById(R.id.ivFiltroMap);
        btnLista = findViewById(R.id.btnListaMap);
        navView = findViewById(R.id.map_nav_view);
        drawerLayout = findViewById(R.id.map_drawer_layout);


        setupMenu();

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),PrincipalActivity.class);
                startActivity(intent);
            }
        });


        ivFilter.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , FiltersActivity.class);
                startActivity(intent);
            }
        });

        mapView = findViewById(R.id.ivMapaMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        //Acá iría el tema de sensores
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (mMap != null) {
                    if (event.values[0] < 5000) {
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapActivity.this, R.raw.dark_style_map));
                    } else {
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapActivity.this, R.raw.light_style_map));
                    }
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        //Solicito permiso de localización
        //Si no tengo permisos, pido permisos
        if (ContextCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            pedirPermisos();
        }
        upDateCurrentPosition();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();


    }


    void setupMenu(){

        menuToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                R.string.menu_open_reader,
                R.string.menu_close_reader);

        drawerLayout.setDrawerListener(menuToggle);
        menuToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() ==  R.id.item_menu_administrar_perfil){
                    Intent intent = new Intent(getApplicationContext() , InformacionPerfilActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_sugerir){
                    Intent intent = new Intent(getApplicationContext() , CrearSugerenciaActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_eventos){
                    //does nothing
                }
                if(item.getItemId() ==  R.id.item_menu_eventos_inscritos){
                    Intent intent = new Intent(getApplicationContext() , EventosInscritosActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_logout){
                    mAuth.signOut();
                    mGoogleSignInClient.signOut();
                    Intent intent = new Intent(getApplicationContext() , StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(item.getItemId() ==  R.id.item_menu_pago){
                    Toast.makeText(getApplicationContext(), item.getTitle() ,Toast.LENGTH_SHORT ).show();

                }
                if(item.getItemId() ==  R.id.item_menu_crear_evento){
                    Intent intent = new Intent(getApplicationContext() , CrearEventoActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private void initalizeEvents(){
        //Llamo al event provider y sale
        //Aca se deben obtener los eventos cercanos
        gcd  = new Geocoder(this, Locale.getDefault());
        List<Event> listEvents = evProv.getAllEventsFromDBB();
        for(Event actual:listEvents){
            LatLng locationActual = actual.getLocation();
            listMarkers.add(mMap.addMarker(new MarkerOptions().position(locationActual).title(actual.getTitle())));
        }
    }
    // needed to setup menu toggle button
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (menuToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "onMapReady: ");
        mMap = googleMap;
        mMap.clear();
        if(!listMarkers.isEmpty()){
            listMarkers.clear();
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            pedirPermisos();
        } else {
            //Init map
            initalizeEvents();
            mMap.setMyLocationEnabled(true);
            try {
                MapsInitializer.initialize(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Lo debo hacer con mi localización
            upDateCurrentPosition();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(4.518640, -74.092700), 10);
            mMap.animateCamera(cameraUpdate);
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }

    }

    private void pedirPermisos(){

        PermissionHandler.requestPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                justificacion,
                LOCATION_PERMISSION_CODE);


        if (ContextCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // si me dieron el permiso
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            SettingsClient client = LocationServices.getSettingsClient(this);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

            task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    startLocationUpdates(); //Todas las condiciones para recibir localizaciones
                }
            });
        }
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
        Log.i("...", "startLocationUpdates: entre starlocationUpdates");
    }
    public void upDateCurrentPosition(){
        mLocationCallback = new LocationCallback(){
            public void onLocationResult(LocationResult locationResult){
                Location location = locationResult.getLastLocation();
                if(location != null){
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }
        };
    }
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
        sensorManager.unregisterListener(lightSensorListener);
        mapView.onPause();
        stopLocationUpdates();

    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener, lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        mapView.onResume();
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