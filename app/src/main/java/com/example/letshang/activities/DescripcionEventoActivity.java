package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.providers.EventProvider;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DescripcionEventoActivity extends AppCompatActivity {

    private TextView tvHost;
    private Button btnCancelar;
    private TextView tvLocationEvento;
    private TextView tvFechaEvento;
    private TextView tvTiempoEvento;
    private TextView tvPrecioEvento;
    private TextView tvDescripcionEvento;

    private EventProvider evProv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_evento);

        int numevento = getIntent().getIntExtra("numevent", 0);

        tvHost = findViewById(R.id.tvHostDescripcionEvento);

        tvHost.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        btnCancelar = findViewById(R.id.btnCancelarDescripcionEvento);

        tvLocationEvento = findViewById(R.id.tvLocationDescripcionEvento);

        tvFechaEvento = findViewById(R.id.tvFechaDescripcionEvento);

        tvTiempoEvento = findViewById(R.id.tvTiempoDescripcionEvento);

        tvPrecioEvento = findViewById(R.id.tvPrecioDescripcionEvento);

        tvDescripcionEvento = findViewById(R.id.tvResumenDescripcionEvento);

        evProv = new EventProvider();

        List<Event> listEvent = evProv.getList();

        int cont = 0;
        for(Event e : listEvent){
            if(cont == numevento){
                getSupportActionBar().setTitle(e.getTitle());
                tvDescripcionEvento.setText(e.getDescription());
                tvPrecioEvento.setText( Long.toString(e.getPrice()));
                tvLocationEvento.setText("e.getLocation()");
                tvHost.setText("e.getUser()");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String fecha =  formatter.format(e.getStartDate())  + " - " + formatter.format(e.getEndDate());
                SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
                String horario = formatter2.format(e.getStartDate()) + " - " + formatter2.format(e.getEndDate());
                tvTiempoEvento.setText(horario);
                tvFechaEvento.setText(fecha);
            }
            cont++;
        }

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
