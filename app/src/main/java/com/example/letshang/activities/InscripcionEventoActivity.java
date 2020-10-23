package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.providers.EventProvider;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class InscripcionEventoActivity extends AppCompatActivity {

    private Button btnSiguiente;
    private TextView tvTituloInscriEvento;
    private TextView tvHostEvento;
    private TextView tvLocationEvento;
    private TextView tvFechaEvento;
    private TextView tvTiempoEvento;
    private TextView tvPrecioEvento;
    private TextView tvDescripcionEvento;

    private EventProvider evProv = EventProvider.getInsatance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscripcion_evento);

        int idEvento = getIntent().getIntExtra("idevento", 1);

        tvTituloInscriEvento = findViewById(R.id.tvTituloInscripcionEvento);

        tvHostEvento = findViewById(R.id.tvHostInscripcionEvento);

        btnSiguiente = findViewById(R.id.btnSiguienteInscripcionEvento);

        tvLocationEvento = findViewById(R.id.tvLocationInscripcionEvento);

        tvFechaEvento = findViewById(R.id.tvFechaInscripcionEvento);

        tvTiempoEvento = findViewById(R.id.tvTiempoInscripcionEvento);

        tvPrecioEvento = findViewById(R.id.tvPrecioInscripcionEvento);

        tvDescripcionEvento = findViewById(R.id.tvResumenInscripcionEvento);


        Event event = evProv.getEventByID(idEvento);

        getSupportActionBar().setTitle("Inscribir evento");
        tvTituloInscriEvento.setText(event.getTitle());
        tvDescripcionEvento.setText(event.getDescription());
        tvPrecioEvento.setText( Long.toString(event.getPrice()));

        LatLng latLng = event.getLocation();
        String city = "";

        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
            city = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvLocationEvento.setText(city);
        tvHostEvento.setText(evProv.getEventHost(idEvento).getName());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fecha =  formatter.format(event.getStartDate())  + " - " + formatter.format(event.getEndDate());
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
        String horario = formatter2.format(event.getStartDate()) + " - " + formatter2.format(event.getEndDate());
        tvTiempoEvento.setText(horario);
        tvFechaEvento.setText(fecha);



        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReglasCondicionesActivity.class);
                startActivity(intent);
            }
        });
    }

}