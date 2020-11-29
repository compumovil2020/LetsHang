package com.example.letshang.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.letshang.R;
import com.example.letshang.model.Host;
import com.example.letshang.providers.EventProvider;

public class PerfilHostActivity extends AppCompatActivity {

    private TextView tvNombrePerfilHost;

    private EventProvider eventProvider = EventProvider.getInsatance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_host);

        tvNombrePerfilHost = findViewById(R.id.tvNombrePerfilHost);

        //Por el momento el host lo saca del evento, toca cambiarlo
        // para que saque de la bd un host en especifico.
        //TODO: descomentarear esto
        //Host host = eventProvider.getEventHost(1);

        Host host = null;


        tvNombrePerfilHost.setText("Host: " + host.getName());

        //Hace falta cuadrar ranking, redes, cantidad eventos realizados

        //Realizar nueva forma de tags

    }
}