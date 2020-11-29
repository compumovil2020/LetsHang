package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.letshang.R;
import com.example.letshang.model.GameEvent;
import com.example.letshang.model.SocialEvent;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Range;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateSocialEventActivity extends AppCompatActivity {

    private AwesomeValidation validation;

    private EditText etGeneroEventoSocial;
    private EditText etTematicaEventoSocial;
    private EditText etEdadMinimaEventoSocial;
    private EditText etReglasEventoSocial;
    private Button btnCrearEventoSocial;
    private FirebaseAuth mAuth;

    private SocialEvent socialEvent;
    private EventProvider eventProvider;
    private UserProvider userProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_social_event);

        validation = new AwesomeValidation(ValidationStyle.BASIC);
        userProvider = UserProvider.getInstance();
        eventProvider = EventProvider.getInsatance();
        mAuth = FirebaseAuth.getInstance();

        //Inflate
        etGeneroEventoSocial = findViewById(R.id.etGeneroEventoSocial);
        etTematicaEventoSocial = findViewById(R.id.etTematicaEventoSocial);
        etEdadMinimaEventoSocial = findViewById(R.id.etEdadMinimaEventoSocial);
        etReglasEventoSocial = findViewById(R.id.etReglasEventoSocial);

        //Validators
        validation.addValidation(this, R.id.etGeneroEventoSocial, RegexTemplate.NOT_EMPTY, R.string.requirederror);
        validation.addValidation(this, R.id.etTematicaEventoSocial, RegexTemplate.NOT_EMPTY, R.string.requirederror);
        validation.addValidation(this, R.id.etEdadMinimaEventoSocial, RegexTemplate.NOT_EMPTY, R.string.requirederror);
        validation.addValidation(this, R.id.etEdadMinimaEventoSocial, Range.open(0,100), R.string.ageerror);
        validation.addValidation(this, R.id.etReglasEventoSocial, RegexTemplate.NOT_EMPTY, R.string.requirederror);

        // obtener datos del evento
        Bundle extras = getIntent().getExtras();
        String eventName = (String) extras.get("name");
        String locationName = (String) extras.get("locationName");
        String description = (String) extras.get("description");
        int precio =  Integer.parseInt((String) extras.get("price"));
        LatLng location = (LatLng) extras.get("location");
        GregorianCalendar startDate = (GregorianCalendar) extras.get("startDate");
        GregorianCalendar endDate = (GregorianCalendar) extras.get("endDate");
        int capacidad = (int) extras.get("capacidad");
        ArrayList<String> tags = (ArrayList<String>) extras.get("tags");

        socialEvent = new SocialEvent(eventName , description, startDate, endDate, precio, capacidad, tags, location, locationName );


        //Listeners
        btnCrearEventoSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validation.validate()){
                    Toast.makeText(CreateSocialEventActivity.this,"valido",Toast.LENGTH_SHORT).show();
                    socialEvent.setMusicGenre(etGeneroEventoSocial.getText().toString());
                    socialEvent.setTheme(etTematicaEventoSocial.getText().toString());
                    socialEvent.setMinimumAge(Integer.parseInt(etEdadMinimaEventoSocial.getText().toString()));
                    socialEvent.setRules(etReglasEventoSocial.getText().toString());

                    // agrega el evento con el provider
                    eventProvider.createEvent(socialEvent, mAuth.getUid() );

                    Intent i = new Intent(CreateSocialEventActivity.this , PrincipalActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        });


    }
}