package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.providers.EventProvider;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ModificarEventoActivity extends AppCompatActivity {

    private EditText etNombre, etLugar, etPrecio, etInicio, etFin;
    private CheckBox cbDeportivo, cbEmpresarial, cbFerias, cbExhibiciones, cbCongresos, cbSocial, cbCatering, cbConciertos, cbEspectaculos, cbConvenciones;
    private Button btnModificar;

    private EventProvider evProv = EventProvider.getInsatance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String idEvento = getIntent().getStringExtra("idevento");

        setContentView(R.layout.activity_modificar_evento);

        etNombre = findViewById(R.id.etNombreModificarEvento);
        etLugar = findViewById(R.id.etLugarModificarEvento);
        etPrecio = findViewById(R.id.etPrecioModificarEvento);
        etInicio = findViewById(R.id.etFechaInicioModificarEvento);
        etFin = findViewById(R.id.etFechaFinModificarEvento);

        cbDeportivo = findViewById(R.id.cbDeportivoModificarEvento);
        cbEmpresarial = findViewById(R.id.cbEmpresarialModificarEvento);
        cbFerias = findViewById(R.id.cbFeriaModificarEvento);
        cbExhibiciones = findViewById(R.id.cbExhibicionesModificarEvento);
        cbCongresos = findViewById(R.id.cbCongresosModificarEvento);
        cbSocial = findViewById(R.id.cbSocialModificarEvento);
        cbCatering = findViewById(R.id.cbCateringModificarEvento);
        cbConciertos = findViewById(R.id.cbConciertosModificarEvento);
        cbEspectaculos = findViewById(R.id.cbEspectaculosModificarEvento);
        cbConvenciones = findViewById(R.id.cbConvencionesModificarEvento);

        btnModificar = findViewById(R.id.btnModificarEvento);

        Event evento = evProv.getEventByID(idEvento);
        getSupportActionBar().setTitle("Modificar Evento");

        //Titulo Evento
        etNombre.setText(evento.getTitle());

        //Localizacion evento
        LatLng latLng = evento.getLocation();
        String city = "";
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
            city = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        etLugar.setText(city);

        //Precio evento
        etPrecio.setText((int) evento.getPrice());

        //Fecha

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fechaInicio =  formatter.format(evento.getStartDate());
        etInicio.setText(fechaInicio);

        String fechaFinal = formatter.format(evento.getEndDate());
        etFin.setText(fechaFinal);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}