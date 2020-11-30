package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.letshang.R;
import com.example.letshang.model.AcademicEvent;
import com.example.letshang.model.AcademicEventLevel;
import com.example.letshang.model.AcademicType;
import com.example.letshang.model.GameEvent;
import com.example.letshang.model.GameEventLevel;
import com.example.letshang.model.MusicEvent;
import com.example.letshang.model.SocialEvent;
import com.example.letshang.model.SportEvent;
import com.example.letshang.model.SportEventLevel;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;
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
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CrearEventoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText etNombre, etLugar, etPrecio, etInicio, etFin, etTags, etDescription, etCapacidad;
    private RadioButton rbSport, rbSocial, rbMusical, rbGaming, rbAcademic;
    private Button btnCrear;
    private GregorianCalendar startDate, endDate;
    private ArrayList<String> tags;
    private ChipGroup chipGroup;

    private TextView tvCuidad;
    private TextView tvTemp;
    private TextView tvDescripcion;
    private ImageView ivClima;

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

    private ConstraintLayout lyEventoMusical;
    private ConstraintLayout lyEventoAcademico;
    private ConstraintLayout lyEventoGame;
    private ConstraintLayout lyEventoSocial;
    private ConstraintLayout lyEventoDeportivo;

    private UserProvider userProvider;
    private EventProvider eventProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflar
        setContentView(R.layout.activity_crear_evento);

        mGeocoder = new Geocoder(getBaseContext());

        userProvider = UserProvider.getInstance();
        eventProvider = EventProvider.getInsatance();

        etNombre = findViewById(R.id.etNombreCrearEvento);
        etLugar = findViewById(R.id.etLugarCrearEvento);
        etPrecio = findViewById(R.id.etPrecioCrearEvento);
        etInicio = findViewById(R.id.etFechaInicioCrearEvento);
        etFin = findViewById(R.id.etFechaFinCrearEvento);
        etTags = findViewById(R.id.etTagsCrearEvento);
        etDescription = findViewById(R.id.editTextTextMultiLine);
        etCapacidad = findViewById(R.id.etCapacidadCrearEvento);
        radioGroup = findViewById(R.id.rgTipoEventoCrearEvento);

        lyEventoMusical = findViewById(R.id.lyEventoMusical);
        lyEventoAcademico = findViewById(R.id.lyEventoAcademico);
        lyEventoGame = findViewById(R.id.lyEventoGame);
        lyEventoSocial = findViewById(R.id.lyEventoSocial);
        lyEventoDeportivo = findViewById(R.id.lyEventoDeportivo);

        rbSocial = findViewById(R.id.rbSocialCrearEvento);
        rbSport = findViewById(R.id.rbSportCrearEvento);
        rbMusical = findViewById(R.id.rbMusicCrearEvento);
        rbGaming = findViewById(R.id.rbGameCrearEvento);
        rbAcademic = findViewById(R.id.rbAcademicCrearEvento);

        chipGroup = (ChipGroup) findViewById(R.id.cgTagsCrearEvento);
        btnCrear = findViewById(R.id.btnCrearEvento);

        mapView = findViewById(R.id.mpMapCrearEvento);

        tvCuidad = findViewById(R.id.tvCuidadCrearEvento);
        tvCuidad.setText("");
        tvTemp = findViewById(R.id.tvTempCrearEvento);
        tvTemp.setText("");
        tvDescripcion = findViewById(R.id.tvDescrCrearEvento);
        tvDescripcion.setText("");
        ivClima = findViewById(R.id.ivClimaCrearEvento);
         // inicializar
        startDate = new GregorianCalendar();
        endDate = new GregorianCalendar();
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

        etLugar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String address = etLugar.getText().toString();
                    if (!address.isEmpty()) {
                        try {
                            List<Address> addresses = mGeocoder.getFromLocationName(address, 2, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRigthLongitude);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address res = addresses.get(0);
                                LatLng pos = new LatLng(res.getLatitude(), res.getLongitude());
                                if (map != null) {
                                    map.clear();
                                    map.addMarker(new MarkerOptions().position(currentLocation).title(geoCoderSearch(currentLocation)).alpha(0.8f).snippet("Ubicación Actual").
                                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                    location = pos;
                                    MarkerOptions mo = new MarkerOptions();
                                    mo.position(location);
                                    mo.title(res.getAddressLine(0));
                                    mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                    mo.alpha(0.8f);
                                    map.addMarker(mo);
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                                    api_key(String.valueOf(pos.latitude),String.valueOf(pos.longitude),res.getLocality());
                                }
                            } else {
                                Toast.makeText(CrearEventoActivity.this, "No se encontró la dirección digitada", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(CrearEventoActivity.this, "Dirección invalida!", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });


        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()){
                    Log.i("selected location", String.valueOf(location.latitude) + " "+String.valueOf(location.longitude));
                    Toast.makeText(getApplicationContext() , "texto: " + etLugar.getText().toString() , Toast.LENGTH_SHORT).show();
                    String nombre = etNombre.getText().toString();
                    Long valor = Long.parseLong(etPrecio.getText().toString());
                    int capacidad = Integer.parseInt(etCapacidad.getText().toString());
                    String descripcion = etDescription.getText().toString();
                    String locationName = etLugar.getText().toString();



                    String host = FirebaseAuth.getInstance().getUid();

                    if(radioGroup.getCheckedRadioButtonId() == rbAcademic.getId()){
                        AwesomeValidation validationAcademic = new AwesomeValidation(ValidationStyle.BASIC);


                        EditText materia = findViewById(R.id.etSubjectAcademicalEvent);
                        EditText idioma = findViewById(R.id.etIdiomaCreateAcademicalEvent);
                        Spinner tipo = findViewById(R.id.spTipoEventoAcademicalEvent);
                        Spinner nivel = findViewById(R.id.spNivelEventoAcademicaEvent);

                        validationAcademic.addValidation(CrearEventoActivity.this, R.id.etSubjectAcademicalEvent, RegexTemplate.NOT_EMPTY, R.string.materiaerror);
                        validationAcademic.addValidation(CrearEventoActivity.this, R.id.etIdiomaCreateAcademicalEvent, RegexTemplate.NOT_EMPTY, R.string.idiomaerror);

                        if(validationAcademic.validate()) {
                            AcademicEvent academicEvent = new AcademicEvent(nombre, descripcion, startDate, endDate, valor, capacidad, tags, location,locationName);

                            academicEvent.setLanguages(idioma.getText().toString());
                            academicEvent.setSubject(materia.getText().toString());
                            switch (nivel.getSelectedItem().toString()) {
                                case "Prescolar":
                                    academicEvent.setLevel(AcademicEventLevel.PRESCHOOL);
                                    break;
                                case "Elemental":
                                    academicEvent.setLevel(AcademicEventLevel.ELEMENTARY);
                                    break;
                                case "Juvenil":
                                    academicEvent.setLevel(AcademicEventLevel.JUVENILE);
                                    break;
                                case "Bachillerato":
                                    academicEvent.setLevel(AcademicEventLevel.HIGHSCHOOL);
                                    break;
                                case "Universitario":
                                    academicEvent.setLevel(AcademicEventLevel.UNIVERSITY);
                                    break;
                                case "Profesional":
                                    academicEvent.setLevel(AcademicEventLevel.PROFFESIONAL);
                                    break;
                            }

                            switch (tipo.getSelectedItem().toString()) {
                                case "Taller":
                                    academicEvent.setTypeAcademicalEvent(AcademicType.TALLER);
                                    break;
                                case "Seminario":
                                    academicEvent.setTypeAcademicalEvent(AcademicType.SEMINARIO);
                                    break;
                                case "Monitoria":
                                    academicEvent.setTypeAcademicalEvent(AcademicType.MONITORIA);
                                    break;
                                case "Charla":
                                    academicEvent.setTypeAcademicalEvent(AcademicType.CHARLA);
                                    break;
                                case "Disertación":
                                    academicEvent.setTypeAcademicalEvent(AcademicType.DISERTACION);
                                    break;
                            }
                            eventProvider.createEvent(academicEvent, host);
                            confirm();
                        }

                    }else if(radioGroup.getCheckedRadioButtonId() == rbSport.getId()){
                        AwesomeValidation validationSport = new AwesomeValidation(ValidationStyle.BASIC);

                        EditText deporte = findViewById(R.id.etDeporteEventoDeportivo);
                        EditText equipo = findViewById(R.id.etEquipoEventoDeportivo);
                        Spinner nivel = findViewById(R.id.spLevelEventoDeportivo);

                        validationSport.addValidation(deporte,RegexTemplate.NOT_EMPTY,"Ingresa el deporte");
                        validationSport.addValidation(equipo,RegexTemplate.NOT_EMPTY,"Ingresa el tamaño del equipo");
                        validationSport.addValidation(equipo,Range.open(1,20),"Ingresa un numero valido");

                        if(validationSport.validate()) {
                            SportEvent sportEvent = new SportEvent(nombre, descripcion, startDate, endDate, valor, capacidad, tags, location,locationName);
                            sportEvent.setSport(deporte.getText().toString());
                            sportEvent.setTeamSize(Integer.parseInt(equipo.getText().toString()));

                            if (nivel.getSelectedItemPosition() == 0) {
                                sportEvent.setLevel(SportEventLevel.BEGINNER);
                            } else if (nivel.getSelectedItemPosition() == 1) {
                                sportEvent.setLevel(SportEventLevel.AMATEUR);
                            } else if (nivel.getSelectedItemPosition() == 2) {
                                sportEvent.setLevel(SportEventLevel.INTERMEDIATE);
                            } else if (nivel.getSelectedItemPosition() == 3) {
                                sportEvent.setLevel(SportEventLevel.ADVANCED);
                            } else if (nivel.getSelectedItemPosition() == 4) {
                                sportEvent.setLevel(SportEventLevel.PROFESSIONAL);
                            }

                            eventProvider.createEvent(sportEvent, host);
                            confirm();
                        }

                    }
                    else if(radioGroup.getCheckedRadioButtonId() == rbSocial.getId()){
                        AwesomeValidation validationSocial = new AwesomeValidation(ValidationStyle.BASIC);

                        EditText genero = findViewById(R.id.etGeneroEventoSocial);
                        EditText tematica = findViewById(R.id.etTematicaEventoSocial);
                        EditText edadMinima = findViewById(R.id.etEdadMinimaEventoSocial);
                        EditText reglas = findViewById(R.id.etEdadMinimaEventoSocial);

                        validationSocial.addValidation(genero,RegexTemplate.NOT_EMPTY,"Ingrese un genero de musica");
                        validationSocial.addValidation(tematica,RegexTemplate.NOT_EMPTY,"Ingrese una tematica");
                        validationSocial.addValidation(edadMinima,RegexTemplate.NOT_EMPTY,"Ingrese una edad");
                        validationSocial.addValidation(edadMinima,Range.open(0,100),"Ingrese una edad entre 0 y 100");
                        validationSocial.addValidation(reglas,RegexTemplate.NOT_EMPTY,"Ingrese las reglas del evento");

                        if(validationSocial.validate()) {
                            SocialEvent socialEvent = new SocialEvent(nombre,descripcion,startDate,endDate,valor,capacidad,tags,location,locationName);
                            socialEvent.setMusicGenre(genero.getText().toString());
                            socialEvent.setTheme(tematica.getText().toString());
                            socialEvent.setMinimumAge(Integer.parseInt(edadMinima.getText().toString()));
                            socialEvent.setRules(reglas.getText().toString());

                            eventProvider.createEvent(socialEvent, host);
                            confirm();
                        }
                    }
                    else if(radioGroup.getCheckedRadioButtonId() == rbGaming.getId()){
                        AwesomeValidation validationGame = new AwesomeValidation(ValidationStyle.BASIC);


                        EditText nombreJuego = findViewById(R.id.etNombreJuegoGameEvent);
                        EditText tipoJuego = findViewById(R.id.etTipoDeJuegoGameEvent);
                        EditText premio = findViewById(R.id.etPremioJuegoGameEvent);
                        EditText rangoEdad = findViewById(R.id.etRangoEdadJuegoGameEvent);
                        Spinner respuestaNivel = findViewById(R.id.spNivelGameEvent);
                        Spinner mayorEdad = findViewById(R.id.spMayorEdadGameEvent);

                        validationGame.addValidation(nombreJuego,RegexTemplate.NOT_EMPTY,"Ingrese el nombre del juego");
                        validationGame.addValidation(tipoJuego,RegexTemplate.NOT_EMPTY,"Ingrese el tipo del juego");
                        validationGame.addValidation(premio,RegexTemplate.NOT_EMPTY,"Ingrese un premio");
                        validationGame.addValidation(rangoEdad,RegexTemplate.NOT_EMPTY,"Ingrese un rango de edad");

                        if(validationGame.validate()) {
                            GameEvent gameEvent = new GameEvent(nombre, descripcion, startDate, endDate, valor, capacidad, tags, location,locationName);
                            gameEvent.setGame(nombreJuego.getText().toString());
                            gameEvent.setKind(tipoJuego.getText().toString());
                            gameEvent.setPrize(premio.getText().toString());
                            gameEvent.setAgeRange(rangoEdad.getText().toString());

                            if (mayorEdad.getSelectedItemPosition() == 0) {
                                gameEvent.setAdult(true);
                            } else if (mayorEdad.getSelectedItemPosition() == 1) {
                                gameEvent.setAdult(false);
                            }

                            if (respuestaNivel.getSelectedItemPosition() == 0) {
                                gameEvent.setLevel(GameEventLevel.BEGINNER);
                            } else if (respuestaNivel.getSelectedItemPosition() == 1) {
                                gameEvent.setLevel(GameEventLevel.AMATEUR);
                            } else if (respuestaNivel.getSelectedItemPosition() == 2) {
                                gameEvent.setLevel(GameEventLevel.INTERMEDIATE);
                            } else if (respuestaNivel.getSelectedItemPosition() == 3) {
                                gameEvent.setLevel(GameEventLevel.ADVANCED);
                            } else if (respuestaNivel.getSelectedItemPosition() == 4) {
                                gameEvent.setLevel(GameEventLevel.PROFESSIONAL);
                            }

                            eventProvider.createEvent(gameEvent, host);
                            confirm();
                        }

                    }
                    else if(radioGroup.getCheckedRadioButtonId() == rbMusical.getId()){
                        AwesomeValidation validationMusic = new AwesomeValidation(ValidationStyle.BASIC);

                        EditText genero = findViewById(R.id.etGeneroEventoMusical);
                        EditText artistas = findViewById(R.id.etArtistasEventoMusical);

                        validationMusic.addValidation(genero,RegexTemplate.NOT_EMPTY,"Ingrese un genero musical");
                        validationMusic.addValidation(artistas,RegexTemplate.NOT_EMPTY,"Ingrese artistas invitados");

                        if(validationMusic.validate()) {
                            MusicEvent musicEvent = new MusicEvent(nombre, descripcion, startDate, endDate, valor, capacidad, tags, location,locationName);
                            musicEvent.setMusic(genero.getText().toString());
                            musicEvent.setArtists(artistas.getText().toString());

                            eventProvider.createEvent(musicEvent, host);
                            confirm();
                        }

                    }

                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                clearSpecificEvent();
                if(i == rbAcademic.getId()){
                    setAcademicEvent();

                }else if(i == rbSport.getId()){
                    setSportEvent();

                }
                else if(i == rbSocial.getId()){
                    setSocialEvent();

                }
                else if(i == rbGaming.getId()){
                    setGamingEvent();

                }
                else if(i == rbMusical.getId()){
                    setMusicalEvent();

                }
            }
        });

    }

    private void confirm() {
        Intent i = new Intent(this,PrincipalActivity.class);
        Toast.makeText(this,"Evento Guardado con Exito",Toast.LENGTH_SHORT).show();
        startActivity(i);
    }

    private void clearSpecificEvent() {
        lyEventoMusical.setVisibility(View.GONE);
        lyEventoAcademico.setVisibility(View.GONE);
        lyEventoGame.setVisibility(View.GONE);
        lyEventoSocial.setVisibility(View.GONE);
        lyEventoDeportivo.setVisibility(View.GONE);
    }


    private void setMusicalEvent() {
        lyEventoMusical.setVisibility(View.VISIBLE);
    }

    private void setGamingEvent() {
        Spinner respuestaMayorEdad, respuestaNivel;
        respuestaMayorEdad = findViewById(R.id.spMayorEdadGameEvent);
        respuestaNivel = findViewById(R.id.spNivelGameEvent);
        //Spinner de nivel
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Principiante");
        spinnerArray.add("Amateur");
        spinnerArray.add("Intermedio");
        spinnerArray.add("Avanzado");
        spinnerArray.add("Profesional");

        //Spiner mayor edad
        List<String> spinnerEdad = new ArrayList<String>();
        spinnerEdad.add("Si");
        spinnerEdad.add("No");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        respuestaNivel.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerEdad);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        respuestaMayorEdad.setAdapter(adapter2);

        lyEventoGame.setVisibility(View.VISIBLE);


    }

    private void setSocialEvent() {
        lyEventoSocial.setVisibility(View.VISIBLE);

    }

    private void setSportEvent() {
        Spinner spinner;
        spinner = findViewById(R.id.spLevelEventoDeportivo);
        // llenar spinner
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Principiante");
        spinnerArray.add("Amateur");
        spinnerArray.add("Intermedio");
        spinnerArray.add("Avanzado");
        spinnerArray.add("Profesional");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        lyEventoDeportivo.setVisibility(View.VISIBLE);

    }

    private void setAcademicEvent() {

        Spinner nivelEventoAcademico, tipoEventoAcademico;
        nivelEventoAcademico = findViewById(R.id.spNivelEventoAcademicaEvent);
        tipoEventoAcademico = findViewById(R.id.spTipoEventoAcademicalEvent);
        List<String> spinnerList1 = Arrays.asList(getResources().getStringArray(R.array.type_academical_event));
        List<String> spinnerList2 = Arrays.asList(getResources().getStringArray(R.array.academic_Levels));
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,spinnerList1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoEventoAcademico.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,spinnerList2);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nivelEventoAcademico.setAdapter(adapter1);
        lyEventoAcademico.setVisibility(View.VISIBLE);

    }

    private boolean validateInput(){

        if(etFin.getText().toString().isEmpty() || etInicio.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext() , "Selecciona las fechas para tu evento" , Toast.LENGTH_SHORT).show();
            return false;
        }
        if(startDate.getTime().compareTo(endDate.getTime()) > 0){
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

        if(chipGroup.getChildCount() == 0){
            Toast.makeText(getApplicationContext() , "Porfavor ingrese como minimo 1 tag" , Toast.LENGTH_SHORT).show();
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
                // +1 because January is zero --> Not anymore baby
                String selectedDate = day + "/" + (month) + "/" + year + " ";

                if(isStart) {
                    startDate.set(GregorianCalendar.YEAR, year);
                    startDate.set(GregorianCalendar.MONTH, month);
                    startDate.set(GregorianCalendar.DAY_OF_MONTH, day);
                }
                else{
                    endDate.set(GregorianCalendar.YEAR, year);
                    endDate.set(GregorianCalendar.MONTH, month);
                    endDate.set(GregorianCalendar.DAY_OF_MONTH, day);
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
                    startDate.set(GregorianCalendar.HOUR, hour);
                    startDate.set(GregorianCalendar.MINUTE, minute);
                    etInicio.setText(selectedDate +  String.valueOf(hour) + ":" + String.valueOf(minute));

                }
                else{
                    Toast.makeText(getApplicationContext() , "estoy en true" , Toast.LENGTH_SHORT);
                    endDate.set(GregorianCalendar.HOUR, hour);
                    endDate.set(GregorianCalendar.MINUTE, minute);
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
                etLugar.setText(geoCoderSearch(location));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                api_key(String.valueOf(location.latitude),String.valueOf(location.longitude),geoCoderSearch(location));

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