package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.letshang.R;
import com.example.letshang.model.MusicEvent;
import com.example.letshang.model.SportEvent;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateMusicEventActivity extends AppCompatActivity {

    private EditText etGenero, etArtistas;
    private Button botonCrear;
    private AwesomeValidation validation;
    private EventProvider ep;
    private UserProvider up;
    private MusicEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_music_event);

        etGenero = findViewById(R.id.etGeneroEventoMusical);
        etArtistas = findViewById(R.id.etArtistasEventoMusical);

        ep = EventProvider.getInsatance();
        up = UserProvider.getInsatance();

        Bundle extras = getIntent().getExtras();
        String eventName = (String) extras.get("name");
        String description = (String) extras.get("description");
        int precio =  Integer.parseInt((String) extras.get("price"));
        LatLng location = (LatLng) extras.get("location");
        GregorianCalendar startDate = (GregorianCalendar) extras.get("startDate");
        GregorianCalendar endDate = (GregorianCalendar) extras.get("endDate");
        int capacidad = (int) extras.get("capacidad");
        ArrayList<String> tags = (ArrayList<String>) extras.get("tags");

        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(this, R.id.etGeneroEventoMusical, RegexTemplate.NOT_EMPTY, R.string.nameerror);
        validation.addValidation(this, R.id.etArtistasEventoMusical, RegexTemplate.NOT_EMPTY, R.string.requirederror);

        event = new MusicEvent(eventName , description, startDate, endDate, precio, capacidad, tags, location );

        botonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.setMusic(etGenero.getText().toString());
                event.setArtists(etArtistas.getText().toString());
                ep.createEvent(event, null);

                Intent i = new Intent(getApplicationContext() , PrincipalActivity.class);
                startActivity(i);
                finish();
            }
        });


    }
}