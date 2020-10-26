package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DescripcionEventoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String TAG = "DescripcionEvento";
    private int idEvento;
    private String fromActivity;
    private Event evento;
    private TextView tvHost;
    private Button btnDescripcion;
    private TextView tvLocationEvento;
    private TextView tvFechaEvento;
    private TextView tvTiempoEvento;
    private TextView tvPrecioEvento;
    private TextView tvDescripcionEvento;
    private CustomMapView mapView;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location currentLocation;
    private  GoogleMap map;
    private FusedLocationProviderClient mFusedLocationClient;
    private int LOCATION_PERMISSION_CODE = 101;
    private static final int REQUEST_CHECK_SETTINGS = 99;
    private String justificacion = "Se necesita el GPS para mostrar la ubicación del evento";
    private EventProvider evProv = EventProvider.getInsatance();
    private Marker eventMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_evento);

        idEvento = getIntent().getIntExtra("idevento", 1);
        fromActivity = getIntent().getStringExtra("from");

        // inflar
        tvHost = findViewById(R.id.tvHostDescripcionEvento);
        tvHost.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        btnDescripcion = findViewById(R.id.btnCancelarDescripcionEvento);
        tvLocationEvento = findViewById(R.id.tvLocationDescripcionEvento);
        tvFechaEvento = findViewById(R.id.tvFechaDescripcionEvento);
        tvTiempoEvento = findViewById(R.id.tvTiempoDescripcionEvento);
        tvPrecioEvento = findViewById(R.id.tvPrecioDescripcionEvento);
        tvDescripcionEvento = findViewById(R.id.tvResumenDescripcionEvento);
        mapView = findViewById(R.id.mpMapDescripcionEvento);

        if(fromActivity.equalsIgnoreCase("Principal")){
            btnDescripcion.setText("Siguiente");
        } else if(fromActivity.equalsIgnoreCase("Inscritos")){
            btnDescripcion.setText("Cancelar");
        }


        // set map
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = createLocationRequest();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLocation = locationResult.getLastLocation();

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
                    startActivity(intent);
                } else if(fromActivity.equalsIgnoreCase("Inscritos")){
                    Intent intent = new Intent(view.getContext(), EventosInscritosActivity.class);
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
    }

    private void initializeEvent(){
        evento = evProv.getEventByID(idEvento-2);

        getSupportActionBar().setTitle(evento.getTitle());
        tvDescripcionEvento.setText(evento.getDescription());
        tvPrecioEvento.setText( Long.toString(evento.getPrice()));

        LatLng latLng = evento.getLocation();
        String city = "";

        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
            city = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvLocationEvento.setText(city);
        tvHost.setText(evProv.getEventHost(idEvento).getName());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fecha =  formatter.format(evento.getStartDate())  + " - " + formatter.format(evento.getEndDate());
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
        String horario = formatter2.format(evento.getStartDate()) + " - " + formatter2.format(evento.getEndDate());
        tvTiempoEvento.setText(horario);
        tvFechaEvento.setText(fecha);
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
                    startLocationUpdates(); //Todas las condiciones para recibir localizaciones
                }
            });
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.i(TAG, "onMapReady: ");
        map = googleMap;

        map.getUiSettings().setMyLocationButtonEnabled(true);
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

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(4.518640, -74.092700), 10);
        map.animateCamera(cameraUpdate);
        map.getUiSettings().setMapToolbarEnabled(false);

        initializeEvent();

        LatLng eventLocation = evento.getLocation();

        eventMarker = map.addMarker( new MarkerOptions().position(eventLocation).title(evento.getTitle()));



    }


    // Si no se tiene permisos...
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
        stopLocationUpdates();

    }

    @Override
    public void onResume() {
        super.onResume();
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
