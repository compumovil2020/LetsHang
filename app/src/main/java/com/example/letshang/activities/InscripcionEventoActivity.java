package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.letshang.R;
import com.example.letshang.model.Event;
import com.example.letshang.providers.EventProvider;

import java.text.SimpleDateFormat;
import java.util.List;

public class InscripcionEventoActivity extends AppCompatActivity {

    private Button btnSiguiente;
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

        int numevento = getIntent().getIntExtra("numevent", 0);

        tvHostEvento = findViewById(R.id.tvHostInscripcionEvento);

        btnSiguiente = findViewById(R.id.btnSiguienteInscripcionEvento);

        tvLocationEvento = findViewById(R.id.tvLocationInscripcionEvento);

        tvFechaEvento = findViewById(R.id.tvFechaInscripcionEvento);

        tvTiempoEvento = findViewById(R.id.tvTiempoInscripcionEvento);

        tvPrecioEvento = findViewById(R.id.tvPrecioInscripcionEvento);

        tvDescripcionEvento = findViewById(R.id.tvResumenInscripcionEvento);


        /*List<Event> listEvent = evProv.getList();

        int cont = 0;
        for(Event e : listEvent){
            if(cont == numevento){
                getSupportActionBar().setTitle(e.getTitle());
                tvDescripcionEvento.setText(e.getDescription());
                tvPrecioEvento.setText( Long.toString(e.getPrice()));
                tvLocationEvento.setText("e.getLocation()");
                tvHostEvento.setText("e.getUser()");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String fecha =  formatter.format(e.getStartDate())  + " - " + formatter.format(e.getEndDate());
                SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
                String horario = formatter2.format(e.getStartDate()) + " - " + formatter2.format(e.getEndDate());
                tvTiempoEvento.setText(horario);
                tvFechaEvento.setText(fecha);
            }
            cont++;
        }*/

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReglasCondicionesActivity.class);
                startActivity(intent);
            }
        });
    }

}