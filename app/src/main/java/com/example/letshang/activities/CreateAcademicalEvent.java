package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.letshang.model.AcademicEvent;
import com.example.letshang.model.AcademicEventLevel;
import com.example.letshang.model.AcademicType;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateAcademicalEvent extends AppCompatActivity {

    private EditText temaEvento, idioma;
    private Spinner nivelEventoAcademico, tipoEventoAcademico;
    private Button crearEventBtn;
    private AwesomeValidation validation;
    private AcademicEvent academicEvent;
    private UserProvider userProvider;
    private EventProvider eventProvider;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_academical_event);

        temaEvento = findViewById(R.id.etSubjectAcademicalEvent);
        idioma = findViewById(R.id.etIdiomaCreateAcademicalEvent);
        nivelEventoAcademico = findViewById(R.id.spNivelEventoAcademicaEvent);
        tipoEventoAcademico = findViewById(R.id.spTipoEventoAcademicalEvent);
        crearEventBtn = findViewById(R.id.btnCrearEventoAcademico);

        userProvider = UserProvider.getInstance();
        eventProvider = EventProvider.getInsatance();

        mAuth = FirebaseAuth.getInstance();


        //Lo que viene de la actividad anterior
        Bundle extras = getIntent().getExtras();
        String eventName = (String) extras.get("name");
        String description = (String) extras.get("description");
        int precio =  Integer.parseInt((String) extras.get("price"));
        LatLng location = (LatLng) extras.get("location");
        GregorianCalendar startDate = (GregorianCalendar) extras.get("startDate");
        GregorianCalendar endDate = (GregorianCalendar) extras.get("endDate");
        int capacidad = (int) extras.get("capacidad");
        String locationName = (String) extras.get("locationName");

        ArrayList<String> tags = (ArrayList<String>) extras.get("tags");
        List<String> spinnerList1 = Arrays.asList(getResources().getStringArray(R.array.type_academical_event));
        List<String> spinnerList2 = Arrays.asList(getResources().getStringArray(R.array.academic_Levels));
        //Validación
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(this, R.id.etSubjectAcademicalEvent, RegexTemplate.NOT_EMPTY, R.string.requirederror);
        validation.addValidation(this, R.id.etIdiomaCreateAcademicalEvent,RegexTemplate.NOT_EMPTY, R.string.requirederror);
        validation.addValidation(this, R.id.spTipoEventoAcademicalEvent,RegexTemplate.NOT_EMPTY, R.string.requirederror);
        validation.addValidation(this, R.id.spNivelEventoAcademicaEvent,RegexTemplate.NOT_EMPTY, R.string.requirederror);

        academicEvent = new AcademicEvent(eventName , description, startDate, endDate, precio, capacidad, tags, location, locationName);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,spinnerList1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoEventoAcademico.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,spinnerList2);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nivelEventoAcademico.setAdapter(adapter1);

        crearEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation.validate()){
                    academicEvent.setLanguages(idioma.getText().toString());
                    academicEvent.setSubject(temaEvento.getText().toString());
                    switch (nivelEventoAcademico.getSelectedItem().toString()){
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

                    switch (tipoEventoAcademico.getSelectedItem().toString()){
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
                    eventProvider.createEvent(academicEvent, mAuth.getUid());
                    Intent i = new Intent(getApplicationContext() , PrincipalActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

}