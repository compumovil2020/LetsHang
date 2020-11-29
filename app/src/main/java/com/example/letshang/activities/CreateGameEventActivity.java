package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.letshang.R;
import com.example.letshang.model.GameEvent;
import com.example.letshang.model.GameEventLevel;
import com.example.letshang.model.SportEvent;
import com.example.letshang.model.SportEventLevel;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateGameEventActivity extends AppCompatActivity {

    private TextView titulo, mayorEdad, nivel;
    private EditText tipo, premio, nombre, rangoEdad;
    private Spinner respuestaMayorEdad, respuestaNivel;
    private AwesomeValidation validation;
    private UserProvider userProvider;
    private EventProvider eventProvider;
    private GameEvent gameEvent;
    private Button button;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game_event);

        titulo = findViewById(R.id.tvTituloGameEvent);
        mayorEdad = findViewById(R.id.tvMayorEdadGameEvent);
        nivel = findViewById(R.id.tvNivelGameEvent);
        tipo = findViewById(R.id.etTipoDeJuegoGameEvent);
        premio = findViewById(R.id.etPremioJuegoGameEvent);
        nombre = findViewById(R.id.etNombreJuegoGameEvent);
        respuestaMayorEdad = findViewById(R.id.spMayorEdadGameEvent);
        respuestaNivel = findViewById(R.id.spNivelGameEvent);
        rangoEdad = findViewById(R.id.etRangoEdadJuegoGameEvent);

        button = findViewById(R.id.bCrearEventoGameEvent);

        userProvider = UserProvider.getInstance();
        eventProvider = EventProvider.getInsatance();

        mAuth = FirebaseAuth.getInstance();
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

        // obtener datos del evento
        Bundle extras = getIntent().getExtras();
        String eventName = (String) extras.get("name");
        String description = (String) extras.get("description");
        int precio =  Integer.parseInt((String) extras.get("price"));
        LatLng location = (LatLng) extras.get("location");
        GregorianCalendar startDate = (GregorianCalendar) extras.get("startDate");
        GregorianCalendar endDate = (GregorianCalendar) extras.get("endDate");
        int capacidad = (int) extras.get("capacidad");
        ArrayList<String> tags = (ArrayList<String>) extras.get("tags");
        String locationName = (String) extras.get("locationName");


        // Form validation
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(this, R.id.etTipoDeJuegoGameEvent, RegexTemplate.NOT_EMPTY, R.string.requirederror);
        validation.addValidation(this, R.id.etPremioJuegoGameEvent, RegexTemplate.NOT_EMPTY, R.string.requirederror);
        validation.addValidation(this, R.id.etNombreJuegoGameEvent, RegexTemplate.NOT_EMPTY, R.string.requirederror);
        validation.addValidation(this, R.id.etRangoEdadJuegoGameEvent, RegexTemplate.NOT_EMPTY, R.string.requirederror);

        gameEvent = new GameEvent(eventName , description, startDate, endDate, precio, capacidad, tags, location, locationName );

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        respuestaNivel.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerEdad);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        respuestaMayorEdad.setAdapter(adapter2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation.validate()){
                    if(respuestaNivel.getSelectedItem() == null){
                        Toast.makeText(getApplicationContext(), "Seleccione un nivel de dificultad", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(respuestaMayorEdad.getSelectedItem() == null){
                        Toast.makeText(getApplicationContext(), "Seleccione un nivel de dificultad", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    gameEvent.setKind(tipo.toString());
                    gameEvent.setGame(nombre.toString());
                    gameEvent.setPrize(premio.toString());
                    gameEvent.setAgeRange(rangoEdad.toString());

                    if(respuestaMayorEdad.getSelectedItemPosition() == 0){
                        gameEvent.setAdult(true);
                    } else if(respuestaMayorEdad.getSelectedItemPosition() == 1){
                        gameEvent.setAdult(false);
                    }

                    if(respuestaNivel.getSelectedItemPosition() == 0){
                        gameEvent.setLevel(GameEventLevel.BEGINNER);
                    }

                    else if(respuestaNivel.getSelectedItemPosition() == 1){
                        gameEvent.setLevel(GameEventLevel.AMATEUR);
                    }

                    else if(respuestaNivel.getSelectedItemPosition() == 2){
                        gameEvent.setLevel(GameEventLevel.INTERMEDIATE);
                    }

                    else if(respuestaNivel.getSelectedItemPosition() == 3){
                        gameEvent.setLevel(GameEventLevel.ADVANCED);
                    }

                    else if(respuestaNivel.getSelectedItemPosition() == 4){
                        gameEvent.setLevel(GameEventLevel.PROFESSIONAL);
                    }

                    // agrega el evento con el provider
                    eventProvider.createEvent(gameEvent, mAuth.getUid() );

                    Intent i = new Intent(getApplicationContext() , PrincipalActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}