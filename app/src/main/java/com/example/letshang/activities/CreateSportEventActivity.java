package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.model.Host;
import com.example.letshang.model.SportEvent;
import com.example.letshang.model.SportEventLevel;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Range;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateSportEventActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText etDeporte, etEquipo;
    private Button botonCrear;
    private AwesomeValidation validation;
    private EventProvider ep;
    private UserProvider up;
    private SportEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sport_event);

        spinner = findViewById(R.id.spLevelEventoDeportivo);
        etDeporte = findViewById(R.id.etDeporteEventoDeportivo);
        etEquipo = findViewById(R.id.etEquipoEventoDeportivo);
        botonCrear = findViewById(R.id.btnCrearEventoDeportivo);

        ep = EventProvider.getInsatance();
        up = UserProvider.getInsatance();


        // llenar spinner
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Principiante");
        spinnerArray.add("Amateur");
        spinnerArray.add("Intermedio");
        spinnerArray.add("Avanzado");
        spinnerArray.add("Profesional");


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

        // Form validation
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(this, R.id.etDeporteEventoDeportivo, RegexTemplate.NOT_EMPTY, R.string.nameerror);
        validation.addValidation(this, R.id.etEquipoEventoDeportivo, RegexTemplate.NOT_EMPTY, R.string.requirederror);



        event = new SportEvent(eventName , description, startDate, endDate, precio, capacidad, tags, location );



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        botonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation.validate()){


                    if(spinner.getSelectedItem() == null){
                        Toast.makeText(getApplicationContext(), "Seleccione un nivel de dificultad", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    event.setSport(etDeporte.getText().toString());
                    event.setTeamSize(Integer.parseInt(etEquipo.getText().toString()));

                    if(spinner.getSelectedItemPosition() == 0){
                        event.setLevel(SportEventLevel.BEGINNER);
                    }

                    else if(spinner.getSelectedItemPosition() == 1){
                        event.setLevel(SportEventLevel.AMATEUR);
                    }

                    else if(spinner.getSelectedItemPosition() == 2){
                        event.setLevel(SportEventLevel.INTERMEDIATE);
                    }

                    else if(spinner.getSelectedItemPosition() == 3){
                        event.setLevel(SportEventLevel.ADVANCED);
                    }

                    else if(spinner.getSelectedItemPosition() == 4){
                        event.setLevel(SportEventLevel.PROFESSIONAL);
                    }

                    // agrega el evento con el provider
                    // TODO: cambiar ese null por un host de verdad
                    ep.createEvent(event, null );

                    Intent i = new Intent(getApplicationContext() , PrincipalActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        });




    }
}