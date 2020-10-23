package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
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

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DescripcionEventoActivity extends AppCompatActivity {

    private TextView tvHost;
    private Button btnCancelar;
    private TextView tvLocationEvento;
    private TextView tvFechaEvento;
    private TextView tvTiempoEvento;
    private TextView tvPrecioEvento;
    private TextView tvDescripcionEvento;

    private EventProvider evProv = EventProvider.getInsatance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_evento);

        int idEvento = getIntent().getIntExtra("idevento", 1);

        tvHost = findViewById(R.id.tvHostDescripcionEvento);

        tvHost.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        btnCancelar = findViewById(R.id.btnCancelarDescripcionEvento);

        tvLocationEvento = findViewById(R.id.tvLocationDescripcionEvento);

        tvFechaEvento = findViewById(R.id.tvFechaDescripcionEvento);

        tvTiempoEvento = findViewById(R.id.tvTiempoDescripcionEvento);

        tvPrecioEvento = findViewById(R.id.tvPrecioDescripcionEvento);

        tvDescripcionEvento = findViewById(R.id.tvResumenDescripcionEvento);


        Event evento = evProv.getEventByID(idEvento-2);

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


        btnCancelar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EventosInscritosActivity.class);
                startActivity(intent);
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
}
